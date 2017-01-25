package it.emarolab.scene_identification_tracking.semanticSceneLibrary.tracker;

import it.emarolab.scene_identification_tracking.semanticSceneLibrary.Logger.SITBase;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.Semantics;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.objects.ObjectSemantics;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import java.util.Collection;
import java.util.HashSet;

/**
 * General interface to define an object shape tracker and propagate object's {@link Semantics}.
 * <p>
 *     This object should manage a set of #Item (i.e.: <code>{@link Collection}&lt;? extends {@link Item}&gt;</code>),
 *     also called <b>tracking set</b>. Where each tracked #Item is addressable by un unique identifier
 *     (@see <code>{@link Item}.equals()</code>).
 *     Thus this implementation place the base for: {@link #add(Object)}, {@link #remove(Object)} and check if
 *     an {@link #contains(Object)} and elements in the Set.<br>
 *     Nevertheless, those functions should be internally used to develop a more easy interface
 *     for tracking: {@link #updateScan(Collection)}, {@link #updateScan(Object)} and {@link #updateScan(Item)}.
 *     This call is aimed into update the tracked {@link Collection} with new shapes first
 *     (see: {@link #update(Object)} and {@link #update(Item)}). Then, it search for shapes no more
 *     in the scene by calling {@link #clean()}. For each of those tracking operation, an
 *     {@link UpdatingState} result will be returned to known the tracking operation
 *     performed by un implementation.
 * <p>
 *     Last but not the least, consider that it manages common function for adding sets of different objects.
 *     Thanks to this, is possible to manipulate the tracked {@link Collection} through two different
 *     objects:
 *     <ul>
 *        <li> the {@link Tracker.Item} (<code>'P'</code>), that contains also a reference to
 *             a primitive (see: {@link Item#getSemanticObject()}).
 *        <li> the {@link ObjectSemantics.Primitive} (<code>'P'</code>), in this case a default
 *             tracked instance of the object will be used (see: {@link Item#Item(Object)}).
 *     </ul>
 *     Such a behaviour is implemented though the {@link CastMappingTry} abstract class
 *     and used by the methods: {@link #castPrimitives(Collection)} and {@link #castTrackedItems(Collection)}
 *     (see also: {@link #add(Collection)}, {@link #remove(Collection)}, {@link #contains(Collection)}).
 * <p>
 *     <b>REMARK</b>: note that it does not depends on
 *     {@link ObjectSemantics.Primitive},
 *     but on {@link it.emarolab.scene_identification_tracking.semanticSceneLibrary.objects.aMOR.MORPrimitive},
 *     just for id casting reasons. The tracker does not really needs a semantic structure.
 *
 * <div style="text-align:center;"><small>
 * <b>File</b>:       it.emarolab.scene_identification_tracking.semanticSceneLibrary.Semantics <br>
 * <b>Licence</b>:    GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
 * <b>Author</b>:     Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
 * <b>affiliation</b>: DIBRIS, EMAROLab, University of Genoa. <br>
 * <b>date</b>:       06/01/2017 <br>
 * </small></div>
 *
 * @param <T> The tracked object represented as an <code>{@link Item}&lt;?,?,P&gt;</code>.
 *            This objects should manage a <code>{@link Collection}&lt;{@link Item}&lt;?,?,P&gt;&gt;</code>
 * @param <P> The primitive object describing the Semantic of the shape to track.
 *           Each <code>'T'</code> object describes a primitive by adding probabilistic information for tracking.
 *
 * @see Item
 * @see ShapeItem
 * @see UpdatingState
 * @see Semantics
 */
public interface Tracker<P, T extends Tracker.Item<?,?, P>> {

    /**
     * Based on {@link #castPrimitives(Collection)} and {@link #castTrackedItems(Collection)} respectively,
     * returns <code>true</code> if a set is contained in the tracking set.
     * Its default implementations relays on: {@link #contains(Object)} and {@link #contains(Item)}, respectively.
     * @param set the set to check for belonging.
     * @return true if the input is contained in the tracking set.
     */
    default boolean contains( Collection<?> set){
        Collection<P> primitive = castPrimitives(set);
        if( primitive != null) {
            for( P p : primitive)
                if( ! contains(p))
                    return false;
        } else {
            Collection<T> tracked = castTrackedItems(set);
            if( tracked != null)
                for( T t : tracked)
                    if( ! contains(t))
                        return false;
        }
        return true;
    }
    /**
     * Based on {@link #castPrimitives(Collection)} and {@link #castTrackedItems(Collection)} respectively,
     * returns the state changes performed in the tracking set for adding (or update) the inputs.
     * Its default implementations relays on: {@link #add(Object)} and {@link #add(Item)}, respectively.
     * @param set the set to add or update.
     * @return the changes made in the tracking set for addings.
     *         It returns {@link UpdatingState#asError()} of the input parameter is not a
     *         {@link Collection} with elements of type: <code>'T'</code> or <code>'P'</code>.
     */
    default Collection< UpdatingState> add( Collection<?> set){
        Collection< UpdatingState> out = new HashSet<>();
        Collection<P> primitive = castPrimitives(set);
        if( primitive != null)
            for( P p : primitive)
                out.add( add(p));
        else {
            Collection<T> tracked = castTrackedItems(set);
            if( tracked != null)
                for( T t : tracked)
                    out.add( add(t));
            else {
                out.add( new UpdatingState().asError("cannot try to remove unknown object in Collection<?>. " +
                        "Possible classes are <? extends { P:primitive, T:tracked}>."));
                return out;
            }
        }
        return out;
    }
    /**
     * Based on {@link #castPrimitives(Collection)} and {@link #castTrackedItems(Collection)} respectively,
     * returns the state changes performed in the tracking set for removing the inputs.
     * Its default implementations relays on: {@link #remove(Object)} and {@link #remove(Item)}, respectively.
     * @param set the set to remove.
     * @return the changes made in the tracking set for removings.
     *         It returns {@link UpdatingState#asError()} of the input parameter is not a
     *         {@link Collection} with elements of type: <code>'T'</code> or <code>'P'</code>.
     */
    default Collection< UpdatingState> remove( Collection<?> set){
        Collection< UpdatingState> out = new HashSet<>();
        Collection<P> primitive = castPrimitives(set);
        if( primitive != null) {
            for( P p : primitive)
                out.add( remove(p));
        } else {
            Collection<T> tracked = castTrackedItems(set);
            if( tracked != null)
                for( T t : tracked)
                    out.add( remove(t));
            else {
                out.add( new UpdatingState().asError("cannot try to remove unknown object in Collection<?>. " +
                        "Possible classes are <? extends { P:primitive, T:tracked}>."));
                return out;
            }
        }
        return out;
    }
    /**
     * This method update the tracked set with all the object given as input
     * considered with {@link #castPrimitives(Collection)} and {@link #castTrackedItems(Collection)},
     * based on {@link #update(Object)} and {@link #update(Item)} respectively.
     * @param objects the set of object (<code>'P'</code> or <code>'T?</code>) to be updated and in the tracking set.
     * @return the state changes for adding (or updating) the inputs object in the tracked set.
     *         It returns {@link UpdatingState#asError()} of the input parameter is not a
     *         {@link Collection} with elements of type: <code>'T'</code> or <code>'P'</code>.
     */
    default Collection< UpdatingState> update( Collection<?> objects){
        Collection<UpdatingState> out = new HashSet< UpdatingState>();
        SITBase.SITBase.LOG( "       ------------------------------ UPDATE STARTS ---------------------------------------");
        Collection<P> primitive = castPrimitives( objects);
        if( primitive != null) {
            for( P p : primitive)
                out.add( update(p));
        } else {
            Collection<T> tracked = castTrackedItems( objects);
            if( tracked != null)
                for( T t : tracked)
                    out.add( update(t));
            else {
                out.add( new UpdatingState().asError("cannot update unknown object in Collection<?>. " +
                        "Possible classes are <? extends { P:primitive, T:tracked}>."));
                return out;
            }
        }
        SITBase.SITBase.LOG( "       ------------------------------  UPDATE ENDS   ---------------------------------------");
        SITBase.SITBase.LOG( "       -------------------------------------------------------------------------------------");
        return out;
    }

