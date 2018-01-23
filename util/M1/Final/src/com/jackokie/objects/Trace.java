package com.jackokie.objects;

public class Trace {
	String userID = null;
	String arriveTime = null;
	Position userPos = new Position();
	int duration = 0;
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getArriveTime() {
		return arriveTime;
	}
	public void setArriveTime(String arriveTime) {
		this.arriveTime = arriveTime;
	}
	public Position getUserPos() {
		return userPos;
	}
	public void setUserPos(Position userPos) {
		this.userPos = userPos;
	}
	public 	int getDuration() {
		return duration;
	}
	public void setDuration(	int duration) {
		this.duration = duration;
	}
}
