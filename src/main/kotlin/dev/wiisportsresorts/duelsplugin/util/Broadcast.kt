package dev.wiisportsresorts.duelsplugin.util

import org.bukkit.Bukkit

object Broadcast {
    fun all(message: String?) {
        for (player in Bukkit.getOnlinePlayers()) {
            player.sendMessage(message)
        }
    }

    fun allExcept(playerName: String, message: String?) {
        for (player in Bukkit.getOnlinePlayers()) {
            if (player.name == playerName) continue
            player.sendMessage(message)
        }
    }
}