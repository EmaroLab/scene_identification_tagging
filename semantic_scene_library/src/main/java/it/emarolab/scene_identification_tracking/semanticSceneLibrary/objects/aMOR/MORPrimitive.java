package it.emarolab.scene_identification_tracking.semanticSceneLibrary.objects.aMOR;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.amor.owlInterface.OWLReferencesInterface;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.Logger;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.Logger.SITBase;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.Semantics;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.aMORDescriptor.MOR3DArray;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.aMORDescriptor.MORSpatialDescriptor;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.objects.ObjectSemantics;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import java.util.HashSet;
import java.util.Set;

/**
 * The java/semantic mapper for a Primitive Object, based on aMOR OWL references.
 * <p>
 *     It implements a two way mapping interface between this java structure and a semantic individual.
 *     Such a procedure is based on OWL data properties that reflect the fields of {@link ObjectSemantics.Primitive},
 *     where the ontology interface is based on the {@link MORSpatialDescriptor.MORSimpleDescriptor} class.<br>
 *     In particular, this implementation manages an individual (which name is described in the {@link MORSpatialDescriptor.MORSimpleDescriptor})
 *     that has 5 OWL data properties. Three of them define the center of mass coordinates
 *     (see {@link MOR3DArray#writeSemantics(MORSpatialDescriptor.MORSimpleDescriptor)} and {@link MOR3DArray#readSemantics(MORSpatialDescriptor.MORSimpleDescriptor)}).
 *     While, the other two are optional and can be used to set semantically the object ID and creation time stamp
 *     (see {@link #shouldMapId(boolean)}, {@link #shouldMapTime(boolean)}
 *     and {@link #isMapingId()}, {@link #isMappingTime()}).
 *
 * <p>
 *     <b>REMARK</b>: see <a href="https://github.com/EmaroLab/multi_ontology_reference">here</a>
 *     for more about the aMOR library.
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
 * @see ObjectSemantics.Primitive
 * @see IndividualDescriptor.Array3D
 */
public class MORPrimitive extends SITBase.SITBase implements ObjectSemantics.Primitive<MORSpatialDescriptor.MORSimpleDescriptor> {

    public static final String INITIAL_PRIMITIVE_TYPE = "GeometricPrimitive";

    /** the default value for the OWL data property name of the X component of the centroid. */
    public static final String CENTROID_X_DATA_PROPERTY_NAME = "has-geometric_centerX"; // todo move them on descriptor
    /** the default value for the OWL data property name of the Y component of the centroid. */
    public static final String CENTROID_Y_DATA_PROPERTY_NAME = "has-geometric_centerY";
    /** the default value for the OWL data property name of the Z component of the centroid. */
    public static final String CENTROID_Z_DATA_PROPERTY_NAME = "has-geometric_centerZ";

    /** the default value for the OWL data property name to semantically describe the creation instant stamp. */
    public static final String CREATED_TIME_DATA_PROPERTY_NAME = "has_time"; // todo move them on semantics
    /** the default value for the OWL data property name to semantically describe the object id. */
    public static final String INSTANCE_ID_DATA_PROPERTY_NAME = "has_id";

    // flags to decide if semantically map also the 'time' and 'id' fields (optional)
    private boolean mapId = false, mapTime = false;

    // internal fields for Semantic.Primitive interface
    private long id, time;
    private MOR3DArray centroid;
    // internal fields for semantic mapping of a Primitive object
    private String idPropertyName = INSTANCE_ID_DATA_PROPERTY_NAME;
    private String timePropertyName = CREATED_TIME_DATA_PROPERTY_NAME;
    // contains individual name and ontology reference
    private MORSpatialDescriptor.MORSimpleDescriptor semantic;

