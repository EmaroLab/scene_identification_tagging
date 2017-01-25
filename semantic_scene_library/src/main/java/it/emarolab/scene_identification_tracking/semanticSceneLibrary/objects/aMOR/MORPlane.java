package it.emarolab.scene_identification_tracking.semanticSceneLibrary.objects.aMOR;

import it.emarolab.scene_identification_tracking.semanticSceneLibrary.Semantics;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.aMORDescriptor.MOR3DArray;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.aMORDescriptor.MORSpatialDescriptor;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.objects.ObjectSemantics;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLLiteral;

/**
 * The java/semantic mapper for a Plane, based on aMOR OWL references.
 * <p>
 *     It implements a two way mapping interface between this java structure and a semantic individual.
 *     It extends {@link MOROrientable} so, it apply its inherited part of the semantic description of an object.
 *     Nevertheless, it adds also the definition of the hessian component of a plane.
 *     Such a procedure is still based on OWL data properties that reflect the fields of {@link ObjectSemantics.Plane},
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
 * @see ObjectSemantics.Plane
 */
public class MORPlane extends MOROrientable implements ObjectSemantics.Plane<MORSpatialDescriptor.MORSimpleDescriptor> {

    public static final String INITIAL_PLANE_TYPE = "Plane";

    /** the default value for the OWL data property name that semantically represent the hessian coefficient of this plane. */
    public static final String HESSIAN_PROPERTY_NAME = "has-geometric_hessian";

    // internal fields for Semantic.Plane interface
    private Double hessian;
    // internal fields for semantic mapping of a Primitive object
    private String hessianPropertyName = HESSIAN_PROPERTY_NAME;

    /**
     * Cloning constructor, create a new object as a clone of the input parameter.
     * @param plane the object to clone.
     */
    public MORPlane( MORPlane plane){
        super( plane);
        this.hessian = plane.hessian;
        this.hessianPropertyName = plane.hessianPropertyName;
    }
    /**
     * It uses the {@link MOROrientable#MOROrientable(String, long)}
     * constructor and set the hessian of this plane to <code>null</code>.
     * @param ontologyName the name to an instantiated {@link it.emarolab.amor.owlInterface.OWLReferences}
     *                     that will describe this plane semantically.
     * @param id the unique identifier of this plane.
     */
    public MORPlane( String ontologyName, long id){
        super( ontologyName, id);
        this.hessian = null;
    }
    /**
     * It uses the {@link MOROrientable#MOROrientable(String, long, MOR3DArray, MOR3DArray)}
     * constructor and set the hessian of this plane to the give value.
     * @param ontologyName the name to an instantiated {@link it.emarolab.amor.owlInterface.OWLReferences}
     *                     that will describe this plane semantically.
     * @param id the unique identifier of this plane.
     * @param centroid the coordinates of the center of mass of this plane.
     * @param axis the components of a vector aligned to the principal axis of this object.
     * @param hessian the hessian coefficient of this plane.
     */
    public MORPlane( String ontologyName, long id, MOR3DArray centroid, MOR3DArray axis, Double hessian){
        super( ontologyName, id, centroid, axis);
        this.hessian = hessian;
    }
    /**
     * It uses the {@link MOROrientable#MOROrientable(MORSpatialDescriptor.MORSimpleDescriptor, long)}
     * constructor and set the hessian of this plane to <code>null</code>.
     * @param semantic the ontology interface for semantically map this plane.
     * @param id the unique identifier of this plane.
     */
    public MORPlane(MORSpatialDescriptor.MORSimpleDescriptor semantic, long id) {
        super( semantic, id);
        this.hessian = null;
    }
    /**
     * It uses the {@link MOROrientable#MOROrientable(MORSpatialDescriptor.MORSimpleDescriptor, long, MOR3DArray, MOR3DArray)}
     * constructor and set the hessian of this plane to the give value.
     * @param semantic the ontology interface for semantically map this plane.
     * @param id the unique identifier of this plane.
     * @param centroid the coordinates of the center of mass of this plane.
     * @param axis the components of a vector aligned to the principal axis of this object.
     * @param hessian the hessian coefficient of this plane.
     */
    public MORPlane(MORSpatialDescriptor.MORSimpleDescriptor semantic, long id, MOR3DArray centroid, MOR3DArray axis, Double hessian) {
        super( semantic, id, centroid, axis);
        this.hessian = hessian;
    }
    @Override
    protected void initialiseTypes() {
        super.initialiseTypes();
        getSemantics().addType(INITIAL_PLANE_TYPE);
    }

