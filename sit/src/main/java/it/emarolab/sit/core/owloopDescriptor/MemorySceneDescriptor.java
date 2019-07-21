package it.emarolab.sit.core.owloopDescriptor;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.amor.owlInterface.SemanticRestriction;
import it.emarolab.owloop.descriptor.construction.descriptorEntitySet.DescriptorEntitySet;
import it.emarolab.owloop.descriptor.construction.descriptorExpression.ConceptExpression;
import it.emarolab.owloop.descriptor.construction.descriptorGround.ConceptGround;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

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
public class MemorySceneDescriptor extends ConceptGround
        implements ConceptExpression.Sub<MemorySceneDescriptor>,
        ConceptExpression.Super<MemorySceneDescriptor>,
        ConceptExpression.Instance<ActualSceneDescriptor>,
        ConceptExpression.Restriction{

    private DescriptorEntitySet.Restrictions restriction = new DescriptorEntitySet.Restrictions();
    private DescriptorEntitySet.Concepts subConcept = new DescriptorEntitySet.Concepts();
    private DescriptorEntitySet.Concepts superConcept = new DescriptorEntitySet.Concepts();
    private DescriptorEntitySet.Individuals classifiedIndividual = new DescriptorEntitySet.Individuals();

    public MemorySceneDescriptor(String instanceName, OWLReferences onto) {
        super(instanceName, onto);
    }

    public MemorySceneDescriptor(OWLClass owlClass, OWLReferences o) {
        super(owlClass, o);
    }

    public int getCardinality() {
        int cardinality = 0;
        for(SemanticRestriction res : getRestrictionConcepts()){
            if ( res instanceof  SemanticRestriction.ClassRestrictedOnMinObject) {
                SemanticRestriction.ClassRestrictedOnMinObject minRes = (SemanticRestriction.ClassRestrictedOnMinObject) res;
                cardinality += minRes.getCardinality();
            }
        }
        return cardinality;
    }

    @Override
    public ActualSceneDescriptor getIndividualDescriptor(OWLNamedIndividual individual, OWLReferences o) {
        return new ActualSceneDescriptor( individual, o);
    }

    @Override
    public EntitySet<OWLNamedIndividual> getIndividualInstances() {
        return classifiedIndividual;
    }

    @Override
    public MemorySceneDescriptor getSubConceptDescriptor(OWLClass owlClass, OWLReferences o) {
        return new MemorySceneDescriptor( owlClass, o);
    }

    @Override
    public EntitySet<OWLClass> getSubConcepts() {
        return subConcept;
    }

    @Override
    public MemorySceneDescriptor getSuperConceptDescriptor(OWLClass owlClass, OWLReferences o) {
        return new MemorySceneDescriptor( owlClass, o);
    }

    @Override
    public EntitySet<OWLClass> getSuperConcepts() {
        return superConcept;
    }

    @Override
    public DescriptorEntitySet.Restrictions getRestrictionConcepts() {
        return restriction;
    }

    @Override
    public List<MappingIntent> readExpressionAxioms() {
        List<MappingIntent> r = Restriction.super.readExpressionAxioms();
        r.addAll( ConceptExpression.Sub.super.readExpressionAxioms());
        r.addAll( ConceptExpression.Super.super.readExpressionAxioms());
        r.addAll( Instance.super.readExpressionAxioms());
        return r;
    }

    @Override
    public List<MappingIntent> writeExpressionAxioms() {
        List<MappingIntent> r = Restriction.super.writeExpressionAxioms();
        r.addAll( ConceptExpression.Super.super.writeExpressionAxioms());
        r.addAll( ConceptExpression.Sub.super.writeExpressionAxioms());
        r.addAll( Instance.super.writeExpressionAxioms());
        return r;
    }

    public void printDescriptions() {
        System.out.println( this.getClass().getSimpleName() +
                "{\n\t" + this.getGround() +
                ":\n\t\t⇐ " + this.classifiedIndividual +
                "\n\t\t≐ " + this.restriction +
                "\n\t\t⊃ " + this.subConcept  +
                "\n\t\t⊂ " + this.superConcept + "\n}");
    }

    @Override
    public String toString() {
        return getGroundInstanceName();
    }
}
