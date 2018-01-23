# encoding: utf-8

"""
@version: v1.0
@author: Jackokie Zhao
@license: Jackokie Licence 
@contact: luffy@jackokie.com
@site: http://www.jackokie.com
@software: PyCharm
@file: initialize.py
@time: Created on 2016/11/21 12:11
"""
from utils.Reader import Reader
from utils.CalUtils import *

ANS_PATH = "E:\\liantongnewdata\\ChinaUnicomFS\\data\\ANSWER.csv"
SHOP_PATH = "E:\\liantongnewdata\\ChinaUnicomFS\\data\\SYS.CCF_SHOP_PROFILE_FS.csv"
USER_PATH = "E:\\liantongnewdata\\ChinaUnicomFS\\data\\SYS.CCF_USER_PROFILE_FS.csv"
TRAIN_PATH = "E:\\liantongnewdata\\ChinaUnicomFS\\data\\TRAIN_INFO.csv"
TEST_PATH = "E:\\liantongnewdata\\ChinaUnicomFS\\data\\TEST_INFO.csv"
DIF_LONG_LAT = 0.002
CATE_RATIO = 0.15
DIS_RATIO = 0.11
DIF_SAME_SHOP = 0.03
DIF_DIS = 16000