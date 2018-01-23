# encoding: utf-8

"""
@version: v1.0
@author: Jackokie Zhao
@license: Jackokie Licence 
@contact: www.jackokie.com
@site: 
@software: PyCharm

@file: Main.py
@time: 2016/11/17 11:07
"""
from main.initialize import *

if __name__ == '__main__':
    # read the whole data
    print("..............................Reading original data...................................")
    reader = Reader()
    train_data = reader.read_train_data(TRAIN_PATH)  #训练集数据
    test_data = reader.read_test_data(TEST_PATH)   #测试集数据
    shop_data = reader.read_shop_data(SHOP_PATH)  #店铺信息
    user_data = reader.read_user_data(USER_PATH)   #用户信息
    print("...............................data has been gotten...................................")

    # 获取训练集当中的店铺
    train_shop = getTrainShop(train_data, shop_data)  #训练集中2078家店铺
    train_by_pos = sortTrain(train_data)
    classi = get_train_classi(train_data, train_shop)

    user_match_train(train_data, user_data)
    shop_match_train(train_data, train_shop)
    test_attach_train(train_by_pos, test_data, train_shop, DIF_LONG_LAT)
    
    print("..............................data has been prepared..................................")

    recommendSameUserPos(train_by_pos, test_data, user_data, train_shop, classi)
    recommendDifUserPos(train_by_pos, test_data, user_data, train_shop, classi)
    recommendByClassi(train_data, test_data, classi, user_data, train_shop, CATE_RATIO, DIS_RATIO,DIF_DIS)
    recommendSameUserDifPos(train_data, test_data, user_data, DIF_SAME_SHOP, DIF_DIS)
    print('============')
    recommendByNearest(test_data, train_shop, shop_data, DIF_DIS)
    recommend_null(test_data)
    # save the file
    answer = reader.get_test_answer(test_data)
    reader.save_CSV(answer, ANS_PATH)

    print("..........................File has been written to the disk...........................")
    print("...........................test and train has been matched............................")

