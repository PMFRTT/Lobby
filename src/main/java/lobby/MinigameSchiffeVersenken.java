package lobby;

import core.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class MinigameSchiffeVersenken implements Listener {

    private static LobbyMain main;
    private Player activePlayer;
    private static boolean hasWon = false;

    private Player challengerPlayer;
    private Player challengedPlayer;

    private int challengerShipsAvailable = 12;
    private int challengedShipsAvailable = 12;

    private int challengerShipsFound = 0;
    private int challengedShipsFound = 0;

    public Inventory challengerInventory;
    public Inventory challengedInventory;
    
    private ItemStack shipPlaced = new ItemStack(Material.RED_STAINED_GLASS_PANE);
    private ItemMeta shipPlacedMeta = shipPlaced.getItemMeta();
    
    private ItemStack shipFound = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
    private ItemMeta shipFoundMeta = shipFound.getItemMeta();
    
    private ItemStack emptyField = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
    private ItemMeta emptyFieldMeta = emptyField.getItemMeta();
    
    private ItemStack emptyFieldFound = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
    private ItemMeta emptyFieldFoundMeta = emptyFieldFound.getItemMeta();
    
    

    private boolean needsClearing = false;

    private HashMap<Player, List<Integer>> shipSlots = new HashMap<Player, List<Integer>>();
    private HashMap<Player, List<Integer>> openedFields = new HashMap<Player, List<Integer>>();


    public MinigameSchiffeVersenken(LobbyMain main) {
        this.main = main;
    }

    private void initialize() {
        Bukkit.getPluginManager().registerEvents(this, main);
        shipPlacedMeta.setDisplayName(Utils.colorize("&cDein eigenes Schiff"));
        shipPlaced.setItemMeta(shipPlacedMeta);
        
        shipFoundMeta.setDisplayName(Utils.colorize("&aEin Schiff deines Gegners"));
        shipFound.setItemMeta(shipFoundMeta);
        
        emptyFieldMeta.setDisplayName(Utils.colorize("--"));
        emptyField.setItemMeta(emptyFieldMeta);
        
        emptyFieldFoundMeta.setDisplayName(Utils.colorize("&8--"));
        emptyFieldFound.setItemMeta(emptyFieldFoundMeta);
    }


    public void gameLoop(Player challenger, Player challenged) {

        initialize();

        openedFields.put(challenged, new ArrayList<Integer>());
        openedFields.put(challenger, new ArrayList<Integer>());

        challengerPlayer = challenger;
        challengedPlayer = challenged;

        challengerInventory = createChallengerInventory(challenger, challenged);
        challengedInventory = createChallengedInventory(challenged, challenger);

        challenger.openInventory(challengerInventory);
        challenged.openInventory(challengedInventory);

        BukkitTask runnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (challengedShipsAvailable != 0 || challengerShipsAvailable != 0) {
                    addItemToInventory(challengedInventory, 45, shipPlaced, challengedShipsAvailable);
                    addItemToInventory(challengerInventory, 45, shipPlaced, challengedShipsAvailable);
                    addItemToInventory(challengerInventory, 53, shipPlaced, challengerShipsAvailable);
                    addItemToInventory(challengedInventory, 53, shipPlaced, challengerShipsAvailable);
                    needsClearing = true;
                } else if (needsClearing) {
                    Bukkit.getScheduler().runTaskLater(main, new Runnable() {
                        @Override
                        public void run() {
                            clearInventory(challengedInventory);
                            clearInventory(challengerInventory);
                            activePlayer = challenged;
                            displayActivePlayer();
                            needsClearing = false;
                        }
                    }, 20);
                } else {
                    addItemToInventory(challengedInventory, 45, shipFound, challengedShipsFound);
                    addItemToInventory(challengerInventory, 45, shipFound, challengedShipsFound);
                    addItemToInventory(challengerInventory, 53, shipFound, challengerShipsFound);
                    addItemToInventory(challengedInventory, 53, shipFound, challengerShipsFound);
                }

                if (challengerShipsFound == 12) {
                    challenged.playSound(challenged.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
                    challenger.playSound(challenger.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 1.0f, 1.0f);
                    hasWon = true;
                }
                if (challengedShipsFound == 12) {
                    challenger.playSound(challenger.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
                    challenged.playSound(challenged.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 1.0f, 1.0f);
                    hasWon = true;
                }

                if (hasWon) {
                    cancel();
                }
            }
        }.runTaskTimerAsynchronously(main, 0L, 1L);
    }


    private Inventory createChallengerInventory(Player challenger, Player challenged) {

        Inventory temp = Bukkit.createInventory((InventoryHolder) challenger, 54, Utils.colorize("Schiffe versenken - &c" + challenged.getDisplayName()));
        temp.setItem(0, getHead(challenged));
        temp.setItem(8, getHead(challenger));

        clearInventory(temp);

        return temp;
    }

    private Inventory createChallengedInventory(Player challenged, Player challenger) {

        Inventory temp = Bukkit.createInventory((InventoryHolder) challenged, 54, Utils.colorize("Schiffe versenken - &c" + challenger.getDisplayName()));
        temp.setItem(0, getHead(challenged));
        temp.setItem(8, getHead(challenger));

        clearInventory(temp);

        return temp;
    }

    private Inventory addItemToInventory(Inventory inventory, int slot, ItemStack itemStack, int amount) {

        itemStack.setAmount(amount);
        inventory.setItem(slot, itemStack);
        return inventory;
    }


    private static ItemStack getHead(Player player) {

        ItemStack item = new ItemStack(Material.LEGACY_SKULL_ITEM, 1, (short) 3);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwner(player.getName());
        meta.setDisplayName(player.getName());
        item.setItemMeta(meta);

        return item;
    }

    private void clearInventory(Inventory inventory) {
        for (int i = 0; i < 54; i++) {
            if (i % 9 > 0 && i % 9 < 8) {
                addItemToInventory(inventory, i, emptyField, 1);
            }
        }
    }

    @EventHandler
    private void getShipPlacement(InventoryClickEvent e) {
        if (e.getClickedInventory().equals(challengedInventory)) {
            if (e.getWhoClicked() == challengedPlayer) {
                if (e.getCurrentItem() != null) {
                    e.setCancelled(true);
                    if (challengedShipsAvailable != 0 && e.getCurrentItem().getType() == Material.GRAY_STAINED_GLASS_PANE) {
                        addItemToInventory(challengedInventory, e.getSlot(), shipPlaced, 1);
                        if (shipSlots.containsKey(challengedPlayer)) {
                            List<Integer> temp = shipSlots.get(challengedPlayer);
                            temp.add(e.getSlot());
                            shipSlots.put(challengedPlayer, temp);
                        } else {
                            shipSlots.put(challengedPlayer, new ArrayList<Integer>() {{
                                add(e.getSlot());
                            }});
                        }
                        challengedShipsAvailable--;
                    } else if (challengerShipsAvailable == 0 && challengedShipsAvailable == 0) {
                        if (activePlayer == challengedPlayer) {
                            if (!openedFields.get(challengerPlayer).contains(e.getSlot())) {
                                                                    if (shipSlots.get(challengerPlayer).contains(e.getSlot())) {
                                        List<Integer> temp = shipSlots.get(challengerPlayer);
                                        temp.removeAll(Arrays.asList(e.getSlot()));
                                        shipSlots.put(challengerPlayer, temp);
                                        activePlayer = challengerPlayer;
                                        displayActivePlayer();
                                        shipFound.setAmount(1);
                                        challengedInventory.setItem(e.getSlot(), shipFound);
                                        challengedShipsFound++;
                                        List<Integer> temp2 = openedFields.get(challengerPlayer);
                                        temp2.add(e.getSlot());
                                        openedFields.put(challengerPlayer, temp2);
                                    } else if (!openedFields.get(challengerPlayer).contains(e.getSlot())) {
                                        challengedInventory.setItem(e.getSlot(), emptyFieldFound);
                                        activePlayer = challengerPlayer;
                                        displayActivePlayer();
                                        List<Integer> temp2 = openedFields.get(challengerPlayer);
                                        temp2.add(e.getSlot());
                                        openedFields.put(challengerPlayer, temp2);
                                    }
                                } else {
                                    challengedPlayer.playSound(challengedPlayer.getLocation(), Sound.BLOCK_ANVIL_HIT, 1.0f, 1.0f);
                                }
                            }
                        }
                }
            }
        } else if (e.getClickedInventory().equals(challengerInventory)) {
            if (e.getWhoClicked() == challengerPlayer) {
                if (e.getCurrentItem() != null) {
                    e.setCancelled(true);
                    if (challengerShipsAvailable != 0 && e.getCurrentItem().getType() == Material.GRAY_STAINED_GLASS_PANE) {
                        addItemToInventory(challengerInventory, e.getSlot(), shipPlaced, 1);
                        if (shipSlots.containsKey(challengerPlayer)) {
                            List<Integer> temp = shipSlots.get(challengerPlayer);
                            temp.add(e.getSlot());
                            shipSlots.put(challengerPlayer, temp);
                        } else {
                            shipSlots.put(challengerPlayer, new ArrayList<Integer>() {{
                                add(e.getSlot());
                            }});
                        }
                        challengerShipsAvailable--;
                    } else if (challengerShipsAvailable == 0 && challengedShipsAvailable == 0) {
                        if (activePlayer == challengerPlayer) {
                            if (!openedFields.get(challengedPlayer).contains(e.getSlot())) {
                                if (shipSlots.get(challengedPlayer).contains(e.getSlot())) {
                                    List<Integer> temp = shipSlots.get(challengedPlayer);
                                    temp.removeAll(Arrays.asList(e.getSlot()));
                                    shipSlots.put(challengedPlayer, temp);
                                    activePlayer = challengedPlayer;
                                    displayActivePlayer();
                                    shipFound.setAmount(1);
                                    challengerInventory.setItem(e.getSlot(), shipFound);
                                    challengerShipsFound++;
                                    List<Integer> temp2 = openedFields.get(challengedPlayer);
                                    temp2.add(e.getSlot());
                                    openedFields.put(challengedPlayer, temp2);
                                } else if (!openedFields.get(challengedPlayer).contains(e.getSlot())) {
                                    challengerInventory.setItem(e.getSlot(), emptyFieldFound);
                                    activePlayer = challengedPlayer;
                                    displayActivePlayer();
                                    List<Integer> temp2 = openedFields.get(challengedPlayer);
                                    temp2.add(e.getSlot());
                                    openedFields.put(challengedPlayer, temp2);
                                }
                            } else {
                                challengerPlayer.playSound(challengerPlayer.getLocation(), Sound.BLOCK_ANVIL_HIT, 1.0f, 1.0f);
                            }
                        }
                    }
                }
            }
        }
        displayActivePlayer();
    }

    private void displayActivePlayer() {

        ItemStack activeItemStack = new ItemStack(Material.GREEN_CONCRETE_POWDER);
        ItemMeta activeItemMeta = activeItemStack.getItemMeta();
        activeItemMeta.setDisplayName(Utils.colorize("&aDu bist an der Reihe"));
        activeItemStack.setItemMeta(activeItemMeta);

        ItemStack inactiveItemStack = new ItemStack(Material.ORANGE_CONCRETE_POWDER);
        ItemMeta inactiveItemMeta = inactiveItemStack.getItemMeta();
        inactiveItemMeta.setDisplayName(Utils.colorize("&6Dein Gegner ist an der Reihe"));
        inactiveItemStack.setItemMeta(inactiveItemMeta);
        if (hasWon){
            challengedPlayer.closeInventory();
            challengerPlayer.closeInventory();
        }

        if (activePlayer == challengedPlayer) {
            for (int i = 9; i < 37; i += 9) {
                challengerInventory.setItem(i, activeItemStack);
                challengedInventory.setItem(i, activeItemStack);
            }
            for (int i = 17; i < 45; i += 9) {
                challengerInventory.setItem(i, inactiveItemStack);
                challengedInventory.setItem(i, inactiveItemStack);
            }
        } else if (activePlayer == challengerPlayer) {
            for (int i = 9; i < 37; i += 9) {
                challengerInventory.setItem(i, inactiveItemStack);
                challengedInventory.setItem(i, inactiveItemStack);
            }
            for (int i = 17; i < 45; i += 9) {
                challengerInventory.setItem(i, activeItemStack);
                challengedInventory.setItem(i, activeItemStack);
            }
        }
    }

}


