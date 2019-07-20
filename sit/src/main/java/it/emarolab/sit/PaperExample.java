package it.emarolab.sit;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.amor.owlInterface.OWLReferencesInterface;
import it.emarolab.sit.realObject.*;
import it.emarolab.sit.sceneRepresentation.FullSceneRepresentation;

import java.util.HashSet;
import java.util.Set;

// duplicated of Test
public class PaperExample
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

        // SCENE 1
        Cylinder r = new Cylinder( ontoRef);
        r.setCenter( 1.49f, .14f, -.22f);
        r.setAxis( .15f, -.02f, -.19f);
        r.setApex( 1.49f, .14f, .19f); // it actually sets a generic point on the axis given by RANSAC computed with PCL
        r.setRadius( .03f);
        r.setHeight( .19f);
        objects.add( r);

        Plane p = new Plane( ontoRef);
        p.setAxis( .37f, -.59f, -.01f);
        p.setCenter( 1.37f, -.35f, -.15f);
        p.setHessian( -1.02f);
        objects.add( p);

        // augment the scene
        Cone c = new Cone( ontoRef);
        c.setCenter( 1.71f, -.09f, -.23f);
        c.setAxis( -.01f, -.01f, -1.40f);
        c.setApex( 1.71f, -.08f, -.13f);
        c.setRadius( .26f);
        c.setHeight( .14f);
        objects.add( c);

        // todo adjust threshold and make other scenes

        SpatialSimplifier simplifier = new SpatialSimplifier( objects);
        FullSceneRepresentation recognition1 = new FullSceneRepresentation( simplifier, ontoRef);


        // learn the new scene if is the case
        if ( recognition1.shouldLearn()) {
            System.out.println("Learning.... ");
            recognition1.learn("Scene");
        }

        System.out.println(" ----------------------------------------------");

        // check recognition after learning
        System.out.println( "Recognised with best confidence: " + recognition1.getRecognitionConfidence() + " should learn? " + recognition1.shouldLearn());
        System.out.println( "Best recognised class: " + recognition1.getBestRecognitionDescriptor());
        System.out.println( "Other recognised classes: " + recognition1.getSceneDescriptor().getIndividualTypes());

        System.out.println(" ----------------------------------------------");
        System.out.println( "Object " + objects);

        ontoRef.saveOntology("/home/bubx/aaa.owl");

        // clean ontology
        ontoRef.removeIndividual( recognition1.getSceneDescriptor().getInstance());
        for ( GeometricPrimitive i : objects)
            ontoRef.removeIndividual( i.getInstance());
        ontoRef.synchronizeReasoner();


/*

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

        // augment the scene
        Cone c = new Cone( ontoRef);
        c.shouldAddTime( true);
        c.setCenter( .3f, .3f, .3f);
        c.setAxis( .0f, .1f, .0f);
        c.setApex( .2f, .3f, .4f);
        c.setRadius( .1f);
        c.setHeight( .05f);
        objects.add( c);

        // augment the scene
        Cylinder c = new Cylinder( ontoRef);
        c.shouldAddTime( true);
        c.setCenter( .3f, .3f, .3f);
        c.setAxis( .0f, .1f, .0f);
        c.setApex( .2f, .3f, .4f); // it actually sets a generic point on the axis given by RANSAC computed with PCL
        c.setRadius( .1f);
        c.setHeight( .05f);
        objects.add( c);

        System.out.println( "Object " + objects);
        System.out.println("1 ----------------------------------------------");

        // the SceneRecognition needs a SpatialSimplifier object
        // to semplify the characteristics of the relations during learning
        SpatialSimplifier simplifier = new SpatialSimplifier( objects);
        // create scene and reason for recognition
        FullSceneRepresentation recognition1 = new FullSceneRepresentation( simplifier, ontoRef);
        //SceneRepresentation recognition1 = new SceneRepresentation( simplifier, ontoRef);


        // if you want the relation to be human friendly again
        //simplifier.populateHumanFriendlyRelationSet();
        // and eventually
        //simplifier.readObjectSemantics( true);
        //objects = simplifier.getObjects();


        System.out.println( "Recognised with best confidence: " + recognition1.getRecognitionConfidence() + " should learn? " + recognition1.shouldLearn());
        System.out.println( "Best recognised class: " + recognition1.getBestRecognitionDescriptor());
        System.out.println( "Other recognised classes: " + recognition1.getSceneDescriptor().getTypeIndividual());

        // learn the new scene if is the case
        if ( recognition1.shouldLearn()) {
            System.out.println("Learning.... ");
            recognition1.learn("Scene");
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
*/
        //Set<SceneClassDescriptor> recognitionClasses = recognition2.getSceneDescriptor().buildTypeIndividual();
        //for ( SceneClassDescriptor cl1 : recognitionClasses) {
        //    for (SceneClassDescriptor cl2 : recognitionClasses) {
        //        if (!cl1.equals(cl2)) {
        //           System.out.println(" is " + cl1.getInstance().getIRI().getRemainder().get() +
        //                    " subclass of " + cl2.getInstance().getIRI().getRemainder().get() + "? " + cl1.getSubConcept().contains(cl2.getInstance()));
        //            System.err.println(cl1);
        //        }
        //    }
        //}

        //ontoRef.saveOntology("/home/bubx/aaa.owl");

        System.out.println("6 ----------------------------------------------");
    }
}
