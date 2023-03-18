package io.github.bodin;

import java.util.HashSet;
import java.util.Set;

public class StaticCircuitService implements CircuitService {
    private Set<String> openCircuits;

    public StaticCircuitService(Set<String> openCircuits) {
        this.openCircuits = new HashSet<>(openCircuits);
    }

    @Override
    public Circuit create(String circuitName) {
        return new StaticCircuit(this.openCircuits.contains(circuitName));
    }
}
