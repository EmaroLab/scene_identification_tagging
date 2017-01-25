package it.emarolab.scene_identification_tracking.semanticSceneLibrary.objects.aMOR;

import it.emarolab.scene_identification_tracking.semanticSceneLibrary.Semantics;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.aMORDescriptor.MOR3DArray;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.aMORDescriptor.MORSpatialDescriptor;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.objects.ObjectSemantics;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLLiteral;

/**
 * The java/semantic mapper for a Sphere, based on aMOR OWL references.
 * <p>
 *     It implements a two way mapping interface between this java structure and a semantic individual.
 *     It extends {@link MORPrimitive} so, it apply its inherited part of the semantic description of an object.
 *     Nevertheless, it adds also the definition of the radius od this sphere.
 *     Such a procedure is still based on OWL data properties that reflect the fields of {@link ObjectSemantics.Sphere},
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
 * @see ObjectSemantics.Sphere
 */
public class MORSphere extends MORPrimitive implements ObjectSemantics.Sphere<MORSpatialDescriptor.MORSimpleDescriptor> {

    public static final String INITIAL_SPHERE_TYPE = "Sphere";

    /** the default value for the OWL data property name that semantically represent the radius of this sphere. */
    public static final String RADIUS_DATA_PROPERTY_NAME = "has-sphere_radius";

    // internal fields for Semantic.Sphere interface
    private Double radius;
    // internal fields for semantic mapping of a Primitive object
    private String radiusPropertyName = RADIUS_DATA_PROPERTY_NAME;

    /**
     * Cloning constructor, create a new object as a clone of the input parameter.
     * @param sphere the object to clone.
     */
    public MORSphere( MORSphere sphere){
        super( sphere);
        this.radius = sphere.radius;
        this.radiusPropertyName = sphere.radiusPropertyName;
    }
    /**
     * It uses the {@link MORPrimitive#MORPrimitive(String, long)}
     * constructor and set the radius of this sphere to <code>null</code>.
     * @param ontologyName the name to an instantiated {@link it.emarolab.amor.owlInterface.OWLReferences}
     *                     that will describe this sphere semantically.
     * @param id the unique identifier of this object.
     */
    public MORSphere( String ontologyName, long id){
        super( ontologyName, id);
        initialise( null);
    }
    /**
     * It uses the {@link MORPrimitive#MORPrimitive(String, long, MOR3DArray)}
     * constructor and set the radius of this sphere to the give value.
     * @param ontologyName the name to an instantiated {@link it.emarolab.amor.owlInterface.OWLReferences}
     *                     that will describe this sphere semantically.
     * @param id the unique identifier of this sphere.
     * @param centroid the coordinates of the center of mass of this sphere.
     * @param radius the radius of this sphere
     */
    public MORSphere( String ontologyName, long id, MOR3DArray centroid, Double radius){
        super( ontologyName, id, centroid);
        initialise( radius);
    }
    /**
     * It uses the {@link MORPrimitive#MORPrimitive(MORSpatialDescriptor.MORSimpleDescriptor, long)}
     * constructor and set the radius of this sphere to <code>null</code>.
     * @param semantic the ontology interface for semantically map this sphere.
     * @param id the unique identifier of this sphere.
     */
    public MORSphere(MORSpatialDescriptor.MORSimpleDescriptor semantic, long id) {
        super( semantic, id);
        initialise( null);
    }
    /**
     * It uses the {@link MORPrimitive#MORPrimitive(MORSpatialDescriptor.MORSimpleDescriptor, long, MOR3DArray)}
     * constructor and set the radius of this sphere to the give value.
     * @param semantic the ontology interface for semantically map this sphere.
     * @param id the unique identifier of this sphere.
     * @param centroid the coordinates of the center of mass of this sphere.
     * @param radius the radius of this sphere
     */
    public MORSphere(MORSpatialDescriptor.MORSimpleDescriptor semantic, long id, MOR3DArray centroid, Double radius) {
        super( semantic, id, centroid);
        initialise( radius);
    }
    // common constructor
    private void initialise( Double radius){
        this.radius = radius;
    }
    @Override
    protected void initialiseTypes() {
        super.initialiseTypes();
        getSemantics().addType(INITIAL_SPHERE_TYPE);
    }

