package dev.wiisportsresorts.duelsplugin.duels;

import dev.wiisportsresorts.duelsplugin.DuelRequest;
import dev.wiisportsresorts.duelsplugin.util.McStyling;
import dev.wiisportsresorts.duelsplugin.util.Timeout;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;

public interface BaseDuel {
  ArrayList<DuelRequest> queue = new ArrayList<>();
  ArrayList<DuelRequest> _current = new ArrayList<>();

  default DuelRequest current() {
    if (_current.isEmpty()) {
      _current.add(null);
      return null;
    }
    return _current.get(0);
  }

  default void setCurrent(DuelRequest nextCurrent) {
    if (_current.isEmpty()) {
      _current.add(null);
    }
    _current.set(0, nextCurrent);
  }

  default void clearCurrent() {
    _current.clear();
  }

  default void enqueue(final DuelRequest request) {
    queue.add(request);

    if (current() == null) {
      processNextRequest();
    } else {
      final String message = McStyling.format(
        "$yellowYou are in queue position $gold#%s$yellow.",
        queue.size() - 1
      );
      request.requester.sendMessage(message);
      request.receiver.sendMessage(message);
    }
  }

  default DuelRequest getNextRequest() {
    Collections.rotate(queue, 1);
    DuelRequest request = queue.get(queue.size() - 1);
    if (request == null) return null;
    setCurrent(request);
    queue.remove(queue.get(queue.size() - 1));
    return request;
  }

  default void processNextRequest() {
    DuelRequest request = getNextRequest();
    if (request == null) return;

    final Player[] players = {request.receiver, request.requester};

    for (int i = 1; i <= 10; i++) {
      final int seconds = i;
      Timeout.setTimeout(11000 - i * 1000, () -> {
        for (final Player player : players) {
          player.playSound(player.getLocation(), Sound.NOTE_STICKS, 10, 1);
          player.sendMessage(McStyling.format("$yellowThe duel starts in $gold%s$yellow seconds!", seconds));
        }
      });
    }

    Timeout.setTimeout(11000, () -> start(players));
  }

  default void start(Player[] players) {
    for (final Player player : players) {
      player.sendMessage(McStyling.format("$redThis duel is missing its start method implementation."));
    }
    end(players);
  }

  default void end(Player[] players) {
    clearCurrent();
  }
}
