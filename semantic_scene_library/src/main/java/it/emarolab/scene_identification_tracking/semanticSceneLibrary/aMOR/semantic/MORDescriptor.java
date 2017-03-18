package it.emarolab.scene_identification_tracking.semanticSceneLibrary.aMOR.semantic;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.synchronisation.Descriptor;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.synchronisation.Mapping;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.reasoner.OWLReasonerRuntimeException;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by bubx on 17/03/17.
 */
public interface MORDescriptor {

    class MORTypeDescriptor
            implements Descriptor.Typing<OWLReferences,OWLNamedIndividual,MORSemantic.MORType> {
        @Override
        public ReadOutcome<OWLNamedIndividual, MORAxiom.MORTyped>
                read(OWLReferences ontology, OWLNamedIndividual instance, MORSemantic.MORType semantic) {

            final MORAxiom.MORTyped owl = semantic.query(ontology, instance);
            final MORAxiom.MORTyped java = semantic.get();

            Mapping.Transitions<Mapping.Intent<OWLNamedIndividual,MORAxiom.MORTyped,Mapping.ReadingState>>
                    transitions = new MORTryRead<OWLNamedIndividual, MORAxiom.MORTyped>() {
                @Override
                public Mapping.Transitions giveAtry() {

                    Mapping.Intent<OWLNamedIndividual, MORAxiom.MORTyped, Mapping.ReadingState>
                            intent = getNewIntent(instance, LOGGING.INTENT_BELONG, java, owl);

                    if ( java.hasParents()) {
                        if ( owl.hasParents()) {
                            // both red, nothing changed
                            if ( java.equals( owl)) {
                                intent.getState().asNotChanged();
                            } else {
                                Set<OWLClass> javaValueCopy = new HashSet<>( java.getParents());
                                for (OWLClass cl : owl.getParents()) {

                                    Mapping.Intent<OWLNamedIndividual, MORAxiom.MORTyped, Mapping.ReadingState>
                                            otherIntent = getNewIntent(instance, LOGGING.INTENT_BELONG_ADD,
                                                null, new MORAxiom.MORTyped( cl));

                                    if ( java.getParents().contains(cl)) {
                                        // both read, not changed
                                        otherIntent.getState().asNotChanged();
                                        // it does not do nothing on the set
                                        // take track of what has not beet added, to be removed later
                                        javaValueCopy.remove(cl);
                                    } else {
                                        // both read, added
                                        otherIntent.getState().asSuccess();
                                        java.getParents().add( cl);
                                    }
                                }
                                for (OWLClass rm : javaValueCopy) {

                                    Mapping.Intent<OWLNamedIndividual, MORAxiom.MORTyped, Mapping.ReadingState>
                                            removeIntent = getNewIntent(instance, LOGGING.INTENT_BELONG_REMOVE,
                                                new MORAxiom.MORTyped( rm), null);

                                    // remove remaining value
                                    removeIntent.getState().asAbsent();
                                    java.getParents().remove( rm);
                                }
                            }
                        } else {
                            // nothing read, nothing changed
                            if ( java.getParents().isEmpty()) {
                                intent.getState().asNotChanged();
                            } else {
                                // nothing read, remove all
                                java.getParents().clear();
                                intent.getState().asSuccess();
                            }
                        }
                    } else { // java cannot be read
                        logError("Cannot write null types for: " + instance);
                        intent.getState().asError();
                    }

                    return getStateTransitions();
                }
            }.perform();

            return new ReadOutcome<>( instance, java, transitions);
        }

        @Override
        public WriteOutcome<OWLNamedIndividual, MORAxiom.MORTyped>
                write(OWLReferences ontology, OWLNamedIndividual instance, MORSemantic.MORType semantic) {

            final MORAxiom.MORTyped owl = semantic.query(ontology, instance);
            final MORAxiom.MORTyped java = semantic.get();

            Mapping.Transitions<Mapping.Intent<OWLNamedIndividual,MORAxiom.MORTyped,Mapping.WritingState>>
                    transitions = new MORTryWrite<OWLNamedIndividual, MORAxiom.MORTyped>() {
                @Override
                public Mapping.Transitions giveAtry() {

                    Mapping.Intent<OWLNamedIndividual, MORAxiom.MORTyped, Mapping.WritingState>
                            intent = getNewIntent(instance, "€", java, owl); // todo add in VOCABULARY

                    if( java.hasParents()) {
                        if ( owl.hasParents()) {
                            // both red, nothing changed
                            if( owl.equals( java)) {
                                intent.getState().asNotChanged();
                            } else {
                                HashSet<OWLClass> semanticValueCopy = new HashSet<>( owl.getParents());
                                for (OWLClass cl : java.getParents()){

                                    Mapping.Intent<OWLNamedIndividual, MORAxiom.MORTyped, Mapping.WritingState>
                                            otherIntent = getNewIntent(instance, LOGGING.INTENT_BELONG_ADD,
                                                new MORAxiom.MORTyped( cl), null);

                                    if ( owl.getParents().contains( cl)){
                                        // both read, not changed
                                        otherIntent.getState().asNotChanged();
                                        // it does not do nothing on the set
                                        // take track of what has not beet added, to be removed later
                                        semanticValueCopy.remove( cl);
                                    } else {
                                        // both read, added
                                        otherIntent.getState().asAdded();
                                        semantic.add( ontology, instance, cl);
                                    }
                                }
                                for (OWLClass rm : semanticValueCopy){

                                    Mapping.Intent<OWLNamedIndividual, MORAxiom.MORTyped, Mapping.WritingState>
                                            removeIntent = getNewIntent(instance, LOGGING.INTENT_BELONG_REMOVE,
                                                null, new MORAxiom.MORTyped(rm));

                                    // remove remaining value
                                    semantic.remove( ontology, instance, rm);
                                    removeIntent.getState().asRemoved();
                                }
                            }
                        } else {
                            // nothing read, nothing changed
                            if ( java.getParents().isEmpty()){
                                intent.getState().asNotChanged();
                            } else {
                                // nothing read, write all
                                for (OWLClass cl : java.getParents()){
                                    //Semantics.WritingState writeAllState = getTypeIntent( getOWLName( cl), javaValue, semanticValue);

                                    Mapping.Intent<OWLNamedIndividual, MORAxiom.MORTyped, Mapping.WritingState>
                                            otherIntent = getNewIntent(instance, LOGGING.INTENT_BELONG_ADD,
                                                new MORAxiom.MORTyped(cl), null);

                                    otherIntent.getState().asAdded();
                                    semantic.add( ontology, instance, cl);
                                }
                            }
                        }
                    } else { // java cannot be read
                        logError( "Cannot write null types for: " + instance);
                        intent.getState().asError();
                    }

                    return getStateTransitions();
                }
            }.perform();

            return new WriteOutcome<>( instance, java, transitions);
        }
    }

