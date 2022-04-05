package lobby;

import core.bungee.BungeeHandler;
import core.bungee.CoreBungeeCordClient;
import core.bungee.Server;
import core.core.CoreSendStringPacket;
import core.Utils;
import lobby.servers.ServersInventory;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
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
            if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                p.openInventory(ServersInventory.buildServerInventory());
                e.setCancelled(true);
            }
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
        if (Objects.equals(e.getClickedInventory(), ServersInventory.getServerInventory())) {
            if (e.getCurrentItem() != null) {
                Server server = new Server(ChatColor.stripColor(Objects.requireNonNull(e.getCurrentItem().getItemMeta()).getDisplayName()), BungeeHandler.getDataset().getPort(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName())), BungeeHandler.getDataset().getVersion(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName())));
                server.connect((Player) e.getWhoClicked());
            }
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e){
        Player player = e.getPlayer();
        Server server;
        switch(player.getLocation().add(0, -1, 0).getBlock().getType()){
            case HAY_BLOCK:
                server = new Server("BINGOSERVER", 25566, "1.16.5");
                server.connect(player);
                break;
            case QUARTZ_PILLAR:
                server = new Server("CHALLENGESERVER", 25561, "1.16.5");
                server.connect(player);
                break;
        }
    }

}
