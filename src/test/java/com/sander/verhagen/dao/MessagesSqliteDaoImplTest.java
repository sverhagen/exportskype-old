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

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.sander.verhagen.domain.Message;
/**
 * Tests for {@link MessagesSqliteDaoImpl}.
 * 
 * @author Sander Verhagen
 */
public class MessagesSqliteDaoImplTest
{

    private String[] messageBodies = {"hi", "how're you?"};

    private MessagesDao subject;

    private Connection connection;

    /**
     * Prepare the connection and the subject for the test.
     */
    @Before
    public void setUp()
    {
        connection = createMock(Connection.class);
        subject = new MessagesSqliteDaoImpl(connection);
    }

    /**
     * Test for {@link MessagesSqliteDaoImpl#getMessages()}.
     * 
     * @throws SQLException
     *         unexpected problem occurred during the test
     */
    @Test
    public void testGetMessages() throws SQLException
    {
        ResultSet resultSet = createResultSetMock();

        Statement statement = createMock(Statement.class);
        expect(connection.createStatement()).andReturn(statement);

        String sql = "SELECT * FROM Messages";
        expect(statement.executeQuery(sql)).andReturn(resultSet);

        replay(connection, resultSet, statement);
        List<Message> messages = subject.getMessages();
        verify(connection, resultSet, statement);
        verifyResultSet(messages);
    }

    private void verifyResultSet(List<Message> messages)
    {
        assertEquals(messageBodies.length, messages.size());
        for (String messageBody : messageBodies)
        {
            boolean found = false;
            for (Message message : messages)
            {
                found = found || message.getBody().equals(messageBody);
            }
            assertTrue(found);
        }
    }

    private ResultSet createResultSetMock() throws SQLException
    {
        ResultSet resultSet = createMock(ResultSet.class);
        for (String messageBody : messageBodies)
        {
            expect(resultSet.next()).andReturn(true);
            expect(resultSet.getString("chatname")).andReturn("chatname");
            expect(resultSet.getString("body_xml")).andReturn(messageBody);
            expect(resultSet.getString("author")).andReturn("author");
            expect(resultSet.getString("from_dispname")).andReturn("Mr./Ms. Author");
            expect(resultSet.getLong("timestamp")).andReturn(123456L);
        }
        
        expect(resultSet.next()).andReturn(true);
        expect(resultSet.getString("chatname")).andReturn("chatname");
        expect(resultSet.getString("body_xml")).andReturn("");
        expect(resultSet.getString("author")).andReturn("author");
        expect(resultSet.getString("from_dispname")).andReturn("Mr./Ms. Author");
        expect(resultSet.getLong("timestamp")).andReturn(123456L);
        
        expect(resultSet.next()).andReturn(false);
        return resultSet;
    }
}
