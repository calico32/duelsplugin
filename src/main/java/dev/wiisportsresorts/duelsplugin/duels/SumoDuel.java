package dev.wiisportsresorts.duelsplugin.duels;

import dev.wiisportsresorts.duelsplugin.DuelsPlugin;
import dev.wiisportsresorts.duelsplugin.util.McStyling;
import dev.wiisportsresorts.duelsplugin.util.Timeout;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class SumoDuel implements BaseDuel {
  public static Function<World, Location> location1 = (World world) ->
    new Location(world, -26.5, 10, -10.5, 90, 0);

  public static Function<World, Location> location2 = (World world) ->
    new Location(world, -40.5, 10, -10.5, -90, 0);

  public Player[] players;
  ArrayList<Timeout> timeouts = new ArrayList<>();

  public void start(Player[] players) {
    this.players = players;

    players[0].teleport(location1.apply(players[0].getWorld()));
    players[1].teleport(location2.apply(players[1].getWorld()));

    for (final Player player : players) {
      new BukkitRunnable() {
        @Override
        public void run() {
          JavaPlugin.getPlugin(DuelsPlugin.class).getLogger().info("applying sumo stuf to " + player.getName());
          player.setGameMode(GameMode.ADVENTURE);
          player.addPotionEffect(
            new PotionEffect(
              PotionEffectType.DAMAGE_RESISTANCE,
              3600,
              255,
              false,
              false
            )
          );
        }
      }.runTask(JavaPlugin.getPlugin(DuelsPlugin.class));

      player.sendMessage(McStyling.format("$goldFight!"));
    }


    // time warnings
    timeouts.add(Timeout.setTimeout(60000, () -> {
      for (final Player player : players) player.sendMessage(McStyling.format("$red2 minutes remaining!"));
    }));
    timeouts.add(Timeout.setTimeout(120000, () -> {
      for (final Player player : players) player.sendMessage(McStyling.format("$red1 minute remaining!"));
    }));
    timeouts.add(Timeout.setTimeout(180000, () -> {
      end(true);
    }));
  }

  public void end(boolean tie) {
    if (!tie) throw new IllegalArgumentException();
    String duelType = current().type.name().toLowerCase();
    String capitalizedDuelType = duelType.substring(0, 1).toUpperCase() + duelType.substring(1);

    reset(McStyling.format("$green %s duel between " +
        "$gold%s$green and $gold%s$green ended in a tie!",
      capitalizedDuelType, players[0].getName(), players[1].getName()));
  }

  public void end(String winnerName, String loserName) {
    String duelType = current().type.name();
    String capitalizedDuelType = duelType.substring(0, 1).toUpperCase() + duelType.substring(1);

    reset(McStyling.format("$gold%s$green won in a %s duel against $gold%s$green!",
      winnerName, capitalizedDuelType, loserName));

    for (Timeout timeout : timeouts) {
      timeout.callback = () -> {};
    }
    timeouts.clear();
  }

  private void reset(String text) {
    for (final Player player : players) {
      new BukkitRunnable() {
        @Override
        public void run() {
          player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
        }
      }.runTask(JavaPlugin.getPlugin(DuelsPlugin.class));
      player.teleport(new Location(player.getWorld(), 0.5, 36, 0.5, 0, 0));
    }

    JavaPlugin.getPlugin(DuelsPlugin.class).getServer().broadcastMessage(text);

    this.players = null;
    clearCurrent();

    // run next
    processNextRequest();
  }
}
