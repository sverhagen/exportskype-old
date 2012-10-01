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

import java.sql.SQLException;
import java.util.List;

import com.sander.verhagen.domain.Message;

/**
 * Definition of DAO for access to messages in the Skype database (&quot;Messages&quot; table).
 * 
 * @author Sander Verhagen
 */
public interface MessagesDao
{
    /**
     * Get all messages from the Skype database.
     * 
     * @return all messages
     * @throws SQLException
     *         problem with database access
     */
    List<Message> getMessages() throws SQLException;

    /**
     * Get number of all messages.
     * 
     * @return message count
     * @throws SQLException
     *         problem with database access
     */
    long getMessageCount() throws SQLException;
}
