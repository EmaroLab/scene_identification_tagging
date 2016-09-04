package it.emarolab.osr.shapeTracking;

import java.util.ArrayList;
import java.util.List;

import it.emarolab.amor.owlDebugger.Logger;
import it.emarolab.osr.RecognitionManualSupervising;
import it.emarolab.osr.scene.sceneRecognition.TrakedShapesIndividualCreator;

public class ShapeTracker {
	
	private Logger log;
	public static Boolean DEBUG = RecognitionManualSupervising.DEBUG;

	static List< RansacTrackedShape> shapeTracker = new ArrayList< RansacTrackedShape>();
	List< RansacTrackedShape> addedShape, removedShape;
	
	public ShapeTracker( List<TrackedShape> geometricTracker){
		log = new Logger( this, DEBUG);
		
		// reset changes to the shape TRacker
		addedShape = new ArrayList< RansacTrackedShape>();
		removedShape = new ArrayList< RansacTrackedShape>();
		
		if( geometricTracker.isEmpty())
			initialiseTracker( geometricTracker);
		else{
			RansacTrackedShape.resetRansacTracker( shapeTracker); // set update and check flag to false for all entity
			track( geometricTracker);
		}
	}

	// create all ransac tracked entity from geometric tracker
	private void initialiseTracker( List<TrackedShape> geometricTracker){
		for( TrackedShape s : geometricTracker){
			addOneShape( s);
			log.addDebugString( " initialise tracked primitive shapes: " + s.toString());
		}
	}
	
	private RansacTrackedShape addOneShape( TrackedShape s){
		float cX, cY, cZ;
		if( s.getShapeTag().equals( TrakedShapesIndividualCreator.SHAPE_TAG_PLANE) || s.getShapeTag().equals( TrakedShapesIndividualCreator.SHAPE_TAG_UNKNOWN)){
			cX = s.getXCentroidP(); // given by point averaging
			cY = s.getYCentroidP();
			cZ = s.getZCentroidP();
		} else {
			cX = s.getXCentroidR(); // given by ransac shape coefficients
			cY = s.getYCentroidR();
			cZ = s.getZCentroidR();
		}
		log.addDebugString( " Incoming shape: " + s.getShapeTag() + " " + cX +" " + cY + " " + cZ);
		RansacTrackedShape r = RansacTrackedShape.createTrackedShape( s.getObjectId(), cX, cY, cZ, s.getShapeTag(), s.getCoefficients());
		shapeTracker.add( r);
		addedShape.add( r);
		return r;
	}
	

	private void track(List<TrackedShape> geometricTracker) {
		for( TrackedShape s : geometricTracker){
			Boolean shapeMatch = false;
			for( RansacTrackedShape r : shapeTracker){
				if( r.equals( s)){
					// the incoming tracker entity is already in the shapeTracker
					r.update( s); 
					r.setChecked( true);
					shapeMatch = true;
					log.addDebugString( " update tracked primitive shapes: " + r.toString()); 
					break;
				}
			}
			if( ! shapeMatch){
				// the incoming tracker entity is not in the shapeTracker
				RansacTrackedShape newShape = addOneShape( s);
				newShape.setChecked( true);
				log.addDebugString( " create new tracked primitive shapes: " + newShape.toString());
			}
		}
		
		//for( RansacTrackedShape r : shapeTracker){
		for( int i = 0; i < shapeTracker.size(); i++){
			RansacTrackedShape r = shapeTracker.get( i);
			if( ! r.isChecked){
				// the shapeTracker r is no more on the incoming tracking shape set
				r.setChecked( null);
				r.setUpdated( null);
				removedShape.add( r);
				shapeTracker.remove( r);
				log.addDebugString( " remove tracked primitive shapes: " + r.toString());
			}
		}
	}
	
	public static List<RansacTrackedShape> getShapeTracker() {
		return shapeTracker;
	}
	public List<RansacTrackedShape> getShapeAdded() {
		return addedShape;
	}
	public List<RansacTrackedShape> getShapeRemoved() {
		return removedShape;
	}
}
