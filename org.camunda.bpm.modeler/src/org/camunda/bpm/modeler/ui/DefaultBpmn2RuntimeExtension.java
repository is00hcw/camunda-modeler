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
package org.camunda.bpm.modeler.ui;
import org.camunda.bpm.modeler.core.IBpmn2RuntimeExtension;
import org.camunda.bpm.modeler.core.preferences.Bpmn2Preferences;
import org.camunda.bpm.modeler.core.utils.ModelUtil.Bpmn2DiagramType;
import org.eclipse.graphiti.ui.editor.DiagramEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;


public class DefaultBpmn2RuntimeExtension implements IBpmn2RuntimeExtension {

	public DefaultBpmn2RuntimeExtension() {
		
	}

	@Override
	public boolean isContentForRuntime(IEditorInput input) {
		return false;
	}

	@Override
	public String getTargetNamespace(Bpmn2DiagramType diagramType){
		String type = "";
		switch (diagramType) {
		case PROCESS:
			type = "/process";
			break;
		case COLLABORATION:
			type = "/collaboration";
			break;
		case CHOREOGRAPHY:
			type = "/choreography";
			break;
		}
		return "http://sample.bpmn2.org/bpmn2/sample" + type;
	}

	@Override
	public void initialize(DiagramEditor editor) {
		
	}

	@Override
	public Composite getPreferencesComposite(Composite parent, Bpmn2Preferences preferences) {
		return null;
	}
}
