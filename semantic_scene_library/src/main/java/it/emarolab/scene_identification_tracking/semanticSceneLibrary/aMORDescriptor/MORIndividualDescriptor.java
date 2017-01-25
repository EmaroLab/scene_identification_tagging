package it.emarolab.scene_identification_tracking.semanticSceneLibrary.aMORDescriptor;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.amor.owlInterface.OWLReferencesInterface;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.Logger;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.Semantics;
import org.semanticweb.owlapi.model.*;

import java.util.*;

/**
 * Created by bubx on 18/01/17.
 */
public interface MORIndividualDescriptor extends Semantics.IndividualDescriptor< OWLReferences, OWLNamedIndividual, OWLClass, OWLDataProperty, OWLObjectProperty, OWLLiteral> {

    void setOntology(OWLReferences ontoRef);

    void setOntologyName(String referenceName);
    void setIndividualName(String individualName);

    /**
     * Set an {@link OWLReferences} able to manipulate and query an OWL ontology.
     * It is retrieved by name, given as input parameter, that must be already
     * available into the aMOR library.
     * @param ontoName the name of an instantiated {@link OWLReferences} that
     *                 describes the OWL Ontology containing the object described by this class
     */
    default void setOntology(String ontoName){ // set ontology name
        setOntologyName( ontoName);

        if( OWLReferencesInterface.OWLReferencesContainer.isInstance( ontoName))
            setOntology( (OWLReferences) OWLReferencesInterface.OWLReferencesContainer.getOWLReferences( ontoName));
        else {
            Logger.SITBase.logERROR( ontoName + "\" aMOR reference not found!");
            return;
        }

        setInstance( getInitialIndividual());
    }

    default OWLNamedIndividual getInitialIndividual(){ // todo not in the interface ????
        if( getIndividualName() != null)
            return getOntology().getOWLIndividual( getIndividualName());
        else Logger.SITBase.logWARNING( this.getClass().getName() + ".setOntology() " +
                "did not create any instance [null] !!!");
        return null;
    }

    /**
     * Set the instance name and try to set the OWL instance based on it.
     * It will fails if the input parameter ot {@link #getOntology()} are null.
     * @param individualName the name of the new OWL instance describing the related object.
     */
    default void setInstance(String individualName) {
        this.setIndividualName( individualName);
        if( getIndividualName() != null & getOntology() != null)
            this.setInstance( getOntology().getOWLIndividual( individualName));
        else Logger.SITBase.logWARNING( "OWLNamedIndividual not set for new name: " + individualName);
    }

    /**
     * Just calls on {@link #ontology}: {@link OWLReferences#synchronizeReasoner()}.
     */
    default void updateReasoner(){
        this.getOntology().synchronizeReasoner();
    }

    default OWLClass getType(int idx){
        return getTypes().get( idx);
    }

    default OWLClass getHighestType(){
        return getType( getTypes().size() - 1);
    }

    List<OWLClass> getTypes();
    void setType( List< OWLClass> types);

    // todo check copy()

    String getIndividualName();
    String getOntologyName();

    boolean addType(OWLClass toAdd);
    default boolean addType( String toAdd){
        OWLClass cl = getOntology().getOWLClass( toAdd);
        return addType( cl);
    }
    default boolean addTypes( Collection<?> toAdd){ // reduce String to OWLClasses or skip
        boolean flag = false;
        for( Object item : toAdd){
            if( item instanceof String)
                flag = addType( ( String) item);
            else if( item instanceof OWLClass)
                flag = flag & addType( ( OWLClass) item);
            else Logger.SITBase.logWARNING( "Cannot add type " + item + " to object: " + this);
        }
        return flag;
    }

    boolean removeType(OWLClass toRemove);
    default boolean removeTypes(String toRemove){
        OWLClass cl = getOntology().getOWLClass(toRemove);
        return removeType( cl);
    }
    default boolean removeTypes( Collection<?> toRemove){ // reduce String to OWLClasses or skip
        boolean flag = false;
        for( Object item : toRemove){
            if( item instanceof String)
                flag = removeTypes( ( String) item);
            else if( item instanceof OWLClass)
                flag = flag & removeType( ( OWLClass) item);
            else Logger.SITBase.logWARNING( "Cannot remove type " + item + " to object: " + this);
        }
        return flag;
    }

    default void clearTypes(){
        getTypes().clear();
    }

    boolean containsTypes(OWLClass toCheck);
    default boolean containsTypes( String toCheck){
        OWLClass cl = getOntology().getOWLClass(toCheck);
        return containsTypes( cl);
    }
    default boolean containsTypes( Collection<?> toCheck){ // reduce String to OWLClasses or skip
        boolean flag = false;
        for( Object item : toCheck){
            if( item instanceof String)
                flag = containsTypes( ( String) item);
            else if( item instanceof OWLClass)
                flag = flag & containsTypes( ( OWLClass) item);
            else Logger.SITBase.logWARNING( "Cannot check type " + item + " to object: " + this);
        }
        return flag;
    }
    // contains only that one !!!
    default boolean sameTypesAs( Collection<?> toAdd){ // reduce String to OWLClasses or skip
        Set< OWLClass> out = new HashSet<>();
        for( Object item : toAdd){
            if( item instanceof String)
                out.add( getOntology().getOWLClass( ( String) item));
            else if( item instanceof OWLClass)
                out.add( ( OWLClass) item);
            else Logger.SITBase.logWARNING( "Cannot check type " + item + " to object: " + this);
        }
        return getTypes().equals( out);
    }