    /** @param hessian the hessian coefficient of this plane to set.  */
    public void setHessian(Double hessian) {
        this.hessian = hessian;
    }
    @Override
    public Double getHessian() {
        return hessian;
    }
    /** @return the OWL literal representation of {@link #getHessian()}. */
    public OWLLiteral getHessianLiteral() {
        return getSemantics().getOntology().getOWLLiteral( getHessian());
    }

    /**
     * @param hessianPropertyName the name, to set, of the OWL data property used for semantically
     *                       map the hessian of this plane.
     */
    public void setHessianPropertyName(String hessianPropertyName) {
        this.hessianPropertyName = hessianPropertyName;
    }
    /**
     * @return the name of the OWL data property used for semantically
     * describe the hessian of this plane.
     */
    public String getHessianPropertyName(){
        return hessianPropertyName;
    }
    /**
     * @return the OWL data property used for semantically describe the hessian of this plane.
     * Based on {@link #getHessianPropertyName()}.
     */
    public OWLDataProperty getHessianProperty(){
        return getSemantics().getOntology().getOWLDataProperty( getHessianPropertyName());
    }

    /**
     * It calls {@link ObjectSemantics.Orientable#average(ObjectSemantics.Primitive)}
     * and than computes the average between the hessian
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

        if( object instanceof ObjectSemantics.Plane) {
            ObjectSemantics.Plane plane = (ObjectSemantics.Plane) object;
            if (this.getHessian() == null | plane.getHessian() == null)
                return false;
            setHessian( ( this.getHessian() + plane.getHessian()) / 2);
            return true;
        }
        return false;
    }

    /**
     * Synchronise the java representation of this Plane with the ontology
     * and return the process state.
     * <p>
     *     It calls {@link MOROrientable#writeSemantics()} and than, if the state
     *     of the operation {@link MappingState#isOK()}, it uses
     *     {@link MORSpatialDescriptor.MORSimpleDescriptor#writeLiteral(String, OWLDataProperty, OWLLiteral)}
     *     to write the hessian coefficient described by this class (called with parameter as:
     *     {@link #getHessian()} and {@link #getHessianLiteral()} respectively).
     *     Those writing process generates more writing states, that are merged in a
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

        WritingState radiusResult = getSemantics().writeLiteral("hessian",
                getHessianProperty(), getHessianLiteral());
        if( ! radiusResult.isOK())
            return radiusResult;

        return superResult.merge( radiusResult);
    }
    /**
     * Synchronise the ontology representation of a Plane with this java structure
     * and return the process state.
     * <p>
     *     It calls {@link MORPrimitive#readSemantics()} and than, if the state
     *     of the operation {@link MappingState#isOK()}, it uses
     *     {@link MORSpatialDescriptor.MORSimpleDescriptor#readLiteral(String, OWLDataProperty, OWLLiteral)}
     *     to read the hessian coefficient of the plane and update this structure (called with parameter as:
     *     {@link #getHessian()} and {@link #getHessianLiteral()} respectively).
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

        log( "[" + getID() + "] Sphere semantic reading ...");

        MORSpatialDescriptor.MORSimpleDescriptor.DataReadingOutcome radiusOutcome = getSemantics().readLiteral( "hessian",
                getHessianProperty(), getHessianLiteral());
        if( ! radiusOutcome.isOK())
            return radiusOutcome.getState();
        else if( radiusOutcome.isSuccess())
            setHessian( radiusOutcome.getDoubleReadBuffer());

        return superResult.merge( radiusOutcome.getState());
    }

    /**
     * Calls {@link #MORPlane(MORPlane)}.
     * @return a <code>new</code> copy of this object.
     */
    @Override // todo check if correctly overwritten everywhere (e,g, scene)
    public ObjectSemantics.Plane<MORSpatialDescriptor.MORSimpleDescriptor> copy() {
        return new MORPlane( this);
    }

    @Override
    public String toString() {
        return super.toString() + "(hessian=" + hessian + ")";
    }
}
