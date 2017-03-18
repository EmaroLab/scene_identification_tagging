package it.emarolab.scene_identification_tracking.semanticSceneLibrary.aMOR.semantic;

import it.emarolab.amor.owlInterface.OWLEnquirer;
import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.Semantic;
import org.semanticweb.owlapi.model.*;

import java.util.Set;

/**
 * Created by bubx on 17/03/17.
 */
public interface MORSemantic {

    class MORType
            implements Semantic.Type<OWLReferences,OWLNamedIndividual,MORAxiom.MORTyped> {

        private MORAxiom.MORTyped types = new MORAxiom.MORTyped();

        public MORType(){}
        public MORType( Set<OWLClass> types){
            this.types.getParents().addAll( types);
        }

        @Override
        public void set( MORAxiom.MORTyped types) {
            this.types = types;
        }

        @Override
        public MORAxiom.MORTyped get() {
            return types;
        }


        @Override
        public MORAxiom.MORTyped query(OWLReferences ontology, OWLNamedIndividual instance) {
            return new MORAxiom.MORTyped( ontology.getIndividualClasses(instance));
        }

        @Override
        public <Y> void add(OWLReferences ontology, OWLNamedIndividual instance, Y type) {
            ontology.addIndividualB2Class( instance, (OWLClass) type);
        }

        @Override
        public <Y> void remove(OWLReferences ontology, OWLNamedIndividual instance, Y type) {
            ontology.removeIndividualB2Class( instance, (OWLClass) type);
        }
    }

    class MORHierarchy
            implements Semantic.Hierarchy<OWLReferences,OWLClass,MORAxiom.MORHierarchised>{

        private MORAxiom.MORHierarchised node = new MORAxiom.MORHierarchised();

        public MORHierarchy(){}
        public MORHierarchy( Set<OWLClass> parents, Set<OWLClass> children){
            this.node.setParents( parents);
            this.node.setChildren( children);
        }

        @Override
        public void set(MORAxiom.MORHierarchised Node) {
            this.node = node;
        }

        @Override
        public MORAxiom.MORHierarchised get() {
            return node;
        }

        @Override
        public MORAxiom.MORHierarchised query(OWLReferences ontology, OWLClass instance) {
            Set<OWLClass> children = ontology.getSubClassOf(instance);
            Set<OWLClass> parent = ontology.getSuperClassOf(instance);
            return new MORAxiom.MORHierarchised( parent, children);
        }

        @Override
        public <Y> void addParent(OWLReferences ontology, OWLClass instance, Y type) {
            ontology.addSubClassOf( (OWLClass) type, instance);
        }
        @Override
        public <Y> void addChild(OWLReferences ontology, OWLClass instance, Y type) {
            ontology.addSubClassOf( instance, (OWLClass) type);
        }

        @Override
        public <Y> void removeParent(OWLReferences ontology, OWLClass instance, Y type) {
            ontology.removeSubClassOf( (OWLClass) type, instance);
        }
        @Override
        public <Y> void removeChild(OWLReferences ontology, OWLClass instance, Y type) {
            ontology.removeSubClassOf( instance, (OWLClass) type);
        }
    }

    class MORClassRestrictionRestrictions
            implements Semantic.ClassRestriction<OWLReferences,OWLClass,MORAxiom.MORMultiClassCardinality> {

        MORAxiom.MORMultiClassCardinality cardinalities = new MORAxiom.MORMultiClassCardinality();

        @Override
        public void set(MORAxiom.MORMultiClassCardinality atom) {
            this.cardinalities = atom;
        }

        @Override
        public MORAxiom.MORMultiClassCardinality get() {
            return cardinalities;
        }


        @Override
        public MORAxiom.MORMultiClassCardinality query(OWLReferences ontology, OWLClass instance) {
            // todo add methods in aMOR
            return null;
        }

        @Override
        public <P, C> void add(OWLReferences ontology, OWLClass instance, P property, C range) {
            // todo to implement in aMOR
        }

        @Override
        public <P, C> void remove(OWLReferences ontology, OWLClass instance, P property, C range) {
            // todo to implement in aMOR
        }
    }

    class MORLink
            implements Semantic.Property<OWLReferences,OWLNamedIndividual,MORAxiom.MORLinked>{

        private MORAxiom.MORLinked link = new MORAxiom.MORLinked();

        public MORLink(){
        }
        public MORLink( OWLObjectProperty property, OWLNamedIndividual value){
            this.link.setProperty( property);
            this.link.setValue( value);
        }

        @Override
        public void set(MORAxiom.MORLinked link) {
            this.link = link;
        }

        @Override
        public MORAxiom.MORLinked get() {
            return link;
        }

        @Override
        public MORAxiom.MORLinked query(OWLReferences ontology, OWLNamedIndividual instance) {
            OWLNamedIndividual value = ontology.getOnlyObjectPropertyB2Individual( instance, link.getProperty());
            return new MORAxiom.MORLinked( link.getProperty(), value);
        }

        @Override
        public <P, V> void add(OWLReferences ontology, OWLNamedIndividual instance, P property, V value) {
            ontology.addObjectPropertyB2Individual( instance, (OWLObjectProperty) property, (OWLNamedIndividual) value);
        }

        @Override
        public <P, V> void remove(OWLReferences ontology, OWLNamedIndividual instance, P property, V value) {
            ontology.removeObjectPropertyB2Individual( instance, (OWLObjectProperty) property, (OWLNamedIndividual) value);
        }
    }

