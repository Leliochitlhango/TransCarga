package model;

import java.io.Serializable;

public class Camiao extends veiculo implements Serializable {

    private String numeroEixos;
    private String tipoMercadoria;

    public Camiao(String matricula, String id, int contacto, int documentoIdentifcacao, String numeroEixos, String tipoMercadoria) {
        super(matricula, id, contacto, documentoIdentifcacao);
        this.numeroEixos = numeroEixos;
        this.tipoMercadoria = tipoMercadoria;
    }

    public Camiao(String matricula, int contacto, int documentoIdentifcacao, String numeroEixos, String tipoMercadoria) {
        super(matricula, contacto, documentoIdentifcacao);
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
