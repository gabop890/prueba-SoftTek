package com.softtek.prueba.controller;

import com.softtek.prueba.controller.exceptions.NonexistentEntityException;
import com.softtek.prueba.controller.exceptions.RollbackFailureException;
import com.softtek.prueba.model.Proveedor;
import com.softtek.prueba.model.Vehiculo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.PrimeFaces;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Gabriel
 */
@ManagedBean(name = "gestionVehiculos")
@ViewScoped
public class GestionVehiculosBean implements Serializable{
    
    private List<Vehiculo> vehiculos;
    private Vehiculo vehiculo;
    VehiculoJpaController vehiculoJpaController;
    private Proveedor proveedor;
    private String placa;
    private String marca;
    private Long numIden;
    private String estado;
    private ProveedorJpaController controller;
    
    @PostConstruct
    public  void init(){
        vehiculos = new ArrayList<>();
        vehiculo = new Vehiculo();
        vehiculoJpaController = new VehiculoJpaController();
        controller = new ProveedorJpaController();
    }
    
    public void getAllVehiculosByProveedor(){
        
    }
    
    public void guardarVehiculo(){
        proveedor = controller.findProveedor(numIden);
        guardar();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Product Added"));
            PrimeFaces.current().executeScript("PF('dlgaddvehiculo').hide()");
            PrimeFaces.current().ajax().update("formvehiculo:dtvehiculo");
    }
    
    public void guardar(){
        try {
            Vehiculo temp = new Vehiculo();
            temp = vehiculoJpaController.findVehiculo(vehiculo.getPlaca());
            if (temp != null) {
                vehiculoJpaController.edit(vehiculo);
            } else {
                if (proveedor != null) {
                    vehiculo.setProveedor(proveedor);
                    vehiculoJpaController.create(vehiculo);
                }
            }
            vehiculo = new Vehiculo();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Product Added"));
            PrimeFaces.current().executeScript("PF('dlgaddvehiculo').hide()");
            PrimeFaces.current().ajax().update("formvehiculo:dtvehiculo");
        } catch (RollbackFailureException ex) {
            Logger.getLogger(GestionVehiculosBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(GestionVehiculosBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void borrarVehiculo(){
        try {
            vehiculoJpaController.destroy(vehiculo.getPlaca());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Vehiculo removido"));
            PrimeFaces.current().ajax().update("form:messages", "formvehiculo:dtvehiculo");
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(GestionVehiculosBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(GestionVehiculosBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(GestionVehiculosBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void deleteProduct() {
        try {
            vehiculoJpaController.destroy(vehiculo.getPlaca());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Product Removed"));
            PrimeFaces.current().ajax().update("form:messages", "formvehiculo:dtvehiculo");
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(GestionProveedoresBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(GestionProveedoresBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(GestionProveedoresBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public List<Vehiculo> getAllVehiculos(){
        vehiculoJpaController = new VehiculoJpaController();
        return vehiculoJpaController.findVehiculoEntities();
    }
    
    public void openNew() {
        vehiculo = new Vehiculo();
    }

    public List<Vehiculo> getVehiculos() {
        vehiculos = vehiculoJpaController.findVehiculoEntities();
        List<Vehiculo> list = new ArrayList<>();
        for (Vehiculo vehiculo1 : vehiculos) {
            if (vehiculo1.getProveedor().equals(proveedor)) {
                list.add(vehiculo1);
            }
        }
        return list;
    }

    public void setVehiculos(List<Vehiculo> vehiculos) {
        this.vehiculos = vehiculos;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public Long getNumIden() {
        return numIden;
    }

    public void setNumIden(Long numIden) {
        this.numIden = numIden;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
