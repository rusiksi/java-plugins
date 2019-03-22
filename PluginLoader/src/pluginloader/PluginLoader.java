/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pluginloader;

import java.util.ArrayList;
import ru.rusiksi.base.Plugin;

/**
 *
 * @author sidorovru
 */
public class PluginLoader {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Plugin loader implementation.");

        ArrayList<Plugin> plugins = new ArrayList<>();
        PluginManager man = new PluginManager("plugins");

        plugins.add(man.load("", "ru.rusiksi.PluginImpl"));             // загрузка внутреннего класса
        plugins.add(man.load("myplugin", "ru.rusiksi.PluginImpl"));     // загрузка плагина из папки myplugin
        plugins.add(man.load("myplugin2", "ru.rusiksi.PluginImpl"));    // загрузка плагина из папки myplugin2

        for (Plugin plugin : plugins) {
            plugin.doUsefull();
        }
    }

}
