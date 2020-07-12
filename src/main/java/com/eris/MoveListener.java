package com.eris;

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
	private final Launcher plugin;
	private Material block;
	private Material plate;
	private boolean isPlate;
	private int speed;
	private Sound sound;
	private Effect effect;
	
	public MoveListener(Launcher instance) {
        plugin = instance;
        reloadConfig();
    }

    public void reloadConfig() {
        Material blockMaterial = Material.matchMaterial(plugin.getConfig().getString("bottom-block", "REDSTONE_BLOCK"));
        if (blockMaterial != null) {
            block = blockMaterial;
        } else {
            plugin.getLogger().warning("The block config was improperly set. Defaulting to Redstone Block.");
            block = Material.REDSTONE_BLOCK;
        }

        Material plateMaterial = Material.matchMaterial(plugin.getConfig().getString("launch-plate", "STONE_PRESSURE_PLATE"));
        if (plateMaterial != null) {
            plate = plateMaterial;
        } else {
            plugin.getLogger().warning("The plate config was improperly set. Defaulting to Stone Pressure Plate.");
            plate = Material.STONE_PRESSURE_PLATE;
        }
        isPlate = plate.toString().toLowerCase().contains("plate");

        int configSpeed = plugin.getConfig().getInt("speed", 5);
        if (configSpeed < 1) {
            plugin.getLogger().warning("The speed was set to a value below 1. Defaulting to 5.");
            speed = 5;
        } else if (configSpeed > 18) {
            plugin.getLogger().warning("The speed was set to a value above the maximum of 18. Defaulting to 5.");
            speed = 5;
        } else {
            speed = configSpeed;
        }

        effect = Effect.valueOf(plugin.getConfig().getString("effect").toUpperCase().replace(" ", "_"));
        sound = Sound.valueOf(plugin.getConfig().getString("sound").toUpperCase().replace(" ", "_"));
    }
	
	@EventHandler(ignoreCancelled = true)
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		Location loc = player.getLocation();
		Material playerBlock = loc.getWorld().getBlockAt(loc).getRelative(0, isPlate ? -1 : -2, 0).getType();
		Material playerPlate = loc.getWorld().getBlockAt(loc).getRelative(0, isPlate ? 0 : -1, 0).getType();
		
		if (playerBlock == block && playerPlate == plate && player.hasPermission("rsl.launch")) {
			player.setVelocity(player.getLocation().getDirection().multiply(speed));
			player.setVelocity(new Vector(player.getVelocity().getX(), 1.0D, player.getVelocity().getZ()));
			player.playSound(player.getLocation(), sound, 1.0F, 1.0F);
			loc.getWorld().playEffect(loc, effect, 4);
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onPlayerDamage(EntityDamageEvent event) {
		Entity player = event.getEntity();
		if (player instanceof Player && player.hasPermission("rsl.launch")) {
			event.setCancelled(true);
		}
	}
}
