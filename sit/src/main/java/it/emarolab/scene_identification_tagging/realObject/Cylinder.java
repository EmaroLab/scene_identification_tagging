package it.emarolab.scene_identification_tagging.realObject;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.scene_identification_tagging.SITBase;
import it.emarolab.scene_identification_tagging.owloopDescriptor.SpatialIndividualDescriptor;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import java.util.List;

/**
 * The ontological cylinder descriptor.
 * <p>
 *     This class describes extends the basic {@link Orientable}
 *     by considering also a generic point in the axis, a radius and a height, in accord
 *     with the ontological structure.
 *
 * <div style="text-align:center;"><small>
 * <b>File</b>:        it.emarolab.scene_identification_tagging.realObject.Cylinder <br>
 * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
 * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
 * <b>affiliation</b>: EMAROLab, DIBRIS, University of Genoa. <br>
 * <b>date</b>:        05/06/17 <br>
 * </small></div>
 */
public class Cylinder
        extends Orientable
        implements SITBase{

    private Float pointX = null;
    private Float pointY = null;
    private Float pointZ = null;
    private Float height = null;
    private Float radius = null;

    /**
     * Initialise and ontological object by have a name based on {@link #currentCOUNT}
     * and {@link INDIVIDUAL#PREFIX_CYLINDER}.
     * @param onto the {@link OWLReferences} ontology that will contain the object individual.
     */
    public Cylinder(OWLReferences onto) {
        super( INDIVIDUAL.PREFIX_CYLINDER + currentCOUNT, onto);
        initialiseProperty();
    }
    /**
     * Initialise and ontological object by have a name based on {@link #currentCOUNT}
     * and {@link INDIVIDUAL#PREFIX_CYLINDER}.
     * @param ontoName the name of the {@link OWLReferences} ontology that will contain the object individual.
     */
    public Cylinder(String ontoName) {
        super(INDIVIDUAL.PREFIX_CYLINDER + currentCOUNT, ontoName);
        initialiseProperty();
    }
    /**
     * Initialise an object in the ontology by fully describing its OWLOOP {@code Ground}.
     * @param instance the object individual.
     * @param onto the {@link OWLReferences} ontology that will contain the object individual.
     */
    public Cylinder(OWLNamedIndividual instance, OWLReferences onto) {
        super(instance, onto);
        initialiseProperty();
    }
    /**
     * Initialise an object in the ontology by fully describing its OWLOOP {@code Ground}.
     * @param instanceName the name of the object individual.
     * @param onto the {@link OWLReferences} ontology that will contain the object individual.
     */
    public Cylinder(String instanceName, OWLReferences onto) {
        super(instanceName, onto);
        initialiseProperty();
    }
    /**
     * Initialise an object in the ontology by fully describing its OWLOOP {@code Ground}.
     * @param instance the object individual.
     * @param ontoName the name of the {@link OWLReferences} ontology that will contain the object individual.
     */
    public Cylinder(OWLNamedIndividual instance, String ontoName) {
        super(instance, ontoName);
        initialiseProperty();
    }
    /**
     * Initialise an object in the ontology by fully describing its OWLOOP {@code Ground}.
     * @param instanceName the name of the object individual.
     * @param ontoName the name of the {@link OWLReferences} ontology that will contain the object individual.
     */
    public Cylinder(String instanceName, String ontoName) {
        super(instanceName, ontoName);
        initialiseProperty();
    }

    // common constructor
    private void initialiseProperty(){
        addData( getPropertyPointX(), true);
        addData( getPropertyPointY(), true);
        addData( getPropertyPointZ(), true);
        addData( getPropertyHeight(), true);
        addData( getPropertyRadius(), true);
        addTypeIndividual( CLASS.CYLINDER);
    }

    /**
     * Return the data property used to map the {@code X}
     * coordinate of a generic point in the cylinder principal axis.
     * @return {@link DATA_PROPERTY#CYLINDER_POINT_X}
     */
    public String getPropertyPointX(){
        return DATA_PROPERTY.CYLINDER_POINT_X;
    }

    /**
     * Return the data property used to map the {@code Y}
     * coordinate of a generic point in the cylinder principal axis.
     * @return {@link DATA_PROPERTY#CYLINDER_POINT_Y}
     */
    public String getPropertyPointY(){
        return DATA_PROPERTY.CYLINDER_POINT_Y;
    }

    /**
     * Return the data property used to map the {@code Z}
     * coordinate of a generic point in the cylinder principal axis.
     * @return {@link DATA_PROPERTY#CYLINDER_POINT_Z}
     */
    public String getPropertyPointZ(){
        return DATA_PROPERTY.CYLINDER_POINT_Z;
    }

    /**
     * Return the data property used to map the height of the cylinder.
     * @return {@link DATA_PROPERTY#CYLINDER_HEIGHT}
     */
    public String getPropertyHeight(){
        return DATA_PROPERTY.CYLINDER_HEIGHT;
    }

    /**
     * Return the data property used to map the height of the cylinder.
     * @return {@link DATA_PROPERTY#CYLINDER_RADIUS}
     */
    public String getPropertyRadius(){
        return DATA_PROPERTY.CYLINDER_RADIUS;
    }

    /**
     * A shortcut for calling {@link #setPointX(Float)},
     * {@link #setPointY(Float)} and {@link #setPointZ(Float)}.
     * @param x the {@code X} coordinate of a point in the axis.
     * @param y the {@code Y} coordinate of a point in the axis.
     * @param z the {@code Z} coordinate of a point in the axis.
     */
    public void setApex(Float x, Float y, Float z){
        setPointX( x);
        setPointY( y);
        setPointZ( z);
    }

    /**
     * Returns the {@code X} coordinate of a
     * point belonging to the principal axis.
     * Measurements should be in meters.
     * @return the {@code X} coordinate of a point in the axis.
     */
    public Float getPointX() {
        return pointX;
    }

    /**
     * Set the {@code X} coordinate of
     * a pointin the axis.
     * Measurements should be in meters.
     * This method automatically add the data to the
     * {@link SpatialIndividualDescriptor}.
     * @param x the {@code X} coordinate of a point in the axis.
     */
    public void setPointX(Float x) {
        this.pointX = x;
        addData( getPropertyPointX(), x, true);
    }

    /**
     * Returns the {@code Y} coordinate of a
     * point belonging to the principal axis.
     * Measurements should be in meters.
     * @return the {@code Y} coordinate of a point in the axis.
     */
    public Float getPointY() {
        return pointY;
    }

    /**
     * Set the {@code Y} coordinate of
     * a pointin the axis.
     * Measurements should be in meters.
     * This method automatically add the data to the
     * {@link SpatialIndividualDescriptor}.
     * @param y the {@code Y} coordinate of a point in the axis.
     */
    public void setPointY(Float y) {
        this.pointY = y;
        addData( getPropertyPointY(), y, true);
    }

    /**
     * Returns the {@code Z} coordinate of a
     * point belonging to the principal axis.
     * Measurements should be in meters.
     * @return the {@code Z} coordinate of a point in the axis.
     */
    public Float getPointZ() {
        return pointZ;
    }

    /**
     * Set the {@code Z} coordinate of
     * a pointin the axis.
     * Measurements should be in meters.
     * This method automatically add the data to the
     * {@link SpatialIndividualDescriptor}.
     * @param z the {@code Z} coordinate of a point in the axis.
     */
    public void setPointZ(Float z) {
        this.pointZ = z;
        addData( getPropertyPointZ(), z, true);
    }

    /**
     * Returns the height value of the cylinder.
     * Measurements should be in meters.
     * @return the height.
     */
    public Float getHight() {
        return height;
    }

    /**
     * Set the height value of the cylinder.
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
     * Returns the radius value of the cylinder.
     * Measurements should be in meters.
     * @return the radius.
     */
    public Float getRadius() {
        return radius;
    }

    /**
     * Set the radius value of the cylinder.
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
     * explicitly set the value of a point in the axis, the height and the radius
     * as well as other measurements from super classes.
     * @return the changes done during reading.
     */
    @Override
    public List<MappingIntent> readSemantic() {
        List<MappingIntent> r = super.readSemantic();
        pointX = getLiteral( getPropertyPointX()).parseFloat();
        pointY = getLiteral( getPropertyPointY()).parseFloat();
        pointZ = getLiteral( getPropertyPointZ()).parseFloat();
        height = getLiteral( getPropertyHeight()).parseFloat();
        radius = getLiteral( getPropertyRadius()).parseFloat();
        return r;
    }
}
