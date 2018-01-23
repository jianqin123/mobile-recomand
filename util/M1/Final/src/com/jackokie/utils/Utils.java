package com.jackokie.utils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.omg.CORBA.OMGVMCID;

import com.jackokie.objects.LabelsCategory;
import com.jackokie.objects.Position;
import com.jackokie.objects.Shop;
import com.jackokie.objects.TestInfo;
import com.jackokie.objects.Trace;
import com.jackokie.objects.TrainInfo;
import com.jackokie.objects.User;
import com.jackokie.objects.Zone;

public class Utils {

	/**
	 * ��ȡѵ�������г��ֵ��û�
	 * 
	 * @param trainData
	 * @param userData
	 * @return
	 */
	public static HashMap<String, User> getTrainUser(HashMap<String, TrainInfo> trainData,
			HashMap<String, User> userData) {
		HashMap<String, User> trainUser = new HashMap<String, User>();
		for (TrainInfo train : trainData.values()) {
			String userID = train.getUserID();
			if (!trainUser.containsKey(userID)) {
				trainUser.put(userID, userData.get(userID));
			}
		}
		return trainUser;
	}

	// �����û�
	public static HashMap<String, User> copyUserData(HashMap<String, User> userData) {
		HashMap<String, User> copyUsers = new HashMap<String, User>();
		for (String userID : userData.keySet()) {
			User user = userData.get(userID);
			User newUser = new User();
			newUser.setUserID(userID);
			newUser.setIncome(user.getIncome());
			newUser.setEntertainment(user.getEntertainment());
			newUser.setBabyLabel(user.getBabyLabel());
			newUser.setGender(user.getGender());
			newUser.setShopLabel(user.getShopLabel());
			newUser.setUserLabels(user.getUserLabels());

			copyUsers.put(userID, newUser);
		}

		return copyUsers;
	}