    String toString();

    default Semantics.WritingState writeType(String debuggingInfo){
        return new MORSpatialDescriptor.MORSimpleDescriptor.TryingWrite( this) {
            protected Semantics.WritingState giveAtry() {
                Semantics.WritingState state = new Semantics.WritingState(); // as inconsistent
                Set<OWLClass> oldTypes = new HashSet<>();
                if ( ! getTypes().isEmpty()) {
                    oldTypes = ontology.getIndividualClasses(individual);
                    if ( ! oldTypes.equals( getTypes())) {
                        for (OWLClass cl : oldTypes)
                            ontology.removeIndividualB2Class( individual, cl);
                        for (OWLClass cl : getTypes())
                            ontology.addIndividualB2Class( individual, cl);
                        state.asUpdated();
                    } else  state.asNotChanged();
                }

                log( this.getClass().getSimpleName() + "\t WRITES TYPE: \"" + individualName
                        + "\"(" + debuggingInfo + ")"
                        + "\t\t " + getOntology().getOWLObjectName( new HashSet<>( getTypes()))
                        + "\t(was in ontology: " + oldTypes + ")"
                        + "\t\t[" + state + "]");
                return state;
            }
        }.perform();
    }
    default MORSpatialDescriptor.MORSimpleDescriptor.TypeReadingOutcome readType(String debuggingInfo){
        final MORSpatialDescriptor.MORSimpleDescriptor.TypeReadingOutcome outcomes = new MORSpatialDescriptor.MORSimpleDescriptor.TypeReadingOutcome();
        new MORSpatialDescriptor.MORSimpleDescriptor.TryingRead( this) {
            @Override
            protected Semantics.ReadingState giveAtry() {
                Set<OWLClass> typesTmp = ontology.getIndividualClasses( individual);
                Semantics.ReadingState state = new Semantics.ReadingState();
                if( typesTmp.isEmpty())
                    state.asAbsent();
                else {
                    if( ! sameTypesAs( typesTmp)){
                        setType( new ArrayList<>( typesTmp));
                        state.asSuccess();
                    }
                    else state.asNotChanged();
                }

                log( this.getClass().getSimpleName() + "\t READS TYPE: \"" + individualName
                        + "\"(" + debuggingInfo + ")"
                        + "\t\t " + getOntology().getOWLObjectName( new HashSet<>( getTypes()))
                        + "\t(was in java:" + getOntology().getOWLObjectName( typesTmp) + ")"
                        + "\t\t[" + state + "]");

                outcomes.setReadBuffer( getTypes(), state);
                return state;
            }
        }.perform();
        return outcomes;
    }

