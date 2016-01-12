

class beFuddledGameModel {
	
	public int id;
	public int points;
	public boolean hasEnded;
	public boolean userWon;
	public int numMoves;
	public String userId;
	public ArrayList<actionModel> actions; 

	public beFuddledGameModel() {
		points = 0;
		hasEnded = false;
		numMoves = 0;
		userWon = false;
		userId = "";
	}

	public beFuddledGameModel(int gameId, String userId) {
		id = gameId;
		this.userId = userId;
		numMoves = 0;
		points = 0;
		hasEnded = false;
		userWon = false;
		actions = new ArrayList<actionModel>();
	}

	public void updatePoints(int points) {
		this.points += points;
	}

	public void updateMoves() {
		moves += 1;
	}

	public void endGame() {
		hasEnded = true;
	}

	public void setWinner(boolean isWinner) {
		userWon = isWinner;
	}

	public void addAction(actionModel action) {
		actions.add(action);
		numMoves += 1;
	}

}