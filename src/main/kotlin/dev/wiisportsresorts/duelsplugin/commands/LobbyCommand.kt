package dev.wiisportsresorts.duelsplugin.commands

import dev.wiisportsresorts.duelsplugin.util.LobbyManager
import dev.wiisportsresorts.duelsplugin.util.McStyling.red
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.annotation.command.Commands

@Commands(org.bukkit.plugin.java.annotation.command.Command(
    name = LobbyCommand.name,
    desc = LobbyCommand.desc,
    usage = LobbyCommand.usage,
    aliases = ["l", "hub"]
))
class LobbyCommand : CommandExecutor {
    companion object {
        const val name = "lobby"
        const val desc = "A command"
        const val usage = "/lobby OR /l OR /hub"
    }

    override fun onCommand(sender: CommandSender, command: Command, alias: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("${red}This command can only be executed by players.")
            return true
        }
        if (LobbyManager.isInLobby(sender.name)) {
            sender.sendMessage("${red}You're already in the lobby!")
            return true
        }
        // TODO lookup location and remove from duel
        return true
    }
}