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

import com.sander.verhagen.domain.Chat;
import com.sander.verhagen.domain.Message;

/**
 * Abstract message entity of in the Trillian XML log format.
 * 
 * @author Sander Verhagen
 */
public abstract class AbstractMessage implements XML
{

    private Chat chat;

    private Message message;

    private String to;

    /**
     * Constructor.
     * 
     * @param chat
     *        total chat ({@link Chat})
     * @param message
     *        single message in chat ({@link Message})
     * @param to
     *        user name that is to be treated as communication partner
     */
    public AbstractMessage(Chat chat, Message message, String to)
    {
        this.chat = chat;
        this.message = message;
        this.to = to;
    }

    /**
     * Format (private) message entity XML. <br/>
     * <br/>
     * Looks like: <code>&lt;message type=&quot;outgoing_privateMessage&quot;
     * time=&quot;1348851843&quot; ms=&quot;0&quot; medium=&quot;SKYPE&quot;
     * to=&quot;sander%2Everhagen&quot; from=&quot;some%2Euser&quot;
     * from_display=&quot;Sander%20Verhagen&quot; text=&quot;Test!&quot;/&gt;</code>
     * 
     * @return entity XML
     */
    public String toXML()
    {
        String author = message.getAuthor();
        boolean incoming = chat.isIncoming(author);
        String to = incoming ? chat.getHomeUser() : this.to;
        String from = incoming ? author : chat.getHomeUser();
        String fromDisplay = message.getAuthorDisplay();
        String direction = incoming ? "incoming" : "outgoing";
        String type = getMessageType();

        StringBuilder result = new StringBuilder();
        result.append("<message ");
        result.append("type=\"" + direction + "_" + type + "\" ");
        result.append("time=\"" + message.getTime() + "\" ");
        result.append("medium=\"SKYPE\" ");
        result.append("to=\"" + EscapeHelper.escape(to) + "\" ");
        result.append("from=\"" + EscapeHelper.escape(from) + "\" ");
        result.append("from_display=\"" + EscapeHelper.escape(fromDisplay) + "\" ");
        result.append("text=\"");
        // TODO: experiment gone wrong; when copying group chats into individual chat logs, Trillian
        // may not well observe the author, hence we added our own tag
        // if (incoming && !this.to.equals(author))
        // {
        // result.append(EscapeHelper.escape("[sent by " + fromDisplay + "] "));
        // }
        result.append(EscapeHelper.escape(message.getBody()) + "\" ");
        result.append("/>");
        return result.toString();
    }

    abstract protected String getMessageType();
}
