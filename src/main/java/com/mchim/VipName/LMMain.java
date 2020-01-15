

package com.mchim.VipName;


import com.mchim.VipName.Commands.OnCommands;
import com.mchim.VipName.Config.ConfigurationLoader;
import com.mchim.VipName.Config.Settings;
import com.mchim.VipName.Listeners.OnPlayerJoin;
import com.mchim.VipName.PlaceHolder.NameHolder;
import org.black_ixx.playerpoints.PlayerPoints;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class LMMain extends JavaPlugin {
    public static LMMain instance;
    public static PlayerPoints playerPoints;
    public LMMain() {
    }

    private void hookPlayerPoints() {
        Plugin plugin = this.getServer().getPluginManager().getPlugin("PlayerPoints");
        playerPoints = PlayerPoints.class.cast(plugin);
        System.out.println("[Lottery]经济插件挂钩完成");
    }

    public void onEnable() {
        instance = this;
        this.EnableListener();
        ConfigurationLoader.loadYamlConfiguration(this, Settings.class, true);
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new NameHolder(this, "vipname");
        }
        hookPlayerPoints();

    }

    public void onDisable() {
    }

    private boolean EnableListener() {
        boolean EnableStatus = false;

        try {
            this.getCommand("VipName").setExecutor(new OnCommands());
            Bukkit.getPluginManager().registerEvents(new OnPlayerJoin(), this);
            EnableStatus = true;
        } catch (Exception var3) {
        }

        return EnableStatus;
    }
}
