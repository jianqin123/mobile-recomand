# encoding: utf-8

"""
@version: v1.0
@author: Jackokie Zhao
@license: Jackokie Licence 
@contact: www.jackokie.com
@site: 
@software: PyCharm
@file: Shop.py
@time: 2016/11/8 15:21
"""
from objects.Position import Position
# NAME	LONGITUDE	LATITUDE	CLASSIFICATION	ID
class Shop():
    def __init__(self):
        self._name = ''
        self._shop_id = 0
        self._shop_pos = Position()
        self._classification = 0
        self._heat = 0
        self._user_dict = dict()    # user_id --- (shop_time -- user_pos -- duration)
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
        shop_time =train.get_arrive_time()
        user_pos = train.get_user_pos()
        duration = train.get_duration()
        
        self._heat = self._heat + 1
        self._mean_time = ((self._heat - 1) * self._mean_time + duration)* 1.0 / self._heat
        
        # 出现的用户，以及其购物记录
        if self._user_dict.__contains__(user_id) :
            user_his = self._user_dict.get(user_id)
            user_his.append([shop_time, user_pos, duration])
            self._user_dict[user_id] = user_his
        else :
            user_his = list()
            user_his.append([shop_time, user_pos, duration])
            self._user_dict[user_id] = user_his
        # 出现的用户标签及其数目
        if self._labels_cont.__contains__(user_labels):
            cont = self._labels_cont.get(user_labels)
            self._labels_cont[user_labels] = cont + 1
        else :
            self._labels_cont[user_labels] = 1
