package model;

import java.util.Objects;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;


public class Cliente {
    
    private int id;
    private  String nome;
    private int contacto;
    private String endereco;


    public Cliente(int id, String nome, int contacto, String endereco) {
        this.id = id;
        this.nome = nome;
        this.contacto = contacto;
        this.endereco = endereco;
    }

    public Cliente() {
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

    public int getContacto() {
        return contacto;
    }

    public void setContacto(int contacto) {
        this.contacto = contacto;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return Objects.equals(getId(), cliente.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "cliente{" +
                "id='" + id + '\'' +
                ", nome='" + nome + '\'' +
                ", contacto=" + contacto +
                ", endereco='" + endereco + '\'' +
                '}';
    }
}
