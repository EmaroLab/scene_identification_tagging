package it.emarolab.scene_identification_tracking.semanticSceneLibrary.objects;

import it.emarolab.scene_identification_tracking.semanticSceneLibrary.Logger.SITBase;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.Semantics;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.aMORDescriptor.MORSpatialDescriptor;

import java.util.HashSet;
import java.util.Set;

// remember to make all object individual disjointed for using spatial SWRL roles !!!!!!!!!!!!!
public interface SpatialSemantics<C,I,PI extends SpatialSemantics.SpatialItem<C,I>> {
    Set< PI> queryItems();

    interface SpatialDescriptor<O,I,C,PD,PO,L> extends Semantics.IndividualDescriptor<O,I,C,PD,PO,L> { //
        Set< ? extends SpatialRelation<C,I,PO,? extends SpatialSemantics.SpatialItem<C,I>>> queryBehind();
        Set< ? extends SpatialRelation<C,I,PO,? extends SpatialSemantics.SpatialItem<C,I>>> queryFront();
        Set< ? extends SpatialRelation<C,I,PO,? extends SpatialSemantics.SpatialItem<C,I>>> queryAbove();
        Set< ? extends SpatialRelation<C,I,PO,? extends SpatialSemantics.SpatialItem<C,I>>> queryBelow();
        Set< ? extends SpatialRelation<C,I,PO,? extends SpatialSemantics.SpatialItem<C,I>>> queryRight();
        Set< ? extends SpatialRelation<C,I,PO,? extends SpatialSemantics.SpatialItem<C,I>>> queryLeft();
        Set< ? extends SpatialRelation<C,I,PO,? extends SpatialSemantics.SpatialItem<C,I>>> queryAlongX();
        Set< ? extends SpatialRelation<C,I,PO,? extends SpatialSemantics.SpatialItem<C,I>>> queryAlongY();
        Set< ? extends SpatialRelation<C,I,PO,? extends SpatialSemantics.SpatialItem<C,I>>> queryAlongZ();
        //Set<? extends SpatialRelation<C,I,PO>> queryAlong(); // implemented here
        Set< ? extends SpatialRelation<C,I,PO,? extends SpatialSemantics.SpatialItem<C,I>>> queryCoaxial();
        Set< ? extends SpatialRelation<C,I,PO,? extends SpatialSemantics.SpatialItem<C,I>>> queryParallel();
        Set< ? extends SpatialRelation<C,I,PO,? extends SpatialSemantics.SpatialItem<C,I>>> queryPerpendicular();

        Set< ? extends SpatialRelation<C,I,PO,? extends SpatialSemantics.SpatialItem<C,I>>> querySpatialProperties(); // implemented here

/*
        default Set<SpatialProperty> querySpatialProperties(){
            Set<SpatialProperty> out = new HashSet<>();
            out.add( queryBehind());
            out.add( queryFront());
            out.add( queryAbove());
            out.add( queryBelow());
            out.add( queryRight());
            out.add( queryLeft());
            out.addAll( queryAlongAll());
            out.add( queryCoaxial());
            out.add( queryParallel());
            out.add( queryPerpendicular());
            return out;
        }
        default ActualRelations querySpatialRelation() {
            ActualRelations out = new ActualRelations( getInstance());
            out.setBehind( queryBehind());
            out.setFront( queryFront());
            out.setAbove( queryAbove());
            out.setBelow( queryBelow());
            out.setRight( queryRight());
            out.setLeft( queryLeft());
            out.setAlong( queryAlong());
            out.setCoaxial( queryCoaxial());
            out.setParallel( queryParallel());
            out.setPerpendicular( queryPerpendicular());
            return out;
        }
       */
    }

