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

package com.sander.verhagen.trillian;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sander.verhagen.domain.Chat;
import com.sander.verhagen.domain.Message;
import com.sander.verhagen.output.OutputHandler;

/**
 * Definition of handler to do something with output of Skype export, and write it to XML files in
 * Trillian XML log format.
 * 
 * @author Sander Verhagen
 */
public class TrillianOutputHandler implements OutputHandler
{
    /**
     * Write XML log files for each of the contacts and for all their XML entities.
     * 
     * @param contactXmls
     *        XML entities mapped onto contacts
     */
    private void writeFiles(Map<String, List<XML>> contactXmls)
    {
        File directory = new File("./output/SKYPE/Query");
        directory.mkdirs();
        for (String qualifiedContact : contactXmls.keySet())
        {
            FileWriter file = createFile(directory, qualifiedContact);
            BufferedWriter writer = new BufferedWriter(file);
            List<XML> contactXml = contactXmls.get(qualifiedContact);
            try
            {
                for (XML xml : contactXml)
                {
                    writer.write(xml.toXML());
                    writer.write("\r\n");
                }
                writer.close();
            }
            catch (IOException exception)
            {
                throw new RuntimeException("Problem writing to file for \"" + qualifiedContact
                        + "\"", exception);
            }
        }
    }

    private Map<String, List<XML>> getQualifiedXmlEntities(Map<String, List<Chat>> qualifiedChats)
    {
        Map<String, List<XML>> contactXmls = new HashMap<String, List<XML>>();
        for (String qualifiedContact : qualifiedChats.keySet())
        {
            List<Chat> chatsForContact = qualifiedChats.get(qualifiedContact);
            for (Chat chat : chatsForContact)
            {
                if (chat.getMessages().isEmpty())
                {
                    continue;
                }
                if (!contactXmls.containsKey(qualifiedContact))
                {
                    contactXmls.put(qualifiedContact, new ArrayList<XML>());
                }
                contactXmls.get(qualifiedContact).add(new SessionStart(chat));
                for (Message message : chat.getMessages())
                {
                    contactXmls.get(qualifiedContact).add(new PrivateMessage(chat, message));
                }
                contactXmls.get(qualifiedContact).add(new SessionStop(chat));
            }
        }

        return contactXmls;
    }

    public void output(Map<String, List<Chat>> qualifiedChats)
    {
        Map<String, List<XML>> qualifiedXmlEntities = getQualifiedXmlEntities(qualifiedChats);
        writeFiles(qualifiedXmlEntities);
    }

    /**
     * Create a file handle for the given contact in the given folder.
     * 
     * @param folder
     *        folder to create file in
     * @param contact
     *        contact to create file for
     * @return file handle
     */
    private FileWriter createFile(File folder, String contact)
    {
        String fileName = folder.getAbsolutePath() + "/" + contact + ".xml";
        try
        {
            return new FileWriter(fileName);
        }
        catch (IOException exception)
        {
            throw new RuntimeException("Problem opening file \"" + fileName + "\"", exception);
        }
    }
}
