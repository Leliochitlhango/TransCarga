package controller;

import dao.CamiaoDAO;
import dao.CarrinhaDAO;
import dao.CarregamentoDAO;
import dao.ProprietarioDAO;
import dataStructure.ListaDuplamenteLigada;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import model.Camiao;
import model.Carrinha;
import model.Carregamento;
import model.Proprietario;
import model.Veiculo;
import util.Validador;

public class VeiculoViewController {

    private static final Logger LOGGER = Logger.getLogger(VeiculoViewController.class.getName());

    public static final String APLICACAO_MERCADORIA = "Transporte de Mercadoria";
    public static final String APLICACAO_PASSAGEIROS = "Transporte de Passageiros";

    private final ListaDuplamenteLigada<Veiculo> dadosTabela = new ListaDuplamenteLigada<>();
    private final ObservableList<Veiculo> observavel = FXCollections.observableArrayList();
    private ListaDuplamenteLigada<Camiao> camioes;
    private ListaDuplamenteLigada<Carrinha> carrinhas;
    private ListaDuplamenteLigada<Proprietario> proprietarios;
    private ListaDuplamenteLigada<Carregamento> todosCarregamentos;

    @FXML private Button btnActualizar;
    @FXML private Button btnEliminar;
    @FXML private Button btnGuardar;
    @FXML private Button btnLimpar;
    @FXML private Button btnFiltroTodos;
    @FXML private Button btnFiltroMercadoria;
    @FXML private Button btnFiltroPassageiros;

    @FXML private ComboBox<String> cbAplicacao;
    @FXML private ComboBox<String> cbProprietario;

    @FXML private TextField txtMatricula;
    @FXML private TextField txtDocumento;
    @FXML private Label lblDocumento;

    @FXML private VBox boxMercadoria;
    @FXML private TextField txtCapacidadeCarga;
    @FXML private TextField txtTipoMercadoria;

    @FXML private VBox boxPassageiros;
    @FXML private TextField txtPassageiros;
    @FXML private TextField txtTipoCarroceria;

    @FXML private Label lblMensagem;
    @FXML private Label lblTotalFrota;
    @FXML private Label lblResumoTabela;

    @FXML private TableView<Veiculo> tabela;
    @FXML private TableColumn<Veiculo, String> colCapacidade;
    @FXML private TableColumn<Veiculo, String> colMarca;
    @FXML private TableColumn<Veiculo, String> colMatricula;
    @FXML private TableColumn<Veiculo, String> colModelo;
    @FXML private TableColumn<Veiculo, String> colProprietario;
    @FXML private TableColumn<Veiculo, String> colTipo;

    private int veiculoIdEmEdicao = 0;
    private String tipoVeiculoEmEdicao = ""; // "CAMIAO" ou "CARRINHA"
    private int filtroAtual = 0; // 0: Todos, 1: Mercadoria, 2: Passageiros

    @FXML
    public void initialize() {
        Validador.aplicarMascaraMatricula(txtMatricula);
        configurarComboAplicacao();
        configurarEventosValidacao();
        carregarDados();
        configurarTabela();
        configurarComboProprietarios();
        aplicarFiltroTabela();
    }

    private void configurarComboAplicacao() {
        if (cbAplicacao != null) {
            cbAplicacao.getItems().clear();
            cbAplicacao.getItems().addAll(APLICACAO_MERCADORIA, APLICACAO_PASSAGEIROS);
            cbAplicacao.setValue(APLICACAO_MERCADORIA);
            mudarAplicacao(null);
        }
    }

    private void carregarDados() {
        try {
            camioes = new CamiaoDAO().findAll();
            carrinhas = new CarrinhaDAO().findAll();
            proprietarios = new ProprietarioDAO().findAll();
            todosCarregamentos = new CarregamentoDAO().findAll();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao carregar veículos", e);
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao carregar dados dos veículos: " + e.getMessage());
            alert.setHeaderText("Erro");
            alert.showAndWait();
        }
    }

    private void configurarComboProprietarios() {
        if (cbProprietario == null) return;
        cbProprietario.getItems().clear();
        if (proprietarios != null) {
            for (Proprietario p : proprietarios) {
                if (p != null && p.getNome() != null) {
                    cbProprietario.getItems().add(p.getNome());
                }
            }
        }
    }

