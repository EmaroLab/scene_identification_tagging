package it.emarolab.scene_identification_tracking.semanticSceneLibrary.tracker;

import it.emarolab.scene_identification_tracking.semanticSceneLibrary.objects.ObjectSemantics;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.objects.aMOR.MORPrimitive;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.aMORDescriptor.MORSpatialDescriptor;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import java.util.HashSet;
import java.util.Set;

/**
 * A tracking Item based {@link Tracker.ShapeItem} and aMOR semantic mapping library.
 * <p>
 *     This object describes an Object by a semantics based on {@link MORPrimitive},
 *     a shape based on ({@link Tracker.ShapeItem}) and an unique identifier
 *     based on {@link OWLNamedIndividual}.
 * <p>
 *     Moreover, it computes the probability of the object to being of a specific
 *     space and compute the probability to be {@link #update(MORPrimitive)} to a new description
 *     (perceiving noise effects). Indeed, the probability is compute in term of scores
 *     by the {@link #updateScore(MORPrimitive)} (see {@link #computeScore(double, double, long)})
 *     and than {@link #normaliseScores()} (see also {@link #normaliseCounters()}).
 *     This computation is base on counting own many times this object as be considered
 *     of a determinate shape and which is the probability that a perception
 *     was failing with respect to new scans (see: Tracker.SemanticObjectItem constants).
 *
 * <div style="text-align:center;"><small>
 * <b>File</b>:       it.emarolab.scene_identification_tracking.semanticSceneLibrary.Semantics <br>
 * <b>Licence</b>:    GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
 * <b>Author</b>:     Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
 * <b>affiliation</b>: DIBRIS, EMAROLab, University of Genoa. <br>
 * <b>date</b>:       06/01/2017 <br>
 * </small></div>
 *
 * @see SemanticItem
 * @see MORPrimitive
 * @see Tracker.Item
 * @see ShapeTracker
 */
public class SemanticItem extends Tracker.ShapeItem< MORPrimitive> {

    /**
     * The max value of the counters after {@link #normaliseCounters()}.
     */
    private static final Long MAX_NORMALISATION_COUNTER = 50l; // todo make them settable
    /**
     * The max value of the counters before than {@link #normaliseCounters()}.
     */
    private static final Long RESET_COUNTER_VALUE = 10000l;//Long.MAX_VALUE; // todo make them settable

    // internal field
    private int shapeCnt = 0;
    // private int shapeScore; // is always 1 due to normalisation
    // used for increase shapeCnt on update and decrease on finaliseUpdates
    private boolean updated = true;
    // used for compute the probable to be of a given shape
    private double primitiveScore = 0, sphereScore = 0, planeScore = 0, coneScore = 0, cylinderScore = 0;
    private long primitiveCnt = 0, sphereCnt = 0, planeCnt = 0, coneCnt = 0, cylinderCnt = 0;

    /**
     * Cloning constructor, create a new object as a clone of the input parameter.
     * @param tracked the object to clone.
     */
    public SemanticItem(SemanticItem tracked){
        super( tracked);
        this.setSemanticObject( (MORPrimitive) tracked.getSemanticObject().copy());
        this.setId( simulateId( tracked.getSemanticObject()));
        this.shapeCnt = tracked.shapeCnt;
        this.updated = tracked.updated;
        this.sphereCnt = tracked.sphereCnt;
        this.planeCnt = tracked.planeCnt;
        this.coneCnt = tracked.coneCnt;
        this.cylinderCnt = tracked. cylinderCnt;
        this.primitiveCnt = tracked.primitiveCnt;
        this.sphereScore = tracked.sphereScore;
        this.planeScore = tracked.planeScore;
        this.coneScore = tracked.coneScore;
        this.cylinderScore = tracked.cylinderScore;
        this.primitiveScore = tracked.primitiveScore;
    }

    /**
     * Initialise this tracking item with a primitive object.
     * @param object the primitive object to track.
     */
    public SemanticItem(MORPrimitive object) {
        super( object);
    }

