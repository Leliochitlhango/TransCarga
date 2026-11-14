package model;


import java.io.Serializable;

public class Transportes implements Serializable {
    private String id;
    private  String nome;
    private   String contacto;

    public Transportes(String id, String nome, String contacto) {
        this.id = id;
        this.nome = nome;
        this.contacto = contacto;
    }

    public Transportes(String nome, String contacto) {
        this.nome = nome;
        this.contacto = contacto;
    }

    public Transportes() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
}
