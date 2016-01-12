

public class userModel {

	public String userId;
	public int gamesStarted;
	public int gamesCompleted;
	public int gamesWon;
	public int gamesLoss;

	public userModel(String id) {
		userId = id;
		gamesStarted = 1;
		gamesCompleted = 0;
		gamesWon = 0;
		gamesLoss = 0;

	}

	public void startGame() {
		gamesStarted += 1;
	}

	public void completeGame() {
		gamesCompleted += 1;
	}

	public void wonGame() {
		gamesWon += 1;
	}

	public void lossGame() {
		gamesLoss += 1;
	}

}