    /**
     * @see Tracker.ShapeItem#getId()
     * @param object a new belief of the shape of the tracked item.
     * @return the value obtained by: {@link #simulateId(MORPrimitive)} by using the input parameter.
     */
    @Override
    public OWLNamedIndividual getId( MORPrimitive object) {
        return simulateId( object);
    }
    /**
     * @param object the object from which simulate the id as it was an {@link SemanticItem}
     * @return the stand way to assign an unique identifier to a tracked object from a semantic
     * primitive object. It is based on {@link MORSpatialDescriptor.MORSimpleDescriptor#getInstance()}
     */
    public static OWLNamedIndividual simulateId( MORPrimitive object){
        return object.getSemantics().getInstance();
    }

    /**
     * <p>
     *     This method checks:
     *     <ul>
     *        <li> if <code>this.equals( object)</code> and return an
     *             {@link Tracker.UpdatingState#ERROR} if this is not <code>true</code>.
     *        <li> updates this belied ({@link #getShapeCnt()} and {@link #isUpdated()})
     *        <li> than, check if {@link #updateScore(MORPrimitive)} is <code>true</code>.
     *        <li> if yes
     *        <ul>
     *            <li> replace ths semantic object with the input parameter.
     *            <li> return a {@link Tracker.UpdatingState#UPDATED} outcome.
     *        </ul>
     *        <li> if {@link #shapeAs(ObjectSemantics.Primitive)} is true
     *        <ul>
     *            <li> makes an {@link ObjectSemantics.Primitive#average(ObjectSemantics.Primitive)}
     *                 between ths semantic object with the input parameter.
     *            <li> return a {@link Tracker.UpdatingState#AVERAGED} outcome.
     *        </ul>
     *        <li> if false, return a {@link Tracker.UpdatingState#NOT_CHANGED} outcome.
     *        <li> it {@link #normaliseScores()} always before to return.
     *     </ul>
     * @param object the new belief describing a semantic object about this tracking Item.
     * @return the updating state changes of the tacked item of updating it with the input
     * belief.
     */
    @Override
    public Tracker.UpdatingState update(MORPrimitive object){
        if( equals( object)) {
            shapeCnt += 1;
            updated = true;

            if ( updateScore(object)) {
                Tracker.Item<Integer, OWLNamedIndividual, MORPrimitive> thisCopy = copy();
                // introduce new object and replace the old
                initialise(object);
                normaliseScores();
                // track updating state
                return new Tracker.UpdatingState().asUpdated( thisCopy, this.copy());
            }
            if ( shapeAs( object)) {
                Tracker.Item<Integer, OWLNamedIndividual, MORPrimitive> thisCopy = this.copy();
                // merge the two shapes
                getSemanticObject().average(object);
                normaliseScores();
                return new Tracker.UpdatingState().asAveraged( thisCopy, this.copy());
            }
            return new Tracker.UpdatingState().asNotChanged( this.copy());
        }
        return new Tracker.UpdatingState().asError( "You can update object with different id during tracking !!!!" +
                "\n " + this.getId() + " == " + simulateId( object) + "? " + equals( object));
    }
    /**
     * <p>
     *     For each shapes <em>S</em>, defined by {@link SemanticItem} it:
     *     <ul>
     *        <li> update the relate score to be in the current belief through:<br>
     *             <code> <em>S</em>Score = {@link #computeScore(double, double, long)}( <em>S</em>Score,
     *                    <em>S</em>2<em>S</em>_PROBABILITY, ++<em>S</em>Cnt);</code>
     *        <li> than it check for all possible shapes of input objects <em>E</em>:
     *        <ul>
     *            <li> update the relate score to be in to check if it is bettwe to move to the new belief through:<br>
     *                 <code> <em>E</em>Score = {@link #computeScore(double, double, long)}( <em>E</em>Score,
     *                        <em>S</em>2<em>E</em>_PROBABILITY, ++<em>E</em>Cnt);</code>
     *            <li> return <code><em>E</em>Score &gt;? <em>S</em>Score</code>
     *        </ul>
     *     </ul>
     *
     * @param object the new belief describing a semantic object about this tracking Item.
     * @return true if the new belief seems to be more correct than the actual.
     */
    protected boolean updateScore( MORPrimitive object) {
        // return if you should and update internal scores
        // all i,j combination where i:this.shape and j:object shape where i,j€{sphere, plane, cone, cylinder}
        if (isSphere()) {
            sphereScore = computeScore( sphereScore, SPHERE2SPHERE_PROBABILITY, ++sphereCnt);
            if (object instanceof ObjectSemantics.Sphere) {
                return false; // never update when equal types
            } if (object instanceof ObjectSemantics.Plane) {
                planeScore = computeScore( planeScore, SPHERE2PLANE_PROBABILITY, ++planeCnt);
                return planeScore > sphereScore;
            } if (object instanceof ObjectSemantics.Cone) {
                coneScore = computeScore( coneScore, SPHERE2CONE_PROBABILITY, ++coneCnt);
                return coneScore > sphereScore;
            } if (object instanceof ObjectSemantics.Cylinder) {
                cylinderScore = computeScore( cylinderScore, SPHERE2CYLINDER_PROBABILITY, ++cylinderCnt);
                return  cylinderScore > sphereScore;
            }
            primitiveScore = computeScore(primitiveScore, SPHERE2PRIMITIVE_PROBABILITY, ++primitiveCnt);
            return primitiveScore > sphereScore;
        }
        if (isPlane()) {
            planeScore = computeScore( planeScore, PLANE2PLANE_PROBABILITY, ++planeCnt);
            if (object instanceof ObjectSemantics.Sphere) {
                sphereScore = computeScore( sphereScore, PLANE2SPHERE_PROBABILITY, ++sphereCnt);
                return  sphereScore > planeScore;
            } if (object instanceof ObjectSemantics.Plane) {
                return false; // never update when equal types
            } if (object instanceof ObjectSemantics.Cone) {
                coneScore = computeScore( coneScore, PLANE2CONE_PROBABILITY, ++coneCnt);
                return coneScore > planeScore;
            } if (object instanceof ObjectSemantics.Cylinder) {
                cylinderScore = computeScore( cylinderScore, PLANE2CYLINDER_PROBABILITY, ++cylinderCnt);
                return  cylinderScore > planeScore;
            }
            primitiveScore = computeScore(primitiveScore, PLANE2PRIMITIVE_PROBABILITY, ++primitiveCnt);
            return primitiveScore > planeScore;
        }
        if (isCone()) {
            coneScore = computeScore( coneScore, CONE2CONE_PROBABILITY, ++coneCnt);
            if (object instanceof ObjectSemantics.Sphere) {
                sphereScore = computeScore( sphereScore, CONE2SPHERE_PROBABILITY, ++sphereCnt);
                return  sphereScore > coneScore;
            } if (object instanceof ObjectSemantics.Plane) {
                planeScore = computeScore( planeScore, CONE2PLANE_PROBABILITY, ++planeCnt);
                return  planeScore > coneScore;
            } if (object instanceof ObjectSemantics.Cone) {
                return false; // never update when equal types
            } if (object instanceof ObjectSemantics.Cylinder) {
                cylinderScore = computeScore( cylinderScore, CONE2CYLINDER_PROBABILITY, ++cylinderCnt);
                return  cylinderScore > coneScore;
            }
            primitiveScore = computeScore(primitiveScore, CONE2PRIMITIVE_PROBABILITY, ++primitiveCnt);
            return primitiveScore > coneScore;
        }
        if (isCylinder()) {
            cylinderScore = computeScore( cylinderScore, CYLINDER2CYLINDER_PROBABILITY, ++cylinderCnt);
            if (object instanceof ObjectSemantics.Sphere) {
                sphereScore = computeScore( sphereScore, CYLINDER2SPHERE_PROBABILITY, ++sphereCnt);
                return sphereScore  > cylinderScore;
            } if (object instanceof ObjectSemantics.Plane) {
                planeScore = computeScore( planeScore, CYLINDER2PLANE_PROBABILITY, ++planeCnt);
                return planeScore > cylinderScore;
            } if (object instanceof ObjectSemantics.Cone) {
                coneScore = computeScore( coneScore, CYLINDER2CONE_PROBABILITY, ++coneCnt);
                return coneScore > cylinderScore;
            } if (object instanceof ObjectSemantics.Cylinder) {
                return false; // never update when equal types
            }
            primitiveScore = computeScore(primitiveScore, CYLINDER2PRIMITIVE_PROBABILITY, ++primitiveCnt);
            return primitiveScore > cylinderScore;
        }
        return true; // always update from unknown shape (not used UNKNOWN2...._PROBABILITY)
    }
    /**
     * @param score the actual score of this object to be of a new shape
     * @param transitionProbability the probability to corecclty change from this shape to the new belief.
     * @param seenCount how many time this Object has been classified with the new belief previously
     *                  (see: normaliseCounters)
     * @return <code>return score + transitionProbability * seenCount;</code>.<br>
     * This value should be overwritten to the given score.
     */
    protected double computeScore( double score, double transitionProbability, long seenCount){
        return score + transitionProbability * seenCount; // todo da ricontrollare!!!!!
    }