    /**
     * It uses the {@link MORSpatialDescriptor.MORSimpleDescriptor.TryingWrite#perform()} interface for synchronise a
     * javaValue (the value of an {@link OWLDataProperty}) from a java representation
     * (the object related to this descriptor) into the {@link #getOntology()}
     * (indeed the value will be written to: {@link #getInstance()}).
     * <p>
     *     In particular, it:
     *     <ul>
     *        <li> reads the value of a property and, if it does not exists,
     *             it creates a new axiom in the ontology by mapping a java representation.
     *             In this case it returns an {@link Semantics.WritingState#ADDED} state.
     *        <li> otherwise, if the value of the previous point exists.
     *             It checks if it is <code>semanticLiteral.equals( javaValue)</code> to the java representation.
     *             If their are not equal, it does not perform any ontological changes and returns
     *             a {@link Semantics.WritingState#NOT_CHANGED} state.
     *        <li> If it is not the case, it synchronise the OWL ontology with respect to
     *             the java representation and returns an {@link Semantics.WritingState#UPDATED} state.
     *     </ul>
     * <p>
     *     <b>REMARK</b>: the first point check for a value relate to a semantic data
     *     property. If the same instance has more than one value for the same property
     *     this implementation does not assure any rule on the used value.
     *
     * @param debuggingInfo a string used for debugging logging.
     * @param property the OWL data property to be used to semantically write a value to an instance
     * @param javaValue the javaValue to be mapped in the instance (in the ontology) defined by this descriptor.
     * @return the state of the writing of a OWL data property value.
     *
     * @see MORSpatialDescriptor.MORSimpleDescriptor.TryingWrite
     */
    default Semantics.WritingState writeLiteral(String debuggingInfo, OWLDataProperty property, OWLLiteral javaValue){
        return new MORSpatialDescriptor.MORSimpleDescriptor.TryingWrite( this) {
            protected Semantics.WritingState giveAtry() {
                // see around
                Semantics.WritingState result;
                OWLLiteral semanticLiteral = getPropertyData( property);
                // adding
                if( semanticLiteral == null) {
                    ontology.addDataPropertyB2Individual( individual, property, javaValue);
                    result = new Semantics.WritingState().asAdded();
                } else if( ! semanticLiteral.equals( javaValue)){ // replacing
                    ontology.replaceDataProperty( individual, property, semanticLiteral, javaValue);
                    result = new Semantics.WritingState().asUpdated();
                } else result = new Semantics.WritingState().asNotChanged(); // nothing

                log( this.getClass().getSimpleName() + "\t WRITES LITERAL: \"" + individualName
                        + "\"(" + debuggingInfo + ")"
                        + "\t\t " + getOntology().getOWLObjectName( property) + "." + getOntology().getOWLObjectName(semanticLiteral)
                        + "\t(was in ontology: " + getOntology().getOWLObjectName(semanticLiteral) + ")"
                        + "\t\t[" + result + "]");
                return result;
            }
        }.perform();
    }
    /**
     * It uses the {@link MORSpatialDescriptor.MORSimpleDescriptor.TryingRead#perform()} interface for synchronise a
     * java representation (the object related to this descriptor) from a literal
     * (the oldJavaValue of an {@link OWLDataProperty}) into the {@link #getOntology()}
     * (indeed the oldJavaValue will be written to: {@link #getInstance()}).
     * <p>
     *     In particular, it:
     *     <ul>
     *        <li> reads the oldJavaValue of a property and, if it does not exists,
     *             In this case it returns an {@link Semantics.ReadingState#ABSENT} state
     *             through the {@link MORSpatialDescriptor.MORSimpleDescriptor.DataReadingOutcome} container (reading buffer will be set to <code>null</code>).
     *        <li> otherwise, if the oldJavaValue of the previous point exists.
     *             It checks if it is <code>semanticValue.equals( oldJavaValue)</code> to the java representation.
     *             If it is the case, it does not perform any changes in the java description and returns
     *             a {@link Semantics.WritingState#NOT_CHANGED} state (though {@link MORSpatialDescriptor.MORSimpleDescriptor.DataReadingOutcome}
     *             with the oldJavaValue get from the ontology in the buffer).
     *        <li> If their are not equal, it synchronise the java representation with the
     *             oldJavaValue read from the OWL ontology and returns an {@link Semantics.WritingState#UPDATED} state.
     *     </ul>
     *
     * @param debuggingInfo a string used for debugging logging.
     * @param property the OWL data property to be used to semantically read the oldJavaValue from the instance
     * @param oldJavaValue the oldJavaValue to be mapped in the java representation from the ontology.
     * @return the state of the reading of an OWL data property oldJavaValue.
     *
     * @see MORSpatialDescriptor.MORSimpleDescriptor.TryingRead
     * @see MORSpatialDescriptor.MORSimpleDescriptor.DataReadingOutcome
     */
    default MORSpatialDescriptor.MORSimpleDescriptor.DataReadingOutcome readLiteral(String debuggingInfo, OWLDataProperty property, OWLLiteral oldJavaValue){
        final MORSpatialDescriptor.MORSimpleDescriptor.DataReadingOutcome outcomes = new MORSpatialDescriptor.MORSimpleDescriptor.DataReadingOutcome();
        new MORSpatialDescriptor.MORSimpleDescriptor.TryingRead( this) {
            @Override
            protected Semantics.ReadingState giveAtry() {
                // see around
                Semantics.ReadingState result;
                OWLLiteral semanticValue = getPropertyData( property);
                if( semanticValue == null) // does not exist
                    result = new Semantics.ReadingState().asAbsent();
                else if( semanticValue.equals( oldJavaValue)) // it was up to date
                    result = new Semantics.ReadingState().asNotChanged();
                else result = new Semantics.ReadingState().asSuccess(); // it has been loaded
                log( this.getClass().getSimpleName() + "\t READS LITERAL: \"" + individualName
                        + "\"(" + debuggingInfo + ")"
                        + "\t\t " + getOntology().getOWLObjectName( property) + "." + getOntology().getOWLObjectName( semanticValue)
                        + "\t(was in java: " + getOntology().getOWLObjectName(oldJavaValue) + ")"
                        + "\t\t[" + result + "]");
                outcomes.setReadBuffer( semanticValue, result);
                return result;
            }
        }.perform();
        return outcomes;
    }
    default Semantics.WritingState writeProperty(String debuggingInfo, OWLObjectProperty property, OWLNamedIndividual javaValue){
        return new MORSpatialDescriptor.MORSimpleDescriptor.TryingWrite( this) {
            protected Semantics.WritingState giveAtry() {
                // see around
                Semantics.WritingState result;
                OWLNamedIndividual semanticValue = getPropertyObject( property);
                // adding
                if( semanticValue == null) {
                    ontology.addObjectPropertyB2Individual( individual, property, javaValue);
                    result = new Semantics.WritingState().asAdded();
                } else if( ! semanticValue.equals(javaValue)){ // replacing
                    ontology.replaceObjectProperty( individual, property, semanticValue, javaValue);
                    result = new Semantics.WritingState().asUpdated();
                } else result = new Semantics.WritingState().asNotChanged(); // nothing

                log( this.getClass().getSimpleName() + "\t WRITES PROPERTY: \"" + individualName
                        + "\"(" + debuggingInfo + ")"
                        + "\t\t " + getOntology().getOWLObjectName( property) + "." + getOntology().getOWLObjectName( semanticValue)
                        + "\t(was in java: " + getOntology().getOWLObjectName(semanticValue) + ")"
                        + "\t\t[" + result + "]");
                return result;
            }
        }.perform();
    }
    default MORSpatialDescriptor.MORSimpleDescriptor.ObjectReadingOutcome readProperty(String debuggingInfo, OWLObjectProperty property, OWLNamedIndividual oldJavaValue){
        final MORSpatialDescriptor.MORSimpleDescriptor.ObjectReadingOutcome outcomes = new MORSpatialDescriptor.MORSimpleDescriptor.ObjectReadingOutcome();
        new MORSpatialDescriptor.MORSimpleDescriptor.TryingRead( this) {
            @Override
            protected Semantics.ReadingState giveAtry() {
                // see around
                Semantics.ReadingState result;
                OWLNamedIndividual semanticValue = getPropertyObject( property);
                if( semanticValue == null) // does not exist
                    result = new Semantics.ReadingState().asAbsent();
                else if( semanticValue.equals( oldJavaValue)) // it was up to date
                    result = new Semantics.ReadingState().asNotChanged();
                else result = new Semantics.ReadingState().asSuccess(); // it has been loaded
                log( this.getClass().getSimpleName() + "\t READS PROPERTY: \"" + individualName
                        + "\"(" + debuggingInfo + ")"
                        + "\t\t " + getOntology().getOWLObjectName( property) + "." + getOntology().getOWLObjectName( semanticValue)
                        + "\t(was in java: " + getOntology().getOWLObjectName(oldJavaValue) + ")"
                        + "\t\t[" + result + "]");
                outcomes.setReadBuffer( semanticValue, result);
                return result;
            }
        }.perform();
        return outcomes;
    }


// todo move Try away
    /**
     * This is an helper based on {@link Semantics.MappingTry} for semantic mpping based on aMOR library.
     * <p>
     * It also implements helper methods to deal with {@link OWLDataProperty},
     * and {@link OWLLiteral} by using the aMOR library.
     *
     * <div style="text-align:center;"><small>
     * <b>File</b>:       it.emarolab.scene_identification_tracking.semanticSceneLibrary.Semantics <br>
     * <b>Licence</b>:    GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
     * <b>Author</b>:     Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
     * <b>affiliation</b>: DIBRIS, EMAROLab, University of Genoa. <br>
     * <b>date</b>:       06/01/2017 <br>
     * </small></div>
     *
     * @param <Y> the class that define the {@link Semantics.MappingState}
     *           (i.e.: {@link Semantics.ReadingState} or {@link Semantics.WritingState}).
     *
     * @see Semantics.MappingTry
     * @see Semantics.MappingState
     * @see Semantics.WritingState
     * @see Semantics.ReadingState
     * @see DataReadingOutcome
     * @see TryingWrite
     * @see TryingRead
     */
    abstract class MappingTry< Y extends Semantics.MappingState> extends Semantics.MappingTry< Y> {
        /** the ontology given on constructor. Directly accessible from extending implementations. */
        protected final OWLReferences ontology;
        /** the name of the #OWLReferences given on constructor. Directly accessible from extending implementations. */
        protected final String ontologyName;
        /** the instance in the ontology given on constructor. Directly accessible from extending implementations. */
        protected final OWLNamedIndividual individual;
        /** the name of the #OWLNamedIndividual given on constructor. Directly accessible from extending implementations. */
        protected final String individualName;

