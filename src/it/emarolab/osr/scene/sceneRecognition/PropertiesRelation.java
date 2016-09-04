package it.emarolab.osr.scene.sceneRecognition;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import it.emarolab.amor.owlInterface.OWLReferences;

public class PropertiesRelation {

	// symmetric property name
	public static final String[] PROPERTIES_NAMES_SYMMETRIC_PRIMITIVE = {"isCoaxialWith", "isParallelTo", "isPerpendicularTo", 
		"isAlong_x", "isAlong_y", "isAlong_z", "onPlane_XY", "onPlane_YZ", "onPlane_XZ"}; 
	// no symmetric property name
	public static final String[] PROPERTIES_NAMES_PRIMITIVE 		= { "isRightOf", "isAboveOf", "isBehindOf" };
	// the inverse property name of non symmetric property RESPECTIVELY !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	public static final String[] PROPERTIES_NAMES_PRIMITIVE_INVERSE = { "isLeftOf",  "isBelowOf", "isInFrontOf"}; 
	
	// with the same indices of above define the scene properties
	public static final String[] PROPERTIES_NAMES_SCENE_SYMMETRIC_PRIMITIVE = { "hasSceneComponent_Coaxial", "hasSceneComponent_Parallel", "hasSceneComponent_Perpendicular", 
		"hasSceneComponent_AlongX", "hasSceneComponent_AlongY", "hasSceneComponent_AlongZ", "hasSceneComponent_CoplanarXY", 
		"hasSceneComponent_onPlaneXY", "hasSceneComponent_onPlaneYZ", "hasSceneComponent_onPlaneXZ"};
	public static final String[] PROPERTIES_NAMES_SCENE_PRIMITIVE	= { "hasSceneComponent_Rightness", "hasSceneComponent_Aboveness", "hasSceneComponent_Behindess"};
	
	private static List< String> allPropertyNames; 
 	static{
		// create list with all the primitive property
 		allPropertyNames = new ArrayList< String>();
		for( int i = 0; i < PROPERTIES_NAMES_SYMMETRIC_PRIMITIVE.length; i++)
			allPropertyNames.add( PROPERTIES_NAMES_SYMMETRIC_PRIMITIVE[ i]);
		for( int i = 0; i < PROPERTIES_NAMES_PRIMITIVE.length; i++)
			allPropertyNames.add( PROPERTIES_NAMES_PRIMITIVE[ i]);
	}
	
 	public static List< PropertiesRelation> initializeAllRelation(){
 		List< PropertiesRelation> all = new ArrayList< PropertiesRelation>();
 		for( String str : allPropertyNames)
 			all.add( new PropertiesRelation( str));
 		return all;
 	}
 	
 	// it does not contains PROPERTIES_NAMES_PRIMITIVE_INVERSE
	public static List< String> getPrimitivePropertyNames(){
		return allPropertyNames;
	}
	
	public static Boolean isSymmetric( String propertyName){
		for( int i = 0; i < PROPERTIES_NAMES_SYMMETRIC_PRIMITIVE.length; i++)
			if( PROPERTIES_NAMES_SYMMETRIC_PRIMITIVE[ i].equals( propertyName))
				return true;
		return false;
	}

	// a symmetric property isPrimitive always
	// a PROPERTIES_NAMES_PRIMITIVE is primitive
	// a PROPERTIES_NAMES_PRIMITIVE_INVERSE is not primitive
	// returns null if it does not exists !!!!!!!!!!
	public static Boolean isPrimitive( String propertyName){
		if( isSymmetric( propertyName))
			return true;
		for( int i = 0; i < PROPERTIES_NAMES_PRIMITIVE.length; i++)
			if( PROPERTIES_NAMES_PRIMITIVE[ i].equals( propertyName))
				return true;
		for( int i = 0; i < PROPERTIES_NAMES_PRIMITIVE_INVERSE.length; i++)
			if( PROPERTIES_NAMES_PRIMITIVE_INVERSE[ i].equals( propertyName))
				return false;		
		return null;
	}
	
