package estructuras;

/**
 * Interfaz base para todas las estructuras de tipo Lista
 * @param <T> Tipo de elemento que almacenará la lista
 */
public interface Lista<T> extends Iterable<T> {

    /**
     * Agrega un elemento al final de la lista
     * @param elemento El elemento a agregar
     */
    void agregar(T elemento);

    /**
     * Agrega un elemento en una posición específica
     * @param elemento El elemento a agregar
     * @param indice La posición donde agregar
     */
    void agregar(T elemento, int indice);

    /**
     * Elimina un elemento de la lista
     * @param elemento El elemento a eliminar
     * @return true si se eliminó, false si no se encontró
     */
    boolean eliminar(T elemento);

    /**
     * Elimina el elemento en una posición específica
     * @param indice La posición del elemento a eliminar
     * @return El elemento eliminado
     */
    T eliminarEn(int indice);

    /**
     * Obtiene el elemento en una posición específica
     * @param indice La posición del elemento
     * @return El elemento en esa posición
     */
    T obtener(int indice);

    /**
     * Busca un elemento en la lista
     * @param elemento El elemento a buscar
     * @return true si existe, false en caso contrario
     */
    boolean contiene(T elemento);

    /**
     * Obtiene el índice de un elemento
     * @param elemento El elemento a buscar
     * @return El índice del elemento o -1 si no existe
     */
    int indiceDe(T elemento);

    /**
     * Verifica si la lista está vacía
     * @return true si está vacía, false en caso contrario
     */
    boolean estaVacia();

    /**
     * Obtiene el tamaño de la lista
     * @return El número de elementos en la lista
     */
    int tamanio();

    /**
     * Limpia todos los elementos de la lista
     */
    void limpiar();
}
