package it.emarolab.scene_identification_tracking.semanticSceneLibrary.objects.aMOR;

import it.emarolab.scene_identification_tracking.semanticSceneLibrary.Semantics;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.aMORDescriptor.MOR3DArray;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.aMORDescriptor.MORSpatialDescriptor;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.objects.ObjectSemantics;

/**
 * The java/semantic mapper for an Orientable objects, based on aMOR OWL references.
 * <p>
 *     It implements a two way mapping interface between this java structure and a semantic individual.
 *     It extends {@link MORPrimitive} so, it apply its inherited part of the semantic description of an object.
 *     Nevertheless, it adds also the definition of the direction of the principal axis of this object.
 *     Such a procedure is still based on OWL data properties that reflect the fields of {@link ObjectSemantics.Orientable},
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
 * @see ObjectSemantics.Orientable
 */
public class MOROrientable extends MORPrimitive implements ObjectSemantics.Orientable<MORSpatialDescriptor.MORSimpleDescriptor> {

    public static final String INITIAL_ORIENTABLE_TYPE = "Orientable";

    /** the default value for the OWL data property name of the X component of the axis. */
    private static final String AXIS_DIRECTION_X_DATA_PROPERTY_NAME = "has-geometric_axisX";
    /** the default value for the OWL data property name of the Y component of the axis. */
    private static final String AXIS_DIRECTION_Y_DATA_PROPERTY_NAME = "has-geometric_axisY";
    /** the default value for the OWL data property name of the Z component of the axis. */
    private static final String AXIS_DIRECTION_Z_DATA_PROPERTY_NAME = "has-geometric_axisZ";

    // internal fields for Semantic.Orientable interface
    private MOR3DArray axis;

    /**
     * Cloning constructor, create a new object as a clone of the input parameter.
     * @param orientable the object to clone.
     */
    public MOROrientable( MOROrientable orientable){
        super( orientable);
        this.axis = new MOR3DArray( orientable.axis);
    }
    /**
     * Construct this class with all invalidate fields. For instance,
     * initially {@link #readSemantics()} should be called.<br>
     * It is based on {@link MORPrimitive#MORPrimitive(String, long)}
     * and the axis is created as {@link MOR3DArray#MOR3DArray()}, where its
     * property names is set, for X, Y and Z, as; {@link #AXIS_DIRECTION_X_DATA_PROPERTY_NAME},
     * {@link #AXIS_DIRECTION_Y_DATA_PROPERTY_NAME}, and {@link #AXIS_DIRECTION_Z_DATA_PROPERTY_NAME} respectively.
     * @param ontologyName the name to an instantiated {@link it.emarolab.amor.owlInterface.OWLReferences}
     *                     that will describe this orientable object semantically.
     * @param id the unique identifier of this object.
     */
    public MOROrientable( String ontologyName, long id){
        super( ontologyName, id);
        initialise( null);
    }
    /**
     * Construct this class by specifying all its fields. For instance,
     * initially {@link #writeSemantics()} should be called.<br>
     * It is based on {@link MORPrimitive#MORPrimitive(String, long, MOR3DArray)},
     * where the input axis may already contains some property names
     * (see {@link MOR3DArray#hasXproperty()}, {@link MOR3DArray#hasYproperty()}
     * and {@link MOR3DArray#hasZproperty()}). If not, those are set as; {@link #AXIS_DIRECTION_X_DATA_PROPERTY_NAME},
     * {@link #AXIS_DIRECTION_Y_DATA_PROPERTY_NAME}, and {@link #AXIS_DIRECTION_Z_DATA_PROPERTY_NAME}.
     * @param ontologyName the name to an instantiated {@link it.emarolab.amor.owlInterface.OWLReferences}
     *                     that will describe this orientable object semantically.
     * @param id the unique identifier of this object.
     * @param centroid the coordinates of the center of mass of this object.
     * @param axis the value of a vector aligned to the principal axis of this object.
     */
    public MOROrientable( String ontologyName, long id, MOR3DArray centroid, MOR3DArray axis){
        super( ontologyName, id, centroid);
        initialise( axis);
    }
    /**
     * Construct this class with all invalidate fields. For instance,
     * initially {@link #readSemantics()} should be called.<br>
     * It is based on {@link MORPrimitive#MORPrimitive(MORSpatialDescriptor.MORSimpleDescriptor, long)}
     * and the axis is created as {@link MOR3DArray#MOR3DArray()}, where its
     * property names is set, for X, Y and Z, as; {@link #AXIS_DIRECTION_X_DATA_PROPERTY_NAME},
     * {@link #AXIS_DIRECTION_Y_DATA_PROPERTY_NAME}, and {@link #AXIS_DIRECTION_Z_DATA_PROPERTY_NAME} respectively.
     * @param semantic the ontology interface for semantically map this object.
     * @param id the unique identifier of this object.
     */
    public MOROrientable(MORSpatialDescriptor.MORSimpleDescriptor semantic, long id) {
        super( semantic, id);
        initialise( null);
    }
    /**
     * Construct this class by specifying all its fields. For instance,
     * initially {@link #writeSemantics()} should be called.<br>
     * It is based on {@link MORPrimitive#MORPrimitive(MORSpatialDescriptor.MORSimpleDescriptor, long, MOR3DArray)},
     * where the input axis may already contains some property names
     * (see {@link MOR3DArray#hasXproperty()}, {@link MOR3DArray#hasYproperty()}
     * and {@link MOR3DArray#hasZproperty()}). If not, those are set as; {@link #AXIS_DIRECTION_X_DATA_PROPERTY_NAME},
     * {@link #AXIS_DIRECTION_Y_DATA_PROPERTY_NAME}, and {@link #AXIS_DIRECTION_Z_DATA_PROPERTY_NAME}.
     * @param semantic the ontology interface for semantically map this object.
     * @param id the unique identifier of this object.
     * @param centroid the coordinates of the center of mass of this object.
     * @param axis the components of a vector aligned to the principal axis of this object.
     */
    public MOROrientable(MORSpatialDescriptor.MORSimpleDescriptor semantic, long id, MOR3DArray centroid, MOR3DArray axis) {
        super( semantic, id, centroid);
        initialise( axis);
    }
    // common constructor
    public void initialise( MOR3DArray axis){
        if( axis == null)
            axis = new MOR3DArray();
        this.axis = initialiseAxis( axis);
    }
    // called on CONSTRUCTORS: set default centroid properties if not already set by an eternal user
    public MOR3DArray initialiseAxis(MOR3DArray axis){ // set to default if not already setted
        if( ! axis.hasXproperty())
            axis.setXproperty( AXIS_DIRECTION_X_DATA_PROPERTY_NAME, getSemantics());
        if( ! axis.hasYproperty())
            axis.setYproperty( AXIS_DIRECTION_Y_DATA_PROPERTY_NAME, getSemantics());
        if( ! axis.hasZproperty())
            axis.setZproperty( AXIS_DIRECTION_Z_DATA_PROPERTY_NAME, getSemantics());
        return axis;
    }
    @Override
    protected void initialiseTypes() {
        super.initialiseTypes();
        getSemantics().addType(INITIAL_ORIENTABLE_TYPE);
    }

