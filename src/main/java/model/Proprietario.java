package model;

import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;


@Entity
@Table(name = "proprietarios")
public class Proprietario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(nullable=false, length=50)
    private  String nome;
    
    @Column(nullable=false, length=15)
    private  String contacto;
    
    @Column(nullable=false, length=20)
    private String numeroDocumentoIdentificacao;

    public Proprietario(int id, String nome, String contacto, String numeroDocumentoIdentificacao) {
        this.id = id;
        this.nome = nome;
        this.contacto = contacto;
        this.numeroDocumentoIdentificacao = numeroDocumentoIdentificacao;
    }

    public Proprietario(String nome, String contacto, String numeroDocumentoIdentificacao) {
        this.nome = nome;
        this.contacto = contacto;
        this.numeroDocumentoIdentificacao = numeroDocumentoIdentificacao;
    }

    public Proprietario() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getNumeroDocumentoIdentificacao() {
        return numeroDocumentoIdentificacao;
    }

    public void setNumeroDocumentoIdentificacao(String numeroDocumentoIdentificacao) {
        this.numeroDocumentoIdentificacao = numeroDocumentoIdentificacao;
    }
}
