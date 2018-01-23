package com.jackokie.objects;

/**
 * @author jackokie E-mail: jackokie@qq.com
 * @version 创建时间：2016年11月26日 下午2:17:31 类说明 :
 */
public class TrainInfo implements Comparable<TrainInfo>{
	private String userID = "";
	private int income = 0;
	private int entertainment = 0;
	private int babyLabel = 0;
	private int gender = 0;
	private int shopLabel = 0;
	private String userLabels = null;
	private String arriveTime = null;
	private int duration = 0;
	private int shopID = 0;
	private int shopClassi = 0;
	private int shopHeat = 0;
	private Position userPos = new Position();
	private Position shopPos = new Position();

	public Position getUserPos() {
		return userPos;
	}

	public void setUserPos(Position userPos) {
		this.userPos = userPos;
	}

	public Position getShopPos() {
		return shopPos;
	}

	public void setShopPos(Position shopPos) {
		this.shopPos = shopPos;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public int getIncome() {
		return income;
	}

	public void setIncome(int income) {
		this.income = income;
	}

	public int getEntertainment() {
		return entertainment;
	}

	public void setEntertainment(int entertainment) {
		this.entertainment = entertainment;
	}

	public int getBabyLabel() {
		return babyLabel;
	}

	public void setBabyLabel(int babyLabel) {
		this.babyLabel = babyLabel;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public int getShopLabel() {
		return shopLabel;
	}

	public void setShopLabel(int shopLabel) {
		this.shopLabel = shopLabel;
	}

	public String getUserLabels() {
		return userLabels;
	}

	public void setUserLabels(String userLabels) {
		this.userLabels = userLabels;
	}

	public String getArriveTime() {
		return arriveTime;
	}

	public void setArriveTime(String arriveTime) {
		this.arriveTime = arriveTime;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getShopID() {
		return shopID;
	}

	public void setShopID(int shopID) {
		this.shopID = shopID;
	}

	public int getShopClassi() {
		return shopClassi;
	}

	public void setShopClassi(int shopClassi) {
		this.shopClassi = shopClassi;
	}

	public int getShopHeat() {
		return shopHeat;
	}

	public void setShopHeat(int shopHeat) {
		this.shopHeat = shopHeat;
	}

	public TrainInfo clone() {
		TrainInfo newTrain = new TrainInfo();
		newTrain.setUserID(userID);
		newTrain.setIncome(income);
		newTrain.setEntertainment(entertainment);
		newTrain.setBabyLabel(babyLabel);
		newTrain.setGender(gender);
		newTrain.setShopLabel(shopLabel);
		newTrain.setUserLabels(userLabels);
		newTrain.setArriveTime(arriveTime);
		newTrain.setUserPos(userPos);
		newTrain.setDuration(duration);
		newTrain.setShopID(shopID);
		newTrain.setShopPos(shopPos);
		newTrain.setShopClassi(shopClassi);
		newTrain.setShopHeat(shopHeat);
		return newTrain;
	}

	@Override
	public int compareTo(TrainInfo train) {
		// 比较书序 Postion > Shopping > UserID
		Position trainPos = train.getUserPos();
		if (!this.userPos.equals(trainPos)) {
			return userPos.compareTo(trainPos);
		} else {
			if (shopLabel != train.getShopLabel()) {
				return shopLabel - train.getShopLabel();
			} else {
				if (!userID.equals(train.getUserID())) {
					return userID.compareTo(train.getUserID());
				} else {
					return 0;
				}
			}
		}
	}
}