    /**
     * Normalise all the scores between [0,1] by using:
     * <code> x_norm = (x - min) / (max - min)</code>.
     */
    protected void normaliseScores(){
        double max = getMaxScore();
        double min = getMinScore();

        sphereScore = normaliseScore(sphereScore, max, min);
        planeScore = normaliseScore(planeScore, max, min);
        coneScore = normaliseScore(coneScore, max, min);
        cylinderScore = normaliseScore(cylinderScore, max, min);
        primitiveScore = normaliseScore(primitiveScore, max, min);
    }
    /**
     * Normalise all the scores between [0,{@link #MAX_NORMALISATION_COUNTER}] by using:
     * <code> x_norm = (x - min) / (max - min)</code>.
     * This is trigger when a counter reaches the {@link #RESET_COUNTER_VALUE}, it is
     * used only for avoiding stack overflow errors due to the continuously increasing of the counters.
     */
    protected void normaliseCounters(){
        long max = MAX_NORMALISATION_COUNTER;
        long min = getMinCounter();

        sphereCnt = (long) normaliseScore( sphereCnt, max, min);
        planeCnt = (long) normaliseScore( planeCnt, max, min);
        coneCnt = (long) normaliseScore( coneCnt, max, min);
        cylinderCnt = (long) normaliseScore( cylinderCnt, max, min);
        primitiveCnt =  (long) normaliseScore(primitiveCnt, max, min);
    }
    private double normaliseScore( double score, double max, double min){
        return ( score - min) / ( max - min);
    }

