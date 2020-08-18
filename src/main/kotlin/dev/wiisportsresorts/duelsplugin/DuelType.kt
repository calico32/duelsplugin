package dev.wiisportsresorts.duelsplugin

import dev.wiisportsresorts.duelsplugin.DuelsPlugin
import dev.wiisportsresorts.duelsplugin.duels.ClassicDuel
import dev.wiisportsresorts.duelsplugin.duels.NodebuffDuel
import dev.wiisportsresorts.duelsplugin.duels.SumoDuel
import org.bukkit.plugin.java.JavaPlugin
import java.util.*
import java.util.stream.Collectors

enum class DuelType {
    SUMO, CLASSIC, NODEBUFF;

    companion object {
        fun enqueue(request: DuelRequest) {
            JavaPlugin.getPlugin(DuelsPlugin::class.java).logger.info(
                "processing duel: type = ${request.type}, req = ${request.requester}, rec = ${request.receiver}"
            )
            when (request.type) {
                SUMO -> SumoDuel.enqueue(request)
                CLASSIC -> ClassicDuel.enqueue(request)
                NODEBUFF -> NodebuffDuel.enqueue(request)
            }
        }

        fun valuesAsString(): String {
            return Arrays.stream(values()).map { obj: DuelType -> obj.name }.map { obj: String -> obj.toLowerCase() }.collect(Collectors.joining(", "))
        }

        fun isValidType(duelType: String): Boolean {
            return Arrays.stream(values()).anyMatch { type: DuelType -> type.name.toLowerCase() == duelType.toLowerCase() }
        }
    }
}