package it.emarolab.scene_identification_tracking.semanticSceneLibrary.semanticInterface.core.definition;

import it.emarolab.scene_identification_tracking.semanticSceneLibrary.semanticInterface.core.Semantic;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.semanticInterface.core.Semantic.Ground;

import java.util.Collection;

/**
 * Created by bubx on 25/03/17.
 */
public interface Xdef<I extends Ground<?,?>, S, A extends Semantic.Atomic<?>>
            extends Semantic.Axiom <I,S,A>{ // aXiom Representation

    interface Data<I extends Ground<?,?>, S, A extends Adef.Literal<S,?>>
            extends Semantic.Axiom <I,S,A>, Xdef<I,S,A>{}

    interface DataSet<I extends Ground<?,?>, S, A extends Adef.Literals<S, ? extends Collection< ? extends Adef.Literal<S,?>>,?>>
            extends Semantic.Axiom <I,S,A>, Xdef<I,S,A>{}


    interface Link<I extends Ground<?,?>, S, A extends Adef.Individual<S,?>>
            extends Semantic.Axiom <I,S,A>, Xdef<I,S,A>{}

    interface LinkSet<I extends Ground<?,?>, S, A extends Adef.Individuals<S, ? extends Collection<? extends Adef.Individual<S,?>>,?>>
            extends Semantic.Axiom <I,S,A>, Xdef<I,S,A>{}


    interface Types<I extends Ground<?,?>, A extends Adef.Classes<? extends Collection<? extends Adef.Class<?>>,?>>
            extends Semantic.Axiom <I,Void,A>, Xdef<I,Void,A>{}

    interface Parents <I extends Ground<?,?>, A extends Adef.Classes<? extends Collection<? extends Adef.Class<?>>,?>>
            extends Semantic.Axiom <I,Void,A>, Xdef<I,Void,A>{}

    interface Children<I extends Ground<?,?>, A extends Adef.Classes<? extends Collection<? extends Adef.Class<?>>,?>>
            extends Semantic.Axiom <I,Void,A>, Xdef<I,Void,A>{}

    //interface MinClassRestriction<>
    // todo add cardinality
}
