package dev.wiisportsresorts.duelsplugin.util

import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

class ItemBuilder {
    val material: Material
    private val itemStack: ItemStack

    constructor(material: Material) {
        this.material = material
        itemStack = ItemStack(material)
    }

    constructor(material: Material, count: Int) {
        this.material = material
        itemStack = ItemStack(material, count)
    }

    constructor(material: Material, count: Int, data: Int) {
        this.material = material
        itemStack = ItemStack(material, count, data.toShort())
    }

    @JvmOverloads
    fun enchant(enchantment: Enchantment?, level: Int, unsafe: Boolean = false): ItemBuilder {
        if (unsafe) itemStack.addUnsafeEnchantment(enchantment, level) else itemStack.addEnchantment(enchantment, level)
        return this
    }

    fun unbreakable(): ItemBuilder {
        itemStack.itemMeta.spigot().isUnbreakable = true
        return this
    }

    fun durability(durability: Int): ItemBuilder {
        return damage(material.maxDurability - durability)
    }

    fun damage(durability: Int): ItemBuilder {
        itemStack.durability = durability.toShort()
        return this
    }

    fun create(): ItemStack {
        return itemStack
    }
}