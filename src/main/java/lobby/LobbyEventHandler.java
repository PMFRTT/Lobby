package lobby;

import core.bungee.CoreBungeeCordClient;
import core.core.CoreMain;
import core.core.CoreSendStringPacket;
import core.Utils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;

public class LobbyEventHandler implements Listener {

    LobbyMain main;


    public LobbyEventHandler(LobbyMain main) {
        this.main = main;
    }

    public void initialize() {
        Bukkit.getPluginManager().registerEvents(this, main);
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        LobbyMain.jumpAndRun.addPlayerTimer(e.getPlayer());
        Player player = e.getPlayer();
        player.teleport(new Location(Bukkit.getWorld("world"), -40, 21, 88));
        player.getInventory().clear();
        player.getInventory().setItem(4, LobbyMain.selector);
        player.getInventory().setItem(8, LobbyMain.quit);
        CoreSendStringPacket.sendPacketToTitle(e.getPlayer(), Utils.colorize("&3Moin&f " + e.getPlayer().getDisplayName()), Utils.colorize(""));
        CoreSendStringPacket.sendPacketToHotbar(e.getPlayer(), Utils.colorize("Willkommen auf &3PMFRTT-Networks&f!"));
    }


    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent e) {
        e.setCancelled(true);
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onPlayerRightClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (p.getItemInHand().equals(main.selector)) {
            p.openInventory(LobbyInventory.getServerSelectorInventory(p));
            e.setCancelled(true);
        }
        if (p.getItemInHand().equals(main.quit)) {
            p.kickPlayer(ChatColor.DARK_RED + "Du hast das Spiel verlassen!");
            e.setCancelled(true);
        }

    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (!e.getPlayer().isOp()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInventoryClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (e.getClickedInventory() != null) {
            if (e.getClickedInventory().equals(LobbyInventory.ServerSelector)) {
                if (e.getSlot() == 0) {
                    e.setCancelled(true);
                    CoreBungeeCordClient.moveToServer(player, "BINGOSERVER");
                } else if (e.getSlot() == 1) {
                    e.setCancelled(true);
                    CoreBungeeCordClient.moveToServer(player, "CHALLENGESERVER");
                } else if (e.getSlot() == 2) {
                    e.setCancelled(true);
                    CoreBungeeCordClient.moveToServer(player, "SURVIVALSERVER");
                } else if (e.getSlot() == 3) {
                    e.setCancelled(true);
                    CoreBungeeCordClient.moveToServer(player, "MobArena");

                } else if (e.getSlot() == 4) {
                    e.setCancelled(true);
                    CoreBungeeCordClient.moveToServer(player, "Skyblock");

                } else if (e.getSlot() == 8) {
                    e.setCancelled(true);
                    CoreBungeeCordClient.moveToServer(player, "Modpack");

                } else {
                    e.setCancelled(true);
                }


            }
        }

    }

}
