import org.json.*;
import java.util.*;
import java.io.*;

public class thghtShreStats {
    
    private static JSONObject statsObject;

    //TR3
    private static long totalMessages;
    private static int totalUniqueUsers;
    private static HashMap<String, Integer> users;
    private static long totalLengthOfWords;
    private static long totalLengthOfChars;
    private static long[] numMsgPerLength = new long[21];
    private static long sumWordsSquares;
    private static long sumCharsSquares;

    // 0 = public, 1 = protected, 2 = private
    private static Status[] statuses = new Status[3];
    // 0 = all, 1 = subscriber, 2 = another user, 3 = self
    private static Recepient[] recepients = new Recepient[4];
    // 0 = in-response, 1 = non-response
    private static Response[] responses = new Response[2];

    private static void initStatistics() {
        int i;

        statsObject  = new JSONObject();
        totalMessages = 0;
        totalUniqueUsers = 0;
        totalLengthOfWords = 0;
        totalLengthOfChars = 0;
        users = new HashMap<String, Integer>(10000);
        sumWordsSquares = 0;
        sumCharsSquares = 0;

        for (i = 0; i < numMsgPerLength.length; i++)
            numMsgPerLength[i] = 0;

        for (i = 0; i < statuses.length; i++)
            statuses[i] = new Status();

        for (i = 0; i < recepients.length; i++)
            recepients[i] = new Recepient();

        for (i = 0; i < responses.length; i++)
            responses[i] = new Response();
    }

    private static double getStdDev(long sumSquares, long totalCount, long sum) {
        double mean = sum / (double)totalCount;
        double variance = sumSquares / (double)totalCount - mean * mean;
        return Math.sqrt(variance);
    }

    private static void parseJSONObj(JSONObject object) {
        try {
            String text = object.getString("text");
            int numWords = text.split("\\s+").length;
            int numChars = text.length();
            String status = object.getString("status");
            String recepient = object.getString("recepient");
            int statusIndex, recepientIndex, responseIndex = 1;
            String userID = object.getString("user");

            totalMessages++;
            if (!users.containsKey(userID)) {
                totalUniqueUsers++;
                users.put(userID, 0);
            }
            totalLengthOfWords += numWords;
            totalLengthOfChars += numChars;
            numMsgPerLength[numWords]++;
            sumWordsSquares += numWords * numWords;
            sumCharsSquares += numChars * numChars;

            if (status.equals("public"))
                statusIndex = 0;
            else if (status.equals("protected"))
                statusIndex = 1;
            else
                statusIndex = 2;

            statuses[statusIndex].totalWords += numWords;
            statuses[statusIndex].sumWordsSquares += numWords * numWords;
            statuses[statusIndex].totalChars += numChars;
            statuses[statusIndex].sumCharsSquares += numChars * numChars;
            statuses[statusIndex].totalMessages++;

            if (recepient.equals("all")) {
                recepientIndex = 0;
                statuses[statusIndex].numAllRcps++;
            } else if (recepient.equals("subscribers")) {
                recepientIndex = 1;
                statuses[statusIndex].numSubRcps++;
            } else if (recepient.equals("self")) {
                recepientIndex = 3;
                statuses[statusIndex].numSelfRcps++;
            } else {
                recepientIndex = 2;
                statuses[statusIndex].numUserRcps++;
            }

            recepients[recepientIndex].totalWords += numWords;
            recepients[recepientIndex].sumWordsSquares += numWords * numWords;
            recepients[recepientIndex].totalChars += numChars;
            recepients[recepientIndex].sumCharsSquares += numChars * numChars;
            recepients[recepientIndex].totalMessages++;

            if (object.has("in-response")) {
                responseIndex = 0;
                statuses[statusIndex].numResponse++;
                recepients[recepientIndex].numResponse++;
            }
            responses[responseIndex].totalWords += numWords;
            responses[responseIndex].sumWordsSquares += numWords * numWords;
            responses[responseIndex].totalChars += numChars;
            responses[responseIndex].sumCharsSquares += numChars * numChars;
            responses[responseIndex].totalMessages++;
        } catch (Exception e) {
            System.out.println("JSON format is not correct. JSON object was not a ThghtShre log record.");
            System.exit(1);
        }
    }

