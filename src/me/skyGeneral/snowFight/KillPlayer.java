package me.skyGeneral.snowFight;

import net.portalkings.api.ArenaAPI;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class KillPlayer implements Runnable {
	ListenerClass arenaStats;
	Player player;
	World arena;
	Main plugin;

	public KillPlayer(Player player, World arena, ListenerClass arenaStats,
			Main plugin) {
		this.player = player;
		this.plugin = plugin;
		this.arena = arena;
		this.arenaStats = arenaStats;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		player.getInventory().clear();
		player.updateInventory();
		player.sendMessage(ChatColor
				.translateAlternateColorCodes(
						'&',
						"&7[&fSnowFight&7&8 You are now spectating! Don't leave! You can play in the next round!"));
		arenaStats.lives.remove(player.getName());
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (p.getWorld().equals(arena)) {
				if (!p.equals(player)) {
					p.hidePlayer(player);
				}
			}
		}
		player.setAllowFlight(true);
		if (ArenaAPI.redTeam.containsKey(player.getName()))
			ArenaAPI.redTeam.remove(player.getName());
		if (ArenaAPI.blueTeam.containsKey(player.getName()))
			ArenaAPI.blueTeam.remove(player.getName());
		Bukkit.getScheduler().runTask(plugin,
				new CheckArena(arena, arenaStats, plugin));

	}

}
