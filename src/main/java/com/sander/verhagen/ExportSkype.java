/**
 * Copyright 2012 Sander Verhagen
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.sander.verhagen;

import static org.apache.commons.collections.CollectionUtils.select;
import static org.apache.commons.collections.CollectionUtils.selectRejected;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.Predicate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sander.verhagen.dao.AccountsDao;
import com.sander.verhagen.dao.AccountsSqliteDaoImpl;
import com.sander.verhagen.dao.ChatsDao;
import com.sander.verhagen.dao.ChatsSqliteDaoImpl;
import com.sander.verhagen.dao.MessagesDao;
import com.sander.verhagen.dao.MessagesSqliteDaoImpl;
import com.sander.verhagen.domain.Chat;
import com.sander.verhagen.domain.Message;
import com.sander.verhagen.output.OutputHandler;
import com.sander.verhagen.trillian.TrillianOutputHandler;

/**
 * Main class and executable for exporting Skype chat history.
 * 
 * @author Sander Verhagen
 */
public class ExportSkype
{
    private static Log log = LogFactory.getLog(ExportSkype.class);

    private AccountsDao accountsDao;

    private ChatsDao chatsDao;

    private MessagesDao messagesDao;

    /**
     * Main executable for exporting Skype chat history
     * 
     * @param args
     *        arguments; none required
     */
    public static void main(String[] args)
    {
        new ExportSkype().execute();
    }

    /**
     * Predicate used to split out collections with group chats and individual chats.
     */
    private final class GroupChatPredicate implements Predicate
    {
        public boolean evaluate(Object subject)
        {
            return ((Chat) subject).isGroupChat();
        }
    }

    @SuppressWarnings("unchecked")
    private void execute()
    {
        DatabaseConnectionHelper connectionHelper = new DatabaseConnectionHelper();
        Connection connection = connectionHelper.open();
        accountsDao = new AccountsSqliteDaoImpl(connection);
        chatsDao = new ChatsSqliteDaoImpl(connection);
        messagesDao = new MessagesSqliteDaoImpl(connection);
        try
        {
            String skypeName = getSkypeName();
            Chat.setHomeUser(skypeName);

            Map<String, Chat> chats = populateChats();
            populateMessages(chats);
            Predicate predicate = new GroupChatPredicate();
            List<Chat> groupChats = (List<Chat>) select(chats.values(), predicate);
            List<Chat> individualChats = (List<Chat>) selectRejected(chats.values(), predicate);

            // Enable following line to copy group chats into individual chat logs
            // individualChats = new ArrayList<Chat>(chats.values());

            Map<String, List<Chat>> mappedIndividualChats =
                    populateMappedIndividualChats(individualChats);

            OutputHandler outputHandler = new TrillianOutputHandler();
            outputHandler.outputIndividual(mappedIndividualChats);
            outputHandler.outputGroups(groupChats);

            log.info("All done");
        }
        catch (SQLException exception)
        {
            throw new RuntimeException("Problem with database access", exception);
        }
        finally
        {
            connectionHelper.close();
        }
    }

    private String getSkypeName() throws SQLException
    {
        List<String> skypeNames = accountsDao.getSkypeNames();
        if (skypeNames.size() == 0)
        {
            throw new RuntimeException("No Skype accounts found in database");
        }
        String skypeName = skypeNames.get(0);
        if (skypeNames.size() > 1)
        {
            log.info("Found more than one Skype account; only using first one (" + skypeName + ")");
        }
        return skypeName;
    }

    /**
     * Convert a list of chats in a structure where chats are mapped onto contacts/partners.
     * 
     * @param chats
     *        chats with messages
     * @return chats mapped onto contacts/partners
     */
    private Map<String, List<Chat>> populateMappedIndividualChats(List<Chat> chats)
    {
        Map<String, List<Chat>> mappedIndividualChats = new HashMap<String, List<Chat>>();
        for (Chat chat : chats)
        {
            List<String> partners = chat.getPartners();
            for (String partner : partners)
            {
                if (!mappedIndividualChats.containsKey(partner))
                {
                    mappedIndividualChats.put(partner, new ArrayList<Chat>());
                }
                mappedIndividualChats.get(partner).add(chat);
            }
        }
        return mappedIndividualChats;
    }

    /**
     * Get bare chats.
     * 
     * @return chats chats mapped onto chat names
     * @throws SQLException
     *         problem with database access
     */
    private Map<String, Chat> populateChats() throws SQLException
    {
        Map<String, Chat> chats = new HashMap<String, Chat>();
        for (Chat chat : chatsDao.getChats())
        {
            chats.put(chat.getName(), chat);
            if (chat.getStart() > chat.getFinish())
            {
                log.warn("Chat \"" + chat.getName()
                        + "\" seems to finish before being started; start: " + chat.getStart()
                        + " finish: " + chat.getFinish());
            }

        }
        return chats;
    }

    /**
     * Add messages to chats.
     * 
     * @param chats
     *        bare chats mapped onto chat names
     * @throws SQLException
     *         problem with database access
     */
    private void populateMessages(Map<String, Chat> chats) throws SQLException
    {
        for (Message message : messagesDao.getMessages())
        {
            String chatName = message.getChatName();
            Chat chat = chats.get(chatName);
            if (chat != null)
            {
                chat.addMessage(message);
            }
        }
    }
}