    /** @param axis the vector aligned to the principal axis of this object to set. */
    public void setAxis(MOR3DArray axis) {
        if( axis == null)
            logError( "cannot set null axis direction to a " + this.getClass().getSimpleName());
        else this.axis = axis;
    }
    @Override
    public MOR3DArray getAxis() {
        return axis;
    }

    /**
     * It calls {@link ObjectSemantics.Primitive#average(ObjectSemantics.Primitive)}
     * and than computes the average between the direction
     * of the normal to the object surface
     * of this class and the input parameter.
     * @param object the coefficients object description to be
     *                  averaged with this structure.
     * @return <code>false</code> if an error occurs.
     * <code>True</code> otherwise.
     */
    @Override
    public boolean average( ObjectSemantics.Primitive object) {
        if( ! super.average( object))
            return false;

        if( object instanceof ObjectSemantics.Orientable) {
            ObjectSemantics.Orientable orientable = (ObjectSemantics.Orientable) object;
            return this.getAxis().average( orientable.getAxis());
        }
        return false;
    }

    /**
     * Synchronise the java representation of this Orientable object with the ontology
     * and return the process state.
     * <p>
     *     It calls {@link MORPrimitive#writeSemantics()} and than, if the state
     *     of the operation {@link MappingState#isOK()}, it uses
     *     {@link MOR3DArray#writeSemantics(MORSpatialDescriptor.MORSimpleDescriptor)}
     *     to write the axis described by this class.
     *     Those writing process generates writing states, that are merged in a
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

        log( "[" + getID() + "] Orientable semantic writing ...");

        WritingState axisResult = getAxis().writeSemantics( getSemantics());
        if( ! axisResult.isOK())
            return axisResult;

        return superResult.merge( axisResult);
    }
    /**
     * Synchronise the ontology representation of an Orientable object with this java structure
     * and return the process state.
     * <p>
     *     It calls {@link MORPrimitive#readSemantics()} and than, if the state
     *     of the operation {@link MappingState#isOK()}, it uses
     *     {@link MOR3DArray#readSemantics(MORSpatialDescriptor.MORSimpleDescriptor)}
     *     to write the axis described by this class.
     *     Those reading process generate more reading states, that are merged in a
     *     single outcome based on: {@link Semantics.ReadingState#merge(ReadingState)}.
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

        log( "[" + getID() + "] Orientable semantic reading ...");

        ReadingState axisResult = getAxis().readSemantics(getSemantics());
        if( ! axisResult.isOK())
            return axisResult;

        return superResult.merge( axisResult);
    }

    @Override
    public String toString() {
        return super.toString() + "(axis=" + axis + ")";
    }
}
