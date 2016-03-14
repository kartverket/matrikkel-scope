package org.scopemvc.view.swing;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scopemvc.core.*;
import org.scopemvc.util.Debug;

import javax.swing.*;

/**
 * A simple implementation of the {@link org.scopemvc.core.View} interface for views that listens directly
 * for {@link org.scopemvc.core.ModelChangeEvent}s from their model.
 *
 * @author Aksel Hilde
 */
public abstract class SimpleView extends JPanel implements View, ModelChangeListener {
   private static final Log LOG = LogFactory.getLog(SimpleView.class);

   private Object boundModel;
   private Controller controller;
   private PropertyManager manager;

   public Object getBoundModel() {
      return boundModel;
   }

   public void setBoundModel(Object inModel) {
      if( LOG.isDebugEnabled() ) {
         LOG.debug("setBoundModel: " + inModel);
      }

      if( getBoundModel() instanceof ModelChangeEventSource ) {
         ((ModelChangeEventSource) getBoundModel()).removeModelChangeListener(this);
      }
      if( inModel instanceof ModelChangeEventSource ) {
         ((ModelChangeEventSource) inModel).addModelChangeListener(this);
      }

      manager = null;
      if( inModel != null ) {
         manager = PropertyManager.getInstance(inModel);
      }

      this.boundModel = inModel;
      boundModelChanged();
   }

   public Controller getController() {
      return controller;
   }

   public void setController(Controller controller) {
      this.controller = controller;
   }

   public void modelChanged(final ModelChangeEvent inEvent) {
      if( Debug.ON ) {
         Debug.assertTrue(getBoundModel() instanceof ModelChangeEventSource, "boundModel not a Model");
      }

      if( LOG.isDebugEnabled() ) {
         LOG.debug("modelChanged: view: " + this + ", event: " + inEvent);
         LOG.debug("modelChanged: event selector: " + Selector.asString(inEvent.getSelector()));
      }

      // Event affects us if a Model changes somewhere at or above us in the hierarchy.
      if( inEvent.getModel() == getBoundModel() ) {
         if( LOG.isDebugEnabled() ) {
            LOG.debug("modelChanged: for me!");
         }

         if (SwingUtilities.isEventDispatchThread()) {
            updateFromModel(inEvent.getSelector());
         } else {
            Runnable runnable = new Runnable() {
               public void run() {
                  updateFromModel(inEvent.getSelector());
               }
            };
            SwingUtilities.invokeLater(runnable);
         }
      }
   }

   /**
    * This method should be overridden by sub classes to get notification when some part of the model has changed.
    * @param selector the pointer to the model element that has changed.
    */
   protected abstract void updateFromModel(Selector selector);

   /**
    * This method should be overridden by sub classes to get notification when the bound model has changed.
    *
    * This method is also called when the bound model is set the first time.
    */
   protected abstract void boundModelChanged() ;

   public void issueControl(Control inControl) {
      SwingUtil.issueControl(this, inControl);
   }
}
