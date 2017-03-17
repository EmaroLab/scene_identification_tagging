package it.emarolab.scene_identification_tracking.semanticSceneLibrary.aMOR.semantic;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.Semantic;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import java.util.Set;

/**
 * Created by bubx on 17/03/17.
 */
public interface MORAxiom {

    class MORType
            implements Semantic.Axiom.Type<OWLReferences,OWLNamedIndividual,MORAtom.MORFamily> {

        MORAtom.MORFamily types = new MORAtom.MORFamily();

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

        MORAtom.MORNode node = new MORAtom.MORNode();

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



}
