import sys
import os


class LabelsCategory(object):
	def __init__(self, user_labels=''):
		self._total_sample = 0
		self._user_labels = user_labels
		
		# 当前标签，用户购物店铺的类别，以及所占的比例
		self._cate_ratio = dict()
		
		# 当前标签，用户购物店铺的类别，以及相应次数
		self._cate_cont = dict()
	
	def set_total_sample(self, total_sample):
		self._total_sample = total_sample
	
	def get_total_sample(self):
		return self._total_sample
	
	def set_cate_ratio(self, cate_ratio):
		self._cate_ratio = cate_ratio
	
	def get_cate_ratio(self):
		return self._cate_ratio
	
	def set_cate_cont(self, cate_cont):
		self._cate_cont = cate_cont
	
	def get_cate_cont(self):
		return self._cate_cont
	
	def add_user_shopping(self, shop):
		classi = shop.get_classification()
		# 如果包含当前分类的map
		if self._cate_cont.__contains__(classi):
			cont = self._cate_cont.get(classi)
			self._cate_cont[classi] = cont + 1
			
			# 总的样本计数器加1
			self._total_sample = self._total_sample + 1
			
			# 更新各个类别所占比例
			for cuCluster in self._cate_ratio.keys():
				newRatio = self._cate_cont.get(cuCluster) * 1.0 / self._total_sample
				self._cate_ratio[cuCluster] = newRatio
			
			# 对其进行排序
			if (self._cate_cont.__len__() > 1):
				self._cate_ratio = dict(sorted(self._cate_ratio.items(), key=lambda item: item[1]))
				self._cate_cont = dict(sorted(self._cate_cont.items(), key=lambda item: item[1]))
		else:
			self._total_sample = self._total_sample + 1
			try:
				self._cate_cont[classi] = 1
				self._cate_ratio[classi] = 0.0
			except Exception:
				pass
			# 更新各个类别所占比例
			for cuCluster in self._cate_cont.keys():
				newRatio = self._cate_cont.get(cuCluster) * 1.0 / self._total_sample
				self._cate_ratio[cuCluster] = newRatio
			
			# 对其进行排序
			if (self._cate_cont.__len__() > 1):
				self._cate_ratio = dict(sorted(self._cate_ratio.items(), key=lambda item: item[1]))
				self._cate_cont = dict(sorted(self._cate_cont.items(), key=lambda item: item[1]))
	
	def contains_classi(self, classi):
		return self._cate_cont.__contains__(classi)
	
	def get_classi_list(self, CATE_RATIO):
		cate_list = list()
		for cate in self._cate_ratio.keys():
			cate_ratio = self._cate_ratio.get(cate)
			if (cate_ratio > CATE_RATIO):
				cate_list.append(cate)
		return cate_list


class Position():
	def __init__(self, long=0, lat=0):
		self._longitude = long
		self._latitude = lat
	
	def get_longitude(self):
		return self._longitude
	
	def set_longitude(self, long):
		self._longitude = long
	
	def get_latitude(self):
		return self._latitude
	
	def set_latitude(self, lat):
		self._latitude = lat
	
	def equal(self, pos):
		if isinstance(pos, Position):
			if (abs(pos.get_latitude() - self._latitude) < 0.0000001) & (
						abs(pos.get_longitude() - self._longitude) < 0.0000001):
				return True
		return False
	
	def near(self, pos, DIF_LONG_LAT):
		'''两个位置是否相近'''
		dif_long = abs(pos.get_longitude() - self._longitude)
		dif_lat = abs(pos.get_latitude() - self._latitude)
		
		if (dif_long < DIF_LONG_LAT) & (dif_lat < DIF_LONG_LAT):
			return True
		else:
			return False
	
	def upNear(self, pos, DEF):
		dif_long = pos.get_longitude() - self._longitude
		dif_lat = pos.get_latitude() - self._latitude
		if dif_long < DEF & dif_lat < DEF:
			return True
		else:
			return False
	
	def inEffectZone(self, pos):
		'''判断是否在店铺的作用域以内'''
		return (pos.get_longitude() > self._longitude) & (pos.get_latitude() > self._latitude)
	
	def degree2rad(self, degree):
		return degree * math.pi / 180.0
	
	def get_dis(self, pos):
		dif_long = abs(self._longitude - pos.get_longitude())
		dif_lat = abs(self._latitude - pos.get_latitude())
		b = (self._latitude + pos.get_latitude()) / 2.0
		dx = self.degree2rad(dif_long) * 6367000.0 * math.cos(self.degree2rad(b))
		dy = 6367000.0 * self.degree2rad(dif_lat)
		dis = math.sqrt(dx * dx + dy * dy)
		return dis
	
	def __eq__(self, other):
		if not isinstance(other, Position):
			raise (TypeError, "can't cmp other type to Post!")
		if self._longitude == other.get_longitude() and self._latitude == other.get_latitude():
			return True
		else:
			return False
	
	def __hash__(self):
		return hash(str(self._longitude) + str(self._latitude))


