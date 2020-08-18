package dev.wiisportsresorts.duelsplugin

import org.bukkit.entity.Player

class DuelRequest(val type: DuelType, val requester: Player, val receiver: Player) {

    fun includesPlayer(player: Player): Boolean {
        return receiver.name == player.name || requester.name == player.name
    }

    fun not(player: Player): Player? {
        if (receiver.name == player.name) return requester
        else if (requester.name == player.name) return receiver

        return null
    }
}