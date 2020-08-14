package dev.wiisportsresorts.duelsplugin.duels;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.function.Function;

public class ClassicDuel implements BaseDuel {
  public static Function<World, Location> location1 = (World world) ->
    new Location(world, -26.5, 10, -10.5, 90, 0);

  public static Function<World, Location> location2 = (World world) ->
    new Location(world, -40.5, 10, -10.5, -90, 0);
}