class Shop():
	def __init__(self):
		self._name = ''
		self._shop_id = 0
		self._shop_pos = Position()
		self._classification = 0
		self._heat = 0
		self._user_dict = dict()  # user_id --- (shop_time -- user_pos -- duration)
		self._labels_cont = dict()  # user_labels -- cont
		self._labels_ratio = dict()
		self._mean_time = 0
	
	def get_effect_zone(self):
		return self._effect_zone
	
	def set_effect_zone(self, effect_zone):
		self._effect_zone = effect_zone
	
	def get_mean_time(self):
		return self._mean_time
	
	def set_mean_time(self, mean_time):
		self._mean_time = mean_time
	
	def get_labels_ratio(self):
		return self._labels_ratio
	
	def set_labels_ratio(self, ratio):
		self._labels_ratio = ratio
	
	def get_labels_cont(self):
		return self._labels_cont
	
	def set_labels_cont(self, labels_cont):
		self._labels_cont = labels_cont
	
	def get_user_dict(self):
		return self._user_dict
	
	def set_user_dict(self, user_dict):
		self.user_dict = user_dict
	
	def get_name(self):
		return self._name
	
	def set_name(self, name):
		self._name = name
	
	def get_shop_id(self):
		return self._shop_id
	
	def set_shop_id(self, shop_id):
		self._shop_id = shop_id
	
	def get_shop_pos(self):
		return self._shop_pos
	
	def set_shop_pos(self, pos):
		self._shop_pos = pos
	
	def get_classification(self):
		return self._classification
	
	def set__classification(self, classification):
		self._classification = classification
	
	def set_shop_heat(self, heat):
		self._heat = heat
	
	def get_shop_heat(self):
		return self._heat
	
	def add_train2shop(self, train):
		# shop heat adding
		# 用户的基本信息
		user_id = train.get_user_id()
		user_labels = train.get_user_labels()
		shop_time = train.get_arrive_time()
		user_pos = train.get_user_pos()
		duration = train.get_duration()
		
		self._heat = self._heat + 1
		self._mean_time = ((self._heat - 1) * self._mean_time + duration) * 1.0 / self._heat
		
		# 出现的用户，以及其购物记录
		if self._user_dict.__contains__(user_id):
			user_his = self._user_dict.get(user_id)
			user_his.append([shop_time, user_pos, duration])
			self._user_dict[user_id] = user_his
		else:
			user_his = list()
			user_his.append([shop_time, user_pos, duration])
			self._user_dict[user_id] = user_his
		# 出现的用户标签及其数目
		if self._labels_cont.__contains__(user_labels):
			cont = self._labels_cont.get(user_labels)
			self._labels_cont[user_labels] = cont + 1
		else:
			self._labels_cont[user_labels] = 1


class TestInfo(object):
	def __init__(self):
		self._user_id = ''
		self._income = 0
		self._entertainment = 0
		self._baby_label = 0
		self._gender = 0
		self._shop_label = 0
		self._user_labels = ''
		self._arrive_time = 0
		self._user_pos = Position(0, 0)
		self._duration = 0
		self._near_train = dict()
		self._shop_id = 0
	
	def get_user_pos(self):
		return self._user_pos
	
	def set_user_pos(self, pos):
		self._user_pos = pos
	
	def get_shop_id(self):
		return self._shop_id
	
	def set_shop_id(self, shop_id):
		self._shop_id = shop_id
	
	def get_user_labels(self):
		return self._user_labels
	
	def set_user_labels(self, userLabels):
		self._user_labels = userLabels
	
	def get_user_id(self):
		return self._user_id
	
	def set_user_id(self, user_id):
		self._user_id = user_id
	
	def get_income(self):
		return self._income
	
	def set_income(self, income):
		self._income = income
	
	def get_entertainment(self):
		return self._entertainment
	
	def set_entertainment(self, entertainment):
		self._entertainment = entertainment
	
	def get_gender(self):
		return self._gender
	
	def set_gender(self, gender):
		self._gender = gender
	
	def get_shop_label(self):
		return self._shop_label
	
	def set_shop_label(self, shopLabel):
		self._shop_label = shopLabel
	
	def get_baby_label(self):
		return self._baby_label
	
	def set_baby_label(self, babyLabel):
		self._baby_label = babyLabel
	
	def get_arrive_time(self):
		return self._arrive_time
	
	def set_arrive_time(self, arrive_time):
		self._arrive_time = arrive_time
	
	def get_duration(self):
		return self._duration
	
	def set_duration(self, duration):
		self._duration = duration
	
	def set_near_train(self, near_train):
		self._near_train = near_train
	
	def get_near_train(self):
		return self._near_train
	
	def __hash__(self):
		return hash(str(self._user_id) + str(self._arrive_time))
	
	def __cmp__(self, other):
		if self.__eq__(other):
			return 0
		elif self.__lt__(other):
			return -1
		elif self.__gt__(other):
			return 1
	
	def __eq__(self, other):
		if not isinstance(other, TestInfo):
			raise (TypeError, "can't cmp other type to TestInfo!")
		if self._arrive_time == other.get_arrive_time() and self._user_id == other.get_user_id():
			return True
		else:
			return False
	
	def __lt__(self, other):
		if not isinstance(other, TestInfo):
			raise (TypeError, "can't cmp other type to TestInfo!")
		if self._user_id < other.get_user_id():
			return True
		elif self._user_id == other.get_user_id() and self._arrive_time < other.get_arrive_time():
			return True
		else:
			return False
	
	def __gt__(self, other):
		if not isinstance(other, TestInfo):
			raise (TypeError, "can't cmp other type to TestInfo!")
		if self._user_id > other.get_user_id():
			return True
		elif self._user_id == other.get_user_id() and self._arrive_time > other.get_arrive_time():
			return True
		else:
			return False


