
package com.sander.verhagen.trillian;

import com.sander.verhagen.domain.Chat;
import com.sander.verhagen.domain.Message;

/**
 * Tests for {@link GroupMessage}, as mainly implemented by {@link AbstractMessageTest}.
 * 
 * @author Sander Verhagen
 */
public class GroupMessageTest extends AbstractMessageTest
{
    @Override
    protected AbstractMessage getMessage(Chat chat, Message message, String to)
    {
        return new GroupMessage(chat, message, to);
    }

    @Override
    protected String getType()
    {
        return "groupMessage";
    }
}
