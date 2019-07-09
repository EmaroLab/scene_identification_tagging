package it.emarolab.scene_identification_tagging.owloopDescriptor;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.amor.owlInterface.SemanticRestriction;
import it.emarolab.owloop.aMORDescriptor.MORAxioms;
import it.emarolab.owloop.aMORDescriptor.MORConcept;
import it.emarolab.owloop.aMORDescriptor.utility.MORConceptBase;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import java.util.List;

/**
 * The  <a href="https://github.com/EmaroLab/owloop">OWLOOP</a> {@code Descriptor} for a class representing an abstract Scene.
 * <p>
 *     This is an OWLClass {@code Descriptor} based on the
 *     <a href="https://github.com/EmaroLab/owloop">OWLOOP</a> API.
 *     It is in charge to synchronise the individuals classified in {@code this}
 *     class, as well as sub and super classes. It also implements
 *     method for ontological class definition and based on this, it
 *     computes the scene class cardinality.
 *
 * <div style="text-align:center;"><small>
 * <b>File</b>:        it.emarolab.scene_identification_tagging.owloopDescriptor.SceneClassDescriptor <br>
 * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
 * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
 * <b>affiliation</b>: EMAROLab, DIBRIS, University of Genoa. <br>
 * <b>date</b>:        05/06/17 <br>
 * </small></div>
 */
public class SceneClassDescriptor
        extends MORConceptBase
        implements MORConcept.Define,
        MORConcept.Sub<SceneClassDescriptor>,
        MORConcept.Super<SceneClassDescriptor>,
        MORConcept.Classify<SceneIndividualDescriptor> {

    private MORAxioms.Restrictions restrictions = new MORAxioms.Restrictions();
    private MORAxioms.Concepts subConcept = new MORAxioms.Concepts();
    private MORAxioms.Concepts superConcept = new MORAxioms.Concepts();
    private MORAxioms.Individuals classifiedIndividual = new MORAxioms.Individuals();

    private int cardinality = 0;

    // constructors for MORConceptBase

    /**
     * Initialise this OWLOOP {@code Descriptor} by fully specifying the {@code Ground}.
     * @param instance the OWLClass managed by this {@code Descriptor}.
     * @param ontoName the {@link OWLReferences} name of the ontology hosting the described class.
     */
    public SceneClassDescriptor(OWLClass instance, String ontoName) {
        super(instance, ontoName);
    }
    /**
     * Initialise this OWLOOP {@code Descriptor} by fully specifying the {@code Ground}.
     * @param instanceName the name of OWLClass managed by this {@code Descriptor}.
     * @param ontoName the {@link OWLReferences} name of the ontology hosting the described class.
     */
    public SceneClassDescriptor(String instanceName, String ontoName) {
        super(instanceName, ontoName);
    }
    /**
     * Initialise this OWLOOP {@code Descriptor} by fully specifying the {@code Ground}.
     * @param instance the OWLClass managed by this {@code Descriptor}.
     * @param ontoName the {@link OWLReferences} to the ontology hosting the described class.
     */
    public SceneClassDescriptor(OWLClass instance, OWLReferences ontoName) {
        super(instance, ontoName);
    }
    /**
     * Initialise this OWLOOP {@code Descriptor} by fully specifying the {@code Ground}.
     * @param instanceName the name of OWLClass managed by this {@code Descriptor}.
     * @param ontoName the {@link OWLReferences} to the ontology hosting the described class.
     */
    public SceneClassDescriptor(String instanceName, OWLReferences ontoName) {
        super(instanceName, ontoName);
    }

    /**
     * This method uses standard OWLOOP synchronisation mechanics
     * to fill this {@code Descriptor} by taking information from the ontology.
     * It also update the {@link #getCardinality()} value based on the retrieved information.
     * @return the changes done during reading.
     */
    @Override
    public List<MappingIntent> readSemantic() {
        List<MappingIntent> r = Sub.super.readSemantic();
        r.addAll( Define.super.readSemantic());
        r.addAll( Super.super.readSemantic());
        r.addAll( Classify.super.readSemantic());
        updateCardinality();
        return r;
    }

    /**
     * This method uses standard OWLOOP synchronisation mechanics
     * to fill the ontology with this {@code Descriptor} contents.
     * It also update the {@link #getCardinality()} value based on the described information.
     * @return the changes done during writing.
     */
    @Override
    public List<MappingIntent> writeSemantic() {
        List<MappingIntent> r = Define.super.writeSemantic();
        r.addAll( Super.super.writeSemantic());
        r.addAll( Classify.super.writeSemantic());
        r.addAll( Sub.super.writeSemantic());
        updateCardinality();
        return r;
    }

    private void updateCardinality(){
        cardinality = 0;
        for( SemanticRestriction r : getDefinitionConcept()){
            if( r instanceof SemanticRestriction.ClassRestrictedOnMinObject){
                cardinality += ((SemanticRestriction.ClassRestrictedOnMinObject) r).getCardinality();
            }
        }
    }


    /**
     * This is a standard OWLOOP implementation.
     * @return the definition of the described OWLClass.
     */
    @Override
    public MORAxioms.Restrictions getDefinitionConcept() {
        return restrictions;
    }

    /**
     * This is a standard OWLOOP implementation.
     * @return a new {@code Descriptor} for a scene class.
     */
    @Override
    public SceneClassDescriptor getNewSubConcept(OWLClass instance, OWLReferences ontology) {
        return new SceneClassDescriptor( instance, ontology);
    }

    /**
     * This is a standard OWLOOP implementation.
     * @return the name of all the sub class of {@code this}.
     */
    @Override
    public MORAxioms.Concepts getSubConcept() {
        return subConcept;
    }

    /**
     * This is a standard OWLOOP implementation.
     * @return a new {@code Descriptor} for a scene class.
     */
    @Override
    public SceneClassDescriptor getNewSuperConcept(OWLClass instance, OWLReferences ontology) {
        return new SceneClassDescriptor( instance, ontology);
    }

    /**
     * This is a standard OWLOOP implementation.
     * @return the name of all the super class of {@code this}.
     */
    @Override
    public MORAxioms.Concepts getSuperConcept() {
        return superConcept;
    }

    /**
     * This is a standard OWLOOP implementation.
     * @return a new {@code Descriptor} for an scene individual.
     */
    @Override
    public SceneIndividualDescriptor getNewIndividualClassified(OWLNamedIndividual instance, OWLReferences ontology) {
        return new SceneIndividualDescriptor( instance, ontology);
    }

    /**
     * This is a standard OWLOOP implementation.
     * @return the name of all the scene individuals classified in {@code this}.
     */
    @Override
    public MORAxioms.Individuals getIndividualClassified() {
        return classifiedIndividual;
    }

    /**
     * The sum of all the cardinality restriction available in the
     * definition of {@code this} abstract scene class.
     * @return the scene cardinality based on {@link #readSemantic()} and {@link #writeSemantic()}.
     */
    public int getCardinality() {
        return cardinality;
    }


    @Override
    public String toString() {
        return "SceneClassDescriptor{" +
                NL + "\t\t\t" + getGround() + " cardinality:" + cardinality +
                "," + NL + "\t⇐ " + classifiedIndividual +
                "," + NL + "\t= " + restrictions +
                "," + NL + "\t⊃ " + subConcept +
                "," + NL + "\t⊂ " + superConcept +
                NL + "}";
    }
}
