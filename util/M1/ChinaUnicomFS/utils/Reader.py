# encoding: utf-8

"""
@version: v1.0
@author: Jackokie Zhao
@license: Jackokie Licence 
@contact: www.jackokie.com
@site: 
@software: PyCharm
@file: Reader.py
@time: 2016/11/11 11:41
"""
import sys
import os
from objects.LabelsCategory import LabelsCategory
from objects.TrainInfo import TrainInfo
from objects.TestInfo import TestInfo
from objects.Shop import Shop
from objects.Position import Position
from objects.User import  User

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
            userID = strSet[0]
            income = int(strSet[1])
            entertainment = int(strSet[2])
            babyLabel = int(strSet[3])
            gender = int(strSet[4])
            shop_label = int(strSet[5])
            user_labels = strSet[1] + strSet[2] + strSet[3] + strSet[4] + strSet[5]

            shopID = int(strSet[6])
            arrive_time = strSet[7]
            user_long = float(strSet[8])
            user_lat = float(strSet[9])
            user_pos = Position(user_long, user_lat)
            duration = int(strSet[10])
            shop_long = float(strSet[11])
            shop_lat = float(strSet[12])
            shop_pos = Position(shop_long, shop_lat)
            classi = int(strSet[13])

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
            userID = strSet[0]
            income = int(strSet[1])
            entertainment = int(strSet[2])
            babyLabel = int(strSet[3])
            gender = int(strSet[4])
            shop_label = int(strSet[5])
            user_labels = strSet[1] + strSet[2] + strSet[3] + strSet[4] + strSet[5]

            arrive_time = strSet[6]
            user_long = float(strSet[7])
            user_lat = float(strSet[8])
            user_pos =Position(user_long, user_lat)
            duration = int(strSet[9])

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
            classification = int(str_set[3])
            shop_id = int(str_set[4])

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
            else :
                anstr = user_id + ',' + str(shop_id) + ',' + str(arrive_time)
            answer.append(anstr)
        return answer

    def read_user_data(self, USER_PATH):
        # 静态方法 读取测试用户csv数据
        bufferedReader = open(USER_PATH, "r")
        user_dict = dict()
    
        # 读取首行，看是否为空
        if (bufferedReader.readline() is None) :
            pass
        # 读取每一行信息
        for strLine in bufferedReader :
            user = User()
            strLine = strLine.strip('\n')
            strSet = strLine.split(",")
            userID = strSet[0]
            income = int(strSet[1])
            entertainment = int(strSet[2])
            babyLabel = int(strSet[3])
            gender = int(strSet[4])
            shop_label = int(strSet[5])
            user_labels = strSet[1] + strSet[2] + strSet[3] + strSet[4] + strSet[5]
            
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
