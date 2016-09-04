package it.emarolab.osr.scene.primitiveShapeData;

import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import it.emarolab.amor.owlInterface.OWLReferences;

public class Dimentional3Data {

	public static final String DEFAULT_DESCRIPTION = "";
	private float x, y, z;
	private String description;
	
	public Dimentional3Data( float x, float y, float z){
		initialize( x, y, z, DEFAULT_DESCRIPTION);
	}	
	public Dimentional3Data( float x, float y, float z, String description){
		initialize( x, y, z, description);
	}
	private void initialize( float x, float y, float z, String description){
		this.x = x;
		this.y = y;
		this.z = z;
		this.description = description;
	}
	
	public float getX(){
		return x;
	}
	
	public float getY(){
		return y;
	}
	
	public float getZ(){
		return z;
	}
	
	@Override
	public String toString(){
		if( DEFAULT_DESCRIPTION.isEmpty())
			return "{ " + x + ", " + y + ", " + z + "}";
		return description + " :{ " + x + ", " + y + ", " + z + "}";
	}
	
	public void addToOntology( OWLReferences ontoRef, OWLNamedIndividual individual, OWLDataProperty xProp, OWLDataProperty yProp, OWLDataProperty zProp) {
		ontoRef.addDataPropertyB2Individual(individual, xProp, ontoRef.getOWLLiteral( this.x));
		ontoRef.addDataPropertyB2Individual(individual, yProp, ontoRef.getOWLLiteral( this.y));
		ontoRef.addDataPropertyB2Individual(individual, zProp, ontoRef.getOWLLiteral( this.z));
	}
	
	public void replaceToOntology( OWLReferences ontoRef, OWLNamedIndividual individual, OWLDataProperty xProp, OWLDataProperty yProp, OWLDataProperty zProp) {
		Dimentional3Data oldValues = getFromIndividual( ontoRef, individual, xProp, yProp, zProp);
		if( oldValues != null){
			ontoRef.replaceDataProperty(individual, xProp, ontoRef.getOWLLiteral( oldValues.x), ontoRef.getOWLLiteral( this.x));
			ontoRef.replaceDataProperty(individual, yProp, ontoRef.getOWLLiteral( oldValues.y), ontoRef.getOWLLiteral( this.y));
			ontoRef.replaceDataProperty(individual, zProp, ontoRef.getOWLLiteral( oldValues.z), ontoRef.getOWLLiteral( this.z));
		} else addToOntology( ontoRef, individual, xProp, yProp, zProp);
	}
	
	public static Dimentional3Data getFromIndividual( OWLReferences ontoRef, OWLNamedIndividual individual, OWLDataProperty xProp, OWLDataProperty yProp, OWLDataProperty zProp){
		Float x = PrimitiveShapeData.getDataPropertyValue( ontoRef, individual, xProp);
		Float y = PrimitiveShapeData.getDataPropertyValue( ontoRef, individual, yProp);
		Float z = PrimitiveShapeData.getDataPropertyValue( ontoRef, individual, zProp);
		if( x == null || y == null || z == null)
			return null;
		return new Dimentional3Data( x, y, z);			
	}
}
