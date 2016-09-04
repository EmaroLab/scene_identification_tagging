package it.emarolab.osr.scene.primitiveShapeData;

import java.util.ArrayList;
import java.util.List;

import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import it.emarolab.amor.owlInterface.OWLReferences;

public class CylinderShapeData extends DirectionableShapeData {

	public static final String HEIGHT_DATA_PROPERTY_NAME = "hasCylinderHeight";
	public static final String RADIUS_DATA_PROPERTY_NAME = "hasCylinderRadius";
	
	public static final String POINT_AXIS_COORDINATE_X_DATA_PROPERTY_NAME = "hasCylinderPointOnAxis_x";
	public static final String POINT_AXIS_COORDINATE_Y_DATA_PROPERTY_NAME = "hasCylinderPointOnAxis_y";
	public static final String POINT_AXIS_COORDINATE_Z_DATA_PROPERTY_NAME = "hasCylinderPointOnAxis_z";
	
	public static final String INDIVIDUAL_NAME_PREFIX = "R";
	
	private Float height, radius;
	private Dimentional3Data pointOnAxis;
	
	public CylinderShapeData(Dimentional3Data pointOnAxis, Float height, Float radius, Dimentional3Data axisDirection, Dimentional3Data centroid,  long shapeId) {
		super(axisDirection, centroid, shapeId);
		this.height = height;
		this.radius = radius;
		this.pointOnAxis = pointOnAxis;
	}
	
	public CylinderShapeData(Dimentional3Data pointOnAxis, Float height, Float radius, Dimentional3Data axisDirection, Dimentional3Data centroid,  long shapeId, String individualName) {
		super(axisDirection, centroid, shapeId, individualName);
		this.height = height;
		this.radius = radius;
		this.pointOnAxis = pointOnAxis;
	}

	public Float getHeight(){
		return this.height;
	}
	
	public Float getRadius(){
		return this.radius;
	}
	
	public Dimentional3Data getPointOnAxis(){
		return this.pointOnAxis;
	}
	
	@Override
	public String toString(){
		return "CYLINDER = " + super.toString() + "( height: " + this.height + ") ; ( radius: " + this.radius + ") ; ( pointOnAxis: " + this.pointOnAxis + ")";
	}
	
	@Override
	protected void addDataPropertyToIndividual(OWLReferences ontoRef, OWLNamedIndividual individual) {
		super.addDataPropertyToIndividual( ontoRef, individual);
		OWLDataProperty pointX_prop = ontoRef.getOWLDataProperty( POINT_AXIS_COORDINATE_X_DATA_PROPERTY_NAME);
		OWLDataProperty pointY_prop = ontoRef.getOWLDataProperty( POINT_AXIS_COORDINATE_Y_DATA_PROPERTY_NAME);
		OWLDataProperty pointZ_prop = ontoRef.getOWLDataProperty( POINT_AXIS_COORDINATE_Z_DATA_PROPERTY_NAME);
		pointOnAxis.addToOntology(ontoRef, individual, pointX_prop, pointY_prop, pointZ_prop);
		OWLDataProperty height_prop = ontoRef.getOWLDataProperty( HEIGHT_DATA_PROPERTY_NAME);
		ontoRef.addDataPropertyB2Individual(individual, height_prop, ontoRef.getOWLLiteral( this.height));
		OWLDataProperty radius_prop = ontoRef.getOWLDataProperty(RADIUS_DATA_PROPERTY_NAME);
		ontoRef.addDataPropertyB2Individual(individual, radius_prop, ontoRef.getOWLLiteral( this.radius));
	}
	
