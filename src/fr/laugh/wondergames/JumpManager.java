package fr.laugh.wondergames;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class JumpManager {
    private final Main plugin;
    private boolean inJump = false;
    private int jumpTimerTask;
    private Location checkpointLocation = null;
    private long startTime;

    public JumpManager(Main plugin) {
        this.plugin = plugin;
    }

    public void startJump(Player p) {
        if (!inJump) {
            inJump = true;
            startTime = System.currentTimeMillis();
            p.sendMessage("§a§lParkour commencé !");
            giveCancelItem(p);
            giveCheckpointItem(p);
            giveBackItem(p);
            startJumpTimer(p);
        }
    }

    public void cancelJump(Player p) {
        if (inJump) {
            inJump = false;
            p.sendMessage("§c§lParkour annulé !");
            stopJumpTimer();
            removeCheckpoint();
            p.getInventory().setItem(5, new ItemStack(Material.AIR));
            p.getInventory().setItem(4, new ItemStack(Material.AIR));
            p.getInventory().setItem(3, new ItemStack(Material.AIR));
            p.setLevel(0);
        }
    }

    public void playerDisconnect(Player p) {
        if (inJump) {
            inJump = false;
            stopJumpTimer();
            p.sendMessage("§c§lParkour annulé ! (Déconnexion)");
            p.getInventory().setItem(5, new ItemStack(Material.AIR));
            p.getInventory().setItem(4, new ItemStack(Material.AIR));
            p.getInventory().setItem(3, new ItemStack(Material.AIR));
            p.setLevel(0);
            removeCheckpoint();
        }
    }

    public void setCheckpoint(Location location) {
        checkpointLocation = location.clone();
    }

    public boolean hasCheckpoint() {
        return checkpointLocation != null;
    }

    public Location getCheckpoint() {
        return checkpointLocation;
    }
    
    public void removeCheckpoint() {
        checkpointLocation = null;
    }

    private void startJumpTimer(Player p) {
        jumpTimerTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            if (p.isOnline()) {
                updateXPBar(p);
            }
        }, 0L, 20L);
    }
    
    public void finishJump(Player p) {
        if (inJump) {
            inJump = false;
            stopJumpTimer();
            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;
            long seconds = elapsedTime / 1000;
            removeCheckpoint();
            p.sendMessage("§a§lVous avez terminé le parkour en " + seconds + " secondes !");
            p.getInventory().setItem(5, new ItemStack(Material.AIR));
            p.getInventory().setItem(4, new ItemStack(Material.AIR));
            p.getInventory().setItem(3, new ItemStack(Material.AIR));
            p.setLevel(0);
        }
    }

    private void stopJumpTimer() {
        Bukkit.getScheduler().cancelTask(jumpTimerTask);
    }

    private void giveCancelItem(Player p) {
        ItemStack lit = new ItemStack(Material.BED);
        ItemMeta litm = lit.getItemMeta();
        litm.setDisplayName("§cAnnuler");
        lit.setItemMeta(litm);
        p.getInventory().setItem(5, lit);
    }

    private void giveCheckpointItem(Player p) {
        ItemStack slime = new ItemStack(Material.SLIME_BALL);
        ItemMeta slimem = slime.getItemMeta();
        slimem.setDisplayName("§aSe téléporter au checkpoint");
        slime.setItemMeta(slimem);
        p.getInventory().setItem(3, slime);
    }
    
    private void giveBackItem(Player p) {
        ItemStack door = new ItemStack(Material.WOOD_DOOR);
        ItemMeta doorm = door.getItemMeta();
        doorm.setDisplayName("§cRéinitialiser");
        door.setItemMeta(doorm);
        p.getInventory().setItem(4, door);
    }

    private void updateXPBar(Player p) {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - startTime;
        long seconds = elapsedTime / 1000;
        p.setLevel((int) seconds);
    }
    
    public boolean isPlayerInJump() {
        return inJump;
    }
}
