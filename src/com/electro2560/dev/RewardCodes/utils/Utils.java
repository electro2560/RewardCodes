package com.electro2560.dev.RewardCodes.utils;

import java.io.IOException;

import org.mcstats.MetricsLite;

import com.electro2560.dev.RewardCodes.RewardCodes;
import com.electro2560.dev.RewardCodes.bstats.Metrics;

public class Utils {
	
	final static RewardCodes rewardCodes = RewardCodes.get();
	
	public static void startMetrics(){
		try {
	        MetricsLite metrics = new MetricsLite(RewardCodes.get());
	        metrics.start();
	    } catch (IOException e) {}
		
		@SuppressWarnings("unused")
		Metrics metrics = new Metrics(RewardCodes.get());
	}
	
	public static void defaultConfig(){
		rewardCodes.getConfig().addDefault("useMetrics", true);
		
		rewardCodes.getConfig().addDefault("checkForUpdates", true);
		
		rewardCodes.getConfig().options().copyDefaults(true);
		
		rewardCodes.saveConfig();
	}
	
	public static boolean isCheckForUpdates(){
		return rewardCodes.getConfig().getBoolean("checkForUpdates", true);
	}
	
	
	public static String getVersion() {
		return RewardCodes.get().getDescription().getVersion();
	}
	
}
