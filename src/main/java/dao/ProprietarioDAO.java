
package dao;

import java.util.List;
import model.Proprietario;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

/**
 *
 * @author 
 */
public class ProprietarioDAO {
    
    public boolean save(Proprietario p) {
        Transaction transaction = null;
        
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            
            transaction = session.beginTransaction();
            
            session.persist(p);
            
            transaction.commit();
            
            return true;
        } catch(Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        } 
    }
    
    public Proprietario findById(int id) {
        
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            
            Proprietario proprietario = session.find(Proprietario.class, id);
            
            return proprietario;
        } catch(Exception e) {
            throw e;
        }
    }
    
    public List<Proprietario> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Proprietario ORDER BY nome",
                    Proprietario.class
            ).getResultList();
        } catch (Exception e) {
            throw e;
        }
    }
    
    
    public boolean update(Proprietario p) {
        Transaction transaction = null;
        
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            
            session.merge(p);
            
            transaction.commit();
            
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            } 
            throw e;
        }
        
    }
    
    public boolean remove(Proprietario p) {
        
        Transaction transaction = null;
        
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            
            transaction = session.beginTransaction();
            
            if (session.contains(p)) {
                
                session.remove(p);
                
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