    /**
     * Cloning constructor, create a new object as a clone of the input parameter.
     * @param primitive the object to clone.
     */
    public MORPrimitive( MORPrimitive primitive) {
        this.id = primitive.id;
        this.time = primitive.time;
        this.centroid = new MOR3DArray(primitive.centroid);
        this.idPropertyName = primitive.idPropertyName;
        this.timePropertyName = primitive.timePropertyName;
        this.semantic = new MORSpatialDescriptor.MORSimpleDescriptor(primitive.semantic);
    }
    /**
     * Construct this class with all invalidate fields. For instance,
     * initially {@link #readSemantics()} should be called.<br>
     * It sets a {@link #createDefaultSemantics(String)} for this object based
     * on the given input name. Also, it constructs the centroid by using
     * {@link MOR3DArray#MOR3DArray()} and its property names ise set, for X, Y and Z, to;
     * {@link #CENTROID_X_DATA_PROPERTY_NAME}, {@link #CENTROID_Y_DATA_PROPERTY_NAME},
     * and {@link #CENTROID_Z_DATA_PROPERTY_NAME} respectively.
     * @param ontologyName the name to an instantiated {@link it.emarolab.amor.owlInterface.OWLReferences}
     *                     that will describe this object semantically.
     * @param id the unique identifier of this object.
     */
    public MORPrimitive( String ontologyName, long id){
        initialise( ontologyName, id, null);
    }
    /**
     * Construct this class by specifying all its fields. For instance,
     * initially {@link #writeSemantics()} should be called.<br>
     * It sets a {@link #createDefaultSemantics(String)} for this object based
     * on the given input name. Also, the input centroid may already contains some property names
     * (see {@link MOR3DArray#hasXproperty()}, {@link MOR3DArray#hasYproperty()}
     * and {@link MOR3DArray#hasZproperty()}). If not, those are set as; {@link #CENTROID_X_DATA_PROPERTY_NAME},
     * {@link #CENTROID_Y_DATA_PROPERTY_NAME}, and {@link #CENTROID_Z_DATA_PROPERTY_NAME}.
     * @param ontologyName the name to an instantiated {@link it.emarolab.amor.owlInterface.OWLReferences}
     *                     that will describe this object semantically.
     * @param id the unique identifier of this object.
     * @param centroid the coordinates of the center of mass of this object.
     */
    public MORPrimitive( String ontologyName, long id, MOR3DArray centroid){
        initialise( ontologyName, id, centroid);
    }
    /**
     * Construct this class with all invalidate fields. For instance,
     * initially {@link #readSemantics()} should be called.<br>
     * In this constructor, the centroid is created as {@link MOR3DArray#MOR3DArray()} and its
     * property names set, for X, Y and Z, as; {@link #CENTROID_X_DATA_PROPERTY_NAME},
     * {@link #CENTROID_Y_DATA_PROPERTY_NAME}, and {@link #CENTROID_Z_DATA_PROPERTY_NAME} respectively.
     * @param semantic the ontology interface for semantically map this object.
     * @param id the unique identifier of this object.
     */
    public MORPrimitive(MORSpatialDescriptor.MORSimpleDescriptor semantic, long id){
        initialise( semantic, id, null);
    }
    /**
     * Construct this class by specifying all its fields. For instance,
     * initially {@link #writeSemantics()} should be called.<br>
     * In this constructor, the input centroid may already contains some property names
     * (see {@link MOR3DArray#hasXproperty()}, {@link MOR3DArray#hasYproperty()}
     * and {@link MOR3DArray#hasZproperty()}). If not, those are set as; {@link #CENTROID_X_DATA_PROPERTY_NAME},
     * {@link #CENTROID_Y_DATA_PROPERTY_NAME}, and {@link #CENTROID_Z_DATA_PROPERTY_NAME}.
     * @param semantic the ontology interface for semantically map this object.
     * @param id the unique identifier of this object.
     * @param centroid the coordinates of the center of mass of this object.
     */
    public MORPrimitive(MORSpatialDescriptor.MORSimpleDescriptor semantic, long id, MOR3DArray centroid){
       initialise( semantic, id, centroid);
    }
    // common constructor (for semantic given from external user)
    private void initialise(MORSpatialDescriptor.MORSimpleDescriptor semantic, long id, MOR3DArray centroid){
        initialise( semantic, id, centroid, null);
    }
    // common constructor (for default semantic usage)
    private void initialise(String ontoName, long id, MOR3DArray centroid){
        initialise( null, id, centroid, ontoName);
    }
    // base common constructor (onto name used only if semantic is null)
    private void initialise(MORSpatialDescriptor.MORSimpleDescriptor semantic, long id, MOR3DArray centroid, String ontoName){
        this.id = id;
        if( semantic != null)
            this.semantic = semantic;//example: new MORDescriptor( String.valueOf( id), "ONTO_NAME"); // individual name == id;
        else {
            //logWarning( "null semantics specified for object: " + this  + "\n\t\t use default semantics? (" + (ONTO_NAME  != null) + ").");
            if( ontoName != null)
                this.semantic = createDefaultSemantics( ontoName);
            else logError( "cannot set semantics for: " + this);
        }
        this.time = System.currentTimeMillis();
        if( centroid == null)
            centroid = new MOR3DArray();
        this.centroid = initialiseCentroid( centroid);
        initialiseTypes();
    }
    protected void initialiseTypes() {
        getSemantics().addType(INITIAL_PRIMITIVE_TYPE);
    }

