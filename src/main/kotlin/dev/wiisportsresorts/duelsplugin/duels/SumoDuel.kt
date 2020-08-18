package dev.wiisportsresorts.duelsplugin.duels

import dev.wiisportsresorts.duelsplugin.DuelsPlugin
import dev.wiisportsresorts.duelsplugin.util.LobbyManager.removeFromLobby
import dev.wiisportsresorts.duelsplugin.util.McStyling.gold
import dev.wiisportsresorts.duelsplugin.util.McStyling.red
import dev.wiisportsresorts.duelsplugin.util.Timeout
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object SumoDuel : BaseDuel {
    val location1 = { world: World? -> Location(world, -33.5, 10.0, -6.5, 0f, 0f) }
    val location2 = { world: World? -> Location(world, -33.5, 10.0, 7.5, -180f, 0f) }

    override fun start(players: Array<Player>) {
        BaseDuel.players = players
        players[0].teleport(location1(players[0].world))
        players[1].teleport(location2(players[1].world))
        for (player in players) {
            removeFromLobby(player.name)
            DuelsPlugin.run {
                player.gameMode = GameMode.ADVENTURE
                player.addPotionEffect(
                    PotionEffect(
                        PotionEffectType.DAMAGE_RESISTANCE,
                        3600,
                        255,
                        false,
                        false
                    )
                )
            }
            player.sendMessage("${gold}Fight!")
        }

        BaseDuel.timeouts.add(Timeout.setTimeout(60000) { for (player in players) player.sendMessage("${red}2 minutes remaining!") })
        BaseDuel.timeouts.add(Timeout.setTimeout(120000) { for (player in players) player.sendMessage("${red}1 minute remaining!") })
        BaseDuel.timeouts.add(Timeout.setTimeout(180000) { endTie() })
    }
}