package it.emarolab.scene_identification_tracking.semanticSceneLibrary.semanticInterface.aMOR;

import it.emarolab.scene_identification_tracking.semanticSceneLibrary.semanticInterface.aMOR.MORGround.GroundIndividual;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.semanticInterface.core.definition.Ddef;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.semanticInterface.core.synchronise.Mapping;

/**
 * Created by bubx on 27/03/17.
 */
// those are axiom mapper that can eventually contains an axiom.
public interface MORDescriptor<I extends MORGround<?>, X extends MORAxiom<I,?,A>, A extends MORAtom<?>>
        extends Ddef<I,X,A>{

    // todo implement read and write in interfaces

    class MORLiteralIntent<M extends Mapping.State> // todo to move
            extends Mapping.Intent<GroundIndividual, MORAxiom.MORLittered, MORAtom.MORLiteral, M>{
        public MORLiteralIntent() {
        }

        public MORLiteralIntent(GroundIndividual instance, String description) {
            super(instance, description);
        }

        public MORLiteralIntent(GroundIndividual instance, M state, String description) {
            super(instance, state, description);
        }

        public MORLiteralIntent(GroundIndividual instance, M state, MORAxiom.MORLittered axiom, String description) {
            super(instance, state, axiom, description);
        }

        public MORLiteralIntent(GroundIndividual instance, M state, MORAtom.MORLiteral queriedAtom, String description) {
            super(instance, state, queriedAtom, description);
        }

        public MORLiteralIntent(GroundIndividual instance, M state, MORAxiom.MORLittered axiom, MORAtom.MORLiteral queriedAtom, String description) {
            super(instance, state, axiom, queriedAtom, description);
        }

        public MORLiteralIntent(Mapping.Intent<GroundIndividual, MORAxiom.MORLittered, MORAtom.MORLiteral, M> copy) {
            super(copy);
        }
    }

    class MORLiteralsIntent<M extends Mapping.State>
            extends Mapping.Intent<GroundIndividual, MORAxiom.MORMultiLettered, MORAtom.MORLiterals,M>{
        public MORLiteralsIntent() {
        }

        public MORLiteralsIntent(GroundIndividual instance, String description) {
            super(instance, description);
        }

        public MORLiteralsIntent(GroundIndividual instance, M state, String description) {
            super(instance, state, description);
        }

        public MORLiteralsIntent(GroundIndividual instance, M state, MORAxiom.MORMultiLettered axiom, String description) {
            super(instance, state, axiom, description);
        }

        public MORLiteralsIntent(GroundIndividual instance, M state, MORAtom.MORLiterals queriedAtom, String description) {
            super(instance, state, queriedAtom, description);
        }

        public MORLiteralsIntent(GroundIndividual instance, M state, MORAxiom.MORMultiLettered axiom, MORAtom.MORLiterals queriedAtom, String description) {
            super(instance, state, axiom, queriedAtom, description);
        }

        public MORLiteralsIntent(Mapping.Intent<GroundIndividual, MORAxiom.MORMultiLettered, MORAtom.MORLiterals, M> copy) {
            super(copy);
        }
    }



    class MORLiteral
        extends SIBase
        implements  Ddef.Data<GroundIndividual,MORAxiom.MORLittered,MORAtom.MORLiteral>,
                    MORDescriptor<GroundIndividual,MORAxiom.MORLittered,MORAtom.MORLiteral>{

        MORAxiom.MORLittered littered = new MORAxiom.MORLittered();

        public MORLiteral(){
        }
        //todo constructors
        public MORLiteral(MORAxiom.MORLittered axiom) {
            setAxiom( axiom);
        }

        public MORLiteral(MORLiteral copy) {
            this.littered = copy.littered;
        }

        @Override
        public MORLiteral copy() {
            return new MORLiteral( this);
        }

        @Override
        public MORAxiom.MORLittered getAxiom() {
            return littered;
        }

        public void setAxiom(MORAxiom.MORLittered axiom) {
            this.littered = axiom;
        }

        @Override
        public Mapping.Transitions
        read(GroundIndividual instance, MORAxiom.MORLittered axiom, MORAtom.MORLiteral queried) {
             return new MORGround.MORIndividualTryRead<MORAxiom.MORLittered, MORAtom.MORLiteral>
                    (new MORLiteralIntent<>( instance, new Mapping.ReadingState(), axiom, queried, "")){ // todo to describe
                @Override
                public Transitions giveAtry() {

                    if( ! axiom.exists()){
                        if ( ! queried.exists()){
                            (( ReadingState)getLastIntent().getState()).asAbsent();
                        } else {
                            axiom.getAtom().set( queried.get());
                            (( ReadingState) getLastIntent().getState()).asSuccess();
                        }
                    } else {
                        if ( ! queried.exists()){
                            axiom.getAtom().clear();
                            (( ReadingState) getLastIntent().getState()).asAbsent();
                        } else {
                            if( axiom.getAtom().equals( queried)){
                                getLastIntent().getState().asNotChanged();
                            } else {
                                if ( axiom.getAtom().equals( queried)){
                                    getLastIntent().getState().asNotChanged();
                                } else {
                                    axiom.getAtom().set(queried.get()); // todo avarage !?
                                    (( ReadingState) getLastIntent().getState()).asSuccess();
                                }
                            }
                        }
                    }

                    return getStateTransitions();
                }
                @Override
                public MORLiteralIntent<ReadingState> instantiateIntent(GroundIndividual instance, String description) {
                    return new MORLiteralIntent<>( instance, new ReadingState(), description);
                }
            }.perform();
        }

        @Override
        public Mapping.Transitions write(GroundIndividual instance, MORAxiom.MORLittered axiom, MORAtom.MORLiteral queried) {
            return new MORGround.MORIndividualTryWrite<MORAxiom.MORLittered, MORAtom.MORLiteral>
                    (new MORLiteralIntent<>( instance, new Mapping.WritingState(), axiom, queried, "")){ // todo to describe
                @Override
                public Transitions giveAtry() {

                    if( ! axiom.exists()){
                        if ( ! queried.exists()){
                            getLastIntent().getState().asNotChanged();
                        } else {
                            axiom.removeAxiom( instance);
                            (( WritingState) getLastIntent().getState()).asRemoved();
                        }
                    } else {
                        if ( ! queried.exists()){
                            axiom.addAxiom( instance);
                            (( WritingState) getLastIntent().getState()).asAdded();
                        } else {
                            if( axiom.getAtom().equals( queried)){
                                getLastIntent().getState().asNotChanged();
                            } else {
                                axiom.removeAxiom(instance);
                                queried.addAxiom(instance, axiom.getSemantic()); // todo avarage !?
                                (( WritingState) getLastIntent().getState()).asUpdated();
                            }
                        }
                    }

                    return getStateTransitions();
                }
                @Override
                public MORLiteralIntent<WritingState> instantiateIntent(GroundIndividual instance, String description) {
                    return new MORLiteralIntent<>( instance, new WritingState(), description);
                }
            }.perform();
        }

    }


    class MORLiterals
        extends SIBase
        implements  Ddef.DataSet<GroundIndividual,MORAxiom.MORMultiLettered,MORAtom.MORLiterals>,
                    MORDescriptor<GroundIndividual, MORAxiom.MORMultiLettered,MORAtom.MORLiterals>{

        MORAxiom.MORMultiLettered multiLittered = new MORAxiom.MORMultiLettered();

        public MORLiterals(){
        }
        //todo constructors
        public MORLiterals(MORAxiom.MORMultiLettered axiom) {
            setAxiom( axiom);
        }

        public MORLiterals(MORLiterals copy) {
            this.multiLittered = copy.multiLittered;
        }

        @Override
        public MORLiterals copy() {
            return new MORLiterals( this);
        }

        @Override
        public MORAxiom.MORMultiLettered getAxiom() {
            return multiLittered;
        }

        public void setAxiom(MORAxiom.MORMultiLettered axiom) {
            this.multiLittered = axiom;
        }

        @Override
        public Mapping.Transitions read(GroundIndividual instance, MORAxiom.MORMultiLettered axiom, MORAtom.MORLiterals queried) {
             return new MORGround.MORIndividualTryRead<MORAxiom.MORMultiLettered, MORAtom.MORLiterals>
                    (new MORLiteralsIntent<>( instance, new Mapping.ReadingState(), axiom, queried, "")){ // todo to describe
                @Override
                public Transitions giveAtry() {
                    if( ! axiom.exists()){
                        if ( ! queried.exists()){
                            (( ReadingState) getLastIntent().getState()).asAbsent();
                        } else {
                            MORLiteral descriptor = new MORLiteral();
                            for ( MORAtom.MORLiteral q : queried.get()){
                                Transitions ins = descriptor.read(instance, new MORAxiom.MORLittered(axiom.getSemantic()), q); // todo no multiple query !!!!
                                getStateTransitions().addAll( ins); // todo to check if bug
                            }
                            (( ReadingState) getLastIntent().getState()).asSuccess();
                        }
                    } else {
                        if ( ! queried.exists()){
                            axiom.getAtom().clear();
                            (( ReadingState) getLastIntent().getState()).asAbsent();
                        } else {
                            if( axiom.getAtom().equals( queried)){ // todo to check if bug
                                getLastIntent().getState().asNotChanged();
                            } else {
                                for ( MORAtom.MORLiteral q : queried.get()){
                                    Intent subIntent = getNewIntent(instance, "", axiom, q); // todo describe
                                    if ( axiom.getAtom().get().contains( q)){
                                        subIntent.getState().asNotChanged();
                                    } else {
                                        axiom.getAtom().get().add( q);
                                        (( ReadingState) subIntent.getState()).asSuccess();
                                    }
                                }
                                for ( MORAtom.MORLiteral a : axiom.getAtom().get()){
                                    Intent subIntent = getNewIntent(instance, "", axiom, a); // todo describe
                                    if ( ! queried.get().contains( a)) {
                                        axiom.getAtom().get().remove(a.get());
                                        (( ReadingState) subIntent.getState()).asAbsent();
                                    }
                                }
                                (( ReadingState) getLastIntent().getState()).asSuccess();
                            }
                        }
                    }

                    return getStateTransitions();
                }
                @Override
                public MORLiteralIntent<ReadingState> instantiateIntent(GroundIndividual instance, String description) {
                    return new MORLiteralIntent<>(instance, new ReadingState(), description);
                }
            }.perform();
        }

        @Override
        public Mapping.Transitions
        write(GroundIndividual instance, MORAxiom.MORMultiLettered axiom, MORAtom.MORLiterals queried) {
            return new MORGround.MORIndividualTryWrite<MORAxiom.MORMultiLettered, MORAtom.MORLiterals>
                    (new MORLiteralsIntent<>( instance, new Mapping.WritingState(), axiom, queried, "")){ // todo to describe
                @Override
                public Transitions giveAtry() {
                    if( ! axiom.exists()){
                        if ( ! queried.exists()){
                            (( WritingState) getLastIntent().getState()).asNotChanged();
                        } else {
                            MORLiteral descriptor = new MORLiteral();
                            for ( MORAtom.MORLiteral q : queried.get()){
                                Transitions ins = descriptor.write(instance, new MORAxiom.MORLittered(axiom.getSemantic()), q); // todo no multiple query !!!!
                                getStateTransitions().addAll( ins); // todo to check if bug
                            }
                            (( WritingState) getLastIntent().getState()).asRemoved();
                        }
                    } else {
                        if ( ! queried.exists()){
                            axiom.getAtom().clear();
                            (( WritingState) getLastIntent().getState()).asRemoved();
                        } else {
                            if( axiom.getAtom().equals( queried)){ // todo to check if bug
                                getLastIntent().getState().asNotChanged();
                            } else {
                                for ( MORAtom.MORLiteral q : queried.get()){
                                    Intent subIntent = getNewIntent(instance, "", axiom, q); // todo describe
                                    if ( axiom.getAtom().get().contains( q)){
                                        subIntent.getState().asNotChanged();
                                    } else {
                                        q.addAxiom( instance, axiom.getSemantic());
                                        (( WritingState) subIntent.getState()).asAdded();
                                    }
                                }
                                for ( MORAtom.MORLiteral a : axiom.getAtom().get()){
                                    Intent subIntent = getNewIntent(instance, "", axiom, a); // todo describe
                                    if ( ! queried.get().contains( a)) {
                                        a.removeAxiom( instance, axiom.getSemantic());
                                        (( WritingState) subIntent.getState()).asRemoved();
                                    }
                                }
                                (( WritingState) getLastIntent().getState()).asUpdated();
                            }
                        }
                    }

                    return getStateTransitions();
                }
                @Override
                public MORLiteralIntent<WritingState> instantiateIntent(GroundIndividual instance, String description) {
                    return new MORLiteralIntent<>(instance, new WritingState(), description);
                }
            }.perform();
        }

    }
}
