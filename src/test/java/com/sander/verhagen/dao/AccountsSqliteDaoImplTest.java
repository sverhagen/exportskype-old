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

/**
 * Tests for {@link AccountsSqliteDaoImpl}.
 * 
 * @author Sander Verhagen
 */
public class AccountsSqliteDaoImplTest
{
    private String[] testAccounts = {"johnny", "susan"};

    private AccountsDao subject;

    private Connection connection;

    /**
     * Prepare the connection and the subject for the test.
     */
    @Before
    public void setUp()
    {
        connection = createMock(Connection.class);
        subject = new AccountsSqliteDaoImpl(connection);
    }

    /**
     * Test for {@link AccountsSqliteDaoImpl#getSkypeNames()}.
     * 
     * @throws SQLException
     *         unexpected problem occurred during the test
     */
    @Test
    public void testGetSkypeNames() throws SQLException
    {
        ResultSet resultSet = createResultSetMock();

        Statement statement = createMock(Statement.class);
        expect(connection.createStatement()).andReturn(statement);

        String sql = "SELECT skypename FROM Accounts";
        expect(statement.executeQuery(sql)).andReturn(resultSet);

        replay(connection, resultSet, statement);
        List<String> skypeNames = subject.getSkypeNames();
        verify(connection, resultSet, statement);

        verifyResultSet(skypeNames);
    }

    private void verifyResultSet(List<String> skypeNames)
    {
        assertEquals(testAccounts.length, skypeNames.size());
        for (String testAccount : testAccounts)
        {
            assertTrue(skypeNames.contains(testAccount));
        }
    }

    private ResultSet createResultSetMock() throws SQLException
    {
        ResultSet resultSet = createMock(ResultSet.class);
        for (String testAccount : testAccounts)
        {
            expect(resultSet.next()).andReturn(true);
            expect(resultSet.getString("skypename")).andReturn(testAccount);
        }
        expect(resultSet.next()).andReturn(false);
        return resultSet;
    }
}
