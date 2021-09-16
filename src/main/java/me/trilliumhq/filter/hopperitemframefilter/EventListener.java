package me.trilliumhq.filter.hopperitemframefilter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Rotation;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class EventListener implements Listener {
    public EventListener() {
    }

    private List<Entity> getEntities(Location location, double x, double y, double z) {
        List<Entity> result = new ArrayList();
        result.addAll(location.getWorld().getNearbyEntities(location, -x, -y, -z));
        result.addAll(location.getWorld().getNearbyEntities(location, x, y, z));
        return result;
    }

    @EventHandler
    public void onItemMove(InventoryMoveItemEvent event) {
        if (event.getInitiator().equals(event.getDestination()) && event.getInitiator().getType().equals(InventoryType.HOPPER)) {
            Inventory hopper = event.getInitiator();
            Location sourceLocation = event.getSource().getLocation();
            Collection<Entity> closeEntities = sourceLocation.getWorld().getNearbyEntities(sourceLocation, 1.2D, 1.2D, 1.2D);
            List<ItemStack> filterInItems = new ArrayList();
            List<ItemStack> filterOutItems = new ArrayList();
            Iterator var7 = closeEntities.iterator();

            while(true) {
                while(true) {
                    Entity closeEntity;
                    ItemFrame itemFrame;
                    do {
                        do {
                            do {
                                if (!var7.hasNext()) {
                                    if (!filterInItems.contains(new ItemStack(Material.AIR)) && !filterOutItems.contains(new ItemStack(Material.AIR)) && (!this.inFilter(filterInItems, event.getItem()) || !this.inFilter(filterOutItems, event.getItem())) && (this.inFilter(filterOutItems, event.getItem()) || !this.inFilter(filterInItems, event.getItem()) && filterInItems.size() != 0)) {
                                        event.setCancelled(true);
                                        ItemStack[] contents = event.getSource().getContents();
                                        int length = contents.length;

                                        for(int i = 0; i < length; ++i) {
                                            ItemStack sourceItem = contents[i];
                                            if (sourceItem != null && this.inFilter(filterInItems, sourceItem) && !this.inFilter(filterOutItems, sourceItem) && (!this.inFilter(filterInItems, sourceItem) || !this.inFilter(filterOutItems, sourceItem))) {
                                                ItemStack outItem = new ItemStack(sourceItem);
                                                outItem.setAmount(1);
                                                event.getInitiator().addItem(new ItemStack[]{outItem});
                                                sourceItem.setAmount(sourceItem.getAmount() - 1);
                                                return;
                                            }
                                        }
                                    }

                                    return;
                                }

                                closeEntity = (Entity)var7.next();
                            } while(!(closeEntity instanceof ItemFrame));

                            itemFrame = (ItemFrame)closeEntity;
                        } while(!closeEntity.getType().equals(EntityType.ITEM_FRAME));
                    } while(!closeEntity.getLocation().getBlock().getRelative(itemFrame.getAttachedFace()).equals(hopper.getLocation().getBlock()));
                        filterInItems.add(itemFrame.getItem());
                }
            }
        }
    }

    private boolean inFilter(List<ItemStack> items, ItemStack eItem) {
        return items.size() != 0 && items.stream().anyMatch((item) -> item.getType() != Material.AIR && (item.isSimilar(eItem) || item.getType() == eItem.getType()));
    }
}