        /**
         * Construct by filling all internal fields with an <code>final</code> value.
         * @param semantics the description of the ontology ({@link MORSpatialDescriptor.MORSimpleDescriptor#getOntology()})
         *                  and of the instance ({@link MORSpatialDescriptor.MORSimpleDescriptor#getInstance()},
         *                  {@link MORSpatialDescriptor.MORSimpleDescriptor#getIndividualName()})
         */
        public MappingTry(MORIndividualDescriptor semantics) {
            ontology = semantics.getOntology();
            ontologyName = ontology.getReferenceName();
            individual = semantics.getInstance();
            individualName = semantics.getIndividualName();
        }

        /**
         * Called on {@link #perform()} if an {@link openllet.owlapi.OWLException} occurs.
         * @return an {@link Semantics.MappingState#INCONSISTENT}
         * state due to an OWL Exception during the semantic mapaping process.
         */
        abstract  protected Y onOWLException();

        /**
         * @return the state of this mapping operation. It can be the
         * {@link #giveAtry()} returning value, the {@link #onError()},
         * if Java {@link Exception} occurs (logged through {@link #logError(Exception)}).
         * Otherwise, il an {@link openllet.owlapi.OWLException} occurs, it returns calls
         * {@link #onOWLException()} and returns its value.
         */
        @Override
        public Y perform() {
            try {
                return giveAtry();
            } catch (openllet.owlapi.OWLException e){
                logError( e);
                return onOWLException();
            } catch (Exception e) {
                logError( e);
                return onError();
            }
        }

