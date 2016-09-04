package it.emarolab.osr.shapeTracking;

import java.util.Arrays;


// this class emulate a ros message !!!!!
/*
#Header header

int64 objectId

float32 xCentroid_P # center of mass computed on the point cloud
float32 yCentroid_P
float32 zCentroid_P

float32 xCentroid_R # center of mass computed using ransac results
float32 yCentroid_R
float32 zCentroid_R

float32[] coefficients	# different set for different primitive shape

string shapeTag		

*/

public class TrackedShape {

	long objectId;
	float xCentroidP, yCentroidP, zCentroidP;
	float xCentroidR, yCentroidR, zCentroidR;
	float[] coefficients;
	String shapeTag; // one of the const value of TrakedShapesIndividualCreator,SHAPE_TAG_... 
	public TrackedShape(long objectId, float xCentroidP, float yCentroidP,
			float zCentroidP, float xCentroidR, float yCentroidR,
			float zCentroidR, float[] coefficients, String shapeTag) {
		super();
		this.objectId = objectId;
		this.xCentroidP = xCentroidP;
		this.yCentroidP = yCentroidP;
		this.zCentroidP = zCentroidP;
		this.xCentroidR = xCentroidR;
		this.yCentroidR = yCentroidR;
		this.zCentroidR = zCentroidR;
		this.coefficients = coefficients;
		this.shapeTag = shapeTag;
	}
	
	public long getObjectId() {
		return objectId;
	}
	public float getXCentroidP() {
		return xCentroidP;
	}
	public float getYCentroidP() {
		return yCentroidP;
	}
	public float getZCentroidP() {
		return zCentroidP;
	}
	public float getXCentroidR() {
		return xCentroidR;
	}
	public float getYCentroidR() {
		return yCentroidR;
	}
	public float getZCentroidR() {
		return zCentroidR;
	}
	public float[] getCoefficients() {
		return coefficients;
	}
	public String getShapeTag() {
		return shapeTag;
	}

	@Override
	public String toString() {
		return "TrackedShape [objectId=" + objectId + ", xCentroidP="
				+ xCentroidP + ", yCentroidP=" + yCentroidP + ", zCentroidP="
				+ zCentroidP + ", xCentroidR=" + xCentroidR + ", yCentroidR="
				+ yCentroidR + ", zCentroidR=" + zCentroidR + ", coefficients="
				+ Arrays.toString(coefficients) + ", shapeTag=" + shapeTag
				+ "]";
	}	
}
