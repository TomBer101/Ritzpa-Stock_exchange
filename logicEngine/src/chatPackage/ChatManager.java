package chatPackage;

import java.util.ArrayList;
import java.util.List;

public class ChatManager
{
    /******************************************************************************/
    private List<SingleChatEntry> chatDataList;
    /******************************************************************************/
    public ChatManager() { this.chatDataList = new ArrayList<>(); }
    /******************************************************************************/
    public synchronized void addChatString(String chatString, String userName)
    {
        chatDataList.add(new SingleChatEntry(chatString, userName));
    }
    /******************************************************************************/
    public synchronized List<SingleChatEntry> getChatDataList() { return chatDataList; }
    /******************************************************************************/
}
