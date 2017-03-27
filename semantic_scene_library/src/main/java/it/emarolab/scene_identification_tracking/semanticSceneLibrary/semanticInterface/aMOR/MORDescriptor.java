package it.emarolab.scene_identification_tracking.semanticSceneLibrary.semanticInterface.aMOR;

import it.emarolab.scene_identification_tracking.semanticSceneLibrary.semanticInterface.Base;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.semanticInterface.aMOR.MORGround.GroundIndividual;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.semanticInterface.core.definition.Ddef;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.semanticInterface.core.synchronise.Mapping;

/**
 * Created by bubx on 27/03/17.
 */
// those are axiom mapper that can eventually contains an axiom.
public interface MORDescriptor<I extends MORGround<?>, X extends MORAxiom<I,?,?>>
        extends Ddef<I,X>{

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

        public MORLiteralIntent(Intent<GroundIndividual, MORAxiom.MORLittered, MORAtom.MORLiteral, M> copy) {
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

        public MORLiteralsIntent(Intent<GroundIndividual, MORAxiom.MORMultiLettered, MORAtom.MORLiterals, M> copy) {
            super(copy);
        }
    }



    class MORLiteral
        extends SIBase
        implements Ddef.Data<GroundIndividual,MORAxiom.MORLittered>, MORDescriptor<GroundIndividual,MORAxiom.MORLittered>{

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
        public MORAxiom.MORLittered getAxiom() {
            return littered;
        }

        public void setAxiom(MORAxiom.MORLittered axiom) {
            this.littered = axiom;
        }

        @Override
        public Mapping.Transitions<MORLiteralIntent<Mapping.ReadingState>>
        read(GroundIndividual instance, MORAxiom.MORLittered axiom) {
            MORAtom.MORLiteral queried = axiom.queryAxiom(instance);

            return new MORGround.MORIndividualTryRead<MORAxiom.MORLittered, MORAtom.MORLiteral,MORLiteralIntent<Mapping.ReadingState>>
                    (new MORLiteralIntent<>( instance, new Mapping.ReadingState(), axiom, queried, "")){ // todo to describe
                @Override
                public Transitions<MORLiteralIntent<ReadingState>> giveAtry() {

                    if( ! axiom.exists()){
                        if ( ! queried.exists()){
                            getLastIntent().getState().asAbsent();
                        } else {
                            axiom.getAtom().set( queried.get());
                            getLastIntent().getState().asSuccess();
                        }
                    } else {
                        if ( ! queried.exists()){
                            axiom.getAtom().clear();
                            getLastIntent().getState().asAbsent();
                        } else {
                            if( axiom.getAtom().equals( queried)){
                                getLastIntent().getState().asNotChanged();
                            } else {
                                if ( axiom.getAtom().equals( queried)){
                                    getLastIntent().getState().asNotChanged();
                                } else {
                                    axiom.getAtom().set(queried.get()); // todo avarage !?
                                    getLastIntent().getState().asSuccess();
                                }
                            }
                        }
                    }

                    return getStateTransitions();
                }
                @Override
                public MORLiteralIntent<ReadingState> instanciateIntent(GroundIndividual instance, String description) {
                    return new MORLiteralIntent<>( instance, description);
                }
            }.perform();
        }

        @Override
        public Mapping.Transitions<MORLiteralIntent<Mapping.WritingState>>
        write(GroundIndividual instance, MORAxiom.MORLittered axiom) {
            MORAtom.MORLiteral queried = axiom.queryAxiom(instance);

            return new MORGround.MORIndividualTryWrite<MORAxiom.MORLittered, MORAtom.MORLiteral,MORLiteralIntent<Mapping.WritingState>>
                    (new MORLiteralIntent<>( instance, new Mapping.WritingState(), axiom, queried, "")){ // todo to describe
                @Override
                public Transitions<MORLiteralIntent<WritingState>> giveAtry() {

                    if( ! axiom.exists()){
                        if ( ! queried.exists()){
                            getLastIntent().getState().asNotChanged();
                        } else {
                            axiom.removeAxiom( instance);
                            getLastIntent().getState().asRemoved();
                        }
                    } else {
                        if ( ! queried.exists()){
                            axiom.addAxiom( instance);
                            getLastIntent().getState().asAdded();
                        } else {
                            if( axiom.getAtom().equals( queried)){
                                getLastIntent().getState().asNotChanged();
                            } else {
                                axiom.removeAxiom(instance);
                                axiom.addAxiom(instance); // todo avarage !?
                                getLastIntent().getState().asUpdated();
                            }
                        }
                    }

                    return getStateTransitions();
                }
                @Override
                public MORLiteralIntent<WritingState> instanciateIntent(GroundIndividual instance, String description) {
                    return new MORLiteralIntent<>( instance, description);
                }
            }.perform();
        }

        @Override
        public MORLiteral copy() {
            return new MORLiteral( this);
        }
    }


    class MORLiterals
        extends SIBase
        implements Ddef.DataSet<GroundIndividual,MORAxiom.MORMultiLettered>, MORDescriptor<GroundIndividual, MORAxiom.MORMultiLettered>{

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
        public MORAxiom.MORMultiLettered getAxiom() {
            return multiLittered;
        }

        public void setAxiom(MORAxiom.MORMultiLettered axiom) {
            this.multiLittered = axiom;
        }

        @Override
        public Mapping.Transitions<MORLiteralIntent<Mapping.ReadingState>>
        read(GroundIndividual instance, MORAxiom.MORMultiLettered axiom) {
            MORAtom.MORLiterals queried = axiom.queryAxiom(instance);

            return null;/*new MORGround.MORIndividualTryRead<MORAxiom.MORMultiLettered, MORAtom.MORLiterals,MORLiteralsIntent<Mapping.ReadingState>>
                    (new MORLiteralsIntent<>( instance, new Mapping.ReadingState(), axiom, queried, "")){ // todo to describe
                @Override
                public Transitions<MORLiteralsIntent<ReadingState>> giveAtry() {

                    if( ! axiom.exists()){
                        if ( ! queried.exists()){
                            getLastIntent().getState().asAbsent();
                        } else {
                            ...
                            getLastIntent().getState().asSuccess();
                        }
                    } else {
                        if ( ! queried.exists()){
                            axiom.getAtom().clear();
                            getLastIntent().getState().asAbsent();
                        } else {
                            if( axiom.getAtom().equals( queried)){
                                ....
                                getLastIntent().getState().asNotChanged();
                            } else {
                                axiom.getAtom().set(queried.get()); // todo avarage !?
                                getLastIntent().getState().asSuccess();
                            }
                        }
                    }

                    return getStateTransitions();
                }
                @Override
                public MORLiteralsIntent<ReadingState> instanciateIntent(GroundIndividual instance, String description) {
                    return new MORLiteralsIntent<>( instance, description);
                }
            }.perform();*/
        }

        @Override
        public Mapping.Transitions<MORLiteralIntent<Mapping.WritingState>>
        write(GroundIndividual instance, MORAxiom.MORMultiLettered axiom) {
            return null;
        }

        @Override
        public Base copy() {
            return null;
        }
    }
}
