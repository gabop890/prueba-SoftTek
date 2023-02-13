/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softtek.prueba.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

/**
 *
 * @author Gabriel
 */
@ManagedBean(name = "lenguaje")
@ViewScoped
public class LenguajeBean implements Serializable {

    private String codigoIdioma;
    private static Map<String, Object> listIdiomas;

    @PostConstruct
    public void init() {
        codigoIdioma = "es";
        listIdiomas = new LinkedHashMap<String, Object>();
        Locale locale = new Locale("ES");
        listIdiomas.put("Español", locale);
        listIdiomas.put("Ingles", Locale.ENGLISH);
    }

    public void onChangeLenguaje(ValueChangeEvent changeEvent) {
        codigoIdioma = changeEvent.getNewValue().toString();

        for (Map.Entry<String, Object> entry : listIdiomas.entrySet()) {
            FacesContext.getCurrentInstance().getViewRoot().setLocale((Locale) entry.getValue());
        }
    }

    public String obtenerFrase(String string) {
        Locale l = new Locale(Locale.forLanguageTag(codigoIdioma).getLanguage());
        ResourceBundle bundle = ResourceBundle.getBundle("i18n/i18nMensaje", l);
        return bundle.getString("i18nPrincipal." + string);
    }

    public Map<String,String> obtenerLista(String string) {
        Locale l = new Locale(Locale.forLanguageTag(codigoIdioma).getLanguage());
        ResourceBundle bundle = ResourceBundle.getBundle("i18n/i18nMensaje", l);
        String temp = "";
        temp = bundle.getString("i18nPrincipal." + string);
        temp = temp.replaceAll("\"", "");
        String[] strings = temp.split(",");
        Map<String,String> map = new HashMap<>();
        for (int i = 0; i < strings.length; i++) {
            map.put(i+"", strings[i]);
        }
        return map;
    }

    /**
     * Get the value of listIdiomas
     *
     * @return the value of listIdiomas
     */
    public Map<String, Object> getListIdiomas() {
        return listIdiomas;
    }

    /**
     * Set the value of listIdiomas
     *
     * @param listIdiomas new value of listIdiomas
     */
    public void setListIdiomas(Map<String, Object> listIdiomas) {
        this.listIdiomas = listIdiomas;
    }

    /**
     * Get the value of codigoIdioma
     *
     * @return the value of codigoIdioma
     */
    public String getCodigoIdioma() {
        return codigoIdioma;
    }

    /**
     * Set the value of codigoIdioma
     *
     * @param codigoIdioma new value of codigoIdioma
     */
    public void setCodigoIdioma(String codigoIdioma) {
        this.codigoIdioma = codigoIdioma;
    }

}
