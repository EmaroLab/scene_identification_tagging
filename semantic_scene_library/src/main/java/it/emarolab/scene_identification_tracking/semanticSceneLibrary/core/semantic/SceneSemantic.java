package it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.semantic;

import it.emarolab.amor.owlInterface.OWLEnquirer;
import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.Base;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.Semantics;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.aMORDescriptor.MORMultiLinkDescriptor;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.aMORDescriptor.MORSceneDescriptor;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.aMORDescriptor.MORSpatialDescriptor;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.aMORDescriptor.MORTypedDescriptor;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.aMORDescriptor.aMORObject.MORPrimitive;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import java.util.*;

public interface SceneSemantic {

    interface SceneHierarchyDescriptor<O,C> extends Semantics.HierarchyDescriptor<O,C> {
        Semantics.WritingState writeSceneCardinality();
        Semantics.ReadingState readSceneCardinality(); // old cardinality?
        SceneSemantic.SceneCardinality getCardinality();
        void setCardinality( SceneSemantic.SceneCardinality cardinality);

        // todo add interface for SWRL
    }

    interface SceneDescriptor<O,I,C> extends Semantics.TypedDescriptor<O,I,C>{

        @Override
        C getBottomType();

        Set<MORPrimitive> getObjects();
        public void setObjects(Set<MORPrimitive> objects);

        Scene<?> getScene();
        default Set<?> getSceneAtom(){
            return getScene().getAtoms();
        }

        default SceneCardinality getCardinality(){
            return getScene().getCardinality();
        }

        // todo setters
    }

    class SceneSemantics<O,I,C>
            extends MORMultiLinkDescriptor.MORMultiLinkedTypedIndividual
            implements Semantics{//}, SceneDescriptor<O,I,C>{

        public static final String SCENE_INDIVIDUAL_NAME = "S";
        public static final String INITIAL_SCENE_TYPE = "Scene";

        private Set<MORPrimitive> objects = new HashSet<>();
        private MORSceneDescriptor.MORScene sceneAtom = null; // updated when writeSemantic() is called
        private boolean atomUpdated = false;// only for debugging

        public SceneSemantics(SceneSemantics copy) {
            super(copy);
            this.objects = new HashSet<>( copy.objects);
            this.sceneAtom = copy.sceneAtom; // todo copy!!!!!!!!!!!!!!!!!!!!!!!11
            this.atomUpdated = copy.atomUpdated;
        }

        public SceneSemantics() {
        }
        public SceneSemantics(String ontoName) {
            super(ontoName);
        }
        public SceneSemantics(OWLReferences ontoRef) {
            super(ontoRef);
        }
        public SceneSemantics(OWLReferences ontoRef, OWLNamedIndividual instance) {
            super(ontoRef, instance);
        }
        public SceneSemantics(String ontoRef, String instance) {
            super(ontoRef, instance);
        }


        public Set<MORPrimitive> getObjects() {
            return objects;
        }
        public void setObjects(Set<MORPrimitive> objects) {
            this.objects = objects;
        }

        private MORSceneDescriptor.MORScene getSceneAtom(){
            MORSpatialDescriptor.MORSpatialCollector collector = new MORSpatialDescriptor.MORSpatialCollector( getOntology());
            for (MORPrimitive o : objects) {
                collector.addAll( o.getSemantics().querySpatialProperties());
            }
            collector.clean();
            log("Query spatial relations within objects: " + getObjects() + "\n\t\treturns: " + collector);
            atomUpdated = true; // this function is only called on writeSemantics()
            return collector.getSceneAtoms();
        }
        public MORSceneDescriptor.MORScene getScene(){
            if( ! atomUpdated)
                logWarning( "call writeSemantics() for update the scene atoms!"); // todo make it better!
            atomUpdated = false;
            return sceneAtom;
        }
        public Set<MORSceneDescriptor.MORSceneAtom> getAtom(){
            return getScene().getAtoms();
        }
        public SceneCardinality getCardinality(){
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
            logError( "Scene cannot recognise any class. Individual " + getInstanceName() + " have null types list.");
            return null;
        }

        // does not check on semantic (only if you call getScene() after writeSemantics()
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
        public MappingTransitions writeSemantic() {
            return new MORTryWrite<OWLNamedIndividual,OWLObjectProperty,OWLNamedIndividual>( Semantics.this, LOGGING.INTENT_TYPE) {
                protected MappingTransitions giveAtry() {
                    log( " Semantic sceneAtom writing ...");

                    // todo check if loses intents?
                    MappingTransitions trans = writeType();
                    WritingState state = (WritingState) trans.merge(); // "classes"

                    int removed = cleanIndividual();
                    int added = 0;
                    sceneAtom = getSceneAtom();
                    for (MORSceneDescriptor.MORSceneAtom atom : sceneAtom.getAtoms()){
                        getOntology().addObjectPropertyB2Individual(getInstance(), atom.getProperty(), atom.getObject());
                        added += 1;

                        log( this.getClass().getSimpleName() + "\t WRITES PROPERTY: \"" + getInstanceName()
                                + "\"(spatial sceneAtom)"
                                + "\t\t " + getOntology().getOWLObjectName( atom.getProperty()) + "." + getOntology().getOWLObjectName( atom.getObject())
                                + "\t\t[" + state + "]");
                    }
                    if( removed >= added) // todo ????????????????? ambiguous
                        state = state.merge( new WritingState().asUpdated());
                    else state = state.merge( new WritingState().asAdded());
                    return trans;// state;
                }
            }.perform();
        }
        @Override
        public MappingTransitions readSemantic() { // reads only the types
            log( " Semantic sceneAtom reading ...");
            return readType(); // "classes"
        }


        @Override
        public MappingTransitions deleteSemantic() {
            return null; // todo implement
        }
    }









