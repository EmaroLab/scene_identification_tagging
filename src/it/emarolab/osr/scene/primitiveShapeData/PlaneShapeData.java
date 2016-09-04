package it.emarolab.osr.scene.primitiveShapeData;

import java.util.ArrayList;
import java.util.List;

import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import it.emarolab.amor.owlInterface.OWLReferences;

public class PlaneShapeData extends DirectionableShapeData {

	public static final String D_COEFFICIENT_PROPERTY_NAME = "hasPlaneCoefficent_d";
	
	public static final String INDIVIDUAL_NAME_PREFIX = "P";
	
	private float dCoefficient;
	
	public PlaneShapeData(Dimentional3Data axisDirection, Dimentional3Data centroid, long shapeId, float dCoefficient) {
		super(axisDirection, centroid, shapeId);
		this.dCoefficient = dCoefficient;
	}
	
	public PlaneShapeData(Dimentional3Data axisDirection, Dimentional3Data centroid,  long shapeId, String individualName, float dCoefficient) {
		super(axisDirection, centroid, shapeId, individualName);
		this.dCoefficient = dCoefficient;
	}
	
	public float getDCoefficient(){
		return dCoefficient;
	}
	
	@Override
	public String toString(){
		return "PLANE = " + super.toString() + "( D_coeff: " + this.dCoefficient + ")"; 
	}
	
	@Override
	protected void addDataPropertyToIndividual(OWLReferences ontoRef, OWLNamedIndividual individual) {
		super.addDataPropertyToIndividual( ontoRef, individual);
		OWLDataProperty d_prop = ontoRef.getOWLDataProperty(D_COEFFICIENT_PROPERTY_NAME);
		ontoRef.addDataPropertyB2Individual(individual, d_prop, ontoRef.getOWLLiteral( this.dCoefficient));
	}
	
	@Override
	protected void replaceDataPropertyToIndividual( OWLReferences ontoRef, OWLNamedIndividual individual) {
		super.replaceDataPropertyToIndividual( ontoRef, individual);
		OWLDataProperty dProp = ontoRef.getOWLDataProperty(D_COEFFICIENT_PROPERTY_NAME);
		Float oldD = PrimitiveShapeData.getDataPropertyValue( ontoRef, individual, dProp);
		if( oldD != null)
			ontoRef.replaceDataProperty(individual, dProp, ontoRef.getOWLLiteral( oldD), ontoRef.getOWLLiteral( this.dCoefficient));
		else ontoRef.addDataPropertyB2Individual(individual, dProp, ontoRef.getOWLLiteral( this.dCoefficient));
	}
	
	public static PlaneShapeData addPlaneIndividual( OWLReferences ontoRef, Float d, Dimentional3Data axis, Dimentional3Data centroid, long shapeId, String name){
		PlaneShapeData p = new PlaneShapeData( axis, centroid, shapeId, name, d);
		p.addShapeIndividualToOntology(ontoRef);
		return p;
	}
	public static PlaneShapeData addPlaneIndividual( OWLReferences ontoRef, Float d, Dimentional3Data axis, Dimentional3Data centroid, long shapeId){
		PlaneShapeData p = new PlaneShapeData( axis, centroid, shapeId, d);
		p.addShapeIndividualToOntology(ontoRef);
		return p;
	}
	
	public static PlaneShapeData replacePlaneIndividual( OWLReferences ontoRef, Float d, Dimentional3Data axis, Dimentional3Data centroid, long shapeId, String name){
		PlaneShapeData p = new PlaneShapeData( axis, centroid, shapeId, name, d);
		p.updateShapeIndividualToOntology(ontoRef);
		return p;
	}
	public static PlaneShapeData replacePlaneIndividual( OWLReferences ontoRef, Float d, Dimentional3Data axis, Dimentional3Data centroid, long shapeId){
		PlaneShapeData p = new PlaneShapeData( axis, centroid, shapeId, d);
		p.updateShapeIndividualToOntology(ontoRef);
		return p;
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
		out.add( ontology.getOWLDataProperty( D_COEFFICIENT_PROPERTY_NAME));
		return out;
	}
}