    private void configurarEventosValidacao() {
        if (txtMatricula != null) {
            txtMatricula.textProperty().addListener((obs, oldVal, newVal) -> {
                verificarMatriculaEmTempoReal();
            });
        }
        if (txtDocumento != null) {
            txtDocumento.textProperty().addListener((obs, oldVal, newVal) -> {
                verificarDocumentoEmTempoReal();
            });
        }
    }

    @FXML
    void mudarAplicacao(ActionEvent event) {
        String selecionado = cbAplicacao != null ? cbAplicacao.getValue() : APLICACAO_MERCADORIA;
        boolean isPassageiros = APLICACAO_PASSAGEIROS.equals(selecionado);

        if (boxMercadoria != null) {
            boxMercadoria.setVisible(!isPassageiros);
            boxMercadoria.setManaged(!isPassageiros);
        }
        if (boxPassageiros != null) {
            boxPassageiros.setVisible(isPassageiros);
            boxPassageiros.setManaged(isPassageiros);
        }
        if (lblDocumento != null) {
            lblDocumento.setText(isPassageiros ? "Documento de Identificação" : "Nº de Chassi / Documento de ID");
        }
    }

    private void verificarMatriculaEmTempoReal() {
        if (txtMatricula == null) return;
        String mat = txtMatricula.getText() != null ? txtMatricula.getText().trim().toUpperCase() : "";

        if (mat.isEmpty()) {
            txtMatricula.getStyleClass().remove("campo-erro");
            limparMensagemSeContem("matrícula");
            return;
        }

        Veiculo dup = encontrarVeiculoPorMatricula(mat);
        if (dup != null && dup.getId() != veiculoIdEmEdicao) {
            marcarErro(txtMatricula, "Esta matrícula já se encontra registada para outro veículo!");
            return;
        }

        if (mat.length() >= 10 && !Validador.isMatriculaValida(mat)) {
            marcarErro(txtMatricula, "Matrícula inválida. Formato moçambicano: AAA-000-XX (ex: ABC-123-MP).");
            return;
        }

        txtMatricula.getStyleClass().remove("campo-erro");
        limparMensagemSeContem("matrícula");
    }

    private void verificarDocumentoEmTempoReal() {
        if (txtDocumento == null) return;
        String doc = txtDocumento.getText() != null ? txtDocumento.getText().trim().toUpperCase() : "";

        if (doc.isEmpty()) {
            txtDocumento.getStyleClass().remove("campo-erro");
            limparMensagemSeContem("chassi");
            return;
        }

        Veiculo dup = encontrarVeiculoPorDocumento(doc);
        if (dup != null && dup.getId() != veiculoIdEmEdicao) {
            marcarErro(txtDocumento, "Este documento/chassi já se encontra registado para outro veículo!");
            return;
        }

        txtDocumento.getStyleClass().remove("campo-erro");
        limparMensagemSeContem("chassi");
    }

    private Veiculo encontrarVeiculoPorMatricula(String mat) {
        if (mat == null) return null;
        String matLimpa = mat.replaceAll("[^a-zA-Z0-9]", "").toUpperCase();

        if (camioes != null) {
            for (Camiao c : camioes) {
                if (c != null && c.getMatricula() != null) {
                    String cMat = c.getMatricula().replaceAll("[^a-zA-Z0-9]", "").toUpperCase();
                    if (!cMat.isEmpty() && cMat.equals(matLimpa)) return c;
                }
            }
        }
        if (carrinhas != null) {
            for (Carrinha c : carrinhas) {
                if (c != null && c.getMatricula() != null) {
                    String cMat = c.getMatricula().replaceAll("[^a-zA-Z0-9]", "").toUpperCase();
                    if (!cMat.isEmpty() && cMat.equals(matLimpa)) return c;
                }
            }
        }
        return null;
    }

