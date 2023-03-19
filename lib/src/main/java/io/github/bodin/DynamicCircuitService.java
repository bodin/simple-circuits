package io.github.bodin;

import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class DynamicCircuitService implements CircuitService {
    private Set<String> openCircuits = Collections.emptySet();

    boolean isOpen(String circuitName) {
        return this.openCircuits.contains(circuitName);
    }

    @Override
    public Circuit create(String circuitName) {
        return new DynamicCircuit(circuitName, this);
    }

    public void reset(Collection<String> state){
        if(state == null){
            this.openCircuits = Collections.emptySet();
        }else{
            this.openCircuits = new HashSet<>(state);
        }
    }

    record DynamicCircuit(String name, DynamicCircuitService service) implements Circuit {
        @Override
        public boolean isOpen() {
            return service.isOpen(this.name);
        }
    }
}