    private static void printStatsForSubsets() {
        System.out.println("----- Stats for subsets of Messages -----\n");
        System.out.println("(By Status)");
        System.out.println("\nPublic Messages");
        System.out.println("----------");
        System.out.printf("Words: %.2f\n", (statuses[0].totalWords / (double)statuses[0].totalMessages));
        System.out.printf("Characters: %.2f\n", (statuses[0].totalChars / (double)statuses[0].totalMessages));
        System.out.printf("Standard deviation of words: %.2f\n", getStdDev(statuses[0].sumWordsSquares, statuses[0].totalMessages, statuses[0].totalWords));
        System.out.printf("Standard deviation of characters: %.2f\n", getStdDev(statuses[0].sumCharsSquares, statuses[0].totalMessages, statuses[0].totalChars));

        System.out.println("\nProtected Messages");
        System.out.println("----------");
        System.out.printf("Words: %.2f\n", (statuses[1].totalWords / (double)statuses[1].totalMessages));
        System.out.printf("Characters: %.2f\n", (statuses[1].totalChars / (double)statuses[1].totalMessages));
        System.out.printf("Standard deviation of words: %.2f\n", getStdDev(statuses[1].sumWordsSquares, statuses[1].totalMessages, statuses[1].totalWords));
        System.out.printf("Standard deviation of characters: %.2f\n", getStdDev(statuses[1].sumCharsSquares, statuses[1].totalMessages, statuses[1].totalChars));

        System.out.println("\nPrivate Messages");
        System.out.println("----------");
        System.out.printf("Words: %.2f\n", (statuses[2].totalWords / (double)statuses[2].totalMessages));
        System.out.printf("Characters: %.2f\n", (statuses[2].totalChars / (double)statuses[2].totalMessages));
        System.out.printf("Standard deviation of words: %.2f\n", getStdDev(statuses[2].sumWordsSquares, statuses[2].totalMessages, statuses[2].totalWords));
        System.out.printf("Standard deviation of characters: %.2f\n", getStdDev(statuses[2].sumCharsSquares, statuses[2].totalMessages, statuses[2].totalChars));

        System.out.println("\n(By Recepient)");

        System.out.println("\nAll Messages");
        System.out.println("----------");
        System.out.printf("Words: %.2f\n", (recepients[0].totalWords / (double)recepients[0].totalMessages));
        System.out.printf("Characters: %.2f\n", (recepients[0].totalChars / (double)recepients[0].totalMessages));
        System.out.printf("Standard deviation of words: %.2f\n", getStdDev(recepients[0].sumWordsSquares, recepients[0].totalMessages, recepients[0].totalWords));
        System.out.printf("Standard deviation of characters: %.2f\n", getStdDev(recepients[0].sumCharsSquares, recepients[0].totalMessages, recepients[0].totalChars));

        System.out.println("\nSubscribers Messages");
        System.out.println("----------");
        System.out.printf("Words: %.2f\n", (recepients[1].totalWords / (double)recepients[1].totalMessages));
        System.out.printf("Characters: %.2f\n", (recepients[1].totalChars / (double)recepients[1].totalMessages));
        System.out.printf("Standard deviation of words: %.2f\n", getStdDev(recepients[1].sumWordsSquares, recepients[1].totalMessages, recepients[1].totalWords));
        System.out.printf("Standard deviation of characters: %.2f\n", getStdDev(recepients[1].sumCharsSquares, recepients[1].totalMessages, recepients[1].totalChars));

        System.out.println("\nUser Messages");
        System.out.println("----------");
        System.out.printf("Words: %.2f\n", (recepients[2].totalWords / (double)recepients[2].totalMessages));
        System.out.printf("Characters: %.2f\n", (recepients[2].totalChars / (double)recepients[2].totalMessages));
        System.out.printf("Standard deviation of words: %.2f\n", getStdDev(recepients[2].sumWordsSquares, recepients[2].totalMessages, recepients[2].totalWords));
        System.out.printf("Standard deviation of characters: %.2f\n", getStdDev(recepients[2].sumCharsSquares, recepients[2].totalMessages, recepients[2].totalChars));

        System.out.println("\nSelf Messages");
        System.out.println("----------");
        System.out.printf("Words: %.2f\n", (recepients[3].totalWords / (double)recepients[3].totalMessages));
        System.out.printf("Characters: %.2f\n", (recepients[3].totalChars / (double)recepients[3].totalMessages));
        System.out.printf("Standard deviation of words: %.2f\n", getStdDev(recepients[3].sumWordsSquares, recepients[3].totalMessages, recepients[3].totalWords));
        System.out.printf("Standard deviation of characters: %.2f\n", getStdDev(recepients[3].sumCharsSquares, recepients[3].totalMessages, recepients[3].totalChars));

        System.out.println("\n(By Response)");

        System.out.println("\nIn-Response Messages");
        System.out.println("----------");
        System.out.printf("Words: %.2f\n", (responses[0].totalWords / (double)responses[0].totalMessages));
        System.out.printf("Characters: %.2f\n", (responses[0].totalChars / (double)responses[0].totalMessages));
        System.out.printf("Standard deviation of words: %.2f\n", getStdDev(responses[0].sumWordsSquares, responses[0].totalMessages, responses[0].totalWords));
        System.out.printf("Standard deviation of characters: %.2f\n", getStdDev(responses[0].sumCharsSquares, responses[0].totalMessages, responses[0].totalChars));

        System.out.println("\nOriginal Messages");
        System.out.println("----------");
        System.out.printf("Words: %.2f\n", (responses[1].totalWords / (double)responses[1].totalMessages));
        System.out.printf("Characters: %.2f\n", (responses[1].totalChars / (double)responses[1].totalMessages));
        System.out.printf("Standard deviation of words: %.2f\n", getStdDev(responses[1].sumWordsSquares, responses[1].totalMessages, responses[1].totalWords));
        System.out.printf("Standard deviation of characters: %.2f\n", getStdDev(responses[1].sumCharsSquares, responses[1].totalMessages, responses[1].totalChars));
        System.out.println();
    }

