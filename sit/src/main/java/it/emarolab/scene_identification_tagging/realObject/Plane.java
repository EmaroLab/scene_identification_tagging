package it.emarolab.scene_identification_tagging.realObject;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.scene_identification_tagging.SITBase;
import it.emarolab.scene_identification_tagging.owloopDescriptor.SpatialIndividualDescriptor;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import java.util.List;

/**
 * The ontological plane descriptor.
 * <p>
 *     This class describes extends the basic {@link Orientable}
 *     by considering also an hessian for a plane, in accord
 *     with the ontological structure.
 *
 * <div style="text-align:center;"><small>
 * <b>File</b>:        it.emarolab.scene_identification_tagging.realObject.Plane <br>
 * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
 * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
 * <b>affiliation</b>: EMAROLab, DIBRIS, University of Genoa. <br>
 * <b>date</b>:        05/06/17 <br>
 * </small></div>
 */
public class Plane
        extends Orientable
        implements SITBase{

    private Float hessian = null;

    /**
     * Initialise and ontological object by have a name based on {@link #currentCOUNT}
     * and {@link INDIVIDUAL#PREFIX_PLANE}.
     * @param onto the {@link OWLReferences} ontology that will contain the object individual.
     */
    public Plane(OWLReferences onto) {
        super( INDIVIDUAL.PREFIX_PLANE + currentCOUNT, onto);
        initialiseProperty();
    }
    /**
     * Initialise and ontological object by have a name based on {@link #currentCOUNT}
     * and {@link INDIVIDUAL#PREFIX_PLANE}.
     * @param ontoName the name of the {@link OWLReferences} ontology that will contain the object individual.
     */
    public Plane(String ontoName) {
        super(INDIVIDUAL.PREFIX_PLANE + currentCOUNT, ontoName);
        initialiseProperty();
    }
    /**
     * Initialise an object in the ontology by fully describing its OWLOOP {@code Ground}.
     * @param instance the object individual.
     * @param onto the {@link OWLReferences} ontology that will contain the object individual.
     */
    public Plane(OWLNamedIndividual instance, OWLReferences onto) {
        super(instance, onto);
        initialiseProperty();
    }
    /**
     * Initialise an object in the ontology by fully describing its OWLOOP {@code Ground}.
     * @param instanceName the name of the object individual.
     * @param onto the {@link OWLReferences} ontology that will contain the object individual.
     */
    public Plane(String instanceName, OWLReferences onto) {
        super(instanceName, onto);
        initialiseProperty();
    }
    /**
     * Initialise an object in the ontology by fully describing its OWLOOP {@code Ground}.
     * @param instance the object individual.
     * @param ontoName the name of the {@link OWLReferences} ontology that will contain the object individual.
     */
    public Plane(OWLNamedIndividual instance, String ontoName) {
        super(instance, ontoName);
        initialiseProperty();
    }
    /**
     * Initialise an object in the ontology by fully describing its OWLOOP {@code Ground}.
     * @param instanceName the name of the object individual.
     * @param ontoName the name of the {@link OWLReferences} ontology that will contain the object individual.
     */
    public Plane(String instanceName, String ontoName) {
        super(instanceName, ontoName);
        initialiseProperty();
    }

    // common constructor
    private void initialiseProperty(){
        addData( getPropertyHessian(), true);
        addTypeIndividual( CLASS.PLANE);
    }

    /**
     * Return the name of data property used to map the hessian.
     * @return {@link DATA_PROPERTY#HESSIAN}
     */
    public String getPropertyHessian(){
        return DATA_PROPERTY.HESSIAN;
    }

    /**
     * Returns the hessian of this plane
     * Measurements should be in meters.
     * @return the plane hessian.
     */
    public Float getHessian() {
        return hessian;
    }

    /**
     * Set the hessian of the plane.
     * Measurements should be in meters.
     * This method automatically add the data to the
     * {@link SpatialIndividualDescriptor}.
     * @param h the hessian of the plane to set.
     */
    public void setHessian(Float h) {
        this.hessian = h;
        addData( getPropertyHessian(), h, true);
    }

    /**
     * Enhance the standard OWLOOP read semantic by
     * explicitly set the value of the hessian,
     * as well as other measurements from super classes.
     * @return the changes done during reading.
     */
    @Override
    public List<MappingIntent> readSemantic() {
        List<MappingIntent> r = super.readSemantic();
        hessian = getLiteral( getPropertyHessian()).parseFloat();
        return r;
    }
}
