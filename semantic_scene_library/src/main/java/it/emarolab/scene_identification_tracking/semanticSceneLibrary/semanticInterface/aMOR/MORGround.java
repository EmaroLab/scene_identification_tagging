package it.emarolab.scene_identification_tracking.semanticSceneLibrary.semanticInterface.aMOR;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.semanticInterface.core.Semantic;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObject;

/**
 * Created by bubx on 25/03/17.
 */
public interface MORGround<J extends OWLObject> extends Semantic.Ground<OWLReferences, J> {

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

        abstract void setInstance( String instanceName);

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

    // todo ground: literal, link

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
        void setInstance(String instanceName) {
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
        void setInstance(String instanceName) {
            setInstance( getOntology().getOWLClass( instanceName));
        }
    }
}
