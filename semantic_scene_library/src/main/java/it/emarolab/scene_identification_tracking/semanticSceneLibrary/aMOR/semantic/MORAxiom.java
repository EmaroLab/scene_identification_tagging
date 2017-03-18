package it.emarolab.scene_identification_tracking.semanticSceneLibrary.aMOR.semantic;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.Semantic;
import org.semanticweb.owlapi.model.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by bubx on 17/03/17.
 */
public interface MORAxiom extends Semantic.Axiom{
    // todo add equal and toString

    class MORTyped
            implements Semantic.Axiom.Family<OWLClass>, MORAxiom {

        private Set<OWLClass> parents = new HashSet<>();

        public MORTyped(){}
        public MORTyped(OWLClass parent){
            this.parents.add( parent);
        }
        public MORTyped(Set<OWLClass> parents){
            this.parents = parents;
        }

        @Override
        public Set<OWLClass> getParents() {
            return parents;
        }

        @Override
        public void setParents(Set<OWLClass> parents) {
            this.parents = parents;
        }
    }

    class MORHierarchised
            extends MORTyped
            implements Semantic.Axiom.Node<OWLClass>, MORAxiom {

        private Set<OWLClass> children = new HashSet<>();

        public MORHierarchised(){
            super();
        }
        public MORHierarchised(Set<OWLClass> parents, Set<OWLClass> children) {
            super( parents);
            this.children = children;
        }

        @Override
        public Set<OWLClass> getChildren() {
            return children;
        }

        @Override
        public void setChildren(Set<OWLClass> children) {
            this.children = children;
        }
    }

    class MORLinked
            implements Semantic.Axiom.Connector<OWLObjectProperty,OWLNamedIndividual>, MORAxiom{

        private OWLObjectProperty property;
        private OWLNamedIndividual value;

        public MORLinked(){}
        public MORLinked(OWLObjectProperty property){
            this.property = property;
        }
        public MORLinked(OWLObjectProperty property, OWLNamedIndividual value){
            this.property = property;
            this.value = value;
        }

        @Override
        public OWLObjectProperty getProperty() {
            return property;
        }

        @Override
        public void setProperty(OWLObjectProperty property) {
            this.property = property;
        }

        @Override
        public OWLNamedIndividual getValue() {
            return value;
        }

        @Override
        public void setValue(OWLNamedIndividual individual) {
            this.value = value;
        }
    }
    class MORMultiLinked
            implements Semantic.Axiom.ConnectorSet<MORLinked>, MORAxiom {

        private Set<MORLinked> links = new HashSet<>();

        public MORMultiLinked(){
        }

        public Set<MORLinked> getSet(){
            return links;
        }
        @Override
        public int size() {
            return links.size();
        }
        @Override
        public boolean isEmpty() {
            return links.isEmpty();
        }
        @Override
        public boolean contains(Object o) {
            return links.contains( o);
        }
        @Override
        public Iterator<MORLinked> iterator() {
            return links.iterator();
        }
        @Override
        public Object[] toArray() {
            return links.toArray();
        }
        @Override
        public <T> T[] toArray(T[] a) {
            return links.toArray( a);
        }
        @Override
        public boolean add(MORLinked morLinkedValue) {
            return links.add( morLinkedValue);
        }
        public boolean add( OWLObjectProperty link, OWLNamedIndividual linkValue) {
            return links.add( new MORLinked( link, linkValue));
        }
        public void add(OWLObjectProperty property, Set<OWLNamedIndividual> values) {
            for( OWLNamedIndividual i : values)
                add( property, values);
        }
        @Override
        public boolean remove(Object o) {
            return links.remove( o);
        }
        @Override
        public boolean containsAll(Collection<?> c) {
            return links.containsAll( c);
        }
        @Override
        public boolean addAll(Collection<? extends MORLinked> c) {
            return links.addAll( c);
        }
        @Override
        public boolean removeAll(Collection<?> c) {
            return links.removeAll( c);
        }
        @Override
        public boolean retainAll(Collection<?> c) {
            return links.retainAll( c);
        }
        @Override
        public void clear() {
            links.clear();
        }
    }

    class MORDataValue
            implements Semantic.Axiom.Connector<OWLDataProperty,OWLLiteral>, MORAxiom{

        private OWLDataProperty property;
        private OWLLiteral value;

        public MORDataValue(){}
        public MORDataValue(OWLDataProperty property){
            this.property = property;
        }
        public MORDataValue(OWLDataProperty property, OWLLiteral value){
            this.property = property;
            this.value = value;
        }

        @Override
        public OWLDataProperty getProperty() {
            return property;
        }

        @Override
        public void setProperty(OWLDataProperty property) {
            this.property = property;
        }

        @Override
        public OWLLiteral getValue() {
            return value;
        }

        @Override
        public void setValue(OWLLiteral individual) {
            this.value = value;
        }
    }

