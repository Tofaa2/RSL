package me.tofaa.rsl;

public enum Operation {

    PLUS,
    MULTIPLY,
    MINUS,
    MOD,
    DIVIDE,

    ;

    public int a = 1000;

    public Number perform(Number n1, Number n2) {
        switch (this) {
            case MOD -> {
                return n1.doubleValue() % n2.doubleValue();
            }
            case PLUS -> {
                return n1.doubleValue() + n2.doubleValue();
            }
            case MINUS-> {
                return n1.doubleValue() - n2.doubleValue();
            }
            case DIVIDE -> {
                return n1.doubleValue() / n2.doubleValue();
            }
            case MULTIPLY -> {
                return n1.doubleValue() * n2.doubleValue();
            }
        }
        return 0;
    }

}
