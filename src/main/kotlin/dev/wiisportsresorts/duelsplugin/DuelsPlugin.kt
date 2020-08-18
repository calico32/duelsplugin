package dev.wiisportsresorts.duelsplugin

import dev.wiisportsresorts.duelsplugin.commands.DuelCommand
import dev.wiisportsresorts.duelsplugin.commands.LobbyCommand
import dev.wiisportsresorts.duelsplugin.kits.KitLayoutStorage
import dev.wiisportsresorts.duelsplugin.listeners.ClassicLoseListener
import dev.wiisportsresorts.duelsplugin.listeners.GlobalListener
import dev.wiisportsresorts.duelsplugin.listeners.SumoLoseListener
import dev.wiisportsresorts.duelsplugin.util.McStyling.red
import org.bukkit.command.CommandExecutor
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.plugin.java.annotation.plugin.Description
import org.bukkit.plugin.java.annotation.plugin.LogPrefix
import org.bukkit.plugin.java.annotation.plugin.Plugin
import org.bukkit.plugin.java.annotation.plugin.Website
import org.bukkit.plugin.java.annotation.plugin.author.Author
import org.bukkit.scheduler.BukkitRunnable
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.io.Reader
import java.nio.charset.StandardCharsets
import java.util.logging.Level

@Plugin(name = "DuelsPlugin", version = "1.0-SNAPSHOT")
@Description(value = "A plugin")
@Author(value = "wiisportsresorts")
@Website(value = "wiisportsresorts.dev")
@LogPrefix(value = "Duels")
class DuelsPlugin : JavaPlugin() {
    private var kitLayoutConfig: FileConfiguration? = null
    private var kitLayoutConfigFile: File? = null
    fun getKitLayoutConfig(): FileConfiguration? {
        if (kitLayoutConfig == null) reloadKitLayoutConfig()
        return kitLayoutConfig
    }

    fun reloadKitLayoutConfig() {
        if (kitLayoutConfigFile == null) kitLayoutConfigFile = File(dataFolder, "kitlayouts.yml")
        kitLayoutConfig = YamlConfiguration.loadConfiguration(kitLayoutConfigFile)

        // Look for defaults in the jar
        val defaultConfigStream: Reader = InputStreamReader(getResource("kitlayouts.yml"), StandardCharsets.UTF_8)
        val defaultConfig = YamlConfiguration.loadConfiguration(defaultConfigStream)
        (kitLayoutConfig as YamlConfiguration?)?.defaults = defaultConfig
    }

    fun saveKitLayoutConfig() {
        if (kitLayoutConfig == null || kitLayoutConfigFile == null) return
        try {
            getKitLayoutConfig()!!.save(kitLayoutConfigFile)
        } catch (exception: IOException) {
            logger.log(Level.SEVERE, "Could not save config to $kitLayoutConfigFile", exception)
        }
    }

    fun saveDefaultKitLayoutConfig() {
        if (kitLayoutConfigFile == null) {
            kitLayoutConfigFile = File(dataFolder, "kitlayouts.yml")
        }
        if (!kitLayoutConfigFile!!.exists()) {
            saveResource("kitlayouts.yml", false)
        }
    }

    private fun registerCommand(name: String, executor: CommandExecutor) {
        val command = getCommand(name)
            ?: return logger.severe("${red}Could not get command for $name (probably not in plugin.yml).")
        command.executor = executor
    }

    override fun onEnable() {
        registerCommand(DuelCommand.name, DuelCommand())
        registerCommand(LobbyCommand.name, LobbyCommand())
        val pluginManager = server.pluginManager
        pluginManager.registerEvents(GlobalListener(), this)
        pluginManager.registerEvents(SumoLoseListener(), this)
        pluginManager.registerEvents(ClassicLoseListener(), this)
        saveDefaultKitLayoutConfig()
        logger.info("Duels plugin enabled!")
    }

    override fun onDisable() {
        logger.info("Duels plugin disabled!")
    }

    companion object {
        @JvmStatic
        fun run(callback: () -> Unit) {
            object : BukkitRunnable() {
                override fun run() {
                    callback()
                }
            }.runTask(getPlugin(DuelsPlugin::class.java))
        }
    }
}