package it.emarolab.sit.example.example;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.amor.owlInterface.OWLReferencesInterface;
import it.emarolab.sit.example.core.RelationInterface;
import it.emarolab.sit.example.core.SITInterface;
import it.emarolab.sit.example.core.owloopDescriptor.MemorySceneClassified;
import it.emarolab.sit.example.core.owloopDescriptor.MemorySceneDescriptor;

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
public abstract class SITExampleBase<R extends RelationInterface> {

    private long sceneNameCnt = 0;
    public static final double CONFIDENCE_TH = .9;

    private OWLReferences ontoRef;

    public SITExampleBase(String refName, String ontoFilePath, String ontoIRIPath){
        this.ontoRef = OWLReferencesInterface.OWLReferencesContainer
                .newOWLReferenceFromFile( refName, ontoFilePath, ontoIRIPath, true);
        this.ontoRef.setOWLManipulatorBuffering( true);
    }

    protected abstract SITInterface getSIT(Set<R> relations);

    public SITInterface evaluateScene(Set<R> relations){
        return evaluateScene( relations, "SCENE-" + sceneNameCnt++);
    }
    public SITInterface evaluateScene(Set<R> relations, String sceneName){
        System.out.print( "******************");
        SITInterface sit = getSIT( relations);
        if( ! relations.isEmpty()) {
            Set<MemorySceneClassified> recognition = sit.recognise(CONFIDENCE_TH);
            if (sit.shouldLearn(recognition)) {
                MemorySceneDescriptor learned = sit.learn(sceneName, ontoRef);
                System.out.println("\t\t\tLEARNING");
                learned.printDescriptions();
            } else {
                System.out.println("\t\t\tRECOGNISING");
                for (MemorySceneClassified rec : recognition)
                    rec.printDescriptions();
            }
            sit.cleanScene();
        }
        return sit;
    }

    public OWLReferences getOntology() {
        return ontoRef;
    }

    public static final String ONTO_FILE_BASE_PATH = "src/main/resources/";
    public static final String ONTO_FILE_NEME = ONTO_FILE_BASE_PATH + "simpleSIT.owl";
    public static final String ONTO_IRI = "http://www.emarolab.it/sit/simple";

}

