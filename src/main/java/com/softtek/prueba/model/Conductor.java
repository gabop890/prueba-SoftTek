/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softtek.prueba.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Gabriel
 */
@Entity
@Table(name = "conductor")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Conductor.findAll", query = "SELECT c FROM Conductor c")
    , @NamedQuery(name = "Conductor.findByNumiden", query = "SELECT c FROM Conductor c WHERE c.numiden = :numiden")
    , @NamedQuery(name = "Conductor.findByNombre", query = "SELECT c FROM Conductor c WHERE c.nombre = :nombre")})
public class Conductor implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "numiden")
    private Integer numiden;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "conductor1")
    private List<Manejo> manejoList;

    public Conductor() {
    }

    public Conductor(Integer numiden) {
        this.numiden = numiden;
    }

    public Conductor(Integer numiden, String nombre) {
        this.numiden = numiden;
        this.nombre = nombre;
    }

    public Integer getNumiden() {
        return numiden;
    }

    public void setNumiden(Integer numiden) {
        this.numiden = numiden;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlTransient
    public List<Manejo> getManejoList() {
        return manejoList;
    }

    public void setManejoList(List<Manejo> manejoList) {
        this.manejoList = manejoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (numiden != null ? numiden.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Conductor)) {
            return false;
        }
        Conductor other = (Conductor) object;
        if ((this.numiden == null && other.numiden != null) || (this.numiden != null && !this.numiden.equals(other.numiden))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.softtek.prueba.model.Conductor[ numiden=" + numiden + " ]";
    }
    
}
