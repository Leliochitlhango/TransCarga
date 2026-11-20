package dataStructure;

public class No <T>{
    private T valor;
    private No<T> anterior;
    private No<T> posterior;


    public No(T valor) {
        this.valor = valor;
        this.anterior =null ;
        this.posterior = null;
    }

    public T getValor() {
        return valor;
    }

    public void setValor(T valor) {
        this.valor = valor;
    }

    public No<T> getAnterior() {
        return anterior;
    }

    public void setAnterior(No<T> anterior) {
        this.anterior = anterior;
    }

    public No<T> getPosterior() {
        return posterior;
    }

    public void setPosterior(No<T> posterior) {
        this.posterior = posterior;
    }
}
