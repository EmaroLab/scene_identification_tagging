package it.emarolab.osr;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLNamedIndividual;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.amor.owlInterface.OWLReferencesInterface.OWLReferencesContainer;
import it.emarolab.osr.scene.primitiveShapeData.PrimitiveShapeData;
import it.emarolab.osr.scene.sceneLearning.SceneConfidenceEvaluator;
import it.emarolab.osr.scene.sceneLearning.SceneLearner;
import it.emarolab.osr.scene.sceneRecognition.SceneIndividualCreator;

public class RecognitionManualSupervising {

	// for logging
	public static final Boolean DEBUG = true; // write log and file
	
	// for loading
	public static final String BASE_PATH = "files/";//"/home/luca-phd/catkin_ws/src/object_perception/semantic_geometric_traking/files/experiment_results/II-sppech-experiment/planes-cylinders/test_18-02-2016_07-20_ /";
	public static final String ONTOLOGY_EXTENSION = ".owl"; 
	public static final String LOAD_FILE_PATH = BASE_PATH + "toSceneExtrapolation" + ONTOLOGY_EXTENSION; // another ontology to start with
	// for saving
	public static final String SAVE_FILE_PATH = BASE_PATH + "scene" + ONTOLOGY_EXTENSION;
	// for onto spec
	public static final String ontoName = "onto1";
	public static final String iriPath	= "http://emaroLab/luca-buoncompagni/2016/3/scene/speechInteraction";
	public static final Integer command = OWLReferencesContainer.COMMAND_LOAD_FILE;
	
	// for onto manage during ROS usage (looping)
//	private static SceneIndividualCreator createdScene = null;
	private static Set< String> allDisjointIndividual = null; 
	
	// given an ontology with primitive populated by a supervisor
	// it applies scene recognition on it
	public static void main( String[] args){
		// load ontology
		OWLReferences ontology = OWLReferencesContainer.newOWLReferenceFromFile( ontoName, LOAD_FILE_PATH, iriPath, PrimitiveShapeData.BUFFERIZE_ONTOLOGY_CHANGES); // use pellet
		
		// you may want to add primitive shape individual by simulating ros messages incoming
		// though the class TrackedShape. it is considered to be in a continuos loop
//		while( true){
//			List<TrackedShape> shapes;// = ....
//					
//			// call reasoning
//			if( createdScene != null){
//				createdScene.removeSceneIndividual();
//				ontology.synchroniseReasoner();
//			}
//			
//			// TRACK !!!!
//			ShapeTracker tr = new ShapeTracker( shapes); 
//			// create individuals
//			new TrakedShapesIndividualCreator( tr, ontology);
//			
//			// make all disjoint individual once
//			makeAllDisjoinedIndividual( ontology)
			
			// call resoning
			ontology.synchroniseReasoner();		
			
			// compute the scene individual
			SceneIndividualCreator createdScene = new SceneIndividualCreator( ontology);
			createdScene.addSceneIndividual();
			// call reasoning
			makeAllDisjoinedIndividual( ontology);
			ontology.synchroniseReasoner();
			
			// evaluate the confidence of the new scene 
			SceneConfidenceEvaluator sceneConf = new SceneConfidenceEvaluator( createdScene, ontology);
			// if it is the case learn new scene
			if( ! sceneConf.isAboveTreshould()){
				SceneLearner learner = new SceneLearner( createdScene.getSceneList(), ontology);
				System.out.println( " NEW SCENE LEARNED !!!!!! " + learner.getSceneName());
			} else System.out.println( "CLASSIFIED !!!!!! (no learning needed)");
			
			// save ontology (clean/update idnvidual)
			ontology.saveOntology( false, SAVE_FILE_PATH);
//		}
	}
	
	
	private static void makeAllDisjoinedIndividual( OWLReferences ontology){
		// make all disjoint individual once
		if( allDisjointIndividual != null)
			ontology.removeDisjointIndividualName( allDisjointIndividual);
		Set< OWLNamedIndividual> allInd = new HashSet< OWLNamedIndividual>();
		allInd.addAll( ontology.getIndividualB2Class( ontology.getFactory().getOWLThing()));
		ontology.makeDisjointIndividuals( allInd);
		allDisjointIndividual = ontology.getOWLObjectName( allInd);
	}
}
