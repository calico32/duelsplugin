package dev.wiisportsresorts.duelsplugin.commands

import dev.wiisportsresorts.duelsplugin.DuelRequest
import dev.wiisportsresorts.duelsplugin.DuelType
import dev.wiisportsresorts.duelsplugin.DuelType.Companion.enqueue
import dev.wiisportsresorts.duelsplugin.DuelType.Companion.isValidType
import dev.wiisportsresorts.duelsplugin.DuelType.Companion.valuesAsString
import dev.wiisportsresorts.duelsplugin.util.LobbyManager
import dev.wiisportsresorts.duelsplugin.util.McStyling
import dev.wiisportsresorts.duelsplugin.util.McStyling.gold
import dev.wiisportsresorts.duelsplugin.util.McStyling.green
import dev.wiisportsresorts.duelsplugin.util.McStyling.red
import dev.wiisportsresorts.duelsplugin.util.McStyling.yellow
import dev.wiisportsresorts.duelsplugin.util.Timeout
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.HoverEvent
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.annotation.command.Commands
import java.util.*

@Commands(org.bukkit.plugin.java.annotation.command.Command(
    name = DuelCommand.name,
    desc = DuelCommand.desc,
    usage = DuelCommand.usage
))
open class DuelCommand : CommandExecutor {
    companion object {
        const val name = "duel"
        const val desc = "A command"
        const val usage = "/duel <player> <type> OR /duel accept <player>"
    }

    private var requests = ArrayList<DuelRequest>()
    override fun onCommand(sender: CommandSender, command: Command, alias: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("${red}This command can only be executed by players.")
            return true
        }
        if (args.size != 2) {
            sender.sendMessage(arrayOf(
                "${red}You must specify someone to duel or to accept a duel from.",
                "${red}Usage: $usage"
            ))
            return true
        }
        return if (args[0] == "accept") acceptRequest(sender, args) else sendRequest(sender, args)
    }

    private fun sendRequest(player: Player, args: Array<String>): Boolean {
        val otherPlayerName = args[0]
        val duelTypeName = args[1]
        if (!LobbyManager.isInLobby(player.name)) {
            player.sendMessage("${red}You must be in the lobby to use this command!")
        }
        if (!isValidType(duelTypeName)) {
            player.sendMessage(arrayOf(
                "${red}Invalid duel type!",
                "${red}Valid types: ${valuesAsString()}"
            ))
            return true
        }
        val duelType = DuelType.valueOf(duelTypeName.toUpperCase())
        if (otherPlayerName == player.name) {
            player.sendMessage("${red}You can't duel yourself!")
            return true
        }
        val otherPlayer = Bukkit.getPlayer(otherPlayerName)
        if (otherPlayer == null) {
            player.sendMessage("$gold$otherPlayerName$red is not online.")
            return true
        }
        val request = DuelRequest(duelType, player, otherPlayer)
        requests.add(request)
        expireRequestAfter(request, 60000)
        otherPlayer.sendMessage(McStyling.separator)
        otherPlayer.spigot().sendMessage(
            *ComponentBuilder(player.name)
                .color(ChatColor.GOLD)
                .append(String.format(" sent you a %s duel request! Click ", duelTypeName.toLowerCase()))
                .color(ChatColor.YELLOW)
                .append("here")
                .color(ChatColor.GOLD)
                .event(ClickEvent(ClickEvent.Action.RUN_COMMAND, "/duel accept " + player.name))
                .event(HoverEvent(
                    HoverEvent.Action.SHOW_TEXT,
                    ComponentBuilder("Click to accept")
                        .color(ChatColor.GREEN)
                        .create()
                ))
                .append(" to accept. This request expires in 60 seconds.")
                .color(ChatColor.YELLOW)
                .create()
        )
        otherPlayer.sendMessage(McStyling.separator)
        player.sendMessage("${yellow}Sent a ${duelTypeName.toLowerCase()} request to $gold$otherPlayerName$yellow.")
        return true
    }

    private fun acceptRequest(receiver: Player, args: Array<String>): Boolean {
//    if (args.length == 1) {
//      receiver.sendMessage(new String[]{
//        McStyling.format("$redYou must specify a duel to accept."),
//        McStyling.format("$redUsage: " + usage)
//      });
//    }
        val reqName = args[1]
        val recName = receiver.name
        for (request in requests) {
            if (request.receiver.name == recName && request.requester.name == reqName) {
                val requester = Bukkit.getPlayer(reqName)
                if (requester == null) {
                    receiver.sendMessage("$gold$reqName$red is not online.")
                    return true
                }
                receiver.sendMessage("${green}Accepted duel from ${requester.name}.")
                requester.sendMessage("${green}Duel accepted by ${receiver.name}.")
                requests.removeIf { r: DuelRequest -> r.receiver.name == recName && r.receiver.name == reqName }
                enqueue(request)
                return true
            }
        }
        receiver.sendMessage("${red}Invalid requester.")
        return true
    }

    private fun expireRequestAfter(request: DuelRequest, delay: Int) {
        Timeout.setTimeout(60000) {
            for (search in requests) {
                if (search.receiver === request.receiver && search.requester === request.requester) {
                    requests.remove(request)
                    request.receiver.sendMessage("${red}Duel request from ${request.requester.name} expired.")
                    request.requester.sendMessage("${red}Duel request to ${request.receiver.name} expired.")
                }
            }
        }
    }
}