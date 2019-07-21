package it.emarolab.sit.core;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.swing.mxGraphComponent;
import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.amor.owlInterface.SemanticRestriction;
import it.emarolab.owloop.core.Axiom;
import it.emarolab.sit.core.owloopDescriptor.ActualSceneDescriptor;
import it.emarolab.sit.core.owloopDescriptor.MemorySceneClassified;
import it.emarolab.sit.core.owloopDescriptor.MemorySceneDescriptor;
import it.emarolab.sit.core.owloopDescriptor.SceneElementDescription;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.ListenableDirectedGraph;
import org.semanticweb.owlapi.model.OWLClass;

import javax.swing.*;
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
        for ( ElementInterface e : getAllElements()){
            SceneElementDescription elementDescr = new SceneElementDescription(e.getInstanceName(), actualScene.getOntology());
            elementDescr.addTypeIndividual( e.getTypeName());
            for ( ElementInterface e1 : getAllElements())
                if ( ! e.equals( e1))
                    elementDescr.addDisjointIndividual( e1.getInstanceName());
            elementDescr.writeExpressionAxioms();
        }
        actualScene.addTypeIndividual( SCENE_ROOT);
        actualScene.writeExpressionAxioms();
    }

    default Set<MemorySceneClassified> recognise(double similarityThreshold){
        Set<MemorySceneClassified> recognised = new HashSet<>();
        for( MemorySceneDescriptor m : getActualScene().buildTypeIndividual()){
            double similarity = 0;
            double actualCardinality = getActualScene().getCardinality();
            if( actualCardinality != 0)
                similarity = m.getCardinality() / actualCardinality;
            //System.out.println( getActualScene().getGroundInstanceName() + " (" + actualCardinality + ") "
            // + m.getGroundInstanceName() + " (" + m.getCardinality() + ") = " + similarity);
            recognised.add( new MemorySceneClassified( m, similarity, similarityThreshold));
        }
        return recognised;
    }
    default boolean shouldLearn(Set<MemorySceneClassified> classified){
        for ( MemorySceneClassified cls : classified)
            if ( cls.highConfidence())
                return false;
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

    default Set<ElementInterface> getAllElements(){
        ActualSceneDescriptor actualScene = getActualScene();
        Set<ElementInterface> allElements = new HashSet<>();
        for ( RelationInterface r : getRelations()) {
            actualScene.addObject(reifyRelation(r), r.getRangeElement().getInstanceName());
            allElements.add( r.getRangeElement());
            allElements.add( r.getDomainElement());
        }
        return allElements;
    }

    default void cleanScene(){
        OWLReferences ont = getActualScene().getOntology();
        for ( ElementInterface e : getAllElements()){
            ont.removeIndividual( e.getInstanceName());
        }
        ont.removeIndividual( getActualScene().getInstance());
        getActualScene().writeExpressionAxioms();
    }

    default ListenableDirectedGraph<MemorySceneDescriptor, MemoryEdge> getMemory(){
        ListenableDirectedGraph<MemorySceneDescriptor, MemoryEdge>
                memory = new ListenableDirectedGraph<>( MemoryEdge.class);

        MemorySceneDescriptor root = new MemorySceneDescriptor(SCENE_ROOT, getActualScene().getOntology());
        root.readExpressionAxioms();

        // set vertices
        Set<MemorySceneDescriptor> builtSub = root.buildSubConcept();
        for ( MemorySceneDescriptor item : builtSub)
            if( !item.getInstance().isOWLNothing())
                memory.addVertex( item);

        // set edges
        for ( MemorySceneDescriptor item : builtSub){
            OWLClass parent = item.getInstance();
            // find parent vertex in graph
            MemorySceneDescriptor parentItem = null;
            for( MemorySceneDescriptor v : memory.vertexSet()){
                if ( v.getInstance().equals( parent)) {
                    parentItem = v;
                    break;
                }
            }
            if ( parentItem != null) {
                Axiom.EntitySet<OWLClass> childern = item.getSubConcepts();
                for (OWLClass child : childern) {
                    if( !child.isOWLNothing()) {
                        MemorySceneDescriptor childItem = null;
                        // find child vertex in graph
                        for (MemorySceneDescriptor v : memory.vertexSet()) {
                            if (v.getInstance().equals(child)) {
                                childItem = v;
                                break;
                            }
                        }

                        if (childItem != null)
                            memory.addEdge(parentItem, childItem);
                    }
                }
            }
        }
        return memory;

    }

    default void showMemory(){
        new GraphShower().showMemory(getMemory());
    }
    default void showMemory(ListenableDirectedGraph<MemorySceneDescriptor, MemoryEdge> memory){
        new GraphShower().showMemory( memory);
    }

    class GraphShower{

        private JFrame frame;

        public void showMemory( ListenableDirectedGraph<MemorySceneDescriptor, MemoryEdge> hierarchy) {
            SwingUtilities.invokeLater(() -> {
                frame = new JFrame("Scene Hierarchy");
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                updateShowing( hierarchy);
            });
        }

        // used by showMemory for using a different thread.
        private void updateShowing( ListenableDirectedGraph<MemorySceneDescriptor, MemoryEdge> hierarchy) {
            SwingUtilities.invokeLater(() -> {
                if (frame == null) {
                    System.err.println("null frame, call showMemory before to update a graph!!");
                    return;
                }
                if (hierarchy == null) {
                    System.err.println("null semantic, build semantic before to update a graph!!");
                    return;
                }

                JGraphXAdapter<MemorySceneDescriptor, MemoryEdge>
                        graphAdapter = new JGraphXAdapter<>(hierarchy);

                graphAdapter.setCellsDisconnectable( false);

                mxIGraphLayout layout = new mxHierarchicalLayout(graphAdapter);
                layout.execute(graphAdapter.getDefaultParent());

                mxGraphComponent graphPanel = new mxGraphComponent(graphAdapter);
                graphPanel.setZoomPolicy(mxGraphComponent.ZOOM_POLICY_WIDTH);
                frame.add( graphPanel);

                frame.pack();
                frame.setLocationByPlatform(true);
                frame.setVisible(true);
            });
        }
    }


    class MemoryEdge extends DefaultEdge {

        public MemoryEdge() {
            super();
        }

        @Override
        public String toString() {
            return "";
        }
    }
}
