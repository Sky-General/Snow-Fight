package me.skyGeneral.snowFight;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class StartArena implements Runnable {
	World arena;
	Main plugin;
	ListenerClass stats;

	public StartArena(World arena, Main plugin, ListenerClass stats) {
		this.arena = arena;
		this.plugin = plugin;
		this.stats = stats;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (player.getWorld().equals(arena))
				player.sendMessage(ChatColor.translateAlternateColorCodes('&',
						"&7[&fSnowFight&7] &8A game is starting in 1 minute!"));

		}
		Bukkit.getScheduler().runTaskLater(plugin,
				new SecondsTillStart(plugin, 5, stats, arena), 1100);

	}

}
