package it.emarolab.osr.shapeTracking;

import java.util.List;

import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import it.emarolab.amor.owlDebugger.Logger;
import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.osr.RecognitionManualSupervising;
import it.emarolab.osr.scene.primitiveShapeData.ConeShapeData;
import it.emarolab.osr.scene.primitiveShapeData.CylinderShapeData;
import it.emarolab.osr.scene.primitiveShapeData.Dimentional3Data;
import it.emarolab.osr.scene.primitiveShapeData.PlaneShapeData;
import it.emarolab.osr.scene.primitiveShapeData.SphereShapeData;
import it.emarolab.osr.scene.sceneRecognition.TrakedShapesIndividualCreator;

public class RansacTrackedShape { // extends TrackedShape{
	
	// probability to pas from an old shape to a new one ( SHAPE_STATE_PROPRTY_OLDshapetag_NEWshapetag)
/*	public static final float SHAPE_STATE_PROPRTY_PLANE_PLANE = .85f, 	 SHAPE_STATE_PROPRTY_PLANE_SPHERE = .01f, 	SHAPE_STATE_PROPRTY_PLANE_CONE = .01f, 	  	SHAPE_STATE_PROPRTY_PLANE_CYLINDER = .06f;
	public static final float SHAPE_STATE_PROPRTY_SPHERE_PLANE = .07f, 	 SHAPE_STATE_PROPRTY_SPHERE_SPHERE = .94f, 	SHAPE_STATE_PROPRTY_SPHERE_CONE = .01f,   	SHAPE_STATE_PROPRTY_SPHERE_CYLINDER = .01f;
	public static final float SHAPE_STATE_PROPRTY_CONE_PLANE = .07f, 	 SHAPE_STATE_PROPRTY_CONE_SPHERE = .01f, 	SHAPE_STATE_PROPRTY_CONE_CONE = .65f, 	 	SHAPE_STATE_PROPRTY_CONE_CYLINDER = .26f;
	public static final float SHAPE_STATE_PROPRTY_CYLINDER_PLANE = .08f, SHAPE_STATE_PROPRTY_CYLINDER_SPHERE = .01f,SHAPE_STATE_PROPRTY_CYLINDER_CONE = .01f, 	SHAPE_STATE_PROPRTY_CYLINDER_CYLINDER = .90f;
	public static final float SHAPE_STATE_PROPRTY_UNKNOWN_SOMETING = 1.0f, SHAPE_STATE_PROPRTY_SOMETING_UNKNOWN = 0.0f;*/
	public static final float SHAPE_STATE_PROPRTY_PLANE_PLANE = .5f, 	 SHAPE_STATE_PROPRTY_PLANE_SPHERE = .1f, 	SHAPE_STATE_PROPRTY_PLANE_CONE = .01f, 	  	SHAPE_STATE_PROPRTY_PLANE_CYLINDER = .1f;
	public static final float SHAPE_STATE_PROPRTY_SPHERE_PLANE = .1f, 	 SHAPE_STATE_PROPRTY_SPHERE_SPHERE = .5f, 	SHAPE_STATE_PROPRTY_SPHERE_CONE = .01f,   	SHAPE_STATE_PROPRTY_SPHERE_CYLINDER = .1f;
	public static final float SHAPE_STATE_PROPRTY_CONE_PLANE = .2f, 	 SHAPE_STATE_PROPRTY_CONE_SPHERE = .2f, 	SHAPE_STATE_PROPRTY_CONE_CONE = .7f, 	 	SHAPE_STATE_PROPRTY_CONE_CYLINDER = .2f;
	public static final float SHAPE_STATE_PROPRTY_CYLINDER_PLANE = .2f,  SHAPE_STATE_PROPRTY_CYLINDER_SPHERE = .2f, SHAPE_STATE_PROPRTY_CYLINDER_CONE = .01f, 	SHAPE_STATE_PROPRTY_CYLINDER_CYLINDER = .6f;
	public static final float SHAPE_STATE_PROPRTY_UNKNOWN_SOMETING = 1.0f, SHAPE_STATE_PROPRTY_SOMETING_UNKNOWN = 0.0f;
	
	private int planeIncomingCnt = 0, sphereIncomingCnt = 0, coneIncomingCnt = 0, cylinderIncomingCnt = 0, unknownIncomingCnt = 0;
	private int planeCnt = 0, sphereCnt = 0, coneCnt = 0, cylinderCnt = 0, unknownCnt = 0;
	
	// parameter to updte shape coefficients by weight avarage
	public static final float UPDATE_EPSILON = 0.05f;	
	public static final float UPDATE_OLD_WEIGHT = 0.3f;
	public static final float UPDATE_NEW_WEIGHT = 0.7f;
	
