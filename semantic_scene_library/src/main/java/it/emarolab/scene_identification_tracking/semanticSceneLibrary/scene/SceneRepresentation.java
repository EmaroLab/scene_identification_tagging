package it.emarolab.scene_identification_tracking.semanticSceneLibrary.scene;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.Logger;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.Semantics;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.aMORDescriptor.MORSpatialDescriptor;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.objects.SpatialSemantics;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.aMORDescriptor.MORSpatialDescriptor.MORSpatialRelation;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.aMORDescriptor.MORSpatialDescriptor.MORSpatialItem;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.objects.aMOR.*;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import java.util.HashSet;
import java.util.Set;

import static it.emarolab.scene_identification_tracking.semanticSceneLibrary.Logger.SITBase.LOGGING_LONG_NAME_LENGTH;

public interface SceneRepresentation {

    interface ClassSceneDescriptor<O,C> extends Semantics.ClassDescriptor<O,C> {

        Semantics.WritingState writeSceneCardinality();
        Semantics.ReadingState readSceneCardinality(); // old cardinality?
        SceneRepresentation.SceneCardinality getCardinality();
        void setCardinality( SceneRepresentation.SceneCardinality cardinality);

        // todo add interface for SWRL
    }

    class MORSpatialCollector extends HashSet<MORSpatialRelation> {

        private OWLReferences ontoRef;

        public MORSpatialCollector(MORSpatialCollector copy){
            this.ontoRef = copy.ontoRef;
            for( MORSpatialRelation rel : copy)
                add( new MORSpatialRelation( rel));
        }

        public MORSpatialCollector(OWLReferences ontoRef){
            this.ontoRef = ontoRef;
        }

        public boolean hasUniqueApplyingObject(){
            if( getApplyngObject().size() == 1)
                return true;
            else return false;
        }
        public Set< MORSpatialItem> getApplyngObject(){
            Set< MORSpatialItem> out = new HashSet<>();
            for( MORSpatialRelation r : this)
                out.add( new MORSpatialItem( this.ontoRef, r.getObject()));
            return out;
        }

        public void clean(){
            Set< MORSpatialRelation> toRemove = new HashSet<>();
            for( MORSpatialRelation r : this)
                if( r.getItems().isEmpty())
                    toRemove.add( r);
            this.removeAll( toRemove);
        }

        public MORScene getSceneAtoms(){
            Set< MORSceneAtom> atoms = new HashSet<>();
            SceneCardinality cardinality = new SceneCardinality( ontoRef);
            for( MORSpatialRelation r : this){
                // try to add the subject of the relation
                MORSceneAtom subject = new MORSceneAtom(ontoRef, r.getObject(), r.getInverseProperty());
                if( atoms.add( subject))
                    addRelationCardinality( subject.getPropertyName(), subject.getTypeName(), cardinality);
                // try to add the the objects of the relations
                Set< MORSpatialItem> items = r.getItems();
                for( MORSpatialItem i : r.getItems()) {
                    MORSceneAtom object = new MORSceneAtom(ontoRef, i.getObject(), r.getInverseProperty());
                    if (atoms.add( object))
                        addRelationCardinality( subject.getPropertyName(), i.getTypeName(), cardinality);
                }
            }
            return new MORScene( atoms, cardinality);
        }
        private void addRelationCardinality(String property, String type, SceneCardinality cardinality){
            if( property.equals( DEFAULT_NAME_COAXIAL_COMPONENT))
                addShapeCardinality( type, cardinality.getCoaxialCardinality());
            if( property.equals( DEFAULT_NAME_PARALLEL_COMPONENT))
                addShapeCardinality( type, cardinality.getParallelCardinality());
            if( property.equals( DEFAULT_NAME_PERPENDICULAR_COMPONENT))
                addShapeCardinality( type, cardinality.getPerpendicularCardinality());
            if( property.equals( DEFAULT_NAME_ALONGX_COMPONENT))
                addShapeCardinality( type, cardinality.getAlongXcardinality());
            if( property.equals( DEFAULT_NAME_ALONGY_COMPONENT))
                addShapeCardinality( type, cardinality.getAlongYcardinality());
            if( property.equals( DEFAULT_NAME_ALONGZ_COMPONENT))
                addShapeCardinality( type, cardinality.getAlongZcardinality());
            if( property.equals( DEFAULT_NAME_RIGHT_COMPONENT))
                addShapeCardinality( type, cardinality.getRightCardinality());
            if( property.equals( DEFAULT_NAME_ABOVE_COMPONENT))
                addShapeCardinality( type, cardinality.getAboveCardinality());
            if( property.equals( DEFAULT_NAME_BEHIND_COMPONENT))
                addShapeCardinality( type, cardinality.getBehindCardinality());
        }
        private void addShapeCardinality(String type, ShapeCardinality cardinality){
            if( type.equals( MORPrimitive.INITIAL_PRIMITIVE_TYPE))
                cardinality.getPrimitiveCardinality().increase();
            if( type.equals( MORSphere.INITIAL_SPHERE_TYPE))
                cardinality.getSphereCardinality().increase();
            if( type.equals( MOROrientable.INITIAL_ORIENTABLE_TYPE))
                cardinality.getOrientableCardinality().increase();
            if( type.equals( MORPlane.INITIAL_PLANE_TYPE))
                cardinality.getPlaneCardinality().increase();
            if( type.equals( MORCone.INITIAL_CONE_TYPE))
                cardinality.getConeCardinality().increase();
            if( type.equals( MORCylinder.INITIAL_CYLINDER_TYPE))
                cardinality.getCylinderCardinality().increase();
        }

