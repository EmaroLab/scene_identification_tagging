package it.emarolab.scene_identification_tagging.sceneRepresentation;

import com.google.common.base.Objects;
import it.emarolab.scene_identification_tagging.SITBase;
import it.emarolab.scene_identification_tagging.owloopDescriptor.SpatialIndividualDescriptor;
import it.emarolab.scene_identification_tagging.owloopDescriptor.SpatialObjectPropertyDescriptor;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;


/**
 * The container for a spatial relation between two objects.
 * <p>
 *     This class is a simple container for relate two
 *     {@link SpatialAtom} (i.e.: {@code subject} and {@code object}) within a spatial relation obtained from the
 *     ontology through SWRL rules. Rules are evaluated between al pairs
 *     of individuals in the {@link CLASS#PRIMITIVE}, remember that those are
 *     computationally expensive and so the number of individual in such
 *     a class should be as small as possible. Also they assume that
 *     all the individuals have all the numbers describing their coefficients.
 *
 * <div style="text-align:center;"><small>
 * <b>File</b>:        it.emarolab.scene_identification_tagging.sceneRepresentation.SpatialRelation <br>
 * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
 * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
 * <b>affiliation</b>: EMAROLab, DIBRIS, University of Genoa. <br>
 * <b>date</b>:        05/06/17 <br>
 * </small></div>
 */
public class SpatialRelation
        implements SITBase{

    private SpatialAtom subject, object;
    private OWLObjectProperty relation;
    private OWLObjectProperty inverseRelation;

    /**
     * Fully construct all the fields (i.e.: {@code getters}) for this object.
     * @param subject the OWLOOP descriptor for the {@link #getSubject()}, all the other manipulation are based on the ontology it describes.
     * @param relation the semantic spatial property between the {@code subject} and {@code object}.
     * @param object the name of the individual which is the {@code object} of the relation. This is wrapped in a {@code new} {@link SpatialIndividualDescriptor}.
     */
    protected SpatialRelation(SpatialIndividualDescriptor subject, OWLObjectProperty relation, OWLNamedIndividual object) {
        this.subject = new SpatialAtom( subject);

        this.relation = relation;

        SpatialIndividualDescriptor objectDescriptor = new SpatialIndividualDescriptor(object, subject.getOntology());
        objectDescriptor.readSemantic();
        this.object = new SpatialAtom(objectDescriptor);

        SpatialObjectPropertyDescriptor propertyDescriptor = new SpatialObjectPropertyDescriptor( relation, subject.getOntology());
        inverseRelation = propertyDescriptor.getSpatialInverseProperty();
    }


    /**
     * Return the semantic object that is the {@code subject}
     * of {@code this} relation.
     * @return the subject of this spatial relation (set on constructor).
     */
    public SpatialAtom getSubject() {
        return subject;
    }

    /**
     * Return the semantic object that is the {@code object}
     * of {@code this} relation.
     * @return the object of this spatial relation (set on constructor).
     */
    public SpatialAtom getObject() {
        return object;
    }

    /**
     * Return the semantic spatial relations that is applied
     * from the {@code subject} to the {@code object}.
     * @return the name of this spatial relation (set on constructor).
     */
    public OWLObjectProperty getRelation() {
        return relation;
    }

    /**
     * Return the semantic inverse spatial relations that is applied
     * from the {@code object} to the {@code subject}.
     * @return the name of the inverse to the given spatial relation (set on constructor).
     */
    public OWLObjectProperty getInverseRelation() {
        return inverseRelation;
    }

    /**
     * Check if the relation is symmetric
     * @return {@code true} if the relation and its inverse are equals.
     */
    public boolean isSymmetric(){
        return relation.equals( inverseRelation);
    }

    /**
     * Set two {@link SpatialRelation}s to be equal if those have the same
     * {@code object} and {@code subject} as well as {@code relation}.
     * Inverse relation equality is not checked.
     * @param o the {@link SpatialRelation} to test for equality.
     * @return {@code true} if this relation is equal to the given object.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SpatialRelation)) return false;
        SpatialRelation that = (SpatialRelation) o;

        boolean direct = getSubject().equals(that.getSubject())
                & getObject().equals(that.getObject())
                & getRelation().equals(that.getRelation());

        /*boolean inverse = getSubject().equals(that.getObject())
                & getObject().equals(that.getSubject())
                & getRelation().equals(that.getInverseRelation());

        return direct | inverse;*/
        return direct;
    }

    /**
     * It is used to implement {@link #equals(Object)} method.
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(getSubject(), getObject(), getRelation());
    }


    @Override
    public String toString() {
        return subject + " " + relation.getIRI().getRemainder().get() + "(=" +
                inverseRelation.getIRI().getRemainder().get() + ")^-1" + object;
    }


}
