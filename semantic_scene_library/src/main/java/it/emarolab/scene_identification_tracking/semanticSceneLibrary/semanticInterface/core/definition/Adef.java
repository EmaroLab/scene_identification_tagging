package it.emarolab.scene_identification_tracking.semanticSceneLibrary.semanticInterface.core.definition;

import it.emarolab.scene_identification_tracking.semanticSceneLibrary.semanticInterface.core.Semantic;

import java.util.Collection;

/**
 * Created by bubx on 24/03/17.
 */
public interface Adef<Y>
        extends Semantic.Atomic<Y> { // Atom Representation

    interface Atom<S,Y>
            extends Semantic.Atom<S,Y>, Adef<Y>{}

    interface Atoms<S, YY extends Collection<? extends Semantic.Atom<S,Y>>, Y>
            extends Semantic.AtomSet<S,YY,Y>, Adef<YY> {}



    interface Individual<S, Y>
            extends Semantic.Atom<S,Y>, Adef<Y> {}

    interface Literal<S, Y>
            extends Semantic.Atom<S,Y>, Adef<Y> {}

    interface Class<Y>
            extends Semantic.Atom<Void,Y>, Adef<Y> {}



    interface Individuals<S, YY extends Collection<? extends Individual<S,Y>>, Y>
            extends Atoms<S,YY,Y>, Adef<YY> {}

    interface Literals<S, YY extends Collection<? extends Literal<S,Y>>, Y>
            extends Atoms<S,YY,Y>, Adef<YY> {}

    interface Classes<YY extends Collection<? extends Class<Y>>, Y>
            extends Atoms<Void,YY,Y>, Adef<YY> {}

    // todo add cardinality

}
