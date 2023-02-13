/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softtek.prueba.controller;

import com.softtek.prueba.controller.exceptions.NonexistentEntityException;
import com.softtek.prueba.controller.exceptions.RollbackFailureException;
import com.softtek.prueba.model.Proveedor;
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

/**
 *
 * @author Gabriel
 */
@ManagedBean(name = "gestionProveedores")
@ViewScoped
public class GestionProveedoresBean implements Serializable {

    ProveedorJpaController controller;
    private Proveedor proveedor;
    private List<Proveedor> proveedoresSeleccionados;
    private List<Proveedor> proveedores;

    @PostConstruct
    public void init() {
        controller = new ProveedorJpaController();
        proveedor = new Proveedor();
        proveedoresSeleccionados = new ArrayList<>();
    }

    public List<Proveedor> getAllProveedores() {
        controller = new ProveedorJpaController();
        return controller.findProveedorEntities();
    }

    public void openNew() {
        proveedor = new Proveedor();
    }

    public void guardarProveedor() {
        try {
            Proveedor p = new Proveedor();
            p = controller.findProveedor(proveedor.getNumiden());
            if (p != null) {
                controller.edit(proveedor);
            } else {
                controller.create(proveedor);
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Vehiculo Added"));
            PrimeFaces.current().executeScript("PF('manageProductDialog').hide()");
            PrimeFaces.current().ajax().update("form:messages", "form:dt-products");
        } catch (RollbackFailureException ex) {
            Logger.getLogger(GestionProveedoresBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(GestionProveedoresBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getDeleteButtonMessage() {
        if (hasSelectedProducts()) {
            int size = proveedoresSeleccionados.size();
            return size > 1 ? size + " products selected" : "1 product selected";
        }

        return "Delete";
    }

    public boolean hasSelectedProducts() {
        return proveedoresSeleccionados != null && !proveedoresSeleccionados.isEmpty();
    }

    public void deleteSelectedProducts() {
        proveedores.removeAll(proveedoresSeleccionados);
        proveedoresSeleccionados = null;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Products Removed"));
        PrimeFaces.current().ajax().update("form:messages", "form:dt-products");
        PrimeFaces.current().executeScript("PF('dtProducts').clearFilters()");
    }

    public void deleteProduct() {
        try {
            controller.destroy(proveedor.getNumiden());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Product Removed"));
            PrimeFaces.current().ajax().update("form:messages", "form:dt-products");
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(GestionProveedoresBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(GestionProveedoresBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(GestionProveedoresBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public List<Proveedor> getProveedores() {
        return proveedoresSeleccionados;
    }

    public void setProveedores(List<Proveedor> proveedores) {
        this.proveedoresSeleccionados = proveedores;
    }

    public List<Proveedor> getProveedoresSeleccionados() {
        return proveedoresSeleccionados;
    }

    public void setProveedoresSeleccionados(List<Proveedor> proveedoresSeleccionados) {
        this.proveedoresSeleccionados = proveedoresSeleccionados;
    }
}
