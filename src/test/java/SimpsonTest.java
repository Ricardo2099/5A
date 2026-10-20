import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class SimpsonTest {

    @ParameterizedTest
    @ValueSource(ints = {0, -2, 2_000_000})
    void rechazaSegmentosFueraDeLimites(int segmentos) {
        assertThrows(IllegalArgumentException.class,
                () -> Simpson.integrarConSegmentos(1.1, 9, segmentos));
    }

    private double calcular(double x, int dof) {
        return Simpson.integrar(x, dof);
    }

    @Test
    void caso1DelPdf() {
        assertEquals(0.35006, calcular(1.1, 9), 0.000005);
    }

    @Test
    void caso2DelPdf() {
        assertEquals(0.36757, calcular(-1.1812, 10), 0.000005);
    }

    @Test
    void caso3DelPdf() {
        assertEquals(0.49500, calcular(2.75, 30), 0.000005);
    }

    @Test
    void diezSegmentosCoincidenConExcel() {
        assertEquals(0.3500589042865572, Simpson.integrarConSegmentos(1.1, 9, 10), 1e-12);
    }

    @Test
    void veinteSegmentosCoincidenConPdf() {
        assertEquals(0.35005864, Simpson.integrarConSegmentos(1.1, 9, 20), 5e-9);
    }

    @Test
    void intervaloNulo() {
        assertEquals(0, Simpson.integrar(0, 9), 0);
    }

    @Test
    void distribucionConUnGradoDeLibertad() {
        // Para dof=1, la integral es atan(x)/pi.
        assertEquals(Math.atan(10) / Math.PI, Simpson.integrar(10, 1), 0.00001);
    }

    @Test
    void rechazaXNegativo() {
        assertThrows(IllegalArgumentException.class, () -> Simpson.integrar(-1, 9));
    }

    @Test
    void rechazaXNoFinito() {
        assertThrows(IllegalArgumentException.class, () -> Simpson.integrar(Double.NaN, 9));
    }

    @Test
    void rechazaGradosInvalidos() {
        assertThrows(IllegalArgumentException.class, () -> Simpson.integrar(1, 0));
    }

    @Test
    void rechazaSegmentosImpares() {
        assertThrows(IllegalArgumentException.class, () -> Simpson.integrarConSegmentos(1, 9, 3));
    }

    @Test
    void detieneCalculoSinConvergencia() {
        assertThrows(IllegalStateException.class, () -> Simpson.integrar(1e20, 9));
    }
}
