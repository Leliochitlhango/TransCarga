package util;

import java.util.regex.Pattern;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.TextField;

public class Validador {

    // Províncias válidas de Moçambique:
    // MP (Maputo Província), MC (Maputo Cidade), GZ (Gaza), IB (Inhambane),
    // SF (Sofala), MN (Manica), TT (Tete), ZB (Zambézia), NA (Nampula),
    // CD/CB (Cabo Delgado), NS (Niassa)
    private static final Pattern PADRAO_MATRICULA = Pattern.compile(
            "^[A-Z]{3}-\\d{3}-(MP|MC|GZ|IB|SF|MN|TT|ZB|NA|CD|CB|NS)$"
    );

    // Telefone Moçambique: 9 dígitos, iniciando com 82, 83, 84, 85, 86, 87 ou 88
    private static final Pattern PADRAO_TELEFONE = Pattern.compile(
            "^8[2-8]\\s?\\d{3}\\s?\\d{4}$"
    );

    // BI Moçambique: 12 ou 13 dígitos seguidos por 1 letra maiúscula (ex: 110100234567B ou 1101002345678B)
    private static final Pattern PADRAO_BI = Pattern.compile(
            "^\\d{12,13}[A-Z]$"
    );

    // NUIT Moçambique: 9 dígitos numéricos
    private static final Pattern PADRAO_NUIT = Pattern.compile(
            "^\\d{9}$"
    );

    // Chassi (VIN internacional/nacional): alfanumérico entre 6 e 17 caracteres
    private static final Pattern PADRAO_CHASSI = Pattern.compile(
            "^[A-HJ-NPR-Z0-9]{6,17}$"
    );

    /**
     * Valida matrícula moçambicana formatada (ex: ABC-123-MP).
     */
    public static boolean isMatriculaValida(String matricula) {
        if (matricula == null) return false;
        String limpo = matricula.trim().toUpperCase();
        return PADRAO_MATRICULA.matcher(limpo).matches();
    }

    /**
     * Formata entrada de matrícula para o padrão moçambicano AAA-000-XX.
     */
    public static String formatarMatricula(String raw) {
        if (raw == null) return "";
        // Remove caracteres que não sejam letras ou números
        String limpo = raw.replaceAll("[^a-zA-Z0-9]", "").toUpperCase();
        if (limpo.length() <= 3) {
            return limpo;
        } else if (limpo.length() <= 6) {
            return limpo.substring(0, 3) + "-" + limpo.substring(3);
        } else {
            String parte1 = limpo.substring(0, 3);
            String parte2 = limpo.substring(3, Math.min(6, limpo.length()));
            String parte3 = limpo.substring(6, Math.min(8, limpo.length()));
            return parte1 + "-" + parte2 + "-" + parte3;
        }
    }

    /**
     * Valida telefone de Moçambique (82XXXXXXX até 88XXXXXXX).
     */
    public static boolean isTelefoneValido(String telefone) {
        if (telefone == null) return false;
        String limpo = telefone.trim();
        return PADRAO_TELEFONE.matcher(limpo).matches();
    }

    /**
     * Formata número de telefone para o padrão moçambicano: 8X XXX XXXX.
     */
    public static String formatarTelefone(String raw) {
        if (raw == null) return "";
        String digitos = raw.replaceAll("\\D", "");
        if (digitos.length() > 9) {
            digitos = digitos.substring(0, 9);
        }

        if (digitos.length() <= 2) {
            return digitos;
        } else if (digitos.length() <= 5) {
            return digitos.substring(0, 2) + " " + digitos.substring(2);
        } else {
            return digitos.substring(0, 2) + " " + digitos.substring(2, 5) + " " + digitos.substring(5);
        }
    }

    /**
     * Valida Bilhete de Identidade moçambicano (12 ou 13 dígitos + 1 letra).
     */
    public static boolean isBIValido(String bi) {
        if (bi == null) return false;
        String limpo = bi.replaceAll("\\s", "").toUpperCase();
        return PADRAO_BI.matcher(limpo).matches();
    }

