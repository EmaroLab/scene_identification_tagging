package it.emarolab.scene_identification_tracking.semanticSceneLibrary.objects.aMOR;

import it.emarolab.scene_identification_tracking.semanticSceneLibrary.Semantics;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.aMORDescriptor.MOR3DArray;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.aMORDescriptor.MORSpatialDescriptor;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.objects.ObjectSemantics;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLLiteral;

/**
 * The java/semantic mapper for a Cone, based on aMOR OWL references.
 * <p>
 *     It implements a two way mapping interface between this java structure and a semantic individual.
 *     It extends {@link MOROrientable} so, it apply its inherited part of the semantic description of an object.
 *     Nevertheless, it adds also the definition of the radius, height and the coordinate of the apex of a cone.
 *     Such a procedure is still based on OWL data properties that reflect the fields of {@link ObjectSemantics.Cone},
 *     where the ontology interface is based on the {@link MORSpatialDescriptor.MORSimpleDescriptor} class.<br>
 *
 * <div style="text-align:center;"><small>
 * <b>File</b>:       it.emarolab.scene_identification_tracking.semanticSceneLibrary.Semantics <br>
 * <b>Licence</b>:    GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
 * <b>Author</b>:     Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
 * <b>affiliation</b>: DIBRIS, EMAROLab, University of Genoa. <br>
 * <b>date</b>:       06/01/2017 <br>
 * </small></div>
 *
 * @see Semantics
 * @see IndividualDescriptor.DataDescriptor
 * @see ObjectSemantics.Cone
 */
public class MORCone extends MOROrientable implements ObjectSemantics.Cone<MORSpatialDescriptor.MORSimpleDescriptor> {

    public static final String INITIAL_CONE_TYPE = "Cone";

    /** the default value for the OWL data property name of the X component of the apex. */
    public static final String APEX_COORDINATE_X_DATA_PROPERTY_NAME = "has-cone_apexX";
    /** the default value for the OWL data property name of the Z component of the apex. */
    public static final String APEX_COORDINATE_Y_DATA_PROPERTY_NAME = "has-cone_apexY";
    /** the default value for the OWL data property name of the Y component of the apex. */
    public static final String APEX_COORDINATE_Z_DATA_PROPERTY_NAME = "has-cone_apexZ";

    /** the default value for the OWL data property name representing the radius of the cone. */
    public static final String RADIUS_DATA_PROPERTY_NAME = "has-cone_radius";
    /** the default value for the OWL data property name representing the height of the cone. */
    public static final String HEIGHT_DATA_PROPERTY_NAME = "has-cone_height";

    // internal fields for Semantic.Cone interface
    private Double radius, height;
    private MOR3DArray apex;
    // internal fields for semantic mapping of a Primitive object
    private String radiusPropertyName = RADIUS_DATA_PROPERTY_NAME;
    private String heightPropertyName = HEIGHT_DATA_PROPERTY_NAME;

