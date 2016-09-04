package it.emarolab.osr.scene.sceneLearning;

import org.semanticweb.owlapi.model.OWLClass;


class ClassExactCardinality{	
	OWLClass classValue;
	int exactCardinality = 0;
	
	public ClassExactCardinality(OWLClass classValue, int exactCardinality) {
		this.classValue = classValue;
		this.exactCardinality = exactCardinality;
	}

	public OWLClass getClassValue() {
		return classValue;
	}
	public int getExactCardinality() {
		return exactCardinality;
	}
	public int addToExactCardinality( int more){
		this.exactCardinality += more;
		return this.exactCardinality;
	}
}	

