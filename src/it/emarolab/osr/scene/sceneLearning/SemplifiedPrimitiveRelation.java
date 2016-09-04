package it.emarolab.osr.scene.sceneLearning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import it.emarolab.amor.owlDebugger.Logger;
import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.osr.scene.sceneRecognition.PrimitiveRelation;

public class SemplifiedPrimitiveRelation {
	
	private static OWLReferences ontology; // set in the class constructor  
	
	private static Logger log = new Logger( SemplifiedPrimitiveRelation.class, true);
	
	// it solve the problem that ( A hasProp exactly 1 B) and ( A hasProp exactly 3 B) must become ( A hasProp exactly 4 B)		
	private OWLClass clExpressionOf; // i.e. it is A
	//private Set< ClassExactCardinality> clValues; // i.e. it is B
	private Map< OWLClass, Integer> clValues; // class <-> cardinality relation
	private OWLObjectProperty property; // i.e. it is hasProp
	
	public SemplifiedPrimitiveRelation( OWLClass clExpressionOf, OWLObjectProperty property, OWLReferences ontology) {
		SemplifiedPrimitiveRelation.ontology = ontology;
		this.clExpressionOf = clExpressionOf;
		this.property = property;
		this.clValues = new HashMap< OWLClass, Integer>();
	}
		
	public OWLClass getClassExpressionOf() {
		return clExpressionOf;
	}
	public OWLObjectProperty getProperty() {
		return property;
	}
	public Map<OWLClass, Integer> getClassValues() {
		return clValues;
	}
	
	@Override
	// if the hasSceneComponent_ property are the same
	public boolean equals(Object obj) {
		if( obj instanceof PrimitiveRelation){
			PrimitiveRelation p = ( PrimitiveRelation) obj;
			if( p.getRelation().getSceneProperty( ontology).equals( this.getProperty()))
				return true;
			return false;
		} return super.equals(obj);
	}
	
	@Override
	public String toString() {
		String out = "Semplified Primitive Relation{ ";
		out += OWLReferences.getOWLName( this.clExpressionOf) + " ";
		out += OWLReferences.getOWLName( this.property) + " ";
		for( OWLClass e : clValues.keySet())
			out += "(=" +  clValues.get( e) + " " + OWLReferences.getOWLName( e) + ") , ";
		return out + "}";
	}

	public static List< SemplifiedPrimitiveRelation> semplifyPrimitiveRelation( List< PrimitiveRelation> primitiveRelations, String className, OWLReferences onto){
		OWLClass cl = onto.getOWLClass( className);
		List< SemplifiedPrimitiveRelation> out = new ArrayList< SemplifiedPrimitiveRelation>();
		for( PrimitiveRelation rel : primitiveRelations){
			// if the first time add it !!!!
			if( out.isEmpty())
				out.add( createNewSemplifiedRelation( cl, rel, onto));
			else{
				boolean found = false;
				for( SemplifiedPrimitiveRelation seRel : out){					
					if( seRel.equals( rel)){
						found = true;						
						if( rel.getIndividualClasses( onto).getPreClass().equals( rel.getIndividualClasses( onto).getPostClass()))
							updateSemplifiedClassRelation( seRel, rel, onto, 2, true);
						else {
							updateSemplifiedClassRelation( seRel, rel, onto, 1, true);
							updateSemplifiedClassRelation( seRel, rel, onto, 1, false);
						}
					}
				}
				if( ! found) // there is no such a relation... create it!!!!
					out.add( createNewSemplifiedRelation( cl, rel, onto));
			}
		}
		log.addDebugString( "{Semplified list: " + out.toString() + "}");
		return out;
	}
	
	private static SemplifiedPrimitiveRelation createNewSemplifiedRelation(  OWLClass baseClass, PrimitiveRelation rel, OWLReferences onto){
		SemplifiedPrimitiveRelation out =  new SemplifiedPrimitiveRelation( baseClass, rel.getRelation().getSceneProperty( onto), onto);
		if( rel.getIndividualClasses( onto).getPreClass().equals( rel.getIndividualClasses( onto).getPostClass()))
			out.getClassValues().put( rel.getIndividualClasses( onto).getPostClass(), 2);//.addClassValue( classCard);
		else{
			out.getClassValues().put( rel.getIndividualClasses( onto).getPreClass(), 1);//out.addClassValue( preClassCard);
			out.getClassValues().put( rel.getIndividualClasses( onto).getPostClass(), 1);//out.addClassValue( postClassCard);
		}
		return out;
	}
		
	private static void updateSemplifiedClassRelation( SemplifiedPrimitiveRelation seRel, PrimitiveRelation rel, OWLReferences onto, int cardinality, boolean pre){ // pre=true post=false
		OWLClass cl;
		if( pre)
			 cl = rel.getIndividualClasses( onto).getPreClass();
		else cl = rel.getIndividualClasses( onto).getPostClass();
				
		if( ! seRel.getClassValues().containsKey( cl))
			// not contains create new
			seRel.getClassValues().put( cl, cardinality);
		else {
			Integer actualCardinality = seRel.getClassValues().get( cl);
			if( actualCardinality == null) // it should not happen anyway
				actualCardinality = 0;
			seRel.getClassValues().put(cl, actualCardinality + cardinality); 
		}
	}
}