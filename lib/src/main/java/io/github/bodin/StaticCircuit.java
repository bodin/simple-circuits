package io.github.bodin;

public record StaticCircuit(boolean open) implements Circuit {
    @Override
    public boolean isOpen() {
        return this.open;
    }
}
