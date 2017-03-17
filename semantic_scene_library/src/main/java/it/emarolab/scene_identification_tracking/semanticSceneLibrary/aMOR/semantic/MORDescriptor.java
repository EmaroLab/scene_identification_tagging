package it.emarolab.scene_identification_tracking.semanticSceneLibrary.aMOR.semantic;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.Mapping;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.representation.Descriptor;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

/**
 * Created by bubx on 17/03/17.
 */
public interface MORDescriptor {

    class MORTypeDescriptor
            implements Descriptor.Type<OWLReferences,OWLNamedIndividual,MORAxiom.MORType> {
        @Override
        public SynchOutcome synchronise(OWLReferences ontology, OWLNamedIndividual instance, MORAxiom.MORType semantic) {
            MORAtom.MORFamily owl = semantic.query(ontology, instance);
            MORAtom.MORFamily java = semantic.get();
            // ...
            return new SynchOutcome(owl, new Mapping.WritingState(), new Mapping.Transitions());
        }
    }

    class MORLinkDescriptor
            implements Descriptor.Property<OWLReferences,OWLNamedIndividual,MORAxiom.MORLink> {
        @Override
        public SynchOutcome synchronise(OWLReferences ontology, OWLNamedIndividual instance, MORAxiom.MORLink semantic) {
            MORAtom.MORLinkedValue owl = semantic.query(ontology, instance);
            MORAtom.MORLinkedValue java = semantic.get();
            // ...
            return new SynchOutcome(owl, new Mapping.WritingState(), new Mapping.Transitions());
        }
    }

    class MORDataDescriptor
            implements Descriptor.Property<OWLReferences,OWLNamedIndividual,MORAxiom.MORData> {
        @Override
        public SynchOutcome synchronise(OWLReferences ontology, OWLNamedIndividual instance, MORAxiom.MORData semantic) {
            MORAtom.MORDataValue owl = semantic.query(ontology, instance);
            MORAtom.MORDataValue java = semantic.get();
            // ...
            return new SynchOutcome(owl, new Mapping.WritingState(), new Mapping.Transitions());
        }
    }
}
