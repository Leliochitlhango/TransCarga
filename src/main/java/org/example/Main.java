package org.example;

import dao.CamiaoDAO;
import dao.CarrinhaDAO;
import dao.ClienteDAO;
import dao.ProprietarioDAO;
import javax.swing.JOptionPane;
import model.Camiao;
import model.Carrinha;
import model.Cliente;
import model.Proprietario;
import model.Veiculo;

public class Main {

    public static void main(String[] args) {
        /*
        Cliente c = new Cliente();
        
        c.setId(1);
        c.setNome("Julio Rafael Chirrime");
        c.setContacto("843805459");
        c.setEndereco("Av. 4 Outubro, Bairro Acordos de Lusaka, Q.19, casa 451");

        
        if (new ClienteDAO().save(c)) {
            JOptionPane.showMessageDialog(null, "Cliente Salvo Com sucesso");
        } else {
            JOptionPane.showMessageDialog(null, "Falha ao salvar cliente");
        }

         */

        Proprietario p1 = new Proprietario();
        p1.setId(1);
        p1.setNome("Bruno");
        p1.setContacto("8796325874");
        p1.setNumeroDocumentoIdentificacao("1101143533D");

        Proprietario p2 = new Proprietario();
        p2.setId(2);
        p2.setNome("Transportes Maputo Lda");
        p2.setContacto("827654321");
        p2.setNumeroDocumentoIdentificacao("1101143533D");

        // 2. Camiões
        Camiao c1 = new Camiao("ABC-123-MC", "DI-0001", "4", "Carga geral");
        c1.setCapacidadeCarga(20000);
        c1.setProprietario(p1);
        
        new CamiaoDAO().save(c1);

        Camiao c2 = new Camiao("DEF-789-MC", "DI-0002", "3", "Combustível");
        c2.setCapacidadeCarga(15000);
        c2.setProprietario(p2);
        
        new CamiaoDAO().save(c2);

        // 3. Carrinhas
        Carrinha v1 = new Carrinha("XYZ-456-MP", "DI-0003", 15);
        v1.setCapacidadeCarga(1500);
        v1.setTipoCarroceria("Fechada");
        v1.setProprietario(p1);
        
        new CarrinhaDAO().save(v1);

        Carrinha v2 = new Carrinha("GHI-321-MP", "DI-0004", 9);
        v2.setCapacidadeCarga(1200);
        v2.setTipoCarroceria("Aberta");
        v2.setProprietario(p2);

        new CarrinhaDAO().save(v2);
        
    }
}
