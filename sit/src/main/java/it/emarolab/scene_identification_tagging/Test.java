package it.emarolab.scene_identification_tagging;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.amor.owlInterface.OWLReferencesInterface;
import it.emarolab.scene_identification_tagging.owloopDescriptor.SceneClassDescriptor;
import it.emarolab.scene_identification_tagging.realObject.*;
import it.emarolab.scene_identification_tagging.sceneRepresentation.SceneRepresentation;

import java.util.HashSet;
import java.util.Set;

/**
 * A runnable example to show how to use the SIT.
 * <p>
 *     This is a simple example that shows how to use the system out of the
 *     ROS environment. It is based in the ontology located in
 *     {@code resources/t_box/empty-scene.owl}. It creates
 *     a scene of two objects, try to recognise it and,
 *     since it cannot, it learn a new representation.
 *     Finally reapply the recognition to show that after words it is
 *     able to recognise it.
 *     <br>
 *     This is done two time, by adding a new object, in order to show
 *     the ability to hierarchically describe scenes and asses
 *     a recognition confidence.
 *     <br>
 *     Numerical value of the object coefficients are given just
 *     for showing purposes and the scene does not have a specific representation.
 *
 * <div style="text-align:center;"><small>
 * <b>File</b>:        it.emarolab.scene_identification_tagging.Test <br>
 * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
 * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
 * <b>affiliation</b>: EMAROLab, DIBRIS, University of Genoa. <br>
 * <b>date</b>:        05/06/17 <br>
 * </small></div>
 */
public class Test
        implements SITBase{

    private static final String ONTO_NAME = "ONTO_NAME"; // an arbitrary name to refer the ontology

    public static void main(String[] args){

        // load ontology
        OWLReferences ontoRef = OWLReferencesInterface.OWLReferencesContainer.newOWLReferenceFromFileWithPellet(
                ONTO_NAME, ONTO_FILE, ONTO_IRI, true);

        // suppress aMOR log
        it.emarolab.amor.owlDebugger.Logger.setPrintOnConsole( false);

        // initialise objects
        Set< GeometricPrimitive> objects = new HashSet<>();

        // define objects
        Sphere s = new Sphere( ontoRef);
        s.shouldAddTime( true);
        s.setCenter( .3f, .3f, .3f);
        s.setRadius( .1f);
        objects.add( s);

        Plane p = new Plane( ontoRef);
        p.shouldAddTime( true);
        p.setAxis( .5f, .4f, .1f);
        p.setCenter( .3f, .1f, .1f);
        p.setHessian( .5f);
        objects.add( p);

        System.out.println( "Object " + objects);
        System.out.println("1 ----------------------------------------------");

        // the SceneRecognition needs a SpatialSimplifier object
        // to semplify the characteristics of the relations during learning
        SpatialSimplifier simplifier = new SpatialSimplifier( objects);
        // create scene and reason for recognition
        SceneRepresentation recognition1 = new SceneRepresentation( simplifier, ontoRef);

        /*
        // if you want the relation to be human friendly again
        simplifier.populateHumanFriendlyRelationSet();
        // and eventually
        simplifier.readObjectSemantics( true);
        objects = simplifier.getObjects();
        */

        System.out.println( "Recognised with best confidence: " + recognition1.getRecognitionConfidence() + " should learn? " + recognition1.shouldLearn());
        System.out.println( "Best recognised class: " + recognition1.getBestRecognitionDescriptor());
        System.out.println( "Other recognised classes: " + recognition1.getSceneDescriptor().getTypeIndividual());

        // learn the new scene if is the case
        if ( recognition1.shouldLearn()) {
            System.out.println("Learning.... ");
            recognition1.learn("TestScene");
        }

        System.out.println("2 ----------------------------------------------");

        // check recognition after learning
        System.out.println( "Recognised with best confidence: " + recognition1.getRecognitionConfidence() + " should learn? " + recognition1.shouldLearn());
        System.out.println( "Best recognised class: " + recognition1.getBestRecognitionDescriptor());
        System.out.println( "Other recognised classes: " + recognition1.getSceneDescriptor().getTypeIndividual());

        System.out.println("3 ----------------------------------------------");
        System.out.println("3 ----------------------------------------------");
        System.out.println("3 ----------------------------------------------");

        // clean ontology
        ontoRef.removeIndividual( recognition1.getSceneDescriptor().getInstance());
        for ( GeometricPrimitive i : objects)
            ontoRef.removeIndividual( i.getInstance());
        ontoRef.synchronizeReasoner();

        // augment the scene
        Cone c = new Cone( ontoRef);
        c.shouldAddTime( true);
        c.setCenter( .3f, .3f, .3f);
        c.setAxis( .0f, .1f, .0f);
        c.setApex( .2f, .3f, .4f);
        c.setRadius( .1f);
        c.setHeight( .05f);
        objects.add( c);

        System.out.println( "Object " + objects);
        System.out.println("4 ----------------------------------------------");

        // check recognition and learn if is the case
        SpatialSimplifier simplifier2 = new SpatialSimplifier( objects);
        SceneRepresentation recognition2 = new SceneRepresentation( simplifier2, ontoRef);
        System.out.println( "Recognised with best confidence: " + recognition2.getRecognitionConfidence() + " should learn? " + recognition2.shouldLearn());
        if ( recognition2.shouldLearn()) {
            System.out.println("Learning.... ");
            recognition2.learn("TestScene2");
        }

        System.out.println( "Recognised with best confidence: " + recognition2.getRecognitionConfidence() + " should learn? " + recognition2.shouldLearn());
        System.out.println( "Best recognised class: " + recognition2.getBestRecognitionDescriptor());
        System.out.println( "Other recognised classes: " + recognition2.getSceneDescriptor().getTypeIndividual());

        System.out.println("5 ----------------------------------------------");

        Set<SceneClassDescriptor> recognitionClasses = recognition2.getSceneDescriptor().buildTypeIndividual();
        for ( SceneClassDescriptor cl1 : recognitionClasses)
            for ( SceneClassDescriptor cl2 : recognitionClasses)
                if ( ! cl1.equals( cl2))
                    System.out.println( " is " + cl1.getInstance().getIRI().getRemainder().get() +
                            " subclass of " + cl2.getInstance().getIRI().getRemainder().get() +"? " + cl1.getSubConcept().contains( cl2.getInstance()));


        System.out.println("6 ----------------------------------------------");
    }
}
