import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class OutFileTest {

    @TempDir
    Path carpeta;

    @Test
    void escribeResultado() throws Exception {
        Path archivo = carpeta.resolve("resultado.txt");
        OutFile.escribirResultado(archivo.toString(), List.of("x: 1.1", "dof: 9"));
        assertEquals("x: 1.1" + System.lineSeparator()
                + "dof: 9" + System.lineSeparator(), Files.readString(archivo));
    }
}