class TrainInfo(object):
	def __init__(self):
		self._user_id = ''
		self._income = 0
		self._entertainment = 0
		self._baby_label = 0
		self._gender = 0
		self._shop_label = 0
		self._user_labels = ''
		self._arrive_time = ''
		self._user_pos = Position(0, 0)
		self._duration = 0
		self._shop_id = 0
		self._shop_pos = Position(0, 0)
		self._shop_classi = 0
		self._shop_heat = 0
	
	def get_user_pos(self):
		return self._user_pos
	
	def set_user_pos(self, pos):
		self._user_pos = pos
	
	def get_shop_pos(self):
		return self.user_pos
	
	def get_user_labels(self):
		return self._user_labels
	
	def set_user_labels(self, userLabels):
		self._user_labels = userLabels
	
	def get_user_id(self):
		return self._user_id
	
	def set_user_id(self, user_id):
		self._user_id = user_id
	
	def get_income(self):
		return self._income
	
	def set_income(self, income):
		self._income = income
	
	def get_entertainment(self):
		return self._entertainment
	
	def set_entertainment(self, entertainment):
		self._entertainment = entertainment
	
	def get_gender(self):
		return self._gender
	
	def set_gender(self, gender):
		self._gender = gender
	
	def get_shop_label(self):
		return self._shop_label
	
	def set_shop_label(self, shopLabel):
		self._shop_label = shopLabel
	
	def get_baby_label(self):
		return self._baby_label
	
	def set_baby_label(self, babyLabel):
		self._baby_label = babyLabel
	
	def get_shop_id(self):
		return self._shop_id
	
	def set_shop_id(self, shop_id):
		self._shop_id = shop_id
	
	def get_arrive_time(self):
		return self._arrive_time
	
	def set_arrive_time(self, arrive_time):
		self._arrive_time = arrive_time
	
	def get_user_pos(self):
		return self._user_pos
	
	def set_user_pos(self, user_pos):
		self._user_pos = user_pos
	
	def get_shop_pos(self):
		return self._shop_pos
	
	def set_shop_pos(self, shop_pos):
		self._shop_pos = shop_pos
	
	def get_shop_classi(self):
		return self._shop_classi
	
	def set__shop_classi(self, classification):
		self._shop_classi = classification
	
	def set_shop_heat(self, heat):
		self._shop_heat = heat
	
	def get_shop_heat(self):
		return self._shop_heat
	
	def get_duration(self):
		return self._duration
	
	def set_duration(self, duration):
		self._duration = duration
	
	def __hash__(self):
		return hash(str(self._user_id) + str(self._arrive_time))
	
	def __cmp__(self, other):
		if self.__eq__(other):
			return 0
		elif self.__lt__(other):
			return -1
		elif self.__gt__(other):
			return 1
	
	def __eq__(self, other):
		if not isinstance(other, TrainInfo):
			raise (TypeError, "can't cmp other type to TrainInfo!")
		if self._arrive_time == other.get_arrive_time() and self._user_id == other.get_user_id():
			return True
		else:
			return False
	
	def __lt__(self, other):
		if not isinstance(other, TrainInfo):
			raise (TypeError, "can't cmp other type to TrainInfo!")
		if self._user_id < other.get_user_id():
			return True
		elif self._user_id == other.get_user_id() and self._arrive_time < other.get_arrive_time():
			return True
		else:
			return False
	
	def __gt__(self, other):
		if not isinstance(other, TrainInfo):
			raise (TypeError, "can't cmp other type to TrainInfo!")
		if self._user_id > other.get_user_id():
			return True
		elif self._user_id == other.get_user_id() and self._arrive_time > other.get_arrive_time():
			return True
		else:
			return False


class User(object):
	def __init__(self):
		self._userID = ''
		self._income = 0
		self._entertainment = 0
		self._baby_label = 0
		self._gender = 0
		self._shop_label = 0
		self._user_labels = 0
		self._shop_cont = 0
		self._shop_his = dict()  # time --- (shop_id, user_pos, shop_pos, duration)
		self._cate_cont = dict()
	
	def get_user_id(self):
		return self._userID
	
	def set_user_id(self, userID):
		self._userID = userID
	
	def get_income(self):
		return self._income
	
	def set_income(self, income):
		self._income = income
	
	def get_entertainment(self):
		return self._entertainment
	
	def set_entertainment(self, ent):
		self._entertainment = ent
	
	def get_baby_label(self):
		return self._baby_label
	
	def set_baby_label(self, babyLabel):
		self._baby_label = babyLabel
	
	def get_gender(self):
		return self._gender
	
	def set_gender(self, gender):
		self._gender = gender
	
	def get_shop_label(self):
		return self._shop_label
	
	def set_shop_label(self, shopLabel):
		self._shop_label = shopLabel
	
	def get_user_labels(self):
		return self._user_labels
	
	def set_user_labels(self, userLabels):
		self._user_labels = userLabels
	
	def get_shop_his(self):
		return self._shop_his
	
	def set_shop_his(self, shop_his):
		self._shop_his = shop_his
	
	def add_train2user(self, trainInfo):
		'''将用户的训练信息添加到该用户的历史记录当中'''
		self._shop_cont = self._shop_cont + 1
		shop_classi = trainInfo.get_shop_classi()
		shop_id = trainInfo.get_shop_id()
		duration = trainInfo.get_duration()
		arrive_time = trainInfo.get_arrive_time()
		user_pos = trainInfo.get_user_pos()
		shop_pos = trainInfo.get_shop_pos()
		# 用户历史记录更新
		self._shop_his[arrive_time] = (shop_id, user_pos, shop_pos, duration)
		if self._cate_cont.__contains__(shop_classi):
			cont = self._cate_cont.get(shop_classi)
			self._cate_cont[shop_classi] = cont + 1
		else:
			self._cate_cont[shop_classi] = 1


import math


def sortTrain(train_data):
	train_sorted = dict()
	for train in train_data.values():
		train_pos = train.get_user_pos()
		if train_sorted.__contains__(train_pos):
			train_sorted.get(train_pos).append(train)
		else:
			train_arr = list()
			train_arr.append(train)
			train_sorted[train_pos] = train_arr
	return train_sorted


def getTrainShop(train_data, shop_data):
	'''获取训练集当中的店铺'''
	train_shop_dict = dict()
	for train in train_data.values():
		shopID = train.get_shop_id()
		if train_shop_dict.__contains__(shopID):
			pass
		else:
			train_shop_dict[shopID] = shop_data.get(shopID)
	return train_shop_dict


def get_no_recommend_test(test_data):
	'''获取那些不需要推荐的用户记录'''
	no_recom = dict()
	for key in test_data.keys():
		test = test_data.get(key)
		if test.get_duration() < 15.1:
			no_recom[key] = test
	return no_recom


