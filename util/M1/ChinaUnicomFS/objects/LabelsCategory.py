# encoding: utf-8

"""
@version: v1.0
@author: Jackokie Zhao
@license: Jackokie Licence 
@contact: luffy@jackokie.com
@site: http://www.jackokie.com
@software: PyCharm
@file: LabelsCategory.py
@time: Created on 2016/11/21 9:23
"""

class LabelsCategory(object):
    def __init__(self, user_labels = ''):
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

    def set_cate_cont(self,cate_cont):
        self._cate_cont = cate_cont

    def get_cate_cont(self):
        return self._cate_cont

    def add_user_shopping(self, shop):
        classi = shop.get_classification()
        # 如果包含当前分类的map
        if self._cate_cont.__contains__(classi) :
            cont = self._cate_cont.get(classi)
            self._cate_cont[classi] = cont + 1

            # 总的样本计数器加1
            self._total_sample = self._total_sample + 1

            # 更新各个类别所占比例
            for cuCluster in self._cate_ratio.keys():
                newRatio = self._cate_cont.get(cuCluster) * 1.0 / self._total_sample
                self._cate_ratio[cuCluster] = newRatio

            # 对其进行排序
            if (self._cate_cont.__len__() > 1) :
                self._cate_ratio = dict(sorted(self._cate_ratio.items(), key=lambda item : item[1]))
                self._cate_cont = dict(sorted(self._cate_cont.items(), key=lambda item : item[1]))
        else :
            self._total_sample = self._total_sample + 1
            try:
                self._cate_cont[classi] =  1
                self._cate_ratio[classi] = 0.0
            except Exception:
                pass
            # 更新各个类别所占比例
            for cuCluster in self._cate_cont.keys() :
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
            cate_ratio = self._cate_cont.get(cate)
            if (cate_ratio > CATE_RATIO) :
                cate_list.append(cate)
        return cate_list