	// ͨ���û�λ���Ƽ�
	public static void recommendByPos(HashMap<String, TrainInfo> trainInfoData, HashMap<String, TestInfo> testInfoData,
			HashMap<Integer, Shop> shopData, HashMap<String, User> userDataCopy,
			HashMap<String, LabelsCategory> classi) {
		// ����λ�õ��Ƽ�

		HashMap<String, HashMap<Integer, Double>> integrateShopMap = new HashMap<String, HashMap<Integer, Double>>();
		for (String testKey : testInfoData.keySet()) {
			TestInfo test = testInfoData.get(testKey);
			HashMap<Integer, Double> confShops = test.getUserConferenceShop();
			HashMap<String, Double> dis2TrainTrace = test.getNearTrain();
			// �ں��Ժ��û�������̵ľ���
			HashMap<Integer, Double> inteShop = getIntegratedShops(confShops, dis2TrainTrace, trainInfoData);
			integrateShopMap.put(testKey, inteShop);
		}

		for (String testKey : integrateShopMap.keySet()) {
			if (testInfoData.get(testKey).getShopID() != 0) {
				continue;
			}

			HashMap<Integer, Double> integrateShops = integrateShopMap.get(testKey);
			TestInfo test = testInfoData.get(testKey);
			User user = userDataCopy.get(test.getUserID());

			// �������û�н���
			if (integrateShops.size() == 0) {
				if (test.getUserConferenceShop().size() != 0) {
					HashMap<Integer, Double> shopMaps = test.getUserConferenceShop();
					HashMap<Integer, Double> shopAttendMaps = new HashMap<Integer, Double>();
					// ��ȡ���Լ�������ʷ�еĹ������������������ͬ�ĵ���
					for (Integer shopID : shopMaps.keySet()) {
						int cate = shopData.get(shopID).getClassification();
						if (user.getCateAttend().containsKey(cate)) {
							shopAttendMaps.put(shopID, shopMaps.get(shopID));
						}
					}
					// ����û�����ļ�¼��Ϊ��
					if (shopAttendMaps.size() != 0) {
						// �û��Ĺ���������
						HashMap<Integer, Double> cateAtten = user.getCateAttend();
						HashMap<Integer, Double> shopScore = new HashMap<Integer, Double>();
						for (Integer shopID : shopAttendMaps.keySet()) {
							double attend = cateAtten.get(shopData.get(shopID).getClassification());
							int heat = shopData.get(shopID).getHeat();
							double dis = shopAttendMaps.get(shopID);
							double score = heat / Math.log(dis);
							shopScore.put(shopID, score);
						}
						// Ѱ�ҵ÷����ĵ���
						double maxScore = 0;
						int matchedShop = 0;
						for (Integer shopID : shopScore.keySet()) {
							if (shopScore.get(shopID) > maxScore) {
								maxScore = shopScore.get(shopID);
								matchedShop = shopID;
							}
						}
						test.setShopID(matchedShop);
						continue;
					} else {
						// ����û�����ļ�¼Ϊ��========�����û��������Զ���

						// ���û������Ѱ��
						if (classi.containsKey(user.getUserLabels())) {
							// ����û�����к��иñ�ǩ���

							LabelsCategory category = classi.get(user.getUserLabels());
							HashMap<Integer, Double> cateRatio = category.getCateRatio();

							// ��ȡ���Լ�������ʷ�еĹ������������������ͬ�ĵ���
							for (Integer shopID : shopMaps.keySet()) {
								int cate = shopData.get(shopID).getClassification();
								if (cateRatio.containsKey(cate)) {
									shopAttendMaps.put(shopID, shopMaps.get(shopID));
								}
							}
							// �����û����������Զ���=============
							// ����û�����ļ�¼��Ϊ��
							if (shopAttendMaps.size() != 0) {
								// �û��Ĺ���������
								HashMap<Integer, Double> shopScore = new HashMap<Integer, Double>();
								for (Integer shopID : shopAttendMaps.keySet()) {
									double attend = cateRatio.get(shopData.get(shopID).getClassification());
									int heat = shopData.get(shopID).getHeat();
									double dis = shopAttendMaps.get(shopID);
									double score = heat / Math.log(dis);
									shopScore.put(shopID, score);
								}
								// Ѱ�ҵ÷����ĵ���
								double maxScore = 0;
								int matchedShop = 0;
								for (Integer shopID : shopScore.keySet()) {
									if (shopScore.get(shopID) > maxScore) {
										maxScore = shopScore.get(shopID);
										matchedShop = shopID;
									}
								}
								test.setShopID(matchedShop);
								continue;
							} else {
								// ================��������û������ԣ��û������Ե��̻�δ��========
								continue;
							}
						} else {
							// �û���ǩ����в����иñ�ǩ���---�����û�Ϊ�µı�ǩ���, ��ʱ�����Ƽ�
							continue;
						}
					}
				} else if (test.getUserConferenceShop().size() != 0) {// ???????????????????????????????????????????????????????????????????????????????????????
					// ��ʱ��ʾ�̻��ĵ���λ���Ƽ���Ϊ�գ����ǽ���Ϊ��
					// ???????????????????????????????????????������հ�
				}
			} else {
				// =============���������Ϊ��=================
				HashMap<Integer, Double> shopMaps = integrateShops;
				HashMap<Integer, Double> shopAttendMaps = new HashMap<Integer, Double>();
				// ��ȡ���Լ�������ʷ�еĹ������������������ͬ�ĵ���
				for (Integer shopID : shopMaps.keySet()) {
					int cate = shopData.get(shopID).getClassification();
					if (user.getCateAttend().containsKey(cate)) {
						shopAttendMaps.put(shopID, shopMaps.get(shopID));
					}
				}
				// ����û�����ļ�¼��Ϊ��
				if (shopAttendMaps.size() != 0) {
					// �û��Ĺ���������
					HashMap<Integer, Double> cateAtten = user.getCateAttend();
					HashMap<Integer, Double> shopScore = new HashMap<Integer, Double>();
					for (Integer shopID : shopAttendMaps.keySet()) {
						double attend = cateAtten.get(shopData.get(shopID).getClassification());
						int heat = shopData.get(shopID).getHeat();
						double dis = shopAttendMaps.get(shopID);
						double score = heat / Math.log(dis);
						shopScore.put(shopID, score);
					}
					// Ѱ�ҵ÷����ĵ���
					double maxScore = 0;
					int matchedShop = 0;
					for (Integer shopID : shopScore.keySet()) {
						if (shopScore.get(shopID) > maxScore) {
							maxScore = shopScore.get(shopID);
							matchedShop = shopID;
						}
					}
					test.setShopID(matchedShop);
					continue;
				} else {
					// ����û�����ļ�¼Ϊ��========�����û��������Զ���

					// ���û������Ѱ��
					if (classi.containsKey(user.getUserLabels())) {
						// ����û�����к��иñ�ǩ���

						LabelsCategory category = classi.get(user.getUserLabels());
						HashMap<Integer, Double> cateRatio = category.getCateRatio();

						// ��ȡ���Լ�������ʷ�еĹ������������������ͬ�ĵ���
						for (Integer shopID : shopMaps.keySet()) {
							int cate = shopData.get(shopID).getClassification();
							if (cateRatio.containsKey(cate)) {
								shopAttendMaps.put(shopID, shopMaps.get(shopID));
							}
						}
						// �����û����������Զ���=============
						// ����û�����ļ�¼��Ϊ��
						if (shopAttendMaps.size() != 0) {
							// �û��Ĺ���������
							HashMap<Integer, Double> shopScore = new HashMap<Integer, Double>();
							for (Integer shopID : shopAttendMaps.keySet()) {
								double attend = cateRatio.get(shopData.get(shopID).getClassification());
								int heat = shopData.get(shopID).getHeat();
								double dis = shopAttendMaps.get(shopID);
								double score = heat / Math.log(dis);
								shopScore.put(shopID, score);
							}
							// Ѱ�ҵ÷����ĵ���
							double maxScore = 0;
							int matchedShop = 0;
							for (Integer shopID : shopScore.keySet()) {
								if (shopScore.get(shopID) > maxScore) {
									maxScore = shopScore.get(shopID);
									matchedShop = shopID;
								}
							}
							test.setShopID(matchedShop);
							continue;
						} else {
							// ================��������û������ԣ��û������Ե��̻�Ϊ��========
							// ��ʱ��ֻ�ܲ鿴�û����漰����̣� ���û���ѵ����¼���벻�ڽ����е��ǲ��ּ�¼��Ȼ����ж��μ���
							continue;
							// ???????????????????????????????????????????????????????????????????????????????????????
						}
					} else {
						// �û���ǩ����в����иñ�ǩ���---�����û�Ϊ�µı�ǩ���, ��ʱ�����Ƽ�
						// ???????????????????????????????????????????????????????????????????????????????????????
						// ��Ϊ��ǰ�Ѿ��������ǵ��ں���������Ƽ������������δ�����Ƽ������Ҵ�ʱ������Щ�µ��û���ǩ�����ѵ�����в�δ����
						// ��ô�ͽ��н�һ�����Ƽ����������ձ��û�����ʷ��Ϣ���ٲ��У��Ͱ�����Χ���̵��ȶ��Ƽ�
						continue;
					}
				}
			}
		}
	}

