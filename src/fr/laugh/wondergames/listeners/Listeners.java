package fr.laugh.wondergames.listeners;

import fr.laugh.wondergames.JumpManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class Listeners implements Listener {
  private final JumpManager jumpManager;
  private Location jumpspawn = new Location(Bukkit.getWorld("world"), -195.5, 67, -8.5, 58.5f, 0.5f);
  
  public Listeners(JumpManager jumpManager) {
    this.jumpManager = jumpManager;
  }
  
  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent e) {
    Player p = e.getPlayer();
    ItemStack hand = p.getItemInHand();
    if (e.getAction().name().contains("PHYSICAL") && e.getClickedBlock() != null) {
      Block clickedBlock = e.getClickedBlock();
      if (clickedBlock.getType() == Material.GOLD_PLATE) {
        if (!this.jumpManager.isPlayerInJump())
          this.jumpManager.startJump(p); 
      } else if (clickedBlock.getType() == Material.IRON_PLATE) {
        if (this.jumpManager.isPlayerInJump()) {
          this.jumpManager.setCheckpoint(p.getLocation());
        } 
      } else if (clickedBlock.getType() == Material.WOOD_PLATE && 
        this.jumpManager.isPlayerInJump()) {
        this.jumpManager.finishJump(p);
      } 
    } else if (e.getAction().name().contains("RIGHT_CLICK") && hand.getType() == Material.BED) {
      this.jumpManager.cancelJump(p);
    } else if (e.getAction().name().contains("RIGHT_CLICK") && hand.getType() == Material.WOOD_DOOR) {
    	p.teleport(jumpspawn);
    } else if (e.getAction().name().contains("RIGHT_CLICK") && hand.getType() == Material.SLIME_BALL) {
      if (this.jumpManager.hasCheckpoint()) {
        p.teleport(this.jumpManager.getCheckpoint());
      } else {
        p.sendMessage("§cPas de checkpoint !");
      } 
    } 
  }
  
  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent e) {
      Player p = e.getPlayer(); 
      if (this.jumpManager.hasCheckpoint() && p.getLocation().distance(this.jumpManager.getCheckpoint()) < 1.0D) { // Check
          this.jumpManager.removeCheckpoint(); // Check
      } 
      this.jumpManager.cancelJump(p); // Check
  }
}
