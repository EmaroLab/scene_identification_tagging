package it.emarolab.sit.example.example.tableScenario;

import it.emarolab.sit.example.example.SITExampleBase;
import it.emarolab.sit.example.core.SITInterface;
import it.emarolab.sit.example.example.simpleSpatialScenario.Point3D;
import it.emarolab.sit.example.example.simpleSpatialScenario.SimpleSIT;
import it.emarolab.sit.example.example.tableScenario.sceneElement.Leg;
import it.emarolab.sit.example.example.tableScenario.sceneElement.Pin;
import it.emarolab.sit.example.example.tableScenario.sceneElement.SpatialObject;
import it.emarolab.sit.example.example.tableScenario.sceneRelation.ConnectedRelation;
import it.emarolab.sit.example.example.tableScenario.sceneRelation.TableEvaluator;

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
public class TableSITExample  extends SITExampleBase<ConnectedRelation> {


    public TableSITExample(String refName, String ontoFilePath, String ontoIRIPath) {
        super(refName, ontoFilePath, ontoIRIPath);
    }

    @Override
    protected SITInterface getSIT(Set<ConnectedRelation> relations) {
        return new SimpleSIT( relations, getOntology());
    }

    public static void main(String[] args) {
        // suppress aMOR log
        it.emarolab.amor.owlDebugger.Logger.setPrintOnConsole( false);
        // start evaluateScene
        TableSITExample ex = new TableSITExample("ONTO1", ONTO_FILE_BASE_PATH + "tableSIT.owl", ONTO_IRI);
        ex.evaluateScene( processScene0( ex), "MOUNTED1");
        ex.evaluateScene( processScene1( ex), "MOUNTED2");
        ex.evaluateScene( processScene2( ex), "MOUNTED3");
        ex.evaluateScene( processScene3( ex), "MOUNTED4");
        SITInterface sit = ex.evaluateScene( processScene3( ex));

        ex.getOntology().synchronizeReasoner();
        sit.showMemory();

        ex.getOntology().saveOntology( ONTO_FILE_BASE_PATH + "tableSITProcessed.owl");
    }

    private static void addTablePin(Set<SpatialObject> sceneElements){
        Pin p = new Pin();
        p.setCenter( new Point3D( -1, -1, 0));
        sceneElements.add( p);
        p = new Pin();
        p.setCenter( new Point3D( -1, 1, 0));
        sceneElements.add( p);
        p = new Pin();
        p.setCenter( new Point3D( 1, 1, 0));
        sceneElements.add( p);
        p = new Pin();
        p.setCenter( new Point3D( 1, -1, 0));
        sceneElements.add( p);
    }

    private static Set<ConnectedRelation> processScene0(TableSITExample ex){
        Set<SpatialObject> sceneElements = new HashSet<>();

        addTablePin( sceneElements);

        Leg l = new Leg();
        l.setCenter( new Point3D( -.99, -1.01, 0));
        sceneElements.add( l);


        TableEvaluator evaluator = new TableEvaluator( sceneElements, ex.getOntology());
        evaluator.evaluate();

        return evaluator.getRelations();
    }

    private static Set<ConnectedRelation> processScene1(TableSITExample ex){
        Set<SpatialObject> sceneElements = new HashSet<>();

        addTablePin( sceneElements);

        Leg l = new Leg();
        l.setCenter( new Point3D( -1.01, -.99, 0));
        sceneElements.add( l);

        l = new Leg();
        l.setCenter( new Point3D( -1.02, .98, 0));
        sceneElements.add( l);

        TableEvaluator evaluator = new TableEvaluator( sceneElements, ex.getOntology());
        evaluator.evaluate();

        return evaluator.getRelations();
    }

    private static Set<ConnectedRelation> processScene2(TableSITExample ex){
        Set<SpatialObject> sceneElements = new HashSet<>();

        addTablePin( sceneElements);

        Leg l = new Leg();
        l.setCenter( new Point3D( -1.01, -.99, 0));
        sceneElements.add( l);

        l = new Leg();
        l.setCenter( new Point3D( -1.02, .98, 0));
        sceneElements.add( l);

        l = new Leg();
        l.setCenter( new Point3D( 1, 1, 0));
        sceneElements.add( l);

        l = new Leg();
        l.setCenter( new Point3D( .98, -1.01, 0));
        sceneElements.add( l);

        TableEvaluator evaluator = new TableEvaluator( sceneElements, ex.getOntology());
        evaluator.evaluate();

        return evaluator.getRelations();
    }

    private static Set<ConnectedRelation> processScene3(TableSITExample ex){
        Set<SpatialObject> sceneElements = new HashSet<>();

        addTablePin( sceneElements);

        Leg l = new Leg();
        l.setCenter( new Point3D( -1.01, -.99, 0));
        sceneElements.add( l);

        l = new Leg();
        l.setCenter( new Point3D( -1.02, .98, 0));
        sceneElements.add( l);

        l = new Leg();
        l.setCenter( new Point3D( 1, 1, 0));
        sceneElements.add( l);

        TableEvaluator evaluator = new TableEvaluator( sceneElements, ex.getOntology());
        evaluator.evaluate();

        return evaluator.getRelations();
    }
}