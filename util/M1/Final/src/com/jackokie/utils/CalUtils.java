package com.jackokie.utils;

import java.awt.RenderingHints.Key;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.TreeMap;

import javax.security.auth.kerberos.KerberosKey;

import java.util.HashMap;
import com.jackokie.objects.LabelsCategory;
import com.jackokie.objects.Position;
import com.jackokie.objects.Shop;
import com.jackokie.objects.TestInfo;
import com.jackokie.objects.Trace;
import com.jackokie.objects.TrainInfo;
import com.jackokie.objects.User;

/**
 * @author jackokie E-mail: jackokie@qq.com
 * @version ����ʱ�䣺2016��11��26�� ����2:18:25 ��˵�� :
 */
public class CalUtils {

	/**
	 * function : ��ȡ���̵ķ�����Ϣ
	 * 
	 * @param trainData
	 *            ѵ��������
	 * @param shopData
	 *            ��������
	 * @return
	 */
	public HashMap<String, LabelsCategory> getTrainClassi(HashMap<String, TrainInfo> trainData,
			HashMap<Integer, Shop> shopData) {
		HashMap<String, LabelsCategory> trainClassi = new HashMap<String, LabelsCategory>();
		// ���û���ǩ�ķ�����Ϣ���л�ȡ
		for (String trainKey : trainData.keySet()) {
			TrainInfo train = trainData.get(trainKey);
			String userLabels = train.getUserLabels();
			int shopID = train.getShopID();
			Shop shop = shopData.get(shopID);

			if (trainClassi.containsKey(userLabels)) {
				// �����а��������ǩ���
				LabelsCategory labelsCategory = trainClassi.get(userLabels);
				labelsCategory.addUserShopping(shop);
			} else {
				// �����в����д����ǩ���
				LabelsCategory labelsCategory = new LabelsCategory(userLabels);
				labelsCategory.addUserShopping(shop);
				trainClassi.put(userLabels, labelsCategory);
			}
		}

		return trainClassi;
	}

	/**
	 * function : �����û���Χ��ƥ���ѵ��������
	 * 
	 * @param trainData
	 *            ѵ��������
	 * @param testData
	 *            ���Լ�����
	 * @param shopData
	 *            ��������
	 * @return ����ƥ��������
	 */
	public void getMatchedTrace(HashMap<String, TrainInfo> trainData, HashMap<String, TestInfo> testData,
			HashMap<Integer, Shop> shopData, HashMap<String, LabelsCategory> classi, double DIF_LONG_LAT) {
		// OFFSET of distance
		for (String testKey : testData.keySet()) {
			// for each test info
			TestInfo test = testData.get(testKey);
			Position testPos = test.getUserPos();
			// get test to all the train dis which is smaller than OFFSET
			// train__pos ==== test_pos
			HashMap<String, Double> disTest2Shop = new HashMap<String, Double>();
			for (String trainKey : trainData.keySet()) {
				TrainInfo trainInfo = trainData.get(trainKey);
				int shopID = trainInfo.getShopID();
				Position trainUserPos = trainInfo.getUserPos();
				Position trainShopPos = trainInfo.getShopPos();
				// �û����ܳ��ֺü��Σ�����trainKeyֻ��һ�Σ� �洢�û��Ĺ켣��ѵ�����й켣�ĳ���
				if (testPos.near(trainUserPos, DIF_LONG_LAT) && testPos.inShopZone(trainShopPos)) {
					double dis = testPos.getDis(trainUserPos);
					if (dis < 10000) {						
						disTest2Shop.put(trainKey, dis);
					}
				}
			}
			test.setNearTrain(disTest2Shop);
		}
	}

