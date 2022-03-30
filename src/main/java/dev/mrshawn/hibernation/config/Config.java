package dev.mrshawn.hibernation.config;

import java.util.Arrays;

public enum Config {

	SHUTDOWN_DELAY("shutdown.delay", 30),
	SHUTDOWN_USE_SHUTDOWN_METHOD("shutdown.use-shutdown-method", false),
	SHUTDOWN_PRE_SHUTDOWN_COMMANDS("shutdown.pre-shutdown-commands", Arrays.asList("broadcast &cServer shutting down!", "stop")),
	MESSAGES_SHUTDOWN_START("messages.shutdown-start", "&6The last player has left the server, shutting down in %delay% minutes"),
	MESSAGES_SHUTDOWN_CANCEL("messages.shutdown-cancel", "&6Player has joined the server, shutdown cancelled"),
	MESSAGES_SHUTDOWN_HALFWAY("messages.shutdown-halfway", "&6The server is shutting down in %remaining% minutes");

	private final String path;
	private final Object defaultValue;

	Config(String path, Object defaultValue) {
		this.path = path;
		this.defaultValue = defaultValue;
	}

	public String getPath() {
		return this.path;
	}

	public Object getDefault() {
		return this.defaultValue;
	}

}