    /**
     * Safely cast an <code>{@link Collection}&lt;?&gt;</code> in an
     * <code>{@link Collection}&lt;T&gt;</code>.
     * @param collection the collection to be cast.
     * @return the casted set or <code>null</code> if the cast was impossible for at lest one element of the
     * @see CastMappingTry
     * @see #add(Collection)
     * @see #contains(Collection)
     * @see #remove(Collection)
     */
    default Collection<T> castTrackedItems(Collection<?> collection){
        final Collection<T> casted = new HashSet<T>();
        CastMappingTry tryer = new CastMappingTry( collection) {
            /**
             * @see Tracker#castTrackedItems(Collection)
             * @param q the element of the input collection of {@link Tracker#castTrackedItems(Collection)},
             *          to be casted in a <code>try/catch( {@link Exception})</code> safe block.
             */
            public void giveAcast(Object q) {
                casted.add( (T) q);
            }
        };
        if( ! tryer.perform())
            return null;
        return casted;
    }
    /**
     * Safely cast all a <code>{@link Collection}&lt;?&gt;</code> in an
     * <code>{@link Collection}&lt;P&gt;</code>.
     * @param collection the collection to be cast.
     * @return the casted set or <code>null</code> if the cast was impossible for at lest one element of the
     * <code>collection</code>.
     * @see CastMappingTry
     * @see #add(Collection)
     * @see #contains(Collection)
     * @see #remove(Collection)
     */
    default Collection<P> castPrimitives(Collection<?> collection){
        final Collection<P> casted = new HashSet<P>();
        CastMappingTry tryer = new CastMappingTry( collection) {
            /**
             * @see Tracker#castPrimitives(Collection)
             * @param q the element of the input collection of {@link Tracker#castPrimitives(Collection)}
             *          to be casted in a <code>try/catch( {@link Exception})</code> safe block.
             */
            public void giveAcast(Object q) {
                casted.add( (P) q);
            }
        };
        if( ! tryer.perform())
            return null;
        return casted;
    }

    /**
     * The <code>{@link Collection}&lt;? extends {T, P}&gt;</code> casting helper.
     * <p>
     *     This class rely on {@link Semantics.Try} for performing a generic
     *     cast in a <code>try/catch( {@link Exception})</code> safety block:
     *     the {@link #giveAcast(Object)} interface.
     *
     * <div style="text-align:center;"><small>
     * <b>File</b>:       it.emarolab.scene_identification_tracking.semanticSceneLibrary.Semantics <br>
     * <b>Licence</b>:    GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
     * <b>Author</b>:     Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
     * <b>affiliation</b>: DIBRIS, EMAROLab, University of Genoa. <br>
     * <b>date</b>:       06/01/2017 <br>
     * </small></div>
     * @see Tracker#castPrimitives(Collection)
     * @see Tracker#castTrackedItems(Collection)
     */
    abstract class CastMappingTry extends Semantics.Try< Boolean> { // true:SUCCESS, false:ERROR
        /** the collection to scan given on constructor */
        final private Collection<?> toScan;
        /**
         * @param toScan the collection to scan in order to cast each its elements.
         */
        public CastMappingTry( Collection<?> toScan){
            this.toScan = toScan;
        }

        /**
         * @param q a single element of the set given on constructor to cast.
         * @see #giveAtry()
         */
        abstract public void giveAcast( Object q);

        /**
         * called in the <code>try/catch( {@link Exception})</code> safe block it
         * scans all the component of the set given on constructor and, for each,
         * calls {@link #giveAcast(Object)}.
         * @return makes {@link #perform()} returning <code>true</code> if an {@link Exception} did not occur.
         */
        @Override
        protected Boolean giveAtry() {
            for( Object q : toScan)
                giveAcast( q);
            return true;
        }
        /**
         * @return makes {@link #perform()} returning <code>false</code> if an {@link Exception} occurs.
         */
        @Override
        protected Boolean onError() {
            return false;
        }
    }

    /**
     * Returns <code>true</code> if a tracked item is contained in the tracking set.
     * Its default implementations relays on {@link #contains(Object)} by using {@link Item#getSemanticObject()}.
     * @param tracked the tracked object to check for belonging.
     * @return true if the input is contained in the tracking set.
     */
    default boolean contains( T tracked){
        return contains( tracked.getSemanticObject());
    }
    /**
     * Returns the state change performed in the tracking set for adding (or update) the input object.
     * Its default implementations relays on {@link #add(Object)} by using {@link Item#getSemanticObject()}.
     * @param tracked the tracked object to add or update.
     * @return the changes made in the tracking set for adding.
     */
    default UpdatingState add( T tracked){
        return add( tracked.getSemanticObject());
    }
    /**
     * Returns the state change performed in the tracking set for removing the input object.
     * Its default implementations relays on {@link #remove(Object)} by using {@link Item#getSemanticObject()}.
     * @param tracked the tracked object to remove.
     * @return the changes made in the tracking set for removing.
     */
    default UpdatingState remove( T tracked){
        return remove( tracked.getSemanticObject());
    }

