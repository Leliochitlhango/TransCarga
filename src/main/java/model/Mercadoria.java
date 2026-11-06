package model;

public class Mercadoria {
    private String id;
    private String Descricao;
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
