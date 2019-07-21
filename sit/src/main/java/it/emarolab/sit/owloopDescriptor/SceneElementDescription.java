package it.emarolab.sit.owloopDescriptor;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.owloop.descriptor.construction.descriptorEntitySet.DescriptorEntitySet;
import it.emarolab.owloop.descriptor.construction.descriptorExpression.IndividualExpression;
import it.emarolab.owloop.descriptor.construction.descriptorGround.ConceptGround;
import it.emarolab.owloop.descriptor.construction.descriptorGround.IndividualGround;
import org.semanticweb.owlapi.model.OWLClass;

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
        implements IndividualExpression.Type<SceneElementTypeDescription>{

    DescriptorEntitySet.Concepts elements = new DescriptorEntitySet.Concepts();

    public SceneElementDescription(String instanceName, OWLReferences onto) {
        super(instanceName, onto);
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
    public List<MappingIntent> readExpressionAxioms() {
        return IndividualGround.Type.super.readExpressionAxioms();
    }

    @Override
    public List<MappingIntent> writeExpressionAxioms() {
        return IndividualGround.Type.super.writeExpressionAxioms();
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() +
                "{\n\t" + this.getGround() +
                ":\t\t∈ " + this.elements + "\n}";
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