    private static void printConditionalHistograms() {
        System.out.println("----- Conditional Histograms -----\n");

        System.out.println("(By Status)");

        System.out.println("\nPublic Messages");
        System.out.println("----------");
        System.out.println("Messages to all: " + statuses[0].numAllRcps);
        System.out.println("Messages to subscribers: " + statuses[0].numSubRcps);
        System.out.println("Messages to another user: " + statuses[0].numUserRcps);
        System.out.println("Messages to self: " + statuses[0].numSelfRcps);
        System.out.println("\nMessages with in-response: " + statuses[0].numResponse);
        System.out.println("Messages without in-response: " + (statuses[0].totalMessages - statuses[0].numResponse));

        System.out.println("\nProtected Messages");
        System.out.println("----------");
        System.out.println("Messages to all: " + statuses[1].numAllRcps);
        System.out.println("Messages to subscribers: " + statuses[1].numSubRcps);
        System.out.println("Messages to another user: " + statuses[1].numUserRcps);
        System.out.println("Messages to self: " + statuses[1].numSelfRcps);
        System.out.println("\nMessages with in-response: " + statuses[1].numResponse);
        System.out.println("Messages without in-response: " + (statuses[1].totalMessages - statuses[1].numResponse));

        System.out.println("\nPrivate Messages");
        System.out.println("----------");
        System.out.println("Messages to all: " + statuses[2].numAllRcps);
        System.out.println("Messages to subscribers: " + statuses[2].numSubRcps);
        System.out.println("Messages to another user: " + statuses[2].numUserRcps);
        System.out.println("Messages to self: " + statuses[2].numSelfRcps);
        System.out.println("\nMessages with in-response: " + statuses[2].numResponse);
        System.out.println("Messages without in-response: " + (statuses[2].totalMessages - statuses[2].numResponse));

        System.out.println("\n(By Recepient)");

        System.out.println("\nAll Messages");
        System.out.println("----------");
        System.out.println("Messages with in-response: " + recepients[0].numResponse);
        System.out.println("Messages without in-response: " + (recepients[0].totalMessages - recepients[0].numResponse));

        System.out.println("\nSubscribers Messages");
        System.out.println("----------");
        System.out.println("Messages with in-response: " + recepients[1].numResponse);
        System.out.println("Messages without in-response: " + (recepients[1].totalMessages - recepients[1].numResponse));

        System.out.println("\nUser Messages");
        System.out.println("----------");
        System.out.println("Messages with in-response: " + recepients[2].numResponse);
        System.out.println("Messages without in-response: " + (recepients[2].totalMessages - recepients[2].numResponse));

        System.out.println("\nSelf Messages");
        System.out.println("----------");
        System.out.println("Messages with in-response: " + recepients[3].numResponse);
        System.out.println("Messages without in-response: " + (recepients[3].totalMessages - recepients[3].numResponse));
    }

