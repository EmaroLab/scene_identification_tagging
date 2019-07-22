package it.emarolab.sit.example.simpleSpatialScenario;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.sit.example.SITExampleBase;
import it.emarolab.sit.core.SITInterface;
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
 * <b>date</b>:        20/07/19 <br>
 * </small></div>
 */
public class SimpleSITExample extends SITExampleBase<SpatialRelation, SimpleSIT> {

    public SimpleSITExample(OWLReferences ontoRef) {
        super( ontoRef);
    }

    public SimpleSITExample(String refName, String ontoFilePath, String ontoIRIPath) {
        super(refName, ontoFilePath, ontoIRIPath);
    }

    @Override
    protected SimpleSIT getSIT(Set<SpatialRelation> relations) {
        return new SimpleSIT( relations, getOntology());
    }

    public static void main(String[] args) {
        // suppress aMOR log
        it.emarolab.amor.owlDebugger.Logger.setPrintOnConsole( false);
        // start evaluateScene
        SimpleSITExample ex = new SimpleSITExample("ONTO1", ONTO_FILE_NEME, ONTO_IRI);
        ex.evaluateScene( processScene0( ex));
        ex.evaluateScene( processScene1( ex));
        ex.evaluateScene( processScene2( ex));
        ex.evaluateScene( processScene3( ex));
        SITInterface sit = ex.evaluateScene( getScene4( ex));

        ex.getOntology().synchronizeReasoner();
        sit.showMemory();

        ex.getOntology().saveOntology( ONTO_FILE_BASE_PATH + "simpleSITProcessed.owl");
    }






    protected static Set<SpatialRelation> processScene0(SimpleSITExample ex){
        Set<GeometricPrimitive> sceneElements = new HashSet<>();

        Cone c = new Cone();
        c.setCenter( new Point3D( 0, 0, 0));
        c.setAxis( new Point3D( 0, 0, 1));
        sceneElements.add( c);

        Cone p = new Cone();
        p.setCenter( new Point3D( 1, 0, 0));
        p.setAxis( new Point3D( .5, .5, 0));
        sceneElements.add( p);

        SpatialEvaluator evaluator = new SpatialEvaluator( sceneElements, ex.getOntology());
        return evaluator.getRelations();
    }

    protected static Set<SpatialRelation> processScene1(SimpleSITExample ex){
        Set<GeometricPrimitive> sceneElements = new HashSet<>();

        Cone c = new Cone();
        c.setCenter( new Point3D( .1, .1, .1));
        c.setAxis( new Point3D( 0, .05, 1));
        sceneElements.add( c);

        Plane p = new Plane();
        p.setCenter( new Point3D( .9, .9, .9));
        p.setAxis( new Point3D( 0, 0, 1));
        sceneElements.add( p);

        SpatialEvaluator evaluator = new SpatialEvaluator( sceneElements, ex.getOntology());
        return evaluator.getRelations();
    }

    protected static Set<SpatialRelation> processScene2(SimpleSITExample ex){
        Set<GeometricPrimitive> sceneElements = new HashSet<>();

        Cone c = new Cone();
        c.setCenter( new Point3D( .1, .1, .1));
        c.setAxis( new Point3D( 0, .05, 1));
        sceneElements.add( c);

        Plane p = new Plane();
        p.setCenter( new Point3D( .9, .9, .9));
        p.setAxis( new Point3D( 0, 0, 1));
        sceneElements.add( p);

        Sphere s = new Sphere();
        s.setCenter( new Point3D( -.9, -.9, -.9));
        sceneElements.add( s);


        SpatialEvaluator evaluator = new SpatialEvaluator( sceneElements, ex.getOntology());
        return evaluator.getRelations();
    }

    protected static Set<SpatialRelation> processScene3(SimpleSITExample ex){
        Set<GeometricPrimitive> sceneElements = new HashSet<>();

        Cone c = new Cone();
        c.setCenter( new Point3D( .1, .1, .1));
        c.setAxis( new Point3D( 0, .05, 1));
        sceneElements.add( c);

        Sphere s = new Sphere();
        s.setCenter( new Point3D( -.9, -.9, -.9));
        sceneElements.add( s);


        SpatialEvaluator evaluator = new SpatialEvaluator( sceneElements, ex.getOntology());
        return evaluator.getRelations();
    }

    protected static Set<SpatialRelation> getScene4(SimpleSITExample ex){
        Set<GeometricPrimitive> sceneElements = new HashSet<>();

        Sphere s = new Sphere();
        s.setCenter( new Point3D( -.1, -.1, -.9));
        sceneElements.add( s);

        s = new Sphere();
        s.setCenter( new Point3D( -.9, -.9, -.9));
        sceneElements.add( s);

        s = new Sphere();
        s.setCenter( new Point3D( -.7, -.7, -.7));
        sceneElements.add( s);

        SpatialEvaluator evaluator = new SpatialEvaluator( sceneElements, ex.getOntology());
        return evaluator.getRelations();
    }

}
