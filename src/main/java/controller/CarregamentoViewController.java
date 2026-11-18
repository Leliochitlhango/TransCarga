package controller;

import dao.CamiaoDAO;
import dao.CarrinhaDAO;
import dao.CarregamentoDAO;
import dao.ClienteDAO;
import dataStructure.ListaDuplamenteLigada;
import java.time.LocalDate;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import model.Camiao;
import model.Carrinha;
import model.Carregamento;
import model.Cliente;
import model.Veiculo;

public class CarregamentoViewController {

    private ListaDuplamenteLigada<Cliente> clientes;
    private ListaDuplamenteLigada<Camiao> camioes;
    private ListaDuplamenteLigada<Carrinha> carrinhas;

    @FXML private Button btnLimpar;
    @FXML private Button btnRegistar;
    @FXML private ComboBox<String> cbCliente;
    @FXML private ComboBox<String> cbVeiculo;
    @FXML private TextField txtTipoVeiculo;
    @FXML private Label lblTipoVeiculo;
    @FXML private Label lblQuantidade;
    @FXML private Label lblCapacidade;
    @FXML private Label lblRotuloCapacidade;
    @FXML private Label lblValidacao;
    @FXML private TextField txtDestino;
    @FXML private TextField txtOrigem;
    @FXML private TextField txtProprietario;
    @FXML private TextField txtQuantidade;
    @FXML private TextField txtValor;

    @FXML private ProgressIndicator piCapacidade;
    @FXML private Label lblStatusCapacidade;

    private double capacidadeVeiculoAtual = 0.0;
    private boolean isVeiculoPassageiros = false;

    @FXML
    public void initialize() {
        carregarDados();
        configurarValidacoesPorEventos();
    }

