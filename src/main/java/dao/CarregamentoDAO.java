
package dao;

import java.util.List;
import model.Carregamento;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

/**
 *
 * @author julius
 */
public class CarregamentoDAO {
    
    public boolean save(Carregamento carregamento) {
        Transaction transaction = null;
        
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            
            transaction = session.beginTransaction();
            
            session.persist(carregamento);
            
            transaction.commit();
            
            return true;
        } catch(Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        } 
    }
    
    public Carregamento findById(int id) {
        
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            
            Carregamento carregamento = session.find(Carregamento.class, id);
            
            return carregamento;
        } catch(Exception e) {
            throw e;
        }
    }
    
    public List<Carregamento> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Carregamento ORDER BY data",
                    Carregamento.class
            ).getResultList();
        } catch (Exception e) {
            throw e;
        }
    }
    
    public boolean update(Carregamento carregamento) {
        Transaction transaction = null;
        
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            
            session.merge(carregamento);
            
            transaction.commit();
            
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            } 
            throw e;
        }
        
    }
    
    public boolean remove(Carregamento carregamento) {
        
        Transaction transaction = null;
        
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            
            transaction = session.beginTransaction();
            
            if (session.contains(carregamento)) {
                
                session.remove(carregamento);
                
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
