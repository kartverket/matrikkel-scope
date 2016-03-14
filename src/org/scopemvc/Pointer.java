package org.scopemvc;

import org.scopemvc.core.Selector;

/**
 * This is a wrapper for Scope 1.0 <code>Selector</code> objects. Used for forward compability with Scope 2.0.
 *
 * @author Aksel Hilde
 */
public class Pointer {
    private Selector selector;

    /**
     * Package private ctor for the factory. Application code should use {@link
     * PointerFactory#getPointer} to create Pointers.
     */

    Pointer(String path) {
        selector = Selector.fromString(path);
    }

    /**
     * Should not be used by the application. Only for use by Scope 2.0 compability code...
     * @return the <code>Selector</code> wrapped by this Pointer
     */
    public Selector getSelector() {
        return selector;
    }
}
