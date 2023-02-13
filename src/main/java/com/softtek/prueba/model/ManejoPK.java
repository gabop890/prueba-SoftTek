/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softtek.prueba.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Gabriel
 */
@Embeddable
public class ManejoPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "placa")
    private String placa;
    @Basic(optional = false)
    @NotNull
    @Column(name = "conductor")
    private int conductor;

    public ManejoPK() {
    }

    public ManejoPK(String placa, int conductor) {
        this.placa = placa;
        this.conductor = conductor;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public int getConductor() {
        return conductor;
    }

    public void setConductor(int conductor) {
        this.conductor = conductor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (placa != null ? placa.hashCode() : 0);
        hash += (int) conductor;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ManejoPK)) {
            return false;
        }
        ManejoPK other = (ManejoPK) object;
        if ((this.placa == null && other.placa != null) || (this.placa != null && !this.placa.equals(other.placa))) {
            return false;
        }
        if (this.conductor != other.conductor) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.softtek.prueba.model.ManejoPK[ placa=" + placa + ", conductor=" + conductor + " ]";
    }
    
}
