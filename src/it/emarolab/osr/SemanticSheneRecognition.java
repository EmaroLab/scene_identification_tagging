package com.github.rosjava.object_perception.semantic_geometric_traking;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ontologyFramework.OFContextManagement.OWLLibrary;
import ontologyFramework.OFContextManagement.OWLReferences;
import ontologyFramework.OFErrorManagement.OFDebugLogger;

import org.ros.exception.ServiceException;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.service.ServiceResponseBuilder;
import org.ros.node.service.ServiceServer;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.OWLDifferentIndividualsAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntologyChange;

import com.github.rosjava.object_perception.semantic_geometric_traking.globFitRecognition.sceneLearning.SceneConfidenceEvaluator;
import com.github.rosjava.object_perception.semantic_geometric_traking.globFitRecognition.sceneLearning.SceneLearner;
import com.github.rosjava.object_perception.semantic_geometric_traking.globFitRecognition.sceneRecognition.SceneIndividualCreator;
import com.github.rosjava.object_perception.semantic_geometric_traking.globFitRecognition.sceneRecognition.TrakedShapesIndividualCreator;
import com.github.rosjava.object_perception.semantic_geometric_traking.shapeTracking.ShapeTracker;

import parsing_msgs.SemanticSceneRecogniton;
import parsing_msgs.SemanticSceneRecognitonRequest;
import parsing_msgs.SemanticSceneRecognitonResponse;
import parsing_msgs.TrackedShape;

public class SemanticSheneRecognition  extends AbstractNodeMain { //&&AbstractNodeMain {

	// experiment parameters // kinect setted to tilt angle = -50
	public static final Integer SIMULATION_MAX_SCAN_CNT = 20; //50;
	
	// ros parameter
	public static String NODE_NAME = "semanticSceneRecognitonSrv"; //PCManager::SEMANTIC_SCENE_RECOGNITION_SERVICE_NAME ... "geometric_traker"; //"semantic_traking";
    public static String SERVICE_NAME = NODE_NAME;
	
	// parameter to open the ontology
    // [true]   use the BASE_LOAD_FILE_PATH to create the ontology and than learn on it, 
 	// [false]  use the LOAD_FILE_PATH to create the ontology and than learn on it,
	public static final String BASE_PATH = "/home/luca-phd/catkin_ws/src/object_perception/semantic_geometric_traking/files/";
	// for loading
	public static final String ONTOLOGY_EXTENSION = ".owl"; 
	public static final String LOAD_FILE_NAME = "globFit-recognition-base"; // the ontology which starting with
	public static final String LOAD_FILE_PATH = BASE_PATH + LOAD_FILE_NAME + ONTOLOGY_EXTENSION; // another ontology to start with
	// for saving
	public static final String SAVE_FILE_NAME = "globFit-recognition-javed"; // withoud extention .owl !!!!!!!!!!!!
	public static final String SAVE_FILE_SUBDIRECTORY =  BASE_PATH + "experiment_results/test_"; // BASE_PATH + SAVE_FILE_SUBDIRECTORY + "05-07-15/"
	public static final String SERIAL_SAVE_FOLDER_NAME = "serials/";
	public static final String SERIAL_SAVE_SUBFOLDER_NAME = "scan_";
	public static final String LOG_FILE_NAME = "experimentLog.txt";
	public static String saveBasePath, savePath, serialSavePath;
	
	// parameter of the ontology
	public static final String ontoName = "onto1";
	public static final String iriPath	= "http://www.semanticweb.org/emaroLab/luca-buoncompagni/2015/7/semanticSceneRecognitionLearning";//"http://www.semanticweb.org/upitalia/ontologies/2015/7/untitled-ontology-9";
	public static final int command = OWLReferences.LOADFROMFILEcommand;
	public static final Boolean START_GUI = false; // with true the script does not save to file
	public static final Boolean OWLLIBRARY_VERBOSE = true;
	
