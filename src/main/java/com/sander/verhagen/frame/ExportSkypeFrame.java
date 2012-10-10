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

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import com.sander.verhagen.ExportSkype;

/**
 * Frame executable for exporting Skype chat history.
 * 
 * @author Sander Verhagen
 */
@SuppressWarnings("serial")
public class ExportSkypeFrame extends JFrame
{
    ExportSkypeFrame()
    {
        initializeWindow();
        add(new JLoggingTable());
        addWindowListener(new CloseWindowAdapter());
    }

    private void initializeWindow()
    {
        setTitle("Export Skype");
        setMinimumSize(new Dimension(480, 200));
        setSize(700, 500);
        setLocationRelativeTo(null);
        setVisible(true);
        requestFocus();
    }

    private final class CloseWindowAdapter extends WindowAdapter
    {
        @Override
        public void windowClosing(WindowEvent e)
        {
            dispose();
        }
    }

    /**
     * Frame executable for exporting Skype chat history.
     * 
     * @param args
     *        arguments; none required
     */
    public static void main(String args[])
    {
        new ExportSkypeFrame();
        new ExportSkype().execute();
    }
}