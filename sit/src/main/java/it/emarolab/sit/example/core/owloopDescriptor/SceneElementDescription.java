package it.emarolab.sit.example.core.owloopDescriptor;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.owloop.descriptor.construction.descriptorEntitySet.DescriptorEntitySet;
import it.emarolab.owloop.descriptor.construction.descriptorExpression.IndividualExpression;
import it.emarolab.owloop.descriptor.construction.descriptorGround.ConceptGround;
import it.emarolab.owloop.descriptor.construction.descriptorGround.IndividualGround;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import java.util.ArrayList;
import java.util.List;

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
public class SceneElementDescription extends IndividualGround
        implements  IndividualExpression.Type<SceneElementTypeDescription>,
                    IndividualExpression.Disjoint<SceneElementDescription>{

    DescriptorEntitySet.Concepts elements = new DescriptorEntitySet.Concepts();
    DescriptorEntitySet.Individuals disjoint = new DescriptorEntitySet.Individuals();

    public SceneElementDescription(String instanceName, OWLReferences onto) {
        super(instanceName, onto);
    }

    public SceneElementDescription(OWLNamedIndividual instance, OWLReferences ontology) {
        super(instance, ontology);
    }

    @Override
    public SceneElementTypeDescription getNewIndividualType(OWLClass instance, OWLReferences ontology) {
        return new SceneElementTypeDescription(instance, ontology);
    }
    // TODO check missmatch (getNew... v.s. get...Descriptor(s)) names in owloop

    @Override
    public DescriptorEntitySet.Concepts getIndividualTypes() {
        return elements;
    }

    @Override
    public SceneElementDescription getNewDisjointIndividual(OWLNamedIndividual instance, OWLReferences ontology) {
        return new SceneElementDescription(instance, ontology);
    }

    @Override
    public DescriptorEntitySet.Individuals getDisjointIndividuals() {
        return disjoint;
    }

    @Override
    public List<MappingIntent> readExpressionAxioms() {
        List<MappingIntent> r = IndividualGround.Type.super.readExpressionAxioms();
        r.addAll( IndividualGround.Disjoint.super.readExpressionAxioms());
        return r;
    }

    @Override
    public List<MappingIntent> writeExpressionAxioms() {
        List<MappingIntent> r = IndividualGround.Type.super.writeExpressionAxioms();
        r.addAll( IndividualGround.Disjoint.super.writeExpressionAxioms());
        return r;
    }


    @Override
    public String toString() {
        return this.getClass().getSimpleName() +
                "{\n\t" + this.getGround() +
                ":\t\t∈ " + this.elements +
                "\t\t≠  " + this.disjoint + "\n}";
    }
}

class SceneElementTypeDescription extends ConceptGround {

    public SceneElementTypeDescription(OWLClass instance, OWLReferences onto) {
        super(instance, onto);
    }

    @Override
    public List<MappingIntent> readExpressionAxioms() {
        System.err.println( this.getClass().getSimpleName() + " cannot be read!");
        return new ArrayList<>();
    }

    @Override
    public List<MappingIntent> writeExpressionAxioms() {
        System.err.println( this.getClass().getSimpleName() + " cannot be written!");
        return new ArrayList<>();
    }
}
