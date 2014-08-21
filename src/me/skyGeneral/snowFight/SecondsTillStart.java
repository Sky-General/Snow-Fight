package me.skyGeneral.snowFight;

import net.portalkings.api.ArenaAPI;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.Note.Tone;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class SecondsTillStart implements Runnable {
	Main plugin;
	int y;
	ListenerClass stats;
	World arena;

	public SecondsTillStart(Main plugin, int y, ListenerClass stats, World arena) {
		this.plugin = plugin;
		this.y = y;
		this.stats = stats;
		this.arena = arena;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (player.getWorld().equals(arena)) {
				if (y == 1) {
					player.playNote(player.getLocation(), Instrument.PIANO,
							Note.flat(0, Tone.A));
					player.sendMessage(ChatColor.translateAlternateColorCodes(
							'&', "&7[&fSnowFight&7]&8 Game starting in &7" + y
									+ "&8 second!"));
				}
				if (y == 0) {
					player.playSound(player.getLocation(), Sound.LEVEL_UP, 10,
							10);
					ArenaAPI.arenaStarted.put(arena.getName(), true);
				}
				if (y > 1) {
					player.playNote(player.getLocation(), Instrument.PIANO,
							Note.flat(0, Tone.A));
					player.sendMessage(ChatColor.translateAlternateColorCodes(
							'&', "&7[&fSnowFight&7]&8 Game starting in &7" + y
									+ "&8 seconds!"));
				}
			}
		}
		y = y - 1;
		if (y != -1)
			Bukkit.getScheduler().runTaskLater(plugin, this, 20);
		else {
			ItemStack blueHat = new ItemStack(Material.LAPIS_BLOCK);
			ItemStack redHat = new ItemStack(Material.REDSTONE_BLOCK);
			ItemStack blueShirt = new ItemStack(Material.LEATHER_CHESTPLATE);
			ItemStack redShirt = new ItemStack(Material.LEATHER_CHESTPLATE);
			ItemStack bluePants = new ItemStack(Material.LEATHER_LEGGINGS);
			ItemStack redPants = new ItemStack(Material.LEATHER_LEGGINGS);
			ItemStack blueShoes = new ItemStack(Material.LEATHER_BOOTS);
			ItemStack redShoes = new ItemStack(Material.LEATHER_BOOTS);

			LeatherArmorMeta rsm = (LeatherArmorMeta) redShirt.getItemMeta();
			rsm.setColor(Color.RED);
			redShirt.setItemMeta(rsm);

			LeatherArmorMeta rpm = (LeatherArmorMeta) redPants.getItemMeta();
			rpm.setColor(Color.RED);
			redPants.setItemMeta(rpm);

			LeatherArmorMeta rbm = (LeatherArmorMeta) redShoes.getItemMeta();
			rbm.setColor(Color.RED);
			redShoes.setItemMeta(rbm);

			LeatherArmorMeta bsm = (LeatherArmorMeta) blueShirt.getItemMeta();
			bsm.setColor(Color.BLUE);
			blueShirt.setItemMeta(bsm);

			LeatherArmorMeta bpm = (LeatherArmorMeta) bluePants.getItemMeta();
			bpm.setColor(Color.BLUE);
			bluePants.setItemMeta(bpm);

			LeatherArmorMeta bbm = (LeatherArmorMeta) blueShoes.getItemMeta();
			bbm.setColor(Color.BLUE);
			blueShoes.setItemMeta(bbm);
			for (Player player : Bukkit.getOnlinePlayers()) {
				if (ArenaAPI.blueTeam.containsKey(player.getName())) {
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
					player.sendMessage(ChatColor.translateAlternateColorCodes(
							'&', "&7[&fSnowFight&7]&8 The game has started!"));
					player.getEquipment().setArmorContents(
							new ItemStack[] { blueShoes, bluePants, blueShirt,
									blueHat });
					player.setCustomName(ChatColor.DARK_BLUE + player.getName());
				}
				if (ArenaAPI.redTeam.containsKey(player.getName())) {
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
					player.sendMessage(ChatColor.translateAlternateColorCodes(
							'&', "&7[&fSnowFight&7]&8 The game has started!"));
					player.getEquipment().setArmorContents(
							new ItemStack[] { redShoes, redPants, redShirt,
									redHat });
					player.setCustomName(ChatColor.RED + player.getName());
				}
				player.setGameMode(GameMode.SURVIVAL);
				player.getInventory().clear();
				player.getInventory()
						.addItem(new ItemStack(Material.BLAZE_ROD));
				player.updateInventory();
				Bukkit.getScheduler().runTask(plugin,
						new CheckArena(arena, stats, plugin));
			}
		}
	}

}