	private Logger log;
	private static Logger staticLog;
	public static Boolean DEBUG = RecognitionManualSupervising.DEBUG;
	
	long objectId;
	Dimentional3Data centroid;
	String shapeTag;
	float[] coefficient;
	
	// those are null if this shape is removed from tracker
	Boolean isUpdated = false; // used to know if someting is changes and so ontology must be update
	Boolean isChecked = false; // used to know if the incoming tracked shape list contains this element (false = no)
	
	public RansacTrackedShape( long objectId, Dimentional3Data centroid, String shapeTag, float[] coefficient) {
		super();	
		log = new Logger( this, DEBUG);
		staticLog = new Logger( RansacTrackedShape.class, DEBUG);
		this.objectId = objectId;
		this.centroid = centroid;
		this.shapeTag = shapeTag;
		this.coefficient = coefficient;
	}
	
	private boolean souldUpdate( TrackedShape geometricTrackedShape){
		if( this.equals( geometricTrackedShape)){ // if object id are equals
			int incomingIdx = updateShapeIdCount( geometricTrackedShape);
			int oldIdx = updateShapeIdCount();
			float incomingProp = stateProbability( geometricTrackedShape);
			float oldProp = stateProbability();
			
			//log.info( this + "   " + incomingIdx * incomingProp + " > " + oldIdx * oldProp + "    " + geometricTrackedShape.getShapeTag() + " " + geometricTrackedShape.getObjectId());
			
			if( incomingIdx * incomingProp > oldIdx * oldProp)
				return true;
		}
		return false;
	}

	private Integer updateShapeIdCount( TrackedShape s){
		if( RansacTrackedShape.isUnlnown( s)){
			log.addDebugString( "Cannot create an individual of primitve type = " + TrakedShapesIndividualCreator.SHAPE_TAG_UNKNOWN);
			return ++unknownIncomingCnt;
		} if( RansacTrackedShape.isPlane( s))
			return ++planeIncomingCnt;
		if( RansacTrackedShape.isSphere( s))
			return ++sphereIncomingCnt;
		if( RansacTrackedShape.isCone( s))
			return ++coneIncomingCnt;
		if( RansacTrackedShape.isCylinder( s))
			return ++cylinderIncomingCnt;
		return null; // must not happen
	}
	
	private Integer updateShapeIdCount(){
		if( this.isUnlnown()){
			log.addDebugString( "Cannot create an individual of primitve type = " + TrakedShapesIndividualCreator.SHAPE_TAG_UNKNOWN);
			return ++unknownCnt;
		} if( this.isPlane())
			return ++planeCnt;
		if( this.isSphere())
			return ++sphereCnt;
		if( this.isCone())
			return ++coneCnt;
		if( this.isCylinder())
			return ++cylinderCnt;
		return null; // must not happen
	}
	
