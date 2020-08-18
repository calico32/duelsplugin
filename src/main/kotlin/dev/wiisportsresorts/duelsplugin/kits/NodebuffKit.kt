package dev.wiisportsresorts.duelsplugin.kits

import dev.wiisportsresorts.duelsplugin.DuelsPlugin
import dev.wiisportsresorts.duelsplugin.util.ItemBuilder
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class NodebuffKit : BaseKit {
    /*
  81x81 flat
  prot2 unbreakable dia armor
  sh1 fire1 unbreakable dia sword
  28x heal2 splash :16421
  4x speed2 (1:30) :8226
  1x fire1 (8:00) :8259
  16x steak
  * */
    private fun makeArmorPiece(material: Material): ItemStack {
        return ItemBuilder(material)
            .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
            .unbreakable()
            .create()
    }

    override fun giveTo(player: Player?) {
        val inventory = player!!.inventory
        val sword = ItemBuilder(Material.DIAMOND_SWORD)
            .enchant(Enchantment.DAMAGE_ALL, 1)
            .enchant(Enchantment.FIRE_ASPECT, 1)
            .unbreakable()
            .create()
        val healPotion = ItemStack(Material.POTION, 1, 16421.toShort()) // heal 2 splash
        val speedPotion = ItemStack(Material.POTION, 1, 8226.toShort()) // speed 2 (1:30)
        val fireResistancePotion = ItemStack(Material.POTION, 1, 8359.toShort()) // fire res 1 (8:00)\
        //    ItemStack enderPearls
        val steak = ItemStack(Material.COOKED_BEEF, 16)
        val armor = arrayOf(
            makeArmorPiece(Material.DIAMOND_BOOTS),
            makeArmorPiece(Material.DIAMOND_LEGGINGS),
            makeArmorPiece(Material.DIAMOND_CHESTPLATE),
            makeArmorPiece(Material.DIAMOND_HELMET)
        )
        DuelsPlugin.run {
            inventory.clear()
            inventory.armorContents = armor

            inventory.heldItemSlot = 0
        }
    }
}