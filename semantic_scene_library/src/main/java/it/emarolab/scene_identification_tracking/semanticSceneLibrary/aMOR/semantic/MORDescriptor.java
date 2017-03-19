package it.emarolab.scene_identification_tracking.semanticSceneLibrary.aMOR.semantic;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.Semantic;
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

                    if ( java.hasElement()) {
                        if ( owl.hasElement()) {
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

    class MORLiteralDescriptor
            implements Descriptor.Properting<OWLReferences,OWLNamedIndividual,MORSemantic.MORLiteral>{

        @Override
        public ReadOutcome<OWLNamedIndividual,MORAxiom.MORLiteralValue>
                read(OWLReferences ontology, OWLNamedIndividual instance, MORSemantic.MORLiteral semantic) {

            final MORAxiom.MORLiteralValue owl = semantic.query(ontology, instance);
            final MORAxiom.MORLiteralValue java = semantic.get();

            Mapping.Transitions<Mapping.Intent<OWLNamedIndividual,MORAxiom.MORLiteralValue,Mapping.ReadingState>>
                    transitions = new MORTryRead<OWLNamedIndividual, MORAxiom.MORLiteralValue>() {
                @Override
                public Mapping.Transitions giveAtry() {

                    Mapping.Intent<OWLNamedIndividual, MORAxiom.MORLiteralValue, Mapping.ReadingState>
                            intent = getNewIntent(instance, "", java, owl); // todo describe

                    if( owl.hasElement()) { // does not exist
                        if ( java.hasElement()){
                            intent.getState().asNotChanged();
                        } else {
                            java.setValue( null);
                            intent.getState().asAbsent();
                        }
                    } else {
                        if ( java.hasElement()){
                            java.set( owl);
                            intent.getState().asSuccess();
                        } else {
                            if ( owl.equals( java)){
                                intent.getState().asNotChanged();
                            } else{
                                java.set( owl);
                                intent.getState().asSuccess();
                            }
                        }
                    }

                    return getStateTransitions();
                }
            }.perform();

            return new ReadOutcome<>( instance, java, transitions);
        }

        @Override
        public WriteOutcome<OWLNamedIndividual,MORAxiom.MORLiteralValue>
                write(OWLReferences ontology, OWLNamedIndividual instance, MORSemantic.MORLiteral semantic) {

            final MORAxiom.MORLiteralValue owl = semantic.query(ontology, instance);
            final MORAxiom.MORLiteralValue java = semantic.get();

            Mapping.Transitions<Mapping.Intent<OWLNamedIndividual,MORAxiom.MORLiteralValue,Mapping.WritingState>>
                    transitions = new MORTryWrite<OWLNamedIndividual, MORAxiom.MORLiteralValue>() {
                @Override
                public Mapping.Transitions giveAtry() {

                    Mapping.Intent<OWLNamedIndividual, MORAxiom.MORLiteralValue, Mapping.WritingState>
                            intent = getNewIntent(instance, "", java, owl);

                    if( ! owl.hasElement()) { // does not exist
                        if ( java.hasElement()){
                            intent.getState().asNotChanged();
                        } else {
                            semantic.add( ontology, instance, java.getProperty(), java.getValue());
                            intent.getState().asAdded();
                        }
                    } else {
                        if ( ! java.hasElement()){
                            semantic.remove( ontology, instance, owl.getProperty(), owl.getValue());
                            intent.getState().asRemoved();
                        } else {
                            if ( owl.equals( java)){
                                intent.getState().asNotChanged();
                            } else{
                                // REPLACE
                                semantic.remove( ontology, instance, owl.getProperty(), owl.getValue());
                                semantic.add( ontology, instance, java.getProperty(), java.getValue());
                                intent.getState().asUpdated();
                            }
                        }
                    }
                    return getStateTransitions();
                }
            }.perform();

            return new WriteOutcome<>( instance, java, transitions);
        }
    }

    class MORData3Descriptor
            implements Descriptor.Properting3D<OWLReferences,OWLNamedIndividual,MORSemantic.MORData3D>{

        private final int AXIS_TAG_X = 1;
        private final int AXIS_TAG_Y = 2;
        private final int AXIS_TAG_Z = 3;

        private <P,V> void read(OWLReferences ontology, OWLNamedIndividual instance,
                     Semantic.Axiom.Connector3D<P, V> owl, Semantic.Axiom.Connector3D<P, V> java,
                     Mapping.Intent<?,?,Mapping.ReadingState> intent, int axisTag) {
            if( owl.hasElement()) { // does not exist
                if ( java.hasElement()){
                    intent.getState().asNotChanged();
                } else {
                    readSpecific( axisTag, java, null);
                    intent.getState().asAbsent();
                }
            } else {
                if ( java.hasElement()){
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

                    read( ontology, instance, owl, java, intent, AXIS_TAG_X);

                    return getStateTransitions();
                }
            }.perform();

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

                    read( ontology, instance, owl, java, intent, AXIS_TAG_Y);

                    return getStateTransitions();
                }
            }.perform();

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

                    read( ontology, instance, owl, java, intent, AXIS_TAG_Z);

                    return getStateTransitions();
                }
            }.perform();

            return new ReadOutcome<>( instance, java, transitions);
        }

        private <P,V> void write(OWLReferences ontology, OWLNamedIndividual instance,
                                 MORSemantic.MORData3D semantic,
                                 Semantic.Axiom.Connector3D<P, V> owl, Semantic.Axiom.Connector3D<P, V> java,
                                 Mapping.Intent<?,?,Mapping.WritingState> intent, int axisTag) {
            if( ! owl.hasElement()) { // does not exist
                if ( java.hasElement()){
                    intent.getState().asNotChanged();
                } else {
                    addSpecific( axisTag, semantic, ontology, instance, java);
                    intent.getState().asAdded();
                }
            } else {
                if ( ! java.hasElement()){
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
            }.perform();

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
            }.perform();

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

                    Mapping.Intent<OWLNamedIndividual, MORAxiom.MORMultiLinked, Mapping.ReadingState>
                            intent = getNewIntent(instance, LOGGING.INTENT_BELONG, java, owl);

                    if ( ! owl.hasElement()){
                        for ( MORAxiom.MORLinked v : java){

                            Mapping.Intent<OWLNamedIndividual, MORAxiom.MORMultiLinked, Mapping.ReadingState>
                                    otherIntent = getNewIntent(instance, LOGGING.INTENT_BELONG,
                                             new MORAxiom.MORMultiLinked( v), null);
                            java.remove( v);
                            otherIntent.getState().asAbsent()
                        }
                    } else {
                        for ( MORAxiom.MORLinked o : owl) {
                            MORAxiom.MORLinked j = java.get(o.getProperty());

                            Mapping.Intent<OWLNamedIndividual, MORAxiom.MORMultiLinked, Mapping.ReadingState>
                                    otherIntent = getNewIntent(instance, "",
                                    new MORAxiom.MORMultiLinked( j), new MORAxiom.MORMultiLinked( o)); // todo describe

                            if( o.hasElement()) { // does not exist
                                if ( j.hasElement()){
                                    otherIntent.getState().asNotChanged();
                                } else {
                                    j.setValue( null);
                                    otherIntent.getState().asAbsent();
                                }
                            } else {
                                if ( j.hasElement()){
                                    j.set( o);
                                    otherIntent.getState().asSuccess();
                                } else {
                                    if ( o.equals( j)){
                                        otherIntent.getState().asNotChanged();
                                    } else{
                                        j.set( o);
                                        otherIntent.getState().asSuccess();
                                    }
                                }
                            }
                        }
                    }

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
