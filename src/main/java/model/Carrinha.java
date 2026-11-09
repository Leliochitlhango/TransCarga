package model;

import java.time.LocalDate;
import java.util.Objects;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "carrinhas")
public class Carrinha extends Veiculo {
    
    @Column(nullable = true)
    private int passageiros;
    
    @Column(nullable = true)
    private String TipCarroceria;

    public Carrinha(String matricula, int id, int documentoIdentifcacao, int passageiros) {
        this.matricula = matricula;
        this.id = id;
        this.passageiros = passageiros;
    }

    public Carrinha() {
            
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