    /*
    abstract class SpatialProperty<P,C,I> extends SITBase implements SpatialDescriptor<P,Set<SpatialItem<C,I>>,String> {
        protected final P property;
        protected final Set<SpatialItem<C,I>> values;

        public SpatialProperty( String propertyName){
            this.property = getSpatialObjectProperty( propertyName);
            this.values = querySpatialObjectPropertyValues();
        }
        public SpatialProperty( P property){
            this.property = property;
            this.values = querySpatialObjectPropertyValues();
        }

        public P getSpatialProperty() {
            return property;
        }
        public Set<SpatialItem<C,I>> getSpatialValues() {
            return values;
        }

        public P getProperty() {
            return property;
        }

        public Set<SpatialItem<C,I>> getValues() {
            return values;
        }

        public int getCardinality() {
            return values.size();
        }

        @Override
        public String toString() {
            String valuesStr = "";
            int cnt = 0;
            for( SpatialItem<C,I> ind: values){
                valuesStr += ind;
                if( ++cnt < values.size())
                    valuesStr += ", ";
            }
            return  "property: " + simplifyOWLName((OWLObject) property) + "  values:{" + valuesStr + "}";
        }
        protected static String simplifyOWLName(OWLObject individual){
            try {
                String out = individual.toString().substring( individual.toString().lastIndexOf("#") + 1, individual.toString().length() - 1);
                if( ! out.isEmpty())
                    return out;
            } catch (Exception e){}
            return individual.toString();
        }
    }
    */




    //class SpatialRelations<C,I,P>extends HashSet<SpatialRelation<C,I,P>>{}
    abstract class SpatialRelation<C,I,P, SI extends SpatialItem<C,I>> extends SITBase implements SpatialSemantics {
        private I object;
        private C type; // shape
        P property, inverseProperty; // property is symmetric (getters returns always 'property') if 'inverseProperty' is not externally set
        Set<SI> items;

        public SpatialRelation(I object, C type, P property) { // you shouldcall queryItem()
            this.object = object;
            this.type = type;
            this.property = property;
        }

        public SpatialRelation( SpatialRelation<C,I,P,SI> copy) {
            this.object = copy.object;
            this.type = copy.type;
            this.property = copy.property;
            this.inverseProperty = copy.inverseProperty;
            this.items = new HashSet<SI>( copy.items);
        }

        public boolean isSymmetric(){
            if( inverseProperty == null)
                return true;
            return property.equals( inverseProperty);
        }
        public void setInverseProperty(P inverseProperty) {
            this.inverseProperty = inverseProperty;
        }
        public P getInverseProperty() {
            if( isSymmetric())
                return property;
            return inverseProperty;
        }

        protected void setType(C type) {
            this.type = type;
        }
        public void setItems(Set<SI> items) {
            this.items = items;
        }

        public void setProperty(P property, I object) {
            this.property = property;
            this.object = object;
            //this.items = queryItems(); // should manage also the type???
        }

        public I getObject() {
            return object;
        }
        public C getType() {
            return type;
        }
        public Set<SI> getItems() {
            return items;
        }
        public P getProperty() {
            return property;
        }

