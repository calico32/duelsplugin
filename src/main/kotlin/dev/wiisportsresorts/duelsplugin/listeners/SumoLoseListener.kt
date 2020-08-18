package dev.wiisportsresorts.duelsplugin.listeners

import dev.wiisportsresorts.duelsplugin.duels.SumoDuel
import dev.wiisportsresorts.duelsplugin.util.McStyling.gold
import dev.wiisportsresorts.duelsplugin.util.McStyling.green
import dev.wiisportsresorts.duelsplugin.util.McStyling.red
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

class SumoLoseListener : Listener {
    @EventHandler
    fun onMove(event: PlayerMoveEvent) {
        if (SumoDuel.current == null) return
        val loser = event.player
        if (SumoDuel.current!!.includesPlayer(loser)) {
            if (loser.location.block.isLiquid) {
                val winner = SumoDuel.current!!.not(loser)

                SumoDuel.endWon(winner!!, loser)
            }
        }
    }
}