def get_recomment_test(test_data):
	'''获取那些需要推荐的用户记录'''
	recom = dict()
	for key in test_data.keys():
		test = test_data.get(key)
		if test.get_duration() > 15.1:
			recom[key] = test
	return recom


def get_no_shop_train(train_data):
	'''获取那些没有推荐的训练记录'''
	train_no_shop = dict()
	for key in train_data.keys():
		train = train_data.get(key)
		if train.get_shop_id == 0:
			train_no_shop[key] = train
	return train_no_shop


def get_shop_train(train_data):
	'''获取那些推荐过的训练记录'''
	train_shop = dict()
	for key in train_data.keys():
		train = train_data.get(key)
		if train.get_shop_id != 0:
			train_shop[key] = train
	return train_shop


def get_train_shop(train_data, shop_data):
	'''获取训练集当中出现的店铺'''
	train_shop = dict()
	for train in train_data.values():
		shop_id = train.get_shop_id()
		shop = shop_data.get(shop_id)
		train_shop[shop_id] = shop
	return train_shop


def get_train_user(train_data, user_data):
	''' 获取训练集当中出现的用户'''
	train_user = dict()
	for train in train_data.values():
		user_id = train.get_shop_id()
		user = user_data.get(user_id)
		train_user[user_id] = user
	return train_user


def user_match_train(train_data, user_data):
	'''训练数据，添加到用户信息内部，表示用户的行为特征'''
	for train in train_data.values():
		user = user_data.get(train.get_user_id())
		user.add_train2user(train)


def shop_match_train(train_data, shop_data):
	'''训练数据添加到店铺信息对象，表示店铺的用户行为记录'''
	for train in train_data.values():
		shop = shop_data.get(train.get_shop_id())
		shop.add_train2shop(train)


def test_attach_train(train_by_pos, test_data, shop_data, DIF_LONG_LAT):
	'''获取与用户相匹配的训练记录'''
	for test_key in test_data.keys():
		test = test_data.get(test_key)
		test_pos = test.get_user_pos()
		# get test to all the train dis which is smaller than OFFSET
		dis_test2shop = dict()  # train__pos  ====  test_pos
		for train_user_pos in train_by_pos.keys():
			train_list = train_by_pos.get(train_user_pos)
			if test_pos.near(train_user_pos, DIF_LONG_LAT):
				for train in train_list:
					train_shop_pos = train.get_shop_pos()
					if test_pos.inEffectZone(train_shop_pos):
						dis = test_pos.get_dis(train_user_pos)
						if dis < 10000:
							train_key = train.get_user_id() + str(train.get_arrive_time())
							dis_test2shop[train_key] = dis
		# print(str(test.get_user_id()) + "-" + test.get_arrive_time() + " : " + str(len(dis_test2shop)))
		test.set_near_train(dis_test2shop)


def get_train_classi(train_dict, shop_dict):
	'''获取训练数据的类别信息'''
	train_classi = dict()
	# 对用户标签的分类信息进行获取
	for train in train_dict.values():
		# 用户标签字符串
		user_labels = train.get_user_labels()
		shop = shop_dict.get(train.get_shop_id())
		if train_classi.__contains__(user_labels):  # 集合中包含此类标签组合
			labels_cat = train_classi.get(user_labels)
			labels_cat.add_user_shopping(shop)
		else:
			labels_cat = LabelsCategory(user_labels)  # 集合中不包含此类标签组合
			labels_cat.add_user_shopping(shop)
			train_classi[user_labels] = labels_cat
	
	return train_classi


# -----------------------1----------------------------
def recommendByTime(test_data, user_data):
	'''同一用户， 按照其时间进行推荐'''
	for test in test_data.values():
		if (test.get_shop_id() != 0):
			continue
		user_pos = test.get_user_pos()
		pre_time = test.get_arrive_time()
		pre_day = int(pre_time[6: 8])
		pre_hour = int(pre_time[8, 10])
		pre_min = int(pre_time[10, 12])
		test_duration = test.get_duration()
		
		user = user_data.get(test.get_user_id())
		shop_his = user.get_shop_his()
		def_minutes = dict()
		for time in shop_his.keys():
			shop_tuple = shop_his.get(time)
			shop_id = shop_tuple[0]
			train_pos = shop_tuple[1]
			train_hour = int(time[8, 10])
			train_day = int(time[6, 8])
			if user_pos.equals(train_pos):
				# 具有小时关系 ----相差一个小时
				def_hour = abs(train_hour - pre_hour)
				if def_hour <= 1:  # abs(def_hour - 6) <= 1
					train_minute = int(time[10, 12])
					def_min = timeDifMinutes(pre_hour, pre_min, train_hour, train_minute)
					def_minutes[shop_id] = def_min
					continue
				
				# 具有星期关系7---14天
				if ((abs(train_day - pre_day) == 7) | (abs(train_day - pre_day) == 14)) & (
							(def_hour <= 1) | (abs(def_hour - 6) <= 1)):
					test.set_shop_id(shop_id)
				break
			
			# 具有duration关系
		
		# 取duration差距最小的推荐给用户
		minMinute = 100
		matched_shop_id = 0
		for shop_id in def_minutes.keys():
			if def_minutes.get(shop_id) < minMinute:
				minMinute = def_minutes.get(shop_id)
				matched_shop_id = shop_id
		test.set_shop_id(matched_shop_id)


def timeDifMinutes(hour1, min1, hour2, min2):
	if hour1 > hour2:
		return 60 + min1 - min2
	elif hour1 < hour2:
		return 60 + min2 - min1
	else:
		return abs(min1 - min2)