    private Veiculo encontrarVeiculoPorDocumento(String doc) {
        if (doc == null) return null;
        String docLimpo = doc.replaceAll("\\s", "").toUpperCase();

        if (camioes != null) {
            for (Camiao c : camioes) {
                if (c != null && c.getDocumentoIdentifcacao() != null) {
                    String cDoc = c.getDocumentoIdentifcacao().replaceAll("\\s", "").toUpperCase();
                    if (!cDoc.isEmpty() && cDoc.equals(docLimpo)) return c;
                }
            }
        }
        if (carrinhas != null) {
            for (Carrinha c : carrinhas) {
                if (c != null && c.getDocumentoIdentifcacao() != null) {
                    String cDoc = c.getDocumentoIdentifcacao().replaceAll("\\s", "").toUpperCase();
                    if (!cDoc.isEmpty() && cDoc.equals(docLimpo)) return c;
                }
            }
        }
        return null;
    }

    private void limparMensagemSeContem(String termo) {
        if (lblMensagem != null && lblMensagem.getText().toLowerCase().contains(termo.toLowerCase())) {
            lblMensagem.setText("");
        }
    }

    private void marcarErro(TextField campo, String mensagem) {
        if (campo != null && !campo.getStyleClass().contains("campo-erro")) {
            campo.getStyleClass().add("campo-erro");
        }
        if (lblMensagem != null) {
            lblMensagem.setText(mensagem);
        }
    }

    private boolean validarCampos() {
        boolean temErro = false;

        String mat = txtMatricula.getText() != null ? txtMatricula.getText().trim().toUpperCase() : "";
        String doc = txtDocumento.getText() != null ? txtDocumento.getText().trim().toUpperCase() : "";
        String propNome = cbProprietario.getValue();
        String aplicacao = cbAplicacao.getValue();

        if (aplicacao == null) {
            if (lblMensagem != null) lblMensagem.setText("Seleccione a aplicação do veículo.");
            temErro = true;
        }

        if (mat.isEmpty()) {
            marcarErro(txtMatricula, "Por favor, introduza a matrícula.");
            temErro = true;
        } else if (!Validador.isMatriculaValida(mat)) {
            marcarErro(txtMatricula, "Matrícula inválida! Formato moçambicano esperado: AAA-000-XX (ex: ABC-123-MP).");
            temErro = true;
        } else {
            Veiculo dup = encontrarVeiculoPorMatricula(mat);
            if (dup != null && dup.getId() != veiculoIdEmEdicao) {
                marcarErro(txtMatricula, "Esta matrícula já se encontra registada!");
                temErro = true;
            }
        }

        if (doc.isEmpty()) {
            marcarErro(txtDocumento, "Por favor, introduza o chassi ou documento de identificação.");
            temErro = true;
        } else {
            Veiculo dup = encontrarVeiculoPorDocumento(doc);
            if (dup != null && dup.getId() != veiculoIdEmEdicao) {
                marcarErro(txtDocumento, "Este número de chassi já pertence a outro veículo!");
                temErro = true;
            }
        }

        if (APLICACAO_PASSAGEIROS.equals(aplicacao)) {
            String passStr = txtPassageiros.getText() != null ? txtPassageiros.getText().trim() : "";
            if (passStr.isEmpty()) {
                marcarErro(txtPassageiros, "Por favor, introduza o total de passageiros (lotação).");
                temErro = true;
            } else {
                try {
                    int p = Integer.parseInt(passStr);
                    if (p <= 0) {
                        marcarErro(txtPassageiros, "A lotação deve ser maior que zero.");
                        temErro = true;
                    }
                } catch (NumberFormatException e) {
                    marcarErro(txtPassageiros, "O total de passageiros deve ser um número inteiro.");
                    temErro = true;
                }
            }
        } else {
            String capStr = txtCapacidadeCarga.getText() != null ? txtCapacidadeCarga.getText().trim() : "";
            if (capStr.isEmpty()) {
                marcarErro(txtCapacidadeCarga, "Por favor, introduza a capacidade de carga em toneladas.");
                temErro = true;
            } else {
                try {
                    int cap = Integer.parseInt(capStr);
                    if (cap <= 0) {
                        marcarErro(txtCapacidadeCarga, "A capacidade de carga deve ser superior a zero.");
                        temErro = true;
                    }
                } catch (NumberFormatException e) {
                    marcarErro(txtCapacidadeCarga, "Capacidade deve ser um número inteiro de toneladas.");
                    temErro = true;
                }
            }
        }

        if (propNome == null || propNome.trim().isEmpty()) {
            if (lblMensagem != null) lblMensagem.setText("Por favor, seleccione o proprietário do veículo.");
            temErro = true;
        }

        return !temErro;
    }

