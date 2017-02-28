package it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.aMORDescriptor;

import com.google.common.base.Objects;
import it.emarolab.amor.owlInterface.OWLEnquirer;
import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.Semantics;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.semantic.GeometricSemantic;
import org.semanticweb.owlapi.model.*;

import java.util.*;

// this consider unique property (replaces) todo: add for multiple
public interface MORLiteralDescriptor
            extends MORDescriptor.MORIndividualDescriptor,
            Semantics.LiteralDescriptor<OWLReferences, OWLNamedIndividual, OWLClass, OWLDataProperty, OWLLiteral> {

    default Map< String, String> getLiteralMapNames(){
        Map< String, String> out = new HashMap<>();
        for( OWLDataProperty o : getLiteralMap().keySet()){
            if ( out.containsKey( getOWLName( o)))
                logWarning( "Overwriting " + getOWLName( o) + getLiteralMap());
            out.put( getOWLName( o), getOWLName( getLiteralMap().get( o)));
        }
        return out;
    }

    default void clearLiterals(){
        for ( OWLDataProperty o : getLiteralMap().keySet())
            getLiteralMap().put( o, null);

        //getLiteralMap().clear();
    }

    default Set< OWLDataProperty> getAllLiterals(){
        Set< OWLDataProperty> out = new HashSet<>();
        for ( OWLDataProperty o : getLiteralMap().keySet())
            out.add( o);
        return out;
    }
    default Set< OWLLiteral> getAllLinkedLiterals(){
        Set< OWLLiteral> out = new HashSet<>();
        for ( OWLDataProperty o : getLiteralMap().keySet())
            out.add( getLiteral( o));
        return out;
    }

    default void linkLiteral(OWLDataProperty property){
        getLiteralMap().put( property, null);
    }
    default void linkLiteral(String propertyName){
        OWLDataProperty property = getOntology().getOWLDataProperty( propertyName);
        linkLiteral( property);
    }

    default void linkLiteral(OWLDataProperty property, OWLLiteral literal){
        getLiteralMap().put(property, literal);
    }
    default void linkLiteral(String propertyName, OWLLiteral literal){
        OWLDataProperty property = getOntology().getOWLDataProperty( propertyName);
        linkLiteral(property, literal);
    }
    default void linkLiteral(String propertyName, Object obj){
        OWLDataProperty property = getOntology().getOWLDataProperty( propertyName);
        OWLLiteral literal = getOntology().getOWLLiteral( obj);
        linkLiteral(property, literal);
    }
    default void linkLiteral(OWLDataProperty property, Object obj){
        OWLLiteral literal = getOntology().getOWLLiteral( obj);
        linkLiteral(property, literal);
    }

    default OWLLiteral removeLiteral(OWLDataProperty property) {
        return getLiteralMap().remove(property);
    }
    default OWLLiteral removeLiteral(OWLLiteral literal){
        OWLDataProperty key = getLiteralProperty( literal);
        return getLiteralMap().remove(key);
    }
    default void removeAllLiteral(){
        this.getLiteralMap().clear();
    }

    default OWLLiteral getLiteral(OWLDataProperty property){
        return getLiteralMap().get( property);
    }
    default OWLLiteral getLiteral(String property){
        return getLiteral( getOntology().getOWLDataProperty( property));
    }
    default String getLinkedDataName(OWLDataProperty property){
        return getOWLName( getLiteral( property));
    }
    default String getLinkedDataName(String property){
        return getOWLName( getLiteral( property));
    }

    default OWLDataProperty getLiteralProperty(OWLLiteral value){
        for( OWLDataProperty p : getLiteralMap().keySet()){
            if( getLiteral( p).equals( value))
                return p;
        }
        return null;
    }
    default OWLDataProperty getLiteralProperty(String value){
        return getLiteralProperty( getOntology().getOWLLiteral( value));
    }
    default String getLiteralPropertyName(OWLLiteral value){
        return getOWLName( getLiteralProperty( value));
    }
    default String getLiteralPropertyName(String value){
        return getOWLName( getLiteralProperty( value));
    }

    @Override
    default Semantics.MappingTransitions readLiteral(){
        return new MORTryRead<OWLNamedIndividual, OWLDataProperty, OWLLiteral>(MORLiteralDescriptor.this,
                LOGGING.INTENT_LITERAL) {
            @Override
            protected Semantics.MappingTransitions giveAtry() {
                getOntology().setOWLEnquirerIncludesInferences( false);
                Set<OWLEnquirer.DataPropertyRelations> relations = getOntology().getDataPropertyB2Individual( getInstance());
                getOntology().setOWLEnquirerIncludesInferences( true);

                if ( relations.isEmpty()){
                    for ( OWLDataProperty v : new HashMap<>( getLiteralMap()).keySet()){
                        Semantics.MappingIntent<OWLNamedIndividual, OWLDataProperty, OWLLiteral, Semantics.ReadingState>
                                intent = getNewIntent( v);
                        intent.setJavaValue( getLiteralMap().get( v));
                        intent.setSemanticValue( null);
                        intent.getState().asAbsent();

                        removeLiteral( v);
                    }
                } else {
                    for (OWLEnquirer.DataPropertyRelations rel : relations)
                        for (OWLLiteral v : rel.getValues()) {
                            readOne(rel.getProperty(), v); // ONLY !!!!
                            break;
                        }
                }
                return getStateTransitions();
            }
            private void readOne(OWLDataProperty property, OWLLiteral semanticValue){
                OWLLiteral javaValue = getLiteral( property);

                Semantics.MappingIntent<OWLNamedIndividual, OWLDataProperty, OWLLiteral, Semantics.ReadingState>
                        intent = getNewIntent(property);
                intent.setJavaValue( javaValue);
                intent.setSemanticValue( semanticValue);


                if( semanticValue == null) { // does not exist
                    if (javaValue == null){
                        intent.getState().asNotChanged();
                    } else {
                        removeLiteral( property);
                        intent.getState().asAbsent();
                    }
                } else {
                    if (javaValue == null){
                        linkLiteral( property, semanticValue);
                        intent.getState().asSuccess();
                    } else {
                        if ( semanticValue.equals( javaValue)){
                            intent.getState().asNotChanged();
                        } else{
                            linkLiteral( property, semanticValue);
                            intent.getState().asSuccess();
                        }
                    }
                }
            }
        }.perform();
    }

    @Override
    default Semantics.MappingTransitions writeLiteral(OWLDataProperty property) {
        return new MORTryWrite<OWLNamedIndividual, OWLDataProperty, OWLLiteral>( MORLiteralDescriptor.this,
                LOGGING.INTENT_LITERAL){
            @Override
            protected Semantics.MappingTransitions giveAtry() {
                UnsafeWriter.write( MORLiteralDescriptor.this, property, getNewIntent( property));
                return getStateTransitions();
            }
        }.perform();
    }
    @Override
    default Semantics.MappingTransitions writeLiterals() {
        return new MORTryWrite<OWLNamedIndividual, OWLDataProperty, OWLLiteral>( MORLiteralDescriptor.this,
                LOGGING.INTENT_LITERAL){
            @Override
            protected Semantics.MappingTransitions giveAtry() {
                for ( OWLDataProperty o : getLiteralMap().keySet())
                    UnsafeWriter.write( MORLiteralDescriptor.this, o, getNewIntent( o));
                return getStateTransitions();
            }
        }.perform();
    }

    class UnsafeWriter {
        private static void write(final MORLiteralDescriptor descriptor, final OWLDataProperty property,
                                                    final Semantics.MappingIntent<OWLNamedIndividual,
                                                            OWLDataProperty, OWLLiteral, Semantics.WritingState> intent){
            OWLLiteral javaValue = descriptor.getLiteral( property);
            OWLLiteral semanticValue = descriptor.getOntology().getOnlyDataPropertyB2Individual(descriptor.getInstance(), property);

            intent.setJavaValue( javaValue);
            intent.setSemanticValue( semanticValue);

            if( semanticValue == null) { // does not exist
                if (javaValue == null){
                    intent.getState().asNotChanged();
                } else {
                    // ADD
                    descriptor.getOntology().addDataPropertyB2Individual( descriptor.getInstance(), property, javaValue);
                    intent.getState().asAdded();
                }
            } else {
                if (javaValue == null){
                    // REMOVE
                    OWLLiteral queriedJavaValue = descriptor.getOntology().getOnlyDataPropertyB2Individual(descriptor.getInstance(), property);
                    descriptor.getOntology().removeDataPropertyB2Individual( descriptor.getInstance(), property, queriedJavaValue);
                    intent.getState().asRemoved();
                } else {
                    if ( semanticValue.equals( javaValue)){
                        intent.getState().asNotChanged();
                    } else{
                        // REPLACE
                        descriptor.getOntology().replaceDataProperty( descriptor.getInstance(), property, semanticValue, javaValue);
                        intent.getState().asUpdated();
                    }
                }
            }
        }
    }


    class MOR3DataProperty extends SITBase
            implements GeometricSemantic.Property3D<OWLDataProperty,OWLLiteral> {
        private MORDataProperty x, y, z;

        public MOR3DataProperty(MOR3DataProperty copy){
            this.x = copy.x.copy();
            this.y = copy.y.copy();
            this.z = copy.z.copy();
        }
        public MOR3DataProperty(){

        }
        public MOR3DataProperty(MORLiteralDescriptor descriptor, String propertyX, String propertyY, String propertyZ) {
            x = new MORDataProperty( descriptor, propertyX);
            y = new MORDataProperty( descriptor, propertyY);
            z = new MORDataProperty( descriptor, propertyZ);
        }
        public MOR3DataProperty(MORLiteralDescriptor descriptor, String propertyPrefix) {
            x = new MORDataProperty( descriptor, propertyPrefix + DATAPROPERTY.DEFAULT_3dPROPERTY_xSUFFIX);
            y = new MORDataProperty( descriptor, propertyPrefix + DATAPROPERTY.DEFAULT_3dPROPERTY_ySUFFIX);
            z = new MORDataProperty( descriptor, propertyPrefix + DATAPROPERTY.DEFAULT_3dPROPERTY_zSUFFIX);
        }
        public MOR3DataProperty(MORLiteralDescriptor descriptor, String propertyPrefix,
                                String xSuffix, String ySuffix, String zSuffix) {
            x = new MORDataProperty( descriptor, propertyPrefix + xSuffix);
            y = new MORDataProperty( descriptor, propertyPrefix + ySuffix);
            z = new MORDataProperty( descriptor, propertyPrefix + zSuffix);
        }

        public void setXYZ( MORLiteralDescriptor descriptor, OWLLiteral x, OWLLiteral y, OWLLiteral z){
            getX().setValue( descriptor, x);
            getY().setValue( descriptor, y);
            getZ().setValue( descriptor, z);
        }
        public void setXYZ( MORLiteralDescriptor descriptor, Double x, Double y, Double z){
            getX().setValue( descriptor, x);
            getY().setValue( descriptor, y);
            getZ().setValue( descriptor, z);
        }
        /*public void setXYZ( MORLiteralDescriptor descriptor, Float x, Float y, Float z) {
            getX().setValue( descriptor, x);
            getY().setValue( descriptor, y);
            getZ().setValue( descriptor, z);
        }*/

        public MORDataProperty getX() {
            return x;
        }
        public MORDataProperty getY() {
            return y;
        }
        public MORDataProperty getZ() {
            return z;
        }

        public boolean hasComponents(){
            return getX() != null & getY() != null & getZ() != null;
        }

        public boolean average( MORLiteralDescriptor descriptor, MOR3DataProperty object){
            if ( hasComponents())
                return getX().avarage( descriptor, object.getX())
                        & getY().avarage( descriptor, object.getY())
                        & getZ().avarage( descriptor, object.getZ());
            else logError( "Average only one component over 3 does not have any sense: " + this + " <-> " + object);
            return false;
        }

        @Override
        public MOR3DataProperty copy() {
            return new MOR3DataProperty(this);
        }

        @Override
        public String toString() {
            return "MOR3D{" + "x=" + x + ", y=" + y + ", z=" + z + "}";
        }
    }



    abstract class MORLiteralProperty<  P extends OWLProperty, V extends OWLObject> extends SITBase.SITBase
            implements  Semantics.Property< P, V>{

        private MORLiteralDescriptor isLiteralDescriptor(Semantics.Descriptor<?, ?> descriptor){
            if ( descriptor == null)
                logError( "MORProperty cannot deal with 'null' input descriptor.");
            try {
                return (MORLiteralDescriptor) descriptor;
            } catch ( ClassCastException e){
                logWarning( "Sorry, MORProperty can deal only with: MORLiteralDescriptor..");
                logError( e);
                return null;
            }
        }

        @Override
        public P getProperty(Semantics.Descriptor<?, ?> descriptor) {
            MORLiteralDescriptor des = isLiteralDescriptor(descriptor);
            if ( des != null)
                return getProperty( des);
            return null;
        }
        public abstract P getProperty(MORLiteralDescriptor descriptor);

        @Override
        public void setProperty(Semantics.Descriptor<?, ?> descriptor, P property) {
            MORLiteralDescriptor des = isLiteralDescriptor(descriptor);
            if ( des != null)
                setProperty( des, property);
        }
        abstract public void setProperty(MORLiteralDescriptor descriptor, P property);

        @Override
        public void setProperty(Semantics.Descriptor<?, ?> descriptor, String propertyName) {
            MORLiteralDescriptor des = isLiteralDescriptor(descriptor);
            if ( des != null)
                setProperty( des, propertyName);
        }
        abstract public void setProperty( MORLiteralDescriptor descriptor, String propertyName);

        @Override
        public void setValue(Semantics.Descriptor<?, ?> descriptor, V value) {
            MORLiteralDescriptor des = isLiteralDescriptor(descriptor);
            if ( des != null)
                setValue( des, value);
        }
        abstract public void setValue( MORLiteralDescriptor descriptor, V value);

        @Override
        public void setValue(Semantics.Descriptor<?, ?> descriptor, P property, V value) {
            MORLiteralDescriptor des = isLiteralDescriptor(descriptor);
            if ( des != null)
                setValue( des, property, value);
        }
        abstract public void setValue(MORLiteralDescriptor descriptor, P property, V value);

        @Override
        public void setValue(Semantics.Descriptor<?, ?> descriptor, String property, V value) {
            MORLiteralDescriptor des = isLiteralDescriptor(descriptor);
            if ( des != null)
                setValue( des, property, value);
        }
        abstract public void setValue(MORLiteralDescriptor descriptor, String property, V value);

    }
    class MORDataProperty extends MORLiteralProperty<  OWLDataProperty, OWLLiteral> {

        private String propertyName;
        private OWLLiteral value = null;
        private Boolean shouldBeMapped = true;

        public MORDataProperty(MORDataProperty copy) {
            this.propertyName = copy.propertyName;
            this.value = copy.value;
        }
        public MORDataProperty() {
        }
        public MORDataProperty(MORLiteralDescriptor descriptor, String propertyName) {
            this.setProperty( descriptor, propertyName);
        }
        public MORDataProperty(MORLiteralDescriptor descriptor, String propertyName, OWLLiteral value) {
            this.setProperty( descriptor, propertyName);
            this.setValue( descriptor, value);
        }

        @Override
        public String getPropertyName() {
            return this.propertyName;
        }

        @Override
        public OWLLiteral getValue() {
            return value;
        }

        @Override
        public MORDataProperty copy() {
            return new MORDataProperty( this);
        }

        @Override
        public OWLDataProperty getProperty(MORLiteralDescriptor descriptor) {
            return descriptor.getOntology().getOWLDataProperty( getPropertyName());
        }

        @Override
        public void setProperty(MORLiteralDescriptor descriptor, OWLDataProperty property) {
            setProperty( descriptor, descriptor.getOWLName( property));
        }

        @Override
        public void setProperty(MORLiteralDescriptor descriptor, String propertyName) {
            if( ! shouldBeMapped){
                this.propertyName = propertyName;
                return;
            }
            if ( propertyName != null){
                OWLLiteral oldValue = descriptor.getLiteral( this.getPropertyName());
                descriptor.removeLiteral( this.getProperty( descriptor));
                this.propertyName = propertyName;
                descriptor.linkLiteral( propertyName, oldValue); // add null value if not exists
                return;
            }
            //logWarning( "Cannot set 'null' OWLDataProperty to " + this);
        }

        @Override
        public void setValue(MORLiteralDescriptor descriptor, OWLLiteral value) {
            if( ! shouldBeMapped){
                this.value = value;
                return;
            }
            if ( this.getPropertyName() != null)
                if (descriptor.getLiteralMap().containsKey( getProperty( descriptor))) {
                    descriptor.linkLiteral(this.getPropertyName(), value);
                    this.value = value;
                    return;
                }
            //logWarning( "Cannot set value: " + value + " to " + this);
        }

        @Override
        public void setValue(MORLiteralDescriptor descriptor, OWLDataProperty property, OWLLiteral value) {
            setValue( descriptor, descriptor.getOWLName( property), value);
        }
        @Override
        public void setValue(MORLiteralDescriptor descriptor, String propertyName, OWLLiteral value) {
            if ( this.getPropertyName() != null){
                if ( getPropertyName() != null) {
                    descriptor.removeLiteral(this.getProperty(descriptor));
                }
                this.propertyName = propertyName;
                this.value = value;
                descriptor.linkLiteral( propertyName, value);
                return;
            }
            //logWarning( "Cannot set value: " + propertyName + "( " + value + ") to " + this);
        }


        public Boolean getShouldBeMapped() {
            return shouldBeMapped;
        }
        public void setMapping( MORLiteralDescriptor descriptor, Boolean shouldBeMapped, String property) {
            this.shouldBeMapped = shouldBeMapped;
            setProperty( descriptor, property);
        }

        public void setValue(MORLiteralDescriptor descriptor, Double value) {
            setValue( descriptor, descriptor.getOntology().getOWLLiteral(value));
        }
        public void setValue(MORLiteralDescriptor descriptor, Float value) {
            setValue( descriptor, descriptor.getOntology().getOWLLiteral( value));
        }
        public void setValue(MORLiteralDescriptor descriptor, String value) {
            setValue( descriptor, descriptor.getOntology().getOWLLiteral( value));
        }
        public void setValue(MORLiteralDescriptor descriptor, Integer value) {
            setValue( descriptor, descriptor.getOntology().getOWLLiteral( value));
        }
        public void setValue(MORLiteralDescriptor descriptor, Long value) {
            setValue( descriptor, descriptor.getOntology().getOWLLiteral( value));
        }
        public void setValue(MORLiteralDescriptor descriptor, Boolean value) {
            setValue( descriptor, descriptor.getOntology().getOWLLiteral( value));
        }

        public Integer getInteger() {
            if (getValue() != null)
                if ( getValue().getDatatype().isInteger())
                    return Integer.valueOf( getValue().getLiteral());
            return null;
        }
        public Boolean getBoolean() {
            if (getValue() != null)
                if ( getValue().getDatatype().isBoolean())
                    return Boolean.valueOf( getValue().getLiteral());
            return null;
        }
        public String getString() {
            if (getValue() != null)
                if ( getValue().getDatatype().isString())
                    return String.valueOf( getValue().getLiteral());
            return null;
        }
        public Float getFloat() {
            if (getValue() != null)
                if ( getValue().getDatatype().isFloat())
                    return Float.valueOf( getValue().getLiteral());
            return null;
        }
        public Double getDouble() {
            if (getValue() != null)
                if ( getValue().getDatatype().isDouble())
                    return Double.valueOf( getValue().getLiteral());
            return null;
        }
        public Long getLong() {
            if (getValue() != null)
                if ( getValue().getDatatype().toString().equals("xsd:long")){
                    String str = getValue().getLiteral();
                    str = str.replaceAll("\"","");
                    str = str.replace("xsd:long","");
                    return Long.valueOf( str);
                }
            return null;
        }

        public boolean avarage( MORLiteralDescriptor descriptor, MORDataProperty object){
            if (object == null){
                logError( "Cannot average null MORLiteral.");
                return false;
            }
            if (object.getValue() == null | this.getValue() == null){
                logError( "Cannot average null MORLiteral.");
                return false;
            }

            if ( getDouble() != null & object.getDouble() != null)
                setValue( descriptor, ( getDouble() + object.getDouble()) / 2);
            if ( getFloat() != null & object.getFloat() != null)
                setValue( descriptor, ( getFloat() + object.getFloat()) / 2);

            logWarning( "Can average only Double and Floats, not: " +
                    descriptor.getOWLName( this.getValue()) + " and " + object.getValue());
            return false;
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof MORDataProperty)) return false;
            MORDataProperty that = (MORDataProperty) o;
            return com.google.common.base.Objects.equal(getPropertyName(), that.getPropertyName()) &&
                    Objects.equal(getValue(), that.getValue());
        }
        @Override
        public int hashCode() {
            return Objects.hashCode(getPropertyName(), getValue());
        }

        @Override
        public String toString() {
            return  getPropertyName() + "(" + padString( getValue() + "", LOGGING.LENGTH_SHORT, false) + ")";
        }
    }




    class MORMultiLinkLiteralTypedIndividual extends MORTypedDescriptor.MORTypedIndividual
            implements MORLiteralDescriptor, MORSpatialDescriptor.MORMultiSpatialDescriptor,
            // redundant implementation, this is just a link to the Object interface
            GeometricSemantic.ObjectDescriptor<OWLReferences, OWLNamedIndividual, OWLClass, OWLObjectProperty, OWLDataProperty, OWLLiteral,
                    MORSpatialDescriptor.MORSpatialProperty, MORSpatialDescriptor.MORSpatialRelation, MORSpatialDescriptor.MORSpatialCollector>{

        private Map<OWLDataProperty, OWLLiteral> literalMap = new HashMap<>();
        private Map<MORSpatialDescriptor.MORSpatialProperty, MORSpatialDescriptor.MORSpatialRelation> multiLinkMap = new HashMap<>();

        public MORMultiLinkLiteralTypedIndividual(MORMultiLinkLiteralTypedIndividual copy) {
            super( copy);
            this.literalMap = new HashMap<>( copy.literalMap);
            this.multiLinkMap = new HashMap<>( copy.multiLinkMap);
        }
        public MORMultiLinkLiteralTypedIndividual() {
        }
        public MORMultiLinkLiteralTypedIndividual(String ontoName) {
            super(ontoName);
        }
        public MORMultiLinkLiteralTypedIndividual(OWLReferences ontoRef) {
            super(ontoRef);
        }
        public MORMultiLinkLiteralTypedIndividual(OWLReferences ontoRef, OWLNamedIndividual instance) {
            super(ontoRef, instance);
        }
        public MORMultiLinkLiteralTypedIndividual(String ontoRef, String instance) {
            super(ontoRef, instance);
        }

        /**
         * @return the linked instances (with the relate property) to the {@code instance}.
         */
        @Override
        public Map<OWLDataProperty, OWLLiteral> getLiteralMap() {
            return literalMap;
        }
        /*@Override
        public Map<OWLObjectProperty, Set<OWLNamedIndividual>> getMultiLinksMap() {
            return multiLinkMap;
        }
        */
        @Override
        public Map<MORSpatialDescriptor.MORSpatialProperty, MORSpatialDescriptor.MORSpatialRelation> getLinksMap() {
            return multiLinkMap;
        }

        @Override
        public MORMultiLinkLiteralTypedIndividual copy() {
            return new MORMultiLinkLiteralTypedIndividual( this);
        }

        @Override // = if has same literal map & super.equal
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof MORFullIndividual)) return false;
            if (!super.equals(o)) return false;

            MORFullIndividual that = (MORFullIndividual) o;

            boolean v = getLiteralMap() != null ? getLiteralMap().equals(that.getLiteralMap()) : that.getLiteralMap() == null;
            return v & super.equals( o);
        }
        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + (getLiteralMap() != null ? getLiteralMap().hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return super.toString()
                    + LOGGING.NEW_LINE + "\t\t\t\t, "
                    + padString( "dataProp:", LOGGING.LENGTH, true) + getLiteralMapNames()
                    + LOGGING.NEW_LINE + "\t\t\t\t, "
                    + padString( "sceneProp:", LOGGING.LENGTH, true) + getLinksMap();
        }
    }

    class MORFullIndividual extends MORMultiLinkDescriptor.MORMultiLinkedTypedIndividual
            implements MORLiteralDescriptor {

        private Map<OWLDataProperty, OWLLiteral> literalMap = new HashMap<>();

        public MORFullIndividual(MORFullIndividual copy) {
            super( copy);
            this.literalMap = copy.literalMap;
        }
        public MORFullIndividual() {
        }
        public MORFullIndividual(String ontoName) {
            super(ontoName);
        }
        public MORFullIndividual(OWLReferences ontoRef) {
            super(ontoRef);
        }
        public MORFullIndividual(OWLReferences ontoRef, OWLNamedIndividual instance) {
            super(ontoRef, instance);
        }
        public MORFullIndividual(String ontoRef, String instance) {
            super(ontoRef, instance);
        }

        /**
         * @return the linked instances (with the relate property) to the {@code instance}.
         */
        @Override
        public Map<OWLDataProperty, OWLLiteral> getLiteralMap() {
            return literalMap;
        }

        @Override
        public MORFullIndividual copy() {
            return new MORFullIndividual( this);
        }

        @Override // = if has same literal map & super.equal
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof MORFullIndividual)) return false;
            if (!super.equals(o)) return false;

            MORFullIndividual that = (MORFullIndividual) o;

            boolean v = getLiteralMap() != null ? getLiteralMap().equals(that.getLiteralMap()) : that.getLiteralMap() == null;
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
                    + padString( "dataProp:", LOGGING.LENGTH, true) + getLiteralMapNames()
                    + LOGGING.NEW_LINE
                    + padString( "objectProp:", LOGGING.LENGTH, true) + getLinkMapNames();
        }
    }
}
