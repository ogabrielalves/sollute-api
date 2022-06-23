package sollute.estoquecerto.entity;

public class FilaObj<T> {

    private int tamanho;
    private T[] fila;

    public FilaObj(int capacidade) {
        fila = (T[]) new Object[capacidade];
        tamanho = 0;
    }

    public boolean isEmpty() {
        return tamanho == 0;
    }

    public boolean isFull() {
        return tamanho == fila.length;
    }

    public void insert(T info) {
        if (isFull()) {
            throw new IllegalStateException("Fila cheia!");
        }
        fila[tamanho++] = info;
    }

    public T peek() {
        return fila[0];
    }

    public T poll() {
        T primeiro = peek();

        if (!isEmpty()) {

            for (int i = 0; i < tamanho - 1; i++) {
                fila[i] = fila[i + 1];
            }

            fila[tamanho--] = null;
        }

        return primeiro;
    }

    public void exibe() {
        if (isEmpty()) {
            System.out.println("Fila vazia");
        } else {
            for (int i = 0; i < tamanho; i++) {
                System.out.println(fila[i]);
            }
        }
    }

    public int getTamanho() {
        return tamanho;
    }

}