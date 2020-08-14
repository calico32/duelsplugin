package dev.wiisportsresorts.duelsplugin;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum DuelType {
  SUMO, CLASSIC;

  public static void enqueue(DuelRequest request) {
    JavaPlugin.getPlugin(DuelsPlugin.class).getLogger().info(String.format("processing duel: type = %s, req = %s, " +
      "rec = %s", request.type, request.requester, request.receiver));
    switch (request.type) {
      case SUMO:
        Duels.sumo.enqueue(request);
        break;
      case CLASSIC:
        Duels.classic.enqueue(request);
        break;
    }
  }

  public static String valuesAsString() {
    return Arrays.stream(DuelType.values()).map(Enum::name).map(String::toLowerCase).collect(Collectors.joining(", "));
  }

  public static boolean isValidType(String duelType) {
    return Arrays.stream(DuelType.values()).anyMatch(type -> type.name().toLowerCase().equals(duelType.toLowerCase()));
  }
}
