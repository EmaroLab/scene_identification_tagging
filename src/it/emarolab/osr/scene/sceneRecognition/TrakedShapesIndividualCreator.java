package it.emarolab.osr.scene.sceneRecognition;

import java.util.ArrayList;
import java.util.List;

import org.semanticweb.owlapi.model.OWLNamedIndividual;

import it.emarolab.amor.owlDebugger.Logger;
import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.osr.RecognitionManualSupervising;
import it.emarolab.osr.scene.primitiveShapeData.ConeShapeData;
import it.emarolab.osr.scene.primitiveShapeData.CylinderShapeData;
import it.emarolab.osr.scene.primitiveShapeData.Dimentional3Data;
import it.emarolab.osr.scene.primitiveShapeData.PlaneShapeData;
import it.emarolab.osr.scene.primitiveShapeData.SphereShapeData;
import it.emarolab.osr.shapeTracking.RansacTrackedShape;
import it.emarolab.osr.shapeTracking.ShapeTracker;

public class TrakedShapesIndividualCreator {

	private Logger log;
	public static Boolean DEBUG = RecognitionManualSupervising.DEBUG;
	
	// coming from the cpp part of the system
	public static final String SHAPE_TAG_UNKNOWN = "unknown";
	public static final String SHAPE_TAG_PLANE = "plane";
	public static final String SHAPE_TAG_SPHERE = "sphere";
	public static final String SHAPE_TAG_CONE = "cone";
	public static final String SHAPE_TAG_CYLINDER = "cylinder";
	
	private List< PlaneShapeData> allPlaneIndividual;
	private List< SphereShapeData> allSphereIndividual;
	private List< ConeShapeData> allConeIndividual;
	private List< CylinderShapeData> allCylinderIndividual;
	
	public TrakedShapesIndividualCreator( ShapeTracker shapesTracker, OWLReferences ontology){
		// initialize
		log = new Logger( this, DEBUG);
		allPlaneIndividual = new ArrayList< PlaneShapeData>();
		allSphereIndividual = new ArrayList< SphereShapeData>();
		allConeIndividual = new ArrayList< ConeShapeData>();
		allCylinderIndividual = new ArrayList< CylinderShapeData>();
		
		// remove old individuals
		for( RansacTrackedShape s : shapesTracker.getShapeRemoved()){
			String individualName = RansacTrackedShape.getDefaultIndividualName( s);
			if( individualName != null){
				OWLNamedIndividual ind = ontology.getOWLIndividual( individualName);
				if( ind != null){
					ontology.removeIndividual( ind);
					//removePropertyB2Individual( ind, PrimitiveShapeData.getObjectPropertyList(ontology), RansacTrackedShape.getShapeDataProperty( s, ontology), ontology);
				}
			}
		}
		
		// create individual from tracked shape service
		for( RansacTrackedShape s : ShapeTracker.getShapeTracker()){
			if( s.isUpdated()){
				if( s.isUnlnown())
					log.addDebugString( "Cannot create an individual of primitve type = " + SHAPE_TAG_UNKNOWN);
				else{
					ontology.removeIndividual( RansacTrackedShape.getDefaultIndividual( s, ontology));
					ontology.applyOWLManipulatorChanges();
					ontology.synchroniseReasoner();
					if( s.isPlane())
						addPlaneIndividual( s, ontology);
					else if( s.isSphere())
						addSphereIndividual( s, ontology);
					else if( s.isCone())
						addConeIndividual( s, ontology);
					else if( s.isCylinder())
						addCylinderIndividual( s, ontology);
				}
			}
		}
	}


	private void addCylinderIndividual( RansacTrackedShape s, OWLReferences ontology) {
		Dimentional3Data pointOnAxis = new Dimentional3Data( s.getCoefficient()[ 0], s.getCoefficient()[ 1], s.getCoefficient()[ 2]);
		Dimentional3Data axisDirection = new Dimentional3Data( s.getCoefficient()[ 3], s.getCoefficient()[ 4], s.getCoefficient()[ 5]);
		float radius = s.getCoefficient()[ 6];
		float height = s.getCoefficient()[ 7];
		Dimentional3Data centroid = s.getCentroid();
		Long shapeId = Long.valueOf( s.getObjectId());
		CylinderShapeData r;
		//if( s.isChecked())
		//	r = CylinderShapeData.replaceCylinderIndividual( ontology, pointOnAxis, height, radius, axisDirection, centroid, shapeId);
		//else r = CylinderShapeData.addCylinderIndividual( ontology, pointOnAxis, height, radius, axisDirection, centroid, shapeId);
		
		// replace does not work.... so remove the individual frist and add a new one !!!!!
		//removePropertyB2Individual( RansacTrackedShape.getDefaultIndividualName( s), PrimitiveShapeData.getObjectPropertyList(ontology), CylinderShapeData.getDataPropertyList( ontology) , ontology);
		r = CylinderShapeData.addCylinderIndividual( ontology, pointOnAxis, height, radius, axisDirection, centroid, shapeId);
		
		allCylinderIndividual.add( r);
	}


