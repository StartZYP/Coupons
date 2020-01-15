//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.mchim.Lottery.Listeners;


import com.mchim.Lottery.LMMain;
import com.mchim.Lottery.Config.Settings;
import com.mchim.Lottery.Data.LotteryData;
import com.mchim.Lottery.Data.LotteryType;
import com.mchim.Lottery.Data.WareData;
import com.mchim.Lottery.GUI.GUIManager;
import com.mchim.Lottery.Thread.LotteryThread;
import com.mchim.Lottery.Utils.Money;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class OnPlayerClickInv implements Listener {
    static int taskID;
    public static List<String> protectList = new ArrayList();

    public OnPlayerClickInv() {
    }

    @EventHandler
    public void click(InventoryClickEvent e) {
        Inventory inv = e.getInventory();
        Player p = (Player)e.getWhoClicked();
        int slot;
        if (inv.getTitle().equals("§c请选择抽奖类型")) {
            e.setCancelled(true);
            slot = e.getRawSlot();
            if (slot < 0 || slot > 8 || slot == 4) {
                return;
            }

            if (slot < 4) {
                GUIManager.createLotteryGUI(p, LotteryType.Money);
            } else {
                GUIManager.createLotteryGUI(p, LotteryType.Coupon);
            }
        }

        if (inv.getTitle().equals("§b§l金币抽奖机") || inv.getTitle().equals("§b§l点券抽奖机")) {
            e.setCancelled(true);
            if (!GUIManager.ldMap.containsKey(p.getName())) {
                return;
            }

            slot = e.getRawSlot();
            if (slot < 0 || slot > 9) {
                p.kickPlayer("§c[系统]§a严禁点击此处!!");
                return;
            }

            if (slot != 0) {
                if (protectList.contains(p.getName())) {
                    p.sendMessage("§c[系统]§a抽奖正在进行中请稍等...");
                    return;
                }

                if (isInvFull(p)) {
                    p.sendMessage("§c[系统]§a你的背包已满!");
                    return;
                }

                LotteryData ld = (LotteryData)GUIManager.ldMap.get(p.getName());
                int money = ld.getPerMoney();
                boolean haveMoney = false;
                String moneyText;
                if (ld.getLt() == LotteryType.Money) {
                    if (Money.haveMoney(p, (double)money)) {
                        haveMoney = true;
                    }

                    moneyText = "金币";
                } else {
                    if (LMMain.playerPoints.getAPI().look(p.getUniqueId()) >= (double)money) {
                        haveMoney = true;
                    }

                    moneyText = "点券";
                }

                if (!haveMoney) {
                    p.sendMessage("§c[系统]§a你的" + moneyText + "不足!");
                    return;
                }

                startLottery(p, inv, ld.getLt());
                if (ld.getLt() == LotteryType.Money) {
                    Money.takeMoney(p, (double)money);
                } else {
                    LMMain.playerPoints.getAPI().set(p.getUniqueId(),LMMain.playerPoints.getAPI().look(p.getUniqueId())-money);
                }
            }
        }

    }

    public static void startLottery(Player p, Inventory inv, LotteryType lt) {
        LotteryData ld;
        if (lt == LotteryType.Money) {
            ld = LMMain.moneyData;
        } else {
            ld = LMMain.couponData;
        }

        if (ld.getItemList().size() < 18) {
            p.sendMessage("§c[系统]§a抽奖机奖品尚未完善,请联系管理员");
        } else {
            int slot = -1;
            Map<ItemStack, Float> itemMap = new HashMap();

            int i;
            for(i = 0; i < ld.getItemList().size(); ++i) {
                itemMap.put(((WareData)ld.getItemList().get(i)).getItem().clone(), (float)((double)((WareData)ld.getItemList().get(i)).getChance() / 1000.0D));
            }

            while(probability(itemMap) == null) {
            }

            ItemStack ware = probability(itemMap);

            for(i = 0; i < ld.getItemList().size(); ++i) {
                if (((WareData)ld.getItemList().get(i)).getItem().equals(ware)) {
                    slot = i;
                }
            }

            if (slot == -1) {
                p.sendMessage("§c[系统]§a内部错误请联系管理员");
            } else {
                LotteryThread lth = new LotteryThread(inv, p, ware, lt, slot, Settings.I.animationBaseSleep);
                protectList.add(p.getName());
                lth.start();
            }
        }
    }

    public static boolean isInvFull(Player p) {
        Inventory inv = p.getInventory();

        for(int i = 0; i < inv.getSize(); ++i) {
            if (inv.getItem(i) == null) {
                return false;
            }
        }

        return true;
    }

    private static ItemStack probability(Map<ItemStack, Float> map) {
        Float total = 0.0F;
        Map<Float, ItemStack> tempMap = new LinkedHashMap();
        Iterator it = map.entrySet().iterator();

        while(it.hasNext()) {
            Entry<ItemStack, Float> entry = (Entry)it.next();
            total = total + (Float)entry.getValue();
            tempMap.put(total, (ItemStack)entry.getKey());
        }

        float index = (new Random()).nextFloat() * total;
        Iterator tempIt = tempMap.entrySet().iterator();

        while(tempIt.hasNext()) {
            Entry<Float, ItemStack> next = (Entry)tempIt.next();
            if (index < (Float)next.getKey()) {
                return (ItemStack)next.getValue();
            }
        }

        return null;
    }
}
