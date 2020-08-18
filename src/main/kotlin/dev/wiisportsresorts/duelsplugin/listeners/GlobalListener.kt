package dev.wiisportsresorts.duelsplugin.listeners

import dev.wiisportsresorts.duelsplugin.DuelsPlugin
import dev.wiisportsresorts.duelsplugin.util.LobbyManager
import dev.wiisportsresorts.duelsplugin.util.LobbyManager.removeFromLobby
import dev.wiisportsresorts.duelsplugin.util.LobbyManager.sendToLobby
import dev.wiisportsresorts.duelsplugin.util.ServerScoreboard.sendScoreboard
import org.bukkit.GameMode
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.potion.PotionEffectType

class GlobalListener : Listener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        DuelsPlugin.run {
            sendToLobby(player.name)
            sendScoreboard(player.name)
        }
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        val player = event.player
        DuelsPlugin.run { player.removePotionEffect(PotionEffectType.SATURATION) }
        removeFromLobby(player.name)
    }

    @EventHandler
    fun onLobbyPlayerDamage(event: EntityDamageEvent) {
        if (event.entityType != EntityType.PLAYER) return
        val player = event.entity as Player

        // disable attacks in lobby
        if (LobbyManager.inLobby.contains(player.name)) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        val player = event.player

        // TODO allow player-placed blocks to be broken
        val block = event.block
        val location = block.location
        if (player.gameMode == GameMode.SURVIVAL) event.isCancelled = true

//    player.getWorld().getBlockAt(location).setType(block.getType());
    }
}