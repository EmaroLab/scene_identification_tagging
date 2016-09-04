package it.emarolab.osr.scene.sceneRecognition;

import java.util.Set;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.amor.owlInterface.OWLReferencesInterface;

public class PrimitiveRelation {
 
	private OWLNamedIndividual preInd, postInd;
	private PropertiesRelation relation;
	private PrimitiveRelation inverse; 
	private RelationClassDescription classDescriptor = null;
	
	// constructor for original properties
	public PrimitiveRelation( OWLNamedIndividual preInd, PropertiesRelation property, OWLNamedIndividual postInd){		
		this.relation = property;
		this.preInd = preInd;
		this.postInd = postInd;
		this.inverse = new PrimitiveRelation( this);
	}
	// constructor for inverse properties
	private PrimitiveRelation( PrimitiveRelation originalRelation){
		// compute inverse relation from the original
		PropertiesRelation property = new PropertiesRelation( originalRelation.getRelation().getInversePropertyName());
		this.relation =  property;
		this.preInd = originalRelation.getPostInd();
		this.postInd = originalRelation.getPreInd();
		this.inverse = originalRelation;
	}
    
	public PrimitiveRelation getInverse() {
		return inverse;
	}
	public OWLNamedIndividual getPreInd() {
		return preInd;
	}
	public OWLNamedIndividual getPostInd() {
		return postInd;
	}
	public PropertiesRelation getRelation() {
		return relation;
	}
	
	public RelationClassDescription getIndividualClasses( OWLReferences ontology){
		if( classDescriptor == null)
			classDescriptor = new RelationClassDescription( ontology, this);
		return classDescriptor;
	}	

	// a primitive relation is equal to another if it is the same or its invert
	@Override
	public boolean equals(  Object obj) {
		if( obj instanceof PrimitiveRelation){
			boolean equal = equal( (PrimitiveRelation) obj);
			boolean inverseEqual = equal( ((PrimitiveRelation) obj).inverse);
			//System.err.println( equal+" "+ inverseEqual + " inverse equal " + this + " == " + ((PrimitiveRelation) obj).inverse + " ");
			if( equal || inverseEqual)
				return true;
			else return false;
		}
		return super.equals( obj);
	}
	private boolean equal( PrimitiveRelation p2){
		if(	this.preInd.equals( p2.preInd) && this.relation.equals( p2.relation) && this.postInd.equals( p2.postInd))
			return true;
		return false;
	}
	
	@Override
	public String toString(){
		String preIndName = OWLReferencesInterface.getOWLName( this.preInd);
		String postIndName = OWLReferencesInterface.getOWLName( this.postInd);
		String propertyName = this.getRelation().getPropertyName();
		String inversePropertyName  = this.getInverse().getRelation().getPropertyName(); 
		String preInvIndName = OWLReferencesInterface.getOWLName( this.getInverse().getPreInd());
		String postInvIndName = OWLReferencesInterface.getOWLName( this.getInverse().getPostInd());
		return "Primitive Relation{ (" + preIndName + " " + propertyName + " " + postIndName + ") = (" + preInvIndName + " " + inversePropertyName + " " +  postInvIndName + ") }";
	}
	
	public class RelationClassDescription{
		
		private String preClassName, postClassName;
		private OWLClass preClass, postClass;
		
		private Set< OWLClass> primitiveTypes;
		public RelationClassDescription( OWLReferences ontology, PrimitiveRelation relation){
			primitiveTypes = getAllPrimitiveTypes( ontology);
			preClass = findPrimitiveClass( ontology.getIndividualClasses( relation.getPreInd()));
			preClassName = ontology.getOWLObjectName( preClass);
			postClass = findPrimitiveClass( ontology.getIndividualClasses( relation.getPostInd()));
			postClassName = ontology.getOWLObjectName( postClass);
		}
		// given a set of classes like [T, Primitive, Cone] it will return [Cone]
		private OWLClass findPrimitiveClass( Set<OWLClass> classes){
			OWLClass primitiveClass = null;
			Boolean classFound = false;
			for( OWLClass cl : classes){
				for( OWLClass clPrim : primitiveTypes){
					if( OWLReferencesInterface.getOWLName( cl).equals( OWLReferencesInterface.getOWLName( clPrim))){
						primitiveClass = cl;
						classFound = true;
						break;
					}
				}
				if( classFound)
					break;
			}
			return primitiveClass;
		}
		private Set< OWLClass> getAllPrimitiveTypes( OWLReferences ontology){
			// get all sub-primitive classes
			Set< OWLClass> primitiveTypes = ontology.getSubClassOf( SceneIndividualCreator.CLASS_NAME_PRIMITIVE);
			for( int i = 0; i < SceneIndividualCreator.NO_PRIMITIVE_CLASS_NAME.length; i++){
				Set< OWLClass> subPrimitiveTypes = ontology.getSubClassOf( SceneIndividualCreator.NO_PRIMITIVE_CLASS_NAME[ i]);
				primitiveTypes.remove( ontology.getOWLClass( SceneIndividualCreator.NO_PRIMITIVE_CLASS_NAME[ i]));
				for( OWLClass cl : subPrimitiveTypes)
					primitiveTypes.add( cl);
			}
			return primitiveTypes;
		}

		public String getPreClassName() {
			return preClassName;
		}
		public String getPostClassName() {
			return postClassName;
		}
		public OWLClass getPreClass() {
			return preClass;
		}
		public OWLClass getPostClass() {
			return postClass;
		}
		
		@Override
		public String toString() {
			return "RelationClassDescription [preClassName=" + preClassName
					+ ", postClassName=" + postClassName + ", preClass="
					+ preClass + ", postClass=" + postClass + "]";
		}
	}
}
