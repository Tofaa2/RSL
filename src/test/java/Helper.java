public final class Helper {


    static long executionTime(Runnable runnable) {
        long now = System.currentTimeMillis();
        runnable.run();
        return System.currentTimeMillis() - now;
    }
}
