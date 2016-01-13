

class moveStats {

	public int totalGames;
	public int totalMoves;
	public int winGames;
	public int winMoves;
	public int lossGames;
	public int lossMoves;

	public int totalSpecialMoves;
	public int rotateCount;
	public int shuffleCount;
	public int invertCount;
	public int clearCount;

	public long summedSquaredTotalMoves;
	public long summedSquaredWinMoves;
	public long summedSquaredLossMoves;



	public moveStats() {
		totalGames = totalMoves = winGames = winMoves = lossGames = lossMoves = 0;
		totalSpecialMoves = rotateCount = shuffleCount = invertCount = clearCount = 0;
		summedSquaredTotalMoves = summedSquaredWinMoves = summedSquaredLossMoves = 0;
	}

	public void updateTotalGames() {
		totalGames += 1;
	}

	public void updateTotalMoves(int moves) {
		totalMoves += moves;
	}

	public void updateWinGames() {
		winGames += 1;
	}

	public void updateWinMoves(int moves) {
		winMoves += moves;
	}

	public void updateLossGames() {
		lossGames += 1;
	}

	public void updateLossMoves(int moves) {
		lossMoves += moves;
	}

	public void updateTotalSpecialMoves() {
		totalSpecialMoves += 1;
	}

	public void updateRotateCount() {
		rotateCount += 1;
	}

	public void updateShuffleCount() {
		shuffleCount += 1;
	}

	public void updateInvertCount() {
		invertCount += 1;
	}

	public void updateClearCount() {
		clearCount += 1;
	}

	public double getTotalAvg() {
		if (totalGames == 0)
			return 0;
		return totalMoves / totalGames;
	}

	public double getTotalStdDev() {
		if (totalGames == 0)
			return 0;
		return Math.sqrt((summedSquaredTotalMoves / totalGames) - (getTotalAvg() * getTotalAvg()));
	}

	public double getWinAvg() {
		if (winGames == 0)
			return 0;
		return winMoves / winGames;
	}

	public double getWinStdDev() {
		if (winGames == 0)
			return 0;
		return Math.sqrt((summedSquaredWinMoves / winGames) - (getWinAvg() * getWinAvg()));
	}

	public double getLossAvg() {
		if (lossGames == 0)
			return 0;
		return lossMoves / lossGames;
	}

	public double getLossStdDev() {
		if (lossGames == 0)
			return 0;
		return Math.sqrt((summedSquaredLossMoves / lossGames) - (getLossAvg() * getLossAvg()));
	}

}