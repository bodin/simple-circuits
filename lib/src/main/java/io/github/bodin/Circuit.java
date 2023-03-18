package io.github.bodin;

public interface Circuit {

    boolean isOpen();

    default boolean isClosed(){
        return !this.isOpen();
    }
}
