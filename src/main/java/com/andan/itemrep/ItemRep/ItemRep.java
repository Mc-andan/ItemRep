package com.andan.itemrep.ItemRep;

import com.andan.itemrep.ItemRep.Listener.PlayerListener;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class ItemRep extends JavaPlugin {

    private static ItemRep instance;

    public static ItemRep getInstance(){
     return instance;
    }



    @Override
    public void onEnable() {

        // Plugin startup logic
        instance = this;
        loadConfig();
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
    }

    private void loadConfig() {
        File f = new File(getDataFolder() + "/config.yml");
        if (f.exists()) {
            reloadConfig();
        } else {
            saveDefaultConfig();
            reloadConfig();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player && args.length == 1) {
            String id = args[0];
            if (getConfig().getConfigurationSection("List").getKeys(false).contains(id)) {
                withItemPlayer((Player) sender,id);
            }


        } else if (sender instanceof ConsoleCommandSender && args.length == 2) {
            String id = args[0];
            Player p = getServer().getPlayerExact(args[1]);
            if (p!=null&&getConfig().getConfigurationSection("List").getKeys(false).contains(id)){
                withItemPlayer(p,id);
            }
        } else {
            sender.sendMessage("§c错误的使用方式.");
        }


        return false;
    }

    private void withItemPlayer(Player player, String id) {
        int slot = ItemRep.getInstance().getConfig().getInt("Item.Slot");

        int ID = getConfig().getInt("List."+id+".id");
        short data = (short) getConfig().getInt("List."+id+".data");
        ItemStack is = player.getInventory().getItem(slot);
        is.setTypeId(ID);
        is.setDurability(data);

        player.getInventory().setItem(slot,is);
        int time = getConfig().getInt("List."+id+".time");
        player.sendMessage(getConfig().getString("List."+id+".message.enable").replace("&","§").replace("%s%",String.valueOf(time)));

        if (time>0){
            getServer().getScheduler().runTaskLater(this, new Runnable() {
                @Override
                public void run() {
                    setItem(player);
                    player.sendMessage(getConfig().getString("List."+id+".message.disable"));
                }
            },time*20);
        }


    }





    public void setItem(Player p){
        int slot = ItemRep.getInstance().getConfig().getInt("Item.Slot");
        ItemStack is = p.getInventory().getItem(slot);
        if (is==null){
            p.getInventory().setItem(slot,t());
        }else if (is==t()){

        }else {
            if (is.hasItemMeta()&&is.getItemMeta().hasDisplayName()&&is.getItemMeta().getDisplayName().contains("§a§n§d§a§n")){
                if (is.getTypeId()!=t().getTypeId()){
                    p.getInventory().setItem(slot,t());
                }
            }else {
                p.getWorld().dropItem(p.getLocation(),is);
                p.getInventory().setItem(slot,t());
            }
        }
        p.updateInventory();
    }

    public ItemStack t(){
        ItemStack is = new ItemStack(Material.getMaterial(ItemRep.getInstance().getConfig().getInt("Item.ID")));
        ItemMeta itemMeta= is.getItemMeta();
        itemMeta.setDisplayName("§a§n§d§a§n§r"+ItemRep.getInstance().getConfig().getString("Item.name"));
        List<String> lore = new ArrayList<>();
        for (String s:ItemRep.getInstance().getConfig().getStringList("Item.Lore")){
            lore.add(s.replace("&","§"));
        }
        itemMeta.setLore(lore);
        is.setItemMeta(itemMeta);
        return is;
    }
}
