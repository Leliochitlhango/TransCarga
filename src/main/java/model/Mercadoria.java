package model;

import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;


@Entity
@Table(name = "mercadorias")
public class Mercadoria {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    
    @Column(nullable=false, length=100)
    private String Descricao;
    
    @Column(nullable=false)
    private int quantidade;

    public Mercadoria(String id, String descricao, int quantidade) {
        this.id = id;
        Descricao = descricao;
        this.quantidade = quantidade;
    }

    public Mercadoria() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescricao() {
        return Descricao;
    }

    public void setDescricao(String descricao) {
        Descricao = descricao;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}