	private static HashMap<Integer, Double> getIntegratedShops(HashMap<Integer, Double> confShops,
			HashMap<String, Double> dis2TrainTrace, HashMap<String, TrainInfo> trainInfoData) {
		HashMap<Integer, Double> inteShop = new HashMap<Integer, Double>();
		int shopID = 0;
		for (String trainKey : dis2TrainTrace.keySet()) {
			shopID = trainInfoData.get(trainKey).getShopID();
			if (confShops.containsKey(shopID)) {
				if (!inteShop.containsKey(shopID)) {
					inteShop.put(shopID, confShops.get(shopID));
				}
			}
		}
		return inteShop;
	}

	// 0. ������Щ0.1��Χ��û�е��̵��û������Ƽ�����Ϊ��������ֵ����Ĳ�����������ĵ���
	public static void recommendNoShop(HashMap<String, TestInfo> testData, HashMap<String, TrainInfo> trainData,
			HashMap<String, LabelsCategory> classi, HashMap<Integer, Shop> shopData) {
		for (TestInfo test : testData.values()) {
			if (test.getShopID() != 0) {
				continue;
			}
			int cont = 0;
			Position testPos = test.getUserPos();
			LabelsCategory cate = classi.get(test.getUserLabels());
			for (Shop shop : shopData.values()) {
				Position shopPos = shop.getPosition();
				// ��γ���������0.05����û�е��̣����Ƽ�����ĵ���
				if (testPos.upNear(shopPos, 0.05)) {
					cont++;
				}
			}
			// �����Χû�е���
			if (cont == 0) {
				int shopID = getUpNearest(test, shopData, cate);
				test.setShopID(shopID);
			}
		}
	}

	private static int getUpNearest(TestInfo test, HashMap<Integer, Shop> shopData, LabelsCategory cate) {
		Position testPos = test.getUserPos();
		double minDis = Double.MAX_VALUE;
		int matchedShopID = 0;
		for (Shop shop : shopData.values()) {
			Position shopPos = shop.getPosition();
			Double dis = testPos.getDis(shopPos);
			// ����Ϊ��С�� ���������������
			if (dis < minDis && cate.getClassiList(0.3).contains(shop.getClassification())) {
				minDis = dis;
				matchedShopID = shop.getShopID();
			}
		}
		return matchedShopID;
	}