        /**
         * @param propertyName the name of the OWL data property to be used to retrieve the literal.
         * @return the literal of a OWL data property (given by name) evaluated with
         * respect to the {@link #individual} of the {@link #ontology}.
         */
        public OWLLiteral getPropertyData( String propertyName){
            OWLDataProperty prop = ontology.getOWLDataProperty( propertyName);
            return getPropertyData( prop);
        }
        /**
         * @param property the OWL data property to be used to retrieve the literal.
         * @return the literal of a OWL data property evaluated with
         * respect to the {@link #individual} of the {@link #ontology}.
         */
        public OWLLiteral getPropertyData(OWLDataProperty property){
            return ontology.getOnlyDataPropertyB2Individual( individual, property);
        }

        public OWLNamedIndividual getPropertyObject( String propertyName){
            OWLObjectProperty prop = ontology.getOWLObjectProperty( propertyName);
            return getPropertyObject( prop);
        }
        public OWLNamedIndividual getPropertyObject(OWLObjectProperty property){
            return ontology.getOnlyObjectPropertyB2Individual( individual, property);
        }

        /**
         * @param propertyName the name of the OWL data property to be used to retrieve the value.
         * @return the {@link Double} value of a OWL data property evaluated with
         * respect to the {@link #individual} of the {@link #ontology}.
         */
        public Double getDoublePropertyValue(String propertyName) {
            OWLDataProperty prop = ontology.getOWLDataProperty( propertyName);
            return getDoublePropertyValue( prop);
        }
        /**
         * @param property the OWL data property to be used to retrieve the value.
         * @return the {@link Double} value of a OWL data property evaluated with
         * respect to the {@link #individual} of the {@link #ontology}.
         */
        public Double getDoublePropertyValue(OWLDataProperty property) {
            OWLLiteral l = getPropertyData( property);
            if ( l == null)
                return null;
            return Double.valueOf(l.getLiteral());
        }

        /**
         * @param propertyName the name of the OWL data property to be used to retrieve the value.
         * @return the {@link Long} value of a OWL data property evaluated with
         * respect to the {@link #individual} of the {@link #ontology}.
         */
        public Long getLongPropertyValue(String propertyName) {
            OWLDataProperty prop = ontology.getOWLDataProperty( propertyName);
            return getLongPropertyValue( prop);
        }
        /**
         * @param property the OWL data property to be used to retrieve the value.
         * @return the {@link Long} value of a OWL data property evaluated with
         * respect to the {@link #individual} of the {@link #ontology}.
         */
        public Long getLongPropertyValue(OWLDataProperty property) {
            OWLLiteral l = getPropertyData( property);
            if ( l == null)
                return null;
            return Long.valueOf(l.getLiteral());
        }

        /**
         * @param propertyName the name of the OWL data property to be used to retrieve the value.
         * @return the {@link String} value of a OWL data property evaluated with
         * respect to the {@link #individual} of the {@link #ontology}.
         */
        public String getStringPropertyValue(String propertyName) {
            OWLDataProperty prop = ontology.getOWLDataProperty( propertyName);
            return getStringPropertyValue( prop);
        }
        /**
         * @param property the OWL data property to be used to retrieve the value.
         * @return the {@link String} value of a OWL data property evaluated with
         * respect to the {@link #individual} of the {@link #ontology}.
         */
        public String getStringPropertyValue(OWLDataProperty property) {
            OWLLiteral l = getPropertyData( property);
            if ( l == null)
                return null;
            return String.valueOf(l.getLiteral());
        }

        /**
         * @param propertyName the name of the OWL data property to be used to retrieve the value.
         * @return the {@link Float} value of a OWL data property evaluated with
         * respect to the {@link #individual} of the {@link #ontology}.
         */
        public Float getFloatPropertyValue(String propertyName) {
            OWLDataProperty prop = ontology.getOWLDataProperty( propertyName);
            return getFloatPropertyValue( prop);
        }
        /**
         * @param property the OWL data property to be used to retrieve the value.
         * @return the {@link Float} value of a OWL data property evaluated with
         * respect to the {@link #individual} of the {@link #ontology}.
         */
        public Float getFloatPropertyValue(OWLDataProperty property) {
            OWLLiteral l = getPropertyData( property);
            if ( l == null)
                return null;
            return Float.valueOf(l.getLiteral());
        }