    /**
     * Formata BI moçambicano em maiúsculas sem espaços.
     */
    public static String formatarBI(String raw) {
        if (raw == null) return "";
        return raw.replaceAll("\\s", "").toUpperCase();
    }

    /**
     * Valida NUIT (9 dígitos numéricos).
     */
    public static boolean isNUITValido(String nuit) {
        if (nuit == null) return false;
        String limpo = nuit.replaceAll("\\s", "");
        return PADRAO_NUIT.matcher(limpo).matches();
    }

    /**
     * Valida número de chassi de veículo (6 a 17 caracteres alfanuméricos).
     */
    public static boolean isChassiValido(String chassi) {
        if (chassi == null) return false;
        String limpo = chassi.trim().toUpperCase();
        return PADRAO_CHASSI.matcher(limpo).matches();
    }

    /**
     * Anexa máscara automática a um TextField de Telefone (8X XXX XXXX).
     */
    public static void aplicarMascaraTelefone(TextField textField) {
        textField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null) return;
            String formatado = formatarTelefone(newVal);
            if (!formatado.equals(newVal)) {
                textField.setText(formatado);
                textField.positionCaret(formatado.length());
            }
        });
    }

    /**
     * Anexa restrição e máscara automática para NUIT (exclusivamente 9 dígitos).
     * Rejeita imediatamente qualquer letra ou caractere especial.
     */
    public static void aplicarMascaraNUIT(TextField textField) {
        if (textField == null) return;
        textField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null) return;
            // Permitir apenas dígitos e no máximo 9
            String apenasDigitos = newVal.replaceAll("\\D", "");
            if (apenasDigitos.length() > 9) {
                apenasDigitos = apenasDigitos.substring(0, 9);
            }
            if (!newVal.equals(apenasDigitos)) {
                textField.setText(apenasDigitos);
                textField.positionCaret(apenasDigitos.length());
            }
        });
    }

    /**
     * Anexa restrição e máscara automática para BI Moçambicano:
     * 12 a 13 dígitos numéricos seguidos de 1 letra maiúscula (ex.: 110100234567B).
     * Rejeita caracteres inválidos e garante a letra em maiúscula no final.
     */
    public static void aplicarMascaraBI(TextField textField) {
        if (textField == null) return;
        textField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null) return;
            String upper = newVal.toUpperCase();
            StringBuilder sb = new StringBuilder();
            
            for (int i = 0; i < upper.length(); i++) {
                char ch = upper.charAt(i);
                int lenAtual = sb.length();
                if (lenAtual < 12) {
                    // Os primeiros 12 devem ser dígitos
                    if (Character.isDigit(ch)) {
                        sb.append(ch);
                    }
                } else if (lenAtual == 12) {
                    // Pode ser o 13º dígito OU a letra final
                    if (Character.isDigit(ch) || (ch >= 'A' && ch <= 'Z')) {
                        sb.append(ch);
                    }
                } else if (lenAtual == 13) {
                    // Se o 13º foi dígito, o 14º deve ser a letra final
                    char char13 = sb.charAt(12);
                    if (Character.isDigit(char13)) {
                        if (ch >= 'A' && ch <= 'Z') {
                            sb.append(ch);
                        }
                    }
                }
            }
            String filtrado = sb.toString();
            if (!newVal.equals(filtrado)) {
                textField.setText(filtrado);
                textField.positionCaret(filtrado.length());
            }
        });
    }

    /**
     * Anexa máscara automática a um TextField de Matrícula (ABC-123-MP).
     */
    public static void aplicarMascaraMatricula(TextField textField) {
        textField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null) return;
            String formatado = formatarMatricula(newVal);
            if (!formatado.equals(newVal)) {
                textField.setText(formatado);
                textField.positionCaret(formatado.length());
            }
        });
    }
}
