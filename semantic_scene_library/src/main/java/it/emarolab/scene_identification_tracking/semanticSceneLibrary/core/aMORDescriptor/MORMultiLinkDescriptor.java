package it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.aMORDescriptor;

import it.emarolab.amor.owlInterface.OWLEnquirer;
import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.Semantics;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import java.util.*;


// this consider unique property (replaces) todo: add for multiple
public interface MORMultiLinkDescriptor
            extends MORDescriptor.MORIndividualDescriptor,
            Semantics.LinkedDescriptor<OWLReferences, OWLNamedIndividual, OWLClass, OWLObjectProperty, Set<OWLNamedIndividual>> {

    default Map< String, Set<String>> getLinkMapNames(){
        Map< String, Set<String>> out = new HashMap<>();
        for( OWLObjectProperty o : getLinksMap().keySet()){
            if ( out.containsKey( getOWLName( o)))
                logWarning( "Overwriting " + getOWLName( o) + getLinksMap());
            out.put( getOWLName( o), getOWLName(getLinksMap().get(o)));
        }
        return out;
    }

    default void clearLinks(){
        for ( OWLObjectProperty o : getLinksMap().keySet())
            getLinksMap().put( o, null);
    }

    default Set< OWLObjectProperty> getAllLinks(){
        Set< OWLObjectProperty> out = new HashSet<>();
        for ( OWLObjectProperty o : getLinksMap().keySet())
            out.add( o);
        return out;
    }
    default Set< Set<OWLNamedIndividual>> getAllLinkedInstance(){
        Set< Set< OWLNamedIndividual>> out = new HashSet<>();
        for ( OWLObjectProperty o : getLinksMap().keySet())
            out.add( getLink( o));
        return out;
    }

    default void linkInstance(OWLObjectProperty property){
        getLinksMap().put( property, null);
    }
    default void linkInstance(String propertyName){
        OWLObjectProperty property = getOntology().getOWLObjectProperty( propertyName);
        linkInstance( property);
    }

    default void linkInstance(OWLObjectProperty property, Set<OWLNamedIndividual> individual){
        if ( ! getLinksMap().containsKey( property))
            getLinksMap().put(property, individual);
        else {
            Set<OWLNamedIndividual> value = getLinksMap().get(property);
            value.addAll( individual);
            getLinksMap().put( property, value);
        }
    }
    default void linkInstance(String propertyName, Set<String> individualName){
        OWLObjectProperty property = getOntology().getOWLObjectProperty( propertyName);
        Set<OWLNamedIndividual> individual = getOntology().getOWLIndividual( individualName);
        linkInstance(property, individual);
    }

    default void linkInstance(OWLObjectProperty property, OWLNamedIndividual individual){
        Set< OWLNamedIndividual> inds = new HashSet<>();
        inds.add( individual);
        linkInstance( property, inds);
    }
    default void linkInstance(String propertyName, String individualName){
        Set<String> inds = new HashSet<>();
        inds.add( individualName);
        linkInstance( propertyName, inds);
    }

    default Set<OWLNamedIndividual> removeLink(OWLObjectProperty property) {
        return getLinksMap().remove(property);
    }
    default Set<OWLNamedIndividual> removeLink(Set<OWLNamedIndividual> individual){
        OWLObjectProperty key = getLinkedProperty( individual);
        return getLinksMap().remove(key);
    }
    default void removeAllLinks(){
        this.getLinksMap().clear();
    }

    default Set<OWLNamedIndividual> getLink(OWLObjectProperty property){
        return getLinksMap().get( property);
    }
    default Set<OWLNamedIndividual> getLink(String property){
        return getLink( getOntology().getOWLObjectProperty( property));
    }
    default Set<String> getLinkedInstanceName(OWLObjectProperty property){
        return getOWLName( getLink( property));
    }
    default Set<String> getLinkedInstanceName(String property){
        return getOWLName( getLink( property));
    }

    default OWLObjectProperty getLinkedProperty( OWLNamedIndividual value){
        for( OWLObjectProperty p : getLinksMap().keySet()){
            if( getLink( p).contains( value))
                return p;
        }
        return null;
    }
    default OWLObjectProperty getLinkedProperty( Set<OWLNamedIndividual> value){
        for( OWLObjectProperty p : getLinksMap().keySet()){
            if( getLink( p).containsAll( value))
                return p;
        }
        return null;
    }
    default OWLObjectProperty getLinkedProperty( String value){
        return getLinkedProperty( getOntology().getOWLIndividual( value));
    }

    default String getLinkedPropertyName( OWLNamedIndividual value){
        return getOWLName( getLinkedProperty( value));
    }
    default String getLinkedPropertyName( String value){
        return getOWLName( getLinkedProperty( value));
    }

    @Override
    default Semantics.MappingTransitions readLink(){
        return new MORTryRead<OWLNamedIndividual, OWLObjectProperty, Set<OWLNamedIndividual>>(MORMultiLinkDescriptor.this,
                LOGGING.INTENT_LINK_MULTI) {
            @Override
            protected Semantics.MappingTransitions giveAtry() {
                getOntology().setOWLEnquirerIncludesInferences( false);
                Set<OWLEnquirer.ObjectPropertyRelations> relations = getOntology().getObjectPropertyB2Individual( getInstance());
                getOntology().setOWLEnquirerIncludesInferences( true);

                if ( relations.isEmpty()){
                    for ( OWLObjectProperty v : new HashMap<>( getLinksMap()).keySet()){

                        Semantics.MappingIntent<OWLNamedIndividual, OWLObjectProperty, Set<OWLNamedIndividual>, Semantics.ReadingState>
                                intent = getNewIntent(v);
                        intent.setJavaValue( getLinksMap().get( v));
                        intent.setSemanticValue( null);
                        intent.getState().asAbsent();

                        removeLink( v);
                    }
                } else {
                    for (OWLEnquirer.ObjectPropertyRelations rel : relations)
                        readSet(rel.getProperty(), rel.getValues());
                }
                return getStateTransitions();
            }
            private void readSet(OWLObjectProperty property, Set< OWLNamedIndividual> semanticValue){
                Set<OWLNamedIndividual> javaValue = getLink( property);

                Semantics.MappingIntent<OWLNamedIndividual, OWLObjectProperty, Set<OWLNamedIndividual>, Semantics.ReadingState>
                        intent = getNewIntent(property);
                intent.setJavaValue( javaValue);
                intent.setSemanticValue( semanticValue);

                if (javaValue != null) {
                    if (semanticValue != null) {
                        // both red, nothing changed
                        if (semanticValue.equals(javaValue)) {
                            intent.getState().asNotChanged();
                        } else {
                            Set<OWLNamedIndividual> javaValueCopy = new HashSet<>( javaValue);
                            for (OWLNamedIndividual cl : semanticValue) {

                                Semantics.MappingIntent<OWLNamedIndividual, OWLObjectProperty, OWLNamedIndividual, Semantics.ReadingState> otherIntent
                                        = new MORMappingIntent<> (MORMultiLinkDescriptor.this, property, getNewState());
                                propagateAtomDescription( otherIntent, LOGGING.INTENT_LINK_MULTI_REMOVE);
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
                                    linkInstance( property, cl);
                                }
                            }
                            for (OWLNamedIndividual rm : javaValueCopy) {
                                Semantics.MappingIntent<OWLNamedIndividual, OWLObjectProperty, OWLNamedIndividual, Semantics.ReadingState> removeIntent
                                        = new MORMappingIntent<> (MORMultiLinkDescriptor.this, property, getNewState());
                                propagateAtomDescription( removeIntent, LOGGING.INTENT_LINK_MULTI_REMOVE);
                                getStateTransitions().add( removeIntent);
                                removeIntent.setSemanticValue( null);
                                removeIntent.setJavaValue( rm);

                                //Semantics.ReadingState removeState = getTypeIntent( getOWLName(rm), javaValue, semanticValue).asAbsent();

                                // remove remaining value
                                removeIntent.getState().asAbsent();
                                getLink( property).remove( rm);//removeLink( property, javaValueCopy);
                            }
                        }
                    } else {
                        // nothing read, nothing changed
                        if (javaValue.isEmpty()) {
                            intent.getState().asNotChanged();
                        } else {
                            // nothing read, remove all
                            clearLinks();
                            intent.getState().asSuccess();
                        }
                    }
                } else { // java cannot be read
                    logError("Cannot write null types for: " + getInstanceName());
                    intent.getState().asError();
                }
            }
        }.perform();
    }

    @Override
    default Semantics.MappingTransitions writeLink(OWLObjectProperty property) {
        return new MORTryWrite<OWLNamedIndividual, OWLObjectProperty, Set<OWLNamedIndividual>>( MORMultiLinkDescriptor.this,
                LOGGING.INTENT_LINK_MULTI){
            @Override
            protected Semantics.MappingTransitions giveAtry() {
                return UnsafeWriter.write( MORMultiLinkDescriptor.this, property, getNewIntent( property));
                //return getStateTransitions();
            }
        }.perform();
    }
    @Override
    default Semantics.MappingTransitions writeLinks() {
        return new MORTryWrite<OWLNamedIndividual, OWLObjectProperty, Set<OWLNamedIndividual>>( MORMultiLinkDescriptor.this,
                LOGGING.INTENT_LINK){
            @Override
            protected Semantics.MappingTransitions giveAtry() {
                Semantics.MappingTransitions out = new Semantics.MappingTransitions();
                for ( OWLObjectProperty o : getLinksMap().keySet())
                    out.addAll( UnsafeWriter.write( MORMultiLinkDescriptor.this, o, getNewIntent( o)));
                return out;//getStateTransitions();
            }
        }.perform();
    }

    class UnsafeWriter {
        private static Semantics.MappingTransitions write(final MORMultiLinkDescriptor descriptor, final OWLObjectProperty property,
                                  final Semantics.MappingIntent<OWLNamedIndividual,
                                          OWLObjectProperty, Set<OWLNamedIndividual>, Semantics.WritingState> intent){

            Set<OWLNamedIndividual> javaValue = descriptor.getLink(property);
            Set<OWLNamedIndividual> semanticValue = descriptor.getOntology().getObjectPropertyB2Individual(descriptor.getInstance(), property);

            intent.setJavaValue( javaValue);
            intent.setSemanticValue( semanticValue);

            Semantics.MappingTransitions out = new Semantics.MappingTransitions( intent);

            if( javaValue != null) {
                if (semanticValue != null && !semanticValue.isEmpty()) {
                    // both red, nothing changed
                    if( semanticValue.equals( javaValue)) {
                        intent.getState().asNotChanged();
                    } else {
                        HashSet< OWLNamedIndividual> semanticValueCopy = new HashSet<>(semanticValue);
                        for ( OWLNamedIndividual cl : javaValue){
                            Semantics.MappingIntent<OWLNamedIndividual, OWLObjectProperty, OWLNamedIndividual, Semantics.WritingState> otherIntent
                                    = new MORMappingIntent<> ( descriptor, property, new Semantics.WritingState());
                            otherIntent.setDescription( intent.getAtom().toString());
                            out.add( otherIntent);
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
                                descriptor.getOntology().addObjectPropertyB2Individual( descriptor.getInstance(), property, cl);
                            }
                        }
                        for (OWLNamedIndividual rm : semanticValueCopy){
                            Semantics.MappingIntent<OWLNamedIndividual, OWLObjectProperty, OWLNamedIndividual, Semantics.WritingState> otherIntent
                                    = new MORMappingIntent<> ( descriptor, property, new Semantics.WritingState());
                            otherIntent.setDescription( intent.getAtom().toString());
                            out.add( otherIntent);
                            otherIntent.setSemanticValue( null);
                            otherIntent.setJavaValue( rm);

                            // remove remaining value
                            descriptor.getOntology().removeObjectPropertyB2Individual( descriptor.getInstance(), property, rm);
                            otherIntent.getState().asRemoved();
                        }
                    }
                } else {
                    // nothing read, nothing changed
                    if ( javaValue.isEmpty()){
                        intent.getState().asNotChanged();
                    } else {
                        // nothing read, write all
                        for (OWLNamedIndividual cl : javaValue){
                            Semantics.MappingIntent<OWLNamedIndividual, OWLObjectProperty, OWLNamedIndividual, Semantics.WritingState> otherIntent
                                    = new MORMappingIntent<> ( descriptor, property, new Semantics.WritingState());
                            otherIntent.setDescription( intent.getAtom().toString());
                            out.add( otherIntent);
                            otherIntent.setSemanticValue( null);
                            otherIntent.setJavaValue( cl);

                            otherIntent.getState().asAdded();
                            descriptor.getOntology().addObjectPropertyB2Individual( descriptor.getInstance(), property, cl);
                        }
                    }
                }
            } else { // java cannot be read
                Logger.logERROR( "Cannot write null types for: " + descriptor.getInstanceName());
                intent.getState().asError();
            }
            return out;
        }
    }

    abstract class MORMultiIndividualProperty<  V extends Set<?>> extends SITBase.SITBase
            implements  Semantics.Property< OWLObjectProperty, V>{

        private MORMultiLinkDescriptor isLinkDescriptor(Semantics.Descriptor<?, ?> descriptor){
            if ( descriptor == null)
                logError( "MORProperty cannot deal with 'null' input descriptor.");
            try {
                return (MORMultiLinkDescriptor) descriptor;
            } catch ( ClassCastException e){
                logWarning( "Sorry, MORProperty can deal only with: MORMultiLinkDescriptor.");
                logError( e);
                return null;
            }
        }

        @Override
        public OWLObjectProperty getProperty(Semantics.Descriptor<?, ?> descriptor) {
            MORMultiLinkDescriptor des = isLinkDescriptor(descriptor);
            if ( des != null)
                return getProperty( des);
            return null;
        }
        public abstract OWLObjectProperty getProperty(MORMultiLinkDescriptor descriptor);

        @Override
        public void setProperty(Semantics.Descriptor<?, ?> descriptor, OWLObjectProperty property) {
            MORMultiLinkDescriptor des = isLinkDescriptor(descriptor);
            if ( des != null)
                setProperty( des, property);
        }
        abstract public void setProperty(MORMultiLinkDescriptor descriptor, OWLObjectProperty property);

        @Override
        public void setProperty(Semantics.Descriptor<?, ?> descriptor, String propertyName) {
            MORMultiLinkDescriptor des = isLinkDescriptor(descriptor);
            if ( des != null)
                setProperty( des, propertyName);
        }
        abstract public void setProperty( MORMultiLinkDescriptor descriptor, String propertyName);

        @Override
        public void setValue(Semantics.Descriptor<?, ?> descriptor, V value) {
            MORMultiLinkDescriptor des = isLinkDescriptor(descriptor);
            if ( des != null)
                setValue( des, value);
        }
        abstract public void setValue( MORMultiLinkDescriptor descriptor, V value);

        @Override
        public void setValue(Semantics.Descriptor<?, ?> descriptor, OWLObjectProperty property, V value) {
            MORMultiLinkDescriptor des = isLinkDescriptor(descriptor);
            if ( des != null)
                setValue( des, property, value);
        }
        abstract public void setValue(MORMultiLinkDescriptor descriptor, OWLObjectProperty property, V value);

        @Override
        public void setValue(Semantics.Descriptor<?, ?> descriptor, String property, V value) {
            MORMultiLinkDescriptor des = isLinkDescriptor(descriptor);
            if ( des != null)
                setValue( des, property, value);
        }
        abstract public void setValue(MORMultiLinkDescriptor descriptor, String property, V value);

    }
    class MORMultiObjectProperty extends MORMultiIndividualProperty< Set<OWLNamedIndividual>> {

        private String propertyName;
        private Set< OWLNamedIndividual> value = null;
        private Boolean shouldBeMapped = true;

        public MORMultiObjectProperty( MORMultiObjectProperty copy) {
            this.propertyName = copy.propertyName;
            this.value = new HashSet<>( copy.value);
        }
        public MORMultiObjectProperty() {
        }
        public MORMultiObjectProperty( MORMultiLinkDescriptor descriptor, String propertyName) {
            this.setProperty( descriptor, propertyName);
        }
        public MORMultiObjectProperty(MORMultiLinkDescriptor descriptor, String propertyName, Set<OWLNamedIndividual> value) {
            this.setProperty( descriptor, propertyName);
            this.setValue( descriptor, value);
        }

        @Override
        public String getPropertyName() {
            return this.propertyName;
        }

        @Override
        public Set< OWLNamedIndividual> getValue() {
            return value;
        }

        @Override
        public MORMultiObjectProperty copy() {
            return new MORMultiObjectProperty( this);
        }

        @Override
        public OWLObjectProperty getProperty(MORMultiLinkDescriptor descriptor) {
            return descriptor.getOntology().getOWLObjectProperty( getPropertyName());
        }

        @Override
        public void setProperty(MORMultiLinkDescriptor descriptor, OWLObjectProperty property) {
            setProperty( descriptor, descriptor.getOWLName( property));
        }

        @Override
        public void setProperty(MORMultiLinkDescriptor descriptor, String propertyName) {
            if( ! shouldBeMapped){
                this.propertyName = propertyName;
                return;
            }
            if ( propertyName != null){
                Set<OWLNamedIndividual> oldValue = descriptor.getLink( this.getPropertyName());
                descriptor.removeLink( this.getProperty( descriptor));
                this.propertyName = propertyName;
                descriptor.linkInstance( getProperty( descriptor), oldValue); // add null value if not exists
                return;
            }
            //logWarning( "Cannot set 'null' OWLObjectProperty to " + this);
        }

        @Override
        public void setValue(MORMultiLinkDescriptor descriptor, Set<OWLNamedIndividual> value) {
            if( ! shouldBeMapped){
                this.value = value;
                return;
            }
            if ( this.getPropertyName() != null)
                if (descriptor.getLinksMap().containsKey( getProperty( descriptor))) {
                    descriptor.linkInstance( getProperty( descriptor), value);
                    this.value = value;
                    return;
                }
            //logWarning( "Cannot set value: " + value + " to " + this);
        }

        @Override
        public void setValue(MORMultiLinkDescriptor descriptor, OWLObjectProperty property, Set< OWLNamedIndividual> value) {
            setValue( descriptor, descriptor.getOWLName( property), value);
        }
        @Override
        public void setValue(MORMultiLinkDescriptor descriptor, String propertyName, Set<OWLNamedIndividual> value) {
            if ( this.getPropertyName() != null){
                if ( getPropertyName() != null) {
                    descriptor.removeLink(this.getProperty(descriptor));
                }
                this.propertyName = propertyName;
                this.value = value;
                descriptor.linkInstance( getProperty( descriptor), value);
                return;
            }
            //logWarning( "Cannot set value: " + propertyName + "( " + value + ") to " + this);
        }


        public Boolean getShouldBeMapped() {
            return shouldBeMapped;
        }
        public void setMapping( MORMultiLinkDescriptor descriptor, Boolean shouldBeMapped, String property) {
            this.shouldBeMapped = shouldBeMapped;
            setProperty( descriptor, property);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof MORMultiObjectProperty)) return false;
            MORMultiObjectProperty that = (MORMultiObjectProperty) o;
            return com.google.common.base.Objects.equal(getPropertyName(), that.getPropertyName()) &&
                    com.google.common.base.Objects.equal(getValue(), that.getValue());
        }

        @Override
        public int hashCode() {
            return com.google.common.base.Objects.hashCode(getPropertyName(), getValue());
        }

        @Override
        public String toString() {
            return  getPropertyName() + "." + padString( getValue() + "", LOGGING.LENGTH_SHORT, false);
        }
    }

    class MORMultiLinkedTypedIndividual extends MORTypedDescriptor.MORTypedIndividual
            implements MORMultiLinkDescriptor {

        private Map<OWLObjectProperty, Set< OWLNamedIndividual>> linkingMap = new HashMap<>();

        public MORMultiLinkedTypedIndividual(MORMultiLinkedTypedIndividual copy) {
            this.linkingMap = new HashMap<>( copy.linkingMap);
        }
        public MORMultiLinkedTypedIndividual() {
        }
        public MORMultiLinkedTypedIndividual(String ontoName) {
            super(ontoName);
        }
        public MORMultiLinkedTypedIndividual(OWLReferences ontoRef) {
            super(ontoRef);
        }
        public MORMultiLinkedTypedIndividual(OWLReferences ontoRef, OWLNamedIndividual instance) {
            super(ontoRef, instance);
        }
        public MORMultiLinkedTypedIndividual(String ontoRef, String instance) {
            super(ontoRef, instance);
        }

        /**
         * @return the linked instances (with the relate property) to the {@code instance}.
         */
        @Override
        public Map<OWLObjectProperty, Set< OWLNamedIndividual>> getLinksMap() {
            return linkingMap;
        }

        @Override
        public MORMultiLinkedTypedIndividual copy() {
            return new MORMultiLinkedTypedIndividual( this);
        }

        @Override // = if has same linking map & super.equal
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof MORMultiLinkedTypedIndividual)) return false;
            if (!super.equals(o)) return false;

            MORMultiLinkedTypedIndividual that = (MORMultiLinkedTypedIndividual) o;

            boolean v = getLinksMap() != null ? getLinksMap().equals(that.getLinksMap()) : that.getLinksMap() == null;
            return v & super.equals( o);
        }
        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + (getLinksMap() != null ? getLinksMap().hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return super.toString()
                    + padString( " objProp:", LOGGING.LENGTH_MEDIUM, true) + getLinkMapNames();
        }
    }

}