    private static void printStats() {
        int i;

        System.out.println("----- Basic Stats -----\n");
        System.out.println("Total number of messages: " + totalMessages);
        System.out.println("Total number of unique users: " + totalUniqueUsers);
        System.out.printf("Average number of words: %.2f\n", (totalLengthOfWords / (double)totalMessages));
        System.out.printf("Average number of characters: %.2f\n", (totalLengthOfChars / (double)totalMessages));
        System.out.printf("Standard deviation of words: %.2f\n", getStdDev(sumWordsSquares, totalMessages, totalLengthOfWords));
        System.out.printf("Standard deviation of chars: %.2f\n\n", getStdDev(sumCharsSquares, totalMessages, totalLengthOfChars));

        System.out.println("----- Message Type Histograms -----\n");
        System.out.println("total [public] messages: " + statuses[0].totalMessages);
        System.out.println("total [protected] messages: " + statuses[1].totalMessages);
        System.out.println("total [private] messages: " + statuses[2].totalMessages + "\n");

        System.out.println("total recepient [all] messages: " + recepients[0].totalMessages);
        System.out.println("total recepient [subscribers] messages: " + recepients[1].totalMessages);
        System.out.println("total recepient [another user] messages: " + recepients[2].totalMessages);
        System.out.println("total recepient [self] messages: " + recepients[3].totalMessages + "\n");

        System.out.println("total in-response messages: " + responses[0].totalMessages);
        System.out.println("total original messages: " + responses[1].totalMessages);

        for (i = 2; i < numMsgPerLength.length; i++)
            System.out.println("Number of messages with [" + i + "] words: " + numMsgPerLength[i]);
        System.out.println();

        printStatsForSubsets();
        printConditionalHistograms();
    }

