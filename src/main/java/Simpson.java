public class Simpson {

    public static final double TOLERANCIA = 0.00001;
    private static final int MAX_SEGMENTOS = 1_310_720;

    public static void validarDatos(double x, int dof) {
        if (!Double.isFinite(x) || x < 0) {
            throw new IllegalArgumentException("x debe ser finito y mayor o igual a cero.");
        }
        if (dof < 1 || dof > 340) {
            throw new IllegalArgumentException("dof debe estar entre 1 y 340.");
        }
    }

    public static double integrar(double x, int dof) {
        validarDatos(x, dof);
        DistribucionT distribucion = new DistribucionT(dof);
        int segmentos = 10;
        double anterior = calcular(x, segmentos, distribucion);
        while (segmentos < MAX_SEGMENTOS) {
            segmentos *= 2;
            double actual = calcular(x, segmentos, distribucion);
            if (Double.isFinite(actual) && Math.abs(actual - anterior) < TOLERANCIA) {
                return actual;
            }
            anterior = actual;
        }
        throw new IllegalStateException("No se alcanzo la tolerancia con el limite de segmentos.");
    }

    public static double integrarConSegmentos(double x, int dof, int segmentos) {
        validarDatos(x, dof);
        if (segmentos < 2 || segmentos % 2 != 0 || segmentos > MAX_SEGMENTOS) {
            throw new IllegalArgumentException("Los segmentos deben ser pares, entre 2 y " + MAX_SEGMENTOS);
        }
        return calcular(x, segmentos, new DistribucionT(dof));
    }

    private static double calcular(double x, int segmentos, DistribucionT distribucion) {
        double ancho = x / segmentos;
        double suma = distribucion.evaluar(0) + distribucion.evaluar(x);
        for (int i = 1; i < segmentos; i++) {
            int coeficiente = i % 2 == 0 ? 2 : 4;
            suma += coeficiente * distribucion.evaluar(i * ancho);
        }
        return (ancho / 3) * suma;
    }
}