    private Proprietario buscarProprietarioSelecionado() {
        String nome = cbProprietario.getValue();
        if (nome == null || proprietarios == null) return null;
        for (Proprietario p : proprietarios) {
            if (p != null && nome.equals(p.getNome())) return p;
        }
        return null;
    }

    @FXML
    void guardar(ActionEvent event) {
        veiculoIdEmEdicao = 0;
        if (!validarCampos()) return;

        try {
            Proprietario p = buscarProprietarioSelecionado();
            String mat = txtMatricula.getText().trim().toUpperCase();
            String doc = txtDocumento.getText().trim().toUpperCase();
            String aplicacao = cbAplicacao.getValue();

            if (APLICACAO_PASSAGEIROS.equals(aplicacao)) {
                Carrinha carrinha = new Carrinha();
                carrinha.setMatricula(mat);
                carrinha.setDocumentoIdentifcacao(doc);
                int passageiros = Integer.parseInt(txtPassageiros.getText().trim());
                carrinha.setPassageiros(passageiros);
                carrinha.setCapacidadeCarga(Math.max(1, passageiros / 10)); // estimativa em ton
                carrinha.setTipoCarroceria(txtTipoCarroceria.getText() != null ? txtTipoCarroceria.getText().trim() : "Passageiros");
                carrinha.setProprietario(p);

                new CarrinhaDAO().save(carrinha);
            } else {
                Camiao camiao = new Camiao();
                camiao.setMatricula(mat);
                camiao.setDocumentoIdentifcacao(doc);
                int cap = Integer.parseInt(txtCapacidadeCarga.getText().trim());
                camiao.setCapacidadeCarga(cap);
                camiao.setTipoMercadoria(txtTipoMercadoria.getText() != null ? txtTipoMercadoria.getText().trim() : "Geral");
                camiao.setProprietario(p);

                new CamiaoDAO().save(camiao);
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Veículo registado com sucesso na frota!");
            alert.setHeaderText("Sucesso");
            alert.showAndWait();

            carregarDados();
            aplicarFiltroTabela();
            limpar(null);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao guardar veículo", e);
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao guardar veículo: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void actualizar(ActionEvent event) {
        if (veiculoIdEmEdicao == 0) return;
        if (!validarCampos()) return;

        try {
            Proprietario p = buscarProprietarioSelecionado();
            String mat = txtMatricula.getText().trim().toUpperCase();
            String doc = txtDocumento.getText().trim().toUpperCase();
            String aplicacao = cbAplicacao.getValue();

            if (APLICACAO_PASSAGEIROS.equals(aplicacao)) {
                Carrinha carrinha = new Carrinha();
                carrinha.setId(veiculoIdEmEdicao);
                carrinha.setMatricula(mat);
                carrinha.setDocumentoIdentifcacao(doc);
                int passageiros = Integer.parseInt(txtPassageiros.getText().trim());
                carrinha.setPassageiros(passageiros);
                carrinha.setCapacidadeCarga(Math.max(1, passageiros / 10));
                carrinha.setTipoCarroceria(txtTipoCarroceria.getText() != null ? txtTipoCarroceria.getText().trim() : "Passageiros");
                carrinha.setProprietario(p);

                new CarrinhaDAO().update(carrinha);
            } else {
                Camiao camiao = new Camiao();
                camiao.setId(veiculoIdEmEdicao);
                camiao.setMatricula(mat);
                camiao.setDocumentoIdentifcacao(doc);
                int cap = Integer.parseInt(txtCapacidadeCarga.getText().trim());
                camiao.setCapacidadeCarga(cap);
                camiao.setTipoMercadoria(txtTipoMercadoria.getText() != null ? txtTipoMercadoria.getText().trim() : "Geral");
                camiao.setProprietario(p);

                new CamiaoDAO().update(camiao);
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Dados do veículo actualizados com sucesso!");
            alert.setHeaderText("Sucesso");
            alert.showAndWait();

            carregarDados();
            aplicarFiltroTabela();
            limpar(null);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao actualizar veículo", e);
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao actualizar veículo: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void eliminar(ActionEvent event) {
        if (veiculoIdEmEdicao == 0) return;

        // Verificar se há carregamentos registados para este veículo
        if (todosCarregamentos != null) {
            for (Carregamento c : todosCarregamentos) {
                if (c != null && c.getVeiculo() != null && c.getVeiculo().getId() == veiculoIdEmEdicao) {
                    Alert alertBloqueio = new Alert(Alert.AlertType.WARNING,
                            "Não é possível eliminar este veículo pois existem viagens/carregamentos registados para ele.");
                    alertBloqueio.setHeaderText("Operação Bloqueada");
                    alertBloqueio.showAndWait();
                    return;
                }
            }
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION,
                "Tem certeza que deseja eliminar este veículo da frota?", ButtonType.YES, ButtonType.NO);
        confirmacao.setHeaderText("Confirmação de Eliminação");
        Optional<ButtonType> resp = confirmacao.showAndWait();

        if (resp.isPresent() && resp.get() == ButtonType.YES) {
            try {
                boolean removido = false;
                if ("CARRINHA".equals(tipoVeiculoEmEdicao)) {
                    Carrinha c = new Carrinha();
                    c.setId(veiculoIdEmEdicao);
                    removido = new CarrinhaDAO().remove(c);
                } else {
                    Camiao c = new Camiao();
                    c.setId(veiculoIdEmEdicao);
                    removido = new CamiaoDAO().remove(c);
                }

                if (removido) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Veículo eliminado da frota com sucesso!");
                    alert.setHeaderText("Sucesso");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Não foi possível encontrar o veículo para remoção.");
                    alert.setHeaderText("Erro");
                    alert.showAndWait();
                }

                carregarDados();
                aplicarFiltroTabela();
                limpar(null);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Erro ao eliminar veículo", e);
                Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao eliminar veículo: " + e.getMessage());
                alert.showAndWait();
            }
        }
    }

    @FXML
    void limpar(ActionEvent event) {
        veiculoIdEmEdicao = 0;
        tipoVeiculoEmEdicao = "";
        txtMatricula.setText("");
        txtDocumento.setText("");
        txtCapacidadeCarga.setText("");
        txtTipoMercadoria.setText("");
        txtPassageiros.setText("");
        txtTipoCarroceria.setText("");

        if (cbProprietario != null) cbProprietario.setValue(null);
        if (cbAplicacao != null) cbAplicacao.setValue(APLICACAO_MERCADORIA);
        mudarAplicacao(null);

        txtMatricula.getStyleClass().remove("campo-erro");
        txtDocumento.getStyleClass().remove("campo-erro");
        txtCapacidadeCarga.getStyleClass().remove("campo-erro");
        txtPassageiros.getStyleClass().remove("campo-erro");

        if (lblMensagem != null) lblMensagem.setText("");
        if (btnActualizar != null) btnActualizar.setDisable(true);
        if (btnEliminar != null) btnEliminar.setDisable(true);
    }

    @FXML
    void selecionarItem(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Veiculo v = tabela.getSelectionModel().getSelectedItem();
            if (v != null) {
                veiculoIdEmEdicao = v.getId();
                txtMatricula.setText(v.getMatricula() != null ? v.getMatricula() : "");
                txtDocumento.setText(v.getDocumentoIdentifcacao() != null ? v.getDocumentoIdentifcacao() : "");

                if (v.getProprietario() != null) {
                    cbProprietario.setValue(v.getProprietario().getNome());
                }

                if (v instanceof Carrinha) {
                    Carrinha c = (Carrinha) v;
                    tipoVeiculoEmEdicao = "CARRINHA";
                    cbAplicacao.setValue(APLICACAO_PASSAGEIROS);
                    mudarAplicacao(null);
                    txtPassageiros.setText(c.getPassageiros() != null ? String.valueOf(c.getPassageiros()) : "");
                    txtTipoCarroceria.setText(c.getTipoCarroceria() != null ? c.getTipoCarroceria() : "");
                } else {
                    Camiao c = (Camiao) v;
                    tipoVeiculoEmEdicao = "CAMIAO";
                    cbAplicacao.setValue(APLICACAO_MERCADORIA);
                    mudarAplicacao(null);
                    txtCapacidadeCarga.setText(String.valueOf(c.getCapacidadeCarga()));
                    txtTipoMercadoria.setText(c.getTipoMercadoria() != null ? c.getTipoMercadoria() : "");
                }

                btnActualizar.setDisable(false);
                btnEliminar.setDisable(false);
                if (lblMensagem != null) lblMensagem.setText("");
            }
        }
    }

    @FXML
    void filtrarTodos(ActionEvent event) {
        filtroAtual = 0;
        atualizarEstiloBotoesFiltro();
        aplicarFiltroTabela();
    }

    @FXML
    void filtrarMercadoria(ActionEvent event) {
        filtroAtual = 1;
        atualizarEstiloBotoesFiltro();
        aplicarFiltroTabela();
    }

    @FXML
    void filtrarPassageiros(ActionEvent event) {
        filtroAtual = 2;
        atualizarEstiloBotoesFiltro();
        aplicarFiltroTabela();
    }

    private void atualizarEstiloBotoesFiltro() {
        btnFiltroTodos.getStyleClass().remove("segmento-activo");
        btnFiltroMercadoria.getStyleClass().remove("segmento-activo");
        btnFiltroPassageiros.getStyleClass().remove("segmento-activo");

        if (filtroAtual == 0) btnFiltroTodos.getStyleClass().add("segmento-activo");
        else if (filtroAtual == 1) btnFiltroMercadoria.getStyleClass().add("segmento-activo");
        else if (filtroAtual == 2) btnFiltroPassageiros.getStyleClass().add("segmento-activo");
    }

    private void configurarTabela() {
        colMatricula.setCellValueFactory(v -> new SimpleStringProperty(v.getValue().getMatricula()));
        colTipo.setCellValueFactory(v -> new SimpleStringProperty(
                v.getValue() instanceof Carrinha ? "Passageiros" : "Mercadoria"
        ));
        colMarca.setCellValueFactory(v -> new SimpleStringProperty(v.getValue().getDocumentoIdentifcacao()));
        colModelo.setCellValueFactory(v -> {
            if (v.getValue() instanceof Carrinha) {
                Carrinha car = (Carrinha) v.getValue();
                return new SimpleStringProperty(car.getTipoCarroceria() != null ? car.getTipoCarroceria() : "Lotação");
            } else {
                Camiao cam = (Camiao) v.getValue();
                return new SimpleStringProperty(cam.getTipoMercadoria() != null ? cam.getTipoMercadoria() : "Carga Geral");
            }
        });
        colCapacidade.setCellValueFactory(v -> {
            if (v.getValue() instanceof Carrinha) {
                Carrinha car = (Carrinha) v.getValue();
                int p = car.getPassageiros() != null ? car.getPassageiros() : 0;
                return new SimpleStringProperty(p + " passageiros");
            } else {
                return new SimpleStringProperty(v.getValue().getCapacidadeCarga() + " toneladas");
            }
        });
        colProprietario.setCellValueFactory(v -> new SimpleStringProperty(
                v.getValue().getProprietario() != null ? v.getValue().getProprietario().getNome() : "Sem proprietário"
        ));

        tabela.setItems(observavel);
    }

    private void aplicarFiltroTabela() {
        dadosTabela.clear();
        int totalCamioes = camioes != null ? camioes.getTamanho() : 0;
        int totalCarrinhas = carrinhas != null ? carrinhas.getTamanho() : 0;

        if (filtroAtual == 0 || filtroAtual == 1) {
            if (camioes != null) {
                for (Camiao c : camioes) {
                    if (c != null) dadosTabela.add(c);
                }
            }
        }
        if (filtroAtual == 0 || filtroAtual == 2) {
            if (carrinhas != null) {
                for (Carrinha c : carrinhas) {
                    if (c != null) dadosTabela.add(c);
                }
            }
        }

        if (lblTotalFrota != null) {
            lblTotalFrota.setText("Total na frota: " + (totalCamioes + totalCarrinhas) + " (" + totalCamioes + " camiões · " + totalCarrinhas + " carrinhas)");
        }
        if (lblResumoTabela != null) {
            lblResumoTabela.setText("Veículos listados: " + dadosTabela.getTamanho());
        }
        observavel.setAll(dadosTabela);
    }
}
