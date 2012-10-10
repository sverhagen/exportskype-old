
package com.sander.verhagen.trillian;

import com.sander.verhagen.domain.Chat;
import com.sander.verhagen.domain.Message;

/**
 * Tests for {@link PrivateMessage}, as mainly implemented by {@link AbstractMessageTest}.
 * 
 * @author Sander Verhagen
 */
public class PrivateMessageTest extends AbstractMessageTest
{
    @Override
    protected AbstractMessage getMessage(Chat chat, Message message, String to)
    {
        return new PrivateMessage(chat, message, to);
    }

    @Override
    protected String getType()
    {
        return "privateMessage";
    }
}
