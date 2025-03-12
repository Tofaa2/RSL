package me.tofaa.rsl.ast;

public interface Expression extends Statement {

    default boolean isNull() {
        return false;
    }

}
