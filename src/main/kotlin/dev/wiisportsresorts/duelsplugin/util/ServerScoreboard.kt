package dev.wiisportsresorts.duelsplugin.util

import dev.wiisportsresorts.duelsplugin.DuelsPlugin
import dev.wiisportsresorts.duelsplugin.util.McStyling.aqua
import dev.wiisportsresorts.duelsplugin.util.McStyling.blue
import dev.wiisportsresorts.duelsplugin.util.McStyling.bold
import dev.wiisportsresorts.duelsplugin.util.McStyling.gray
import dev.wiisportsresorts.duelsplugin.util.McStyling.red
import dev.wiisportsresorts.duelsplugin.util.McStyling.yellow
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Objective
import org.bukkit.scoreboard.Scoreboard

object ServerScoreboard {
    private const val OBJECTIVE_NAME = "$aqua${bold}PVP"
    private const val ONLINE_TEAM = "onlinePlayers"
    private const val ONLINE_ENTRY = blue
    private var updateLowPriorityTaskId: Int? = null
    private var updateHighPriorityTaskId: Int? = null
    private fun makeOnlineCounter(board: Scoreboard, objective: Objective, labelScore: Int, counterScore: Int) {
        // online players
        objective.getScore("Online:").score = labelScore
        val onlineCounter = board.registerNewTeam(ONLINE_TEAM)
        onlineCounter.addEntry(ONLINE_ENTRY)
        objective.getScore(ONLINE_ENTRY).score = counterScore
        updateLowPriority(board)
    }

    private fun updateLowPriority(board: Scoreboard) {
        board.getTeam(ONLINE_TEAM).prefix = "$aqua${Bukkit.getOnlinePlayers().size}/${Bukkit.getMaxPlayers()}"
    }

    private fun updateHighPriority(board: Scoreboard) {
        val health = board.getObjective("health")
        val onlinePlayers = Bukkit.getOnlinePlayers()
        for (player in onlinePlayers) {
            health.getScore(player.name).score = player.health.toInt()
        }
    }

    @JvmOverloads
    fun makeObjective(board: Scoreboard = Bukkit.getScoreboardManager().newScoreboard): Scoreboard {
        val sidebar = board.registerNewObjective(OBJECTIVE_NAME, "dummy")
        sidebar.displaySlot = DisplaySlot.SIDEBAR
        // empty line
        sidebar.getScore(red).score = 15
        // online players counter
        makeOnlineCounter(board, sidebar, 14, 13)
        // empty line
        sidebar.getScore(yellow).score = 12
        // ip at bottom
        sidebar.getScore("${gray}mc.wiisportsresorts.dev").score = 0
        val health = board.registerNewObjective("health", "health")
        health.displaySlot = DisplaySlot.BELOW_NAME
        health.displayName = "$red❤"
        return board
    }

    fun sendScoreboard(playerName: String?) {
        val player = Bukkit.getPlayer(playerName)
        player.scoreboard = makeObjective()
        if (updateLowPriorityTaskId == null) {
            val scheduler = Bukkit.getScheduler()
            updateLowPriorityTaskId = scheduler.scheduleSyncRepeatingTask(JavaPlugin.getPlugin(DuelsPlugin::class.java), {
                DuelsPlugin.run {
                    for (toUpdate in Bukkit.getOnlinePlayers()) {
                        updateLowPriority(toUpdate.scoreboard)
                    }
                }
            }, 0, 40)
        }
        if (updateHighPriorityTaskId == null) {
            val scheduler = Bukkit.getScheduler()
            updateHighPriorityTaskId = scheduler.scheduleSyncRepeatingTask(JavaPlugin.getPlugin(DuelsPlugin::class.java), {
                DuelsPlugin.run {
                    for (toUpdate in Bukkit.getOnlinePlayers()) {
                        updateHighPriority(toUpdate.scoreboard)
                    }
                }
            }, 0, 10)
        }
    }
}