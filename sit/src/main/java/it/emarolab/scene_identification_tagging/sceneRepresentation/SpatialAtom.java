package it.emarolab.scene_identification_tagging.sceneRepresentation;

import it.emarolab.owloop.aMORDescriptor.MORAxioms;
import it.emarolab.scene_identification_tagging.SITBase;
import it.emarolab.scene_identification_tagging.owloopDescriptor.SpatialClassDescriptor;
import it.emarolab.scene_identification_tagging.owloopDescriptor.SpatialIndividualDescriptor;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import java.util.Set;

/**
 * The container for objects and their shape.
 * <p>
 *     This class is a simple container for objects (individuals) and
 *     their shape (ontological class). The latter is considered
 *     to be the leaf in the {@link CLASS#PRIMITIVE} hierarchy in
 *     the ontology where it is assumed that the individual is
 *     classified in only one leaf.
 *
 * <div style="text-align:center;"><small>
 * <b>File</b>:        it.emarolab.scene_identification_tagging.sceneRepresentation.SpatialAtom <br>
 * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
 * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
 * <b>affiliation</b>: EMAROLab, DIBRIS, University of Genoa. <br>
 * <b>date</b>:        05/06/17 <br>
 * </small></div>
 */
public class SpatialAtom
        implements SITBase{

    private OWLClass shape;
    private OWLNamedIndividual object;

    /**
     * Fully initialise this object by setting the object name and its shape.
     * The individual is {@link SpatialIndividualDescriptor#getInstance()},
     * while the second is obtained by searching in all the classification
     * (@link {@link SpatialIndividualDescriptor#buildTypeIndividual()}) for
     * the class the does not have any sub classes apart from OWLNothing.
     * @param objectDescriptor the OWLOOP description of the individual that
     *                         represents an object with a given shape.
     */
    protected SpatialAtom(SpatialIndividualDescriptor objectDescriptor) {
        object = objectDescriptor.getInstance();
        shape = getBottomType( objectDescriptor.buildTypeIndividual());
    }

    private OWLClass getBottomType( Set<SpatialClassDescriptor> shapesSet){
        for (SpatialClassDescriptor shapes : shapesSet) {
            MORAxioms.Concepts subShapes = shapes.getSubConcept();
            if (subShapes.size() == 1) { // OWLNothing
                return shapes.getInstance();
            }
            getBottomType( shapes.buildSubConcept()); // recursive
        }
        System.err.println( "SIT object hierarchy should be singleton !!!");
        return null; // it should not happen. HP: shapes have only one leaf !!!
    }

    /**
     * Return the singleton class classifying this object obtained on constructor
     * that represents its shape.
     * @return the shape type of this object (described as an ontological class).
     */
    public OWLClass getType() {
        return shape;
    }

    /**
     * Return the instance described by the OWLOOP object given on constructor.
     * @return the name of this object (described as an ontological individual).
     */
    public OWLNamedIndividual getIndividual() {
        return object;
    }

    /**
     * Set two {@link SpatialAtom}s to be equal if those have the same
     * {@code object} names regardless from the types.
     * @param o the {@link SpatialAtom} to test for equality.
     * @return {@code true} if this atom is equal to the given object.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SpatialAtom)) return false;
        SpatialAtom that = (SpatialAtom) o;
        return getIndividual() != null ? getIndividual().equals(that.getIndividual()) : that.getIndividual() == null;
    }
    /**
     * It is used to implement {@link #equals(Object)} method.
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        return getIndividual() != null ? getIndividual().hashCode() : 0;
    }

    @Override
    public String toString() {
        return object.getIRI().getRemainder().get() + ":'" + shape.getIRI().getRemainder().get() + "' ";
    }
}