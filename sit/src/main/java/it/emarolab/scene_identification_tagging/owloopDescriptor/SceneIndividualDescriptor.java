package it.emarolab.scene_identification_tagging.owloopDescriptor;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.owloop.aMORDescriptor.MORAxioms;
import it.emarolab.owloop.aMORDescriptor.MORIndividual;
import it.emarolab.owloop.aMORDescriptor.utility.MORIndividualBase;
import it.emarolab.owloop.aMORDescriptor.utility.dataProperty.MORHierarchicalDataProperty;
import it.emarolab.owloop.aMORDescriptor.utility.objectProperty.MORHierarchicalObjectProperty;
import it.emarolab.scene_identification_tagging.SITBase;
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
 * <b>File</b>:        it.emarolab.scene_identification_tagging.owloopDescriptor.SceneIndividualDescriptor <br>
 * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
 * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
 * <b>affiliation</b>: EMAROLab, DIBRIS, University of Genoa. <br>
 * <b>date</b>:        05/06/17 <br>
 * </small></div>
 */
public class SceneIndividualDescriptor
        extends MORIndividualBase
        implements MORIndividual.Type<SceneClassDescriptor>,
        MORIndividual.ObjectLink<MORHierarchicalObjectProperty>,
        MORIndividual.DataLink<MORHierarchicalDataProperty>,
        SITBase{

    private int cardinality = 0;

    private MORAxioms.Concepts individualTypes = new MORAxioms.Concepts();
    private MORAxioms.ObjectSemantics objectLinks = new MORAxioms.ObjectSemantics();
    private MORAxioms.DataSemantics dataLinks = new MORAxioms.DataSemantics();


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
    public List<MappingIntent> readSemantic() {
        List<MappingIntent> r = MORIndividual.Type.super.readSemantic();
        r.addAll( MORIndividual.ObjectLink.super.readSemantic());
        r.addAll( MORIndividual.DataLink.super.readSemantic());
        updateCardinality();
        return r;
    }

    /**
     * This method uses standard OWLOOP synchronisation mechanics
     * to fill the ontology with this {@code Descriptor} contents.
     * @return the changes done during writing.
     */
    @Override
    public List<MappingIntent> writeSemantic() {
        List<MappingIntent> r = MORIndividual.Type.super.writeSemantic();
        r.addAll( MORIndividual.ObjectLink.super.writeSemantic());
        r.addAll( MORIndividual.DataLink.super.writeSemantic());
        updateCardinality();
        return r;
    }

    private void updateCardinality(){
        cardinality = 0; // hp: all object properties are spatial relations
        for ( MORAxioms.ObjectSemantic s : getObjectSemantics())
            cardinality += s.getValues().size();
    }

    /**
     * This is a standard OWLOOP implementation.
     * @return a new {@code Descriptor} for a scene class in which {@code this} individual belongs.
     */
    @Override // you can change the returning type to any implementations of MORConcept
    public SceneClassDescriptor getNewTypeIndividual(OWLClass instance, OWLReferences ontology) {
        return new SceneClassDescriptor( instance, ontology);
    }

    /**
     * This is a standard OWLOOP implementation.
     * @return the name of all the scene classes in which {@code this} individual belongs.
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
    public MORHierarchicalObjectProperty getNewObjectIndividual(MORAxioms.ObjectSemantic instance, OWLReferences ontology) {
        return new MORHierarchicalObjectProperty( instance.getSemantic(), ontology);
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

    /**
     * The sum of all the spatial object properties applied to {@code this}
     * concrete scene individual.
     * @return the individual cardinality based on {@link #readSemantic()} and {@link #writeSemantic()}.
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
}