    private static void createHistObj(String type, long words, long chars, long totalMessages, long sumWordSquares, long sumCharSquares) {
        try {
            JSONObject object = new JSONObject();
            object.put("words", words);
            object.put("chars", chars);
            object.put("stdDevWords", getStdDev(sumWordSquares, totalMessages, words));
            object.put("stdDevChars", getStdDev(sumCharSquares, totalMessages, chars));
            statsObject.put(type + "AvgLengthHistogram", object);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createCondHist(String type, int index) {
        try {
            JSONObject object = new JSONObject();
            object.put("numAllRecepients", statuses[index].numAllRcps);
            object.put("numSubRecepients", statuses[index].numSubRcps);
            object.put("numUserRecepients", statuses[index].numUserRcps);
            object.put("numSelfRecepients", statuses[index].numSelfRcps);
            statsObject.put(type + "RecepientsHistogram", object);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createResponseHist(String type, long numResponses, long numOriginals) {
        try {
            JSONObject object = new JSONObject();
            object.put("in-response", numResponses);
            object.put("original", numOriginals);
            statsObject.put(type + "ResponseHistogram", object);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void buildJSONObj() {
        JSONObject temp;

        try {
            // basic stats
            statsObject.put("totalMessages", totalMessages);
            statsObject.put("totalUniqueUsers", totalUniqueUsers);
            statsObject.put("averageNumWords", (totalLengthOfWords / (double)totalMessages));
            statsObject.put("averageNumChars", (totalLengthOfChars / (double)totalMessages));
            statsObject.put("stdDevWords", getStdDev(sumWordsSquares, totalMessages, totalLengthOfWords));
            statsObject.put("stdDevChars", getStdDev(sumCharsSquares, totalMessages, totalLengthOfChars));
            // message type histograms
            temp = new JSONObject();
            temp.put("public", statuses[0].totalMessages);
            temp.put("protected", statuses[1].totalMessages);
            temp.put("private", statuses[2].totalMessages);
            statsObject.put("numMessagesByStatusHistogram", temp);

            temp = new JSONObject();
            temp.put("all", recepients[0].totalMessages);
            temp.put("subscribers", recepients[1].totalMessages);
            temp.put("user", recepients[2].totalMessages);
            temp.put("self", recepients[3].totalMessages);
            statsObject.put("numMessagesByRecepientHistogram", temp);

            temp = new JSONObject();
            temp.put("in-response", responses[0].totalMessages);
            temp.put("original", responses[1].totalMessages);
            statsObject.put("numMessagesByResponseHistogram", temp);
            
            temp = new JSONObject();
            for (int i = 2; i < numMsgPerLength.length; i++)
                temp.put("" + i, numMsgPerLength[i]);
            statsObject.put("numMessagesByLengthHistogram", temp);

            // stats for subsets of messages
            String[] statusNames = {"public", "protected", "private"};
            for (int i = 0; i < statuses.length; i++) {
                createHistObj(statusNames[i], statuses[i].totalWords, statuses[i].totalChars,
                    statuses[i].totalMessages, statuses[i].sumWordsSquares,
                    statuses[i].sumCharsSquares);
                createCondHist(statusNames[i], i);
                createResponseHist(statusNames[i], statuses[i].numResponse,
                    (statuses[i].totalMessages - statuses[i].numResponse));
                    
            }

            String[] recepientNames = {"all", "subscribers", "user", "self"};
            for (int i = 0; i < recepients.length; i++) {
                createHistObj(recepientNames[i], recepients[i].totalWords, recepients[i].totalChars,
                    recepients[i].totalMessages, recepients[i].sumWordsSquares,
                    recepients[i].sumCharsSquares);
                createResponseHist(recepientNames[i], recepients[i].numResponse,
                    (recepients[i].totalMessages - recepients[i].numResponse));
            }

            String[] responseNames = {"in-response", "original"};
            for (int i = 0; i < responses.length; i++) {
                createHistObj(responseNames[i], responses[i].totalWords, responses[i].totalChars,
                    responses[i].totalMessages, responses[i].sumWordsSquares,
                    responses[i].sumCharsSquares);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printJSONObj(String filename) {
        buildJSONObj();
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(filename));
            writer.println(statsObject.toString(2));
            writer.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Not enough commandline-arguments.");
            System.out.println("correct format: java -cp .:org.json-20120521.jar [.json file] [optional output file name]");
            System.exit(1);
        }
        try {
            JSONTokener tokenizer = new JSONTokener(new FileReader(new File(args[0])));

            initStatistics();

            while (tokenizer.more()) {
                try {
                    tokenizer.skipTo('{');
                    parseJSONObj(new JSONObject(tokenizer));
                } catch (Exception e) {
                    break;
                }
            }

            if (args.length >= 2)
                printJSONObj(args[1]);
            else
                printStats();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
