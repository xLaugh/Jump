package fr.laugh.wondergames;

import fr.laugh.wondergames.listeners.Listeners;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
  private JumpManager jumpManager;
  
  public void onEnable() {
    System.out.println("Jump On");
    this.jumpManager = new JumpManager(this);
    getServer().getPluginManager().registerEvents((Listener)new Listeners(this.jumpManager), (Plugin)this);
  }
}
