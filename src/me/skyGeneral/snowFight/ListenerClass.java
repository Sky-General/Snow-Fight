package me.skyGeneral.snowFight;

import java.util.HashMap;
import java.util.Map;

import net.portalkings.api.ArenaAPI;
import net.portalkings.api.PlayerState;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ListenerClass implements Listener {
	Main plugin;
	Map<String, Integer> lives = new HashMap<>();

	public ListenerClass(Main plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerDamage(EntityDamageByEntityEvent e) {
		e.setCancelled(true);
		if (e.getEntity() instanceof Player) {
			Player player = (Player) e.getEntity();
			if (e.getDamager() instanceof Snowball) {
				if (!lives.containsKey(player.getName()))
					return;
				int live = lives.get(player.getName());
				Snowball snowball = (Snowball) e.getDamager();
				if (snowball.getShooter().equals(player))
					return;
				live--;
				lives.put(player.getName(), live);
				/* This needs to be long to prevent memory leaks */
				if (ArenaAPI.blueTeam.containsKey(player.getName()))
					player.teleport(new Location(player.getWorld(), plugin
							.getConfig().getInt(
									"Arena." + player.getWorld().getName()
											+ ".blueSpawn.X"), plugin
							.getConfig().getInt(
									"Arena." + player.getWorld().getName()
											+ ".blueSpawn.Y"), plugin
							.getConfig().getInt(
									"Arena." + player.getWorld().getName()
											+ ".blueSpawn.Z")));
				if (ArenaAPI.redTeam.containsKey(player.getName()))
					player.teleport(new Location(player.getWorld(), plugin
							.getConfig().getInt(
									"Arena." + player.getWorld().getName()
											+ ".redSpawn.X"), plugin
							.getConfig().getInt(
									"Arena." + player.getWorld().getName()
											+ ".redSpawn.Y"), plugin
							.getConfig().getInt(
									"Arena." + player.getWorld().getName()
											+ ".redSpawn.Z")));
				if (live == 0) {
					player.sendMessage(ChatColor.translateAlternateColorCodes(
							'&', "&7[&fSnowFight&7] &8You have died."));
					Bukkit.getScheduler().runTask(
							plugin,
							new KillPlayer(player, player.getWorld(), this,
									plugin));
				}
				if (live == 1)
					player.sendMessage(ChatColor.translateAlternateColorCodes(
							'&', "&7[&fSnowFight&7]&8 You have &7" + (live)
									+ "&8 live left."));
				if (live > 1)
					player.sendMessage(ChatColor.translateAlternateColorCodes(
							'&', "&7[&fSnowFight&7]&8 You have &7" + (live)
									+ "&8 lives left."));

			}
		}
	}

	@EventHandler
	public void onPlayerHunger(FoodLevelChangeEvent e) {
		e.setFoodLevel(20);
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		World spawn = Bukkit.getWorld(plugin.getConfig()
				.getString("Main world"));
		Location sspawn = ((World) spawn).getSpawnLocation();
		ArenaAPI.resetPlayer(player, sspawn);
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		World spawn = Bukkit.getWorld(plugin.getConfig()
				.getString("Main world"));
		Bukkit.getScheduler().runTask(plugin,
				new PlayerQuit(player, spawn, player.getWorld(), this, plugin));
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		if (player.getItemInHand().getType().equals(Material.STONE_SPADE)) {
			if (e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
				/* Delay here */
				if (ArenaAPI.getPlayerState(player).equals(PlayerState.INGAME)) {
					player.launchProjectile(Snowball.class);
					player.playSound(player.getLocation(), Sound.BLAZE_HIT, 10,
							10);
				}
			}
		} else {
			if (e.getClickedBlock() == null)
				return;
			if (e.getClickedBlock().getState() instanceof Sign) {
				World arena;
				Sign sign = (Sign) e.getClickedBlock().getState();
				if (ChatColor.stripColor(sign.getLine(0)).equalsIgnoreCase(
						"[snowfight]")) {
					try {
						Bukkit.getWorld(ChatColor.stripColor(sign.getLine(1)));
						arena = Bukkit.getWorld(ChatColor.stripColor(sign
								.getLine(1)));
						if (!ArenaAPI.arenaSize.containsKey(arena.getName()))
							ArenaAPI.arenaSize.put(arena.getName(), 0);
						if (!ArenaAPI.arenaStarted.containsKey(arena.getName()))
							ArenaAPI.arenaStarted.put(arena.getName(), false);
						if (ArenaAPI.arenaSize.get(arena.getName()) >= 35) {
							player.sendMessage(ChatColor.RED
									+ "Sorry, that arena is full.");
							return;
						}
						if (ArenaAPI.arenaStarted.get(arena.getName())) {
							player.sendMessage(ChatColor.RED
									+ "Sorry, that game has already started.");
							return;
						}
						player.teleport(arena.getSpawnLocation());
						int size = ArenaAPI.arenaSize.get(arena.getName());
						ArenaAPI.arenaSize.put(arena.getName(), (size + 1));
						lives.put(player.getName(), 3);
						if (ArenaAPI.redTeam.size() < ArenaAPI.blueTeam.size())
							ArenaAPI.redTeam.put(player.getName(), arena);
						else
							ArenaAPI.blueTeam.put(player.getName(), arena);
						if (ArenaAPI.arenaSize.get(arena.getName()) == 2)
							Bukkit.getScheduler().runTask(plugin,
									new StartArena(arena, plugin, this));

					} catch (NullPointerException ex) {
						return;
					}

					for (Player p : Bukkit.getOnlinePlayers()) {
						if (p.getWorld().equals(arena)) {
							if (ArenaAPI.redTeam.containsKey(player.getName()))
								p.sendMessage(ChatColor
										.translateAlternateColorCodes(
												'&',
												"&7[&fSnowFight&7]&c "
														+ player.getName()
														+ "&8 has joined the game! &7(&f"
														+ arena.getPlayers()
																.size()
														+ "&7/&f35&7)"));
							if (ArenaAPI.blueTeam.containsKey(player.getName()))
								p.sendMessage(ChatColor
										.translateAlternateColorCodes(
												'&',
												"&7[&fSnowFight&7]&b "
														+ player.getName()
														+ "&8 has joined the game! &7(&f"
														+ arena.getPlayers()
																.size()
														+ "&7/&f35&7)"));
						}
					}
				}

			}
		}
	}
}
