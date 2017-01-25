package it.emarolab.scene_identification_tracking.semanticSceneLibrary.example;

import it.emarolab.amor.owlDebugger.Logger;
import it.emarolab.amor.owlInterface.OWLReferencesInterface;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.objects.aMOR.MORCone;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.objects.aMOR.MORCylinder;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.objects.aMOR.MORPrimitive;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.objects.aMOR.MORSphere;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.tracker.ShapeTracker;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.tracker.Tracker;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

// todo extend example and debugging
/**
 * An example of shape #Tracker API usage
 * <p>
 *     It allow to easy simulate noises perceptions and appreciate the work of the
 *     {@link ShapeTracker}.
 *
 * <div style="text-align:center;"><small>
 * <b>File</b>:       it.emarolab.scene_identification_tracking.semanticSceneLibrary.Semantics <br>
 * <b>Licence</b>:    GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
 * <b>Author</b>:     Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
 * <b>affiliation</b>: DIBRIS, EMAROLab, University of Genoa. <br>
 * <b>date</b>:       06/01/2017 <br>
 * </small></div>
 *
 * @see Tracker
 */
public class ShapeTrackerTest {

    /**
     * Run a shape tracking example. It initialise the tracker with
     * {@link #getInitialPrimitive()} and iterate for several tracking
     * scan by obtaining ne simulated perception with {@link #getOnlinePrimitive(double)}.
     * @param args not used.
     */
    public static void main(String[] args) {
        //Semantics.Base.showDebugs( true);

        ShapeTracker tracker = new ShapeTracker();
        tracker.update( getInitialPrimitive());
        System.out.println( "initial scan \n" + tracker + "\n\n");
        tracker.clean();

        int  i = 1;
        for(  ;i <= 50; i++) {
            Collection<Tracker.UpdatingState> states = tracker.updateScan(getOnlinePrimitive(0.10));
            System.out.println( i + "-th Scan:");
            for(Tracker.UpdatingState s : states)
                System.out.println( "\t\t\t" + s + "\n");
        }
        System.out.println( "\n########################################################################################### ");
        System.out.println( "######################### 1 " + tracker);
        System.out.println( "########################################################################################### \n");

        for( int  j = 1; j <= 100; j++) {
            Collection<Tracker.UpdatingState> states = tracker.updateScan(getOnlinePrimitive(0.90));
            System.out.println( i + "+" + j + "-th Scan:");
            for(Tracker.UpdatingState s : states)
                System.out.println( "\t\t\t" + s + "\n");
        }
        System.out.println( "\n########################################################################################### ");
        System.out.println( "######################### 2 " + tracker);
        System.out.println( "########################################################################################### \n");
    }

    /** @return a simulating perception of objects in a table.
     * In particular, a {@link MORSphere},
     * a {@link MORCylinder} and a {@link MORCone}.
     */
    private static Collection<MORPrimitive> getInitialPrimitive(){
        // initialise an ontology with the aMOR interface (used for Semantics.Descriptor)
        Logger.setPrintOnConsole( false); // remove aMOR log
        OWLReferencesInterface.OWLReferencesContainer.newOWLReferenceFromFileWithPellet(
                ObjectHierarchyTest.ONTO_NAME,
                ObjectHierarchyTest.ONTO_FILE_LOADING_PATH,
                ObjectHierarchyTest.ONTO_IRI_PATH,
                ObjectHierarchyTest.ONTO_BUFFERISE_CHANGES);
        // create some initial dummy shape to be tracked
        Set<MORPrimitive> obects = new HashSet<>();
        obects.add( new MORSphere( ObjectHierarchyTest.ONTO_NAME, 1));
        obects.add( new MORCone( ObjectHierarchyTest.ONTO_NAME, 2));
        obects.add( new MORCylinder( ObjectHierarchyTest.ONTO_NAME, 3));
        return obects;
    }

    /**
     * returns with a given probability a simulated perception with
     * the same objects as {@link #getInitialPrimitive()}. Otherwise
     * returns a perception where the cone is consider as a cylinder
     * and viceversa (tracking Item ID swap).
     * @param prop the probability to obtain the same perception as {@link #getInitialPrimitive()}
     * @return a new simulated perceptions to be update in the tracker.
     */
    private static Collection<MORPrimitive> getOnlinePrimitive( double prop){
        Set< MORPrimitive> obects = new HashSet<>();
        if( getRandom() < prop) {
            System.out.println( "\t\t\t\t++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            obects.add(new MORCone(ObjectHierarchyTest.ONTO_NAME, 2));
            obects.add(new MORCylinder(ObjectHierarchyTest.ONTO_NAME, 3));
        } else {
            System.out.println( "\t\t\t\t**********************************************************************");
            obects.add(new MORCone(ObjectHierarchyTest.ONTO_NAME, 3));
            obects.add(new MORCylinder(ObjectHierarchyTest.ONTO_NAME, 2));
        }
        return obects;
    }

    /** @return a random number between [0,1].*/
    private static double getRandom(){ // between 0 and 1
        return Math.random();
    }
}