    /** @return the minimum score between all th possible shapes. */
    protected double getMinScore(){
        double min = Double.MAX_VALUE;
        for( Double s : getScores())
            if( s < min)
                min = s;
        return min;
    }
    /** @return the minimum count between all th possible shapes. */
    protected long getMinCounter(){
        long min = Long.MAX_VALUE;
        for( Long s : getCounters())
            if( s < min)
                min = s;
        return min;
    }
    /** @return the maximum score between all th possible shapes. */
    protected double getMaxScore(){
        double max = Double.MIN_VALUE;
        for( Double s : getScores())
            if( s > max)
                max = s;
        return max;
    }
    private Set< Double> getScores(){
        Set< Double> out = new HashSet<>();
        out.add( sphereScore);
        out.add( planeScore);
        out.add( coneScore);
        out.add( cylinderScore);
        out.add(primitiveScore);
        return out;
    }
    private Set< Long> getCounters(){
        Set< Long> out = new HashSet<>();
        out.add( sphereCnt);
        out.add( planeCnt);
        out.add( coneCnt);
        out.add( cylinderCnt);
        return out;
    }

    /**@return the probability to being a sphere (see: {@link Tracker.ShapeItem}). */
    public double getSphereScore() {
        return sphereScore;
    }
    /**@return the probability to being a plane. see: {@link Tracker.ShapeItem}).*/
    public double getPlaneScore() {
        return planeScore;
    }
    /**@return the probability to being a cone. see: {@link Tracker.ShapeItem}).*/
    public double getConeScore() {
        return coneScore;
    }
    /**@return the probability to being a cylinder. see: {@link Tracker.ShapeItem}).*/
    public double getCylinderScore() {
        return cylinderScore;
    }
    /**@return the probability to being a primitive. see: {@link Tracker.ShapeItem}).*/
    public double getPrimitiveScore() {
        return primitiveScore;
    }

