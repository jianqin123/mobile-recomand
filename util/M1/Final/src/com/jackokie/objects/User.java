package com.jackokie.objects;

import java.util.ArrayList;
import java.util.HashMap;

public class User {
	private String userID = null;
	private int income = 0;
	private int entertainment = 0;
	private int babyLabel = 0;
	private int gender = 0;
	private int shopLabel = 0;
	private String userLabels = null;
	// *************************************用户的一致性特性*************************************
	// 一致性用户
	private ArrayList<User> accordUsers = null;

	// *************************************用户兴趣*************************************
	// 用户的猎奇度，用户去的店铺数越多，表示用户可能会追求新意
	private double userCuriosity = 0;
	// 用户的广泛度， 用户去的店铺类型越多，表示用户兴趣点越广泛
	private double cateVariety = 0;

	// 表示用户定位坐标与店铺坐标差值的分布情况
	private HashMap<Integer, Double> dis2ShopMap = new HashMap<Integer, Double>();


	// *************************************用户的购物时间特征*************************************
	private ArrayList<String> shopTime = new ArrayList<String>();
	private ArrayList<String> traceTime = new ArrayList<String>();

	// *************************************用户购物行为*************************************
	// 训练集购物次数
	private int shopCont = 0;
	// 描述用户对于特定类型店铺消费次数------店铺类别：消费次数
	private HashMap<Integer, Integer> cateCont = new HashMap<Integer, Integer>();
	// 描述用户对于特定类别店铺的倾向性------ 店铺类别：所占比例
	private HashMap<Integer, Double> cateAttend = new HashMap<Integer, Double>();
	// 描述训练用户购物的记录------ 时间---店铺
	private HashMap<String, Integer> shopHis = new HashMap<String, Integer>();
	// 描述用户的购物记录统计----------店铺ID : 消费次数
	private HashMap<Integer, Integer> shopStat = new HashMap<Integer, Integer>();
	// 描述用户不同时刻去同一家店铺，记录的店铺位置------ 店铺ID ： 店铺定位：定位次数
	private HashMap<Integer, HashMap<Position, Integer>> shopPosStat = new HashMap<Integer, HashMap<Position, Integer>>();

	// *************************************用户的地点特征信息*************************************
	// 总的trace次数
	private int traceCont = 0;
	// 用户活跃度
	private double activeness = 0;
	// 用户轨迹频率
	private HashMap<Position, Integer> posFreq = new HashMap<Position, Integer>();
	// 用户的轨迹
	private HashMap<String, Trace> traceMap = new HashMap<String, Trace>();

	// ***************************************************************************************************************

	public HashMap<String, Trace> getTraceMap() {
		return traceMap;
	}
	public void setTraceMap(HashMap<String, Trace> traceMap) {
		this.traceMap = traceMap;
	}

	public HashMap<Integer, Double> getCateAttend() {
		return cateAttend;
	}

	public void setCateAttend(HashMap<Integer, Double> cateAttend) {
		this.cateAttend = cateAttend;
	}

	public HashMap<Integer, Integer> getShopStat() {
		return shopStat;
	}

	public ArrayList<User> getAccordUsers() {
		return accordUsers;
	}

	public void setAccordUsers(ArrayList<User> accordUsers) {
		this.accordUsers = accordUsers;
	}

	public double getUserCuriosity() {
		return userCuriosity;
	}

	public void setUserCuriosity(double userCuriosity) {
		this.userCuriosity = userCuriosity;
	}

	public double getCateVariety() {
		return cateVariety;
	}

	public void setCateVariety(double cateVariety) {
		this.cateVariety = cateVariety;
	}

	public HashMap<Integer, Double> getDis2ShopMap() {
		return dis2ShopMap;
	}

	public void setDis2ShopMap(HashMap<Integer, Double> dis2ShopMap) {
		this.dis2ShopMap = dis2ShopMap;
	}
	public ArrayList<String> getShopTime() {
		return shopTime;
	}

	public void setShopTime(ArrayList<String> shopTime) {
		this.shopTime = shopTime;
	}

	public ArrayList<String> getTraceTime() {
		return traceTime;
	}

