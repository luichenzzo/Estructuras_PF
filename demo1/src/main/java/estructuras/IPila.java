package estructuras;

/**
 * Interfaz para la estructura de datos Pila (Stack) - LIFO
 * @param <T> Tipo de elemento que almacenará la pila
 */
public interface IPila<T> extends Iterable<T> {

    /**
     * Apila un elemento en el tope de la pila
     * @param elemento El elemento a apilar
     */
    void apilar(T elemento);

    /**
     * Desapila y retorna el elemento en el tope de la pila
     * @return El elemento en el tope
     * @throws java.util.EmptyStackException si la pila está vacía
     */
    T desapilar();

    /**
     * Retorna el elemento en el tope sin removerlo
     * @return El elemento en el tope
     * @throws java.util.EmptyStackException si la pila está vacía
     */
    T verTope();

    /**
     * Verifica si la pila está vacía
     * @return true si está vacía, false en caso contrario
     */
    boolean estaVacia();

    /**
     * Obtiene el tamaño de la pila
     * @return El número de elementos en la pila
     */
    int tamanio();

    /**
     * Limpia todos los elementos de la pila
     */
    void limpiar();

    /**
     * Busca un elemento en la pila
     * @param elemento El elemento a buscar
     * @return true si existe, false en caso contrario
     */
    boolean contiene(T elemento);

    /**
     * Busca la posición de un elemento desde el tope (0 = tope)
     * @param elemento El elemento a buscar
     * @return La posición desde el tope, o -1 si no existe
     */
    int buscar(T elemento);
}

