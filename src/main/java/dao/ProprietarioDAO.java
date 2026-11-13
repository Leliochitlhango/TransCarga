
package dao;

import dataStructure.ListaDuplamenteLigada;
import java.util.ArrayList;
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
    
    public ListaDuplamenteLigada<Proprietario> findAll() {
        
        ListaDuplamenteLigada<Proprietario> lista = new ListaDuplamenteLigada<>();
        
        List<Proprietario> listaAuxiliar = new ArrayList<>();
        
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            listaAuxiliar = session.createQuery(
                    "FROM Proprietario ORDER BY nome",
                    Proprietario.class
            ).getResultList();
            
            for (Proprietario p: listaAuxiliar) {
                lista.inserirFim(p);
            }
            
            return lista;
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
