package it.emarolab.scene_identification_tracking.semanticSceneLibrary.aMORDescriptor;

import it.emarolab.amor.owlInterface.OWLEnquirer;
import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.amor.owlInterface.OWLReferencesInterface;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.Logger;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.Semantics;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.scene.SceneRepresentation;
import org.semanticweb.owlapi.model.*;

import java.util.HashSet;
import java.util.Set;

import static it.emarolab.scene_identification_tracking.semanticSceneLibrary.scene.SceneSemantics.INITIAL_SCENE_TYPE;

public interface MORClassDescriptor extends Semantics.ClassDescriptor<OWLReferences, OWLClass> {

    default String getOntologyName(){
        return getOntology().getReferenceName();
    }
    default String getInstanceName(){
        return getOntology().getOWLObjectName( getInstance());
    }

    default void setOntology(String ontoName) {
        OWLReferences ontoRef = (OWLReferences) OWLReferencesInterface.OWLReferencesContainer.getOWLReferences(ontoName);
        if ( this.getOntology() == null)
            Logger.SITBase.logERROR( "MORClassDescriptor did not find any instantiated OWLReferences with name: " + ontoName);
        else this.setOntology( ontoRef);
    }
    default void setInstance(String instanceName) {
        if ( this.getOntology() != null){
            if( instanceName == null)
                Logger.SITBase.logERROR( "MORClassDescriptor cannot set 'null' instance name");
            else this.setInstance( getOntology().getOWLClass( instanceName));
            return;
        }
        Logger.SITBase.logERROR( "MORClassDescriptor cannot set instance name: " + instanceName + ", with 'null' ontology.");
    }

    @Override
    default Semantics.WritingState writeSuperInstances(){
        return new TryingWrite() {
            protected Semantics.WritingState giveAtry() {
                Semantics.WritingState state = new Semantics.WritingState();
                Set<OWLClass> superCl = getOntology().getSuperClassOf( getInstance());
                boolean doit = true;
                if( superCl != null) {
                    if( superCl.isEmpty())
                        state.asAdded();
                    if (getSuperInstances().equals(superCl)) {
                        state = state.merge( new Semantics.WritingState().asNotChanged());
                        doit = false;
                    }

                    if( superCl.removeAll(getSuperInstances()))
                        state = state.merge( new Semantics.WritingState().asRemoved());
                    for (OWLClass c : superCl)
                        getOntology().removeSubClassOf(c, getInstance());
                }
                if ( doit)
                    for( OWLClass c : getSuperInstances())
                        getOntology().addSubClassOf( c, getInstance());
                state.asUpdated();
                log( this.getClass().getSimpleName() + "\t WRITES SUPER CLASSES: \"" + getOntology().getOWLObjectName( getSuperInstances())
                        + "\t\tremoved: " + getOntology().getOWLObjectName( superCl)
                        + "\t\t[" + state + "]");
                return state;
            }
        }.perform();
    }
    @Override
    default Semantics.WritingState writeSubInstances(){
        return new TryingWrite() {
            protected Semantics.WritingState giveAtry() {
                Semantics.WritingState state = new Semantics.WritingState();
                Set<OWLClass> subCl = getOntology().getSubClassOf( getInstance());
                boolean doit = true;
                if( subCl != null) {
                    if( subCl.isEmpty())
                        state.asAdded();
                    if (getSubInstances().equals(subCl)) {
                        state = state.merge( new Semantics.WritingState().asNotChanged());
                        doit = false;
                    }

                    if( subCl.removeAll(getSubInstances()))
                        state = state.merge( new Semantics.WritingState().asRemoved());
                    for (OWLClass c : subCl)
                        getOntology().removeSubClassOf( getInstance(), c);
                }
                if ( doit)
                    for( OWLClass c : getSubInstances())
                        getOntology().addSubClassOf( getInstance(), c);
                state.asUpdated();
                log( this.getClass().getSimpleName() + "\t WRITES SUB CLASSES: \"" + getOntology().getOWLObjectName( getSubInstances())
                        + "\t\tremoved: " + getOntology().getOWLObjectName( subCl)
                        + "\t\t[" + state + "]");
                return state;
            }
        }.perform();
    }

