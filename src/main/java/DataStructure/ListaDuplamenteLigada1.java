package DataStructure;

public interface ListaDuplamenteLigada1 <T> {
    void inserirInicio(T t);
    void inserirFim(T t);
    void inserir(int posicao, T t);


    T removerInicio();
    T removerFim();
    T remover(int posicao) ;

    T pesquisar(T t);
    T obter(int posicao);
    boolean contem(T t);
    boolean estaVazia();
    void actualizar(int posicao, T t);
    void inverter();
    void limpar();


}
