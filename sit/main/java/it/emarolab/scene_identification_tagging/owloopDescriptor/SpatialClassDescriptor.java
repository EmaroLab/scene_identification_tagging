package it.emarolab.scene_identification_tagging.owloopDescriptor;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.owloop.aMORDescriptor.MORAxioms;
import it.emarolab.owloop.aMORDescriptor.MORConcept;
import it.emarolab.owloop.aMORDescriptor.utility.MORConceptBase;
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
 * <b>File</b>:        it.emarolab.scene_identification_tagging.owloopDescriptor.SpatialClassDescriptor <br>
 * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
 * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
 * <b>affiliation</b>: EMAROLab, DIBRIS, University of Genoa. <br>
 * <b>date</b>:        05/06/17 <br>
 * </small></div>
 */
public class SpatialClassDescriptor
        extends MORConceptBase
        implements MORConcept.Sub<SpatialClassDescriptor>,
        MORConcept.Classify<SpatialIndividualDescriptor> {

    private MORAxioms.Concepts subConcept = new MORAxioms.Concepts();
    private MORAxioms.Individuals classifiedIndividual = new MORAxioms.Individuals();

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
    public List<MappingIntent> readSemantic() {
        List<MappingIntent> r = MORConcept.Sub.super.readSemantic();
        r.addAll( MORConcept.Classify.super.readSemantic());
        return r;
    }

    /**
     * This method uses standard OWLOOP synchronisation mechanics
     * to fill the ontology with this {@code Descriptor} contents.
     * @return the changes done during writing.
     */
    @Override
    public List<MappingIntent> writeSemantic() {
        List<MappingIntent> r = MORConcept.Sub.super.writeSemantic();
        r.addAll( MORConcept.Classify.super.writeSemantic());
        return r;
    }

    /**
     * This is a standard OWLOOP implementation.
     * @return a new {@code Descriptor} for a scene class.
     */
    @Override
    public SpatialClassDescriptor getNewSubConcept(OWLClass instance, OWLReferences ontology) {
        return new SpatialClassDescriptor( instance, ontology);
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
     * @return a new {@code Descriptor} for an object individual.
     */
    @Override
    public SpatialIndividualDescriptor getNewIndividualClassified(OWLNamedIndividual instance, OWLReferences ontology) {
        return new SpatialIndividualDescriptor( instance, ontology);
    }

    /**
     * This is a standard OWLOOP implementation.
     * @return the name of all the object individuals classified in {@code this}.
     */
    @Override
    public MORAxioms.Individuals getIndividualClassified() {
        return classifiedIndividual;
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