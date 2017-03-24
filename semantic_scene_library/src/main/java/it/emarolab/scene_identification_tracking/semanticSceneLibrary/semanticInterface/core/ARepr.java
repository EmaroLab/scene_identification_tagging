package it.emarolab.scene_identification_tracking.semanticSceneLibrary.semanticInterface.core;

import it.emarolab.scene_identification_tracking.semanticSceneLibrary.semanticInterface.core.Semantic.Atom;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.semanticInterface.core.Semantic.AtomSet;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.semanticInterface.core.Semantic.Instance;

import java.util.Collection;

/**
 * Created by bubx on 24/03/17.
 */
public interface ARepr { // Atom Representation

    interface Individual<I extends Instance<?,?>, W, Y>
        extends Atom<I,W,Y>{
    }
    interface Individuals<I extends Instance<?,?>, W, Y extends Collection<?>>
        extends AtomSet<I,W,Y> {
    }

    interface Literal<I extends Instance<?,?>, W, Y>
            extends Atom<I,W,Y>{
    }
    interface Literals<I extends Instance<?,?>, W, Y extends Collection<?>>
            extends AtomSet<I,W,Y> {
    }

    interface Class<I extends Instance<?,?>, W, Y>
            extends Atom<I,W,Y>{
    }
    interface Classes<I extends Instance<?,?>, W, Y extends Collection<?>>
            extends AtomSet<I,W,Y> {
    }

    // todo add cardinality

}
