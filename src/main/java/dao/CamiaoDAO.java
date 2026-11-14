
package dao;

import dataStructure.ListaDuplamenteLigada;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import model.Camiao;
import util.HibernateUtil;
/**
 *
 * @author julius
 */
public class CamiaoDAO {
    
    public boolean save(Camiao camiao) {
        Transaction transaction = null;
        
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            
            transaction = session.beginTransaction();
            
            session.persist(camiao);
            
            transaction.commit();
            
            return true;
        } catch(Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        } 
    }
    
    public Camiao findById(int id) {
        
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            
            Camiao camiao = session.find(Camiao.class, id);
            
            return camiao;
        } catch(Exception e) {
            throw e;
        }
    }
    
    public ListaDuplamenteLigada<Camiao> findAll() {
        ListaDuplamenteLigada<Camiao> lista = new ListaDuplamenteLigada<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Camiao> camioes = session.createQuery(
                    "FROM Camiao ORDER BY capacidadeCarga",
                    Camiao.class
            ).getResultList();
            for (Camiao c: camioes) {
                lista.inserirFim(c);
            }
            return lista;
        } catch (Exception e) {
            throw e;
        }
    }
    
    public boolean update(Camiao camiao) {
        Transaction transaction = null;
        
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            
            session.merge(camiao);
            
            transaction.commit();
            
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            } 
            throw e;
        }
        
    }
    
    public boolean remove(Camiao camiao) {
        
        Transaction transaction = null;
        
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            
            transaction = session.beginTransaction();
            
            if (session.contains(camiao)) {
                
                session.remove(camiao);
                
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