    public interface Scene<SA> {
        Set< SA> getAtoms();
        SceneCardinality getCardinality();
    }
    interface SceneAtom<C,I,P> extends SpatialSemantic.SpatialItem<C,I> {
        P getProperty();
    }

    class SceneCardinality extends Base.SITBase {
        private ShapeCardinality behindCardinality, aboveCardinality, rightCardinality,
                alongXcardinality, alongYcardinality, alongZcardinality,
                parallelCardinality, perpendicularCardinality, coaxialCardinality;

        public SceneCardinality( ) {
            initialise();
        }
        public SceneCardinality( OWLReferences ontoRef) {
            initialise();
            setSemantics( ontoRef);
        }
        public SceneCardinality( SceneCardinality copy) {
            this.behindCardinality = new ShapeCardinality( copy.behindCardinality);
            this.aboveCardinality = new ShapeCardinality( copy.aboveCardinality);
            this.rightCardinality = new ShapeCardinality( copy.rightCardinality);
            this.alongXcardinality = new ShapeCardinality( copy.alongXcardinality);
            this.alongYcardinality = new ShapeCardinality( copy.alongYcardinality);
            this.alongZcardinality = new ShapeCardinality( copy.alongZcardinality);
            this.parallelCardinality = new ShapeCardinality( copy.parallelCardinality);
            this.perpendicularCardinality = new ShapeCardinality( copy.perpendicularCardinality);
            this.coaxialCardinality = new ShapeCardinality( copy.coaxialCardinality);
        }
        private void initialise(){
            this.behindCardinality = new ShapeCardinality();
            this.aboveCardinality = new ShapeCardinality();
            this.rightCardinality = new ShapeCardinality();
            this.alongXcardinality = new ShapeCardinality();
            this.alongYcardinality = new ShapeCardinality();
            this.alongZcardinality = new ShapeCardinality();
            this.parallelCardinality = new ShapeCardinality();
            this.perpendicularCardinality = new ShapeCardinality();
            this.coaxialCardinality = new ShapeCardinality();
        }
        public void setSemantics( OWLReferences ontoRef){
            behindCardinality.setSemantics( ontoRef, DEFAULT_SPATIAL_BEHIND);
            aboveCardinality.setSemantics( ontoRef, DEFAULT_SPATIAL_ABOVE);
            rightCardinality.setSemantics( ontoRef, DEFAULT_SPATIAL_RIGHT);
            alongXcardinality.setSemantics( ontoRef, DEFAULT_SPATIAL_LONGX);
            alongYcardinality.setSemantics( ontoRef, DEFAULT_SPATIAL_LONGY);
            alongZcardinality.setSemantics( ontoRef, DEFAULT_SPATIAL_LONGZ);
            parallelCardinality.setSemantics( ontoRef, DEFAULT_SPATIAL_PARALLEL);
            perpendicularCardinality.setSemantics( ontoRef, DEFAULT_SPATIAL_PERPENDICULAR);
            coaxialCardinality.setSemantics( ontoRef, DEFAULT_SPATIAL_COAXIAL);
        }

