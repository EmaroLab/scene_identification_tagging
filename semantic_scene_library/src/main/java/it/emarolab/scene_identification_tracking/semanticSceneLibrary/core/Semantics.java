package it.emarolab.scene_identification_tracking.semanticSceneLibrary.core;

import com.google.common.base.Objects;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.Base;
import org.apache.commons.lang3.reflect.Typed;

import java.util.*;

import static it.emarolab.scene_identification_tracking.semanticSceneLibrary.Base.Logger.LOG;
import static it.emarolab.scene_identification_tracking.semanticSceneLibrary.Base.Logger.logERROR;
import static it.emarolab.scene_identification_tracking.semanticSceneLibrary.Base.Logger.logWARNING;
import static java.lang.Math.toIntExact;

/**
 * The definition of the mapping between a Java object and a semantic structure.
 * <p>
 *     General interface that puts the base for mapping a Java object into the
 *     ontology ({@code write}) and from the semantic representation to the Java object ({@code read}).
 *     <br>
 *     The actual behaviour of those methods depends on the type of semantic defined by the relative
 *     sets of {@link Descriptor}. Thus, for generic purposes also a separate methods for
 *     {@code delete} a semantic knowledge is required.
 *     <br>
 *     All changes are tracked through the {@link ReadingState} and {@link WritingState} classes.
 *
 * <div style="text-align:center;"><small>
 * <b>File</b>:        {@link Semantics} <br>
 * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
 * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
 * <b>affiliation</b>: DIBRIS, EMAROLab, University of Genoa. <br>
 * <b>date</b>:        04/02/2017 <br>
 * </small></div>
 *
 * @see Descriptor
 * @see Base
 * @see MappingState
 */
public interface Semantics extends Base{

    // [[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[  SEMANTIC INTERFACE  ]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]
    //                                             todo add learner
    /**
     * Reads the object characteristics from the semantic structure (map from semantic).
     * @return the state of the reading process.
     */
    MappingTransitions readSemantic();
    /**
     * Writes the object characteristics in the semantic structure (map to semantic).
     * @return the state of the writing process.
     */
    MappingTransitions writeSemantic();
    /**
     * Delete all the references of the object characteristics from the semantic structure.
     */
    MappingTransitions deleteSemantic();


    // [[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[   SEMANTIC CHANGES   ]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]
// todo add string and equals method (general comment ;) )
    class MappingTransitions extends ArrayList<MappingIntent<?,?,?,? extends MappingState>>{

        public MappingTransitions(int initialCapacity) {
            super(initialCapacity);
        }

        public MappingTransitions() {
        }
        public MappingTransitions(Collection<? extends MappingIntent<?, ?, ?, ? extends MappingState>> c) {
            super(c);
        }
        public MappingTransitions(MappingIntent<?, ?, ?, ? extends MappingState> intent) {
            super();
            add( intent);
        }

        // todo add merge policy for different state types
        public MappingState merge(){ // tested only on same MappingStates
            sort();
            String info = "Merging mapping states: [";
            if( ! isEmpty()){
                MappingState state = (MappingState) get(0).getState().copy();
                info += state;// + "[" + state.getTypeName() + "]";
                if ( size() > 1)
                    for( int i = 1; i < size(); i++) {
                        MappingState stateJ = (MappingState) get(i).getState().copy();
                        info += "~" + stateJ;// + "[" + stateJ.getTypeName() + "]";
                        state = stateJ.merge(state);
                        info += " = " + state;// + "[" + state.getTypeName() + "]";
                    }
                LOG( info + "]");
                return state;
            }
            LOG( info + "]");
            logWARNING( "Cannot merge transitions with empty elements");
            return null;
        }

        public void sort() { // by time
           Collections.sort(this);
        }

        @Override
        public String toString() {

            List<MappingIntent> out = new ArrayList<>(this);
            Collections.sort( out);

            String info = "Mapping transition intents: {";
            String merged = LOGGING.MSG_EMPTY;
            if (! isEmpty()) {
                info += LOGGING.NEW_LINE;
                int cnt = 0;
                for (MappingIntent<?, ?, ?, ?> i : out) {
                    if (++cnt < size())
                        info += "\t\t" + i.toString() + "," + LOGGING.NEW_LINE;
                    else info += "\t\t" + i + LOGGING.NEW_LINE;
                }

                if (size() == 1)
                    merged = get( 0).getState().toString();
                else merged = merge().toString();
            }
            info += "}";
            return info + " merged state: " + merged;
        }


    }
    class MappingIntent<I,A,L, M extends MappingState> extends SITBase implements Comparable<MappingIntent<?,?,?,?>>{

        private long time = System.currentTimeMillis();
        private String description;
        private M state;
        private I instance;
        private A atom;
        private L javaValue, semanticValue;

        public MappingIntent(I instance, A atom, M state){
            this.instance = instance;
            this.atom = atom;
            this.state = state;
        }
        // todo add meaningfull constructors
        public MappingIntent(MappingIntent<I,A,L,M> copy){
            this.time = copy.time;
            this.description = copy.description;
            this.state.state = copy.state.state;
            this.instance = copy.instance;
            this.atom = copy.atom;
            this.javaValue = copy.javaValue;
            this.semanticValue = copy.semanticValue;
        }

        @Override
        public MappingIntent<I,A,L,M> copy() {
            return new MappingIntent<>( this);
        }

        public void setDescription(String description) {
            this.description = description;
        }
        public void setJavaValue(L javaValue) {
            this.javaValue = javaValue;
        }
        public void setSemanticValue(L semanticValue) {
            this.semanticValue = semanticValue;
        }
        public long getTime() {
            return time;
        }
        public String getDescription() {
            return description;
        }
        public M getState() {
            return state;
        }
        public I getInstance() {
            return instance;
        }
        public A getAtom() {
            return atom;
        }
        public L getJavaValue() {
            return javaValue;
        }
        public L getSemanticValue() {
            return semanticValue;
        }

        public String toStringDescription(){
            return "{" + padString( getState().getTypeName() + "-" + getDescription(), LOGGING.LENGTH_MEDIUM, true) + "}  ";
        }
        public String toStringStates(){
            return "[" + padString( getState().toString(), LOGGING.LENGTH_SHORT, false) + "]  ";
        }
        public String toStringAxioms(){
            String out = padString( instance.toString(), LOGGING.LENGTH_NUMBER, true);
            if (getAtom() != null)
                if ( ! getAtom().equals( LOGGING.MSG_EMPTY))
                    out += "." + getAtom();
            if ( getJavaValue() == null)
                out += "(j,";
            else out += "(j:" + getJavaValue() + ", ";
            if ( getSemanticValue() == null)
                out += "s)";
            else out += "s:" + getSemanticValue() + ")";
            return out;
        }

