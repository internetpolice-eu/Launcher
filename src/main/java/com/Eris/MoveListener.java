package com.Eris;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class MoveListener implements Listener{
	private Launcher plugin;
	private boolean plateWarn = false;
	private boolean blockWarn = false;
	private boolean lowSpeedWarn = false;
	private boolean highSpeedWarn = false;
	
	public MoveListener(Launcher instance){
        plugin = instance;
    }
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event){
		Player player = event.getPlayer();
		Location loc = player.getLocation();
		String plateName = plugin.getConfig().getString("launch-plate");
		boolean isPlate = plateName.toLowerCase().contains("plate");
		Material cfg_plate = Material.matchMaterial(plateName);
		Material cfg_block = Material.matchMaterial(plugin.getConfig().getString("bottom-block"));
		Material bottom_block = loc.getWorld().getBlockAt(loc).getRelative(0, isPlate ? -1 : -2, 0).getType();
		Material plate = loc.getWorld().getBlockAt(loc).getRelative(0, isPlate ? 0 : -1, 0).getType();
		int speed = plugin.getConfig().getInt("speed");
		if(cfg_plate == null){
			if(!plateWarn){
				plugin.logger.warning("The plate config was improperly set. Defaulting to Stone Pressure Plate.");
				plateWarn = true;
			}
			cfg_plate = Material.STONE_PLATE;
		}
		if(cfg_block == null){
			if(!blockWarn){
				plugin.logger.warning("The block config was improperly set. Defaulting to Redstone Block.");
				blockWarn = true;
			}
			cfg_block = Material.REDSTONE_BLOCK;
		}
		if(speed < 1){
			if(!lowSpeedWarn){
				plugin.logger.warning("The speed was set to a value below 1.");
				lowSpeedWarn = true;
			}
			speed = 1;
		}
		if(speed > 18){
			if(!highSpeedWarn){
				plugin.logger.warning("The speed was set to a value above the maximum of 18.");
				highSpeedWarn = true;
			}
			speed = 18;
		}
		
		if((player instanceof Player) && player.hasPermission("rsl.launch") && bottom_block == cfg_block && plate == cfg_plate){
			player.setVelocity(player.getLocation().getDirection().multiply(speed));
			player.setVelocity(new Vector(player.getVelocity().getX(), 1.0D, player.getVelocity().getZ()));
			player.playSound(player.getLocation(), Sound.valueOf(plugin.getConfig().getString("sound").toUpperCase().replace(" ", "_")), 1.0F, 1.0F);
			loc.getWorld().playEffect(loc, Effect.valueOf(plugin.getConfig().getString("effect").toUpperCase().replace(" ", "_")), 4);
		}
	}
	
	@EventHandler
	public void onPlayerDamage(EntityDamageEvent event){
		Entity player = event.getEntity();
		if(player instanceof Player && ((Player)player).hasPermission("rsl.launch")){
			event.setCancelled(true);
		}
	}
	public static void main(String args[]){
		System.out.println(Material.matchMaterial(""));
	}
	
	protected void resetWarnings(){
		plateWarn = blockWarn = lowSpeedWarn = highSpeedWarn = false;
	}
}