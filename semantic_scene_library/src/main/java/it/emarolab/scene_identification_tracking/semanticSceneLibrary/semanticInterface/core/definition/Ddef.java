package it.emarolab.scene_identification_tracking.semanticSceneLibrary.semanticInterface.core.definition;

import it.emarolab.scene_identification_tracking.semanticSceneLibrary.semanticInterface.core.Semantic;

/**
 * Created by bubx on 25/03/17.
 */
public interface Ddef<I extends Semantic.Ground<?,?>, X extends Xdef<I,?,A>, A extends Adef<?>>
            extends Semantic.Descriptor<I,X,A>{ // DescriptorRepresentation


    interface Data< I extends Semantic.Ground<?,?>,
                    X extends Xdef.Data<I,?,A>,
                    A extends Adef.Literal<?,?>>
            extends Semantic.Descriptor<I,X,A>, Ddef<I,X,A>{}

    interface DataSet<  I extends Semantic.Ground<?,?>,
                        X extends Xdef.DataSet<I,?,A>,
                        A extends Adef.Literals<?,?,?>>
            extends Semantic.Descriptor<I,X,A>, Ddef<I,X,A>{}

    interface Link< I extends Semantic.Ground<?,?>,
                    X extends Xdef.Link<I,?,A>,
                    A extends Adef.Individual<?,?>>
            extends Semantic.Descriptor<I,X,A>, Ddef<I,X,A>{}

    interface LinkSet<  I extends Semantic.Ground<?,?>,
                        X extends Xdef.LinkSet<I,?,A>,
                        A extends Adef.Individuals<?,?,?>>
            extends Semantic.Descriptor<I,X,A>, Ddef<I,X,A>{}


    interface Types<    I extends Semantic.Ground<?,?>,
                        X extends Xdef.Types<I,A>,
                        A extends Adef.Classes<?,?>>
            extends Semantic.Descriptor<I,X,A>, Ddef<I,X,A>{}


    interface Parents<  I extends Semantic.Ground<?,?>,
                        X extends Xdef.Types<I,A>,
                        A extends Adef.Classes<?,?>>
            extends Semantic.Descriptor<I,X,A>, Ddef<I,X,A>{}

    interface Children< I extends Semantic.Ground<?,?>,
                        X extends Xdef.Types<I,A>,
                        A extends Adef.Classes<?,?>>
            extends Semantic.Descriptor<I,X,A>, Ddef<I,X,A>{}

    // todo: add data/objectPropertyPARENT and data/objectPropertyCHILDREN interface

    //interface MinClassRestriction<>
    // todo add cardinality
}
