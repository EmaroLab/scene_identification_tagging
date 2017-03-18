package it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.representation;

import it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.Mapping;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.Semantic;

/**
 * Created by bubx on 16/03/17.
 */
public interface Descriptor<O,I,X extends Semantic<O,I,?>> {

    SynchOutcome synchronise(O ontology, I instance, X semantic);

    interface Type<O,I,X extends Semantic.Axiom.Type<O,I,?>> extends Descriptor<O,I,X>{
    }

    //interface Hyerarchy<O,I,X extends Semantic.Axiom.Hierarchy<O,I,?>> extends Descriptor<O,I,X>{
    //}

    interface Property<O,I,X extends Semantic.Axiom.Property<O,I,?>> extends Descriptor<O,I,X>{
    }

    interface MultiProperty<O,I,X extends Semantic.Axiom.MultiProperty<O,I,?>> extends Descriptor<O,I,X>{
    }

    interface SynchOutcomeInterface<X extends Semantic.Atom>{
        X getSemantic();

        Mapping.State getState();

        Mapping.Transitions getTrasition();
    }
    class SynchOutcome<X extends Semantic.Atom> implements SynchOutcomeInterface<X>{

        private final X semantic;
        private final Mapping.State state;
        private final Mapping.Transitions transitions;

        public SynchOutcome(X semantic, Mapping.State state, Mapping.Transitions transitions){
            this.semantic = semantic;
            this.state = state;
            this.transitions = transitions;
        }

        @Override
        public X getSemantic() {
            return null;
        }

        @Override
        public Mapping.State getState() {
            return null;
        }

        @Override
        public Mapping.Transitions getTrasition() {
            return null;
        }
    }
}
