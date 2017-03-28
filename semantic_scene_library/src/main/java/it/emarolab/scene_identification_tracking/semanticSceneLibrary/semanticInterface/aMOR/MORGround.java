package it.emarolab.scene_identification_tracking.semanticSceneLibrary.semanticInterface.aMOR;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.semanticInterface.core.Semantic;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.semanticInterface.core.synchronise.Mapping;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.reasoner.OWLReasonerRuntimeException;

/**
 * Created by bubx on 25/03/17.
 */
public interface MORGround<J extends OWLObject>
        extends Semantic.Ground<OWLReferences, J> {

    static  <I extends Semantic.Ground<?, ?>>
    MORGround.GroundIndividual groundIndividual(I instance){
        if ( instance instanceof MORGround.GroundIndividual)
            return  (MORGround.GroundIndividual) instance;
        return null; // todo log
    }

    static <I extends Semantic.Ground<?, ?>>
    MORGround.GroundClass groundClass( I instance){
        if ( instance instanceof MORGround.GroundClass)
            return  (MORGround.GroundClass) instance;
        return null; // todo log
    }




    interface MORTrier< I extends GroundBase<?>, X extends MORAxiom<I,?,A>, A extends MORAtom<?>, M extends Mapping.State>
            extends Mapping.TrierInterface<I,X,A,M>{

        default Mapping.Transitions onJavaError(Exception e){
            logError( e);
            getStateTransitions().get( getStateTransitions().size() - 1).getState().asError();
            return getStateTransitions();
        }
        default Mapping.Transitions onOWLError(Exception e){
            logError( e);
            getStateTransitions().get( getStateTransitions().size() - 1).getState().asError();
            return getStateTransitions();
        }
        default Mapping.Transitions onInconsistency(Exception e){
            logError( e);
            getStateTransitions().get( getStateTransitions().size() - 1).getState().asInconsistent();
            return getStateTransitions();
        }

        @Override
        default Mapping.Transitions onError(Exception e){
            if ( e instanceof OWLReasonerRuntimeException)
                return onInconsistency( e);
            if ( e instanceof openllet.owlapi.OWLException)
                return onOWLError( e);
            if ( e instanceof org.semanticweb.owlapi.model.OWLException)
                return onOWLError( e);
            return onJavaError( e);
        }
    }

    // todo ground: literal, link

    abstract class GroundBase<J extends OWLObject>
            extends SIBase
            implements MORGround<J> {

        private OWLReferences ontology;
        private J instance;

        public GroundBase() {
        }
        public GroundBase(OWLReferences ontology) {
            this.setOntology( ontology);
        }
        public GroundBase(OWLReferences ontology, J instance) {
            this.setOntology( ontology);
            this.setInstance( instance);
        }
        public GroundBase(OWLReferences ontology, String instanceName) {
            this.setOntology( ontology, instanceName);
        }
        public GroundBase( GroundBase<J> copy){
            this.ontology = copy.ontology;
            this.instance = copy.instance;
        }

        public void setOntology( OWLReferences ontology, String instanceName){
            setOntology( ontology);
            setInstance( instanceName);
        }

        void setInstance( J instance){
            this.instance = instance;
        }

        @Override
        public OWLReferences getOntology() {
            return ontology;
        }

        public void setOntology(OWLReferences ontology){
            this.ontology = ontology;
            this.instance = null;
        }

        @Override
        public J getInstance() {
            return instance;
        }

        abstract public void setInstance( String instanceName);

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof GroundBase)) return false;

            GroundBase<?> that = (GroundBase<?>) o;

            if (getOntology() != null ? !getOntology().getReferenceName().equals(that.getOntology().getReferenceName()) : that.getOntology() != null)
                return false;
            return getInstance() != null ? getInstance().equals(that.getInstance()) : that.getInstance() == null;
        }

        @Override
        public int hashCode() {
            int result = getOntology() != null ? getOntology().hashCode() : 0;
            result = 31 * result + (getInstance() != null ? getInstance().hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return ontology.getReferenceName() + "@" + ontology.getOWLObjectName( getInstance());
        }
    }

    class GroundIndividual
            extends GroundBase<OWLNamedIndividual>{

        public GroundIndividual() {
        }
        public GroundIndividual(OWLReferences ontology) {
            super(ontology);
        }
        public GroundIndividual(OWLReferences ontology, OWLNamedIndividual instance) {
            super(ontology, instance);
        }
        public GroundIndividual(OWLReferences ontology, String instanceName) {
            super(ontology, instanceName);
        }
        public GroundIndividual(GroundBase<OWLNamedIndividual> copy) {
            super(copy);
        }

        @Override
        public GroundIndividual copy() {
            return new GroundIndividual(this);
        }

        @Override
        public void setInstance(String instanceName) {
            setInstance( getOntology().getOWLIndividual( instanceName));
        }
    }

    class GroundClass
            extends GroundBase<OWLClass> {

        public GroundClass() {
        }
        public GroundClass(OWLReferences ontology) {
            super(ontology);
        }
        public GroundClass(OWLReferences ontology, OWLClass instance) {
            super(ontology, instance);
        }
        public GroundClass(OWLReferences ontology, String instanceName) {
            super(ontology, instanceName);
        }
        public GroundClass(GroundBase<OWLClass> copy) {
            super(copy);
        }

        @Override
        public GroundClass copy() {
            return new GroundClass( this);
        }

        @Override
        public void setInstance(String instanceName) {
            setInstance( getOntology().getOWLClass( instanceName));
        }
    }

    abstract class MORTrierRead<I extends GroundBase<?>,X extends MORAxiom<I,?,A>, A extends MORAtom<?>>
            extends Mapping.Trier<I,X,A,Mapping.ReadingState>
            implements MORTrier<I,X,A,Mapping.ReadingState>{

        public MORTrierRead() {
        }
        public MORTrierRead(Intent intent) {
            super(intent);
        }

        @Override
        public Mapping.ReadingState getNewState() {
            return new Mapping.ReadingState();
        }
    }

    abstract class MORClassTryRead< X extends MORAxiom<GroundClass,?,A>, A extends MORAtom<?>>
            extends MORTrierRead<GroundClass,X,A>{

        public MORClassTryRead() {
        }
        public MORClassTryRead(Intent intent) {
            super(intent);
        }
    }

    abstract class MORIndividualTryRead<X extends MORAxiom<GroundIndividual,?,A>, A extends MORAtom<?>>
            extends MORTrierRead<GroundIndividual,X,A>{

        public MORIndividualTryRead() {
        }
        public MORIndividualTryRead(Intent intent) {
            super(intent);
        }
    }



    abstract class MORTrierWrite<   I extends GroundBase<?>,X extends MORAxiom<I,?,A>, A extends MORAtom<?>>
            extends Mapping.Trier<I,X,A,Mapping.WritingState>
            implements MORTrier<I,X,A,Mapping.WritingState>{

        public MORTrierWrite() {
        }
        public MORTrierWrite(Intent intent) {
            super(intent);
        }

        @Override
        public Mapping.WritingState getNewState() {
            return new Mapping.WritingState();
        }
    }

    abstract class MORClassTryWrite<X extends MORAxiom<GroundClass,?,A>, A extends MORAtom<?>>
            extends MORTrierWrite<GroundClass,X,A>{


        public MORClassTryWrite() {
        }
        public MORClassTryWrite(Intent intent) {
            super(intent);
        }
    }

    abstract class MORIndividualTryWrite< X extends MORAxiom<GroundIndividual,?,A>, A extends MORAtom<?>>
            extends MORTrierWrite<GroundIndividual,X,A>{

        public MORIndividualTryWrite() {
        }
        public MORIndividualTryWrite(Intent intent) {
            super(intent);
        }
    }

}
