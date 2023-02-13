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
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.softtek.prueba.model.Proveedor;
import com.softtek.prueba.model.Manejo;
import com.softtek.prueba.model.Vehiculo;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.transaction.UserTransaction;

/**
 *
 * @author Gabriel
 */
public class VehiculoJpaController implements Serializable {

    public VehiculoJpaController() {
    }
    
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("com.softTek_prueba_war_1.0-SNAPSHOTPU");
    private EntityManager em = emf.createEntityManager();
    private EntityTransaction utx = em.getTransaction();

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Vehiculo vehiculo) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (vehiculo.getManejoList() == null) {
            vehiculo.setManejoList(new ArrayList<Manejo>());
        }
        try {
            em = getEntityManager();
            utx = em.getTransaction();
            utx.begin();
            Proveedor proveedor = vehiculo.getProveedor();
            if (proveedor != null) {
                proveedor = em.getReference(proveedor.getClass(), proveedor.getNumiden());
                vehiculo.setProveedor(proveedor);
            }
            List<Manejo> attachedManejoList = new ArrayList<Manejo>();
            for (Manejo manejoListManejoToAttach : vehiculo.getManejoList()) {
                manejoListManejoToAttach = em.getReference(manejoListManejoToAttach.getClass(), manejoListManejoToAttach.getManejoPK());
                attachedManejoList.add(manejoListManejoToAttach);
            }
            vehiculo.setManejoList(attachedManejoList);
            em.persist(vehiculo);
            if (proveedor != null) {
                proveedor.getVehiculoList().add(vehiculo);
                proveedor = em.merge(proveedor);
            }
            for (Manejo manejoListManejo : vehiculo.getManejoList()) {
                Vehiculo oldVehiculoOfManejoListManejo = manejoListManejo.getVehiculo();
                manejoListManejo.setVehiculo(vehiculo);
                manejoListManejo = em.merge(manejoListManejo);
                if (oldVehiculoOfManejoListManejo != null) {
                    oldVehiculoOfManejoListManejo.getManejoList().remove(manejoListManejo);
                    oldVehiculoOfManejoListManejo = em.merge(oldVehiculoOfManejoListManejo);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findVehiculo(vehiculo.getPlaca()) != null) {
                throw new PreexistingEntityException("Vehiculo " + vehiculo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Vehiculo vehiculo) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        try {
            em = getEntityManager();
            utx = em.getTransaction();
            utx.begin();
            Vehiculo persistentVehiculo = em.find(Vehiculo.class, vehiculo.getPlaca());
            Proveedor proveedorOld = persistentVehiculo.getProveedor();
            Proveedor proveedorNew = vehiculo.getProveedor();
            List<Manejo> manejoListOld = persistentVehiculo.getManejoList();
            List<Manejo> manejoListNew = vehiculo.getManejoList();
            List<String> illegalOrphanMessages = null;
            for (Manejo manejoListOldManejo : manejoListOld) {
                if (!manejoListNew.contains(manejoListOldManejo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Manejo " + manejoListOldManejo + " since its vehiculo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (proveedorNew != null) {
                proveedorNew = em.getReference(proveedorNew.getClass(), proveedorNew.getNumiden());
                vehiculo.setProveedor(proveedorNew);
            }
            List<Manejo> attachedManejoListNew = new ArrayList<Manejo>();
            for (Manejo manejoListNewManejoToAttach : manejoListNew) {
                manejoListNewManejoToAttach = em.getReference(manejoListNewManejoToAttach.getClass(), manejoListNewManejoToAttach.getManejoPK());
                attachedManejoListNew.add(manejoListNewManejoToAttach);
            }
            manejoListNew = attachedManejoListNew;
            vehiculo.setManejoList(manejoListNew);
            vehiculo = em.merge(vehiculo);
            if (proveedorOld != null && !proveedorOld.equals(proveedorNew)) {
                proveedorOld.getVehiculoList().remove(vehiculo);
                proveedorOld = em.merge(proveedorOld);
            }
            if (proveedorNew != null && !proveedorNew.equals(proveedorOld)) {
                proveedorNew.getVehiculoList().add(vehiculo);
                proveedorNew = em.merge(proveedorNew);
            }
            for (Manejo manejoListNewManejo : manejoListNew) {
                if (!manejoListOld.contains(manejoListNewManejo)) {
                    Vehiculo oldVehiculoOfManejoListNewManejo = manejoListNewManejo.getVehiculo();
                    manejoListNewManejo.setVehiculo(vehiculo);
                    manejoListNewManejo = em.merge(manejoListNewManejo);
                    if (oldVehiculoOfManejoListNewManejo != null && !oldVehiculoOfManejoListNewManejo.equals(vehiculo)) {
                        oldVehiculoOfManejoListNewManejo.getManejoList().remove(manejoListNewManejo);
                        oldVehiculoOfManejoListNewManejo = em.merge(oldVehiculoOfManejoListNewManejo);
                    }
                }
            }
            utx.commit();
        } catch (IllegalOrphanException ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = vehiculo.getPlaca();
                if (findVehiculo(id) == null) {
                    throw new NonexistentEntityException("The vehiculo with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        try {
            em = getEntityManager();
            utx = em.getTransaction();
            utx.begin();
            Vehiculo vehiculo;
            try {
                vehiculo = em.getReference(Vehiculo.class, id);
                vehiculo.getPlaca();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The vehiculo with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Manejo> manejoListOrphanCheck = vehiculo.getManejoList();
            for (Manejo manejoListOrphanCheckManejo : manejoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Vehiculo (" + vehiculo + ") cannot be destroyed since the Manejo " + manejoListOrphanCheckManejo + " in its manejoList field has a non-nullable vehiculo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Proveedor proveedor = vehiculo.getProveedor();
            if (proveedor != null) {
                proveedor.getVehiculoList().remove(vehiculo);
                proveedor = em.merge(proveedor);
            }
            em.remove(vehiculo);
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

    public List<Vehiculo> findVehiculoEntities() {
        return findVehiculoEntities(true, -1, -1);
    }

    public List<Vehiculo> findVehiculoEntities(int maxResults, int firstResult) {
        return findVehiculoEntities(false, maxResults, firstResult);
    }

    private List<Vehiculo> findVehiculoEntities(boolean all, int maxResults, int firstResult) {
        em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Vehiculo.class));
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

    public Vehiculo findVehiculo(String id) {
        em = getEntityManager();
        try {
            return em.find(Vehiculo.class, id);
        } finally {
            em.close();
        }
    }

    public int getVehiculoCount() {
        em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Vehiculo> rt = cq.from(Vehiculo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
