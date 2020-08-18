package dev.wiisportsresorts.duelsplugin.duels

import dev.wiisportsresorts.duelsplugin.DuelRequest
import dev.wiisportsresorts.duelsplugin.util.Broadcast
import dev.wiisportsresorts.duelsplugin.util.LobbyManager
import dev.wiisportsresorts.duelsplugin.util.McStyling.gold
import dev.wiisportsresorts.duelsplugin.util.McStyling.green
import dev.wiisportsresorts.duelsplugin.util.McStyling.red
import dev.wiisportsresorts.duelsplugin.util.McStyling.yellow
import dev.wiisportsresorts.duelsplugin.util.Timeout
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player
import java.sql.Time

interface BaseDuel {
    companion object {
        var queue = mutableListOf<DuelRequest>()
        val timeouts: MutableList<Timeout> = mutableListOf()
        var players: Array<Player>? = null
        var current: DuelRequest? = null
    }

    val current: DuelRequest?
        get() {
            return Companion.current
        }

    fun duelName(): String? {
        if (current == null) throw Error("current is null, cannot get duel name")
        val name = current!!.type.name.toLowerCase()
        return name.substring(0, 1).toUpperCase() + name.substring(1)
    }

    fun enqueue(request: DuelRequest) {
        queue.add(request)
        if (current == null) {
            processNextRequest()
        } else {
            val message = "${yellow}You are in queue position $gold#${queue.size - 1}$yellow."

            request.requester.sendMessage(message)
            request.receiver.sendMessage(message)
        }
    }

    val nextRequest: DuelRequest?
        get() {
            if (queue.isEmpty()) return null
            val request = queue.first()
            queue = queue.subList(1, queue.lastIndex)
            return request
        }

    fun processNextRequest() {
        val request = nextRequest ?: return
        val players = arrayOf(request.receiver, request.requester)
        for (i in 1..5) {
            Timeout.setTimeout(5000 - i * 1000) {
                for (player in players) {
                    player.playSound(player.location, Sound.NOTE_STICKS, 10f, 1f)
                    player.sendMessage("${yellow}The duel starts in $gold$i$yellow second${if (i != 1) "s" else ""}!")
                }
            }
        }
        Timeout.setTimeout(5000) {
            Companion.current = request
            start(players)
        }
    }

    fun start(players: Array<Player>) {
        for (player in players) {
            player.sendMessage("${red}This duel is missing its start method implementation.")
        }
        endTie()
    }

    fun endTie() {
        for (player in players!!) player.playSound(player.location, Sound.NOTE_PLING, 10f, 1f)

        broadcastTie(players!![0].name, players!![1].name)
        reset()
    }


    fun endWon(winner: Player, loser: Player) {
        winner.playSound(winner.location, Sound.NOTE_PLING, 10f, 2f)
        winner.sendMessage("${green}You won the ${duelName()} against $gold%${loser.name}$green!")

        loser.playSound(winner.location, Sound.NOTE_PLING, 10f, 0f)
        loser.sendMessage("${red}You lost the sumo duel against $gold%${winner.name}$red!")

        broadcastWon(winner.name, loser.name)
        reset()
        for (timeout in timeouts) {
            Bukkit.getLogger().info("callback: " + timeout.callback)
            timeout.cancel()
            timeout.callback = {}
        }
        timeouts.clear()
    }

    fun reset() {
        for (player in players!!) {
            player.sendMessage("${yellow}Returning to lobby in 5 seconds.")
            Timeout.setTimeout(5000) { LobbyManager.sendToLobby(player.name) }
        }
        players = null
        Companion.current = null

        // run next
        processNextRequest()
    }


    fun broadcastTie(player1: String, player2: String) {
        Broadcast.all("${green}${duelName()} duel between $gold${player1}${green} and $gold${player2}${green} ended in a tie!")
    }

    fun broadcastWon(winner: String, loser: String) {
        Broadcast.all("$gold${winner}$green won in a ${duelName()} duel against $gold${loser}$green!")
    }

}