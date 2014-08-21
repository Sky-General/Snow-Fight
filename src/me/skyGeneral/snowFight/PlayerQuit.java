package me.skyGeneral.snowFight;

import net.portalkings.api.ArenaAPI;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class PlayerQuit implements Runnable {
	Player player;
	World spawn;
	World arena;
	ListenerClass stats;
	Main plugin;

	public PlayerQuit(Player player, World spawn, World arena,
			ListenerClass stats, Main plugin) {
		this.player = player;
		this.spawn = spawn;
		this.arena = arena;
		this.stats = stats;
		this.plugin = plugin;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		ArenaAPI.redTeam.remove(player.getName());
		ArenaAPI.blueTeam.remove(player.getName());
		player.teleport(spawn.getSpawnLocation());
		stats.lives.remove(player.getName());
		if (ArenaAPI.arenaSize.containsKey(arena.getName())) {
			int size = ArenaAPI.arenaSize.get(arena.getName());
			size = size - 1;
			ArenaAPI.arenaSize.put(arena.getName(), size);
			Bukkit.getScheduler().runTask(plugin,
					new CheckArena(arena, stats, plugin));
		}
		player.getInventory().clear();
		for (Player p : Bukkit.getOnlinePlayers())
			p.showPlayer(player);
		ArenaAPI.resetPlayer(player, spawn.getSpawnLocation());
	}

}