        // consider only object and property
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof SpatialRelation)) return false;
            SpatialRelation<?, ?, ?, ?> that = (SpatialRelation<?, ?, ?, ?>) o;

            if( that.getItems().contains( this.getObject()) &
                    that.getInverseProperty().equals( this.getInverseProperty()))
                return true;
            if( this.getItems().contains( that.getObject()) &
                    this.getInverseProperty().equals( that.getInverseProperty()))
                return true;
            return false;
        }
        @Override
        public int hashCode() {
            int result = getObject() != null ? getObject().hashCode() : 0;
            result = 31 * result + (getProperty() != null ? getProperty().hashCode() : 0);
            return result;
        }

        /*public Set< SpatialRelation<C,I,P>> asSet(){
            SpatialRelations<C,I,P> out = new SpatialRelations<C,I,P>();
            out.add( this);
            return out;
        }*/
    }
    abstract class SpatialItem<C,I> extends SITBase {
        private I object;
        private C type; // shape

        public SpatialItem(){
        }
        public SpatialItem(I object, C type) {
            this.object = object;
            this.type = type;
        }

        public SpatialItem(SpatialItem<C,I> copy) {
            this.object = copy.object;
            this.type = copy.type;
        }

        protected void setObject(I object) {
            this.object = object;
        }
        protected void setType(C type) {
            this.type = type;
        }

        public I getObject() {
            return object;
        }
        public C getType() {
            return type;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof SpatialItem)) return false;

            SpatialItem<?, ?> that = (SpatialItem<?, ?>) o;

            if (getObject() != null ? !getObject().equals(that.getObject()) : that.getObject() != null) return false;
            return getType() != null ? getType().equals(that.getType()) : that.getType() == null;
        }
        @Override
        public int hashCode() {
            int result = getObject() != null ? getObject().hashCode() : 0;
            result = 31 * result + (getType() != null ? getType().hashCode() : 0);
            return result;
        }

        @Override
        abstract public String toString();
    }

    /*
    abstract class Spatial3DProperty< P,C,I>{

        public static final String DEFAULT_PROP_SUFFIX_X = "X";
        public static final String DEFAULT_PROP_SUFFIX_Y = "Y";
        public static final String DEFAULT_PROP_SUFFIX_Z = "Z";

        private final SpatialProperty<P,C,I> spatialXproperty, spatialYproperty, spatialZproperty;

        public Spatial3DProperty( String propertyName) {
            this.spatialXproperty = getNewSceneProperty( propertyName + DEFAULT_PROP_SUFFIX_X);
            this.spatialYproperty = getNewSceneProperty( propertyName + DEFAULT_PROP_SUFFIX_Y);
            this.spatialZproperty = getNewSceneProperty( propertyName + DEFAULT_PROP_SUFFIX_Z);
        }
        public Spatial3DProperty( String propertyName,
                                  String xSuffix, String ySuffix, String zSuffix) {
            this.spatialXproperty = getNewSceneProperty( propertyName + xSuffix);
            this.spatialYproperty = getNewSceneProperty( propertyName + ySuffix);
            this.spatialZproperty = getNewSceneProperty( propertyName + zSuffix);
        }

        public abstract SpatialProperty<P,C,I> getNewSceneProperty(String propertyName);

        public SpatialProperty<P,C,I> getSpatialXproperty() {
            return spatialXproperty;
        }
        public P getXProperty() {
            return spatialXproperty.getSpatialProperty();
        }
        public Set<SpatialItem<C,I>> getXvalues() {
            return spatialXproperty.getSpatialValues();
        }

        public SpatialProperty<P,C,I> getSpatialYproperty() {
            return spatialYproperty;
        }
        public P getYProperty() {
            return spatialYproperty.getSpatialProperty();
        }
        public Set<SpatialItem<C,I>> getYvalues() {
            return spatialYproperty.getSpatialValues();
        }

        public SpatialProperty<P,C,I> getSpatialZproperty() {
            return spatialZproperty;
        }
        public P getZProperty() {
            return spatialZproperty.getSpatialProperty();
        }
        public Set<SpatialItem<C,I>> getZvalues() {
            return spatialZproperty.getSpatialValues();
        }

        @Override
        public String toString() {
            return "{X : " + spatialXproperty +
                    ", Y : " + spatialYproperty +
                    ", Z : " + spatialZproperty + '}';
        }
    }

    class ActualRelations< P,C,I>{
        private final I individual;
        private SpatialProperty<P,C,I> behind, front, above, below, right, left,
                coaxial, parallel, perpendicular;
        private Spatial3DProperty<P,C,I> along;

        public ActualRelations(I individual) {
            this.individual = individual;
        }

        public boolean hasBehind(){
            return ! behind.getSpatialValues().isEmpty();
        }
        public boolean hasFront(){
            return ! front.getSpatialValues().isEmpty();
        }
        public boolean hasAbove(){
            return ! above.getSpatialValues().isEmpty();
        }
        public boolean hasBelow(){
            return ! below.getSpatialValues().isEmpty();
        }
        public boolean hasRight(){
            return ! right.getSpatialValues().isEmpty();
        }
        public boolean hasLeft(){
            return ! left.getSpatialValues().isEmpty();
        }
        public boolean hasCoaxial(){
            return ! coaxial.getSpatialValues().isEmpty();
        }
        public boolean hasParallel(){
            return ! parallel.getSpatialValues().isEmpty();
        }
        public boolean hasPerpendicular(){
            return ! perpendicular.getSpatialValues().isEmpty();
        }
        public boolean hasAlong(){
            return (! along.getXvalues().isEmpty()) & (! along.getYvalues().isEmpty()) & (! along.getZvalues().isEmpty());
        }

        public void setBehind(SpatialProperty<P,C,I> behind) {
            this.behind = behind;
        }
        public void setFront(SpatialProperty<P,C,I> front) {
            this.front = front;
        }
        public void setAbove(SpatialProperty<P,C,I> above) {
            this.above = above;
        }
        public void setBelow(SpatialProperty<P,C,I> below) {
            this.below = below;
        }
        public void setRight(SpatialProperty<P,C,I> right) {
            this.right = right;
        }
        public void setLeft(SpatialProperty<P,C,I> left) {
            this.left = left;
        }
        public void setCoaxial(SpatialProperty<P,C,I> coaxial) {
            this.coaxial = coaxial;
        }
        public void setParallel(SpatialProperty<P,C,I> parallel) {
            this.parallel = parallel;
        }
        public void setPerpendicular(SpatialProperty<P,C,I> perpendicular) {
            this.perpendicular = perpendicular;
        }
        public void setAlong(Spatial3DProperty<P,C,I> along) {
            this.along = along;
        }

        public SpatialProperty<P,C,I> getBehind() {
            return behind;
        }
        public SpatialProperty<P,C,I> getFront() {
            return front;
        }
        public SpatialProperty<P,C,I> getAbove() {
            return above;
        }
        public SpatialProperty<P,C,I> getBelow() {
            return below;
        }
        public SpatialProperty<P,C,I> getRight() {
            return right;
        }
        public SpatialProperty<P,C,I> getLeft() {
            return left;
        }
        public SpatialProperty<P,C,I> getCoaxial() {
            return coaxial;
        }
        public SpatialProperty<P,C,I> getParallel() {
            return parallel;
        }
        public SpatialProperty<P,C,I> getPerpendicular() {
            return perpendicular;
        }
        public Spatial3DProperty<P,C,I> getAlong() {
            return along;
        }

        public I getIndividual() {
            return individual;
        }

        public Set< ? extends SpatialProperty<P,C,I>> getAllRealtions(){
            Set< SpatialProperty<P,C,I>> out = new HashSet<>();
            if( hasBehind())
                out.add( getBehind());
            if( hasFront())
                out.add( getFront());
            if( hasAbove())
                out.add( getAbove());
            if( hasBelow())
                out.add( getBelow());
            if( hasRight())
                out.add( getRight());
            if( hasLeft())
                out.add( getLeft());
            if( hasCoaxial())
                out.add( getCoaxial());
            if( hasParallel())
                out.add( getParallel());
            if( hasPerpendicular())
                out.add( getPerpendicular());
            if( hasAlong()){
                out.add(getAlong().getSpatialXproperty());
                out.add(getAlong().getSpatialYproperty());
                out.add(getAlong().getSpatialZproperty());
            }
            return out;
        }

        @Override
        public String toString() {
            return "Spatial Relations for: " + individual + "{\n"
                    + "\t       behind: " + behind + ";\n"
                    + "\t       front: " + front + ";\n"
                    + "\t        above: " + above + ";\n"
                    + "\t        below: " + below + ";\n"
                    + "\t        right: " + right + ";\n"
                    + "\t         left: " + left + ";\n"
                    + "\t      coaxial: " + coaxial + ";\n"
                    + "\t     parallel: " + parallel + ";\n"
                    + "\tperpendicular: " + perpendicular + ";\n"
                    + "\t        along: " + along + "\n}";
        }
    }
*/
    class DefaultNames {
        private DefaultNames(){} // not instantiable

        // Asymmetric properties
        public static final String BEHIND_PROPERTY_NAME = "isBehindOf";
        public static final String ABOVE_PROPERTY_NAME = "isAboveOf";
        public static final String RIGHT_PROPERTY_NAME = "isRightOf";
        public static final String BEHIND_INVERSE_PROPERTY_NAME = "isInFrontOf";
        public static final String ABOVE_INVERSE_PROPERTY_NAME = "isBelowOf";
        public static final String RIGHT_INVERSE_PROPERTY_NAME = "isLeftOf";

        // Symmetric properties
        public static final String ALONGX_PROPERTY_NAME = "isAlongX";
        public static final String ALONGY_PROPERTY_NAME = "isAlongY";
        public static final String ALONGZ_PROPERTY_NAME = "isAlongZ";
        public static final String COAXIAL_PROPERTY_NAME = "isCoaxialWith";
        public static final String PARALLEL_PROPERTY_NAME = "isParallelTo";
        public static final String PERPENDICULAR_PROPERTY_NAME = "isPerpendicularTo";
    }


}
