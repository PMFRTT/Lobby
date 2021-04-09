package lobby.servers;

import core.Utils;
import core.bungee.Server;
import core.core.CoreMain;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class ServersInventory {

    private static final Inventory serverInventory = Bukkit.createInventory(null, 9);

    public static ItemStack selector = new ItemStack(Material.COMPASS, 1);
    public static ItemMeta selectorItemMeta = selector.getItemMeta();

    public static void init() {
        selectorItemMeta.setDisplayName(ChatColor.AQUA + "Serverwahl");
        selector.setItemMeta(selectorItemMeta);
        buildServerInventory();
    }

    public static Inventory buildServerInventory() {
        ArrayList<Server> servers = CoreMain.mySQLBungee.getServers();
        int i = 0;
        for (Server server : servers) {
            if (!server.getName().equals("LOBBYSERVER")) {
                ItemStack serverPanel = new ItemStack(Material.LIME_STAINED_GLASS_PANE, 1);
                ItemMeta serverPanelMeta = serverPanel.getItemMeta();
                assert serverPanelMeta != null;
                serverPanelMeta.setDisplayName(Utils.colorize("&a" + WordUtils.capitalize(server.getName().toLowerCase())));
                ArrayList<String> Lore = new ArrayList<String>();
                Lore.add(Utils.colorize("&8Dieser Server benötigt die Version " + server.getVersion()));
                serverPanelMeta.setLore(Lore);
                serverPanel.setItemMeta(serverPanelMeta);
                serverInventory.setItem(i, serverPanel);
                i++;
            }
        }
        return serverInventory;
    }

    public static Inventory getServerInventory(){
        return serverInventory;
    }

}
