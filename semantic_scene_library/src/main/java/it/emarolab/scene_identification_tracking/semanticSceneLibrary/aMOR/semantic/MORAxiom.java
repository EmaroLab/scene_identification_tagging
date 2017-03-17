package it.emarolab.scene_identification_tracking.semanticSceneLibrary.aMOR.semantic;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.Semantic;
import org.semanticweb.owlapi.model.*;

import java.util.Set;

/**
 * Created by bubx on 17/03/17.
 */
public interface MORAxiom {

    class MORType
            implements Semantic.Axiom.Type<OWLReferences,OWLNamedIndividual,MORAtom.MORFamily> {

        private MORAtom.MORFamily types = new MORAtom.MORFamily();

        public MORType(){}
        public MORType( Set<OWLClass> types){
            this.types.getParents().addAll( types);
        }

        @Override
        public MORAtom.MORFamily query(OWLReferences ontology, OWLNamedIndividual instance) {
            return new MORAtom.MORFamily( ontology.getIndividualClasses(instance));
        }

        @Override
        public void set( MORAtom.MORFamily types) {
            this.types = types;
        }

        @Override
        public MORAtom.MORFamily get() {
            return types;
        }
    }

    class MORHierarchy
            implements Semantic.Axiom.Type<OWLReferences,OWLClass,MORAtom.MORNode>{

        private MORAtom.MORNode node = new MORAtom.MORNode();

        public MORHierarchy(){}
        public MORHierarchy( Set<OWLClass> parents, Set<OWLClass> children){
            this.node.setParents( parents);
            this.node.setChildren( children);
        }

        @Override
        public MORAtom.MORNode query(OWLReferences ontology, OWLClass instance) {
            Set<OWLClass> children = ontology.getSubClassOf(instance);
            Set<OWLClass> parent = ontology.getSuperClassOf(instance);
            return new MORAtom.MORNode( parent, children);
        }

        @Override
        public void set(MORAtom.MORNode Node) {
            this.node = node;
        }

        @Override
        public MORAtom.MORNode get() {
            return node;
        }
    }

    // todo MORClass

    class MORLink
            implements Semantic.Axiom.Property<OWLReferences,OWLNamedIndividual,MORAtom.MORLinkedValue>{

        private MORAtom.MORLinkedValue link = new MORAtom.MORLinkedValue();

        public MORLink(){
        }
        public MORLink( OWLObjectProperty property, OWLNamedIndividual value){
            this.link.setProperty( property);
            this.link.setValue( value);
        }

        @Override
        public MORAtom.MORLinkedValue query(OWLReferences ontology, OWLNamedIndividual instance) {
            OWLNamedIndividual value = ontology.getOnlyObjectPropertyB2Individual( instance, link.getProperty());
            return new MORAtom.MORLinkedValue( link.getProperty(), value);
        }

        @Override
        public void set(MORAtom.MORLinkedValue link) {
            this.link = link;
        }

        @Override
        public MORAtom.MORLinkedValue get() {
            return link;
        }
    }

    class MORMultiLink
            implements Semantic.Atom2

    class MORData
            implements Semantic.Axiom.Property<OWLReferences,OWLNamedIndividual,MORAtom.MORDataValue>{

        private MORAtom.MORDataValue link = new MORAtom.MORDataValue();

        public MORData(){
        }
        public MORData(OWLDataProperty property, OWLLiteral value){
            this.link.setProperty( property);
            this.link.setValue( value);
        }

        @Override
        public MORAtom.MORDataValue query(OWLReferences ontology, OWLNamedIndividual instance) {
            OWLLiteral value = ontology.getOnlyDataPropertyB2Individual( instance, link.getProperty());
            return new MORAtom.MORDataValue( link.getProperty(), value);
        }

        @Override
        public void set(MORAtom.MORDataValue link) {
            this.link = link;
        }

        @Override
        public MORAtom.MORDataValue get() {
            return link;
        }
    }



}
