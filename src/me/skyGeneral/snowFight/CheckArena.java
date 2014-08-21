package me.skyGeneral.snowFight;

import net.portalkings.api.ArenaAPI;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class CheckArena implements Runnable {
	World arena;
	ListenerClass stats;
	Main plugin;

	public CheckArena(World arena, ListenerClass stats, Main plugin) {
		this.arena = arena;
		this.stats = stats;
		this.plugin = plugin;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		World spawn = Bukkit.getWorld(plugin.getConfig()
				.getString("Main world"));
		int redTeamSize = 0;
		int blueTeamSize = 0;
		for (int r1 = ArenaAPI.redTeam.size(); r1 != 0; r1--)
			if (ArenaAPI.redTeam.containsValue(arena))
				redTeamSize = redTeamSize + 1;
		for (int b1 = ArenaAPI.blueTeam.size(); b1 != 0; b1--)
			if (ArenaAPI.blueTeam.containsValue(arena))
				blueTeamSize = blueTeamSize + 1;
		if (!ArenaAPI.arenaStarted.get(arena.getName()))
			return;
		if (redTeamSize == 0) {
			for (Player player : Bukkit.getOnlinePlayers()) {
				if (player.getWorld().equals(arena)) {
					player.sendMessage(ChatColor.translateAlternateColorCodes(
							'&', "&7[&fSnowFight&7]&8 The &bblue &8team won!"));
					ArenaAPI.arenaStarted.put(arena.getName(), false);
					ArenaAPI.resetPlayer(player, spawn.getSpawnLocation());
					return;
				}
			}
		}
		if (blueTeamSize == 0) {
			for (Player player : Bukkit.getOnlinePlayers()) {
				if (player.getWorld().equals(arena)) {
					player.sendMessage(ChatColor.translateAlternateColorCodes(
							'&', "&7[&fSnowFight&7]&8 The &cred &8team won!"));
					player.teleport(spawn.getSpawnLocation());
					ArenaAPI.arenaStarted.put(arena.getName(), false);
					ArenaAPI.resetPlayer(player, spawn.getSpawnLocation());
					return;
				}
			}
		}
	}
}