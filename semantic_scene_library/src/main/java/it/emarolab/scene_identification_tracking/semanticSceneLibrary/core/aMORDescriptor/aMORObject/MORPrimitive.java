package it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.aMORDescriptor.aMORObject;

import com.google.common.base.Objects;
import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.amor.owlInterface.OWLReferencesInterface;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.Base;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.aMORDescriptor.MORDescriptor;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.semantic.GeometricSemantic;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.aMORDescriptor.MORLiteralDescriptor.*;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import java.util.HashSet;
import java.util.Set;

public class MORPrimitive extends Base.SITBase
        implements GeometricSemantic.Primitive<MORMultiLinkLiteralTypedIndividual, MORDataProperty, MOR3DataProperty>{

    private static long ID = 0;

    //private SemanticID timeId;
    private MORDataProperty time, id;
    private MOR3DataProperty centroid; //MORLiteral3D centroid;
    private MORMultiLinkLiteralTypedIndividual descriptor;
    // todo add time,id enablers for printing

    public MORPrimitive(OWLReferences ontoRef) {
        initialiseDescriptor( new MORMultiLinkLiteralTypedIndividual(ontoRef),
                shouldMapProperty( DATAPROPERTY.DEFAULT_obj_TIME, false),
                shouldMapProperty( DATAPROPERTY.DEFAULT_obj_ID, false));

        this.centroid = new MOR3DataProperty( getSemantics(), DATAPROPERTY.DEFAULT_objPRIMITIVE_CENTER);
        this.getSemantics().addType( CLASS.OBJECT);
        this.getSemantics().querySpatialProperties();
    }
    public MORPrimitive(String ontoName) {
        initialiseDescriptor( new MORMultiLinkLiteralTypedIndividual(ontoName),
                shouldMapProperty( DATAPROPERTY.DEFAULT_obj_TIME, false),
                shouldMapProperty( DATAPROPERTY.DEFAULT_obj_ID, false));

        this.centroid = new MOR3DataProperty( getSemantics(), DATAPROPERTY.DEFAULT_objPRIMITIVE_CENTER);
        this.getSemantics().addType( CLASS.OBJECT);
        this.getSemantics().querySpatialProperties();
    }
    public MORPrimitive(String ontoName, boolean mapTime, boolean mapId) {
        initialiseDescriptor( new MORMultiLinkLiteralTypedIndividual(ontoName),
                shouldMapProperty( DATAPROPERTY.DEFAULT_obj_TIME, mapTime),
                shouldMapProperty( DATAPROPERTY.DEFAULT_obj_ID, mapId));

        this.centroid = new MOR3DataProperty( getSemantics(), DATAPROPERTY.DEFAULT_objPRIMITIVE_CENTER);
        this.getSemantics().addType( CLASS.OBJECT);
        this.getSemantics().querySpatialProperties();
    }
    public MORPrimitive(OWLReferences ontoRef, boolean mapTime, boolean mapId) {
        initialiseDescriptor( new MORMultiLinkLiteralTypedIndividual(ontoRef),
                shouldMapProperty( DATAPROPERTY.DEFAULT_obj_TIME, mapTime),
                shouldMapProperty( DATAPROPERTY.DEFAULT_obj_ID, mapId));

        this.centroid = new MOR3DataProperty();
        this.getSemantics().addType( CLASS.OBJECT);
        this.getSemantics().querySpatialProperties();
    }
    private String shouldMapProperty( String prop, boolean b){
        if ( !b)
            return null;
        return prop;
    }
    private void initialiseDescriptor(MORMultiLinkLiteralTypedIndividual descriptor, String timeProperty, String idProperty){
        this.descriptor = descriptor;


        // by default set the name as the id, not further managed by this class
        this.descriptor.setInstance( String.valueOf(  ID++));

        // it add the property on the descriptor to be mapped
        // you may want to reset for readings
        this.time = new MORDataProperty();
        time.setMapping( getSemantics(),false, timeProperty);
        time.setValue( getSemantics(), System.currentTimeMillis());


        this.id = new MORDataProperty();
        id.setMapping( getSemantics(), false, idProperty);
        id.setValue( getSemantics(), ID);
    }

    public MORPrimitive(MORPrimitive copy) {
        this.time = copy.time.copy();
        this.id = copy.id.copy();
        this.descriptor = copy.descriptor.copy();
        this.centroid = copy.centroid.copy();
    }

    /**
     * @return the unique identifier for this object
     */
    @Override
    public MORDataProperty getID() {
        return id;
    }


    /**
     * @return the absolute time stamp in milli-seconds
     * (see: {@link System#currentTimeMillis()})
     * of the instant in which this object has been created.
     */
    @Override
    public MORDataProperty getTime() {
        return time;
    }

    /**
     * @return the spatial (and semantic) descriptor of the centre of mass of this Object.
     */
    @Override @Deprecated // todo make all the Semantic.DescriptorS interface deal with Semantic.Property Object
    public MOR3DataProperty getCentroid() {
        return centroid;
    }
    @Override @Deprecated
    public void setCentroid(MOR3DataProperty centroid) {
        if ( centroid != null) {
            this.centroid = centroid.copy();
        }
    }

    public void setCentroid(MORPrimitive s) {
        this.centroid.setXYZ( getSemantics(), s.centroid.getX().getValue(), s.centroid.getY().getValue(), s.centroid.getZ().getValue());
    }
    public void setCentroid( Double x, Double y, Double z){
        this.centroid.setXYZ( getSemantics(), x, y, z);
    }
    public void setCentroidProperty( String prefix){
        this.centroid.setProperty( getSemantics(), prefix);
    }
    public void setCentroidProperty( String prefix, String xSuffix, String ySuffix, String zSuffix){
        this.centroid.setProperty( getSemantics(), prefix, xSuffix, ySuffix, zSuffix);
    }


    public void setMappingId( Boolean shouldMap){
        this.getID().setMapping( getSemantics(), shouldMap, DATAPROPERTY.DEFAULT_obj_ID);
    }
    public void setMappingId( String idPropertyName){
        this.getID().setMapping( getSemantics(), true, idPropertyName);
    }

    public void setMappingTime( Boolean shouldMap){
        this.getTime().setMapping( getSemantics(), shouldMap, DATAPROPERTY.DEFAULT_obj_TIME);
    }
    public void setMappingTime( String timePropertyName){
        this.getTime().setMapping( getSemantics(), true, timePropertyName);
    }

    /**
     * @return the class to refer to, for manipulating the semantic structure.
     */
    @Override
    public MORMultiLinkLiteralTypedIndividual getSemantics() {
        return descriptor;
    }

    @Override
    public boolean average(GeometricSemantic.Primitive<MORMultiLinkLiteralTypedIndividual, MORDataProperty, MOR3DataProperty> object) {
        return centroid.average( getSemantics(), object.getCentroid());
    }

    @Override
    public boolean isDesribed() {
        return getSemantics().getOWLName( getSemantics().getTypes()).contains( getDescribedClassName());
    }

    @Override
    public boolean isPerceivable() {
        return getDescribedClassName().equals( inferShapeTag( getSemantics()));
    }

    public OWLClass getDescribedClass() {
        return getSemantics().getOntology().getOWLClass( getDescribedClassName());
    }
    public String getDescribedClassName() {
        return CLASS.PRIMITIVE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MORPrimitive)) return false;
        MORPrimitive that = (MORPrimitive) o;
        return Objects.equal(getID().getLong(), that.getID().getLong());
    }
    @Override
    public int hashCode() {
        return Objects.hashCode( getID());
    }

    /**
     * This method should be based on a copy constructor that takes the
     * derived class and all {@code super} copy constrcutors.<br>
     * Indeed it should just: {@code return new Base( this);}.
     *
     * @return a new copy of this class.
     */
    @Override
    public MORPrimitive copy() {
        return new MORPrimitive( this);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " -> (" + getID() + "-" + getTime()  + ")" + LOGGING.NEW_LINE
                + "\t\t\t\t, " + descriptor;
    }


    public static OWLClass inferShape(OWLReferences ontoRef, OWLNamedIndividual object) {
        return inferShape( ontoRef, ontoRef.getIndividualClasses( object));
    }
    public static OWLClass inferShape(OWLReferences ontoRef, Set<OWLClass> types) {
        Set< OWLClass> bottomClass = new HashSet<>();
        for ( OWLClass cl : types) {
            Set<OWLClass> subShapes = ontoRef.getSubClassOf(cl);
            if ( subShapes.size() == 1 & subShapes.contains( ontoRef.getOWLFactory().getOWLNothing()))
                bottomClass.add(cl);
        }
        if (bottomClass.size() > 1)
            Logger.logWARNING( "Object shape hierarchy tree should have disjointed perceivable shapes. Found: "
                    + ontoRef.getOWLName( bottomClass));
        for (OWLClass c : bottomClass)
            return c; // !!!!! ONLY
        return null; // not perceivable ( empty)
    }
    public static OWLClass inferShape(MORDescriptor<OWLNamedIndividual> descriptor) {
        return inferShape( descriptor.getOntology(), descriptor.getInstance());
    }
    public static String inferShapeTag(MORDescriptor<OWLNamedIndividual> descriptor) {
        OWLClass infer = inferShape( descriptor);
        if ( infer == null)
            return null; // not perceivable
        else return descriptor.getOWLName( infer);
    }
    public static String inferShapeTag(OWLReferences ontoRef, OWLNamedIndividual object) {
        OWLClass infer = inferShape( ontoRef, object);
        if ( infer == null)
            return null; // not perceivable
        else return ontoRef.getOWLName( infer);
    }

    // todo: change static metod to work with OWL top entity
    public static Set<MORPrimitive> loadFromClass(String ontoName) {
        Base.Logger.LOG("Load and all primitives... " );
        OWLReferences ontoRef = (OWLReferences) OWLReferencesInterface.OWLReferencesContainer.getOWLReferences( ontoName);
        if ( ontoRef != null)
            return loadFromClass( ontoRef);
        Logger.logERROR( " Cannot get reference: '" + ontoName + "' from OWLReferenceContainer:" + OWLReferencesInterface.OWLReferencesContainer.getOWLReferencesKeys());
        return null;
    }
    public static Set<MORPrimitive> loadFromClass(OWLReferences ontoRef) {
        return loadFromClass( ontoRef, true);
    }
    public static Set<MORPrimitive> loadFromClass(OWLReferences ontoRef, Boolean idFromName) {
        Logger.LOG("Loading all object semantics ...");
        Set< MORPrimitive> allObjects = new HashSet<>();

        Set< OWLNamedIndividual> individuals = ontoRef.getIndividualB2Class( CLASS.OBJECT);
        for ( OWLNamedIndividual i : individuals){
            String shapeClass = inferShapeTag( ontoRef, i);
            if ( shapeClass != null) {
                MORPrimitive primitive = null;
                if (shapeClass.equals(CLASS.PRIMITIVE))
                    primitive = new MORPrimitive(ontoRef);
                else if (shapeClass.equals(CLASS.SPHERE))
                    primitive = new MORSphere( ontoRef);
                else if (shapeClass.equals(CLASS.ORIENTBLE))
                    primitive = new MOROrientable(ontoRef);
                else if (shapeClass.equals(CLASS.PLANE))
                    primitive = new MORPlane(ontoRef);
                else if (shapeClass.equals(CLASS.CONE))
                    primitive = new MORCone(ontoRef);
                else if (shapeClass.equals(CLASS.CYLINDER))
                    primitive = new MORCylinder(ontoRef);
                if ( primitive != null){
                    primitive.setIndidual( i);
                    if (idFromName)
                        primitive.id.setValue( primitive.getSemantics(), Long.valueOf(ontoRef.getOWLObjectName(i)));
                    allObjects.add( primitive);
                    //Logger.LOG( " --> new primitive:" + primitive);
                } else Logger.logERROR( "Cannot instantiate a MORPrimitive of OWLClass: " + shapeClass);
            } else Logger.logWARNING( "Cannot instantiate object '" + ontoRef.getOWLObjectName( i)
                        + "' of shape: " + shapeClass);
        }
        Base.Logger.LOG("Primitives found: " + MORPrimitive.getInstancesName( ontoRef, allObjects));
        return allObjects;
    }
    public static MappingTransitions readAllSemantics(Set< MORPrimitive> allObjects){
        Logger.LOG("Reading all object semantics ...");
        MappingTransitions out = new MappingTransitions();
        for( MORPrimitive o : allObjects) {
            out.addAll(o.readSemantic());
            Logger.LOG( " ../.. reading " + o);
        }
        return out;
    }
    public static MappingTransitions writeAllSemantics(Set< MORPrimitive> allObjects){
        Logger.LOG("Writing all object semantics ...");
        MappingTransitions out = new MappingTransitions();
        for( MORPrimitive o : allObjects) {
            Logger.LOG( " ../.. write " + o);
            out.addAll(o.writeSemantic());
        }
        return out;
    }

    public static Set< String> getInstancesName(OWLReferences ontoRef, Set< MORPrimitive> objects){
        Set< String> out = new HashSet<>();
        for ( MORPrimitive o : objects)
            out.add( ontoRef.getOWLObjectName( o.getSemantics().getInstance()));
        return out;
    }

    public void setIndidual(OWLNamedIndividual indidual) {
        this.getSemantics().setInstance( indidual);
    }
    public void setIndidual( String indidualName) {
        this.getSemantics().setInstance( indidualName);
    }
}