    class MORLinks
            implements Semantic.MultiProperty<OWLReferences,OWLNamedIndividual,MORAxiom.MORMultiLinked>{

        MORAxiom.MORMultiLinked links = new MORAxiom.MORMultiLinked();

        public MORLinks(){
        }

        @Override
        public void set(MORAxiom.MORMultiLinked links) {
            this.links = links;
        }

        @Override
        public MORAxiom.MORMultiLinked get() {
            return links;
        }


        @Override
        public MORAxiom.MORMultiLinked query(OWLReferences ontology, OWLNamedIndividual instance) {
            Set<OWLEnquirer.ObjectPropertyRelations> values = ontology.getObjectPropertyB2Individual(instance);
            MORAxiom.MORMultiLinked links = new MORAxiom.MORMultiLinked();
            for ( OWLEnquirer.ObjectPropertyRelations r : values)
                links.add( r.getProperty(), r.getValues());
            return links;
        }

        @Override
        public <P,V> void add(OWLReferences ontology, OWLNamedIndividual instance, P property, V value) {
            ontology.addObjectPropertyB2Individual(instance,
                    (OWLObjectProperty) property,(OWLNamedIndividual) value);
        }
        @Override
        public <P,V> void remove(OWLReferences ontology, OWLNamedIndividual instance, P property, V value) {
            ontology.removeObjectPropertyB2Individual(instance,
                    (OWLObjectProperty) property,(OWLNamedIndividual) value);
        }
    }

    class MORData
            implements Semantic.Property<OWLReferences,OWLNamedIndividual,MORAxiom.MORDataValue>{

        private MORAxiom.MORDataValue link = new MORAxiom.MORDataValue();

        public MORData(){
        }
        public MORData(OWLDataProperty property, OWLLiteral value){
            this.link.setProperty( property);
            this.link.setValue( value);
        }

        @Override
        public void set(MORAxiom.MORDataValue link) {
            this.link = link;
        }

        @Override
        public MORAxiom.MORDataValue get() {
            return link;
        }


        @Override
        public MORAxiom.MORDataValue query(OWLReferences ontology, OWLNamedIndividual instance) {
            OWLLiteral value = ontology.getOnlyDataPropertyB2Individual( instance, link.getProperty());
            return new MORAxiom.MORDataValue( link.getProperty(), value);
        }

        @Override
        public <P, V> void add(OWLReferences ontology, OWLNamedIndividual instance, P property, V value) {
            ontology.addDataPropertyB2Individual( instance, (OWLDataProperty) property, (OWLLiteral) value);
        }

        @Override
        public <P, V> void remove(OWLReferences ontology, OWLNamedIndividual instance, P property, V value) {
            ontology.removeDataPropertyB2Individual( instance, (OWLDataProperty) property, (OWLLiteral) value);
        }
    }

    class MORData3D
            implements Semantic.Property3D<OWLReferences,OWLNamedIndividual,MORAxiom.MORDataValue3D>{

        private MORAxiom.MORDataValue3D link3D;

        public MORData3D(){
            link3D = new MORAxiom.MORDataValue3D();
        }
        public MORData3D(OWLReferences onto, String prefix, String xSuff, String ySuff, String zSuff){
            link3D = new MORAxiom.MORDataValue3D( onto, prefix, xSuff, ySuff, zSuff);
        }

        @Override
        public void set(MORAxiom.MORDataValue3D atom) {
            link3D = atom;
        }

        @Override
        public MORAxiom.MORDataValue3D get() {
            return link3D;
        }


        @Override
        public MORAxiom.MORDataValue3D query(OWLReferences ontology, OWLNamedIndividual instance) {
            MORAxiom.MORDataValue3D queriedLink = new MORAxiom.MORDataValue3D();
            queriedLink.setXproperty( link3D.getXproperty());
            queriedLink.setXvalue(
                    ontology.getOnlyDataPropertyB2Individual( instance, queriedLink.getXproperty()));
            queriedLink.setYproperty( link3D.getYproperty());
            queriedLink.setYvalue(
                    ontology.getOnlyDataPropertyB2Individual( instance, queriedLink.getYproperty()));
            queriedLink.setZproperty( link3D.getZproperty());
            queriedLink.setZvalue(
                    ontology.getOnlyDataPropertyB2Individual( instance, queriedLink.getZproperty()));
            return queriedLink;
        }

        @Override
        public <P, V> void add(OWLReferences ontology, OWLNamedIndividual instance, P property, V value) {
            ontology.addDataPropertyB2Individual( instance, (OWLDataProperty) property, (OWLLiteral) value);

        }

        @Override
        public <P, V> void remove(OWLReferences ontology, OWLNamedIndividual instance, P property, V value) {
            ontology.removeDataPropertyB2Individual( instance, (OWLDataProperty) property, (OWLLiteral) value);
        }
    }
}
