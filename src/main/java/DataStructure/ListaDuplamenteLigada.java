package DataStructure;

import model.Camiao;

import java.util.Objects;

public class ListaDuplamenteLigada <T> implements ListaDuplamenteLigada1<T>{
    private No<T> inicio;
    private No<T> fim;
    private int tamanho;


    public ListaDuplamenteLigada() {
        this.inicio = null;
        this.fim = null;
        this.tamanho = 0;
    }

    public No<T> getInicio() {
        return inicio;
    }

    public void setInicio(No<T> inicio) {
        this.inicio = inicio;
    }

    public No<T> getFim() {
        return fim;
    }

    public void setFim(No<T> fim) {
        this.fim = fim;
    }

    public int getTamanho() {
        return tamanho;
    }

    public void setTamanho(int tamanho) {
        this.tamanho = tamanho;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ListaDuplamenteLigada<?> that = (ListaDuplamenteLigada<?>) o;
        return getTamanho() == that.getTamanho() && Objects.equals(getInicio(), that.getInicio()) && Objects.equals(getFim(), that.getFim());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getInicio(), getFim(), getTamanho());
    }


    @Override
    public void inserirInicio(T t) {
        No<T> no=new No<>(t);
        if (inicio==null) {
            inicio = no;
            fim = no;
        }else{
            no.setPosterior(inicio);
            inicio.setAnterior(no);
            inicio=no;
        }
        tamanho++;
    }

    @Override
    public void inserirFim(T t) {
        No<T> no=new No<>(t);
        if (inicio==null) {
            fim=no;
            inicio=no;
        }else {
            fim.setPosterior(no);
            no.setAnterior(fim);
            fim=no;
        }
        tamanho++;

    }

    @Override
    public void inserir(int posicao, T t) {
        No<T> atual=inicio;
        No<T> no=new No<>(t);
        if ((posicao<0)||(posicao>tamanho))
            throw new IndexOutOfBoundsException();
        if (posicao == tamanho)
            inserirFim(t);
        else
            if (posicao==0)
                inserirInicio(t);
        else{
            for (int i = 0; i < posicao; i++) {
                    atual=atual.getPosterior();
            }
                No<T> anterior=atual.getAnterior();
                anterior.setPosterior(no);
                no.setAnterior(anterior);
                atual.setAnterior(no);
                no.setPosterior(atual);
                tamanho++;
            }
    }

    @Override
    public T removerInicio() {
        if (inicio==null) {
            return  null;
        }else {
            if (tamanho == 0){
                inicio = null;
                fim = null;
        }else




            tamanho--;
        }
    }

    @Override
    public T removerFim() {
        return null;
    }

    @Override
    public T remover(int posicao) {
        return null;
    }

    @Override
    public T pesquisar(T t) {
        return null;
    }

    @Override
    public T obter(int posicao) {
        return null;
    }

    @Override
    public boolean contem(T t) {
        return false;
    }

    @Override
    public void percorrerInicio() {

    }

    @Override
    public void percorrerFim() {

    }

    @Override
    public T primeiro() {
        return null;
    }

    @Override
    public T ultimo() {
        return null;
    }

    @Override
    public int tamanho() {
        return 0;
    }

    @Override
    public boolean estaVazia() {
        return false;
    }

    @Override
    public void actualizar(int posicao, T t) {

    }

    @Override
    public void inverter() {

    }

    @Override
    public void limpar() {

    }
}
