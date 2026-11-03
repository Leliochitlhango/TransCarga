package model;

import java.time.LocalDate;
import java.util.Objects;

public class carregamento {
    private String id;
    private veiculo veiculo;
    private String localOrigem;
    private LocalDate Data;
    private double valorCobrado;
    private String LocaDestino;
    private cliente cliente;


    public carregamento(String id, veiculo veiculo, String localOrigem, LocalDate data, double valorCobrado, String locaDestino, cliente cliente) {
        this.id = id;
        this.veiculo = veiculo;
        this.localOrigem = localOrigem;
        Data = data;
        this.valorCobrado = valorCobrado;
        LocaDestino = locaDestino;
        this.cliente = cliente;
    }

    public carregamento() {
    }

    public cliente getCliente() {
        return cliente;
    }

    public void setCliente(cliente cliente) {
        this.cliente = cliente;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(veiculo veiculo) {
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
        carregamento that = (carregamento) o;
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
