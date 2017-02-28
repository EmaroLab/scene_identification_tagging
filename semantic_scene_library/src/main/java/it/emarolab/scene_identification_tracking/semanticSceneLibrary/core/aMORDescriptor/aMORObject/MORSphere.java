package it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.aMORDescriptor.aMORObject;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.aMORDescriptor.MORLiteralDescriptor.*;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.semantic.GeometricSemantic;

public class MORSphere extends MORPrimitive
        implements GeometricSemantic.Sphere<MORMultiLinkLiteralTypedIndividual, MORDataProperty, MOR3DataProperty>{

    private MORDataProperty radius = null;

    public MORSphere(MORSphere copy) {
        super(copy);
        this.radius = copy.radius.copy();
    }
    public MORSphere(OWLReferences ontoRef) {
        super(ontoRef);
        this.radius = new MORDataProperty( getSemantics(), DATAPROPERTY.DEFAULT_objSPHERE_RADIUS);
    }
    public MORSphere(String ontoName) {
        super(ontoName);
        this.radius = new MORDataProperty( getSemantics(), DATAPROPERTY.DEFAULT_objSPHERE_RADIUS);
    }
    public MORSphere(String ontoName, boolean mapTime, boolean mapId) {
        super(ontoName, mapTime, mapId);
        this.radius = new MORDataProperty( getSemantics(), DATAPROPERTY.DEFAULT_objSPHERE_RADIUS);
    }
    public MORSphere(OWLReferences ontoRef, boolean mapTime, boolean mapId) {
        super(ontoRef, mapTime, mapId);
        this.radius = new MORDataProperty();
    }


    /**
     * @return the radius of the sphere
     */
    @Override @Deprecated
    public MORDataProperty getRadius() {
        return radius;
    }

    /**
     * @param radius the radius of the sphere to set
     */
    @Override@Deprecated
    public void setRadius( MORDataProperty radius) {
        if ( radius != null) {
            this.radius = radius.copy();
        }
    }


    public void setRadius( Double r){
        this.radius.setValue( getSemantics(), r);
    }
    public void setRadiusProperty( String prefix){
        this.radius.setProperty( getSemantics(), prefix);
    }


    @Override
    public String getDescribedClassName() {
        return CLASS.SPHERE;
    }

    /**
     * Merge this primitive with other descriptor of the same type
     * by averaging all the coefficients represented in this class.
     *
     * @param object the coefficients object descriptor to be
     *               averaged with this structure.
     * @return <code>false</code> if an error occurs.
     * <code>True</code> otherwise.
     */
    @Override
    public boolean average( GeometricSemantic.Primitive<MORMultiLinkLiteralTypedIndividual, MORDataProperty, MOR3DataProperty> object) {
        if( object instanceof MORSphere) {
            MORSphere that = ( MORSphere) object;
            if ( super.average( that))
                return radius.avarage( getSemantics(), radius);
            return false;
        }
        super.average( object);
        logWarning( getClass().getSigners() + " cannot average a Sphere with a: " + object.getClass().getSimpleName());
        return false;
    }

    /**
     * This method should be based on a copy constructor that takes the
     * derived class and all {@code super} copy constrcutors.<br>
     * Indeed it just: {@code return new MORSphere( this);}.
     *
     * @return a new copy of this class.
     */
    @Override
    public MORSphere copy() {
        return new MORSphere( this);
    }
}