    // called on CONSTRUCTORS: set default centroid properties if not already set by an eternal user
    private MOR3DArray initialiseCentroid(MOR3DArray centroid){
        if( ! centroid.hasXproperty())
            centroid.setXproperty( CENTROID_X_DATA_PROPERTY_NAME, getSemantics());
        if( ! centroid.hasYproperty())
            centroid.setYproperty( CENTROID_Y_DATA_PROPERTY_NAME, getSemantics());
        if( ! centroid.hasZproperty())
            centroid.setZproperty( CENTROID_Z_DATA_PROPERTY_NAME, getSemantics());
        return centroid;
    }
    // called on constructors (needs id to be setted)
    /**
     * It generate a new MOR semantic for this object by generating
     * the name of the individual that will describe this object in an
     * ontology.<br>
     * This method is called on constructors: {@link #MORPrimitive(String, long)}
     * and {@link #MORPrimitive(String, long, MOR3DArray)}). Moreover,
     * its returning value is set to the semantic described by this class
     * (see: {@link #getSemantics()}) during this object construction.
     * @param ontologyName the name to an instantiated {@link it.emarolab.amor.owlInterface.OWLReferences}
     *                     that will describe this object semantically.
     * @return a new {@link MORSpatialDescriptor.MORSimpleDescriptor} for mapping thi object
     * to/from an OWLOntology.
     */
    protected MORSpatialDescriptor.MORSimpleDescriptor createDefaultSemantics(String ontologyName){
        return new MORSpatialDescriptor.MORSimpleDescriptor( getDefaultIndividualName(), ontologyName);
    }
    /**
     * It is called by {@link #createDefaultSemantics(String)} and return the name of the
     * semantic individual describing this class. <br>
     * Indeed, it set the name as the unique object identifier.
     * Namely, it returns: <code>String.valueOf( {@link #getID()})</code>
     * @return the default name of the OWL individual describing this object in the ontology.
     */
    protected String getDefaultIndividualName(){
        return String.valueOf( this.getID());
    }

    @Override
    public long getID() {
        return id;
    }
    @Override
    public long getTime() {
        return time;
    }

    /** @param centroid the coordinates of the center of mas to set. */
    public void setCentroid(MOR3DArray centroid) {
        if( centroid == null)
            logError( "cannot set null centroid to a " + this.getClass().getSimpleName());
        else this.centroid = centroid;
    }
    @Override
    public MOR3DArray getCentroid() {
        return centroid;
    }

    /**
     * @param idPropertyName the name, to set, of the OWL data property used for semantically
     *                       map the unique identifier of this object.
     */
    public void setIdPropertyName(String idPropertyName) {
        this.idPropertyName = idPropertyName;
    }
    /**
     * @return the name of the OWL data property used for semantically
     * describe the unique identifier of this object.
     */
    public String getIdPropertyName(){
        return idPropertyName;
    }
    /**
     * @return the OWL data property used for semantically describe the unique identifier of this object.
     * Based on {@link #getIdPropertyName()}.
     */
    public OWLDataProperty getIdProperty(){
        return semantic.getOntology().getOWLDataProperty( getIdPropertyName());
    }
    /** @return the OWL literal representation of {@link #getID()}. */
    public OWLLiteral getIdLiteral(){
        return semantic.getOntology().getOWLLiteral( getID());
    }
    /**
     * @param mapId the flag indicating if the unique identifier of this object
     *              should (or should not) be used during {@link #readSemantics()}
     *              and {@link #writeSemantics()} operations.<br>
     *              By default, this value is <code>false</code>.
     */
    public void shouldMapId(boolean mapId) {
        this.mapId = mapId;
    }
    /**
     * @return a flag indicating if the unique identifier of this object is being used during
     * {@link #readSemantics()} and {@link #writeSemantics()} operations.
     */
    public boolean isMapingId() {
        return mapId;
    }

