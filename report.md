# Introducción

Nuestro análisis responde a la pregunta "¿Puede ser que en una línea de código en específico haya una división por cero en alguna corrida del programa?". Mostrar una advertencia en una línea quiere decir que puede haber una división por cero, y no mostrarla quiere decir que es imposible que en esa línea haya una división por cero.

Esto quiere decir que si queremos tener "soundness", debemos ser "conservadores" con el reporte: siempre que pueda haber una división por cero, aunque no estemos seguros, debemos mostrar una advertencia. Sólo en el caso que estemos seguros que una división tiene el divisor distinto de cero podemos no mostrar una advertencia.

"Completeness", en cambio, podríamos lograrlo si sólo mostrásemos advertencias en las líneas en las que ocurre una división por cero en alguna corrida del programa. Si mostramos una advertencia en alguna línea en la que no hay una división por cero en ninguna corrida posible del programa, no tendríamos completeness.

Como en la mayoría de los análisis estáticos de código vistos hasta ahora, sacrificaremos completeness a cambio de asegurar soundness, ya que es imposible lograr las dos al mismo tiempo en el caso general.

# Primera solución

Mi primer modelado del análisis fue utilizando la siguiente semántica de valores del reticulado:

- BOTTOM: Valor tomado cuando todavía la variable no fue asignada.
- ZERO/NOT_ZERO: Valor tomado cuando la variable es cero o no cero, según corresponda.
- MAYBE_ZERO: Valor tomado por una variable después de ser asignada un valor que puede ser tanto cero como no cero.

Esta conceptualización nos lleva a que los parámetros de una función tomarán el valor BOTTOM al principio, y que ni bien se haga alguna operación con ellas el resultado será MAYBE_ZERO.

Basándonos en esto, es claro que al encontrarnos con una división por una variable con valor ZERO o MAYBE_ZERO el analizador deberá mostrar una advertencia, pero, ¿debería mostrar una cuando se divide por una variable con valor BOTTOM? Si la respuesta es no, nos encontramos con que no estamos cumpliendo nuestro requerimiento de soundness como lo especificamos anteriormente, ya que en cualquier momento en el que haya una división por un parámetro no asignado tendremos una posible división por cero que no estaremos identificando, como en el siguiente ejemplo:

```c++
int ejemplo1(int x) {
    return 1 / x;
}
```

Además, lleva a inconsistencias evitables entre programas equivalentes. En el siguiente ejemplo se ve que, convirtiendo la función `ejemplo1` a una equivalente, logramos que nuestro analizador reporte el error:

```c++
int ejemplo1b(int x) {
    x = x + 0;
    return 1 / x;
}
```

Por lo tanto, nuestro analizador debe mostrar advertencias cuando se divide por BOTTOM. Pero esto lleva a otros problemas. Además de hacer que BOTTOM y MAYBE_ZERO tengan el mismo significado para nuestro analizador (los dos significan "esto puede ser cero"), nos lleva a casos como el siguiente:

```c++
int ejemplo2(int x, int y) {
    if (x == y) {
        x = 1
    }
    return y / x;
}
```

Si hiciéramos el grafo de control de flujo de `ejemplo2`, podríamos ver que después del `if` ocurre una fusión o "merge" de los valores de la variable `x`. Por la rama del `if`, `x` tendrá el valor NOT_ZERO, y por la otra rama tendrá el valor BOTTOM. Como la fusión resulta en el mínimo valor del reticulado mayor o igual a los dos valores fusionados, `x` tendrá valor NOT_ZERO en la última línea. Esto quiere decir que nuestro analizador no mostrará una advertencia en la última línea, ya que piensa que `x` no podrá nunca ser cero ahí. Pero esto no es cierto si usamos esta función llamándola con parámetros `(0, 1)`. Por lo tanto, no es suficiente mostrar una advertencia cuando hay una división por BOTTOM para que nuestro analizador sea sound.

# Segunda solución

Para resolver estos problemas, tomé inspiración de la solución al análisis similar de detección de signos de las variables, presentada en el libro Static Program Analysis de Anders Møller y Michael I. Schwartzbach, Capítulo 5 Sección 1, página 48. En ella, los valores del reticulado toman otros significados diferentes.

Los cambios realizados a la semántica son los siguientes. ZERO y NOT_ZERO mantienen sus significados, pero los otros no:

- BOTTOM será el valor tomado por las variables cuando no le interese al analizador el valor real de esa variable. Esto ocurrirá cuando el valor es el resultado de una división por cero, ya que el programa termina ni bien hay una división por cero.
- MAYBE_ZERO será el valor tomado por cualquier variable que interese saber el valor real pero no pueda ser definido. Esto incluye las variables que no fueron todavía asignadas.

Estos nuevos significados implican que toda variable, inicializada o no, tendrá un valor diferente de BOTTOM hasta que sea usada en una operación que produzca un error.

