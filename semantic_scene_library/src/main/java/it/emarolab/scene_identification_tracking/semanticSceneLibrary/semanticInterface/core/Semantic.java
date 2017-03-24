package it.emarolab.scene_identification_tracking.semanticSceneLibrary.semanticInterface.core;

import it.emarolab.scene_identification_tracking.semanticSceneLibrary.semanticInterface.Base;

import java.util.Collection;

/**
 * Created by bubx on 23/03/17.
 */
public interface Semantic {

    interface AtomBase<I extends Instance<?,?>, W, Y>
            extends Base {
        Y get();

        void addAxiom( I instance, W symbol);
        void removeAxiom( I instance, W symbol);
    }
    interface Atom<I extends Instance<?,?>, W, Y>
            extends AtomBase<I,W,Y>{
    }
    interface AtomSet<I extends Instance<?,?>, W, Y extends Collection<?>>
            extends AtomBase<I,W,Y>{
    }

    interface Axiom<I extends Instance<?,?>, W, A extends AtomBase<I,W,?>>
            extends Base {
        A getAtom();
        W getSymbol();

        void addAxiom( I instance);
        void removeAxiom( I instance);
    }

    interface Descriptor<I extends Instance<?,?>, X extends Axiom<I,?,?>>
            extends Base {
        X getSemantic();

        Intents read( I instance);
        Intents write( I instance);
    }



    interface Instance< O, I>
            extends Base {
        O getOntology();
        I getInstance();
    }


    // todo move in syncronise mapping
    // add Try
    interface Intents
            extends Collection<Intent>, Base{
    }
    interface Intent<X extends Axiom<?,?,?>>
            extends Base{
        State getState();
        X getAxiom();
    }
    interface State
            extends Base{
        int getState();
    }
}