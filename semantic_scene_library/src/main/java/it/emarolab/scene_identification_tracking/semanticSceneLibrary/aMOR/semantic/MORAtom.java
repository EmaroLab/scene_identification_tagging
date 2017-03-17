package it.emarolab.scene_identification_tracking.semanticSceneLibrary.aMOR.semantic;

import it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.Semantic;
import org.semanticweb.owlapi.model.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by bubx on 17/03/17.
 */
public interface MORAtom {

    class MORFamily implements Semantic.Atom.Family<OWLClass> {

        private Set<OWLClass> parents = new HashSet<>();

        public MORFamily(){}
        public MORFamily(Set<OWLClass> parent){
            this.parents = parent;
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

    class MORNode
            extends MORFamily
            implements Semantic.Atom.Node<OWLClass> {

        private Set<OWLClass> children = new HashSet<>();

        public MORNode(){
            super();
        }
        public MORNode(Set<OWLClass> parents, Set<OWLClass> children) {
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

    class MORLinkedValue
            implements Semantic.Atom2.Connector<OWLObjectProperty,OWLNamedIndividual>{

        private OWLObjectProperty property;
        private OWLNamedIndividual value;

        public MORLinkedValue(){}
        public MORLinkedValue(OWLObjectProperty property){
            this.property = property;
        }
        public MORLinkedValue(OWLObjectProperty property, OWLNamedIndividual value){
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

    class MORMultiLinkValue
            implements Semantic.Atom2.ConnectorSet<OWLObjectProperty,OWLNamedIndividual> {

        private Set<Connector<OWLObjectProperty,OWLNamedIndividual>> links = new HashSet<>();

        public MORMultiLinkValue(){

        }

        public boolean add( OWLObjectProperty link, OWLNamedIndividual linkValue,) {
            return links.add( new MORMultiLink());
        }

        public Set<Connector<OWLObjectProperty, OWLNamedIndividual>> getSet(){
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
        public Iterator<Connector<OWLObjectProperty, OWLNamedIndividual>> iterator() {
            return links.iterator();
        }
        @Override @Deprecated // use getSet instead
        public Connector<OWLObjectProperty,OWLNamedIndividual>[] toArray() {
            return null;
        }
        @Override @Deprecated // use getSet instead
        public <T> T[] toArray(T[] a) {
            return null;
        }
        @Override @Deprecated // use add(..) instead
        public boolean add(Connector<OWLObjectProperty, OWLNamedIndividual> linkValue) {
            return links.add( linkValue);
        }
        @Override
        public boolean addAll(Collection<? extends Connector<OWLObjectProperty, OWLNamedIndividual>> c) {
            return links.addAll( c);
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
            implements Semantic.Atom2.Connector<OWLDataProperty,OWLLiteral>{

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



}
