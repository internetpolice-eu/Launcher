package com.eris;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class Launcher extends JavaPlugin {
    private MoveListener listener;

    @Override
	public void onEnable() {
        listener = new MoveListener(this);
		getServer().getPluginManager().registerEvents(listener, this);
		getConfig().options().copyDefaults(true);
		saveConfig();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (commandLabel.equalsIgnoreCase("rslreload") && sender.hasPermission("rsl.reload")) {
			reloadConfig();
			listener.reloadConfig();
			sender.sendMessage("The Launcher config has been reloaded.");
		}
		return false;
	}
}
