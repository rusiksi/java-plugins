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

/**
 * Загрузчик классов
 *
 * @author sidorovru
 */
public class PluginClassLoader extends ClassLoader {

    /**
     * папка с плагинами
     */
    private final String pluginRootDirectory;

    public PluginClassLoader(String pluginRootDirectory) {
        this.pluginRootDirectory = pluginRootDirectory;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if (name.contains(File.separator)) // если мы получили название класса с указанием папки размещения (слешем)
        {
            // загружаем нашим способом
            String pluginNameFolder = name.substring(0, name.indexOf(File.separatorChar));
            String classfileName = name.substring(name.lastIndexOf('.') + 1);
            Path path = Paths.get(pluginRootDirectory, pluginNameFolder, classfileName + ".class");
            try {
                byte[] data = loadClassData(path);
                if (data != null) {
                    String className = name.substring(name.lastIndexOf(File.separatorChar) + 1);
                    Class<?> cl = defineClass(className, data, 0, data.length);
                    return cl;
                }
            } catch (IOException ex) {
                // ниже вернём исключение
            }
        }
        throw new ClassNotFoundException("Class '" + name + "' not found");
    }

    /**
     * Загрузка байт-кода скомпилированного класса
     *
     * @param path путь к файлу класса
     * @return байт-код класса
     * @throws IOException
     */
    private byte[] loadClassData(Path path) throws IOException {
        File file = path.toFile();

        long size = file.length();
        if (size < 1 || size > Integer.MAX_VALUE) {
            System.out.println("Too big class size");
            return null;
        }
        InputStream in = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        in.read(data);
        return data;
    }
}
