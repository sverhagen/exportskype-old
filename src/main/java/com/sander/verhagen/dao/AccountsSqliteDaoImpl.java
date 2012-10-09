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

/**
 * Implementation of DAO for access to accounts in the Skype database (&quot;Accounts&quot; table).
 * 
 * @author Sander Verhagen
 */
public class AccountsSqliteDaoImpl implements AccountsDao
{
    private Connection connection;

    /**
     * Constructor.
     * 
     * @param connection
     *        (opened) database connection
     */
    public AccountsSqliteDaoImpl(Connection connection)
    {
        this.connection = connection;
    }

    public List<String> getSkypeNames() throws SQLException
    {
        List<String> skypeNames = new ArrayList<String>();
        Statement statement = connection.createStatement();
        String sql = "SELECT skypename FROM Accounts";
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next())
        {
            skypeNames.add(resultSet.getString("skypename"));
        }
        return skypeNames;
    }

}
