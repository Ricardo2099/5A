import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.management.ManagementFactory;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class MainTest {

    @TempDir
    Path carpeta;

    private record Resultado(int codigo, String salida) { }

    private Resultado ejecutar(String... argumentos) throws Exception {
        List<String> comando = new ArrayList<>();
        comando.add(Path.of(System.getProperty("java.home"), "bin", "java").toString());
        // El proceso separado permite probar System.exit sin cerrar JUnit.
        // Hereda el agente de JaCoCo para incluir Main en el mismo reporte.
        for (String opcion : ManagementFactory.getRuntimeMXBean().getInputArguments()) {
            if (opcion.startsWith("-javaagent:") && opcion.contains("jacoco")) {
                comando.add(opcion);
            }
        }
        comando.add("-cp");
        comando.add(Path.of(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).toString());
        comando.add("Main");
        comando.addAll(List.of(argumentos));
        Path salida = carpeta.resolve("consola.txt");
        Process proceso = new ProcessBuilder(comando).redirectErrorStream(true)
                .redirectOutput(salida.toFile()).start();
        try {
            if (!proceso.waitFor(20, TimeUnit.SECONDS)) {
                throw new IllegalStateException("Main no termino en 20 segundos.");
            }
            return new Resultado(proceso.exitValue(),
                    Files.readString(salida, StandardCharsets.UTF_8).replace("\r\n", "\n"));
        } finally {
            if (proceso.isAlive()) {
                proceso.destroyForcibly();
            }
        }
    }

    @ParameterizedTest
    @CsvSource({"datos/test1.txt,1.1,9,0.35005864,0.35006",
            "datos/test2.txt,1.1812,10,0.36757341,0.36757",
            "datos/test3.txt,2.75,30,0.49499986,0.49500"})
    void muestraResultadoDelArchivo(String archivo, String x, String dof,
            String integral, String redondeado) throws Exception {
        String esperado = "Archivo: " + archivo + "\nx: " + x + "\ndof: " + dof
                + "\nIntegral de 0 a x: " + integral
                + "\nResultado a 5 decimales: " + redondeado + "\n";
        assertEquals(new Resultado(0, esperado), ejecutar(archivo));
    }

    @Test
    void usaPrimerArchivoSinArgumentos() throws Exception {
        assertEquals(new Resultado(0, "Archivo: datos/test1.txt\nx: 1.1\ndof: 9\n"
                + "Integral de 0 a x: 0.35005864\nResultado a 5 decimales: 0.35006\n"), ejecutar());
    }

    @Test
    void informaUsoConArgumentosExtra() throws Exception {
        assertEquals(new Resultado(1, "Uso: java -cp target/classes Main [datos/test1.txt]\n"),
                ejecutar("datos/test1.txt", "extra"));
    }

    @Test
    void informaArchivoInexistente() throws Exception {
        Resultado resultado = ejecutar(carpeta.resolve("ausente.txt").toString());
        assertTrue(resultado.codigo() == 1 && resultado.salida().startsWith("Error: ")
                && resultado.salida().contains("ausente.txt"));
    }

    @Test
    void informaDatosInvalidos() throws Exception {
        Path archivo = carpeta.resolve("invalido.txt");
        Files.writeString(archivo, "1.1\n9.5\n");
        assertEquals(new Resultado(1, "Error: x debe ser numerico y dof debe ser entero.\n"),
                ejecutar(archivo.toString()));
    }

    @Test
    void informaFaltaDeConvergencia() throws Exception {
        Path archivo = carpeta.resolve("grande.txt");
        Files.writeString(archivo, "1e20\n9\n");
        assertEquals(new Resultado(1, "Error: No se alcanzo la tolerancia con el limite de segmentos.\n"),
                ejecutar(archivo.toString()));
    }
}
