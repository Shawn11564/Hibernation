package dev.mrshawn.hibernation;

import dev.mrshawn.hibernation.config.Config;
import dev.mrshawn.hibernation.config.FileSettings;
import dev.mrshawn.hibernation.utils.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class Hibernation extends JavaPlugin implements Listener {

	private static Hibernation instance;
	private FileSettings settings;
	private long taskID;

	@Override
	public void onEnable() {
		instance = this;
		settings = new FileSettings(this, "config", true)
				.loadSettings(Config.class);
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
			Chat.log(settings.getString(Config.MESSAGES_SHUTDOWN_CANCEL));
			taskID = 0;
		}
	}

	public void startCountdown() {
		Chat.log(settings.getString(Config.MESSAGES_SHUTDOWN_START));
		taskID = new BukkitRunnable() {
			@Override
			public void run() {
				Chat.log(settings.getString(Config.MESSAGES_SHUTDOWN_HALFWAY));
			}
		}.runTaskLater(this, (settings.getInt(Config.SHUTDOWN_DELAY) * 20L * 60) / 2).getTaskId();

		new BukkitRunnable() {
			@Override
			public void run() {
				if (getServer().getOnlinePlayers().size() > 0) {
					Chat.log(settings.getString(Config.MESSAGES_SHUTDOWN_CANCEL));
					getServer().getScheduler().cancelTasks(Hibernation.getInstance());
					return;
				}
				for (String command : settings.getStringList(Config.SHUTDOWN_PRE_SHUTDOWN_COMMANDS)) {
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
				}
				if (Hibernation.getInstance().isEnabled()) {
					if (settings.getBoolean(Config.SHUTDOWN_USE_SHUTDOWN_METHOD)) {
						Bukkit.shutdown();
					}
				}
			}
		}.runTaskLater(this, settings.getInt(Config.SHUTDOWN_DELAY) * 20L * 60);

	}

	public static Hibernation getInstance() {
		return instance;
	}

}
