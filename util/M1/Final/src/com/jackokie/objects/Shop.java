package com.jackokie.objects;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author jackokie E-mail: jackokie@qq.com
 * @version ����ʱ�䣺2016��11��26�� ����2:17:49 ��˵�� :
 */
public class Shop {
	private String name = "";
	private int shopID = 0;
	private int classification = 0;
	private Position position = new Position();

	// �����ȶ�
	private int heat = 0;
	// �û��Ĺ������
	private HashMap<String, Integer> shopContMap = new HashMap<String, Integer>();
	// ���̵Ķ�λ��
	private ArrayList<Position> posPool = new ArrayList<Position>();
	// ���̵�������/��Ч��
	private Zone effectZone = new Zone();
	// ���̵Ŀ���ƽ��ʱ��
	private double meanTime = 0;
	// ���������ͳ�ƣ����ڼ���ƽ��ʱ�䣬�Լ�������ǩ�ķֲ�
	private int shopCont = 0;
	// ���̵Ŀ��������Ϣ
	private HashMap<String, Integer> labelsCont = new HashMap<String, Integer>();
	// ���̵Ŀ�����������
	private HashMap<String, Double> labelsRatio = new HashMap<String, Double>();
	/**
	 * function : shop heat adding
	 * 
	 * @param train
	 */
	public void addUserInfo(TrainInfo train) {

		// ƽ��shop ʱ��ı仯
		int time = train.getDuration();
		meanTime = (shopCont * meanTime + time) * 1.0 / shopCont;

		// �����ȶȣ��Լ��û���������ı仯
		heat = heat + 1;
		shopCont = shopCont + 1;

		String userID = train.getUserID();

		// �����û����������ͳ��ֵ
		if (shopContMap.containsKey(userID)) {
			shopContMap.put(userID, this.shopContMap.get(userID) + 1);
		} else {
			shopContMap.put(userID, 1);
		}

		// ���̶�λ�صı仯�����û��Ķ�λ���뵽��λ��
		Position userPos = train.getUserPos();
		posPool.add(userPos);

		// ����������ĸı�
		effectZone.addPos(userPos);

		// ���²�ͬ�û����Ĺ�������Լ�ռ��
		String userLabels = train.getUserLabels();
		if (labelsCont.containsKey(userLabels)) {
			// ���´����Ĺ������
			int cont = labelsCont.get(userLabels);
			cont++;
			labelsCont.put(userLabels, cont);

			// ����ռ��
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
