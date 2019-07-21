package it.emarolab.sit.example.tableScenario.sceneRelation;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.sit.example.simpleSpatialScenario.Point3D;
import it.emarolab.sit.example.tableScenario.sceneElement.SpatialObject;

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
 * <b>date</b>:        21/07/19 <br>
 * </small></div>
 */
public class TableEvaluator {

    private static final double INTERVAL = .05; // in meters

    private Set<SpatialObject> elements = new HashSet<>();
    private Set<ConnectedRelation> relations = new HashSet<>();
    private OWLReferences ontology = null;

    public TableEvaluator(){}
    public TableEvaluator(Set<SpatialObject> elements, OWLReferences ontology){
        this.elements = elements;
        this.ontology = ontology;
    }


    public Set<SpatialObject> getElements() {
        return elements;
    }

    public Set<ConnectedRelation> getRelations() {
        return relations;
    }

    public void evaluate(){
        for (SpatialObject g1 : elements) {
            for (SpatialObject g2 : elements) {
                if (!g1.equals(g2)) {
                    Point3D c1 = g1.getCenter();
                    Point3D c2 = g2.getCenter();
                    if (c1 != null & c2 != null) {
                        double dx = (c1.getX() - c2.getX());
                        double dy = (c1.getY() - c2.getY());
                        double dz = (c1.getZ() - c2.getZ());
                        double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
                        if (distance < INTERVAL) {
                            relations.add(new ConnectedRelation(g1, "connected", g2, ontology));
                            relations.add(new ConnectedRelation(g2, "connected", g1, ontology));
                        }
                    }
                }
            }
        }
    }
}
