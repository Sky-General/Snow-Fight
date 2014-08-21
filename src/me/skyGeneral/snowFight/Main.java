package me.skyGeneral.snowFight;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	public void onEnable() {
		new ListenerClass(this);
		new SnowFightCommand(this, "snowfight");
	}

}
