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

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.sander.verhagen.domain.Chat;

/**
 * Tests for {@link AbstractSession} and as thus serving as an abstract base class for tests for
 * children of {@link AbstractSession}.
 * 
 * @author Sander Verhagen
 */
public abstract class AbstractSessionTest
{

    private static final long START = 12345L;

    private static final long FINISH = 123456L;

    private static final String OTHER = "other.user";

    private static final String HOME_USER = "home.user";

    private String expected;

    /**
     * Prepare some expectations. Would've been a constant, but need to call <code>getType()</code>
     */
    @Before
    public void setUp()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("<session type=\"" + getType() + "\" ");
        builder.append("time=\"" + getExpectedTime(START, FINISH) + "\" ");
        builder.append("medium=\"SKYPE\" ");
        builder.append("to=\"" + OTHER + "\" ");
        builder.append("from=\"" + HOME_USER + "\" />");
        expected = builder.toString();
    }

    /**
     * Test for {@link GroupMessage#toXML()}.
     */
    @Test
    public void testToXmlIncoming()
    {
        Chat chat = createMockChat();

        replay(chat);
        String actual = getSession(chat, OTHER).toXML();
        verify(chat);

        assertEquals(expected, actual);
    }

    abstract protected AbstractSession getSession(Chat chat, String to);

    abstract protected String getType();

    abstract long getExpectedTime(long start, long finish);

    private Chat createMockChat()
    {
        Chat chat = createMock(Chat.class);
        expect(chat.getFinish()).andReturn(FINISH).anyTimes();
        expect(chat.getFrom()).andReturn(HOME_USER).anyTimes();
        expect(chat.getStart()).andReturn(START).anyTimes();
        expect(chat.getTo()).andReturn(OTHER).anyTimes();
        return chat;
    }
}
