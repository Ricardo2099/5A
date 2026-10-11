import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class ReadFileTest {

    @TempDir
    Path carpeta;

    @Test
    void leeDosValoresConEspacios() throws IOException {
        Path archivo = carpeta.resolve("entrada.txt");
        Files.writeString(archivo, " 1.1 \n 9 \n");
        assertArrayEquals(new double[] {1.1, 9}, ReadFile.leerDatos(archivo.toString()), 0);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "1.1\n", "1.1\n9\n3", "texto\n9", "1.1\n9.5",
            "NaN\n9", "Infinity\n9", "-1\n9", "1\n0", "1\n341", "\n9"})
    void rechazaArchivoInvalido(String contenido) throws IOException {
        Path archivo = carpeta.resolve("invalido.txt");
        Files.writeString(archivo, contenido);
        assertThrows(IllegalArgumentException.class, () -> ReadFile.leerDatos(archivo.toString()));
    }

    @Test
    void rechazaArchivoInexistente() {
        assertThrows(IOException.class, () -> ReadFile.leerDatos(carpeta.resolve("ausente.txt").toString()));
    }
}
