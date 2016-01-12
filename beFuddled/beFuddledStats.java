import org.json.*;
import java.io.*;
import java.util.*;


class beFuddledStats {

	private static ArrayList<String> validKeys = new ArrayList<String>(Arrays.asList("game", "action", "user"));
	private static HashMap<Integer, beFuddledGameModel> gameCollection = new HashMap<Integer, beFuddledGameModel>();
	private static HashMap<String, userModel> userCollection = new HashMap<String, userModel>();


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

	public static Point extractLocation(JSONObject locObj) {

		int x = (Integer)locObj.get("x");
		int y = (Integer)locObj.get("y");

		return new Point(x, y);

	}

	public static actionModel retrieveActionModel(JSONObject actionObj) {

		actionModel returnModel;
		String type = actionObj.get("actionType").toString();
		int actionNum = actionObj.get("actionNumber");

		switch(type) {
			case "GameStart":
				returnModel = new actionModel(type, actionNum);
				break;
			case "Move":
				int pointsAdded = ((Integer)actionObj.get("pointsAdded")).intValue();
				int points = (Integer)actionObj.get("points");
				JSONObject locObj = (JSONObject)actionObj.get("location");
				Point location = extractLocation(locObj);
				returnModel = new actionModel(type, actionNum, pointsAdded, location, points);
				break;
			case "SpecialMove":
				int pointsAdded = ((Integer)actionObj.get("pointsAdded")).intValue();
				int points = (Integer)actionObj.get("points");
				String specialMove = actionObj.get("move").toString();
				returnModel = new actionModel(type, actionNum, pointsAdded, specialMove, points);
				break;
			case "GameEnd":
				String status = actionObj.get("gameStatus");
				int points = (Integer)actionObj.get("points");
				returnModel = new actionModel(type, actionNum, status, points);
				break;
			default:
				returnModel = new actionModel();
		}

		return returnModel;

	}

	public static beFuddledGameModel retrieveGameModel(int gameId, String userId, JSONObject actionObj) {

		beFuddledGameModel gameModel;
		userModel user;

		if (gameCollection.containsKey((Integer)gameId)) {
			gameModel = gameCollection.get((Integer)gameId);

			actionModel gameAction = retrieveActionModel(actionObj);

			if (gameAction.type == "GameEnd") {
				gameModel.endGame();
				gameModel.userWon = gameAction.status == "Win" ? true : false;

				user = userCollection.get(userId);
				user.completeGame();
				if (gameModel.userWon) {
					user.wonGame();
				}
				else {
					user.lossGame();
				}
			}

			gameModel.addAction(gameAction);
		}
		else {
			gameModel = new beFuddledGameModel(gameId, userId);

			actionModel gameAction = retrieveActionModel(actionObj);
			gameModel.addAction(gameAction);

			gameCollection.put((Integer)gameId, gameModel);

			if (userCollection.containsKey(userId)) {
				user = userCollection.get(userId);
			}
			else {
				user = new userModel(userId);
				userCollection.put(userId, user);
			}
		}

		return gameModel;

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
					JSONArray jsonArr = new JSONArray(tokenizer);

					for (int jsonCount = 0; jsonCount < jsonArr.length(); ++jsonCount) {

						
						JSONObject parsedJSON = jsonArr.getJSONObject(jsonCount);
						boolean isValidJSON = checkJSONValidity(parsedJSON);

						if (isValidJSON) {

							if (args.length > 1) {
								FileWriter writer = new FileWriter(args[1]);
							}
							else {
								//FileWriter writer = new FileWriter(System.out);
							}

							int gameId = (Integer)parsedJSON.get("game");
							JSONObject actionObj = (JSONObject)parsedJSON.get("action");
							String userId = parsedJSON.get("user").toString();

							beFuddledGameModel gameModel = retrieveGameModel(gameId, userId, actionObj);

							

						}
						else {
							System.out.println("The supplied JSON file is malformatted.");
							System.out.println("Please make sure the input file is for beFuddled JSON objects.");
							break;
						}

					}

				}
				else {
					System.out.println("The input file you specified does not exist!");
					System.out.println("Please re-run the program with an existing json file.");
				}
			}
			catch (Exception e) {

			}
		}

	}

}