package com.jackokie.objects;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author jackokie E-mail: jackokie@qq.com
 * @version 创建时间：2016年11月26日 下午2:17:49 类说明 :
 */
public class Shop {
	private String name = "";
	private int shopID = 0;
	private int classification = 0;
	private Position position = new Position();

	// 店铺热度
	private int heat = 0;
	// 用户的购物次数
	private HashMap<String, Integer> shopContMap = new HashMap<String, Integer>();
	// 店铺的定位池
	private ArrayList<Position> posPool = new ArrayList<Position>();
	// 店铺的作用域/有效域
	private Zone effectZone = new Zone();
	// 店铺的客流平均时间
	private double meanTime = 0;
	// 购物次数的统计，用于计算平均时间，以及客流标签的分布
	private int shopCont = 0;
	// 店铺的客流类别信息
	private HashMap<String, Integer> labelsCont = new HashMap<String, Integer>();
	// 店铺的客流量类别比例
	private HashMap<String, Double> labelsRatio = new HashMap<String, Double>();
	/**
	 * function : shop heat adding
	 * 
	 * @param train
	 */
	public void addUserInfo(TrainInfo train) {

		// 平均shop 时间的变化
		int time = train.getDuration();
		meanTime = (shopCont * meanTime + time) * 1.0 / shopCont;

		// 店铺热度，以及用户购物次数的变化
		heat = heat + 1;
		shopCont = shopCont + 1;

		String userID = train.getUserID();

		// 单个用户购物次数的统计值
		if (shopContMap.containsKey(userID)) {
			shopContMap.put(userID, this.shopContMap.get(userID) + 1);
		} else {
			shopContMap.put(userID, 1);
		}

		// 店铺定位池的变化，将用户的定位加入到定位池
		Position userPos = train.getUserPos();
		posPool.add(userPos);

		// 店铺作用域的改变
		effectZone.addPos(userPos);

		// 更新不同用户类别的购物次数以及占比
		String userLabels = train.getUserLabels();
		if (labelsCont.containsKey(userLabels)) {
			// 更新此类别的购物次数
			int cont = labelsCont.get(userLabels);
			cont++;
			labelsCont.put(userLabels, cont);

			// 更新占比
			for (String labels : labelsCont.keySet()) {
				double ratio = labelsCont.get(labels) * 1.0 / shopCont;
				labelsRatio.put(labels, ratio);
			}
		} else {
			labelsCont.put(userLabels, 1);
			for (String labels : labelsCont.keySet()) {
				double ratio = labelsCont.get(labels) * 1.0 / shopCont;
				labelsRatio.put(labels, ratio);
			}

		}
	}

	public HashMap<String, Integer> getUserMap() {
		return shopContMap;
	}

	public void setUserMap(HashMap<String, Integer> userMap) {
		this.shopContMap = userMap;
	}

	public String getName() {
		return name;
	}

	public HashMap<String, Integer> getShopContMap() {
		return shopContMap;
	}

	public void setShopContMap(HashMap<String, Integer> shopContMap) {
		this.shopContMap = shopContMap;
	}

	public ArrayList<Position> getPool() {
		return posPool;
	}

	public void setPool(ArrayList<Position> posPool) {
		this.posPool = posPool;
	}

	public Zone getEffectZone() {
		return effectZone;
	}

	public void setEffectZone(Zone effectZone) {
		this.effectZone = effectZone;
	}

	public double getMeanTime() {
		return meanTime;
	}

	public void setMeanTime(double meanTime) {
		this.meanTime = meanTime;
	}

	public int getShopCont() {
		return shopCont;
	}

	public void setShopCont(int shopCont) {
		this.shopCont = shopCont;
	}

	public HashMap<String, Integer> getLabelsCont() {
		return labelsCont;
	}

	public void setLabelsCont(HashMap<String, Integer> labelsCont) {
		this.labelsCont = labelsCont;
	}

	public HashMap<String, Double> getLabelsRatio() {
		return labelsRatio;
	}

	public void setLabelsRatio(HashMap<String, Double> labelsRatio) {
		this.labelsRatio = labelsRatio;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getShopID() {
		return shopID;
	}

	public void setShopID(int shopID) {
		this.shopID = shopID;
	}

	public int getClassification() {
		return classification;
	}

	public void setClassification(int classification) {
		this.classification = classification;
	}

	public int getHeat() {
		return heat;
	}

	public void setHeat(int heat) {
		this.heat = heat;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}
}
