package io.github.bodin;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class DynamicCircuitService implements CircuitService {
    private Set<String> openCircuits = Collections.synchronizedSet(new HashSet());

    boolean isOpen(String circuitName) {
        return this.openCircuits.contains(circuitName);
    }

    @Override
    public Circuit create(String circuitName) {
        return new DynamicCircuit(circuitName, this);
    }

    public void open(String circuitName){
        this.openCircuits.add(circuitName);
    }

    public void close(String circuitName){
        this.openCircuits.remove(circuitName);
    }

    public void reset(){
        this.openCircuits.clear();
    }

    record DynamicCircuit(String name, DynamicCircuitService service) implements Circuit {
        @Override
        public boolean isOpen() {
            return service.isOpen(this.name);
        }
    }
}