	public static final Boolean DEBUG = true; // write log and file
	
	// list of individual to do not delete while clean ontology
	public static final String[] BASE_INDIVIDUALS = {SceneLearner.INDIVIDUAL_NAME_LEARNING_PARAMETER, SceneLearner.INDIVIDUAL_NAME_RECOGNITION_PARAMETER}; // at every scan remove all individual except those
	
	private static Long scanId = 0l;
	//private Log rosLog;
	private OFDebugLogger ofLog;
	private OWLReferences ontology;
	
	List<OWLOntologyChange> changes;
    
	@Override
	public GraphName getDefaultNodeName() {
		return GraphName.of( NODE_NAME); //"rosjava/" + NODE_NAME);
	}

	@Override 
	public void onStart(ConnectedNode connectedNode) {
		//log.info( "NODE CREATED !!! ");
		initializeServer( connectedNode);		
		
		@SuppressWarnings("unused")
		ServiceServer<SemanticSceneRecognitonRequest, SemanticSceneRecognitonResponse> semanticSceneRecogniser =
                connectedNode.newServiceServer( SERVICE_NAME, SemanticSceneRecogniton._TYPE,

                        new ServiceResponseBuilder<SemanticSceneRecognitonRequest, SemanticSceneRecognitonResponse>() {
			
							@Override
							public void build(	SemanticSceneRecognitonRequest request, SemanticSceneRecognitonResponse response) 
									throws ServiceException {								
								//log.error( " SERVER CALLED !!! " + shapes.size() + shapes.get(0).getShapeTag());
								serialSavePath = saveBasePath + SERIAL_SAVE_FOLDER_NAME + SAVE_FILE_NAME + "_" + scanId + ONTOLOGY_EXTENSION;							
								
								if( scanId < SIMULATION_MAX_SCAN_CNT){
									// prepare server input
									List<TrackedShape> shapes = request.getShapes().getTrackedShapes();
									
									// call server
									long t1 = System.nanoTime();
									doOnServerCall( shapes, ontology);
									long t2 = System.nanoTime();
									
									// count times of server calls and log time
									Double time = ( t2 - t1) / 1000000D;
									ofLog.addDebugStrign( " SCAN " + scanId + " completed in "  + time + " [ms]" );
									scanId = scanId + 1l;
								} else ofLog.addDebugStrign( " experiment ended !");
                            }
                        });
		
	}

	// initialize ros service, load ontology and set save path	
	private void initializeServer( ConnectedNode connectedNode){
		// create ros logger
		//rosLog = connectedNode.getLog();

		// set the save path
		String testId = new SimpleDateFormat("dd-MM-yyyy_HH-mm").format(new Date());
		saveBasePath = SAVE_FILE_SUBDIRECTORY + testId + "/";
		savePath =  saveBasePath + SAVE_FILE_NAME + ONTOLOGY_EXTENSION;

		// create Ontology Framework logger
		ofLog = new OFDebugLogger( this, DEBUG);
		new File( saveBasePath).mkdirs();
		OFDebugLogger.setPrintOnFile( saveBasePath + "/" + LOG_FILE_NAME); // add double // before name due to a bug to be solved
		ofLog.addDebugStrign( "  SEMANTIC SHENE RECOGNITION server initialised !!!!!!  log written on path " + OFDebugLogger.getPrintOnFile());
		
		// load the ontology
		ontology = new OWLReferences( ontoName, LOAD_FILE_PATH, iriPath, command); // use pellet
		//ontology.setOWLVerbose( OWLLIBRARY_VERBOSE);
		
		/*// run GUI if it is the case
		if( START_GUI)
			new Thread( new GuiRunner( ontoName)).start();
		.. stop it! */
	}
	
