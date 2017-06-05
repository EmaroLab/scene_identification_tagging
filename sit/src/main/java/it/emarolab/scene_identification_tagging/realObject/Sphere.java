package it.emarolab.scene_identification_tagging.realObject;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.scene_identification_tagging.SITBase;
import it.emarolab.scene_identification_tagging.owloopDescriptor.SpatialIndividualDescriptor;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import java.util.List;

/**
 * The ontological sphere descriptor.
 * <p>
 *     This class describes extends the basic {@link GeometricPrimitive}
 *     by considering also a radius for a sphere, in accord
 *     with the ontological structure.
 *
 * <div style="text-align:center;"><small>
 * <b>File</b>:        it.emarolab.scene_identification_tagging.realObject.Sphere <br>
 * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
 * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
 * <b>affiliation</b>: EMAROLab, DIBRIS, University of Genoa. <br>
 * <b>date</b>:        05/06/17 <br>
 * </small></div>
 */
public class Sphere
        extends GeometricPrimitive
        implements SITBase{

    private Float radius = null;

    /**
     * Initialise and ontological object by have a name based on {@link #currentCOUNT}
     * and {@link INDIVIDUAL#PREFIX_SPHERE}.
     * @param onto the {@link OWLReferences} ontology that will contain the object individual.
     */
    public Sphere(OWLReferences onto) {
        super( INDIVIDUAL.PREFIX_SPHERE + currentCOUNT, onto);
        initialiseProperty();
    }
    /**
     * Initialise and ontological object by have a name based on {@link #currentCOUNT}
     * and {@link INDIVIDUAL#PREFIX_SPHERE}.
     * @param ontoName the name of the {@link OWLReferences} ontology that will contain the object individual.
     */
    public Sphere(String ontoName) {
        super(INDIVIDUAL.PREFIX_SPHERE + currentCOUNT, ontoName);
        initialiseProperty();
    }
    /**
     * Initialise an object in the ontology by fully describing its OWLOOP {@code Ground}.
     * @param instance the object individual.
     * @param onto the {@link OWLReferences} ontology that will contain the object individual.
     */
    public Sphere(OWLNamedIndividual instance, OWLReferences onto) {
        super(instance, onto);
        initialiseProperty();
    }
    /**
     * Initialise an object in the ontology by fully describing its OWLOOP {@code Ground}.
     * @param instanceName the name of the object individual.
     * @param onto the {@link OWLReferences} ontology that will contain the object individual.
     */
    public Sphere(String instanceName, OWLReferences onto) {
        super(instanceName, onto);
        initialiseProperty();
    }
    /**
     * Initialise an object in the ontology by fully describing its OWLOOP {@code Ground}.
     * @param instance the object individual.
     * @param ontoName the name of the {@link OWLReferences} ontology that will contain the object individual.
     */
    public Sphere(OWLNamedIndividual instance, String ontoName) {
        super(instance, ontoName);
        initialiseProperty();
    }
    /**
     * Initialise an object in the ontology by fully describing its OWLOOP {@code Ground}.
     * @param instanceName the name of the object individual.
     * @param ontoName the name of the {@link OWLReferences} ontology that will contain the object individual.
     */
    public Sphere(String instanceName, String ontoName) {
        super(instanceName, ontoName);
        initialiseProperty();
    }

    // common constructor
    private void initialiseProperty(){
        addData( getPropertyRadius(), true);
        addTypeIndividual( CLASS.SPHERE);
    }

    /**
     * Return the name of data property used to map the radius.
     * @return {@link DATA_PROPERTY#RADIUS_SPHERE}
     */
    public String getPropertyRadius(){
        return DATA_PROPERTY.RADIUS_SPHERE;
    }

    /**
     * Returns the radius of this sphere
     * Measurements should be in meters.
     * @return the sphere radius.
     */
    public Float getRadius() {
        return radius;
    }

    /**
     * Set the radius of the sphere.
     * Measurements should be in meters.
     * This method automatically add the data to the
     * {@link SpatialIndividualDescriptor}.
     * @param r the radius of the sphere to set.
     */
    public void setRadius(Float r) {
        this.radius = r;
        addData( getPropertyRadius(), r, true);
    }

    /**
     * Enhance the standard OWLOOP read semantic by
     * explicitly set the value of the radius,
     * as well as other measurements from super classes.
     * @return the changes done during reading.
     */
    @Override
    public List<MappingIntent> readSemantic() {
        List<MappingIntent> r = super.readSemantic();
        radius = getLiteral( getPropertyRadius()).parseFloat();
        return r;
    }
}
