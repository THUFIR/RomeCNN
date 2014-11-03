package dur.bounceme.net.rome.jpa;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import romereader.exceptions.NonexistentEntityException;

public class LinkJpaController implements Serializable {

    private final static Logger log = Logger.getLogger(LinkJpaController.class.getName());

    public LinkJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Link link) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(link);
            em.getTransaction().commit();
        } catch (Exception e) {
            log.fine(e.toString());
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Link link) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            link = em.merge(link);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = link.getId();
                if (findLink(id) == null) {
                    throw new NonexistentEntityException("The link with id " + id + " no longer exists.");
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
            Link link;

            try {
                link = em.getReference(Link.class, id);
                link.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The link with id " + id + " no longer exists.", enfe);
            }
            em.remove(link);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Link> findLinkEntities() {
        return findLinkEntities(true, -1, -1);
    }

    public List<Link> findLinkEntities(int maxResults, int firstResult) {
        return findLinkEntities(false, maxResults, firstResult);
    }

    private List<Link> findLinkEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq
                    .select(cq.from(Link.class
                            ));
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

    public Link findLink(Integer id) {
        EntityManager em = getEntityManager();

        try {
            return em.find(Link.class, id);
        } finally {
            em.close();
        }
    }

    public int getLinkCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Link> rt = cq.from(Link.class
            );
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
