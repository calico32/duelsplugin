package dev.wiisportsresorts.duelsplugin.listeners;

import dev.wiisportsresorts.duelsplugin.Duels;
import dev.wiisportsresorts.duelsplugin.util.McStyling;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class SumoLoseListener implements Listener {
  @EventHandler
  public void onMove(PlayerMoveEvent event) {
    Player player = event.getPlayer();
    if (Duels.sumo.current() == null) return;
    if (Duels.sumo.current().includesPlayer(player)) {
      if (player.getLocation().getBlock().isLiquid()) {
        Player winner = Duels.sumo.current().not(player);
        player.sendMessage(McStyling.format("$redYou lost the sumo duel against $gold%s$red!", winner.getName()));
        winner.sendMessage(McStyling.format("$greenYou won the sumo duel against $gold%s$green!", player.getName()));
        Duels.sumo.end(winner.getName(), player.getName());
      }
    }
  }
}
