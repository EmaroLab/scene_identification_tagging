package it.emarolab.scene_identification_tagging.sceneRepresentation;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.owloop.aMORDescriptor.MORAxioms;
import it.emarolab.scene_identification_tagging.SITBase;
import it.emarolab.scene_identification_tagging.owloopDescriptor.SceneClassDescriptor;
import it.emarolab.scene_identification_tagging.owloopDescriptor.SceneIndividualDescriptor;
import it.emarolab.scene_identification_tagging.owloopDescriptor.SpatialIndividualDescriptor;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import java.util.*;

/**
 * The core of this system than compute the SIT algorithm.
 * <p>
 *     This class is in charge to map the {@link SpatialRelation} between two
 *     objects in a set of relation to an abstract scene representation.
 *     It also compute the recognition with a given confidence.
 *     Last but not the least, it is able to {@link #learn(String)}
 *     the actual concrete scene by using it as a template and
 *     create a new semantic abstract representation (stored in the ontology)
 *     that can be used for further recognition.
 *     Note that those are asserted to by sub classes of {@link CLASS#SCENE}
 *     and the reasoner is in charge (though instance checking) to
 *     create a hierarchy between scenes that share some similarities.
 *     <br>
 *     During constructor it computes the SIT algorithm by create a set of {@link SpatialRelation}
 *     ({@link #getRelations()}). Than it creates a new onotlogical class that
 *     describes the concrete scene by using a {@code static} counter to have
 *     a unique name (note that this could be not unique if the results are stored in
 *     the ontology and the system is showdown and than restarted). An optional flag for adding
 *     the time stamp data property (i.e.: {@link DATA_PROPERTY#TIME}) is also available.
 *     Finally, it uses those two information to define the concrete scene representation from spatial relations and
 *     classifies it in the {@link CLASS#SCENE} hierarchy of the ontology. This initialise the
 *     {@link #getRecognitionConfidence()} and the related {@link #getBestRecognitionDescriptor()}.
 *     The confidence is compute as {@code C/I} where, {@code C} is the best (i.e.: higher) cardinality of the
 *     class describing the scene, while {@code I} is the cardinality of the individual describing the scene.
 *     This is a number always within [0,1].
 *     <br>
 *     The spatial relation between a Scene and two objects are obtained by
 *     adding the {@link OBJECT_PROPERTY#PREFIX_HAS} to the spatial object property given between two individuals.
 *     <br>
 *
 * <div style="text-align:center;"><small>
 * <b>File</b>:        it.emarolab.scene_identification_tagging.sceneRepresentation.SceneRepresentation <br>
 * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
 * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
 * <b>affiliation</b>: EMAROLab, DIBRIS, University of Genoa. <br>
 * <b>date</b>:        05/06/17 <br>
 * </small></div>
 */
