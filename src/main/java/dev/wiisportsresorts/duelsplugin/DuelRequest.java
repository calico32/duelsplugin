package dev.wiisportsresorts.duelsplugin;

import org.bukkit.entity.Player;

public class DuelRequest {
  public Player requester;
  public Player receiver;
  public DuelType type;

  public DuelRequest(DuelType type, Player requester, Player receiver) {
    this.type = type;
    this.requester = requester;
    this.receiver = receiver;
  }

  public boolean includesPlayer(Player player) {
    return includesPlayer(player.getName());
  }

  public boolean includesPlayer(String playerName) {
    return Duels.sumo.current().receiver.getName().equals(playerName) ||
      Duels.sumo.current().requester.getName().equals(playerName);
  }

  public Player not(Player player) {
    if (Duels.sumo.current().receiver.getName().equals(player.getName()))
      return requester;
    else if (Duels.sumo.current().requester.getName().equals(player.getName()))
      return receiver;

    return null;
  }
}
