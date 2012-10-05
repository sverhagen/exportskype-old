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

package com.sander.verhagen.domain;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sander.verhagen.trillian.Session;

/**
 * Chat entity as pulled from the Skype database.
 * 
 * @author Sander Verhagen
 */
public class Chat
{
    private List<Message> messages = new ArrayList<Message>();

    private static String homeUser = "";

    /**
     * Set the user for which we are exporting Skype logs.
     * 
     * @param homeUser
     *        user for which exporting
     */
    public static void setHomeUser(String homeUser)
    {
        Chat.homeUser = homeUser;
    }

    private String name;

    private long start;

    private long finish;

    private List<String> partners = new ArrayList<String>();

    /**
     * Constructor. Will also try to determine the chat partner(s) from various columns in the given
     * record
     * 
     * @param resultSet
     *        record as pulled from the Skype database
     * @throws SQLException
     *         problem with database access
     */
    public Chat(ResultSet resultSet) throws SQLException
    {
        this.name = resultSet.getString("name");
        this.start = resultSet.getLong("timestamp");
        this.finish = resultSet.getLong("last_change");
        String participants = resultSet.getString("participants");
        addPartners(participants);

        if (partners.size() == 0)
        {
            String dialogPartner = resultSet.getString("dialog_partner");
            addPartners(dialogPartner);
        }

        if (partners.size() == 0)
        {
            String posters = resultSet.getString("posters");
            addPartners(posters);
        }
    }

    private void addPartners(String possiblePartners)
    {
        if (possiblePartners != null)
        {
            for (String possiblePartner : possiblePartners.split(" "))
            {
                if (!possiblePartner.equals(homeUser) && !partners.contains(possiblePartner))
                {
                    partners.add(possiblePartner);
                }
            }
        }
    }

    /**
     * Whether this chat is a group chat, i.e. more than one partner, i.e. more than two
     * participants.
     * 
     * @return <code>true</code> if a group chat; <code>false</code> if not a group chat
     */
    public boolean isGroupChat()
    {
        return partners.size() > 1;
    }

    /**
     * Name of the chat, typically something like
     * <code>#sander.verhagen/$some.user;8efaa70613fe6cb9</code>
     * 
     * @return chat name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Get start time of chat, Unix epoch.
     * 
     * @return start time
     */
    public long getStart()
    {
        return start;
    }

    /**
     * Get end time of chat, Unix epoch.
     * 
     * @return end time
     */
    public long getFinish()
    {
        return finish;
    }

    /**
     * Whether a message of the given author is considered incoming for this chat.
     * 
     * @param author
     *        author to test whether is considered incoming
     * @return <code>true</code> for incoming; <code>false</code> for outgoing
     */
    public boolean isIncoming(String author)
    {
        return !homeUser.equals(author);
    }

    /**
     * Get the user for which we are exporting Skype logs.
     * 
     * @return user for which exporting
     */
    public String getHomeUser()
    {
        return homeUser;
    }

    /**
     * Get the contacts that were partners in this chat.
     * 
     * @return contacts that were partners in chat
     */
    public List<String> getPartners()
    {
        return partners;
    }

    /**
     * Get the contact to which outgoing messages will go. For now using the first partner, not sure
     * if that's always desirable in multi-partner chats; really depends on where this is used for,
     * see e.g. {@link Session#toXML}
     * 
     * @return contact to which outgoing
     */
    public String getTo()
    {
        return partners.get(0);
    }

    /**
     * Get the contact to which incoming messages will go; see {@link #getHomeUser}.
     * 
     * @return contact to which incoming
     */
    public String getFrom()
    {
        return homeUser;
    }

    /**
     * Whether this chat has any partners.
     * 
     * @return <code>true</code> if chat has partners; <code>false</code> if it doesn't
     */
    public boolean hasPartners()
    {
        return !partners.isEmpty();
    }

    /**
     * Add a message to this chat.
     * 
     * @param message
     *        message to add to chat
     */
    public void addMessage(Message message)
    {
        addPartners(message.getAuthor());
        messages.add(message);
    }

    /**
     * Get all messages that were added to this chat.
     * 
     * @return messages of chat
     */
    public List<Message> getMessages()
    {
        return messages;
    }
}
