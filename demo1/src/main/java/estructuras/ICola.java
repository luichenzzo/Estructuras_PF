package estructuras;

/**
 * Interfaz para la estructura de datos Cola (Queue) - FIFO
 * @param <T> Tipo de elemento que almacenará la cola
 */
public interface ICola<T> extends Iterable<T> {

    /**
     * Encola un elemento al final de la cola
     * @param elemento El elemento a encolar
     */
    void encolar(T elemento);

    /**
     * Desencola y retorna el elemento al frente de la cola
     * @return El elemento al frente
     * @throws java.util.NoSuchElementException si la cola está vacía
     */
    T desencolar();

    /**
     * Retorna el elemento al frente sin removerlo
     * @return El elemento al frente
     * @throws java.util.NoSuchElementException si la cola está vacía
     */
    T verFrente();

    /**
     * Retorna el elemento al final sin removerlo
     * @return El elemento al final
     * @throws java.util.NoSuchElementException si la cola está vacía
     */
    T verFinal();

    /**
     * Verifica si la cola está vacía
     * @return true si está vacía, false en caso contrario
     */
    boolean estaVacia();

    /**
     * Obtiene el tamaño de la cola
     * @return El número de elementos en la cola
     */
    int tamanio();

    /**
     * Limpia todos los elementos de la cola
     */
    void limpiar();

    /**
     * Busca un elemento en la cola
     * @param elemento El elemento a buscar
     * @return true si existe, false en caso contrario
     */
    boolean contiene(T elemento);
}

