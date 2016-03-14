package org.scopemvc;

/**
 * Creates <code>Pointer</code> objects for references to the view model.
 * @author Aksel Hilde
 */
public class PointerFactory {

    public static Pointer getPointer(String pointerPath) {
        return new Pointer(pointerPath);
    }
}
