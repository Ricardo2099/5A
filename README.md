# Programa 5A: regla de Simpson

Calcula la integral de la densidad t de Student **desde 0 hasta x**.
Este resultado es el area entre 0 y x, no la probabilidad acumulada desde menos infinito.
Requiere JDK 17 o posterior y Maven. No requiere Excel ni librerias matematicas externas.

## Ejecucion

Desde la carpeta del repositorio:

```text
cd 5A
mvn compile
java -cp target/classes Main 1.1 9
java -cp target/classes Main 1.1812 10
java -cp target/classes Main 2.75 30
```

Tambien se puede ejecutar mediante Maven:

```text
mvn compile exec:java "-Dexec.args=1.1812 10"
```

Main requiere dos argumentos: primero `x` con punto decimal y luego `dof` entero.
La salida se muestra en consola y se guarda en `resultado.txt` mediante `OutFile`.
Ante una entrada invalida o falta de convergencia, muestra el error y termina con
codigo distinto de cero.

## Clases

- `Main`: recibe `x` y `dof` por argumentos y muestra los resultados.
- `OutFile`: guarda los resultados calculados en `resultado.txt`.
- `DistribucionT`: calcula la densidad t de Student usando `Gamma`.
- `Gamma`: calcula gamma manualmente con multiplicaciones; no usa una libreria de gamma.
- `Simpson`: aplica Simpson y controla la convergencia.

## Calculos

Para v = dof:

```text
f(t) = Gamma((v+1)/2) / (sqrt(v*pi) * Gamma(v/2))
       * (1 + t*t/v)^(-(v+1)/2)
w = x / segmentos
p = w/3 * [f(0) + 4*f(w) + 2*f(2*w) + ... + 4*f(x-w) + f(x)]
```

Se empieza con 10 segmentos, se repite con 20 y se compara la diferencia
absoluta. Se siguen duplicando los segmentos hasta que la diferencia entre
dos resultados consecutivos sea menor que 0.00001. Se devuelve el ultimo
resultado, sin redondear los pasos intermedios. Se permiten como maximo
1,310,720 segmentos; si no converge, se informa un error.

Gamma se calcula con Gamma(z) = (z-1)*Gamma(z-1), usando las bases
Gamma(1)=1 y Gamma(1/2)=sqrt(pi). Para dof entero solo se necesitan argumentos
enteros o semienteros. Esta implementacion sencilla admite dof de 1 a 340
para evitar desbordamientos de gamma en double; no es una funcion gamma
general. x debe ser finito y no negativo.

## Referencias revisadas

`5a.pdf`: tabla 1 (pagina 4 del archivo), procedimiento de Simpson (pagina 5),
gamma (paginas 8-9) y ejemplo con 10/20 segmentos (paginas 10-12).

`5a_prototype.xlsx`, hoja `t-student`: H21 contiene
0.3500589042865572 para x=1.1, dof=9 y 10 segmentos.
**La segunda iteracion del Excel cambia dof a 19 en C31**, por lo que H58
(0.35746278990455704) no representa el refinamiento del mismo caso.
El programa sigue el PDF: conserva dof=9 y duplica unicamente los segmentos.
Con 20 segmentos, el PDF indica 0.35005864.

## JUnit y JaCoCo

```text
mvn test
mvn verify
```

`mvn test` ejecuta JUnit. `mvn verify` tambien ejecuta JUnit y genera el reporte
en `target/site/jacoco/index.html`. Abrir ese archivo en un navegador.
En PowerShell:

```powershell
Start-Process .\target\site\jacoco\index.html
```

Los tres casos obligatorios pasan directamente los valores de `x` y `dof` y comparan
contra constantes del PDF con tolerancia 0.000005 (media unidad del quinto
decimal). Esta tolerancia de comparacion es distinta del criterio de parada
de Simpson. Hay pruebas adicionales de gamma, valores intermedios del
Excel/PDF, integral con dof=1, entradas invalidas y limite de iteraciones.
Cada metodo de prueba contiene exactamente una asercion.

`MainTest` ejecuta el programa en procesos Java separados y comprueba la salida
y el codigo de terminacion: los tres pares de valores, argumentos faltantes o
extra, datos invalidos y falta de convergencia.
Cada proceso hereda el agente JaCoCo de Maven, de modo que sus ejecuciones se
incorporan al reporte sin que `System.exit` cierre JUnit.

Verificacion con `mvn clean verify`: 52 pruebas aprobadas; cobertura total de
392/405 instrucciones (96.8%), 70/75 lineas (93.3%) y 47/48 ramas (97.9%).
Todas las ramas de Main estan cubiertas. Las llamadas a `System.exit` pueden
seguir rojas porque no retornan al punto de instrumentacion, aunque se verifica
el codigo de salida. Tambien quedan sin ejecutar los constructores implicitos
de las clases que solo contienen metodos estaticos. No hay exclusiones de clases
para aumentar la cobertura.

| Entrada | x | dof | Esperado (5 decimales) |
|---|---:|---:|---:|
| datos/test1.txt | 1.1 | 9 | 0.35006 |
| datos/test2.txt | 1.1812 | 10 | 0.36757 |
| datos/test3.txt | 2.750 | 30 | 0.49500 |

La estructura estandar Maven y las pruebas sin interaccion permiten agregar
GitHub Actions posteriormente. No se incluyen workflows ni pipelines.
`target/` esta excluido mediante `.gitignore`.
