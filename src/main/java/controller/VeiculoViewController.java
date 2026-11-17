package controller;

import dao.CamiaoDAO;
import dao.CarrinhaDAO;
import dao.ProprietarioDAO;
import dataStructure.ListaDuplamenteLigada;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import model.Camiao;
import model.Carrinha;
import model.Proprietario;
import model.Veiculo;

public class VeiculoViewController {

    private ListaDuplamenteLigada<Camiao> camioes;
    private ListaDuplamenteLigada<Carrinha> carrinhas;
    private ListaDuplamenteLigada<Proprietario> proprietarios;

    private final ObservableList<Veiculo> dadosTabela = FXCollections.observableArrayList();

    @FXML private Button btnActualizar;
    @FXML private Button btnGuardar;
    @FXML private Button btnLimpar;

    @FXML private ComboBox<String> cbProprietario;
    @FXML private ComboBox<String> cbTipo;

    @FXML private TableView<Veiculo> tabela;
    @FXML private TableColumn<Veiculo, String> colCapacidade;
    @FXML private TableColumn<Veiculo, String> colMarca;
    @FXML private TableColumn<Veiculo, String> colMatricula;
    @FXML private TableColumn<Veiculo, String> colModelo;
    @FXML private TableColumn<Veiculo, String> colProprietario;
    @FXML private TableColumn<Veiculo, String> colTipo;

    @FXML private Label lblMarca;
    @FXML private Label lblModelo;
    @FXML private Label lblMensagem;

    @FXML private TextField txtCapacidade;
    @FXML private TextField txtMarca;
    @FXML private TextField txtMatricula;
    @FXML private TextField txtModelo;

    @FXML
    public void initialize() {
        cbTipo.getItems().setAll("CAMIAO", "CARRINHA");

        configurarEventosValidacao();
        carregarDados();
        configurarTabela();
        preencherTabela();
    }

