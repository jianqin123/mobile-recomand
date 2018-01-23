package com.jackokie.objects;

import java.util.HashMap;

/**
 * @author jackokie E-mail: jackokie@qq.com
 * @version 创建时间：2016年11月26日 下午2:17:40 类说明 :
 */
/**
 * @author jackokie
 *
 */
public class TestInfo {
	private String userID = "";
	private int income = 0;
	private int entertainment = 0;
	private int babyLabel = 0;
	private int gender = 0;
	private int shopLabel = 0;
	private String userLabels = null;
	private String arriveTime = null;
	private Position userPosition = new Position();
	private int duration = 0;
	private HashMap<String, Double> nearTrain = new HashMap<String, Double>();
	private int shopID = 0;
	private HashMap<Integer, Double> userConferenceShop = new HashMap<Integer, Double>();
	private HashMap<String, Double> shopEffectUsers = new HashMap<String, Double>();
	// 按照店铺的作用域得出的用户候选店铺
	private HashMap<Integer, Shop> candidateShops = new HashMap<Integer, Shop>();
	private int attendCate = 0;
	private double attendRatio = 0;

	public int getAttendCate() {
		return attendCate;
	}

	public void setAttendCate(int attendCate) {
		this.attendCate = attendCate;
	}

	public double getAttendRatio() {
		return attendRatio;
	}

	public void setAttendRatio(double attendRatio) {
		this.attendRatio = attendRatio;
	}

	public HashMap<Integer, Shop> getCandidateShops() {
		return candidateShops;
	}

	public void setCandidateShops(HashMap<Integer, Shop> candidateShops) {
		this.candidateShops = candidateShops;
	}

	public Position getUserPosition() {
		return userPosition;
	}

	public void setUserPosition(Position userPosition) {
		this.userPosition = userPosition;
	}

	public HashMap<Integer, Double> getUserConferenceShop() {
		return userConferenceShop;
	}

	public void setUserConferenceShop(HashMap<Integer, Double> userConferenceShop) {
		this.userConferenceShop = userConferenceShop;
	}

	public HashMap<String, Double> getShopEffectUsers() {
		return shopEffectUsers;
	}

	public void setShopEffectUsers(HashMap<String, Double> shopEffectUsers) {
		this.shopEffectUsers = shopEffectUsers;
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

	public Position getUserPos() {
		return userPosition;
	}

	public void setUserPos(Position userPosition) {
		this.userPosition = userPosition;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public HashMap<String, Double> getNearTrain() {
		return nearTrain;
	}

	public void setNearTrain(HashMap<String, Double> nearTrain) {
		this.nearTrain = nearTrain;
	}

	public int getShopID() {
		return shopID;
	}

	public void setShopID(int shopID) {
		this.shopID = shopID;
	}

	/**
	 * 将店铺存入用户此时定位所能涉及的地方
	 * @param shop
	 */
	public void addConfShop(Shop shop, double dis) {
		userConferenceShop.put(shop.getShopID(), dis);
	}

	public boolean inTrainUsers(HashMap<String, User> trainUser) {
		return trainUser.containsKey(userID);
	}

	// 给用户增加候选店铺
	public void addCandidateShop(Shop shop) {
		candidateShops.put(shop.getShopID(), shop);
	}
}
