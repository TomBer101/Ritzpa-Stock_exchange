package chatPackage;

public class SingleChatEntry
{
    /******************************************************************************/
    private final String chatString;
    private final String userName;
    private final long time;
    /******************************************************************************/
    public SingleChatEntry(String chatString, String userName)
    {
        this.chatString = chatString;
        this.userName = userName;
        this.time = System.currentTimeMillis();
    }
    /******************************************************************************/
    public String getChatString() { return chatString; }
    public String getUserName() { return userName; }
    public long getTime() { return time; }
    /******************************************************************************/
    @Override
    public String toString() {
        return (userName != null ? userName + ": " : "") + chatString;
    }
    /******************************************************************************/
}
