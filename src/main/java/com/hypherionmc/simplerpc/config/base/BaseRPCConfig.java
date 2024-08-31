package com.hypherionmc.simplerpc.config.base;

import com.hypherionmc.craterlib.core.config.AbstractConfig;
import com.hypherionmc.craterlib.core.config.ConfigController;
import com.hypherionmc.simplerpc.RPCConstants;
import com.hypherionmc.simplerpc.config.impl.ClientConfig;
import com.hypherionmc.simplerpc.discord.ButtonWrapper;
import shadow.hypherionmc.moonconfig.core.CommentedConfig;
import shadow.hypherionmc.moonconfig.core.conversion.ObjectConverter;
import shadow.hypherionmc.moonconfig.core.fields.RandomArrayList;
import shadow.hypherionmc.moonconfig.core.file.CommentedFileConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author HypherionSA
 *
 * Base config class containing methods for working with SRPC Config Files
 */
public abstract class BaseRPCConfig<S extends BaseRPCConfig> extends AbstractConfig<S> {

    /**
     * Create a new Config class
     *
     * @param configName - The name of the config, excluding extension
     * @param langCode - Language Code to append to the file name
     */
    public BaseRPCConfig(String configName, String langCode) {
        super(RPCConstants.MOD_ID, "simple-rpc", getConfigName(configName, "simple-rpc", langCode));
    }

    /**
     * Internal method to help load translated config files
     *
     * @param configName The base config file name
     * @param subFolder The subfolder where the file is found
     * @param langCode The language code to load
     * @return The name of the config file that was found
     */
    private static String getConfigName(String configName, String subFolder, String langCode) {
        String configName1;

        File configDir = new File("config" + (subFolder.isEmpty() ? "" : File.separator + subFolder));
        configName1 = configName;

        File translated = new File(configDir + File.separator + configName + "_" + langCode + ".toml");
        if (!langCode.isEmpty() && translated.exists()) {
            configName1 = configName + "_" + langCode + ".toml";
        }

        return configName1;
    }

    /**
     * This method has to be called in the config constructor. This creates or upgrades the config file as needed
     *
     * @param config - The config class to use
     */
    @Override
    public void registerAndSetup(S config) {
        if (!config.getConfigPath().exists() || config.getConfigPath().length() < 2) {
            appendAdditional();
            saveConfig(config);
            RPCConstants.logger.info("Saved config to {}", config.getConfigPath().getAbsolutePath());
        } else {
            migrateConfigInternal(config);
        }

        RPCConstants.logger.info("Loading config file from {}", config.getConfigPath().getAbsolutePath());
        /* Register the Config for Watching and events */
        ConfigController.register_config(this);
        this.configReloaded();
    }

    /**
     * INTERNAL METHOD - Upgrades the config files in the events the config structure changes
     *
     * @param conf - The config class to load
     */
    private void migrateConfigInternal(S conf) {
        /* Set up the Serializer and Config Objects */
        CommentedFileConfig config = CommentedFileConfig.builder(conf.getConfigPath()).build();
        CommentedFileConfig newConfig = CommentedFileConfig.builder(conf.getConfigPath()).build();
        config.load();

        int ver = config.contains("general.version") ? config.getInt("general.version") : config.getIntOrElse("version", 0);

        if (ver != getConfigVersion()) {
            if (ver < 22 && this instanceof ClientConfig) {
                config.close();
                conf.getConfigPath().renameTo(new File(conf.getConfigPath().getAbsolutePath().replace(".toml", ".legacy")));
                RPCConstants.logger.error("Your Simple RPC config file is too old and cannot be upgraded. A new one has been created and your old one backed up to simple-rpc.legacy");
                newConfig.save();
                return;
            }

            /* Upgrade the config */
            new ObjectConverter().toConfig(conf, newConfig);
            updateConfigValuesInternal(config, newConfig, newConfig, "", ver);
            conf.getConfigPath().renameTo(new File(conf.getConfigPath().getAbsolutePath().replace(".toml", ".bak")));
            newConfig.save();
        }

        config.close();
        newConfig.close();
    }

    /**
     * Takes an old config format and updates it to the new config format
     *
     * @param oldConfig - The old config file
     * @param newConfig - The new config that will be saved
     * @param outputConfig - The final config that will be written to the disk. Usually a copy of `newConfig`
     * @param subKey - Used internally
     * @param ver - The version of the old config. Used internally
     */
    private void updateConfigValuesInternal(CommentedConfig oldConfig, CommentedConfig newConfig, CommentedConfig outputConfig, String subKey, int ver) {
        /* Loop over the config keys and check what has changed */
        newConfig.valueMap().forEach((key, value) -> {
            String finalKey = subKey + (subKey.isEmpty() ? "" : ".") + key;
            if (value instanceof CommentedConfig) {
                updateConfigValuesInternal(oldConfig, (CommentedConfig) value, outputConfig, finalKey, ver);
            } else {
                Object val = oldConfig.contains(finalKey) ? oldConfig.get(finalKey) : value;
                if ((finalKey.contains("largeImageKey") || finalKey.contains("smallImageKey"))) {
                    if (!List.class.isAssignableFrom(val.getClass())) {
                        val = RandomArrayList.of(val);
                    }
                }
                outputConfig.set(finalKey, val);
            }
        });

        List<CommentedConfig> commentedConfig = newConfig.get("dimension_overrides.dimensions");

        if (commentedConfig != null && !commentedConfig.isEmpty()) {
            commentedConfig.forEach(dim -> {
                if (!dim.contains("buttons")) {
                    dim.add("buttons", new ArrayList<ButtonWrapper>());
                }
                Object largeImg = dim.get("largeImageKey");
                if (!List.class.isAssignableFrom(largeImg.getClass())) {
                    dim.update("largeImageKey", RandomArrayList.of(largeImg));
                } else {
                    dim.update("largeImageKey", largeImg);
                }
                Object smallImg = dim.get("smallImageKey");
                if (!List.class.isAssignableFrom(smallImg.getClass())) {
                    dim.update("smallImageKey", RandomArrayList.of(smallImg));
                } else {
                    dim.update("smallImageKey", smallImg);
                }
            });
            outputConfig.set("dimension_overrides.dimensions", commentedConfig);
        }

        List<CommentedConfig> entryConfig = newConfig.get("entry");

        if (entryConfig != null && !entryConfig.isEmpty()) {
            entryConfig.forEach(dim -> {
                Object largeImg = dim.get("largeImageKey");
                if (!List.class.isAssignableFrom(largeImg.getClass())) {
                    dim.update("largeImageKey", RandomArrayList.of(largeImg));
                } else {
                    dim.update("largeImageKey", largeImg);
                }
                Object smallImg = dim.get("smallImageKey");
                if (!List.class.isAssignableFrom(smallImg.getClass())) {
                    dim.update("smallImageKey", RandomArrayList.of(smallImg));
                } else {
                    dim.update("smallImageKey", smallImg);
                }
            });
            outputConfig.set("entry", entryConfig);
        }

        if (outputConfig.contains("general.version")) {
            outputConfig.update("general.version", getConfigVersion());
        }

        if (outputConfig.contains("version")) {
            outputConfig.update("version", getConfigVersion());
        }

        if (oldConfig.contains("general.clientID")) {
            outputConfig.update("general.applicationID", oldConfig.get("general.clientID"));
        }

    }

    /**
     * Allows you to add additional information or set default config values before the file is created
     */
    public void appendAdditional() {

    }

    /**
     * Used to determine if the config structure needs to be upgraded
     *
     * @return - The current config version of the file
     */
    public int getConfigVersion() {
        return 0;
    }
}
