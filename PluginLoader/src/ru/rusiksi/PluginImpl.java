/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.rusiksi;

import ru.rusiksi.base.Plugin;

/**
 * Класс плагина который имеет имя идентичное имени в плагине, для проверки
 * загрузки.
 *
 * @author sidorovru
 */
public class PluginImpl implements Plugin {

    @Override
    public void doUsefull() {
        System.out.println("doUsefull FROM PLUGIN LOADER! ");
    }
}
