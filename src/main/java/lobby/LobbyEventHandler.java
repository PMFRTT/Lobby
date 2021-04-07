package lobby;

import core.bungee.CoreBungeeCordClient;
import core.bungee.Server;
import core.core.CoreMain;
import core.core.CoreSendStringPacket;
import core.Utils;
import lobby.servers.ServersInventory;
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

import java.util.Objects;

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
        LobbyMain.jumpAndRun.getPlayerTimer(e.getPlayer()).setCheckPoint(); // fixed #4
        Player player = e.getPlayer();
        player.teleport(new Location(Bukkit.getWorld("world"), -40, 21, 88));
        player.getInventory().clear();
        player.getInventory().setItem(4, ServersInventory.selector);
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
        if (p.getItemInHand().equals(ServersInventory.selector)) {
            p.openInventory(ServersInventory.buildServerInventory());
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
        if (e.getClickedInventory().equals(ServersInventory.buildServerInventory())) {
            if (e.getCurrentItem() != null) {
                Server server = new Server(ChatColor.stripColor(Objects.requireNonNull(e.getCurrentItem().getItemMeta()).getDisplayName()), CoreMain.mySQLBungee.getPort(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName())), CoreMain.mySQLBungee.getVersion(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName())));
                server.connect((Player) e.getWhoClicked());
            }
            e.setCancelled(true);
        }


    }

}
