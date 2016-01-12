

class actionModel {

	public String type;
	public int actionNum;
	public int pointsAdded;
	public Point location;
	public String specialMove;
	public int points;
	public String status;

	public actionModel(String type, int actionNum) {
		this.type = type;
		this.actionNum = actionNum;
	}

	public actionModel(String type, int actionNum, int pointsAdded, Point location, int points) {
		this.type = type;
		this.actionNum = actionNum;
		this.pointsAdded = pointsAdded;
		this.location = location;
		this.points = points;
	}

	public actionModel(String type, int actionNum, int pointsAdded, String specialMove, int points) {
		this.type = type;
		this.actionNum = actionNum;
		this.pointsAdded = pointsAdded;
		this.specialMove = specialMove;
		this.points = points;
	}

	public actionModel(String type, int actionNum, String status, int points) {
		this.type = type;
		this.actionNum = actionNum;
		this.status = status;
		this.points = points;
	}

}