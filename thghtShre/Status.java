public class Status {
    public long totalWords;
    public long totalChars;
    public long totalMessages;
    public long numAllRcps;
    public long numSubRcps;
    public long numUserRcps;
    public long numSelfRcps;
    public long numResponse;
    public long sumWordsSquares;
    public long sumCharsSquares;

    public Status() {
        totalWords = 0;
        totalChars = 0;
        totalMessages = 0;
        numAllRcps = 0;
        numSubRcps = 0;
        numUserRcps = 0;
        numSelfRcps = 0;
        numResponse = 0;
        sumWordsSquares = 0;
        sumCharsSquares = 0;
    }
}
