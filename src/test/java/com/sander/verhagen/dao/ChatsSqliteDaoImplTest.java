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

import com.sander.verhagen.domain.Chat;

/**
 * Tests for {@link ChatsSqliteDaoImpl}.
 * 
 * @author Sander Verhagen
 */
public class ChatsSqliteDaoImplTest
{
    private String[] chatNames = {"chatName1", "chatName2"};

    private ChatsDao subject;

    private Connection connection;

    /**
     * Prepare the connection and the subject for the test.
     */
    @Before
    public void setUp()
    {
        connection = createMock(Connection.class);
        subject = new ChatsSqliteDaoImpl(connection);
    }

    /**
     * Test for {@link ChatsSqliteDaoImpl#getChats()}.
     * 
     * @throws SQLException
     *         unexpected problem occurred during the test
     */
    @Test
    public void testGetChats() throws SQLException
    {
        ResultSet resultSet = createResultSetMock();

        Statement statement = createMock(Statement.class);
        expect(connection.createStatement()).andReturn(statement);

        String sql = "SELECT * FROM Chats ORDER BY timestamp";
        expect(statement.executeQuery(sql)).andReturn(resultSet);

        replay(connection, resultSet, statement);
        List<Chat> chats = subject.getChats();
        verify(connection, resultSet, statement);
        verifyResultSet(chats);
    }

    private void verifyResultSet(List<Chat> chats)
    {
        assertEquals(chatNames.length, chats.size());
        for (String chatName : chatNames)
        {
            boolean found = false;
            for (Chat chat : chats)
            {
                found = found || chat.getName().equals(chatName);
            }
            assertTrue(found);
        }
    }

    private ResultSet createResultSetMock() throws SQLException
    {
        ResultSet resultSet = createMock(ResultSet.class);
        for (String chatName : chatNames)
        {
            expect(resultSet.next()).andReturn(true);
            expect(resultSet.getString("name")).andReturn(chatName);
            expect(resultSet.getLong("timestamp")).andReturn(123456L);
            expect(resultSet.getLong("last_change")).andReturn(123457L);
            expect(resultSet.getString("participants")).andReturn("john susie");
        }
        
        expect(resultSet.next()).andReturn(true);
        expect(resultSet.getString("name")).andReturn("partnerless chat");
        expect(resultSet.getLong("timestamp")).andReturn(123456L);
        expect(resultSet.getLong("last_change")).andReturn(123457L);
        expect(resultSet.getString("participants")).andReturn("");
        expect(resultSet.getString("dialog_partner")).andReturn("");
        expect(resultSet.getString("posters")).andReturn("");
        
        expect(resultSet.next()).andReturn(false);
        return resultSet;
    }

}
