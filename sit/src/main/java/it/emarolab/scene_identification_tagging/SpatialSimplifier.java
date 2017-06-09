package it.emarolab.scene_identification_tagging;

import it.emarolab.owloop.core.Semantic;
import it.emarolab.scene_identification_tagging.owloopDescriptor.SpatialIndividualDescriptor;
import it.emarolab.scene_identification_tagging.owloopDescriptor.SpatialObjectPropertyDescriptor;
import it.emarolab.scene_identification_tagging.realObject.GeometricPrimitive;
import it.emarolab.scene_identification_tagging.sceneRepresentation.SceneRepresentation;

import java.util.Set;


/**
 * The class to manipulate the ontology to obtain the minimal set of spatial relation for {@link SceneRepresentation}.
 * <p>
 *     This class simplifies the ontology in order to query only the minimal set
 *     of relations that are needed by the {@link SceneRepresentation#learn(String)} method.
 *     It is also able to revert the simplification (by modifying the ontology)
 *     in order to query the spatial relations in a more human understandable way.
 *     <br>
 *     The actual T-Box considers always human friendly spatial relations.
 *
 * <div style="text-align:center;"><small>
 * <b>File</b>:        it.emarolab.scene_identification_tagging.SpatialSimplifier <br>
 * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
 * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
 * <b>affiliation</b>: EMAROLab, DIBRIS, University of Genoa. <br>
 * <b>date</b>:        05/06/17 <br>
 * </small></div>
 */
public class SpatialSimplifier
        implements SITBase{

    private Set< GeometricPrimitive> objects;

    /**
     * It stores an internal representation of objects {@code Descriptor} to be added in the
     * simplified ontology or in the human friendly representation.
     * @param objects the object of the scene.
     */
    public SpatialSimplifier(Set< GeometricPrimitive> objects){
        this.objects = objects;
    }

    /**
     * It manipulates the ontology, and so the {@link #getObjects()} sets, in order to have only the minimal
     * set of spatial relation. Note that this method does not call {@link GeometricPrimitive#readSemantic()}.
     * In particular it:
     * <ul>
     *     <li>1: sets all individual disjointed,</li>
     *     <li>2: clears previous spatial relations applied to the given individuals,</li>
     *     <li>3: sets the primary (not inverse) relation to be synchronised, </li>
     *     <li>4: removes transitivity and symmetric property features. </li>
     * </ul>
     */
    public void populateMinimalRelationSet() {
        // add objects
        boolean done = false;
        for ( SpatialIndividualDescriptor i : objects){
            for ( SpatialIndividualDescriptor j : objects)
                if ( ! i.equals( j)) // todo do it with amor and remove from description
                    i.addDisjointIndividual( j.getInstance()); // 1-

            i.getObjectSemantics().clear(); // 2-
            i.addObject( OBJECT_PROPERTY.SPATIAL_ABOVE); // 3-
            i.addObject( OBJECT_PROPERTY.SPATIAL_BEHIND);
            i.addObject( OBJECT_PROPERTY.SPATIAL_RIGHT);
            i.addObject( OBJECT_PROPERTY.SPATIAL_PARALLEL);
            i.addObject( OBJECT_PROPERTY.SPATIAL_PERPENDICULAR);
            i.addObject( OBJECT_PROPERTY.SPATIAL_COAXIAL);
            i.addObject( OBJECT_PROPERTY.SPATIAL_ALONGX);
            i.addObject( OBJECT_PROPERTY.SPATIAL_ALONGY);
            i.addObject( OBJECT_PROPERTY.SPATIAL_ALONGZ);

            if ( ! done) { // hp: all the objects have the same type of possible relation
                for (SpatialObjectPropertyDescriptor d : i.buildObjectIndividual()) {
                    // this does not need readSemantic() or writeSemantic()
                    d.setNotTransitive(); // 4-
                    if ( d.isSpatialSymmetric()){
                        d.setNotSymmetric();
                    }
                }
                done = true;
            }

            i.writeSemantic();
        }
    }

    /**
     * It manipulates the ontology, and so the {@link #getObjects()} sets, in order to have
     * all the spatial relation in am ore human friendly representation.
     * Note that this method does not call {@link GeometricPrimitive#readSemantic()},
     * sor performances purposes, since you may what to do further manipulation.
     * In particular it:
     * <ul>
     *     <li>1: resets transitive and symmetric properties,</li>
     *     <li>2: clear the object property description {@link Semantic.SemanticAxioms}.
     *            This will make OWLOOP reading all the spatial relations,
     *            not only the primary, but also the inverse.</li>
     * </ul>
     */
    public void populateHumanFriendlyRelationSet() {
        // add objects
        boolean transitiveAdded = false;
        for ( SpatialIndividualDescriptor i : objects){

            if ( ! transitiveAdded) { // hp: all the objects have the same type of possible relation
                for (SpatialObjectPropertyDescriptor d : i.buildObjectIndividual()) {
                    // this does not need readSemantic() or writeSemantic()
                    d.setTransitive(); // -1
                    if ( d.isSpatialSymmetric())
                        d.setSymmetric();
                }
                transitiveAdded = true;
            }

            // if it is empty it reads all properties
            i.getObjectSemantics().clear(); // 2-
        }
    }

    /**
     * Returns the objects of the scene. Their spatial representation
     * changes based on the last call of {@link #populateMinimalRelationSet()}
     * or {@link #populateHumanFriendlyRelationSet()}.
     * @return the given object of the scene with minimal or human friendly
     * set of spatial relation.
     */
    public Set< GeometricPrimitive> getObjects() {
        return objects;
    }

    /**
     * Calls call {@link GeometricPrimitive#readSemantic()} for all the
     * given objects.
     * @param reason {@code true} if you want to call the reasoning task
     *                           befoure to read.
     */
    public void readObjectSemantics( boolean reason){
        boolean done = false;
        for ( SpatialIndividualDescriptor i : objects){
            if ( ! done) {
                i.reason();
                done = true;
            }
            i.readSemantic();
        }
    }
}