    /**
     * Cloning constructor, create a new object as a clone of the input parameter.
     * @param cone the object to clone.
     */
    public MORCone( MORCone cone){
        super( cone);
        this.radius = cone.radius;
        this.height = cone.height;
        this.apex = cone.apex;
        this.radiusPropertyName = cone.radiusPropertyName;
        this.heightPropertyName = cone.heightPropertyName;
    }
    /**
     * Construct this class with all invalidate fields. For instance,
     * initially {@link #readSemantics()} should be called.<br>
     * It is based on {@link MOROrientable#MOROrientable(String, long)}
     * and the apex is created as {@link MOR3DArray#MOR3DArray()}, where its
     * property names is set, for X, Y and Z, as; {@link #APEX_COORDINATE_X_DATA_PROPERTY_NAME},
     * {@link #APEX_COORDINATE_Y_DATA_PROPERTY_NAME}, and {@link #APEX_COORDINATE_Z_DATA_PROPERTY_NAME} respectively.
     * Finally, the radius and height of the cone is set to <code>null</code>.
     * @param ontologyName the name to an instantiated {@link it.emarolab.amor.owlInterface.OWLReferences}
     *                     that will describe this cone semantically.
     * @param id the unique identifier of this cone.
     */
    public MORCone( String ontologyName, long id){
        super( ontologyName, id);
        initialise( null, null, null);
    }
    /**
     * Construct this class by specifying all its fields. For instance,
     * initially {@link #writeSemantics()} should be called.<br>
     * It is based on {@link MOROrientable#MOROrientable(String, long, MOR3DArray, MOR3DArray)},
     * where the input apex may already contains some property names
     * (see {@link MOR3DArray#hasXproperty()}, {@link MOR3DArray#hasYproperty()}
     * and {@link MOR3DArray#hasZproperty()}). If not, those are set as; {@link #APEX_COORDINATE_X_DATA_PROPERTY_NAME},
     * {@link #APEX_COORDINATE_Y_DATA_PROPERTY_NAME}, and {@link #APEX_COORDINATE_Z_DATA_PROPERTY_NAME}.
     * @param ontologyName the name to an instantiated {@link it.emarolab.amor.owlInterface.OWLReferences}
     *                     that will describe this cone semantically.
     * @param id the unique identifier of this cone.
     * @param centroid the coordinates of the center of mass of this cone.
     * @param axis the value of a vector aligned to the principal axis of this cone.
     * @param radius the radius of this cone.
     * @param height the height of this cone.
     * @param apex the coordinate of the apex of this cone.
     */
    public MORCone( String ontologyName, long id, MOR3DArray centroid, MOR3DArray axis,
                    Double radius, Double height, MOR3DArray apex){
        super( ontologyName, id, centroid, axis);
        initialise( radius, height, apex);
    }
    /**
     * Construct this class with all invalidate fields. For instance,
     * initially {@link #readSemantics()} should be called.<br>
     * It is based on {@link MOROrientable#MOROrientable(MORSpatialDescriptor.MORSimpleDescriptor, long)}
     * and the apex is created as {@link MOR3DArray#MOR3DArray()}, where its
     * property names is set, for X, Y and Z, as; {@link #APEX_COORDINATE_X_DATA_PROPERTY_NAME},
     * {@link #APEX_COORDINATE_Y_DATA_PROPERTY_NAME}, and {@link #APEX_COORDINATE_Z_DATA_PROPERTY_NAME} respectively.
     * Finally, the radius and height of the cone is set to <code>null</code>.
     * @param semantic the ontology interface for semantically map this cone.
     * @param id the unique identifier of this cone.
     */
    public MORCone(MORSpatialDescriptor.MORSimpleDescriptor semantic, long id) {
        super(semantic, id);
        initialise( null, null, null);
    }
    /**
     * Construct this class by specifying all its fields. For instance,
     * initially {@link #writeSemantics()} should be called.<br>
     * It is based on {@link MOROrientable#MOROrientable(MORSpatialDescriptor.MORSimpleDescriptor, long, MOR3DArray, MOR3DArray)},
     * where the input apex may already contains some property names
     * (see {@link MOR3DArray#hasXproperty()}, {@link MOR3DArray#hasYproperty()}
     * and {@link MOR3DArray#hasZproperty()}). If not, those are set as; {@link #APEX_COORDINATE_X_DATA_PROPERTY_NAME},
     * {@link #APEX_COORDINATE_Y_DATA_PROPERTY_NAME}, and {@link #APEX_COORDINATE_Z_DATA_PROPERTY_NAME}.
     * @param semantic the ontology interface for semantically map this cone.
     * @param id the unique identifier of this cone.
     * @param centroid the coordinates of the center of mass of this cone.
     * @param axis the value of a vector aligned to the principal axis of this cone.
     * @param radius the radius of this cone.
     * @param height the height of this cone.
     * @param apex the coordinate of the apex of this cone.
     */
    public MORCone(MORSpatialDescriptor.MORSimpleDescriptor semantic, long id, MOR3DArray centroid, MOR3DArray axis
            , Double radius, Double height, MOR3DArray apex) {
        super(semantic, id, centroid, axis);
        initialise( radius, height, apex);
    }
    // common constructor
    private void initialise( Double radius, Double height, MOR3DArray apex){
        this.radius = radius;
        this.height = height;
        if( apex == null)
            apex = new MOR3DArray();
        this.apex = initialiseApex( apex);
    }
    // called on CONSTRUCTORS: set default centroid properties if not already set by an eternal user
    private MOR3DArray initialiseApex( MOR3DArray axis){
        if( ! axis.hasXproperty())
            axis.setXproperty( APEX_COORDINATE_X_DATA_PROPERTY_NAME, getSemantics());
        if( ! axis.hasYproperty())
            axis.setYproperty( APEX_COORDINATE_Y_DATA_PROPERTY_NAME, getSemantics());
        if( ! axis.hasZproperty())
            axis.setZproperty( APEX_COORDINATE_Z_DATA_PROPERTY_NAME, getSemantics());
        return axis;
    }
    @Override
    protected void initialiseTypes() {
        super.initialiseTypes();
        getSemantics().addType(INITIAL_CONE_TYPE);
    }

