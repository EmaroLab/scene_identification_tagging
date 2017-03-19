package it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.synchronisation;

import it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.Semantic;

/**
 * Created by bubx on 16/03/17.
 */
public interface Descriptor<O,I,S extends Semantic<O,I,?>> {

    ReadOutcome<I,?> read(O ontology, I instance);
    WriteOutcome<I,?> write(O ontology, I instance, S semantic);

    interface Typing<O,I,S extends Semantic.Type<O,I,?>>
            extends Descriptor<O,I,S>{
    }

    interface Hierarching<O,I,S extends Semantic.Hierarchy<O,I,?>>
            extends Descriptor<O,I,S>{
    }

/*    interface Properting<O,I,S extends Semantic.Property<O,I,?>>
            extends Descriptor<O,I,S>{
    }

    interface Properting3D<O,I,S extends Semantic.Property3D<O,I,?>>
            extends Descriptor<O,I,S>{
        @Override
        default ReadOutcome<I,?> read(O ontology, I instance, S semantic){
            ReadOutcome<I, ?> outcome = readX(ontology, instance, semantic);
            outcome.merge( readY( ontology, instance, semantic));
            outcome.merge( readZ( ontology, instance, semantic));
            return outcome;
        }
        ReadOutcome<I,?> readX(O ontology, I instance, S semantic);
        ReadOutcome<I,?> readY(O ontology, I instance, S semantic);
        ReadOutcome<I,?> readZ(O ontology, I instance, S semantic);

        @Override
        default WriteOutcome<I, ?> write(O ontology, I instance, S semantic){
            WriteOutcome<I, ?> outcome = writeX(ontology, instance, semantic);
            outcome.merge( writeY( ontology, instance, semantic));
            outcome.merge( writeZ( ontology, instance, semantic));
            return outcome;
        }
        WriteOutcome<I,?> writeX(O ontology, I instance, S semantic);
        WriteOutcome<I,?> writeY(O ontology, I instance, S semantic);
        WriteOutcome<I,?> writeZ(O ontology, I instance, S semantic);
    }

    interface MultiProperting<O,I,S extends Semantic.MultiProperty<O,I,?>>
            extends Descriptor<O,I,S>{
    }

    interface ClassRestricting<O,I,S extends Semantic.ClassRestriction<O,I,?>>
            extends Descriptor<O,I,S>{
    }
*/
    interface SynchOutcomeInterface<I,A extends Semantic.Axiom>{
        I getInstance(); // todo check if necessery

        A getAxiom(); // todo check if necessery

        Mapping.State getState();

        Mapping.Transitions getTrasition();


    }
    class SynchOutcome<I,A extends Semantic.Axiom, M extends Mapping.State>
            implements SynchOutcomeInterface<I,A>{

        private I instance;
        private A axiom;
        private M state;
        private Mapping.Transitions transitions;

        // todo: check <-> to be copied????
        public SynchOutcome(I instance, A axiom, Mapping.Transitions<Mapping.Intent<I,A,M>> transitions){
            this.instance = instance;
            this.axiom = axiom;
            this.transitions = transitions;
            this.state = transitions.merge();
        }

        @Override
        public I getInstance(){
            return instance;
        }

        @Override
        public A getAxiom() {
            return axiom;
        }

        @Override
        public M getState() {
            return state;
        }

        @Override
        public Mapping.Transitions getTrasition() {
            return null;
        }

        public void merge( SynchOutcome outcome){
            this.instance = (I) outcome.getInstance();
            this.axiom = (A) outcome.getAxiom();
            this.state = (M) state.merge( outcome.getState());
            transitions.addAll( outcome.getTrasition());
        }

        @Override
        public String toString() {
            return "SynchOutcome{" +
                    "instance=" + instance +
                    ", axiom=" + axiom +
                    ", state=" + state +
                    ", transitions=" + transitions +
                    '}';
        }
    }
    class ReadOutcome<I,A extends Semantic.Axiom>
            extends SynchOutcome<I,A,Mapping.ReadingState>{
        public ReadOutcome(I instance, A axiom,
                           Mapping.Transitions<Mapping.Intent<I,A,Mapping.ReadingState>> transitions) {
            super(instance, axiom, transitions);
        }
    }
    class WriteOutcome<I,A extends Semantic.Axiom>
            extends SynchOutcome<I,A,Mapping.WritingState>{
        public WriteOutcome(I instance, A axiom,
                            Mapping.Transitions<Mapping.Intent<I,A,Mapping.WritingState>> transitions) {
            super(instance, axiom, transitions);
        }
    }
}
