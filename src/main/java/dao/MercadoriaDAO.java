
package dao;

import java.util.List;
import model.Mercadoria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

/**
 *
 * @author julius
 */
public class MercadoriaDAO {
    
    public boolean save(Mercadoria m) {
        Transaction transaction = null;
        
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            
            transaction = session.beginTransaction();
            
            session.persist(m);
            
            transaction.commit();
            
            return true;
        } catch(Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        } 
    }
    
    public Mercadoria findById(int id) {
        
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            
            Mercadoria m = session.find(Mercadoria.class, id);
            
            return m;
        } catch(Exception e) {
            throw e;
        }
    }
    
    public List<Mercadoria> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Mercadoria ORDER BY descricao",
                    Mercadoria.class
            ).getResultList();
        } catch (Exception e) {
            throw e;
        }
    }
    
    public boolean update(Mercadoria m) {
        Transaction transaction = null;
        
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            
            session.merge(m);
            
            transaction.commit();
            
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            } 
            throw e;
        }
        
    }
    
    public boolean remove(Mercadoria m) {
        
        Transaction transaction = null;
        
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            
            transaction = session.beginTransaction();
            
            if (session.contains(m)) {
                
                session.remove(m);
                
                transaction.commit();
                
                return true;
            }
            
            return false;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
        
    }
}
