package dev.wiisportsresorts.duelsplugin.listeners

import dev.wiisportsresorts.duelsplugin.duels.ClassicDuel
import org.bukkit.GameMode
import org.bukkit.Sound
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent

class ClassicLoseListener : Listener {
    @EventHandler
    fun onPlayerDamage(event: EntityDamageEvent) {
        if (event.entityType != EntityType.PLAYER) return
        if (ClassicDuel.current == null) return
        val loser = event.entity as Player
        if (loser.health - event.finalDamage <= 0) {
            if (ClassicDuel.current!!.includesPlayer(loser)) return

            loser.health = loser.maxHealth
            loser.gameMode = GameMode.SPECTATOR

            val winner = ClassicDuel.current!!.not(loser)

            ClassicDuel.endWon(winner!!, loser)
        }
    }
}