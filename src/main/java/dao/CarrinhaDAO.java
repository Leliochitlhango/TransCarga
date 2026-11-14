
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
            
            Carrinha carrinha = session.find(Carrinha.class, id);
            
            return carrinha;
        } catch(Exception e) {
            throw e;
        }
    }
    
    public ListaDuplamenteLigada<Carrinha> findAll() {
        ListaDuplamenteLigada<Carrinha> lista = new ListaDuplamenteLigada<Carrinha>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Carrinha> carrinhas = session.createQuery(
                    "FROM Carrinha ORDER BY capacidadeCarga",
                    Carrinha.class
            ).getResultList();
            for (Carrinha c: carrinhas) {
                lista.inserirFim(c);
            }
            return lista;
        } catch (Exception e) {
            throw e;
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
            
            if (session.contains(carrinha)) {
                
                session.remove(carrinha);
                
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
