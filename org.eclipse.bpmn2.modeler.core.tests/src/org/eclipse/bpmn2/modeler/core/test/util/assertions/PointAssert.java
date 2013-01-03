/******************************************************************************* 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * camunda services GmbH - initial API and implementation 
 *
 ******************************************************************************/

package org.eclipse.bpmn2.modeler.core.test.util.assertions;

import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.fest.assertions.api.AbstractAssert;
import org.fest.assertions.api.Assertions;

public class PointAssert extends AbstractAssert<PointAssert, Point> {

	protected PointAssert(Point actual) {
		super(actual, PointAssert.class);
	}
	
	public PointAssert isEqualTo(Point expected) {
		
		if (actual == null) {
			Assertions.fail(String.format("Expected actual to equal <%s> but was <null>", expected));
		}
		
		if (actual.getX() != expected.getX() || actual.getY() != expected.getY()) {
			Assertions.fail(String.format("Expected actual to equal <Point(%s, %s)> but was <Point(%s, %s)>", expected.getX(), expected.getY(), actual.getX(), actual.getY()));
		}
		
		return myself;
	}
}
