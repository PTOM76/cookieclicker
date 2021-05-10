package ml.pkom.cookieclicker;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Plugin extends JavaPlugin implements Listener {

    public final String MOD_ID = "cookieclicker"; // MODID
    public final String MOD_NAME = "CookieClicker"; // MOD名
    public final String MOD_VER = "1.0.0"; // MODバージョン
    public final String MOD_AUT = "Pitan_MAD"; // MOD開発者

    public static ArrayList<User> users = new ArrayList<>();
    public static ArrayList<Player> playingUsers = new ArrayList<>();
    
    FileConfiguration config;

    @Override
    public void onEnable() {
        config = getConfig();
        if(!getDataFolder().exists()){
            getDataFolder().mkdir();            
        }
        getLogger().info(MOD_NAME + " is enabled!");
        getServer().getPluginManager().registerEvents(new GuiClickEvent(), this);
        new BukkitRunnable() {
            public void run() {
                for (Player player : playingUsers) {
                    Integer cursorAmount = users.get(getIndexOfUser(player)).getCursorAmount();
                    if (cursorAmount != 0 || cursorAmount == null) {
                        users.get(getIndexOfUser(player)).addCookiesAmount(cursorAmount);
                        playCookieClicker(player);
                    }
                }
            }
        }.runTaskTimer(this, 0, 20);
    }

    @Override
    public void onDisable() {
        saveCookieConfig();
        getLogger().info(MOD_NAME + " is disabled!");
    }

    public void loadPlayerSaveData(Player player){
        if (new File(getDataFolder(), "config.yml").exists()) {
            if(config.contains("save." + player.getUniqueId())){
                users.get(getIndexOfUser(player))
                        .setCookiesAmount(config.getInt("save." + player.getUniqueId() + ".data.cookies"));
                users.get(getIndexOfUser(player))
                        .setCursorAmount(config.getInt("save." + player.getUniqueId() + ".data.cursor"));
            }
        }
        return;
    }

    public void saveCookieConfig(){
        for (User user : users) {
            config.set("save." + user.getPlayer().getUniqueId() + ".name", user.getPlayer().getName());
            config.set("save." + user.getPlayer().getUniqueId() + ".data.cookies", user.getCookiesAmount());
            config.set("save." + user.getPlayer().getUniqueId() + ".data.cursor", user.getCursorAmount());
        }
        saveConfig();
    }

    public void addUser(Player player){
        boolean isSet = false;
        for (User user : users) {
            if (user.getPlayer() == player) {
                isSet = true;
                break;
            }
        }
        if (!isSet) {
            users.add(new User(player));
            loadPlayerSaveData(player);
            //getLogger().info("プレイヤーをクッキーユーザーに追加しました。");
        }
    }

    public static int getIndexOfUser(Player player) {
        for (User user : users) {
            if (user.getPlayer() == player) {
                return users.indexOf(user);
            }
        }
        return -1;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String str, String[] args) {
        if (cmd.getName().equalsIgnoreCase("cookieclicker")) {
            Player player = (Player) sender;
            addUser(player);
            playCookieClicker(player);
            return true;
        }
        if (cmd.getName().equalsIgnoreCase("cookies")) {
            Player player = (Player) sender;
            if(args.length == 0){
                addUser(player);
                sender.sendMessage("§aあなたの持っているクッキーは" + users.get(getIndexOfUser(player)).getCookiesAmount() + "です。");
            }else if(args.length >= 1){
                Player argsPlayer =  Bukkit.getPlayer(args[0]);
                addUser(argsPlayer);
                sender.sendMessage("§a" + argsPlayer.getName() + "の持っているクッキーは" + users.get(getIndexOfUser(argsPlayer)).getCookiesAmount() + "です。");
            }
            return true;
        }
        return false;
    }

    public static ItemStack cookie;
    public static ItemStack cursor;

    public static void playCookieClicker(Player player) {
        playingUsers.add(player);
        Inventory gui = Bukkit.createInventory(null, 54, "§rCookie:" + users.get(getIndexOfUser(player)).getCookiesAmount() + " §6§lCookieClicker");
        cookie = new ItemStack(Material.COOKIE);
        ItemMeta cookieMeta = cookie.getItemMeta();
        cookieMeta.setDisplayName("§r[RightClick]");
        cookie.setItemMeta(cookieMeta);
        gui.setItem(10, cookie);
        for (int i = 1; i < 4; i++) {
            setSlot(gui, -6 + (9 * i), Material.STAINED_GLASS_PANE, 1, 7, "§r", null);
        }
        for (int i = 1; i < 5; i++) {
            setSlot(gui, 26 + i, Material.STAINED_GLASS_PANE, 1, 7, "§r", null);
        }

        cursor = new ItemStack(Material.WOOL);
        ItemMeta cursorMeta = cursor.getItemMeta();
        cursorMeta.setDisplayName("§rカーソル §7[§d" + users.get(getIndexOfUser(player)).getCursorAmount() + "§7]");
        List<String> loreList = new ArrayList<>();
        loreList.add("§r自動でクッキーをクリックしてくれるカーソル");
        loreList.add("§a必要なクッキーの枚数§7: §d" + (users.get(getIndexOfUser(player)).getCursorAmount() + 1) * 2);
        cursorMeta.setLore(loreList);
        cursor.setItemMeta(cursorMeta);
        gui.setItem(45, cursor);

        player.openInventory(gui);
    }

    public static void setSlot(Inventory inventory, int slotNumber, Material itemType, int amount, int data, String name, String lore) {
        ItemStack item = new ItemStack(itemType);
        item.setAmount(amount);
        ItemMeta itemMeta = item.getItemMeta();
        item.setDurability((short) data);
        if (name != null) {
            itemMeta.setDisplayName(name);
        }
        if (lore != null) {
            List<String> loreList;
            loreList = Arrays.asList(lore.split("\n"));
            itemMeta.setLore(loreList);
        }
        item.setItemMeta(itemMeta);
        inventory.setItem(slotNumber, item);
    }
}