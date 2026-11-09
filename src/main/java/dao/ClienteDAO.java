
package dao;

import java.util.List;
import model.Cliente;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

/**
 *
 * @author julius
 */
public class ClienteDAO {
    
    public boolean save(Cliente cliente) {
        Transaction transaction = null;
        
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            
            transaction = session.beginTransaction();
            
            session.persist(cliente);
            
            transaction.commit();
            
            return true;
        } catch(Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        } 
    }
    
    public Cliente findById(int id) {
        
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            
            Cliente cliente = session.find(Cliente.class, id);
            
            return cliente;
        } catch(Exception e) {
            throw e;
        }
    }
    
    public List<Cliente> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Cliente ORDER BY nome",
                    Cliente.class
            ).getResultList();
        } catch (Exception e) {
            throw e;
        }
    }
    
    public boolean update(Cliente cliente) {
        Transaction transaction = null;
        
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            
            session.merge(cliente);
            
            transaction.commit();
            
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            } 
            throw e;
        }
        
    }
    
    public boolean remove(Cliente cliente) {
        
        Transaction transaction = null;
        
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            
            transaction = session.beginTransaction();
            
            if (session.contains(cliente)) {
                
                session.remove(cliente);
                
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
