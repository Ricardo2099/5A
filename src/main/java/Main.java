import java.io.IOException;
import java.util.Locale;

public class Main {

    public static void main(String[] args) {
        if (args.length > 1) {
            System.err.println("Uso: java -cp target/classes Main [datos/test1.txt]");
            System.exit(1);
        }
        String archivo = args.length == 0 ? "datos/test1.txt" : args[0];
        try {
            double[] datos = ReadFile.leerDatos(archivo);
            double x = datos[0];
            int dof = (int) datos[1];
            double resultado = Simpson.integrar(x, dof);
            System.out.println("Archivo: " + archivo);
            System.out.println("x: " + x);
            System.out.println("dof: " + dof);
            System.out.printf(Locale.ROOT, "Integral de 0 a x: %.8f%n", resultado);
            System.out.printf(Locale.ROOT, "Resultado a 5 decimales: %.5f%n", resultado);
        } catch (IOException | IllegalArgumentException | IllegalStateException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}