	private Float stateProbability( TrackedShape s){
		// plane v.s. plane
		if( this.isPlane() && RansacTrackedShape.isPlane( s))
			return SHAPE_STATE_PROPRTY_PLANE_PLANE;
		// plane v.s. sphere
		else if( this.isPlane() && RansacTrackedShape.isSphere( s))
			return SHAPE_STATE_PROPRTY_PLANE_SPHERE;
		// plane v.s. cone
		else if( this.isPlane() && RansacTrackedShape.isCone( s))
			return SHAPE_STATE_PROPRTY_PLANE_CONE;
		// plane v.s. cylinder
		else if( this.isPlane() && RansacTrackedShape.isCylinder( s))
			return SHAPE_STATE_PROPRTY_PLANE_CYLINDER;
		// sphere v.s. plane
		else if( this.isSphere() && RansacTrackedShape.isPlane( s))
			return SHAPE_STATE_PROPRTY_SPHERE_PLANE;
		// sphere v.s. sphere
		else if( this.isSphere() && RansacTrackedShape.isSphere( s))
			return SHAPE_STATE_PROPRTY_SPHERE_SPHERE;
		// sphere v.s. cone
		else if( this.isSphere() && RansacTrackedShape.isCone( s))
			return SHAPE_STATE_PROPRTY_SPHERE_CONE;
		// sphere v.s. cylinder
		else if( this.isSphere() && RansacTrackedShape.isCylinder( s))
			return SHAPE_STATE_PROPRTY_SPHERE_CYLINDER;
		// cone v.s. plane
		else if( this.isCone() && RansacTrackedShape.isPlane( s))
			return SHAPE_STATE_PROPRTY_CONE_PLANE;
		// cone v.s. sphere
		else if( this.isCone() && RansacTrackedShape.isSphere( s))
			return SHAPE_STATE_PROPRTY_CONE_SPHERE;
		// cone v.s. cone
		else if( this.isCone() && RansacTrackedShape.isCone( s))
			return SHAPE_STATE_PROPRTY_CONE_CONE;
		// cone v.s. cylinder
		else if( this.isCone() && RansacTrackedShape.isCylinder( s))
			return SHAPE_STATE_PROPRTY_CONE_CYLINDER;
		// cylinder v.s. plane
		else if( this.isCylinder() && RansacTrackedShape.isPlane( s))
			return SHAPE_STATE_PROPRTY_CYLINDER_PLANE;
		// cylinder v.s. sphere
		else if( this.isCylinder() && RansacTrackedShape.isSphere( s))
			return SHAPE_STATE_PROPRTY_CYLINDER_SPHERE;
		// cylinder v.s. cone
		else if( this.isCylinder() && RansacTrackedShape.isCone( s))
			return SHAPE_STATE_PROPRTY_CYLINDER_CONE;
		// cylinder v.s. cylinder
		else if( this.isCylinder() && RansacTrackedShape.isCylinder( s))
			return SHAPE_STATE_PROPRTY_CYLINDER_CYLINDER;
		// unknown v.s. ???
		else if( this.isUnlnown())
			return SHAPE_STATE_PROPRTY_UNKNOWN_SOMETING;
		// ?? v.s. unknown
		else if( RansacTrackedShape.isUnlnown( s))
			return SHAPE_STATE_PROPRTY_SOMETING_UNKNOWN;
		// must not be happen !!!!!!!
		else return null;
	}
	
	private float stateProbability(){
		if( this.isPlane())
			return SHAPE_STATE_PROPRTY_PLANE_PLANE;
		if( this.isSphere())
			return SHAPE_STATE_PROPRTY_SPHERE_SPHERE;
		if( this.isCone())
			return SHAPE_STATE_PROPRTY_CONE_CONE;
		if( this.isCylinder())
			return SHAPE_STATE_PROPRTY_CYLINDER_CYLINDER;
		// if it is unknown
		return SHAPE_STATE_PROPRTY_SOMETING_UNKNOWN;
					
	}
	
	public boolean update( TrackedShape geometricTrackedShape){
		boolean result = false;
		if( souldUpdate( geometricTrackedShape)){
			// update all the individual if the shape tag changes with respect to probability
			Dimentional3Data geometricCentroid = new Dimentional3Data( geometricTrackedShape.getXCentroidR(), geometricTrackedShape.getYCentroidR(), geometricTrackedShape.getZCentroidR());
			this.centroid = geometricCentroid;
			this.shapeTag = geometricTrackedShape.getShapeTag();
			this.coefficient = geometricTrackedShape.getCoefficients();
			result = true;
		} else {
			// make an weight average between the coefficients of the shape
			if( this.shapeTag.equals( geometricTrackedShape.getShapeTag())){ // => coefficient vector of the same type
				for( int i = 0; i < this.getCoefficient().length; i++){
					if( Math.abs( this.coefficient[ i] - geometricTrackedShape.getCoefficients()[ i]) > UPDATE_EPSILON)
						result = true;
					this.coefficient[ i] = ( this.coefficient[ i] * UPDATE_OLD_WEIGHT + geometricTrackedShape.getCoefficients()[ i] * UPDATE_NEW_WEIGHT) 
							/ ( UPDATE_OLD_WEIGHT + UPDATE_NEW_WEIGHT);
				}
			}
		}
		this.setUpdated( result);
		return result;
	}
	
	public long getObjectId() {
		return objectId;
	}
	public Dimentional3Data getCentroid() {
		return centroid;
	}
	public String getShapeTag() {
		return shapeTag;
	}
	public float[] getCoefficient() {
		return coefficient;
	}
	
	public Boolean isUpdated() {
		return isUpdated;
	}

	public void setUpdated(Boolean isUpdated) {
		this.isUpdated = isUpdated;
	}
	
	public Boolean isChecked() {
		return isChecked;
	}

	public void setChecked(Boolean isChecked) {
		this.isChecked = isChecked;
	}
	
	public boolean isPlane(){
		if( this.getShapeTag().equalsIgnoreCase( TrakedShapesIndividualCreator.SHAPE_TAG_PLANE))
			return true;
		return false;
	}
	
	public boolean isSphere(){
		if( this.getShapeTag().equalsIgnoreCase( TrakedShapesIndividualCreator.SHAPE_TAG_SPHERE))
			return true;
		return false;
	}
	