	// 1. �����û������ڼ��㣬������������һ���ܣ������û���λ����ͬ�����Ƽ�ͬһ�ҵ���
	public static void recommendByWeek(HashMap<String, User> userData, HashMap<String, TestInfo> testData,
			HashMap<String, TrainInfo> trainData) {
		for (TestInfo test : testData.values()) {
			if (test.getShopID() != 0) {
				continue;
			}

			User user = userData.get(test.getUserID());
			Position userPosition = test.getUserPos();
			String predictTime = test.getArriveTime();
			int predictDay = Integer.parseInt(predictTime.substring(6, 8));
			int predictHour = Integer.parseInt(predictTime.substring(8, 10));

			HashMap<String, Integer> shopHis = user.getShopHis();
			for (String time : shopHis.keySet()) {
				int shopID = shopHis.get(time);
				String key = user.getUserID() + time;
				TrainInfo trainInfo = trainData.get(key);
				Position trainPos = trainInfo.getUserPos();
				int trainDay = Integer.parseInt(trainInfo.getArriveTime().substring(6, 8));
				int trainHour = Integer.parseInt(trainInfo.getArriveTime().substring(8, 10));
				// ѵ�����û���Ԥ��λ�����
				if (trainPos.equals(userPosition)) {

					// ����Сʱ�Ĺ�ϵ1
					int defHour = Math.abs(trainHour - predictHour);
					if (defHour <= 1 && trainDay <= 5) {
						test.setShopID(shopID);
						break;
					}

					// ����������ڵĹ�ϵ
					// if ((Math.abs(trainDay - predictDay) == 7 ||
					// Math.abs(trainDay - predictDay) == 14)
					// && (defHour <= 2 || Math.abs(defHour - 6) <= 1)) {
					// test.setShopID(shopID);
					// break;
					// }

					// ����Сʱ��ϵ2
					// if (Math.abs(defHour - 6) <= 1) {
					// test.setShopID(shopID);
					// break;
					// }
				}
			}
		}
	}

	// 2. ��ͬ�û�����ͬλ�õ��Ƽ�
	public static void recommendDifUserPos(TreeMap<Position, ArrayList<TrainInfo>> posMap,
			HashMap<String, TestInfo> testData, HashMap<String, User> userData, HashMap<Integer, Shop> shopData,
			HashMap<String, LabelsCategory> classi) {
		// �д�Լ110������
		for (TestInfo test : testData.values()) {
			if (test.getShopID() != 0) {
				continue;
			}
			Position userPos = test.getUserPos();
			ArrayList<TrainInfo> trainList = posMap.get(userPos);
			if (trainList == null) {
				continue;
			}
			// ��λ�õĵ���ͳ��
			HashMap<Integer, Integer> shopStat = new HashMap<Integer, Integer>();
			ArrayList<TrainInfo> sameLabelTrain = getSameLabelTrain(test, trainList);
			for (TrainInfo train : trainList) {
				int shopID = train.getShopID();
				if (shopStat.containsKey(shopID)) {
					shopStat.put(shopID, shopStat.get(shopID) + 1);
				} else {
					shopStat.put(shopID, 1);
				}
			}
			LabelsCategory category = classi.get(test.getUserLabels());
			ArrayList<Integer> cateList = category.getClassiList(0.3);
			// ���ֻ��һ�ҵ���
			if (shopStat.size() == 1) {
				for (TrainInfo train : trainList) {
					int shopID = train.getShopID();
					String testTime = test.getArriveTime();
					String trainTime = train.getArriveTime();
					int testHour = Integer.parseInt(testTime.substring(8, 10));
					int trainHour = Integer.parseInt(trainTime.substring(8, 10));
					int defHour = Math.abs(testHour - trainHour);
					int testDay = Integer.parseInt(testTime.substring(6, 8));
					int trainDay = Integer.parseInt(trainTime.substring(6, 8));
					int defDay = Math.abs(testDay - trainDay);
					int testDur = test.getDuration();
					int trainDur = train.getDuration();
					if (cateList.contains(shopData.get(shopID).getClassification()) && defDay <= 3
							&& Math.abs(testDur - trainDur) <= 3) {
						test.setShopID(shopID);
						continue;
					}
				}
			} else {
				// ����кü��ҵ���

			}

		}
	}

	// 3. ����ͬһ�û��� ��������û���ѵ���Ķ�λ�����С��0.01�� ������Ϊͬһλ�ã���ʱ�Ƽ���ͬ�ĵ���
	public static void recommendSameUserDifPos(HashMap<String, TrainInfo> trainData, HashMap<String, TestInfo> testData, 
			HashMap<String, User> userData, double dif_same_shop, double DIF) {
		for (TestInfo test : testData.values()) {
			if (test.getShopID() != 0) {
				continue;
			}
			User user = userData.get(test.getUserID());
			int userID = Integer.parseInt(user.getUserID());
			HashMap<String, Integer> shopHis = user.getShopHis();
			Position userPosition = test.getUserPos();
			for (String time : shopHis.keySet()) {
				TrainInfo trainInfo = trainData.get(userID + time);
				if (trainInfo.getUserPos().near(userPosition, dif_same_shop)) {
					if (test.getUserPos().getDis(trainInfo.getShopPos()) < DIF) {
						test.setShopID(trainInfo.getShopID());
						break;
					}
				}
			}
		}
	}