    /**
     * Returns <code>true</code> if an object is contained in the tracking set.
     * This is the basic implementation for all the polymorphism for <code>contains(...)</code> methods.
     * In this implementation, you should create a new {@link Item#Item(Item)}.
     * @param object the primitive object to check for belonging.
     * @return true if the input is contained in the tracking set.
     */
    boolean contains( P object);
    /**
     * Returns the state change performed in the tracking set for adding (or updating) the input primitive object.
     * This is the basic implementation for all the polymorphism for <code>add(...)</code> methods.
     * In this implementation, you should create a new {@link Item#Item(Item)}.
     * @param object the primitive object to add or update.
     * @return the changes made in the tracking set for adding.
     */
    UpdatingState add( P object);
    /**
     * Returns the state change performed in the tracking set for removing the input primitive object.
     * This is the basic implementation for all the polymorphism for <code>remove(...)</code> methods.
     * In this implementation, you should create a new {@link Item#Item(Item)}.
     * @param object the primitive object to remove.
     * @return the changes made in the tracking set for removing.
     */
    UpdatingState remove(P object);

    /**@return the semantic objects of the tracking set managed by this implementation. */
    Collection<? extends P> getTrackedPrimitives();
    /**@return the tracking set. */
    Collection<? extends SemanticItem> getTrackedSet();

    /**
     * This method is considered to be used for updating in one shot the
     * tracker with the new information coming from a perception module.
     * Thus, it update the state of the internal tracking set ({@link #update(Collection)})
     * first. And tha, it calls {@link #clean()} for removing objects that are no more
     * seen by the perceiving component.
     * @param objects the set of objects to be updated and in the tracking set.
     * @return the concatenation of returning values obtained by updating and cleaning.
     */
    default Collection<UpdatingState> updateScan( Collection<?> objects){
        Collection<UpdatingState> out = update(objects);
        out.addAll( clean());
        return out;
    }
    /**
     * This method is considered to be used for updating in one shot the
     * tracker with the new information coming from a perception module.
     * Thus, it update the state of the internal tracking set ({@link #update(Item)})
     * first. And tha, it calls {@link #clean()} for removing objects that are no more
     * seen by the perceiving component.
     * @param tracked the tracked object to be updated and in the tracking set.
     * @return the concatenation of returning values obtained by updating and cleaning.
     */
    default UpdatingState updateScan( T tracked){
        return update( tracked);
    }
    /**
     * This method is considered to be used for updating in one shot the
     * tracker with the new information coming from a perception module.
     * Thus, it update the state of the internal tracking set ({@link #update(Object)})
     * first. And tha, it calls {@link #clean()} for removing objects that are no more
     * seen by the perceiving component.
     * @param objects the primitive object to be updated and in the tracking set.
     * @return the concatenation of returning values obtained by updating and cleaning.
     */
    default UpdatingState updateScan( P objects){
        return update( objects);
    }

    /**
     * Base implementation for updating this tracking set.
     * It should create a <code>new T</code> elemnt (see: {@link Item#Item(Item)})
     * and call {@link #update(Item)}.
     * @param object the primitive object ot add/update in the tracking set.
     * @return the updating state change performed to add/update the tracking set.
     */
    UpdatingState update( P object);
    /**
     * Base implementation for updating this tracking set.
     * It should {@link UpdatingState#ADDED} the input parameter
     * if the set if it is not available. Otherwise it checks that the probability
     * of changing shape is high enough ({@link UpdatingState#UPDATED}) to be considered.
     * Otherwise, it {@link UpdatingState#AVERAGED} the tracking state and the input parameter,
     * if those are of the same shape. Where an input object is consider to be the same of
     * an object in the tracking set by {@link Item#getId()}.
     * @param tracked the tracked object ot add/update in the tracking set.
     * @return the updating state change performed to add/update the tracking set.
     */
    UpdatingState update( T tracked);

    /**
     * This function should {@link #remove(Item)} elements that are no more used while
     * updating this tracking set.
     * @return the updating state change while cleaning the tracking set.
     */
    Collection< UpdatingState> clean();

    /**
     * The base item that defines the elements of the tracking set, managed by a {@link Tracker}.
     * <p>
     *     This class defines a structure thant contains:
     *     <ul>
     *        <li> <code>'S'</code>: the description of the Shape.
     *             Good practice is to use an identifing tag (e.g.: an Integer) and
     *             base the shape definition it through class hierarchy,
     *             see {@link ObjectSemantics.Primitive}). fon an example.
     *             <ul> <li> {@link #getShape()} </ul>
     *        <li> <code>'I'</code>: the unique identifier for the object to which
     *             assign a tracked shape estimation. This is a crucial point for the managing
     *             of the tracked set (see: {@link Tracker}). Good practice is to
     *             set id equal to the semantic individual that describes the object,
     *             see: {@link Semantics.IndividualDescriptor.DataDescriptor#getInstance()}.
     *             <ul> <li> {@link #getId()} </ul>
     *        <li> <code>'P'</code>: the shape definition, and relate coefficient, of the
     *             object as a primitive representation.
     *             It describes semantically the object to be tracked in shape
     *             and it is suppose to come from perception and affected by noise.
     *             <ul> <li> {@link #getSemanticObject()} </ul>
     *     </ul>
     * <p>
     *     This class gives the basic implementation by leaving to the extendings
     *     the role to implement:
     *     <ul>
     *         <li> the initialisation of the shape identifying instances: <code>S {@link #shape};</code>
     *         <li> the initialisation of the shape identifying instances: <code>I {@link #id};</code>
     *         <li> the initialisation of the shape identifying instances: <code>P {@link #object};</code>
     *         <li> a {@link #copy()} procedure though copy constrcutors {@link #Item(Item)}.
     *         <li> a {@link #shapeAs(Object)} method for deciding if {@link UpdatingState#UPDATED}
     *              or {@link UpdatingState#AVERAGED} during tracking.
     *         <li> an {@link #update(Object)} method to decide if the incoming belief of the
     *              shape of the object should be UPDATED, AVERAGED or {@link UpdatingState#NOT_CHANGED}
     *              with the new incoming primitive semantic object.
     *     </ul>
     *     Nevertheless, it implement felicitating procedure for tracking set management by overwriting
     *     the <code>{@link Item#equals(Object)}</code> in order to compare between each other:
     *     {@link Item}, semantic objects (<code>'P'</code>) and item ID (<code>'I'</code>).<br>
     *     <b>REMARK</b>: only <code>this.equals( something)</code> will work as expected,
     *     <code>something.equals( this)</code> not!
     *
     *
     * <div style="text-align:center;"><small>
     * <b>File</b>:       it.emarolab.scene_identification_tracking.semanticSceneLibrary.Semantics <br>
     * <b>Licence</b>:    GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
     * <b>Author</b>:     Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
     * <b>affiliation</b>: DIBRIS, EMAROLab, University of Genoa. <br>
     * <b>date</b>:       06/01/2017 <br>
     * </small></div>
     *
     * @param <S> the shape tag identifier type.
     * @param <I> the object unique identifier type.
     * @param <P> the primitive semantic object type.
     *
     * @see Tracker
     * @see Semantics
     */
    abstract class Item< S, I, P> extends SITBase.SITBase {
        // private internal field that are needed by all elements of the tracker
        private S shape;
        private I id;
        private P object;

