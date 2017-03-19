package it.emarolab.scene_identification_tracking.semanticSceneLibrary.aMOR.semantic;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.synchronisation.Descriptor;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.synchronisation.Mapping;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.reasoner.OWLReasonerRuntimeException;

/**
 * Created by bubx on 17/03/17.
 */
@SuppressWarnings("ALL")
public interface MORDescriptor {

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

    class MORTypeDescriptor
            implements Descriptor.Typing<OWLReferences,OWLNamedIndividual,MORSemantic.MORType> {
        @Override
        public ReadOutcome<OWLNamedIndividual, MORAxiom.MORTyped>
                read(OWLReferences ontology, OWLNamedIndividual instance) {

            final MORSemantic.MORType semantic = new MORSemantic.MORType();
            final MORAxiom.MORTyped java = semantic.get();

            Mapping.Transitions<Mapping.Intent<OWLNamedIndividual,MORAxiom.MORTyped,Mapping.ReadingState>>
                    transitions = new MORTryRead<OWLNamedIndividual, MORAxiom.MORTyped>() {
                @Override
                public Mapping.Transitions giveAtry() {

                    MORAxiom.MORTyped owl = semantic.query(ontology, instance);

                    Mapping.Intent<OWLNamedIndividual, MORAxiom.MORTyped, Mapping.ReadingState>
                            intent = getNewIntent(instance, LOGGING.INTENT_BELONG, java, owl);

                    new MORSynchronise.SetReader<OWLNamedIndividual, MORAxiom.MORTyped, OWLClass>( intent) {
                        @Override
                        void trigger_JavaNOTExists_OWLExists() {
                            java.getParents().addAll( owl.getParents());
                            super.trigger_JavaNOTExists_OWLExists();
                        }
                        @Override
                        void trigger_JavaExists_OWLNotExists() {
                            java.getParents().clear();
                            super.trigger_JavaExists_OWLNotExists();
                        }
                        @Override
                        void trigger_JavaExists_OWLExists() {
                            update( java.getParents(), owl.getParents());
                        }
                        @Override
                        Mapping.Intent<OWLNamedIndividual, MORAxiom.MORTyped, Mapping.ReadingState>
                        getUpdateIntent( OWLClass j, OWLClass o, String description) {
                            return getNewIntent(instance, LOGGING.INTENT_BELONG + description,
                                    new MORAxiom.MORTyped(j), new MORAxiom.MORTyped(o));
                        }
                    }.synchronise( java, owl);

                    return getStateTransitions();
                }
            }.perform();

            return new ReadOutcome<>( instance, java, transitions);
        }

        @Override
        public WriteOutcome<OWLNamedIndividual, MORAxiom.MORTyped>
                write(OWLReferences ontology, OWLNamedIndividual instance, MORSemantic.MORType semantic) {

            final MORAxiom.MORTyped java = semantic.get();

            Mapping.Transitions<Mapping.Intent<OWLNamedIndividual,MORAxiom.MORTyped,Mapping.WritingState>>
                    transitions = new MORTryWrite<OWLNamedIndividual, MORAxiom.MORTyped>() {
                @Override
                public Mapping.Transitions giveAtry() {

                    MORAxiom.MORTyped owl = semantic.query(ontology, instance);

                    Mapping.Intent<OWLNamedIndividual, MORAxiom.MORTyped, Mapping.WritingState>
                            intent = getNewIntent(instance, "€", java, owl); // todo add in VOCABULARY

                    new MORSynchronise.SetWriter<OWLNamedIndividual, MORAxiom.MORTyped, OWLClass>( intent) {
                        @Override
                        void trigger_JavaNOTExists_OWLExists() {
                            semantic.remove( ontology, instance, owl.getParents());
                            super.trigger_JavaNOTExists_OWLExists();
                        }
                        @Override
                        void trigger_JavaExists_OWLNotExists() {
                            semantic.add( ontology, instance, java.getParents());
                            super.trigger_JavaExists_OWLNotExists();
                        }
                        @Override
                        void trigger_JavaExists_OWLExists() {
                            update( java.getParents(), owl.getParents());
                        }
                        @Override
                        Mapping.Intent<OWLNamedIndividual, MORAxiom.MORTyped, Mapping.WritingState> getUpdateIntent(OWLClass j, OWLClass o, String description) {
                            return getNewIntent(instance, LOGGING.INTENT_BELONG + description,
                                    new MORAxiom.MORTyped(j), new MORAxiom.MORTyped(o));
                        }
                        @Override
                        void addToSemantic(OWLClass t) {
                            semantic.add( ontology, instance, t);
                        }
                        @Override
                        void removeFromSemantic(OWLClass t) {
                            semantic.remove( ontology, instance, t);
                        }
                    }.synchronise( java, owl);

                    return getStateTransitions();
                }
            }.perform();

            return new WriteOutcome<>( instance, java, transitions);
        }
    }

