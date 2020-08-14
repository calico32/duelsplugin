package dev.wiisportsresorts.duelsplugin.util;

public class McStyling {
  public static final String black = "§0";
  public static final String darkBlue = "§1";
  public static final String darkGreen = "§2";
  public static final String darkAqua = "§3";
  public static final String darkRed = "§4";
  public static final String darkPurple = "§5";
  public static final String gold = "§6";
  public static final String gray = "§7";
  public static final String darkGray = "§8";
  public static final String blue = "§9";
  public static final String green = "§a";
  public static final String aqua = "§b";
  public static final String red = "§c";
  public static final String lightPurple = "§d";
  public static final String yellow = "§e";
  public static final String white = "§f";
  public static final String obfuscated = "§k";
  public static final String bold = "§l";
  public static final String strikethrough = "§m";
  public static final String underline = "§n";
  public static final String italic = "§o";
  public static final String reset = "§r";
  public static final String separator = aqua + "----------------------------------------------------" + reset;
  public static String format(String message, Object... replacements) {
    return String.format("$darkAqua[duels] $reset" + message, replacements)
      .replace("$black", black)
      .replace("$darkBlue", darkBlue)
      .replace("$darkGreen", darkGreen)
      .replace("$darkAqua", darkAqua)
      .replace("$darkRed", darkRed)
      .replace("$darkPurple", darkPurple)
      .replace("$gold", gold)
      .replace("$gray", gray)
      .replace("$darkGray", darkGray)
      .replace("$blue", blue)
      .replace("$green", green)
      .replace("$aqua", aqua)
      .replace("$red", red)
      .replace("$lightPurple", lightPurple)
      .replace("$yellow", yellow)
      .replace("$white", white)
      .replace("$obfuscated", obfuscated)
      .replace("$bold", bold)
      .replace("$strikethrough", strikethrough)
      .replace("$underline", underline)
      .replace("$italic", italic)
      .replace("$reset", reset);
  }
}
