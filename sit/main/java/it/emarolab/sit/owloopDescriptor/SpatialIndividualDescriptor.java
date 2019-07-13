package it.emarolab.sit.owloopDescriptor;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.owloop.descriptor.construction.descriptorEntitySet.DescriptorEntitySet;
import it.emarolab.owloop.descriptor.construction.descriptorExpression.IndividualExpression;
import it.emarolab.owloop.descriptor.construction.descriptorGround.IndividualGround;
import it.emarolab.owloop.descriptor.utility.dataPropertyDescriptor.HierarchicalDataPropertyDesc;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import java.util.List;

/**
 * The  <a href="https://github.com/EmaroLab/owloop">OWLOOP</a> {@code Descriptor} for an individual representing a concrete Object.
 * <p>
 *     This is an OWLNamedIndividual {@code Descriptor} based on the
 *     <a href="https://github.com/EmaroLab/owloop">OWLOOP</a> API.
 *     It is in charge to synchronise the types of {@code this} individual,
 *     its data and object properties as well as disjoint individuals.
 *
 * <div style="text-align:center;"><small>
 * <b>File</b>:        it.emarolab.sit.owloopDescriptor.SpatialIndividualDescriptor <br>
 * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
 * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
 * <b>affiliation</b>: EMAROLab, DIBRIS, University of Genoa. <br>
 * <b>date</b>:        05/06/17 <br>
 * </small></div>
 */
