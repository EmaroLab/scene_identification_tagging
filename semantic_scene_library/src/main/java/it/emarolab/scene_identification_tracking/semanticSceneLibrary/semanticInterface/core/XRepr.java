package it.emarolab.scene_identification_tracking.semanticSceneLibrary.semanticInterface.core;

import it.emarolab.scene_identification_tracking.semanticSceneLibrary.semanticInterface.core.Semantic.Axiom;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.semanticInterface.core.Semantic.Instance;

/**
 * Created by bubx on 25/03/17.
 */
public interface XRepr { // aXiom Representation

    interface Data<I extends Instance<?,?>, W, A extends ARepr.Literal<I,W,?>>
            extends Axiom<I,W,A>{
    }
    interface DataSet<I extends Instance<?,?>, W, A extends ARepr.Literals<I,W,?>>
            extends Axiom<I,W,A>{
    }

    interface Link<I extends Instance<?,?>, W, A extends ARepr.Individual<I,W,?>>
            extends Axiom<I,W,A>{
    }
    interface LinkSet<I extends Instance<?,?>, W, A extends ARepr.Individuals<I,W,?>>
            extends Axiom<I,W,A>{
    }

    interface Types<I extends Instance<?,?>,W, A extends ARepr.Classes<I,W,?>>
            extends Axiom<I,W,A>{
    }

    interface Parents <I extends Instance<?,?>,W, A extends ARepr.Classes<I,W,?>>
            extends Axiom<I,W,A>{
    }
    interface Children<I extends Instance<?,?>,W, A extends ARepr.Classes<I,W,?>>
            extends Axiom<I,W,A>{
    }

    //interface MinClassRestriction<>
    // todo add cardinality
}
