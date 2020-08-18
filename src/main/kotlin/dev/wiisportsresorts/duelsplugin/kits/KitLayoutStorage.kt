package dev.wiisportsresorts.duelsplugin.kits

import dev.wiisportsresorts.duelsplugin.DuelType
import dev.wiisportsresorts.duelsplugin.DuelsPlugin
import org.bukkit.plugin.java.JavaPlugin

object KitLayoutStorage {
    fun setLayoutString(type: DuelType, uuid: String?, layout: String?) {
        val plugin = JavaPlugin.getPlugin(DuelsPlugin::class.java)
        val config = plugin.getKitLayoutConfig()
        val duelType = type.toString().toLowerCase()
        config!!["layouts.$duelType.$uuid"] = layout
        plugin.saveKitLayoutConfig()
    }

    /**
     * If the String does not exist but a default value has been specified,
     * this will return the default value. If the String does not exist and
     * no default value was specified, this will return null.
     *
     * @param type Type of duel
     * @param uuid UUID of player
     * @return Layout string or null if not present
     */
    fun getLayoutString(type: DuelType, uuid: String?): String? {
        val plugin = JavaPlugin.getPlugin(DuelsPlugin::class.java)
        val config = plugin.getKitLayoutConfig()
        val duelType = type.toString().toLowerCase()
        return config!!.getString(String.format("layouts.%s.%s", duelType, uuid))
    }
}