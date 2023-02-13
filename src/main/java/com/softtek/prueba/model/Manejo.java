/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softtek.prueba.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Gabriel
 */
@Entity
@Table(name = "manejo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Manejo.findAll", query = "SELECT m FROM Manejo m")
    , @NamedQuery(name = "Manejo.findByPlaca", query = "SELECT m FROM Manejo m WHERE m.manejoPK.placa = :placa")
    , @NamedQuery(name = "Manejo.findByConductor", query = "SELECT m FROM Manejo m WHERE m.manejoPK.conductor = :conductor")
    , @NamedQuery(name = "Manejo.findByFechaini", query = "SELECT m FROM Manejo m WHERE m.fechaini = :fechaini")
    , @NamedQuery(name = "Manejo.findByFechaFin", query = "SELECT m FROM Manejo m WHERE m.fechaFin = :fechaFin")})
public class Manejo implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ManejoPK manejoPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fechaini")
    @Temporal(TemporalType.DATE)
    private Date fechaini;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fechafin")
    @Temporal(TemporalType.DATE)
    private Date fechaFin;
    @JoinColumn(name = "conductor", referencedColumnName = "numiden", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Conductor conductor1;
    @JoinColumn(name = "placa", referencedColumnName = "placa", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Vehiculo vehiculo;

    public Manejo() {
    }

    public Manejo(ManejoPK manejoPK) {
        this.manejoPK = manejoPK;
    }

    public Manejo(ManejoPK manejoPK, Date fechaini, Date fechaFin) {
        this.manejoPK = manejoPK;
        this.fechaini = fechaini;
        this.fechaFin = fechaFin;
    }

    public Manejo(String placa, int conductor) {
        this.manejoPK = new ManejoPK(placa, conductor);
    }

    public ManejoPK getManejoPK() {
        return manejoPK;
    }

    public void setManejoPK(ManejoPK manejoPK) {
        this.manejoPK = manejoPK;
    }

    public Date getFechaini() {
        return fechaini;
    }

    public void setFechaini(Date fechaini) {
        this.fechaini = fechaini;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Conductor getConductor1() {
        return conductor1;
    }

    public void setConductor1(Conductor conductor1) {
        this.conductor1 = conductor1;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (manejoPK != null ? manejoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Manejo)) {
            return false;
        }
        Manejo other = (Manejo) object;
        if ((this.manejoPK == null && other.manejoPK != null) || (this.manejoPK != null && !this.manejoPK.equals(other.manejoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.softtek.prueba.model.Manejo[ manejoPK=" + manejoPK + " ]";
    }
    
}
