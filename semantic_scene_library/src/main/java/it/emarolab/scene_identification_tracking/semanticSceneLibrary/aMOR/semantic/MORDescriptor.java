package it.emarolab.scene_identification_tracking.semanticSceneLibrary.aMOR.semantic;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.representation.Descriptor;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

/**
 * Created by bubx on 17/03/17.
 */
public interface MORDescriptor {

    class MORTypeDescriptor
            implements Descriptor.Type<OWLReferences,OWLNamedIndividual,MORSemantic.MORType> {
        @Override
        public ReadOutcome<?> read(OWLReferences ontology, OWLNamedIndividual instance, MORSemantic.MORType semantic) {
            return null;
        }

        @Override
        public WriteOutcome<?> write(OWLReferences ontology, OWLNamedIndividual instance, MORSemantic.MORType semantic) {

            return null;
        }
        /*@Override
        public SynchOutcome synchronise(OWLReferences ontology, OWLNamedIndividual instance, MORAxiom.MORType semantic) {
            MORAtom.MORTyped owl = semantic.query(ontology, instance);
            MORAtom.MORTyped java = semantic.get();
            .ex. semantic.add( ontology, instance, semantic.get().getParents().toArray()[0]);
            semantic.add(ontology, instance,

            // ...
            return new SynchOutcome(owl, new Mapping.WritingState(), new Mapping.Transitions());
        }*/
    }

    class MORLinkDescriptor
            implements Descriptor.Property<OWLReferences,OWLNamedIndividual,MORSemantic.MORLink> {

    }

    class MORDataDescriptor
            implements Descriptor.Property<OWLReferences,OWLNamedIndividual,MORSemantic.MORData> {

    }


}
