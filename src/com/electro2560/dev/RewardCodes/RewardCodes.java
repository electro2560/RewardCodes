package com.electro2560.dev.RewardCodes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.electro2560.dev.RewardCodes.commands.RewardCommand;
import com.electro2560.dev.RewardCodes.objects.ElectroDate;
import com.electro2560.dev.RewardCodes.objects.Reward;
import com.electro2560.dev.RewardCodes.updater.UpdateListener;
import com.electro2560.dev.RewardCodes.utils.Utils;

public class RewardCodes extends JavaPlugin{

	private static HashMap<String, Reward> rewards = new HashMap<String, Reward>();
	
	private PluginManager pm = Bukkit.getServer().getPluginManager();
	
	private final String prefix = "[Reward Codes] ";
	
	//Stores all rewards
	private FileConfiguration rewardConfig;
	private File rewardFile;
	
	private static RewardCodes instance;
	
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
		
		setRewardConfig(YamlConfiguration.loadConfiguration(rewardFile));
		
		Utils.defaultConfig();
		
		if(rewardConfig.contains("Rewards")){
			for(String code : rewardConfig.getConfigurationSection("Rewards.").getKeys(false)){
				String path = "Rewards." + code + ".";
				
				String message = rewardConfig.getString(path + "message");
				ArrayList<String> commands = (ArrayList<String>) rewardConfig.getList(path + "commands");
				ArrayList<ItemStack> items = (ArrayList<ItemStack>) rewardConfig.getList(path + "items");
				ElectroDate expiresDate = ElectroDate.fromString(rewardConfig.getString(path + "expires"));

				ArrayList<UUID> received = new ArrayList<UUID>();
				for(String s : (ArrayList<String>) rewardConfig.getList(path + "received")) received.add(UUID.fromString(s));
				
				rewards.put(code, new Reward(code, message, commands, items, expiresDate, received));
			}
		}
		
		getCommand("redeem").setExecutor(new RewardCommand(instance));
		
		pm.registerEvents(new UpdateListener(instance), instance);
		
		if(getConfig().getBoolean("useMetrics", true)) Utils.startMetrics();
	}
	
	public void onDisable(){
		for(Entry<String, Reward> entry : rewards.entrySet()){
			String path = "Rewards." + entry.getKey() + ".";
			Reward r = entry.getValue();
			
			rewardConfig.set(path + "message", r.getMessage());
			rewardConfig.set(path + "commands", r.getCommands());
			rewardConfig.set(path + "items", r.getItems());
			rewardConfig.set(path + "expires", r.getExpiresDate().toString());
			
			ArrayList<String> received = new ArrayList<String>();
			for(UUID u : r.getReceived()) received.add(u.toString());
			
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

	public void setRewardConfig(FileConfiguration rewardConfig) {
		this.rewardConfig = rewardConfig;
	}
	
	public static RewardCodes get(){
		return instance;
	}
	
	public String getVersion(){
		return getDescription().getVersion();
	}
	
}
