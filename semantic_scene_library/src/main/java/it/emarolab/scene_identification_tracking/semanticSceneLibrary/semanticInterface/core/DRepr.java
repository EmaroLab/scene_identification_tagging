package it.emarolab.scene_identification_tracking.semanticSceneLibrary.semanticInterface.core;

import it.emarolab.scene_identification_tracking.semanticSceneLibrary.semanticInterface.core.Semantic.Descriptor;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.semanticInterface.core.Semantic.Instance;

/**
 * Created by bubx on 25/03/17.
 */
public interface DRepr { // DescriptorRepresentation

    interface Data<I extends Instance<?,?>,
                    X extends XRepr.Data<I,?,? extends ARepr.Literal<I,?,?>>>
        extends Descriptor<I,X>{
    }
    interface DataSet<I extends Instance<?,?>,
                    X extends XRepr.DataSet<I,?,? extends ARepr.Literals<I,?,?>>>
            extends Descriptor<I,X>{
    }

    interface Link<I extends Instance<?,?>,
                    X extends XRepr.Data<I,?,? extends ARepr.Individual<I,?,?>>>
            extends Descriptor<I,X>{
    }
    interface LinkSet<I extends Instance<?,?>,
                    X extends XRepr.DataSet<I,?,? extends ARepr.Individuals<I,?,?>>>
            extends Descriptor<I,X>{
    }

    interface Types<I extends Instance<?,?>,
                    X extends XRepr.Types<I,?,? extends ARepr.Classes<I,?,?>>>
            extends Descriptor<I,X>{
    }

    interface TypeParents<I extends Instance<?,?>,
                    X extends XRepr.Types<I,?,? extends ARepr.Classes<I,?,?>>>
            extends Descriptor<I,X>{
    }

    interface TypeChildren<I extends Instance<?,?>,
                    X extends XRepr.Types<I,?,? extends ARepr.Classes<I,?,?>>>
            extends Descriptor<I,X>{
    }

    // todo: add data/objectPropertyPARENT and data/objectPropertyCHILDREN interface

    //interface MinClassRestriction<>
    // todo add cardinality
}
