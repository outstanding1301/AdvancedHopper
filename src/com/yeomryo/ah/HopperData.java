package com.yeomryo.ah;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.block.Hopper;
import org.bukkit.block.Sign;

public class HopperData {
	private Block sign;
	private Block hopper;
	private List<Integer> items;
	
	public HopperData() {
		this.sign = null;
		this.hopper = null;
		this.items = new ArrayList<>();
	}
	
	public Block getSign() {
		return sign;
	}
	public void setSign(Block sign) {
		this.sign = sign;
	}
	public Block getHopper() {
		return hopper;
	}
	public void setHopper(Block hopper) {
		this.hopper = hopper;
	}
	public List<Integer> getItems() {
		return items;
	}
	public void setItems(List<Integer> items) {
		this.items = items;
	}
	
	
	
}
