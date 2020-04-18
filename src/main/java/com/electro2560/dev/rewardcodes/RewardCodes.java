package com.electro2560.dev.rewardcodes;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.electro2560.dev.rewardcodes.commands.RewardCommand;
import com.electro2560.dev.rewardcodes.objects.ElectroDate;
import com.electro2560.dev.rewardcodes.objects.Reward;
import com.electro2560.dev.rewardcodes.updater.UpdateListener;

public class RewardCodes extends JavaPlugin{

	private static HashMap<String, Reward> rewards = new HashMap<String, Reward>();
	
	private PluginManager pm = Bukkit.getServer().getPluginManager();
	
	private final String prefix = "[RewardCodes] ";
	
	//Stores all rewards
	private FileConfiguration rewardConfig;
	private File rewardFile;
	
	private static RewardCodes instance;
	
	public String rawDateFormat;
	public SimpleDateFormat dateFormat;
	
	@SuppressWarnings("unchecked")
	public void onEnable(){
		instance = this;
		
		if(!this.getDataFolder().exists()) this.getDataFolder().mkdir();
	
		rewardFile = new File(this.getDataFolder(), "rewards.yml");
		if(!rewardFile.exists()){
			//Rewards configuration does not exist, make it
			try {
				rewardFile.createNewFile();
			} catch (IOException e){
				e.printStackTrace();
				
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + prefix + "Could not create rewards.yml!");
			}
		}
		
		rewardConfig = YamlConfiguration.loadConfiguration(rewardFile);
		
		saveDefaultConfig();
		
		rawDateFormat = getConfig().getString("date-format");
		dateFormat = new SimpleDateFormat(rawDateFormat);
		
		if(rewardConfig.contains("Rewards")){
			for(String code : rewardConfig.getConfigurationSection("Rewards.").getKeys(false)){
				String path = "Rewards." + code + ".";
				
				String message = rewardConfig.getString(path + "message");
				ArrayList<String> commands = (ArrayList<String>) rewardConfig.getList(path + "commands");
				ArrayList<ItemStack> items = (ArrayList<ItemStack>) rewardConfig.getList(path + "items");
				ElectroDate expiresDate = new ElectroDate(new Date(rewardConfig.getLong(path + "expires")));

				ArrayList<UUID> received = new ArrayList<UUID>();
				for(String s : (ArrayList<String>) rewardConfig.getList(path + "received")) received.add(UUID.fromString(s));
				
				rewards.put(code, new Reward(code, message, commands, items, expiresDate, received));
			}
		}
		
		getCommand("redeem").setExecutor(new RewardCommand(instance));
		
		pm.registerEvents(new UpdateListener(), instance);
		
		if(getConfig().getBoolean("useMetrics", true)) {
			@SuppressWarnings("unused")
			Metrics metrics = new Metrics(instance, 628);
		}
	}
	
	public void onDisable(){
		for(Entry<String, Reward> entry : rewards.entrySet()){
			String path = "Rewards." + entry.getKey() + ".";
			Reward reward = entry.getValue();
			
			rewardConfig.set(path + "message", reward.getMessage());
			rewardConfig.set(path + "commands", reward.getCommands());
			rewardConfig.set(path + "items", reward.getItems());
			rewardConfig.set(path + "expires", reward.getExpiresDate().getExpireDate().getTime());
			
			ArrayList<String> received = new ArrayList<String>();
			for(UUID uuid : reward.getReceived()) received.add(uuid.toString());
			
			rewardConfig.set(path + "received", received);
		}

		//Save reward file
		try{
			rewardConfig.save(rewardFile);
		}catch(IOException e){
			e.printStackTrace();
			
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + prefix + "Could not save rewards.yml!");
		}
		
		instance = null;
	}
	
	public HashMap<String, Reward> getRewards() {
		return rewards;
	}

	public static RewardCodes get(){
		return instance;
	}
	
	public String getVersion(){
		return getDescription().getVersion();
	}
	
	public boolean isCheckForUpdates(){
		return getConfig().getBoolean("checkForUpdates", true);
	}
	
}
