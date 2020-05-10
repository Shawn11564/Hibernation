package dev.mrshawn.hibernation;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class Hibernation extends JavaPlugin implements Listener {

	private long delay;
	private long taskID;

	@Override
	public void onEnable() {
		getConfig().options().copyDefaults();
		saveDefaultConfig();
		delay = getConfig().getInt("restart-delay") * 20L;
		getServer().getPluginManager().registerEvents(this, this);
	}

	@EventHandler
	public void onDisconnect(PlayerQuitEvent event) {
		if (getServer().getOnlinePlayers().size() - 1 == 0) {
			startCountdown();
		}
	}

	@EventHandler
	public void onConnect(PlayerJoinEvent event) {
		if (taskID != 0) {
			getServer().getScheduler().cancelTasks(this);
			getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "Player has joined the server, shutdown cancelled!");
			taskID = 0;
		}
	}

	public void startCountdown() {

		getServer().getConsoleSender().sendMessage(ChatColor.RED + "Last player has left the server, shutting down in: " + (delay / 20) + " seconds");

		taskID = new BukkitRunnable() {
			@Override
			public void run() {
				getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "Server will shutdown in: " + (delay / 20) / 2 + " seconds");
			}
		}.runTaskLater(this, delay / 2).getTaskId();

		new BukkitRunnable() {
			@Override
			public void run() {
				if (getServer().getOnlinePlayers().size() - 1 <= 0) {
					getServer().shutdown();
				}
			}
		}.runTaskLater(this, delay);
	}
}