   /* class MORLiteralDescriptor
            implements Descriptor.Properting<OWLReferences,OWLNamedIndividual,MORSemantic.MORLiteral>{

        @Override
        public Descriptor.ReadOutcome<OWLNamedIndividual,MORAxiom.MORLiteralValue>
                synchronise(OWLReferences ontology, OWLNamedIndividual instance, MORSemantic.MORLiteral semantic) {

            final MORAxiom.MORLiteralValue owl = semantic.query(ontology, instance);
            final MORAxiom.MORLiteralValue java = semantic.get();

            Mapping.Transitions<Mapping.Intent<OWLNamedIndividual,MORAxiom.MORLiteralValue,Mapping.ReadingState>>
                    transitions = new MORTryRead<OWLNamedIndividual, MORAxiom.MORLiteralValue>() {
                @Override
                public Mapping.Transitions giveAtry() {

                    Mapping.Intent<OWLNamedIndividual, MORAxiom.MORLiteralValue, Mapping.ReadingState>
                            intent = getNewIntent(instance, "", java, owl); // todo describe

                    // todo implement

                    return getStateTransitions();
                }
            }.synchronise();

            return new Descriptor.ReadOutcome<>( instance, java, transitions);
        }

        @Override
        public Descriptor.WriteOutcome<OWLNamedIndividual,MORAxiom.MORLiteralValue>
                write(OWLReferences ontology, OWLNamedIndividual instance, MORSemantic.MORLiteral semantic) {

            final MORAxiom.MORLiteralValue owl = semantic.query(ontology, instance);
            final MORAxiom.MORLiteralValue java = semantic.get();

            Mapping.Transitions<Mapping.Intent<OWLNamedIndividual,MORAxiom.MORLiteralValue,Mapping.WritingState>>
                    transitions = new MORTryWrite<OWLNamedIndividual, MORAxiom.MORLiteralValue>() {
                @Override
                public Mapping.Transitions giveAtry() {

                    Mapping.Intent<OWLNamedIndividual, MORAxiom.MORLiteralValue, Mapping.WritingState>
                            intent = getNewIntent(instance, "", java, owl);

                    // todo implement

                    return getStateTransitions();
                }
            }.synchronise();

            return new Descriptor.WriteOutcome<>( instance, java, transitions);
        }
    }

    class MORData3Descriptor
            implements Descriptor.Properting3D<OWLReferences,OWLNamedIndividual,MORSemantic.MORData3D>{

        private final int AXIS_TAG_X = 1;
        private final int AXIS_TAG_Y = 2;
        private final int AXIS_TAG_Z = 3;

        private <P,V> void synchronise(OWLReferences ontology, OWLNamedIndividual instance,
                     Semantic.Axiom.Connector3D<P, V> owl, Semantic.Axiom.Connector3D<P, V> java,
                     Mapping.Intent<?,?,Mapping.ReadingState> intent, int axisTag) {
            if( owl.exists()) { // does not exist
                if ( java.exists()){
                    intent.getState().asNotChanged();
                } else {
                    readSpecific( axisTag, java, null);
                    intent.getState().asAbsent();
                }
            } else {
                if ( java.exists()){
                    readSpecific( axisTag, java, owl);
                    intent.getState().asSuccess();
                } else {
                    if ( owl.equals( java)){
                        intent.getState().asNotChanged();
                    } else{
                        java.getX().set( owl.getX());
                        readSpecific( axisTag, java, owl);
                        intent.getState().asSuccess();
                    }
                }
            }
            //return java; //todo to check
        }
        private <P,V> void readSpecific(int axisTag,
                                        Semantic.Axiom.Connector3D<P,V> java, Semantic.Axiom.Connector3D<P,V> owl){
            switch (axisTag){
                case AXIS_TAG_X : java.getX().set( owl.getX()); break;
                case AXIS_TAG_Y : java.getY().set( owl.getY()); break;
                case AXIS_TAG_Z : java.getZ().set( owl.getZ()); break;
            }
        }

        @Override
        public ReadOutcome<OWLNamedIndividual,MORAxiom.MORLiteralValue3D>
                readX(OWLReferences ontology, OWLNamedIndividual instance, MORSemantic.MORData3D semantic) {


            final MORAxiom.MORLiteralValue3D owl = semantic.query(ontology, instance);
            final MORAxiom.MORLiteralValue3D java = semantic.get();

            Mapping.Transitions<Mapping.Intent<OWLNamedIndividual,MORAxiom.MORLiteralValue3D,Mapping.ReadingState>>
                    transitions = new MORTryRead<OWLNamedIndividual, MORAxiom.MORLiteralValue3D>() {
                @Override
                public Mapping.Transitions giveAtry() {

                    Mapping.Intent<OWLNamedIndividual, MORAxiom.MORLiteralValue3D, Mapping.ReadingState>
                            intent = getNewIntent(instance, "", java, owl); // todo describe

                    synchronise( ontology, instance, owl, java, intent, AXIS_TAG_X);

                    return getStateTransitions();
                }
            }.synchronise();

            return new ReadOutcome<>( instance, java, transitions);
        }
        @Override
        public ReadOutcome<OWLNamedIndividual,MORAxiom.MORLiteralValue3D>
                readY(OWLReferences ontology, OWLNamedIndividual instance, MORSemantic.MORData3D semantic) {


            final MORAxiom.MORLiteralValue3D owl = semantic.query(ontology, instance);
            final MORAxiom.MORLiteralValue3D java = semantic.get();

            Mapping.Transitions<Mapping.Intent<OWLNamedIndividual,MORAxiom.MORLiteralValue3D,Mapping.ReadingState>>
                    transitions = new MORTryRead<OWLNamedIndividual, MORAxiom.MORLiteralValue3D>() {
                @Override
                public Mapping.Transitions giveAtry() {

                    Mapping.Intent<OWLNamedIndividual, MORAxiom.MORLiteralValue3D, Mapping.ReadingState>
                            intent = getNewIntent(instance, "", java, owl); // todo describe

                    synchronise( ontology, instance, owl, java, intent, AXIS_TAG_Y);

                    return getStateTransitions();
                }
            }.synchronise();

            return new ReadOutcome<>( instance, java, transitions);
        }
        @Override
        public ReadOutcome<OWLNamedIndividual,MORAxiom.MORLiteralValue3D>
                readZ(OWLReferences ontology, OWLNamedIndividual instance, MORSemantic.MORData3D semantic) {


            final MORAxiom.MORLiteralValue3D owl = semantic.query(ontology, instance);
            final MORAxiom.MORLiteralValue3D java = semantic.get();

            Mapping.Transitions<Mapping.Intent<OWLNamedIndividual,MORAxiom.MORLiteralValue3D,Mapping.ReadingState>>
                    transitions = new MORTryRead<OWLNamedIndividual, MORAxiom.MORLiteralValue3D>() {
                @Override
                public Mapping.Transitions giveAtry() {

                    Mapping.Intent<OWLNamedIndividual, MORAxiom.MORLiteralValue3D, Mapping.ReadingState>
                            intent = getNewIntent(instance, "", java, owl); // todo describe

                    synchronise( ontology, instance, owl, java, intent, AXIS_TAG_Z);

                    return getStateTransitions();
                }
            }.synchronise();

            return new ReadOutcome<>( instance, java, transitions);
        }

        private <P,V> void write(OWLReferences ontology, OWLNamedIndividual instance,
                                 MORSemantic.MORData3D semantic,
                                 Semantic.Axiom.Connector3D<P, V> owl, Semantic.Axiom.Connector3D<P, V> java,
                                 Mapping.Intent<?,?,Mapping.WritingState> intent, int axisTag) {
            if( ! owl.exists()) { // does not exist
                if ( java.exists()){
                    intent.getState().asNotChanged();
                } else {
                    addSpecific( axisTag, semantic, ontology, instance, java);
                    intent.getState().asAdded();
                }
            } else {
                if ( ! java.exists()){
                    removeSpecific( axisTag, semantic, ontology, instance, java);
                    intent.getState().asRemoved();
                } else {
                    if ( owl.equals( java)){
                        intent.getState().asNotChanged();
                    } else{
                        // REPLACE
                        removeSpecific( axisTag, semantic, ontology, instance, java);
                        addSpecific( axisTag, semantic, ontology, instance, java);
                        intent.getState().asUpdated();
                    }
                }
            }
            //return java; //todo to check
        }
        private void addSpecific(int axisTag, MORSemantic.MORData3D semantic,
                                 OWLReferences ontology, OWLNamedIndividual instance,
                                 Semantic.Axiom.Connector3D<?,?> java){
            switch (axisTag){
                case AXIS_TAG_X : semantic.addX( ontology, instance, java.getX()); break;
                case AXIS_TAG_Y : semantic.addY( ontology, instance, java.getY()); break;
                case AXIS_TAG_Z : semantic.addZ( ontology, instance, java.getZ()); break;
            }
        }
        private void removeSpecific(int axisTag, MORSemantic.MORData3D semantic,
                                 OWLReferences ontology, OWLNamedIndividual instance,
                                 Semantic.Axiom.Connector3D<?,?> java){
            switch (axisTag){
                case AXIS_TAG_X : semantic.removeX( ontology, instance, java.getX()); break;
                case AXIS_TAG_Y : semantic.removeY( ontology, instance, java.getY()); break;
                case AXIS_TAG_Z : semantic.removeZ( ontology, instance, java.getZ()); break;
            }
        }

        @Override
        public WriteOutcome<OWLNamedIndividual,MORAxiom.MORLiteralValue3D>
                writeX(OWLReferences ontology, OWLNamedIndividual instance, MORSemantic.MORData3D semantic) {

            final MORAxiom.MORLiteralValue3D owl = semantic.query(ontology, instance);
            final MORAxiom.MORLiteralValue3D java = semantic.get();

            Mapping.Transitions<Mapping.Intent<OWLNamedIndividual,MORAxiom.MORLiteralValue3D,Mapping.WritingState>>
                    transitions = new MORTryWrite<OWLNamedIndividual, MORAxiom.MORLiteralValue3D>() {
                @Override
                public Mapping.Transitions giveAtry() {

                    Mapping.Intent<OWLNamedIndividual, MORAxiom.MORLiteralValue3D, Mapping.WritingState>
                            intent = getNewIntent(instance, "", java, owl); // todo describe

                    write( ontology, instance, semantic, owl, java, intent, AXIS_TAG_X);

                    return getStateTransitions();
                }
            }.synchronise();

            return new WriteOutcome<>( instance, java, transitions);
        }
        @Override
        public WriteOutcome<OWLNamedIndividual,MORAxiom.MORLiteralValue3D>
                writeY(OWLReferences ontology, OWLNamedIndividual instance, MORSemantic.MORData3D semantic) {

            final MORAxiom.MORLiteralValue3D owl = semantic.query(ontology, instance);
            final MORAxiom.MORLiteralValue3D java = semantic.get();

            Mapping.Transitions<Mapping.Intent<OWLNamedIndividual,MORAxiom.MORLiteralValue3D,Mapping.WritingState>>
                    transitions = new MORTryWrite<OWLNamedIndividual, MORAxiom.MORLiteralValue3D>() {
                @Override
                public Mapping.Transitions giveAtry() {

                    Mapping.Intent<OWLNamedIndividual, MORAxiom.MORLiteralValue3D, Mapping.WritingState>
                            intent = getNewIntent(instance, "", java, owl); // todo describe

                    write( ontology, instance, semantic, owl, java, intent, AXIS_TAG_Y);

                    return getStateTransitions();
                }
            }.synchronise();

            return new WriteOutcome<>( instance, java, transitions);
        }
        @Override
        public WriteOutcome<OWLNamedIndividual,MORAxiom.MORLiteralValue3D>
                writeZ(OWLReferences ontology, OWLNamedIndividual instance, MORSemantic.MORData3D semantic) {

            final MORAxiom.MORLiteralValue3D owl = semantic.query(ontology, instance);
            final MORAxiom.MORLiteralValue3D java = semantic.get();

            Mapping.Transitions<Mapping.Intent<OWLNamedIndividual,MORAxiom.MORLiteralValue3D,Mapping.WritingState>>
                    transitions = new MORTryWrite<OWLNamedIndividual, MORAxiom.MORLiteralValue3D>() {
                @Override
                public Mapping.Transitions giveAtry() {

                    Mapping.Intent<OWLNamedIndividual, MORAxiom.MORLiteralValue3D, Mapping.WritingState>
                            intent = getNewIntent(instance, "", java, owl); // todo describe

                    write( ontology, instance, semantic, owl, java, intent, AXIS_TAG_Z);

                    return getStateTransitions();
                }
            }.synchronise();

            return new WriteOutcome<>( instance, java, transitions);
        }
    }

    class MORLinksDescriptor
            implements Descriptor.MultiProperting<OWLReferences,OWLNamedIndividual,MORSemantic.MORLinks>{
        @Override
        public Descriptor.ReadOutcome<OWLNamedIndividual,MORAxiom.MORMultiLinked>
                synchronise(OWLReferences ontology, OWLNamedIndividual instance, MORSemantic.MORLinks semantic) {

            final MORAxiom.MORMultiLinked owl = semantic.query(ontology, instance);
            final MORAxiom.MORMultiLinked java = semantic.get();

            Mapping.Transitions<Mapping.Intent<OWLNamedIndividual,MORAxiom.MORMultiLinked,Mapping.ReadingState>>
                    transitions = new MORTryRead<OWLNamedIndividual, MORAxiom.MORMultiLinked>() {
                @Override
                public Mapping.Transitions giveAtry() {

                    Mapping.Intent<OWLNamedIndividual, MORAxiom.MORMultiLinked, Mapping.ReadingState>
                            intent = getNewIntent(instance, LOGGING.INTENT_BELONG, java, owl);

                    // todo implement

                    return getStateTransitions();
                }
            }.synchronise();

            return new Descriptor.ReadOutcome<>( instance, java, transitions);
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
            }.synchronise();

            return new Descriptor.WriteOutcome<>( instance, java, transitions);
        }
    }

    class MORClassRestriction
            implements Descriptor.ClassRestricting<OWLReferences,OWLClass,MORSemantic.MORMinCardinalityRestriction>{

        @Override
        public ReadOutcome<OWLClass,MORAxiom.MORMultiMinCardinalised>
                synchronise(OWLReferences ontology, OWLClass instance, MORSemantic.MORMinCardinalityRestriction semantic) {

            final MORAxiom.MORMultiMinCardinalised owl = semantic.query(ontology, instance);
            final MORAxiom.MORMultiMinCardinalised java = semantic.get();

            Mapping.Transitions<Mapping.Intent<OWLClass,MORAxiom.MORMultiMinCardinalised,Mapping.ReadingState>>
                    transitions = new MORTryRead<OWLClass, MORAxiom.MORMultiMinCardinalised>() {
                @Override
                public Mapping.Transitions giveAtry() {
                    // todo implement
                    return getStateTransitions();
                }
            }.synchronise();

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
            }.synchronise();

            return new WriteOutcome<>( instance, java, transitions);
        }
    }
*/

