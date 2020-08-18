package dev.wiisportsresorts.duelsplugin.util

import dev.wiisportsresorts.duelsplugin.DuelsPlugin
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*

object LobbyManager {
    var inLobby: MutableList<String> = ArrayList()
    val lobbyLocation = { world: World? -> Location(world, 0.5, 36.0, 0.5, 0f, 0f) }
    fun sendToLobby(playerName: String) {
        // check if already exists in lobby
        if (inLobby.contains(playerName)) return

        // move to lobby, heal
        inLobby.add(playerName)
        val player = Bukkit.getPlayer(playerName)
        DuelsPlugin.run {
            player.gameMode = GameMode.ADVENTURE
            player.maxHealth = 20.0
            player.health = 20.0
            player.foodLevel = 20
            player.inventory.clear()
            player.inventory.helmet = null
            player.inventory.chestplate = null
            player.inventory.leggings = null
            player.inventory.boots = null
            player.addPotionEffect(
                    PotionEffect(
                            PotionEffectType.SATURATION, Int.MAX_VALUE,
                            255,
                            false,
                            false
                    ),
                    true
            )

            // remove every effect except for saturation
            val effects = player.activePotionEffects
            for (effect in effects) {
                val type = effect.type
                if (type === PotionEffectType.SATURATION) continue
                player.removePotionEffect(effect.type)
            }
            player.teleport(lobbyLocation(player.world))
        }
    }

    fun isInLobby(playerName: String): Boolean {
        return inLobby.contains(playerName)
    }

    fun removeFromLobby(playerName: String) {
        // check if player in lobby
        if (!inLobby.contains(playerName)) return
        inLobby.remove(playerName)
    }
}