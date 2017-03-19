package it.emarolab.scene_identification_tracking.semanticSceneLibrary.aMOR.semantic;

import it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.synchronisation.Mapping;
import org.semanticweb.owlapi.model.OWLObject;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by bubx on 19/03/17.
 */
public interface MORSynchronise {

    abstract class MORReader< I extends OWLObject, A extends MORAxiom, M extends Mapping.State>{

        protected Mapping.Intent<I,A,M> startingIntent;

        public MORReader(Mapping.Intent<I,A,M> intent) {
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

        void synchronise(A java, A owl){
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

    abstract class SetReader< I extends OWLObject, A extends MORAxiom, T extends OWLObject>
            extends MORReader<I,A,Mapping.ReadingState>{

        public SetReader(Mapping.Intent<I,A,Mapping.ReadingState> intent) {
            super(intent);
        }

        abstract Mapping.Intent<I,A,Mapping.ReadingState> getUpdateIntent(T j, T o, String description);

        void update( Set<T> javaSet, Set<T> owlSet){
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
            Set< T> toRemove = new HashSet<>();
            for( T j : javaSet){

                Mapping.Intent<I, A, Mapping.ReadingState> otherIntent =
                        getUpdateIntent( j, null, "-"); // todo to describe

                toRemove.add( j);
                otherIntent.getState().asAbsent();
            }
            javaSet.removeAll( toRemove);
        }
    }

    abstract class SetWriter< I extends OWLObject, A extends MORAxiom, T extends OWLObject>
            extends MORReader<I,A,Mapping.WritingState>{

        public SetWriter(Mapping.Intent<I,A,Mapping.WritingState> intent) {
            super(intent);
        }

        abstract Mapping.Intent<I,A,Mapping.WritingState> getUpdateIntent(T j, T o, String description);

        abstract void addToSemantic( T t);
        abstract void removeFromSemantic( T t);

        void update( Set<T> javaSet, Set<T> owlSet){
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

            for( T o : owlSet){

                Mapping.Intent<I,A,Mapping.WritingState> otherIntent =
                        getUpdateIntent( null, o, "-"); // todo to describe

                removeFromSemantic( o);//semantic.remove( ontology, instance, o);
                otherIntent.getState().asRemoved();
            }
        }
    }
}
