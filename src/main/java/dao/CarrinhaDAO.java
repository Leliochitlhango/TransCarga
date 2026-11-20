
package dao;

import dataStructure.ListaDuplamenteLigada;
import java.util.List;
import model.Carrinha;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

/**
 *
 * @author julius
 */
public class CarrinhaDAO {
    
    public boolean save(Carrinha carrinha) {
        Transaction transaction = null;
        
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            
            transaction = session.beginTransaction();
            
            session.persist(carrinha);
            
            transaction.commit();
            
            return true;
        } catch(Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        } 
    }
    
    public Carrinha findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.find(Carrinha.class, id);
        }
    }
    
    public ListaDuplamenteLigada<Carrinha> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Carrinha> carrinhas = session.createQuery(
                    "FROM Carrinha ORDER BY capacidadeCarga",
                    Carrinha.class
            ).getResultList();
            return ListaDuplamenteLigada.de(carrinhas);
        }
    }
    
    
    public boolean update(Carrinha carrinha) {
        Transaction transaction = null;
        
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            
            session.merge(carrinha);
            
            transaction.commit();
            
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            } 
            throw e;
        }
        
    }
    
    public boolean remove(Carrinha carrinha) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Carrinha entity = session.find(Carrinha.class, carrinha.getId());
            if (entity != null) {
                session.remove(entity);
                transaction.commit();
                return true;
            }
            if (transaction != null) transaction.rollback();
            return false;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }
}
