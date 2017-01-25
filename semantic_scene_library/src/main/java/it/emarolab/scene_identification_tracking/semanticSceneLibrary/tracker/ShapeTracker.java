package it.emarolab.scene_identification_tracking.semanticSceneLibrary.tracker;

import it.emarolab.scene_identification_tracking.semanticSceneLibrary.Logger.SITBase;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.objects.ObjectSemantics;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.objects.aMOR.MORPrimitive;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.aMORDescriptor.MORSpatialDescriptor;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * A {@link Tracker} implementation for {@link MORPrimitive} semantic objects
 * and {@link SemanticItem} Item description.
 * <p>
 *     It implements a tracker that can track the shape of a semantic object,
 *     described as {@link MORPrimitive} into an {@link SemanticItem} item.
 * <p>
 *     Indeed, it is just a <b>tracking set</b> manger of {@link ObjectSemantics.Primitive}
 *     objects, semantically mapped in OWL through aMOR library
 *     (see: {@link MORSpatialDescriptor.MORSimpleDescriptor}).
 *     This class set the {@link Tracker.Item} unique identifier to be the OWL individual
 *     describing semantically the object, thus <code>'I'</code> type parameters is replaced with
 *     {@link OWLNamedIndividual}.
 * <p>
 *     Most of the tracking implementations is available in the specific {@link Tracker.Item } description,
 *     this class is limited in managing the set of object an {@link Tracker.UpdatingState}.
 *     Nevertheless, it is also in charge to manage the {@link #clean()} procedure for removing
 *     objects not perceived anymore if they are not update for a specific number of scans
 *     (see: {@link #setRemovingCntThreshold(int)}).
 *
 * <div style="text-align:center;"><small>
 * <b>File</b>:       it.emarolab.scene_identification_tracking.semanticSceneLibrary.Semantics <br>
 * <b>Licence</b>:    GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
 * <b>Author</b>:     Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
 * <b>affiliation</b>: DIBRIS, EMAROLab, University of Genoa. <br>
 * <b>date</b>:       06/01/2017 <br>
 * </small></div>
 *
 *  @see MORPrimitive
 *  @see SemanticItem
 *  @see Tracker
 */
public class ShapeTracker extends SITBase.SITBase
        implements Tracker< MORPrimitive, SemanticItem> {

    /**
     * The default number of not update scans that triggers a
     * {@link UpdatingState#REMOVED} actions from the tracked set.
     */
    private static final int REMOVING_COUNT_THRESHOLD = 3;

    /** the tracked set */
    private Set<SemanticItem> trackedSemanticItems = new HashSet<>();
    private int removingCnt; // it is negative (internal state for external setting)

    /**
     * Construct this object with the default REMOVING_COUNT_THRESHOLD.
     */
    public ShapeTracker(){
        setRemovingCntThreshold( REMOVING_COUNT_THRESHOLD);
    }
    /**
     * Construct this object with the given removing count threshold.
     * @param removingCountThreshold the value to set as {@link #setRemovingCntThreshold(int)}.
     */
    public ShapeTracker( int removingCountThreshold){
        setRemovingCntThreshold( removingCountThreshold);
    }
    /**
     * Construct this object with the default REMOVING_COUNT_THRESHOLD
     * and {@link #add(MORPrimitive)} a set of tracking items.
     * @param object a set of semantic objects to add to this tracking set.
     */
    public ShapeTracker( MORPrimitive object){
        setRemovingCntThreshold( REMOVING_COUNT_THRESHOLD);
        add( object);
    }
    /**
     * Construct this object with the default REMOVING_COUNT_THRESHOLD
     * and {@link #add(Item)} a set of tracking items.
     * @param object a set of semantic objects to add to this tracking set.
     */
    public ShapeTracker( SemanticItem object){
        setRemovingCntThreshold( REMOVING_COUNT_THRESHOLD);
        add( object);
    }
    /**
     * Construct this object with the default REMOVING_COUNT_THRESHOLD
     * and {@link #add(Collection)} a set of tracking items.
     * @param objects a set of semantic objects to add to this tracking set.
     */
    public ShapeTracker( Collection<?> objects){
        setRemovingCntThreshold( REMOVING_COUNT_THRESHOLD);
        add( objects);
    }
    /**
     * Construct this object with the given removing count threshold.
     * and {@link #add(Object)} a tracking item.
     * @param object the semantic object to add to this tracking set.
     * @param removingCountThreshold the number of time of not updating scan for removing a tracking item.
     */
    public ShapeTracker( int removingCountThreshold, MORPrimitive object){
        setRemovingCntThreshold( removingCountThreshold);
        add( object);
    }
    /**
     * Construct this object with the given removing count threshold.
     * and {@link #add(Item)} a tracking item.
     * @param object the semantic object to add to this tracking set.
     * @param removingCountThreshold the number of time of not updating scan for removing a tracking item.
     */
    public ShapeTracker( int removingCountThreshold, SemanticItem object){
        setRemovingCntThreshold( removingCountThreshold);
        add( object);
    }
    /**
     * Construct this object with the given removing count threshold.
     * and {@link #add(Collection)} a set of tracking items.
     * @param objects a set of semantic objects to add to this tracking set.
     * @param removingCountThreshold the number of time of not updating scan for removing a tracking item.
     */
    public ShapeTracker( int removingCountThreshold, Collection<?> objects){
        setRemovingCntThreshold( removingCountThreshold);
        add( objects);
    }

    /**
     * @param removingCnt The number of not update scans that triggers a
     * {@link UpdatingState#REMOVED} actions from the tracked set.
     *  This value must be positive.
     */
    public void setRemovingCntThreshold(int removingCnt) {
        this.removingCnt = -1 * removingCnt;
        if( removingCnt > 0)
            logWarning( "Removing counter threshould from shape tracking should be negative!" +
                    "\n " + removingCnt + " set, nothing will be never deleted.");
    }

    /**
     * Basic contains interface by {@link Item#getId()}
     * (see {@link it.emarolab.scene_identification_tracking.semanticSceneLibrary.tracker.Tracker.Item#equals(Object)})
     * @param id the unique identifier of the object to check if already tracked.
     * @return <code>true</code> if the given id is contained in the tracking set.
     */
    public boolean contains( OWLNamedIndividual id){
        return trackedSemanticItems.contains( id);
    }
    /**
     * Basic contains interface by {@link Item#getId()}
     * (see {@link it.emarolab.scene_identification_tracking.semanticSceneLibrary.tracker.Tracker.Item#equals(Object)})
     * @param id the unique identifier of the object to remove.
     * @return <code>true</code> if the given id has been removed from the tracking set.
     */
    public boolean remove( OWLNamedIndividual id){
        return trackedSemanticItems.remove( id);
    }
    /**
     * Basic contains interface by {@link Item#getId()}
     * (see {@link it.emarolab.scene_identification_tracking.semanticSceneLibrary.tracker.Tracker.Item#equals(Object)})
     * @param id the unique identifier of the object to get.
     * @return the {@link MORPrimitive} object with the given identifier.
     */
    public MORPrimitive get(OWLNamedIndividual id){
        SemanticItem out = getTacked(id);
        if( out != null)
            return out.getSemanticObject();
        return null;
    }
    /**
     * Basic contains interface by {@link Item#getId()}
     * (see {@link it.emarolab.scene_identification_tracking.semanticSceneLibrary.tracker.Tracker.Item#equals(Object)})
     * @param id the unique identifier of the object to get.
     * @return the tracked {@link Tracker.Item} object with the given identifier.
     */
    public SemanticItem getTacked(OWLNamedIndividual id){
        for( SemanticItem t : trackedSemanticItems)
            if( t.getId().equals( id))
                return t;
        return null;
    }

    @Override
    public boolean contains(MORPrimitive object) {
        for( SemanticItem t : trackedSemanticItems)
            if( t.getId().equals( object.getSemantics().getInstance()))
                return true;
        return false;
    }
    /**
     * This methods adds a new {@link SemanticItem#SemanticItem(MORPrimitive)}
     * item in the tracking shape (see: {@link Tracker#add(Object)}).
     * @param object the primitive object to add or update.
     * @return returns <code>{@link Tracker.UpdatingState#asAdded(Item)}</code> with a copy of the actual Item.
     */
    @Override
    public UpdatingState add(MORPrimitive object) {
        SemanticItem tracked = new SemanticItem(object);
        trackedSemanticItems.add( tracked);
        return new UpdatingState().asAdded( tracked.copy());
    }
    /**
     * This methods remove a new {@link SemanticItem#SemanticItem(MORPrimitive)}
     * item in the tracking shape (see: {@link Tracker#remove(Object)}).
     * @param object the primitive object to remove.
     * @return returns <code>{@link Tracker.UpdatingState#asRemoved(Item)}</code> with a copy of the actual Item.
     */
    @Override
    public UpdatingState remove(MORPrimitive object) {
        for( SemanticItem t : trackedSemanticItems)
            if( t.getId().equals( object.getSemantics().getInstance())) {
                trackedSemanticItems.remove(t);
                return new UpdatingState().asRemoved( t.copy());
            }
        return new UpdatingState().asNotChanged( new SemanticItem( object));
    }

    /**
     * @see #update(SemanticItem)
     * @param object the primitive object ot add/update in the tracking set.
     * @return the updating changes to the tracked set by considering the new given belief.
     */
    @Override
    public UpdatingState update(MORPrimitive object) {
        SemanticItem tracked = new SemanticItem( object);
        return update( tracked);
    }
    /**
     * The basic implementation method for update an item in the tracking set.<br>
     * If the tracking set does not contains the new belief it calls {@link #add(MORPrimitive)}.
     * Otherwise, for each elements in the tracked set it searches for the object with the same
     * ID and calls {@link SemanticItem#update(Object)}.
     * @param object the new belief of an object to track with respect to the {@link #getTrackedPrimitives()}
     * @return the updating changes to the tracked set by considering the new given belief.
     */
    @Override
    public UpdatingState update(SemanticItem object) {
        if( ! contains( object)) // todo: could be fuster if contsins retuns the item?
            return add( object);
        else for( SemanticItem tracked : trackedSemanticItems)
            if ( tracked.equals( object))
                return tracked.update(object);
        return new UpdatingState().asError( "Neither added nor updated!!!!!");
    }

    /**
     * It search any Items in the tracking set that have a counter less than <code>-1 * {@link #setRemovingCntThreshold(int)}</code>.
     * Since {@link SemanticItem} keep track of the times of scan than a current
     * belief has been undated, if it goes less than 0 represents that the object is not seen anymore.
     * @return the updating changes performed while cleaning the managed tracking set.
     */
    @Override
    public Collection<UpdatingState> clean() {
        Set<SemanticItem> toRemove = new HashSet<>();
        for( SemanticItem tracked : trackedSemanticItems){
            tracked.finaliseUpdates();
            if( tracked.getShapeCnt() < removingCnt)
                toRemove.add( tracked);
        }
        Collection<UpdatingState> out = new HashSet<>();
        for( SemanticItem t : toRemove)
            out.add( remove( t));
        return out;
    }

    @Override
    public Collection<? extends MORPrimitive> getTrackedPrimitives() {
        Collection< MORPrimitive> out = new HashSet<>();
        for( SemanticItem t : trackedSemanticItems)
            out.add( t.getSemanticObject());
        return out;
    }
    @Override
    public Collection<? extends SemanticItem> getTrackedSet() {
        return trackedSemanticItems;
    }

    @Override
    public String toString(){
        String out = "Tracker: {";
        int cnt = 0;
        for( SemanticItem tracked : trackedSemanticItems) {
            if( cnt == 0)
                out += "\t" + tracked;
            else out += "\t\t\t" + tracked;
            if(  ++cnt < trackedSemanticItems.size())
                out += ";\n";
        }
        return out + "\n}";
    }
}
