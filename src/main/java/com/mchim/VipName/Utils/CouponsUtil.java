//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.mchim.VipName.Utils;

import com.mchim.VipName.LMMain;
import org.bukkit.entity.Player;

public class CouponsUtil {
    public CouponsUtil() {
    }

    public static double getCoupon(Player p) {
        return LMMain.playerPoints.getAPI().look(p.getUniqueId());
    }

    public static void setCoupon(Player p, double money) {
        LMMain.playerPoints.getAPI().set(p.getUniqueId(),(int)money);
    }
}
