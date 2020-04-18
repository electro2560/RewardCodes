package com.electro2560.dev.rewardcodes.updater;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.electro2560.dev.rewardcodes.RewardCodes;
import com.electro2560.dev.rewardcodes.utils.Perms;

public class UpdateListener implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		if (RewardCodes.get().isCheckForUpdates() && event.getPlayer().hasPermission(Perms.isCheckForUpdates)) {
			UpdateUtil.sendUpdateMessage(event.getPlayer(), RewardCodes.get());
		}
	}
}
