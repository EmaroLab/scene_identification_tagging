package it.emarolab.scene_identification_tracking.semanticSceneLibrary.semanticInterface.core.definition;

import it.emarolab.scene_identification_tracking.semanticSceneLibrary.semanticInterface.core.Semantic;

/**
 * Created by bubx on 25/03/17.
 */
public interface Ddef<I extends Semantic.Ground<?,?>, X extends Semantic.Axiom<I,?,?>>
            extends Semantic.Descriptor<I,X>{ // DescriptorRepresentation

    interface Data< I extends Semantic.Ground<?,?>,
                    X extends Xdef.Data<I,?,? extends Adef.Literal<?,?>>>
            extends Semantic.Descriptor<I,X>, Ddef<I,X>{}

    interface DataSet<  I extends Semantic.Ground<?,?>,
                        X extends Xdef.DataSet<I,?,? extends Adef.Literals<?,?,?>>>
            extends Semantic.Descriptor<I,X>, Ddef<I,X>{}

    interface Link< I extends Semantic.Ground<?,?>,
                    X extends Xdef.Data<I,?,? extends Adef.Individual<?,?>>>
            extends Semantic.Descriptor<I,X>, Ddef<I,X>{}

    interface LinkSet<  I extends Semantic.Ground<?,?>,
                        X extends Xdef.DataSet<I,?,? extends Adef.Individuals<?,?,?>>>
            extends Semantic.Descriptor<I,X>, Ddef<I,X>{}


    interface Types<    I extends Semantic.Ground<?,?>,
                        X extends Xdef.Types<I,? extends Adef.Classes<?,?>>>
            extends Semantic.Descriptor<I,X>, Ddef<I,X>{}


    interface Parents<  I extends Semantic.Ground<?,?>,
            X extends Xdef.Types<I,? extends Adef.Classes<?,?>>>
            extends Semantic.Descriptor<I,X>, Ddef<I,X>{}

    interface Children< I extends Semantic.Ground<?,?>,
                        X extends Xdef.Types<I,? extends Adef.Classes<?,?>>>
            extends Semantic.Descriptor<I,X>, Ddef<I,X>{}

    // todo: add data/objectPropertyPARENT and data/objectPropertyCHILDREN interface

    //interface MinClassRestriction<>
    // todo add cardinality
}
