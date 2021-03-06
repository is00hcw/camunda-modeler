/******************************************************************************* 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * camunda services GmbH - initial API and implementation 
 *
 ******************************************************************************/
package org.camunda.bpm.modeler.test.importer;

import org.camunda.bpm.modeler.core.importer.ModelImport;
import org.camunda.bpm.modeler.test.AbstractEditorTest;

/**
 * 
 * @author Nico Rehwaldt
 * @author Daniel Meyer
 *
 */
public abstract class AbstractImportBpmnModelTest extends AbstractEditorTest {

	/**
	 * Creates the model import usable by a test case
	 * @return
	 */
	protected ModelImport createModelImport() {
		return new ModelImport(diagramTypeProvider, bpmnResource);
	}
}
