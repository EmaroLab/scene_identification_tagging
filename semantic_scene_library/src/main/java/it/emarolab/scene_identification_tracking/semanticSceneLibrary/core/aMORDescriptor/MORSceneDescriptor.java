package it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.aMORDescriptor;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.semantic.SceneSemantic;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import java.util.Set;

public class MORSceneDescriptor {


    public static class MORScene implements SceneSemantic.Scene< MORSceneAtom> { // todo move away
        private Set< MORSceneAtom> atoms;
        private SceneSemantic.SceneCardinality cardinality;

        public MORScene(Set< MORSceneAtom> atoms, SceneSemantic.SceneCardinality cardinality) {
            this.atoms = atoms;
            this.cardinality = cardinality;
        }

        @Override
        public Set< MORSceneAtom> getAtoms() {
            return atoms;
        }
        @Override
        public SceneSemantic.SceneCardinality getCardinality() {
            return cardinality;
        }
    }

    public static class MORSceneAtom extends MORSpatialDescriptor.MORSpatialItem
            implements SceneSemantic.SceneAtom<OWLClass,OWLNamedIndividual,OWLObjectProperty> {

        private OWLObjectProperty property;

        public MORSceneAtom( MORSceneAtom copy){
            super( copy);
            this.property = copy.property;
        }
        public MORSceneAtom(OWLReferences ontoRef, OWLNamedIndividual object, OWLObjectProperty property, OWLClass type) {
            super(ontoRef, object, type); // you can get the type here instead of reading it
            this.property = mapSceneProperty( property);
        }
        private OWLObjectProperty mapSceneProperty( OWLObjectProperty spatialProperty){
            String propertyName = ontoRef.getOWLObjectName( spatialProperty);
            if( propertyName.equals( OBJECTPROPERTY.DEFAULT_RELATION_ABOVE))
                return ontoRef.getOWLObjectProperty( OBJECTPROPERTY.DEFAULT_SPATIAL_ABOVE);
            if( propertyName.equals( OBJECTPROPERTY.DEFAULT_RELATION_BEHIND))
                return ontoRef.getOWLObjectProperty( OBJECTPROPERTY.DEFAULT_SPATIAL_BEHIND);
            if( propertyName.equals( OBJECTPROPERTY.DEFAULT_RELATION_RIGHT))
                return ontoRef.getOWLObjectProperty( OBJECTPROPERTY.DEFAULT_SPATIAL_RIGHT);
            if( propertyName.equals( OBJECTPROPERTY.DEFAULT_RELATION_LONGX))
                return ontoRef.getOWLObjectProperty( OBJECTPROPERTY.DEFAULT_SPATIAL_LONGX);
            if( propertyName.equals( OBJECTPROPERTY.DEFAULT_RELATION_LONGY))
                return ontoRef.getOWLObjectProperty( OBJECTPROPERTY.DEFAULT_SPATIAL_LONGY);
            if( propertyName.equals( OBJECTPROPERTY.DEFAULT_RELATION_LONGZ))
                return ontoRef.getOWLObjectProperty( OBJECTPROPERTY.DEFAULT_SPATIAL_LONGZ);
            if( propertyName.equals( OBJECTPROPERTY.DEFAULT_RELATION_PARALLEL))
                return ontoRef.getOWLObjectProperty( OBJECTPROPERTY.DEFAULT_SPATIAL_PARALLEL);
            if( propertyName.equals( OBJECTPROPERTY.DEFAULT_RELATION_PERPENDICULAR))
                return ontoRef.getOWLObjectProperty( OBJECTPROPERTY.DEFAULT_SPATIAL_PERPENDICULAR);
            if( propertyName.equals( OBJECTPROPERTY.DEFAULT_RELATION_COAXIAL))
                return ontoRef.getOWLObjectProperty( OBJECTPROPERTY.DEFAULT_SPATIAL_COAXIAL);
            logWarning( "Spatial property: " + propertyName + " not mapped in scene descriptions.");
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
