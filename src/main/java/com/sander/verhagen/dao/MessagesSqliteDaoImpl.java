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

import com.sander.verhagen.domain.Message;

/**
 * Implementation of DAO for access to messages in the Skype database (&quot;Messages&quot; table).
 * 
 * @author Sander Verhagen
 */
public class MessagesSqliteDaoImpl implements MessagesDao
{
    private Connection connection;

    /**
     * Constructor.
     * 
     * @param connection
     *        (opened) database connection
     */
    public MessagesSqliteDaoImpl(Connection connection)
    {
        this.connection = connection;
    }

    public long getMessageCount() throws SQLException
    {
        Statement statement = connection.createStatement();
        String sql = "SELECT count(*) FROM Messages";
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next())
        {
            return resultSet.getInt("count(*)");
        }
        return 0;
    }

    public List<Message> getMessages() throws SQLException
    {
        List<Message> messages = new ArrayList<Message>();
        Statement statement = connection.createStatement();
        String sql = "SELECT * FROM Messages";
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next())
        {
            Message message = new Message(resultSet);
            if (!message.isEmpty())
            {
                messages.add(message);
            }
        }
        return messages;
    }
}
