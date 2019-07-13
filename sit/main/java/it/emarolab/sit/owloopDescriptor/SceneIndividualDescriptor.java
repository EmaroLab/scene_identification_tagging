package it.emarolab.sit.owloopDescriptor;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.owloop.descriptor.construction.descriptorEntitySet.DescriptorEntitySet;
import it.emarolab.owloop.descriptor.construction.descriptorExpression.IndividualExpression;
import it.emarolab.owloop.descriptor.construction.descriptorGround.IndividualGround;
import it.emarolab.owloop.descriptor.utility.dataPropertyDescriptor.HierarchicalDataPropertyDesc;
import it.emarolab.owloop.descriptor.utility.objectPropertyDescriptor.HierarchicalObjectPropertyDesc;
import it.emarolab.sit.SITBase;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import java.util.List;

/**
 * The  <a href="https://github.com/EmaroLab/owloop">OWLOOP</a> {@code Descriptor} for an individual representing a concrete scene.
 * <p>
 *     This is an OWLNamedIndividual {@code Descriptor} based on the
 *     <a href="https://github.com/EmaroLab/owloop">OWLOOP</a> API.
 *     It is in charge to synchronise the types of {@code this} individual,
 *     as well as its data and object properties. It also defines
 *     the scene cardinality associate to a concrete scene representation.
 *
 * <div style="text-align:center;"><small>
 * <b>File</b>:        it.emarolab.sit.owloopDescriptor.SceneIndividualDescriptor <br>
 * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
 * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
 * <b>affiliation</b>: EMAROLab, DIBRIS, University of Genoa. <br>
 * <b>date</b>:        05/06/17 <br>
 * </small></div>
 */
public class SceneIndividualDescriptor
        extends IndividualGround
        implements IndividualExpression.Type<SceneClassDescriptor>,
        IndividualExpression.ObjectLink<HierarchicalObjectPropertyDesc>,
        IndividualExpression.DataLink<HierarchicalDataPropertyDesc>,
        SITBase{

    private int cardinality = 0;

    private DescriptorEntitySet.Concepts individualTypes = new DescriptorEntitySet.Concepts();
    private DescriptorEntitySet.ObjectLinksSet objectLinks = new DescriptorEntitySet.ObjectLinksSet();
    private DescriptorEntitySet.DataLinksSet dataLinks = new DescriptorEntitySet.DataLinksSet();


    /**
     * Initialise this OWLOOP {@code Descriptor} by fully specifying the {@code Ground}.
     * @param instance the OWL individual managed by this {@code Descriptor}.
     * @param onto the {@link OWLReferences} to an ontology hosting the described class.
     */
    public SceneIndividualDescriptor(OWLNamedIndividual instance, OWLReferences onto) {
        super(instance, onto);
    }
    /**
     * Initialise this OWLOOP {@code Descriptor} by fully specifying the {@code Ground}.
     * @param instanceName the name of the OWL individual managed by this {@code Descriptor}.
     * @param onto the {@link OWLReferences} to an ontology hosting the described class.
     */
    public SceneIndividualDescriptor(String instanceName, OWLReferences onto) {
        super(instanceName, onto);
    }
    /**
     * Initialise this OWLOOP {@code Descriptor} by fully specifying the {@code Ground}.
     * @param instance the OWL individual managed by this {@code Descriptor}.
     * @param ontoName the {@link OWLReferences} name of the ontology hosting the described class.
     */
    public SceneIndividualDescriptor(OWLNamedIndividual instance, String ontoName) {
        super(instance, ontoName);
    }
    /**
     * Initialise this OWLOOP {@code Descriptor} by fully specifying the {@code Ground}.
     * @param instanceName the name of the OWL individual managed by this {@code Descriptor}.
     * @param ontoName the {@link OWLReferences} name of the ontology hosting the described class.
     */
    public SceneIndividualDescriptor(String instanceName, String ontoName) {
        super(instanceName, ontoName);
    }

    /**
     * This method uses standard OWLOOP synchronisation mechanics
     * to fill this {@code Descriptor} by taking information from the ontology.
     * @return the changes done during reading.
     */
    @Override
    public List<MappingIntent> readExpressionAxioms() {
        List<MappingIntent> r = IndividualExpression.Type.super.readExpressionAxioms();
        r.addAll( IndividualExpression.ObjectLink.super.readExpressionAxioms());
        r.addAll( IndividualExpression.DataLink.super.readExpressionAxioms());
        updateCardinality();
        return r;
    }

    /**
     * This method uses standard OWLOOP synchronisation mechanics
     * to fill the ontology with this {@code Descriptor} contents.
     * @return the changes done during writing.
     */
    @Override
    public List<MappingIntent> writeExpressionAxioms() {
        List<MappingIntent> r = IndividualExpression.Type.super.writeExpressionAxioms();
        r.addAll( IndividualExpression.ObjectLink.super.writeExpressionAxioms());
        r.addAll( IndividualExpression.DataLink.super.writeExpressionAxioms());
        updateCardinality();
        return r;
    }

    private void updateCardinality(){
        cardinality = 0; // hp: all object properties are spatial relations
        for ( DescriptorEntitySet.ObjectLinks s : getIndividualObjectProperties())
            cardinality += s.getValues().size();
        cardinality=cardinality/2;
    }

    /**
     * The sum of all the spatial object properties applied to {@code this}
     * concrete scene individual.
     * @return the individual cardinality based on {@link #readExpressionAxioms()} ()} and {@link #writeExpressionAxioms()} ()}.
     */
    public int getCardinality() {
        return cardinality;
    }

    @Override
    public String toString() {
        return "SceneIndividualDescriptor{" +
                NL + "\t\t\t" + getGround() + " cardinality:" + cardinality +
                "," + NL + "\t∈ " + individualTypes +
                "," + NL + "\t⊨ " + objectLinks +
                "," + NL + "\t⊢ " + dataLinks +
                NL + "}";
    }

    /**
     * This is a standard OWLOOP implementation.
     * @return a new {@code Descriptor} for a scene class in which {@code this} individual belongs.
     */
    @Override
    public SceneClassDescriptor getNewIndividualType(OWLClass instance, OWLReferences ontology) {
        return new SceneClassDescriptor( instance, ontology);
    }


    /**
     * This is a standard OWLOOP implementation.
     * @return an object property of {@code this} individual.
     */
    @Override
    public HierarchicalObjectPropertyDesc getNewIndividualObjectProperty(DescriptorEntitySet.ObjectLinks instance, OWLReferences ontology) {
        return new HierarchicalObjectPropertyDesc( instance.getExpression(), ontology);
    }

    /**
     * This is a standard OWLOOP implementation.
     * @return a data property of {@code this} individual.
     */
    @Override
    public HierarchicalDataPropertyDesc getNewIndividualDataProperty(DescriptorEntitySet.DataLinks instance, OWLReferences ontology) {
        return new HierarchicalDataPropertyDesc( instance.getExpression(), ontology);
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
     * @return the names and values of all the synchronised object properties of {@code this} individual.
     */
    @Override
    public DescriptorEntitySet.ObjectLinksSet getIndividualObjectProperties() {
        return objectLinks;
    }


    /**
     * This is a standard OWLOOP implementation.
     * @return the name of all the scene classes in which {@code this} individual belongs.
     */
    @Override
    public DescriptorEntitySet.Concepts getIndividualTypes() {
        return individualTypes;
    }
}