        public void addCardinality(OWLObjectProperty spatialRelation, OWLClass shape, int cardinality){
            if ( spatialRelation.equals( behindCardinality.getProperty()))
                behindCardinality.addCardinality( shape, cardinality);
            else if ( spatialRelation.equals( aboveCardinality.getProperty()))
                aboveCardinality.addCardinality( shape, cardinality);
             else if ( spatialRelation.equals( behindCardinality.getProperty()))
                behindCardinality.addCardinality( shape, cardinality);
             else if ( spatialRelation.equals( rightCardinality.getProperty()))
                rightCardinality.addCardinality( shape, cardinality);
             else if ( spatialRelation.equals( alongXcardinality.getProperty()))
                alongXcardinality.addCardinality( shape, cardinality);
            else if ( spatialRelation.equals( alongYcardinality.getProperty()))
                alongYcardinality.addCardinality( shape, cardinality);
             else if ( spatialRelation.equals( alongZcardinality.getProperty()))
                alongZcardinality.addCardinality( shape, cardinality);
             else if ( spatialRelation.equals( parallelCardinality.getProperty()))
                parallelCardinality.addCardinality( shape, cardinality);
             else if ( spatialRelation.equals( perpendicularCardinality.getProperty()))
                perpendicularCardinality.addCardinality( shape, cardinality);
             else if ( spatialRelation.equals( coaxialCardinality.getProperty()))
                coaxialCardinality.addCardinality( shape, cardinality);
             logWarning( "cannot add any cardinal restriction on data property: " + spatialRelation);
        }

        public ShapeCardinality getBehindCardinality() {
            return behindCardinality;
        }
        public ShapeCardinality getAboveCardinality() {
            return aboveCardinality;
        }
        public ShapeCardinality getRightCardinality() {
            return rightCardinality;
        }
        public ShapeCardinality getAlongXcardinality() {
            return alongXcardinality;
        }
        public ShapeCardinality getAlongYcardinality() {
            return alongYcardinality;
        }
        public ShapeCardinality getAlongZcardinality() {
            return alongZcardinality;
        }
        public ShapeCardinality getParallelCardinality() {
            return parallelCardinality;
        }
        public ShapeCardinality getPerpendicularCardinality() {
            return perpendicularCardinality;
        }
        public ShapeCardinality getCoaxialCardinality() {
            return coaxialCardinality;
        }

        public Cardinality computeTotal(){
            return behindCardinality.computeTotal().
                    increase( aboveCardinality.computeTotal()).
                    increase( rightCardinality.computeTotal()).
                    increase( alongXcardinality.computeTotal()).
                    increase( alongYcardinality.computeTotal()).
                    increase( alongZcardinality.computeTotal()).
                    increase( parallelCardinality.computeTotal()).
                    increase( perpendicularCardinality.computeTotal()).
                    increase( coaxialCardinality.computeTotal());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof SceneCardinality)) return false;

            SceneCardinality that = (SceneCardinality) o;

