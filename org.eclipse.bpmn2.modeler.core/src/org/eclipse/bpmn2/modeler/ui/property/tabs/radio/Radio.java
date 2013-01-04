/******************************************************************************* 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * camunda services GmbH - initial API and implementation 
 *
 ******************************************************************************/

package org.eclipse.bpmn2.modeler.ui.property.tabs.radio;

import static org.eclipse.bpmn2.modeler.ui.property.tabs.util.Events.RADIO_SELECTION_CHANGED;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.bpmn2.modeler.core.Activator;
import org.eclipse.bpmn2.modeler.ui.property.tabs.util.Events.RadioSelectionChanged;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;


/**
 * 
 * @author Nico Rehwaldt
 */
public class Radio {

	public static class RadioGroup<T> {

		private List<Listener> listeners = new ArrayList<Listener>();
		private Map<T, Button> memberMap = new HashMap<T, Button>();
		
		private Model<T> model;
		
		public RadioGroup() {
			this.model = new Model<T>();
		}
		
		/**
		 * Returns the radio control for the given value
		 * 
		 * @param value
		 * @return
		 */
		public Button getRadioControl(T value) {
			return memberMap.get(value);
		}
		
		/**
		 * Adds a radio button to the group
		 * @param radio
		 */
		public void add(final Button radio, final T value) {
			memberMap.put(value, radio);
		}
		
		/**
		 * Returns the selection of the radio group
		 * @return
		 */
		public T getSelection() {
			return model.getSelection();
		}
		
		protected void deselectAllBut(Button radio) {
			for (Button b: memberMap.values()) {
				if (radio == null || !b.equals(radio)) {
					b.setSelection(false);
					
					// required in order to propagate the selection change to the buttons
					b.notifyListeners(SWT.Selection, new Event());
				}
			}
		}

		protected void notifyListeners(int eventType, final RadioSelectionChanged<T> event) {
			if (eventType != RADIO_SELECTION_CHANGED) {
				return;
			}

			for (Listener l : listeners) {
				try {
					l.handleEvent(event);
				} catch (Exception e) {
					// need to do that in order to handle all listeners
					Activator.logError(e);
				}
			}
		}
	}
	
	/**
	 * Model for a radio group
	 * 
	 * @author nico.rehwaldt
	 *
	 * @param <T>
	 */
	static class Model<T> {

		protected T selection;
		
		public Model() { }
		
		public void setSelection(T selection) {
			this.selection = selection;
		}
		
		public T getSelection() {
			return selection;
		}
	}
	
	/**
	 * Radio selection event listener adapter super class
	 * 
	 * @author nico.rehwaldt
	 *
	 * @param <T>
	 */
	public static abstract class RadioSelectionAdapter<T> implements Listener {
		
		@Override
		public final void handleEvent(Event event) {
			if (event instanceof RadioSelectionChanged) {
				try {
					RadioSelectionChanged<T> evt = (RadioSelectionChanged<T>) event;
					radioSelectionChanged(evt);
				} catch (Exception e) {
					// ignore
				}
			}
		}
		
		/**
		 * The radio selection changed
		 * 
		 * @param event
		 */
		public abstract void radioSelectionChanged(RadioSelectionChanged<T> event);
	}
}
