package com.jackokie.objects;

import java.awt.List;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author jackokie E-mail: jackokie@qq.com
 * @version ����ʱ�䣺2016��11��26�� ����8:29:15 ��˵�� :
 */
public class LabelsCategory {
	private int totalSample = 0;
	private int cate = 0;
	private String userLabels = null;
	private HashMap<Integer, Integer> cateCont = new HashMap<Integer, Integer>();
	private HashMap<Integer, Double> cateRatio = new HashMap<Integer, Double>();

	/**
	 * @param userLabels
	 */
	public LabelsCategory(String userLabels) {
		super();
		this.userLabels = userLabels;
	}

	public int getTotalSample() {
		return totalSample;
	}

	public void setTotalSample(int totaSample) {
		this.totalSample = totaSample;
	}

	public int getCate() {
		return cate;
	}

	public void setCate(int cate) {
		this.cate = cate;
	}

	public String getUserLabels() {
		return userLabels;
	}

	public void setUserLabels(String userLabels) {
		this.userLabels = userLabels;
	}

	public HashMap<Integer, Integer> getCateCont() {
		return cateCont;
	}

	public void setCateCont(HashMap<Integer, Integer> cateCont) {
		this.cateCont = cateCont;
	}

	public HashMap<Integer, Double> getCateRatio() {
		return cateRatio;
	}

	public void setCateRatio(HashMap<Integer, Double> cateRatio) {
		this.cateRatio = cateRatio;
	}

	/**
	 * function : ���ӵ��̵��������Ϣ����
	 * 
	 * @param shop
	 */
	public void addUserShopping(Shop shop) {

		int classi = shop.getClassification();
		// ���������ǰ�����map
		if (cateCont.containsKey(classi)) {
			int cont = this.cateCont.get(classi);
			this.cateCont.put(classi, cont + 1);

			// �ܵ�������������1
			this.totalSample = this.totalSample + 1;

			// ���¸��������ռ����
			for (Integer key : cateRatio.keySet()) {
				double newRatio = cateCont.get(key) * 1.0 / totalSample;
				cateRatio.put(key, newRatio);
			}
		} else {
			this.totalSample = this.totalSample + 1;
			this.cateCont.put(classi, 1);
			this.cateRatio.put(classi, (double) 0);
			// ���¸��������ռ����
			for (Integer key : cateRatio.keySet()) {
				double newRatio = cateCont.get(key) * 1.0 / totalSample;
				cateRatio.put(key, newRatio);
			}
		}
	}

	/**
	 * function : ����ǩ����Ƿ������classi
	 * 
	 * @param classi
	 *            ����ǩ
	 * @return �Ƿ���
	 */
	public boolean containsClassi(int classi) {
		return cateCont.containsKey(classi);
	}

	/**
	 * function : ���ر���ǩ�п����б�Ϊ��Ӧ������б�
	 * 
	 * @param CATE_RATIO
	 * @return
	 */
	public ArrayList<Integer> getClassiList(double CATE_RATIO) {
		ArrayList<Integer> cateList = new ArrayList<Integer>();
		for (Integer cate : cateRatio.keySet()) {
			double ratio = cateRatio.get(cate);
			if (ratio > CATE_RATIO) {
				cateList.add(cate);
			}
		}
		return cateList;
	}
}
