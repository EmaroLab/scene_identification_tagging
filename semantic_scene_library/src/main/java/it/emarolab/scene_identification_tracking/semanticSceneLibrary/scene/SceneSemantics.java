package it.emarolab.scene_identification_tracking.semanticSceneLibrary.scene;

import it.emarolab.amor.owlInterface.OWLEnquirer;
import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.Semantics;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.aMORDescriptor.MORIndividualDescriptor;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.objects.aMOR.MORPrimitive;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.scene.SceneRepresentation.MORScene;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.scene.SceneRepresentation.MORSpatialCollector;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.scene.SceneRepresentation.MORSceneAtom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SceneSemantics extends MORIndividualDescriptor.MORSimpleDescriptor implements Semantics {

    public static final String SCENE_INDIVIDUAL_NAME = "S";
    public static final String INITIAL_SCENE_TYPE = "Scene";

    private Set<MORPrimitive> objects = new HashSet<>();
    private MORScene sceneAtom = null; // updated when writeSemantic() is called
    private boolean atomUpdated = false;// only for debugging

    public SceneSemantics(MORSimpleDescriptor descriptor) {
        super(descriptor);
        // todo add comping
    }

    public SceneSemantics() {
        super(SCENE_INDIVIDUAL_NAME);
        this.addType( INITIAL_SCENE_TYPE);
    }
    public SceneSemantics(String individualName, String ontoName) {
        super(individualName, ontoName);
        this.addType( INITIAL_SCENE_TYPE);
    }
    public SceneSemantics(String ontoName) {
        super(SCENE_INDIVIDUAL_NAME, ontoName);
        this.addType( INITIAL_SCENE_TYPE);
    }
    public SceneSemantics(OWLReferences ontoRef) {
        super(SCENE_INDIVIDUAL_NAME);
        setOntology( ontoRef);
        this.addType( INITIAL_SCENE_TYPE);
    }
    public SceneSemantics(OWLNamedIndividual individual, OWLReferences ontoRef) {
        super(individual, ontoRef);
        this.addType( INITIAL_SCENE_TYPE);
    }

    public SceneSemantics(Set< MORPrimitive> objects) {
        super(SCENE_INDIVIDUAL_NAME);
        this.objects = objects;
        this.addType( INITIAL_SCENE_TYPE);
    }
    public SceneSemantics(String individualName, String ontoName, Set< MORPrimitive> objects) {
        super(individualName, ontoName);
        this.objects = objects;
        this.addType( INITIAL_SCENE_TYPE);
    }
    public SceneSemantics(String ontoName, Set< MORPrimitive> objects) {
        super(SCENE_INDIVIDUAL_NAME, ontoName);
        this.objects = objects;
        this.addType( INITIAL_SCENE_TYPE);
    }
    public SceneSemantics(OWLReferences ontoRef, Set< MORPrimitive> objects) {
        super(SCENE_INDIVIDUAL_NAME);
        setOntology( ontoRef);
        this.objects = objects;
        this.addType( INITIAL_SCENE_TYPE);
    }
    public SceneSemantics(OWLNamedIndividual individual, OWLReferences ontoRef, Set< MORPrimitive> objects) {
        super(individual, ontoRef);
        this.objects = objects;
        this.addType( INITIAL_SCENE_TYPE);
    }

    public Set<MORPrimitive> getObjects() {
        return objects;
    }
    public void setObjects(Set<MORPrimitive> objects) {
        this.objects = objects;
    }

    private MORScene getSceneAtom(){
        MORSpatialCollector collector = new MORSpatialCollector( getOntology());
        for (MORPrimitive o : objects) {
            collector.addAll( o.getSemantics().querySpatialProperties());
        }
        collector.clean();
        log("Query spatial relations within objects: " + getObjects() + "\n\t\treturns: " + collector);
        atomUpdated = true; // this function is only called on writeSemantics()
        return collector.getSceneAtoms();
    }
    public MORScene getScene(){
        if( ! atomUpdated)
            logWARNING( "call writeSemantics() for update the scene atoms!");
        atomUpdated = false;
        return sceneAtom;
    }
    public Set<MORSceneAtom> getAtom(){
        return getScene().getAtoms();
    }
    public SceneRepresentation.SceneCardinality getCardinality(){
        return getScene().getCardinality();
    }

    public boolean isRecognisedScene(){
        List<OWLClass> recognised = getRecognisedScene();
        if( recognised == null)
            return false;
        return recognised.isEmpty();
    }
    public int getRecognisedScneCnt(){
        List<OWLClass> recognised = getRecognisedScene();
        if( recognised == null)
            return -1;
        return recognised.size();
    }
    public List<OWLClass> getRecognisedScene() {
        if (getTypes() != null) {
            List<OWLClass> recognised = new ArrayList<>( getTypes());
            recognised.remove(SCENE_INDIVIDUAL_NAME);
            return recognised;
        }
        logERROR( "Scene cannot recognise any class. Individual " + getIndividualName() + " have null types list.");
        return null;
    }

    // does not check on semantics (only if you call getScene() after writeSemantics()
    public boolean hasUpdatedAtoms(){
        return atomUpdated;
    }

    protected int cleanIndividual(){
        int cnt = 0;
        for( OWLEnquirer.ObjectPropertyRelations p : getOntology().getObjectPropertyB2Individual(getInstance()))
            for (OWLNamedIndividual i : p.getValues()) {
                getOntology().removeObjectPropertyB2Individual(p.getIndividuals(), p.getProperty(), i);
                cnt += 1;
            }
        return cnt;
    }

    @Override
    public SceneSemantics copy() {
        return new SceneSemantics( this);
    }

    @Override
    public WritingState writeSemantics() {
        return new MORIndividualDescriptor.TryingWrite( this) {
            protected Semantics.WritingState giveAtry() {
                log( " Semantic sceneAtom writing ...");

                WritingState state = writeType("classes");

                int removed = cleanIndividual();
                int added = 0;
                sceneAtom = getSceneAtom();
                for (MORSceneAtom atom : sceneAtom.getAtoms()){
                    ontology.addObjectPropertyB2Individual(getInstance(), atom.getProperty(), atom.getObject());
                    added += 1;

                    log( this.getClass().getSimpleName() + "\t WRITES PROPERTY: \"" + individualName
                            + "\"(spatial sceneAtom)"
                            + "\t\t " + getOntology().getOWLObjectName( atom.getProperty()) + "." + getOntology().getOWLObjectName( atom.getObject())
                            + "\t\t[" + state + "]");
                }
                if( removed >= added) // todo ????????????????? ambiguous
                    state = state.merge( new WritingState().asUpdated());
                else state = state.merge( new WritingState().asAdded());
                return state;
            }
        }.perform();
    }
    @Override
    public ReadingState readSemantics() { // reads only the types
        log( " Semantic sceneAtom reading ...");
        return readType("class").getState();
    }
}
