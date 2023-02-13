/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softtek.prueba.controller;

import com.softtek.prueba.controller.exceptions.NonexistentEntityException;
import com.softtek.prueba.controller.exceptions.PreexistingEntityException;
import com.softtek.prueba.controller.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.softtek.prueba.model.Conductor;
import com.softtek.prueba.model.Manejo;
import com.softtek.prueba.model.ManejoPK;
import com.softtek.prueba.model.Vehiculo;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.UserTransaction;

/**
 *
 * @author Gabriel
 */
public class ManejoJpaController implements Serializable {

    public ManejoJpaController(UserTransaction utx) {
        this.utx = utx;
        this.emf = Persistence.createEntityManagerFactory("com.softTek_prueba_war_1.0-SNAPSHOTPU");
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Manejo manejo) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (manejo.getManejoPK() == null) {
            manejo.setManejoPK(new ManejoPK());
        }
        manejo.getManejoPK().setConductor(manejo.getConductor1().getNumiden());
        manejo.getManejoPK().setPlaca(manejo.getVehiculo().getPlaca());
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Conductor conductor1 = manejo.getConductor1();
            if (conductor1 != null) {
                conductor1 = em.getReference(conductor1.getClass(), conductor1.getNumiden());
                manejo.setConductor1(conductor1);
            }
            Vehiculo vehiculo = manejo.getVehiculo();
            if (vehiculo != null) {
                vehiculo = em.getReference(vehiculo.getClass(), vehiculo.getPlaca());
                manejo.setVehiculo(vehiculo);
            }
            em.persist(manejo);
            if (conductor1 != null) {
                conductor1.getManejoList().add(manejo);
                conductor1 = em.merge(conductor1);
            }
            if (vehiculo != null) {
                vehiculo.getManejoList().add(manejo);
                vehiculo = em.merge(vehiculo);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findManejo(manejo.getManejoPK()) != null) {
                throw new PreexistingEntityException("Manejo " + manejo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Manejo manejo) throws NonexistentEntityException, RollbackFailureException, Exception {
        manejo.getManejoPK().setConductor(manejo.getConductor1().getNumiden());
        manejo.getManejoPK().setPlaca(manejo.getVehiculo().getPlaca());
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Manejo persistentManejo = em.find(Manejo.class, manejo.getManejoPK());
            Conductor conductor1Old = persistentManejo.getConductor1();
            Conductor conductor1New = manejo.getConductor1();
            Vehiculo vehiculoOld = persistentManejo.getVehiculo();
            Vehiculo vehiculoNew = manejo.getVehiculo();
            if (conductor1New != null) {
                conductor1New = em.getReference(conductor1New.getClass(), conductor1New.getNumiden());
                manejo.setConductor1(conductor1New);
            }
            if (vehiculoNew != null) {
                vehiculoNew = em.getReference(vehiculoNew.getClass(), vehiculoNew.getPlaca());
                manejo.setVehiculo(vehiculoNew);
            }
            manejo = em.merge(manejo);
            if (conductor1Old != null && !conductor1Old.equals(conductor1New)) {
                conductor1Old.getManejoList().remove(manejo);
                conductor1Old = em.merge(conductor1Old);
            }
            if (conductor1New != null && !conductor1New.equals(conductor1Old)) {
                conductor1New.getManejoList().add(manejo);
                conductor1New = em.merge(conductor1New);
            }
            if (vehiculoOld != null && !vehiculoOld.equals(vehiculoNew)) {
                vehiculoOld.getManejoList().remove(manejo);
                vehiculoOld = em.merge(vehiculoOld);
            }
            if (vehiculoNew != null && !vehiculoNew.equals(vehiculoOld)) {
                vehiculoNew.getManejoList().add(manejo);
                vehiculoNew = em.merge(vehiculoNew);
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
                ManejoPK id = manejo.getManejoPK();
                if (findManejo(id) == null) {
                    throw new NonexistentEntityException("The manejo with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(ManejoPK id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Manejo manejo;
            try {
                manejo = em.getReference(Manejo.class, id);
                manejo.getManejoPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The manejo with id " + id + " no longer exists.", enfe);
            }
            Conductor conductor1 = manejo.getConductor1();
            if (conductor1 != null) {
                conductor1.getManejoList().remove(manejo);
                conductor1 = em.merge(conductor1);
            }
            Vehiculo vehiculo = manejo.getVehiculo();
            if (vehiculo != null) {
                vehiculo.getManejoList().remove(manejo);
                vehiculo = em.merge(vehiculo);
            }
            em.remove(manejo);
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

    public List<Manejo> findManejoEntities() {
        return findManejoEntities(true, -1, -1);
    }

    public List<Manejo> findManejoEntities(int maxResults, int firstResult) {
        return findManejoEntities(false, maxResults, firstResult);
    }

    private List<Manejo> findManejoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Manejo.class));
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

    public Manejo findManejo(ManejoPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Manejo.class, id);
        } finally {
            em.close();
        }
    }

    public int getManejoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Manejo> rt = cq.from(Manejo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
