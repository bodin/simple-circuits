package io.github.bodin;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

public interface Circuit {

    boolean isOpen();

    default boolean isClosed(){
        return !this.isOpen();
    }

    default <T> Optional<T> call(Callable<T> c) throws Exception {
        if(this.isOpen()) return Optional.empty();
        return Optional.of(c.call());
    }
    default <T> Optional<T> supply(Supplier<T> s) {
        if(this.isOpen()) return Optional.empty();
        return Optional.of(s.get());
    }

    default boolean run(Runnable r) {
        if(this.isOpen()) return false;
        r.run();
        return true;
    }
}
