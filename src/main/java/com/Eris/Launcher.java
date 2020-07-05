package com.Eris;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class Launcher extends JavaPlugin{	
	public void onEnable(){
		PluginDescriptionFile config = getDescription();
		logger.info(config.getName() + " Version " + config.getVersion() + " Loaded.");
		Bukkit.getPluginManager().registerEvents((Listener)listener, this);
		getConfig().options().copyDefaults(true);
		saveConfig();
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String args[]){
		Player player = (Player)sender;
		if(commandLabel.equalsIgnoreCase("rslreload") && player.hasPermission("rsl.reload")){
			this.reloadConfig();
			player.sendMessage("The Launcher config has been reloaded.");
			listener.resetWarnings();
		}
		return false;
	}

	private final MoveListener listener = new MoveListener(this);
	protected final Logger logger = Logger.getLogger("Minecraft");
}
