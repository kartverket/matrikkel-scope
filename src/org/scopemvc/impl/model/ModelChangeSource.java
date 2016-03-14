package org.scopemvc.impl.model;

/**
 * This interface is created just for forward compability with Scope 2.0. No metods here for now...
 *
 * @author Aksel Hilde
 */
public interface ModelChangeSource {

    /**
     * Return the wrapper for this model or sub-model
     *
     * @return the wrapper
     */
    //ModelWrapper getModelWrapper();

    /**
     * Sets the wrapper for this model or sub-model
     * 
     * @param wrapper The model wrapper
     */    
    //void setModelWrapper(ModelWrapper wrapper);

    /**
     * Gets the pointer that gives the path to the root of the model
     * 
     * @return the pointer to the root of the model if this is a sub-model, else null if this is the root model
     */
    //Pointer getPointerToRoot();
    
    /**
     * Sets the pointer to the root of the model
     * 
     * @param pointer The pointer to the root of the model 
     */
    //void setPointerToRoot(Pointer pointer);
}