            if (getBehindCardinality() != null ? !getBehindCardinality().equals(that.getBehindCardinality()) : that.getBehindCardinality() != null)
                return false;
            if (getAboveCardinality() != null ? !getAboveCardinality().equals(that.getAboveCardinality()) : that.getAboveCardinality() != null)
                return false;
            if (getRightCardinality() != null ? !getRightCardinality().equals(that.getRightCardinality()) : that.getRightCardinality() != null)
                return false;
            if (getAlongXcardinality() != null ? !getAlongXcardinality().equals(that.getAlongXcardinality()) : that.getAlongXcardinality() != null)
                return false;
            if (getAlongYcardinality() != null ? !getAlongYcardinality().equals(that.getAlongYcardinality()) : that.getAlongYcardinality() != null)
                return false;
            if (getAlongZcardinality() != null ? !getAlongZcardinality().equals(that.getAlongZcardinality()) : that.getAlongZcardinality() != null)
                return false;
            if (getParallelCardinality() != null ? !getParallelCardinality().equals(that.getParallelCardinality()) : that.getParallelCardinality() != null)
                return false;
            if (getPerpendicularCardinality() != null ? !getPerpendicularCardinality().equals(that.getPerpendicularCardinality()) : that.getPerpendicularCardinality() != null)
                return false;
            return getCoaxialCardinality() != null ? getCoaxialCardinality().equals(that.getCoaxialCardinality()) : that.getCoaxialCardinality() == null;
        }

        @Override
        public int hashCode() {
            int result = getBehindCardinality() != null ? getBehindCardinality().hashCode() : 0;
            result = 31 * result + (getAboveCardinality() != null ? getAboveCardinality().hashCode() : 0);
            result = 31 * result + (getRightCardinality() != null ? getRightCardinality().hashCode() : 0);
            result = 31 * result + (getAlongXcardinality() != null ? getAlongXcardinality().hashCode() : 0);
            result = 31 * result + (getAlongYcardinality() != null ? getAlongYcardinality().hashCode() : 0);
            result = 31 * result + (getAlongZcardinality() != null ? getAlongZcardinality().hashCode() : 0);
            result = 31 * result + (getParallelCardinality() != null ? getParallelCardinality().hashCode() : 0);
            result = 31 * result + (getPerpendicularCardinality() != null ? getPerpendicularCardinality().hashCode() : 0);
            result = 31 * result + (getCoaxialCardinality() != null ? getCoaxialCardinality().hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "{\n" +
                    padString( "Behind/Front : ", LOGGING.LENGTH_LONG,true) + behindCardinality + ";\n" +
                    padString( "Above/Below : ", LOGGING.LENGTH_LONG,true) + aboveCardinality + ";\n" +
                    padString( "Right/Left : ", LOGGING.LENGTH_LONG,true) + rightCardinality + ";\n" +
                    padString( "Along X : ", LOGGING.LENGTH_LONG,true)  + alongXcardinality + ";\n" +
                    padString( "Y : ", LOGGING.LENGTH_LONG,true)  + alongYcardinality + ";\n" +
                    padString( "Z : ", LOGGING.LENGTH_LONG,true)  + alongZcardinality + ";\n" +
                    padString( "Parallel : ", LOGGING.LENGTH_LONG,true)  + parallelCardinality + ";\n" +
                    padString( "Perpendicular : ", LOGGING.LENGTH_LONG,true)  + perpendicularCardinality + ";\n" +
                    padString( "Coaxial : ", LOGGING.LENGTH_LONG,true)  + coaxialCardinality + "\n}";
        }

        @Override
        public SceneCardinality copy() {
            return new SceneCardinality( this);
        }
    }
    class ShapeCardinality extends Base.SITBase {

        private String propertyName;
        private OWLObjectProperty property;
        private Cardinality primitiveCardinality, sphereCardinality, orientableCardinality,
                coneCardinality, cylinderCardinality, planeCardinality;

        public ShapeCardinality() {
            initialise();
        }
        public ShapeCardinality( OWLReferences ontoRef, String property) {
            initialise();
            setSemantics( ontoRef, property);
        }

        public ShapeCardinality(ShapeCardinality copy) {
            super();
            propertyName = copy.propertyName;
            property = copy.property;
            primitiveCardinality = new Cardinality( copy.primitiveCardinality);
            sphereCardinality = new Cardinality( copy.sphereCardinality);
            orientableCardinality = new Cardinality( copy.orientableCardinality);
            coneCardinality = new Cardinality( copy.coneCardinality);
            cylinderCardinality = new Cardinality( copy.cylinderCardinality);
            planeCardinality = new Cardinality( copy.planeCardinality);
        }

        private void initialise(){
            this.primitiveCardinality = new Cardinality();
            this.sphereCardinality = new Cardinality();
            this.orientableCardinality = new Cardinality();
            this.coneCardinality = new Cardinality();
            this.cylinderCardinality = new Cardinality();
            this.planeCardinality = new Cardinality();
        }
        public void setSemantics( OWLReferences ontoRef, String property){
            this.propertyName = property;
            this.property = ontoRef.getOWLObjectProperty(property);
            this.primitiveCardinality.setSemantics( ontoRef, CLASS.PRIMITIVE, property);
            this.sphereCardinality.setSemantics( ontoRef, CLASS.SPHERE, property);
            this.orientableCardinality.setSemantics( ontoRef, CLASS.ORIENTBLE, property);
            this.coneCardinality.setSemantics( ontoRef, CLASS.CONE, property);
            this.cylinderCardinality.setSemantics( ontoRef, CLASS.CYLINDER, property);
            this.planeCardinality.setSemantics( ontoRef, CLASS.PLANE, property);
        }

        public void addCardinality( OWLClass cl, int cardinality){
            if( cl.equals( primitiveCardinality.getType())){
                primitiveCardinality.setType( cl);
                primitiveCardinality.increase( cardinality);
            } else if( cl.equals( sphereCardinality.getType())){
                sphereCardinality.setType( cl);
                sphereCardinality.increase( cardinality);
            } else if( cl.equals( orientableCardinality.getType())){
                orientableCardinality.setType( cl);
                orientableCardinality.increase( cardinality);
            } else if( cl.equals( coneCardinality.getType())){
                coneCardinality.setType( cl);
                coneCardinality.increase( cardinality);
            } else if( cl.equals( cylinderCardinality.getType())){
                cylinderCardinality.setType( cl);
                cylinderCardinality.increase( cardinality);
            } else if( cl.equals( planeCardinality.getType())){
                planeCardinality.setType( cl);
                planeCardinality.increase( cardinality);
            }
        }

        public Cardinality getPrimitiveCardinality() {
            return primitiveCardinality;
        }
        public Cardinality getSphereCardinality() {
            return sphereCardinality;
        }
        public Cardinality getOrientableCardinality() {
            return orientableCardinality;
        }
        public Cardinality getConeCardinality() {
            return coneCardinality;
        }
        public Cardinality getCylinderCardinality() {
            return cylinderCardinality;
        }
        public Cardinality getPlaneCardinality() {
            return planeCardinality;
        }

        public Cardinality computeTotal(){
            return primitiveCardinality.increase( sphereCardinality).
                    increase( orientableCardinality).increase( planeCardinality).
                    increase( coneCardinality).increase(cylinderCardinality);
        }

        public OWLObjectProperty getProperty() {
            return property;
        }
        public String getPropertyName() {
            return propertyName;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ShapeCardinality)) return false;

            ShapeCardinality that = (ShapeCardinality) o;

            if (getPropertyName() != null ? !getPropertyName().equals(that.getPropertyName()) : that.getPropertyName() != null)
                return false;
            if (getProperty() != null ? !getProperty().equals(that.getProperty()) : that.getProperty() != null)
                return false;
            if (getPrimitiveCardinality() != null ? !getPrimitiveCardinality().equals(that.getPrimitiveCardinality()) : that.getPrimitiveCardinality() != null)
                return false;
            if (getSphereCardinality() != null ? !getSphereCardinality().equals(that.getSphereCardinality()) : that.getSphereCardinality() != null)
                return false;
            if (getOrientableCardinality() != null ? !getOrientableCardinality().equals(that.getOrientableCardinality()) : that.getOrientableCardinality() != null)
                return false;
            if (getConeCardinality() != null ? !getConeCardinality().equals(that.getConeCardinality()) : that.getConeCardinality() != null)
                return false;
            if (getCylinderCardinality() != null ? !getCylinderCardinality().equals(that.getCylinderCardinality()) : that.getCylinderCardinality() != null)
                return false;
            return getPlaneCardinality() != null ? getPlaneCardinality().equals(that.getPlaneCardinality()) : that.getPlaneCardinality() == null;
        }

        @Override
        public int hashCode() {
            int result = getPropertyName() != null ? getPropertyName().hashCode() : 0;
            result = 31 * result + (getProperty() != null ? getProperty().hashCode() : 0);
            result = 31 * result + (getPrimitiveCardinality() != null ? getPrimitiveCardinality().hashCode() : 0);
            result = 31 * result + (getSphereCardinality() != null ? getSphereCardinality().hashCode() : 0);
            result = 31 * result + (getOrientableCardinality() != null ? getOrientableCardinality().hashCode() : 0);
            result = 31 * result + (getConeCardinality() != null ? getConeCardinality().hashCode() : 0);
            result = 31 * result + (getCylinderCardinality() != null ? getCylinderCardinality().hashCode() : 0);
            result = 31 * result + (getPlaneCardinality() != null ? getPlaneCardinality().hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return  "{ "  + primitiveCardinality + "; " +
                    sphereCardinality + "; " +
                    orientableCardinality + "; " +
                    coneCardinality + "; " +
                    cylinderCardinality + "; " +
                    planeCardinality + "} ";
        }

        @Override
        public ShapeCardinality copy() {
            return new ShapeCardinality( this);
        }
    }
    class Cardinality extends Base.SITBase {
        private String typeName = "";
        private int cardinality;
        private OWLObjectProperty property;
        private OWLClass type;

        protected Cardinality(){
            this.cardinality = 0;
            this.typeName = "??";
        }
        protected Cardinality( Cardinality c){
            this.type = c.type;
            this.typeName = c.typeName;
            this.cardinality = c.cardinality;
            this.property = c.property;
        }
        protected Cardinality( OWLReferences ontoRef, String type, String property){
            this.cardinality = 0;
            setSemantics( ontoRef, type, property);
        }
        protected Cardinality( int cardinality, OWLReferences ontoRef, String type, String property){
            this.cardinality = cardinality;
            setSemantics( ontoRef, type, property);
        }

        public void setSemantics( OWLReferences ontoRef, String type, String property){
            this.property = ontoRef.getOWLObjectProperty( property);
            this.type = ontoRef.getOWLClass( type);
            this.typeName = type;
        }

        public void setType(OWLClass type) {
            this.type = type;
        }

        public Cardinality set(int cardinality) {
            if( cardinality < 0){
                this.cardinality = 0;
                logWarning("Cardinality cannot be negative, 0 set instead." );
            } else this.cardinality = cardinality;
            return this;
        }
        public Cardinality reset() {
            cardinality = 0;
            return this;
        }

        public Cardinality increase() {
            return set( getCardinality() + 1);
        }
        public Cardinality increase(int toAdd) {
            return set( getCardinality() + toAdd);
        }
        public Cardinality increase(Cardinality toAdd) {
            return new Cardinality( this).set( getCardinality() + toAdd.getCardinality());
        }

        public Cardinality decrease() {
            return set( getCardinality() - 1);
        }
        public Cardinality decrease(int toSubtract) {
            return set( getCardinality() - toSubtract);
        }
        public Cardinality decrease(Cardinality toSubtract) {
            return new Cardinality( this).set( getCardinality() - toSubtract.getCardinality());
        }

        public boolean hasCardinality(){
            return this.cardinality > 0;
        }

        public int getCardinality() {
            return cardinality;
        }


        public String getTypeName() {
            return typeName;
        }
        public OWLObjectProperty getProperty() {
            return property;
        }
        public OWLClass getType() {
            return type;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Cardinality)) return false;

            Cardinality that = (Cardinality) o;

            if (getCardinality() != that.getCardinality()) return false;
            if (getTypeName() != null ? !getTypeName().equals(that.getTypeName()) : that.getTypeName() != null)
                return false;
            if (getProperty() != null ? !getProperty().equals(that.getProperty()) : that.getProperty() != null)
                return false;
            return getType() != null ? getType().equals(that.getType()) : that.getType() == null;
        }

        @Override
        public int hashCode() {
            int result = getTypeName() != null ? getTypeName().hashCode() : 0;
            result = 31 * result + getCardinality();
            result = 31 * result + (getProperty() != null ? getProperty().hashCode() : 0);
            result = 31 * result + (getType() != null ? getType().hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            String out = "[" + cardinality + ']';
            if( typeName == null)
                return "d" + out;
            if( typeName.isEmpty())
                return "d" + out;
            return padString(typeName, LOGGING.LENGTH_MEDIUM, true) + out;
        }

        @Override
        public Cardinality copy() {
            return new Cardinality( this);
        }
    }
}
