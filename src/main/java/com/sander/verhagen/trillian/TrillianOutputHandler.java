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
    private static final File PARENT_FOLDER = new File("./output/SKYPE");

    /**
     * Get XML entities grouped by categories, where the categories are the chat names. Given are
     * group chats, and we handle those as separate logs, hence referencing by chat names
     * 
     * @param groupChats
     *        group chats to get XML entities for
     * @return XML entities grouped by categories, where categories are chat names
     */
    private Map<String, List<XML>> getCategorizedXmlEntities(List<Chat> groupChats)
    {
        Map<String, List<XML>> categorizedXmlEntries = new HashMap<String, List<XML>>();
        for (Chat chat : groupChats)
        {
            String name = chat.getName();
            name = getValidFileName(name);
            getCategorizedXmlEntitiesForSingleChat(categorizedXmlEntries, name, chat, true);
        }
        return categorizedXmlEntries;
    }

    static String getValidFileName(String name)
    {
        return name.replaceAll("/\\$(.*;)*", "\\$");
    }

    /**
     * Get XML entities grouped by categories, where the categories are the name of the contact that
     * the home user chatted with. Given are chats for individual contacts, for which we want logs
     * per contact, hence referencing by contact name
     * 
     * @param chats
     *        chats to get XML entities for
     * @return XML entities grouped by categories, where categories are contact names
     */
    private Map<String, List<XML>> getCategorizedXmlEntities(Map<String, List<Chat>> chats)
    {
        Map<String, List<XML>> categorizedXmlEntries = new HashMap<String, List<XML>>();
        for (String contact : chats.keySet())
        {
            List<Chat> chatsForContact = chats.get(contact);
            for (Chat chat : chatsForContact)
            {
                getCategorizedXmlEntitiesForSingleChat(categorizedXmlEntries, contact, chat, false);
            }
        }
        return categorizedXmlEntries;
    }

    /**
     * Get XML entities of the given chat, all grouped by the given category.
     * 
     * @param categorizedXmlEntities
     *        XML entities grouped by categories, where XML entities are to be added to
     * @param category
     *        category to group XML entities by (may be contact name, group chat name)
     * @param chat
     *        chat to get XML entities for
     */
    private void getCategorizedXmlEntitiesForSingleChat(
            Map<String, List<XML>> categorizedXmlEntities, String category, Chat chat, boolean group)
    {
        String to = group ? chat.getTo() : category;
        if (chat.getMessages().isEmpty())
        {
            return;
        }
        if (!categorizedXmlEntities.containsKey(category))
        {
            categorizedXmlEntities.put(category, new ArrayList<XML>());
        }
        categorizedXmlEntities.get(category).add(new SessionStart(chat, to));
        for (Message message : chat.getMessages())
        {
            categorizedXmlEntities.get(category).add(new PrivateMessage(chat, message, to));
        }
        categorizedXmlEntities.get(category).add(new SessionStop(chat, to));
    }

    /**
     * {@inheritDoc}
     */
    public void outputIndividual(Map<String, List<Chat>> mappedChats)
    {
        Map<String, List<XML>> categorizedXmlEntities = getCategorizedXmlEntities(mappedChats);
        File folder = new File(PARENT_FOLDER, "Query");
        writeFiles(categorizedXmlEntities, folder);
    }

    /**
     * {@inheritDoc}
     */
    public void outputGroups(List<Chat> groupChats)
    {
        Map<String, List<XML>> categorizedXmlEntities = getCategorizedXmlEntities(groupChats);
        File folder = new File(PARENT_FOLDER, "Channel");
        writeFiles(categorizedXmlEntities, folder);
    }

    /**
     * Write XML log files for each of the categories and for all their XML entities.
     * 
     * @param categorizedXmlEntities
     *        XML entities mapped onto categories
     * @param folder
     *        folder for XML log files
     */
    private void writeFiles(Map<String, List<XML>> categorizedXmlEntities, File folder)
    {
        for (String category : categorizedXmlEntities.keySet())
        {
            String baseFileName = category;
            File file = new File(folder, baseFileName + ".xml");
            writeFile(categorizedXmlEntities.get(category), file);
        }
    }

    /**
     * Write the given XML entities to a XML log file with the given base file name.
     * 
     * @param xmlEntities
     *        XML entities to write
     * @param file
     *        file to write to
     */
    private void writeFile(List<XML> xmlEntities, File file)
    {
        FileWriter fileWriter = createFile(file);
        BufferedWriter writer = new BufferedWriter(fileWriter);
        try
        {
            for (XML xml : xmlEntities)
            {
                writer.write(xml.toXML());
                writer.write("\r\n");
            }
            writer.close();
        }
        catch (IOException exception)
        {
            throw new RuntimeException("Problem writing file for " + file, exception);
        }
    }

    /**
     * Create a file handle for the given base file name. Also creates folder, if needed
     * 
     * @param baseFileName
     *        base file name (thus without extension)
     * @return file handle
     */
    private FileWriter createFile(File file)
    {
        file.getParentFile().mkdirs();
        try
        {
            return new FileWriter(file);
        }
        catch (IOException exception)
        {
            throw new RuntimeException("Problem opening file " + file, exception);
        }
    }

}
