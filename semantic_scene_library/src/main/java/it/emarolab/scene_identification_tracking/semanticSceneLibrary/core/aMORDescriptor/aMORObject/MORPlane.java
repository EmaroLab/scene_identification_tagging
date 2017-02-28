package it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.aMORDescriptor.aMORObject;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.aMORDescriptor.MORLiteralDescriptor.*;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.semantic.GeometricSemantic;

public class MORPlane extends MOROrientable
        implements GeometricSemantic.Plane<MORMultiLinkLiteralTypedIndividual, MORDataProperty, MOR3DataProperty>{

    private MORDataProperty hessian;

    public MORPlane(MORPlane copy) {
        super(copy);
        this.hessian = copy.hessian.copy();
    }
    public MORPlane(OWLReferences ontoRef) {
        super(ontoRef);
        this.hessian = new MORDataProperty(getSemantics(), DATAPROPERTY.DEFAULT_objPLANE_HESSIAN);
    }
    public MORPlane(String ontoName) {
        super(ontoName);
        this.hessian = new MORDataProperty(getSemantics(), DATAPROPERTY.DEFAULT_objPLANE_HESSIAN);
    }
    public MORPlane(String ontoName, boolean mapTime, boolean mapId) {
        super(ontoName, mapTime, mapId);
        this.hessian = new MORDataProperty(getSemantics(), DATAPROPERTY.DEFAULT_objPLANE_HESSIAN);
    }
    public MORPlane(OWLReferences ontoRef, boolean mapTime, boolean mapId) {
        super(ontoRef, mapTime, mapId);
        this.hessian = new MORDataProperty();
    }

    /**
     * @return the hessian (<code>'d'</code>) coefficient of this plane.
     */
    @Override
    public MORDataProperty getHessian() {
        return hessian;
    }

    /**
     * @param hessian the hessian (<code>'d'</code>) coefficient of this plane to set.
     */
    @Override @Deprecated
    public void setHessian(MORDataProperty hessian) {
        if (hessian != null) {
            this.hessian = hessian.copy();
        }
    }


    public void setHessian( Double r){
        this.hessian.setValue( getSemantics(), r);
    }
    public void setHessianProperty( String prefix){
        this.hessian.setProperty( getSemantics(), prefix);
    }


    @Override
    public String getDescribedClassName() {
        return CLASS.PLANE;
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
        if( object instanceof MORPlane) {
            MORPlane that = ( MORPlane) object;
            if ( super.average( that))
                return hessian.avarage( getSemantics(), that.hessian);
            return false;
        }
        super.average( object);
        logWarning( getClass().getSigners() + " cannot average a Plane with a: " + object.getClass().getSimpleName());
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
    public MORPlane copy() {
        return new MORPlane( this);
    }
}