    @Override
    default Semantics.ReadingState readSuperInstances(){
        return new TryingRead() {
            protected Semantics.ReadingState giveAtry() {
                Semantics.ReadingState state = new Semantics.ReadingState();
                Set<OWLClass> readed = getOntology().getSuperClassOf( getInstance());
                if( readed != null){
                    if( readed.isEmpty())
                        state.asAbsent();
                    if( readed.equals( getSuperInstances()))
                        state = state.merge( new Semantics.ReadingState().asNotChanged());


                    getSuperInstances().clear();
                    for (OWLClass c : readed )
                        getSuperInstances().add(c);
                    state = state.merge( new Semantics.ReadingState().asSuccess());
                }

                log( this.getClass().getSimpleName() + "\t READ SUPER CLASSES: \""
                        + "\t\tnew sub classes: " + getOntology().getOWLObjectName( readed) + ")"
                        + "\t\t[" + state + "]");
                return state;
            }
        }.perform();
    }
    @Override
    default Semantics.ReadingState readSubInstances(){
        return new TryingRead() {
            protected Semantics.ReadingState giveAtry() {
                Semantics.ReadingState state = new Semantics.ReadingState();
                Set<OWLClass> readed = getOntology().getSubClassOf( getInstance());
                if( readed != null){
                    if( readed.isEmpty())
                        state.asAbsent();
                    if( readed.equals( getSubInstances()))
                        state = state.merge( new Semantics.ReadingState().asNotChanged());


                    getSubInstances().clear();
                    for (OWLClass c : readed )
                        getSubInstances().add(c);
                    state = state.merge( new Semantics.ReadingState().asSuccess());
                }

                log( this.getClass().getSimpleName() + "\t READ SUB CLASSES: \""
                        + "\t\tnew sub classes: " + getOntology().getOWLObjectName( readed) + ")"
                        + "\t\t[" + state + "]");
                return state;
            }
        }.perform();

    }

    class MORSimpleClassDescriptor extends Logger.SITBase implements MORClassDescriptor{

        private OWLReferences ontology;
        private OWLClass instance;
        private Set<OWLClass> subClasses = new HashSet<>(), superClasses = new HashSet<>();

        public MORSimpleClassDescriptor( MORSimpleClassDescriptor copy) {
            this.ontology = copy.ontology;
            this.instance = copy.instance;
            this.subClasses = new HashSet<>( copy.subClasses);
            this.superClasses = new HashSet<>( copy.superClasses);
        }
        public MORSimpleClassDescriptor(String ontoName) {
            this.setOntology( ontoName);
        }
        public MORSimpleClassDescriptor(OWLReferences ontology) {
            this.setOntology( ontology);
        }
        public MORSimpleClassDescriptor(String ontoName, String instanceName) {
            this.setOntology( ontoName);
            this.setInstance( getOntology().getOWLClass( instanceName));
        }
        public MORSimpleClassDescriptor(OWLReferences ontology, String instanceName) {
            this.setOntology( ontology);
            this.setInstance( getOntology().getOWLClass( instanceName));
        }
        public MORSimpleClassDescriptor(OWLReferences ontology, OWLClass instance) {
            this.setOntology( ontology);
            this.setInstance( instance);
        }

        @Override
        public Set<OWLClass> getSubInstances() {
            return subClasses;
        }
        @Override
        public Set<OWLClass> getSuperInstances() {
            return superClasses;
        }

        @Override
        public OWLReferences getOntology() {
            return ontology;
        }
        @Override
        public void setOntology(OWLReferences ontology) {
            this.ontology = ontology;
        }

        @Override
        public OWLClass getInstance() {
            return instance;
        }
        @Override
        public void setInstance(OWLClass instance) {
            this.instance = instance;
        }
    }


    interface MORClassSceneDescriptor extends MORClassDescriptor, SceneRepresentation.ClassSceneDescriptor<OWLReferences, OWLClass>{

