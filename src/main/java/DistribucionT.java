public class DistribucionT {

    private final int dof;
    private final double constante;

    public DistribucionT(int dof) {
        if (dof < 1 || dof > 340) {
            throw new IllegalArgumentException("dof debe estar entre 1 y 340.");
        }
        this.dof = dof;
        constante = Gamma.gamma((dof + 1) / 2.0)
                / (Math.sqrt(dof * Math.PI) * Gamma.gamma(dof / 2.0));
    }

    public double evaluar(double x) {
        if (!Double.isFinite(x)) {
            throw new IllegalArgumentException("x debe ser finito.");
        }
        return constante * Math.pow(1 + x * x / dof, -(dof + 1) / 2.0);
    }

}
