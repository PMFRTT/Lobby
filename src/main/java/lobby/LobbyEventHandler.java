package lobby;

import core.CoreBungeeCordClient;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;

public class LobbyEventHandler implements Listener {

    LobbyMain main;


    public LobbyEventHandler(LobbyMain main){
        this.main = main;
    }

    public void initialize(){
        Bukkit.getPluginManager().registerEvents(this, main);
    }



    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        main.playerTimer.remove(p.getDisplayName());
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {

        main.portalTimer.put(e.getPlayer().getDisplayName(), 5);
        Player player = e.getPlayer();
        player.teleport(new Location(Bukkit.getWorld("world"), -40, 21, 88));
        e.setJoinMessage(main.getJoinMessage(player));
        player.getInventory().clear();
        player.getInventory().setItem(4, main.selector);
        player.getInventory().setItem(8, main.quit);
    }


    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        e.setQuitMessage(main.getDisconnectMessage(player));
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
            p.openInventory(LobbyInventory.getServerSelectorInventory());
        } else if (p.getItemInHand().equals(main.quit)) {
            p.kickPlayer(ChatColor.DARK_RED + "Du hast das Spiel verlassen!");
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
        if (e.getInventory().equals(e.getClickedInventory())) {
            if (e.getSlot() == 0) {
                e.setCancelled(true);
                CoreBungeeCordClient.moveToServer(player, "BingoServer");
            } else if (e.getSlot() == 1) {
                e.setCancelled(true);
                CoreBungeeCordClient.moveToServer(player, "ChallengeServer");

            } else if (e.getSlot() == 2) {
                e.setCancelled(true);
                CoreBungeeCordClient.moveToServer(player, "SurvivalServer");

            } else if (e.getSlot() == 8) {
                e.setCancelled(true);
                CoreBungeeCordClient.moveToServer(player, "DebugServer");

            } else {
                e.setCancelled(true);
            }

        }

    }

}