	private SceneIndividualCreator createdScene = null;
	private void doOnServerCall( List<TrackedShape> shapes, OWLReferences ontology) {
		// call reasoning
		if( createdScene != null){
			createdScene.removeSceneIndividual();
			ontology.synchroniseReasoner();
		}
		
		ontology.printOntonolyOnConsole();
		
		// TRACK !!!!
		ShapeTracker tr = new ShapeTracker( shapes); 
		// create individuals
		new TrakedShapesIndividualCreator( tr, ontology);
		
		// call reasoning
		makeDisjointIndividual();
		ontology.synchroniseReasoner();		
		
		// compute the scene individual
		createdScene = new SceneIndividualCreator( ontology);
		createdScene.addSceneIndividual();
		// call reasoning
		makeDisjointIndividual();
		ontology.synchroniseReasoner();
		
		// evaluate the confidence of the new scene 
		SceneConfidenceEvaluator sceneConf = new SceneConfidenceEvaluator( createdScene, ontology);
		// if it is the case learn new scene
		if( ! sceneConf.isAboveTreshould()){
			SceneLearner learner = new SceneLearner( createdScene.getSceneList(), ontology);
			ofLog.addDebugStrign( " NEW SCENE LEARNED !!!!!! " + learner.getSceneName());
		} else ofLog.addDebugStrign( "CLASSIFIED !!!!!! (no learning needed)");
		
		// save ontology (clean/update idnvidual)
		saveOntology( serialSavePath, false); // true);s
		//saveOntology( savePath, false);	
		
		ontology = new OWLReferences( ontoName, LOAD_FILE_PATH, iriPath, command); // use pellet
	}

	// Shutdown called
	@Override
	public void onShutdown(Node node) {
		// save learned ontology when the system stops
		ontology.synchroniseReasoner();
		//saveOntology( savePath, false);
		OFDebugLogger.flush();
		super.onShutdown( node);
	}
	
	// other possible node methods .... not used !!!!!!!
	@Override
	public void onError(Node node, Throwable throwable) {
		super.onError( node, throwable);
	}
	// Shutdown executed
	@Override
	public void onShutdownComplete(Node node) {
		super.onShutdownComplete(node);
	}
	
	
	private void saveOntology( String path, Boolean export){
		try{
			OWLLibrary.saveOntology( export, path, ontology);
			ofLog.addDebugStrign( "SAVE exported ONTOLOGY in path : " + path);
		} catch( Exception e){
			try{
				OWLLibrary.saveOntology( false, path, ontology);
				ofLog.addDebugStrign( "[export error!!] SAVE ONTOLOGY in path: " + path);
			} catch( Exception ex){
				ofLog.addDebugStrign( "error !!!! impossible to save ontology on path: " + path + " " + ex.getCause());
			}
		}
	}
	
	Set< OWLNamedIndividual> allDisjointIndividual = null; 
	private String makeDisjointIndividual(){		
		try{
			// remove old different individual axiom
			if( allDisjointIndividual != null){
				OWLDifferentIndividualsAxiom removeDifferentIndAxiom = ontology.getFactory().getOWLDifferentIndividualsAxiom( allDisjointIndividual);
				List<OWLOntologyChange> remove = ontology.getManager().removeAxiom( ontology.getOntology(), removeDifferentIndAxiom);
				ontology.applyChanges( remove);
			}
			// add new different individual axiom
			Set< OWLNamedIndividual> allInd = new HashSet< OWLNamedIndividual>();
			allInd.addAll( ontology.getIndividualB2Class( ontology.getFactory().getOWLThing()));
			OWLDifferentIndividualsAxiom differentIndAxiom = ontology.getFactory().getOWLDifferentIndividualsAxiom( allInd);
			ontology.applyChanges( new AddAxiom( ontology.getOntology(), differentIndAxiom));
			String log = " set different individuals: { ";
			for( OWLNamedIndividual i : allInd)
				log += OWLLibrary.getOWLObjectName( i) + ", ";
			allDisjointIndividual = allInd;
			return log + "}";
		} catch( Exception e ){
			e.printStackTrace();
		}
		return "";
	}
}
