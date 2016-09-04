package it.emarolab.osr.scene.sceneRecognition;

//import com.github.rosjava.object_perception.semantic_geometric_traking.SemanticSheneRecognition; 
//import com.github.rosjava.object_perception.semantic_geometric_traking.globFitRecognition.primitiveShapeData.PrimitiveShapeData;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import it.emarolab.amor.owlDebugger.Logger;
import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.osr.RecognitionManualSupervising;

public class SceneIndividualCreator {

	public static final String INDIVIDUAL_NAME_SCENE = "Scene";
	public static final String CLASS_NAME_PRIMITIVE = "Primitive";
	public static final String[] NO_PRIMITIVE_CLASS_NAME = {"Directionable"};
	public static final String CLASS_NAME_SCENE = "Scene";
	public static final String DATA_PROPERTY_NAME_CARDINALITY = "hasSceneCardinality";
		
	private List< PrimitiveRelation> sceneList;
	private static OWLReferences ontology;
	private Integer cardinality = 0; // used to compute classification confidence
	private OWLNamedIndividual sceneIndividual;
	
	private Logger log;
	public static final Boolean DEBUG = RecognitionManualSupervising.DEBUG;
	
	public void removeSceneIndividual() {
		// add scene primitive relations 
		sceneIndividual = ontology.getOWLIndividual( INDIVIDUAL_NAME_SCENE);
		// remove all old values
		for( PrimitiveRelation rel : sceneList){
			OWLObjectProperty prop = rel.getRelation().getSceneProperty(ontology);
			if( sceneIndividual != null){
				Set< OWLNamedIndividual> toRemove = ontology.getObjectPropertyB2Individual( sceneIndividual, prop);
				if( toRemove != null)
					for( OWLNamedIndividual value: toRemove)
						ontology.removeObjectPropertyB2Individual(sceneIndividual, prop, value);
			}
		}
		// remove cardinality as a property to the scene individual
		OWLDataProperty prop = ontology.getOWLDataProperty( DATA_PROPERTY_NAME_CARDINALITY);
		Set<OWLLiteral> toRemove = ontology.getDataPropertyB2Individual( sceneIndividual, prop);
		if( toRemove != null)
			for( OWLLiteral value : toRemove)
				ontology.removeDataPropertyB2Individual( sceneIndividual, prop, value);
	}
	
	// use the populate scene list to add the individual scene to the ontology
	public void addSceneIndividual() {
		List< Relationships> addedRelations = new ArrayList< Relationships>();
		// add scene primitive relations 
		sceneIndividual = ontology.getOWLIndividual( INDIVIDUAL_NAME_SCENE);
		// add new property
		for( PrimitiveRelation rel : sceneList){
			if( ! containsRelation( addedRelations, rel)){ // if not olready added
				OWLObjectProperty prop = rel.getRelation().getSceneProperty(ontology);
				ontology.addObjectPropertyB2Individual( sceneIndividual, prop, rel.getPreInd());
				addedRelations.add( new Relationships(prop, rel.getPreInd()));
				//log.addDebugStrign( " ADDING 1 " + OWLLibrary.getOWLObjectName(sceneIndividual) + " " + OWLLibrary.getOWLObjectName(prop) + " " + OWLLibrary.getOWLObjectName( rel.getPreInd()));
				ontology.addObjectPropertyB2Individual( sceneIndividual, prop, rel.getPostInd());
				addedRelations.add( new Relationships(prop, rel.getPreInd()));
				//log.addDebugStrign( " ADDING 2 " + OWLLibrary.getOWLObjectName(sceneIndividual) + " " + OWLLibrary.getOWLObjectName(prop) + " " + OWLLibrary.getOWLObjectName( rel.getPostInd()));
			}
		}
		// add cardinality as a property to the scene individual
		OWLDataProperty prop = ontology.getOWLDataProperty( DATA_PROPERTY_NAME_CARDINALITY);
		// add new value
		OWLLiteral value = ontology.getOWLLiteral( cardinality);
		ontology.addDataPropertyB2Individual( sceneIndividual, prop, value);
	}
	
