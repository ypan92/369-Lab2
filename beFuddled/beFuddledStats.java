import org.json.*;
import java.io.*;
import java.util.*;


class beFuddledStats {

	private static ArrayList<String> validKeys = new ArrayList<String>(Arrays.asList("game", "action", "user"));

	//for user stats BR7
	private static HashMap<String, userModel> userCollection = new HashMap<String, userModel>();
	//for move stats BR6 & BR9
	private static moveStats actionStats = new moveStats();
	//for game stats BR4 & BR5
	private static gamePointsStats pointsStats = new gamePointsStats();
	//for histogram BR6
	private static HashMap<Integer, Integer> gamesByMoveCount = new HashMap<Integer, Integer>();
	//for move histogram BR8
	private static locationStats locStats = new locationStats();
	private static userStats usrStats = new userStats();

	private static int longestGameMoves = 0;
	private static String longestGameMovesUser;


	public static boolean checkJSONValidity(JSONObject objToCheck) {

		boolean isValid = true;
		String[] keyNames = objToCheck.getNames(objToCheck);
		for (int keyCount = 0; keyCount < keyNames.length; ++keyCount) {

			if (!validKeys.contains(keyNames[keyCount])) {
				isValid = false;
				break;
			}

		}

		return isValid;

	}

	public static String extractLocation(JSONObject locObj) {

		try {
			int x = (Integer)locObj.get("x");
			int y = (Integer)locObj.get("y");
			return "" + x + "," + y;
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return "";

	}

	public static userModel getUserModel(String id) {

		userModel user;

		if (userCollection.containsKey(id)) {
			user = userCollection.get(id);
		}
		else {
			user = new userModel(id);
			userCollection.put(id, user);
			user = userCollection.get(id);
		}

		return user;

	}

	public static void updateStats(int gameId, JSONObject actionObj, String userId) {

		try {
			String type = actionObj.get("actionType").toString();
			int actionNum = (Integer)actionObj.get("actionNumber");
			userModel user;

			switch(type) {
				case "GameStart":
					user = getUserModel(userId);
					user.startGame();
					pointsStats.updateGamesStarted();
					break;
				case "Move":
					JSONObject locObj = (JSONObject)actionObj.get("location");
					String location = extractLocation(locObj);
					
					if (locStats.locationFrequencies.containsKey(location)) {
						int occurrence = locStats.locationFrequencies.get(location);
						occurrence += 1;
						locStats.locationFrequencies.put(location, occurrence);
					}
					else {
						locStats.locationFrequencies.put(location, 1);
					}

					break;
				case "SpecialMove":
					String specialMove = actionObj.get("move").toString();
					actionStats.updateTotalSpecialMoves();
					
					switch(specialMove) {
						case "Rotate":
							actionStats.updateRotateCount();
							break;
						case "Shuffle":
							actionStats.updateShuffleCount();
							break;
						case "Clear":
							actionStats.updateClearCount();
							break;
						case "Invert":
							actionStats.updateInvertCount();
							break;
						default:

					}

					break;
				case "GameEnd":
					String status = actionObj.get("gameStatus").toString();
					int points = (Integer)actionObj.get("points");
					
					pointsStats.updateGamesCompleted();
					pointsStats.updateTotalPoints(points);
					pointsStats.summedSquaredTotalPoints += points * points;

					actionStats.updateTotalGames();
					actionStats.updateTotalMoves(actionNum);
					actionStats.summedSquaredTotalMoves += actionNum * actionNum;

					user = getUserModel(userId);
					user.completeGame();



					if (status.equals("Win")) {
						pointsStats.updateGamesWon();
						pointsStats.updateWinPoints(points);
						pointsStats.summedSquaredWinPoints += points * points;

						actionStats.updateWinGames();
						actionStats.updateWinMoves(actionNum);
						actionStats.summedSquaredWinMoves += actionNum * actionNum;

						user.wonGame();
					}
					else {
						pointsStats.updateGamesLoss();
						pointsStats.updateLossPoints(points);
						pointsStats.summedSquaredLossPoints += points * points;

						actionStats.updateLossGames();
						actionStats.updateLossMoves(actionNum);
						actionStats.summedSquaredLossMoves += actionNum * actionNum;

						user.lossGame();
					}


					if (gamesByMoveCount.containsKey(actionNum)) {
						Integer gamesWithThatMoveCount = gamesByMoveCount.get(actionNum);
						gamesWithThatMoveCount += 1;
						gamesByMoveCount.put(actionNum, gamesWithThatMoveCount);
					}
					else {
						gamesByMoveCount.put(actionNum, 1);
					}

					if (actionNum > longestGameMoves) {
						longestGameMoves = actionNum;
						longestGameMovesUser = userId;
					}

					break;
				default:
					
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static ArrayList<Integer> getMoveHistogram() {

		int it;
		ArrayList<Integer> result = new ArrayList<Integer>();

		int moves0to15 = 0;
		for (it = 0; it < 15; it++) {
			if (gamesByMoveCount.containsKey(it)) {
				moves0to15 += gamesByMoveCount.get(it);
			}
		}
		result.add(moves0to15);

		int moves15to30 = 0;
		for (it = 15; it < 30; it++) {
			if (gamesByMoveCount.containsKey(it)) {
				moves15to30 += gamesByMoveCount.get(it);
			}
		}
		result.add(moves15to30);

		int moves30to40 = 0;
		for (it = 30; it < 40; it++) {
			if (gamesByMoveCount.containsKey(it)) {
				moves30to40 += gamesByMoveCount.get(it);
			}
		}
		result.add(moves30to40);

		int moves40to50 = 0;
		for (it = 40; it < 50; it++) {
			if (gamesByMoveCount.containsKey(it)) {
				moves40to50 += gamesByMoveCount.get(it);
			}
		}
		result.add(moves40to50);

		int moves50to60 = 0;
		for (it = 50; it < 60; it++) {
			if (gamesByMoveCount.containsKey(it)) {
				moves50to60 += gamesByMoveCount.get(it);
			}
		}
		result.add(moves50to60);

		int moves60to80 = 0;
		for (it = 60; it < 80; it++) {
			if (gamesByMoveCount.containsKey(it)) {
				moves60to80 += gamesByMoveCount.get(it);
			}
		}
		result.add(moves60to80);

		int moves80to100 = 0;
		for (it = 80; it < 100; it++) {
			if (gamesByMoveCount.containsKey(it)) {
				moves80to100 += gamesByMoveCount.get(it);
			}
		}
		result.add(moves80to100);


		return result;

	}

	public static void computeUserStats() {

		for (userModel model : userCollection.values()) {

			if (model.gamesStarted > 0) {
				usrStats.totalStarters += 1;
			}

			if (model.gamesCompleted > 0) {
				usrStats.totalCompleters += 1;
			}

			if (model.gamesStarted == usrStats.largestStarts) {
				usrStats.largestStartIds.add(model.userId);
			}
			else if (model.gamesStarted > usrStats.largestStarts) {
				usrStats.largestStarts = model.gamesStarted;
				usrStats.largestStartIds.clear();
				usrStats.largestStartIds.add(model.userId);
			}

			if (model.gamesCompleted == usrStats.largestCompletes) {
				usrStats.largestCompleteIds.add(model.userId);
			}
			else if (model.gamesCompleted > usrStats.largestCompletes) {
				usrStats.largestCompletes = model.gamesCompleted;
				usrStats.largestCompleteIds.clear();
				usrStats.largestCompleteIds.add(model.userId);
			}

			if (model.gamesWon == usrStats.largestWins) {
				usrStats.largestWinIds.add(model.userId);
			}
			else if (model.gamesWon > usrStats.largestWins) {
				usrStats.largestWins = model.gamesWon;
				usrStats.largestWinIds.clear();
				usrStats.largestWinIds.add(model.userId);
			}

			if (model.gamesLoss == usrStats.largestLosses) {
				usrStats.largestLossIds.add(model.userId);
			}
			else if (model.gamesLoss > usrStats.largestLosses) {
				usrStats.largestLosses = model.gamesLoss;
				usrStats.largestLossIds.clear();
				usrStats.largestLossIds.add(model.userId);
			}
		}

	}

	private static String convertArrayListToString(ArrayList<String> userIds) {
		String result = "";

		for (String s : userIds) {
			result += s + ", ";
		}

		return result.substring(0, result.length() - 2);
	}


	public static JSONObject resultJSON(ArrayList<Integer> moveHistogram, ArrayList<String> top10Locations, ArrayList<Integer> top10LocFreq) {

		JSONObject result = new JSONObject();
		try {
			result.put("Total Games Seen", pointsStats.gamesStarted);
			result.put("Total Completed Games", pointsStats.gamesCompleted);
			result.put("Average Points Per Game", pointsStats.getTotalAvg());
			result.put("Standard Deviation for Points Per Game", pointsStats.getTotalStdDev());
			result.put("Average Points Per Win Game", pointsStats.getWinAvg());
			result.put("Standard Deviation for Points Per Win Game", pointsStats.getWinStdDev());
			result.put("Average Points Per Loss Game", pointsStats.getLossAvg());
			result.put("Standard Deviation for Points Per Loss Game", pointsStats.getLossStdDev());
			result.put("Average Moves Per Game", actionStats.getTotalAvg());
			result.put("Standard Deviation of Moves Per Game", actionStats.getTotalStdDev());
			result.put("Average Moves Per Win Game", actionStats.getWinAvg());
			result.put("Standard Deviation of Moves Per Win Game", actionStats.getWinStdDev());
			result.put("Average Moves Per Loss Game", actionStats.getLossAvg());
			result.put("Standard Deviation of Moves Per Loss Game", actionStats.getLossStdDev());
			
			JSONObject moveHistogramChart = new JSONObject();
			moveHistogramChart.put("[0,15]", moveHistogram.get(0));
			moveHistogramChart.put("[15,30]", moveHistogram.get(1));
			moveHistogramChart.put("[30,40]", moveHistogram.get(2));
			moveHistogramChart.put("[40,50]", moveHistogram.get(3));
			moveHistogramChart.put("[50,60]", moveHistogram.get(4));
			moveHistogramChart.put("[60,80]", moveHistogram.get(5));
			moveHistogramChart.put("[80+]", moveHistogram.get(6));

			result.put("Moves Per Game Histogram", moveHistogramChart);

			result.put("Total Users Who Started A Game", usrStats.totalStarters);
			result.put("Total Users Who Completed A Game", usrStats.totalCompleters);
			result.put("Largest Number of Games Started By A User", usrStats.largestStarts);
			result.put("Largest Number of Games Completed By A User", usrStats.largestCompletes);
			result.put("Largest Number of Wins a User Had", usrStats.largestWins);
			result.put("Largest Number of Losses a User Had", usrStats.largestLosses);

			result.put("User Who Played The Longest Game", longestGameMovesUser);
			result.put("User(s) Who Started the Largest Number of Games", convertArrayListToString(usrStats.largestStartIds));
			result.put("User(s) Who Completed the Largest Number of Games", convertArrayListToString(usrStats.largestCompleteIds));
			result.put("User(s) Who Won the Most Games", convertArrayListToString(usrStats.largestWinIds));
			result.put("User(s) Who Loss the Most Games", convertArrayListToString(usrStats.largestLossIds));

			JSONObject locationHistogram = new JSONObject();
			int ndx = 0;
			for (String loc : top10Locations) {
				String x = loc.substring(0, loc.indexOf(','));
				String y = loc.substring(loc.indexOf(','), loc.length());
				locationHistogram.put("[" + x + ", " + y + "]", top10LocFreq.get(ndx));
				ndx += 1;
			}
			result.put("Histogram of Moves", locationHistogram);

			JSONObject specialMoveHistogram = new JSONObject();
			specialMoveHistogram.put("Rotate", actionStats.rotateCount);
			specialMoveHistogram.put("Shuffle", actionStats.shuffleCount);
			specialMoveHistogram.put("Clear", actionStats.clearCount);
			specialMoveHistogram.put("Invert", actionStats.invertCount);

			result.put("Historgram of Special Moves", specialMoveHistogram);

		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}


	public static void main(String[] args) {

		if (args.length < 1) {
			System.out.println("\nYou did not specify a json file to read from!");
			System.out.println("Please re-run the program as: java beFuddledStats srcFileName");
			System.out.println("\tWhere srcFileName is the name of the file containing the JSON" 
								+ " to perform computations on.");
		}
		else {
			try {
				File srcFile = new File(args[0]);
				if (srcFile.exists() && !srcFile.isDirectory()) {

					JSONTokener tokenizer = new JSONTokener(new FileReader(srcFile));
	                while (tokenizer.more()) {
	                    try {
							tokenizer.skipTo('{');
							JSONObject parsedJSON = new JSONObject(tokenizer);
							boolean isValidJSON = checkJSONValidity(parsedJSON);

							if (isValidJSON) {

								int gameId = (Integer)parsedJSON.get("game");
								JSONObject actionObj = (JSONObject)parsedJSON.get("action");
								String userId = parsedJSON.get("user").toString();

								updateStats(gameId, actionObj, userId);

							}
							else {
								System.out.println("The supplied JSON file is malformatted.");
								System.out.println("Please make sure the input file is for beFuddled JSON objects.");
								break;
							}
	                    }
	                    catch (Exception e) {
	                        break;
	                    }
					}

					ArrayList<Integer> moveHistogram = getMoveHistogram();
					computeUserStats();

					HashMap sortedLocations = locStats.getSortedLocations();
					ArrayList<String> top10Locations = new ArrayList<String>();
					int count = 0;
					for (Object key : sortedLocations.keySet()) {
						top10Locations.add(key.toString());
						count += 1;
						if (count == 10)
							break;
					}

					ArrayList<Integer> top10LocFreq = new ArrayList<Integer>();
					count = 0;
					for (Object value : sortedLocations.values()) {
						top10LocFreq.add((Integer)value);
						count += 1;
						if (count == 10)
							break;
					}


					JSONObject result = resultJSON(moveHistogram, top10Locations, top10LocFreq);

					System.out.println(result.toString(2));

				}
				else {
					System.out.println("The input file you specified does not exist!");
					System.out.println("Please re-run the program with an existing json file.");
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
