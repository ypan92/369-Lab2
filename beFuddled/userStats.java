import java.util.ArrayList;

class userStats {

	public int totalStarters;
	public int totalCompleters;
	public int largestStarts;
	public int largestCompletes;
	public int largestWins;
	public int largestLosses;

	public ArrayList<String> largestStartIds;
	public ArrayList<String> largestCompleteIds;
	public ArrayList<String> largestWinIds;
	public ArrayList<String> largestLossIds;

	public userStats() {
		totalStarters = totalCompleters = largestStarts = largestCompletes = largestWins = largestLosses = 0;
		largestStartIds = largestCompleteIds = largestWinIds = largestLossIds = new ArrayList<String>();
	}

}