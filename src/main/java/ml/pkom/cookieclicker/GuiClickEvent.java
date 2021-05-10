package ml.pkom.cookieclicker;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class GuiClickEvent implements Listener {

    @EventHandler
    public void GuiClick(InventoryClickEvent event) {
        if (event.getInventory().getName().contains("§6§lCookieClicker")) {
            event.setCancelled(true);
            if (event.getInventory().getItem(event.getSlot()) == null) {
                return;
            }
            if (event.getInventory().getItem(event.getSlot()).getItemMeta().getDisplayName() == Plugin.cookie.getItemMeta().getDisplayName()) {
                Plugin.users.get(Plugin.getIndexOfUser((Player) event.getWhoClicked())).addCookiesAmount(1);
                Plugin.playCookieClicker((Player) event.getWhoClicked());
                return;
            }
            if (event.getInventory().getItem(event.getSlot()).getItemMeta().getDisplayName() == Plugin.cursor.getItemMeta().getDisplayName()) {
                int needCookies = (Plugin.users.get(Plugin.getIndexOfUser((Player) event.getWhoClicked())).getCursorAmount() + 1) * 2;
                if (Plugin.users.get(Plugin.getIndexOfUser((Player) event.getWhoClicked())).getCookiesAmount() >= needCookies) {
                    Plugin.users.get(Plugin.getIndexOfUser((Player) event.getWhoClicked())).removeCookiesAmount(needCookies);
                    Plugin.users.get(Plugin.getIndexOfUser((Player) event.getWhoClicked())).addCursorAmount(1);
                    Plugin.playCookieClicker((Player) event.getWhoClicked());
                }else{
                    event.getWhoClicked().sendMessage("§cクッキーが足りません。");
                }
                return;
            }
        }
        return;
    }
    @EventHandler
    public void GuiClose(InventoryCloseEvent event) {
        if (event.getInventory().getName().contains("§6§lCookieClicker")) {
            Plugin.playingUsers.remove((Player) event.getPlayer());
            return;
        }
        return;
    }
}
