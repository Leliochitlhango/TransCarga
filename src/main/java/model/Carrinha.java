package model;



public class Carrinha extends Veiculo {
    
    private int passageiros;
    private String TipCarroceria;

    public Carrinha(String matricula, int id, int contacto, int documentoIdentifcacao, int passageiros) {
        this.matricula = matricula;
        this.id = id;
        this.passageiros = passageiros;
    }

    public Carrinha(String matricula, int contacto, int documentoIdentifcacao, int passageiros) {
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