	/**
	 * function :
	 * 
	 * @param trainData
	 * @param testData
	 * @param classi
	 * @param shopData
	 */
	public void matchNullTest(HashMap<String, TrainInfo> trainData, HashMap<String, TestInfo> testData,
			HashMap<String, LabelsCategory> trainClassi, HashMap<Integer, Shop> shopData, double CATE_RATIO,
			double DIS_RATIO_2, double DIS) {
		for (TestInfo test : testData.values()) {
			// ���shop_id��Ϊ0��������
			if (test.getShopID() != 0) {
				continue;
			}

			String userLabels = test.getUserLabels();
			Position testUserPos = test.getUserPos();

			HashMap<Integer, Double> nearShop = new HashMap<Integer, Double>();
			for (TrainInfo train : trainData.values()) {
				int shopID = train.getShopID();
				Position shopPos = train.getShopPos();

				double dis = shopPos.getDis(testUserPos);
				if (dis < DIS) {
					if (!nearShop.containsKey(shopID)) {
						nearShop.put(shopID, dis);
					} else {
						if (dis < nearShop.get(shopID)) {
							nearShop.put(shopID, dis);
						}
					}
				}
			}

			// �û�������Ϣ
			if (!trainClassi.containsKey(userLabels)) {
				continue;
			}

			LabelsCategory classi = trainClassi.get(userLabels);
			ArrayList<Integer> cateList = classi.getClassiList(CATE_RATIO);

			// ��ȡ������Сֵ
			double minDis = Double.MAX_VALUE;
			HashMap<Integer, Double> nearShopClassi = new HashMap<Integer, Double>();
			for (Integer shopID : nearShop.keySet()) {
				Shop shop = shopData.get(shopID);
				int shopClassi = shop.getClassification();
				double dis = nearShop.get(shopID);
				if (cateList.contains(shopClassi)) {
					// �õ���Χ���������Լ�labels��Ӧ�ĵ���
					nearShopClassi.put(shopID, dis);
					// ��ȡ��̵ĵ��̾���
					if (dis < minDis) {
						minDis = dis;
					}
				}
			}

			// �������̣�������ȶȵĵ��̽������
			int maxHeat = 0;
			int matchedShopID = 0;
			for (Integer shopID : nearShopClassi.keySet()) {
				Shop shop = shopData.get(shopID);
				if ((nearShop.get(shopID) - minDis) / minDis < DIS_RATIO_2) {
					int shopHeat = shop.getHeat();
					if (shopHeat > maxHeat) {
						maxHeat = shopHeat;
						matchedShopID = shopID;
					}
				}
			}
			test.setShopID(matchedShopID);
		}
	}

	/**
	 * function :
	 * 
	 * @param shopData
	 * @param testData
	 */
	public void matchNullRemain(HashMap<Integer, Shop> shopData, HashMap<String, TestInfo> testData) {
		// ����3KM����û�е��̵ģ�ѡȡ���������ĵ����Ƽ�
		for (TestInfo test : testData.values()) {
			if (test.getShopID() != 0) {
				continue;
			}
			Position testUserPos = test.getUserPos();
			double minDis = Double.MAX_VALUE;
			int matchedShopID = 0;
			for (Shop shop : shopData.values()) {
				Position shopPos = shop.getPosition();
				double dis = shopPos.getDis(testUserPos);

				if (dis < minDis) {
					minDis = dis;
					matchedShopID = shop.getShopID();
				}
			}

			test.setShopID(matchedShopID);
		}
	}

	/**
	 * function : ��ȡ����ַ�����ʽ
	 * 
	 * @param testData
	 * @return
	 */
	public static ArrayList<String> getAnswerStr(HashMap<String, TestInfo> testData) {
		// '''Get the answer string'''
		ArrayList<String> answerStr = new ArrayList<String>();
		answerStr.add("USERID,SHOPID,ARRIVAL_TIME");
		for (String key : testData.keySet()) {
			TestInfo testInfo = testData.get(key);
			String userID = testInfo.getUserID();
			String arriveTime = testInfo.getArriveTime();
			int shopID = testInfo.getShopID();
			String testStr = null;
			if (shopID == 0) {
				 testStr = userID + "," + "," + arriveTime;
			} else {
				testStr = userID + "," + shopID + "," + arriveTime + ",";
			}
			answerStr.add(testStr);
		}
		return answerStr;
	}

