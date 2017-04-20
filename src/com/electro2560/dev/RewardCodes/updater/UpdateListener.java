package com.electro2560.dev.RewardCodes.updater;

import java.beans.ConstructorProperties;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.electro2560.dev.RewardCodes.RewardCodes;
import com.electro2560.dev.RewardCodes.utils.Perms;
import com.electro2560.dev.RewardCodes.utils.Utils;

public class UpdateListener implements Listener {
	private final RewardCodes plugin;

	@ConstructorProperties({ "plugin" })
	public UpdateListener(RewardCodes plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		if (e.getPlayer().hasPermission(Perms.isCheckForUpdates) && Utils.isCheckForUpdates()) {
			UpdateUtil.sendUpdateMessage(e.getPlayer(), plugin);
		}
	}
}
