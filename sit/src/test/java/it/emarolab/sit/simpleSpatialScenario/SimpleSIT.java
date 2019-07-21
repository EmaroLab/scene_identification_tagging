package it.emarolab.sit.simpleSpatialScenario;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.sit.core.SITInterface;
import it.emarolab.sit.owloopDescriptor.ActualSceneDescriptor;
import it.emarolab.sit.simpleSpatialScenario.sceneRelation.SpatialRelation;

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

    private Set<SpatialRelation> relations;
    private OWLReferences ontology;
    private ActualSceneDescriptor actualSceneDescriptor;

    public SimpleSIT(Set<SpatialRelation> relations, OWLReferences ontology) {
        this.relations = relations;
        this.ontology = ontology;
        this.actualSceneDescriptor = new ActualSceneDescriptor("S", ontology);
        encodeScene();
    }

    @Override
    public Set<SpatialRelation> getRelations() {
        return relations;
    }

    @Override
    public ActualSceneDescriptor getActualScene() {
        return actualSceneDescriptor;
    }
}
