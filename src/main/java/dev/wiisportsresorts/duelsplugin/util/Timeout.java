package dev.wiisportsresorts.duelsplugin.util;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class Timeout {
  public Runnable callback;
  Timeout(int delayMs, Runnable callback) {
    this.callback = callback;
    CompletableFuture.delayedExecutor(delayMs, TimeUnit.MILLISECONDS).execute(this::run);
  }

  private void run() {
    callback.run();
  }

  public static Timeout setTimeout(int delayMs, final Runnable callback) {
     return new Timeout(delayMs, callback);
  }
}
