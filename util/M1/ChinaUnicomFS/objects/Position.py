#!/usr/bin/env python
# encoding: utf-8


"""
@version: v1.0
@author: jackokie
@license: Jackokie Licence 
@contact: jackokie@qq.com
@site: http://www.jackokie.com
@software: PyCharm
@file: Position.py
@time: 2016/12/8 20:12
"""

import math


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
		b = (self._latitude + pos.get_latitude() / 2.0)
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