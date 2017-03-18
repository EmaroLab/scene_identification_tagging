package it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.synchronisation;

import it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.Semantic;

/**
 * Created by bubx on 16/03/17.
 */
public interface Descriptor<O,I,S extends Semantic<O,I,?>> {

    ReadOutcome<?> read(O ontology, I instance, S semantic);
    WriteOutcome<?> write(O ontology, I instance, S semantic);

    interface Typing<O,I,S extends Semantic.Type<O,I,?>> extends Descriptor<O,I, S>{
    }

    interface Hierarching<O,I,S extends Semantic.Hierarchy<O,I,?>> extends Descriptor<O,I, S>{
    }

    interface Properting<O,I,S extends Semantic.Property<O,I,?>> extends Descriptor<O,I, S>{
    }

    interface Properting3D<O,I,S extends Semantic.Property<O,I,?>> extends Descriptor<O,I, S>{
    }

    interface MultiProperting<O,I,S extends Semantic.MultiProperty<O,I,?>> extends Descriptor<O,I, S>{
    }

    interface ClassRestricting<O,I,S extends Semantic.ClassRestriction<O,I,?>> extends Descriptor<O,I, S>{
    }

    // todo to connect with implementation
    interface SynchOutcomeInterface<A extends Semantic.Axiom>{
        A getSemantic();

        Mapping.State getState();

        Mapping.Transitions getTrasition();
    }
    class SynchOutcome<A extends Semantic.Axiom, M extends Mapping.State> implements SynchOutcomeInterface<A>{

        private final A semantic;
        private final M state;
        private final Mapping.Transitions transitions;

        public SynchOutcome(A semantic, M state, Mapping.Transitions transitions){
            this.semantic = semantic;
            this.state = state;
            this.transitions = transitions;
        }

        @Override
        public A getSemantic() {
            return null;
        }

        @Override
        public M getState() {
            return null;
        }

        @Override
        public Mapping.Transitions getTrasition() {
            return null;
        }
    }
    class ReadOutcome<A extends Semantic.Axiom>
            extends SynchOutcome<A,Mapping.ReadingState>{
        public ReadOutcome(A semantic, Mapping.ReadingState state, Mapping.Transitions transitions) {
            super(semantic, state, transitions);
        }
    }
    class WriteOutcome<A extends Semantic.Axiom>
            extends SynchOutcome<A,Mapping.ReadingState>{
        public WriteOutcome(A semantic, Mapping.ReadingState state, Mapping.Transitions transitions) {
            super(semantic, state, transitions);
        }
    }
}
