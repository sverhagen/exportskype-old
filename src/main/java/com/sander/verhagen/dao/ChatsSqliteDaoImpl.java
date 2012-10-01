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

package com.sander.verhagen.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sander.verhagen.domain.Chat;

/**
 * Implementation of DAO for access to chats in the Skype database (&quot;Chats&quot; table).
 * 
 * @author Sander Verhagen
 */
public class ChatsSqliteDaoImpl implements ChatsDao
{
    private static Log log = LogFactory.getLog(ChatsSqliteDaoImpl.class);

    private Connection connection;

    /**
     * Constructor.
     * 
     * @param connection
     *        (opened) database connection
     */
    public ChatsSqliteDaoImpl(Connection connection)
    {
        this.connection = connection;
    }

    public List<Chat> getChats() throws SQLException
    {
        List<Chat> chats = new ArrayList<Chat>();
        Statement statement = connection.createStatement();
        String sql = "SELECT * FROM Chats";
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next())
        {
            Chat chat = new Chat(resultSet);
            if (chat.hasPartners())
            {
                chats.add(chat);
            }
            else
            {
                log.info("Has no partners: chat " + chat.getName());
            }
        }
        return chats;
    }

}
