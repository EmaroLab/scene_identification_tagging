package it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.aMORDescriptor.aMORObject;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.aMORDescriptor.MORLiteralDescriptor.*;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.semantic.GeometricSemantic;


public class MORCone extends MOROrientable
        implements GeometricSemantic.Cone<MORMultiLinkLiteralTypedIndividual, MORDataProperty, MOR3DataProperty>{

    private MORDataProperty radius, height;
    private MOR3DataProperty apex;

    public MORCone(MORCone copy) {
        super(copy);
        this.radius = copy.radius.copy();
        this.height = copy.height.copy();
        this.apex = copy.apex.copy();
    }

    public MORCone(OWLReferences ontoRef) {
        super(ontoRef);
        this.radius = new MORDataProperty(getSemantics(), DATAPROPERTY.DEFAULT_objCONE_RADIUS);
        this.height = new MORDataProperty( getSemantics(), DATAPROPERTY.DEFAULT_objCONE_HEIGHT);
        this.apex = new MOR3DataProperty( getSemantics(), DATAPROPERTY.DEFAULT_objCONE_APEX);
    }
    public MORCone(String ontoName) {
        super(ontoName);
        this.radius = new MORDataProperty(getSemantics(), DATAPROPERTY.DEFAULT_objCONE_RADIUS);
        this.height = new MORDataProperty( getSemantics(), DATAPROPERTY.DEFAULT_objCONE_HEIGHT);
        this.apex = new MOR3DataProperty( getSemantics(), DATAPROPERTY.DEFAULT_objCONE_APEX);
    }
    public MORCone(String ontoName, boolean mapTime, boolean mapId) {
        super(ontoName, mapTime, mapId);
        this.radius = new MORDataProperty(getSemantics(), DATAPROPERTY.DEFAULT_objCONE_RADIUS);
        this.height = new MORDataProperty( getSemantics(), DATAPROPERTY.DEFAULT_objCONE_HEIGHT);
        this.apex = new MOR3DataProperty( getSemantics(), DATAPROPERTY.DEFAULT_objCONE_APEX);
    }
    public MORCone(OWLReferences ontoRef, boolean mapTime, boolean mapId) {
        super(ontoRef, mapTime, mapId);
        this.radius = new MORDataProperty();
        this.height = new MORDataProperty();
        this.apex = new MOR3DataProperty();
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
     * @return the spatial (and semantic) description of the coordinates of the apex of this Cone.
     */
    public MOR3DataProperty getApex() {
        return apex;
    }
    @Deprecated
    public void setApex(MOR3DataProperty apex) {
        if ( apex != null) {
            this.apex = apex.copy();
        }
    }


    public void setApex(MORCone s) {
        this.apex.setXYZ( getSemantics(), s.apex.getX().getValue(), s.apex.getY().getValue(), s.apex.getZ().getValue());
    }
    public void setApex( Double x, Double y, Double z){
        this.apex.setXYZ( getSemantics(), x, y, z);
    }
    public void setApexProperty( String prefix){
        this.apex.setProperty( getSemantics(), prefix);
    }
    public void setApexProperty( String prefix, String xSuffix, String ySuffix, String zSuffix){
        this.apex.setProperty( getSemantics(), prefix, xSuffix, ySuffix, zSuffix);
    }

    @Override
    public String getDescribedClassName() {
        return CLASS.CONE;
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
        if( object instanceof MORCone) {
            MORCone that = ( MORCone) object;
            if ( super.average( that))
                return apex.average( getSemantics(), that.apex)
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
    public MORCone copy() {
        return new MORCone( this);
    }

}
