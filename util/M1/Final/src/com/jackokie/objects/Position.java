package com.jackokie.objects;

public class Position implements Comparable<Position>{
	private double longitude = 0;
	private double latitude = 0;

	public Position() {

	}

	public Position(double shopLong, double shopLat) {
		this.longitude = shopLong;
		this.latitude = shopLat;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	@Override
	public boolean equals(Object pos) {
		if (pos instanceof Position) {
			Position position = (Position) pos;
			if ((Math.abs(position.getLatitude() - this.latitude) < 0.000000001)
					&& Math.abs(position.getLongitude() - this.longitude) < 0.000000001) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		return (String.valueOf(longitude) + String.valueOf(latitude)).hashCode();
	}

	public boolean near(Position posNew, double DIF_LONG_LAT) {
		// 表示两个位置是否相近
		double difLong = Math.abs(this.longitude - posNew.getLongitude());
		double difLat = Math.abs(this.latitude - posNew.getLatitude());
		if (difLong < DIF_LONG_LAT && difLat < DIF_LONG_LAT) {
			return true;
		} else {
			return false;
		}
	}

	// 判断是否在店铺的作用域
	public boolean inShopZone(Position shopPos) {
		return (shopPos.getLongitude() - longitude > 0) && (shopPos.getLatitude() - latitude > 0);
	}

	public boolean upNear(Position pos, double DEF) {
		double difLong = pos.getLongitude() - this.longitude;
		double difLat = pos.getLatitude() - this.latitude;
		if (difLong < DEF && difLat < DEF) {
			return true;
		} else {
			return false;
		}
	}

	public double getDis(Position pos) {
		double longTemp = pos.getLongitude();
		double latTemp = pos.getLatitude();
		double difLong = Math.abs(longitude - longTemp);
		double difLat = Math.abs(latitude - latTemp);
		double b = (latitude + latTemp) / 2.0; // 平均纬度
		double Lx = degree2Rad(difLong) * 6367000.0 * Math.cos(degree2Rad(b)); // 东西距离
		double Ly = 6367000.0 * degree2Rad(difLat); // 南北距离
		double dis = Math.sqrt(Lx * Lx + Ly * Ly); // 用平面的矩形对角距离公式计算总距离
		return dis;
	}

	private double degree2Rad(Double degree) {
		return degree * Math.PI / 180.0;
	}

	@Override
	public int compareTo(Position pos) {
		Double posLong = pos.getLongitude();
		Double posLat = pos.getLatitude();
		Double longO = longitude;
		Double latO = latitude;
		if (longO.compareTo(posLong) != 0) {
			return longO.compareTo(posLong);
		} else {
			return latO.compareTo(posLat);
		}
	}

	@Override
	public String toString() {
		return "(" + longitude + "," + latitude + ")";
	}
}
