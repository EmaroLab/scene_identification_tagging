package it.emarolab.scene_identification_tracking.semanticSceneLibrary.aMOR.descriptor;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.aMOR.semantic.MORAtom;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.aMOR.semantic.MORAxiom.MORType;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.representation.Descriptor;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

/**
 * Created by bubx on 16/03/17.
 */
public class MORTypeDescriptor
        implements Descriptor.Type<OWLReferences,OWLNamedIndividual,MORType> {

    @Override
    public SynchOutcome synchronise(OWLReferences ontology, OWLNamedIndividual instance, MORAtom.MORFamily semantic) {
        MORAtom.MORFamily read = semantic.query(ontology, instance);
        if ( )

        return null;
    }
}
