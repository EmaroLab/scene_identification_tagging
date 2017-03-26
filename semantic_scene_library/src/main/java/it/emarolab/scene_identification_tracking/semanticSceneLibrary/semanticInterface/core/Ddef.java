package it.emarolab.scene_identification_tracking.semanticSceneLibrary.semanticInterface.core;

import it.emarolab.scene_identification_tracking.semanticSceneLibrary.semanticInterface.core.Semantic.Descriptor;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.semanticInterface.core.Semantic.Ground;

/**
 * Created by bubx on 25/03/17.
 */
public interface Ddef<I extends Ground<?,?>, X extends Semantic.Axiom<I,?,?>>
            extends Descriptor<I,X>{ // DescriptorRepresentation

    interface Data<I extends Ground<?,?>,
                    X extends Xdef.Data<I,?,? extends Adef.Literal<?,?>>>
        extends Descriptor<I,X>, Ddef<I,X>{
    }
    interface DataSet<I extends Ground<?,?>,
                    X extends Xdef.DataSet<I,?,? extends Adef.Literals<?,?,?>>>
            extends Descriptor<I,X>, Ddef<I,X>{
    }

    interface Link<I extends Ground<?,?>,
                    X extends Xdef.Data<I,?,? extends Adef.Individual<?,?>>>
            extends Descriptor<I,X>, Ddef<I,X>{
    }
    interface LinkSet<I extends Ground<?,?>,
                    X extends Xdef.DataSet<I,?,? extends Adef.Individuals<?,?,?>>>
            extends Descriptor<I,X>, Ddef<I,X>{
    }

    interface Types<I extends Ground<?,?>,
                    X extends Xdef.Types<I,? extends Adef.Classes<?,?>>>
            extends Descriptor<I,X>, Ddef<I,X>{
    }

    interface TypeChildren<I extends Ground<?,?>,
                    X extends Xdef.Types<I,? extends Adef.Classes<?,?>>>
            extends Descriptor<I,X>, Ddef<I,X>{
    }

    interface TypeParents<I extends Ground<?,?>,
            X extends Xdef.Types<I,? extends Adef.Classes<?,?>>>
            extends Descriptor<I,X>, Ddef<I,X>{
    }

    // todo: add data/objectPropertyPARENT and data/objectPropertyCHILDREN interface

    //interface MinClassRestriction<>
    // todo add cardinality
}
