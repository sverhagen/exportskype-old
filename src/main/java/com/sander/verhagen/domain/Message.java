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

import org.apache.commons.lang3.StringUtils;

/**
 * Message entity as pulled from the Skype database.
 * 
 * @author Sander Verhagen
 */
public class Message
{

    private String body;

    private String author;

    private String authorDisplay;

    private long time;

    private String chatName;

    /**
     * Constructor.
     * 
     * @param resultSet
     *        record as pulled from the Skype database
     * @throws SQLException
     *         problem with database access
     */
    public Message(ResultSet resultSet) throws SQLException
    {
        this.chatName = resultSet.getString("chatname");
        this.body = resultSet.getString("body_xml");
        this.author = resultSet.getString("author");
        this.authorDisplay = resultSet.getString("from_dispname");
        this.time = resultSet.getLong("timestamp");
    }

    /**
     * Get the body of the message.
     * 
     * @return message body
     */
    public String getBody()
    {
        return body;
    }

    /**
     * Get the author of the message.
     * 
     * @return message author
     */
    public String getAuthor()
    {
        return author;
    }

    /**
     * Get the time of the message, Unix epoch.
     * 
     * @return message time
     */
    public long getTime()
    {
        return time;
    }

    /**
     * Get the display name of the author of the message.
     * 
     * @return message author display name
     */
    public String getAuthorDisplay()
    {
        return authorDisplay;
    }

    /**
     * Get the chat name that this message belongs to.
     * 
     * @return chat name
     */
    public String getChatName()
    {
        return chatName;
    }

    /**
     * Determine if the message is empty.
     * 
     * @return <code>true</code> if message empty; <code>false</code> if message contains something
     */
    public boolean isEmpty()
    {
        return StringUtils.isBlank(body);
    }
}