    @Override
    public void setRadius(Double radius) {
        this.radius = radius;
    }
    @Override
    public void setHeight(Double height) {
        this.height = height;
    }
    /** @param apex the coordinates of the apex to set. */
    public void setApex(MOR3DArray apex) {
        if( apex == null)
            logError( "cannot set null apex direction to a " + this.getClass().getSimpleName());
        else this.apex = apex;;
    }

    @Override
    public Double getRadius() {
        return radius;
    }
    @Override
    public Double getHeight() {
        return height;
    }
    @Override
    public MOR3DArray getApex() {
        return apex;
    }

    /** @return the OWL literal representation of {@link #getRadius()}. */
    public OWLLiteral getRadiusLiteral(){
        return getSemantics().getOntology().getOWLLiteral( getRadius());
    }
    /** @return the OWL literal representation of {@link #getHeight()}. */
    public OWLLiteral getHeightLiteral(){
        return getSemantics().getOntology().getOWLLiteral( getHeight());
    }

    /**
     * @param radiusPropertyName the name, to set, of the OWL data property used for semantically
     *                       map the radius of this cone.
     */
    public void setRadiusPropertyName(String radiusPropertyName) {
        this.radiusPropertyName = radiusPropertyName;
    }
    /**
     * @param heightPropertyName the name, to set, of the OWL data property used for semantically
     *                       map the height of this cone.
     */
    public void setHeightPropertyName(String heightPropertyName) {
        this.heightPropertyName = heightPropertyName;
    }

    /** @return the name of the OWL data property used for semantically describe the radius of this cone. */
    public String getRadiusPropertyName() {
        return radiusPropertyName;
    }
    /** @return the name of the OWL data property used for semantically describe the height of this cone. */
    public String getHeightPropertyName() {
        return heightPropertyName;
    }

    /**
     * @return the OWL data property used for semantically describe the radius of this cone.
     * Based on {@link #getRadiusPropertyName()}.
     */
    public OWLDataProperty getRadiusProperty(){
        return getSemantics().getOntology().getOWLDataProperty( getRadiusPropertyName());
    }
    /**
     * @return the OWL data property used for semantically describe the height of this cone.
     * Based on {@link #getHeightPropertyName()}.
     */
    public OWLDataProperty getHeightProperty(){
        return getSemantics().getOntology().getOWLDataProperty( getHeightPropertyName());
    }

    /**
     * It calls {@link ObjectSemantics.Orientable#average(ObjectSemantics.Primitive)}
     * and than computes the average between the: radius,
     * height and apex of this class and the input parameter.
     * @param object the coefficients object description to be
     *                  averaged with this structure.
     * @return <code>false</code> if an error occurs.
     * <code>True</code> otherwise.
     */
    @Override
    public boolean average( ObjectSemantics.Primitive object) {
        if( ! super.average( object))
            return false;

        if( object instanceof ObjectSemantics.Cone) {
            ObjectSemantics.Cone cone = (ObjectSemantics.Cone) object;
            if (this.getRadius() == null | cone.getRadius() == null)
                return false;
            setRadius( ( this.getRadius() + cone.getRadius()) / 2);
            if (this.getHeight() == null | cone.getHeight() == null)
                return false;
            setHeight( ( this.getHeight() + cone.getHeight()) / 2);
            return this.getApex().average( cone.getAxis());
        }
        return false;
    }

