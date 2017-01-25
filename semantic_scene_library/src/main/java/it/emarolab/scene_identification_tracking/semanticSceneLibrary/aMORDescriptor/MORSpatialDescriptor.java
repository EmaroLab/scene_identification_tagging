package it.emarolab.scene_identification_tracking.semanticSceneLibrary.aMORDescriptor;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.Logger;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.Semantics;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.objects.ObjectSemantics;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.objects.SpatialSemantics;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.objects.aMOR.MORPrimitive;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.scene.SceneRepresentation;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.scene.SceneRepresentation.MORSpatialCollector;
import org.semanticweb.owlapi.model.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by bubx on 17/01/17.
 */
public interface MORSpatialDescriptor extends SpatialSemantics.SpatialDescriptor<OWLReferences,OWLNamedIndividual,OWLClass,OWLDataProperty,OWLObjectProperty,OWLLiteral> {
// todo move after MORDescriptor.... SceneRepresentation should not see this interface!!


    @Override
    default MORSpatialCollector queryBehind() {
        return new MORSpatialRelation( getOntology(), getInstance(),
                SpatialSemantics.DefaultNames.BEHIND_PROPERTY_NAME).asSet();
    }
    @Override
    default MORSpatialCollector queryFront() {
        MORSpatialRelation front = new MORSpatialRelation( getOntology(), getInstance(),
                SpatialSemantics.DefaultNames.BEHIND_INVERSE_PROPERTY_NAME);
        front.setInverseProperty(  SpatialSemantics.DefaultNames.BEHIND_PROPERTY_NAME);
        return front.asSet();
    }
    @Override
    default MORSpatialCollector queryAbove() {
        return new MORSpatialRelation( getOntology(), getInstance(),
                SpatialSemantics.DefaultNames.ABOVE_PROPERTY_NAME).asSet();
    }
    @Override
    default MORSpatialCollector queryBelow() {
        MORSpatialRelation below = new MORSpatialRelation( getOntology(), getInstance(),
                SpatialSemantics.DefaultNames.ABOVE_INVERSE_PROPERTY_NAME);
        below.setInverseProperty( SpatialSemantics.DefaultNames.ABOVE_PROPERTY_NAME);
        return below.asSet();
    }
    @Override
    default MORSpatialCollector queryRight() {
        return new MORSpatialRelation( getOntology(), getInstance(),
                getOntology().getOWLObjectProperty( SpatialSemantics.DefaultNames.RIGHT_PROPERTY_NAME)).asSet();
    }
    @Override
    default MORSpatialCollector queryLeft() {
        MORSpatialRelation left = new MORSpatialRelation( getOntology(), getInstance(),
                SpatialSemantics.DefaultNames.RIGHT_INVERSE_PROPERTY_NAME);
        left.setInverseProperty( SpatialSemantics.DefaultNames.RIGHT_PROPERTY_NAME);
        return left.asSet();

    }
    @Override
    default MORSpatialCollector queryAlongX() {
        return new MORSpatialRelation( getOntology(), getInstance(),
                SpatialSemantics.DefaultNames.ALONGX_PROPERTY_NAME).asSet();
    }
    @Override
    default MORSpatialCollector queryAlongY() {
        return new MORSpatialRelation( getOntology(), getInstance(),
                SpatialSemantics.DefaultNames.ALONGY_PROPERTY_NAME).asSet();
    }
    @Override
    default MORSpatialCollector queryAlongZ() {
        return new MORSpatialRelation( getOntology(), getInstance(),
                SpatialSemantics.DefaultNames.ALONGZ_PROPERTY_NAME).asSet();
    }
    default MORSpatialCollector queryAlong() {
        MORSpatialCollector out = new MORSpatialCollector( getOntology());
        out.addAll( queryAlongX());
        out.addAll( queryAlongY());
        out.addAll( queryAlongZ());
        return out;
    }
    @Override
    default MORSpatialCollector queryCoaxial() {
        return new MORSpatialRelation( getOntology(), getInstance(),
                SpatialSemantics.DefaultNames.COAXIAL_PROPERTY_NAME).asSet();
    }
    @Override
    default MORSpatialCollector queryParallel() {
        return new MORSpatialRelation( getOntology(), getInstance(),
                SpatialSemantics.DefaultNames.PARALLEL_PROPERTY_NAME).asSet();
    }
    @Override
    default MORSpatialCollector queryPerpendicular(){
        return new MORSpatialRelation( getOntology(), getInstance(),
                SpatialSemantics.DefaultNames.PERPENDICULAR_PROPERTY_NAME).asSet();
    }

    @Override
    default MORSpatialCollector querySpatialProperties() {
        MORSpatialCollector out = new MORSpatialCollector( getOntology());
        out.addAll( queryBehind());
        out.addAll( queryFront());
        out.addAll( queryAbove());
        out.addAll( queryBelow());
        out.addAll( queryRight());
        out.addAll( queryLeft());
        out.addAll( queryAlong());
        out.addAll( queryCoaxial());
        out.addAll( queryParallel());
        out.addAll( queryPerpendicular());
        return out;
    }