    private void carregarDados() {
        try {
            clientes = new ClienteDAO().findAll();
            camioes = new CamiaoDAO().findAll();
            carrinhas = new CarrinhaDAO().findAll();

            cbCliente.getItems().clear();
            if (clientes != null) {
                for (Cliente c : clientes) {
                    if (c != null && c.getNome() != null) {
                        cbCliente.getItems().add(c.getNome());
                    }
                }
            }

            cbVeiculo.getItems().clear();
            if (camioes != null) {
                for (Camiao c : camioes) {
                    if (c != null && c.getMatricula() != null) {
                        cbVeiculo.getItems().add(c.getMatricula());
                    }
                }
            }

            if (carrinhas != null) {
                for (Carrinha c : carrinhas) {
                    if (c != null && c.getMatricula() != null) {
                        cbVeiculo.getItems().add(c.getMatricula());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (lblValidacao != null) {
            lblValidacao.setText("");
        }
    }

    private void configurarValidacoesPorEventos() {
        // Evento ao alterar quantidade: validação em tempo real de carga ou lotação
        txtQuantidade.textProperty().addListener((obs, oldVal, newVal) -> {
            validarQuantidadeEmTempoReal();
        });

        // Evento ao alterar valor cobrado: validação numérica
        txtValor.textProperty().addListener((obs, oldVal, newVal) -> {
            validarValorEmTempoReal();
        });

        txtOrigem.textProperty().addListener((obs, oldVal, newVal) -> {
            txtOrigem.getStyleClass().remove("campo-erro");
            if (lblValidacao != null && lblValidacao.getText().contains("origem")) {
                lblValidacao.setText("");
            }
        });

        txtDestino.textProperty().addListener((obs, oldVal, newVal) -> {
            txtDestino.getStyleClass().remove("campo-erro");
            if (lblValidacao != null && lblValidacao.getText().contains("destino")) {
                lblValidacao.setText("");
            }
        });

        cbCliente.valueProperty().addListener((obs, oldVal, newVal) -> {
            cbCliente.getStyleClass().remove("campo-erro");
            if (lblValidacao != null && lblValidacao.getText().contains("cliente")) {
                lblValidacao.setText("");
            }
        });

        cbVeiculo.valueProperty().addListener((obs, oldVal, newVal) -> {
            cbVeiculo.getStyleClass().remove("campo-erro");
            veiculoSelecionado(null);
        });
    }

    private void validarQuantidadeEmTempoReal() {
        String texto = txtQuantidade.getText() != null ? txtQuantidade.getText().trim() : "";
        if (texto.isEmpty()) {
            txtQuantidade.getStyleClass().remove("campo-erro");
            atualizarIndicadorCapacidade(0.0);
            return;
        }

        try {
            double qtd = Double.parseDouble(texto.replace(",", "."));
            if (qtd <= 0) {
                marcarErro(txtQuantidade, "A quantidade deve ser maior que zero.");
                atualizarIndicadorCapacidade(0.0);
                return;
            }

            txtQuantidade.getStyleClass().remove("campo-erro");
            atualizarIndicadorCapacidade(qtd);

            if (capacidadeVeiculoAtual > 0 && qtd > capacidadeVeiculoAtual) {
                if (lblValidacao != null) {
                    if (isVeiculoPassageiros) {
                        lblValidacao.setText(String.format("⚠️ Excesso de lotação! Passageiros (%.0f) excede a capacidade máxima de %.0f lugares!",
                                qtd, capacidadeVeiculoAtual));
                    } else {
                        lblValidacao.setText(String.format("⚠️ Carga excessiva! Quantidade (%.1f t) excede a capacidade máxima suportada (%.1f t)!",
                                qtd, capacidadeVeiculoAtual));
                    }
                }
            } else if (lblValidacao != null && (lblValidacao.getText().contains("capacidade") || lblValidacao.getText().contains("lotação"))) {
                lblValidacao.setText("");
            }
        } catch (NumberFormatException ex) {
            marcarErro(txtQuantidade, "Quantidade inválida. Introduza apenas números.");
            atualizarIndicadorCapacidade(0.0);
        }
    }

    private void validarValorEmTempoReal() {
        String texto = txtValor.getText() != null ? txtValor.getText().trim() : "";
        if (texto.isEmpty()) {
            txtValor.getStyleClass().remove("campo-erro");
            return;
        }

        try {
            double val = Double.parseDouble(texto.replace(",", "."));
            if (val < 0) {
                marcarErro(txtValor, "O valor cobrado não pode ser negativo.");
            } else {
                txtValor.getStyleClass().remove("campo-erro");
                if (lblValidacao != null && lblValidacao.getText().contains("valor")) {
                    lblValidacao.setText("");
                }
            }
        } catch (NumberFormatException ex) {
            marcarErro(txtValor, "Valor inválido. Introduza apenas números (ex: 45000).");
        }
    }

    private void atualizarIndicadorCapacidade(double qtd) {
        if (piCapacidade == null || lblStatusCapacidade == null) return;

        if (capacidadeVeiculoAtual <= 0) {
            piCapacidade.setProgress(0.0);
            lblStatusCapacidade.setText("Seleccione o veículo");
            lblStatusCapacidade.getStyleClass().setAll("pilula", "pilula-amarela");
            btnRegistar.setDisable(false);
            return;
        }

        double percentual = qtd / capacidadeVeiculoAtual;
        piCapacidade.setProgress(Math.min(percentual, 1.0));

        if (percentual > 1.0) {
            piCapacidade.getStyleClass().remove("capacidade-ok");
            if (!piCapacidade.getStyleClass().contains("capacidade-excedida")) {
                piCapacidade.getStyleClass().add("capacidade-excedida");
            }
            if (isVeiculoPassageiros) {
                lblStatusCapacidade.setText(String.format("Lotação excedida (%.0f%%)", percentual * 100));
            } else {
                lblStatusCapacidade.setText(String.format("Carga excedida (%.0f%%)", percentual * 100));
            }
            lblStatusCapacidade.getStyleClass().setAll("pilula", "pilula-vermelha");
            btnRegistar.setDisable(true); // Bloqueia conforme requerido
        } else {
            piCapacidade.getStyleClass().remove("capacidade-excedida");
            if (!piCapacidade.getStyleClass().contains("capacidade-ok")) {
                piCapacidade.getStyleClass().add("capacidade-ok");
            }
            if (qtd == 0) {
                lblStatusCapacidade.setText("Disponível (0%)");
                lblStatusCapacidade.getStyleClass().setAll("pilula", "pilula-verde");
            } else {
                String desc = isVeiculoPassageiros ? "Dentro da lotação" : "Dentro da capacidade";
                lblStatusCapacidade.setText(String.format("%s (%.0f%%)", desc, percentual * 100));
                lblStatusCapacidade.getStyleClass().setAll("pilula", "pilula-verde");
            }
            btnRegistar.setDisable(false);
        }
    }

    @FXML
    void veiculoSelecionado(ActionEvent event) {
        String matricula = cbVeiculo.getValue();
        if (matricula == null) return;

        Veiculo veiculo = encontrarVeiculo(matricula);
        if (veiculo != null) {
            if (veiculo instanceof Carrinha) {
                Carrinha car = (Carrinha) veiculo;
                isVeiculoPassageiros = true;
                int maxPass = car.getPassageiros() != null ? car.getPassageiros() : car.getCapacidadeCarga();
                capacidadeVeiculoAtual = maxPass;
                lblCapacidade.setText(String.valueOf(maxPass));
                if (lblRotuloCapacidade != null) lblRotuloCapacidade.setText("Lotação máx. (pass.)");
                if (lblQuantidade != null) lblQuantidade.setText("Total de Passageiros a transportar");
                if (lblTipoVeiculo != null) lblTipoVeiculo.setText("Tipo de Serviço / Rota de Passageiros");
                txtTipoVeiculo.setText(car.getTipoCarroceria() != null ? car.getTipoCarroceria() : "Transporte de Passageiros");
                txtQuantidade.setPromptText("Ex.: 15");
            } else {
                Camiao cam = (Camiao) veiculo;
                isVeiculoPassageiros = false;
                capacidadeVeiculoAtual = cam.getCapacidadeCarga();
                lblCapacidade.setText(String.valueOf(cam.getCapacidadeCarga()));
                if (lblRotuloCapacidade != null) lblRotuloCapacidade.setText("Capacidade máx. (ton)");
                if (lblQuantidade != null) lblQuantidade.setText("Quantidade de Carga (toneladas)");
                if (lblTipoVeiculo != null) lblTipoVeiculo.setText("Mercadoria / Carga a transportar");
                txtTipoVeiculo.setText(cam.getTipoMercadoria() != null ? cam.getTipoMercadoria() : "Carga Geral");
                txtQuantidade.setPromptText("Ex.: 12");
            }

            if (veiculo.getProprietario() != null) {
                txtProprietario.setText(veiculo.getProprietario().getNome());
            } else {
                txtProprietario.setText("Sem proprietário");
            }

            validarQuantidadeEmTempoReal();
        }
    }

    private Veiculo encontrarVeiculo(String matricula) {
        if (camioes != null) {
            for (Camiao c : camioes) {
                if (c != null && matricula.equals(c.getMatricula())) return c;
            }
        }
        if (carrinhas != null) {
            for (Carrinha c : carrinhas) {
                if (c != null && matricula.equals(c.getMatricula())) return c;
            }
        }
        return null;
    }

    @FXML
    void registar(ActionEvent event) {
        boolean temErro = false;

        if (cbCliente.getValue() == null || cbCliente.getValue().trim().isEmpty()) {
            marcarErro(cbCliente, "Seleccione um cliente.");
            temErro = true;
        }

        if (cbVeiculo.getValue() == null || cbVeiculo.getValue().trim().isEmpty()) {
            marcarErro(cbVeiculo, "Seleccione um veículo.");
            temErro = true;
        }

        String qtdStr = txtQuantidade.getText() != null ? txtQuantidade.getText().trim() : "";
        double quantidade = 0.0;
        if (qtdStr.isEmpty()) {
            marcarErro(txtQuantidade, "Preencha a quantidade ou total de passageiros.");
            temErro = true;
        } else {
            try {
                quantidade = Double.parseDouble(qtdStr.replace(",", "."));
                if (quantidade <= 0) {
                    marcarErro(txtQuantidade, "A quantidade deve ser positiva.");
                    temErro = true;
                }
            } catch (NumberFormatException e) {
                marcarErro(txtQuantidade, "Quantidade inválida.");
                temErro = true;
            }
        }

        if (capacidadeVeiculoAtual > 0 && quantidade > capacidadeVeiculoAtual) {
            marcarErro(txtQuantidade, isVeiculoPassageiros ? 
                    "⚠️ A lotação informada excede a capacidade máxima de passageiros do veículo!" : 
                    "⚠️ A quantidade informada excede a capacidade de carga suportada pelo veículo!");
            temErro = true;
        }

        String valorStr = txtValor.getText() != null ? txtValor.getText().trim() : "";
        double valorCobrado = 0.0;
        if (valorStr.isEmpty()) {
            marcarErro(txtValor, "Preencha o valor cobrado.");
            temErro = true;
        } else {
            try {
                valorCobrado = Double.parseDouble(valorStr.replace(",", "."));
                if (valorCobrado < 0) {
                    marcarErro(txtValor, "O valor não pode ser negativo.");
                    temErro = true;
                }
            } catch (NumberFormatException e) {
                marcarErro(txtValor, "Valor cobrado inválido.");
                temErro = true;
            }
        }

        if (txtOrigem.getText() == null || txtOrigem.getText().trim().isEmpty()) {
            marcarErro(txtOrigem, "Preencha o local de origem.");
            temErro = true;
        }

        if (txtDestino.getText() == null || txtDestino.getText().trim().isEmpty()) {
            marcarErro(txtDestino, "Preencha o local de destino.");
            temErro = true;
        }

        if (temErro) {
            return;
        }

        try {
            Carregamento novoCarregamento = new Carregamento();
            novoCarregamento.setLocalOrigem(txtOrigem.getText().trim());
            novoCarregamento.setLocaDestino(txtDestino.getText().trim());
            novoCarregamento.setValorCobrado(valorCobrado);
            // Data é extraída automaticamente pelo sistema
            novoCarregamento.setData(LocalDate.now());
            novoCarregamento.setQuantidade(quantidade);

            if (clientes != null) {
                for (Cliente cli : clientes) {
                    if (cli != null && cli.getNome() != null && cli.getNome().equals(cbCliente.getValue())) {
                        novoCarregamento.setCliente(cli);
                        break;
                    }
                }
            }

            Veiculo veiculo = encontrarVeiculo(cbVeiculo.getValue());
            if (veiculo != null) {
                novoCarregamento.setVeiculo(veiculo);
            }

            new CarregamentoDAO().save(novoCarregamento);

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Serviço / Carregamento registado com sucesso com a data de hoje (" + LocalDate.now() + ")!");
            alert.setHeaderText("Operação Concluída");
            alert.showAndWait();

            limpar(null);

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao gravar carregamento: " + e.getMessage());
            alert.setHeaderText("Erro");
            alert.showAndWait();
        }
    }

    @FXML
    void limpar(ActionEvent event) {
        cbCliente.setValue(null);
        cbVeiculo.setValue(null);
        txtProprietario.clear();
        txtTipoVeiculo.clear();
        txtQuantidade.clear();
        txtValor.clear();
        txtOrigem.clear();
        txtDestino.clear();

        capacidadeVeiculoAtual = 0.0;
        isVeiculoPassageiros = false;
        lblCapacidade.setText("0");
        if (lblRotuloCapacidade != null) lblRotuloCapacidade.setText("Capacidade máx.");
        if (lblQuantidade != null) lblQuantidade.setText("Quantidade (toneladas / passageiros)");
        if (lblTipoVeiculo != null) lblTipoVeiculo.setText("Mercadoria / Descrição do Serviço");

        if (piCapacidade != null) {
            piCapacidade.setProgress(0.0);
            piCapacidade.getStyleClass().remove("capacidade-excedida");
            if (!piCapacidade.getStyleClass().contains("capacidade-ok")) {
                piCapacidade.getStyleClass().add("capacidade-ok");
            }
        }
        if (lblStatusCapacidade != null) {
            lblStatusCapacidade.setText("Aguardando dados");
            lblStatusCapacidade.getStyleClass().setAll("pilula", "pilula-verde");
        }
        if (lblValidacao != null) {
            lblValidacao.setText("");
        }
        btnRegistar.setDisable(false);

        cbCliente.getStyleClass().remove("campo-erro");
        cbVeiculo.getStyleClass().remove("campo-erro");
        txtQuantidade.getStyleClass().remove("campo-erro");
        txtValor.getStyleClass().remove("campo-erro");
        txtOrigem.getStyleClass().remove("campo-erro");
        txtDestino.getStyleClass().remove("campo-erro");
    }

    private void marcarErro(javafx.scene.Node campo, String mensagem) {
        if (campo != null && !campo.getStyleClass().contains("campo-erro")) {
            campo.getStyleClass().add("campo-erro");
        }
        if (lblValidacao != null) {
            lblValidacao.setText(mensagem);
        }
    }
}
