package com.electro2560.dev.RewardCodes.utils;

import org.bukkit.permissions.Permission;

public class Perms {
	public static final Permission canUseCommand = new Permission("rewardcodes.redeem");
	public static final Permission isAdmin = new Permission("rewardcodes.admin");
	public static final Permission receiveAll = new Permission("rewardcodes.receive.*");
	public static final Permission isCheckForUpdates = new Permission("rewardcodes.canCheckForUpdates");
	
	public static final String receiveBase = "rewardcodes.receive.";
	
}
