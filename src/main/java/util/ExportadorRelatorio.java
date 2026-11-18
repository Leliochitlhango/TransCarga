package util;

import dataStructure.ListaDuplamenteLigada;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Carregamento;

public class ExportadorRelatorio {

    public static void exportarCarregamentos(Stage stage, ListaDuplamenteLigada<Carregamento> carregamentos, String tituloFiltro) {
        if (carregamentos == null || carregamentos.getTamanho() == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Não existem dados na tabela para exportar.");
            alert.setHeaderText("Aviso");
            alert.showAndWait();
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exportar Relatório - TransCarga Moçambique");
        fileChooser.setInitialFileName("Relatorio_TransCarga_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm")));
        
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Documento CSV (*.csv)", "*.csv"),
                new FileChooser.ExtensionFilter("Documento PDF / HTML (*.html)", "*.html")
        );

        File arquivo = fileChooser.showSaveDialog(stage);
        if (arquivo == null) return;

        try {
            if (arquivo.getName().toLowerCase().endsWith(".csv")) {
                salvarComoCSV(arquivo, carregamentos);
            } else {
                salvarComoHTML(arquivo, carregamentos, tituloFiltro);
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Relatório exportado com sucesso para:\n" + arquivo.getAbsolutePath());
            alert.setHeaderText("Exportação Concluída");
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao exportar relatório: " + e.getMessage());
            alert.setHeaderText("Erro");
            alert.showAndWait();
        }
    }

    private static void salvarComoCSV(File arquivo, ListaDuplamenteLigada<Carregamento> lista) throws Exception {
        try (FileOutputStream fos = new FileOutputStream(arquivo);
             OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
             PrintWriter writer = new PrintWriter(osw)) {

            // Escrever BOM UTF-8 para reconhecimento de acentos no Excel
            fos.write(0xEF);
            fos.write(0xBB);
            fos.write(0xBF);

            writer.println("Data;Veículo;Proprietário;Cliente;Origem;Destino;Quantidade;Valor Cobrado (MZN)");

            for (Carregamento c : lista) {
                if (c == null) continue;
                String data = c.getData() != null ? c.getData().toString() : "";
                String veiculo = c.getVeiculo() != null ? c.getVeiculo().getMatricula() : "";
                String prop = (c.getVeiculo() != null && c.getVeiculo().getProprietario() != null) ? 
                        c.getVeiculo().getProprietario().getNome() : "";
                String cliente = c.getCliente() != null ? c.getCliente().getNome() : "";
                String origem = c.getLocalOrigem() != null ? c.getLocalOrigem() : "";
                String destino = c.getLocaDestino() != null ? c.getLocaDestino() : "";
                String qtd = String.valueOf(c.getQuantidade());
                String valor = String.format("%.2f", c.getValorCobrado());

                writer.println(String.format("%s;%s;%s;%s;%s;%s;%s;%s",
                        data, veiculo, prop, cliente, origem, destino, qtd, valor));
            }
        }
    }

    private static void salvarComoHTML(File arquivo, ListaDuplamenteLigada<Carregamento> lista, String tituloFiltro) throws Exception {
        try (PrintWriter writer = new PrintWriter(arquivo, StandardCharsets.UTF_8)) {
            writer.println("<!DOCTYPE html>");
            writer.println("<html lang='pt'>");
            writer.println("<head>");
            writer.println("<meta charset='UTF-8'>");
            writer.println("<title>Relatório TransCarga Moçambique</title>");
            writer.println("<style>");
            writer.println("body { font-family: 'Segoe UI', Arial, sans-serif; margin: 30px; color: #1F2D2A; }");
            writer.println(".header { border-bottom: 3px solid #00A85A; padding-bottom: 12px; margin-bottom: 20px; }");
            writer.println(".header h1 { margin: 0; color: #008A4A; font-size: 24px; }");
            writer.println(".header p { margin: 4px 0 0; color: #5E6F6A; font-size: 13px; }");
            writer.println("table { width: 100%; border-collapse: collapse; margin-top: 15px; font-size: 13px; }");
            writer.println("th { background-color: #00A85A; color: white; text-align: left; padding: 10px; }");
            writer.println("td { padding: 9px 10px; border-bottom: 1px solid #E6ECE9; }");
            writer.println("tr:nth-child(even) { background-color: #F8FAF9; }");
            writer.println(".totais { margin-top: 20px; text-align: right; font-size: 15px; font-weight: bold; color: #008A4A; }");
            writer.println("@media print { .no-print { display: none; } body { margin: 10mm; } }");
            writer.println("</style>");
            writer.println("</head>");
            writer.println("<body>");
            
            writer.println("<div class='header'>");
            writer.println("<h1>TransCarga Moçambique · Relatório Oficial</h1>");
            writer.println("<p>Gerado em: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + " | Filtro: " + tituloFiltro + "</p>");
            writer.println("</div>");

            writer.println("<table>");
            writer.println("<thead><tr>");
            writer.println("<th>Data</th><th>Veículo</th><th>Proprietário</th><th>Cliente</th><th>Origem</th><th>Destino</th><th>Qtd/Pass</th><th>Valor (MZN)</th>");
            writer.println("</tr></thead>");
            writer.println("<tbody>");

            double totalValor = 0;
            double totalQtd = 0;
            for (Carregamento c : lista) {
                if (c == null) continue;
                String data = c.getData() != null ? c.getData().toString() : "";
                String veiculo = c.getVeiculo() != null ? c.getVeiculo().getMatricula() : "";
                String prop = (c.getVeiculo() != null && c.getVeiculo().getProprietario() != null) ? 
                        c.getVeiculo().getProprietario().getNome() : "";
                String cliente = c.getCliente() != null ? c.getCliente().getNome() : "";
                String origem = c.getLocalOrigem() != null ? c.getLocalOrigem() : "";
                String destino = c.getLocaDestino() != null ? c.getLocaDestino() : "";
                double qtd = c.getQuantidade() != null ? c.getQuantidade() : 0.0;
                double val = c.getValorCobrado();

                totalValor += val;
                totalQtd += qtd;

                writer.println(String.format("<tr><td>%s</td><td><strong>%s</strong></td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%.1f</td><td>%.2f</td></tr>",
                        data, veiculo, prop, cliente, origem, destino, qtd, val));
            }

            writer.println("</tbody>");
            writer.println("</table>");

            writer.println("<div class='totais'>");
            writer.println(String.format("<p>Total de Serviços: %d | Volume Total: %.1f | Faturamento Geral: %.2f MZN</p>",
                    lista.getTamanho(), totalQtd, totalValor));
            writer.println("</div>");

            writer.println("<script>window.onload = function() { /* pode imprimir direto com window.print() se desejar */ };</script>");
            writer.println("</body>");
            writer.println("</html>");
        }
    }
}