	/**
	 * function :
	 * 
	 * @param trainData
	 * @return
	 */
	public ArrayList<HashMap<String, TrainInfo>> splitTrainData(HashMap<String, TrainInfo> trainData) {
		// ѵ�����ݷֳ�ʮ��
		HashMap<String, TrainInfo> trainData0 = new HashMap<String, TrainInfo>();
		HashMap<String, TrainInfo> trainData1 = new HashMap<String, TrainInfo>();
		HashMap<String, TrainInfo> trainData2 = new HashMap<String, TrainInfo>();
		HashMap<String, TrainInfo> trainData3 = new HashMap<String, TrainInfo>();
		HashMap<String, TrainInfo> trainData4 = new HashMap<String, TrainInfo>();
		HashMap<String, TrainInfo> trainData5 = new HashMap<String, TrainInfo>();
		HashMap<String, TrainInfo> trainData6 = new HashMap<String, TrainInfo>();
		HashMap<String, TrainInfo> trainData7 = new HashMap<String, TrainInfo>();
		HashMap<String, TrainInfo> trainData8 = new HashMap<String, TrainInfo>();
		HashMap<String, TrainInfo> trainData9 = new HashMap<String, TrainInfo>();

		ArrayList<HashMap<String, TrainInfo>> splitList = new ArrayList<HashMap<String, TrainInfo>>();
		Random random = new Random(1000);
		for (String trainKey : trainData.keySet()) {
			TrainInfo trainInfo = trainData.get(trainKey);
			TrainInfo tempTrain = trainInfo.clone();
			int i = random.nextInt(10);
			switch (i) {
			case 0:
				trainData0.put(trainKey, tempTrain);
				break;
			case 1:
				trainData1.put(trainKey, tempTrain);
				break;
			case 2:
				trainData2.put(trainKey, tempTrain);
				break;
			case 3:
				trainData3.put(trainKey, tempTrain);
				break;
			case 4:
				trainData4.put(trainKey, tempTrain);
				break;
			case 5:
				trainData5.put(trainKey, tempTrain);
				break;
			case 6:
				trainData6.put(trainKey, tempTrain);
				break;
			case 7:
				trainData7.put(trainKey, tempTrain);
				break;
			case 8:
				trainData8.put(trainKey, tempTrain);
				break;
			case 9:
				trainData9.put(trainKey, tempTrain);
				break;
			}
		}

		splitList.add(trainData0);
		splitList.add(trainData1);
		splitList.add(trainData2);
		splitList.add(trainData3);
		splitList.add(trainData4);
		splitList.add(trainData5);
		splitList.add(trainData6);
		splitList.add(trainData7);
		splitList.add(trainData8);
		splitList.add(trainData9);

		return splitList;
	}

	/**
	 * function :
	 * 
	 * @param testInfoDataOrig
	 * @return
	 */
	public HashMap<String, TestInfo> convertTrain2Test(HashMap<String, TrainInfo> testInfoDataOrig) {
		HashMap<String, TestInfo> testInfoData = new HashMap<String, TestInfo>();
		for (String key : testInfoDataOrig.keySet()) {
			TrainInfo trainInfo = testInfoDataOrig.get(key);
			TestInfo testInfo = new TestInfo();
			testInfo.setArriveTime(trainInfo.getArriveTime());
			testInfo.setBabyLabel(trainInfo.getBabyLabel());
			testInfo.setDuration(trainInfo.getDuration());
			testInfo.setEntertainment(trainInfo.getEntertainment());
			testInfo.setGender(trainInfo.getGender());
			testInfo.setIncome(trainInfo.getIncome());
			testInfo.setShopID(0);
			testInfo.setShopLabel(trainInfo.getShopLabel());
			testInfo.setUserID(trainInfo.getUserID());
			testInfo.setUserLabels(trainInfo.getUserLabels());
			testInfo.setUserPos(trainInfo.getUserPos());
			testInfoData.put(key, testInfo);
		}
		return testInfoData;
	}

	/**
	 * function : ��ȡԤ���׼ȷ��
	 * 
	 * @param testInfoData
	 * @param trainData
	 * @return
	 */
	public double getAccuracy(HashMap<String, TestInfo> testInfoData, HashMap<String, TrainInfo> trainData) {
		int cont = testInfoData.size();
		int num = 0;
		for (String key : testInfoData.keySet()) {
			TestInfo test = testInfoData.get(key);
			TrainInfo train = trainData.get(key);
			if (test.getShopID() == train.getShopID()) {
				num = num + 1;
			}
		}
		double accuracy = num * 1.0 / cont;
		return accuracy;
	}

	/**
	 * function : ������ƽ��ֵ
	 * 
	 * @param accuracy
	 *            ��������
	 * @return ����ƽ��ֵ
	 */
	public double getMean(double[] accuracy) {
		double sum = 0;
		int len = accuracy.length;
		for (int i = 0; i < len; i++) {
			sum += accuracy[i];
		}
		return sum / len;
	}

