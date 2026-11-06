package model;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@MappedSuperclass
public abstract class Veiculo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;
    
    @Column(nullable = false, length=20)
    protected String matricula;
    
    @Column(nullable = false, length=20)
    protected String documentoIdentifcacao;
    
    @Column(nullable = false)
    protected int capacidadeCarga;
    
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "proprietario_id", nullable = false)
    protected Proprietario proprietario;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getDocumentoIdentifcacao() {
        return documentoIdentifcacao;
    }

    public void setDocumentoIdentifcacao(String documentoIdentifcacao) {
        this.documentoIdentifcacao = documentoIdentifcacao;
    }
    
    public int getCapacidadeCarga() {
        return this.capacidadeCarga;
    }
    
    public void setCapacidadeCarga(int capacidade) {
        this.capacidadeCarga = capacidade;
    }
    
    public void setProprietario(Proprietario p) {
        this.proprietario = p;
    }
}
