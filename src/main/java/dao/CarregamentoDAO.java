
package dao;

import dataStructure.ListaDuplamenteLigada;
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
            return session.find(Carregamento.class, id);
        }
    }
    
    public ListaDuplamenteLigada<Carregamento> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Carregamento> carregamentos =  session.createQuery(
                    "FROM Carregamento ORDER BY data",
                    Carregamento.class
            ).getResultList();
            return ListaDuplamenteLigada.de(carregamentos);
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
            Carregamento entity = session.find(Carregamento.class, carregamento.getId());
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