    private void carregarDados() {
        try {
            camioes = new CamiaoDAO().findAll();
            carrinhas = new CarrinhaDAO().findAll();
            proprietarios = new ProprietarioDAO().findAll();

            cbProprietario.getItems().clear();
            if (proprietarios != null) {
                for (Proprietario p : proprietarios) {
                    if (p != null && p.getNome() != null) {
                        cbProprietario.getItems().add(p.getNome());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void configurarEventosValidacao() {
        if (txtMatricula != null) {
            txtMatricula.textProperty().addListener((obs, oldVal, newVal) -> {
                txtMatricula.getStyleClass().remove("campo-erro");
                limparMensagemSeContem("matrícula");
            });
        }

        if (txtCapacidade != null) {
            txtCapacidade.textProperty().addListener((obs, oldVal, newVal) -> {
                txtCapacidade.getStyleClass().remove("campo-erro");
                limparMensagemSeContem("capacidade");
            });
        }

        if (txtMarca != null) {
            txtMarca.textProperty().addListener((obs, oldVal, newVal) -> {
                txtMarca.getStyleClass().remove("campo-erro");
                limparMensagemSeContem("documento");
            });
        }

        if (txtModelo != null) {
            txtModelo.textProperty().addListener((obs, oldVal, newVal) -> {
                txtModelo.getStyleClass().remove("campo-erro");
                limparMensagemSeContem("mercadoria");
                limparMensagemSeContem("passageiros");
            });
        }

        if (cbTipo != null) {
            cbTipo.valueProperty().addListener((obs, oldVal, newVal) -> {
                cbTipo.getStyleClass().remove("campo-erro");
                mudarTipoVeiculo(null);
            });
        }

        if (cbProprietario != null) {
            cbProprietario.valueProperty().addListener((obs, oldVal, newVal) -> {
                cbProprietario.getStyleClass().remove("campo-erro");
                limparMensagemSeContem("proprietário");
            });
        }
    }

    private void limparMensagemSeContem(String termo) {
        if (lblMensagem != null && lblMensagem.getText().toLowerCase().contains(termo.toLowerCase())) {
            lblMensagem.setText("");
        }
    }

    private void marcarErro(javafx.scene.Node campo, String mensagem) {
        if (campo != null && !campo.getStyleClass().contains("campo-erro")) {
            campo.getStyleClass().add("campo-erro");
        }
        if (lblMensagem != null) {
            lblMensagem.setText(mensagem);
        }
    }

    private boolean validarCampos() {
        boolean temErro = false;

        String tipo = cbTipo.getValue();
        if (tipo == null || tipo.trim().isEmpty()) {
            marcarErro(cbTipo, "Seleccione o tipo de veículo (Camião ou Carrinha).");
            temErro = true;
        }

        String matricula = txtMatricula.getText() != null ? txtMatricula.getText().trim() : "";
        if (matricula.isEmpty()) {
            marcarErro(txtMatricula, "Por favor, introduza a matrícula do veículo.");
            temErro = true;
        }

        String marca = txtMarca.getText() != null ? txtMarca.getText().trim() : "";
        if (marca.isEmpty()) {
            marcarErro(txtMarca, "Por favor, introduza o documento de identificação/marca.");
            temErro = true;
        }

        String capacidadeStr = txtCapacidade.getText() != null ? txtCapacidade.getText().trim() : "";
        if (capacidadeStr.isEmpty()) {
            marcarErro(txtCapacidade, "Por favor, introduza a capacidade de carga.");
            temErro = true;
        } else {
            try {
                int cap = Integer.parseInt(capacidadeStr);
                if (cap <= 0) {
                    marcarErro(txtCapacidade, "A capacidade deve ser maior que zero.");
                    temErro = true;
                }
            } catch (NumberFormatException e) {
                marcarErro(txtCapacidade, "Capacidade inválida. Introduza apenas números inteiros.");
                temErro = true;
            }
        }

        String modelo = txtModelo.getText() != null ? txtModelo.getText().trim() : "";
        if ("CARRINHA".equals(tipo)) {
            if (modelo.isEmpty()) {
                marcarErro(txtModelo, "Introduza o número de passageiros.");
                temErro = true;
            } else {
                try {
                    int pass = Integer.parseInt(modelo);
                    if (pass < 0) {
                        marcarErro(txtModelo, "Número de passageiros não pode ser negativo.");
                        temErro = true;
                    }
                } catch (NumberFormatException e) {
                    marcarErro(txtModelo, "Número de passageiros inválido. Introduza apenas números.");
                    temErro = true;
                }
            }
        } else if ("CAMIAO".equals(tipo)) {
            if (modelo.isEmpty()) {
                marcarErro(txtModelo, "Introduza o tipo de mercadoria suportada.");
                temErro = true;
            }
        }

        if (cbProprietario.getValue() == null || cbProprietario.getValue().trim().isEmpty()) {
            marcarErro(cbProprietario, "Seleccione o proprietário do veículo.");
            temErro = true;
        }

        return !temErro;
    }

    private Proprietario encontrarProprietario(String nome) {
        if (proprietarios != null && nome != null) {
            for (Proprietario p : proprietarios) {
                if (p != null && nome.equals(p.getNome())) {
                    return p;
                }
            }
        }
        return null;
    }

    @FXML
    void guardar(ActionEvent event) {
        if (!validarCampos()) return;

        String tipo = cbTipo.getValue();
        Proprietario prop = encontrarProprietario(cbProprietario.getValue());
        int cap = Integer.parseInt(txtCapacidade.getText().trim());

        try {
            if ("CAMIAO".equals(tipo)) {
                Camiao c = new Camiao();
                c.setMatricula(txtMatricula.getText().trim());
                c.setDocumentoIdentifcacao(txtMarca.getText().trim());
                c.setProprietario(prop);
                c.setCapacidadeCarga(cap);
                c.setTipoMercadoria(txtModelo.getText().trim());
                c.setNumeroEixos("4");

                new CamiaoDAO().save(c);
                new Alert(Alert.AlertType.INFORMATION, "Camião registado com sucesso!").showAndWait();

            } else if ("CARRINHA".equals(tipo)) {
                Carrinha c = new Carrinha();
                c.setMatricula(txtMatricula.getText().trim());
                c.setDocumentoIdentifcacao(txtMarca.getText().trim());
                c.setProprietario(prop);
                c.setCapacidadeCarga(cap);
                c.setPassageiros(Integer.valueOf(txtModelo.getText().trim()));
                c.setTipoCarroceria("Fechada");

                new CarrinhaDAO().save(c);
                new Alert(Alert.AlertType.INFORMATION, "Carrinha registada com sucesso!").showAndWait();
            }

            carregarDados();
            preencherTabela();
            limpar(null);

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Erro ao gravar veículo: " + e.getMessage()).showAndWait();
        }
    }

    @FXML
    void actualizar(ActionEvent event) {
        if (!validarCampos()) return;

        String tipo = cbTipo.getValue();
        Proprietario prop = encontrarProprietario(cbProprietario.getValue());
        int cap = Integer.parseInt(txtCapacidade.getText().trim());

        try {
            int id = 0;
            if (btnActualizar.getAccessibleText() != null) {
                id = Integer.parseInt(btnActualizar.getAccessibleText());
            }

            if ("CAMIAO".equals(tipo)) {
                Camiao c = new Camiao();
                c.setId(id);
                c.setMatricula(txtMatricula.getText().trim());
                c.setDocumentoIdentifcacao(txtMarca.getText().trim());
                c.setProprietario(prop);
                c.setCapacidadeCarga(cap);
                c.setTipoMercadoria(txtModelo.getText().trim());
                c.setNumeroEixos("4");

                new CamiaoDAO().update(c);
                new Alert(Alert.AlertType.INFORMATION, "Camião actualizado com sucesso!").showAndWait();

            } else if ("CARRINHA".equals(tipo)) {
                Carrinha c = new Carrinha();
                c.setId(id);
                c.setMatricula(txtMatricula.getText().trim());
                c.setDocumentoIdentifcacao(txtMarca.getText().trim());
                c.setProprietario(prop);
                c.setCapacidadeCarga(cap);
                c.setPassageiros(Integer.valueOf(txtModelo.getText().trim()));
                c.setTipoCarroceria("Fechada");

                new CarrinhaDAO().update(c);
                new Alert(Alert.AlertType.INFORMATION, "Carrinha actualizada com sucesso!").showAndWait();
            }

            carregarDados();
            preencherTabela();
            limpar(null);

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Erro ao actualizar veículo: " + e.getMessage()).showAndWait();
        }
    }

    @FXML
    void limpar(ActionEvent event) {
        cbTipo.setValue(null);
        txtMatricula.clear();
        txtMarca.clear();
        txtModelo.clear();
        txtCapacidade.clear();
        cbProprietario.setValue(null);

        cbTipo.getStyleClass().remove("campo-erro");
        txtMatricula.getStyleClass().remove("campo-erro");
        txtMarca.getStyleClass().remove("campo-erro");
        txtModelo.getStyleClass().remove("campo-erro");
        txtCapacidade.getStyleClass().remove("campo-erro");
        cbProprietario.getStyleClass().remove("campo-erro");

        if (lblMensagem != null) lblMensagem.setText("");
        if (btnActualizar != null) {
            btnActualizar.setDisable(true);
            btnActualizar.setAccessibleText(null);
        }
    }

    @FXML
    void mudarTipoVeiculo(ActionEvent event) {
        String tipo = cbTipo.getValue();
        if ("CAMIAO".equals(tipo)) {
            lblMarca.setText("Doc. ID / Marca");
            txtMarca.setPromptText("Ex.: DI-0023 / Volvo");
            lblModelo.setText("Mercadoria");
            txtModelo.setPromptText("Ex.: Cimento / Areia Grossa");
        } else if ("CARRINHA".equals(tipo)) {
            lblMarca.setText("Doc. ID / Marca");
            txtMarca.setPromptText("Ex.: DI-0023 / Toyota");
            lblModelo.setText("Passageiros");
            txtModelo.setPromptText("Ex.: 15");
        }
    }

    private void configurarTabela() {
        colMatricula.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getMatricula()));
        colTipo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue() instanceof Camiao ? "CAMIÃO" : "CARRINHA"));
        colMarca.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDocumentoIdentifcacao()));
        colModelo.setCellValueFactory(c -> {
            Veiculo v = c.getValue();
            if (v instanceof Camiao) {
                return new SimpleStringProperty(((Camiao) v).getTipoMercadoria());
            } else if (v instanceof Carrinha) {
                return new SimpleStringProperty(((Carrinha) v).getPassageiros() + " pass.");
            }
            return new SimpleStringProperty("");
        });
        colCapacidade.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCapacidadeCarga() + " t"));
        colProprietario.setCellValueFactory(c -> {
            Veiculo v = c.getValue();
            return new SimpleStringProperty(v.getProprietario() != null ? v.getProprietario().getNome() : "");
        });

        tabela.setItems(dadosTabela);
        tabela.setOnMouseClicked(this::selecionarItem);
    }

    private void preencherTabela() {
        dadosTabela.clear();
        if (camioes != null) {
            for (Camiao c : camioes) {
                if (c != null) dadosTabela.add(c);
            }
        }
        if (carrinhas != null) {
            for (Carrinha c : carrinhas) {
                if (c != null) dadosTabela.add(c);
            }
        }
    }

    private void selecionarItem(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Veiculo v = tabela.getSelectionModel().getSelectedItem();
            if (v != null) {
                txtMatricula.setText(v.getMatricula() != null ? v.getMatricula() : "");
                txtMarca.setText(v.getDocumentoIdentifcacao() != null ? v.getDocumentoIdentifcacao() : "");
                txtCapacidade.setText(String.valueOf(v.getCapacidadeCarga()));

                if (v instanceof Camiao) {
                    cbTipo.setValue("CAMIAO");
                    txtModelo.setText(((Camiao) v).getTipoMercadoria() != null ? ((Camiao) v).getTipoMercadoria() : "");
                } else if (v instanceof Carrinha) {
                    cbTipo.setValue("CARRINHA");
                    txtModelo.setText(((Carrinha) v).getPassageiros() != null ? String.valueOf(((Carrinha) v).getPassageiros()) : "");
                }

                if (v.getProprietario() != null) {
                    cbProprietario.setValue(v.getProprietario().getNome());
                }

                if (btnActualizar != null) {
                    btnActualizar.setAccessibleText(String.valueOf(v.getId()));
                    btnActualizar.setDisable(false);
                }

                cbTipo.getStyleClass().remove("campo-erro");
                txtMatricula.getStyleClass().remove("campo-erro");
                txtMarca.getStyleClass().remove("campo-erro");
                txtModelo.getStyleClass().remove("campo-erro");
                txtCapacidade.getStyleClass().remove("campo-erro");
                cbProprietario.getStyleClass().remove("campo-erro");
                if (lblMensagem != null) lblMensagem.setText("");
            }
        }
    }
}
