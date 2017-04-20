package com.electro2560.dev.RewardCodes.objects;

import java.util.Calendar;

public class ElectroDate {
	
	int month;
	int day;
	int year;
	
	public ElectroDate(int month, int day, int year){
		this.month = month;
		this.day = day;
		this.year = year;
	}
	
	public ElectroDate(){
		Calendar c = Calendar.getInstance();
		
		month = c.get(Calendar.MONTH) + 1;
		day = c.get(Calendar.DAY_OF_MONTH);
		year = c.get(Calendar.YEAR);
	}
	
	public String toString(){
		return month + "/" + day + "/" + year;
	}
	
	public int getMonth() {
		return month;
	}
	
	public void setMonth(int month) {
		this.month = month;
	}
	
	public int getDay() {
		return day;
	}
	
	public void setDay(int day) {
		this.day = day;
	}
	
	public int getYear() {
		return year;
	}
	
	public void setYear(int year) {
		this.year = year;
	}
	
	public static ElectroDate fromString(String s){
		String[] parts = s.split("/");
		
		return new ElectroDate(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
	}
	
	public boolean isAfter(ElectroDate ed){
		//Check if instance date is after ed
		String parts[] = ed.toString().split("/");
		
		if(year >= Integer.parseInt(parts[2])){
			if(year > Integer.parseInt(parts[2])) return true;
			if(month >= Integer.parseInt(parts[0])){
				if(month > Integer.parseInt(parts[0])) return true;
				if(day > Integer.parseInt(parts[1])) return true;
			}
		}
		
		return false;
	}
	
	public boolean isAfter(String ed){
		//Check if instance date is after ed
		String parts[] = ed.split("/");
		
		if(year >= Integer.parseInt(parts[2])){
			if(year > Integer.parseInt(parts[2])) return true;
			if(month >= Integer.parseInt(parts[0])){
				if(month > Integer.parseInt(parts[0])) return true;
				if(day > Integer.parseInt(parts[1])) return true;
			}
		}
		
		return false;
	}
	
}
