package it.emarolab.scene_identification_tagging.realObject;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.scene_identification_tagging.SITBase;
import it.emarolab.scene_identification_tagging.owloopDescriptor.SpatialIndividualDescriptor;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import java.util.List;

/**
 * The base class for an object descriptor.
 * <p>
 *     This class describes each ontological objects, by forcing them
 *     to have a 3D center of mass and an optional time
 *     stamp as well as an unique identifier. All those data
 *     are managed through data properties in a way that the ontology
 *     is able to infer spatial properties between objects through SWRL rules.
 *
 * <div style="text-align:center;"><small>
 * <b>File</b>:        it.emarolab.scene_identification_tagging.realObject.GeometricPrimitive <br>
 * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
 * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
 * <b>affiliation</b>: EMAROLab, DIBRIS, University of Genoa. <br>
 * <b>date</b>:        05/06/17 <br>
 * </small></div>
 */
public class GeometricPrimitive
        extends SpatialIndividualDescriptor
        implements SITBase{

    /**
     * The unique identifier to be used for individual name.
     * This field is {@code static} but it can have duplicate in case
     * in which the ontology is saved to file and system restarted.
     */
    protected static long currentCOUNT = 0;

    private Long time = System.currentTimeMillis();
    private boolean addTime = false;
    private boolean addId = false;

    private Float centerX = null;
    private Float centerY = null;
    private Float centerZ = null;
    private Long id = null;

    /**
     * Initialise and ontological object by have a name based on {@link #currentCOUNT}
     * and {@link INDIVIDUAL#PREFIX_PRIMITIVE}.
     * @param onto the {@link OWLReferences} ontology that will contain the object individual.
     */
    public GeometricPrimitive(OWLReferences onto) {
        super( INDIVIDUAL.PREFIX_PRIMITIVE + currentCOUNT, onto);
        initialiseProperty();
        currentCOUNT += 1;
    }
    /**
     * Initialise and ontological object by have a name based on {@link #currentCOUNT}
     * and {@link INDIVIDUAL#PREFIX_PRIMITIVE}.
     * @param ontoName the name of the {@link OWLReferences} ontology that will contain the object individual.
     */
    public GeometricPrimitive(String ontoName) {
        super(INDIVIDUAL.PREFIX_PRIMITIVE + currentCOUNT, ontoName);
        initialiseProperty();
        currentCOUNT += 1;
    }
    /**
     * Initialise an object in the ontology by fully describing its OWLOOP {@code Ground}.
     * @param instance the object individual.
     * @param onto the {@link OWLReferences} ontology that will contain the object individual.
     */
    public GeometricPrimitive(OWLNamedIndividual instance, OWLReferences onto) {
        super(instance, onto);
        initialiseProperty();
        currentCOUNT += 1;
    }
    /**
     * Initialise an object in the ontology by fully describing its OWLOOP {@code Ground}.
     * @param instanceName the name of the object individual.
     * @param onto the {@link OWLReferences} ontology that will contain the object individual.
     */
    public GeometricPrimitive(String instanceName, OWLReferences onto) {
        super(instanceName, onto);
        initialiseProperty();
        currentCOUNT += 1;
    }
    /**
     * Initialise an object in the ontology by fully describing its OWLOOP {@code Ground}.
     * @param instance the object individual.
     * @param ontoName the name of the {@link OWLReferences} ontology that will contain the object individual.
     */
    public GeometricPrimitive(OWLNamedIndividual instance, String ontoName) {
        super(instance, ontoName);
        initialiseProperty();
        currentCOUNT += 1;
    }
    /**
     * Initialise an object in the ontology by fully describing its OWLOOP {@code Ground}.
     * @param instanceName the name of the object individual.
     * @param ontoName the name of the {@link OWLReferences} ontology that will contain the object individual.
     */
    public GeometricPrimitive(String instanceName, String ontoName) {
        super(instanceName, ontoName);
        initialiseProperty();
        currentCOUNT += 1;
    }

    // common constructor initialise
    private void initialiseProperty(){
        addData( getPropertyCenterX(), true);
        addData( getPropertyCenterY(), true);
        addData( getPropertyCenterZ(), true);
        addData( getPropertyTime(), time, true);
        // add this to avoid error on bug in the SWRL
        addTypeIndividual( CLASS.PRIMITIVE);
    }

    /**
     * Return the name of data property used to map time stamps.
     * @return {@link DATA_PROPERTY#TIME}
     */
    public String getPropertyTime() {
        return DATA_PROPERTY.TIME;
    }

    /**
     * Return the name of data property used to map unique identifier.
     * @return {@link DATA_PROPERTY#ID}
     */
    public String getPropertyId() {
        return DATA_PROPERTY.ID;
    }

    /**
     * Return the name of data property used to map the {@code X}
     * coordinate of the center of mass.
     * @return {@link DATA_PROPERTY#CENTER_X}
     */
    public String getPropertyCenterX(){
        return DATA_PROPERTY.CENTER_X;
    }

    /**
     * Return the name of data property used to map the {@code Y}
     * coordinate of the center of mass.
     * @return {@link DATA_PROPERTY#CENTER_Y}
     */
    public String getPropertyCenterY(){
        return DATA_PROPERTY.CENTER_Y;
    }

    /**
     * Return the name of data property used to map the {@code Z}
     * coordinate of the center of mass.
     * @return {@link DATA_PROPERTY#CENTER_Z}
     */
    public String getPropertyCenterZ(){
        return DATA_PROPERTY.CENTER_Z;
    }

    /**
     * Return the time stamp, by default the construction time
     * in milliseconds.
     * @return the time stamp of the object.
     */
    public Long getTime() {
        return time;
    }

    /**
     * Set the time stamp associated to {@code this} object.
     * if {@link #isAddingTime()}, this data will be added to the
     * {@link SpatialIndividualDescriptor}.
     * @param time the time stamp of the object.
     */
    public void setTime(Long time) { // does not change object name
        this.time = this.time;
        if ( addTime)
            addData( getPropertyTime(), this.time, true);
    }

    /**
     * Return the unique identifier, by default it is {@code null}
     * @return the unique identifier of the object, if is has been set.
     */
    public Long getId() {
        return id;
    }

    /**
     * Set the unique identifier associated to {@code this} object.
     * if {@link #isAddingId()}, this data will be added to the
     * {@link SpatialIndividualDescriptor}.
     * @param id the unique identifier of the object.
     */
    public void setId(Long id) {
        this.id = id;
        if ( addId)
            addData( getPropertyId(), id, true);
    }

    /**
     * A shortcut for calling {@link #setCenterX(Float)},
     * {@link #setCenterY(Float)} and {@link #setCenterZ(Float)}.
     * @param x the {@code X} coordinate of the center of mass.
     * @param y the {@code Y} coordinate of the center of mass.
     * @param z the {@code Z} coordinate of the center of mass.
     */
    public void setCenter(Float x, Float y, Float z){
        setCenterX( x);
        setCenterY( y);
        setCenterZ( z);
    }

    /**
     * Returns the {@code X} coordinate of the
     * center of mass. Measurements should be in meters.
     * @return the {@code X} coordinate of the c.o.m.
     */
    public Float getCenterX() {
        return centerX;
    }

    /**
     * Set the {@code X} coordinate of the
     * center of mass. Measurements should be in meters.
     * This method automatically add the data to the
     * {@link SpatialIndividualDescriptor}.
     * @param x the {@code X} coordinate of the c.o.m to set.
     */
    public void setCenterX(Float x) {
        this.centerX = x;
        addData( getPropertyCenterX(), x, true);
    }

    /**
     * Returns the {@code Y} coordinate of the
     * center of mass. Measurements should be in meters.
     * @return the {@code Y} coordinate of the c.o.m.
     */
    public Float getCenterY() {
        return centerY;
    }

    /**
     * Set the {@code Y} coordinate of the
     * center of mass. Measurements should be in meters.
     * This method automatically add the data to the
     * {@link SpatialIndividualDescriptor}.
     * @param y the {@code Y} coordinate of the c.o.m to set.
     */
    public void setCenterY(Float y) {
        this.centerY = y;
        addData( getPropertyCenterY(), y, true);
    }

    /**
     * Returns the {@code Z} coordinate of the
     * center of mass. Measurements should be in meters.
     * @return the {@code Z} coordinate of the c.o.m.
     */
    public Float getCenterZ() {
        return centerZ;
    }

    /**
     * Set the {@code Z} coordinate of the
     * center of mass. Measurements should be in meters.
     * This method automatically add the data to the
     * {@link SpatialIndividualDescriptor}.
     * @param z the {@code Z} coordinate of the c.o.m to set.
     */
    public void setCenterZ(Float z) {
        this.centerZ = z;
        addData( getPropertyCenterZ(), z, true);
    }

    /**
     * Enhance the standard OWLOOP read semantic by
     * explicitly set the value of the center of mass and,
     * eventually the time stamp and unique identifier.
     * @return the changes done during reading.
     */
    @Override
    public List<MappingIntent> readSemantic() {
        List<MappingIntent> r = super.readSemantic();
        centerX = getLiteral( getPropertyCenterX()).parseFloat();
        centerY = getLiteral( getPropertyCenterY()).parseFloat();
        centerZ = getLiteral( getPropertyCenterZ()).parseFloat();
        if( addId)
            id = Long.valueOf( getLiteral( getPropertyId()).getLiteral());
        if( addTime)
            time = Long.valueOf( getLiteral( getPropertyTime()).getLiteral());
        return r;
    }

    /**
     * An adding time enable/disable flag.
     * @return {@code true} if the time stamp is synchronised w.r.t. the ontology.
     * {@code false} otherwise.
     */
    public boolean isAddingTime() {
        return addTime;
    }

    /**
     * An adding time enable/disable flag for synchronising the
     * time stamp w.r.t. the ontology.
     * @param flag {@code true} to enable synchronisation, {@code false} to disable.
     */
    public void shouldAddTime(boolean flag) {
        addTime = flag;
    }

    /**
     * An adding unique identifier enable/disable flag.
     * @return {@code true} if the time stamp is synchronised w.r.t. the ontology.
     * {@code false} otherwise.
     */
    public boolean isAddingId() {
        return addTime;
    }

    /**
     * An adding time enable/disable flag for synchronising the
     * unique identifier w.r.t. the ontology.
     * @param flag {@code true} to enable synchronisation, {@code false} to disable.
     */
    public void shouldAddId(boolean flag) {
        addId = flag;
    }
}
