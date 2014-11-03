package romereader;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.RollbackException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import romereader.exceptions.NonexistentEntityException;

public class LinkDAO implements Serializable {

    private final static Logger log = Logger.getLogger(LinkDAO.class.getName());

    public LinkDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /*
     public Link searchForMatch(Link newLink) {
     EntityManager em = getEntityManager();
     Link linkResult = null;
     CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
     CriteriaQuery<Link> linkCriteriaQuery = criteriaBuilder.createQuery(Link.class);
     Root<Link> clientRoot = linkCriteriaQuery.from(Link.class);
     linkCriteriaQuery.select(clientRoot);
     List<Predicate> predicates = new ArrayList<>();
     //      predicates.add(criteriaBuilder.equal(clientRoot.get(Link_.link), "%" + newLink.getLink() + "%"));
     linkCriteriaQuery.where(predicates.toArray(new Predicate[0]));
     List<Link> links = em.createQuery(linkCriteriaQuery).getResultList();
     try {
     linkResult = em.createQuery(linkCriteriaQuery).getSingleResult();
     } catch (javax.persistence.NoResultException nre) {
     log.info(nre.toString());
     }
     em.close();
     return linkResult;
     }

     */
    public void create(Link link) {
        EntityManager em = null;
        link.setStatus(0);
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(link);
            em.getTransaction().commit();
        } catch (RollbackException rbe) {
            log.warning(rbe.toString());
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void create2(Link link) {
        log.warning(link.toString());
        EntityManager em = null;
        link.setStatus(0);
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.merge(link);
            em.getTransaction().commit();
        } catch (RollbackException rbe) {
            log.warning(rbe.toString());
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Link links) throws NonexistentEntityException, Exception {
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
            Link links;
            try {
                links = em.getReference(Link.class, id);
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

    public List<Link> findLinksEntities() {
        return findLinksEntities(true, -1, -1);
    }

    public List<Link> findLinksEntities(int maxResults, int firstResult) {
        return findLinksEntities(false, maxResults, firstResult);
    }

    private List<Link> findLinksEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Link.class));
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

    public Link findLinks(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Link.class, id);
        } finally {
            em.close();
        }
    }

    public int getLinksCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Link> rt = cq.from(Link.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
