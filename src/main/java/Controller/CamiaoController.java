package Controller;

import DataStructure.ListaDuplamenteLigada;
import model.Camiao;

public class CamiaoController implements controller<Camiao> {
    ListaDuplamenteLigada<Camiao> camiaoListaDuplamenteLigada = new ListaDuplamenteLigada<>();

    @Override
    public void registar(Camiao camiao) {
        camiaoListaDuplamenteLigada.inserirFim(camiao);

    }

    @Override
    public void atualizar(Camiao camiao) {

    }

    @Override
    public Camiao pesquisar(String t) {
        return null;
    }

    @Override
    public ListaDuplamenteLigada<Camiao> listar() {
    }

    @Override
    public void remover(Camiao camiao) {

    }
}
