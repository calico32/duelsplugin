package dev.wiisportsresorts.duelsplugin.kits

import dev.wiisportsresorts.duelsplugin.DuelType
import dev.wiisportsresorts.duelsplugin.DuelsPlugin
import dev.wiisportsresorts.duelsplugin.util.ItemBuilder
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class ClassicKit : BaseKit {
    private fun makeArmorPiece(material: Material): ItemStack {
        return ItemBuilder(material)
            .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
            .unbreakable()
            .create()
    }

    override fun giveTo(player: Player?) {
        val inventory = player!!.inventory
        val sword = ItemBuilder(Material.IRON_SWORD).unbreakable().create()
        val flintAndSteel = ItemBuilder(Material.FLINT_AND_STEEL).durability(2).create()
        val bow = ItemBuilder(Material.BOW).unbreakable().create()
        val fishingRod = ItemBuilder(Material.FISHING_ROD).create()
        val arrows = ItemBuilder(Material.ARROW, 5).create()
        val armor = arrayOf(
            ItemBuilder(Material.IRON_BOOTS),
            ItemBuilder(Material.IRON_LEGGINGS),
            ItemBuilder(Material.IRON_CHESTPLATE),
            ItemBuilder(Material.IRON_HELMET)
        ).map { i ->
            i.enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                .unbreakable()
                .create()
        }
        var layout = KitLayoutStorage.getLayoutString(DuelType.CLASSIC, player.uniqueId.toString())
        if (layout == null) {
            layout = KitItems.build(arrayOf(arrayOf()))
            KitLayoutStorage.setLayoutString(DuelType.CLASSIC, player.uniqueId.toString(), layout)
        }
        DuelsPlugin.run {
            inventory.clear()
            inventory.armorContents = armor.toTypedArray()
            val parsed = KitItems.parse(layout)
            for (i in parsed.indices) {
                inventory.setItem(i, when (parsed[i]) {
                    KitItems.MELEE -> sword
                    KitItems.FLINTANDSTEEL -> flintAndSteel
                    KitItems.ROD -> fishingRod
                    KitItems.ARROWS -> arrows
                    KitItems.RANGED -> bow
                    KitItems.NONE -> null
                    else -> null
                })
            }
        }
    }
}