package it.emarolab.osr.scene.sceneLearning;

import java.util.Set;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import it.emarolab.amor.owlDebugger.Logger;
import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.osr.RecognitionManualSupervising;
import it.emarolab.osr.scene.sceneRecognition.SceneIndividualCreator;

public class SceneConfidenceEvaluator {

	public static final String PROPERTY_NAME_CONFIDENCE_TRESHOULD = "hasParameter_Confidence_th";
	public static final String PROPERTY_BASE_NAME_RECOGNITION_COUNT = "hasParameter_RecognitionTimes_";
	
	private Logger log;
	public static final Boolean DEBUG = RecognitionManualSupervising.DEBUG;
	
	private Integer classCardinality, individualCardinality;
	private Float sceneConfidence;
	private Float confidenceThresould;
	private Set< OWLClass> sceneClasses;
	
	public SceneConfidenceEvaluator( SceneIndividualCreator scene, OWLReferences ontology){
		this.log = new Logger( this, DEBUG);
		// get threshould value from ontology
		OWLNamedIndividual param = ontology.getOWLIndividual( SceneLearner.INDIVIDUAL_NAME_LEARNING_PARAMETER);
		OWLDataProperty prop = ontology.getOWLDataProperty( PROPERTY_NAME_CONFIDENCE_TRESHOULD);
		confidenceThresould = ontology.getOnlyDataPropertyB2Individual(param, prop).parseFloat();
		// get scene cardinality
		individualCardinality = scene.getCardinality();
		// get max cardinality of all the class where scene individual belongs to
		OWLNamedIndividual sceneIndividual = scene.getSceneIndividual();//ontology.getOWLIndividual( SceneIndividualCreator.INDIVIDUAL_NAME_SCENE);
		sceneClasses = getAllSceneClasses( sceneIndividual, ontology);
		// log recognised scenes and count how many time it has been recognised
		String logStr = " scene recognised in calsses: { ";
		for( OWLClass scl :  sceneClasses) {
			logStr +=  OWLReferences.getOWLName( scl) + ", ";
			countRecognisedShene( scl, ontology);
		}
		log.addDebugString( logStr + "}");
		classCardinality = getMaxClassCardinality( sceneIndividual, sceneClasses, ontology);
		// compute confidence
		sceneConfidence = computeSceneConfidence( classCardinality, individualCardinality);
		log.addDebugString( " compute scene confidence= " + sceneConfidence + " ( ==  " + classCardinality + " / " + individualCardinality + ")");
	}
	
	// set a property to the learning parameter as "hasParameter_RecognitionCount_SceneClassName exactly 1 x^^integer"
	private void countRecognisedShene( OWLClass recognisedClass, OWLReferences ontology){
		OWLNamedIndividual paramInd = ontology.getOWLIndividual( SceneLearner.INDIVIDUAL_NAME_LEARNING_PARAMETER);
		OWLDataProperty prop = ontology.getOWLDataProperty( PROPERTY_BASE_NAME_RECOGNITION_COUNT + OWLReferences.getOWLName( recognisedClass));
		// remove old values and get the actual count name
		Set< OWLLiteral> values = ontology.getDataPropertyB2Individual(paramInd, prop);
		Integer maxValue = 0;
		for( OWLLiteral l : values){
			Integer value = Integer.valueOf( l.getLiteral());
			if( value > maxValue)
				maxValue = value;
			ontology.removeDataPropertyB2Individual(paramInd, prop, l);
		}
		OWLLiteral newValue = ontology.getOWLLiteral( maxValue + 1);
		ontology.addDataPropertyB2Individual(paramInd, prop, newValue);
	}

	private Set<OWLClass> getAllSceneClasses(OWLNamedIndividual sceneIndividual, OWLReferences ontology) {
		Set< OWLClass> allClasses = ontology.getIndividualClasses( sceneIndividual);
		allClasses.remove( ontology.getOWLClass( SceneIndividualCreator.CLASS_NAME_SCENE));
		allClasses.remove( ontology.getFactory().getOWLThing());
		return allClasses;
	}
	
	private Integer getMaxClassCardinality(OWLNamedIndividual sceneIndividual, Set<OWLClass> sceneClasses, OWLReferences ontology) {
		Integer maxCardinality = 0;
		for( OWLClass cl : sceneClasses){
			OWLNamedIndividual paramInd = ontology.getOWLIndividual( SceneLearner.INDIVIDUAL_NAME_LEARNING_PARAMETER);
			OWLDataProperty prop = ontology.getOWLDataProperty( SceneLearner.PROPERTY_BASE_NAME_CARDINALITY + ontology.getOWLObjectName( cl));
			OWLLiteral cardinalityLiteral = ontology.getOnlyDataPropertyB2Individual(paramInd, prop);
			if( cardinalityLiteral != null){
				Integer cardinality = Integer.valueOf( cardinalityLiteral.getLiteral());
				if( cardinality > maxCardinality)
					maxCardinality = cardinality;
			}
		}
		return maxCardinality;
	}
	
	private Float computeSceneConfidence( int classCardinality, int individualCardinality){
		if( individualCardinality != 0)
			return Float.valueOf( classCardinality) / Float.valueOf( individualCardinality);
		else return 0.0f;
	}

	public Boolean isAboveTreshould(){
		if( sceneConfidence > confidenceThresould)
			return true;
		return false;
	}
	
	public Float getConfidenceThresould(){
		return confidenceThresould;
	}
	
	public Integer getClassCardinality() {
		return classCardinality;
	}

	public Integer getIndividualCardinality() {
		return individualCardinality;
	}

	public Float getSceneConfidence() {
		return sceneConfidence;
	}

	public Set<OWLClass> getSceneClasses() {
		return sceneClasses;
	}
	
}