	public boolean isCone(){
		if( this.getShapeTag().equalsIgnoreCase( TrakedShapesIndividualCreator.SHAPE_TAG_CONE))
			return true;
		return false;
	}
	
	public boolean isCylinder(){
		if( this.getShapeTag().equalsIgnoreCase( TrakedShapesIndividualCreator.SHAPE_TAG_CYLINDER))
			return true;
		return false;
	}
	
	public boolean isUnlnown(){
		if( this.getShapeTag().equalsIgnoreCase( TrakedShapesIndividualCreator.SHAPE_TAG_UNKNOWN))
			return true;
		return false;
	}
	
	@Override
	// two are equal if the object id is equal
	// remember to call RansacTrackedShape.equals( TrackedShape)
	public boolean equals(Object obj) {
		if( obj instanceof TrackedShape){
			return this.objectId == (( TrackedShape) obj).getObjectId();
		} else return super.equals(obj);
	}

	@Override
	public String toString(){
		return "{ " + shapeTag + ", id:" + objectId + ", checked:" + isChecked + ", updated:" + isUpdated + "}";
	}
	
	public static RansacTrackedShape createTrackedShape( long objectId, float centroidX, float centroidY, float centroidZ, String shapeTag, float[] coefficient){
		Dimentional3Data centroid = new Dimentional3Data( centroidX, centroidY, centroidZ);
		return createTrackedShape( objectId, centroid, shapeTag, coefficient);
	}
	
	public static RansacTrackedShape createTrackedShape( long objectId, Dimentional3Data centroid, String shapeTag, float[] coefficient){
		RansacTrackedShape t = new RansacTrackedShape( objectId, centroid, shapeTag, coefficient);
		t.setUpdated( true);
		return t;
	}
	
	public static void resetRansacTracker( List< RansacTrackedShape> shapeTracker){
		for( RansacTrackedShape r: shapeTracker){
			r.setUpdated( false);
			r.setChecked( false);
		}
	}	
	
	
	public static boolean isPlane( TrackedShape s){
		if( s.getShapeTag().equalsIgnoreCase( TrakedShapesIndividualCreator.SHAPE_TAG_PLANE))
			return true;
		return false;
	}
	
	public static boolean isSphere( TrackedShape s){
		if( s.getShapeTag().equalsIgnoreCase( TrakedShapesIndividualCreator.SHAPE_TAG_SPHERE))
			return true;
		return false;
	}
	
	public static boolean isCone( TrackedShape s){
		if( s.getShapeTag().equalsIgnoreCase( TrakedShapesIndividualCreator.SHAPE_TAG_CONE))
			return true;
		return false;
	}
	
	public static boolean isCylinder( TrackedShape s){
		if( s.getShapeTag().equalsIgnoreCase( TrakedShapesIndividualCreator.SHAPE_TAG_CYLINDER))
			return true;
		return false;
	}
	
	public static boolean isUnlnown( TrackedShape s){
		if( s.getShapeTag().equalsIgnoreCase( TrakedShapesIndividualCreator.SHAPE_TAG_UNKNOWN))
			return true;
		return false;
	}
	
	public static String getDefaultIndividualName( RansacTrackedShape s){
		String individualName = null;
		if( s.isUnlnown())
			staticLog.addDebugString( "Cannot delate an individual of primitve type UNKNOWN ");
		else if( s.isPlane())
			individualName = PlaneShapeData.computeDefaultIndividualName( s.getObjectId());
		else if( s.isSphere())
			individualName = SphereShapeData.computeDefaultIndividualName( s.getObjectId());
		else if( s.isCone())
			individualName = ConeShapeData.computeDefaultIndividualName( s.getObjectId());
		else if( s.isCylinder())
			individualName = CylinderShapeData.computeDefaultIndividualName( s.getObjectId());
		return individualName;
	}
	
	public static OWLNamedIndividual getDefaultIndividual( RansacTrackedShape s, OWLReferences onto){
		String individualName = getDefaultIndividualName( s);
		return onto.getOWLIndividual(individualName);
	}
	
	public static List< OWLDataProperty> getShapeDataProperty( RansacTrackedShape s, OWLReferences onto){
		if( s.isPlane()){
			return PlaneShapeData.getDataPropertyList( onto);
		}if( s.isSphere()){
			return SphereShapeData.getDataPropertyList( onto);
		}if( s.isCone()){
			return ConeShapeData.getDataPropertyList( onto);
		}if( s.isCylinder()){
			return CylinderShapeData.getDataPropertyList( onto);
		} else return null;
	}
	
}
