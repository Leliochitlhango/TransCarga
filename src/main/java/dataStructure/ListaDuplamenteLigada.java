package dataStructure;

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
        if (inicio==null)
            return  null;

        T elemento= inicio.getValor();
        if (tamanho == 0){
                inicio = null;
                fim = null;
        }else {
            inicio = inicio.getPosterior();
            inicio.setAnterior(null);
            tamanho--;
        }
        return elemento;
    }

    @Override
    public T removerFim() {
        if (inicio==null)
            return  null;
        T elemento= fim.getValor();
        if (tamanho == 0) {
            inicio = null;
            fim = null;
        }else {
            fim = fim.getAnterior();
            fim.setPosterior(null);
            tamanho--;
        }
        return  elemento;
    }

    @Override
    public T remover(int posicao) {
        No<T> atual=inicio;
        if ((posicao<0)||(posicao>=tamanho))
            throw new IndexOutOfBoundsException();
        if (posicao == tamanho-1)
            return removerFim();
        else
            if (posicao==0)
                return removerInicio();
            else {
                for (int i = 0; i < posicao; i++) {
                    atual=atual.getPosterior();
                }
                T elemento=atual.getValor();
                No<T> posteiror=atual.getPosterior();
                No<T> anterior=atual.getAnterior();
                posteiror.setAnterior(anterior);
                anterior.setPosterior(posteiror);
                tamanho--;
                return elemento;
            }


    }

    @Override
    public T pesquisar(T t) {

        No<T> atual = inicio;

        while (atual != null) {

            if (atual.getValor().equals(t)) {
                return atual.getValor();
            }

            atual = atual.getPosterior();
        }

        return null;
    }

    @Override
    public T obter(int posicao) {
        No<T> atual=inicio;
        if ((posicao<0)||(posicao>tamanho))
            throw new IndexOutOfBoundsException();
        if (posicao == tamanho)
            return fim.getValor();
        else
            if (posicao==0)
                return inicio.getValor();
            else  {
                for (int i = 0; i < posicao; i++) {
                    atual=atual.getPosterior();
                }
                return  atual.getValor();
            }

    }

    @Override
    public boolean contem(T t) {
        return pesquisar(t)!=null;
    }


    @Override
    public boolean estaVazia() {
        return  inicio==null;
    }

    @Override
    public void actualizar(int posicao, T t) {
        No<T> atual=inicio;
        if ((posicao<0)||(posicao>tamanho))
            throw new IndexOutOfBoundsException();
        if (posicao == tamanho)
           fim.setValor(t);
        else
        if (posicao==0)
            inicio.setValor(t);
        else {
            for (int i = 0; i < posicao; i++) {
                atual = atual.getPosterior();
            }
            atual.setValor(t);
        }
    }

    @Override
    public void inverter() {
        No<T> atual=inicio;
        No<T> temporario=null;
        while (atual!=null) {
            temporario=atual.getAnterior();
            atual.setAnterior(atual.getPosterior());
            atual.setPosterior(temporario);
            atual=atual.getAnterior();
        }
        temporario=inicio;
        inicio=fim;
        fim=temporario;
    }

    @Override
    public void limpar() {
        inicio=null;
        fim=null;
        tamanho=0;
    }
}


