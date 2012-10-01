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

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Some database-related helper functions.
 * 
 * @author Sander Verhagen
 */
public class DatabaseConnectionHelper
{
    private static Log log = LogFactory.getLog(DatabaseConnectionHelper.class);

    private Connection connection;

    /**
     * Constructor.
     */
    public DatabaseConnectionHelper()
    {
        try
        {
            Class.forName("org.sqlite.JDBC");
        }
        catch (ClassNotFoundException exception)
        {
            throw new RuntimeException("Requires SQLite JDBC driver on classpath", exception);
        }
    }

    /**
     * Open the database connection.
     * 
     * @return database connection
     */
    public Connection open()
    {
        try
        {
            String url = determineDatabaseUrl();
            log.info("Determined database URL: " + url);
            connection = DriverManager.getConnection(url);
            return connection;
        }
        catch (SQLException exception)
        {
            throw new RuntimeException("Problem opening database connection", exception);
        }
    }

    /**
     * Close the database connection.
     */
    public void close()
    {
        try
        {
            connection.close();
        }
        catch (SQLException exception)
        {
            throw new RuntimeException("Problem closing database connection", exception);
        }
    }

    /**
     * Determine the database URL. The trick here is to determine it without asking the user to tell
     * us their Skype name. Note that this is currently working for Windows, it should be not too
     * difficult to make it work for other platforms, if anyone desires this. Note that this is
     * currently only supporting a single account for each system user, it should not be too
     * difficult to make it work for multiple users<br/>
     * <br/>
     * The file location of the database should be something like:
     * <code>C:\Users\&lt;Windows user&gt;\Application Data\Skype\&lt;Skype user&gt;\main.db</code>
     * 
     * @return database URL as determined
     */
    private String determineDatabaseUrl()
    {
        String userHome = System.getProperty("user.home");

        File homeFolder = new File(userHome + "\\Application Data\\Skype");
        String[] extensions = {"db"};
        Collection<File> files = FileUtils.listFiles(homeFolder, extensions, true);
        List<File> mainFiles = new ArrayList<File>();
        for (File file : files)
        {
            if (file.getName().equals("main.db"))
            {
                mainFiles.add(file);
            }
        }

        switch (mainFiles.size())
        {
            case 0:
                throw new RuntimeException("No database file found");
            case 1:
                return "jdbc:sqlite:" + mainFiles.get(0).getAbsolutePath();
            default:
                throw new RuntimeException("Multiple database files found; "
                        + "don't know which one to choose");
        }
    }
}
