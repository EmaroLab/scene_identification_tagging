package it.emarolab.scene_identification_tracking.semanticSceneLibrary.aMOR.semantic;

import it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.synchronisation.Mapping;
import org.semanticweb.owlapi.model.OWLObject;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by bubx on 19/03/17.
 */
public interface MORSynch<A extends MORAxiom> {

    void synchronise(A java, A owl);

    abstract class MORSynchroniser< I extends OWLObject, A extends MORAxiom, M extends Mapping.State>
            implements MORSynch<A> {

        protected Mapping.Intent<I,A,M> startingIntent;

        public MORSynchroniser(Mapping.Intent<I,A,M> intent) {
            startingIntent = intent;
        }

        void trigger_JavaNotExists_OWLNotExists(){
            startingIntent.getState().asNotChanged();
        }
        void trigger_JavaNOTExists_OWLExists(){
            if ( startingIntent.getState() instanceof Mapping.ReadingState)
                ((Mapping.ReadingState) startingIntent.getState()).asSuccess();
            else if ( startingIntent.getState() instanceof Mapping.WritingState)
                ((Mapping.WritingState) startingIntent.getState()).asRemoved();
            //else // todo LOG
        }
        void trigger_JavaExists_OWLNotExists(){
            if ( startingIntent.getState() instanceof Mapping.ReadingState)
                ((Mapping.ReadingState) startingIntent.getState()).asAbsent();
            else if ( startingIntent.getState() instanceof Mapping.WritingState)
                ((Mapping.WritingState) startingIntent.getState()).asAdded();
            //else // todo LOG
        }
        void trigger_JavaExists_OWLExists(){
            if ( startingIntent.getState() instanceof Mapping.ReadingState)
                ((Mapping.ReadingState) startingIntent.getState()).asSuccess();
            else if ( startingIntent.getState() instanceof Mapping.WritingState)
                ((Mapping.WritingState) startingIntent.getState()).asUpdated();
            //else // todo LOG
        }

        @Override
        public void synchronise(A java, A owl){
            if( ! java.exists()){
                if ( ! owl.exists())
                    trigger_JavaNotExists_OWLNotExists();
                else trigger_JavaNOTExists_OWLExists();
            } else {
                if( ! owl.exists())
                    trigger_JavaExists_OWLNotExists();
                else trigger_JavaExists_OWLExists();
            }
        }
    }

    abstract class MORSetSynchroniser< I extends OWLObject, A extends MORAxiom, T extends OWLObject>
            extends MORSynchroniser<I,A,Mapping.ReadingState> {

        public MORSetSynchroniser(Mapping.Intent<I,A,Mapping.ReadingState> intent) {
            super(intent);
        }

        abstract Mapping.Intent<I,A,Mapping.ReadingState> getUpdateIntent(T j, T o, String description);

        void update( Collection<T> javaSet, Collection<T> owlSet){
            for ( T o : owlSet){
                T j = null;
                if( javaSet.contains( o)) {
                    j = o;
                    javaSet.remove( o);
                }

                Mapping.Intent<I, A, Mapping.ReadingState> otherIntent =
                        getUpdateIntent( j, o, "+"); // todo to describe

                if ( j != null) { // 'java' contains 'o'
                    otherIntent.getState().asNotChanged();
                } else {
                    javaSet.add( o);
                    otherIntent.getState().asSuccess();
                }
            }
            Collection< T> toRemove = new HashSet<>();
            for( T j : javaSet){

                Mapping.Intent<I, A, Mapping.ReadingState> otherIntent =
                        getUpdateIntent( j, null, "-"); // todo to describe

                toRemove.add( j);
                otherIntent.getState().asAbsent();
            }
            javaSet.removeAll( toRemove);
        }
    }

    abstract class MORSetWriter< I extends OWLObject, A extends MORAxiom, T extends OWLObject>
            extends MORSynchroniser<I,A,Mapping.WritingState> {

        public MORSetWriter(Mapping.Intent<I,A,Mapping.WritingState> intent) {
            super(intent);
        }

        abstract Mapping.Intent<I,A,Mapping.WritingState> getUpdateIntent(T j, T o, String description);

        abstract void addToSemantic( T t);
        abstract void removeFromSemantic( T t);

        void update(Collection<T> javaSet, Collection<T> owlSet){

            System.err.println( " java " + javaSet);
            System.err.println( " owl " + javaSet);

            for ( T j : javaSet){
                T o = null;
                if( owlSet.contains( j)) {
                    o = j;
                    owlSet.remove( j);
                }

                Mapping.Intent<I,A,Mapping.WritingState> otherIntent =
                        getUpdateIntent( j, o, "+"); // todo to describe

                if ( o == null) { // 'owl' does not contains 'j'
                    addToSemantic( j);//semantic.add( ontology, instance, j);
                    otherIntent.getState().asUpdated();
                } else {
                    otherIntent.getState().asNotChanged();
                }
            }

            System.err.println( " remove " + owlSet);

            for( T o : owlSet){

                Mapping.Intent<I,A,Mapping.WritingState> otherIntent =
                        getUpdateIntent( null, o, "-"); // todo to describe

                removeFromSemantic( o);//semantic.remove( ontology, instance, o);
                otherIntent.getState().asRemoved();
            }
        }
    }
}
