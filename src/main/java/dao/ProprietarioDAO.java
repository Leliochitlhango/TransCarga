
package dao;

import dataStructure.ListaDuplamenteLigada;
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
            return session.find(Proprietario.class, id);
        }
    }
    
    public ListaDuplamenteLigada<Proprietario> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Proprietario> resultados = session.createQuery(
                    "FROM Proprietario ORDER BY nome",
                    Proprietario.class
            ).getResultList();
            return ListaDuplamenteLigada.de(resultados);
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
            Proprietario entity = session.find(Proprietario.class, p.getId());
            if (entity != null) {
                session.remove(entity);
                transaction.commit();
                return true;
            }
            if (transaction != null) {
                transaction.rollback();
            }
            return false;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }
    
}
