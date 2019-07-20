package it.emarolab.sit.owloopDescriptor;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.amor.owlInterface.SemanticRestriction;
import it.emarolab.owloop.descriptor.construction.descriptorEntitySet.DescriptorEntitySet;
import it.emarolab.owloop.descriptor.construction.descriptorExpression.ConceptExpression;
import it.emarolab.owloop.descriptor.construction.descriptorGround.ConceptGround;
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
 * <b>File</b>:        it.emarolab.sit.owloopDescriptor.SceneClassDescriptor <br>
 * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
 * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
 * <b>affiliation</b>: EMAROLab, DIBRIS, University of Genoa. <br>
 * <b>date</b>:        05/06/17 <br>
 * </small></div>
 */
public class SceneClassDescriptor
        extends ConceptGround
        implements ConceptExpression.Restriction,
        ConceptExpression.Sub<SceneClassDescriptor>,
        ConceptExpression.Super<SceneClassDescriptor>,
        ConceptExpression.Instance<SceneIndividualDescriptor> {

    private DescriptorEntitySet.Restrictions restrictions = new DescriptorEntitySet.Restrictions();
    private DescriptorEntitySet.Concepts subConcept = new DescriptorEntitySet.Concepts();
    private DescriptorEntitySet.Concepts superConcept = new DescriptorEntitySet.Concepts();
    private DescriptorEntitySet.Individuals classifiedIndividual = new DescriptorEntitySet.Individuals();

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
    public List<MappingIntent> readExpressionAxioms() {
        List<MappingIntent> r = Sub.super.readExpressionAxioms();
        r.addAll( Restriction.super.readExpressionAxioms());
        r.addAll( Super.super.readExpressionAxioms());
        r.addAll( Instance.super.readExpressionAxioms());
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
    public List<MappingIntent> writeExpressionAxioms() {
        List<MappingIntent> r = Restriction.super.writeExpressionAxioms();
        r.addAll( Super.super.writeExpressionAxioms());
        r.addAll( Instance.super.writeExpressionAxioms());
        r.addAll( Sub.super.writeExpressionAxioms());
        updateCardinality();
        return r;
    }

    private void updateCardinality(){
        cardinality = 0;
        for( SemanticRestriction r : getRestrictionConcepts()){
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
    public DescriptorEntitySet.Restrictions getRestrictionConcepts() {
        return restrictions;
    }

    /**
     * This is a standard OWLOOP implementation.
     * @return a new {@code Descriptor} for a scene class.
     */
    @Override
    public SceneClassDescriptor getSubConceptDescriptor(OWLClass instance, OWLReferences ontology) {
        return new SceneClassDescriptor( instance, ontology);
    }


    /**
     * This is a standard OWLOOP implementation.
     * @return the name of all the sub class of {@code this}.
     */
    @Override
    public DescriptorEntitySet.Concepts getSubConcepts() {
        return subConcept;
    }

    /**
     * This is a standard OWLOOP implementation.
     * @return a new {@code Descriptor} for a scene class.
     */
    @Override
    public SceneClassDescriptor getSuperConceptDescriptor(OWLClass instance, OWLReferences ontology) {
        return new SceneClassDescriptor( instance, ontology);
    }

    /**
     * This is a standard OWLOOP implementation.
     * @return the name of all the super class of {@code this}.
     */
    @Override
    public DescriptorEntitySet.Concepts getSuperConcepts() {
        return superConcept;
    }

    /**
     * This is a standard OWLOOP implementation.
     * @return a new {@code Descriptor} for an scene individual.
     */
    @Override
    public SceneIndividualDescriptor getIndividualDescriptor(OWLNamedIndividual instance, OWLReferences ontology) {
        return new SceneIndividualDescriptor( instance, ontology);
    }

    /**
     * This is a standard OWLOOP implementation.
     * @return the name of all the scene individuals classified in {@code this}.
     */
    @Override
    public DescriptorEntitySet.Individuals getIndividualInstances() {
        return classifiedIndividual;
    }

    /**
     * The sum of all the cardinality restriction available in the
     * definition of {@code this} abstract scene class.
     * @return the scene cardinality based on {@link #readExpressionAxioms()} ()} and {@link #writeExpressionAxioms()} ()}.
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
