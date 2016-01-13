

class gamePointsStats {

	public int gamesStarted;
	public int gamesCompleted;
	public int totalPoints;
	public int winPoints;
	public int lossPoints;
	public int gamesWon;
	public int gamesLoss;

	public double totalAvg;
	public double totalStdDev;
	public double winAvg;
	public double winStdDev;
	public double lossAvg;
	public double lossStdDev;

	public long summedSquaredTotalPoints;
	public long summedSquaredWinPoints;
	public long summedSquaredLossPoints;

	public gamePointsStats() {
		gamesStarted = gamesCompleted = totalPoints = winPoints = lossPoints = gamesWon = gamesLoss = 0;
		totalAvg = totalStdDev = winAvg = winStdDev = lossAvg = lossStdDev = 0.0;
		summedSquaredTotalPoints = summedSquaredWinPoints = summedSquaredLossPoints = 0;
	}

	public void updateGamesStarted() {
		gamesStarted += 1;
	}

	public void updateGamesCompleted() {
		gamesCompleted += 1;
	}

	public void updateTotalPoints(int points) {
		totalPoints += points;
	}

	public void updateWinPoints(int points) {
		winPoints += points;
	}

	public void updateLossPoints(int points) {
		lossPoints += points;
	}

	public void updateGamesWon() {
		gamesWon += 1;
	}

	public void updateGamesLoss() {
		gamesLoss += 1;
	}

	public double getTotalAvg() {
		if (gamesCompleted == 0)
			return 0;
		return totalPoints / gamesCompleted;
	}

	public double getTotalStdDev() {
		if (gamesCompleted == 0)
			return 0;
		return Math.sqrt((summedSquaredTotalPoints / gamesCompleted) - (getTotalAvg() * getTotalAvg()));
	}

	public double getWinAvg() {
		if (gamesWon == 0)
			return 0;
		return winPoints / gamesWon;
	}

	public double getWinStdDev() {
		if (gamesWon == 0)
			return 0;
		return Math.sqrt((summedSquaredWinPoints / gamesWon) - (getWinAvg() * getWinAvg()));
	}

	public double getLossAvg() {
		if (gamesLoss == 0)
			return 0;
		return lossPoints / gamesLoss;
	}

	public double getLossStdDev() {
		if (gamesLoss == 0)
			return 0;
		return Math.sqrt((summedSquaredLossPoints / gamesLoss) - (getLossAvg() * getLossAvg()));
	}

}