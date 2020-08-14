package dev.wiisportsresorts.duelsplugin.listeners;

import dev.wiisportsresorts.duelsplugin.DuelsPlugin;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class GlobalListener implements Listener {
  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    new BukkitRunnable() {
      @Override
      public void run() {
        player.setGameMode(GameMode.ADVENTURE);
        player.addPotionEffect(
          new PotionEffect(
            PotionEffectType.SATURATION,
            Integer.MAX_VALUE,
            255,
            false,
            false
          ),
          true
        );
      }
    }.runTask(JavaPlugin.getPlugin(DuelsPlugin.class));

  }
}
