package it.emarolab.sit.example.core;

import it.emarolab.amor.owlInterface.OWLReferences;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;

/**
 * ...
 * <p>
 * ...
 * <p>
 * <div style="text-align:center;"><small>
 * <b>File</b>:        ${FILE} <br>
 * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
 * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
 * <b>affiliation</b>: DIBRIS, EMAROLab, University of Genoa. <br>
 * <b>date</b>:        21/07/19 <br>
 * </small></div>
 */
public interface RelationInterface<E extends ElementInterface> {

    E getDomainElement();
    E getRangeElement();
    String getRelationName();

    OWLReferences getOntology();

    default OWLNamedIndividual getDomain(){
        return getOntology().getOWLIndividual( getDomainElement().getInstanceName());
    }
    default OWLNamedIndividual getRange(){
        return getOntology().getOWLIndividual( getRangeElement().getInstanceName());
    }
    default OWLObjectProperty getRelation(){
        return getOntology().getOWLObjectProperty( getRelationName());
    }
}