        /**
         * This constructor should used for object cloning.
         * Anyway, this implementation does not do nothing.
         * @param item not used
         */
        public Item( Item<S, I, P> item){
            // delegate clone where parameters S,I,O are defined
        }
        /**
         * Initialise this tracked item by assigning a new semantic object <code>'O'</code>.
         * (see: {@link #initialise(Object)}.
         * @param object the semantic object that represents tracker belief about this ID.
         */
        public Item(P object) {
            initialise(object);
        }
        /**
         * Reinitialise all private fields of this item based on a new semantic primitive Object.<br>
         * In particular, it updates the {@link #object} field and than calls
         * {@link #getId(Object)} and {@link #getShape(Object)} by assigning the returning value
         * to {@link #id} and {@link #shape} respectively.
         * @param object the new object describing the shape of this tracked item.
         */
        public void initialise(P object) {
            this.object = object;
            this.id = getId(object);
            this.shape = getShape(object);
        }

        /**
         * Given a new semantic belief of the shape of this object
         * return a new shape tag that will be set to {@link #shape}
         * on {@link #initialise(Object)}.
         * @param object a new belief of the shape of the tracked item.
         * @return the representation of the new shape of the object.
         */
        abstract public S getShape(P object);
        /**
         * Given a new semantic belief of the shape of this object
         * return a new unique identifier for this Item that will be set to {@link #id}
         * on {@link #initialise(Object)}.
         * @param object a new belief of the shape of the tracked item.
         * @return the representation of the new shape of the object.
         */
        abstract public I getId(P object);

        /** @return the unique identifier ({@link #id}) of this tracked item. */
        public I getId() {
            return id;
        }
        /** @return the shape identifier ({@link #shape}) of this tracked item. */
        public S getShape() {
            return shape;
        }
        /** @return the semantic representation of the tracked object ({@link #object}). */
        public P getSemanticObject() {
            return object;
        }

        /** @param shape the shape identifier parameter to set to the {@link #shape} description of this Item. */
        public void setShape(S shape) {
            this.shape = shape;
        }
        /** @param id the unique identifier parameter to set to the {@link #id} description of this Item. */
        public void setId(I id) {
            this.id = id;
        }
        /** @param object the semantic object parameter to set to the {@link #object} description of this Item. */
        public void setSemanticObject(P object) {
            this.object = object;
        }

        /**
         * Calls {@link #update(Object)} by retrieving the {@link #getSemanticObject()}
         * from the input value.
         * @param object the Item to be updated to this tracked item
         * @return the resulting value coming from {@link #update(Object)}.
         */
        public UpdatingState update(Item<S, I, P> object) {
            return update( object.getSemanticObject());
        }
        /**
         * Base updating interface. This method should use probabilistic methods
         * to deal with perception noise and decide if the old belief (about the shape of an object)
         * is better (or not) than a new one.<br>
         * This method should be called only for inputs parameter that are equals (same {@link #getId()})
         * to this object. If yes, if can decide to {@link UpdatingState#UPDATED} the Item
         * with a new belief or returns {@link UpdatingState#NOT_CHANGED} if the new belief is unsealable.
         * It can return also {@link UpdatingState#AVERAGED} if the two Items are of the
         * same shapes.
         *
         * @param object the new belief describing a semantic object about this tracking Item.
         * @return the updating state changes during the operation.
         */
        abstract UpdatingState update(P object);

        /**
         * @param object a new semantic Object to check for shape equality with respect to <code>this</code>.
         * @return <code>true</code> if <code>this</code> has the same {@link #getShape()} value that
         * would have the input parameter if it were a tracked Item.
         */
        abstract public boolean shapeAs(P object);

        /**
         * It should call the proper copy constructor and apply its hierarchy
         * (do not use <code>super.copy()</code> here).
         * @return a <code>new</code> copy of this object.
         */
        abstract public Item<S,I, P> copy();

