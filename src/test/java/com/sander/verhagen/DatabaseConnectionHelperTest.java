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

import static org.junit.Assert.*;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

import com.sander.verhagen.DatabaseConnectionHelper;

/**
 * Tests for {@link DatabaseConnectionHelper}.
 * 
 * @author Sander Verhagen
 */
public class DatabaseConnectionHelperTest
{

    /**
     * Test for <code>DatabaseConnectionHelper.determineDatabaseUrl</code>.
     */
    @Test
    public void testDetermineDatabaseUrl()
    {
        String fileName = "src\\test\\resources\\myHome\\Application Data\\Skype\\my.user\\main.db";
        File file = new File(fileName);
        System.setProperty("user.home", "src\\test\\resources\\myHome");
        String string = DatabaseConnectionHelper.determineDatabaseUrl();
        assertEquals("jdbc:sqlite:" + file.getAbsolutePath(), string);
    }

    /**
     * Test for <code>DatabaseConnectionHelper.determineDatabaseUrl</code> when no database exists.
     */
    @Test
    public void testDetermineDatabaseUrlNoDatabase()
    {
        System.setProperty("user.home", "src\\test\\resources\\myHomeNoDatabase");
        try
        {
            DatabaseConnectionHelper.determineDatabaseUrl();
        }
        catch (RuntimeException exception)
        {
            assertEquals("No database file found", exception.getMessage());
        }
    }

    /**
     * Test for <code>DatabaseConnectionHelper.determineDatabaseUrl</code> when multiple database
     * exists.
     */
    @Test
    public void testDetermineDatabaseUrlMultipleDatabases()
    {
        System.setProperty("user.home", "src\\test\\resources\\myHomeMultipleDatabases");
        try
        {
            DatabaseConnectionHelper.determineDatabaseUrl();
        }
        catch (RuntimeException exception)
        {
            assertEquals("Multiple database files found; don't know which one to choose",
                    exception.getMessage());
        }
    }

    /**
     * Test for {@link DatabaseConnectionHelper#open()} and {@link DatabaseConnectionHelper#close()}
     * .
     * 
     * @throws SQLException
     *         if a database access error occurs
     */
    @Test
    public void testConnection() throws SQLException
    {
        System.setProperty("user.home", "src\\test\\resources\\myHome");
        DatabaseConnectionHelper subject = new DatabaseConnectionHelper();
        Connection connection = subject.open();
        assertFalse(connection.isClosed());
        subject.close();
        assertTrue(connection.isClosed());
    }

}