    /** @param radius the radius of this sphere to set.  */
    public void setRadius(Double radius) {
        this.radius = radius;
    }
    @Override
    public Double getRadius() {
        return radius;
    }
    /** @return the OWL literal representation of {@link #getRadius()}. */
    public OWLLiteral getRadiusLiteral() {
        return getSemantics().getOntology().getOWLLiteral( getRadius());
    }

    /**
     * @param radiusPropertyName the name, to set, of the OWL data property used for semantically
     *                       map the radius of this sphere.
     */
    public void setRadiusPropertyName(String radiusPropertyName) {
        this.radiusPropertyName = radiusPropertyName;
    }
    /**
     * @return the name of the OWL data property used for semantically
     * describe the radius of this sphere.
     */
    public String getRadiusPropertyName(){
        return radiusPropertyName;
    }
    /**
     * @return the OWL data property used for semantically describe the radius of this sphere.
     * Based on {@link #getRadiusPropertyName()}.
     */
    public OWLDataProperty getRadiusProperty(){
        return getSemantics().getOntology().getOWLDataProperty( getRadiusPropertyName());
    }

    /**
     * It calls {@link ObjectSemantics.Primitive#average(ObjectSemantics.Primitive)}
     * and than computes the average between the radius
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

        if( object instanceof ObjectSemantics.Sphere) {
            ObjectSemantics.Sphere sphere = (ObjectSemantics.Sphere) object;
            if (this.getRadius() == null | sphere.getRadius() == null)
                return false;
            setRadius( ( this.getRadius() + sphere.getRadius()) / 2);
            return true;
        }
        return false;
    }

    /**
     * Synchronise the java representation of this Sphere with the ontology
     * and return the process state.
     * <p>
     *     It calls {@link MORPrimitive#writeSemantics()} and than, if the state
     *     of the operation {@link MappingState#isOK()}, it uses
     *     {@link MORSpatialDescriptor.MORSimpleDescriptor#writeLiteral(String, OWLDataProperty, OWLLiteral)}
     *     to write the radius described by this class (called with parameter as:
     *     {@link #getRadiusProperty()} and {@link #getRadiusLiteral()} respectively).
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

        log( "[" + getID() + "] Sphere semantic writing ...");

        WritingState radiusResult = getSemantics().writeLiteral("radius",
                getRadiusProperty(), getRadiusLiteral());
        if( ! radiusResult.isOK())
            return radiusResult;

        return superResult.merge( radiusResult);
    }
    /**
     * Synchronise the ontology representation of a Sphere with this java structure
     * and return the process state.
     * <p>
     *     It calls {@link MORPrimitive#readSemantics()} and than, if the state
     *     of the operation {@link MappingState#isOK()}, it uses
     *     {@link MORSpatialDescriptor.MORSimpleDescriptor#readLiteral(String, OWLDataProperty, OWLLiteral)}
     *     to read the radius of a sphere and update this structure (called with parameter as:
     *     {@link #getRadiusProperty()} and {@link #getRadiusLiteral()} respectively).
     *     Those reading process generate more reading states that are merged in a
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

        log( "[" + getID() + "] Sphere semantic reading ...");

        MORSpatialDescriptor.MORSimpleDescriptor.DataReadingOutcome radiusOutcome = getSemantics().readLiteral( "radius",
                getRadiusProperty(), getRadiusLiteral());
        if( ! radiusOutcome.isOK())
            return radiusOutcome.getState();
        else if( radiusOutcome.isSuccess())
            setRadius( radiusOutcome.getDoubleReadBuffer());

        return superResult.merge( radiusOutcome);
    }

    /**
     * Calls {@link #MORSphere(MORSphere)}
     * @return a <code>new</code> copy of this object.
     */
    @Override
    public ObjectSemantics.Sphere<MORSpatialDescriptor.MORSimpleDescriptor> copy() {
        return new MORSphere( this);
    }

    @Override
    public String toString() {
        return super.toString() + "(radius=" + radius + ")";
    }
}