        /**
         * This methods manage the equality between: Item, <code>'S'</code> and <code>'I'</code>
         * by reducing all process to evaluate the: {@link #getId()} value.
         * This method is aimed to be used with a {@link Collection} (e.g.: {@link java.util.Set})
         * instance of the tracking set (see: {@link Tracker}).
         * @param o the object to check for equality with respect to <code>this</code>.
         * @return <code>true</code> if this object is equals to the input parameter.
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            try { // try with an object 'o' that is this actual ID
                if (((I) o).equals(id))
                    return true;
            } catch (Exception e) {
            }

            if (!(o instanceof Item))
                return false;
            Item<?, ?, ?> item = (Item<?, ?, ?>) o;
            return getId() != null ? getId().equals(item.getId()) : item.getId() == null;
        }
        @Override
        public int hashCode() {
            return getId() != null ? getId().hashCode() : 0;
        }

        @Override
        public String toString() {
            return "Item{" +
                    "shape=" + shape +
                    ", id=" + id +
                    ", object=" + object +
                    '}';
        }
    }

    /**
     * The implementation of an {@link Item} with {@link Integer} shape identifier and {@link OWLNamedIndividual} as ID.
     * <p>
     *     This is a special type of tracked {@link Item}, which can deal with the semantic
     *     object hierarchy (with root class: {@link ObjectSemantics.Primitive}). <br>
     *     In particular, it describes the shape of the Objects with integer values that
     *     can be easily checked by using:
     *     {@link #isUndefined()}, {@link #isPrimitive()}, {@link #isOrientable()},
     *     {@link #isSphere()}, {@link #isPlane()}, {@link #isCone()} and {@link #isCylinder()}
     *     (see: {@link #getShape(Object)}).
     * <p>
     *     Also, it contains the definition (as constants) of the confusion matrix of the error
     *     in recognise objects shapes on a table top scenario published in:<br>
     *     <tt>Buoncompagni, Luca, and Fulvio Mastrogiovanni. "A Software Architecture for Object Perception
     *     and Semantic Representation." Workshop on Artificial Intelligence and Robotics (AIRO 2015).</tt><br>
     *     Those results are obtaining by using the Primitive Identification Tagging and Tracking
     *     <a href="https://github.com/EmaroLab/primitive_identification_tracking_tagging">(PIT)</a>
     *     ROS architecture.
     * <p>
     *     Last but not the least, it poses the basic interface to thw aMOR and OWl interface by
     *     forcing {@link Item} parameters to be:
     *     <ul>
     *        <li> <code>'I'</code> an {@link OWLNamedIndividual}, which defines an unique semantic object.
     *        <li> <code>'P'</code> an extension of {@link ObjectSemantics.Primitive}.
     *        <li> <code>'S'</code> an Intger.
     *     </ul>
     *
     * <div style="text-align:center;"><small>
     * <b>File</b>:       it.emarolab.scene_identification_tracking.semanticSceneLibrary.Semantics <br>
     * <b>Licence</b>:    GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
     * <b>Author</b>:     Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
     * <b>affiliation</b>: DIBRIS, EMAROLab, University of Genoa. <br>
     * <b>date</b>:       06/01/2017 <br>
     * </small></div>
     *
     * @param <P> The primitive object describing the Semantic of the shape to track.
     *
     * @see Item
     * @see OWLNamedIndividual
     * @see Tracker
     * @see SemanticItem
     */
    abstract class ShapeItem< P extends ObjectSemantics.Primitive>
            extends Item< Integer, OWLNamedIndividual, P>{

        // todo to review!!!
        // X2Y_PROBABILITY = intention to change a tracked object of shape X in shape Y

        /** the probability that a {@link ObjectSemantics.Primitive} or {@link ObjectSemantics.Orientable},
         * is correctly classified as a plane again.
         */
        public static final double PRIMITIVE2PRIMITIVE_PROBABILITY = 87.40 / 100;
        /**
         * the probability that an previously miss classified object as a:
         * {@link ObjectSemantics.Primitive} or {@link ObjectSemantics.Orientable},
         * is now more correctly classified as: {@link ObjectSemantics.Plane}.
         */
        public static final double PRIMITIVE2PLANE_PROBABILITY    = 11.65 / 100;
        /**
         * the probability that an previously miss classified object as a:
         * {@link ObjectSemantics.Primitive} or {@link ObjectSemantics.Orientable},
         * is now more correctly classified as: {@link ObjectSemantics.Sphere}.
         */
        public static final double PRIMITIVE2SPHERE_PROBABILITY   =  0.20 / 100;
        /**
         * the probability that an previously miss classified object as a:
         * {@link ObjectSemantics.Primitive} or {@link ObjectSemantics.Orientable},
         * is now more correctly classified as: {@link ObjectSemantics.Cone}.
         */
        public static final double PRIMITIVE2CONE_PROBABILITY     =  1.06 / 100;
        /**
         * the probability that an previously miss classified object as a:
         * {@link ObjectSemantics.Primitive} or {@link ObjectSemantics.Orientable},
         * is now more correctly classified as: {@link ObjectSemantics.Cylinder}.
         */
        public static final double PRIMITIVE2CYLINDER_PROBABILITY =  1.60 / 100;

        /**
         * the probability that an previously miss classified object as a {@link ObjectSemantics.Plane},
         * is now more correctly classified as: {@link ObjectSemantics.Primitive} or {@link ObjectSemantics.Orientable}.
         */
        public static final double PLANE2PRIMITIVE_PROBABILITY = 12.50 / 100;
        /** the probability that a {@link ObjectSemantics.Plane} is correctly classified as a plane again. */
        public static final double PLANE2PLANE_PROBABILITY     = 82.54 / 100;
        /**
         * the probability that an previously miss classified object as a {@link ObjectSemantics.Plane},
         * is now correctly classified as a {@link ObjectSemantics.Sphere}.
         */
        public static final double PLANE2SPHERE_PROBABILITY    =  6.63 / 100;
        /**
         * the probability that an previously miss classified object as a {@link ObjectSemantics.Plane},
         * is now correctly classified as a {@link ObjectSemantics.Cone}.
         */
        public static final double PLANE2CONE_PROBABILITY      =  7.22 / 100;
        /**
         * the probability that an previously miss classified object as a {@link ObjectSemantics.Plane},
         * is now correctly classified as a {@link ObjectSemantics.Cylinder}.
         */
        public static final double PLANE2CYLINDER_PROBABILITY  =  8.70 / 100;

        /**
         * the probability that an previously miss classified object as a {@link ObjectSemantics.Sphere},
         * is now more correctly classified as: {@link ObjectSemantics.Primitive} or {@link ObjectSemantics.Orientable}.
         */
        public static final double SPHERE2PRIMITIVE_PROBABILITY =  0.10 / 100; // todo to review!!!
        /**
         * the probability that an previously miss classified object as a {@link ObjectSemantics.Sphere},
         * is now correctly classified as a {@link ObjectSemantics.Plane}.
         */
        public static final double SPHERE2PLANE_PROBABILITY     =  0.10 / 100; // todo to review!!!
        /** the probability that a {@link ObjectSemantics.Sphere} is correctly classified as a sphere again. */
        public static final double SPHERE2SPHERE_PROBABILITY    = 93.17 / 100;
        /**
         * the probability that an previously miss classified object as a {@link ObjectSemantics.Sphere},
         * is now correctly classified as a {@link ObjectSemantics.Cone}.
         */
        public static final double SPHERE2CONE_PROBABILITY      =  0.10 / 100; // todo to review!!!
        /**
         * the probability that an previously miss classified object as a {@link ObjectSemantics.Sphere},
         * is now correctly classified as a {@link ObjectSemantics.Cylinder}.
         */
        public static final double SPHERE2CYLINDER_PROBABILITY  =  0.10 / 100; // todo to review!!!

        /**
         * the probability that an previously miss classified object as a {@link ObjectSemantics.Cone},
         * is now more correctly classified as: {@link ObjectSemantics.Primitive} or {@link ObjectSemantics.Orientable}.
         */
        public static final double CONE2PRIMITIVE_PROBABILITY =  0.10 / 100; // todo to review!!!
        /**
         * the probability that an previously miss classified object as a {@link ObjectSemantics.Cone},
         * is now correctly classified as a {@link ObjectSemantics.Plane}.
         */
        public static final double CONE2PLANE_PROBABILITY     =  0.32 / 100;
        /**
         * the probability that an previously miss classified object as a {@link ObjectSemantics.Cone},
         * is now correctly classified as a {@link ObjectSemantics.Sphere}.
         */
        public static final double CONE2SPHERE_PROBABILITY    =  0.10 / 100; // todo to review!!!
        /** the probability that a {@link ObjectSemantics.Cone} is correctly classified as a cone again. */
        public static final double CONE2CONE_PROBABILITY      = 66.04 / 100;
        /**
         * the probability that an previously miss classified object as a {@link ObjectSemantics.Cone},
         * is now correctly classified as a {@link ObjectSemantics.Cylinder}.
         */
        public static final double CONE2CYLINDER_PROBABILITY  =  0.11 / 100;

        /**
         * the probability that an previously miss classified object as a {@link ObjectSemantics.Cylinder},
         * is now more correctly classified as: {@link ObjectSemantics.Primitive} or {@link ObjectSemantics.Orientable}.
         */
        public static final double CYLINDER2PRIMITIVE_PROBABILITY =  0.10 / 100;
        /**
         * the probability that an previously miss classified object as a {@link ObjectSemantics.Cylinder},
         * is now correctly classified as a {@link ObjectSemantics.Plane}.
         */
        public static final double CYLINDER2PLANE_PROBABILITY     =  5.49 / 100;
        /**
         * the probability that an previously miss classified object as a {@link ObjectSemantics.Cylinder},
         * is now correctly classified as a {@link ObjectSemantics.Sphere}.
         */
        public static final double CYLINDER2SPHERE_PROBABILITY    =  0.10 / 100; // todo to review!!!
        /**
         * the probability that an previously miss classified object as a {@link ObjectSemantics.Cylinder},
         * is now correctly classified as a {@link ObjectSemantics.Cone}.
         */
        public static final double CYLINDER2CONE_PROBABILITY      = 25.68 / 100;
        /** the probability that a {@link ObjectSemantics.Cylinder} is correctly classified as a cylinder again. */
        public static final double CYLINDER2CYLINDER_PROBABILITY  = 89.59 / 100;

        /** an undefined identifier for the object shape. */
        private static final int UNDEFINED_SHAPE_TAG = -1;
        /** the {@link ObjectSemantics.Primitive} identifier for the object shape. */
        private static final int PRIMITIVE_SHAPE_TAG = 0;
        /** the {@link ObjectSemantics.Sphere} identifier for the object shape. */
        private static final int SPHERE_SHAPE_TAG = 1;
        /**
         * the {@link ObjectSemantics.Orientable} identifier for the object shape.
         * By default, set equal to {@link #PRIMITIVE_SHAPE_TAG} since
         * not distinguishable from a perceiving point of view.
         */
        private static final int ORIENTABLE_SHAPE_TAG = PRIMITIVE_SHAPE_TAG;
        /** the {@link ObjectSemantics.Plane} identifier for the object shape. */
        private static final int PLANE_SHAPE_TAG = 3;
        /** the {@link ObjectSemantics.Cone} identifier for the object shape. */
        private static final int CONE_SHAPE_TAG = 4;
        /** the {@link ObjectSemantics.Cylinder} identifier for the object shape. */
        private static final int CYLINDER_SHAPE_TAG = 5;

        /**
         * Cloning constructor, create a new object as a clone of the input parameter.
         * @param item the object to clone.
         */
        public ShapeItem(Item< Integer, OWLNamedIndividual, P> item) {
            super( item);
            this.setShape(( Integer) item.getShape());
        }
        /**
         * Create a new tracking item by assigning its {@link #object}.
         * Just calls {@link Item#Item(Object)}.
         * @param object the semantic object to described this tracked instance.
         */
        public ShapeItem(P object) {
            super(object);
        }

        /**
         * <p>
         *     Assign tho this item its shape by looking on {@link ObjectSemantics.Primitive} class hierarchy.
         *     Te returning value is automatically assigned to {@link #shape} during {@link Item} constructors.
         * <p> More in particular, it returns:
         *     <ul>
         *        <li> {@link #PRIMITIVE_SHAPE_TAG} if <code>object instaceof {@link ObjectSemantics.Primitive}</code>,
         *        <li> {@link #SPHERE_SHAPE_TAG} if <code>object instaceof {@link ObjectSemantics.Sphere}</code>,
         *        <li> {@link #ORIENTABLE_SHAPE_TAG} if <code>object instaceof {@link ObjectSemantics.Orientable}</code>,
         *        <li> {@link #PLANE_SHAPE_TAG} if <code>object instaceof {@link ObjectSemantics.Plane}</code>,
         *        <li> {@link #CONE_SHAPE_TAG} if <code>object instaceof {@link ObjectSemantics.Cone}</code>,
         *        <li> {@link #CYLINDER_SHAPE_TAG} if <code>object instaceof {@link ObjectSemantics.Cylinder}</code>,
         *        <li> {@link #UNDEFINED_SHAPE_TAG} otherwise.
         *     </ul>
         * @param object the semantic object to track.
         * @return an integer state identifying the objects shape.
         * @see #isUndefined()
         * @see #isPlane()
         * @see #isPrimitive()
         * @see #isSphere()
         * @see #isOrientable()
         * @see #isPlane()
         * @see #isCone()
         * @see #isCylinder()
         */
        @Override
        public Integer getShape( P object) {
            int shape = UNDEFINED_SHAPE_TAG;
            if( object instanceof ObjectSemantics.Primitive)
                shape = PRIMITIVE_SHAPE_TAG;
            if( object instanceof ObjectSemantics.Sphere)
                shape = SPHERE_SHAPE_TAG;
            if( object instanceof ObjectSemantics.Orientable)
                shape = ORIENTABLE_SHAPE_TAG;
            if( object instanceof ObjectSemantics.Plane)
                shape = PLANE_SHAPE_TAG;
            if( object instanceof ObjectSemantics.Cone)
                shape = CONE_SHAPE_TAG;
            if( object instanceof ObjectSemantics.Cylinder)
                shape = CYLINDER_SHAPE_TAG;
            return shape;
        }

        /**
         * @param object the object to compare with this tracked Item.
         * @return <code>true</code> if the given object is of the same shape of this tracked Item.
         * @see #getShape(ObjectSemantics.Primitive)
         */
        @Override
        public boolean shapeAs(  P object){
            if( ( isPrimitive() | isOrientable()) & (
                    object instanceof ObjectSemantics.Primitive | object instanceof ObjectSemantics.Orientable))
                return true;
            if( isSphere() & object instanceof ObjectSemantics.Sphere)
                return true;
            if( isPlane() & object instanceof ObjectSemantics.Plane)
                return true;
            if( isCone() & object instanceof ObjectSemantics.Cone)
                return true;
            if( isCylinder() & object instanceof ObjectSemantics.Cylinder)
                return true;
            return false; // isUndefined()
        }

        /**
         * @return if this is an object of an undefined shape.
         *        More formally: <code>returns {@link #getShape()} == {@link #UNDEFINED_SHAPE_TAG}</code>.
         */
        public boolean isUndefined(){
            return getShape()== UNDEFINED_SHAPE_TAG;
        }
        /**
         * @return if this is an object of a {@link ObjectSemantics.Primitive} shape.
         *        More formally: <code>returns {@link #getShape()} == {@link #PRIMITIVE_SHAPE_TAG}</code>.
         */
        public boolean isPrimitive(){
            return getShape() == PRIMITIVE_SHAPE_TAG;
        }
        /**
         * @return if this is an object of a {@link ObjectSemantics.Sphere} shape.
         *        More formally: <code>returns {@link #getShape()} == {@link #SPHERE_SHAPE_TAG}</code>.
         */
        public boolean isSphere(){
            return getShape() == SPHERE_SHAPE_TAG;
        }
        /**
         * @return if this is an object of a {@link ObjectSemantics.Orientable} shape.
         *        More formally: <code>returns {@link #getShape()} == {@link #ORIENTABLE_SHAPE_TAG}</code>.
         */
        public boolean isOrientable(){
            return getShape() == ORIENTABLE_SHAPE_TAG;
        }
        /**
         * @return if this is an object of a {@link ObjectSemantics.Plane} shape.
         *        More formally: <code>returns {@link #getShape()} == {@link #PLANE_SHAPE_TAG}</code>.
         */
        public boolean isPlane(){
            return getShape() == PLANE_SHAPE_TAG;
        }
        /**
         * @return if this is an object of a {@link ObjectSemantics.Cone} shape.
         *        More formally: <code>returns {@link #getShape()} == {@link #CONE_SHAPE_TAG}</code>.
         */
        public boolean isCone(){
            return getShape() == CONE_SHAPE_TAG;
        }
        /**
         * @return if this is an object of a {@link ObjectSemantics.Cylinder} shape.
         *        More formally: <code>returns {@link #getShape()} == {@link #CYLINDER_SHAPE_TAG}</code>.
         */
        public boolean isCylinder(){
            return getShape() == CYLINDER_SHAPE_TAG;
        }

        @Override
        public String toString() {
            String sh = " UNKNOWN";
            if( isSphere())
                sh = " SPHERE ";
            else if( isPlane())
                sh = "  PLANE ";
            else if( isCone())
                sh = "  CONE  ";
            else if( isCylinder())
                sh = "CYLINDER";
            return  "[" + getId() + "]" + sh;
        }
    }