    class MORDataValue3D
            implements Semantic.Axiom.Connector3D<OWLDataProperty,OWLLiteral>, MORAxiom{

        private OWLDataProperty propertyX, propertyY, propertyZ;
        private OWLLiteral valueX, valueY, valueZ;

        public MORDataValue3D(){}
        public MORDataValue3D(OWLReferences onto, String prefix, String xSuff, String ySuff, String zSuff){
            if (prefix == null){
                propertyX = onto.getOWLDataProperty( xSuff);
                propertyY = onto.getOWLDataProperty( ySuff);
                propertyZ = onto.getOWLDataProperty( zSuff);
            } else {
                propertyX = onto.getOWLDataProperty(prefix + xSuff);
                propertyY = onto.getOWLDataProperty(prefix + ySuff);
                propertyZ = onto.getOWLDataProperty(prefix + zSuff);
            }
        }


        @Override
        public OWLDataProperty getXproperty() {
            return propertyX;
        }
        @Override
        public void setXproperty(OWLDataProperty property) {
            this.propertyX = property;
        }
        @Override
        public OWLLiteral getXvalue() {
            return valueX;
        }
        @Override
        public void setXvalue(OWLLiteral literal) {
            this.valueX = literal;
        }

        @Override
        public OWLDataProperty getYproperty() {
            return propertyY;
        }
        @Override
        public void setYproperty(OWLDataProperty property) {
            this.propertyY = property;
        }
        @Override
        public OWLLiteral getYvalue() {
            return valueY;
        }
        @Override
        public void setYvalue(OWLLiteral literal) {
            this.valueY = literal;
        }

        @Override
        public OWLDataProperty getZproperty() {
            return propertyZ;
        }
        @Override
        public void setZproperty(OWLDataProperty property) {
            this.propertyZ = property;
        }
        @Override
        public OWLLiteral getZvalue() {
            return valueZ;
        }
        @Override
        public void setZvalue(OWLLiteral literal) {
            this.valueZ = literal;
        }
    }

    // todo MORDataClassCardinality & MORMultiDataClassCardinality
    class MORMinCardinalised
            implements Semantic.Axiom.CardinalityConnector<OWLObjectProperty, OWLClass>, MORAxiom{

        private OWLObjectProperty property;
        private OWLClass range;
        private int cardinality = 0;

        public MORMinCardinalised(){}
        public MORMinCardinalised(OWLObjectProperty property, OWLClass range, int cardinality){
            this.property = property;
            this.range = range;
            this.cardinality = cardinality;
        }

        @Override
        public OWLObjectProperty getProperty() {
            return property;
        }
        @Override
        public void setProperty(OWLObjectProperty property) {
            this.property = property;
        }

        @Override
        public OWLClass getValue() {
            return this.range;
        }
        @Override
        public void setValue(OWLClass range) {
            this.range = range;
        }

        @Override
        public int getCardinality() {
            return cardinality;
        }
        @Override
        public void setCardinality(int cardinality) {
            this.cardinality = cardinality;
        }
    }
    class MORMultiMinCardinalised
            implements Semantic.Axiom.CardinalityConnectorSet<MORMinCardinalised>, MORAxiom{

        private Set<MORMinCardinalised> links = new HashSet<>();

        public MORMultiMinCardinalised(){
        }

        @Override
        public Set<MORMinCardinalised> getSet(){
            return links;
        }

        @Override
        public int size() {
            return links.size();
        }
        @Override
        public boolean isEmpty() {
            return links.isEmpty();
        }
        @Override
        public boolean contains(Object o) {
            return links.contains(o);
        }
        @Override
        public Iterator<MORMinCardinalised> iterator() {
            return links.iterator();
        }
        @Override
        public Object[] toArray() {
            return links.toArray();
        }
        @Override
        public <T> T[] toArray(T[] a) {
            return links.toArray( a);
        }
        @Override
        public boolean add(MORMinCardinalised classCardinality) {
            return links.add( classCardinality);
        }
        public boolean add( OWLObjectProperty link, OWLClass range, int cardinality) {
            return links.add( new MORMinCardinalised(link, range, cardinality));
        }
        @Override
        public boolean remove(Object o) {
            return links.remove( o);
        }
        @Override
        public boolean containsAll(Collection<?> c) {
            return links.containsAll( c);
        }
        @Override
        public boolean addAll(Collection<? extends MORMinCardinalised> c) {
            return links.addAll( c);
        }
        @Override
        public boolean removeAll(Collection<?> c) {
            return links.removeAll( c);
        }
        @Override
        public boolean retainAll(Collection<?> c) {
            return links.retainAll( c);
        }
        @Override
        public void clear() {
            links.clear();
        }
    }
}
