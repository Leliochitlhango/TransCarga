package dataStructure;

import java.util.AbstractList;
import java.util.Collection;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Predicate;

public class ListaDuplamenteLigada<T> extends AbstractList<T> implements ListaDuplamenteLigada1<T>, Iterable<T> {

    private No<T> inicio;
    private No<T> fim;
    private int tamanho;

    public ListaDuplamenteLigada() {
        this.inicio = null;
        this.fim = null;
        this.tamanho = 0;
    }

    public ListaDuplamenteLigada(Collection<? extends T> colecao) {
        this();
        if (colecao != null) {
            for (T item : colecao) {
                inserirFim(item);
            }
        }
    }

    public static <E> ListaDuplamenteLigada<E> de(Collection<? extends E> colecao) {
        ListaDuplamenteLigada<E> lista = new ListaDuplamenteLigada<>();
        if (colecao != null) {
            for (E item : colecao) {
                lista.inserirFim(item);
            }
        }
        return lista;
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
        modCount++;
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
        modCount++;
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
            modCount++;
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
        if (inicio == null) {
            throw new NoSuchElementException("Lista vazia");
        }
        modCount++;
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
        if (fim == null) {
            throw new NoSuchElementException("Lista vazia");
        }
        modCount++;
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
            modCount++;
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
        No<T> atual = inicio;
        while (atual != null) {
            if (Objects.equals(atual.getValor(), t)) {
                return true;
            }
            atual = atual.getPosterior();
        }
        return false;
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
        modCount++;
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
        modCount++;
        inicio = null;
        fim = null;
        tamanho = 0;
    }

    public void ordenar(Comparator<? super T> comparator) {
        if (tamanho <= 1) return;
        modCount++;
        inicio = mergeSort(inicio, comparator);
        No<T> atual = inicio;
        while (atual != null && atual.getPosterior() != null) {
            atual = atual.getPosterior();
        }
        fim = atual;
    }

    private No<T> mergeSort(No<T> no, Comparator<? super T> comparator) {
        if (no == null || no.getPosterior() == null) {
            return no;
        }
        No<T> segundo = dividir(no);
        no = mergeSort(no, comparator);
        segundo = mergeSort(segundo, comparator);
        return fundir(no, segundo, comparator);
    }

    private No<T> dividir(No<T> cabeca) {
        No<T> rapido = cabeca;
        No<T> lento = cabeca;
        while (rapido.getPosterior() != null && rapido.getPosterior().getPosterior() != null) {
            rapido = rapido.getPosterior().getPosterior();
            lento = lento.getPosterior();
        }
        No<T> segundaMetade = lento.getPosterior();
        lento.setPosterior(null);
        if (segundaMetade != null) {
            segundaMetade.setAnterior(null);
        }
        return segundaMetade;
    }

    private No<T> fundir(No<T> primeiro, No<T> segundo, Comparator<? super T> comparator) {
        if (primeiro == null) return segundo;
        if (segundo == null) return primeiro;

        No<T> cabeca;
        if (comparator.compare(primeiro.getValor(), segundo.getValor()) <= 0) {
            cabeca = primeiro;
            primeiro = primeiro.getPosterior();
        } else {
            cabeca = segundo;
            segundo = segundo.getPosterior();
        }
        cabeca.setAnterior(null);

        No<T> atual = cabeca;
        while (primeiro != null && segundo != null) {
            if (comparator.compare(primeiro.getValor(), segundo.getValor()) <= 0) {
                atual.setPosterior(primeiro);
                primeiro.setAnterior(atual);
                primeiro = primeiro.getPosterior();
            } else {
                atual.setPosterior(segundo);
                segundo.setAnterior(atual);
                segundo = segundo.getPosterior();
            }
            atual = atual.getPosterior();
        }

        if (primeiro != null) {
            atual.setPosterior(primeiro);
            primeiro.setAnterior(atual);
        } else if (segundo != null) {
            atual.setPosterior(segundo);
            segundo.setAnterior(atual);
        }

        return cabeca;
    }

    public ListaDuplamenteLigada<T> filtrar(Predicate<? super T> predicate) {
        ListaDuplamenteLigada<T> resultado = new ListaDuplamenteLigada<>();
        for (T item : this) {
            if (predicate.test(item)) {
                resultado.inserirFim(item);
            }
        }
        return resultado;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private No<T> atual = inicio;
            private No<T> ultimoRetornado = null;
            private int expectedModCount = modCount;

            final void checkForComodification() {
                if (modCount != expectedModCount) {
                    throw new ConcurrentModificationException();
                }
            }

            @Override
            public boolean hasNext() {
                checkForComodification();
                return atual != null;
            }

            @Override
            public T next() {
                checkForComodification();
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                ultimoRetornado = atual;
                T valor = atual.getValor();
                atual = atual.getPosterior();
                return valor;
            }

            @Override
            public void remove() {
                checkForComodification();
                if (ultimoRetornado == null) {
                    throw new IllegalStateException();
                }
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
                ultimoRetornado = null;
                modCount++;
                expectedModCount = modCount;
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
            private int expectedModCount = modCount;

            final void checkForComodification() {
                if (modCount != expectedModCount) {
                    throw new ConcurrentModificationException();
                }
            }

            @Override
            public boolean hasNext() {
                checkForComodification();
                return proximoIndice < tamanho;
            }

            @Override
            public T next() {
                checkForComodification();
                if (!hasNext()) throw new NoSuchElementException();
                ultimoRetornado = proximoNo;
                T valor = proximoNo.getValor();
                proximoNo = proximoNo.getPosterior();
                proximoIndice++;
                return valor;
            }

            @Override
            public boolean hasPrevious() {
                checkForComodification();
                return proximoIndice > 0;
            }

            @Override
            public T previous() {
                checkForComodification();
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
                checkForComodification();
                return proximoIndice;
            }

            @Override
            public int previousIndex() {
                checkForComodification();
                return proximoIndice - 1;
            }

            @Override
            public void remove() {
                checkForComodification();
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
                modCount++;
                expectedModCount = modCount;
            }

            @Override
            public void set(T t) {
                checkForComodification();
                if (ultimoRetornado == null) throw new IllegalStateException();
                ultimoRetornado.setValor(t);
            }

            @Override
            public void add(T t) {
                checkForComodification();
                inserir(proximoIndice, t);
                proximoIndice++;
                ultimoRetornado = null;
                expectedModCount = modCount;
            }
        };
    }
}