        @Override
        public String toString() {
            Set< MORSpatialItem> items = getApplyngObject();
            if( items.isEmpty())
                return "[empty spatial rations]";
            if( items.size() == 1) {
                String out = "query spatial properties for individual: " + getApplyngObject() + "{\n";
                int cnt = 0;
                for (MORSpatialRelation r : this) {
                    out += "   -" + (cnt + 1) + ".\t\t" + r.getPropertyName(LOGGING_LONG_NAME_LENGTH) + "." + r.getItems();
                    if (++cnt < size())
                        out += ";\n";
                    else out += "\n}";
                }
                return out;
            }
            String out = "query spatial properties: {\n";
            int cnt = 0;
            for (MORSpatialRelation r : this) {
                out += "   -" + (cnt + 1) + ".\t\t" + r;
                if (++cnt < size())
                    out += ";\n";
                else out += "\n}";
            }
            return out;
        }
    }

    class MORScene{ // todo move away
        private Set< MORSceneAtom> atoms;
        private SceneCardinality cardinality;

        public MORScene(Set<MORSceneAtom> atoms, SceneCardinality cardinality) {
            this.atoms = atoms;
            this.cardinality = cardinality;
        }

        public Set<MORSceneAtom> getAtoms() {
            return atoms;
        }
        public SceneCardinality getCardinality() {
            return cardinality;
        }
    }
    class SceneCardinality extends Logger.SITBase{
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
            behindCardinality.setSemantics( ontoRef, DEFAULT_NAME_BEHIND_COMPONENT);
            aboveCardinality.setSemantics( ontoRef, DEFAULT_NAME_ABOVE_COMPONENT);
            rightCardinality.setSemantics( ontoRef, DEFAULT_NAME_RIGHT_COMPONENT);
            alongXcardinality.setSemantics( ontoRef, DEFAULT_NAME_ALONGX_COMPONENT);
            alongYcardinality.setSemantics( ontoRef, DEFAULT_NAME_ALONGY_COMPONENT);
            alongZcardinality.setSemantics( ontoRef, DEFAULT_NAME_ALONGZ_COMPONENT);
            parallelCardinality.setSemantics( ontoRef, DEFAULT_NAME_PARALLEL_COMPONENT);
            perpendicularCardinality.setSemantics( ontoRef, DEFAULT_NAME_PERPENDICULAR_COMPONENT);
            coaxialCardinality.setSemantics( ontoRef, DEFAULT_NAME_COAXIAL_COMPONENT);
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
             logWARNING( "cannot add any cardinal restriction on data property: " + spatialRelation);
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
                    getStringfixedLength( "Behind/Front : ", LOGGING_LONG_NAME_LENGTH,true) + behindCardinality + ";\n" +
                    getStringfixedLength( "Above/Below : ", LOGGING_LONG_NAME_LENGTH,true) + aboveCardinality + ";\n" +
                    getStringfixedLength( "Right/Left : ", LOGGING_LONG_NAME_LENGTH,true) + rightCardinality + ";\n" +
                    getStringfixedLength( "Along X : ", LOGGING_LONG_NAME_LENGTH,true)  + alongXcardinality + ";\n" +
                    getStringfixedLength( "Y : ", LOGGING_LONG_NAME_LENGTH,true)  + alongYcardinality + ";\n" +
                    getStringfixedLength( "Z : ", LOGGING_LONG_NAME_LENGTH,true)  + alongZcardinality + ";\n" +
                    getStringfixedLength( "Parallel : ", LOGGING_LONG_NAME_LENGTH,true)  + parallelCardinality + ";\n" +
                    getStringfixedLength( "Perpendicular : ", LOGGING_LONG_NAME_LENGTH,true)  + perpendicularCardinality + ";\n" +
                    getStringfixedLength( "Coaxial : ", LOGGING_LONG_NAME_LENGTH,true)  + coaxialCardinality + "\n}";
        }
    }
    class ShapeCardinality extends Logger.SITBase{

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
            this.primitiveCardinality.setSemantics( ontoRef, MORPrimitive.INITIAL_PRIMITIVE_TYPE, property);
            this.sphereCardinality.setSemantics( ontoRef, MORSphere.INITIAL_SPHERE_TYPE, property);
            this.orientableCardinality.setSemantics( ontoRef, MOROrientable.INITIAL_ORIENTABLE_TYPE, property);
            this.coneCardinality.setSemantics( ontoRef, MORCone.INITIAL_CONE_TYPE, property);
            this.cylinderCardinality.setSemantics( ontoRef, MORCylinder.INITIAL_CYLINDER_TYPE, property);
            this.planeCardinality.setSemantics( ontoRef, MORPlane.INITIAL_PLANE_TYPE, property);
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
    }
    class Cardinality extends Logger.SITBase{
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
            return getStringfixedLength(typeName, LOGGING_MEDIUM_LENGTH, true) + out;
        }
    }

    // todo to move on scene package
    public static final String DEFAULT_NAME_COAXIAL_COMPONENT = "has-scene_coaxial";
    public static final String DEFAULT_NAME_PARALLEL_COMPONENT = "has-scene_parallel";
    public static final String DEFAULT_NAME_PERPENDICULAR_COMPONENT = "has-scene_perpendicular";
    public static final String DEFAULT_NAME_ALONGX_COMPONENT = "has-scene_alongX";
    public static final String DEFAULT_NAME_ALONGY_COMPONENT = "has-scene_alongY";
    public static final String DEFAULT_NAME_ALONGZ_COMPONENT = "has-scene_alongZ";
    public static final String DEFAULT_NAME_RIGHT_COMPONENT = "has-scene_right";
    public static final String DEFAULT_NAME_ABOVE_COMPONENT = "has-scene_above";
    public static final String DEFAULT_NAME_BEHIND_COMPONENT = "has-scene_behind";

    class MORSceneAtom extends MORSpatialDescriptor.MORSpatialItem {

        private OWLObjectProperty property;

        public MORSceneAtom( MORSceneAtom copy){
            super( copy);
            this.property = copy.property;
        }
        public MORSceneAtom(OWLReferences ontoRef, OWLNamedIndividual object, OWLObjectProperty property) {
            super(ontoRef, object); // you can get the type here instead of reading it
            this.property = mapSceneProperty( property);
        }
        private OWLObjectProperty mapSceneProperty( OWLObjectProperty spatialProperty){
            String propertyName = ontoRef.getOWLObjectName( spatialProperty);
            if( propertyName.equals(SpatialSemantics.DefaultNames.ABOVE_PROPERTY_NAME))
                return ontoRef.getOWLObjectProperty( DEFAULT_NAME_ABOVE_COMPONENT);
            if( propertyName.equals(SpatialSemantics.DefaultNames.BEHIND_PROPERTY_NAME))
                return ontoRef.getOWLObjectProperty( DEFAULT_NAME_BEHIND_COMPONENT);
            if( propertyName.equals(SpatialSemantics.DefaultNames.RIGHT_PROPERTY_NAME))
                return ontoRef.getOWLObjectProperty( DEFAULT_NAME_RIGHT_COMPONENT);
            if( propertyName.equals(SpatialSemantics.DefaultNames.ALONGX_PROPERTY_NAME))
                return ontoRef.getOWLObjectProperty(DEFAULT_NAME_ALONGX_COMPONENT);
            if( propertyName.equals(SpatialSemantics.DefaultNames.ALONGY_PROPERTY_NAME))
                return ontoRef.getOWLObjectProperty(DEFAULT_NAME_ALONGY_COMPONENT);
            if( propertyName.equals(SpatialSemantics.DefaultNames.ALONGZ_PROPERTY_NAME))
                return ontoRef.getOWLObjectProperty(DEFAULT_NAME_ALONGZ_COMPONENT);
            if( propertyName.equals(SpatialSemantics.DefaultNames.PARALLEL_PROPERTY_NAME))
                return ontoRef.getOWLObjectProperty(DEFAULT_NAME_PARALLEL_COMPONENT);
            if( propertyName.equals(SpatialSemantics.DefaultNames.PERPENDICULAR_PROPERTY_NAME))
                return ontoRef.getOWLObjectProperty(DEFAULT_NAME_PERPENDICULAR_COMPONENT);
            if( propertyName.equals(SpatialSemantics.DefaultNames.COAXIAL_PROPERTY_NAME))
                return ontoRef.getOWLObjectProperty(DEFAULT_NAME_COAXIAL_COMPONENT);
            logERROR( "Spatial property: " + propertyName + " not mapped in scene descriptions.");
            return null;
        }

        public OWLObjectProperty getProperty() {
            return property;
        }
        public String getPropertyName(){
            return ontoRef.getOWLObjectName( getProperty());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof MORSceneAtom)) return false;
            if (!super.equals(o)) return false;

            MORSceneAtom that = (MORSceneAtom) o;

            return this.getProperty().equals( that.getProperty()) & this.getObject().equals(that.getObject());
        }
        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + (getProperty() != null ? getProperty().hashCode() : 0);
            result = 31 * result + (getObject() != null ? getObject().hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return this.getPropertyName() + "(" + this.getTypeName() + "#" + this.getObjectName() + ")";
        }
    }
}
