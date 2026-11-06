package controller;

import dataStructure.ListaDuplamenteLigada;

public interface controller <T>{
    void registar(T t);
    void atualizar(T t);
    T pesquisar(String t);
    ListaDuplamenteLigada<T> listar();
     void remover(T t);

}
