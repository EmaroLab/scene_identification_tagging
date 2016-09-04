package it.emarolab.osr.scene.primitiveShapeData;

import java.util.ArrayList;
import java.util.List;

import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import it.emarolab.amor.owlInterface.OWLReferences;

public class SphereShapeData extends PrimitiveShapeData {

	public static final String RADIUS_DATA_PROPERTY_NAME = "hasSphereRadius";
	
	public static final String INDIVIDUAL_NAME_PREFIX = "S";
	
	private Float radius;
	
	public SphereShapeData(Float radius, Dimentional3Data centroid, long shapeId) {
		super(centroid, shapeId);
		this.radius = radius; 
	}
	
	public SphereShapeData(Float radius, Dimentional3Data centroid, long shapeId, String individualName) {
		super(centroid, shapeId, individualName);
		this.radius = radius; 
	}
	
	public Float getRadius(){
		return this.radius;
	}

	@Override
	public String toString(){
		return "SPHERE= " + super.toString() + "( radius: " + this.getRadius() + ")"; 
	}
	
	@Override
	protected void addDataPropertyToIndividual(OWLReferences ontoRef, OWLNamedIndividual individual) {
		super.addDataPropertyToIndividual( ontoRef, individual);
		OWLDataProperty radiusProp = ontoRef.getOWLDataProperty(RADIUS_DATA_PROPERTY_NAME);
		ontoRef.addDataPropertyB2Individual(individual, radiusProp, ontoRef.getOWLLiteral( this.radius));
	}
	
	@Override
	protected void replaceDataPropertyToIndividual( OWLReferences ontoRef, OWLNamedIndividual individual) {
		super.replaceDataPropertyToIndividual( ontoRef, individual);
		OWLDataProperty radiusProp = ontoRef.getOWLDataProperty(RADIUS_DATA_PROPERTY_NAME);
		Float oldRadius = PrimitiveShapeData.getDataPropertyValue( ontoRef, individual, radiusProp);
		if( oldRadius != null)
			ontoRef.replaceDataProperty(individual, radiusProp, ontoRef.getOWLLiteral( oldRadius), ontoRef.getOWLLiteral( this.radius));
		else ontoRef.addDataPropertyB2Individual(individual, radiusProp, ontoRef.getOWLLiteral( this.radius));
	}
		
	public static SphereShapeData addSphereIndividual( OWLReferences ontoRef, Float radius, Dimentional3Data centroid,  long shapeId, String individualName){
		SphereShapeData s = new SphereShapeData( radius, centroid, shapeId, individualName); 
		s.addShapeIndividualToOntology(ontoRef);
		return s;
	}
	public static SphereShapeData addSphereIndividual( OWLReferences ontoRef, Float radius, Dimentional3Data centroid, long shapeId){
		SphereShapeData s = new SphereShapeData( radius, centroid, shapeId); 
		s.addShapeIndividualToOntology(ontoRef);
		return s;
	}
	
	public static SphereShapeData replaceSphereIndividual( OWLReferences ontoRef, Float radius, Dimentional3Data centroid,  long shapeId, String individualName){
		SphereShapeData s = new SphereShapeData( radius, centroid, shapeId, individualName); 
		s.updateShapeIndividualToOntology(ontoRef);
		return s;
	}
	public static SphereShapeData replaceSphereIndividual( OWLReferences ontoRef, Float radius, Dimentional3Data centroid, long shapeId){
		SphereShapeData s = new SphereShapeData( radius, centroid, shapeId); 
		s.updateShapeIndividualToOntology(ontoRef);
		return s;
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
		out.add( ontology.getOWLDataProperty( RADIUS_DATA_PROPERTY_NAME));
		return out;
	}
}
