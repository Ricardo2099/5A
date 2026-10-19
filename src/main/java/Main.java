import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Main {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Uso: java -cp target/classes Main <x> <dof>");
            System.exit(1);
        }
        try {
            double x = Double.parseDouble(args[0]);
            int dof = Integer.parseInt(args[1]);
            double resultado = Simpson.integrar(x, dof);
            List<String> lineas = List.of(
                    "x: " + x,
                    "dof: " + dof,
                    String.format(Locale.ROOT, "Integral de 0 a x: %.8f", resultado),
                    String.format(Locale.ROOT, "Resultado a 5 decimales: %.5f", resultado));
            OutFile.escribirResultado("resultado.txt", lineas);
            for (String linea : lineas) {
                System.out.println(linea);
            }
            System.out.println("Resultado generado en: resultado.txt");
        } catch (NumberFormatException e) {
            System.err.println("Error: x debe ser numerico y dof debe ser entero.");
            System.exit(1);
        } catch (IOException | IllegalArgumentException | IllegalStateException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}
