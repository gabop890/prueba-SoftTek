/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softtek.prueba.controller;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Gabriel
 */
@ManagedBean(name = "gestionIngreso")
@ViewScoped
public class GestionIngresoBean implements Serializable{
    
    private String usuario;

    @PostConstruct
    public void init(){
        usuario = "";
    }

    public GestionIngresoBean() {
    }
    
    public String onChangeRol(){
        if (usuario != null) {
            if (usuario.equals("0")) {
                return "pages/gestionProveedores.xhtml";
            }else if (usuario.equals("1")) {
                return "pages/gestionVehiculos.xhtml";
            }
        }
        return "principal.xhtml";
    }
    
    /**
     * Get the value of usuario
     *
     * @return the value of usuario
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * Set the value of usuario
     *
     * @param usuario new value of usuario
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    
}