    /**
     * The changing state during a manipulation on the tracking set (see: {@link Tracker}).
     * <p>
     *     This class contains a copy of the tracked {@link Item} before ({@link #getOldItem()}) and
     *     after ({@link #getNewItem()}) as well as a tag indicating the manipulation state ({@link #getState()}).
     *     In particular it describes:
     *     <ul>
     *        <li> {@link #ERROR}, if a Java error occurs.
     *        <li> {@link #INCONSISTENT} , if the state was not considered.
     *        <li> {@link #NOT_CHANGED}, if nothing has been changed in the tracking set.
     *        <li> {@link #ADDED}, if something has been added ot the tracked set.
     *        <li> {@link #UPDATED}, if a semantic object has been replaced in the tracking set.
     *        <li> {@link #AVERAGED}, if two semantic objects have been merged (since equal shape) in the tracking set.
     *        <li> {@link #REMOVED}, if a object has been removed from the tracking set.
     *     </ul>
     * <p>
     *     <b>REMARK</b>: extending classes must have static state fields assigned to inter values bigger than {@link #REMOVED}.
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
     * @see Item
     */
    class UpdatingState extends Semantics.WritingState {
        // inheres ERROR, INCONSISTENT, NOT_CHANGED (unesed), ADDED, UPDATED
        /** The identifier for the AVERAGED state. */
        static public final int AVERAGED = 4;
        /** The identifier for the REMOVED state. */
        static public final int REMOVED = 5;

