package it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.aMORDescriptor;

import com.google.common.base.Objects;
import it.emarolab.amor.owlInterface.OWLEnquirer;
import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.Semantics;
import org.semanticweb.owlapi.model.*;

import java.util.*;

// this consider unique property (replaces) todo: add for multiple
public interface MORLinkDescriptor
        extends MORDescriptor.MORIndividualDescriptor,
        Semantics.LinkedDescriptor<OWLReferences, OWLNamedIndividual, OWLClass, OWLObjectProperty, OWLNamedIndividual> {

    default Map< String, String> getLinkMapNames(){
        Map< String, String> out = new HashMap<>();
        for( OWLObjectProperty o : getLinksMap().keySet()){
            if ( out.containsKey( getOWLName( o)))
                logWarning( "Overwriting " + getOWLName( o) + getLinksMap());
            out.put( getOWLName( o), getOWLName( getLinksMap().get( o)));
        }
        return out;
    }

    default void clearLineks(){
        for ( OWLObjectProperty o : getLinksMap().keySet())
            getLinksMap().put( o, null);
        //getLinksMap().clear();
    }

    default Set< OWLObjectProperty> getAllLinks(){
        Set< OWLObjectProperty> out = new HashSet<>();
        for ( OWLObjectProperty o : getLinksMap().keySet())
            out.add( o);
        return out;
    }
    default Set< OWLNamedIndividual> getAllLinkedInstance(){
        Set< OWLNamedIndividual> out = new HashSet<>();
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

    default void linkInstance(OWLObjectProperty property, OWLNamedIndividual individual){
        getLinksMap().put(property, individual);
    }
    default void linkInstance(String propertyName, String individualName){
        OWLObjectProperty property = getOntology().getOWLObjectProperty( propertyName);
        OWLNamedIndividual individual = getOntology().getOWLIndividual( individualName);
        linkInstance(property, individual);
    }
    default void linkInstance(String propertyName, OWLNamedIndividual individual){
        OWLObjectProperty property = getOntology().getOWLObjectProperty( propertyName);
        linkInstance(property, individual);
    }
    default void linkInstance(OWLObjectProperty property, String individualName){
        OWLNamedIndividual individual = getOntology().getOWLIndividual( individualName);
        linkInstance(property, individual);
    }

    default OWLNamedIndividual removeLink(OWLObjectProperty property) {
        return getLinksMap().remove(property);
    }
    default OWLNamedIndividual removeLink(OWLNamedIndividual individual){
        OWLObjectProperty key = getLinkedProperty( individual);
        return getLinksMap().remove(key);
    }
    default void removeAllLinks(){
            this.getLinksMap().clear();
    }

    default OWLNamedIndividual getLink(OWLObjectProperty property){
        return getLinksMap().get( property);
    }
    default OWLNamedIndividual getLink(String property){
        return getLink( getOntology().getOWLObjectProperty( property));
    }
    default String getLinkedInstanceName(OWLObjectProperty property){
        return getOWLName( getLink( property));
    }
    default String getLinkedInstanceName(String property){
        return getOWLName( getLink( property));
    }

    default OWLObjectProperty getLinkedProperty( OWLNamedIndividual value){
        for( OWLObjectProperty p : getLinksMap().keySet()){
            if( getLink( p).equals( value))
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
        return new MORTryRead<OWLNamedIndividual, OWLObjectProperty, OWLNamedIndividual>(MORLinkDescriptor.this,
                LOGGING.INTENT_LINK) {
            @Override
            protected Semantics.MappingTransitions giveAtry() {
                getOntology().setOWLEnquirerIncludesInferences( false);
                Set<OWLEnquirer.ObjectPropertyRelations> relations = getOntology().getObjectPropertyB2Individual( getInstance());
                getOntology().setOWLEnquirerIncludesInferences( true);

                if ( relations.isEmpty()){
                    for ( OWLObjectProperty v : new HashMap<>( getLinksMap()).keySet()){
                        Semantics.MappingIntent<OWLNamedIndividual, OWLObjectProperty, OWLNamedIndividual, Semantics.ReadingState>
                                intent = getNewIntent(v);
                        intent.setJavaValue( getLinksMap().get( v));
                        intent.setSemanticValue( null);
                        intent.getState().asAbsent();

                        removeLink( v);
                    }
                } else {
                    for (OWLEnquirer.ObjectPropertyRelations rel : relations)
                        for (OWLNamedIndividual v : rel.getValues()) {
                            readOne(rel.getProperty(), v); // ONLY !!!!
                            break;
                        }
                }
                return getStateTransitions();
            }
            private void readOne( OWLObjectProperty property, OWLNamedIndividual semanticValue){
                OWLNamedIndividual javaValue = getLink( property);

                Semantics.MappingIntent<OWLNamedIndividual, OWLObjectProperty, OWLNamedIndividual, Semantics.ReadingState>
                        intent = getNewIntent(property);
                intent.setJavaValue( javaValue);
                intent.setSemanticValue( semanticValue);


                if( semanticValue == null) { // does not exist
                    if (javaValue == null){
                        intent.getState().asNotChanged();
                    } else {
                        removeLink( property);
                        intent.getState().asAbsent();
                    }
                } else {
                    if (javaValue == null){
                        linkInstance( property, semanticValue);
                        intent.getState().asSuccess();
                    } else {
                        if ( semanticValue.equals( javaValue)){
                            intent.getState().asNotChanged();
                        } else{
                            linkInstance( property, semanticValue);
                            intent.getState().asSuccess();
                        }
                    }
                }
            }
        }.perform();
    }

    @Override
    default Semantics.MappingTransitions writeLink(OWLObjectProperty property) {
        return new MORTryWrite<OWLNamedIndividual, OWLObjectProperty, OWLNamedIndividual>( MORLinkDescriptor.this,
                LOGGING.INTENT_LINK){
            @Override
            protected Semantics.MappingTransitions giveAtry() {
                UnsafeWriter.write( MORLinkDescriptor.this, property, getNewIntent( property));
                return getStateTransitions();
            }
        }.perform();
    }
    @Override
    default Semantics.MappingTransitions writeLinks() {
        return new MORTryWrite<OWLNamedIndividual, OWLObjectProperty, OWLNamedIndividual>( MORLinkDescriptor.this,
                LOGGING.INTENT_LINK){
            @Override
            protected Semantics.MappingTransitions giveAtry() {
                for ( OWLObjectProperty o : getLinksMap().keySet())
                    UnsafeWriter.write( MORLinkDescriptor.this, o, getNewIntent( o));
                return getStateTransitions();
            }
        }.perform();
    }

    class UnsafeWriter {
        private static void write(final MORLinkDescriptor descriptor, final OWLObjectProperty property,
                                  final Semantics.MappingIntent<OWLNamedIndividual,
                                          OWLObjectProperty, OWLNamedIndividual, Semantics.WritingState> intent){
            OWLNamedIndividual javaValue = descriptor.getLink( property);
            OWLNamedIndividual semanticValue = descriptor.getOntology().getOnlyObjectPropertyB2Individual(descriptor.getInstance(), property);

            intent.setJavaValue( javaValue);
            intent.setSemanticValue( semanticValue);

            if( semanticValue == null) { // does not exist
                if (javaValue == null){
                    intent.getState().asNotChanged();
                } else {
                    // ADD
                    descriptor.getOntology().addObjectPropertyB2Individual( descriptor.getInstance(), property, javaValue);
                    intent.getState().asAdded();
                }
            } else {
                if (javaValue == null){
                    // REMOVE
                    OWLNamedIndividual queriedJavaValue = descriptor.getOntology().getOnlyObjectPropertyB2Individual(descriptor.getInstance(), property);
                    descriptor.getOntology().removeObjectPropertyB2Individual( descriptor.getInstance(), property, queriedJavaValue);
                    intent.getState().asRemoved();
                } else {
                    if ( semanticValue.equals( javaValue)){
                        intent.getState().asNotChanged();
                    } else{
                        // REPLACE
                        descriptor.getOntology().replaceObjectProperty( descriptor.getInstance(), property, semanticValue, javaValue);
                        intent.getState().asUpdated();
                    }
                }
            }
        }
    }

    abstract class MORIndividulProperty<  V extends OWLObject> extends SITBase.SITBase
            implements  Semantics.Property< OWLObjectProperty, V>{

        private MORLinkDescriptor isLinkDescriptor(Semantics.Descriptor<?, ?> descriptor){
            if ( descriptor == null)
                logError( "MORProperty cannot deal with 'null' input descriptor.");
            try {
                return (MORLinkDescriptor) descriptor;
            } catch ( ClassCastException e){
                logWarning( "Sorry, MORProperty can deal only with: MORLinkDesriptor.");
                logError( e);
                return null;
            }
        }

        @Override
        public OWLObjectProperty getProperty(Semantics.Descriptor<?, ?> descriptor) {
            MORLinkDescriptor des = isLinkDescriptor(descriptor);
            if ( des != null)
                return getProperty( des);
            return null;
        }
        public abstract OWLObjectProperty getProperty(MORLinkDescriptor descriptor);

        @Override
        public void setProperty(Semantics.Descriptor<?, ?> descriptor, OWLObjectProperty property) {
            MORLinkDescriptor des = isLinkDescriptor(descriptor);
            if ( des != null)
                setProperty( des, property);
        }
        abstract public void setProperty(MORLinkDescriptor descriptor, OWLObjectProperty property);

        @Override
        public void setProperty(Semantics.Descriptor<?, ?> descriptor, String propertyName) {
            MORLinkDescriptor des = isLinkDescriptor(descriptor);
            if ( des != null)
                setProperty( des, propertyName);
        }
        abstract public void setProperty( MORLinkDescriptor descriptor, String propertyName);

        @Override
        public void setValue(Semantics.Descriptor<?, ?> descriptor, V value) {
            MORLinkDescriptor des = isLinkDescriptor(descriptor);
            if ( des != null)
                setValue( des, value);
        }
        abstract public void setValue( MORLinkDescriptor descriptor, V value);

        @Override
        public void setValue(Semantics.Descriptor<?, ?> descriptor, OWLObjectProperty property, V value) {
            MORLinkDescriptor des = isLinkDescriptor(descriptor);
            if ( des != null)
                setValue( des, property, value);
        }
        abstract public void setValue(MORLinkDescriptor descriptor, OWLObjectProperty property, V value);

        @Override
        public void setValue(Semantics.Descriptor<?, ?> descriptor, String property, V value) {
            MORLinkDescriptor des = isLinkDescriptor(descriptor);
            if ( des != null)
                setValue( des, property, value);
        }
        abstract public void setValue(MORLinkDescriptor descriptor, String property, V value);

    }
    class MORObjectProperty extends MORIndividulProperty< OWLNamedIndividual> {

        private String propertyName;
        private OWLNamedIndividual value = null;
        private Boolean shouldBeMapped = true;

        public MORObjectProperty(MORObjectProperty copy) {
            this.propertyName = copy.propertyName;
            this.value = copy.value;
        }
        public MORObjectProperty() {
        }
        public MORObjectProperty(MORLinkDescriptor descriptor, String propertyName) {
            this.setProperty( descriptor, propertyName);
        }
        public MORObjectProperty(MORLinkDescriptor descriptor, String propertyName, OWLNamedIndividual value) {
            this.setProperty( descriptor, propertyName);
            this.setValue( descriptor, value);
        }

        @Override
        public String getPropertyName() {
            return this.propertyName;
        }

        @Override
        public OWLNamedIndividual getValue() {
            return value;
        }

        @Override
        public MORObjectProperty copy() {
            return new MORObjectProperty( this);
        }

        @Override
        public OWLObjectProperty getProperty(MORLinkDescriptor descriptor) {
            return descriptor.getOntology().getOWLObjectProperty( getPropertyName());
        }

        @Override
        public void setProperty(MORLinkDescriptor descriptor, OWLObjectProperty property) {
            setProperty( descriptor, descriptor.getOWLName( property));
        }

        @Override
        public void setProperty(MORLinkDescriptor descriptor, String propertyName) {
            if( ! shouldBeMapped){
                this.propertyName = propertyName;
                return;
            }
            if ( propertyName != null){
                OWLNamedIndividual oldValue = descriptor.getLink( this.getPropertyName());
                descriptor.removeLink( this.getProperty( descriptor));
                this.propertyName = propertyName;
                descriptor.linkInstance( propertyName, oldValue); // add null value if not exists
                return;
            }
            //logWarning( "Cannot set 'null' OWLObjectProperty to " + this);
        }

        @Override
        public void setValue(MORLinkDescriptor descriptor, OWLNamedIndividual value) {
            if( ! shouldBeMapped){
                this.value = value;
                return;
            }
            if ( this.getPropertyName() != null)
                if (descriptor.getLinksMap().containsKey( getProperty( descriptor))) {
                    descriptor.linkInstance(this.getPropertyName(), value);
                    this.value = value;
                    return;
                }
            //logWarning( "Cannot set value: " + value + " to " + this);
        }

        @Override
        public void setValue(MORLinkDescriptor descriptor, OWLObjectProperty property, OWLNamedIndividual value) {
            setValue( descriptor, descriptor.getOWLName( property), value);
        }
        @Override
        public void setValue(MORLinkDescriptor descriptor, String propertyName, OWLNamedIndividual value) {
            if ( this.getPropertyName() != null){
                if ( getPropertyName() != null) {
                    descriptor.removeLink(this.getProperty(descriptor));
                }
                this.propertyName = propertyName;
                this.value = value;
                descriptor.linkInstance( propertyName, value);
                return;
            }
            //logWarning( "Cannot set value: " + propertyName + "( " + value + ") to " + this);
        }


        public Boolean getShouldBeMapped() {
            return shouldBeMapped;
        }
        public void setMapping( MORLinkDescriptor descriptor, Boolean shouldBeMapped, String property) {
            this.shouldBeMapped = shouldBeMapped;
            setProperty( descriptor, property);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof MORObjectProperty)) return false;
            MORObjectProperty that = (MORObjectProperty) o;
            return com.google.common.base.Objects.equal(getPropertyName(), that.getPropertyName()) &&
                    Objects.equal(getValue(), that.getValue());
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(getPropertyName(), getValue());
        }

        @Override
        public String toString() {
            return  getPropertyName() + "." + padString( getValue() + "", LOGGING.LENGTH_SHORT, false);
        }
    }

    class MORLinkedTypedIndividual extends MORTypedDescriptor.MORTypedIndividual
            implements MORLinkDescriptor {

        private Map<OWLObjectProperty, OWLNamedIndividual> linkingMap = new HashMap<>();

        public MORLinkedTypedIndividual(MORLinkedTypedIndividual copy) {
            this.linkingMap = new HashMap<>( copy.linkingMap);
        }
        public MORLinkedTypedIndividual() {
        }
        public MORLinkedTypedIndividual(String ontoName) {
            super(ontoName);
        }
        public MORLinkedTypedIndividual(OWLReferences ontoRef) {
            super(ontoRef);
        }
        public MORLinkedTypedIndividual(OWLReferences ontoRef, OWLNamedIndividual instance) {
            super(ontoRef, instance);
        }
        public MORLinkedTypedIndividual(String ontoRef, String instance) {
            super(ontoRef, instance);
        }

        @Override
        public Map<OWLObjectProperty, OWLNamedIndividual> getLinksMap() {
            return linkingMap;
        }

        @Override
        public MORLinkedTypedIndividual copy() {
            return new MORLinkedTypedIndividual( this);
        }

        @Override // = if has same linking map & super.equal
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof MORLinkedTypedIndividual)) return false;
            if (!super.equals(o)) return false;

            MORLinkedTypedIndividual that = (MORLinkedTypedIndividual) o;

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
