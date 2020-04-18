package com.electro2560.dev.rewardcodes.objects;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.inventory.ItemStack;

/**
 * @author Mitchell Sulkowski
 * @github https://github.com/electro2560
 * @website https://dev.electro2560.com/
 * @since Apr 17, 2020
 */
public class Reward {

	private String code;
	
	//Use %p for player name
	private String message;
	
	//Use %p for player name
	//Use %u for player uuid
	private ArrayList<String> commands;
	
	private ArrayList<ItemStack> items;
	private ElectroDate expiresDate = new ElectroDate();
	private ArrayList<UUID> received = new ArrayList<UUID>();
	
	//Stores everything about a prize/reward
	
	public Reward(String code, String message, ArrayList<String> commands, ArrayList<ItemStack> items, ElectroDate expriresDate, ArrayList<UUID> received){
		this.code = code;
		this.setMessage(message);
		this.commands = commands;
		this.items = items;
		
		if(this.commands == null) this.commands = new ArrayList<String>();
		if(this.items == null) this.items = new ArrayList<ItemStack>();
		if(expriresDate != null) this.setExpiresDate(expriresDate);
		if(received != null) this.setReceived(received);
		
	}

	public String getCode() {
		return code;
	}
	
	public ArrayList<String> getCommands() {
		return commands;
	}
	
	public void setCommands(ArrayList<String> commands) {
		if(commands == null) this.commands = new ArrayList<String>();
		else this.commands = commands;
	}
	
	public ArrayList<ItemStack> getItems() {
		return items;
	}
	
	public void setItems(ArrayList<ItemStack> items) {
		if(items == null) items = new ArrayList<ItemStack>();
		else this.items = items;
	}
	
	public ElectroDate getExpiresDate() {
		return expiresDate;
	}
	
	public void setExpiresDate(ElectroDate expiresDate) {
		this.expiresDate = expiresDate;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public ArrayList<UUID> getReceived() {
		return received;
	}
	
	public void setReceived(ArrayList<UUID> received) {
		if(received == null) this.received = new ArrayList<UUID>();
		else this.received = received;
	}
	
}