    class MORSpatialRelation extends SpatialSemantics.SpatialRelation<
            OWLClass, OWLNamedIndividual, OWLObjectProperty, MORSpatialItem>{

        private OWLReferences ontoRef;

        public MORSpatialRelation( OWLReferences ontoRef, OWLNamedIndividual object, String property) {
            super( null, null, null);
            initialise( ontoRef,object, ontoRef.getOWLObjectProperty( property));
        }
        public MORSpatialRelation( OWLReferences ontoRef, OWLNamedIndividual object, OWLObjectProperty property) {
            super( null, null, null);
            initialise( ontoRef, object, property);
        }

        public MORSpatialRelation(MORSpatialRelation rel) {
            super(rel);
            this.ontoRef = rel.ontoRef;
        }

        public void initialise( OWLReferences ontoref, OWLNamedIndividual object, OWLObjectProperty property) {
            this.setProperty( property, object); // todo make two setters
            this.ontoRef = ontoref;
            this.setItems( this.queryItems());
            OWLClass type = MORPrimitive.getObjectShape( ontoref, getObject());
            this.setType( type);
        }

        public String getObjectName(){
            return this.ontoRef.getOWLObjectName( this.getObject());
        }
        public String getObjectName(int length){
            return getObjectName( length, false);
        }
        public String getObjectName(int length, boolean right){
            return Logger.getfixedStringLength( getObjectName(), length, right);
        }

        public String getTypeName(){
            return this.ontoRef.getOWLObjectName( this.getType());
        }
        public String getTypeName(int length){
            return getTypeName( length, true);
        }
        public String getTypeName(int length, boolean right){
            return Logger.getfixedStringLength( getTypeName(), length, right);
        }

        public String getPropertyName(){
            return this.ontoRef.getOWLObjectName( this.getProperty());
        }
        public String getPropertyName(int length){
            return getPropertyName( length, true);
        }
        public String getPropertyName(int length, boolean right){
            return Logger.getfixedStringLength( getPropertyName(), length, right);
        }

        public void setInverseProperty(String inverseProperty) {
            this.setInverseProperty( ontoRef.getOWLObjectProperty(inverseProperty));
        }

        @Override
        public Set< MORSpatialItem> queryItems() {
            Set< MORSpatialItem> items = new HashSet<>();
            for ( OWLNamedIndividual i :  this.ontoRef.getObjectPropertyB2Individual( this.getObject(), this.getProperty()))
                items.add( new MORSpatialItem( this.ontoRef, i));
            return items;
        }

        public MORSpatialCollector asSet() {
            MORSpatialCollector out = new MORSpatialCollector( this.ontoRef);
            out.add( this);
            return out;
        }

        @Override
        public String toString() {
            return "(" + this.getTypeName(LOGGING_NAME_LENGTH) + "#" + this.getObjectName( LOGGING_NUMBER_LENGTH) + ")  "
                    + getPropertyName(LOGGING_NAME_LENGTH) + " : " + getItems();
        }
    }

    class MORSpatialItem extends SpatialSemantics.SpatialItem<OWLClass, OWLNamedIndividual>{

        protected OWLReferences ontoRef;

        //private MORSpatialItem(){}//for copy

        public MORSpatialItem( OWLReferences ontoRef, OWLNamedIndividual object) {
            super(object, null);
            this.ontoRef = ontoRef;

            this.setType( MORPrimitive.getObjectShape( this.ontoRef, object));
        }

        public MORSpatialItem(MORSpatialItem copy) {
            super( copy);
            this.ontoRef = copy.ontoRef;
        }

        public String getObjectName(){
            return this.ontoRef.getOWLObjectName( this.getObject());
        }
        public String getObjectName( int length){
            return getStringfixedLength( getObjectName(), length, false);
        }


        public String getTypeName(){
            return this.ontoRef.getOWLObjectName( this.getType());
        }
        public String getTypeName( int length){
            return getStringfixedLength( getTypeName(), length, true);
        }

        @Override
        public String toString() {
            return this.getTypeName( LOGGING_SHORT_NAME_LENGTH) + "#" + this.getObjectName( LOGGING_NUMBER_LENGTH);
        }
    }

    /**
     * The aMOR interface to manipulate an {@link OWLOntology}
     * and give a semantic to a  {@link ObjectSemantics.Primitive} object.
     * <p>
     *     Each instance of this class should be assigned to the description of an object.
     *     Indeed, it is a container describing:
     *     <ul>
     *        <li> the OWL ontology (as an {@link OWLReferences}), retrieved by name.
     *             This representation is the one in which the related object will be mapped (read/write).
     *        <li> an OWL Individual, defined by name, that semantically describes the related object.
     *     </ul>
     * <p>
     *     <b>REMARK</b>: see <a href="https://github.com/EmaroLab/multi_ontology_reference">here</a>
     *     for more about the aMOR library.
     *
     * <div style="text-align:center;"><small>
     * <b>File</b>:       it.emarolab.scene_identification_tracking.semanticSceneLibrary.Semantics <br>
     * <b>Licence</b>:    GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
     * <b>Author</b>:     Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
     * <b>affiliation</b>: DIBRIS, EMAROLab, University of Genoa. <br>
     * <b>date</b>:       06/01/2017 <br>
     * </small></div>
     *
     *  @see Semantics
     *  @see Semantics.IndividualDescriptor
     *  @see MORPrimitive
     *  @see MOR3DArray
     */
    class MORSimpleDescriptor extends MORIndividualDescriptor.MORSimpleDescriptor implements MORSpatialDescriptor {
        public MORSimpleDescriptor(MORSpatialDescriptor.MORSimpleDescriptor descriptor) {
            super(descriptor);
        }
        public MORSimpleDescriptor(String individualName) {
            super(individualName);
        }
        public MORSimpleDescriptor(OWLNamedIndividual individual, OWLReferences ontoRef) {
            super(individual, ontoRef);
        }
        public MORSimpleDescriptor(String individualName, String ontoName) {
            super(individualName, ontoName);
        }
    }
}
