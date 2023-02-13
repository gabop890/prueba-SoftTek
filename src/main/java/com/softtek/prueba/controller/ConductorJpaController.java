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
import com.softtek.prueba.model.Conductor;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.softtek.prueba.model.Manejo;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.UserTransaction;

/**
 *
 * @author Gabriel
 */
public class ConductorJpaController implements Serializable {
    
    @Resource
    private UserTransaction utx;
    
    @Resource
    private EntityManager em;

    public void create(Conductor conductor) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (conductor.getManejoList() == null) {
            conductor.setManejoList(new ArrayList<Manejo>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            List<Manejo> attachedManejoList = new ArrayList<Manejo>();
            for (Manejo manejoListManejoToAttach : conductor.getManejoList()) {
                manejoListManejoToAttach = em.getReference(manejoListManejoToAttach.getClass(), manejoListManejoToAttach.getManejoPK());
                attachedManejoList.add(manejoListManejoToAttach);
            }
            conductor.setManejoList(attachedManejoList);
            em.persist(conductor);
            for (Manejo manejoListManejo : conductor.getManejoList()) {
                Conductor oldConductor1OfManejoListManejo = manejoListManejo.getConductor1();
                manejoListManejo.setConductor1(conductor);
                manejoListManejo = em.merge(manejoListManejo);
                if (oldConductor1OfManejoListManejo != null) {
                    oldConductor1OfManejoListManejo.getManejoList().remove(manejoListManejo);
                    oldConductor1OfManejoListManejo = em.merge(oldConductor1OfManejoListManejo);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findConductor(conductor.getNumiden()) != null) {
                throw new PreexistingEntityException("Conductor " + conductor + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Conductor conductor) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            Conductor persistentConductor = em.find(Conductor.class, conductor.getNumiden());
            List<Manejo> manejoListOld = persistentConductor.getManejoList();
            List<Manejo> manejoListNew = conductor.getManejoList();
            List<String> illegalOrphanMessages = null;
            for (Manejo manejoListOldManejo : manejoListOld) {
                if (!manejoListNew.contains(manejoListOldManejo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Manejo " + manejoListOldManejo + " since its conductor1 field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Manejo> attachedManejoListNew = new ArrayList<Manejo>();
            for (Manejo manejoListNewManejoToAttach : manejoListNew) {
                manejoListNewManejoToAttach = em.getReference(manejoListNewManejoToAttach.getClass(), manejoListNewManejoToAttach.getManejoPK());
                attachedManejoListNew.add(manejoListNewManejoToAttach);
            }
            manejoListNew = attachedManejoListNew;
            conductor.setManejoList(manejoListNew);
            conductor = em.merge(conductor);
            for (Manejo manejoListNewManejo : manejoListNew) {
                if (!manejoListOld.contains(manejoListNewManejo)) {
                    Conductor oldConductor1OfManejoListNewManejo = manejoListNewManejo.getConductor1();
                    manejoListNewManejo.setConductor1(conductor);
                    manejoListNewManejo = em.merge(manejoListNewManejo);
                    if (oldConductor1OfManejoListNewManejo != null && !oldConductor1OfManejoListNewManejo.equals(conductor)) {
                        oldConductor1OfManejoListNewManejo.getManejoList().remove(manejoListNewManejo);
                        oldConductor1OfManejoListNewManejo = em.merge(oldConductor1OfManejoListNewManejo);
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
                Integer id = conductor.getNumiden();
                if (findConductor(id) == null) {
                    throw new NonexistentEntityException("The conductor with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            Conductor conductor;
            try {
                conductor = em.getReference(Conductor.class, id);
                conductor.getNumiden();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The conductor with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Manejo> manejoListOrphanCheck = conductor.getManejoList();
            for (Manejo manejoListOrphanCheckManejo : manejoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Conductor (" + conductor + ") cannot be destroyed since the Manejo " + manejoListOrphanCheckManejo + " in its manejoList field has a non-nullable conductor1 field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(conductor);
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

    public List<Conductor> findConductorEntities() {
        return findConductorEntities(true, -1, -1);
    }

    public List<Conductor> findConductorEntities(int maxResults, int firstResult) {
        return findConductorEntities(false, maxResults, firstResult);
    }

    private List<Conductor> findConductorEntities(boolean all, int maxResults, int firstResult) {
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Conductor.class));
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

    public Conductor findConductor(Integer id) {
        try {
            return em.find(Conductor.class, id);
        } finally {
            em.close();
        }
    }

    public int getConductorCount() {
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Conductor> rt = cq.from(Conductor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