# -----------------------2----------------------------
def recommendSameUserPos(train_by_pos, test_data, user_data, train_shop, classi):
	'''同一用户，在相同位置的推荐'''
	for test in test_data.values():
		if test.get_shop_id() != 0:
			continue
		
		test_user_pos = test.get_user_pos()
		train_list = train_by_pos.get(test_user_pos)
		if not train_list:
			continue
		# 该位置的店铺统计
		shop_stat = dict()
		# 同一用户的不同记录
		same_user_train = get_same_user_train(test, train_list)
		same_labels_train = get_same_labels_train(test, train_list)
		for train in train_list:
			shop_id = train.get_shop_id()
			if shop_stat.__contains__(shop_id):
				shop_stat[shop_id] = shop_stat.get(shop_id) + 1
			else:
				shop_stat[shop_id] = 1
		
		# 如果该位置的所有用户只有一家店铺
		if len(shop_stat) == 1:
			for train in train_list:
				if test.get_user_labels() == train.get_user_labels():
					test.set_shop_id(train.get_shop_id())
		# ===========================================================================
		# ==========================对于同一用户而言=======================================
		
		# A. 如果具有本用户的训练数据
		if len(same_user_train) != 0:
			# 求出出现次数最多的店铺
			max_cont_stat = get_max_appear_cont(shop_stat)
			shop_appear = get_shop_appear(same_user_train)
			shop_appear_labels = get_shop_appear(same_labels_train)
			# 如果只出现一次
			if len(same_user_train) == 1:
				train = same_user_train[0]
				test.set_shop_id(train.get_shop_id())
				continue
			elif len(same_user_train) == 2:
				train = same_user_train[0]
				if len(shop_appear) == 1:  # 出现的两家店铺是相同的
					test.set_shop_id(train.get_shop_id())
					continue
				else:
					if len(max_cont_stat) == 1:
						keys = list(max_cont_stat.keys())
						shop_id = keys[0]
						test.set_shop_id(shop_id)
						continue
					else:
						shop_id = get_max_score(test, shop_appear, train_shop)
						test.set_shop_id(shop_id)
						continue
			elif len(same_user_train) == 3:
				# 出现两家店铺或者是一家店铺，则推荐出现次数最多的店铺
				if len(shop_appear) < 3:
					for (shop_id, cont) in shop_appear.items():
						if cont > 1:
							test.set_shop_id(shop_id)
							break
				else:  # 三家店铺都不相同
					matched_id = get_max_score(test, shop_appear, train_shop)
					test.set_shop_id(matched_id)
					continue
			elif len(same_user_train) == 4:
				if len(shop_appear) == 1:
					test.set_shop_id(same_user_train[0].get_shop_id())
				elif len(shop_appear) == 2:
					matched_id = get_max_score(test, shop_appear, train_shop)
					test.set_shop_id(matched_id)
				elif len(shop_appear) == 3:
					# 推荐出现次数最多的那一家店铺
					for (shop_id, cont) in shop_appear.items():
						if cont == 2:
							test.set_shop_id(shop_id)
				else:
					# 如果出现了4次
					matched_id = get_max_score(test, shop_appear, train_shop)
					test.set_shop_id(matched_id)
			elif len(same_user_train) == 5:
				matched_id = get_max_score(test, shop_appear_labels, train_shop)
				test.set_shop_id(matched_id)


def get_max_appear_cont(shop_stat):
	max_cont = 0
	# shop_id : appear_cont
	cont_stat = dict()
	for (shop_id, cont) in shop_stat.items():
		if cont > max_cont:
			max_cont = cont
	for (shop_id, cont) in shop_stat.items():
		if cont == max_cont:
			cont_stat[shop_id] = max_cont
	return cont_stat


def get_shop_appear(same_user_train):
	shop_appear = dict()
	for train in same_user_train:
		shop_id = train.get_shop_id()
		if shop_appear.__contains__(shop_id):
			shop_appear[shop_id] = shop_appear.get(shop_id) + 1
		else:
			shop_appear[shop_id] = 1
	return shop_appear


def get_max_score(test, shop_appear, shop_data):
	scores = dict()
	for (shop_id, heat) in shop_appear.items():
		dis = shop_data.get(shop_id).get_shop_pos().get_dis(test.get_user_pos())
		score = shop_data.get(shop_id).get_shop_heat() / math.log(dis)
		scores[shop_id] = score
	max_score = 0
	matched_id = 0
	for (shop_id, score) in scores.items():
		if score > max_score:
			max_score = score
			matched_id = shop_id
	return matched_id


def get_same_user_train(test, train_list):
	same_user_train = list()
	for train in train_list:
		if test.get_user_id() == train.get_user_id():
			same_user_train.append(train)
	return same_user_train


def get_same_labels_train(test, train_list):
	same_labels_train = list()
	for train in train_list:
		if test.get_user_labels() == train.get_user_labels():
			same_labels_train.append(train)
	return same_labels_train


def recommendDifUserPos(train_by_pos, test_data, user_data, shop_data, classi):
	for test in test_data.values():
		if (test.get_shop_id()) != 0:
			continue
		user_pos = test.get_user_pos()
		train_list = train_by_pos.get(user_pos)
		if not train_list:
			continue
		# 该位置的店铺统计
		shop_stat = dict()
		for train in train_list:
			shop_id = train.get_shop_id()
			if shop_stat.__contains__(shop_id):
				shop_stat[shop_id] = shop_stat.get(shop_id) + 1
			else:
				shop_stat[shop_id] = 1
		category = classi.get(test.get_user_labels())
		cate_list = category.get_classi_list(0.3)
		# 仅仅推荐具有一家店铺的情况
		if len(shop_stat) == 1:
			for train in train_list:
				shop_id = train.get_shop_id()
				test_time = test.get_arrive_time()
				train_time = train.get_arrive_time()
				test_day = int(test_time[6:8])
				train_day = int(train_time[6:8])
				def_day = abs(test_day - train_day)
				test_duration = test.get_duration()
				train_duration = train.get_duration()
				if (shop_data.get(shop_id).get_classification() in cate_list) & (def_day <= 3) & (abs(
							test_duration - train_duration) <= 3):
					test.set_shop_id(shop_id)
					continue


