package com.jackokie.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.jackokie.objects.Position;
import com.jackokie.objects.Shop;
import com.jackokie.objects.TestInfo;
import com.jackokie.objects.Trace;
import com.jackokie.objects.TrainInfo;
import com.jackokie.objects.User;

/**
 * @author jackokie E-mail: jackokie@qq.com
 * @version ����ʱ�䣺2016��11��26�� ����2:18:08 ��˵�� :
 */
public class CSVTools {

	/**
	 * function : ��ȡѵ������Ϣ
	 * 
	 * @param TRAIN_INFO_PATH
	 *            ѵ����·��
	 * @return ѵ������Ϣ
	 * @throws IOException
	 * @throws NumberFormatException
	 */
	public HashMap<String, TrainInfo> readTrainInfo(String TRAIN_INFO_PATH) throws NumberFormatException, IOException {
		BufferedReader bufferedReader = new BufferedReader(new FileReader(TRAIN_INFO_PATH));
		HashMap<String, TrainInfo> trainMap = new HashMap<String, TrainInfo>();

		String strTemp = null;
		String[] strSet = null;
		String userID = null;
		int income = 0;
		int entertainment = 0;
		;
		int babyLabel = 0;
		int gender = 0;
		int shopLabel = 0;
		String userLabels = null;
		int shopID = 0;
		String arriveTime = null;
		double userLong = 0;
		double userLat = 0;
		int duration = 0;
		double shopLong = 0;
		double shopLat = 0;
		Position userPos = null;
		Position shopPos = null;
		int shopClassi = 0;
		// ��ȡ���У����Ƿ�Ϊ��
		if ((strTemp = bufferedReader.readLine()) != null) {

			// ��ȡÿһ����Ϣ��������user����
			while ((strTemp = bufferedReader.readLine()) != null) {
				TrainInfo train = new TrainInfo();
				strSet = strTemp.split(",");
				userID = strSet[0];
				income = Integer.parseInt(strSet[1]);
				entertainment = Integer.parseInt(strSet[2]);
				babyLabel = Integer.parseInt(strSet[3]);
				gender = Integer.parseInt(strSet[4]);
				shopLabel = Integer.parseInt(strSet[5]);
				userLabels = strSet[1] + strSet[2] + strSet[3] + strSet[4] + strSet[5];

				shopID = Integer.parseInt(strSet[6]);
				arriveTime = strSet[7];
				userLong = Double.parseDouble(strSet[8]);
				userLat = Double.parseDouble(strSet[9]);
				userPos = new Position(userLong, userLat);
				duration = Integer.parseInt(strSet[10]);
				shopLong = Double.parseDouble(strSet[11]);
				shopLat = Double.parseDouble(strSet[12]);
				shopPos = new Position(shopLong, shopLat);
				shopClassi = Integer.parseInt(strSet[13]);

				train.setUserID(userID);
				train.setIncome(income);
				train.setEntertainment(entertainment);
				train.setBabyLabel(babyLabel);
				train.setGender(gender);
				train.setShopLabel(shopLabel);
				train.setUserLabels(userLabels);
				train.setShopID(shopID);
				train.setArriveTime(arriveTime);
				train.setUserPos(userPos);
				train.setDuration(duration);
				train.setShopPos(shopPos);
				train.setShopClassi(shopClassi);

				// ��ѵ����Ϣ����HashMap
				String key = userID + arriveTime;
				trainMap.put(key, train);
			}
		}
		bufferedReader.close();
		return trainMap;
	}

