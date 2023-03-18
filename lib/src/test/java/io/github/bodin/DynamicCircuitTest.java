/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package io.github.bodin;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DynamicCircuitTest {
    @Test void testDynamicCircuit() {
        DynamicCircuitService service = new DynamicCircuitService();
        Circuit dynamicCircuit = service.create("dynamic-circuit");

        assertFalse(service.isOpen("dynamic-circuit"));
        assertFalse(dynamicCircuit.isOpen());

        service.open("foo");

        assertFalse(service.isOpen("dynamic-circuit"));
        assertFalse(dynamicCircuit.isOpen());

        service.open("dynamic-circuit");

        assertTrue(service.isOpen("dynamic-circuit"));
        assertTrue(dynamicCircuit.isOpen());

        service.close("dynamic-circuit");

        assertFalse(service.isOpen("dynamic-circuit"));
        assertFalse(dynamicCircuit.isOpen());
    }
}