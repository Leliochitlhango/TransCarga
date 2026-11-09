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
@Table(name = "carregamentos")
public class Carregamento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "veiculo_id", nullable = false)
    private Veiculo veiculo;
    
    @Column(nullable = false, length=100)
    private String localOrigem;
    
    @Column(nullable = false)
    private LocalDate Data;
    
    @Column(nullable = false)
    private double valorCobrado;
    
    @Column(nullable = false, length=100)
    private String LocaDestino;
    
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;


    public Carregamento(int id, Veiculo veiculo, String localOrigem, LocalDate data, double valorCobrado, String locaDestino, Cliente cliente) {
        this.id = id;
        this.veiculo = veiculo;
        this.localOrigem = localOrigem;
        Data = data;
        this.valorCobrado = valorCobrado;
        LocaDestino = locaDestino;
        this.cliente = cliente;
    }

    public Carregamento() {
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public String getLocalOrigem() {
        return localOrigem;
    }

    public void setLocalOrigem(String localOrigem) {
        this.localOrigem = localOrigem;
    }

    public LocalDate getData() {
        return Data;
    }

    public void setData(LocalDate data) {
        Data = data;
    }

    public double getValorCobrado() {
        return valorCobrado;
    }

    public void setValorCobrado(double valorCobrado) {
        this.valorCobrado = valorCobrado;
    }

    public String getLocaDestino() {
        return LocaDestino;
    }

    public void setLocaDestino(String locaDestino) {
        LocaDestino = locaDestino;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Carregamento that = (Carregamento) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "carregamento{" +
                "id='" + id + '\'' +
                ", veiculo=" + veiculo +
                ", localOrigem='" + localOrigem + '\'' +
                ", Data=" + Data +
                ", valorCobrado=" + valorCobrado +
                ", LocaDestino='" + LocaDestino + '\'' +
                '}';
    }
}
