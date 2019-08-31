package com.andan.itemrep.ItemRep.Listener;

import com.andan.itemrep.ItemRep.ItemRep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PlayerListener implements Listener {

    @EventHandler
    private void onPlayerJoiner(PlayerJoinEvent e){
        ItemRep.getInstance().setItem(e.getPlayer());
    }


    @EventHandler
    private void onClick(InventoryClickEvent e){
        if (e.getCurrentItem()==null)return;
        if (e.getCurrentItem()==ItemRep.getInstance().t())e.setCancelled(true);
        if (e.getCurrentItem().hasItemMeta()&&e.getCurrentItem().getItemMeta().hasDisplayName()&&e.getCurrentItem().getItemMeta().getDisplayName().startsWith("§a§n§d§a§n"))e.setCancelled(true);

    }



    @EventHandler
    private void onde(PlayerDeathEvent e){
        if (!e.getKeepInventory()){
            List<ItemStack> drops = e.getDrops();
            if (drops.contains(ItemRep.getInstance().t())){
                drops.remove(ItemRep.getInstance().t());
            }
            ItemStack isss = null;
            for (ItemStack is: drops){
                if (is.hasItemMeta()&&is.getItemMeta().hasDisplayName()&&is.getItemMeta().getDisplayName().startsWith("§a§n§d§a§n")){
                    isss = is;
                }
            }
            if (isss!=null){
                drops.remove(isss);
            }
        }
    }

    @EventHandler
    private void onre(PlayerRespawnEvent e){
        ItemRep.getInstance().setItem(e.getPlayer());
    }



}