	private void addConeIndividual( RansacTrackedShape s, OWLReferences ontology) {
		Dimentional3Data apex = new Dimentional3Data( s.getCoefficient()[ 0], s.getCoefficient()[ 1], s.getCoefficient()[ 2]);
		Dimentional3Data axisDirection = new Dimentional3Data( s.getCoefficient()[ 3], s.getCoefficient()[ 4], s.getCoefficient()[ 5]);
		float radius = s.getCoefficient()[ 6];
		float height = s.getCoefficient()[ 7];
		Dimentional3Data centroid = s.getCentroid();
		Long shapeId = Long.valueOf( s.getObjectId());
		ConeShapeData c;
		//if( s.isChecked())
		//	c =  ConeShapeData.replaceConeIndividual( ontology, apex, height, radius, axisDirection, centroid, shapeId);
		//else c = ConeShapeData.addConeIndividual( ontology, apex, height, radius, axisDirection, centroid, shapeId);
		
		// replace does not work.... so remove the individual frist and add a new one !!!!!
		//removePropertyB2Individual( RansacTrackedShape.getDefaultIndividualName( s), PrimitiveShapeData.getObjectPropertyList(ontology), ConeShapeData.getDataPropertyList( ontology) , ontology);
		c = ConeShapeData.addConeIndividual( ontology, apex, height, radius, axisDirection, centroid, shapeId);
		
		allConeIndividual.add( c);
	}


	private void addSphereIndividual(RansacTrackedShape s, OWLReferences ontology) {
		Dimentional3Data centroid = s.getCentroid();
		float radius = s.getCoefficient()[ 3];
		Long shapeId = Long.valueOf( s.getObjectId());
		SphereShapeData sp;
		//if( s.isChecked())
		//	sp =  SphereShapeData.replaceSphereIndividual( ontology, radius, centroid, shapeId);
		//else sp = SphereShapeData.addSphereIndividual( ontology, radius, centroid, shapeId);
		
		// replace does not work.... so remove the individual frist and add a new one !!!!!
		//removePropertyB2Individual( RansacTrackedShape.getDefaultIndividualName( s), PrimitiveShapeData.getObjectPropertyList(ontology), SphereShapeData.getDataPropertyList( ontology) , ontology);
		sp = SphereShapeData.addSphereIndividual( ontology, radius, centroid, shapeId);
		
		allSphereIndividual.add( sp);
	}


	private void addPlaneIndividual( RansacTrackedShape s, OWLReferences ontology) {
		Dimentional3Data axis = new Dimentional3Data( s.getCoefficient()[ 0] / getPlaneDelta( s),
				s.getCoefficient()[ 1] / getPlaneDelta( s), s.getCoefficient()[ 2] / getPlaneDelta( s)); // ??????????
		float d = s.getCoefficient()[ 3];
		Dimentional3Data centroid = s.getCentroid();
		Long shapeId = Long.valueOf( s.getObjectId());
		PlaneShapeData p;
		//if( s.isChecked())
			p =  PlaneShapeData.replacePlaneIndividual( ontology, d, axis, centroid, shapeId);
		//else p = PlaneShapeData.addPlaneIndividual( ontology, d, axis, centroid, shapeId);
		
		// replace does not work.... so remove the individual frist and add a new one !!!!!
		//removePropertyB2Individual( RansacTrackedShape.getDefaultIndividualName( s), PrimitiveShapeData.getObjectPropertyList(ontology), PlaneShapeData.getDataPropertyList( ontology) , ontology);
		p = PlaneShapeData.addPlaneIndividual( ontology, d, axis, centroid, shapeId);
		
		allPlaneIndividual.add( p);
	}
	
	
	/*private void removePropertyB2Individual( String indName, List< OWLObjectProperty> objProps, List< OWLDataProperty> dataProps, OWLReferences onto){
		OWLNamedIndividual ind = onto.getOWLIndividual( indName);
		removePropertyB2Individual(  ind, objProps, dataProps, onto);
	}
	private void removePropertyB2Individual( OWLNamedIndividual ind, List< OWLObjectProperty> objProps, List< OWLDataProperty> dataProps, OWLReferences onto){
		if( dataProps != null)
			for( OWLDataProperty prop : dataProps){
				Set< OWLLiteral> values = onto.getDataPropertyB2Individual( ind, prop);
				if( values != null)
					for( OWLLiteral lit : values)
						if( lit != null)
							onto.removeDataPropertyB2Individual( ind, prop, lit, PrimitiveShapeData.BUFFERIZE_ONTOLOGY_CHANGES);
			}
		if( objProps != null)
			for( OWLObjectProperty prop : objProps){
				Set< OWLNamedIndividual> values = onto.getObjectPropertyB2Individual( ind, prop);
				if( values != null)
					for( OWLNamedIndividual i : values)
						if( i != null)
							onto.removeObjectPropertyB2Individual( ind, prop, i, PrimitiveShapeData.BUFFERIZE_ONTOLOGY_CHANGES);
			}
	}*/

	private float getPlaneDelta( RansacTrackedShape s){
		float q1 = s.getCoefficient()[ 0] * s.getCoefficient()[ 0];
		float q2 = s.getCoefficient()[ 1] * s.getCoefficient()[ 1];
		float q3 = s.getCoefficient()[ 3] * s.getCoefficient()[ 3];
		return (float) Math.sqrt( q1 + q2 + q3);
	}

	public List< PlaneShapeData> getAllPlaneIndividual() {
		return allPlaneIndividual;
	}


	public List< SphereShapeData> getAllSphereIndividual() {
		return allSphereIndividual;
	}


	public List< ConeShapeData> getAllConeIndividual() {
		return allConeIndividual;
	}


	public List< CylinderShapeData> getAllCylinderIndividual() {
		return allCylinderIndividual;
	}
	
	
}
