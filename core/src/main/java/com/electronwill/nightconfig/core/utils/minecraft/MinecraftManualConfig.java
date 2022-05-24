package com.electronwill.nightconfig.core.utils.minecraft;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.conversion.ObjectConverter;
import com.electronwill.nightconfig.core.file.FileConfig;
import com.electronwill.nightconfig.core.file.FileConfigBuilder;
import com.electronwill.nightconfig.core.file.GenericBuilder;
import org.bukkit.plugin.java.JavaPlugin;

public class MinecraftManualConfig extends MinecraftConfig {

	private final ObjectConverter converter = new ObjectConverter();

	protected MinecraftManualConfig(JavaPlugin plugin, String path, boolean replaceResource) {
		super(plugin, path, replaceResource);
		converter.toObject(config, this);
	}

	@Override
	protected final GenericBuilder<Config, FileConfig> setupBuilder(FileConfigBuilder builder) {
		return builder;
	}

	public void save() {
		converter.toConfig(this, config);
		config.save();
	}

}
