package dev.wiisportsresorts.duelsplugin.kits

import java.util.*
import java.util.function.Consumer

enum class KitItems(val format: String) {
    MELEE("swd"),
    RANGED("bow"),
    ROD("rod"),
    ARROWS("arw"),
    FLINTANDSTEEL("fns"),
    SPEEDPOTION("spd"),
    FIREPOTION("fir"),
    HEALPOTION("hlp"),
    /**
     * Empty item stack
     */
    NONE("x");

    override fun toString(): String {
        return format
    }

    companion object {
        fun build(rows: Array<Array<KitItems>>): String {
            val rowList: MutableList<MutableList<KitItems>> = rows.map { row -> row.toMutableList() }.toMutableList()
            for (i in rowList.size..3) {
                rowList.add(ArrayList())
            }
            rowList.forEach(Consumer { row: MutableList<KitItems> ->
                for (i in row.size..8) {
                    row.add(NONE)
                }
            })
            val toStringArray = { list: List<KitItems> -> list.map { item: KitItems -> item.toString() }.toTypedArray() }
            return arrayOf(
                toStringArray(rowList[0]).joinToString("-"),
                toStringArray(rowList[1]).joinToString("-"),
                toStringArray(rowList[2]).joinToString("-"),
                toStringArray(rowList[3]).joinToString("-")
            ).joinToString("/")
        }

        fun parse(layout: String): Array<KitItems> {
            val rows = layout.split("/".toRegex()).toTypedArray()
            val output: MutableList<KitItems> = ArrayList()
            for (i in rows.indices) {
                val itemStrings = rows[i].split("-".toRegex()).toTypedArray()
                for (itemString in itemStrings) {
                    output.add(valueOf(itemString))
                }
            }
            return output.toTypedArray()
        }
    }
}