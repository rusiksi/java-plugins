/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pluginloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import ru.rusiksi.base.Plugin;

/**
 * Менеджер плагинов. Позволяет загружать классы плагинов даже с одинаковым
 * наименованием.
 *
 * @author sidorovru
 */
public class PluginManager {

    /**
     * путь к плагинам
     */
    private final String pluginRootDirectory;
    /**
     * наш собственный кеш классов, необходим для возможности грузить классы
     * разных плагинов с одинаковым именем
     */
    private final HashMap<String, Class<?>> cache;
    /**
     * текущий загрузчик классов
     */
    private PluginClassLoader currentLoader;

    public PluginManager(String pluginRootDirectory) {
        this.pluginRootDirectory = pluginRootDirectory;
        cache = new HashMap<>();
        currentLoader = new PluginClassLoader(pluginRootDirectory);
    }

    /**
     * Загрузка плагина с указанием пути к плагину
     *
     * @param pluginName имя плагина (папки где они находятся)
     * @param pluginClassName имя класса плагина
     * @return загруженный объект плагина
     */
    public Plugin load(String pluginName, String pluginClassName) {
        String fullName = pluginClassName;
        if (pluginName != null && !pluginName.trim().isEmpty()) {
            fullName = pluginName + File.separatorChar + fullName;
        }
        try {
            Class cl = null;
            if (cache.containsKey(fullName)) {
                cl = cache.get(fullName);
            } else {
                cl = loadClass(fullName);
                cache.put(fullName, cl);
            }
            if (cl != null) {
                Object obj = cl.newInstance();
                if (obj instanceof Plugin) {
                    return (Plugin) obj;
                }
            }
        } catch (ClassNotFoundException ex) {
            System.out.println("Class not found: " + ex.getLocalizedMessage());
        } catch (InstantiationException | IllegalAccessException ex) {
            System.out.println("Loading error: " + ex.getLocalizedMessage());
        }
        return null;
    }

    /**
     * Загрузка класса через текущий загрузчик. Если новый загружаемый класс
     * имеет имя класса которое уже было ранее загружено, будет сгенерировано
     * исключение, которое перехватывается методом, и создаётся следующий
     * загрузчик.
     *
     * @param fullName полное имя класса плагина (включая название папки)
     * @return загруженный класс
     * @throws ClassNotFoundException исключение если класс не найден
     */
    private Class<?> loadClass(String fullName) throws ClassNotFoundException {
        try {
            Class cl = currentLoader.loadClass(fullName);
            return cl;
        } catch (LinkageError e) // ошибка загрузки класса (дублирование названия класса)
        {
            currentLoader = new PluginClassLoader(pluginRootDirectory);
            return currentLoader.loadClass(fullName);
        }
    }

}