    class MORHierarchyDescriptor
            implements Descriptor.Hierarching<OWLReferences,OWLClass,MORSemantic.MORHierarchy>{

        @Override
        public Descriptor.ReadOutcome<OWLClass,MORAxiom.MORHierarchised>
        read(OWLReferences ontology, OWLClass instance) {

            final MORSemantic.MORHierarchy semantic = new MORSemantic.MORHierarchy();
            final MORAxiom.MORHierarchised java = semantic.get();

            Mapping.Transitions<Mapping.Intent<OWLClass,MORAxiom.MORHierarchised,Mapping.ReadingState>>
                    transitions = new MORTryRead<OWLClass, MORAxiom.MORHierarchised>() {
                @Override
                public Mapping.Transitions giveAtry() {
                    MORAxiom.MORHierarchised owl = semantic.query(ontology, instance);

                    Mapping.Intent<OWLClass, MORAxiom.MORHierarchised, Mapping.ReadingState>
                            intent = getNewIntent(instance, "", java, owl); // to describe

                    new MORSynchronise.SetReader<OWLClass, MORAxiom.MORHierarchised, OWLClass>( intent) {
                        @Override
                        void trigger_JavaNOTExists_OWLExists() {
                            java.getParents().addAll( owl.getParents());
                            java.getChildren().addAll( owl.getChildren());
                            super.trigger_JavaNOTExists_OWLExists();
                        }
                        @Override
                        void trigger_JavaExists_OWLNotExists() {
                            java.getParents().clear();
                            java.getChildren().clear();
                            super.trigger_JavaExists_OWLNotExists();
                        }
                        @Override
                        void trigger_JavaExists_OWLExists() {
                            update( java.getParents(), owl.getParents());
                            update( java.getChildren(), owl.getChildren());
                        }
                        @Override
                        Mapping.Intent<OWLClass, MORAxiom.MORHierarchised, Mapping.ReadingState>
                        getUpdateIntent( OWLClass j, OWLClass o, String description) {
                            // todo add descriptor str for parent & child?
                            return getNewIntent(instance, LOGGING.INTENT_BELONG + description,
                                    new MORAxiom.MORHierarchised(j,null), new MORAxiom.MORHierarchised(o,null));
                        }
                    }.synchronise( java, owl);

                    return getStateTransitions();
                }
            }.perform();

            return new Descriptor.ReadOutcome<>( instance, java, transitions);
        }

        @Override
        public WriteOutcome<OWLClass,MORAxiom.MORHierarchised>
        write(OWLReferences ontology, OWLClass instance, MORSemantic.MORHierarchy semantic) {

            final MORAxiom.MORHierarchised java = semantic.get();

            Mapping.Transitions<Mapping.Intent<OWLClass,MORAxiom.MORHierarchised,Mapping.WritingState>>
                    transitions = new MORTryWrite<OWLClass, MORAxiom.MORHierarchised>() {
                @Override
                public Mapping.Transitions giveAtry() {

                    MORAxiom.MORHierarchised owl = semantic.query(ontology, instance);

                    Mapping.Intent<OWLClass, MORAxiom.MORHierarchised, Mapping.WritingState>
                            intent = getNewIntent(instance, "€", java, owl); // todo add in VOCABULARY

                    new MORSynchronise.SetWriter<OWLClass, MORAxiom.MORHierarchised, OWLClass>( intent) {
                        boolean updateParent = true; // false: updateChildren
                        @Override
                        void trigger_JavaNOTExists_OWLExists() {
                            semantic.removeParents( ontology, instance, owl.getParents());
                            semantic.removeChildren( ontology, instance, owl.getChildren());
                            super.trigger_JavaNOTExists_OWLExists();
                        }
                        @Override
                        void trigger_JavaExists_OWLNotExists() {
                            semantic.addParents( ontology, instance, java.getParents());
                            semantic.addChildren( ontology, instance, java.getChildren());
                            super.trigger_JavaExists_OWLNotExists();
                        }
                        @Override
                        void trigger_JavaExists_OWLExists() {
                            update( java.getParents(), owl.getParents());
                            updateParent = false;
                            update( java.getChildren(), owl.getChildren());
                        }
                        @Override
                        Mapping.Intent<OWLClass, MORAxiom.MORHierarchised, Mapping.WritingState> getUpdateIntent(OWLClass j, OWLClass o, String description) {
                            return getNewIntent(instance, LOGGING.INTENT_BELONG + description,
                                    new MORAxiom.MORHierarchised(j,null), new MORAxiom.MORHierarchised(o,null));
                        }
                        @Override
                        void addToSemantic(OWLClass t) {
                            if( updateParent)
                                semantic.addParents( ontology, instance, t);
                            else semantic.addChildren( ontology, instance, t);
                        }
                        @Override
                        void removeFromSemantic(OWLClass t) {
                            if ( updateParent)
                                semantic.removeParents( ontology, instance, t);
                            else semantic.removeChildren( ontology, instance, t);
                        }
                    }.synchronise( java, owl);

                    return getStateTransitions();
                }
            }.perform();

            return new WriteOutcome<>( instance, java, transitions);
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
