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

package com.sander.verhagen.output;

import java.util.List;
import java.util.Map;

import com.sander.verhagen.domain.Chat;

/**
 * Definition of handler to do something with output of Skype export.
 * 
 * @author Sander Verhagen
 */
public interface OutputHandler
{

    /**
     * Handle output of Skype export for individual chats. (I.e. excluding group chats)
     * 
     * @param mappedIndividualChats
     *        chats mapped onto individual contacts
     */
    void outputIndividual(Map<String, List<Chat>> mappedIndividualChats);

    /**
     * Handle output of Skype export for group chats.
     * 
     * @param groupChats
     *        group chats
     */
    void outputGroups(List<Chat> groupChats);
}
