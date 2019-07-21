package it.emarolab.sit.core;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.amor.owlInterface.SemanticRestriction;
import it.emarolab.sit.owloopDescriptor.ActualSceneDescriptor;
import it.emarolab.sit.owloopDescriptor.MemorySceneDescriptor;
import it.emarolab.sit.owloopDescriptor.SceneElementDescription;

import java.util.HashSet;
import java.util.Set;

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
public interface SITInterface {

    String SCENE_ROOT = "SCENE";

    Set<? extends RelationInterface> getRelations(); // get domain, rel, range
    ActualSceneDescriptor getActualScene();

    default String reifyRelation(RelationInterface r){
        return r.getRelationName() + r.getDomainElement().getTypeName();
    }

    default void encodeScene(){
        ActualSceneDescriptor actualScene = getActualScene();
        Set<ElementInterface> allElements = new HashSet<>();
        for ( RelationInterface r : getRelations()) {
            actualScene.addObject(reifyRelation(r), r.getRangeElement().getInstanceName());
            allElements.add( r.getRangeElement());
            allElements.add( r.getDomainElement());
        }
        for ( ElementInterface e : allElements){
            SceneElementDescription elementDescr = new SceneElementDescription(e.getInstanceName(), actualScene.getOntology());
            elementDescr.addTypeIndividual( e.getTypeName());
            elementDescr.writeExpressionAxioms();
        }
        actualScene.addTypeIndividual( SCENE_ROOT);
        actualScene.writeExpressionAxioms();
        actualScene.getOntology().applyOWLManipulatorChanges();
        actualScene.reason();
        actualScene.readExpressionAxioms();
    }

    default Set<MemorySceneDescriptor> recognise(){
        Set<MemorySceneDescriptor> out = getActualScene().buildTypeIndividual();
        for (MemorySceneDescriptor d : out)
            d.readExpressionAxioms();
        return out;
    }

    default boolean shouldLearn(double confidence){
        for( MemorySceneDescriptor m : recognise()){
            double c = 0;
            if( m.getCardinality() != 0)
                c = getActualScene().getCardinality() / m.getCardinality();
            if( c > confidence)
                return false;
        }
        return true;
    }

    default MemorySceneDescriptor learn(String sceneName, OWLReferences ontology){
        MemorySceneDescriptor learned = new MemorySceneDescriptor(sceneName, ontology);
        learned.addSuperConcept( SCENE_ROOT);
        for ( RelationInterface r : getRelations()) {
            boolean found = false;
            for (SemanticRestriction s : learned.getRestrictionConcepts()){
                if( s instanceof  SemanticRestriction.ClassRestrictedOnMinObject) {
                    SemanticRestriction.ClassRestrictedOnMinObject s1 = (SemanticRestriction.ClassRestrictedOnMinObject) s;
                    if (s1.getPropertyName().equals(reifyRelation(r)) & s.getValueName().equals(r.getRangeElement().getTypeName())) {
                        s1.setCardinality(s1.getCardinality() + 1);
                        found = true;
                        break;
                    }
                }
            }
            if ( ! found)
                learned.addMinObjectRestriction( reifyRelation(r), 1, r.getRangeElement().getTypeName());
        }
        learned.writeExpressionAxioms();
        return learned;
    }
}