        @Override
        default Semantics.WritingState writeSceneCardinality() {
            return new TryingWrite() {
                protected Semantics.WritingState giveAtry() {
                    Semantics.WritingState state;
                    if( getCardinality() == null) {
                        logERROR("MORClassSceneDescriptor cannot write scne with 'null' cardinality");
                        state = new Semantics.WritingState().asError();
                    }else {
                        // todo manage not changed
                        setCardinality( getCardinality());

                        writeShapeCardinality( getCardinality().getAboveCardinality());
                        writeShapeCardinality( getCardinality().getBehindCardinality());
                        writeShapeCardinality( getCardinality().getRightCardinality());
                        writeShapeCardinality( getCardinality().getAlongXcardinality());
                        writeShapeCardinality( getCardinality().getAlongYcardinality());
                        writeShapeCardinality( getCardinality().getAlongZcardinality());
                        writeShapeCardinality( getCardinality().getParallelCardinality());
                        writeShapeCardinality( getCardinality().getPerpendicularCardinality());
                        writeShapeCardinality( getCardinality().getCoaxialCardinality());

                        getOntology().convertSuperClassesToEquivalentClass( getInstance());

                        state = new Semantics.WritingState().asUpdated();
                    }
                    return state;
                }
                private void writeShapeCardinality(SceneRepresentation.ShapeCardinality cardinality){
                    writeCardinality( cardinality.getPrimitiveCardinality());
                    writeCardinality( cardinality.getSphereCardinality());
                    writeCardinality( cardinality.getOrientableCardinality());
                    writeCardinality( cardinality.getPlaneCardinality());
                    writeCardinality( cardinality.getConeCardinality());
                    writeCardinality( cardinality.getCylinderCardinality());
                }
                private void writeCardinality(SceneRepresentation.Cardinality cardinality){
                    if( cardinality.getCardinality() > 0) {
                        getOntology().addMinObjectClassExpression(getInstance(), cardinality.getProperty(), cardinality.getCardinality(), cardinality.getType());
                        log( "Write scene class restriction: \t" + getInstanceName() + " " +
                                getOntology().getOWLObjectName(cardinality.getProperty()) + " min " +
                                cardinality.getCardinality() + " " +
                                getOntology().getOWLObjectName(cardinality.getType()) + "\t [" + new Semantics.WritingState().asUpdated() + "]");
                    }
                }
            }.perform();
        }

        @Override
        default Semantics.ReadingState readSceneCardinality() {
            return new TryingRead() {
                protected Semantics.ReadingState giveAtry() {

                    SceneRepresentation.SceneCardinality card = new SceneRepresentation.SceneCardinality( getOntology());
                    log( "read scene cardinality restriction: \n");
                    for( OWLEnquirer.ClassRestriction r : getOntology().getClassRestrictions(getInstance())) {
                        log( "\t\t\t" + r);
                        if (r.restrictsOverObjectProperty()) {
                            card.addCardinality(r.getObjectProperty(), r.getObjectRestriction(), r.getCardinality());
                        }
                    }
                    return new Semantics.ReadingState(); // todo to set

                }
            }.perform();
        }

    }
    class MORSimpleClassSceneDescriptor extends MORSimpleClassDescriptor implements MORClassSceneDescriptor{

        private SceneRepresentation.SceneCardinality cardinality;

        public MORSimpleClassSceneDescriptor( MORSimpleClassSceneDescriptor copy) {
            super( copy);
            this.cardinality = copy.cardinality;
        }

