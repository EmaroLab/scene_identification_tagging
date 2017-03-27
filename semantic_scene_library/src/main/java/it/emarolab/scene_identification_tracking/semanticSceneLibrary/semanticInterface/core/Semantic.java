package it.emarolab.scene_identification_tracking.semanticSceneLibrary.semanticInterface.core;

import it.emarolab.scene_identification_tracking.semanticSceneLibrary.semanticInterface.Base;

import java.util.Collection;

/**
 * Created by bubx on 23/03/17.
 */
public interface Semantic {

    interface Atomic<Y>
            extends Base {
        Y get();

        boolean exists();
        void clear();
    }
    interface Atom<S, Y>
            extends Atomic<Y> {
        void set(Y literal);

        <I extends Ground<?,?>> void addAxiom( I instance, S semantic, Y atom);
        default <I extends Ground<?,?>> void addAxiom( I instance, S semantic){
            addAxiom( instance, semantic, get());
        }

        <I extends Ground<?,?>> void removeAxiom( I instance, S semantic, Y atom);
        default <I extends Ground<?,?>> void removeAxiom( I instance, S semantic){
            removeAxiom( instance, semantic, get());
        }

        <I extends Ground<?,?>> Y queryAxiom( I instance, S semantic);
        default <I extends Ground<?,?>> void queryAxiom( I instance, S semantic, Atom<S,Y> inputAtom){
            inputAtom.set( queryAxiom( instance, semantic));
        }
    }
    interface AtomSet<S, YY extends Collection<? extends Atom<S,Y>>,Y>
            extends Atomic<YY> {

        default <I extends Ground<?,?>> void addAxiom(I instance, S semantic){
            for( Atom<S,Y> a : get())
                a.addAxiom( instance, semantic, a.get());
        }

        default <I extends Ground<?,?>> void removeAxiom(I instance, S semantic){
            for( Atom<S,Y> a : get())
                a.removeAxiom( instance, semantic, a.get());
        }

        <I extends Ground<?,?>> Collection<Y> queryAxiom( I instance, S semantic);

        default <I extends Ground<?,?>> void queryAxiom(I instance, S semantic, AtomSet<S,YY,Y> newAtom){
            Collection<Y> ys = queryAxiom( instance, semantic);
            for ( Y y : ys)
                newAtom.get().add( getNewElement( y));
        }
        <A extends Atom<S,Y>> A getNewElement( Y value);
    }

    interface Axiom<I extends Ground<?,?>, S, A extends Atomic<?>>
            extends Base {
        A getAtom();
        S getSemantic();
        boolean exists();
        void clear();

        void addAxiom( I instance);
        void removeAxiom( I instance);

        A queryAxiom(I instance); // return new
    }

    interface Descriptor<I extends Ground<?,?>, X extends Axiom<I,?,?>>
            extends Base {
        X getAxiom();

        Intents read( I instance, X axiom);
        Intents write( I instance, X axiom);


        default Intents read( I instance){
            return read( instance, getAxiom());
        }
        default Intents write( I instance){
            return write( instance, getAxiom());
        }
    }



    interface Ground< O, J>
            extends Base {
        O getOntology();
        J getInstance();
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