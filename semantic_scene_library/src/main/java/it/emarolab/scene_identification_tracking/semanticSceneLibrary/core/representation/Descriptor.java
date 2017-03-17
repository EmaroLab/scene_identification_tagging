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

    interface Hyerarchy<O,I,X extends Semantic.Axiom.Hierarchy<O,I,?>> extends Descriptor<O,I,X>{
    }


    interface Property<O,I,P,V> extends Descriptor<O,I,Semantic.Axiom.Property<O,I,P,V>>{
    }

    interface MultiProperty<O,I,P,V> extends Descriptor<O,I,Semantic.Axiom.MultiProperty<O,I,P,V>>{
    }

    interface SynchOutcome<X extends Semantic<?,?,?>>{
        X getSemantic();

        Mapping.State getState();

        Mapping.Transitions getTrasition();
    }
}