    /**
     * @return the number of thime that this item (with this semantic) object have been seen. It can be negative
     * if not updated before than call {@link #finaliseUpdates()}.
     */
    public int getShapeCnt() {
        return shapeCnt;
    }

    /**@return the number of thime that this Item has been seen as a primitive object. (see: {@link #normaliseCounters()})*/
    public long getPrimitiveCnt() {
        return primitiveCnt;
    }
    /**@return the number of thime that this Item has been seen as a sphere (see: {@link #normaliseCounters()})*/
    public long getSphereCnt() {
        return sphereCnt;
    }
    /**@return the number of thime that this Item has been seen as a plane object. (see: {@link #normaliseCounters()})*/
    public long getPlaneCnt() {
        return planeCnt;
    }
    /**@return the number of thime that this Item has been seen as a cone. (see: {@link #normaliseCounters()})*/
    public long getConeCnt() {
        return coneCnt;
    }
    /**@return the number of thime that this Item has been seen as a cylinder. (see: {@link #normaliseCounters()})*/
    public long getCylinderCnt() {
        return cylinderCnt;
    }

    /** @return <code>true</code> if this item has been {@link #update(MORPrimitive)} before to {@link #finaliseUpdates()}. */
    public boolean isUpdated() {
        return updated;
    }
    /**
     * This method should be called from a tracking set interface (see: {@link Tracker})
     * in order to store if this item has not been updated in the last scan.
     * It effects {@link #getShapeCnt()} by decreasing the counter and triggers
     * {@link #normaliseCounters()} is a counter is grather than or equal {@link #RESET_COUNTER_VALUE}
     */
    public void finaliseUpdates(){
        if( ! updated)
            shapeCnt -= 1;
        else updated = false;

        if( sphereCnt >= RESET_COUNTER_VALUE || planeCnt >= RESET_COUNTER_VALUE
                || coneCnt >= RESET_COUNTER_VALUE || cylinderCnt >= RESET_COUNTER_VALUE)
            normaliseCounters();
    }

    /**
     * This methods assure that objects are collected in a set by id. It
     * returns <code>true</code> if:
     * <ul>
     *     <li> the two Items have the same unique identifier.
     *     <li> this Item have an ID equal to the {@link #simulateId(MORPrimitive)} of the given object.
     *     <li> this Item have an ID equal to the given value.
     * </ul>
     * <br>
     * <b>REMARK</b>: the equality is not symmetric.
     * @param o the object to check for equality with respect to <code>this</code>.
     * @return true if the given object and <code>this</code> are refering to the same {@link #getId()}.
     * @see it.emarolab.scene_identification_tracking.semanticSceneLibrary.tracker.Tracker.Item#equals(Object)
     */
    @Override
    public boolean equals(Object o) {
        if( o == null)
            return false;
        if( super.equals( o))
            return true;

        if( o instanceof MORPrimitive)
            if( super.equals( simulateId( (MORPrimitive) o))) // iterative call
                return true;

        return false;
    }

    @Override
    public Tracker.Item<Integer, OWLNamedIndividual, MORPrimitive> copy() {
        return new SemanticItem( this);
    }

    @Override
    public String toString() {
        return  super.toString() + "(cnt:"+ shapeCnt + ", updated:" + updated + ")"
                + "\tscores=" + "{unknown:" + primitiveScore + ", sphere:" + sphereScore + ", plane:" + planeScore
                + ", cone:" + coneScore + ", cylinder:" + cylinderScore + "}"
                + "\tcnts=" +  "{unknown:" + primitiveCnt + ", sphere:" + sphereCnt + ", plane:" + planeCnt
                + ", cone:" + coneCnt + ", cylinder:" + cylinderCnt + "}";
    }
}