	// return propertyName if symmetric (the inverse property is the property itself !!!!!!!!)
	// return null if inverse does not exists
	// return the inverse property name
	public static String getInverse( String propertyName){
		if( isSymmetric( propertyName))
			return propertyName;
		for( int i = 0; i < PROPERTIES_NAMES_PRIMITIVE.length; i++)
			if( PROPERTIES_NAMES_PRIMITIVE[ i].equals( propertyName))
				return PROPERTIES_NAMES_PRIMITIVE_INVERSE[ i];
		for( int i = 0; i < PROPERTIES_NAMES_PRIMITIVE_INVERSE.length; i++)
			if( PROPERTIES_NAMES_PRIMITIVE_INVERSE[ i].equals( propertyName))
				return PROPERTIES_NAMES_PRIMITIVE[ i];
		return null;
	}
	
	public static String getSceneProperty( String propertyName){
		if( isSymmetric( propertyName))
			for( int i = 0; i < PROPERTIES_NAMES_SYMMETRIC_PRIMITIVE.length; i++)
				if( PROPERTIES_NAMES_SYMMETRIC_PRIMITIVE[ i].equals(propertyName))
					return PROPERTIES_NAMES_SCENE_SYMMETRIC_PRIMITIVE[ i];
		for( int i = 0; i < PROPERTIES_NAMES_PRIMITIVE.length; i++)
			if( PROPERTIES_NAMES_PRIMITIVE[ i].equals( propertyName))
				return PROPERTIES_NAMES_SCENE_PRIMITIVE[ i];
		return null;
	}
	
	
	private Boolean isSymmetric, isPrimitive;
	private String propertyName, inversePropertyName;
	private String scenePropertyName;
	public PropertiesRelation( String propertyName){
		this.propertyName = propertyName;
		this.isSymmetric = isSymmetric( propertyName);
		this.isPrimitive = isPrimitive( propertyName);
		this.inversePropertyName = getInverse( propertyName);
		this.scenePropertyName = getSceneProperty( propertyName);
	}
	public Boolean isSymmetric() {
		return isSymmetric;
	}
	public Boolean isPrimitive() {
		return isPrimitive;
	}
	public String getPropertyName() {
		return propertyName;
	}
	public OWLObjectProperty getProperty( OWLReferences ontology){
		return ontology.getOWLObjectProperty( propertyName);
	}
	public Set< OWLNamedIndividual> getPropertyValue( OWLReferences ontology, OWLNamedIndividual ind){
		return ontology.getObjectPropertyB2Individual( ind, getProperty( ontology));
	}
	public String getInversePropertyName() {
		return inversePropertyName;
	}
	public OWLObjectProperty getInverseProperty( OWLReferences ontology){
		return ontology.getOWLObjectProperty( inversePropertyName);
	}
	public Set< OWLNamedIndividual> getInversePropertyValue( OWLReferences ontology, OWLNamedIndividual ind){
		return ontology.getObjectPropertyB2Individual( ind, getInverseProperty( ontology));
	}
	public String getScenePropertyName(){
		return this.scenePropertyName;
	}
	public OWLObjectProperty getSceneProperty( OWLReferences ontology){
		return ontology.getOWLObjectProperty( this.scenePropertyName);
	}

	@Override
	public String toString() {
		return "Relation:{property:" + this.propertyName + " inverse:" + this.inversePropertyName + " isSymmetric:" + this.isSymmetric + " isPrimitive:" + this.isPrimitive + "}";
	}

	@Override
	public boolean equals(Object obj) {
		if( obj instanceof PropertiesRelation){
			if( this.propertyName.equals( ( (PropertiesRelation)obj).propertyName) && this.inversePropertyName.equals( ( (PropertiesRelation)obj).inversePropertyName))
				return true;
			else return false; 
		}
		return super.equals(obj);
	}
	
	
}