public class SpatialIndividualDescriptor
        extends IndividualGround
        implements IndividualExpression.Disjoint<SpatialIndividualDescriptor>,
        IndividualExpression.Type<SpatialClassDescriptor>,
        IndividualExpression.DataLink<HierarchicalDataPropertyDesc>, // this buildings are never read or write
        IndividualExpression.ObjectLink<SpatialObjectPropertyDescriptor>{

    private DescriptorEntitySet.Individuals disjointIndividual = new DescriptorEntitySet.Individuals();
    private DescriptorEntitySet.Concepts individualTypes = new DescriptorEntitySet.Concepts();
    private DescriptorEntitySet.ObjectLinksSet objectLinks = new DescriptorEntitySet.ObjectLinksSet();
    private DescriptorEntitySet.DataLinksSet dataLinks = new DescriptorEntitySet.DataLinksSet();

    /**
     * Initialise this OWLOOP {@code Descriptor} by fully specifying the {@code Ground}.
     * @param instance the OWL individual managed by this {@code Descriptor}.
     * @param onto the {@link OWLReferences} to an ontology hosting the described class.
     */
    public SpatialIndividualDescriptor(OWLNamedIndividual instance, OWLReferences onto) {
        super(instance, onto);
    }
    /**
     * Initialise this OWLOOP {@code Descriptor} by fully specifying the {@code Ground}.
     * @param instanceName the name of the OWL individual managed by this {@code Descriptor}.
     * @param onto the {@link OWLReferences} to an ontology hosting the described class.
     */
    public SpatialIndividualDescriptor(String instanceName, OWLReferences onto) {
        super(instanceName, onto);
    }
    /**
     * Initialise this OWLOOP {@code Descriptor} by fully specifying the {@code Ground}.
     * @param instance the OWL individual managed by this {@code Descriptor}.
     * @param ontoName the {@link OWLReferences} name of the ontology hosting the described class.
     */
    public SpatialIndividualDescriptor(OWLNamedIndividual instance, String ontoName) {
        super(instance, ontoName);
    }
    /**
     * Initialise this OWLOOP {@code Descriptor} by fully specifying the {@code Ground}.
     * @param instanceName the name of the OWL individual managed by this {@code Descriptor}.
     * @param ontoName the {@link OWLReferences} name of the ontology hosting the described class.
     */
    public SpatialIndividualDescriptor(String instanceName, String ontoName) {
        super(instanceName, ontoName);
    }

    /**
     * This method uses standard OWLOOP synchronisation mechanics
     * to fill this {@code Descriptor} by taking information from the ontology.
     * @return the changes done during reading.
     */
    @Override
    public List<MappingIntent> readExpressionAxioms() {
        List<MappingIntent> r = Disjoint.super.readExpressionAxioms();
        r.addAll( Type.super.readExpressionAxioms());
        r.addAll( ObjectLink.super.readExpressionAxioms());
        r.addAll( DataLink.super.readExpressionAxioms());
        return r;
    }

    /**
     * This method uses standard OWLOOP synchronisation mechanics
     * to fill the ontology with this {@code Descriptor} contents.
     * @return the changes done during writing.
     */
    @Override
    public List<MappingIntent> writeExpressionAxioms() {
        List<MappingIntent> r = Disjoint.super.writeExpressionAxioms();
        r.addAll( Type.super.writeExpressionAxioms());
        r.addAll( ObjectLink.super.writeExpressionAxioms());
        r.addAll( DataLink.super.writeExpressionAxioms());
        return r;
    }

    /**
     * This is a standard OWLOOP implementation.
     * @return a new {@code Descriptor} for an individual disjoint to {@code this}.
     */
    @Override
    public SpatialIndividualDescriptor getNewDisjointIndividual(OWLNamedIndividual individual, OWLReferences o) {
        return new SpatialIndividualDescriptor( individual, o);
    }


    /**
     * This is a standard OWLOOP implementation.
     * @return a data property of {@code this} individual.
     */
    @Override
    public HierarchicalDataPropertyDesc getNewIndividualDataProperty(DescriptorEntitySet.DataLinks instance, OWLReferences ontology) {
        return new HierarchicalDataPropertyDesc( instance.getExpression(), ontology);
    }


    @Override
    public String toString() {
        return "SpatialIndividualDescriptor{" +
                NL + "\t\t\t" + getGround() +
                ":" + NL + "\t≠ " + disjointIndividual +
                "," + NL + "\t∈ " + individualTypes +
                "," + NL + "\t⊨ " + objectLinks +
                "," + NL + "\t⊢ " + dataLinks +
                NL + "}";
    }

    /**
     * This is a standard OWLOOP implementation.
     * @return the name of all disjoint individuals to {@code this}.
     */
    @Override
    public DescriptorEntitySet.Individuals getDisjointIndividuals() {
        return disjointIndividual;
    }


    /**
     * This is a standard OWLOOP implementation.
     * @return the names and values of all the synchronised data properties of {@code this} individual.
     */
    @Override
    public DescriptorEntitySet.DataLinksSet getIndividualDataProperties() {
        return dataLinks;
    }

    /**
     * This is a standard OWLOOP implementation.
     * @return an object property of {@code this} individual.
     */
    @Override
    public SpatialObjectPropertyDescriptor getNewIndividualObjectProperty(DescriptorEntitySet.ObjectLinks instance, OWLReferences ontology) {
        return new SpatialObjectPropertyDescriptor( instance.getExpression(), ontology);
    }

    /**
     * This is a standard OWLOOP implementation.
     * @return the names and values of all the synchronised object properties of {@code this} individual.
     */
    @Override
    public DescriptorEntitySet.ObjectLinksSet getIndividualObjectProperties() {
        return objectLinks;
    }

    /**
     * This is a standard OWLOOP implementation.
     * @return a new {@code Descriptor} for a class classifying {@code this} individual.
     */
    @Override
    public SpatialClassDescriptor getNewIndividualType(OWLClass instance, OWLReferences ontology) {
        return new SpatialClassDescriptor( instance, ontology);
    }

    /**
     * This is a standard OWLOOP implementation.
     * @return the name of all the class in which {@code this} individual is belonging to.
     */
    @Override
    public DescriptorEntitySet.Concepts getIndividualTypes() {
        return individualTypes;
    }

}
