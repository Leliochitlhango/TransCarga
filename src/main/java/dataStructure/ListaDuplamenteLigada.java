package dataStructure;

import java.util.AbstractList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Predicate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ListaDuplamenteLigada<T> extends AbstractList<T> implements ListaDuplamenteLigada1<T>, Iterable<T> {

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

    // Métodos obrigatórios do AbstractList / java.util.List
    @Override
    public int size() {
        return tamanho;
    }

    @Override
    public T get(int index) {
        return obter(index);
    }

    @Override
    public T set(int index, T element) {
        if (index < 0 || index >= tamanho) {
            throw new IndexOutOfBoundsException("Índice: " + index + ", Tamanho: " + tamanho);
        }
        No<T> atual = obterNo(index);
        T antigo = atual.getValor();
        atual.setValor(element);
        return antigo;
    }

    @Override
    public void add(int index, T element) {
        inserir(index, element);
    }

    @Override
    public T remove(int index) {
        return remover(index);
    }

    @Override
    public void clear() {
        limpar();
    }

    @Override
    public boolean add(T t) {
        inserirFim(t);
        return true;
    }

    // Métodos da Interface ListaDuplamenteLigada1
    @Override
    public void inserirInicio(T t) {
        No<T> no = new No<>(t);
        if (inicio == null) {
            inicio = no;
            fim = no;
        } else {
            no.setPosterior(inicio);
            inicio.setAnterior(no);
            inicio = no;
        }
        tamanho++;
    }

    @Override
    public void inserirFim(T t) {
        No<T> no = new No<>(t);
        if (inicio == null) {
            inicio = no;
            fim = no;
        } else {
            fim.setPosterior(no);
            no.setAnterior(fim);
            fim = no;
        }
        tamanho++;
    }

    @Override
    public void inserir(int posicao, T t) {
        if (posicao < 0 || posicao > tamanho) {
            throw new IndexOutOfBoundsException("Posição: " + posicao + ", Tamanho: " + tamanho);
        }
        if (posicao == 0) {
            inserirInicio(t);
        } else if (posicao == tamanho) {
            inserirFim(t);
        } else {
            No<T> atual = obterNo(posicao);
            No<T> novo = new No<>(t);
            No<T> anterior = atual.getAnterior();

            anterior.setPosterior(novo);
            novo.setAnterior(anterior);
            novo.setPosterior(atual);
            atual.setAnterior(novo);
            tamanho++;
        }
    }

    @Override
    public T removerInicio() {
        if (inicio == null) return null;
        T elemento = inicio.getValor();
        if (inicio == fim) {
            inicio = null;
            fim = null;
        } else {
            inicio = inicio.getPosterior();
            inicio.setAnterior(null);
        }
        tamanho--;
        return elemento;
    }

    @Override
    public T removerFim() {
        if (fim == null) return null;
        T elemento = fim.getValor();
        if (inicio == fim) {
            inicio = null;
            fim = null;
        } else {
            fim = fim.getAnterior();
            fim.setPosterior(null);
        }
        tamanho--;
        return elemento;
    }

    @Override
    public T remover(int posicao) {
        if (posicao < 0 || posicao >= tamanho) {
            throw new IndexOutOfBoundsException("Posição: " + posicao + ", Tamanho: " + tamanho);
        }
        if (posicao == 0) {
            return removerInicio();
        } else if (posicao == tamanho - 1) {
            return removerFim();
        } else {
            No<T> atual = obterNo(posicao);
            T elemento = atual.getValor();
            No<T> anterior = atual.getAnterior();
            No<T> posterior = atual.getPosterior();

            anterior.setPosterior(posterior);
            posterior.setAnterior(anterior);
            tamanho--;
            return elemento;
        }
    }

    @Override
    public T pesquisar(T t) {
        No<T> atual = inicio;
        while (atual != null) {
            if (Objects.equals(atual.getValor(), t)) {
                return atual.getValor();
            }
            atual = atual.getPosterior();
        }
        return null;
    }

    @Override
    public T obter(int posicao) {
        if (posicao < 0 || posicao >= tamanho) {
            throw new IndexOutOfBoundsException("Posição: " + posicao + ", Tamanho: " + tamanho);
        }
        return obterNo(posicao).getValor();
    }

    /**
     * Otimização de navegação duplamente ligada:
     * Se a posição estiver na primeira metade, navega a partir do início.
     * Se estiver na segunda metade, navega a partir do fim!
     */
    private No<T> obterNo(int posicao) {
        if (posicao < 0 || posicao >= tamanho) {
            throw new IndexOutOfBoundsException("Posição: " + posicao + ", Tamanho: " + tamanho);
        }

        if (posicao < (tamanho / 2)) {
            No<T> atual = inicio;
            for (int i = 0; i < posicao; i++) {
                atual = atual.getPosterior();
            }
            return atual;
        } else {
            No<T> atual = fim;
            for (int i = tamanho - 1; i > posicao; i--) {
                atual = atual.getAnterior();
            }
            return atual;
        }
    }

    @Override
    public boolean contem(T t) {
        return pesquisar(t) != null;
    }

    @Override
    public boolean estaVazia() {
        return tamanho == 0;
    }

    @Override
    public void actualizar(int posicao, T t) {
        if (posicao < 0 || posicao >= tamanho) {
            throw new IndexOutOfBoundsException("Posição: " + posicao + ", Tamanho: " + tamanho);
        }
        obterNo(posicao).setValor(t);
    }

    @Override
    public void inverter() {
        No<T> atual = inicio;
        No<T> temp = null;
        while (atual != null) {
            temp = atual.getAnterior();
            atual.setAnterior(atual.getPosterior());
            atual.setPosterior(temp);
            atual = atual.getAnterior();
        }
        if (temp != null) {
            fim = inicio;
            inicio = temp.getAnterior();
        }
    }

    @Override
    public void limpar() {
        inicio = null;
        fim = null;
        tamanho = 0;
    }

    /**
     * Algoritmo de Ordenação na Lista Duplamente Ligada (Bubble Sort de nós).
     */
    public void ordenar(Comparator<? super T> comparator) {
        if (tamanho <= 1) return;
        boolean trocou;
        do {
            trocou = false;
            No<T> atual = inicio;
            while (atual != null && atual.getPosterior() != null) {
                if (comparator.compare(atual.getValor(), atual.getPosterior().getValor()) > 0) {
                    T temp = atual.getValor();
                    atual.setValor(atual.getPosterior().getValor());
                    atual.getPosterior().setValor(temp);
                    trocou = true;
                }
                atual = atual.getPosterior();
            }
        } while (trocou);
    }

    /**
     * Filtragem funcional retornando nova ListaDuplamenteLigada.
     */
    public ListaDuplamenteLigada<T> filtrar(Predicate<? super T> predicate) {
        ListaDuplamenteLigada<T> resultado = new ListaDuplamenteLigada<>();
        for (T item : this) {
            if (predicate.test(item)) {
                resultado.inserirFim(item);
            }
        }
        return resultado;
    }

    /**
     * Retorna visualização ObservableList para TableView do JavaFX
     * suportada 100% diretamente por esta ListaDuplamenteLigada.
     */
    public ObservableList<T> asObservableList() {
        return FXCollections.observableList(this);
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private No<T> atual = inicio;

            @Override
            public boolean hasNext() {
                return atual != null;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                T valor = atual.getValor();
                atual = atual.getPosterior();
                return valor;
            }
        };
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        if (index < 0 || index > tamanho) {
            throw new IndexOutOfBoundsException("Índice: " + index + ", Tamanho: " + tamanho);
        }

        return new ListIterator<T>() {
            private No<T> proximoNo = (index == tamanho) ? null : obterNo(index);
            private No<T> ultimoRetornado = null;
            private int proximoIndice = index;

            @Override
            public boolean hasNext() {
                return proximoIndice < tamanho;
            }

            @Override
            public T next() {
                if (!hasNext()) throw new NoSuchElementException();
                ultimoRetornado = proximoNo;
                T valor = proximoNo.getValor();
                proximoNo = proximoNo.getPosterior();
                proximoIndice++;
                return valor;
            }

            @Override
            public boolean hasPrevious() {
                return proximoIndice > 0;
            }

            @Override
            public T previous() {
                if (!hasPrevious()) throw new NoSuchElementException();
                if (proximoNo == null) {
                    proximoNo = fim;
                } else {
                    proximoNo = proximoNo.getAnterior();
                }
                ultimoRetornado = proximoNo;
                proximoIndice--;
                return ultimoRetornado.getValor();
            }

            @Override
            public int nextIndex() {
                return proximoIndice;
            }

            @Override
            public int previousIndex() {
                return proximoIndice - 1;
            }

            @Override
            public void remove() {
                if (ultimoRetornado == null) throw new IllegalStateException();
                No<T> anterior = ultimoRetornado.getAnterior();
                No<T> posterior = ultimoRetornado.getPosterior();

                if (anterior == null) {
                    inicio = posterior;
                } else {
                    anterior.setPosterior(posterior);
                }

                if (posterior == null) {
                    fim = anterior;
                } else {
                    posterior.setAnterior(anterior);
                }

                tamanho--;
                if (proximoNo == ultimoRetornado) {
                    proximoNo = posterior;
                } else {
                    proximoIndice--;
                }
                ultimoRetornado = null;
            }

            @Override
            public void set(T t) {
                if (ultimoRetornado == null) throw new IllegalStateException();
                ultimoRetornado.setValor(t);
            }

            @Override
            public void add(T t) {
                inserir(proximoIndice, t);
                proximoIndice++;
                ultimoRetornado = null;
            }
        };
    }
}
