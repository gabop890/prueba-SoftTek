/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softtek.prueba.controller;

import com.softtek.prueba.controller.exceptions.IllegalOrphanException;
import com.softtek.prueba.controller.exceptions.NonexistentEntityException;
import com.softtek.prueba.controller.exceptions.PreexistingEntityException;
import com.softtek.prueba.controller.exceptions.RollbackFailureException;
import com.softtek.prueba.model.Proveedor;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.softtek.prueba.model.Vehiculo;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.transaction.UserTransaction;

/**
 *
 * @author Gabriel
 */
public class ProveedorJpaController implements Serializable {

    public ProveedorJpaController() {
    }

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("com.softTek_prueba_war_1.0-SNAPSHOTPU");
    private EntityManager em = emf.createEntityManager();
    private EntityTransaction utx = em.getTransaction();

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Proveedor proveedor) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (proveedor.getVehiculoList() == null) {
            proveedor.setVehiculoList(new ArrayList<Vehiculo>());
        }
        try {
            em = getEntityManager();
            utx = em.getTransaction();
            utx.begin();
//            em = getEntityManager();
            List<Vehiculo> attachedVehiculoList = new ArrayList<Vehiculo>();
            for (Vehiculo vehiculoListVehiculoToAttach : proveedor.getVehiculoList()) {
                vehiculoListVehiculoToAttach = em.getReference(vehiculoListVehiculoToAttach.getClass(), vehiculoListVehiculoToAttach.getPlaca());
                attachedVehiculoList.add(vehiculoListVehiculoToAttach);
            }
            proveedor.setVehiculoList(attachedVehiculoList);
            em.persist(proveedor);
            for (Vehiculo vehiculoListVehiculo : proveedor.getVehiculoList()) {
                Proveedor oldProveedorOfVehiculoListVehiculo = vehiculoListVehiculo.getProveedor();
                vehiculoListVehiculo.setProveedor(proveedor);
                vehiculoListVehiculo = em.merge(vehiculoListVehiculo);
                if (oldProveedorOfVehiculoListVehiculo != null) {
                    oldProveedorOfVehiculoListVehiculo.getVehiculoList().remove(vehiculoListVehiculo);
                    oldProveedorOfVehiculoListVehiculo = em.merge(oldProveedorOfVehiculoListVehiculo);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findProveedor(proveedor.getNumiden()) != null) {
                throw new PreexistingEntityException("Proveedor " + proveedor + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Proveedor proveedor) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        try {
            em = getEntityManager();
            utx = em.getTransaction();
            utx.begin();
            Proveedor persistentProveedor = em.find(Proveedor.class, proveedor.getNumiden());
            List<Vehiculo> vehiculoListOld = persistentProveedor.getVehiculoList();
            List<Vehiculo> vehiculoListNew = proveedor.getVehiculoList();
            List<String> illegalOrphanMessages = null;
            for (Vehiculo vehiculoListOldVehiculo : vehiculoListOld) {
                if (!vehiculoListNew.contains(vehiculoListOldVehiculo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Vehiculo " + vehiculoListOldVehiculo + " since its proveedor field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Vehiculo> attachedVehiculoListNew = new ArrayList<Vehiculo>();
            for (Vehiculo vehiculoListNewVehiculoToAttach : vehiculoListNew) {
                vehiculoListNewVehiculoToAttach = em.getReference(vehiculoListNewVehiculoToAttach.getClass(), vehiculoListNewVehiculoToAttach.getPlaca());
                attachedVehiculoListNew.add(vehiculoListNewVehiculoToAttach);
            }
            vehiculoListNew = attachedVehiculoListNew;
            proveedor.setVehiculoList(vehiculoListNew);
            proveedor = em.merge(proveedor);
            for (Vehiculo vehiculoListNewVehiculo : vehiculoListNew) {
                if (!vehiculoListOld.contains(vehiculoListNewVehiculo)) {
                    Proveedor oldProveedorOfVehiculoListNewVehiculo = vehiculoListNewVehiculo.getProveedor();
                    vehiculoListNewVehiculo.setProveedor(proveedor);
                    vehiculoListNewVehiculo = em.merge(vehiculoListNewVehiculo);
                    if (oldProveedorOfVehiculoListNewVehiculo != null && !oldProveedorOfVehiculoListNewVehiculo.equals(proveedor)) {
                        oldProveedorOfVehiculoListNewVehiculo.getVehiculoList().remove(vehiculoListNewVehiculo);
                        oldProveedorOfVehiculoListNewVehiculo = em.merge(oldProveedorOfVehiculoListNewVehiculo);
                    }
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = proveedor.getNumiden();
                if (findProveedor(id) == null) {
                    throw new NonexistentEntityException("The proveedor with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        try {
            em = getEntityManager();
            utx = em.getTransaction();
            utx.begin();
            Proveedor proveedor;
            try {
                proveedor = em.getReference(Proveedor.class, id);
                proveedor.getNumiden();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The proveedor with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Vehiculo> vehiculoListOrphanCheck = proveedor.getVehiculoList();
            for (Vehiculo vehiculoListOrphanCheckVehiculo : vehiculoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Proveedor (" + proveedor + ") cannot be destroyed since the Vehiculo " + vehiculoListOrphanCheckVehiculo + " in its vehiculoList field has a non-nullable proveedor field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(proveedor);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Proveedor> findProveedorEntities() {
        return findProveedorEntities(true, -1, -1);
    }

    public List<Proveedor> findProveedorEntities(int maxResults, int firstResult) {
        return findProveedorEntities(false, maxResults, firstResult);
    }

    private List<Proveedor> findProveedorEntities(boolean all, int maxResults, int firstResult) {
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Proveedor.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Proveedor findProveedor(Long id) {
        em = getEntityManager();
        try {
            return em.find(Proveedor.class, id);
        } finally {
            em.close();
        }
    }

    public int getProveedorCount() {
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Proveedor> rt = cq.from(Proveedor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