# -----------------------3----------------------------
def recommendSameUserDifPos(train_data, test_data, user_data, DIF_SAME_SHOP, DIF=0.02):
	'''对于同一用户，如果测试用户与训练的定位，相差小于0.02， 则视其为同一位置， 此时推荐同一店铺
	并且需要保证，此时的用户坐标，跟以前的用户坐标不相等'''
	for test in test_data.values():
		if test.get_shop_id() != 0:
			continue
		user_id = test.get_user_id()
		# time --- (shop_id, user_pos, duration)
		shop_his = user_data.get(user_id).get_shop_his()
		user_pos = test.get_user_pos()
		for time in shop_his.keys():
			train = train_data.get(user_id + time)
			if train.get_user_pos().near(user_pos, DIF_SAME_SHOP):
				if user_pos.get_dis(train.get_shop_pos()) < DIF:
					test.set_shop_id(train.get_shop_id())
					break


# -----------------------4----------------------------
# 剩余一部分的用户坐标跟以前的位置的坐标是完全相等的，就先不推荐了


# -----------------------5----------------------------
def recommendByClassi(train_data, test_data, train_classi, user_data, shop_data, CATE_RATIO, DIS_RATIO, DIF):
	'''按照用户的类别以及位置进行推荐'''
	num = 0
	for test in test_data.values():
		if test.get_shop_id() != 0:
			continue
		
		test_pos = test.get_user_pos()  # 获取测试集的经纬度
		user_labels = test.get_user_labels()  # 获取测试集用户的标签
		
		# 获取相近的用户轨迹
		near_train = test.get_near_train()
		
		if len(near_train) == 0:  # 有119条空值，5992条
			continue
		if not train_classi.__contains__(user_labels):
			continue
		
		cate = train_classi.get(user_labels)
		cate_list = cate.get_classi_list(CATE_RATIO)
		min_dis = 100000000000
		near_train_classi = dict()  # 与本用户类别有关的店铺
		# ========================================
		# 此时推荐的是距离最近的店铺，准确性有待商榷
		for key in near_train.keys():
			train = train_data.get(key)
			shop_pos = train.get_shop_pos()
			test2shop_dis = test_pos.get_dis(shop_pos)
			shop_classi = train.get_shop_classi()
			
			if (shop_classi in cate_list) & (test_pos.inEffectZone(shop_pos)):
				near_train_classi[key] = near_train.get(key)
				if test2shop_dis < min_dis:
					min_dis = test2shop_dis
		# print (min_dis)
		
		
		# 统计此店铺在训练集中出现的次数--此时只是在周围的一定距离当中
		temp = dict()
		for key in near_train_classi.keys():
			# num=num+1
			train = train_data.get(key)
			
			test2train_dis = near_train_classi.get(key)
			if ((test2train_dis - min_dis) / min_dis) < DIS_RATIO:
				# print((test2train_dis - min_dis) / min_dis)
				shop_id = train.get_shop_id()
				if temp.__contains__(shop_id):
					temp[shop_id] = temp.get(shop_id) + 1
				else:
					temp[shop_id] = 1
		max_score = 0
		matched_id = 0
		#
		for shop_id in temp.keys():
			
			cont = float(temp.get(shop_id))
			shop = shop_data.get(shop_id)
			dis = test_pos.get_dis(shop.get_shop_pos())
			score = cont / dis * (shop.get_shop_heat())
			# print(score)
			if (score > max_score) & (dis < DIF):
				max_score = score
				matched_id = shop_id
		
		if matched_id != 0:
			test.set_shop_id(matched_id)
	print(num)


# -----------------------6----------------------------
def recommendSamePos(train_data, test_data, shop_data, user_data, classi):
	'''对于训练集中，不同用户，与测试用户位置完全一样的集合， 但是，同时也要考虑到类别'''
	pos_dict = dict()
	for train_key in train_data.keys():
		train = train_data.get(train_key)
		train_pos = train.get_user_pos()
		if pos_dict.__contains__(train_pos):
			pos_dict.get(train_pos).append(train_key)
		else:
			train_key_list = list()
			train_key_list.append(train_key)
			pos_dict[train_key] = train_key_list
	
	for test in test_data.values():
		if test.get_shop_id() != 0:
			continue
		arrive_time = test.get_arrive_time()
		test_hour = arrive_time[8, 10]
		test_duration = test.get_duration()
		user_labels = test.get_user_labels()
		if not classi._contains_(user_labels):
			continue
		category = classi.get(user_labels)
		
		#   阈值=========================
		cate_list = category.get_classi_list(0.25)
		
		# 计算用户训练集中已经具有的店铺类别，推荐其不存在的店铺类别
		shop_his = user_data.get(test.get_user_id()).get_shop_his()
		for shop_id in shop_his.values():
			cate = shop_data.get(shop_id).get_classification()
			if cate_list._contains_(cate):
				cate_list.remove(cate)
		if len(cate_list) == 0:
			continue
		test_pos = test.get_user_pos()
		if pos_dict.__contains__(test_pos):
			# 训练中出现该位置的训练条目
			train_key_list = pos_dict.get(test_pos)
			for train_key in train_key_list:
				train = train_data.get(train_key)
				time = train.get_arrive_time()
				train_hour = int(time[8, 10])
				train_duration = train.get_duration()
				
				duRate = abs(train_duration - test_duration) * 1.0 / max(train_duration, test_duration)
				def_hour = abs(test_hour - train_hour)
				
				if cate_list._contains_(train.get_shop_classi()) & (
							(def_hour <= 1) | (abs(def_hour - 6) <= 1)) & (duRate <= 0.1):
					test.set_shop_id(train.get_shop_id())
					break