    class MORDataDescriptor
            implements Descriptor.Properting<OWLReferences,OWLNamedIndividual,MORSemantic.MORData>{

        @Override
        public ReadOutcome<OWLNamedIndividual,MORAxiom.MORDataValue>
                read(OWLReferences ontology, OWLNamedIndividual instance, MORSemantic.MORData semantic) {

            final MORAxiom.MORDataValue owl = semantic.query(ontology, instance);
            final MORAxiom.MORDataValue java = semantic.get();

            Mapping.Transitions<Mapping.Intent<OWLNamedIndividual,MORAxiom.MORDataValue,Mapping.ReadingState>>
                    transitions = new MORTryRead<OWLNamedIndividual, MORAxiom.MORDataValue>() {
                @Override
                public Mapping.Transitions giveAtry() {
                    // todo implement
                    return getStateTransitions();
                }
            }.perform();

            return new ReadOutcome<>( instance, java, transitions);
        }

        @Override
        public WriteOutcome<OWLNamedIndividual,MORAxiom.MORDataValue>
                write(OWLReferences ontology, OWLNamedIndividual instance, MORSemantic.MORData semantic) {

            final MORAxiom.MORDataValue owl = semantic.query(ontology, instance);
            final MORAxiom.MORDataValue java = semantic.get();

            Mapping.Transitions<Mapping.Intent<OWLNamedIndividual,MORAxiom.MORDataValue,Mapping.WritingState>>
                    transitions = new MORTryWrite<OWLNamedIndividual, MORAxiom.MORDataValue>() {
                @Override
                public Mapping.Transitions giveAtry() {
                    // todo implement
                    return getStateTransitions();
                }
            }.perform();

            return new WriteOutcome<>( instance, java, transitions);
        }
    }

