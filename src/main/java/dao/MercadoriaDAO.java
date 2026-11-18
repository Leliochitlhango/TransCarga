
package dao;

import dataStructure.ListaDuplamenteLigada;
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
    
    public ListaDuplamenteLigada<Mercadoria> findAll() {
        ListaDuplamenteLigada<Mercadoria> lista = new ListaDuplamenteLigada<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Mercadoria> resultados = session.createQuery(
                    "FROM Mercadoria ORDER BY descricao",
                    Mercadoria.class
            ).getResultList();
            for (Mercadoria m : resultados) {
                lista.inserirFim(m);
            }
            return lista;
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
