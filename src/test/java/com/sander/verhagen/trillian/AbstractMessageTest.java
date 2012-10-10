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

import static org.junit.Assert.*;

import static org.easymock.EasyMock.*;

import org.junit.Before;
import org.junit.Test;

import com.sander.verhagen.domain.Chat;
import com.sander.verhagen.domain.Message;

/**
 * Tests for {@link AbstractMessage} and as thus serving as an abstract base class for tests for
 * children of {@link AbstractMessage}.
 * 
 * @author Sander Verhagen
 */
public abstract class AbstractMessageTest
{
    private static final long MESSAGE_TIME = 23456L;

    private static final String AUTHOR_DISPLAY = "Mr./Ms. Author";

    private static final String OTHER = "other.user";

    private static final String HOME_USER = "home.user";

    private static final String CHAT_NAME = "chatName";

    private static final String BODY = "hey, how're you?";

    private String expectedIncoming;

    private String expectedOutgoing;

    /**
     * Prepare some expectations. Would've been a constant, but need to call <code>getType()</code>
     */
    @Before
    public void setUp()
    {
        StringBuilder incoming = new StringBuilder();
        incoming.append("<message type=\"incoming_" + getType() + "\" ");
        incoming.append("time=\"23456\" ");
        incoming.append("medium=\"SKYPE\" ");
        incoming.append("to=\"home.user\" ");
        incoming.append("from=\"other.user\"" + " ");
        incoming.append("from_display=\"Mr.%2FMs.%20Author\" ");
        incoming.append("text=\"hey%2C%20how%27re%20you%3F\" />");
        expectedIncoming = incoming.toString();

        StringBuilder outgoing = new StringBuilder();
        outgoing.append("<message type=\"outgoing_" + getType() + "\" ");
        outgoing.append("time=\"23456\" ");
        outgoing.append("medium=\"SKYPE\" ");
        outgoing.append("to=\"other.user\" ");
        outgoing.append("from=\"home.user\" ");
        outgoing.append("from_display=\"Mr.%2FMs.%20Author\" ");
        outgoing.append("text=\"hey%2C%20how%27re%20you%3F\" />");
        expectedOutgoing = outgoing.toString();
    }

    /**
     * Test for {@link GroupMessage#toXML()}.
     */
    @Test
    public void testToXmlIncoming()
    {
        Message message = createMockMessage(true);
        Chat chat = createMockChat();

        replay(chat, message);
        String actual = getMessage(chat, message, OTHER).toXML();
        verify(chat, message);

        assertEquals(expectedIncoming, actual);
    }

    abstract protected AbstractMessage getMessage(Chat chat, Message message, String to);

    abstract protected String getType();

    /**
     * Test for {@link GroupMessage#toXML()}.
     */
    @Test
    public void testToXmlOutgoing()
    {
        Message message = createMockMessage(false);
        Chat chat = createMockChat();

        replay(chat, message);
        String actual = getMessage(chat, message, OTHER).toXML();
        verify(chat, message);

        assertEquals(expectedOutgoing, actual);
    }

    private Chat createMockChat()
    {
        Chat chat = createMock(Chat.class);
        expect(chat.isIncoming(OTHER)).andReturn(true).anyTimes();
        expect(chat.isIncoming(HOME_USER)).andReturn(false).anyTimes();
        expect(chat.getFinish()).andReturn(123456L).anyTimes();
        expect(chat.getFrom()).andReturn(OTHER).anyTimes();
        expect(chat.getHomeUser()).andReturn(HOME_USER).anyTimes();
        expect(chat.getName()).andReturn(CHAT_NAME).anyTimes();
        expect(chat.getStart()).andReturn(12345L).anyTimes();
        expect(chat.getTo()).andReturn(HOME_USER).anyTimes();
        return chat;
    }

    private Message createMockMessage(boolean incoming)
    {
        Message message = createMock(Message.class);
        expect(message.getAuthor()).andReturn(incoming ? OTHER : HOME_USER).anyTimes();
        expect(message.getAuthorDisplay()).andReturn(AUTHOR_DISPLAY).anyTimes();
        expect(message.getBody()).andReturn(BODY).anyTimes();
        expect(message.getTime()).andReturn(MESSAGE_TIME).anyTimes();
        return message;
    }

}
