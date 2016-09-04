package it.emarolab.osr.scene.sceneLearning;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.SWRLAtom;
import org.semanticweb.owlapi.model.SWRLClassAtom;
import org.semanticweb.owlapi.model.SWRLIndividualArgument;
import org.semanticweb.owlapi.model.SWRLObjectPropertyAtom;
import org.semanticweb.owlapi.model.SWRLRule;
import org.semanticweb.owlapi.model.SWRLVariable;

import it.emarolab.amor.owlInterface.OWLReferences;

public class SWRLmanager {

	private Set<SWRLAtom> antecedent, postecedent;
	private String ontologyIri;
	private OWLReferences ontology;
	
	public SWRLmanager( OWLReferences ontology){
		this.ontology = ontology;
		antecedent = new HashSet< SWRLAtom>();
		postecedent = new HashSet< SWRLAtom>();
		ontologyIri = ontology.getIriOntologyPath().toString();
	}
	
	// to get a variable
	protected SWRLVariable getVariable( String var){
		return ontology.getFactory().getSWRLVariable( IRI.create( ontologyIri + "#" + var));
	}
	protected SWRLIndividualArgument getConst( OWLNamedIndividual individual){
		 return( ontology.getFactory().getSWRLIndividualArgument( individual));
	}
	
	protected void addRoole(){
		SWRLRule rule = ontology.getFactory().getSWRLRule( antecedent, postecedent);
		ontology.applyOWLManipulatorChangesAddAxiom( rule);
	}
	
	// to look for individual into class (i.e. cl( ?var))
	private SWRLClassAtom addClassAtom( OWLClass cl, SWRLVariable var, Set<SWRLAtom> collection){
 		SWRLClassAtom classAtom = ontology.getFactory().getSWRLClassAtom( cl, var);
 		collection.add( classAtom);
 		return classAtom;
	}
	// to look for individual into class (i.e. cl( ?var))
	private SWRLClassAtom addClassAtom( OWLClass cl, SWRLIndividualArgument var, Set<SWRLAtom> collection){
 		SWRLClassAtom classAtom = ontology.getFactory().getSWRLClassAtom( cl, var);
 		collection.add( classAtom);
 		return classAtom;
	}
	protected SWRLClassAtom addClassCondition( OWLClass cl, SWRLVariable var){
 		return addClassAtom( cl, var, antecedent);
	}
	protected SWRLClassAtom addClassCondition( OWLClass cl, SWRLIndividualArgument var){
 		return addClassAtom( cl, var, antecedent);
	}
	protected SWRLClassAtom addClassInferation( OWLClass cl, SWRLVariable var){
 		return addClassAtom( cl, var, postecedent);
	}
	protected SWRLClassAtom addClassInferation( OWLClass cl, SWRLIndividualArgument var){
 		return addClassAtom( cl, var, postecedent);
	}
	
	// to look for the value of an object property (i.e. objProp())
	private SWRLObjectPropertyAtom addObjectPropertyAtom( SWRLVariable preInd, OWLObjectProperty prop, SWRLVariable postInd, Set<SWRLAtom> collection){
		SWRLObjectPropertyAtom dataRule = ontology.getFactory().getSWRLObjectPropertyAtom( prop, preInd, postInd);
		collection.add( dataRule);
		return dataRule;
	}
	protected SWRLObjectPropertyAtom addObjectPropertyCondition( SWRLVariable preInd, OWLObjectProperty prop, SWRLVariable postInd){
		return addObjectPropertyAtom( preInd, prop, postInd, antecedent);
	}
	protected SWRLObjectPropertyAtom addObjectPropertyInferation( SWRLVariable preInd, OWLObjectProperty prop, SWRLVariable postInd){
		return addObjectPropertyAtom( preInd, prop, postInd, postecedent);
	}
	
	// to look for the value of an object property (i.e. objProp())
	private SWRLObjectPropertyAtom addObjectPropertyAtom( SWRLIndividualArgument preInd, OWLObjectProperty prop, SWRLVariable postInd, Set<SWRLAtom> collection){
		SWRLObjectPropertyAtom dataRule = ontology.getFactory().getSWRLObjectPropertyAtom( prop, preInd, postInd);
		collection.add( dataRule);
	    return dataRule;
	}
	protected SWRLObjectPropertyAtom addObjectPropertyCondition( SWRLIndividualArgument preInd, OWLObjectProperty prop, SWRLVariable postInd){
		return addObjectPropertyAtom( preInd, prop, postInd, antecedent);
	}
	protected SWRLObjectPropertyAtom addObjectPropertyInferation( SWRLIndividualArgument preInd, OWLObjectProperty prop, SWRLVariable postInd){
		return addObjectPropertyAtom( preInd, prop, postInd, postecedent);
	}
	
	// to look for the value of an object property (i.e. objProp())
	private SWRLObjectPropertyAtom addObjectPropertyAtom( SWRLVariable preInd, OWLObjectProperty prop, SWRLIndividualArgument postInd, Set<SWRLAtom> collection){
		SWRLObjectPropertyAtom dataRule = ontology.getFactory().getSWRLObjectPropertyAtom( prop, preInd, postInd);
		collection.add( dataRule);
	    return dataRule;
	}
	protected SWRLObjectPropertyAtom addObjectPropertyCondition( SWRLVariable preInd, OWLObjectProperty prop, SWRLIndividualArgument postInd){
		return addObjectPropertyAtom( preInd, prop, postInd, antecedent);
	}
	protected SWRLObjectPropertyAtom addObjectPropertyInferation( SWRLVariable preInd, OWLObjectProperty prop, SWRLIndividualArgument postInd){
		return addObjectPropertyAtom( preInd, prop, postInd, postecedent);
	}
}
