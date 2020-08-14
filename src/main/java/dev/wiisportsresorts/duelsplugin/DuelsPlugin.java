package dev.wiisportsresorts.duelsplugin;

import dev.wiisportsresorts.duelsplugin.commands.DuelCommand;
import dev.wiisportsresorts.duelsplugin.listeners.GlobalListener;
import dev.wiisportsresorts.duelsplugin.listeners.SumoLoseListener;
import dev.wiisportsresorts.duelsplugin.util.McStyling;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.plugin.Description;
import org.bukkit.plugin.java.annotation.plugin.LogPrefix;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.Website;
import org.bukkit.plugin.java.annotation.plugin.author.Author;

@Plugin(name = "DuelsPlugin", version = "1.0-SNAPSHOT")
@Description(value = "A plugin")
@Author(value = "wiisportsresorts")
@Website(value = "wiisportsresorts.dev")
@LogPrefix(value = "Duels")

public class DuelsPlugin extends JavaPlugin {
  private void registerCommand(String name, CommandExecutor executor) {
    PluginCommand command = getCommand(name);
    if (command == null) {
      getLogger().severe(McStyling.format(
        "$redCould not get command for %s (probably not in plugin.yml).", name
      ));
      return;
    }
    command.setExecutor(executor);
  }

  @Override
  public void onEnable() {
    registerCommand(DuelCommand.name, new DuelCommand());
    getServer().getPluginManager().registerEvents(new SumoLoseListener(), this);
    getServer().getPluginManager().registerEvents(new GlobalListener(), this);
    getLogger().info("Duels plugin enabled!");
  }

  @Override
  public void onDisable() {
    getLogger().info("Duels plugin disabled!");
  }
}

// Amm, in your event handler, you should first check if the entity is a player and only then cast it.
// If you also want to disable them doing damage, you should create another EntityDamageByEntity event and use
// e.getDamager() to see if the damager is a player or not. If it's a player, you should cast the damager to
// a player and check if the player's name is found in the list. If it is, you simply do e.setCancelled(true).
