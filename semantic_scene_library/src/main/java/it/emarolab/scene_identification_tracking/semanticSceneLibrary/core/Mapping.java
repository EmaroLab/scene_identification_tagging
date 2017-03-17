package it.emarolab.scene_identification_tracking.semanticSceneLibrary.core;

import com.google.common.base.Objects;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.Base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static java.lang.Math.toIntExact;

/**
 * Created by bubx on 16/03/17.
 */
public interface Mapping extends Base{

    // [[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[   SEMANTIC CHANGES   ]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]
// todo add string and equals method (general comment ;) )
    class Transitions extends ArrayList<MappingIntent<?,?,?,? extends State>> {

        public Transitions(int initialCapacity) {
            super(initialCapacity);
        }
        public Transitions() {
        }
        public Transitions(Collection<? extends MappingIntent<?, ?, ?, ? extends State>> c) {
            super(c);
        }
        public Transitions(MappingIntent<?, ?, ?, ? extends State> intent) {
            super();
            add( intent);
        }

        // todo add merge policy for different state types
        public State merge(){ // tested only on same MappingStates
            sort();
            String info = "Merging mapping states: [";
            if( ! isEmpty()){
                State state = (State) get(0).getState().copy();
                info += state;// + "[" + state.getTypeName() + "]";
                if ( size() > 1)
                    for( int i = 1; i < size(); i++) {
                        State stateJ = (State) get(i).getState().copy();
                        info += "~" + stateJ;// + "[" + stateJ.getTypeName() + "]";
                        state = stateJ.merge(state);
                        info += " = " + state;// + "[" + state.getTypeName() + "]";
                    }
                Logger.LOG( info + "]");
                return state;
            }
            Logger.LOG( info + "]");
            Logger.logWARNING( "Cannot merge transitions with empty elements");
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

    class MappingIntent<I,V,S extends Semantic<?,I,V>, M extends State> extends SITBase implements Comparable<MappingIntent<?,?,?,?>>{

        private long time = System.currentTimeMillis();
        private String description;
        private M state;
        private I instance;
        private V javaValue, semanticValue;

        public MappingIntent(){}
        public MappingIntent( I instance, String description){
            this.instance = instance;
            this.description = description;
        }
        public MappingIntent( I instance, String description, M state){
            this.instance = instance;
            this.description = description;
            this.state = state;
        }

        public MappingIntent(MappingIntent<I,V,S,M> copy){
            this.time = copy.time;
            this.description = copy.description;
            this.state.state = copy.state.state;
            this.instance = copy.instance;
            this.javaValue = copy.javaValue;
            this.semanticValue = copy.semanticValue;
        }
        @Override
        public MappingIntent<I,V,S,M> copy() {
            return new MappingIntent<>( this);
        }

        public long getTime() {
            return time;
        }

        public I getInstance() {
            return instance;
        }

        public String getDescription() {
            return description;
        }
        public void setDescription(String description) {
            this.description = description;
        }

        public M getState() {
            return state;
        }
        public void setState(M state) {
            this.state = state;
        }

        public V getJavaValue() {
            return javaValue;
        }
        public void setJavaValue(V javaValue) {
            this.javaValue = javaValue;
        }

        public V getSemanticValue() {
            return semanticValue;
        }
        public void setSemanticValue(V semanticValue) {
            this.semanticValue = semanticValue;
        }


        @Override
        public String toString() {
            return "?????? ";//toStringDescription() + toStringStates() + toStringAxioms();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof MappingIntent)) return false;

            MappingIntent<?, ?, ?, ?> that = (MappingIntent<?, ?, ?, ?>) o;

            if (getDescription() != null ? !getDescription().equals(that.getDescription()) : that.getDescription() != null)
                return false;
            if (getState() != null ? !getState().equals(that.getState()) : that.getState() != null) return false;
            if (getInstance() != null ? !getInstance().equals(that.getInstance()) : that.getInstance() != null)
                return false;
            if (getJavaValue() != null ? !getJavaValue().equals(that.getJavaValue()) : that.getJavaValue() != null)
                return false;
            return getSemanticValue() != null ? getSemanticValue().equals(that.getSemanticValue()) : that.getSemanticValue() == null;
        }

        @Override
        public int hashCode() {
            int result = getDescription() != null ? getDescription().hashCode() : 0;
            result = 31 * result + (getState() != null ? getState().hashCode() : 0);
            result = 31 * result + (getInstance() != null ? getInstance().hashCode() : 0);
            result = 31 * result + (getJavaValue() != null ? getJavaValue().hashCode() : 0);
            result = 31 * result + (getSemanticValue() != null ? getSemanticValue().hashCode() : 0);
            return result;
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
     * <b>File</b>:        Semantics} <br>
     * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
     * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
     * <b>affiliation</b>: DIBRIS, EMAROLab, University of Genoa. <br>
     * <b>date</b>:        04/02/2017 <br>
     * </small></div>
     *
     * @see STATEMAPPING
     * @see WritingState
     * @see ReadingState
     */
    abstract class State extends Base.SITBase {

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
        public State(State copy){
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
        public State(int state) {
            if (state >= STATEMAPPING.ERROR & state <= STATEMAPPING.NOT_CHANGED)
                this.state = state;
            else state = STATEMAPPING.INITIALISED;
        }
        /**
         * Empty constructor, initialises this {@link #state} {@link #asInconsistent()} and assign a debugging message.
         */
        public State() {
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
        public State asError() {
            this.state = STATEMAPPING.ERROR;
            return this;
        }

        /**
         * set this state as {@link #NOT_CHANGED}
         * @return <code>'this'</code>, for chaining calls.
         */
        public State asNotChanged() {
            this.state = STATEMAPPING.NOT_CHANGED;
            return this;
        }

        /**
         * set this state as {@link #INCONSISTENT}
         * @return <code>'this'</code>, for chaining calls.
         */
        public State asInconsistent() {
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
        abstract public State merge(State otherResult);/* {
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
        public static State combineResults(State r1, State r2) {

            if (r1.isInitialised())
                return r2;
            else if (r2.isInitialised())
                return r1;

            // high priority for inconsistent
            State inc = checkInconsistency( r1, r2);
            if( inc != null)
                return inc;

            // quite high priority for errors
            State err = checkError( r1, r2);
            if ( err != null)
                return err;

            State notC = checkNotChanged(r1, r2);
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
        protected static State checkInconsistency(State r1, State r2){
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
        protected static State checkError(State r1, State r2){
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
        protected static State checkNotChanged(State r1, State r2){
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
            if (!(o instanceof State)) return false;
            State that = (State) o;
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
     *     In particular, this class extend {@link State} by adding more states,
     *     in details:<ul>
     *         <li> {@link #ERROR}: implemented in {@link State},
     *         <li> {@link #INCONSISTENT}: implemented in {@link State},
     *         <li> {@link #NOT_CHANGED}: implemented in {@link State},
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
     * <b>File</b>:        {Semantics} <br>
     * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
     * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
     * <b>affiliation</b>: DIBRIS, EMAROLab, University of Genoa. <br>
     * <b>date</b>:        04/02/2017 <br>
     * </small></div>
     *
     * @see STATEMAPPING
     * @see State
     */
    class WritingState extends State {

        /**
         * Copy constrcutors, it creates a new object by coping all its fields.
         * This method should use {@code super} hierarchy constrcutors
         * and it is called by {@link #copy()}.
         * @param copy the state to copy.
         */
        public WritingState( State copy){
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
        public WritingState merge( State otherResult) {
            if (otherResult != null) {
                try {
                    return new WritingState( combineResults(this, (WritingState) otherResult));  // set always 'this' as first parameter
                } catch ( ClassCastException e){
                    Logger.logWARNING( "you should not merge " + otherResult.getClass().getSimpleName() + " with Reading states");
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
     *     In particular, this class extend {@link State} by adding more states,
     *     in details:<ul>
     *         <li> {@link #ERROR}: implemented in {@link State},
     *         <li> {@link #INCONSISTENT}: implemented in {@link State},
     *         <li> {@link #NOT_CHANGED}: implemented in {@link State},
     *         <li> <b>ABSENT</b>: reading failure, since not data were available in the semantic description,
     *         <li> <b>SUCCESS</b>: reading success, java descriptor has been up to dated.
     *     </ul>
     *
     * <p>
     *     <b>REMARK</b>: extending classes must have static state fields
     *     assigned to inter values bigger than {@link #SUCCESS}.
     *
     * <div style="text-align:center;"><small>
     * <b>File</b>:        { Semantics} <br>
     * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
     * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
     * <b>affiliation</b>: DIBRIS, EMAROLab, University of Genoa. <br>
     * <b>date</b>:        04/02/2017 <br>
     * </small></div>
     *
     * @see STATEMAPPING
     * @see State
     */
    class ReadingState extends State {

        /**
         * Copy constrcutors, it creates a new object by coping all its fields.
         * This method should use {@code super} hierarchy constrcutors
         * and it is called by {@link #copy()}.
         * @param copy the state to copy.
         */
        public ReadingState( State copy){
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
         * java descriptor has been up to dated.
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
        public ReadingState merge( State otherResult) {
            if (otherResult != null)
                try {
                    return new ReadingState( combineResults(this, (ReadingState) otherResult));  // set always 'this' as first parameter
                } catch ( ClassCastException e){
                    Logger.logWARNING( "you should not merge " + otherResult.getClass().getSimpleName() + " with Reading states");
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

}
