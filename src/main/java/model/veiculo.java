package model;

import java.io.Serializable;

public abstract class veiculo implements Serializable {
    private String id;
    private String matricula;
    private int contacto;
    private int DocumentoIdentifcacao;
    private int CapacidadeCarga;

    public veiculo(String matricula, String id, int contacto, int documentoIdentifcacao) {
        this.matricula = matricula;
        this.id = id;
        this.contacto = contacto;
        DocumentoIdentifcacao = documentoIdentifcacao;
    }

    public veiculo(String matricula, int contacto, int documentoIdentifcacao) {
        this.matricula = matricula;
        this.contacto = contacto;
        DocumentoIdentifcacao = documentoIdentifcacao;
    }


    public veiculo() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public int getContacto() {
        return contacto;
    }

    public void setContacto(int contacto) {
        this.contacto = contacto;
    }

    public int getDocumentoIdentifcacao() {
        return DocumentoIdentifcacao;
    }

    public void setDocumentoIdentifcacao(int documentoIdentifcacao) {
        DocumentoIdentifcacao = documentoIdentifcacao;
    }
}
