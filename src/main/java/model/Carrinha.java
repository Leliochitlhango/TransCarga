package model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("CARRINHA")
public class Carrinha extends Veiculo {

    @Column(name = "passageiros")
    private Integer passageiros;

    @Column(name = "tipo_carroceria")
    private String tipoCarroceria;

    public Carrinha() {
    }

    public Carrinha(String matricula, String documentoIdentifcacao, Integer passageiros) {
        this.matricula = matricula;
        this.documentoIdentifcacao = documentoIdentifcacao;
        this.passageiros = passageiros;
    }

    public String getTipoCarroceria() {
        return tipoCarroceria;
    }

    public void setTipoCarroceria(String tipoCarroceria) {
        this.tipoCarroceria = tipoCarroceria;
    }

    public Integer getPassageiros() {
        return passageiros;
    }

    public void setPassageiros(Integer passageiros) {
        this.passageiros = passageiros;
    }
}