package model;

public class Propietario {
    private String id;
    private  String nome;
    private  int contacto;
    private int numeroDocumentoIdentificacao;

    public Propietario(String id, String nome, int contacto, int numeroDocumentoIdentificacao) {
        this.id = id;
        this.nome = nome;
        this.contacto = contacto;
        this.numeroDocumentoIdentificacao = numeroDocumentoIdentificacao;
    }

    public Propietario(String nome, int contacto, int numeroDocumentoIdentificacao) {
        this.nome = nome;
        this.contacto = contacto;
        this.numeroDocumentoIdentificacao = numeroDocumentoIdentificacao;
    }

    public Propietario() {
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

    public int getContacto() {
        return contacto;
    }

    public void setContacto(int contacto) {
        this.contacto = contacto;
    }

    public int getNumeroDocumentoIdentificacao() {
        return numeroDocumentoIdentificacao;
    }

    public void setNumeroDocumentoIdentificacao(int numeroDocumentoIdentificacao) {
        this.numeroDocumentoIdentificacao = numeroDocumentoIdentificacao;
    }
}
