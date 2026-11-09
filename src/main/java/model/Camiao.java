package model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("CAMIAO")
public class Camiao extends Veiculo {

    @Column(name = "numero_eixos")
    private String numeroEixos;

    @Column(name = "tipo_mercadoria", length = 20)
    private String tipoMercadoria;

    public Camiao() {
    }

    public Camiao(String matricula, String documentoIdentifcacao,
                  String numeroEixos, String tipoMercadoria) {
        this.matricula = matricula;
        this.documentoIdentifcacao = documentoIdentifcacao;
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