    /**
     * @param timePropertyName the name, to set, of the OWL data property used for semantically
     *                       map the creation instant stamp of this object.
     */
    public void setTimePropertyName(String timePropertyName) {
        this.timePropertyName = timePropertyName;
    }
    /**
     * @return the name of the OWL data property used for semantically
     * describe the creation instant stamp of this object.
     */
    public String getTimePropertyName(){
        return timePropertyName;
    }
    /**
     * @return the OWL data property used for semantically describe the creation time stamp of this object.
     * Based on {@link #getTimePropertyName()}.
     */
    public OWLDataProperty getTimeProperty(){
        return semantic.getOntology().getOWLDataProperty( getTimePropertyName());
    }
    /** @return the OWL literal representation of {@link #getTime()}. */
    public OWLLiteral getTimeLiteral(){
        return semantic.getOntology().getOWLLiteral( getTime());
    }
    /**
     * @param mapTime the flag indicating if the creation instant stamp of this object
     *              should (or should not) be used during {@link #readSemantics()}
     *              and {@link #writeSemantics()} operations.<br>
     *              By default, this value is <code>false</code>.
     */
    public void shouldMapTime(boolean mapTime) {
        this.mapTime = mapTime;
    }
    /**
     * @return a flag indicating if the creation instant stamp of this object is being used during
     * {@link #readSemantics()} and {@link #writeSemantics()} operations.
     */
    public boolean isMappingTime() {
        return mapTime;
    }

    /**
     * @return the object that is in charge to read and write in the ontology.
     * It must be given not null in the constructor. For aMOR library,
     * it mainly cares about the individual name, describing the object, and the
     * reference name, describing the ontology.
     * @see ObjectSemantics.Primitive#getSemantics()
     */
    @Override
    public MORSpatialDescriptor.MORSimpleDescriptor getSemantics() {
        return semantic;
    }

    /**
     * In particular, it checks if the input object is of the same
     * shape of the this representation. Than, it replace the
     * description of the centroid as the arithmetic average between
     * the input parameter and this object.
     * @param object the coefficients object description to be
     *                  averaged with this structure.
     * @return <code>false</code> if an error occurs.
     * <code>True</code> otherwise.
     */
    @Override
    public boolean average(ObjectSemantics.Primitive object) {
        if( ! this.getClass().equals( object.getClass()))
            return false;
        return this.getCentroid().average( object.getCentroid());
    }

    /**
     * Calls {@link #MORPrimitive( MORPrimitive)}.
     * @return a <code>new</code> copy of this object.
     */
    @Override
    public ObjectSemantics.Primitive<MORSpatialDescriptor.MORSimpleDescriptor> copy() {
        return new MORPrimitive( this);
    }

    /**
     * Synchronise the java representation of this Primitive object with the ontology
     * and return the process state.
     * <p>
     *      It is based on {@link MOR3DArray#writeSemantics(MORSpatialDescriptor.MORSimpleDescriptor)} for
     *      semantically map the coordinate of the center of mass in the ontology.
     *      Also, if this option has been enabled, it writes the unique identifier ({@link #getID()})
     *      and creation time stamp ({@link #getTime()}) based on: {@link #getIdPropertyName()}
     *      and {@link #getTimePropertyName()}. The mapping of those latter properties of
     *      the objects are based on: {@link MORSpatialDescriptor.MORSimpleDescriptor#writeLiteral(String, OWLDataProperty, OWLLiteral)}.
     *      Those writing process generate more writing states, that are merged in a
     *      single outcome based on: {@link Semantics.WritingState#merge(WritingState)}.
     * @return the state of this writing process.
     * @see ObjectSemantics.Primitive#writeSemantics()
     * @see MOR3DArray#writeSemantics(MORSpatialDescriptor.MORSimpleDescriptor)
     * @see WritingState
     */
    @Override
    public WritingState writeSemantics() {
        log( "[" + getID() + "] Primitive semantic writing ...");

        WritingState state = getSemantics().writeType("classes");

        WritingState centroidResult = centroid.writeSemantics(getSemantics());
        state = state.merge( centroidResult);
        if( mapId){
            WritingState idResult = getSemantics().writeLiteral("id",
                    getIdProperty(), getIdLiteral());
            state = state.merge( idResult);
        }
        if( mapTime){
            WritingState timeResult = getSemantics().writeLiteral("time",
                    getTimeProperty(), getTimeLiteral());
            state = state.merge( timeResult);
        }
        return state;
    }

