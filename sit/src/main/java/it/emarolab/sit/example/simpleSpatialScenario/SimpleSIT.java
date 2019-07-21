package it.emarolab.sit.example.simpleSpatialScenario;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.sit.core.RelationInterface;
import it.emarolab.sit.core.SITInterface;
import it.emarolab.sit.core.owloopDescriptor.ActualSceneDescriptor;
import it.emarolab.sit.core.owloopDescriptor.MemorySceneClassified;
import it.emarolab.sit.core.owloopDescriptor.MemorySceneDescriptor;

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
 * <b>date</b>:        21/07/19 <br>
 * </small></div>
 */
public class SimpleSIT implements SITInterface {

    private Set<? extends RelationInterface> relations;
    private OWLReferences ontology;
    private ActualSceneDescriptor actualSceneDescriptor;
    private Set<MemorySceneClassified> storedRecognition;
    private MemorySceneDescriptor learned;

    public SimpleSIT(Set<? extends RelationInterface> relations, OWLReferences ontology) {
        this.relations = relations;
        this.ontology = ontology;
        this.actualSceneDescriptor = new ActualSceneDescriptor("S", ontology);
        encodeScene();
    }

    @Override
    public void encodeScene() {
        SITInterface.super.encodeScene();
        ontology.applyOWLManipulatorChanges();
        actualSceneDescriptor.reason();
        actualSceneDescriptor.readExpressionAxioms();
    }

    @Override
    public MemorySceneDescriptor learn(String sceneName, OWLReferences ontology){
        MemorySceneDescriptor learned = SITInterface.super.learn(sceneName, ontology);
        ontology.applyOWLManipulatorChanges();
        learned.readExpressionAxioms();
        this.learned = learned;
        return learned;
    }

    @Override
    public void cleanScene(){
        SITInterface.super.cleanScene();
        ontology.applyOWLManipulatorChanges();
    }

    @Override
    public Set<? extends RelationInterface> getRelations() {
        return relations;
    }

    @Override
    public ActualSceneDescriptor getActualScene() {
        return actualSceneDescriptor;
    }


    @Override
    public Set<MemorySceneClassified> recognise(double similarityThreshold) {
        Set<MemorySceneClassified> out = SITInterface.super.recognise(similarityThreshold);
        this.storedRecognition = out;
        return out;
    }

    // they might be null
    public Set<MemorySceneClassified> getRecognition(){
        return storedRecognition;
    }
    public MemorySceneDescriptor getLearned(){
        return learned;
    }
}
