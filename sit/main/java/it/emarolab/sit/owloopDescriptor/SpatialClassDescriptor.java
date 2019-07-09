package it.emarolab.sit.owloopDescriptor;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.owloop.descriptor.construction.descriptorEntitySet.DescriptorEntitySet;
import it.emarolab.owloop.descriptor.construction.descriptorExpression.ConceptExpression;
import it.emarolab.owloop.descriptor.construction.descriptorGround.ConceptGround;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import java.util.List;

/**
 * The  <a href="https://github.com/EmaroLab/owloop">OWLOOP</a> {@code Descriptor} for a class representing a abstract Object.
 * <p>
 *     This is an OWLClass {@code Descriptor} based on the
 *     <a href="https://github.com/EmaroLab/owloop">OWLOOP</a> API.
 *     It is in charge to synchronise the individuals classified in {@code this}
 *     class, as well as sub classes.
 *
 * <div style="text-align:center;"><small>
 * <b>File</b>:        it.emarolab.sit.owloopDescriptor.SpatialClassDescriptor <br>
 * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
 * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
 * <b>affiliation</b>: EMAROLab, DIBRIS, University of Genoa. <br>
 * <b>date</b>:        05/06/17 <br>
 * </small></div>
 */
public class SpatialClassDescriptor
        extends ConceptGround
        implements ConceptExpression.Sub<SpatialClassDescriptor>,
        ConceptExpression.Instance<SpatialIndividualDescriptor> {

    private DescriptorEntitySet.Concepts subConcept = new DescriptorEntitySet.Concepts();
    private DescriptorEntitySet.Individuals classifiedIndividual = new DescriptorEntitySet.Individuals();

    /**
     * Initialise this OWLOOP {@code Descriptor} by fully specifying the {@code Ground}.
     * @param instance the OWLClass managed by this {@code Descriptor}.
     * @param onto the {@link OWLReferences} to an ontology hosting the described class.
     */
    public SpatialClassDescriptor(OWLClass instance, OWLReferences onto) {
        super(instance, onto);
    }
    /**
     * Initialise this OWLOOP {@code Descriptor} by fully specifying the {@code Ground}.
     * @param instanceName the name of the OWLClass managed by this {@code Descriptor}.
     * @param onto the {@link OWLReferences} to an ontology hosting the described class.
     */
    public SpatialClassDescriptor(String instanceName, OWLReferences onto) {
        super(instanceName, onto);
    }
    /**
     * Initialise this OWLOOP {@code Descriptor} by fully specifying the {@code Ground}.
     * @param instance the OWLClass managed by this {@code Descriptor}.
     * @param ontoName the {@link OWLReferences} name of the ontology hosting the described class.
     */
    public SpatialClassDescriptor(OWLClass instance, String ontoName) {
        super(instance, ontoName);
    }
    /**
     * Initialise this OWLOOP {@code Descriptor} by fully specifying the {@code Ground}.
     * @param instanceName the name of the OWLClass managed by this {@code Descriptor}.
     * @param ontoName the {@link OWLReferences} name of the ontology hosting the described class.
     */
    public SpatialClassDescriptor(String instanceName, String ontoName) {
        super(instanceName, ontoName);
    }

    /**
     * This method uses standard OWLOOP synchronisation mechanics
     * to fill this {@code Descriptor} by taking information from the ontology.
     * @return the changes done during reading.
     */
    @Override
    public List<MappingIntent> readExpressionAxioms() {
        List<MappingIntent> r = ConceptExpression.Sub.super.readExpressionAxioms();
        r.addAll( ConceptExpression.Instance.super.readExpressionAxioms());
        return r;
    }

    /**
     * This method uses standard OWLOOP synchronisation mechanics
     * to fill the ontology with this {@code Descriptor} contents.
     * @return the changes done during writing.
     */
    @Override
    public List<MappingIntent> writeExpressionAxioms() {
        List<MappingIntent> r = ConceptExpression.Sub.super.writeExpressionAxioms();
        r.addAll( ConceptExpression.Instance.super.writeExpressionAxioms());
        return r;
    }

    /**
     * This is a standard OWLOOP implementation.
     * @return a new {@code Descriptor} for an object individual.
     */
    @Override
    public SpatialIndividualDescriptor getIndividualDescriptor(OWLNamedIndividual instance, OWLReferences ontology) {
        return new SpatialIndividualDescriptor( instance, ontology);
    }

    /**
     * This is a standard OWLOOP implementation.
     * @return the name of all the object individuals classified in {@code this}.
     */
    @Override
    public DescriptorEntitySet.Individuals getIndividualInstances() {
        return classifiedIndividual;
    }

    /**
     * This is a standard OWLOOP implementation.
     * @return a new {@code Descriptor} for a scene class.
     */
    @Override
    public SpatialClassDescriptor getSubConceptDescriptor(OWLClass instance, OWLReferences ontology) {
        return new SpatialClassDescriptor( instance, ontology);
    }

    /**
     * This is a standard OWLOOP implementation.
     * @return the name of all the sub class of {@code this}.
     */
    @Override
    public DescriptorEntitySet.Concepts getSubConcepts() {
        return subConcept;
    }

    @Override
    public String toString() {
        return "MORFullObjectProperty{" +
                NL + "\t\t\t" + getGround() +
                "," + NL + "\t⇐ " + classifiedIndividual +
                "," + NL + "\t⊃ " + subConcept +
                NL + "}";
    }
}