	// 4. �����û�������Լ�λ�ã������Ƽ�
	public static void recommendByClassi(HashMap<String, TrainInfo> trainData, HashMap<String, TestInfo> testData,
			HashMap<String, LabelsCategory> trainClassi, HashMap<Integer, Shop> shopData, double CATE_RATIO,
			double DIS_RATIO_1, double DIF) {
		for (TestInfo test : testData.values()) {
			if (test.getShopID() != 0) {
				continue;
			}
			Position testUserPos = test.getUserPos();
			String userLabels = test.getUserLabels();

			// ��ȡ�����ѵ���켣
			HashMap<String, Double> nearTrain = test.getNearTrain();
			// �Ե��̽��й���
			// �Ե�����ǩ���Ż�
			if (userLabels.equals("21122")) {
				// ֻ����1�����
				Iterator<String> iterator = nearTrain.keySet().iterator();
				while (iterator.hasNext()) {
					String trainKey = iterator.next();
					TrainInfo train = trainData.get(trainKey);
					Shop shop = shopData.get(train.getShopID());
					HashMap<String, Integer> labelsCont = shop.getLabelsCont();
					if (train.getShopClassi() != 1 || (!labelsCont.containsKey(userLabels))) {
						iterator.remove();
					}
				}
			} else if (userLabels.equals("11121")) {
				// ֻ����3�����
				Iterator<String> iterator = nearTrain.keySet().iterator();
				while (iterator.hasNext()) {
					String trainKey = iterator.next();
					TrainInfo train = trainData.get(trainKey);
					Shop shop = shopData.get(train.getShopID());
					HashMap<String, Integer> labelsCont = shop.getLabelsCont();
					if (train.getShopClassi() != 3 || (!labelsCont.containsKey(userLabels))) {
						iterator.remove();
					}
				}
			} else if (userLabels.equals("27015")) {
				// ֻ����5�����
				Iterator<String> iterator = nearTrain.keySet().iterator();
				while (iterator.hasNext()) {
					String trainKey = iterator.next();
					TrainInfo train = trainData.get(trainKey);
					Shop shop = shopData.get(train.getShopID());
					HashMap<String, Integer> labelsCont = shop.getLabelsCont();
					if (train.getShopClassi() != 5 || (!labelsCont.containsKey(userLabels))) {
						iterator.remove();
					}
				}
			} else if (userLabels.equals("12115")) {
				// ֻ����5�����
				Iterator<String> iterator = nearTrain.keySet().iterator();
				while (iterator.hasNext()) {
					String trainKey = iterator.next();
					TrainInfo train = trainData.get(trainKey);
					Shop shop = shopData.get(train.getShopID());
					HashMap<String, Integer> labelsCont = shop.getLabelsCont();
					if ((train.getShopClassi() != 1 && train.getShopClassi() != 8) || (!labelsCont.containsKey(userLabels))) {
						iterator.remove();
					}
				}
			} else {
				// �������е�ѵ�����ݣ������ѵ����������Ӧ�ĵ��̣�������ʷ���У������е�UserLables��ϲ����ڸ��û���userLabels���򽫸õ��̴Ӹ��û����Ƽ��б����Ƴ�
				Iterator<String> iterator = nearTrain.keySet().iterator();
				while (iterator.hasNext()) {
					String trainKey = iterator.next();
					TrainInfo train = trainData.get(trainKey);
					Shop shop = shopData.get(train.getShopID());
					HashMap<String, Integer> labelsCont = shop.getLabelsCont();
					if (!labelsCont.containsKey(userLabels)) {
						iterator.remove();
					}
				}
			}

			// �����Χû����ͬ�ĵ���
			if (nearTrain.size() == 0) {
				continue;
			}
			// �û�������Ϣ
			if (!trainClassi.containsKey(userLabels)) {
				continue;
			}
			LabelsCategory category = trainClassi.get(userLabels);
			ArrayList<Integer> cateList = category.getClassiList(CATE_RATIO);
			double minDis = Double.MAX_VALUE;

			// ��ʾ��Χ�����뱾��������������ͬ
			HashMap<String, Double> nearTrainClassi = new HashMap<>();
			for (String key : nearTrain.keySet()) {
				TrainInfo trainInfo = trainData.get(key);
				Position shopPos = trainInfo.getShopPos();
				double test2ShopDis = testUserPos.getDis(shopPos);
				int shopClassi = trainInfo.getShopClassi();

				if (cateList.contains(shopClassi) && testUserPos.inShopZone(shopPos)) {
					nearTrainClassi.put(key, nearTrain.get(key));
					if (test2ShopDis < minDis) {
						minDis = test2ShopDis;
					}
				}
			}

			// ͳ�ƴ˵�����ѵ�����г��ֵĴ���
			HashMap<Integer, Integer> temp = new HashMap<Integer, Integer>();
			for (String key : nearTrainClassi.keySet()) {
				TrainInfo train = trainData.get(key);
				double test2trainDis = nearTrainClassi.get(key);
				if ((test2trainDis - minDis) / minDis < DIS_RATIO_1) {
					int shopID = train.getShopID();
					if (temp.containsKey(shopID)) {
						temp.put(shopID, temp.get(shopID) + 1);
					} else {
						temp.put(shopID, 1);
					}
				}
			}

			int shopID = 0;
			double maxScore = 0;
			for (Integer shopKey : temp.keySet()) {
				int cont = temp.get(shopKey);
				Shop shop = shopData.get(shopKey);
				double dis = test.getUserPos().getDis(shop.getPosition());

				double score = cont / dis * shop.getHeat();
				if (score > maxScore && dis < DIF) {
					maxScore = score;
					shopID = shopKey;
				}
			}
			if (shopID != 0) {
				test.setShopID(shopID);
			}
		}
	}