        // internal field (copy of tracked ITEM)
        private Item i0, i1;

        /**
         * instantiate this object {@link #asInconsistent()}.
         */
        public UpdatingState() {
            asInconsistent();
        }
        /**
         * Constructs this object with a given state.<br>
         * The input parameter must be:
         * <code> {@link #state} &gt;= {@link #ERROR} &amp; {@link #state} &lt;= {@link #REMOVED} </code>.
         * Otherwise, the {@link #state} will be set based on {@link #UpdatingState()}.
         * @param state the outcome of a tracking operation.
         */
        public UpdatingState(int state) {
            if (state >= ERROR & state <= REMOVED)
                this.state = state;
        }

        /**
         * Set this state as {@link #AVERAGED} and take a snapshot of the tracked {@link Item}.
         * You should pass {@link Item#copy()} to this method.
         * @param oldObj the tracked item before than merge it with another (with the same shape).
         * @param newObj the tracked item after the semantic averaging.
         * @return <code>this</code> for chaining calls.
         */
        public UpdatingState asAveraged( Item oldObj, Item newObj) {
            this.state = AVERAGED;
            i0 = oldObj;
            i1 = newObj;
            log( "  ##### AVERAGING with a new Scan: " + i0 + "\t###\told Scan: " + i1);
            return this;
        }