	@Override
	protected void replaceDataPropertyToIndividual( OWLReferences ontoRef, OWLNamedIndividual individual) {
		super.replaceDataPropertyToIndividual( ontoRef, individual);
		OWLDataProperty pointX_prop = ontoRef.getOWLDataProperty( POINT_AXIS_COORDINATE_X_DATA_PROPERTY_NAME);
		OWLDataProperty pointY_prop = ontoRef.getOWLDataProperty( POINT_AXIS_COORDINATE_Y_DATA_PROPERTY_NAME);
		OWLDataProperty pointZ_prop = ontoRef.getOWLDataProperty( POINT_AXIS_COORDINATE_Z_DATA_PROPERTY_NAME);
		pointOnAxis.replaceToOntology(ontoRef, individual, pointX_prop, pointY_prop, pointZ_prop);
		OWLDataProperty height_prop = ontoRef.getOWLDataProperty( HEIGHT_DATA_PROPERTY_NAME);
		Float oldHeight = PrimitiveShapeData.getDataPropertyValue(ontoRef, individual, height_prop);
		if( oldHeight != null)
			ontoRef.replaceDataProperty( individual, height_prop, ontoRef.getOWLLiteral( oldHeight), ontoRef.getOWLLiteral( this.height));
		//else ontoRef.addDataPropertyB2Individual(individual, height_prop, ontoRef.getOWLLiteral( this.height), BUFFERIZE_ONTOLOGY_CHANGES);
		OWLDataProperty radius_prop = ontoRef.getOWLDataProperty( RADIUS_DATA_PROPERTY_NAME);
		Float oldRadius = PrimitiveShapeData.getDataPropertyValue(ontoRef, individual, radius_prop);
		if( oldRadius != null)
			ontoRef.replaceDataProperty(individual, radius_prop, ontoRef.getOWLLiteral( oldRadius), ontoRef.getOWLLiteral( this.radius));
		else ontoRef.addDataPropertyB2Individual(individual, radius_prop, ontoRef.getOWLLiteral( this.radius));
	}
	
	public static CylinderShapeData addCylinderIndividual( OWLReferences ontoRef, Dimentional3Data pointOnAxis, Float height, Float radius, Dimentional3Data axisDirection, Dimentional3Data centroid, long shapeId){
		CylinderShapeData r = new CylinderShapeData( pointOnAxis, height, radius, axisDirection, centroid, shapeId); 
		r.addShapeIndividualToOntology(ontoRef);
		return r;
	}
	public static CylinderShapeData addCylinderIndividual( OWLReferences ontoRef, Dimentional3Data pointOnAxis, Float height, Float radius, Dimentional3Data axisDirection, Dimentional3Data centroid, long shapeId, String individualName){
		CylinderShapeData r = new CylinderShapeData( pointOnAxis, height, radius, axisDirection, centroid, shapeId, individualName); 
		r.addShapeIndividualToOntology(ontoRef);
		return r;
	}
	
	public static CylinderShapeData replaceCylinderIndividual( OWLReferences ontoRef, Dimentional3Data pointOnAxis, Float height, Float radius, Dimentional3Data axisDirection, Dimentional3Data centroid, long shapeId){
		CylinderShapeData r = new CylinderShapeData( pointOnAxis, height, radius, axisDirection, centroid, shapeId); 
		r.updateShapeIndividualToOntology(ontoRef);
		return r;
	}
	public static CylinderShapeData replaceCylinderIndividual( OWLReferences ontoRef, Dimentional3Data pointOnAxis, Float height, Float radius, Dimentional3Data axisDirection, Dimentional3Data centroid, long shapeId, String individualName){
		CylinderShapeData r = new CylinderShapeData( pointOnAxis, height, radius, axisDirection, centroid, shapeId, individualName); 
		r.updateShapeIndividualToOntology(ontoRef);
		return r;
	}
	
	@Override
	String getIndividualNamePrefix() {
		return INDIVIDUAL_NAME_PREFIX;
	}

	public static String computeDefaultIndividualName( long shapeId){
		return INDIVIDUAL_NAME_PREFIX + INDIVIDUAL_NAME_MIDDLE_STR + shapeId;
	}
	
	public static List<OWLDataProperty> getDataPropertyList(OWLReferences ontology) {
		List<OWLDataProperty> out = new ArrayList<OWLDataProperty>();
		out.addAll( PrimitiveShapeData.getDataPropertyList( ontology));
		out.addAll( DirectionableShapeData.getDataPropertyList( ontology));
		out.add( ontology.getOWLDataProperty( HEIGHT_DATA_PROPERTY_NAME));
		out.add( ontology.getOWLDataProperty( RADIUS_DATA_PROPERTY_NAME));
		out.add( ontology.getOWLDataProperty( POINT_AXIS_COORDINATE_X_DATA_PROPERTY_NAME));
		out.add( ontology.getOWLDataProperty( POINT_AXIS_COORDINATE_Y_DATA_PROPERTY_NAME));
		out.add( ontology.getOWLDataProperty( POINT_AXIS_COORDINATE_Z_DATA_PROPERTY_NAME));
		return out;
	}
}
