package model;

import java.io.Serializable;

public class Carrinha extends veiculo implements Serializable {
    private int passageiros;
    private String TipCarroceria;

    public Carrinha(String matricula, String id, int contacto, int documentoIdentifcacao, int passageiros) {
        super(matricula, id, contacto, documentoIdentifcacao);
        this.passageiros = passageiros;
    }

    public Carrinha(String matricula, int contacto, int documentoIdentifcacao, int passageiros) {
        super(matricula, contacto, documentoIdentifcacao);
        this.passageiros = passageiros;
    }

    public Carrinha(int passageiros) {
        this.passageiros = passageiros;
    }

    public String getTipCarroceria() {
        return TipCarroceria;
    }

    public void setTipCarroceria(String tipCarroceria) {
        TipCarroceria = tipCarroceria;
    }

    public int getPassageiros() {
        return passageiros;
    }

    public void setPassageiros(int passageiros) {
        this.passageiros = passageiros;
    }
}
