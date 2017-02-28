package it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.aMORDescriptor.aMORObject;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.aMORDescriptor.MORLiteralDescriptor.*;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.semantic.GeometricSemantic;

public class MOROrientable extends MORPrimitive
        implements GeometricSemantic.Orientable<MORMultiLinkLiteralTypedIndividual, MORDataProperty, MOR3DataProperty>{

    private MOR3DataProperty axis;

    public MOROrientable(MOROrientable copy) {
        super(copy);
        this.axis = copy.axis.copy();
    }
    public MOROrientable(OWLReferences ontoRef) {
        super(ontoRef);
        this.axis = new MOR3DataProperty(getSemantics(), DATAPROPERTY.DEFAULT_objORIENTABLE_AXIS);
    }
    public MOROrientable(String ontoName) {
        super(ontoName);
        this.axis = new MOR3DataProperty(getSemantics(), DATAPROPERTY.DEFAULT_objORIENTABLE_AXIS);
    }
    public MOROrientable(String ontoName, boolean mapTime, boolean mapId) {
        super(ontoName, mapTime, mapId);
        this.axis = new MOR3DataProperty(getSemantics(), DATAPROPERTY.DEFAULT_objORIENTABLE_AXIS);
    }
    public MOROrientable(OWLReferences ontoRef, boolean mapTime, boolean mapId) {
        super(ontoRef, mapTime, mapId);
        this.axis = new MOR3DataProperty();
    }


    /**
     * @return the spatial (and semantic) description of the axis direction of this Object.
     */
    @Override @Deprecated
    public MOR3DataProperty getAxis() {
        return axis;
    }

    @Override @Deprecated
    public void setAxis(MOR3DataProperty axis) {
        if ( axis != null) {
            this.axis = axis.copy();
        }
    }

    public void setAxis(MOROrientable s) {
        this.axis.setXYZ( getSemantics(), s.axis.getX().getValue(), s.axis.getY().getValue(), s.axis.getZ().getValue());
    }
    public void setAxis( Double x, Double y, Double z){
        this.axis.setXYZ( getSemantics(), x, y, z);
    }
    public void setAxisProperty( String prefix){
        this.axis.setProperty( getSemantics(), prefix);
    }
    public void setAxisProperty( String prefix, String xSuffix, String ySuffix, String zSuffix){
        this.axis.setProperty( getSemantics(), prefix, xSuffix, ySuffix, zSuffix);
    }



    @Override
    public String getDescribedClassName() {
        return CLASS.ORIENTBLE;
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
        if( object instanceof MOROrientable) {
            MOROrientable that = ( MOROrientable) object;
            if ( super.average( that))
                return axis.average( getSemantics(), that.axis);
            return false;
        }
        super.average( object);
        logWarning( getClass().getSigners() + " cannot average an Orientable with a: " + object.getClass().getSimpleName());
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
    public MOROrientable copy() {
        return new MOROrientable( this);
    }

}