        @Override
        public String toString() {
            return toStringDescription() + toStringStates() + toStringAxioms();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof MappingIntent)) return false;
            MappingIntent<?, ?, ?, ?> mIntent = (MappingIntent<?, ?, ?, ?>) o;
            return Objects.equal(getState(), mIntent.getState()) &&
                    Objects.equal(getInstance(), mIntent.getInstance()) &&
                    Objects.equal(getAtom(), mIntent.getAtom()) &&
                    Objects.equal(getJavaValue(), mIntent.getJavaValue()) &&
                    Objects.equal(getSemanticValue(), mIntent.getSemanticValue());
        }
        @Override
        public int hashCode() {
            return Objects.hashCode(getState(), getInstance(), getAtom(), getJavaValue(), getSemanticValue());
        }

        @Override
        public int compareTo(MappingIntent<?,?,?,?> o) {
            return toIntExact( time - o.time);
        }
    }

    /**
     * Describes the common states between reading and writing from semantic.
     * <p>
     *     In particular, this class manages the states:<ul>
     *        <li> <b>ERROR</b>: java error occurs while mapping semantic,
     *        <li> <b>INCONSISTENT</b>: generated by the semantic reasoner or inconsistent Java computations,
     *        <li> <b>NOT_CHANGED</b>: the mapping between semantic and
     *             java-structures is up to date. This operation did not produced any changes.
     *     </ul>
     * <p>
     *     <b>REMARK</b>: extending classes must have static state fields
     *     assigned to inter values bigger than {@link #NOT_CHANGED}.
     *
     * <div style="text-align:center;"><small>
     * <b>File</b>:        {@link Semantics} <br>
     * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
     * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
     * <b>affiliation</b>: DIBRIS, EMAROLab, University of Genoa. <br>
     * <b>date</b>:        04/02/2017 <br>
     * </small></div>
     *
     * @see STATEMAPPING
     * @see WritingState
     * @see ReadingState
     * @see TryMap
     * @see Descriptor
     */
    abstract class MappingState extends Base.SITBase {

        /**
         * The internal state of this instance.
         */
        protected int state;

        /**
         * Copy constrcutors, it creates a new object by coping all its fields.
         * This method should use {@code super} hierarchy constrcutors
         * and it is called by {@link #copy()}.
         * @param copy the state to copy.
         */
        public MappingState( MappingState copy){
            super();
            this.state = copy.state;
        }
        /**
         * Constructs this object with a given state.<br>
         * The input parameter must be:
         * <code> {@link #state} &gt;= {@link #ERROR} &amp; {@link #state} &lt;= {@link #NOT_CHANGED} </code>.
         * Otherwise, the {@link #state} will be set {@link #asInconsistent()}.
         * @param state the outcome of a semantic mapping (read/write) operation.
         */
        public MappingState(int state) {
            if (state >= STATEMAPPING.ERROR & state <= STATEMAPPING.NOT_CHANGED)
                this.state = state;
            else state = STATEMAPPING.INITIALISED;
        }
        /**
         * Empty constructor, initialises this {@link #state} {@link #asInconsistent()} and assign a debugging message.
         */
        public MappingState() {
            state = STATEMAPPING.INITIALISED;
        }

        /** @return the {@link #state} assigned to this semantic mapping operation. */
        public int getState() {
            return state;
        }

        /**
         * set this state as {@link #ERROR}
         * @return <code>'this'</code>, for chaining calls.
         */
        public MappingState asError() {
            this.state = STATEMAPPING.ERROR;
            return this;
        }

        /**
         * set this state as {@link #NOT_CHANGED}
         * @return <code>'this'</code>, for chaining calls.
         */
        public MappingState asNotChanged() {
            this.state = STATEMAPPING.NOT_CHANGED;
            return this;
        }

        /**
         * set this state as {@link #INCONSISTENT}
         * @return <code>'this'</code>, for chaining calls.
         */
        public MappingState asInconsistent() {
            this.state = STATEMAPPING.INCONSISTENT;
            return this;
        }

        /**
         * @return <code>true</code> if <code>{@link #state} &gt; 0</code>.
         * Namely, if {@link #state} is not {@link #ERROR} or {@link #INCONSISTENT}.
         */
        public boolean isOK() {
            return state > STATEMAPPING.INCONSISTENT;
        }

        public boolean isInitialised() {
            return state == STATEMAPPING.INITIALISED;
        }

        /**
         * @return <code>true</code> if this operation produces an error.
         * Namely, <code>return {@link #state} == {@link #ERROR}</code>.
         */
        public boolean isError() {
            return state == STATEMAPPING.ERROR;
        }

        /**
         * @return <code>true</code> if this operation did not change nothing.
         * Namely, <code>return {@link #state} == {@link #NOT_CHANGED};</code>.
         */
        public boolean isNotChanged() {
            return state == STATEMAPPING.NOT_CHANGED;
        }

        /**
         * @return <code>true</code> if this operation brings the semantic on inconsistency.
         * Namely, <code>return {@link #state} == {@link #INCONSISTENT}</code>.
         */
        public boolean isInconsistent() {
            return state == STATEMAPPING.INCONSISTENT;
        }

        /**
         * Merges an input state with this state and return a combined feasible state.<br>
         * It is based on {@link WritingState#combineResults( WritingState, WritingState)}
         * with <code>'this'</code> and <code>'otherResult</code> as input
         * parameter respectively.
         * @param otherResult the state to be merged with <code>'this'</code>.
         * @return a new state that comes from the combination of <code>'this'</code>
         * and the input parameter.
         */
        abstract public MappingState merge( MappingState otherResult);/* {
            if (otherResult != null)
                combineResults(this, otherResult);  // set always 'this' as first parameter
            return this;
        }*/

        /**
         * Merges two writings states and generates an output feasible state.
         * <p>In particular, it returns:<ul>
         *    <li> {@link #INCONSISTENT}: if at lest one state is in inconsistent state,
         *    <li> {@link #ERROR}: if at least one state is in error state,
         *    <li> {@link #NOT_CHANGED}: if both did not change nothing.
         *                          If only one input parameter is in {@link #NOT_CHANGED} state, it returns the other.
         * </ul><p>
         * @param r1 the first writing state to merge (it should be {@code this} object).
         * @param r2 the second writing state to merge (it should be the incoming object).
         * @return a new writing state obtained from the combination of the input parameters.
         */
        public static MappingState combineResults( MappingState r1, MappingState r2) {

            if (r1.isInitialised())
                return r2;
            else if (r2.isInitialised())
                return r1;

            // high priority for inconsistent
            MappingState inc = checkInconsistency( r1, r2);
            if( inc != null)
                return inc;

            // quite high priority for errors
            MappingState err = checkError( r1, r2);
            if ( err != null)
                return err;

            MappingState notC = checkNotChanged(r1, r2);
            if ( notC != null)
                return r2.asNotChanged();
            return notC;
        }
        /**
         * Returns an {@link #INCONSISTENT} state if both the inputs are inconsistent.
         * Otherwise it returns an {@link #isOK()} status.
         * @param r1 the first state to check for inconsistency (it should be {@code this} object).
         * @param r2 the second state to check for inconsistency (it should be the incoming object).
         * @return an inconsistent state if both inputs are inconsistent, an {@code OK} state otherwise.
         */
        protected static MappingState checkInconsistency( MappingState r1, MappingState r2){
            if (r1.isInconsistent() & r2.isInconsistent()) // !!!!!!!!
                return r2;
            else { // otherwise return the other
                if (r1.isInconsistent())
                    return r2;
                if (r2.isInconsistent()) {
                    return r1;
                }
            }
            return null;  // cannot take a decision now
        }
        /**
         * Returns an {@link #ERROR} state if both the inputs are describing an error.
         * Otherwise it returns an {@link #isOK()} status.
         * @param r1 the first state to check for errors (it should be {@code this} object).
         * @param r2 the second state to check for errors (it should be the incoming object).
         * @return an error state if both inputs are inconsistent, an {@code OK} state otherwise.
         */
        protected static MappingState checkError( MappingState r1, MappingState r2){
            if ( r1.isError() | r2.isError())
                return r2;
            return null;  // cannot take a decision now
        }
        /**
         * Returns an {@link #NOT_CHANGED} state if both the inputs are up to dated.
         * Otherwise returns the state that has been changed or {@link #INCONSISTENT}
         * if no one of the inputs are in {@link #NOT_CHANGED} status.
         * @param r1 the first state to check for out of date (it should be {@code this} object).
         * @param r2 the second state to check for out of date (it should be the incoming object).
         * @return an up to date state if both inputs are not changed, the other
         * state if only one is up to date or an inconsistency state otherwise.
         */
        protected static MappingState checkNotChanged( MappingState r1, MappingState r2){
            // set as not changed if both did not change
            if (r1.isNotChanged() & r2.isNotChanged())
                return r2; // NOT CHANGED
            else { // otherwise return the other
                if (r1.isNotChanged())
                    return r2;
                if (r2.isNotChanged()) {
                    return r1;
                }
            }
            return null; // cannot take a decision now
        }

        public String getTypeName(){
            return LOGGING.STATE_TYPE_MAPPING;
        }

        /*@Override
        public MappingState copy() {
            return new MappingState( this);
        }*/

        /**
         * Consider equal states by check for the equal {@link #state}.
         * @param o an Mapping state to check for equality with rescpect to {@code this} state.
         * @return {@code true} if the input parameter and {@ceode this} object are equal.
         *         {@code False} otherwise.
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof MappingState)) return false;
            MappingState that = (MappingState) o;
            return getState() == that.getState();
        }
        @Override
        public int hashCode() {
            return Objects.hashCode(getState());
        }

        @Override
        public String toString() {
            if (isNotChanged())
                return LOGGING.STATE_NOT_CHANGED;
            if (isInconsistent())
                return LOGGING.STATE_INCONSISTENT;
            if (isInitialised())
                return LOGGING.STATE_INITIALISED;
            return LOGGING.STATE_ERROR;
        }

    }

    /**
     * This class implements the possible states for a semantic writing process.
     * <p>
     *     In particular, this class extend {@link MappingState} by adding more states,
     *     in details:<ul>
     *         <li> {@link #ERROR}: implemented in {@link MappingState},
     *         <li> {@link #INCONSISTENT}: implemented in {@link MappingState},
     *         <li> {@link #NOT_CHANGED}: implemented in {@link MappingState},
     *         <li> <b>ADDED</b>: the writing operation produced something new in the semantic description,
     *         <li> <b>UPDATED</b>: the writing operation changes some semantic.
     *         <li> <b>REMOVED</b>: the writing operation removes some semantic.
     *     </ul>
     *
     * <p>
     *     <b>REMARK</b>: extending classes must have static state fields
     *     assigned to inter values bigger than {@link #REMOVED}.
     *
     * <div style="text-align:center;"><small>
     * <b>File</b>:        {@link Semantics} <br>
     * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
     * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
     * <b>affiliation</b>: DIBRIS, EMAROLab, University of Genoa. <br>
     * <b>date</b>:        04/02/2017 <br>
     * </small></div>
     *
     * @see STATEMAPPING
     * @see MappingState
     * @see #writeSemantic()
     * @see #deleteSemantic()
     * @see Descriptor
     */
    class WritingState extends MappingState {

        /**
         * Copy constrcutors, it creates a new object by coping all its fields.
         * This method should use {@code super} hierarchy constrcutors
         * and it is called by {@link #copy()}.
         * @param copy the state to copy.
         */
        public WritingState( MappingState copy){
            super( copy);
        }
        /**
         * Constructs this object with a given state.<br>
         * The input parameter must be:
         * <code> {@link #state} &gt;= {@link #ERROR} &amp; {@link #state} &lt;= {@link #NOT_CHANGED} </code>.
         * Otherwise, the {@link #state} will be set {@link #asInconsistent()}.
         *
         * @param state the outcome of a semantic mapping (read/write) operation.
         */
        public WritingState(int state) {
            super();
            if (state >= STATEMAPPING.ERROR & state <= STATEMAPPING.REMOVED)
                this.state = state;
        }
        /**
         * Empty constructor, initialises this {@link #state} {@link #asInconsistent()} and assign a debugging message.
         */
        public WritingState() {
            super();
        }


        /**
         * set this state as {@link #ADDED}
         * @return <code>'this'</code>, for chaining calls.
         */
        public WritingState asAdded() {
            this.state = STATEMAPPING.ADDED;
            return this;
        }
        /**
         * set this state as {@link #UPDATED}
         * @return <code>'this'</code>, for chaining calls.
         */
        public WritingState asUpdated() {
            this.state = STATEMAPPING.UPDATED;
            return this;
        }
        /**
         * set this state as {@link #REMOVED}
         * @return <code>'this'</code>, for chaining calls.
         */
        public WritingState asRemoved() {
            this.state = STATEMAPPING.REMOVED;
            return this;
        }

        @Override
        public WritingState asError() {
            return (WritingState) super.asError();
        }

        @Override
        public WritingState asNotChanged() {
            return (WritingState) super.asNotChanged();
        }

        @Override
        public WritingState asInconsistent() {
            return (WritingState) super.asInconsistent();
        }

        /**
         * @return <code>true</code> if the writing operation adds something in the semantic.
         * Namely, <code>return {@link #state} == {@link #ADDED}</code>.
         */
        public boolean isAdded() {
            return state == STATEMAPPING.ADDED;
        }
        /**
         * @return <code>true</code> if this operation change the semantic of something.
         * Namely, <code>return {@link #state} == {@link #UPDATED}</code>.
         */
        public boolean isUpdated() {
            return state == STATEMAPPING.UPDATED;
        }
        /**
         * @return <code>true</code> if this operation removes the semantic of something.
         * Namely, <code>return {@link #state} == {@link #REMOVED}</code>.
         */
        public boolean isRemoved() {
            return state == STATEMAPPING.REMOVED;
        }

        /**
         * Merges an input state with this state and return a combined feasible state.<br>
         * It is based on {@link WritingState#combineResults( WritingState, WritingState)}
         * with <code>'this'</code> and <code>'otherResult</code> as input
         * parameter respectively.
         * @param otherResult the state to be merged with <code>'this'</code>.
         * @return a new state that comes from the combination of <code>'this'</code>
         * and the input parameter.
         */
        @Override
        public WritingState merge( MappingState otherResult) {
            if (otherResult != null) {
                try {
                    return new WritingState( combineResults(this, (WritingState) otherResult));  // set always 'this' as first parameter
                } catch ( ClassCastException e){
                    logWARNING( "you should not merge " + otherResult.getClass().getSimpleName() + " with Reading states");
                    return new WritingState( combineResults(this, otherResult));  // set always 'this' as first parameter
                }
            }
            return this;

            /*if (otherResult != null)
                    combineResults(this, (WritingState) otherResult);  // set always 'this' as first parameter
            return this;*/
        }

        /**
         * Merges two writings states and generates an output feasible state.
         * <p>In particular, it returns:<ul>
         *    <li> {@link #INCONSISTENT}: if at lest one state is in inconsistent state,
         *    <li> {@link #ERROR}: if at least one state is in error state,
         *    <li> {@link #NOT_CHANGED}: if both did not change nothing.
         *                          If only one input parameter is in {@link #NOT_CHANGED} state, it returns the other.
         *    <li> {@link #UPDATED}:  if both update something in the semantic.
         *                          If only one input parameter is in {@link #UPDATED} state, it returns the other.
         *    <li> {@link #REMOVED}: if both removed something in the semantic.
         *                          If only one input parameter is in {@link #REMOVED} state, it returns the other.
         *    <li> {@link #ADDED}: if is not one of the case above.
         * </ul><p>
         * @param r1 the first writing state to merge.
         * @param r2 the second writing state to merge.
         * @return a new writing state obtained from the combination of the input parameters.
         */
        public static WritingState combineResults( WritingState r1, WritingState r2) {

            if (r1.isInitialised())
                return r2;
            else if (r2.isInitialised())
                return r1;

            // todo check for REMOVED

            // high priority for inconsistent
            WritingState inc = (WritingState) checkInconsistency( r1, r2);
            if( inc != null)
                return inc;

            // quite high priority for errors
            WritingState err = (WritingState) checkError( r1, r2);
            if ( err != null)
                return err;

            // set as not changed if both did not change
            WritingState notC = (WritingState) checkNotChanged(r1, r2);
            if ( notC != null)
                return notC;

            // set as update if both have been updated
            WritingState up = checkUpdated( r1, r2);
            if( up.isOK())
                return up;

            // set as removed if both have been removed
            WritingState re = checkRemoved( r1, r2);
            if( re.isOK())
                return re;

            if( ( r1.isInconsistent() | r1.isNotChanged()) & ( r2.isInconsistent() | r2.isNotChanged()))
                return r2.copy().asNotChanged();

            // set as added for the other case (added v.s. {not changed, updated, added})
            return r2.copy().asAdded(); // ADDED
        }
        /**
         * Returns a {@link #UPDATED} state if both the inputs are updated.
         * Otherwise it returns the state that is not up dated. Finally, it returns
         * {@link #INCONSISTENT} if no one of the inputs have been updated
         * @param r1 the first state to check for out of date
         * @param r2 the second state to check for out of date
         * @return an updated state if both inputs are updated, the other
         * state if only one is updated or an inconsistency state otherwise.
         */
        protected static WritingState checkUpdated( WritingState r1, WritingState r2){
            WritingState out = r2.copy();
            if (r1.isUpdated() & r2.isUpdated())
                return out; // UPDATED
            else { // otherwise return the other
                if (r1.isUpdated())
                    return out;
                if (r2.isUpdated()) {
                    out.state = r1.state;
                    return out;
                }
            }
            return out.asInconsistent(); // INCONSISTENCY (should not happen)
        }
        /**
         * Returns a {@link #REMOVED} state if both the inputs are removed.
         * Otherwise it returns the state that is not removed. Finally, it returns
         * {@link #INCONSISTENT} if no one of the inputs have been removed.
         * @param r1 the first state to check for out of date
         * @param r2 the second state to check for out of date
         * @return an updated state if both inputs are removed, the other
         * state if only one is removed or an inconsistency state otherwise.
         */
        protected static WritingState checkRemoved( WritingState r1, WritingState r2){
            WritingState out = r2.copy();
            if (r1.isRemoved() | r2.isRemoved())
                return out.asRemoved(); // REMOVED
            else { // otherwise return the other
                if (r1.isRemoved())
                    return out;
                if (r2.isRemoved()) {
                    out.state = r1.state;
                    return out;
                }
            }
            return out.asInconsistent(); // INCONSISTENCY (should not happen)
        }

        @Override
        public String getTypeName(){
            return LOGGING.STATE_TYPE_WRITE;
        }

        @Override
        public WritingState copy() {
            return new WritingState( this);
        }

        @Override
        public String toString() {
            String sup = super.toString();
            if ( ! sup.equals( LOGGING.STATE_ERROR))
                return sup;
            if (isAdded())
                return LOGGING.STATE_ADDED;
            if (isUpdated())
                return LOGGING.STATE_UPDATED;
            if (isRemoved())
                return LOGGING.STATE_REMOVED;
            return LOGGING.STATE_ERROR;
        }
    }

    /**
     * This class implements the possible states for a semantic reading process.
     * <p>
     *     In particular, this class extend {@link MappingState} by adding more states,
     *     in details:<ul>
     *         <li> {@link #ERROR}: implemented in {@link MappingState},
     *         <li> {@link #INCONSISTENT}: implemented in {@link MappingState},
     *         <li> {@link #NOT_CHANGED}: implemented in {@link MappingState},
     *         <li> <b>ABSENT</b>: reading failure, since not data were available in the semantic description,
     *         <li> <b>SUCCESS</b>: reading success, java representation has been up to dated.
     *     </ul>
     *
     * <p>
     *     <b>REMARK</b>: extending classes must have static state fields
     *     assigned to inter values bigger than {@link #SUCCESS}.
     *
     * <div style="text-align:center;"><small>
     * <b>File</b>:        {@link Semantics} <br>
     * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
     * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
     * <b>affiliation</b>: DIBRIS, EMAROLab, University of Genoa. <br>
     * <b>date</b>:        04/02/2017 <br>
     * </small></div>
     *
     * @see STATEMAPPING
     * @see MappingState
     * @see #readSemantic()
     * @see Descriptor
     */
    class ReadingState extends MappingState {

        /**
         * Copy constrcutors, it creates a new object by coping all its fields.
         * This method should use {@code super} hierarchy constrcutors
         * and it is called by {@link #copy()}.
         * @param copy the state to copy.
         */
        public ReadingState( MappingState copy){
            super( copy);
        }
        /**
         * Constructs this object with a given state.<br>
         * The input parameter must be:
         * <code> {@link #state} &gt;= {@link #ERROR} &amp; {@link #state} &lt;= {@link #NOT_CHANGED} </code>.
         * Otherwise, the {@link #state} will be set {@link #asInconsistent()}.
         *
         * @param state the outcome of a semantic mapping (read/write) operation.
         */
        public ReadingState(int state) {
            super();
            if (state >= ERROR | state <= SUCCESS)
                this.state = state;
        }
        /**
         * Empty constructor, initialises this {@link #state} {@link #asInconsistent()} and assign a debugging message.
         */
        public ReadingState() {
            super();
        }

        /**
         * set this state as {@link #ABSENT}
         * @return <code>'this'</code>, for chaining calls.
         */
        public ReadingState asAbsent() {
            this.state = STATEMAPPING.ABSENT;
            return this;
        }

        /**
         * set this state as {@link #SUCCESS}
         * @return <code>'this'</code>, for chaining calls.
         */
        public ReadingState asSuccess() {
            this.state = STATEMAPPING.SUCCESS;
            return this;
        }

        @Override
        public ReadingState asError() {
            return (ReadingState) super.asError();
        }

        @Override
        public ReadingState asNotChanged() {
            return (ReadingState) super.asNotChanged();
        }

        @Override
        public ReadingState asInconsistent() {
            return (ReadingState) super.asInconsistent();
        }

        /**
         * @return <code>true</code> if the readings fails since the no data has been found.
         * Namely, <code>return {@link #state} == {@link #ABSENT}</code>.
         */
        public boolean isAbsent() {
            return state == STATEMAPPING.ABSENT;
        }

        /**
         * @return <code>true</code> if the read operation was successes and
         * java representation has been up to dated.
         * Namely, <code>return {@link #state} == {@link #SUCCESS}</code>.
         */
        public boolean isSuccess() {
            return state == STATEMAPPING.SUCCESS;
        }

        /**
         * Merges an input state with this state and return a combined feasible state.<br>
         * It is based on {@link ReadingState#combineResults( ReadingState, ReadingState)}
         * with <code>'this'</code> and <code>'otherResult</code> as input
         * parameter respectively.
         * @param otherResult the state to be merged with <code>'this'</code>.
         * @return a new state that comes from the combination of <code>'this'</code>
         * and the input parameter.
         */
        @Override
        public ReadingState merge( MappingState otherResult) {
            if (otherResult != null)
                try {
                    return new ReadingState( combineResults(this, (ReadingState) otherResult));  // set always 'this' as first parameter
                } catch ( ClassCastException e){
                    logWARNING( "you should not merge " + otherResult.getClass().getSimpleName() + " with Reading states");
                    return new ReadingState( combineResults(this, otherResult));
                }
            return this;

            /*if (otherResult != null)
                combineResults(this, otherResult);  // set always 'this' as first parameter
            return this;*/
        }

        /**
         * Merges two reading states and generates an output feasible state.
         * <p>In particular, it returns:<ul>
         *    <li> {@link #INCONSISTENT}: if at lest one state is in inconsistent state,
         *    <li> {@link #ERROR}: if at least one state is in error state,
         *    <li> {@link #NOT_CHANGED}: if both did not change nothing.
         *                          If only one input parameter is in {@link #NOT_CHANGED} state, it returns the other.
         *    <li> {@link #ABSENT}:  if at least one state is in absent state,
         *    <li> {@link #SUCCESS}: if is not one of the case above.
         * </ul>
         * @param r1 the first reading state to merge.
         * @param r2 the second reading state to merge.
         * @return a new reading state obtained from the combination of the input parameters.
         */
        public static ReadingState combineResults( ReadingState r1, ReadingState r2) {
            if (r1.isInitialised())
                return r2;
            else if (r2.isInitialised())
                return r1;

            // todo check for REMOVED

            // high priority for inconsistent
            ReadingState inc = (ReadingState) checkInconsistency( r1, r2);
            if( inc != null)
                return inc;

            // quite high priority for errors
            ReadingState err = (ReadingState) checkError( r1, r2);
            if ( err != null)
                return err;

            // set as not changed if both did not change
            ReadingState notC = (ReadingState) checkNotChanged(r1, r2);
            if ( notC != null)
                return notC;

            // set as absent if both have been updated
            ReadingState abse = checkAbsent( r1, r2);
            if( abse.isOK())
                return abse;

            if( ( r1.isInconsistent() | r1.isNotChanged()) & ( r2.isInconsistent() | r2.isNotChanged()))
                return r2.copy().asNotChanged();

            // set as success for the other case (success v.s. success})
            return r2.copy().asSuccess(); // SUCCESS
        }
        /**
         * Returns an {@link #ABSENT} state if both the inputs are in an absent states.
         * Otherwise, it returns an {@link #INCONSISTENT} state.
         * @param r1 the first state to check for absent state
         * @param r2 the second state to check for absent state
         * @return an absent state if both inputs are updated,
         * or an inconsistency state otherwise.
         */
        protected static ReadingState checkAbsent( ReadingState r1, ReadingState r2){
            ReadingState out = r2.copy();
            if (r1.isAbsent() | r2.isAbsent())
                return out.asAbsent(); // ABSENT
            else
                return out.asInconsistent(); // INCONSISTENCY (should not happen)
        }

        @Override
        public String getTypeName(){
            return LOGGING.STATE_TYPE_READ;
        }

        @Override
        public ReadingState copy(){
            return new ReadingState ( this);
        }

        @Override
        public String toString() {
            String sup = super.toString();
            if ( ! sup.equals( LOGGING.STATE_ERROR))
                return sup;
            if (isSuccess())
                return LOGGING.STATE_SUCCESS;
            if (isAbsent())
                return LOGGING.STATE_ABSENT;
            return LOGGING.STATE_ERROR;
        }
    }



    // [[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[ DESCRIPTOR INTERFACE ]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]
    //                                              todo: add other custom descriptors
    //                                              (e.g.: sub/super data/object property, stochastic populations, force fields etc.)
    /**
     * The basic interface for mapping knowledge in the ontology.
     * <p>
     *     This interface gives the basic fon an object that can manipulate the ontology
     *     by synchronising java and semantic representations.
     *     Its manipulations can be tracked though the returned {@link MappingState}
     *     and it should use the {@link Base.TryMap} helper.
     *     <br>
     *     It is generic enough to consider different semantic,
     *     e.g.: OWL, MongoDB, Fuzzy etc.
     * <p>
     *     In particular, this interface defines that a semantic description
     *     should container a reference to the {@code ontology} in which semantically
     *     map an {@code instance}. Extensions of this interface should create
     *     a reference to the semantic structure and define the type of instance to map.
     *     <br>
     *     Base on those, they should also implements the methods for write
     *     and read the different chunks of knowledge.
     *
     * <div style="text-align:center;"><small>
     * <b>File</b>:        {@link Semantics} <br>
     * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
     * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
     * <b>affiliation</b>: DIBRIS, EMAROLab, University of Genoa. <br>
     * <b>date</b>:        04/02/2017 <br>
     * </small></div>
     *
     * @param <O> the reference to the ontology.
     * @param <I> the instance describing a semantic chunk of knowledge in the ontology.
     *
     * @see Semantics
     * @see MappingState
     */
    interface Descriptor<O, I> extends Base {
        /**
         * The object able to manipulate the ontology to set for this semantic descriptor.
         * @param ontology the ontology to set
         * @return {@code false} if an error occurs, {@code true} otherwise
         */
        boolean setOntology( O ontology);
        /**
         * The instance describing a particular knowledge in the ontology.
         * @param instance the semantic instance to set
         * @return {@code false} if an error occurs, {@code true} otherwise
         */
        boolean setInstance( I instance);

        /**  @return the object able to manipulating a semantic structure. */
        O getOntology();
        /** @return the instance that describes a particular knowledge in the ontology. */
        I getInstance();

        /**
         * Writes the {@link #getInstance()} value into the {@code ontology}.
         * @return the states of the writing operations.
         */// @param debug a message to be propagated in the returning states.
        MappingTransitions writeInstance(); // not used in OWL API
        /**
         * Writes the {@link #getInstance()} value into the {@code ontology}
         * by calling {@link #writeInstance(String)}, with {@link LOGGING#MSG_EMPTY} message..
         * @return the states of the writing operations
         */// with empty debugging messages ({@link WritingState#getDescription()}).
        //default MTransiton writeInstance() {
        //    return writeInstance( LOGGING.MSG_EMPTY);
        //} // not used in OWL API

        /**
         * Reads the {@link #getInstance()} value into the {@code ontology}.
         * @return the states of the writing operations
         */
        MappingTransitions readInstance(); // not used in OWL API
        /**
         * Reads the {@link #getInstance()} value into the {@code ontology}
         * by calling {@link #readInstance(String)}, with {@link LOGGING#MSG_EMPTY} message..
         * @return the states of the writing operations.
         */ // with empty debugging messages ({@link WritingState#getDescription()}).
        //default MTransiton readInstance() {
        //    return readInstance( LOGGING.MSG_EMPTY);
        //} // not used in OWL API

        /**
         * Removes the {@link #getInstance()} value into the {@code ontology}.
         * @return the states of the writing operations.
         */
        MappingTransitions deleteInstance();
        /**
         * Removes the {@link #getInstance()} value into the {@code ontology}
         * by calling {@link #deleteInstance(String)}, with {@link LOGGING#MSG_EMPTY} message..
         * @return the states of the writing operations
         */// with empty debugging messages ({@link WritingState#getDescription()}).
        //default MTransiton deleteInstance() {
        //    return deleteInstance( LOGGING.MSG_EMPTY);
        //}
    }

    /**
     * The Semantic Descriptor for mapping hierarchy knowledge in the ontology.
     * <p>
     *     This interface extends {@link Descriptor} by giving
     *     a hierachy to the described {@code instance}.
     *     <br>
     *     More in details, its defines a {@link Set} of super {@code instances}
     *     and a {@link Set} of sub {@code instances}
     *     that describes a part of the hierarchy of the {@link #getInstance()} results.
     *
     * <div style="text-align:center;"><small>
     * <b>File</b>:        {@link Semantics} <br>
     * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
     * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
     * <b>affiliation</b>: DIBRIS, EMAROLab, University of Genoa. <br>
     * <b>date</b>:        04/02/2017 <br>
     * </small></div>
     *
     * @param <O> the reference to the ontology.
     * @param <I> the instance describing a semantic chunk of knowledge in the ontology.
     *
     * @see Semantics
     * @see MappingState
     * @see Descriptor
     */
    interface HierarchyDescriptor<O, I> extends Descriptor<O, I> {

        /** @return all the instances that are parents of {@link #getInstance()}. */
        Set<I> getSuperInstances();
        /** @return all the instances that are children of {@link #getInstance()}. */
        Set<I> getSubInstances();

        /**
         * Writes the {@link #getSuperInstances()} value into the {@code ontology}.
         * @return the states of the writing operations.
         */
        MappingTransitions writeSuperInstances();
        /**
         * Writes the {@link #getSuperInstances()} value into the {@code ontology}
         * by calling {@link #writeSuperInstances()}, with {@link LOGGING#MSG_EMPTY} message.
         * @return the states of the writing operations
         */// with empty debugging messages ({@link WritingState#getDescription()}).
        //default WritingState writeSuperInstances(){
         //   return writeSuperInstances( LOGGING.MSG_EMPTY);
        //}

        /**
         * Writes the {@link #getSubInstances()} values into the {@code ontology}.
         * @return the states of the writing operations.
         */
        MappingTransitions writeSubInstances();
        /**
         * Writes the {@link #getSubInstances()} values into the {@code ontology}
         * by calling {@link #writeSubInstances()}, with {@link LOGGING#MSG_EMPTY} message.
         * @return the states of the writing operations
         */// with empty debugging messages ({@link WritingState#getDescription()}).
        //default WritingState writeSubInstances(){
        //    return writeSubInstances( LOGGING.MSG_EMPTY);
        //}

        /**
         * Writes the {@link #getSuperInstances()} and the {@link #getSubInstances()}
         * values into the {@code ontology} by {@link WritingState#merge(MappingState)}ing
         * the results of {@link #writeSubInstances()} and {@link #writeSuperInstances()}..
         * @return the states of the writing operations.
         */
        default MappingTransitions writeHierarchy(){
            MappingTransitions state = writeSubInstances();
            state.addAll( writeSuperInstances());
            return state;
        }
        /**
         * Writes the {@link #getSuperInstances()} and the {@link #getSubInstances()}
         * by calling {@link #writeHierarchy( String)}, with {@link LOGGING#MSG_EMPTY} message.
         * @return the states of the writing operations
         */// with empty debugging messages ({@link WritingState#getDescription()}).
        //default WritingState writeHierarchy(){
        //    return writeHierarchy( LOGGING.MSG_EMPTY);
        //}

        /**
         * Reads the {@link #getSuperInstances()} values into the {@code ontology}.
         * @return the states of the reading operations.
         */
        MappingTransitions readSuperInstances();
        /**
         * Reads the {@link #getSuperInstances()} values into the {@code ontology}
         * by calling {@link #readSuperInstances()}, with {@link LOGGING#MSG_EMPTY} message.
         * @return the states of the writing operations
         */// with empty debugging messages ({@link WritingState#getDescription()}).
        //default ReadingState readSuperInstances(){
        //    return readSuperInstances( LOGGING.MSG_EMPTY);
        //}

        /**
         * Reads the {@link #getSubInstances()} values into the {@code ontology}.
         * @return the states of the reading operations.
         */
        MappingTransitions readSubInstances();
        /**
         * Reads the {@link #getSubInstances()} values into the {@code ontology}
         * by calling {@link #readSubInstances()}, with {@link LOGGING#MSG_EMPTY} message.
         * @return the states of the writing operations
         */
        //default ReadingState readSubInstances(){
        //    return readSubInstances( LOGGING.MSG_EMPTY);
        //}

        /**
         * Reads the {@link #getSuperInstances()} and the {@link #getSubInstances()}
         * values into the {@code ontology} by {@link ReadingState#merge(MappingState)}ing
         * the results of {@link #readSubInstances()} and {@link #readSuperInstances()}.
         * @return the states of the writing operations.
         */
        default MappingTransitions readHierarchy(){
            MappingTransitions state = readSubInstances();
            state.addAll( readSuperInstances());
            return state;
        }
        /**
         * Reads the {@link #getSuperInstances()} and the {@link #getSubInstances()}
         * by calling {@link #readHierarchy( String)}, with {@link LOGGING#MSG_EMPTY} message.
         * @return the states of the writing operations
         */// with empty debugging messages ({@link WritingState#getDescription()}).
        //default ReadingState readHierarchy(){
        //    return readHierarchy( LOGGING.MSG_EMPTY);
        //}

        // todo add remove{Super/Sup Instance and Hierarchy} (not used in OWL api, put as Deprecated on MORDescriptor)
    }

    /**
     * The Semantic Descriptor for mapping typed knowledge in the ontology.
     * <p>
     *     This interface extends {@link Descriptor} by giving
     *     a type to the described {@code instance}.
     *     <br>
     *     More in details, its defines an ordered {@link List} of Classes ({@code C})
     *     that constrains the types of the {@link #getInstance()} results.
     *
     * <div style="text-align:center;"><small>
     * <b>File</b>:        {@link Semantics} <br>
     * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
     * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
     * <b>affiliation</b>: DIBRIS, EMAROLab, University of Genoa. <br>
     * <b>date</b>:        04/02/2017 <br>
     * </small></div>
     *
     * @param <O> the reference to the ontology.
     * @param <I> the instance describing a semantic chunk of knowledge in the ontology.
     * @param <C> the types of the instance.
     *
     * @see Semantics
     * @see MappingState
     * @see Descriptor
     */
    interface TypedDescriptor< O, I, C> extends  Descriptor<O, I> {

        /**
         * @return the types of the {@code instance}.
         */
        Collection<C> getTypes();

        C getBottomType();


        /**
         * Reads the {@link #getTypes()} value into the {@code ontology}.
         * @return the states of the reading operations.
         */
        MappingTransitions readType();
        /**
         * Reads the {@link #getTypes()} value into the {@code ontology}
         * by calling {@link #readType(String)}, with {@link LOGGING#MSG_EMPTY} message.
         * @return the states of the reading operations
         */// with empty debugging messages ({@link WritingState#getDescription()}).
        //default ReadingState readType() {
        //    return readType( LOGGING.MSG_EMPTY);
        //}


        /**
         * Writes the {@link #getTypes()} value into the {@code ontology}.
         * @return the states of the writing operations.
         */
        MappingTransitions writeType();
        /**
         * Writes the {@link #getInstance()} value into the {@code ontology}
         * by calling {@link #writeType( String)}, with {@link LOGGING#MSG_EMPTY} message.
         * @return the states of the writing operations
         */// with empty debugging messages ({@link WritingState#getDescription()}).
        //default WritingState writeType() {
        //    return writeType( LOGGING.MSG_EMPTY);
        //}

        // todo add remove{Super/Sup Instance and Hierarchy} (not used in OWL api, put as Deprecated on MORDescriptor)
    }

    /**
     * The Semantic Descriptor for mapping linked instances.
     * <p>
     *     This interface extends {@link Descriptor} by giving
     *     the possibility to link other instances to the {@link Descriptor#getInstance()} object.
     *     <br>
     *     More in details, its defines a {@link Map} of linking {@code properties}
     *     and related {@code instance} as well as implementing the synchronisation (read/write)
     *     of those properties into the ontology.
     *
     * <div style="text-align:center;"><small>
     * <b>File</b>:        {@link Semantics} <br>
     * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
     * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
     * <b>affiliation</b>: DIBRIS, EMAROLab, University of Genoa. <br>
     * <b>date</b>:        04/02/2017 <br>
     * </small></div>
     *
     * @param <O> the reference to the ontology.
     * @param <I> the instance describing a semantic chunk of knowledge in the ontology.
     * @param <OP> the type of property used for linking instances to {@code I}
     *
     * @see Semantics
     * @see MappingState
     * @see Descriptor
     */
    interface LinkedDescriptor<O, I, C, OP, V> extends TypedDescriptor<O, I, C> {

        /**
         * @return the linked instances (with the relate property) to the {@code instance}.
         */
        Map< OP, V> getLinksMap();

        /**
         * Writes a specific linked instances referred by the property in the
         * {@link #getLinksMap()}.
         * @param property the property of the linked instance.
         * @return the states of the writing operations.
         */
        MappingTransitions writeLink(final OP property);

        /**
         * Writes all the linked instances contained in the {@link #getLinksMap()}.
         * Default implementation loops in the map and it calls {@link #writeLink(Object)}.
         * @return the states of the writing operations
         */// with empty debugging messages ({@link WritingState#getDescription()}).
        default MappingTransitions writeLinks() {
            MappingTransitions state = new MappingTransitions();
            for (OP o : getLinksMap().keySet()) {
                MappingTransitions out = writeLink(o);
                Logger.LOG("|||||||||||||||||||||||||||||||||| " + out);
                state.addAll(out);
            }
            return state;
        }

        /**
         * Reads a specific linked instances referred by the property in the
         * {@link #getLinksMap()}.
         * @return the states of the writing operations.
         */
        MappingTransitions readLink();
    }


    //interface MultiLinkedDescriptor<O, I, OP, V extends Set> extends LinkedDescriptor<O, I, OP, V> {
    //    Map< OP, V> getMultiLinksMap();
    //}

    /**
     * The Semantic Descriptor for mapping data linked to instances.
     * <p>
     *     This interface extends {@link Descriptor} by giving
     *     the possibility to link literals to the {@link Descriptor#getInstance()} object.
     *     <br>
     *     More in details, its defines a {@link Map} of linking {@code properties}
     *     and related {@code literal} as well as implementing the synchronisation (read/write)
     *     of those properties into the ontology.
     *
     * <div style="text-align:center;"><small>
     * <b>File</b>:        {@link Semantics} <br>
     * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
     * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
     * <b>affiliation</b>: DIBRIS, EMAROLab, University of Genoa. <br>
     * <b>date</b>:        04/02/2017 <br>
     * </small></div>
     *
     * @param <O> the reference to the ontology.
     * @param <I> the instance describing a semantic chunk of knowledge in the ontology.
     * @param <DP> the type of property used for linking literals to {@code I}
     * @param <L> the type of literals managed by this descriptor.
     *
     * @see Semantics
     * @see MappingState
     * @see Descriptor
     */
    interface LiteralDescriptor<O, I, C, DP, L> extends TypedDescriptor<O,I,C> {

        /**
         * @return the linked literals (with the relate property) to the {@code instance}.
         */
        Map< DP, L> getLiteralMap();

        /**
         * Writes a specific linked literal referred by the property in the
         * {@link #getLiteralMap()}.
         * @param property the property of the linked literal.
         * @return the states of the writing operations.
         */
        MappingTransitions writeLiteral(final DP property);
        /**
         * Writes a specific linked literal referred by the property in the
         * {@link #getLiteralMap()} by calling {@link #writeLiteral(String, Object)},
         * with {@link LOGGING#MSG_EMPTY} message.
         * @param property the property of the linked literal.
         * @return the states of the writing operations
         */// with empty debugging messages ({@link WritingState#getDescription()}).
        //default WritingState writeLiteral(final DP property) {
        //    return writeLiteral(LOGGING.MSG_EMPTY, property);
        //}
        /**
         * Writes all the linked literal contained in the {@link #getLiteralMap()}.
         * Default implementation loops in the map and calls {@link #writeLiteral(String, Object)}.
         * @param debug a message to be propagated in the returning states.
         * @return the states of the writing operations.
         */
        //default WritingState writeLiterals(final String debug) {
        //    WritingState state = new WritingState( new DebuggingIntent<WritingState>(
        //            LOGGING.STATE_TYPE_WRITE, LiteralDescriptor.this.getInstance().toString(), LOGGING.MSG_EMPTY));
        //    for (DP o : getLiteralMap().keySet())
        //        state.merge( writeLiteral(debug, o));
        //    return state;
        //}
        /**
         * Writes all the linked literal contained in the {@link #getLiteralMap()}.
         * Default implementation loops in the map and it calls {@link #writeLiteral(Object)}.
         * @return the states of the writing operations
         */// with empty debugging messages ({@link WritingState#getDescription()}).
        default MappingTransitions writeLiterals() {

            //WritingState state = new WritingState( new DebuggingIntent<WritingState>(
                //LOGGING.STATE_TYPE_WRITE, LiteralDescriptor.this.getInstance().toString(), "LITERAL"));

            MappingTransitions state = new MappingTransitions();
            for (DP o : getLiteralMap().keySet())
                state.addAll( writeLiteral( o));

            return state;
        }

        /**
         * Reads a specific linked literal referred by the property in the
         * {@link #getLiteralMap()}.
         * @return the states of the writing operations.
         */
        MappingTransitions readLiteral();
        /**
         * Reads a specific linked instances referred by the property in the
         * {@link #getLiteralMap()} by calling {@link #readLiteral(String)},
         * with {@link LOGGING#MSG_EMPTY} message.
         * @return the states of the writing operations
         */// with empty debugging messages ({@link WritingState#getDescription()}).
        //default ReadingState readLiteral() {
        //    return readLiteral(LOGGING.MSG_EMPTY);
        //}

        // todo add remove{Super/Sup Instance and Hierarchy} (not used in OWL api, put as Deprecated on MORDescriptor)
    }



    interface Property< P, V> extends Base{

        String getPropertyName();
        default String getValueName(){
            if ( getValue() == null)
                return  "NA";
            return padString( getValue().toString(), LOGGING.LENGTH_NUMBER, true);
        }

        P getProperty( Descriptor<?,?> descriptor);
        void setProperty( Descriptor<?,?> descriptor, P property);
        void setProperty( Descriptor<?,?> descriptor, String propertyName);

        V getValue(); // V : value to manage all the the other parameters e.g. String or Double
        void setValue( Descriptor<?,?> descriptor, V value);
        void setValue( Descriptor<?,?> descriptor, P property, V value);
        void setValue( Descriptor<?,?> descriptor, String propertyName, V value);
    }
}