    /**
     * Synchronise the ontology representation of a Primitive object with this java structure
     * and return the process state.
     * <p>
     *      It is based on {@link MOR3DArray#readSemantics(MORSpatialDescriptor.MORSimpleDescriptor)} for
     *      semantically map the coordinate of the center of mass from the ontology.
     *      Also, if this option has been enabled, it reads the unique identifier ({@link #getID()})
     *      and creation time stamp ({@link #getTime()}) based on: {@link #getIdPropertyName()}
     *      and {@link #getTimePropertyName()}. The mapping of those latter properties of
     *      the objects are based on: {@link MORSpatialDescriptor.MORSimpleDescriptor#readLiteral(String, OWLDataProperty, OWLLiteral)}.
     *      Those reading process generate more reading states, that are merged in a
     *      single outcome based on: {@link Semantics.ReadingState#merge(ReadingState)}.
     * @return the state of this reading process.
     * @see ObjectSemantics.Primitive#readSemantics()
     * @see MOR3DArray#readSemantics(MORSpatialDescriptor.MORSimpleDescriptor)
     * @see ReadingState
     */
    @Override
    public ReadingState readSemantics() {
        log( "[" + getID() + "] Primitive semantic reading ...");

        ReadingState state = centroid.readSemantics(getSemantics());
        if( mapId)
            state = state.merge( readId());
        if( mapTime)
            state = state.merge( readTime());
        if ( state.isOK())
            state = state.merge( semantic.readType("class"));
        return state;
    }
    private MORSpatialDescriptor.MORSimpleDescriptor.DataReadingOutcome readId(){
        MORSpatialDescriptor.MORSimpleDescriptor.DataReadingOutcome idOutcome = getSemantics().readLiteral( "id",
                getIdProperty(), getIdLiteral());
        if( ! idOutcome.isOK())
            return idOutcome;
        else if( idOutcome.isSuccess())
            id = idOutcome.getLongReadBuffer();
        return idOutcome;
    }
    private MORSpatialDescriptor.MORSimpleDescriptor.DataReadingOutcome readTime(){
        MORSpatialDescriptor.MORSimpleDescriptor.DataReadingOutcome timeOutcome = getSemantics().readLiteral( "time",
                getTimeProperty(), getTimeLiteral());
        if( ! timeOutcome.isOK())
            return timeOutcome;
        else if( timeOutcome.isSuccess())
            time = timeOutcome.getLongReadBuffer();
        return timeOutcome;
    }

