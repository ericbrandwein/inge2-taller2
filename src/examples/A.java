public class A {
    public static int main() {
        int i = 0;
        int j = 1;
        int d;
        d = j / i;
        if (d > 0)
            d = 1;

        int e = d;
        return e;
    }

    public int ejercicio1(int m, int n) {
        int x = 0;
        int j = m / (x * n);
        return j;
    }

    public int ejercicio2(int m, int n) {
        int x = n - n;
        int i = x + m;
        int j = m / x;
        return j;
    }

    public int ejercicio3(int m, int n) {
        int x = 0;
        if (m != 0) {
            x = m;
        } else {
            x = 1;
        }
        int j = n / x;
        return j;
    }

    public int ejercicio4(int m, int n) {
        int x = 0;
        int j = m / n;
        return j;
    }

    public int divisionSinWarning() {
        int x = 1;
        return 2 / x;
    }

    public int divisionConIfSinWarning(int x) {
        if (x > 1) {
            x = 1;
        } else {
            x = 2;
        }
        return 2 / x;
    }

    public int parametroMantieneSuPosibilidadDeSerCeroDespuesDeUnMerge(int x) {
        if (x > 1) {
            x = 1;
        }
        return 2 / x; // Debería haber warning
    }

    // Esto no compila porque java no te deja tener variables locales inicializadas.
    /*
    public int localMantieneSuPosibilidadDeSerCeroDespuesDeUnMerge() {
        int x;
        if (x > 1) {
            x = 1;
        }
        return 2 / x; // Debería haber warning
    }
     */
}
