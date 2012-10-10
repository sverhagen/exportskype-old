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

package com.sander.verhagen.frame;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.varia.NullAppender;

/**
 * Table that shows logging from the system. Adds itself as appender
 * 
 * @author Sander Verhagen
 */
@SuppressWarnings("serial")
public class JLoggingTable extends JTable
{
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("EEE, MMM d");

    private static final SimpleDateFormat TIME_FORMATTER = new SimpleDateFormat("HH:mm:ss");

    private AddRowAppender addRowAppender = new AddRowAppender();

    private class AddRowAppender extends NullAppender
    {
        @Override
        public void doAppend(LoggingEvent event)
        {
            Date date = new Date(event.timeStamp);
            String message = event.getMessage().toString();
            String level = event.getLevel().toString();
            addRow(date, level, message);
        }
    };

    private DefaultTableModel model = new DefaultTableModel()
    {
        @Override
        public java.lang.Class<?> getColumnClass(int columnIndex)
        {
            return String.class;
        };
    };

    /**
     * Constructor.
     */
    public JLoggingTable()
    {
        setModel(model);
        model.addColumn("Date");
        model.addColumn("Time");
        model.addColumn("Severity");
        model.addColumn("Description");
        setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        getColumnModel().getColumn(0).setMaxWidth(120);
        getColumnModel().getColumn(1).setMaxWidth(120);
        getColumnModel().getColumn(2).setMaxWidth(120);
        setDefaultRenderer(String.class, new LineWrapCellRenderer());
        setShowGrid(false);
        addRow(new Date(), "", "Initializing...");
        Logger.getRootLogger().addAppender(addRowAppender);
    }

    private void addRow(Date date, String level, String message)
    {
        String dateString = DATE_FORMATTER.format(date);
        String timeString = TIME_FORMATTER.format(date);
        model.addRow(new String[] {dateString, timeString, level, message});
    }
}
