public class DistribucionT {

    private final int dof;
    private final double constante;

    public DistribucionT(int dof) {
        if (dof < 1 || dof > 340) {
            throw new IllegalArgumentException("dof debe estar entre 1 y 340.");
        }
        this.dof = dof;
        constante = gamma((dof + 1) / 2.0)
                / (Math.sqrt(dof * Math.PI) * gamma(dof / 2.0));
    }

    public double evaluar(double x) {
        if (!Double.isFinite(x)) {
            throw new IllegalArgumentException("x debe ser finito.");
        }
        return constante * Math.pow(1 + x * x / dof, -(dof + 1) / 2.0);
    }

    public static double gamma(double z) {
        if (!Double.isFinite(z) || z < 0.5 || z > 171 || z * 2 != Math.rint(z * 2)) {
            throw new IllegalArgumentException("Gamma admite enteros y semienteros de 0.5 a 171.");
        }
        // Gamma(z) = (z - 1) * Gamma(z - 1).
        // Bases: Gamma(1) = 1 y Gamma(1/2) = sqrt(pi).
        double resultado = 1;
        while (z > 1) {
            z--;
            resultado *= z;
        }
        return z == 0.5 ? resultado * Math.sqrt(Math.PI) : resultado;
    }
}
