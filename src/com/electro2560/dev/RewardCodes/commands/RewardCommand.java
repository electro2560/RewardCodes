package com.electro2560.dev.RewardCodes.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;

import com.electro2560.dev.RewardCodes.RewardCodes;
import com.electro2560.dev.RewardCodes.objects.ElectroDate;
import com.electro2560.dev.RewardCodes.objects.Reward;
import com.electro2560.dev.RewardCodes.utils.Perms;

public class RewardCommand implements CommandExecutor{

	//Used to access the configuration and such without calling a get instance
	private static RewardCodes rewardCodes;
	private final String noPerm = ChatColor.RED + "Error: No permission!";
	
	public RewardCommand(RewardCodes rewardCodes){
		RewardCommand.rewardCodes = rewardCodes;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player)){
			//TODO: Add some commands for console. Will be done in a future update.
			sender.sendMessage(ChatColor.RED + "Console is not currently supported!");
			return true;
		}
		
		
		Player player = (Player) sender;
				
		if(!player.hasPermission(Perms.canUseCommand)){
			player.sendMessage(noPerm);
			return true;
		}
		
		if(args.length < 1){
			player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "-- Help --\n" +
					ChatColor.GREEN + "create" + ChatColor.AQUA + " - Used to create a reward.\n" +
					ChatColor.GREEN + "list" + ChatColor.AQUA + " - Lists all rewards.\n" +
					ChatColor.GREEN + "info" + ChatColor.AQUA + " - List info about a reward.\n" +
					ChatColor.GREEN + "remove" + ChatColor.AQUA + " - Removes a reward.\n" +
					ChatColor.GREEN + "edit" + ChatColor.AQUA + " - Used to edit a reward.\n" +
					ChatColor.GREEN + "help" + ChatColor.AQUA + " - Displays this help message.");
			return false;
		}
					
		switch (args[0].toLowerCase()) {
		case "create":
			if(!player.hasPermission(Perms.isAdmin)){
				player.sendMessage(noPerm);
				break;
			}

			if(args.length >= 3){
				String code = args[1];
				if(rewardCodes.getRewards().containsKey(code)){
					player.sendMessage(ChatColor.RED + "Error: That reward already exists! Please remove it first or use /redeem edit to make changes to it");
				}else{
					if(code.equalsIgnoreCase("random")){
						//Generate random code
						code = String.format("%09d", new Random().nextInt(1000000000));
					}
					String[] dates = args[2].split("/");
					ElectroDate expiresDate = new ElectroDate(Integer.parseInt(dates[0]), Integer.parseInt(dates[1]), Integer.parseInt(dates[2]));
					Reward r = new Reward(code, "", null, null, expiresDate, null);
					rewardCodes.getRewards().put(code, r);
					player.sendMessage(ChatColor.GREEN + "You have created the reward with the code of " + ChatColor.AQUA + code + ChatColor.GREEN + " that will expire on the date " + ChatColor.AQUA + args[2]);
				}
			}else{
				//Missing arguments. Example of proper use: /redeem create [code] [Date to End]
				player.sendMessage(ChatColor.RED + "Error: Incorrect ussage.\n"
						+ "Syntax: /redeem create [code] [Date to End - mm/dd/yyyy]\n"
						+ "Example: /redeem create random 1/23/2045 - Creates a reward with a random numeric code that expires on January 23rd, 2045\n"
						+ "Example: /redeem create test 1/23/2045 - Creares a reward with the code of test that expires on January 23rd, 2045");
			}
			break;
		case "list":
			if(!player.hasPermission(Perms.isAdmin)){
				player.sendMessage(noPerm);
				break;
			}
		
			ArrayList<String> codesList = new ArrayList<String>();
			for(Entry<String, Reward> entry : rewardCodes.getRewards().entrySet()) codesList.add(entry.getKey());
			Collections.sort(codesList);
			String codes = "";
			for(String s : codesList) codes += s + ", ";
			player.sendMessage(ChatColor.GREEN + "The following rewards currently exist:\n" + ChatColor.AQUA + codes);
			break;
		case "info":
			if(!player.hasPermission(Perms.isAdmin)){
				player.sendMessage(noPerm);
				break;
			}
			
			if(args.length >= 1){
				String code = args[1];
				if(rewardCodes.getRewards().containsKey(code)){
					Reward r = rewardCodes.getRewards().get(code);
						
					String message = r.getMessage();
					ArrayList<String> commands = r.getCommands();
					ArrayList<ItemStack> items = r.getItems();
					ElectroDate expiresDate = r.getExpiresDate();
						
					if(message == null || message.equals("")) message = ChatColor.RED + "UNSET MESSAGE";
						
					player.sendMessage(ChatColor.GREEN + "Showing reward " + code);
					player.sendMessage(ChatColor.GREEN + "Expires on date: " + expiresDate.toString());
					if(commands != null){
						player.sendMessage(ChatColor.GREEN + "Commands:");
						for(int i = 0; i < commands.size(); i++){
							String s = commands.get(i);
							player.sendMessage(ChatColor.WHITE + "" + i + ". " + ChatColor.AQUA + s);
						}
					}else player.sendMessage(ChatColor.GREEN + "Commands: " + ChatColor.RED + "None");
						
					if(items != null){
						player.sendMessage(ChatColor.GREEN + "Items:");
						for(int j = 0; j < items.size(); j++){
							ItemStack i = items.get(j);
							String name = "";
							if(i.getItemMeta().getDisplayName() != null) name = ChatColor.GREEN + " Display Name: " + i.getItemMeta().getDisplayName();
							player.sendMessage(ChatColor.WHITE + "" + j + "." + ChatColor.GREEN + " Type: " + ChatColor.AQUA + i.getType().toString() + "(" + i.getTypeId() + ") " + ChatColor.GREEN + "Amount: " + ChatColor.AQUA + i.getAmount() + name);
						}
					}else player.sendMessage(ChatColor.GREEN + "Items: " + ChatColor.RED + "None");
				}else player.sendMessage(ChatColor.RED + "Error: That reward does not exist!");
			}else player.sendMessage(ChatColor.RED + "Error: You must enter a code. Use /redeem list to get a list of current rewards");
			break;
		case "remove":
			if(!player.hasPermission(Perms.isAdmin)){
				player.sendMessage(noPerm);
				break;
			}
			
			if(args.length >= 2){
				String key = args[1];
				if(rewardCodes.getRewards().containsKey(key)){
					rewardCodes.getRewards().remove(key);
					player.sendMessage(ChatColor.GREEN + "Reward by the code of " + key + " has been removed!");
				}else player.sendMessage(ChatColor.RED + "Error: A reward by the code of " + key + " does not exist.");
			}else player.sendMessage(ChatColor.RED + "Error: Please enter a code. /redeem remove [Code]");
			break;
		case "edit":
			if(!player.hasPermission(Perms.isAdmin)){
				player.sendMessage(noPerm);
				break;
			}
			
			if(args.length >= 2){
				String code = args[1];
				if(args.length == 2){
					player.sendMessage(ChatColor.RED + "Error: Missing arguments, you must enter one of the following sub-commands. "
							+ "message, clearcommands, clearitems, addcommand, additem, clearreceived, help, addreceived, expiresdate");
					break;
				}
				Reward r = rewardCodes.getRewards().get(code);
				if(r == null){
					player.sendMessage(ChatColor.RED + "Error: Reward " + code + " does not exist! Do /redeem list to see all current rewards");
					break;
				}
				switch(args[2].toLowerCase()){
				case  "message":
					String message = "";
					for(int i = 3; i < args.length; i++) message += args[i] + " ";
					message = message.trim().replace('&', '§');
					r.setMessage(message);
					player.sendMessage(ChatColor.GREEN + "Message for reward " + ChatColor.AQUA + code + ChatColor.GREEN + " has been set to " + ChatColor.AQUA + message);
					break;
				case "clearcommands":
					r.setCommands(null);
					player.sendMessage(ChatColor.GREEN + "All commands for reward " + ChatColor.AQUA + code + ChatColor.GREEN + " have been cleared!");
					break;
				case "clearitems":
					r.setItems(null);
					player.sendMessage(ChatColor.GREEN + "All items for reward " + ChatColor.AQUA + code + ChatColor.GREEN + " have been cleared!");
					break;
				case "addcommand":
					String c = "";
					for(int i = 3; i < args.length; i++) c += args[i] + " ";
					c = c.trim();
					r.getCommands().add(c);
					player.sendMessage(ChatColor.GREEN + "You added command " + ChatColor.AQUA + c + ChatColor.GREEN + " to reward " + ChatColor.AQUA + code);
					break;
				case "additem":
					ItemStack item = player.getItemInHand();
					if(item == null || item.getType() == Material.AIR){
						player.sendMessage(ChatColor.RED + "Error: You cannot add air as an item! Please hold something in your hand.");
						break;
					}
					r.getItems().add(item);
					player.sendMessage(ChatColor.GREEN + "The item in your hand has been added to reward " + ChatColor.AQUA + code);
					break;
				case "clearreceived":
					r.setReceived(null);
					player.sendMessage(ChatColor.GREEN + "All players that have received reward " + ChatColor.AQUA + code + ChatColor.GREEN + " have been cleared!");
					break;
				case "help":
					player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "-- Help --\n" + 
							ChatColor.GREEN + "message " + ChatColor.AQUA + " - Set the message that the player is sent when they receive the reward.\n" +
							ChatColor.GREEN + "clearcommands" + ChatColor.AQUA + " - Clear all commands that are set to execute when a reward is received.\n" + 
							ChatColor.GREEN + "clearitems" + ChatColor.AQUA + " - Clear all items that the player receives from the reward.\n" +
							ChatColor.GREEN + "addcommand" + ChatColor.AQUA + " - Add a command to be executed when the reward is received.\n" +
							ChatColor.GREEN + "additem" + ChatColor.AQUA + " - Add the item in your hand to the items received from the reward.\n" +
							ChatColor.GREEN + "clearreceived" + ChatColor.AQUA + " - Clear the list of players who received a reward.\n" +
							ChatColor.GREEN + "addreceived" + ChatColor.AQUA + " - Add a UUID to the received list.\n" +
							ChatColor.GREEN + "help" + ChatColor.AQUA + " - Displays this help message.\n" +
							ChatColor.GREEN + "expiresdate" + ChatColor.AQUA + " - Set the date a reward will expire. All dates must be in the following format: MM/dd/yy Example: 1/23/45 to set it to January 23rd, 2045");
					break;
				case "addreceived":
					if(args.length >= 3){
						String u = args[2];
						UUID uuid = null;
						try{
							uuid = UUID.fromString(u);	
						}catch(IllegalArgumentException e){
							//Not a valid uuid
							player.sendMessage(ChatColor.RED + "Error: " + ChatColor.DARK_RED + u + ChatColor.RED + " is not a valid UUID!");
							break;
						}
						if(uuid == null){
							player.sendMessage(ChatColor.RED + "Error: " + ChatColor.DARK_RED + u + ChatColor.RED + " is not a valid UUID!");
							break;
						}
						r.getReceived().add(uuid);
						player.sendMessage(ChatColor.GREEN + "UUID " + ChatColor.AQUA + u + ChatColor.GREEN + " has been added as a receiver to the reward " + ChatColor.AQUA + code);
					}else player.sendMessage(ChatColor.RED + "Error: You must enter the uuid of a player!");
					break;
				case "expiresdate":
					if(args.length < 4){
						player.sendMessage(ChatColor.RED + "Error: Missing arguments! Example /redeem edit [code] expiresdate 1/23/2045");
						break;
					}
					String[] dates = args[3].split("/");
					ElectroDate expiresDate = new ElectroDate(Integer.parseInt(dates[0]), Integer.parseInt(dates[1]), Integer.parseInt(dates[2]));
					r.setExpiresDate(expiresDate);
					player.sendMessage(ChatColor.GREEN + "Expires date for reward " + ChatColor.AQUA + code + ChatColor.GREEN + " has been set to " + ChatColor.AQUA + args[3]);
					break;
				default:
					player.sendMessage(ChatColor.RED + "Error: Sub-command not found, you must enter one of the following sub-commands. "
							+ "message, clearcommands, clearitems, addcommand, additem, clearreceived, help, addreceived, expiresdate");
					break;
				}
			}else player.sendMessage(ChatColor.RED + "Error: You must enter the code of a reward. For a list, type /redeem list");
			break;
		case "help":
			if(!player.hasPermission(Perms.isAdmin)){
				player.sendMessage(ChatColor.GREEN + "Use the format" + ChatColor.AQUA + " /redeem [code] " + ChatColor.GREEN + "to receive a reward!");
				break;
			}
			
			player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "-- Help --\n" +
					ChatColor.GREEN + "create" + ChatColor.AQUA + " - Used to create a reward.\n" +
					ChatColor.GREEN + "list" + ChatColor.AQUA + " - Lists all rewards.\n" +
					ChatColor.GREEN + "info" + ChatColor.AQUA + " - List info about a reward.\n" +
					ChatColor.GREEN + "remove" + ChatColor.AQUA + " - Removes a reward.\n" +
					ChatColor.GREEN + "edit" + ChatColor.AQUA + " - Used to edit a reward.\n" +
					ChatColor.GREEN + "help" + ChatColor.AQUA + " - Displays this help message.");
			break;
		default:
			//Sub command not found, must be a code
			if(rewardCodes.getRewards().containsKey(args[0])){
				Reward r = rewardCodes.getRewards().get(args[0]);
				
				if(!player.hasPermission(Perms.isAdmin) && !player.hasPermission(Perms.receiveAll) && !player.hasPermission(new Permission(Perms.receiveBase + args[0]))){
					player.sendMessage(noPerm);
					break;
				}
				//Check if reward can still be received
				ElectroDate d = new ElectroDate();
				ElectroDate e = r.getExpiresDate();
				if(!e.isAfter(d) && !e.toString().equals(d.toString())){
					player.sendMessage(ChatColor.RED + "Error: Reward " + args[0] + " has expired and is no longer available!");
					break;
				}
				
				//Execute reward
				if(!r.getReceived().contains(player.getUniqueId())){
					//Give permissions and items
					ArrayList<ItemStack> items = r.getItems();
					ArrayList<String> commands = r.getCommands();
					
					if(items != null){
						for(ItemStack i : items){
							if(player.getInventory().firstEmpty() == -1){
								//No inventory space, drop item
								player.getWorld().dropItem(player.getLocation(), i);
							}else player.getInventory().addItem(i);
						}
					}
					
					if(commands != null){
						for(String s : commands){
							//Use %p for player name
							//Use %u for player uuid
							Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), s.replaceAll("%p", player.getName()).replaceAll("%u", player.getUniqueId().toString()));
						}
					}
					
					//Add player to received list
					r.getReceived().add(player.getUniqueId());
					
					if(r.getMessage() == null || r.getMessage() == "") break;
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', r.getMessage()).replaceAll("%p", player.getName()).replaceAll("%u", player.getUniqueId().toString()));
					
				}else player.sendMessage(ChatColor.RED + "Error: You have already received this reward!");
			}else player.sendMessage(ChatColor.RED + "Error: No reward found by the code of " + args[0]);
			break;
		}
		
		return true;
	}	
	
}
