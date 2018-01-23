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
	// *************************************�û���һ��������*************************************
	// һ�����û�
	private ArrayList<User> accordUsers = null;

	// *************************************�û���Ȥ*************************************
	// �û�������ȣ��û�ȥ�ĵ�����Խ�࣬��ʾ�û����ܻ�׷������
	private double userCuriosity = 0;
	// �û��Ĺ㷺�ȣ� �û�ȥ�ĵ�������Խ�࣬��ʾ�û���Ȥ��Խ�㷺
	private double cateVariety = 0;

	// ��ʾ�û���λ��������������ֵ�ķֲ����
	private HashMap<Integer, Double> dis2ShopMap = new HashMap<Integer, Double>();


	// *************************************�û��Ĺ���ʱ������*************************************
	private ArrayList<String> shopTime = new ArrayList<String>();
	private ArrayList<String> traceTime = new ArrayList<String>();

	// *************************************�û�������Ϊ*************************************
	// ѵ�����������
	private int shopCont = 0;
	// �����û������ض����͵������Ѵ���------����������Ѵ���
	private HashMap<Integer, Integer> cateCont = new HashMap<Integer, Integer>();
	// �����û������ض������̵�������------ ���������ռ����
	private HashMap<Integer, Double> cateAttend = new HashMap<Integer, Double>();
	// ����ѵ���û�����ļ�¼------ ʱ��---����
	private HashMap<String, Integer> shopHis = new HashMap<String, Integer>();
	// �����û��Ĺ����¼ͳ��----------����ID : ���Ѵ���
	private HashMap<Integer, Integer> shopStat = new HashMap<Integer, Integer>();
	// �����û���ͬʱ��ȥͬһ�ҵ��̣���¼�ĵ���λ��------ ����ID �� ���̶�λ����λ����
	private HashMap<Integer, HashMap<Position, Integer>> shopPosStat = new HashMap<Integer, HashMap<Position, Integer>>();

	// *************************************�û��ĵص�������Ϣ*************************************
	// �ܵ�trace����
	private int traceCont = 0;
	// �û���Ծ��
	private double activeness = 0;
	// �û��켣Ƶ��
	private HashMap<Position, Integer> posFreq = new HashMap<Position, Integer>();
	// �û��Ĺ켣
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
	 * �û����ӹ켣�����ں���ѧϰ�����û��Ĺ�����Ϊ���Ӷ��ܹ�����׼ȷ��Ԥ��
	 * 
	 * @param trace
	 *            �û��Ĺ켣
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

	// �û�����ѵ����Ϣ
	public void addShopInfo(TrainInfo trainInfo) {

		// ��������
		shopCont = shopCont + 1;
		int shopClassi = trainInfo.getShopClassi();
		int shopID = trainInfo.getShopID();
		String arriveTime = trainInfo.getArriveTime();
		Position userPos = trainInfo.getUserPos();
		Position shopPos = trainInfo.getShopPos();

		// ����������
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

		// �û�����ʱ���¼
		shopHis.put(arriveTime, shopID);

		// ���̹����¼ͳ��
		if (shopStat.containsKey(shopID)) {
			int cont = shopStat.get(shopID);
			cont++;
			shopStat.put(shopID, cont);
			
			// ��¼��ͬ����ʱ���û��Ĳ�ͬ��λ����
			HashMap<Position, Integer> posMap = shopPosStat.get(shopID);
			// ������λ�ü�¼
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
		
		// �����û��Ķ�λ��������
		double dis = userPos.getDis(shopPos);
		dis2ShopMap.put(shopID, dis);
	}

}
