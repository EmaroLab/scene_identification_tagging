package it.emarolab.scene_identification_tagging.sceneRepresentation;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.owloop.aMORDescriptor.MORAxioms;
import it.emarolab.scene_identification_tagging.SITBase;
import it.emarolab.scene_identification_tagging.SpatialSimplifier;
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
 *     objects in a set of relations to an abstract scene representation.
 *     It also compute the recognition with a given confidence.
 *     Last but not the least, it is able to {@link #learn(String)}
 *     the actual concrete scene by using it as a template and
 *     create a new semantic abstract representation (stored in the ontology)
 *     that can be used for further recognition.
 *     Note that those are asserted to by sub classes of {@link CLASS#SCENE}
 *     and the reasoner is in charge (though instance checking) to
 *     create a hierarchy between scenes that share some similarities.
 *     <br>
 *     During constructor it computes the SIT algorithm for the recognition phase,
 *     by create a set of {@link SpatialRelation} ({@link #getRelations()}) applied
 *     to an abstract {@link INDIVIDUAL#SCENE}, appended with a static counter
 *     (*     An optional flag for adding the time stamp data property (i.e.: {@link DATA_PROPERTY#TIME}) is also available.).
 *     This is the minimal set of the relation that represents the scene, to obtained
 *     the relations in more human friendly way you need to use
 *     {@link SpatialSimplifier#populateHumanFriendlyRelationSet()}, and eventually
 *     {@link SpatialSimplifier#readObjectSemantics(boolean)}.
 *     <br>
 *     Eventually you may want to use {@link #shouldLearn()} and {@link #learn(String)}
 *     to create a new ontological class that can be used for further recognise
 *     the presented composition of objects. This sets also the
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
     * describes spatial relations between two objects (already populated from SWRL rules)
     * in the minimal form. With that it computes the recognition of the scene by adding
     * the abstract scene representation (i.e.: individual) to the ontology.
     * @param objects the objects describing spatial relations.
     * @param ontoRef the ontology that the SIT should manipulate.
     */
    public SceneRepresentation(SpatialSimplifier objects, OWLReferences ontoRef){
        objects.populateMinimalRelationSet();
        objects.readObjectSemantics( true); // call reasoner to compute SWRL rules and readSemantic()
        relations = computeSceneRelations(objects.getObjects());
        sceneDescriptor = new SceneIndividualDescriptor( getSceneIndividualName(), ontoRef);
        applyScene( sceneDescriptor, relations);
        computeRecognitionConfidence( sceneDescriptor);
    }

    // unique individual scene name
    private String getSceneIndividualName(){
        return INDIVIDUAL.SCENE + (ID++);
    }

    // get the minimal set of relation between all obejcts
    private Set< SpatialRelation> computeSceneRelations(Collection<? extends SpatialIndividualDescriptor> objects) {
        Set< SpatialRelation> relations = new HashSet<>();
        if ( ! objects.isEmpty())
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
            OWLObjectProperty spatialInverseRelation = getSpatialRelation( sceneDescriptor.getOntology(), r.getInverseRelation());
            if ( ! spatialInverseRelation.equals( spatialRelation))
                sceneDescriptor.addObject( spatialInverseRelation, r.getSubject().getIndividual());
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

        // semplify relations
        // Map< has-scene_...., Set<SpatialAtom>
        Map<OWLObjectProperty, Set<SpatialAtom>> toLearn = new HashMap<>();
        for ( SpatialRelation r : relations){
            learnAtom( toLearn, r.getRelation(), r.getObject());
            learnAtom( toLearn, r.getInverseRelation(), r.getSubject());
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
        learned.writeSemanticInconsistencySafe( true); // cal reasoning

        // update this internal class
        sceneDescriptor.readSemantic();
        bestRecognitionDescriptor = learned;
        recognitionConfidence = 1;
    }
    // called twice for inverse and direct spatial relation
    private void learnAtom(Map<OWLObjectProperty, Set<SpatialAtom>> toLearn, OWLObjectProperty relation, SpatialAtom object){
        OWLObjectProperty sceneRelation = getSpatialRelation( sceneDescriptor.getOntology(), relation);
        if ( toLearn.containsKey( sceneRelation))
            toLearn.get( sceneRelation).add( object);
        else {
            HashSet<SpatialAtom> atoms = new HashSet<>();
            atoms.add( object);
            toLearn.put( sceneRelation, atoms);
        }
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
        // external information
        private OWLObjectProperty relation;
        private OWLClass shape;
        private int cardinality = 1;

        public LearningData(OWLObjectProperty p, OWLClass type) {
            this.relation = p;
            this.shape = type;
        }

        private OWLObjectProperty getRelation() {
            return relation;
        }
        private OWLClass getShape() {
            return shape;
        }
        private int getCardinality() {
            return cardinality;
        }
        private void increaseCardinality() {
            cardinality += 1;
        }

        @Override // check if relation and shapes are equals
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof LearningData)) return false;

            LearningData that = (LearningData) o;

            if (getRelation() != null ? !getRelation().equals(that.getRelation()) : that.getRelation() != null)
                return false;
            return getShape() != null ? getShape().equals(that.getShape()) : that.getShape() == null;
        }
        @Override
        public int hashCode() {
            int result = getRelation() != null ? getRelation().hashCode() : 0;
            result = 31 * result + (getShape() != null ? getShape().hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return relation.getIRI().getRemainder().get() + " min " + cardinality + " " + shape.getIRI().getRemainder().get();
        }
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
     * The minimal set of spatial relation properties between oll the individuals
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

