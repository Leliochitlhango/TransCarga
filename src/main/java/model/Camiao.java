package model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "camioes")
public class Camiao extends Veiculo {

    @Column(nullable = false)
    private String numeroEixos;
    
    @Column(nullable = false, length=20)
    private String tipoMercadoria;
    

    public Camiao(String matricula, String id, int contacto, int documentoIdentifcacao, String numeroEixos, String tipoMercadoria) {
        this.numeroEixos = numeroEixos;
        this.tipoMercadoria = tipoMercadoria;
    }

    public Camiao(String matricula, int contacto, int documentoIdentifcacao, String numeroEixos, String tipoMercadoria) {
        this.numeroEixos = numeroEixos;
        this.tipoMercadoria = tipoMercadoria;
    }

    public Camiao(String numeroEixos, String tipoMercadoria) {
        this.numeroEixos = numeroEixos;
        this.tipoMercadoria = tipoMercadoria;
    }

    public String getNumeroEixos() {
        return numeroEixos;
    }

    public void setNumeroEixos(String numeroEixos) {
        this.numeroEixos = numeroEixos;
    }

    public String getTipoMercadoria() {
        return tipoMercadoria;
    }

    public void setTipoMercadoria(String tipoMercadoria) {
        this.tipoMercadoria = tipoMercadoria;
    }
}
