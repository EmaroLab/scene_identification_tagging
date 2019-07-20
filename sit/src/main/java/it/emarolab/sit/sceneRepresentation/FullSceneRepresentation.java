package it.emarolab.sit.sceneRepresentation;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.sit.SITBase;
import it.emarolab.sit.SpatialSimplifier;
import it.emarolab.sit.owloopDescriptor.SceneClassDescriptor;
import it.emarolab.sit.owloopDescriptor.SceneIndividualDescriptor;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import java.util.*;

// copied from SceneRepresentation
public class FullSceneRepresentation extends SceneRepresentation
        implements SITBase{

    //TODO updated documentation
    //TODO avoid copied code with SceneRepresentation

    //private Set< SpatialRelation> relations;
    //private SceneIndividualDescriptor sceneDescriptor;
    //private SceneClassDescriptor bestRecognitionDescriptor;
    //private double recognitionConfidence;
    //private long time = System.currentTimeMillis();
    //private static long ID;
    //private boolean addTime = false;

    public FullSceneRepresentation(SpatialSimplifier objects, OWLReferences ontoRef){
        super();
        initialize( objects, ontoRef);
        applyScene( sceneDescriptor, relations);
        computeRecognitionConfidence( sceneDescriptor);
    }

    // add the scene individual to the ontology
    private void applyScene(SceneIndividualDescriptor sceneDescriptor, Set<SpatialRelation> relations){
        for ( SpatialRelation r : relations){
            OWLObjectProperty spatialRelation = getSpatialRelation( sceneDescriptor.getOntology(), r.getSubject(), r.getRelation());
            sceneDescriptor.addObject( spatialRelation, r.getObject().getIndividual());
            OWLObjectProperty spatialInverseRelation = getSpatialRelation( sceneDescriptor.getOntology(), r.getObject(), r.getInverseRelation());
            if ( ! spatialInverseRelation.equals( spatialRelation))
                sceneDescriptor.addObject( spatialInverseRelation, r.getSubject().getIndividual());
        }
        sceneDescriptor.addTypeIndividual( CLASS.SCENE);
        if (addTime)
            sceneDescriptor.addData( DATA_PROPERTY.TIME, time, true);
        sceneDescriptor.writeReadExpressionAxioms( true);
    }

    // get spatial relation between a Scene and an Object from the spatial relation between two Objects
    private OWLObjectProperty getSpatialRelation(OWLReferences ontology, SpatialAtom subject, OWLObjectProperty relation) {
        String relationName = relation.getIRI().getRemainder().get();
        Optional<String> type = subject.getType().getIRI().getRemainder();
        String sceneRelationName =  type.get() + relationName;
        return ontology.getOWLObjectProperty( sceneRelationName);
    }

    /**
     * It uses the {@link #getSceneDescriptor()} as a template
     * to create a new abstract scene class the perfectly fits its recognition
     * (i.e.: {@code {@link #getRecognitionConfidence()} = 1}).
     * This method calls the reasoning task to place the new scene in the {@link CLASS#SCENE}
     * hierarchy and updates the {@link #getRecognitionConfidence()} and {@link #getSceneDescriptor()}.
     * @param newSceneName the name of the new scene representation to learn
     */
    @Override
    public void learn(String newSceneName) {
        SceneClassDescriptor learned = new SceneClassDescriptor( newSceneName, sceneDescriptor.getOntology());

        // semplify relations
        // Map< has-scene_...., Set<SpatialAtom>
        Map<OWLObjectProperty, Set<SpatialAtom>> toLearn = new HashMap<>();
        for ( SpatialRelation r : relations){
            learnAtom( toLearn, r.getSubject(), r.getRelation(), r.getObject());
            //learnAtom( toLearn, r.getInverseRelation(), r.getSubject());
        }

        // count number of types
        Set<LearningData> shapeCardinality = new HashSet<>();
        for ( OWLObjectProperty p : toLearn.keySet()){
            for ( SpatialAtom a : toLearn.get( p)){
                LearningData l = new LearningData( p, a.getType());
                boolean found = false;
                for (LearningData stored : shapeCardinality){
                    if ( stored.equals( l)){
                        stored.increaseCardinality();
                        found = true;
                        break;
                    }
                }
                if ( ! found)
                    shapeCardinality.add( l);
            }
        }

        // update the ontology
        for ( LearningData learning : shapeCardinality)
            learned.addMinObjectRestriction( learning.getRelation(), learning.getCardinality(), learning.getShape());
        learned.addSuperConcept( CLASS.SCENE);
        learned.writeReadExpressionAxioms( true); // cal reasoning

        // update this internal class
        sceneDescriptor.readExpressionAxioms();
        bestRecognitionDescriptor = learned;
        recognitionConfidence = 1;
    }
    // called twice for inverse and direct spatial relation
    private void learnAtom(Map<OWLObjectProperty, Set<SpatialAtom>> toLearn, SpatialAtom subject, OWLObjectProperty relation, SpatialAtom object){
        OWLObjectProperty sceneRelation = getSpatialRelation( sceneDescriptor.getOntology(), subject, relation);
        if ( toLearn.containsKey( sceneRelation))
            toLearn.get( sceneRelation).add( object);
        else {
            HashSet<SpatialAtom> atoms = new HashSet<>();
            atoms.add( object);
            toLearn.put( sceneRelation, atoms);
        }
    }
}

