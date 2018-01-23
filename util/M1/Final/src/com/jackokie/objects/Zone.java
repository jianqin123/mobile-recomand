package com.jackokie.objects;

public class Zone {
	private double centLong = 0;
	private double centLat = 0;
	private double leftLong = 0;
	private double rightLong = 0;
	private double upLat = 0;
	private double downLat = 0;

	public double getCentLong() {
		return centLong;
	}

	public void setCentLong(double centLong) {
		this.centLong = centLong;
	}

	public double getCentLat() {
		return centLat;
	}

	public void setCentLat(double centLat) {
		this.centLat = centLat;
	}

	public double getLeftLong() {
		return leftLong;
	}

	public void setLeftLong(double leftLong) {
		this.leftLong = leftLong;
	}

	public double getRightLong() {
		return rightLong;
	}

	public void setRightLong(double rightLong) {
		this.rightLong = rightLong;
	}

	public double getUpLat() {
		return upLat;
	}

	public void setUpLat(double upLat) {
		this.upLat = upLat;
	}

	public double getDownLat() {
		return downLat;
	}

	public void setDownLat(double downLat) {
		this.downLat = downLat;
	}

	/**
	 * 判断是否在该区域
	 * @param pos
	 * @return
	 */
	public boolean inZone(Position pos) {
		boolean boolong = (pos.getLongitude() >= leftLong) && (pos.getLongitude() <= rightLong);
		boolean boolat = (pos.getLatitude() >= downLat) && (pos.getLatitude() <= upLat);
		return boolong && boolat;
	}

	/**
	 * 将定位加入到该区域中
	 * @param userPos
	 */
	public void addPos(Position userPos) {
		double userLong = userPos.getLongitude();
		double userLat = userPos.getLatitude();
		// 域的范围===右侧界限
		if (userLong > rightLong) {
			rightLong = userLong;
		}
		// 域的范围===左侧界限
		if (userLong < leftLong) {
			leftLong = userLong;
		}
		// 域的范围===上侧界限
		if (userLat > upLat) {
			upLat = userLat;
		}
		// 域的范围===下侧界限
		if (userLat < downLat) {
			downLat = userLat;
		}
	}
}
