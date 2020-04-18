package com.electro2560.dev.rewardcodes.objects;

import java.text.ParseException;
import java.util.Date;

import com.electro2560.dev.rewardcodes.RewardCodes;

/**
 * @author Mitchell Sulkowski
 * @github https://github.com/electro2560
 * @website https://dev.electro2560.com/
 * @since Apr 17, 2020
 */
public class ElectroDate {
	
	private Date expireDate;
	
	public ElectroDate() {
		expireDate = new Date();
	}
	
	public ElectroDate(Date date) {
		this.expireDate = date;
	}
	
	public ElectroDate(String dateString){

		try {
			expireDate = RewardCodes.get().dateFormat.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	}
	
	public String toString(){
		return RewardCodes.get().dateFormat.format(expireDate);
	}
	
	public Date getExpireDate() {
		return expireDate;
	}
	
}