        /** @return the aMOR ontology reference given on constructor. */
        public OWLReferences getOntology() {
            return ontology;
        }
        /** @return the aMOR ontology reference name given on constructor. */
        public String getOntologyName() {
            return ontologyName;
        }
        /** @return the OWL instance of the {@link #ontology} given on constructor. */
        public OWLNamedIndividual getIndividual() {
            return individual;
        }
        /** @return the OWL instance name of the {@link #ontology} given on constructor. */
        public String getIndividualName() {
            return individualName;
        }
    }
    /**
     * The {@link MORSpatialDescriptor.MORSimpleDescriptor.MappingTry} implementation for reading operations ({@link Semantics.ReadingState}).
     *
     * <div style="text-align:center;"><small>
     * <b>File</b>:       it.emarolab.scene_identification_tracking.semanticSceneLibrary.Semantics <br>
     * <b>Licence</b>:    GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
     * <b>Author</b>:     Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
     * <b>affiliation</b>: DIBRIS, EMAROLab, University of Genoa. <br>
     * <b>date</b>:       06/01/2017 <br>
     * </small></div>
     *
     * @see MORSpatialDescriptor.MORSimpleDescriptor.MappingTry
     * @see Semantics.ReadingState
     */
    abstract class TryingRead extends MappingTry<Semantics.ReadingState> {

        /**
         * Empty constructor based on {@link MORSpatialDescriptor.MORSimpleDescriptor.MappingTry#MappingTry(MORSpatialDescriptor.MORSimpleDescriptor)}.
         * @param semantics the aMOR based description of the ontology
         *                  and instance used during semantic mapping.
         */
        public TryingRead(MORIndividualDescriptor semantics) {
            super(semantics);
        }

        /**
         * @param r1 the first reading process state to be merged.
         * @param r2 the second reading process state to be merged
         * @return the reading process state based on the input parameters.
         * Indeed, it calls {@link Semantics.ReadingState#combineResults(Semantics.ReadingState, Semantics.ReadingState)}.
         */
        protected Semantics.ReadingState merge(Semantics.ReadingState r1, Semantics.ReadingState r2) {
            return Semantics.ReadingState.combineResults(r1, r2);
        }

        @Override
        protected Semantics.ReadingState onOWLException(){
            return new Semantics.ReadingState().asInconsistent();
        }

        @Override
        protected Semantics.ReadingState onError(){
            return new Semantics.ReadingState().asError();
        }
    }
    /**
     * The {@link MORSpatialDescriptor.MORSimpleDescriptor.MappingTry} implementation for writing operations ({@link Semantics.WritingState}).
     *
     * <div style="text-align:center;"><small>
     * <b>File</b>:       it.emarolab.scene_identification_tracking.semanticSceneLibrary.Semantics <br>
     * <b>Licence</b>:    GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
     * <b>Author</b>:     Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
     * <b>affiliation</b>: DIBRIS, EMAROLab, University of Genoa. <br>
     * <b>date</b>:       06/01/2017 <br>
     * </small></div>
     *
     * @see Semantics.MappingTry
     * @see Semantics.WritingState
     */
    abstract class TryingWrite extends MappingTry<Semantics.WritingState> {

        /**
         * Empty constructor based on {@link MORSpatialDescriptor.MORSimpleDescriptor.MappingTry#MappingTry(MORSpatialDescriptor.MORSimpleDescriptor)}.
         * @param semantics the aMOR based description of the ontology
         *                  and instance used during semantic mapping.
         */
        public TryingWrite(MORIndividualDescriptor semantics) {
            super(semantics);
        }

        /**
         * @param r1 the first writing process state to be merged.
         * @param r2 the second writing process state to be merged
         * @return the writing process state based on the input parameters.
         * Indeed, it calls {@link Semantics.WritingState#combineResults(Semantics.WritingState, Semantics.WritingState)}.
         */
        protected Semantics.WritingState merge(Semantics.WritingState r1, Semantics.WritingState r2) {
            return Semantics.WritingState.combineResults( r1, r2);
        }

        @Override
        protected Semantics.WritingState onOWLException(){
            return new Semantics.WritingState().asInconsistent();
        }

        @Override
        protected Semantics.WritingState onError(){
            return new Semantics.WritingState().asError();
        }
    }


    /**
     * A container for reading results.
     * <p>
     *     It implements an extension of {@link Semantics.ReadingOutcome} by
     *     specifying the type of the reading buffer as {@link OWLLiteral}.
     *
     * <div style="text-align:center;"><small>
     * <b>File</b>:       it.emarolab.scene_identification_tracking.semanticSceneLibrary.Semantics <br>
     * <b>Licence</b>:    GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
     * <b>Author</b>:     Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
     * <b>affiliation</b>: DIBRIS, EMAROLab, University of Genoa. <br>
     * <b>date</b>:       06/01/2017 <br>
     * </small></div>
     *
     * @see MORSpatialDescriptor.MORSimpleDescriptor
     * @see Semantics.ReadingOutcome
     */
    class DataReadingOutcome extends Semantics.ReadingOutcome<OWLLiteral> {
        /**
         * Empty constructor, set null buffer and inconsistent reading stet
         * Indeed it calls {@link Semantics.ReadingOutcome#ReadingOutcome()}.
         */
        public DataReadingOutcome(){
            super();
        }
        /**
         * Construct by specifying all the fields of this container.
         * Indeed it calls {@link Semantics.ReadingOutcome#ReadingOutcome(Object, Semantics.ReadingState)}).
         * @param readBuffer the value read from the ontology.
         * @param results the result of the reading process.
         */
        public DataReadingOutcome(OWLLiteral readBuffer, Semantics.ReadingState results) {
            super( readBuffer, results);
        }