    // todo: change static metod to work with OWL top entity
    public static Set<MORPrimitive> loadFromClasses(String ontoName){
        Set< MORPrimitive> allObjects = new HashSet<>();
        Set< OWLNamedIndividual> mapped = new HashSet<>();

        OWLReferences ontoRef = (OWLReferences) OWLReferencesInterface.OWLReferencesContainer.getOWLReferences( ontoName);

        try{
            Logger.SITBase.LOG("loading ...");
            Set<OWLNamedIndividual> cones = ontoRef.getIndividualB2Class(MORCone.INITIAL_CONE_TYPE);
            mapped.addAll( cones);
            for( OWLNamedIndividual i : cones) {
                MORCone obj = new MORCone(ontoName, Long.valueOf(ontoRef.getOWLObjectName(i)));  //ID = individualName
                allObjects.add( obj);
                Logger.SITBase.LOG( "\t[" + MORCone.INITIAL_CONE_TYPE + "] ... adding: " + obj);
            }

            Set<OWLNamedIndividual> cylinders = ontoRef.getIndividualB2Class(MORCylinder.INITIAL_CYLINDER_TYPE);
            mapped.addAll( cylinders);
            for( OWLNamedIndividual i : cylinders) {
                MORCylinder obj = new MORCylinder(ontoName, Long.valueOf(ontoRef.getOWLObjectName(i))); //ID = individualName
                allObjects.add( obj);
                Logger.SITBase.LOG( "\t[" + MORCylinder.INITIAL_CYLINDER_TYPE + "] ... adding: " + obj);
            }

            Set<OWLNamedIndividual> planes = ontoRef.getIndividualB2Class(MORPlane.INITIAL_PLANE_TYPE);
            mapped.addAll( planes);
            for( OWLNamedIndividual i : planes) {
                MORPlane obj = new MORPlane(ontoName, Long.valueOf(ontoRef.getOWLObjectName(i))); //ID = individualName
                allObjects.add( obj);
                Logger.SITBase.LOG( "\t[" + MORPlane.INITIAL_PLANE_TYPE + "] ... adding: " + obj);
            }

            Set<OWLNamedIndividual> orientables = ontoRef.getIndividualB2Class(MOROrientable.INITIAL_ORIENTABLE_TYPE);
            orientables.removeAll( mapped);
            mapped.addAll( orientables);
            for( OWLNamedIndividual i : orientables) {
                MOROrientable obj = new MOROrientable(ontoName, Long.valueOf(ontoRef.getOWLObjectName(i))); //ID = individualName
                allObjects.add( obj);
                Logger.SITBase.LOG( "\t[" + MOROrientable.INITIAL_ORIENTABLE_TYPE + "] ... adding: " + obj);
            }

            Set<OWLNamedIndividual> spheres = ontoRef.getIndividualB2Class(MORSphere.INITIAL_SPHERE_TYPE);
            mapped.addAll( spheres);
            for( OWLNamedIndividual i : spheres) {
                MORSphere obj = new MORSphere(ontoName, Long.valueOf(ontoRef.getOWLObjectName(i))); //ID = individualName
                allObjects.add( obj);
                Logger.SITBase.LOG( "\t[" + MORSphere.INITIAL_SPHERE_TYPE + "] ... adding: " + obj);
            }

            Set<OWLNamedIndividual> primitive = ontoRef.getIndividualB2Class(MORPrimitive.INITIAL_PRIMITIVE_TYPE);
            primitive.removeAll( mapped);
            mapped.addAll( primitive);
            for( OWLNamedIndividual i : primitive) {
                MORPrimitive obj = new MORPrimitive(ontoName, Long.valueOf(ontoRef.getOWLObjectName(i))); //ID = individualName
                allObjects.add( obj);
                Logger.SITBase.LOG( "\t[" + MORPrimitive.INITIAL_PRIMITIVE_TYPE + "] ... adding: " + obj);
            }
        }catch (NumberFormatException e){
            // todo try to get the id from semantics (see INSTANCE_ID_DATA_PROPERTY_NAME = "has_id")
            Logger.SITBase.logERROR( "Cannot load object with name that is not a 'long id'.");
            Logger.SITBase.logERROR(e);
        }
        Logger.SITBase.LOG( "... read all semantics ...");
        readAllSemantics( allObjects);
        Logger.SITBase.LOG( "... loading completed !");
        return allObjects;
    }

    public static void readAllSemantics(Set< MORPrimitive> allObjects){
        for( MORPrimitive o : allObjects)
            o.readSemantics();
    }
    public static void writeAllSemantics(Set< MORPrimitive> allObjects){
        for( MORPrimitive o : allObjects)
            o.writeSemantics();
    }

    public static OWLClass getObjectShape( OWLReferences ontoRef, OWLNamedIndividual individual){
        Set<OWLClass> classes = ontoRef.getIndividualClasses( individual);
        boolean isOrientable = false, isPrimitive = false;
        for( OWLClass cl : classes){
            String className = ontoRef.getOWLObjectName( cl);
            // disjoined class
            if (className.equals(MORCylinder.INITIAL_CYLINDER_TYPE))
                return cl;
            if (className.equals(MORCone.INITIAL_CONE_TYPE))
                return cl;
            if (className.equals(MORPlane.INITIAL_PLANE_TYPE))
                return cl;
            if (className.equals(MORSphere.INITIAL_SPHERE_TYPE))
                return cl;

            // common, parent class
            if (className.equals(MOROrientable.INITIAL_ORIENTABLE_TYPE))
                isOrientable = true;
            if (className.equals(MORPrimitive.INITIAL_PRIMITIVE_TYPE))
                isPrimitive = true;
        }
        if( isOrientable)
            return ontoRef.getOWLClass( MOROrientable.INITIAL_ORIENTABLE_TYPE);
        if( isPrimitive)
            return ontoRef.getOWLClass( MORPrimitive.INITIAL_PRIMITIVE_TYPE);
        return null;
    }

    @Override
    public String toString() {
        return "(id=" + id +
                ")(time=" + time +
                ")(semantic=" + semantic +
                ")(centroid=" + centroid + ")";
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MORPrimitive)) return false;

        MORPrimitive that = (MORPrimitive) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}