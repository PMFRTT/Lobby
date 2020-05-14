
package lobby;

import core.CoreBungeeCordClient;
import core.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


import java.util.ArrayList;

public class LobbyInventory {


    public static Inventory getServerSelectorInventory() {
        Inventory ServerSelector = Bukkit.createInventory(null, 9, org.bukkit.ChatColor.AQUA + "Serverwahl");

        addServerToInventory(25566, "Bingo Server", 0, ServerSelector);
        addServerToInventory(25561, "Challenge Server", 1, ServerSelector);
        addServerToInventory(25562, "Survival Server", 2, ServerSelector);
        fillEmptySlots(ServerSelector);
        return ServerSelector;

    }

    private static void addServerToInventory(int port, String name, int slot, Inventory inventory) {

        if (CoreBungeeCordClient.isOnline(port)) {
            ItemStack serverPanel = new ItemStack(Material.LIME_STAINED_GLASS_PANE, 1);
            ItemMeta serverPanelMeta = serverPanel.getItemMeta();
            serverPanelMeta.setDisplayName(Utils.colorize("&2" + name));
            ArrayList<String> Lore = new ArrayList<String>();
            Lore.add(Utils.colorize(""));
            Lore.add(Utils.colorize("&aDieser Server ist Online"));
            Lore.add(Utils.colorize("&8Dieser Server benötigt die Version 1.15.2"));
            serverPanelMeta.setLore(Lore);
            serverPanel.setItemMeta(serverPanelMeta);
            inventory.setItem(slot, serverPanel);
        } else {
            ItemStack serverPanel = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
            ItemMeta serverPanelMeta = serverPanel.getItemMeta();
            serverPanelMeta.setDisplayName(Utils.colorize("&c" + name));
            ArrayList<String> Lore = new ArrayList<String>();
            Lore.add(Utils.colorize(""));
            Lore.add(Utils.colorize("&cDieser Server ist Offline"));
            Lore.add(Utils.colorize("&8Dieser Server benötigt die Version 1.15.2"));
            serverPanelMeta.setLore(Lore);
            serverPanel.setItemMeta(serverPanelMeta);
            inventory.setItem(slot, serverPanel);
        }

    }

    private static void fillEmptySlots(Inventory inventory){
        for(int i = 0; i < inventory.getSize(); i++){
            if(inventory.getItem(i) == null) {
                ItemStack empty = new ItemStack(Material.BLACK_STAINED_GLASS_PANE,1);
                ItemMeta emptyMeta = empty.getItemMeta();
                emptyMeta.setDisplayName(Utils.colorize("&7Kein Server verfügbar!"));
                empty.setItemMeta(emptyMeta);
                inventory.setItem(i, empty);
            }
        }
    }


}
