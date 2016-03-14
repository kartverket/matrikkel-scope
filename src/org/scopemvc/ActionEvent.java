package org.scopemvc;

import org.scopemvc.core.Control;

/**
 * This is a wrapper for {@link org.scopemvc.core.Control} objects in Scope 1.0.1. Raison d'etre is forward compability and easy
 * migration to Scope 2.0.
 *
 * @author Aksel Hilde
 */
public class ActionEvent {
   private Control control;
   private String eventmask;
   private int modifier;

   /**
    * Create an actionEvent that wrapps a <code>Control</code>
    * @param control the Scope 1.0 <code>Control</code> that messages will be forwarded to.
    * @deprecated bruk ActionEvent(Object source, String actionId)
    */
   public ActionEvent(Control control) {
      this.control = control;
   }

   public ActionEvent(String eventmask, String actionevent, Object parameter, int modifier) {
      control = new Control(actionevent, parameter);
      this.eventmask = eventmask;
      this.modifier = modifier;
   }

   /**
    * Create an actionEvent that wrapps a Scope 1.0 <code>Control</code>
    * @param source The object where the event originated, not used here just for forward compability with Scope 2.0
    * @param actionId The unique ID identifying the ActionEvent
    */
   public ActionEvent(Object source, String actionId) {
      this.control = new Control(actionId);
   }

   /**
    * Should not be used by the application. Only for use by Scope 2.0 compability code...
    * @return the <code>Control</code> wrapped by this ActionEvent
    */
   public Control getControl() {
      return control;
   }

   /**
    * Forwards the call directly to the wrapped control.
    */
   public String getID() {
      return control.getID();
   }

   /**
    * Forwards the call directly to the wrapped control.
    */
   public Object getParameter() {
      return control.getParameter();
   }

   /**
    * Forwards the call directly to the wrapped control.
    */
   public void setParameter(Object object) {
      control.setParameter(object);
   }

   /**
    * For compability with Scope 2.0<p>
    *
    * Forwards the call to {@link org.scopemvc.core.Control#isMatched isMatched} on the wrapped control.
    */
   public boolean isConsumed() {
      return control.isMatched();
   }

   /**
    * For compability with Scope 2.0<p>
    *
    * Forwards the call to {@link org.scopemvc.core.Control#markMatched markMatched} on the wrapped control.
    */
   public void consume() {
      control.markMatched();
   }

   /**
    * Tests if this ActionEvent matches the given ID. <br>
    * Use this method in Controller's doHandleControl to discover Controls that
    * you want to handle. <p>
    *
    * <i>Unlike Scope 1.0.1 you must use <code>consume()</code> when you want to mark til event as handled and
    * avoid passing it up the chain-of-responsibility!</i>
    *
    * @param actionID The ID to test against
    * @return true if this control ID matches the passed ID.
    */
   public final boolean matchesID(String actionID) {
      //assert (control.getID()!=null);

      if (actionID == null) {
         throw new IllegalArgumentException("Can't match against a null ID.");
      }

      return (control.getID().equals(actionID));
   }

   public String getEventmask() {
      return eventmask;
   }

   public int getModifier() {
      return modifier;
   }

   public void setEventmask(String eventmask) {
      this.eventmask = eventmask;
   }

   public void setModifier(int modifier) {
      this.modifier = modifier;
   }

   public final boolean matchesMask(String mask) {
      if(eventmask == null) return false;
      else return eventmask.equals(mask);
   }

}
