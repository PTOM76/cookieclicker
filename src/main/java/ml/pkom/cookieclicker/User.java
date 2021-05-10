package ml.pkom.cookieclicker;

import org.bukkit.entity.Player;

public class User {
    private Player player;
    private int cookies = 0;
    private int cursor = 0;

    public User(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }
    // クッキーの枚数
    public int getCookiesAmount() {
        return cookies;
    }

    public void setCookiesAmount(int i) {
        cookies = i;
    }

    public void addCookiesAmount(int i) {
        cookies = cookies + i;
    }

    public void removeCookiesAmount(int i) {
        cookies = cookies - i;
    }

    // カーソルの数
    public int getCursorAmount() {
        return cursor;
    }

    public void setCursorAmount(int i) {
        cursor = i;
    }

    public void addCursorAmount(int i) {
        cursor = cursor + i;
    }

    public void removeCursorAmount(int i) {
        cursor = cursor - i;
    }
    
    public void saveUser() {
        
    }
    
}