Veamos si resuelve esto nuestros problemas. En la función `ejemplo1`, ahora `x` tendrá valor MAYBE_ZERO antes de la línea de la división, y por lo tanto el analizador mostrará una advertencia. Ocurre lo mismo en `ejemplo1b`. Y, en la función `ejemplo2`, la fusión de los valores de `x` resultará en MAYBE_ZERO, ya que en la rama del `if` tendrá valor NOT_ZERO, como antes, y en la otra rama tendrá valor MAYBE_ZERO. Por lo tanto, también nuestro analizador mostrará una advertencia en este caso. Logramos cumplir soundness, aparentemente.

# Resultados de las operaciones

La semántica que elegí define las reglas de resultados para las operaciones que se verán a continuación.

El supremo entre dos valores de un reticulado es el mínimo valor del reticulado que es mayor o igual a los dos valores, y eso no se ve afectado por nuestra elección de siginificado para los valores.

Todas las operaciones con BOTTOM resultan en BOTTOM. Si una variable tiene valor BOTTOM, quiere decir que el programa nunca llegará a tener un valor para esa variable en ese punto, y por lo tanto tampoco tendrá un valor para el resultado de cualquier operación con esa variable. Veamos los resultados de las operaciones entre los tres valores restantes: ZERO, NOT_ZERO, y MAYBE_ZERO.

## Suma
- La suma con ZERO resulta en el mismo valor que se está sumando.
- La suma entre NOT_ZEROs resulta en MAYBE_ZERO, ya que podría una variable ser el opuesto de la otra o no.
- La suma con MAYBE_ZERO resulta en MAYBE_ZERO, ya que la variable con MAYBE_ZERO podría ser el opuesto de la otra o no.

## Resta
- Si el segundo operador es ZERO, el resultado es el mismo que el del primer operador.
- Si el primer operador es ZERO, también, ya que no distinguimos entre el signo de las variables.
- La resta entre NOT_ZEROs resulta en MAYBE_ZERO, ya que podrían ser las dos variables iguales o no.
- La resta con cualquier operador MAYBE_ZERO resulta en MAYBE_ZERO, ya que podría ser igual al otro operador o no.

## Multiplicación
- La multiplicación por ZERO resulta en ZERO.
- La multiplicación entre NOT_ZEROs resulta en NOT_ZERO.
- La multiplicación por MAYBE_ZERO resulta en MAYBE_ZERO.

## División
- La división por ZERO resulta en BOTTOM, porque el programa nunca asignará ningún valor a ese resultado, ya que habrá terminado con un error.
- La división entre NOT_ZEROs resulta en MAYBE_ZERO, por el redondeo de C++ al dividir dos valores enteros. Si hiciéramos, por ejemplo, 1 / 2, el resultado sería 0.
- En los otros casos, el resultado de dividir por NOT_ZERO mantiene el valor del dividendo.
- Dividir por MAYBE_ZERO resulta en MAYBE_ZERO. Es cierto que es posible que haya una división por cero en este nodo, pero también podría ocurrir que no, y por lo tanto nos interesa seguir analizando después del mismo. Esto nos obliga a no usar BOTTOM como el resultado de esta operación.

# Implementación

Para lograr que los parámetros comiencen con valor MAYBE_ZERO, eliminé las líneas del constructor de `DivisionByZeroAnalysis` que inicializaban las variables locales con BOTTOM, puse MAYBE_ZERO como valor por defecto de `resolvedValue` en el `ZeroLatticeValueVisitor`, e hice que `VariableToLatticeMap` devuelva MAYBE_ZERO para una variable todavía no existente en el mapa.

Para definir los resultados de las operaciones, sobreescribí en cada elemento del enumerado `ZeroLattice` las funciones de operaciones. Además, utilicé *double dispatch* sobre los valores de los operandos, eliminando la programación procedural por una alternativa más orientada a objetos. Esto genera muchos métodos (4 por operación por cada valor del reticulado, es decir 4 * 4 * 4 = 64) y repite resultados para las operaciones conmutativas como la suma, pero permite tener un código muy fácil de modificar, lo cual me sirvió cuando cambié de la primera a la segunda solución.

Para probar los resultados de la implementación, agregué al archivo `A.java` los ejercicios presentados en el enunciado y otras funciones que me parecieron útiles.

# Organización de los archivos

Los resultados se pueden ver en el archivo `A.jimple`. En el directorio `src/` se puede ver el proyecto modificado basado en el provisto por la cátedra, con el archivo `A.java` en el directorio `src/examples/`. El archivo `src/ejecutar.sh`, cuando se ejecuta desde el directorio `src/`, corre el analizador sobre el código del archivo `A.java`, guarda los resultados en `src/sootOutput/A.jimple`, y los muestra por pantalla.

# Conclusión

Es fácil implementar un analizador con casos bordes que rompan su soundness, y esto ocurre simplemente por la gran cantidad de casos a revisar. La primera solución parecía razonable, pero igualmente vimos que nos llevaba a inconsistencias. Además, este analizador maneja un reticulado con solo cuatro valores posibles. En un análisis más sofisticado, con muchos más valores, la probabilidad de error aumenta exponencialmente, y por lo tanto la prueba constante de nuestras decisiones con casos reales se vuelve indispensable.