	public ArrayList<String> getAccuracyList(ArrayList<Double[]> totalAccuracy) {
		// DIF_LONG_LAT DIS_RATIO_1 DIS_RATIO_2 CATE_RATIO DIS ACCURACY
		ArrayList<String> answerStr = new ArrayList<String>();
		answerStr.add("DIF_LONG_LAT,DIS_RATIO_1,DIS_RATIO_2,CATE_RATIO,DIS,ACCURACY");
		for (Double[] accuracy : totalAccuracy) {
			String testStr = accuracy[0] + "," + accuracy[1] + "," + accuracy[2] + "," + accuracy[3] + "," + accuracy[4]
					+ "," + accuracy[5];
			answerStr.add(testStr);
		}
		return answerStr;
	}

	// ��������ת��Ϊʱ���ʽ
	public String timeConvert(long l) {
		Date date = new Date(l);
		SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
		return format.format(date);
	}

	/**
	 * ѵ������ƥ�䵽������Ϣ
	 * 
	 * @param shopData
	 * @param trainData
	 */
	public void shopMatchTrain(HashMap<Integer, Shop> shopData, HashMap<String, TrainInfo> trainData) {
		for (TrainInfo train : trainData.values()) {
			int shopID = train.getShopID();
			Shop shop = shopData.get(shopID);
			shop.addUserInfo(train);
		}
	}

	/**
	 * ѵ������ƥ�䵽�û���Ϣ
	 * 
	 * @param userData
	 * @param trainData
	 */
	public void userMatchTrain(HashMap<String, User> userData, HashMap<String, TrainInfo> trainData) {
		for (TrainInfo train : trainData.values()) {
			String userID = train.getUserID();
			User user = userData.get(userID);
			user.addShopInfo(train);
		}
	}

	/**
	 * �û��켣ƥ�䵽�û���Ϣ
	 * 
	 * @param userData
	 * @param traceData
	 */
	public void userMatchTrace(HashMap<String, User> userData, HashMap<String, Trace> traceData) {
		for (Trace trace : traceData.values()) {
			String userID = trace.getUserID();
			User user = userData.get(userID);
			user.addTrace(trace);
		}
	}

	/**
	 * ���û�����̽���ƥ�䣬�ֱ�����û����漰���е����Լ������������е��û�
	 * 
	 * @param testData
	 * @param userData
	 * @param shopData
	 */
	public void userMatchShop(HashMap<String, TestInfo> testData, HashMap<String, User> userData,
			HashMap<Integer, Shop> shopData) {

	}

	/**
	 * ��ȡѵ�����еĵ��̼���
	 * 
	 * @param trainData
	 * @param shopData
	 * @return
	 */
	public HashMap<Integer, Shop> getTrainShop(HashMap<String, TrainInfo> trainData, HashMap<Integer, Shop> shopData) {
		HashMap<Integer, Shop> trainShopMap = new HashMap<Integer, Shop>();
		for (TrainInfo train : trainData.values()) {
			int shopID = train.getShopID();
			if (!trainShopMap.containsKey(shopID)) {
				trainShopMap.put(shopID, shopData.get(shopID));
			}
		}
		return trainShopMap;
	}

