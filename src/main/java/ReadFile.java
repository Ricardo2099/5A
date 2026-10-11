import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ReadFile {

    // Reutilizado de ReadFile de 2A/3A.
    public static List<String> leerLineas(String nombreArchivo) throws IOException {
        return Files.readAllLines(Path.of(nombreArchivo), StandardCharsets.UTF_8);
    }

    public static double[] leerDatos(String nombreArchivo) throws IOException {
        List<String> lineas = leerLineas(nombreArchivo);
        if (lineas.size() != 2) {
            throw new IllegalArgumentException("El archivo debe contener dos lineas: x y dof.");
        }
        try {
            double x = Double.parseDouble(lineas.get(0).trim());
            int dof = Integer.parseInt(lineas.get(1).trim());
            Simpson.validarDatos(x, dof);
            return new double[] {x, dof};
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("x debe ser numerico y dof debe ser entero.");
        }
    }
}