# -----------------------8----------------------------
def recommendByNearest(test_data, train_shop_data, total_shop_data, DIF):
	'''对于周围没有店铺的，推荐最近的店铺'''
	for test in test_data.values():
		if test.get_shop_id() != 0:
			continue
		test_pos = test.get_user_pos()
		min_dis = 100000000
		matched_shop_id = 0
		for shop in train_shop_data.values():
			shop_pos = shop.get_shop_pos()
			if test_pos.inEffectZone(shop_pos):
				dis = test_pos.get_dis(shop_pos)
				if dis < min_dis:
					matched_shop_id = shop.get_shop_id()
					min_dis = dis
		try:
			shop_pos = total_shop_data.get(matched_shop_id).get_shop_pos()
			if test_pos.get_dis(shop_pos) < DIF:
				test.set_shop_id(matched_shop_id)
			else:
				min = 1000000000
				for shop in total_shop_data.values():
					shop_pos_all = shop.get_shop_pos()
					dis = shop_pos_all.get_dis(test_pos)
					if (dis < min) & test_pos.inEffectZone(shop_pos_all):
						min = dis
						matched_shop_id = shop.get_shop_id()
				test.set_shop_id(matched_shop_id)
		except:
			pass


# -----------------------0----------------------------
def recommendNoShop(test_data, classi, shop_data, DEF=0.05, RATIO=0.3):
	'''对于那些在一定的经纬度内部，附件并没有任何店铺的用户，推荐离其较近的店铺'''
	for test in test_data.values():
		if test.get_shop_id() != 0:
			continue
		# 统计在一定坐标范围内店铺的个数
		cont = 0
		test_pos = test.get_user_pos()
		cate = classi.get(test.get_user_labels)
		
		for shop in shop_data.values():
			shop_pos = shop.get_pos()
			# 经纬度相差DEF以内
			if test_pos.upNear(shop_pos, DEF):
				cont = cont + 1
		
		# 如果周围没有店铺
		if cont == 0:
			shop_id = getUpNearest(test, shop_data, cate, RATIO)
			test.set_shop_id()


def getUpNearest(test, shop_data, cate, RATIO):
	test_pos = test.get_user_pos()
	minDis = 100000000000000000000000
	matched_id = 0
	for shop in shop_data.values():
		shop_pos = shop.get_shop_pos()
		dis = test_pos.get_dis(shop_pos)
		if (dis <= minDis) & (cate.get_classi_list(RATIO)._contains_(shop.get_classification())):
			minDis = dis
			matched_id = shop.get_shop_id()
	return matched_id


