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
 * @author Ivar Meikas
 ******************************************************************************/
package org.camunda.bpm.modeler.core.features.participant;

import java.util.List;

import org.camunda.bpm.modeler.core.di.DIUtils;
import org.camunda.bpm.modeler.core.features.DefaultResizeBPMNShapeFeature;
import org.camunda.bpm.modeler.core.layout.util.Layouter;
import org.camunda.bpm.modeler.core.utils.BusinessObjectUtil;
import org.camunda.bpm.modeler.core.utils.FeatureSupport;
import org.eclipse.bpmn2.ChoreographyActivity;
import org.eclipse.bpmn2.Lane;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReason;
import org.eclipse.graphiti.features.IResizeShapeFeature;
import org.eclipse.graphiti.features.context.IResizeShapeContext;
import org.eclipse.graphiti.features.context.impl.ResizeShapeContext;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;

public class ResizeParticipantFeature extends DefaultResizeBPMNShapeFeature {

	public static final String POOL_RESIZE_PROPERTY = "pool.resize";
	public static final String RESIZE_FIRST_LANE = "resize.first.lane";
	
	public ResizeParticipantFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canResizeShape(IResizeShapeContext context) {
		EObject container = context.getShape().eContainer();
		if (container instanceof PictogramElement) {
			PictogramElement containerElem = (PictogramElement) container;
			if (BusinessObjectUtil.containsElementOfType(containerElem, ChoreographyActivity.class)) {
				return false;
			}
		}
		return super.canResizeShape(context);
	}
	
	private void resizeLaneHeight(IResizeShapeContext context) {
		ContainerShape participantShape = (ContainerShape) context.getShape();
		GraphicsAlgorithm ga = participantShape.getGraphicsAlgorithm();
		
		ContainerShape laneToResize = null;
		GraphicsAlgorithm laneToResizeGA  = null;
		int width = 0;
		int height = 0;
		int x = 0;
		int y = 0;
		boolean resizeFirstLane = false;
		boolean resize = false;
		if (FeatureSupport.isHorizontal(participantShape)) {
			int dHeight = context.getHeight() - ga.getHeight();
			if (dHeight != 0) {
				resize = true;
				if (context.getY() != ga.getY()) {
					laneToResize = (ContainerShape) FeatureSupport.getFirstLaneInContainer(participantShape);
					resizeFirstLane = true;
				} else {
					laneToResize = (ContainerShape) FeatureSupport.getLastLaneInContainer(participantShape);
				}
				laneToResizeGA = laneToResize.getGraphicsAlgorithm();
				width = laneToResizeGA.getWidth();
				height = laneToResizeGA.getHeight() + dHeight;
				x = laneToResizeGA.getX();
				y = laneToResizeGA.getY();
			}
		} else {
			int dWidth = context.getWidth() - ga.getWidth();
			if (dWidth != 0) {
				resize = true;
				if (context.getX() != ga.getX()) {
					laneToResize = (ContainerShape) FeatureSupport.getFirstLaneInContainer(participantShape);
					resizeFirstLane = true;
				} else {
					laneToResize = (ContainerShape) FeatureSupport.getLastLaneInContainer(participantShape);
				}
				laneToResizeGA = laneToResize.getGraphicsAlgorithm();
				width = laneToResizeGA.getWidth() + dWidth;
				height = laneToResizeGA.getHeight();
				x = laneToResizeGA.getX();
				y = laneToResizeGA.getY();
			}
		}
		if (resize) {
			ResizeShapeContext newContext = new ResizeShapeContext(laneToResize);
			
			newContext.setLocation(x, y);
			newContext.setHeight(height);
			newContext.setWidth(width);
			
			newContext.putProperty(POOL_RESIZE_PROPERTY, true);
			newContext.putProperty(RESIZE_FIRST_LANE, resizeFirstLane);
			
			IResizeShapeFeature resizeFeature = getFeatureProvider().getResizeShapeFeature(newContext);
			if (resizeFeature.canResizeShape(newContext)) {
				resizeFeature.resizeShape(newContext);
			}
			if (FeatureSupport.isHorizontal(participantShape)) {
				((ResizeShapeContext) context).setHeight(ga.getHeight());
			} else {
				((ResizeShapeContext) context).setWidth(ga.getWidth());
			}
		}
	}
	
	private void resizeLaneWidth(IResizeShapeContext context) {
		ContainerShape participantShape = (ContainerShape) context.getShape();
		GraphicsAlgorithm ga = participantShape.getGraphicsAlgorithm();
		
		int dHeight = context.getHeight() - ga.getHeight();
		int dWidth = context.getWidth() - ga.getWidth();
		
		if ((dWidth != 0 && FeatureSupport.isHorizontal(participantShape)) ||
				(dHeight != 0 && !FeatureSupport.isHorizontal(participantShape))) {
			List<PictogramElement> childrenShapes = FeatureSupport.getChildsOfBusinessObjectType(participantShape, Lane.class);
			for (PictogramElement currentPicElem : childrenShapes) {
				if (currentPicElem instanceof ContainerShape) {
					ContainerShape currentContainerShape = (ContainerShape) currentPicElem; 
					GraphicsAlgorithm laneGA = currentContainerShape.getGraphicsAlgorithm();
					
					ResizeShapeContext newContext = new ResizeShapeContext(currentContainerShape);
					
					newContext.setLocation(laneGA.getX(), laneGA.getY());
					if (FeatureSupport.isHorizontal(participantShape)) {
						newContext.setWidth(laneGA.getWidth() + dWidth);
						newContext.setHeight(laneGA.getHeight());
					} else {
						newContext.setHeight(laneGA.getHeight() + dHeight);
						newContext.setWidth(laneGA.getWidth());
					}
					
					newContext.putProperty(POOL_RESIZE_PROPERTY, true);
					
					IResizeShapeFeature resizeFeature = getFeatureProvider().getResizeShapeFeature(newContext);
					if (resizeFeature.canResizeShape(newContext)) {
						resizeFeature.resizeShape(newContext);
					}
				}
			}
		}
	}
	
	@Override
	protected void preResize(IResizeShapeContext context) {
		super.preResize(context);
		
		Shape shape = context.getShape();
		
		if (BusinessObjectUtil.containsChildElementOfType(shape, Lane.class)) {
			resizeLaneHeight(context);
			resizeLaneWidth(context);
		}
	}
}
