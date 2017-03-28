package it.emarolab.scene_identification_tracking.semanticSceneLibrary.semanticInterface.test;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.amor.owlInterface.OWLReferencesInterface;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.semanticInterface.Base;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.semanticInterface.aMOR.MORAtom;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.semanticInterface.aMOR.MORGround;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.semanticInterface.core.Semantic;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLLiteral;

/**
 * Created by bubx on 28/03/17.
 */
public class SemanticTest {

    public static final String ONTO_NAME = "ONTO_NAME";
    public static final String ONTO_FILE_SAVING_PATH = //System.getProperty("user.dir") + // todo add to vocabolry
            "resources/semantic-scene-library/example/";
    public static final String ONTO_FILE_LOADING_PATH = //System.getProperty("user.dir") +
            "resources/semantic-scene-library/Aboxes/empty-object.owl";
    public static final String ONTO_IRI_PATH = "http://www.semanticweb.org/emaroLab/luca-buoncompagni/sit";
    public static final boolean ONTO_BUFFERISE_CHANGES = false;

    public static void main(String[] args) {

        OWLReferences onto = loadOWLReference( ONTO_FILE_SAVING_PATH, ONTO_IRI_PATH);
        MORGround.GroundIndividual ground = new MORGround.GroundIndividual(onto);
        ground.setInstance( "T");

        OWLDataProperty semantic = onto.getOWLDataProperty( "literalProperty-test");

        LiteralTest test = new LiteralTest( ground, semantic);
        test.doTest();

        //onto.saveOntology(  ONTO_FILE_SAVING_PATH + "testName.owl");
    }

    public static OWLReferences loadOWLReference(String ontoFileLoadingPath, String ontoIRI){
        // initialise an ontology with the aMOR interface (used for Semantics.Descriptor)
        OWLReferences ontoRef = OWLReferencesInterface.OWLReferencesContainer.newOWLReferenceFromFileWithPellet(
                ONTO_NAME, ontoFileLoadingPath, ontoIRI, ONTO_BUFFERISE_CHANGES);

        Base.Logger.LOG( "\tontological representation loaded from: " + ontoFileLoadingPath);

        // set logging
        it.emarolab.amor.owlDebugger.Logger.setPrintOnConsole( false); // remove aMOR log
        // Base.Logger.shutDownLoggers( true);
        Base.Logger.showDebugs( true);

        return ontoRef;
    }

    abstract static public class AtomTest<S,Y> // tod AtomsTest
            extends Test.AtomTest< MORGround<?>,S,MORAtom.MORAtomBase<S,Y>,Y>{

        public AtomTest(MORGround<?> instance, S semantic, MORAtom.MORAtomBase<S,Y> toTest) {
            super(instance, semantic, toTest);
        }
        public AtomTest(MORGround<?> instance, S semantic) {
            super(instance, semantic);
        }

        protected MORAtom.MORAtomBase<S, Y> newTest(){
            MORAtom.MORAtomBase<S, Y> newToTest = getNewToTest();
            newToTest.set( getAtomToTest());
            return newToTest;
        }


        @Override
        public Semantic.Transitions doTest() {

            MORAtom.MORAtomBase<S, Y> test = newTest();

            // hp:  java : null  &  semantic : null
            toTest.addAxiom( instance, semantic, test.get());
            //      java : null  &  semantic : *
            toTest.removeAxiom( instance, semantic, test.get());
            //      java : null  &  semantic : null
            test.set( getAtomToTest());
            //      java : *     &  semantic : null
            toTest.addAxiom( instance, semantic, newTest().get());
            //      java : *     &  semantic : *
            toTest.set( getAtom2ToTest());
            //      java : +     &  semantic : *
            toTest.removeAxiom( instance, semantic, newTest().get());
            //      java : +     &  semantic : *  [absent]
            toTest.addAxiom( instance, semantic, newTest().get());
            //      java : +     &  semantic : +
            toTest.clear();
            //      java : null  &  semantic : +
            toTest.queryAxiom( instance, semantic);
            //      java : +     &  semantic : +

            return null; // todo !?!?!?!?!?!!?!?!?
        }

        @Override
        public Base copy() {
            return null; // todo implement
        }
    }

    public static class LiteralTest
            extends AtomTest<OWLDataProperty, OWLLiteral> {

        public LiteralTest( MORGround.GroundIndividual instance, OWLDataProperty semantic, MORAtom.MORLiteral toTest) {
            super(instance, semantic, toTest);
        }
        public LiteralTest( MORGround.GroundIndividual instance, OWLDataProperty semantic) {
            super(instance, semantic);
        }

        @Override
        public MORAtom.MORLiteral getNewToTest() {
            return new MORAtom.MORLiteral();
        }

        @Override
        public OWLLiteral getAtomToTest() {
            return getInstance().getOntology().getOWLLiteral( "test");
        }

        @Override
        public OWLLiteral getAtom2ToTest() {
            return getInstance().getOntology().getOWLLiteral( "test2"); // todo try with numbers
        }
    }
}
