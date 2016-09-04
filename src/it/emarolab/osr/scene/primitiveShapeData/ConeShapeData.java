package it.emarolab.osr.scene.primitiveShapeData;

import it.emarolab.amor.owlInterface.OWLReferences;

import java.util.ArrayList;
import java.util.List;

import org.semanticweb.owlapi.model.OWLDataProperty; 
import org.semanticweb.owlapi.model.OWLNamedIndividual;


public class ConeShapeData extends DirectionableShapeData {

	public static final String HEIGHT_DATA_PROPERTY_NAME = "hasConeHeight";
	public static final String RADIUS_DATA_PROPERTY_NAME = "hasConeRadius";
	
	public static final String APEX_COORDINATE_X_DATA_PROPERTY_NAME = "hasConeApix_x";
	public static final String APEX_COORDINATE_Y_DATA_PROPERTY_NAME = "hasConeApix_y";
	public static final String APEX_COORDINATE_Z_DATA_PROPERTY_NAME = "hasConeApix_z";
	
	public static final String INDIVIDUAL_NAME_PREFIX = "C";
	
	private Float height, radius;
	private Dimentional3Data apex;
	
	public ConeShapeData(Dimentional3Data apex, Float height, Float radius, Dimentional3Data axisDirection, Dimentional3Data centroid, long shapeId) {
		super(axisDirection, centroid, shapeId);
		this.height = height;
		this.radius = radius;
		this.apex = apex;
	}
	
	public ConeShapeData(Dimentional3Data apex, Float height, Float radius, Dimentional3Data axisDirection, Dimentional3Data centroid, long shapeId, String individualName) {
		super(axisDirection, centroid, shapeId, individualName);
		this.height = height;
		this.radius = radius;
		this.apex = apex;
	}

	public Float getHeight(){
		return this.height;
	}
	
	public Float getRadius(){
		return this.radius;
	}
	
	public Dimentional3Data getApex(){
		return this.apex;
	}
	
	@Override
	public String toString(){
		return "CONE = " + super.toString() + "( height: " + this.height + ") ; ( radius: " + this.radius + ") ; ( apex: " + this.apex + ")";
	}

	@Override
	protected void addDataPropertyToIndividual(OWLReferences ontoRef, OWLNamedIndividual individual) {
		super.addDataPropertyToIndividual( ontoRef, individual);
		OWLDataProperty apexX_prop = ontoRef.getOWLDataProperty( APEX_COORDINATE_X_DATA_PROPERTY_NAME);
		OWLDataProperty apexY_prop = ontoRef.getOWLDataProperty( APEX_COORDINATE_Y_DATA_PROPERTY_NAME);
		OWLDataProperty apexZ_prop = ontoRef.getOWLDataProperty( APEX_COORDINATE_Z_DATA_PROPERTY_NAME);
		apex.addToOntology(ontoRef, individual, apexX_prop, apexY_prop, apexZ_prop);
		OWLDataProperty height_prop = ontoRef.getOWLDataProperty( HEIGHT_DATA_PROPERTY_NAME);
		ontoRef.addDataPropertyB2Individual(individual, height_prop, ontoRef.getOWLLiteral( this.height));
		OWLDataProperty radius_prop = ontoRef.getOWLDataProperty( RADIUS_DATA_PROPERTY_NAME);
		ontoRef.addDataPropertyB2Individual(individual, radius_prop, ontoRef.getOWLLiteral( this.radius));
	}
	
	@Override
	protected void replaceDataPropertyToIndividual( OWLReferences ontoRef, OWLNamedIndividual individual) {
		super.replaceDataPropertyToIndividual( ontoRef, individual);
		OWLDataProperty apexX_prop = ontoRef.getOWLDataProperty( APEX_COORDINATE_X_DATA_PROPERTY_NAME);
		OWLDataProperty apexY_prop = ontoRef.getOWLDataProperty( APEX_COORDINATE_Y_DATA_PROPERTY_NAME);
		OWLDataProperty apexZ_prop = ontoRef.getOWLDataProperty( APEX_COORDINATE_Z_DATA_PROPERTY_NAME);
		apex.replaceToOntology( ontoRef, individual, apexX_prop, apexY_prop, apexZ_prop);
		OWLDataProperty height_prop = ontoRef.getOWLDataProperty( HEIGHT_DATA_PROPERTY_NAME);
		Float oldHeight = PrimitiveShapeData.getDataPropertyValue(ontoRef, individual, height_prop);
		if( oldHeight != null)
			ontoRef.replaceDataProperty(individual, height_prop, ontoRef.getOWLLiteral( oldHeight), ontoRef.getOWLLiteral( this.height));
		else ontoRef.addDataPropertyB2Individual(individual, height_prop, ontoRef.getOWLLiteral( this.height));
		OWLDataProperty radiusProp = ontoRef.getOWLDataProperty( RADIUS_DATA_PROPERTY_NAME);
		Float oldRadius = PrimitiveShapeData.getDataPropertyValue(ontoRef, individual, radiusProp);
		if( oldRadius != null)
			ontoRef.replaceDataProperty(individual, radiusProp, ontoRef.getOWLLiteral(oldRadius), ontoRef.getOWLLiteral( this.radius));
		else ontoRef.addDataPropertyB2Individual(individual, radiusProp, ontoRef.getOWLLiteral( this.radius));
	}
	
	public static ConeShapeData addConeIndividual( OWLReferences ontoRef, Dimentional3Data apex, Float height, Float radius, Dimentional3Data axisDirection, Dimentional3Data centroid, long shapeId, String individualName){
		ConeShapeData c = new ConeShapeData( apex, height, radius, axisDirection, centroid, shapeId, individualName); 
		c.addShapeIndividualToOntology(ontoRef);
		return c;
	}
	public static ConeShapeData addConeIndividual( OWLReferences ontoRef, Dimentional3Data apex, Float height, Float radius, Dimentional3Data axisDirection, Dimentional3Data centroid, long shapeId){
		ConeShapeData c = new ConeShapeData( apex, height, radius, axisDirection, centroid, shapeId); 
		c.addShapeIndividualToOntology(ontoRef);
		return c;
	}
	
	public static ConeShapeData replaceConeIndividual( OWLReferences ontoRef, Dimentional3Data apex, Float height, Float radius, Dimentional3Data axisDirection, Dimentional3Data centroid, long shapeId, String individualName){
		ConeShapeData c = new ConeShapeData( apex, height, radius, axisDirection, centroid, shapeId, individualName); 
		c.updateShapeIndividualToOntology( ontoRef);
		return c;
	}
	public static ConeShapeData replaceConeIndividual( OWLReferences ontoRef, Dimentional3Data apex, Float height, Float radius, Dimentional3Data axisDirection, Dimentional3Data centroid, long shapeId){
		ConeShapeData c = new ConeShapeData( apex, height, radius, axisDirection, centroid, shapeId); 
		c.updateShapeIndividualToOntology( ontoRef);
		return c;
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
		out.add( ontology.getOWLDataProperty( APEX_COORDINATE_X_DATA_PROPERTY_NAME));
		out.add( ontology.getOWLDataProperty( APEX_COORDINATE_Y_DATA_PROPERTY_NAME));
		out.add( ontology.getOWLDataProperty( APEX_COORDINATE_Z_DATA_PROPERTY_NAME));
		return out;
	}
}