        /**  @return the value of {@link OWLLiteral} available in the reading buffere as a {@link Double}.*/
        public Double getDoubleReadBuffer(){
            if( getReadBuffer() == null)
                return null;
            return Double.valueOf( getReadBuffer().getLiteral());
        }
        /**  @return the value of {@link OWLLiteral} available in the reading buffere as a {@link String}.*/
        public String getStringReadBuffer(){
            if( getReadBuffer() == null)
                return null;
            return String.valueOf( getReadBuffer().getLiteral());
        }
        /**  @return the value of {@link OWLLiteral} available in the reading buffere as a {@link Long}.*/
        public Long getLongReadBuffer(){
            if( getReadBuffer() == null)
                return null;
            return Long.valueOf( getReadBuffer().getLiteral());
        }
        /**  @return the value of {@link OWLLiteral} available in the reading buffere as a {@link Float}.*/
        public Float getFloatReadBuffer(){
            if( getReadBuffer() == null)
                return null;
            return Float.valueOf( getReadBuffer().getLiteral());
        }

        /**
         * Based on {@link Semantics.ReadingState#merge(Semantics.ReadingState)}, it
         * combine two reading states in one.
         * @param otherResult the state of a reading operation to be merges with <code>this.{@link #getState()}</code>.
         * @return <code>this</code>, for chaining calls
         */
        @Override
        public DataReadingOutcome merge(Semantics.ReadingState otherResult){
            this.getState().merge( otherResult);
            return this;
        }

        /**
         * Based on {@link Semantics.ReadingState#merge(Semantics.ReadingState)}, it
         * combine two reading states in one.
         * @param otherResult the outcome containing the state ({@link #getState()})
         *                    of a reading operation to be merged with <code>this.{@link #getState()}</code>.
         * @return <code>this</code>, for chaining calls
         */
        public DataReadingOutcome merge(DataReadingOutcome otherResult){
            this.merge( otherResult.getState());
            return this;
        }
    }
    class ObjectReadingOutcome extends Semantics.ReadingOutcome<OWLNamedIndividual> {
        /**
         * Empty constructor, set null buffer and inconsistent reading stet
         * Indeed it calls {@link Semantics.ReadingOutcome#ReadingOutcome()}.
         */
        public ObjectReadingOutcome(){
            super();
        }
        /**
         * Construct by specifying all the fields of this container.
         * Indeed it calls {@link Semantics.ReadingOutcome#ReadingOutcome(Object, Semantics.ReadingState)}).
         * @param readBuffer the value read from the ontology.
         * @param results the result of the reading process.
         */
        public ObjectReadingOutcome(OWLNamedIndividual readBuffer, Semantics.ReadingState results) {
            super( readBuffer, results);
        }

        /**
         * Based on {@link Semantics.ReadingState#merge(Semantics.ReadingState)}, it
         * combine two reading states in one.
         * @param otherResult the state of a reading operation to be merges with <code>this.{@link #getState()}</code>.
         * @return <code>this</code>, for chaining calls
         */
        @Override
        public ObjectReadingOutcome merge(Semantics.ReadingState otherResult){
            this.getState().merge( otherResult);
            return this;
        }

        /**
         * Based on {@link Semantics.ReadingState#merge(Semantics.ReadingState)}, it
         * combine two reading states in one.
         * @param otherResult the outcome containing the state ({@link #getState()})
         *                    of a reading operation to be merged with <code>this.{@link #getState()}</code>.
         * @return <code>this</code>, for chaining calls
         */
        public ObjectReadingOutcome merge(ObjectReadingOutcome otherResult){
            this.merge( otherResult.getState());
            return this;
        }
    }
    class TypeReadingOutcome extends Semantics.ReadingOutcome< List< OWLClass>> {
        /**
         * Empty constructor, set null buffer and inconsistent reading stet
         * Indeed it calls {@link Semantics.ReadingOutcome#ReadingOutcome()}.
         */
        public TypeReadingOutcome(){
            super();
        }
        /**
         * Construct by specifying all the fields of this container.
         * Indeed it calls {@link Semantics.ReadingOutcome#ReadingOutcome(Object, Semantics.ReadingState)}).
         * @param readBuffer the value read from the ontology.
         * @param results the result of the reading process.
         */
        public TypeReadingOutcome(List< OWLClass> readBuffer, Semantics.ReadingState results) {
            super( readBuffer, results);
        }

        /**
         * Based on {@link Semantics.ReadingState#merge(Semantics.ReadingState)}, it
         * combine two reading states in one.
         * @param otherResult the state of a reading operation to be merges with <code>this.{@link #getState()}</code>.
         * @return <code>this</code>, for chaining calls
         */
        @Override
        public TypeReadingOutcome merge(Semantics.ReadingState otherResult){
            this.getState().merge( otherResult);
            return this;
        }

