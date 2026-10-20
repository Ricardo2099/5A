
public class Gamma {

    private Gamma() {

    }

    public static double gamma(double z) {

        if (z < 0.5 || z > 171) {
            throw new IllegalArgumentException("Valor fuera del rango permitido.");
        }
        if (!Double.isFinite(z)) {
            throw new IllegalArgumentException("El numero no es valido.");
        }
        double doble = z * 2;
        if (doble != Math.rint(doble)) {
            throw new IllegalArgumentException("Solo se permiten enteros y semienteros.");
        }

        double resultado = 1.0;
        double numero = z;

        while (numero > 1) {
            numero = numero - 1;
            resultado = resultado * numero;
        }
        if (numero == 0.5) {
            resultado = resultado * Math.sqrt(Math.PI);
        }

        return resultado;
    }
}
