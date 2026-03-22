public class Peterson4Threads {
    private PetersonLock[] tree = new PetersonLock[3];

    public Peterson4Threads() {
        for (int i = 0; i < 3; i++) {
            tree[i] = new PetersonLock();
        }
    }

    public void lock(int threadId) {
        // Nivel 0: Los hilos 0-1 compiten en tree[0], 2-3 en tree[1]
        int node = threadId / 2;
        int idInNode = threadId % 2;
        tree[node].lock(idInNode);

        // Nivel 1: Los ganadores de la ronda anterior compiten en tree[2]
        tree[2].lock(node);
    }

    public void unlock(int threadId) {
        // Liberar en orden inverso: de la final a la primera ronda
        tree[2].unlock(threadId / 2);
        tree[threadId / 2].unlock(threadId % 2);
    }
}