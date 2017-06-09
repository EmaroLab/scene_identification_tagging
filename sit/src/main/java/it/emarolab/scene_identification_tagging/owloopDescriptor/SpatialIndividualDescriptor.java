package it.emarolab.scene_identification_tagging.owloopDescriptor;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.owloop.aMORDescriptor.MORAxioms;
import it.emarolab.owloop.aMORDescriptor.MORIndividual;
import it.emarolab.owloop.aMORDescriptor.utility.MORIndividualBase;
import it.emarolab.owloop.aMORDescriptor.utility.dataProperty.MORHierarchicalDataProperty;
import it.emarolab.owloop.aMORDescriptor.utility.objectProperty.MORHierarchicalObjectProperty;
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
 * <b>File</b>:        it.emarolab.scene_identification_tagging.owloopDescriptor.SpatialIndividualDescriptor <br>
 * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
 * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
 * <b>affiliation</b>: EMAROLab, DIBRIS, University of Genoa. <br>
 * <b>date</b>:        05/06/17 <br>
 * </small></div>
 */
public class SpatialIndividualDescriptor
        extends MORIndividualBase
        implements MORIndividual.Disjoint<SpatialIndividualDescriptor>,
        MORIndividual.Type<SpatialClassDescriptor>,
        MORIndividual.DataLink<MORHierarchicalDataProperty>, // this buildings are never read or write
        MORIndividual.ObjectLink<SpatialObjectPropertyDescriptor>{

    private MORAxioms.Individuals disjointIndividual = new MORAxioms.Individuals();
    private MORAxioms.Concepts individualTypes = new MORAxioms.Concepts();
    private MORAxioms.ObjectSemantics objectLinks = new MORAxioms.ObjectSemantics();
    private MORAxioms.DataSemantics dataLinks = new MORAxioms.DataSemantics();

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
    public List<MappingIntent> readSemantic() {
        List<MappingIntent> r = Disjoint.super.readSemantic();
        r.addAll( Type.super.readSemantic());
        r.addAll( ObjectLink.super.readSemantic());
        r.addAll( DataLink.super.readSemantic());
        return r;
    }

    /**
     * This method uses standard OWLOOP synchronisation mechanics
     * to fill the ontology with this {@code Descriptor} contents.
     * @return the changes done during writing.
     */
    @Override
    public List<MappingIntent> writeSemantic() {
        List<MappingIntent> r = Disjoint.super.writeSemantic();
        r.addAll( Type.super.writeSemantic());
        r.addAll( ObjectLink.super.writeSemantic());
        r.addAll( DataLink.super.writeSemantic());
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
     * @return the name of all disjoint individuals to {@code this}.
     */
    @Override
    public MORAxioms.Individuals getDisjointIndividual() {
        return disjointIndividual;
    }


    /**
     * This is a standard OWLOOP implementation.
     * @return a new {@code Descriptor} for a class classifying {@code this} individual.
     */
    @Override
    public SpatialClassDescriptor getNewTypeIndividual(OWLClass instance, OWLReferences ontology) {
        return new SpatialClassDescriptor( instance, ontology);
    }

    /**
     * This is a standard OWLOOP implementation.
     * @return the name of all the class in which {@code this} individual is belonging to.
     */
    @Override
    public MORAxioms.Concepts getTypeIndividual() {
        return individualTypes;
    }

    /**
     * This is a standard OWLOOP implementation.
     * @return an object property of {@code this} individual.
     */
    @Override
    public SpatialObjectPropertyDescriptor getNewObjectIndividual(MORAxioms.ObjectSemantic instance, OWLReferences ontology) {
        return new SpatialObjectPropertyDescriptor( instance.getSemantic(), ontology);
    }

    /**
     * This is a standard OWLOOP implementation.
     * @return the names and values of all the synchronised object properties of {@code this} individual.
     */
    @Override
    public MORAxioms.ObjectSemantics getObjectSemantics() {
        return objectLinks;
    }

    /**
     * This is a standard OWLOOP implementation.
     * @return a data property of {@code this} individual.
     */
    @Override
    public MORHierarchicalDataProperty getNewDataIndividual(MORAxioms.DataSemantic instance, OWLReferences ontology) {
        return new MORHierarchicalDataProperty( instance.getSemantic(), ontology);
    }

    /**
     * This is a standard OWLOOP implementation.
     * @return the names and values of all the synchronised data properties of {@code this} individual.
     */
    @Override
    public MORAxioms.DataSemantics getDataSemantics() {
        return dataLinks;
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
}