    class MORData3Descriptor
            implements Descriptor.Properting3D<OWLReferences,OWLNamedIndividual,MORSemantic.MORData3D>{

        @Override
        public ReadOutcome<OWLNamedIndividual,MORAxiom.MORDataValue3D>
                read(OWLReferences ontology, OWLNamedIndividual instance, MORSemantic.MORData3D semantic) {

            final MORAxiom.MORDataValue3D owl = semantic.query(ontology, instance);
            final MORAxiom.MORDataValue3D java = semantic.get();

            Mapping.Transitions<Mapping.Intent<OWLNamedIndividual,MORAxiom.MORDataValue3D,Mapping.ReadingState>>
                    transitions = new MORTryRead<OWLNamedIndividual, MORAxiom.MORDataValue3D>() {
                @Override
                public Mapping.Transitions giveAtry() {
                    // todo implement
                    return getStateTransitions();
                }
            }.perform();

            return new ReadOutcome<>( instance, java, transitions);
        }

        @Override
        public WriteOutcome<OWLNamedIndividual,MORAxiom.MORDataValue3D>
                write(OWLReferences ontology, OWLNamedIndividual instance, MORSemantic.MORData3D semantic) {

            final MORAxiom.MORDataValue3D owl = semantic.query(ontology, instance);
            final MORAxiom.MORDataValue3D java = semantic.get();

            Mapping.Transitions<Mapping.Intent<OWLNamedIndividual,MORAxiom.MORDataValue3D,Mapping.WritingState>>
                    transitions = new MORTryWrite<OWLNamedIndividual, MORAxiom.MORDataValue3D>() {
                @Override
                public Mapping.Transitions giveAtry() {
                    // todo implement
                    return getStateTransitions();
                }
            }.perform();

            return new WriteOutcome<>( instance, java, transitions);
        }
    }

    class MORLinksDescriptor
            implements Descriptor.MultiProperting<OWLReferences,OWLNamedIndividual,MORSemantic.MORLinks>{
        @Override
        public ReadOutcome<OWLNamedIndividual,MORAxiom.MORMultiLinked>
                read(OWLReferences ontology, OWLNamedIndividual instance, MORSemantic.MORLinks semantic) {

            final MORAxiom.MORMultiLinked owl = semantic.query(ontology, instance);
            final MORAxiom.MORMultiLinked java = semantic.get();

            Mapping.Transitions<Mapping.Intent<OWLNamedIndividual,MORAxiom.MORMultiLinked,Mapping.ReadingState>>
                    transitions = new MORTryRead<OWLNamedIndividual, MORAxiom.MORMultiLinked>() {
                @Override
                public Mapping.Transitions giveAtry() {
                    // todo implement
                    return getStateTransitions();
                }
            }.perform();

            return new ReadOutcome<>( instance, java, transitions);
        }

        @Override
        public WriteOutcome<OWLNamedIndividual,MORAxiom.MORMultiLinked>
                write(OWLReferences ontology, OWLNamedIndividual instance, MORSemantic.MORLinks semantic) {

            final MORAxiom.MORMultiLinked owl = semantic.query(ontology, instance);
            final MORAxiom.MORMultiLinked java = semantic.get();

            Mapping.Transitions<Mapping.Intent<OWLNamedIndividual,MORAxiom.MORMultiLinked,Mapping.WritingState>>
                    transitions = new MORTryWrite<OWLNamedIndividual, MORAxiom.MORMultiLinked>() {
                @Override
                public Mapping.Transitions giveAtry() {
                    // todo implement
                    return getStateTransitions();
                }
            }.perform();

            return new WriteOutcome<>( instance, java, transitions);
        }
    }

    class MORHierarchyDescriptor
            implements Descriptor.Hierarching<OWLReferences,OWLClass,MORSemantic.MORHierarchy>{

        @Override
        public ReadOutcome<OWLClass,MORAxiom.MORHierarchised>
                read(OWLReferences ontology, OWLClass instance, MORSemantic.MORHierarchy semantic) {

            final MORAxiom.MORHierarchised owl = semantic.query(ontology, instance);
            final MORAxiom.MORHierarchised java = semantic.get();

            Mapping.Transitions<Mapping.Intent<OWLClass,MORAxiom.MORHierarchised,Mapping.ReadingState>>
                    transitions = new MORTryRead<OWLClass, MORAxiom.MORHierarchised>() {
                @Override
                public Mapping.Transitions giveAtry() {
                    // todo implement
                    return getStateTransitions();
                }
            }.perform();

            return new ReadOutcome<>( instance, java, transitions);
        }

        @Override
        public WriteOutcome<OWLClass,MORAxiom.MORHierarchised>
                write(OWLReferences ontology, OWLClass instance, MORSemantic.MORHierarchy semantic) {

            final MORAxiom.MORHierarchised owl = semantic.query(ontology, instance);
            final MORAxiom.MORHierarchised java = semantic.get();

            Mapping.Transitions<Mapping.Intent<OWLClass,MORAxiom.MORHierarchised,Mapping.WritingState>>
                    transitions = new MORTryWrite<OWLClass, MORAxiom.MORHierarchised>() {
                @Override
                public Mapping.Transitions giveAtry() {
                    // todo implement
                    return getStateTransitions();
                }
            }.perform();

            return new WriteOutcome<>( instance, java, transitions);
        }
    }