    /**
     * Synchronise the java representation of this Cone with the ontology
     * and return the process state.
     * <p>
     *     It calls {@link MOROrientable#writeSemantics()} and than, if the state
     *     of the operation {@link MappingState#isOK()}, it uses
     *     {@link MORSpatialDescriptor.MORSimpleDescriptor#writeLiteral(String, OWLDataProperty, OWLLiteral)}
     *     to write the radius, and height, described by this class (called with parameter as:
     *     {@link #getRadiusProperty()}, {@link #getRadiusLiteral()} and
     *     {@link #getHeightProperty()}, {@link #getHeightLiteral()} respectively).
     *     Finally, if the state of the operation is still {@link MappingState#isOK()},
     *     it uses {@link MOR3DArray#writeSemantics(MORSpatialDescriptor.MORSimpleDescriptor)} to write the apex of this cone.
     *     Those writing process generate more writing states, that are merged in a
     *     single outcome based on: {@link Semantics.WritingState#merge(WritingState)}.
     * @return the state of this writing process.
     * @see ObjectSemantics.Primitive#writeSemantics()
     * @see MOR3DArray#writeSemantics(MORSpatialDescriptor.MORSimpleDescriptor)
     * @see WritingState
     */
    @Override
    public WritingState writeSemantics() {
        WritingState superResult = super.writeSemantics();
        if( ! superResult.isOK())
            return superResult;

        log( "[" + getID() + "] Cone semantic writing ...");

        WritingState radiusResult = getSemantics().writeLiteral("radius",
                getRadiusProperty(), getRadiusLiteral());
        if( ! radiusResult.isOK())
            return radiusResult;
        else superResult = superResult.merge( radiusResult);

        WritingState heightResult = getSemantics().writeLiteral("height",
                getHeightProperty(), getHeightLiteral());
        if( ! heightResult.isOK())
            return heightResult;
        else superResult = superResult.merge( heightResult);

        WritingState apexResult = apex.writeSemantics( getSemantics());
        if( ! apexResult.isOK())
            return apexResult;
        else superResult = superResult.merge( apexResult);

        return superResult;
    }
    /**
     * Synchronise the ontology representation of a Cone object with this java structure
     * and return the process state.
     * <p>
     *     It calls {@link MOROrientable#readSemantics()} and than, if the state
     *     of the operation {@link MappingState#isOK()}, it uses
     *     {@link MORSpatialDescriptor.MORSimpleDescriptor#readLiteral(String, OWLDataProperty, OWLLiteral)}
     *     to read the radius, and height, from the ontology (called with parameter as:
     *     {@link #getRadiusProperty()}, {@link #getRadiusLiteral()} and
     *     {@link #getHeightProperty()}, {@link #getHeightLiteral()} respectively).
     *     Finally, if the state of the operation is still {@link MappingState#isOK()},
     *     it uses {@link MOR3DArray#readSemantics(MORSpatialDescriptor.MORSimpleDescriptor)} to red the apex of this cone.
     *     Those reading process generate more reading states, that are merged in a
     *     single outcome based on: {@link Semantics.WritingState#merge(WritingState)}.
     * @return the state of this reading process.
     * @see ObjectSemantics.Primitive#readSemantics()
     * @see MOR3DArray#readSemantics(MORSpatialDescriptor.MORSimpleDescriptor)
     * @see ReadingState
     */
    @Override
    public ReadingState readSemantics() {
        ReadingState superResult = super.readSemantics();
        if( ! superResult.isOK())
            return superResult;

        log( "[" + getID() + "] Cone semantic reading ...");

        MORSpatialDescriptor.MORSimpleDescriptor.DataReadingOutcome radiusOutcome = getSemantics().readLiteral( "radius",
                getRadiusProperty(), getRadiusLiteral());
        if( ! radiusOutcome.isOK())
            return radiusOutcome.getState();
        else if( radiusOutcome.isSuccess())
            setRadius( radiusOutcome.getDoubleReadBuffer());
        superResult = superResult.merge( radiusOutcome);

        MORSpatialDescriptor.MORSimpleDescriptor.DataReadingOutcome heightOutcome = getSemantics().readLiteral( "height",
                getHeightProperty(), getHeightLiteral());
        if( ! heightOutcome.isOK())
            return heightOutcome.getState();
        else if( heightOutcome.isSuccess())
            setRadius( heightOutcome.getDoubleReadBuffer());
        superResult = superResult.merge( heightOutcome);

        ReadingState apexResult = apex.readSemantics(getSemantics());
        if( ! apexResult.isOK())
            return apexResult;
        superResult = superResult.merge( apexResult);

        return superResult;
    }

    /**
     * Calls {@link #MORCone(MORCone)}.
     * @return a <code>new</code> copy of this object.
     */
    @Override
    public ObjectSemantics.Cone<MORSpatialDescriptor.MORSimpleDescriptor> copy() {
        return new MORCone( this);
    }

    @Override
    public String toString() {
        return super.toString() + "(radius=" + radius
                + ")(height=" + height
                + ")(apex=" + apex + ")";

    }

}
