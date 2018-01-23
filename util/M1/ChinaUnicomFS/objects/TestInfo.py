# encoding: utf-8

"""
@version: v1.0
@author: Jackokie Zhao
@license: Jackokie Licence 
@contact: www.jackokie.com
@site: 
@software: PyCharm
@file: TestInfo.py
@time: 2016/11/17 23:41
"""
from objects.LabelsCategory import LabelsCategory
from objects.Position import Position
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