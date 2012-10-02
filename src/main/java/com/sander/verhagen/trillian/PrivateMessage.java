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
 * Private message entity of in the Trillian XML log format.
 * 
 * @author Sander Verhagen
 */
public class PrivateMessage implements XML
{

    private Chat chat;

    private Message message;

    /**
     * Constructor.
     * 
     * @param chat
     *        total chat ({@link Chat})
     * @param message
     *        single message in chat ({@link Message})
     */
    public PrivateMessage(Chat chat, Message message)
    {
        this.chat = chat;
        this.message = message;
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
        String to = incoming ? chat.getHomeUser() : author;
        String from = incoming ? author : chat.getHomeUser();
        String fromDisplay = message.getAuthorDisplay();
        String type = incoming ? "incoming_privateMessage" : "outgoing_privateMessage";

        StringBuilder result = new StringBuilder();
        result.append("<message ");
        result.append("type=\"" + type + "\" ");
        result.append("time=\"" + message.getTime() + "\" ");
        result.append("medium=\"SKYPE\" ");
        result.append("to=\"" + EscapeHelper.escape(to) + "\" ");
        result.append("from=\"" + EscapeHelper.escape(from) + "\" ");
        result.append("from_display=\"" + EscapeHelper.escape(fromDisplay) + "\" ");
        result.append("text=\"" + EscapeHelper.escape(message.getBody()) + "\" ");
        result.append("/>");
        return result.toString();
    }

}
