public class PetersonLock {
    private volatile boolean[] flag = new boolean[2];
    private volatile int victim;

    public void lock(int id) {
        int i = id;
        int j = 1 - i;
        flag[i] = true;
        victim = i;
        while (flag[j] && victim == i) {
            // Espera activa
        }
    }

    public void unlock(int id) {
        flag[id] = false;
    }
}