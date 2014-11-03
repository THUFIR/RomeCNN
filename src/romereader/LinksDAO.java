
package romereader;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import romereader.exceptions.NonexistentEntityException;

public class LinksDAO implements Serializable {

    
    private final static Logger log = Logger.getLogger(LinksDAO.class.getName());

    
    public LinksDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Links links) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(links);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Links links) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            links = em.merge(links);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = links.getId();
                if (findLinks(id) == null) {
                    throw new NonexistentEntityException("The links with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Links links;
            try {
                links = em.getReference(Links.class, id);
                links.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The links with id " + id + " no longer exists.", enfe);
            }
            em.remove(links);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Links> findLinksEntities() {
        return findLinksEntities(true, -1, -1);
    }

    public List<Links> findLinksEntities(int maxResults, int firstResult) {
        return findLinksEntities(false, maxResults, firstResult);
    }

    private List<Links> findLinksEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Links.class));
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

    public Links findLinks(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Links.class, id);
        } finally {
            em.close();
        }
    }

    public int getLinksCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Links> rt = cq.from(Links.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }


    
    public Links searchForMatch(Links newLink) {
        EntityManager em = getEntityManager();
        Links linkResult = null;
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Links> clientCriteriaQuery = criteriaBuilder.createQuery(Links.class);
        Root<Links> clientRoot = clientCriteriaQuery.from(Links.class);
        clientCriteriaQuery.select(clientRoot);
        List<Links> links = em.createQuery(clientCriteriaQuery).getResultList();
        try {
            linkResult = em.createQuery(clientCriteriaQuery).getSingleResult();
        } catch (javax.persistence.NoResultException nre) {
            log.info(nre.toString());
        }
        em.close();
        return linkResult;
    }
    
}
