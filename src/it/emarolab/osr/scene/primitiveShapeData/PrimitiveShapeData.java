package it.emarolab.osr.scene.primitiveShapeData;

import java.util.ArrayList;
import java.util.List;

import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import it.emarolab.amor.owlDebugger.Logger;
import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.osr.RecognitionManualSupervising;
import it.emarolab.osr.scene.sceneLearning.SceneLearner;
import it.emarolab.osr.scene.sceneRecognition.PropertiesRelation;


public abstract class PrimitiveShapeData {
	
	public static final Boolean BUFFERIZE_ONTOLOGY_CHANGES = false;
	
	public static final String CENTROID_X_DATA_PROPERTY_NAME = "hasPrimitiveGeometricCenter_x";
	public static final String CENTROID_Y_DATA_PROPERTY_NAME = "hasPrimitiveGeometricCenter_y";
	public static final String CENTROID_Z_DATA_PROPERTY_NAME = "hasPrimitiveGeometricCenter_z";
	
	public static final String PROPERTY_PARAMETER_SHAPE_ID = "hasParameter_Primitive_cnt";
	
	public static final String INDIVIDUAL_NAME_MIDDLE_STR = "_";
	
	public static final Boolean DEBUG = RecognitionManualSupervising.DEBUG;
	private Logger log;
	
	protected Dimentional3Data centroid;
	protected String individualName;
	protected long shapeId; // Probably it will not be used here... it can be set to NULL
	
	protected static Long individualId = 0l;
	
	abstract String getIndividualNamePrefix();
	
	public PrimitiveShapeData( Dimentional3Data centroid, long shapeId){
		this.log = new Logger( this, DEBUG);
		this.centroid = centroid;
		// this.individualName = null; uses property "hasParameter_Primitive_cnt" to use a incremental shape idx
		this.individualName = this.getIndividualNamePrefix() + INDIVIDUAL_NAME_MIDDLE_STR + shapeId;
		this.shapeId = shapeId;
	}
	
	public PrimitiveShapeData( Dimentional3Data centroid, long shapeID, String individualName){
		this.centroid = centroid;
		this.individualName = individualName;
	}
	
	public Dimentional3Data getCentroid(){
		return this.centroid;
	}
	
	public String getIndividualName(){
		return this.individualName;
	}
	
	public long getShapeId(){
		return this.shapeId;
	}
	
	protected Long getIndividualId() {
		return individualId;
	}

	public void setIndividualId(Long individualId) {
		PrimitiveShapeData.individualId = individualId;
	}

	@Override
	public String toString(){
		return "( indName: " + this.individualName + ") ; ( centroid: " + this.centroid + ") ; ";  
	}
	
	// map the centroid to the ontology
	public void addShapeIndividualToOntology( OWLReferences ontoRef){
		mapShapeOntOntology( ontoRef, false);
	}
	public void updateShapeIndividualToOntology(OWLReferences ontoRef) {
		mapShapeOntOntology( ontoRef, true);
	}
	private void mapShapeOntOntology( OWLReferences ontoRef, Boolean update){
		if( individualName == null){
			individualId = getIndivdualId( ontoRef);
			individualName = this.getIndividualNamePrefix() + INDIVIDUAL_NAME_MIDDLE_STR + individualId;
		}
		OWLNamedIndividual individual = ontoRef.getOWLIndividual( this.getIndividualName());
		if( update)
			replaceDataPropertyToIndividual( ontoRef, individual);
		else 
			addDataPropertyToIndividual( ontoRef, individual);

		log.addDebugString( " added to ontology " + ontoRef.toString() + "\n  -->\t" + this.toString());
	}
	
	private Long getIndivdualId( OWLReferences ontoRef) {
		OWLNamedIndividual param = ontoRef.getOWLIndividual( SceneLearner.INDIVIDUAL_NAME_LEARNING_PARAMETER);
		OWLDataProperty prop = ontoRef.getOWLDataProperty( PROPERTY_PARAMETER_SHAPE_ID);
		OWLLiteral value = ontoRef.getOnlyDataPropertyB2Individual(param, prop);
		if( value == null){
			// create count
			OWLLiteral initValue = ontoRef.getOWLLiteral( Long.valueOf( 0));
			ontoRef.addDataPropertyB2Individual( param, prop, initValue);
			return 0l;
		} else {
			// update count
		    individualId = individualId + 1l;
		    OWLLiteral newValue = ontoRef.getOWLLiteral( Long.valueOf( individualId));
			ontoRef.replaceDataProperty(param, prop, value, newValue);
			return individualId;//Long.valueOf( value.getLiteral());
		}
	}

	protected void addDataPropertyToIndividual(OWLReferences ontoRef, OWLNamedIndividual individual){
		OWLDataProperty centroidX_prop = ontoRef.getOWLDataProperty(CENTROID_X_DATA_PROPERTY_NAME);
		OWLDataProperty centroidY_prop = ontoRef.getOWLDataProperty(CENTROID_Y_DATA_PROPERTY_NAME);
		OWLDataProperty centroidZ_prop = ontoRef.getOWLDataProperty(CENTROID_Z_DATA_PROPERTY_NAME);
		centroid.addToOntology( ontoRef, individual, centroidX_prop, centroidY_prop, centroidZ_prop);
	}
	

	protected void replaceDataPropertyToIndividual(OWLReferences ontoRef, OWLNamedIndividual individual) {
		OWLDataProperty centroidX_prop = ontoRef.getOWLDataProperty(CENTROID_X_DATA_PROPERTY_NAME);
		OWLDataProperty centroidY_prop = ontoRef.getOWLDataProperty(CENTROID_Y_DATA_PROPERTY_NAME);
		OWLDataProperty centroidZ_prop = ontoRef.getOWLDataProperty(CENTROID_Z_DATA_PROPERTY_NAME);
		centroid.replaceToOntology( ontoRef, individual, centroidX_prop, centroidY_prop, centroidZ_prop);
	}
	
	// return null if the relation does'n not exists
	public static Float getDataPropertyValue( OWLReferences ontoRef, OWLNamedIndividual individual, OWLDataProperty prop){
		Float out;
		OWLLiteral l = ontoRef.getOnlyDataPropertyB2Individual( individual, prop);
		if( l == null)
			out = null;
		else out = Float.valueOf( l.getLiteral());
		return out;
	}
	
	public static List< OWLDataProperty> getDataPropertyList(OWLReferences ontology) {
		List<OWLDataProperty> out = new ArrayList<OWLDataProperty>();
		out.add( ontology.getOWLDataProperty( CENTROID_X_DATA_PROPERTY_NAME));
		out.add( ontology.getOWLDataProperty( CENTROID_Y_DATA_PROPERTY_NAME));
		out.add( ontology.getOWLDataProperty( CENTROID_Z_DATA_PROPERTY_NAME));
		return out;
	}
	
	public static List< OWLObjectProperty> getObjectPropertyList(OWLReferences ontology) {
		List< OWLObjectProperty> out = new ArrayList< OWLObjectProperty>();
		for( int i = 0; i < PropertiesRelation.PROPERTIES_NAMES_SYMMETRIC_PRIMITIVE.length; i++)
			out.add( ontology.getOWLObjectProperty( PropertiesRelation.PROPERTIES_NAMES_SYMMETRIC_PRIMITIVE[ i]));
		for( int i = 0; i < PropertiesRelation.PROPERTIES_NAMES_PRIMITIVE.length; i++)
			out.add( ontology.getOWLObjectProperty( PropertiesRelation.PROPERTIES_NAMES_PRIMITIVE[ i]));
		return out;
	}
}
