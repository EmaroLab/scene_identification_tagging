package it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.aMORDescriptor;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.Semantics;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.aMORDescriptor.aMORObject.MORPrimitive;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import java.util.*;


public interface MORTypedDescriptor
        extends MORDescriptor.MORIndividualDescriptor,
        Semantics.TypedDescriptor<OWLReferences, OWLNamedIndividual, OWLClass> {

    /*default Set<String> getTypesName(){ // todo add other names
        Set<String> names = new ArrayList<>();
        for ( OWLClass cl : getTypes())
            names.add( getOWLName( cl));
        return names;
    }*/

    @Override
    Set<OWLClass> getTypes();
    @Override
    OWLClass getBottomType();//return MORPrimitive.inferShape( getOntology(), getTypes());

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
            else Logger.logWARNING( "Cannot add type " + item + " to object: " + this);
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
            else Logger.logWARNING( "Cannot remove type " + item + " to object: " + this);
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
            else Logger.logWARNING( "Cannot check type " + item + " to object: " + this);
        }
        return flag;
    }
    // contains only that one !!!
    default boolean isAllTypes(Collection<?> toAdd){ // reduce String to OWLClasses or skip
        Set< OWLClass> clToAdd = new HashSet<>();
        for( Object item : toAdd){
            if( item instanceof String)
                clToAdd.add( getOntology().getOWLClass( ( String) item));
            else if( item instanceof OWLClass)
                clToAdd.add( ( OWLClass) item);
            else Logger.logWARNING( "Cannot check type " + item + " to object: " + this);
        }

        /* return true iff all classes toAdd are also in this.getTypes();
        for(OWLClass a : getTypes()){
            boolean find = false;
            for (OWLClass t : clToAdd)
                if( a.equals( t)){
                    find = true;
                    break;
                }
            if( ! find)
                return false;
        }
        return true;
        */
        return getTypes().equals( clToAdd);
    }

    @Override
    default Semantics.MappingTransitions readType(){
        return new MORTryRead<OWLNamedIndividual, String, Set<OWLClass>>(MORTypedDescriptor.this, LOGGING.INTENT_TYPE) {
            @Override
            protected Semantics.MappingTransitions giveAtry() {
                Set<OWLClass> javaValue = new HashSet<>(getTypes());
                Set<OWLClass> semanticValue = getOntology().getIndividualClasses(getInstance()); // it may be reduced during the computations

                Semantics.MappingIntent<OWLNamedIndividual, String, Set<OWLClass>, Semantics.ReadingState>
                        intent = getNewIntent(LOGGING.INTENT_BELONG);
                intent.setJavaValue( javaValue);
                intent.setSemanticValue( semanticValue);

                if (javaValue != null) {
                    if (semanticValue != null) {
                        // both red, nothing changed
                        if (semanticValue.equals(javaValue)) {
                            intent.getState().asNotChanged();
                        } else {
                            Set<OWLClass> javaValueCopy = new HashSet<>( javaValue);
                            for (OWLClass cl : semanticValue) {

                                Semantics.MappingIntent<OWLNamedIndividual, String, OWLClass, Semantics.ReadingState> otherIntent
                                        = new MORMappingIntent<> (MORTypedDescriptor.this, LOGGING.INTENT_BELONG_ADD, getNewState());
                                propagateAtomDescription( otherIntent);
                                getStateTransitions().add( otherIntent);
                                otherIntent.setSemanticValue( cl);
                                otherIntent.setJavaValue( null);

                                if (javaValue.contains(cl)) {
                                    // both read, not changed
                                    otherIntent.getState().asNotChanged();
                                    // it does not do nothing on the set
                                    // take track of what has not beet added, to be removed later
                                    javaValueCopy.remove(cl);
                                } else {
                                    // both read, added
                                    otherIntent.getState().asSuccess();
                                    addType(cl);
                                }
                            }
                            for (OWLClass rm : javaValueCopy) {
                                Semantics.MappingIntent<OWLNamedIndividual, String, OWLClass, Semantics.ReadingState> removeIntent
                                        = new MORMappingIntent<> (MORTypedDescriptor.this, LOGGING.INTENT_BELONG_REMOVE, getNewState());
                                propagateAtomDescription( removeIntent);
                                getStateTransitions().add( removeIntent);
                                removeIntent.setSemanticValue( null);
                                removeIntent.setJavaValue( rm);

                                //Semantics.ReadingState removeState = getTypeIntent( getOWLName(rm), javaValue, semanticValue).asAbsent();

                                // remove remaining value
                                removeIntent.getState().asAbsent();
                                removeType(rm);
                            }
                        }
                    } else {
                        // nothing read, nothing changed
                        if (javaValue.isEmpty()) {
                            intent.getState().asNotChanged();
                        } else {
                            // nothing read, remove all
                            clearTypes();
                            intent.getState().asSuccess();
                        }
                    }
                } else { // java cannot be read
                    logError("Cannot write null types for: " + getInstanceName());
                    intent.getState().asError();
                }
                return getStateTransitions();
            }
        }.perform();
    }

    @Override
    default Semantics.MappingTransitions writeType(){ // todo remove debugging info
        return new MORTryWrite<OWLNamedIndividual, String, Set<OWLClass>>(MORTypedDescriptor.this, LOGGING.INTENT_TYPE) {
            @Override
            protected Semantics.MappingTransitions giveAtry() {
                Set<OWLClass> javaValue = new HashSet<>( getTypes());
                Set<OWLClass> semanticValue = getOntology().getIndividualClasses( getInstance());

                Semantics.MappingIntent<OWLNamedIndividual, String, Set<OWLClass>, Semantics.WritingState> intent = getNewIntent("€");
                intent.setJavaValue( javaValue);
                intent.setSemanticValue( semanticValue);

                if( javaValue != null) {
                    if (semanticValue != null && !semanticValue.isEmpty()) {
                        // both red, nothing changed
                        if( semanticValue.equals( javaValue)) {
                            intent.getState().asNotChanged();
                        } else {
                            HashSet<OWLClass> semanticValueCopy = new HashSet<>(semanticValue);
                            for (OWLClass cl : javaValue){
                                Semantics.MappingIntent<OWLNamedIndividual, String, OWLClass, Semantics.WritingState> otherIntent
                                        = new MORMappingIntent<> (MORTypedDescriptor.this, LOGGING.INTENT_BELONG_ADD, getNewState());
                                propagateAtomDescription( otherIntent);
                                getStateTransitions().add( otherIntent);
                                otherIntent.setSemanticValue( null);
                                otherIntent.setJavaValue( cl);

                                if (semanticValue.contains( cl)){
                                    // both read, not changed
                                    otherIntent.getState().asNotChanged();
                                    // it does not do nothing on the set
                                    // take track of what has not beet added, to be removed later
                                    semanticValueCopy.remove( cl);
                                } else {
                                    // both read, added
                                    otherIntent.getState().asAdded();
                                    getOntology().addIndividualB2Class( getInstance(), cl);
                                }
                            }
                            for (OWLClass rm : semanticValueCopy){
                                Semantics.MappingIntent<OWLNamedIndividual, String, OWLClass, Semantics.WritingState> removeIntent
                                        = new MORMappingIntent<> (MORTypedDescriptor.this, LOGGING.INTENT_BELONG_REMOVE, getNewState());
                                propagateAtomDescription( removeIntent);
                                getStateTransitions().add( removeIntent);
                                removeIntent.setSemanticValue( rm);
                                removeIntent.setJavaValue( null);

                                // remove remaining value
                                getOntology().removeIndividualB2Class( getInstance(), rm);
                                removeIntent.getState().asRemoved();
                            }
                        }
                    } else {
                        // nothing read, nothing changed
                        if ( javaValue.isEmpty()){
                            intent.getState().asNotChanged();
                        } else {
                            // nothing read, write all
                            for (OWLClass cl : javaValue){
                                //Semantics.WritingState writeAllState = getTypeIntent( getOWLName( cl), javaValue, semanticValue);

                                Semantics.MappingIntent<OWLNamedIndividual, String, OWLClass, Semantics.WritingState> otherIntent
                                        = new MORMappingIntent<> (MORTypedDescriptor.this, LOGGING.INTENT_BELONG_ADD, getNewState());
                                propagateAtomDescription( otherIntent);
                                getStateTransitions().add( otherIntent);
                                otherIntent.setSemanticValue( null);
                                otherIntent.setJavaValue( cl);

                                otherIntent.getState().asAdded();
                                getOntology().addIndividualB2Class( getInstance(), cl);
                            }
                        }
                    }
                } else { // java cannot be read
                    logError( "Cannot write null types for: " + getInstanceName());
                    intent.getState().asError();
                }
                return getStateTransitions();
            }
        }.perform();
    }

    class MORTypedIndividual extends MORIndividual
            implements MORTypedDescriptor {

        private Set<OWLClass> types = new HashSet<>();
        private OWLClass bottomType = null;

        public MORTypedIndividual(MORTypedIndividual copy) {
            super(copy);
            this.types = new HashSet<>( copy.types);
        }
        public MORTypedIndividual() {
        }
        public MORTypedIndividual(String ontoName) {
            super(ontoName);
        }
        public MORTypedIndividual(OWLReferences ontoRef) {
            super(ontoRef);
        }
        public MORTypedIndividual(OWLReferences ontoRef, OWLNamedIndividual instance) {
            super(ontoRef, instance);
        }
        public MORTypedIndividual(String ontoRef, String instance) {
            super(ontoRef, instance);
        }

        @Override
        public boolean addType(OWLClass toAdd) {
            return types.add( toAdd);
        }

        @Override
        public boolean removeType(OWLClass toRemove) {
            return types.remove( toRemove);
        }

        @Override
        public boolean containsTypes(OWLClass toCheck) {
            return types.contains( toCheck);
        }

        /**
         * @return the types of the {@code instance}.
         */
        @Override
        public Set<OWLClass> getTypes() {
            return types;
        }

        @Override
        public OWLClass getBottomType() {
            return bottomType;
        }

        @Override
        public Semantics.MappingTransitions readType() {
            Semantics.MappingTransitions out = MORTypedDescriptor.super.readType();
            bottomType = MORPrimitive.inferShape( getOntology(), getTypes());
            return out;
        }
        @Override
        public Semantics.MappingTransitions writeType() {
            Semantics.MappingTransitions out = MORTypedDescriptor.super.writeType();
            bottomType = MORPrimitive.inferShape( getOntology(), getTypes());
            return out;
        }

        @Override
        public MORTypedIndividual copy() {
            return new MORTypedIndividual( this);
        }

        @Override // = if super is equal and types are equals
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof MORTypedIndividual)) return false;
            if (!super.equals(o)) return false;

            MORTypedIndividual that = (MORTypedIndividual) o;

            boolean v = getTypes() != null ? getTypes().equals(that.getTypes()) : that.getTypes() == null;
            return v & super.equals( o);
        }
        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + (getTypes() != null ? getTypes().hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return super.toString() + padString( " Types: ", LOGGING.LENGTH_MEDIUM, true) + getOWLName( getTypes());
        }
    }
}