        /**
         * Based on {@link Semantics.ReadingState#merge(Semantics.ReadingState)}, it
         * combine two reading states in one.
         * @param otherResult the outcome containing the state ({@link #getState()})
         *                    of a reading operation to be merged with <code>this.{@link #getState()}</code>.
         * @return <code>this</code>, for chaining calls
         */
        public TypeReadingOutcome merge(TypeReadingOutcome otherResult){
            this.merge( otherResult.getState());
            return this;
        }
    }



    class MORSimpleDescriptor extends Logger.SITBase
            implements MORIndividualDescriptor {
        //implements SpatialDescriptor.SpatialSemantics< OWLReferences, OWLNamedIndividual, OWLClass,
        //                                                                 OWLDataProperty, OWLObjectProperty, OWLLiteral> {

        // !!    REMARK     !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        // !! move OWL named instance in a new 'OWLDescriptor' class and extend MoRDescription with it.            !!
        // !!!this improves compatibility with tracker by changing the parameter of the trackeing object class to:   !!
        // see SemanticObjectItem< OWLDescription>                                                                !!
        // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

        // internal fields
        private String individualName;
        private String ontologyName = null;
        private OWLReferences ontology;
        private OWLNamedIndividual instance;
        private List< OWLClass> types; // todo comment, setter and getter

        /**
         * Cloning constructor, create a new object as a clone of the input parameter.
         * @param descriptor the object to clone.
         */
        public MORSimpleDescriptor(MORSimpleDescriptor descriptor){
            this.individualName = descriptor.individualName;
            this.ontology = descriptor.ontology;
            this.ontologyName = descriptor.ontologyName;
            this.individualName = descriptor.individualName;
            this.types = new ArrayList<>( descriptor.types);
        }
        /**
         * Construct this object by assigning the name of the instance.
         * It does not assign any OWLReferences to this object.
         * @param individualName the name of the OWL instance describe the
         *                       semantic object related to this descriptor.
         */
        public MORSimpleDescriptor(String individualName) {
            this.individualName = individualName;
            this.types = new ArrayList<>();
        }
        public MORSimpleDescriptor(OWLNamedIndividual individual, OWLReferences ontoRef) { // todo add on extending constructors
            this.ontologyName = ontoRef.getReferenceName();
            this.ontology = ontoRef;
            this.instance = individual;
            this.individualName = ontoRef.getOWLObjectName( individual);
            this.types = new ArrayList<>();
        }
        /**
         * Fully construct the descriptor of the semantic of the object.
         * @param individualName the name of the OWL instance describe the
         *                       semantic object related to this descriptor.
         * @param ontoName the name an instantiated {@link OWLReferences} linked to the
         *                 ontology describing this object.
         */
        public MORSimpleDescriptor(String individualName, String ontoName) {
            this.individualName = individualName;
            setOntology( ontoName);
            this.types = new ArrayList<>();
        }


        @Override
        public boolean addType(OWLClass toAdd){
            return types.add( toAdd);
        }
        @Override
        public boolean removeType(OWLClass toRemove){
            return types.remove( toRemove);
        }
        @Override
        public boolean containsTypes(OWLClass toCheck){
            return types.contains( toCheck);
        }


        @Override
        public void setOntologyName(String referenceName) {
            OWLReferences ref = (OWLReferences) OWLReferencesInterface.OWLReferencesContainer.getOWLReferences( referenceName);
            if( ref != null) {
                this.ontology = ref;
                this.ontologyName = referenceName;
            } else SITBase.logERROR( "MORDescriptor cannot set an not instanced OWLReference by name: " + referenceName);
        }

        @Override
        public void setIndividualName(String individualName) {
            if( individualName != null){
                this.individualName = individualName;
                if( getOntology() != null)
                    this.instance = this.getOntology().getOWLIndividual( individualName);
                else SITBase.logWARNING( "MORDescriptor update instance name to " + individualName + ". " +
                        "But it cannot create a new individual since OWLReference is null.");
            } else SITBase.logERROR( "MORDescriptor cannot change the individual name to 'null'");
        }

        @Override
        public List<OWLClass> getTypes() {
            return this.types;
        }

        @Override
        public void setType(List<OWLClass> types) {
            this.types = types;
        }

        @Override
        public String getIndividualName() {
            return this.individualName;
        }

        @Override
        public String getOntologyName() {
            return this.ontologyName;
        }

        @Override
        public MORSimpleDescriptor copy() {
            return new MORSimpleDescriptor( this);
        }

        @Override
        public OWLReferences getOntology() {
            return this.ontology;
        }

        @Override
        public OWLNamedIndividual getInstance() {
            return this.instance;
        }

        @Override
        public void setOntology(OWLReferences ontology)  {
            this.ontology = ontology;
            this.setOntologyName( ontology.getReferenceName());
            this.setInstance( getInitialIndividual());
        }

        @Override
        public void setInstance(OWLNamedIndividual instance) {
            this.instance = instance;
        }
    }

}