	private static String getMin(HashMap<String, Double> nearTrain) {
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

	// 4. ����ѵ�����У���ͬ�û���������û�λ����ȫһ���ļ��ϣ� ���ǣ�ͬʱҲҪ���ǵ����
	public static void recommendSamePos(HashMap<String, TestInfo> testData, HashMap<String, TrainInfo> trainData,
			HashMap<String, LabelsCategory> classi) {
		// ��������ѵ���û���λ����Ϣ
		HashMap<Position, ArrayList<String>> posMap = new HashMap<Position, ArrayList<String>>();
		TrainInfo train = null;
		for (String trainKey : trainData.keySet()) {
			train = trainData.get(trainKey);
			Position trainPos = train.getUserPos();
			if (posMap.containsKey(trainPos)) {
				posMap.get(trainPos).add(trainKey);
			} else {
				ArrayList<String> trainKeyArr = new ArrayList<String>();
				trainKeyArr.add(trainKey);
				posMap.put(trainPos, trainKeyArr);
			}
		}

		for (TestInfo testInfo : testData.values()) {
			if (testInfo.getShopID() != 0) {
				continue;
			}
			int testHour = Integer.parseInt(testInfo.getArriveTime().substring(8, 10));
			int testDur = testInfo.getDuration();
			LabelsCategory category = classi.get(testInfo.getUserLabels());
			if (category == null) {
				continue;
			}
			Position testPos = testInfo.getUserPos();
			if (posMap.containsKey(testPos)) {
				// ѵ���г��ָ�λ�õ�ѵ����Ŀ
				ArrayList<String> trainKeyArr = posMap.get(testPos);
				for (String trainKey : trainKeyArr) {
					TrainInfo trainInfo = trainData.get(trainKey);

					int trainHour = Integer.parseInt(trainInfo.getArriveTime().substring(8, 10));
					int trainDur = trainInfo.getDuration();

					double duRate = Math.abs(trainDur - testDur) * 1.0 / Math.max(trainDur, testDur);
					int defHour = Math.abs(testHour - trainHour);
					if ((defHour <= 1 || Math.abs(defHour - 6) <= 1) && duRate < 0.1
							&& classi.containsKey(trainInfo.getShopClassi())) {
						testInfo.setShopID(trainInfo.getShopID());
						continue;
					}
				}
			}
		}
	}

	public static double recommendByNearest(HashMap<Integer, Shop> shopData, HashMap<Integer, Shop> totalShopData,
			HashMap<String, TestInfo> testData, double DIF) {
		double cont = 0;
		// ����3KM����û�е��̵ģ�ѡȡ���������ĵ����Ƽ�
		for (TestInfo test : testData.values()) {
			if (test.getShopID() != 0) {
				continue;
			}
			Position testPos = test.getUserPos();
			double minDis = Double.MAX_VALUE;
			int matchedShopID = 0;
			for (Shop shop : shopData.values()) {
				Position shopPos = shop.getPosition();
				if (testPos.inShopZone(shopPos)) {
					double dis = testPos.getDis(shopPos);
					if (dis < minDis) {
						minDis = dis;
						matchedShopID = shop.getShopID();
					}
				}
			}
			try {
				Position shopPos = shopData.get(matchedShopID).getPosition();
				if (testPos.getDis(shopPos) < DIF) {
					test.setShopID(matchedShopID);
				} else {
					double min = Double.MAX_VALUE;
					for (Shop shop : totalShopData.values()) {
						Position shopPosAll = shop.getPosition();
						double dis = shopPosAll.getDis(testPos);
						if (dis < min && testPos.inShopZone(shopPosAll)) {
							min = dis;
							matchedShopID = shop.getShopID();
						}
					}
					cont++;
					test.setShopID(matchedShopID);
					
//					if (testPos.getDis(shopPos) < 15000) {
//						test.setShopID(0);
//					} else {
//						test.setShopID(matchedShopID);
//					}
				}
			} catch (Exception e) {

			}
		}
		 System.out.println(cont);
		return cont;

	}

	public static TreeMap<Position, ArrayList<TrainInfo>> sortTrain(HashMap<String, TrainInfo> trainData) {
		TreeMap<Position, ArrayList<TrainInfo>> trainSorted = new TreeMap<Position, ArrayList<TrainInfo>>();
		for (TrainInfo train : trainData.values()) {
			Position trainPos = train.getUserPos();
			if (trainSorted.containsKey(trainPos)) {
				trainSorted.get(trainPos).add(train);
			} else {
				ArrayList<TrainInfo> trainArr = new ArrayList<TrainInfo>();
				trainArr.add(train);
				trainSorted.put(trainPos, trainArr);
			}
		}

		for (ArrayList<TrainInfo> trainArr : trainSorted.values()) {
			Collections.sort(trainArr);
		}

		return trainSorted;
	}

	public static void recommendSameUserPos(TreeMap<Position, ArrayList<TrainInfo>> posMap,
			HashMap<String, TestInfo> testData, HashMap<String, User> userData, HashMap<Integer, Shop> shopData,
			HashMap<String, LabelsCategory> classi) {
		int num=0;
		for (TestInfo test : testData.values()) {
			if (test.getShopID() != 0) {
				continue;
			}
			Position userPos = test.getUserPos();
			ArrayList<TrainInfo> trainList = posMap.get(userPos);
			if (trainList == null) {
				continue;
			}
			// ��λ�õĵ���ͳ��
			HashMap<Integer, Integer> shopStat = new HashMap<Integer, Integer>();
			// ͬһ�û��Ĳ�ͬ��¼
			ArrayList<TrainInfo> sameUserTrain = getSameUserTrain(test, trainList);
			ArrayList<TrainInfo> sameLabelTrain = getSameLabelTrain(test, trainList);
			
			for (TrainInfo train : trainList) {
				int shopID = train.getShopID();
				if (shopStat.containsKey(shopID)) {
					shopStat.put(shopID, shopStat.get(shopID) + 1);
				} else {
					shopStat.put(shopID, 1);
				}
			}

			// 1. �����λ�������û�ֻ��һ�ҵ��̣����Ƽ��õ���
			//System.out.println(shopStat.size()+"-----");
			if (shopStat.size() == 1) {
				num=num+1;
				for (TrainInfo train : trainList) {
					if (test.getUserLabels().equals(train.getUserLabels())) {
						
						test.setShopID(train.getShopID());
					}
				}
			}
			// ==========================================================================================
			// ====================================����ͬһ�û�����===========================================
			// ==========================================================================================

			// A. ������б��û���ѵ������
//			if (sameUserTrain.size() != 0) {
//				// ����ִ������ĵ���
//				HashMap<Integer, Integer> maxContStat = getMaxAppearCont(shopStat);
//				HashMap<Integer, Integer> shopAppear = getShopAppear(sameUserTrain);
//				HashMap<Integer, Integer> shopAppearLabels = getShopAppear(sameLabelTrain);
//				// ͬһ�û���ѵ����¼�о��еĴ���
//				switch (sameUserTrain.size()) {
//				case 1: {
//					// ���ֻ���ֹ�һ�Σ���ѳ��ֵ���һ���Ƽ����û���������������������������������������
//					TrainInfo train = sameUserTrain.get(0);
//					test.setShopID(train.getShopID());
//					break;
//				}
//				case 2: {
//					// ���ֵ����ҵ�������ͬ��
//					TrainInfo train = sameUserTrain.get(0);
//					if (shopAppear.size() == 1) {
//						test.setShopID(sameUserTrain.get(0).getShopID());
//					} else {
//						// ������ֵ����ҵ��̲�һ�����Ƽ��������ĵ��̣�������ִ������ĵ��̲�ֻһ�ң����Ƽ��뱾��¼ʱ����ӽ��ļ�¼
//
//						// ������ֻ������һ��
//						if (maxContStat.size() == 1){
//							int shopID = maxContStat.keySet().iterator().next();
//							test.setShopID(shopID);
//							continue;
//						} else {
//							int shopID = getMaxScore(test, shopAppear, shopData);
//							test.setShopID(shopID);
//						}
//					}
//					break;
//				}
//				case 3: {
//					// �������һ�����һ�ҵ��̣�����ѡ���ִ��������һ�ҵ���
//					if (shopAppear.size() < 3) {
//						for (Integer shopID : shopAppear.keySet()) {
//							if (shopAppear.get(shopID) > 1) {
//								test.setShopID(shopID);
//								break;
//							}
//						}
//					} else {
//						// ���ҵ��̶���ͬ���Ƽ��������ĵ��̣�������ִ������ĵ��̲�ֻһ�ң����Ƽ��뱾��¼ʱ����ӽ��ļ�¼
//						int matcheID = getMaxScore(test, shopAppear, shopData);
//						test.setShopID(matcheID);
//					}
//					break;
//				}
//				case 4: {
//					if (shopAppear.size() == 1) {
//						test.setShopID(sameUserTrain.get(0).getShopID());
//					} else if (shopAppear.size() == 2) {
//						// �������ҵ��̣��Ƽ��������ĵ��̣�������ִ������ĵ��̲�ֻһ�ң����Ƽ��뱾��¼ʱ����ӽ��ļ�¼
//						int matcheID = getMaxScore(test, shopAppear, shopData);
//						test.setShopID(matcheID);
//					} else if (shopAppear.size() == 3) {
//						// �Ƽ�����������һ�ҵ���
//						for (Integer shopID : shopAppear.keySet()) {
//							if (shopAppear.get(shopID) == 2) {
//								test.setShopID(shopID);
//							}
//						}
//					} else {
//						// ���������4�Σ����ҵ��̶�����ͬ���Ƽ��������ĵ��̣�������ִ������ĵ��̲�ֻһ�ң����Ƽ��뱾��¼ʱ����ӽ��ļ�¼
//						int matcheID = getMaxScore(test, shopAppear, shopData);
//						test.setShopID(matcheID);
//					}
//				}
//					break;
//				case 5: {
//					// �������ҵ��̣��Ƽ��������ĵ��̣�������ִ������ĵ��̲�ֻһ�ң����Ƽ��뱾��¼ʱ����ӽ��ļ�¼
//					int matcheID = getMaxScore(test, shopAppearLabels, shopData);
//					test.setShopID(matcheID);
//				}
//					break;
//				}
//			}
		}
		System.out.println(num);
	}

	private static ArrayList<TrainInfo> getSameUserTrain(TestInfo test, ArrayList<TrainInfo> trainList) {
		ArrayList<TrainInfo> sameUserTrain = new ArrayList<TrainInfo>();
		for (TrainInfo train : trainList) {
			if (test.getUserID().equals(train.getUserID())) {
				sameUserTrain.add(train);
			}
		}
		return sameUserTrain;
	}

	private static HashMap<Integer, Integer> getShopAppear(ArrayList<TrainInfo> trains) {
		HashMap<Integer, Integer> shopAppear = new HashMap<Integer, Integer>();
		for (TrainInfo train : trains) {
			int shopID = train.getShopID();
			if (shopAppear.containsKey(shopID)) {
				shopAppear.put(shopID, shopAppear.get(shopID) + 1);
			} else {
				shopAppear.put(shopID, 1);
			}
		}
		return shopAppear;
	}

	private static int getMaxScore(TestInfo test, HashMap<Integer, Integer> shopAppear,
			HashMap<Integer, Shop> shopData) {
		HashMap<Integer, Double> scores = new HashMap<Integer, Double>();
		for (Integer shopID : shopAppear.keySet()) {
			int heat = shopAppear.get(shopID);
			Double dis = shopData.get(shopID).getPosition().getDis(test.getUserPos());
			double score = shopData.get(shopID).getHeat() / Math.log(dis);
			scores.put(shopID, score);
		}

		double maxScore = 0;
		int matcheID = 0;
		for (Integer shopID : scores.keySet()) {
			double score = scores.get(shopID);
			if (score > maxScore) {
				maxScore = score;
				matcheID = shopID;
			}
		}
		return matcheID;
	}

	private static HashMap<Integer, Integer> getMaxAppearCont(HashMap<Integer, Integer> shopStat) {
		int maxCont = 0;
		// ����ID�� ���ִ���
		HashMap<Integer, Integer> contStat = new HashMap<Integer, Integer>();
		for (Integer shopID : shopStat.keySet()) {
			if (shopStat.get(shopID) > maxCont) {
				maxCont = shopStat.get(shopID);
			}
		}
		for (Integer shopID : shopStat.keySet()) {
			if (shopStat.get(shopID) == maxCont) {
				contStat.put(shopID, maxCont);
			}
		}
		return contStat;
	}

	private static ArrayList<TrainInfo> getSameLabelTrain(TestInfo test, ArrayList<TrainInfo> trainList) {
		ArrayList<TrainInfo> sameLabelsTrain = new ArrayList<TrainInfo>();
		for (TrainInfo train : trainList) {
			if (test.getUserLabels().equals(train.getUserLabels())) {
				sameLabelsTrain.add(train);
			}
		}
		return sameLabelsTrain;
	}

	public static void recommendNull(HashMap<String, TestInfo> testData) {
		for (TestInfo test : testData.values()) {
			if (test.getDuration() < 16) {
				test.setShopID(0);
			}
		}
	}
}
