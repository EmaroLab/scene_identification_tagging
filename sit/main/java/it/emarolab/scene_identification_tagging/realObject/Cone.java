package it.emarolab.scene_identification_tagging.realObject;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.scene_identification_tagging.SITBase;
import it.emarolab.scene_identification_tagging.owloopDescriptor.SpatialIndividualDescriptor;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import java.util.List;

/**
 * The ontological cone descriptor.
 * <p>
 *     This class describes extends the basic {@link Orientable}
 *     by considering also a apex, a radius and a height, in accord
 *     with the ontological structure.
 *
 * <div style="text-align:center;"><small>
 * <b>File</b>:        it.emarolab.scene_identification_tagging.realObject.Cone <br>
 * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
 * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
 * <b>affiliation</b>: EMAROLab, DIBRIS, University of Genoa. <br>
 * <b>date</b>:        05/06/17 <br>
 * </small></div>
 */
public class Cone
        extends Orientable
        implements SITBase{

    private Float apexX = null;
    private Float apexY = null;
    private Float apexZ = null;
    private Float height = null;
    private Float radius = null;

    /**
     * Initialise and ontological object by have a name based on {@link #currentCOUNT}
     * and {@link INDIVIDUAL#PREFIX_CONE}.
     * @param onto the {@link OWLReferences} ontology that will contain the object individual.
     */
    public Cone(OWLReferences onto) {
        super( INDIVIDUAL.PREFIX_CONE + currentCOUNT, onto);
        initialiseProperty();
    }
    /**
     * Initialise and ontological object by have a name based on {@link #currentCOUNT}
     * and {@link INDIVIDUAL#PREFIX_CONE}.
     * @param ontoName the name of the {@link OWLReferences} ontology that will contain the object individual.
     */
    public Cone(String ontoName) {
        super(INDIVIDUAL.PREFIX_CONE + currentCOUNT, ontoName);
        initialiseProperty();
    }
    /**
     * Initialise an object in the ontology by fully describing its OWLOOP {@code Ground}.
     * @param instance the object individual.
     * @param onto the {@link OWLReferences} ontology that will contain the object individual.
     */
    public Cone(OWLNamedIndividual instance, OWLReferences onto) {
        super(instance, onto);
        initialiseProperty();
    }
    /**
     * Initialise an object in the ontology by fully describing its OWLOOP {@code Ground}.
     * @param instanceName the name of the object individual.
     * @param onto the {@link OWLReferences} ontology that will contain the object individual.
     */
    public Cone(String instanceName, OWLReferences onto) {
        super(instanceName, onto);
        initialiseProperty();
    }
    /**
     * Initialise an object in the ontology by fully describing its OWLOOP {@code Ground}.
     * @param instance the object individual.
     * @param ontoName the name of the {@link OWLReferences} ontology that will contain the object individual.
     */
    public Cone(OWLNamedIndividual instance, String ontoName) {
        super(instance, ontoName);
        initialiseProperty();
    }
    /**
     * Initialise an object in the ontology by fully describing its OWLOOP {@code Ground}.
     * @param instanceName the name of the object individual.
     * @param ontoName the name of the {@link OWLReferences} ontology that will contain the object individual.
     */
    public Cone(String instanceName, String ontoName) {
        super(instanceName, ontoName);
        initialiseProperty();
    }

    // common constructor
    private void initialiseProperty(){
        addData( getPropertyApexX(), true);
        addData( getPropertyApexY(), true);
        addData( getPropertyApexZ(), true);
        addData( getPropertyHeight(), true);
        addData( getPropertyRadius(), true);
        addTypeIndividual( CLASS.CONE);
    }

    /**
     * Return the data property used to map the {@code X}
     * coordinate of the apex of the code.
     * @return {@link DATA_PROPERTY#CONE_APEX_X}
     */
    public String getPropertyApexX(){
        return DATA_PROPERTY.CONE_APEX_X;
    }

    /**
     * Return the data property used to map the {@code Y}
     * coordinate of the apex of the code.
     * @return {@link DATA_PROPERTY#CONE_APEX_Y}
     */
    public String getPropertyApexY(){
        return DATA_PROPERTY.CONE_APEX_Y;
    }

    /**
     * Return the data property used to map the {@code Z}
     * coordinate of the apex of the code.
     * @return {@link DATA_PROPERTY#CONE_APEX_Z}
     */
    public String getPropertyApexZ(){
        return DATA_PROPERTY.CONE_APEX_Z;
    }

    /**
     * Return the data property used to map the height of the code.
     * @return {@link DATA_PROPERTY#CONE_HEIGHT}
     */
    public String getPropertyHeight(){
        return DATA_PROPERTY.CONE_HEIGHT;
    }

    /**
     * Return the data property used to map the radius of the cone.
     * @return {@link DATA_PROPERTY#CONE_RADIUS}
     */
    public String getPropertyRadius(){
        return DATA_PROPERTY.CONE_RADIUS;
    }

    /**
     * A shortcut for calling {@link #setApexX(Float)},
     * {@link #setApexY(Float)} and {@link #setApexZ(Float)}.
     * @param x the {@code X} coordinate of the apex.
     * @param y the {@code Y} coordinate of the apex.
     * @param z the {@code Z} coordinate of the apex.
     */
    public void setApex(Float x, Float y, Float z){
        setApexX( x);
        setApexY( y);
        setApexZ( z);
    }

    /**
     * Returns the {@code X} coordinate of the apex.
     * Measurements should be in meters.
     * @return the {@code X} component of the apex.
     */
    public Float getApexX() {
        return apexX;
    }

    /**
     * Set the {@code X} coordinate of the apex.
     * Measurements should be in meters.
     * This method automatically add the data to the
     * {@link SpatialIndividualDescriptor}.
     * @param x the {@code X} coordinate of the apex.
     */
    public void setApexX(Float x) {
        this.apexX = x;
        addData( getPropertyApexX(), x, true);
    }

    /**
     * Returns the {@code Y} coordinate of the apex.
     * Measurements should be in meters.
     * @return the {@code Y} component of the apex.
     */
    public Float getApexY() {
        return apexY;
    }

    /**
     * Set the {@code Y} coordinate of the apex.
     * Measurements should be in meters.
     * This method automatically add the data to the
     * {@link SpatialIndividualDescriptor}.
     * @param y the {@code Y} coordinate of the apex.
     */
    public void setApexY(Float y) {
        this.apexY = y;
        addData( getPropertyApexY(), y, true);
    }

    /**
     * Returns the {@code Z} coordinate of the apex.
     * Measurements should be in meters.
     * @return the {@code Z} component of the apex.
     */
    public Float getApexZ() {
        return apexZ;
    }

    /**
     * Set the {@code Z} coordinate of the apex.
     * Measurements should be in meters.
     * This method automatically add the data to the
     * {@link SpatialIndividualDescriptor}.
     * @param z the {@code Z} coordinate of the apex.
     */
    public void setApexZ(Float z) {
        this.apexZ = z;
        addData( getPropertyApexZ(), z, true);
    }

    /**
     * Returns the height value of the cone.
     * Measurements should be in meters.
     * @return the height of the cone.
     */
    public Float getHight() {
        return height;
    }

    /**
     * Set the height value of the cone.
     * Measurements should be in meters.
     * This method automatically add the data to the
     * {@link SpatialIndividualDescriptor}.
     * @param h the height value.
     */
    public void setHeight(Float h) {
        this.height = h;
        addData( getPropertyHeight(), h, true);
    }

    /**
     * Returns the radius value of the cone.
     * Measurements should be in meters.
     * @return the radius of the cone.
     */
    public Float getRadius() {
        return radius;
    }

    /**
     * Set the radius value of the cone.
     * Measurements should be in meters.
     * This method automatically add the data to the
     * {@link SpatialIndividualDescriptor}.
     * @param r the radius value.
     */
    public void setRadius(Float r) {
        this.radius = r;
        addData( getPropertyRadius(), r, true);
    }

    /**
     * Enhance the standard OWLOOP read semantic by
     * explicitly set the value of the apex, the height and the radius
     * as well as other measurements from super classes.
     * @return the changes done during reading.
     */
    @Override
    public List<MappingIntent> readSemantic() {
        List<MappingIntent> r = super.readSemantic();
        apexX = getLiteral( getPropertyApexX()).parseFloat();
        apexY = getLiteral( getPropertyApexY()).parseFloat();
        apexZ = getLiteral( getPropertyApexZ()).parseFloat();
        height = getLiteral( getPropertyHeight()).parseFloat();
        radius = getLiteral( getPropertyRadius()).parseFloat();
        return r;
    }
}
