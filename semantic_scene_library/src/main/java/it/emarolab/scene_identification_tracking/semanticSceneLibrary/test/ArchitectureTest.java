package it.emarolab.scene_identification_tracking.semanticSceneLibrary.test;

import it.emarolab.amor.owlDebugger.Logger;
import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.amor.owlInterface.OWLReferencesInterface;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.aMOR.semantic.MORAxiom;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.aMOR.semantic.MORDescriptor;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.aMOR.semantic.MORSemantic;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.synchronisation.Descriptor;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import static it.emarolab.scene_identification_tracking.semanticSceneLibrary.Base.Logger.LOG;
import static it.emarolab.scene_identification_tracking.semanticSceneLibrary.Base.Logger.showDebugs;


public class ArchitectureTest {

    public static final String ONTO_NAME = "ONTO_NAME";
    public static final String ONTO_FILE_SAVING_PATH = //System.getProperty("user.dir") + // todo add to vocabolry
            "resources/semantic-scene-library/example/" + ArchitectureTest.class.getSimpleName() + ".owl";

    public static final String ONTO_FILE_LOADING_PATH = //System.getProperty("user.dir") +
            "resources/semantic-scene-library/Aboxes/empty-object.owl";

    public static final String ONTO_IRI_PATH = "http://www.semanticweb.org/emaroLab/luca-buoncompagni/sit";
    public static final boolean ONTO_BUFFERISE_CHANGES = false;

    public static void main(String[] args) {
        OWLReferences ontoRef = addTestingObjects(ONTO_NAME, ONTO_FILE_LOADING_PATH, ONTO_IRI_PATH, ONTO_BUFFERISE_CHANGES, true, false);

        // update the reasoner to place object individual in the correct class
        long t = System.nanoTime();
        LOG( "Reasoning starting ....");
        ontoRef.synchronizeReasoner();
        double sec = (System.nanoTime() - t) / 1000000;
        LOG( ".... end:" + sec + " ms.");

        // save the ontology by exporting the inferences to see API reasoning effects
        ontoRef.saveOntology( ONTO_FILE_SAVING_PATH); // remember to call the reasoner when opening this file
        LOG( "\tontological representation saved on: " + ONTO_FILE_SAVING_PATH);

    }

    public static OWLReferences addTestingObjects(String ontoName, String ontoFileLoadingPath, String ontoIRI,
                                                  boolean bufferise, boolean logging, boolean onlyFeasible){
        // initialise an ontology with the aMOR interface (used for Semantics.Descriptor)
        OWLReferences ontoRef = OWLReferencesInterface.OWLReferencesContainer.newOWLReferenceFromFileWithPellet(
                ontoName, ontoFileLoadingPath, ontoIRI, bufferise);

        LOG( "\tontological representation loaded from: " + ontoFileLoadingPath);

        // set logging
        Logger.setPrintOnConsole( false); // remove aMOR log
        //Semantics.Base.shutDownLoggers( true);
        showDebugs( logging);


        OWLNamedIndividual ind = ontoRef.getOWLIndividual("ind");
        OWLClass sphere = ontoRef.getOWLClass("Sphere");
        ontoRef.addIndividualB2Class( ind, sphere);
        //ontoRef.synchronizeReasoner();
/*
        MORDescriptor.MORTypeDescriptor descr = new MORDescriptor.MORTypeDescriptor();

        Descriptor.ReadOutcome<OWLNamedIndividual, MORAxiom.MORTyped> d = descr.read(ontoRef, ind);
        LOG( "!!!!!" + d);
        LOG( "$$$$$ " + d.getAxiom());

        MORSemantic.MORType t = new MORSemantic.MORType();
        t.get().getParents().add( ontoRef.getOWLClass( "AAA"));

        LOG( "!!!!!" + descr.write( ontoRef, ind, t));
*/

        MORDescriptor.MORHierarchyDescriptor descr = new MORDescriptor.MORHierarchyDescriptor();
        Descriptor.ReadOutcome<OWLClass, MORAxiom.MORHierarchised> read = descr.read(ontoRef, ontoRef.getOWLClass("Orientable"));
        LOG(" !!!!! " + read);
        read.getAxiom().getChildren().clear();
        read.getAxiom().getChildren().add( ontoRef.getOWLClass( "C"));
        read.getAxiom().getParents().remove( ontoRef.getOWLClass("GeometricPrimitive"));
        read.getAxiom().getParents().add( ontoRef.getOWLClass( "P"));
        LOG(" !!!!! " + descr.write( ontoRef, ontoRef.getOWLClass( "Orientable"),
                new MORSemantic.MORHierarchy( read.getAxiom())));

        return ontoRef;
    }
}