public class SceneRepresentation
        implements SITBase{

    private Set< SpatialRelation> relations;
    private SceneIndividualDescriptor sceneDescriptor;
    private SceneClassDescriptor bestRecognitionDescriptor;
    private double recognitionConfidence;
    private long time = System.currentTimeMillis();
    private static long ID;
    private boolean addTime = false;

    /**
     * This constructor assume that the given {@code object} have all object property that
     * describes spatial relations between two objects (already populated from SWRL rules).
     * It initialises all the fields of {@code this} class.
     * @param objects the objects describing spatial relations.
     * @param ontoRef the ontology that the SIT should manipulate.
     */
    public SceneRepresentation(Collection< ? extends SpatialIndividualDescriptor> objects, OWLReferences ontoRef){
        relations = computeSceneRelations(objects);
        sceneDescriptor = new SceneIndividualDescriptor( getSceneIndividualName(), ontoRef);
        applyScene( sceneDescriptor, relations);
        computeRecognitionConfidence( sceneDescriptor);
    }

    private String getSceneIndividualName(){
        return INDIVIDUAL.SCENE + (ID++);
    }

    private Set< SpatialRelation> computeSceneRelations(Collection<? extends SpatialIndividualDescriptor> objects) {
        Set< SpatialRelation> relations = new HashSet<>();
        if ( ! objects.isEmpty())
            // hp: object properties of individual belonging to "GeometricPrimitive" are all spatial relations
            for ( SpatialIndividualDescriptor o : objects) {
                for (MORAxioms.ObjectSemantic s : o.getObjectSemantics()) {
                    for (OWLNamedIndividual i : s.getValues()) {
                        relations.add(new SpatialRelation(o, s.getSemantic(), i));
                    }
                }
            }
        return relations;
    }

    // add the scene individual to the ontology
    private void applyScene(SceneIndividualDescriptor sceneDescriptor, Set<SpatialRelation> relations){
        for ( SpatialRelation r : relations){
            OWLObjectProperty spatialRelation = getSpatialRelation( sceneDescriptor.getOntology(), r.getRelation());
            sceneDescriptor.addObject( spatialRelation, r.getObject().getIndividual());
        }
        sceneDescriptor.addTypeIndividual( CLASS.SCENE);
        if (addTime)
            sceneDescriptor.addData( DATA_PROPERTY.TIME, time, true);
        sceneDescriptor.writeSemanticInconsistencySafe( true);
    }

    // get spatial relation between a Scene and an Object from the spatial relation between two Objects
    private OWLObjectProperty getSpatialRelation(OWLReferences ontology, OWLObjectProperty relation) {
        String relationName = relation.getIRI().getRemainder().get();
        String sceneRelationName = OBJECT_PROPERTY.SCENE_SPATIAL_PRFIX + relationName;
        return ontology.getOWLObjectProperty( sceneRelationName);
    }

    // compute the best scene classification (confidence and descriptor)
    private void computeRecognitionConfidence( SceneIndividualDescriptor sceneDescriptor){
        double individualCardinality = sceneDescriptor.getCardinality();
        double bestCardinality = 0;
        for ( SceneClassDescriptor recognised : sceneDescriptor.buildTypeIndividual()){
            if ( recognised.getCardinality() > bestCardinality){
                bestCardinality = recognised.getCardinality();
                bestRecognitionDescriptor = recognised;
            }
        }
        if ( individualCardinality > 0)
            recognitionConfidence = bestCardinality / individualCardinality;
        else recognitionConfidence = 0;
    }

    /**
     * Return true if the scene has been not clarified or if
     * the {@link #getRecognitionConfidence()} is lower than {@link SITBase#CONFIDENCE_THRESHOLD}.
     * The latter, indicates that only a small part of the spatial relations in the scene
     * has been recognised. Otherwise it returns {@code false}.
     * @return It indicate if the recognition went well.
     */
    public boolean shouldLearn(){
        if ( sceneDescriptor.getTypeIndividual().size() <= 1) // [Scene]
            return true;
        else {
            if ( recognitionConfidence < CONFIDENCE_THRESHOLD)
                return true;
            return false;
        }
    }

    /**
     * It uses the {@link #getSceneDescriptor()} as a template
     * to create a new abstract scene class the perfectly fits its recognition
     * (i.e.: {@code {@link #getRecognitionConfidence()} = 1}).
     * This method calls the reasoning task to place the new scene in the {@link CLASS#SCENE}
     * hierarchy and updates the {@link #getRecognitionConfidence()} and {@link #getSceneDescriptor()}.
     * @param newSceneName the name of the new scene representation to learn
     */
    public void learn(String newSceneName) {
        SceneClassDescriptor learned = new SceneClassDescriptor( newSceneName, sceneDescriptor.getOntology());
        // map the spatial relation between the Scene and Objects in a class definition
        Set<LearningData> shapeCardinality = new HashSet<>();
        for ( SpatialRelation r : relations){
            OWLObjectProperty sceneRelation = getSpatialRelation(sceneDescriptor.getOntology(), r.getRelation());
            LearningData learning = new LearningData( sceneRelation, r);
            boolean found = false;
            for ( LearningData l : shapeCardinality) {
                if ( l.equals(learning)){
                    found = true;
                    break;
                }
                if ( l.getRelation().equals( learning.getRelation())
                        & l.getShape().equals( learning.getShape())){
                    l.increaseCardinality();
                    found = true;
                    break;
                }
            }
            if (! found)
                shapeCardinality.add( learning);
        }

        for ( LearningData toLearn : shapeCardinality)
            learned.addMinObjectRestriction( toLearn.getRelation(), toLearn.getCardinality(), toLearn.getShape());

        // update the ontology
        learned.addSuperConcept( CLASS.SCENE);
        learned.writeSemanticInconsistencySafe( true); // cal resoning
        sceneDescriptor.readSemantic();
        // update this internal class
        bestRecognitionDescriptor = learned;
        recognitionConfidence = 1;
    }

    /*
     * This class is internally used by learn()
     * to map the SpatialRelation between objects in
     * relation used in the class representation
     * (e.g.: 'Sphere1 isRightOf Sphere2' --> 'has-scene_isRightOf min 2 Sphere'
     * It is mainly used to count the occurrence of relation
     * with respect to specific shapes
     */
    private class LearningData{
        private OWLObjectProperty relation;
        private OWLClass shape;
        private OWLNamedIndividual individual;
        private int cardinality = 1;

        private LearningData(OWLObjectProperty sceneRelation, SpatialRelation spatialRelation) {
            this.relation = sceneRelation;
            this.shape = spatialRelation.getObject().getType();
            this.individual = spatialRelation.getObject().getIndividual();
        }

        private OWLObjectProperty getRelation() {
            return relation;
        }
        private OWLClass getShape() {
            return shape;
        }
        private OWLNamedIndividual getIndividual() {
            return individual; // used in equal
        }
        private int getCardinality() {
            return cardinality;
        }
        private void increaseCardinality() {
            cardinality += 1;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof LearningData)) return false;

            LearningData that = (LearningData) o;

            if (getRelation() != null ? !getRelation().equals(that.getRelation()) : that.getRelation() != null)
                return false;
            if (getShape() != null ? !getShape().equals(that.getShape()) : that.getShape() != null) return false;
            return getIndividual() != null ? getIndividual().equals(that.getIndividual()) : that.getIndividual() == null;
        }
        @Override
        public int hashCode() {
            int result = getRelation() != null ? getRelation().hashCode() : 0;
            result = 31 * result + (getShape() != null ? getShape().hashCode() : 0);
            result = 31 * result + (getIndividual() != null ? getIndividual().hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return relation.getIRI().getRemainder().get() + " min " + cardinality + " " + shape.getIRI().getRemainder().get();
        }
    }

    /**
     * The spatial relation object properties between oll the individuals
     * given on the constructor.
     * @return the spatial relations between objects
     */
    public Set<SpatialRelation> getRelations() {
        return relations;
    }

    /**
     * The concrete scene description created on the constructor.
     * @return an OWLOOP description for the individual describing the
     * concrete scene.
     */
    public SceneIndividualDescriptor getSceneDescriptor() {
        return sceneDescriptor;
    }

    /**
     * The abstract scene class that best fits the recognition of this concrete scene individual.
     * Other classes that describe more generically this scene can be obtained from the super
     * classes of this {@code Descriptor}.
     * @return the class that best recognises the scene.
     */
    public SceneClassDescriptor getBestRecognitionDescriptor() {
        return bestRecognitionDescriptor;
    }

    /**
     * The value of the confidences computed as the best recognition cardinality over
     * the scene individual cardinality. This values is always within [0,1] where
     * 0 means not recognised and 1 perfectly recognised.
     * @return the recognition confidence.
     */
    public double getRecognitionConfidence() {
        return recognitionConfidence;
    }

    /**
     * Return if this class add (or not) a time stamp data property to
     * the concrete scene individual (i.e.: {@link DATA_PROPERTY#TIME})
     * @return {@code true} if the time stamp is introduced in the ontology. {@code false} otherwise
     */
    public boolean isAddingTime() {
        return addTime;
    }

    /**
     * Enable/disable the adding of a time stamp data property to
     * the concrete scene individual (i.e.: {@link DATA_PROPERTY#TIME})
     * @param addTime {@code true} enable time stamping, {@code false} disable it.
     */
    public void setAddingTime(boolean addTime) {
        this.addTime = addTime;
    }
}

