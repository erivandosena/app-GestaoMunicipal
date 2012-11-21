package br.net.rwd.camaramulungu.util;

public class NormalizaString {

	public static String normalizar(String str) {
		// str = Normalizer.normalize(str, Normalizer.Form.NFD);
		// str = str.replaceAll("[^\\p{ASCII}]", "");

		// Troca os caracteres acentuados por nao acentuados
		String[][] caracteresAcento = { { "�", "A" }, { "�", "a" },
				{ "�", "E" }, { "�", "e" }, { "�", "I" }, { "�", "i" },
				{ "�", "O" }, { "�", "o" }, { "�", "U" }, { "�", "u" },
				{ "�", "A" }, { "�", "a" }, { "�", "E" }, { "�", "e" },
				{ "�", "I" }, { "�", "i" }, { "�", "O" }, { "�", "o" },
				{ "�", "U" }, { "�", "u" }, { "�", "A" }, { "�", "a" },
				{ "�", "E" }, { "�", "e" }, { "�", "I" }, { "�", "i" },
				{ "�", "O" }, { "�", "o" }, { "�", "U" }, { "�", "u" },
				{ "�", "A" }, { "�", "a" }, { "�", "E" }, { "�", "e" },
				{ "�", "I" }, { "�", "i" }, { "�", "O" }, { "�", "o" },
				{ "�", "U" }, { "�", "u" }, { "�", "A" }, { "�", "a" },
				{ "�", "O" }, { "�", "o" }, { "�", "C" }, { "�", "c" },
				{ "�", "Y" }, { "�", "y" }, { "�", "y" }, { "�", "y" },
				{ "�", "N" }, { "�", "n" }, };

		for (int i = 0; i < caracteresAcento.length; i++) {
			str = str
					.replaceAll(caracteresAcento[i][0], caracteresAcento[i][1]);
		}

		// Troca os caracteres especiais da string por ""
		String[] caracteresEspeciais = { "!", "@", "#", "\\$", "%", "�", "&",
				"\\*", "\\(", "\\)", "_", "�", "\\+", "`", "\\{", "\\}", "\\^",
				"\\|", "\\?", "<", ">", ":", "'", "\\\\", "\\//", "\\=", "�",
				"\\[", "\\]", "\\~", "\\.", "\\;", "�", "�", "�", "�", "�",
				"\\�", "�", "\\�", "\\�", "�", "�", "\\�", "�", "\\�", "," };
		for (int i = 0; i < caracteresEspeciais.length; i++) {
			str = str.replaceAll(caracteresEspeciais[i], "");
		}

		// Troca os espa�os no inicio e no fim por ""
		str = str.replaceAll("^\\s+", "");
		str = str.replaceAll("\\s+$", "");
		
		// Troca os espa�os duplicados, tabula��es e etc por "-"
		str = str.replaceAll("\\s+", "-");
		
		// Troca tudo por minusculo
		return str.toLowerCase();
	}

}