	public void setTraceTime(ArrayList<String> traceTime) {
		this.traceTime = traceTime;
	}

	public HashMap<Integer, Integer> getCateCont() {
		return cateCont;
	}

	public void setCateCont(HashMap<Integer, Integer> cateCont) {
		this.cateCont = cateCont;
	}

	public HashMap<String, Integer> getShopHis() {
		return shopHis;
	}

	public void setShopHis(HashMap<String, Integer> shopHis) {
		this.shopHis = shopHis;
	}

	public HashMap<Integer, HashMap<Position, Integer>> getShopPosStat() {
		return shopPosStat;
	}

	public void setShopPosStat(HashMap<Integer, HashMap<Position, Integer>> shopPosStat) {
		this.shopPosStat = shopPosStat;
	}

	public int getTraceCont() {
		return traceCont;
	}

	public void setTraceCont(int traceCont) {
		this.traceCont = traceCont;
	}

	public double getActiveness() {
		return activeness;
	}

	public void setActiveness(double activeness) {
		this.activeness = activeness;
	}

	public void setShopStat(HashMap<Integer, Integer> shopStat) {
		this.shopStat = shopStat;
	}

	public int getShopCont() {
		return shopCont;
	}

	public void setShopCont(int shopCont) {
		this.shopCont = shopCont;
	}

	public HashMap<Position, Integer> getPosFreq() {
		return posFreq;
	}

	public void setPosFreq(HashMap<Position, Integer> posFreq) {
		this.posFreq = posFreq;
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

	/**
	 * 用户增加轨迹，用于后续学习单个用户的购物行为，从而能够更加准确的预测
	 * 
	 * @param trace
	 *            用户的轨迹
	 */
	public void addTrace(Trace trace) {
		traceCont = traceCont + 1;
		Position pos = trace.getUserPos();
		String time = trace.getArriveTime();
		traceMap.put(time, trace);
		traceTime.add(time);
		if (posFreq.containsKey(pos)) {
			int cont = posFreq.get(pos);
			cont++;
			posFreq.put(pos, cont);
		}
	}

	// 用户增加训练信息
	public void addShopInfo(TrainInfo trainInfo) {

		// 购物总数
		shopCont = shopCont + 1;
		int shopClassi = trainInfo.getShopClassi();
		int shopID = trainInfo.getShopID();
		String arriveTime = trainInfo.getArriveTime();
		Position userPos = trainInfo.getUserPos();
		Position shopPos = trainInfo.getShopPos();

		// 店铺类别更新
		if (cateCont.containsKey(shopClassi)) {
			int cont = cateCont.get(shopClassi);
			cont++;
			cateCont.put(shopClassi, cont);
			for (Integer classi : cateCont.keySet()) {
				double ratio = cateCont.get(classi) * 1.0 / shopCont; 
				cateAttend.put(shopClassi, ratio);
			}
		} else {
			cateCont.put(shopClassi, 1);
			for (Integer classi : cateCont.keySet()) {
				double ratio = cateCont.get(classi) * 1.0 / shopCont; 
				cateAttend.put(shopClassi, ratio);
			}
		}

		// 用户购物时间记录
		shopHis.put(arriveTime, shopID);

		// 店铺购物记录统计
		if (shopStat.containsKey(shopID)) {
			int cont = shopStat.get(shopID);
			cont++;
			shopStat.put(shopID, cont);
			
			// 记录相同店铺时候，用户的不同定位坐标
			HashMap<Position, Integer> posMap = shopPosStat.get(shopID);
			// 包含此位置记录
			if (posMap.containsKey(userPos)) {
				int posCont = posMap.get(userPos);
				posCont++;
				posMap.put(userPos, posCont);
			} else {
				posMap.put(userPos, 1);
			}
		} else {
			shopStat.put(shopID, 1);
			HashMap<Position, Integer> posMap = new HashMap<Position, Integer>();
			posMap.put(userPos, 1);
			shopPosStat.put(shopID, posMap);
		}
		
		// 对于用户的定位精度问题
		double dis = userPos.getDis(shopPos);
		dis2ShopMap.put(shopID, dis);
	}

}
