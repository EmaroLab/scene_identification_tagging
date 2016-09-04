package it.emarolab.osr.scene.primitiveShapeData;

import java.util.ArrayList;
import java.util.List;

import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import it.emarolab.amor.owlInterface.OWLReferences;

public abstract class DirectionableShapeData extends PrimitiveShapeData{

	private static final String AXIS_DIRECTION_X_DATA_PROPERTY_NAME = "hasPrimitiveAxisDirection_x";
	private static final String AXIS_DIRECTION_Y_DATA_PROPERTY_NAME = "hasPrimitiveAxisDirection_y";
	private static final String AXIS_DIRECTION_Z_DATA_PROPERTY_NAME = "hasPrimitiveAxisDirection_z";
	
	private Dimentional3Data axisDirection;
	
	public DirectionableShapeData(Dimentional3Data axisDirection, Dimentional3Data centroid, long shapeId) {
		super(centroid, shapeId);
		this.axisDirection = axisDirection;
	}
	
	public DirectionableShapeData(Dimentional3Data axisDirection, Dimentional3Data centroid, long shapeId, String individualName) {
		super(centroid, shapeId, individualName);
		this.axisDirection = axisDirection;
	}

	public Dimentional3Data getAxisDirection(){
		return this.axisDirection;
	}
	
	@Override
	public String toString(){
		return super.toString() + "( axisDirection: " + this.axisDirection + ") ; ";
	}
	
	@Override
	protected void addDataPropertyToIndividual(OWLReferences ontoRef, OWLNamedIndividual individual) {
		super.addDataPropertyToIndividual( ontoRef, individual);
		OWLDataProperty axisX_dataProp = ontoRef.getOWLDataProperty( AXIS_DIRECTION_X_DATA_PROPERTY_NAME);
		OWLDataProperty axisY_dataProp = ontoRef.getOWLDataProperty( AXIS_DIRECTION_Y_DATA_PROPERTY_NAME);
		OWLDataProperty axisZ_dataProp = ontoRef.getOWLDataProperty( AXIS_DIRECTION_Z_DATA_PROPERTY_NAME);
		axisDirection.addToOntology( ontoRef,  individual, axisX_dataProp, axisY_dataProp, axisZ_dataProp);
	}
	
	@Override
	protected void replaceDataPropertyToIndividual( OWLReferences ontoRef, OWLNamedIndividual individual) {
		super.replaceDataPropertyToIndividual( ontoRef, individual);
		OWLDataProperty axisX_dataProp = ontoRef.getOWLDataProperty( AXIS_DIRECTION_X_DATA_PROPERTY_NAME);
		OWLDataProperty axisY_dataProp = ontoRef.getOWLDataProperty( AXIS_DIRECTION_Y_DATA_PROPERTY_NAME);
		OWLDataProperty axisZ_dataProp = ontoRef.getOWLDataProperty( AXIS_DIRECTION_Z_DATA_PROPERTY_NAME);
		axisDirection.replaceToOntology( ontoRef,  individual, axisX_dataProp, axisY_dataProp, axisZ_dataProp);
	}
	
	public static List<OWLDataProperty> getDataPropertyList(OWLReferences ontology) {
		List<OWLDataProperty> out = new ArrayList<OWLDataProperty>();
		out.add( ontology.getOWLDataProperty( AXIS_DIRECTION_X_DATA_PROPERTY_NAME));
		out.add( ontology.getOWLDataProperty( AXIS_DIRECTION_Y_DATA_PROPERTY_NAME));
		out.add( ontology.getOWLDataProperty( AXIS_DIRECTION_Z_DATA_PROPERTY_NAME));
		return out;
	}
}
