import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class DistribucionTTest {

    @ParameterizedTest
    @ValueSource(doubles = {Double.NaN, Double.POSITIVE_INFINITY, 0, -1, 172})
    void rechazaArgumentosFueraDelDominioGamma(double z) {
        assertThrows(IllegalArgumentException.class, () -> DistribucionT.gamma(z));
    }

    @ParameterizedTest
    @ValueSource(doubles = {Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void rechazaDensidadConXNoFinito(double x) {
        assertThrows(IllegalArgumentException.class, () -> new DistribucionT(9).evaluar(x));
    }

    @Test
    void rechazaCeroGradosDeLibertad() {
        assertThrows(IllegalArgumentException.class, () -> new DistribucionT(0));
    }

    @Test
    void gammaEntera() {
        assertEquals(24, DistribucionT.gamma(5), 1e-12);
    }

    @Test
    void gammaSemientera() {
        assertEquals(11.63172839656745, DistribucionT.gamma(4.5), 1e-12);
    }

    @Test
    void gammaUnMedio() {
        assertEquals(Math.sqrt(Math.PI), DistribucionT.gamma(0.5), 1e-12);
    }

    @Test
    void gammaUno() {
        assertEquals(1, DistribucionT.gamma(1), 0);
    }

    @Test
    void rechazaGammaNoSemientera() {
        assertThrows(IllegalArgumentException.class, () -> DistribucionT.gamma(1.2));
    }

    @Test
    void densidadCoincideConExcel() {
        assertEquals(0.38803490887166864, new DistribucionT(9).evaluar(0), 1e-12);
    }

    @Test
    void rechazaGradosQueDesbordanGamma() {
        assertThrows(IllegalArgumentException.class, () -> new DistribucionT(341));
    }
}
