import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class OutFile {

    public static void escribirResultado(
            String nombreArchivo,
            List<String> lineas) throws IOException {
        Files.writeString(
                Path.of(nombreArchivo),
                String.join(System.lineSeparator(), lineas)
                        + System.lineSeparator(),
                StandardCharsets.UTF_8);
    }
}