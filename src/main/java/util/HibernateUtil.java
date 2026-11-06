package util;

import model.Camiao;
import model.Carregamento;
import model.Carrinha;
import model.Cliente;
import model.Mercadoria;
import model.Proprietario;
import model.Veiculo;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


/**
 * Classe responsável pela configuração do hibernate para a persistência de
 * dados no banco
 *
 * @author julius
 */
public class HibernateUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    private static SessionFactory buildSessionFactory() {

        try {

            Configuration config = new Configuration();

            config.setProperty(
                    "hibernate.connection.driver_class",
                    "com.mysql.cj.jdbc.Driver"
            );

            config.setProperty(
                    "hibernate.connection.url",
                    "jdbc:mysql://localhost:3307/petromoz"
            );

            config.setProperty(
                    "hibernate.connection.username",
                    "root"
            );

            config.setProperty(
                    "hibernate.connection.password",
                    ""
            );

            //config.setProperty(
            //    "hibernate.dialect", 
            //    "org.hibernate.dialect.MySQLDialect"
            //);
            config.setProperty(
                    "hibernate.hbm2ddl.auto",
                    "update"
            );

            config.setProperty(
                    "hibernate.show_sql",
                    "true"
            );

            config.setProperty(
                    "hibernate.format_sql",
                    "true"
            );

            //config.addAnnotatedClass(Abastecimento.class);
            

            return config.buildSessionFactory();

        } catch (HibernateException ex) {
            System.err.println("Erro ao criar SessionFactory: " + ex);
            throw new ExceptionInInitializerError(ex);
        }

    }
}
