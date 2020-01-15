//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.mchim.Lottery.Thread;

import com.mchim.Lottery.LMMain;
import com.mchim.Lottery.Config.Settings;
import com.mchim.Lottery.Data.LotteryData;
import com.mchim.Lottery.Data.LotteryType;
import com.mchim.Lottery.Data.WareData;
import com.mchim.Lottery.Listeners.OnPlayerClickInv;
import com.mchim.Lottery.Utils.ItemMaker;
import com.mchim.Lottery.Utils.Money;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class LotteryThread extends Thread {
    public Inventory inv;
    public Player p;
    public ItemStack item;
    public LotteryType lt;
    public int slot;
    public int sleep;
    public int count = 26;
    public ItemStack zhizhen;

    public LotteryThread(Inventory inv, Player p, ItemStack item, LotteryType lt, int slot, int sleep) {
        this.zhizhen = ItemMaker.create(Material.REDSTONE, (short)0, "§a幸运指针", new String[0]);
        this.inv = inv;
        this.p = p;
        this.item = item;
        this.lt = lt;
        this.slot = slot;
        this.sleep = sleep;
    }

    public void run() {
        int time = LMMain.rd.nextInt(2) + 2;

        int money;
        for(int j = 0; j < time; ++j) {
            if (j < time - 1) {
                this.inv.removeItem(new ItemStack[]{this.zhizhen});
                this.inv.setItem(27, this.zhizhen);

                for(money = 0; money < 18; ++money) {
                    try {
                        Thread.sleep((long)(this.sleep + LMMain.rd.nextInt(Settings.I.animationRandomSleep)));
                    } catch (InterruptedException var11) {
                        var11.printStackTrace();
                    }

                    this.p.playSound(this.p.getLocation(), Sound.ITEM_PICKUP, 10.0F, 10.0F);
                    this.inv.removeItem(new ItemStack[]{this.zhizhen});
                    if (money < 9) {
                        ++this.count;
                        this.inv.setItem(this.count, this.zhizhen);
                        if (this.count == 35) {
                            this.count = 45;
                        }
                    } else {
                        --this.count;
                        this.inv.setItem(this.count, this.zhizhen);
                        if (this.count == 36) {
                            this.count = 26;
                            break;
                        }
                    }
                }
            } else {
                for(money = 0; money < 18; ++money) {
                    try {
                        Thread.sleep((long)(this.sleep + LMMain.rd.nextInt(Settings.I.animationRandomSleep)));
                    } catch (InterruptedException var12) {
                        var12.printStackTrace();
                    }

                    this.p.playSound(this.p.getLocation(), Sound.ITEM_PICKUP, 10.0F, 10.0F);
                    if (this.slot == money - 1) {
                        break;
                    }

                    this.inv.removeItem(new ItemStack[]{this.zhizhen});
                    if (money < 9) {
                        ++this.count;
                        this.inv.setItem(this.count, this.zhizhen);
                        if (this.count == 35) {
                            this.count = 45;
                        }
                    } else {
                        --this.count;
                        this.inv.setItem(this.count, this.zhizhen);
                    }
                }
            }
        }

        this.p.playSound(this.p.getLocation(), Sound.LEVEL_UP, 10.0F, 10.0F);
        double pMoney;
        String type;
        if (this.lt == LotteryType.Money) {
            type = "金币";
            money = Settings.I.PerMoneyLottery;
            pMoney = Money.getMoney(this.p);
        } else {
            type = "点券";
            money = Settings.I.perCouponLottery;
            pMoney = LMMain.playerPoints.getAPI().look(p.getUniqueId());
        }

        String itemName;
        if (this.item.hasItemMeta()) {
            if (this.item.getItemMeta().hasDisplayName()) {
                itemName = this.item.getItemMeta().getDisplayName();
            } else {
                itemName = this.item.getType().toString();
            }
        } else {
            itemName = this.item.getType().toString();
        }

        this.p.sendMessage("§c[系统]§a恭喜你花费了§b" + money + "§a" + type + "§a在" + type + "抽奖机抽到了§e" + itemName + "§a数量x§e" + this.item.getAmount());
        this.p.sendMessage("§c[系统]§a你剩余" + type + "§a:§e§l" + pMoney);
        LotteryData ld;
        if (this.lt == LotteryType.Money) {
            ld = LMMain.moneyData;
        } else {
            ld = LMMain.couponData;
        }

        int chance = ((WareData)ld.getItemList().get(this.slot)).getChance();
        if (chance <= Settings.I.lowChanceBoardCast) {
            Bukkit.broadcastMessage("§c[系统]§a恭喜玩家§d" + this.p.getName() + "§a花费了§b" + money + "§a" + type + "§a在" + type + "抽奖机抽到了§e" + itemName + "§a数量x§e" + this.item.getAmount());
        }

        this.p.getInventory().addItem(new ItemStack[]{this.item.clone()});

        try {
            Thread.sleep(2000L);
        } catch (InterruptedException var10) {
            var10.printStackTrace();
        }

        this.inv.removeItem(new ItemStack[]{this.zhizhen});
        this.inv.setItem(27, this.zhizhen);
        OnPlayerClickInv.protectList.remove(this.p.getName());
    }
}
