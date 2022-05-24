package com.electronwill.nightconfig.core.utils.minecraft;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.file.FileConfig;
import com.electronwill.nightconfig.core.file.FileConfigBuilder;
import com.electronwill.nightconfig.core.file.GenericBuilder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.Closeable;
import java.io.File;
import java.lang.reflect.InvocationTargetException;

public abstract class MinecraftConfig implements Closeable {

	protected static MinecraftConfig instance;
	public static <T> T loadConfig(MinecraftConfig instance) {
		MinecraftConfig.instance = instance;
		return (T) instance;
	}

	public static <T> T getInstance() {
		return (T) instance;
	}

	protected final FileConfig config;
	protected MinecraftConfig(JavaPlugin plugin, String path, boolean replaceResource) {
		File configFile = new File(plugin.getDataFolder(), path);
		GenericBuilder<Config, FileConfig> builder = setupBuilder(
			FileConfig.builder(configFile)
		);

		if(!configFile.exists()) {
			plugin.saveResource(path, replaceResource);
		}

		config = builder.build();
		config.load();
	}

	protected abstract GenericBuilder<Config, FileConfig> setupBuilder(FileConfigBuilder builder);

	public final void close() {
		config.close();
	}

}
