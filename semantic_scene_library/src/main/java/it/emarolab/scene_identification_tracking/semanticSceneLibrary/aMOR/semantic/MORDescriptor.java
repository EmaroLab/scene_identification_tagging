package it.emarolab.scene_identification_tracking.semanticSceneLibrary.aMOR.semantic;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.synchronisation.Descriptor;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.synchronisation.Mapping;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.reasoner.OWLReasonerRuntimeException;

/**
 * Created by bubx on 17/03/17.
 */
public interface MORDescriptor {

    class MORTypeDescriptor
            implements Descriptor.Typing<OWLReferences,OWLNamedIndividual,MORSemantic.MORType> {
        @Override
        public ReadOutcome<OWLNamedIndividual, MORAxiom.MORTyped>
                read(OWLReferences ontology, OWLNamedIndividual instance, MORSemantic.MORType semantic) {

            final MORAxiom.MORTyped owl = semantic.query(ontology, instance);
            final MORAxiom.MORTyped java = semantic.get();
            final MORAxiom.MORTyped read = new MORAxiom.MORTyped();

            Mapping.Transitions<Mapping.Intent<OWLNamedIndividual,MORAxiom.MORTyped,Mapping.ReadingState>>
                    transitions = new MORTryRead<OWLNamedIndividual, MORAxiom.MORTyped>() {
                @Override
                public Mapping.Transitions giveAtry() {

                }
            }.perform();

            return new ReadOutcome<>( instance, read, transitions);
        }

        @Override
        public WriteOutcome<OWLNamedIndividual, MORAxiom.MORTyped>
                write(OWLReferences ontology, OWLNamedIndividual instance, MORSemantic.MORType semantic) {

            final MORAxiom.MORTyped owl = semantic.query(ontology, instance);
            final MORAxiom.MORTyped java = semantic.get();
            final MORAxiom.MORTyped write = new MORAxiom.MORTyped();

            Mapping.Transitions<Mapping.Intent<OWLNamedIndividual,MORAxiom.MORTyped,Mapping.WritingState>>
                    transitions = new MORTryRead<OWLNamedIndividual, MORAxiom.MORTyped>() {
                @Override
                public Mapping.Transitions giveAtry() {

                }
            }.perform();

            return new WriteOutcome<>( instance, write, transitions);
        }
    }








    interface MORTrier<I extends OWLObject, A extends MORAxiom, M extends Mapping.State>
            extends Mapping.TrierInterface<I,A,M>{

        default Mapping.Transitions onJavaError(Exception e){
            // todo logError(e)
            getStateTransitions().get( getStateTransitions().size() - 1).getState().asError();
            return getStateTransitions();
        }
        default Mapping.Transitions onOWLError(Exception e){
            // todo logError( "OWL" + e)
            getStateTransitions().get( getStateTransitions().size() - 1).getState().asError();
            return getStateTransitions();
        }
        default Mapping.Transitions onInconsistency(Exception e){
            // todo logError(e)
            getStateTransitions().get( getStateTransitions().size() - 1).getState().asInconsistent();
            return getStateTransitions();
        }

        @Override
        default Mapping.Transitions onError(Exception e){
            if ( e instanceof OWLReasonerRuntimeException)
                return onInconsistency( e);
            if ( e instanceof OWLException)
                return onOWLError( e);
            return onJavaError( e);
        }
    }

    abstract class MORTryWrite<I extends OWLObject, A extends MORAxiom>
            extends Mapping.Trier<I,A,Mapping.WritingState>
            implements MORTrier<I,A,Mapping.WritingState>{

        public MORTryWrite() {
            super();
        }
        public MORTryWrite(Mapping.Intent<I, A, Mapping.WritingState> intent) {
            super(intent);
        }

        @Override
        public Mapping.WritingState getNewState() {
            return new Mapping.WritingState();
        }
    }

    abstract class MORTryRead<I extends OWLObject, A extends MORAxiom>
            extends Mapping.Trier<I,A,Mapping.ReadingState>
            implements MORTrier<I,A,Mapping.ReadingState>{

        public MORTryRead() {
            super();
        }
        public MORTryRead(Mapping.Intent<I, A, Mapping.ReadingState> intent) {
            super(intent);
        }

        @Override
        public Mapping.ReadingState getNewState() {
            return new Mapping.ReadingState();
        }
    }
}