	/**
	 * function : ��ȡ���Լ�����
	 * 
	 * @param TEST_INFO_PATH
	 *            ���Լ�·��
	 * @return ���Լ�map����
	 * @throws IOException
	 */
	public HashMap<String, TestInfo> readTestInfo(String TEST_INFO_PATH) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new FileReader(TEST_INFO_PATH));
		HashMap<String, TestInfo> testMap = new HashMap<String, TestInfo>();

		String strTemp = null;
		String[] strSet = null;
		String userID = null;
		int income = 0;
		int entertainment = 0;
		;
		int babyLabel = 0;
		int gender = 0;
		int shopLabel = 0;
		String userLabels = null;
		String arriveTime = null;
		double userLong = 0;
		double userLat = 0;
		Position userPosition = null;
		int duration = 0;
		// ��ȡ���У����Ƿ�Ϊ��
		if ((strTemp = bufferedReader.readLine()) != null) {

			// ��ȡÿһ����Ϣ��������user����
			while ((strTemp = bufferedReader.readLine()) != null) {
				TestInfo test = new TestInfo();
				strSet = strTemp.split(",");
				userID = strSet[0];
				income = Integer.parseInt(strSet[1]);
				entertainment = Integer.parseInt(strSet[2]);
				babyLabel = Integer.parseInt(strSet[3]);
				gender = Integer.parseInt(strSet[4]);
				shopLabel = Integer.parseInt(strSet[5]);
				userLabels = strSet[1] + strSet[2] + strSet[3] + strSet[4] + strSet[5];

				arriveTime = strSet[6];
				userLong = Double.parseDouble(strSet[7]);
				userLat = Double.parseDouble(strSet[8]);
				userPosition = new Position(userLong, userLat);
				duration = Integer.parseInt(strSet[9]);

				test.setUserID(userID);
				test.setIncome(income);
				test.setEntertainment(entertainment);
				test.setBabyLabel(babyLabel);
				test.setGender(gender);
				test.setShopLabel(shopLabel);
				test.setUserLabels(userLabels);
				test.setArriveTime(arriveTime);
				test.setUserPos(userPosition);
				test.setDuration(duration);

				// ��ѵ����Ϣ����HashMap
				String key = userID + arriveTime;
				testMap.put(key, test);
			}
		}
		bufferedReader.close();
		return testMap;
	}

	/**
	 * function : ��ȡ��������
	 * 
	 * @param SHOP_PATH
	 *            �������ݼ�·��
	 * @return �������ݽṹ��Map����
	 * @throws IOException
	 */
	public HashMap<Integer, Shop> readShopData(String SHOP_PATH) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new FileReader(SHOP_PATH));
		HashMap<Integer, Shop> shopMap = new HashMap<Integer, Shop>();
		String strTemp = null;
		// ��ȡ���У����Ƿ�Ϊ��
		if ((strTemp = bufferedReader.readLine()) != null) {
			String[] strSet = null;
			int shopID = 0;
			String name = null;
			int classification = 0;
			double shopLong = 0;
			double shopLat = 0;
			Position shopPos = null;

			// ��ȡÿһ����Ϣ��������user����
			while ((strTemp = bufferedReader.readLine()) != null) {
				Shop shop = new Shop();
				strSet = strTemp.split(",");
				name = strSet[0];
				shopLong = Double.parseDouble(strSet[1]);
				shopLat = Double.parseDouble(strSet[2]);
				shopPos = new Position(shopLong, shopLat);
				classification = Integer.parseInt(strSet[3]);
				shopID = Integer.parseInt(strSet[4]);

				shop.setName(name);
				shop.setClassification(classification);
				shop.setShopID(shopID);
				shop.setPosition(shopPos);

				// ���̻�����HashMap
				shopMap.put(shopID, shop);
			}
		}
		bufferedReader.close();
		return shopMap;
	}

	/**
	 * function : ��������
	 * 
	 * @param list
	 *            �����б�
	 * @param filePath
	 *            �ļ�����Ŀ¼
	 * @throws IOException
	 */
	public static void saveCSV(ArrayList<String> list, String filePath) throws IOException {
		File csv = new File(filePath); // CSV�����ļ�
		if (csv.exists())
			csv.delete();
		BufferedWriter bw = new BufferedWriter(new FileWriter(csv, true));
		int listSize = list.size();
		for (int i = 0; i < listSize; i++) {
			bw.write(list.get(i));
			bw.newLine();
		}
		bw.close();
	}

	// ��ȡ�û��켣����
	public HashMap<String, Trace> readTraceData(String tracePath) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new FileReader(tracePath));
		HashMap<String, Trace> traceMap = new HashMap<String, Trace>();

		String strTemp = null;
		String[] strSet = null;
		String userID = null;
		String arriveTime = null;
		double userLong = 0;
		double userLat = 0;
		Position userPosition = null;
		int duration = 0;
		// ��ȡ���У����Ƿ�Ϊ��
		if ((strTemp = bufferedReader.readLine()) != null) {

			// ��ȡÿһ����Ϣ��������user����
			while ((strTemp = bufferedReader.readLine()) != null) {
				Trace trace = new Trace();
				strSet = strTemp.split(",");
				userID = strSet[0];
				arriveTime = strSet[1];
				userLong = Double.parseDouble(strSet[2]);
				userLat = Double.parseDouble(strSet[3]);
				userPosition = new Position(userLong, userLat);
				duration = Integer.parseInt(strSet[4]);

				trace.setUserID(userID);
				trace.setArriveTime(arriveTime);
				trace.setUserPos(userPosition);
				trace.setDuration(duration);

				// ���켣��Ϣ����HashMap
				String key = userID + arriveTime;
				traceMap.put(key, trace);
			}
		}
		bufferedReader.close();
		return traceMap;
	}

	/**
	 * ��ȡ�û�����
	 * 
	 * @param USER_PATH
	 *            �û��ļ�·��
	 * @return �û�����Map����
	 * @throws IOException 
	 */
	public HashMap<String, User> readUserData(String USER_PATH) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new FileReader(USER_PATH));
		HashMap<String, User> userMap = new HashMap<String, User>();

		String strTemp = null;
		String[] strSet = null;
		String userID = null;
		int income = 0;
		int entertainment = 0;
		int babyLabel = 0;
		int gender = 0;
		int shopLabel = 0;
		String userLabels = null;

		// ��ȡ���У����Ƿ�Ϊ��
		if ((strTemp = bufferedReader.readLine()) != null) {

			// ��ȡÿһ����Ϣ��������user����
			while ((strTemp = bufferedReader.readLine()) != null) {
				User user = new User();
				strSet = strTemp.split(",");
				userID = strSet[0];
				income = Integer.parseInt(strSet[1]);
				entertainment = Integer.parseInt(strSet[2]);
				babyLabel = Integer.parseInt(strSet[3]);
				gender = Integer.parseInt(strSet[4]);
				shopLabel = Integer.parseInt(strSet[5]);
				userLabels = strSet[1] + strSet[2] + strSet[3] + strSet[4] + strSet[5];

				user.setUserID(userID);
				user.setIncome(income);
				user.setEntertainment(entertainment);
				user.setBabyLabel(babyLabel);
				user.setGender(gender);
				user.setShopLabel(shopLabel);
				user.setUserLabels(userLabels);

				// ���û���Ϣ����HashMap
				userMap.put(userID, user);
			}
		}
		bufferedReader.close();
		return userMap;
	}

	public HashMap<String, TestInfo> readTestFinal(String TEST_INFO_PATH) throws NumberFormatException, IOException {
		BufferedReader bufferedReader = new BufferedReader(new FileReader(TEST_INFO_PATH));
		HashMap<String, TestInfo> testMap = new HashMap<String, TestInfo>();

		String strTemp = null;
		String[] strSet = null;
		String userID = null;
		int shopID = 0;
		String arriveTime = null;
		// ��ȡ���У����Ƿ�Ϊ��
		if ((strTemp = bufferedReader.readLine()) != null) {

			// ��ȡÿһ����Ϣ��������user����
			while ((strTemp = bufferedReader.readLine()) != null) {
				TestInfo test = new TestInfo();
				strSet = strTemp.split(",");
				userID = strSet[0];
				try {					
					shopID = Integer.parseInt(strSet[1]);
				} catch (Exception e) {
					shopID = 0;
				}
				arriveTime = strSet[2];

				test.setUserID(userID);
				test.setArriveTime(arriveTime);
				test.setShopID(shopID);
					// ��ѵ����Ϣ����HashMap
				String key = userID + arriveTime;
				testMap.put(key, test);
			}
		}
		bufferedReader.close();
		return testMap;
	}
}
