package me.skyGeneral.snowFight;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SnowFightCommand implements CommandExecutor {
	Main plugin;

	public SnowFightCommand(Main plugin, String cmd) {
		this.plugin = plugin;
		plugin.getCommand(cmd).setExecutor(this);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("snowfight")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (args.length == 0) {
					player.sendMessage(ChatColor
							.translateAlternateColorCodes('&',
									"&8&m=====================&7&l[&f&lSnowFight&7&l]&8&m====================="));
					player.sendMessage("");
					player.sendMessage("");
					player.sendMessage("");
					player.sendMessage(ChatColor
							.translateAlternateColorCodes(
									'&',
									"&7&lHey there, and welcome, to &f&lSnowFight&7&l! You don't need to be using any commands though ;-)"));
					player.sendMessage("");
					player.sendMessage("");
					player.sendMessage("");
					player.sendMessage(ChatColor
							.translateAlternateColorCodes('&',
									"&8&m====================================================="));
				}
				if (player.hasPermission("pk.admin")) {
					if (args.length == 2) {
						if (args[0].equalsIgnoreCase("set")) {
							if (args[1].equalsIgnoreCase("red")) {
								plugin.getConfig().set(
										"Arena." + player.getWorld().getName()
												+ ".redSpawn.X",
										player.getLocation().getX());
								plugin.getConfig().set(
										"Arena." + player.getWorld().getName()
												+ ".redSpawn.Y",
										player.getLocation().getY());
								plugin.getConfig().set(
										"Arena." + player.getWorld().getName()
												+ ".redSpawn.Z",
										player.getLocation().getZ());
								plugin.saveConfig();
								player.sendMessage(ChatColor
										.translateAlternateColorCodes('&',
												"&7[&fSnowFight&7]&8 You have set the &cred &8spawn."));
							}
							if (args[1].equalsIgnoreCase("blue")) {
								plugin.getConfig().set(
										"Arena." + player.getWorld().getName()
												+ ".blueSpawn.X",
										player.getLocation().getX());
								plugin.getConfig().set(
										"Arena." + player.getWorld().getName()
												+ ".blueSpawn.Y",
										player.getLocation().getY());
								plugin.getConfig().set(
										"Arena." + player.getWorld().getName()
												+ ".blueSpawn.Z",
										player.getLocation().getZ());
								plugin.saveConfig();
								player.sendMessage(ChatColor
										.translateAlternateColorCodes('&',
												"&7[&fSnowFight&7]&8 You have set the &bblue &8spawn."));
							}
						}

					}
				} else {
					player.sendMessage(ChatColor
							.translateAlternateColorCodes('&',
									"&8&m=====================&7&l[&f&lSnowFight&7&l]&8&m====================="));
					player.sendMessage("");
					player.sendMessage("");
					player.sendMessage("");
					player.sendMessage(ChatColor
							.translateAlternateColorCodes(
									'&',
									"&7&lHey there, and welcome, to &f&lSnowFight&7&l! You don't need to be using any commands though ;-)"));
					player.sendMessage("");
					player.sendMessage("");
					player.sendMessage("");
					player.sendMessage(ChatColor
							.translateAlternateColorCodes('&',
									"&8&m====================================================="));
				}
			} else {
				sender.sendMessage(ChatColor.RED + "Hey! Players only!");
			}
		}
		return false;
	}

}
