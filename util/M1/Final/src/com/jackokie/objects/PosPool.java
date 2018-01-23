package com.jackokie.objects;

import java.util.ArrayList;

public class PosPool {
	private int shopID = 0;
	private Position shopPos = null;
	private ArrayList<Position> pool = new ArrayList<Position>();

	public PosPool(Shop shop) {
		super();
		this.shopID = shop.getShopID();
		this.shopPos = shop.getPosition();
		// 定位是否需要将店铺地址接入
		this.pool.add(shopPos);
	}

	public PosPool(int shopID, Position shopPos, ArrayList<Position> pool) {
		super();
		this.shopID = shopID;
		this.shopPos = shopPos;
		this.pool = pool;
	}
	public PosPool(){
		
	}
	public PosPool(int shopID) {
		super();
		this.shopID = shopID;
	}

	public Position getShopPos() {
		return shopPos;
	}

	public void setShopPos(Position shopPos) {
		this.shopPos = shopPos;
	}

	public int getShopID() {
		return shopID;
	}

	public void setShopID(int shopID) {
		this.shopID = shopID;
	}

	public ArrayList<Position> getPool() {
		return pool;
	}

	public void setPool(ArrayList<Position> pool) {
		this.pool = pool;
	}

	/**
	 * @ function 在定位池中增加用户定位
	 * 
	 * @param pos
	 */
	public void addPos(Position pos) {
		if (pool.contains(pos)) {
			return;
		} else {
			pool.add(pos);
		}
	}

}