        public MORSimpleClassSceneDescriptor(String ontoName) {
            super(ontoName);
            this.cardinality = new SceneRepresentation.SceneCardinality(getOntology());
            getSuperInstances().add( getOntology().getOWLClass( INITIAL_SCENE_TYPE));//make it settable
        }
        public MORSimpleClassSceneDescriptor(OWLReferences ontology) {
            super(ontology);
            this.cardinality = new SceneRepresentation.SceneCardinality(getOntology());
            getSuperInstances().add( getOntology().getOWLClass( INITIAL_SCENE_TYPE));
        }
        public MORSimpleClassSceneDescriptor(String ontoName, String instanceName) {
            super(ontoName, instanceName);
            this.cardinality = new SceneRepresentation.SceneCardinality(getOntology());
            getSuperInstances().add( getOntology().getOWLClass( INITIAL_SCENE_TYPE));
        }
        public MORSimpleClassSceneDescriptor(OWLReferences ontology, String instanceName) {
            super(ontology, instanceName);
            this.cardinality = new SceneRepresentation.SceneCardinality(getOntology());
            getSuperInstances().add( getOntology().getOWLClass( INITIAL_SCENE_TYPE));
        }
        public MORSimpleClassSceneDescriptor(OWLReferences ontology, OWLClass instance) {
            super(ontology, instance);
            this.cardinality = new SceneRepresentation.SceneCardinality(getOntology());
            getSuperInstances().add( instance);
        }

        @Override
        public SceneRepresentation.SceneCardinality getCardinality() {
            return cardinality;
        }

        @Override
        public void setCardinality(SceneRepresentation.SceneCardinality cardinality) {
            this.cardinality = cardinality;
        }

    }

    // todo remove (use variable across this file) (already done here, to be done in others)
    abstract class MappingTry< Y extends Semantics.MappingState> extends Semantics.MappingTry< Y> {

        abstract  protected Y onOWLException();

        @Override
        public Y perform() {
            try {
                return giveAtry();
            } catch (openllet.owlapi.OWLException e){
                logError( e);
                return onOWLException();
            } catch (Exception e) {
                logError( e);
                return onError();
            }
        }
    }
    /**
     * The {@link MORSpatialDescriptor.MORSimpleDescriptor.MappingTry} implementation for reading operations ({@link Semantics.ReadingState}).
     *
     * <div style="text-align:center;"><small>
     * <b>File</b>:       it.emarolab.scene_identification_tracking.semanticSceneLibrary.Semantics <br>
     * <b>Licence</b>:    GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
     * <b>Author</b>:     Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
     * <b>affiliation</b>: DIBRIS, EMAROLab, University of Genoa. <br>
     * <b>date</b>:       06/01/2017 <br>
     * </small></div>
     *
     * @see MORSpatialDescriptor.MORSimpleDescriptor.MappingTry
     * @see Semantics.ReadingState
     */
    abstract class TryingRead extends MappingTry<Semantics.ReadingState> {

        protected Semantics.ReadingState merge(Semantics.ReadingState r1, Semantics.ReadingState r2) {
            return Semantics.ReadingState.combineResults(r1, r2);
        }

        @Override
        protected Semantics.ReadingState onOWLException(){
            return new Semantics.ReadingState().asInconsistent();
        }

        @Override
        protected Semantics.ReadingState onError(){
            return new Semantics.ReadingState().asError();
        }
    }
    /**
     * The {@link MORSpatialDescriptor.MORSimpleDescriptor.MappingTry} implementation for writing operations ({@link Semantics.WritingState}).
     *
     * <div style="text-align:center;"><small>
     * <b>File</b>:       it.emarolab.scene_identification_tracking.semanticSceneLibrary.Semantics <br>
     * <b>Licence</b>:    GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
     * <b>Author</b>:     Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
     * <b>affiliation</b>: DIBRIS, EMAROLab, University of Genoa. <br>
     * <b>date</b>:       06/01/2017 <br>
     * </small></div>
     *
     * @see Semantics.MappingTry
     * @see Semantics.WritingState
     */
    abstract class TryingWrite extends MappingTry<Semantics.WritingState> {

        protected Semantics.WritingState merge(Semantics.WritingState r1, Semantics.WritingState r2) {
            return Semantics.WritingState.combineResults( r1, r2);
        }

        @Override
        protected Semantics.WritingState onOWLException(){
            return new Semantics.WritingState().asInconsistent();
        }

        @Override
        protected Semantics.WritingState onError(){
            return new Semantics.WritingState().asError();
        }
    }
}
