package it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.aMORDescriptor.aMORObject;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.aMORDescriptor.MORLiteralDescriptor.*;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.semantic.GeometricSemantic;

public class MORCylinder extends MOROrientable
        implements GeometricSemantic.Cylinder<MORMultiLinkLiteralTypedIndividual, MORDataProperty, MOR3DataProperty>{

    private MORDataProperty radius, height;
    private MOR3DataProperty pointOnAxis;


    public MORCylinder(MORCylinder copy) {
        super(copy);
        this.radius = copy.radius.copy();
        this.height = copy.height.copy();
        this.pointOnAxis = copy.pointOnAxis.copy();
    }

    public MORCylinder(OWLReferences ontoRef) {
        super(ontoRef);
        this.radius = new MORDataProperty( getSemantics(), DATAPROPERTY.DEFAULT_objCYLINDER_RADIUS);
        this.height = new MORDataProperty( getSemantics(), DATAPROPERTY.DEFAULT_objCYLINDER_HEIGHT);
        this.pointOnAxis = new MOR3DataProperty( getSemantics(), DATAPROPERTY.DEFAULT_objCYLINDER_POINT);
    }

    public MORCylinder(String ontoName) {
        super(ontoName);
        this.radius = new MORDataProperty( getSemantics(), DATAPROPERTY.DEFAULT_objCYLINDER_RADIUS);
        this.height = new MORDataProperty( getSemantics(), DATAPROPERTY.DEFAULT_objCYLINDER_HEIGHT);
        this.pointOnAxis = new MOR3DataProperty( getSemantics(), DATAPROPERTY.DEFAULT_objCYLINDER_POINT);
    }
    public MORCylinder(String ontoName, boolean mapTime, boolean mapId) {
        super(ontoName);
        this.radius = new MORDataProperty( getSemantics(), DATAPROPERTY.DEFAULT_objCYLINDER_RADIUS);
        this.height = new MORDataProperty( getSemantics(), DATAPROPERTY.DEFAULT_objCYLINDER_HEIGHT);
        this.pointOnAxis = new MOR3DataProperty( getSemantics(), DATAPROPERTY.DEFAULT_objCYLINDER_POINT);
    }
    public MORCylinder(OWLReferences ontoRef, boolean mapTime, boolean mapId) {
        super(ontoRef, mapTime, mapId);
        this.radius = new MORDataProperty();
        this.height = new MORDataProperty();
        this.pointOnAxis = new MOR3DataProperty();
    }

    /**
     * @return the radius of the cone.
     */
    @Override @Deprecated
    public MORDataProperty getRadius() {
        return radius;
    }
    /**
     * @param radius the radius of the cone to set.
     */
    @Override @Deprecated
    public void setRadius(MORDataProperty radius) {
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


    /**
     * @return the height of the cone.
     */
    @Override @Deprecated
    public MORDataProperty getHeight() {
        return height;
    }
    /**
     * @param height the height of the cone to set.
     */
    @Override @Deprecated
    public void setHeight(MORDataProperty height) {
        if ( height != null) {
            this.height = height.copy();
        }
    }


    public void setHeight( Double r){
        this.height.setValue( getSemantics(), r);
    }
    public void setHeightProperty( String prefix){
        this.height.setProperty( getSemantics(), prefix);
    }


    /**
     * @return the spatial (and semantic) description of the coordinates of the pointOnAxis of this Cone.
     */ @Deprecated
    public MOR3DataProperty getPointOnAxis() {
        return pointOnAxis;
    }
    @Deprecated
    public void setPointOnAxis(MOR3DataProperty pointOnAxis) {
        if ( pointOnAxis != null) {
            this.pointOnAxis = pointOnAxis.copy();
        }
    }


    public void setPointOnAxis(MORCylinder s) {
        this.pointOnAxis.setXYZ( getSemantics(), s.pointOnAxis.getX().getValue(), s.pointOnAxis.getY().getValue(), s.pointOnAxis.getZ().getValue());
    }
    public void setPointOnAxis( Double x, Double y, Double z){
        this.pointOnAxis.setXYZ( getSemantics(), x, y, z);
    }
    public void setPointOnAxisProperty( String prefix){
        this.pointOnAxis.setProperty( getSemantics(), prefix);
    }
    public void setPointOnAxisProperty( String prefix, String xSuffix, String ySuffix, String zSuffix){
        this.pointOnAxis.setProperty( getSemantics(), prefix, xSuffix, ySuffix, zSuffix);
    }


    @Override
    public String getDescribedClassName() {
        return CLASS.CYLINDER;
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
        if( object instanceof MORCylinder) {
            MORCylinder that = ( MORCylinder) object;
            if ( super.average( that))
                return pointOnAxis.average( getSemantics(), that.pointOnAxis)
                        & radius.avarage( getSemantics(), that.radius)
                        & height.avarage( getSemantics(), that.height);
            return false;
        }
        super.average( object);
        logWarning( getClass().getSigners() + " cannot average a Cone with a: " + object.getClass().getSimpleName());
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
    public MORCylinder copy() {
        return new MORCylinder( this);
    }

}
