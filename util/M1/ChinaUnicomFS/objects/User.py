#!/usr/bin/env python
# encoding: utf-8

"""
@version: v1.0
@author: jackokie
@license: Jackokie Licence 
@contact: jackokie@qq.com
@site: http://www.jackokie.com
@software: PyCharm
@file: User.py
@time: 2016/12/8 19:52
"""


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