        /**
         * Set this state as {@link #REMOVED} and take a snapshot of the tracked {@link Item}.
         * You should pass {@link Item#copy()} to this method.
         * This method forces {@link #getNewItem()} to <code>null</code>.
         * @param oldObj the tracked item that has been remote.
         * @return <code>this</code> for chaining calls.
         */
        public UpdatingState asRemoved( Item oldObj) {
            this.state = REMOVED;
            i0 = oldObj;
            i1 = null;
            log( "  ##### REMOVING old Shape: " + i0 + "\t###\told Shape: " + i1);
            return this;
        }

        /**
         * Do not use directly this method, use {@link #asUpdated(Item, Item)} instead.
         * TThis method set {@link #getState()} to {@link #UPDATED},
         * does not affect {@link #getOldItem()} and {@link #getNewItem()}.
         * @return <code>this</code> for chaining calls.
         */
        @Deprecated @Override
        public UpdatingState asUpdated() {
            logWarning( "  ##### you should not use deprecated methods UpdatingState.asUpdate().");
            return (UpdatingState) super.asUpdated();
        }
        /**
         * Set this state as {@link #UPDATED} and take a snapshot of the tracked {@link Item}.
         * You should pass {@link Item#copy()} to this method.
         * @param oldObj the tracked item that has been update.
         * @param newObj the tracked item after the semantic updating.
         * @return <code>this</code> for chaining calls.
         */
        public UpdatingState asUpdated( Item oldObj, Item newObj) {
            i0 = oldObj;
            i1 = newObj;
            log( "  ##### UPDATING with a new Shape: " + i0 + "\t###\told Shape: " + i1);
            return (UpdatingState) super.asUpdated();
        }

        /**
         * Do not use directly this method, use {@link #asAdded(Item)}  instead.
         * This method set {@link #getState()} to {@link #ADDED},
         * but does not affect {@link #getOldItem()} and {@link #getNewItem()}.
         * @return <code>this</code> for chaining calls.
         */
        @Deprecated @Override
        public UpdatingState asAdded() {
            logWarning( "  ##### you should not use deprecated methods UpdatingState.asAdded().");
            return (UpdatingState) super.asAdded();
        }
        /**
         * Set this state as {@link #ADDED} and take a snapshot of the tracked {@link Item}.
         * You should pass {@link Item#copy()} to this method.
         * This method forces {@link #getOldItem()} to <code>null</code>.
         * @param newObj the tracked item that has been added.
         * @return <code>this</code> for chaining calls.
         */
        public UpdatingState asAdded( Item newObj) {
            this.i0 = null;
            this.i1 = newObj;
            log( "  ##### ADDING with a new Shape: " + i0 + "\t###\told Shape: " + i1);
            return (UpdatingState) super.asAdded();
        }

        /**
         * Calls {@link #asError(String)} with empty debugging message.
         * @return <code>this</code> for chaining calls.
         */
        @Override
        public UpdatingState asError() {
            return asError( "");
        }
        /**
         * Set this state as {@link #ERROR}.
         * This method forces {@link #getOldItem()} and  {@link #getNewItem()} to <code>null</code>.
         * @param debug a debbugging string logged through {@link SITBase#logERROR(Object)}
         * @return <code>this</code> for chaining calls.
         */
        public UpdatingState asError( String debug) {
            this.i0 = null;
            this.i1 = null;
            logError( "  ##### ERROR on updating new shape. " + debug);
            return (UpdatingState) super.asError();
        }

        /**
         * Do not use directly this method, use {@link #asNotChanged(Item)} instead.
         * This method set {@link #getState()} to {@link #NOT_CHANGED},
         * but does not affect {@link #getOldItem()} and {@link #getNewItem()}.
         * @return <code>this</code> for chaining calls.
         */
        @Override @Deprecated
        public UpdatingState asNotChanged() {
            logWarning( "  ##### you should not use deprecated methods UpdatingState.asNotChanged().");
            return (UpdatingState) super.asNotChanged();
        }
        /**
         * Set this state as {@link #NOT_CHANGED} and take a snapshot of the tracked {@link Item}.
         * You should pass {@link Item#copy()} to this method.
         * @param obj the tracked item that has been not changed, equal for
         *               {@link #getOldItem()} and {@link #getNewItem()}.
         * @return <code>this</code> for chaining calls.
         */
        public UpdatingState asNotChanged( Item obj) {
            this.i0 = obj;
            this.i1 = obj;
            log( "  ##### Scanning but NOT CHANGING Shape: " + i0 + "\t###\told Shape: " + i1);
            return (UpdatingState) super.asNotChanged();
        }

        /**
         * Set this state as {@link #INCONSISTENT}.
         * This method forces {@link #getOldItem()} and  {@link #getNewItem()} to <code>null</code>.
         * @return <code>this</code> for chaining calls.
         */
        @Override
        public UpdatingState asInconsistent() {
            this.i0 = null;
            this.i1 = null;
            return (UpdatingState) super.asInconsistent();
        }

        /**
         * @return <code>true</code> if this operation did update the shape of a tracked {@link Item}.
         * Namely, <code>return {@link #state} == {@link #AVERAGED};</code>.
         */
        public boolean isAveraged() {
            return state == AVERAGED;
        }
        /**
         * @return <code>true</code> if this operation did remove a tracked {@link Item}.
         * Namely, <code>return {@link #state} == {@link #AVERAGED};</code>.
         */
        public boolean isRemoved() {
            return state == REMOVED;
        }

        /** @return the snapshot given during state evaluation as <code>'old'</code> tracked item. */
        public Item getOldItem() {
            return i0;
        }
        /** @return the snapshot given during state evaluation as <code>'new'</code> tracked item. */
        public Item getNewItem() {
            return i1;
        }

        @Override
        public String toString() {
            String items = "{old:" + i0 + ", new:" + i1 +"}";
            if (isInconsistent())
                return "INCONSISTENT " + items;
            if (isNotChanged())
                return " NOT CHANGED " + items;
            if (isAdded())
                return "     ADDED   " + items;
            if (isUpdated())
                return "   UPDATED   " + items;
            if(isRemoved())
                return "   REMOVED   " + items;
            if(isAveraged())
                return "   AVERAGED  " + items;
            return "   ERROR  " + items;
        }
    }
}
