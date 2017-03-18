package it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.synchronisation;

import it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.Semantic;

/**
 * Created by bubx on 16/03/17.
 */
public interface Descriptor<O,I,S extends Semantic<O,I,?>> {

    ReadOutcome<I,?> read(O ontology, I instance, S semantic);
    WriteOutcome<I,?> write(O ontology, I instance, S semantic);

    interface Typing<O,I,S extends Semantic.Type<O,I,?>>
            extends Descriptor<O,I,S>{
    }

    interface Hierarching<O,I,S extends Semantic.Hierarchy<O,I,?>>
            extends Descriptor<O,I,S>{
    }

    interface Properting<O,I,S extends Semantic.Property<O,I,?>>
            extends Descriptor<O,I,S>{
    }

    interface Properting3D<O,I,S extends Semantic.Property3D<O,I,?>>
            extends Descriptor<O,I,S>{
    }

    interface MultiProperting<O,I,S extends Semantic.MultiProperty<O,I,?>>
            extends Descriptor<O,I,S>{
    }

    interface ClassRestricting<O,I,S extends Semantic.ClassRestriction<O,I,?>>
            extends Descriptor<O,I,S>{
    }

    // todo to connect with implementation
    interface SynchOutcomeInterface<I,A extends Semantic.Axiom>{
        I getInstance(); // todo check if necessery

        A getAxiom(); // todo check if necessery

        Mapping.State getState();

        Mapping.Transitions getTrasition();
    }
    class SynchOutcome<I,A extends Semantic.Axiom, M extends Mapping.State>
            implements SynchOutcomeInterface<I,A>{

        private final I instance;
        private final A axiom;
        private final M state;
        private final Mapping.Transitions transitions;

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
