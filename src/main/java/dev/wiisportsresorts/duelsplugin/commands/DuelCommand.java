package dev.wiisportsresorts.duelsplugin.commands;

import dev.wiisportsresorts.duelsplugin.DuelRequest;
import dev.wiisportsresorts.duelsplugin.DuelType;
import dev.wiisportsresorts.duelsplugin.util.McStyling;
import dev.wiisportsresorts.duelsplugin.util.Timeout;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.annotation.command.Commands;

import java.util.ArrayList;

@Commands(
  @org.bukkit.plugin.java.annotation.command.Command(
    name = DuelCommand.name,
    desc = DuelCommand.desc,
    usage = DuelCommand.usage
  )
)
public class DuelCommand implements CommandExecutor {
  public static final String name = "duel";
  public static final String desc = "A command";
  public static final String usage = "/duel <player> <type> OR /duel accept <player>";
  public ArrayList<DuelRequest> requests = new ArrayList<>();

  public boolean onCommand(final CommandSender sender, final Command command, final String alias, final String[] args) {
    if (!(sender instanceof Player)) {
      sender.sendMessage(McStyling.format("$redThis command can only be executed by players."));
      return true;
    }

    final Player player = (Player) sender;
    if (args.length != 2) {
      player.sendMessage(new String[]{
        McStyling.format("$redYou must specify someone to duel or to accept a duel from."),
        McStyling.format("$redUsage: %s", usage)
      });
      return true;
    }

    if (args[0].equals("accept")) return acceptRequest(player, args);
    else return sendRequest(player, args);
  }

  private boolean sendRequest(final Player player, final String[] args) {
    final String otherPlayerName = args[0];
    final String duelTypeName = args[1];

    if (!DuelType.isValidType(duelTypeName)) {
      player.sendMessage(new String[]{
        McStyling.format("$redInvalid duel type!"),
        McStyling.format("$redValid types: " + DuelType.valuesAsString())
      });
      return true;
    }

    final DuelType duelType = DuelType.valueOf(duelTypeName.toUpperCase());

    if (otherPlayerName.equals(player.getName())) {
      player.sendMessage(McStyling.format("$redYou can't duel yourself!"));
      return true;
    }

    final Player otherPlayer = Bukkit.getPlayer(otherPlayerName);
    if (otherPlayer == null) {
      player.sendMessage(McStyling.format("$red$gold%s $redis not online.", otherPlayerName));
      return true;
    }

    final DuelRequest request = new DuelRequest(duelType, player, otherPlayer);
    requests.add(request);
    expireRequestAfter(request, 60000);

    otherPlayer.sendMessage(McStyling.separator);
    otherPlayer.spigot().sendMessage(
      new ComponentBuilder(player.getName())
        .color(ChatColor.GOLD)
        .append(String.format(" sent you a %s duel request! Click ", duelTypeName.toLowerCase()))
        .color(ChatColor.YELLOW)
        .append("here")
        .color(ChatColor.GOLD)
        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/duel accept " + player.getName()))
        .event(new HoverEvent(
          HoverEvent.Action.SHOW_TEXT,
          new ComponentBuilder("Click to accept")
            .color(ChatColor.GREEN)
            .create()
        ))
        .append(" to accept. This request expires in 60 seconds.")
        .color(ChatColor.YELLOW)
        .create()
    );
    otherPlayer.sendMessage(McStyling.separator);

    player.sendMessage(McStyling.format(
      "$yellowSent a %s request to $gold%s$yellow.",
      duelTypeName.toLowerCase(),
      otherPlayerName
    ));

    return true;
  }

  private boolean acceptRequest(final Player receiver, final String[] args) {
//    if (args.length == 1) {
//      receiver.sendMessage(new String[]{
//        McStyling.format("$redYou must specify a duel to accept."),
//        McStyling.format("$redUsage: " + usage)
//      });
//    }
    String reqName = args[1];
    String recName = receiver.getName();
    for (final DuelRequest request : requests) {
      if (request.receiver.getName().equals(recName) && request.requester.getName().equals(reqName)) {
        final Player requester = Bukkit.getPlayer(reqName);
        if (requester == null) {
          receiver.sendMessage(McStyling.format("$red$gold%s $redis not online.", reqName));
          return true;
        }

        receiver.sendMessage(McStyling.format("$greenAccepted duel from %s.", requester.getName()));
        requester.sendMessage(McStyling.format("$greenDuel accepted by %s.", receiver.getName()));
        requests.removeIf(r -> r.receiver.getName().equals(recName) && r.requester.getName().equals(reqName));

        DuelType.enqueue(request);
        return true;
      }
    }

    receiver.sendMessage(McStyling.format("$redInvalid requester."));
    return true;
  }

  protected void expireRequestAfter(final DuelRequest request, final int delay) {
    Timeout.setTimeout(60000, () -> {
      for (final DuelRequest search : requests) {
        if (search.receiver == request.receiver && search.requester == request.requester) {
          requests.remove(request);

          request.receiver.sendMessage(
            McStyling.format("$redDuel request from %s expired.", request.requester.getName())
          );
          request.requester.sendMessage(
            McStyling.format("$redDuel request to %s expired.", request.receiver.getName())
          );
        }
      }
    });
  }
}

// pos 1: -26.5 10 -10.5
// pos 2: -40.5 10 -10.5
