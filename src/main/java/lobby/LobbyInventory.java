
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

	public static ItemStack bingoServer = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
	public static ItemMeta bingoServerMeta = bingoServer.getItemMeta();

	public static ItemStack challengeServer = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
	public static ItemMeta challengeServerMeta = challengeServer.getItemMeta();
	
	public static ItemStack survivalServer = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
	public static ItemMeta survivalServerMeta = survivalServer.getItemMeta();

	public static ItemStack empty = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
	public static ItemMeta emptyMeta = empty.getItemMeta();

	public static Inventory getServerSelectorInventory() {

		emptyMeta.setDisplayName("KEIN SERVER VERFÜGBAR!");
		empty.setItemMeta(emptyMeta);

		Inventory temp = Bukkit.createInventory(null, 9, org.bukkit.ChatColor.AQUA + "Serverwahl");
		
		if (CoreBungeeCordClient.isOnline(25566)) {
			bingoServer = new ItemStack(Material.LIME_STAINED_GLASS_PANE, 1);
			bingoServerMeta.setDisplayName(Utils.colorize("&2Bingo Server"));
			ArrayList<String> Lore = new ArrayList<String>();
			Lore.add(Utils.colorize(""));
			Lore.add(Utils.colorize("&aDieser Server ist Online"));
			Lore.add(Utils.colorize("&6Dieser Server benötigt die Version 1.15.2"));
			bingoServerMeta.setLore(Lore);
			bingoServerMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			bingoServerMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
			bingoServer.setItemMeta(bingoServerMeta);
			temp.setItem(0, bingoServer);
		} else {
			bingoServer = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
			bingoServerMeta.setDisplayName(Utils.colorize("&cBingo Server"));
			if (bingoServerMeta.hasEnchants()) {
				bingoServerMeta.removeEnchant(Enchantment.ARROW_DAMAGE);
			}
			ArrayList<String> Lore = new ArrayList<String>();
			Lore.add(Utils.colorize(""));
			Lore.add(Utils.colorize("&cDieser Server ist Offline"));
			Lore.add(Utils.colorize("&8Dieser Server benötigt die Version 1.15.2"));
			bingoServerMeta.setLore(Lore);
			bingoServer.setItemMeta(bingoServerMeta);
			temp.setItem(0, bingoServer);
		}

		if (CoreBungeeCordClient.isOnline(25561)) {
			challengeServer = new ItemStack(Material.LIME_STAINED_GLASS_PANE, 1);
			challengeServerMeta.setDisplayName(Utils.colorize("&2Challenge Server"));
			ArrayList<String> Lore = new ArrayList<String>();
			Lore.add(Utils.colorize(""));
			Lore.add(Utils.colorize("&aDieser Server ist Online"));
			Lore.add(Utils.colorize("&8Dieser Server benötigt die Version 1.15.2"));
			challengeServerMeta.setLore(Lore);
			challengeServerMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			challengeServerMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
			challengeServer.setItemMeta(challengeServerMeta);
			temp.setItem(1, challengeServer);
		} else {
			challengeServer = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
			challengeServerMeta.setDisplayName(Utils.colorize("&cChallenge Server"));
			ArrayList<String> Lore = new ArrayList<String>();
			Lore.add(Utils.colorize(""));
			Lore.add(Utils.colorize("&cDieser Server ist Offline"));
			Lore.add(Utils.colorize("&8Dieser Server benötigt die Version 1.15.2"));
			challengeServerMeta.setLore(Lore);
			if (challengeServerMeta.hasEnchants()) {
				challengeServerMeta.removeEnchant(Enchantment.ARROW_DAMAGE);
			}
			challengeServer.setItemMeta(challengeServerMeta);
			temp.setItem(1, challengeServer);
		}
		
		if (CoreBungeeCordClient.isOnline(25562)) {
			survivalServer = new ItemStack(Material.LIME_STAINED_GLASS_PANE, 1);
			survivalServerMeta.setDisplayName(Utils.colorize("&2Survival Server"));
			ArrayList<String> Lore = new ArrayList<String>();
			Lore.add(Utils.colorize(""));
			Lore.add(Utils.colorize("&aDieser Server ist Online"));
			Lore.add(Utils.colorize("&8Dieser Server benötigt die Version 1.15.2"));
			survivalServerMeta.setLore(Lore);
			survivalServerMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			survivalServerMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
			survivalServer.setItemMeta(survivalServerMeta);
			temp.setItem(2, survivalServer);
		} else {
			survivalServer = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
			survivalServerMeta.setDisplayName(Utils.colorize("&cSurvival Server"));
			ArrayList<String> Lore = new ArrayList<String>();
			Lore.add(Utils.colorize(""));
			Lore.add(Utils.colorize("&cDieser Server ist Offline"));
			Lore.add(Utils.colorize("&8Dieser Server benötigt die Version 1.15.2"));
			survivalServerMeta.setLore(Lore);
			if (survivalServerMeta.hasEnchants()) {
				survivalServerMeta.removeEnchant(Enchantment.ARROW_DAMAGE);
			}
			survivalServer.setItemMeta(survivalServerMeta);
			temp.setItem(2, survivalServer);
		}

		temp.setItem(3, empty);
		temp.setItem(4, empty);
		temp.setItem(5, empty);
		temp.setItem(6, empty);
		temp.setItem(7, empty);
		temp.setItem(8, empty);
		return temp;

	}

}
