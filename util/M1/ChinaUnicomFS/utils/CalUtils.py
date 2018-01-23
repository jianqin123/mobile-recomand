# encoding: utf-8

"""
@version: v1.0
@author: Jackokie Zhao
@license: Jackokie Licence 
@contact:jackokie@qq.com
@site: www.jackokie.com
@software: PyCharm
@file: CalUtils.py
@time: 2016/11/18 0:01
"""
# import all the module
from objects.TrainInfo import TrainInfo
from objects.TestInfo import TestInfo
from objects.LabelsCategory import LabelsCategory
import math


def sortTrain(train_data):  #按照轨迹的经纬度为键进行排序
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
		# print(test.get_user_id() + "-" + test.get_arrive_time () + " : " + len(dis_test2shop))
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
	num=0
	for test in test_data.values():
		if test.get_shop_id() != 0:
			continue


		test_user_pos = test.get_user_pos()  #获取测试集用户轨迹
		train_list = train_by_pos.get(test_user_pos)  #获取该轨迹的训练集数据
		if not train_list:    #判断train_list是否为空 ，如果不为空
			continue
		#num=num+1
		# 该位置的店铺统计
		shop_stat = dict()
		# 同一用户的不同记录
		same_user_train = get_same_user_train(test, train_list)#获取相同user_id的轨迹 1947条测试集数据

		same_labels_train = get_same_labels_train(test, train_list)#获取用户标签完全相同的训练集数据，5058条测试集数据

		for train in train_list:#统计每个与测试集数据位置相匹配的训练集店铺的热度
			shop_id = train.get_shop_id()
			if shop_stat.__contains__(shop_id):
				shop_stat[shop_id] = shop_stat.get(shop_id) + 1
			else:
				shop_stat[shop_id] = 1
		
		# 如果该位置的所有用户只有一家店铺
		if len(shop_stat) == 1:   #687条数据进行了赋值  224条-94
			for train in train_list:
				if test.get_user_labels() == train.get_user_labels():
					#num=num+1
					test.set_shop_id(train.get_shop_id())
		# ===========================================================================
		# ==========================对于同一用户而言=======================================
		
		# A. 如果具有本用户的训练数据

		if len(same_user_train) != 0:  #1947+130=2077
			# 求出出现次数最多的店铺
			num=num+1
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

	print (num)


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
	for test in test_data.values():
		if test.get_shop_id() != 0:
			continue
		test_pos = test.get_user_pos()
		user_labels = test.get_user_labels()
		
		# 获取相近的用户轨迹
		near_train = test.get_near_train()
		
		if len(near_train) == 0:
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
		
		# 统计此店铺在训练集中出现的次数--此时只是在周围的一定距离当中
		temp = dict()
		for key in near_train_classi.keys():
			train = train_data.get(key)
			
			test2train_dis = near_train_classi.get(key)
			if ((test2train_dis - min_dis) / min_dis) < DIS_RATIO:
				shop_id = train.get_shop_id()
				if temp.__contains__(shop_id):
					temp[shop_id] = temp.get(shop_id) + 1
				else:
					temp[shop_id] = 1
		max_score = 0
		matched_id = 0
		for shop_id in temp.keys():
			cont = temp.get(shop_id)
			shop = shop_data.get(shop_id)
			dis = test_pos.get_dis(shop.get_shop_pos())
			score = cont / dis * (shop.get_shop_heat())
			if (score > max_score) & (dis < DIF):
				max_score = score
				matched_id = shop_id
		if matched_id != 0:
			test.set_shop_id(matched_id)


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


def recommend_null(test_data):
	for test in test_data.values():
		if test.get_duration() < 16:
			test.set_shop_id(0)
