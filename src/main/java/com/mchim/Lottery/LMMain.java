//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.mchim.Lottery;

import com.mchim.Lottery.Commands.AdminCommands;
import com.mchim.Lottery.Commands.LotteryCommands;
import com.mchim.Lottery.Config.ConfigurationLoader;
import com.mchim.Lottery.Config.Settings;
import com.mchim.Lottery.Data.LotteryData;
import com.mchim.Lottery.Listeners.OnPlayerClickInv;
import com.mchim.Lottery.Utils.File.FileUtil;
import java.io.IOException;
import java.util.Random;
import net.milkbowl.vault.economy.Economy;
import org.black_ixx.playerpoints.PlayerPoints;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class LMMain extends JavaPlugin {
    public static LMMain instance;
    public static Economy vault;
    public static Random rd = new Random();
    public static LotteryData moneyData;
    public static LotteryData couponData;
    public static PlayerPoints playerPoints;

    public LMMain() {
    }

    public void onEnable() {
        instance = this;
        ConfigurationLoader.loadYamlConfiguration(this, Settings.class, true);

        try {
            FileUtil.getFile();
        } catch (IOException var2) {
            var2.printStackTrace();
        }
        hookPlayerPoints();
        moneyData.setPerMoney(Settings.I.PerMoneyLottery);
        couponData.setPerMoney(Settings.I.perCouponLottery);
        this.EnableListener();
        this.setupEconomy();
    }


    private void hookPlayerPoints() {
        Plugin plugin = this.getServer().getPluginManager().getPlugin("PlayerPoints");
        playerPoints = PlayerPoints.class.cast(plugin);
        System.out.println("[Lottery]经济插件挂钩完成");
    }

    public void onDisable() {
        try {
            FileUtil.saveFile();
        } catch (IOException var2) {
            var2.printStackTrace();
        }

    }

    private boolean setupEconomy() {
        RegisteredServiceProvider economyProvider = Bukkit.getServicesManager().getRegistration(Economy.class);
        if (economyProvider != null) {
            vault = (Economy)economyProvider.getProvider();
        }

        return vault != null;
    }

    private boolean DisableListener() {
        boolean DisableStatus = false;

        try {
            HandlerList.unregisterAll(this);
            DisableStatus = true;
        } catch (Exception var3) {
        }

        return DisableStatus;
    }

    private boolean EnableListener() {
        boolean EnableStatus = false;

        try {
            this.getCommand("cj").setExecutor(new LotteryCommands());
            this.getCommand("lt").setExecutor(new AdminCommands());
            Bukkit.getPluginManager().registerEvents(new OnPlayerClickInv(), this);
            EnableStatus = true;
        } catch (Exception var3) {
        }

        return EnableStatus;
    }
}
