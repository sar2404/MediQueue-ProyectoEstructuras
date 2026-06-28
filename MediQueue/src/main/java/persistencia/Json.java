package persistencia;

// lectura de valores de una linea json escrita a mano
public class Json {

    // devuelve el valor de la clave en la linea
    public static String valorDe(String linea, String clave) {

        int i = linea.indexOf("\"" + clave + "\"");
        i = linea.indexOf(":", i) + 1;
        while (linea.charAt(i) == ' ') {
            i++;
        }

        // si empieza con comilla es texto, sino numero
        if (linea.charAt(i) == '"') {
            int fin = linea.indexOf("\"", i + 1);
            return linea.substring(i + 1, fin);
        } else {
            int fin = i;
            while (fin < linea.length()) {
                char c = linea.charAt(fin);
                if (c == ',' || c == '}' || c == ' ') {
                    break;
                }
                fin++;
            }
            return linea.substring(i, fin);
        }
    }
}
