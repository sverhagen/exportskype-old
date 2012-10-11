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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.text.StyleConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sander.verhagen.ExportSkype;

/**
 * Frame executable for exporting Skype chat history.
 * 
 * @author Sander Verhagen
 */
@SuppressWarnings("serial")
public class ExportSkypeFrame extends JFrame
{
    private static Logger log = LoggerFactory.getLogger(ExportSkypeFrame.class);

    ExportSkypeFrame()
    {
        add(new JLoggingTable());
        initializeStaticText();
        initializeWindow();
        addWindowListener(new CloseWindowAdapter());
        setIcon();
    }

    private void initializeStaticText()
    {
        try
        {
            JHyperlinkTextPane textPane = new JHyperlinkTextPane();
            textPane.addText("Licensed under the ");
            textPane.addHyperlinkText("Apache License",
                    "http://www.apache.org/licenses/LICENSE-2.0");
            textPane.addText(", free and \"as-is\". More information on ");
            textPane.addHyperlinkText("GitHub", "http://sverhagen.github.com/Export-Skype/");
            textPane.setAlignment(StyleConstants.ALIGN_CENTER);
            add(BorderLayout.SOUTH, textPane);
        }
        catch (URISyntaxException exception)
        {
            log.error("Cannot initialize static hyperlink text", exception);
        }
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

    private void setIcon()
    {
        String name = "icon.png";
        InputStream stream = getClass().getResourceAsStream(name);
        try
        {
            Image image = ImageIO.read(stream);
            setIconImage(image);
        }
        catch (IllegalArgumentException exception)
        {
            log.error("Could not find icon " + name, exception);
        }
        catch (IOException exception)
        {
            log.error("Problem reading icon " + name, exception);
        }
    }

    private final class CloseWindowAdapter extends WindowAdapter
    {
        @Override
        public void windowClosing(WindowEvent e)
        {
            dispose();
            System.exit(1);
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