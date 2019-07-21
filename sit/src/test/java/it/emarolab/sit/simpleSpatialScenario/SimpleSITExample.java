package it.emarolab.sit.simpleSpatialScenario;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.amor.owlInterface.OWLReferencesInterface;
import it.emarolab.sit.owloopDescriptor.MemorySceneDescriptor;
import it.emarolab.sit.simpleSpatialScenario.sceneElement.Cone;
import it.emarolab.sit.simpleSpatialScenario.sceneElement.GeometricPrimitive;
import it.emarolab.sit.simpleSpatialScenario.sceneElement.Plane;
import it.emarolab.sit.simpleSpatialScenario.sceneRelation.SpatialEvaluator;
import it.emarolab.sit.simpleSpatialScenario.sceneRelation.SpatialRelation;

import java.util.HashSet;
import java.util.Set;

/**
 * ...
 * <p>
 * ...
 * <p>
 * <div style="text-align:center;"><small>
 * <b>File</b>:        ${FILE} <br>
 * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
 * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
 * <b>affiliation</b>: DIBRIS, EMAROLab, University of Genoa. <br>
 * <b>date</b>:        20/07/19 <br>
 * </small></div>
 */
public class SimpleSITExample {

    public static final double CONFIDENCE_TH = .6;
    public static final String ONTO_FILE_BASE_PATH = "src/test/resources/";

    private final OWLReferences ontoRef;

    public SimpleSITExample(String refName, String ontoFilePath, String ontoIRIPath){
        this.ontoRef = OWLReferencesInterface.OWLReferencesContainer
                .newOWLReferenceFromFile( refName, ontoFilePath, ontoIRIPath, true);
        this.ontoRef.setOWLManipulatorBuffering( true);

        test();
        test();
        ontoRef.saveOntology( ONTO_FILE_BASE_PATH + "simpleSITProcessed.owl");
    }

    private void test(){

        Set<GeometricPrimitive> sceneElements = new HashSet<>();


        Cone c = new Cone();
        c.setCenter( new Point3D( .1, .1, .1));
        c.setAxis( new Point3D( 0, 0, 1));
        c.setRadius( .5);
        c.setHeight( .2);
        c.setApex( new Point3D( .2, .3, .5));
        sceneElements.add( c);

        Plane p = new Plane();
        p.setCenter( new Point3D( .1, .1, .9));
        p.setAxis( new Point3D( 0, 0, 1));
        p.setHessian( 0.3);
        sceneElements.add( p);

        SpatialEvaluator evaluator = new SpatialEvaluator( sceneElements, ontoRef);
        evaluator.evaluate();
        Set<SpatialRelation> relations = evaluator.getRelations();

        System.out.println( "******************");
        SimpleSIT sit = new SimpleSIT( relations, ontoRef);
        System.out.println( sit.recognise());
        System.out.println( "------------------");
        if (sit.shouldLearn( CONFIDENCE_TH)) {
            MemorySceneDescriptor learned = sit.learn("SCENE-" + cnt++, ontoRef);
            learned.getOntology().applyOWLManipulatorChanges();
            learned.readExpressionAxioms();
            System.out.println( learned);
        }
        System.out.println( "******************");
    }

    private long cnt = 0;

    public static void main(String[] args) {
        // suppress aMOR log
        it.emarolab.amor.owlDebugger.Logger.setPrintOnConsole( false);
        // start test
        new SimpleSITExample("ONTO1",
                ONTO_FILE_BASE_PATH + "simpleSIT.owl",
                "http://www.emarolab.it/sit/simple");
    }
}
