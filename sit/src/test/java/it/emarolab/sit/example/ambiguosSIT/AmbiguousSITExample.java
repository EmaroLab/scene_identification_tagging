package it.emarolab.sit.example.ambiguosSIT;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.sit.example.core.RelationInterface;
import it.emarolab.sit.example.core.SITInterface;
import it.emarolab.sit.example.simpleSpatialScenario.Point3D;
import it.emarolab.sit.example.simpleSpatialScenario.SimpleSIT;
import it.emarolab.sit.example.simpleSpatialScenario.SimpleSITExample;
import it.emarolab.sit.example.simpleSpatialScenario.sceneElement.Cone;
import it.emarolab.sit.example.simpleSpatialScenario.sceneElement.GeometricPrimitive;
import it.emarolab.sit.example.simpleSpatialScenario.sceneElement.Plane;
import it.emarolab.sit.example.simpleSpatialScenario.sceneElement.Sphere;
import it.emarolab.sit.example.simpleSpatialScenario.sceneRelation.SpatialEvaluator;
import it.emarolab.sit.example.simpleSpatialScenario.sceneRelation.SpatialRelation;

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
 * <b>date</b>:        21/07/19 <br>
 * </small></div>
 */
public class AmbiguousSITExample extends SimpleSITExample {


    public AmbiguousSITExample(String refName, String ontoFilePath, String ontoIRIPath) {
        super(refName, ontoFilePath, ontoIRIPath);
    }

    @Override
    protected SITInterface getSIT(Set<SpatialRelation> relations) {
        return new AmbiguousSIT(relations, getOntology());
    }

    public static void main(String[] args) {
        // suppress aMOR log
        it.emarolab.amor.owlDebugger.Logger.setPrintOnConsole( false);
        // start evaluateScene
        SimpleSITExample ex = new SimpleSITExample("ONTO1", ONTO_FILE_NEME, ONTO_IRI);
        ex.evaluateScene( SimpleSITExample.processScene0( ex));
        ex.evaluateScene( SimpleSITExample.processScene1( ex));
        ex.evaluateScene( SimpleSITExample.processScene2( ex));
        ex.evaluateScene( SimpleSITExample.processScene3( ex));
        ex.evaluateScene( SimpleSITExample.getScene4( ex));
        SITInterface sit = ex.evaluateScene( processScene5( ex));

        ex.getOntology().synchronizeReasoner();
        sit.showMemory();

        ex.getOntology().saveOntology( ONTO_FILE_BASE_PATH + "ambiguousSITProcessed.owl");
    }

    private static Set<SpatialRelation> processScene5(SimpleSITExample ex){
        Set<GeometricPrimitive> sceneElements = new HashSet<>();

        Cone c = new Cone();
        c.setCenter( new Point3D( .1, .1, .1));
        c.setAxis( new Point3D( 0, .05, 1));
        sceneElements.add( c);

        Sphere s = new Sphere();
        s.setCenter( new Point3D( -.9, -.9, -.9));
        sceneElements.add( s);

        Cone a = new Cone();
        a.setCenter( new Point3D( .1, .1, .1));
        a.setAxis( new Point3D( 0, .05, 1));
        sceneElements.add( a);

        Plane p = new Plane();
        p.setCenter( new Point3D( .9, .9, .9));
        p.setAxis( new Point3D( 0, 0, 1));
        sceneElements.add( p);


        SpatialEvaluator evaluator = new SpatialEvaluator( sceneElements, ex.getOntology());
        evaluator.evaluate();

        return evaluator.getRelations();
    }

}


class AmbiguousSIT extends SimpleSIT {

    public AmbiguousSIT(Set<SpatialRelation> relations, OWLReferences ontology) {
        super(relations, ontology);
    }

    @Override
    public String reifyRelation(RelationInterface r) {
        return r.getRelationName();
    }
}
