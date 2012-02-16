/*******************************************************************************
 * Copyright (c) 2011 Red Hat, Inc.
 *  All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 *
 * @author Bob Brodt
 ******************************************************************************/

package org.eclipse.bpmn2.modeler.core.adapters;

import java.util.Hashtable;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * @author Bob Brodt
 *
 */
public class ExtendedPropertiesAdapter extends AdapterImpl {
	
	public static final String PROPERTY_DESCRIPTOR = "property.descriptor";
	protected Hashtable<
		Integer, // feature ID
		Hashtable<String,Object>> // property key and value
			featureProperties = new Hashtable<Integer, Hashtable<String,Object>>();
	protected Hashtable <
		String, // property key
		Object> // value
			objectProperties = new Hashtable <String,Object>();
	
	protected AdapterFactory adapterFactory;
	
	public ExtendedPropertiesAdapter(AdapterFactory adapterFactory, EObject object) {
		super();
		this.adapterFactory = adapterFactory;
		setTarget(object);
	}

	public void setObjectDescriptor(ObjectDescriptor pd) {
		setProperty(PROPERTY_DESCRIPTOR,pd);
	}

	public ObjectDescriptor getObjectDescriptor() {
		ObjectDescriptor pd = (ObjectDescriptor) getProperty(PROPERTY_DESCRIPTOR);
		if (pd==null) {
			pd = new ObjectDescriptor(adapterFactory, (EObject)getTarget());
			setProperty(PROPERTY_DESCRIPTOR,pd);
		}
		return pd;
	}

	public FeatureDescriptor getFeatureDescriptor(EStructuralFeature feature) {
		FeatureDescriptor pd = (FeatureDescriptor) getProperty(feature.getFeatureID(),PROPERTY_DESCRIPTOR);
		if (pd==null) {
			pd = new FeatureDescriptor(adapterFactory, (EObject)getTarget(), feature);
			setProperty(feature.getFeatureID(),PROPERTY_DESCRIPTOR,pd);
		}
		return pd;
	}

	public void setFeatureDescriptor(EStructuralFeature feature, FeatureDescriptor pd) {
		setProperty(feature.getFeatureID(),PROPERTY_DESCRIPTOR,pd);
	}

	public Object getProperty(String key) {
		return objectProperties.get(key);
	}

	public boolean getBooleanProperty(String key) {
		Object result = getProperty(key);
		if (result instanceof Boolean)
			return ((Boolean)result);
		return false;
	}

	public void setProperty(String key, Object value) {
		objectProperties.put(key, value);
	}

	public Object getProperty(EStructuralFeature feature, String key) {
		return getProperty(feature.getFeatureID(), key);
	}

	public boolean getBooleanProperty(EStructuralFeature feature, String key) {
		Object result = getProperty(feature, key);
		if (result instanceof Boolean)
			return ((Boolean)result);
		return false;
	}

	public void setProperty(EStructuralFeature feature, String key, Object value) {
		setProperty(feature.getFeatureID(), key, value);
	}

	public Object getProperty(int id, String key) {
		Integer idObject = Integer.valueOf(id);
		Hashtable<String,Object> props = featureProperties.get(idObject);
		if (props==null) {
			props = new Hashtable<String,Object>();
			featureProperties.put(idObject,props);
		}
		return props.get(key);
	}

	public void setProperty(int id, String key, Object value) {
		Integer idObject = Integer.valueOf(id);
		Hashtable<String,Object> props = featureProperties.get(idObject);
		if (props==null) {
			props = new Hashtable<String,Object>();
			featureProperties.put(idObject,props);
		}
		props.put(key, value);
	}
}