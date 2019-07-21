package it.emarolab.sit.owloopDescriptor;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.owloop.descriptor.construction.descriptorEntitySet.DescriptorEntitySet;
import it.emarolab.owloop.descriptor.construction.descriptorExpression.IndividualExpression;
import it.emarolab.owloop.descriptor.construction.descriptorGround.IndividualGround;
import it.emarolab.owloop.descriptor.construction.descriptorGround.ObjectPropertyGround;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;

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
 * <b>date</b>:        20/07/19 <br>
 * </small></div>
 */
public class ActualSceneDescriptor extends IndividualGround
        implements IndividualExpression.ObjectLink<SceneRelationDescription>,
        IndividualExpression.Type<MemorySceneDescriptor>{

    DescriptorEntitySet.ObjectLinksSet sceneRelation = new DescriptorEntitySet.ObjectLinksSet();
    DescriptorEntitySet.Concepts types = new DescriptorEntitySet.Concepts();

    public ActualSceneDescriptor(String individual, OWLReferences ontology) {
        super(individual, ontology);
    }

    public ActualSceneDescriptor(OWLNamedIndividual individual, OWLReferences ontology) {
        super(individual, ontology);
    }

    public int getCardinality() {
        int cardinality = 0;
        for(DescriptorEntitySet.ObjectLinks obj : getIndividualObjectProperties()){
            for (OWLNamedIndividual i : obj.getValues())
                cardinality++;
        }
        return cardinality;
    }

    @Override @Deprecated
    public SceneRelationDescription getNewIndividualObjectProperty(DescriptorEntitySet.ObjectLinks instance, OWLReferences ontology) {
        return new SceneRelationDescription( instance.getExpression(), ontology);
    }

    @Override
    public DescriptorEntitySet.ObjectLinksSet getIndividualObjectProperties() {
        return sceneRelation;
    }

    @Override
    public MemorySceneDescriptor getNewIndividualType(OWLClass instance, OWLReferences ontology) {
        return new MemorySceneDescriptor( instance, ontology);
    }

    @Override
    public DescriptorEntitySet.Concepts getIndividualTypes() {
        return types;
    }

    @Override
    public List<MappingIntent> readExpressionAxioms() {
        List<MappingIntent> r = IndividualExpression.ObjectLink.super.readExpressionAxioms();
        r.addAll( IndividualExpression.Type.super.readExpressionAxioms());
        return r;
    }

    @Override
    public List<MappingIntent> writeExpressionAxioms() {
        List<MappingIntent> r = IndividualExpression.ObjectLink.super.writeExpressionAxioms();
        r.addAll( IndividualExpression.Type.super.writeExpressionAxioms());
        return r;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() +
                "{\n\t" + this.getGround() +
                ":\t\t∈ " + this.types +
                "\n\t\t⊨ " + this.sceneRelation + "\n}";
    }
}

class SceneRelationDescription extends ObjectPropertyGround{

    public SceneRelationDescription(OWLObjectProperty instance, OWLReferences onto) {
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
