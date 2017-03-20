package it.emarolab.scene_identification_tracking.semanticSceneLibrary.test;

import it.emarolab.amor.owlDebugger.Logger;
import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.amor.owlInterface.OWLReferencesInterface;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.aMOR.semantic.MORDescriptor;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.aMOR.semantic.MORSemantic;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
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
        OWLDataProperty prop = ontoRef.getOWLDataProperty("data-prop");
        //ontoRef.synchronizeReasoner();

/*      // TEST TYPED DESCRIPTOR
        MORDescriptor.MORTypeDescriptor descr = new MORDescriptor.MORTypeDescriptor();

        Descriptor.ReadOutcome<OWLNamedIndividual, MORAxiom.MORTyped> d = descr.read(ontoRef, ind);
        LOG( "!!!!!" + d);
        LOG( "$$$$$ " + d.getAxiom());

        MORSemantic.MORType t = new MORSemantic.MORType();
        t.get().getParents().add( ontoRef.getOWLClass( "AAA"));

        LOG( "!!!!!" + descr.write( ontoRef, ind, t));
*/

/*      // TEST HIERARCHY DESCRIPTOR
        MORDescriptor.MORHierarchyDescriptor descr = new MORDescriptor.MORHierarchyDescriptor();
        Descriptor.ReadOutcome<OWLClass, MORAxiom.MORHierarchised> read = descr.read(ontoRef, ontoRef.getOWLClass("Orientable"));
        LOG(" !!!!! " + read);
        read.getAxiom().getChildren().clear();
        read.getAxiom().getChildren().add( ontoRef.getOWLClass( "C"));
        read.getAxiom().getParents().remove( ontoRef.getOWLClass("GeometricPrimitive"));
        read.getAxiom().getParents().add( ontoRef.getOWLClass( "P"));
        LOG(" !!!!! " + descr.write( ontoRef, ontoRef.getOWLClass( "Orientable"),
                new MORSemantic.MORHierarchy( read.getAxiom())));
*/

/*        // TEST LITERAL DESCRIPTOR
        ontoRef.addDataPropertyB2Individual( ind, prop, ontoRef.getOWLLiteral( 1.2));
        MORDescriptor.MORLiteralDescriptor descr = new MORDescriptor.MORLiteralDescriptor();
        MORSemantic.MORLiteral literalSem = new MORSemantic.MORLiteral(prop);
        LOG( " !!!!!! "  + descr.read( ontoRef, ind, literalSem));
        literalSem.get().setAtom( ontoRef.getOWLLiteral( "Str"));
        LOG( " !!!!!! "  + descr.write( ontoRef, ind, literalSem));
*/

        // TEST MULTI LITERAL DESCRIPTOR
        ontoRef.addDataPropertyB2Individual( ind, prop, ontoRef.getOWLLiteral( 1.2));
        ontoRef.addDataPropertyB2Individual( ind, prop, ontoRef.getOWLLiteral( 1.3));
        ontoRef.addDataPropertyB2Individual( ind, prop, ontoRef.getOWLLiteral( 1.4));
        MORDescriptor.MORLiteralsDescriptor descr = new MORDescriptor.MORLiteralsDescriptor();
        MORSemantic.MORLiterals literalsSem = new MORSemantic.MORLiterals( prop);
        LOG( " !!!!!! "  + descr.read( ontoRef, ind, literalsSem));
        literalsSem.get().remove( ontoRef.getOWLLiteral( 1.2));
        LOG( " !!!!!! "  + descr.write( ontoRef, ind, literalsSem));
        literalsSem.get().clear();
        LOG( " !!!!!! "  + descr.write( ontoRef, ind, literalsSem));
        literalsSem.get().add( ontoRef.getOWLLiteral( "new"));
        LOG( " !!!!!! "  + descr.write( ontoRef, ind, literalsSem));
        
        return ontoRef;
    }
}