class Reader():
	'读写数据的类，用于csv数据的读写'
	
	@staticmethod
	def save_CSV(listInfo=list(), filePath=""):
		"静态方法，保存数据"
		
		# 判断文件是否存在，如果存在则删除
		if os.path.exists(filePath):
			os.remove(filePath)
		
		# 将内容写入文件
		csv = open(filePath, "w")
		for line in listInfo:
			csv.write(line + '\n')
		csv.close()
	
	@staticmethod
	def read_train_data(DataPath):
		# 静态方法 读取用户csv数据
		bufferedReader = open(DataPath, "r")
		train_info_dict = dict()
		
		# 读取首行，看是否为空
		if (bufferedReader.readline() is None):
			pass
		# 读取每一行信息
		for strLine in bufferedReader:
			train_info = TrainInfo()
			strLine = strLine.strip('\n')
			strSet = strLine.split(",")
			userID = str(int(float(strSet[0])))
			income = int(float(strSet[1]))
			entertainment = int(float(strSet[2]))
			babyLabel = int(float(strSet[3]))
			gender = int(float(strSet[4]))
			shop_label = int(float(strSet[5]))
			user_labels = str(income) + str(entertainment) + str(babyLabel) + str(gender) + str(shop_label)
			
			shopID = int(float(strSet[6]))
			arrive_time = strSet[7][0:14]
			user_long = float(strSet[8])
			user_lat = float(strSet[9])
			user_pos = Position(user_long, user_lat)
			duration = int(float(strSet[10]))
			shop_long = float(strSet[11])
			shop_lat = float(strSet[12])
			shop_pos = Position(shop_long, shop_lat)
			classi = int(float(strSet[13]))
			
			train_info.set_duration(duration)
			train_info.set_arrive_time(arrive_time)
			train_info.set_entertainment(entertainment)
			train_info.set_shop_pos(shop_pos)
			train_info.set_user_id(userID)
			train_info.set_income(income)
			train_info.set_baby_label(babyLabel)
			train_info.set_gender(gender)
			train_info.set_shop_label(shop_label)
			train_info.set_user_labels(user_labels)
			train_info.set_shop_id(shopID)
			train_info.set_user_pos(user_pos)
			train_info.set__shop_classi(classi)
			
			key = userID + arrive_time
			train_info_dict[key] = train_info
		
		bufferedReader.close()
		return train_info_dict
	
	@staticmethod
	def read_test_data(DataPath):
		# 静态方法 读取测试用户csv数据
		bufferedReader = open(DataPath, "r")
		test_info_dict = dict()
		
		# 读取首行，看是否为空
		if (bufferedReader.readline() is None):
			pass
		# 读取每一行信息
		for strLine in bufferedReader:
			test_info = TestInfo()
			strLine = strLine.strip('\n')
			strSet = strLine.split(",")
			userID = str(int(float(strSet[0])))
			income = int(float(strSet[1]))
			entertainment = int(float(strSet[2]))
			babyLabel = int(float(strSet[3]))
			gender = int(float(strSet[4]))
			shop_label = int(float(strSet[5]))
			user_labels = str(income) + str(entertainment) + str(babyLabel) + str(gender) + str(shop_label)
			
			arrive_time = strSet[6][0:14]
			user_long = float(strSet[7])
			user_lat = float(strSet[8])
			user_pos = Position(user_long, user_lat)
			duration = int(float(strSet[9]))
			
			test_info.set_duration(duration)
			test_info.set_arrive_time(arrive_time)
			test_info.set_entertainment(entertainment)
			test_info.set_user_id(userID)
			test_info.set_income(income)
			test_info.set_baby_label(babyLabel)
			test_info.set_gender(gender)
			test_info.set_shop_label(shop_label)
			test_info.set_user_labels(user_labels)
			test_info.set_user_pos(user_pos)
			
			key = userID + arrive_time
			test_info_dict[key] = test_info
		
		bufferedReader.close()
		return test_info_dict
	
	@staticmethod
	def read_shop_data(data_path):
		'静态方法 读取店铺csv数据'
		buffer_reader = open(data_path, "r")
		shop_dict = dict()
		
		# 读取首行，看是否为空
		if (buffer_reader.readline() is None):
			pass
		# 读取每一行信息
		for strLine in buffer_reader:
			strLine = strLine.strip('\n')
			# NAME	LONGITUDE	LATITUDE	CLASSIFICATION	ID
			str_set = strLine.split(",")
			shop_name = str(str_set[0])
			shop_longitude = float(str_set[1])
			shop_latitude = float(str_set[2])
			shop_pos = Position(shop_longitude, shop_latitude)
			classification = int(float(str_set[3]))
			shop_id = int(float(str_set[4]))
			
			shop = Shop()
			shop.set_name(shop_name)
			shop.set_shop_pos(shop_pos)
			shop.set__classification(classification)
			shop.set_shop_id(shop_id)
			
			shop_dict[shop_id] = shop
		
		buffer_reader.close()
		return shop_dict
	
	@staticmethod
	def get_test_answer(all_test_info):
		'''Get the answer string'''
		answer = list()
		answer.append('USERID,SHOPID,ARRIVAL_TIME')
		for test in all_test_info.values():
			user_id = test.get_user_id()
			arrive_time = test.get_arrive_time()
			shop_id = test.get_shop_id()
			if shop_id == 0:
				anstr = user_id + ',' + ',' + str(arrive_time)
			else:
				anstr = user_id + ',' + str(shop_id) + ',' + str(arrive_time)
			answer.append(anstr)
		return answer
	
	def read_user_data(self, USER_PATH):
		# 静态方法 读取测试用户csv数据
		bufferedReader = open(USER_PATH, "r")
		user_dict = dict()
		
		# 读取首行，看是否为空
		if (bufferedReader.readline() is None):
			pass
		# 读取每一行信息
		for strLine in bufferedReader:
			user = User()
			strLine = strLine.strip('\n')
			strSet = strLine.split(",")
			userID = str(int(float(strSet[0])))
			income = int(float(strSet[1]))
			entertainment = int(float(strSet[2]))
			babyLabel = int(float(strSet[3]))
			gender = int(float(strSet[4]))
			shop_label = int(float(strSet[5]))
			user_labels = str(income) + str(entertainment) + str(babyLabel) + str(gender) + str(shop_label)
			
			user.set_user_id(userID)
			user.set_income(income)
			user.set_baby_label(babyLabel)
			user.set_gender(gender)
			user.set_shop_label(shop_label)
			user.set_entertainment(entertainment)
			user.set_user_labels(user_labels)
			user_dict[userID] = user
		
		bufferedReader.close()
		return user_dict


def recommend_null(test_data):
	for test in test_data.values():
		if test.get_duration() < 16:
			test.set_shop_id(0)


if __name__ == '__main__':
	ANS_PATH = "D:\\data1\\ANSWER.csv"
	SHOP_PATH = "D:\\data1\\SYS.CCF_SHOP_PROFILE_FS.csv"
	USER_PATH = "D:\\data1\\SYS.CCF_USER_PROFILE_FS.csv"
	TRAIN_PATH = "D:\\data1\\TRAIN_INFO_FILTERED.csv"
	TEST_PATH = "D:\\data1\\TEST_INFO.csv"
	DIF_LONG_LAT = 0.002
	CATE_RATIO = 0.15
	DIS_RATIO = 0.11
	DIF_SAME_SHOP = 0.03
	DIF_DIS = 16000
	# read the whole data
	print("..............................Reading original data...................................")
	reader = Reader()
	train_data = reader.read_train_data(TRAIN_PATH)
	test_data = reader.read_test_data(TEST_PATH)
	shop_data = reader.read_shop_data(SHOP_PATH)
	user_data = reader.read_user_data(USER_PATH)
	print("...............................data has been gotten...................................")
	
	# 获取训练集当中的店铺
	train_shop = getTrainShop(train_data, shop_data)
	train_by_pos = sortTrain(train_data)
	classi = get_train_classi(train_data, train_shop)
	
	user_match_train(train_data, user_data)
	shop_match_train(train_data, train_shop)
	test_attach_train(train_by_pos, test_data, train_shop, DIF_LONG_LAT)
	
	print("..............................data has been prepared..................................")
	
	recommendSameUserPos(train_by_pos, test_data, user_data, train_shop, classi)
	recommendDifUserPos(train_by_pos, test_data, user_data, train_shop, classi)
	recommendByClassi(train_data, test_data, classi, user_data, train_shop, CATE_RATIO, DIS_RATIO, DIF_DIS)
	recommendSameUserDifPos(train_data, test_data, user_data, DIF_SAME_SHOP, DIF_DIS)
	recommendByNearest(test_data, train_shop, shop_data, DIF_DIS)
	recommend_null(test_data)
	# save the file
	answer = reader.get_test_answer(test_data)
	reader.save_CSV(answer, ANS_PATH)
	
	print("..........................File has been written to the disk...........................")
	print("...........................test and train has been matched............................")