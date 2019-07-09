package it.emarolab.scene_identification_tagging.realObject;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.scene_identification_tagging.SITBase;
import it.emarolab.scene_identification_tagging.owloopDescriptor.SpatialIndividualDescriptor;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import java.util.List;

/**
 * The ontological descriptor of an orientable object.
 * <p>
 *     This class describes extends the basic {@link GeometricPrimitive}
 *     by considering also a direction of the principal axis, in accord
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
public class Orientable
        extends GeometricPrimitive
        implements SITBase{

    private Float axisX = null;
    private Float axisY = null;
    private Float axisZ = null;

    /**
     * Initialise and ontological object by have a name based on {@link #currentCOUNT}
     * and {@link INDIVIDUAL#PREFIX_ORIENTABLE}.
     * @param onto the {@link OWLReferences} ontology that will contain the object individual.
     */
    public Orientable(OWLReferences onto) {
        super( INDIVIDUAL.PREFIX_ORIENTABLE + currentCOUNT, onto);
        initialiseProperty();
    }
    /**
     * Initialise and ontological object by have a name based on {@link #currentCOUNT}
     * and {@link INDIVIDUAL#PREFIX_ORIENTABLE}.
     * @param ontoName the name of the {@link OWLReferences} ontology that will contain the object individual.
     */
    public Orientable(String ontoName) {
        super(INDIVIDUAL.PREFIX_ORIENTABLE + currentCOUNT, ontoName);
        initialiseProperty();
    }
    /**
     * Initialise an object in the ontology by fully describing its OWLOOP {@code Ground}.
     * @param instance the object individual.
     * @param onto the {@link OWLReferences} ontology that will contain the object individual.
     */
    public Orientable(OWLNamedIndividual instance, OWLReferences onto) {
        super(instance, onto);
        initialiseProperty();
    }
    /**
     * Initialise an object in the ontology by fully describing its OWLOOP {@code Ground}.
     * @param instanceName the name of the object individual.
     * @param onto the {@link OWLReferences} ontology that will contain the object individual.
     */
    public Orientable(String instanceName, OWLReferences onto) {
        super(instanceName, onto);
        initialiseProperty();
    }
    /**
     * Initialise an object in the ontology by fully describing its OWLOOP {@code Ground}.
     * @param instance the object individual.
     * @param ontoName the name of the {@link OWLReferences} ontology that will contain the object individual.
     */
    public Orientable(OWLNamedIndividual instance, String ontoName) {
        super(instance, ontoName);
        initialiseProperty();
    }
    /**
     * Initialise an object in the ontology by fully describing its OWLOOP {@code Ground}.
     * @param instanceName the name of the object individual.
     * @param ontoName the name of the {@link OWLReferences} ontology that will contain the object individual.
     */
    public Orientable(String instanceName, String ontoName) {
        super(instanceName, ontoName);
        initialiseProperty();
    }

    // common constructor
    private void initialiseProperty(){
        addData( getPropertyAxisX(), true);
        addData( getPropertyAxisY(), true);
        addData( getPropertyAxisZ(), true);
        addTypeIndividual( CLASS.ORIENTABLE);
    }

    /**
     * Return the name of data property used to map the {@code X}
     * component of the direction of the object.
     * @return {@link DATA_PROPERTY#AXIS_X}
     */
    public String getPropertyAxisX(){
        return DATA_PROPERTY.AXIS_X;
    }

    /**
     * Return the name of data property used to map the {@code Y}
     * component of the direction of the object.
     * @return {@link DATA_PROPERTY#AXIS_Y}
     */
    public String getPropertyAxisY(){
        return DATA_PROPERTY.AXIS_Y;
    }

    /**
     * Return the name of data property used to map the {@code Z}
     * component of the direction of the object.
     * @return {@link DATA_PROPERTY#AXIS_Z}
     */
    public String getPropertyAxisZ(){
        return DATA_PROPERTY.AXIS_Z;
    }

    /**
     * A shortcut for calling {@link #setAxisY(Float)},
     * {@link #setAxisY(Float)} and {@link #setAxisZ(Float)}.
     * @param x the {@code X} component of th direction.
     * @param y the {@code Y} component of th direction.
     * @param z the {@code Z} component of th direction.
     */
    public void setAxis(Float x, Float y, Float z){
        setAxisX( x);
        setAxisY( y);
        setAxisZ( z);
    }

    /**
     * Returns the {@code X} component of the direction.
     * Measurements should be in meters.
     * @return the {@code X} component of the principal direction.
     */
    public Float getAxisX() {
        return axisX;
    }

    /**
     * Set the {@code X} component of the direction.
     * Measurements should be in meters.
     * This method automatically add the data to the
     * {@link SpatialIndividualDescriptor}.
     * @param x the {@code X} component of the principal direction.
     */
    public void setAxisX(Float x) {
        this.axisX = x;
        addData( getPropertyAxisX(), x, true);
    }

    /**
     * Returns the {@code Y} component of the direction.
     * Measurements should be in meters.
     * @return the {@code Y} component of the principal direction.
     */
    public Float getAxisY() {
        return axisY;
    }

    /**
     * Set the {@code Y} component of the direction.
     * Measurements should be in meters.
     * This method automatically add the data to the
     * {@link SpatialIndividualDescriptor}.
     * @param y the {@code Y} component of the principal direction.
     */
    public void setAxisY(Float y) {
        this.axisY = y;
        addData( getPropertyAxisY(), y, true);
    }

    /**
     * Returns the {@code Z} component of the direction.
     * Measurements should be in meters.
     * @return the {@code z} component of the principal direction.
     */
    public Float getAxisZ() {
        return axisZ;
    }

    /**
     * Set the {@code z} component of the direction.
     * Measurements should be in meters.
     * This method automatically add the data to the
     * {@link SpatialIndividualDescriptor}.
     * @param z the {@code Z} component of the principal direction.
     */
    public void setAxisZ(Float z) {
        this.axisZ = z;
        addData( getPropertyAxisZ(), z, true);
    }

    /**
     * Enhance the standard OWLOOP read semantic by
     * explicitly set the value of the principal direction,
     * as well as other measurements from super classes.
     * @return the changes done during reading.
     */
    @Override
    public List<MappingIntent> readSemantic() {
        List<MappingIntent> r = super.readSemantic();
        axisX = getLiteral( getPropertyAxisX()).parseFloat();
        axisY = getLiteral( getPropertyAxisY()).parseFloat();
        axisZ = getLiteral( getPropertyAxisZ()).parseFloat();
        return r;
    }
}