	/**
	 * function : �����û�������Լ�λ�ã������Ƽ�
	 * 
	 * @param trainData
	 * @param testData
	 * @param classi
	 * @param shopData
	 */
	public void matchByClassi(HashMap<String, TrainInfo> trainData, HashMap<String, TestInfo> testData,
			HashMap<String, LabelsCategory> trainClassi, HashMap<Integer, Shop> shopData, double CATE_RATIO,
			double DIS_RATIO_1) {
		// CATE_RATIO �û������ѡȡ > CATE_RATIO �ſ����Ƽ������, Ĭ����0.3
		// DIS_RATIO_1 = 0.1; �û��ж�����λ����ѵ��λ�þ�������С�ľ����ֵ
		for (TestInfo test : testData.values()) {
			if (test.getShopID() != 0) {
				continue;
			}
			// get the user position
			Position testPos = test.getUserPos();
			String userLabels = test.getUserLabels();

			// ��ȡ�����ѵ���켣
			HashMap<String, Double> nearTrain = test.getNearTrain();

			// �����Χû����ͬ�ĵ���
			if (nearTrain.size() == 0) {
				continue;
			} else {
				// �û�������Ϣ
				if (!trainClassi.containsKey(userLabels)) {
					continue;
				}
				LabelsCategory classi = trainClassi.get(userLabels);
				ArrayList<Integer> cateList = classi.getClassiList(CATE_RATIO);
				double minDis = Double.MAX_VALUE;

				// ��ʾ��Χ�����뱾��������������ͬ
				HashMap<String, Double> nearTrainClassi = new HashMap<>();
				for (String key : nearTrain.keySet()) {
					TrainInfo trainInfo = trainData.get(key);
					Position shopPos = trainInfo.getShopPos();
					double testShopDis = testPos.getDis(shopPos);
					int shopClassi = trainInfo.getShopClassi();
					if (cateList.contains(shopClassi)) {
						nearTrainClassi.put(key, nearTrain.get(key));
						if (testShopDis < minDis) {
							minDis = testShopDis;
						}
					}
				}

				if (minDis < Math.pow(10, -6)) {
					minDis = Math.pow(10, -7);
				}

				// ͳ�ƴ˵�����ѵ�����г��ֵĴ���
				HashMap<Integer, Integer> temp = new HashMap<Integer, Integer>();
				for (String key : nearTrainClassi.keySet()) {
					TrainInfo trainInfo = trainData.get(key);
					double test2trainDis = nearTrainClassi.get(key);
					if ((test2trainDis - minDis) / minDis < DIS_RATIO_1) {
						int shopID = trainInfo.getShopID();
						if (temp.containsKey(shopID)) {
							temp.put(shopID, temp.get(shopID) + 1);
						} else {
							temp.put(shopID, 1);
						}
					}
				}

				double max = 0;
				int shopID = 0;
				for (Integer shopKey : temp.keySet()) {
					int cont = temp.get(shopKey);
					if (cont > max) {
						max = cont;
						shopID = shopKey;
					}
				}
				test.setShopID(shopID);
			}

			if ((test.getShopID() == 0) && (nearTrain.size() != 0)) {
				// ѡȡ�����һ������
				String trainKey = getMin(nearTrain);
				int shopID = trainData.get(trainKey).getShopID();
				test.setShopID(shopID);
			}
		}
	}

	private String getMin(HashMap<String, Double> nearTrain) {
		double minDis = Double.MAX_VALUE;
		String trainKey = null;
		for (String key : nearTrain.keySet()) {
			double tempDis = nearTrain.get(key);
			if (tempDis < minDis) {
				minDis = tempDis;
				trainKey = key;
			}
		}
		return trainKey;
	}

	public void matchByID(HashMap<String, TrainInfo> trainData, HashMap<String, TestInfo> testData,
			HashMap<String, LabelsCategory> classi, HashMap<Integer, Shop> shopData, double DIF_LONG_LAT) {
		for (TestInfo test : testData.values()) {
			if (test.getShopID() != 0) {
				continue;
			}
			String userID = test.getUserID();
			// get the user position
			Position testPos = test.getUserPos();

			// ����ÿһ��ѵ���켣������û�id��ͬ�����ң���γ����������������򽫸õ����Ƽ����û�
			// ?????????????????????????????????????????????????????????????????????????????????
			HashMap<Integer, Integer> samePosShop = new HashMap<Integer, Integer>(); // ���û���ͬλ�õĵ��̼�¼
			for (TrainInfo train : trainData.values()) {
				String trainUserID = train.getUserID();
				Position trainUserPos = train.getUserPos();
				int shopID = train.getShopID();
				if (trainUserID.equals(userID) && (trainUserPos.near(testPos, DIF_LONG_LAT))) {
					if (samePosShop.containsKey(shopID)) {
						int cont = samePosShop.get(shopID);
						samePosShop.put(shopID, cont + 1);
					} else {
						samePosShop.put(shopID, 1);
					}
				}
			}

			// ====================�Ƽ�=================================
			int matchedShopID = 0;
			// �����ͬ�û���ѵ������û��λ����ͬ�ļ�¼
			if (samePosShop.size() == 0) {
				continue;
			}
			// ���ֻ��һ�����̣���ô�ͽ����Ƽ�
			if (samePosShop.size() == 1) {
				matchedShopID = samePosShop.keySet().iterator().next();
				test.setShopID(matchedShopID);
				continue;
			}
			// ����ж���λ����ͬ�ļ�¼��ѡȡ���ִ�������
			if (samePosShop.size() > 1) {
				int maxCont = 0;
				for (Integer shopID : samePosShop.keySet()) {
					int shopCont = samePosShop.get(shopID);
					if (shopCont > maxCont) {
						maxCont = shopCont;
						matchedShopID = shopID;
					}
				}
				test.setShopID(matchedShopID);
				continue;
			}
		}
	}


}