    class MORClassRestriction
            implements Descriptor.ClassRestricting<OWLReferences,OWLClass,MORSemantic.MORMinCardinalityRestriction>{

        @Override
        public ReadOutcome<OWLClass,MORAxiom.MORMultiMinCardinalised>
                read(OWLReferences ontology, OWLClass instance, MORSemantic.MORMinCardinalityRestriction semantic) {

            final MORAxiom.MORMultiMinCardinalised owl = semantic.query(ontology, instance);
            final MORAxiom.MORMultiMinCardinalised java = semantic.get();

            Mapping.Transitions<Mapping.Intent<OWLClass,MORAxiom.MORMultiMinCardinalised,Mapping.ReadingState>>
                    transitions = new MORTryRead<OWLClass, MORAxiom.MORMultiMinCardinalised>() {
                @Override
                public Mapping.Transitions giveAtry() {
                    // todo implement
                    return getStateTransitions();
                }
            }.perform();

            return new ReadOutcome<>( instance, java, transitions);
        }

        @Override
        public WriteOutcome<OWLClass,MORAxiom.MORMultiMinCardinalised>
                write(OWLReferences ontology, OWLClass instance, MORSemantic.MORMinCardinalityRestriction semantic) {

            final MORAxiom.MORMultiMinCardinalised owl = semantic.query(ontology, instance);
            final MORAxiom.MORMultiMinCardinalised java = semantic.get();

            Mapping.Transitions<Mapping.Intent<OWLClass,MORAxiom.MORMultiMinCardinalised,Mapping.WritingState>>
                    transitions = new MORTryWrite<OWLClass, MORAxiom.MORMultiMinCardinalised>() {
                @Override
                public Mapping.Transitions giveAtry() {
                    // todo implement
                    return getStateTransitions();
                }
            }.perform();

            return new WriteOutcome<>( instance, java, transitions);
        }
    }


    interface MORTrier<I extends OWLObject, A extends MORAxiom, M extends Mapping.State>
            extends Mapping.TrierInterface<I,A,M>{

        default Mapping.Transitions onJavaError(Exception e){
            // todo logError(e)
            getStateTransitions().get( getStateTransitions().size() - 1).getState().asError();
            return getStateTransitions();
        }
        default Mapping.Transitions onOWLError(Exception e){
            // todo logError( "OWL" + e)
            getStateTransitions().get( getStateTransitions().size() - 1).getState().asError();
            return getStateTransitions();
        }
        default Mapping.Transitions onInconsistency(Exception e){
            // todo logError(e)
            getStateTransitions().get( getStateTransitions().size() - 1).getState().asInconsistent();
            return getStateTransitions();
        }

        @Override
        default Mapping.Transitions onError(Exception e){
            if ( e instanceof OWLReasonerRuntimeException)
                return onInconsistency( e);
            if ( e instanceof OWLException)
                return onOWLError( e);
            return onJavaError( e);
        }
    }

    abstract class MORTryWrite<I extends OWLObject, A extends MORAxiom>
            extends Mapping.Trier<I,A,Mapping.WritingState>
            implements MORTrier<I,A,Mapping.WritingState>{

        public MORTryWrite() {
            super();
        }
        public MORTryWrite(Mapping.Intent<I, A, Mapping.WritingState> intent) {
            super(intent);
        }

        @Override
        public Mapping.WritingState getNewState() {
            return new Mapping.WritingState();
        }
    }

    abstract class MORTryRead<I extends OWLObject, A extends MORAxiom>
            extends Mapping.Trier<I,A,Mapping.ReadingState>
            implements MORTrier<I,A,Mapping.ReadingState>{

        public MORTryRead() {
            super();
        }
        public MORTryRead(Mapping.Intent<I, A, Mapping.ReadingState> intent) {
            super(intent);
        }

        @Override
        public Mapping.ReadingState getNewState() {
            return new Mapping.ReadingState();
        }
    }
}