	private Boolean containsRelation( List< Relationships> addedRelations, PrimitiveRelation rel){
		for( Relationships r : addedRelations)
			if( r.equals( rel))
				return true;
		return false;
	}
	
	// create and populate primitive scene list
	public SceneIndividualCreator( OWLReferences ontology){
		this.log = new Logger( this, DEBUG);
		
		sceneList = new ArrayList< PrimitiveRelation>();
		SceneIndividualCreator.ontology = ontology;
		
		Set< OWLNamedIndividual> primitiveIndividuals = ontology.getIndividualB2Class( CLASS_NAME_PRIMITIVE);
		List< PropertiesRelation> allRelations = PropertiesRelation.initializeAllRelation();
		// for all the primitive shape
		if( primitiveIndividuals != null && allRelations != null){
			for( OWLNamedIndividual prInd : primitiveIndividuals){
				// for all the primitive shape relations
				for( PropertiesRelation prRel : allRelations){
					Set< OWLNamedIndividual> primitiveValues = prRel.getPropertyValue( ontology, prInd);
					// for all the values of the property applied to the individual
					for( OWLNamedIndividual prValue : primitiveValues){
						//log.addDebugStrign( "$$$$$$ " + OWLLibrary.getOWLObjectName( prInd) + " " + prRel + " " + OWLLibrary.getOWLObjectName( prValue));
						this.populateList( prInd, prRel, prValue);
					}
				}
			}
		}
		cardinality = sceneList.size() * 2; // consider also inverse
		log.addDebugString( " sceneList: " + sceneList);
	}
	
	public List<PrimitiveRelation> getSceneList() {
		return sceneList;
	}

	public Integer getCardinality() {
		return cardinality;
	}

	// create primitive relation and add it to the scene list
	private void populateList(OWLNamedIndividual prInd, PropertiesRelation prRel, OWLNamedIndividual prValue) {
		PrimitiveRelation candidateRelation = new PrimitiveRelation( prInd, prRel, prValue);
		if( ! isInverseListed( candidateRelation))
			sceneList.add( new PrimitiveRelation( prInd, prRel, prValue));
	}

	// check if a primitive relation is equal to another. (equals means the same relation or it inverse)
	private boolean isInverseListed( PrimitiveRelation candidateRelation) {
		//log.addDebugStrign("----------");
		for( PrimitiveRelation alreadyRelation : sceneList)
			if( alreadyRelation.equals( candidateRelation))
				return true;
		return false;
	}
	
	public String getSceneIndividualName(){
		return OWLReferences.getOWLName( sceneIndividual);
	}
	public OWLNamedIndividual getSceneIndividual(){
		return sceneIndividual;
	}

	
	public class Relationships{
		private OWLNamedIndividual value;
		private OWLObjectProperty property;
		public Relationships( OWLObjectProperty property, OWLNamedIndividual value) {
			this.value = value;
			this.property = property;
		}
		public OWLNamedIndividual getValue() {
			return value;
		}
		public OWLObjectProperty getProperty() {
			return property;
		}
		@Override
		public String toString() {
			return "Relationships [property=" + OWLReferences.getOWLName( property) + ", value=" + OWLReferences.getOWLName( value) + "]";
		}
		@Override
		public boolean equals(Object obj) {
			if( obj instanceof Relationships){
				Relationships r = ( Relationships) obj;
				if( this.value.equals( r.value))
					if( this.property.equals( r.property))
						return true;
				return false;
			} else if( obj instanceof PrimitiveRelation){
				PrimitiveRelation p = ( PrimitiveRelation) obj;
				if( p.getRelation().getPropertyName().equals( OWLReferences.getOWLName( this.property)))
					if( p.getPostInd().equals( this.value) || p.getPreInd().equals( this.value))
						return true;
				return false;	
			}
			return super.equals(obj);
		}		
	}
}
