package it.emarolab.scene_identification_tracking.semanticSceneLibrary.aMORDescriptor;

import it.emarolab.scene_identification_tracking.semanticSceneLibrary.Semantics;

import it.emarolab.scene_identification_tracking.semanticSceneLibrary.objects.ObjectSemantics;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLLiteral;

/**
 * A semantically mappable {@link Semantics.IndividualDescriptor.Array3D} based on aMOR library.
 * <p>
 * It describes the semantic property of each components of the array as an
 * {@link OWLDataProperty}, referee by name. Thus, the <code>'T'</code>
 * parameter of the {@link Semantics.IndividualDescriptor.SemanticMap.Resources}
 * interface is set to: {@link String}. On the other hand the <code>'D'</code>
 * parameter is set to {@link MORSpatialDescriptor.MORSimpleDescriptor}, the object that will
 * map a property name, given as a <code>String</code>, to an <code>OWLDataProperty</code>.
 * <p>
 * Also, it implements the
 * {@link Semantics.IndividualDescriptor.SemanticMap}
 * interface by defining how a general 3D array should be semantically map in an ontology.
 * <p>
 *     <b>REMARK</b>: see <a href="https://github.com/EmaroLab/multi_ontology_reference">here</a>
 *     for more about the aMOR library.
 *
 *
 * <div style="text-align:center;"><small>
 * <b>File</b>:       it.emarolab.scene_identification_tracking.semanticSceneLibrary.Semantics <br>
 * <b>Licence</b>:    GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
 * <b>Author</b>:     Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
 * <b>affiliation</b>: DIBRIS, EMAROLab, University of Genoa. <br>
 * <b>date</b>:       06/01/2017 <br>...
 * </small></div>
 *
 * @see Semantics.IndividualDescriptor.Array3D
 * @see Semantics.IndividualDescriptor.SemanticMap.Resources
 * @see ObjectSemantics.Primitive
 * @see ObjectSemantics.Orientable
 * @see ObjectSemantics.Cone
 * @see ObjectSemantics.Cylinder
 */
public class MOR3DArray extends Semantics.IndividualDescriptor.Array3D
        implements Semantics.IndividualDescriptor.SemanticMap.Resources<MORSpatialDescriptor.MORSimpleDescriptor, String> {

    /**
     * The default value for the {@link OWLLiteral} used when,
     * {@link #getX()}, {@link #getY()} or {@link #getZ()} did not
     * pass the {@link #testGetter(Double)}.
     */
    private static final double NULL_LITERAL_VALUE = 0;

    // private fields (to be mapped together)
    private String xPropertyName = null, yPropertyName = null, zPropertyName = null;
    private OWLDataProperty xProperty, yProperty, zProperty;

    /**
     * Cloning constructor, create a new object as a clone of the input parameter.
     * @param array the object to clone.
     */
    public MOR3DArray( MOR3DArray array){
        super( array);
        this.xProperty = array.xProperty;
        this.yProperty = array.yProperty;
        this.zPropertyName = array.zPropertyName;
        this.xPropertyName = array.xPropertyName;
        this.yPropertyName = array.yPropertyName;
        this.zPropertyName = array.zPropertyName;
    }
    /** Construct by calling super: {@link Semantics.IndividualDescriptor.Array3D#Array3D()}. */
    public MOR3DArray() {
        super();
    }
    /**
     * Construct by calling super: {@link Semantics.IndividualDescriptor.Array3D#Array3D( String)}.
     * @param description a comment used in {@link #toString()} method for debugging purposes.
     */
    public MOR3DArray(String description) {
        super(description);
    }
    /**
     * Construct by calling super: {@link Semantics.IndividualDescriptor.Array3D#Array3D(double,double,double)}.
     * @param x the value of the X component.
     * @param y the value of the Y component.
     * @param z the value of the Z component.
     */
    public MOR3DArray(double x, double y, double z) {
        super(x, y, z);
    }
    /**
     * Construct by calling super: {@link Semantics.IndividualDescriptor.Array3D#Array3D(double, double, double, String)}.
     * @param x the value of the X component.
     * @param y the value of the Y component.
     * @param z the value of the Z component.
     * @param description a comment used in {@link #toString()} for debugging purposes.
     */
    public MOR3DArray(double x, double y, double z, String description) {
        super(x, y, z, description);
    }

    /**
     * Calls {@link #MOR3DArray(MOR3DArray)}
     * @return a <code>new</code> copy of this object.
     */
    @Override
    public Semantics.IndividualDescriptor.Array3D copy() {
        return new MOR3DArray( this);
    }

    /** @return the semantic property of the X component of this array. */
    public OWLDataProperty getXproperty() {
        return xProperty;
    }
    /** @return the semantic property of the Y component of this array. */
    public OWLDataProperty getYproperty() {
        return yProperty;
    }
    /** @return the semantic property of the Z component of this array. */
    public OWLDataProperty getZproperty() {
        return zProperty;
    }

    /** @return the name of the data property that semantically describe the X component of the array.*/
    public String getXpropertyName() {
        return xPropertyName;
    }
    /** @return the name of the data property that semantically describe the Y component of the array.*/
    public String getYpropertyName() {
        return yPropertyName;
    }
    /** @return the name of the data property that semantically describe the Z component of the array.*/
    public String getZpropertyName() {
        return zPropertyName;
    }

    /**
     * If the property name and the {@link MORSpatialDescriptor.MORSimpleDescriptor#getOntology()}, related to the
     * input parameter (<code>semantic</code>), is not null than, sets the {@link #xPropertyName}
     * and retrieve its semantic {@link OWLDataProperty} representation: {@link #xProperty}.
     * @param xPropertyName the name of the semantic property that describes the X component.
     * @param semantic the aMOR instance referring to the considering ontology.
     * @see Semantics.IndividualDescriptor.SemanticMap.Resources#setXproperty(Object, Semantics.IndividualDescriptor.DataDescriptor)
     */
    @Override
    public void setXproperty(String xPropertyName, MORSpatialDescriptor.MORSimpleDescriptor semantic) {
        if( xPropertyName != null & semantic != null )
            if( semantic.getOntology() != null) {
                this.xPropertyName = xPropertyName;
                this.xProperty = semantic.getOntology().getOWLDataProperty( xPropertyName);
            } else showPropertyNameSettingError();
        else showPropertyNameSettingError();
    }
    /**
     * If the property name and the {@link MORSpatialDescriptor.MORSimpleDescriptor#getOntology()}, related to the
     * input parameter (<code>semantic</code>), is not null than, sets the {@link #yPropertyName}
     * and retrieve its semantic {@link OWLDataProperty} representation: {@link #yProperty}.
     * @param yPropertyName the name of the semantic property that describes the Y component.
     * @param semantic the aMOR instance referring to the considering ontology.
     * @see Semantics.IndividualDescriptor.SemanticMap.Resources#setYproperty(Object, Semantics.IndividualDescriptor.DataDescriptor)
     */
    @Override
    public void setYproperty(String yPropertyName, MORSpatialDescriptor.MORSimpleDescriptor semantic) {
        if( yPropertyName != null & semantic != null)
            if ( semantic.getOntology() != null) {
                this.yPropertyName = yPropertyName;
                this.yProperty = semantic.getOntology().getOWLDataProperty( yPropertyName);
            } else showPropertyNameSettingError();
        else showPropertyNameSettingError();
    }
    /**
     * If the property name and the {@link MORSpatialDescriptor.MORSimpleDescriptor#getOntology()}, related to the
     * input parameter (<code>semantic</code>), is not null than, sets the {@link #zPropertyName}
     * and retrieve its semantic {@link OWLDataProperty} representation: {@link #zProperty}.
     * @param zPropertyName the name of the semantic property that describes the Z component.
     * @param semantic the aMOR instance referring to the considering ontology.
     * @see Resources#setZproperty(Object, Semantics.IndividualDescriptor.DataDescriptor)
     */
    @Override
    public void setZproperty(String zPropertyName, MORSpatialDescriptor.MORSimpleDescriptor semantic) {
        if( zPropertyName != null & semantic != null)
            if( semantic.getOntology() != null) {
                this.zPropertyName = zPropertyName;
                this.zProperty = semantic.getOntology().getOWLDataProperty( zPropertyName);
            } else showPropertyNameSettingError();
        else showPropertyNameSettingError();
    }
    // just called by set{X,Y,Z}property
    private void showPropertyNameSettingError(){
        logError( "set{X,Y,Z}propertyName(..): property name cannot be null and the semantic descriptor should be fully initialised");
    }
    /**
     * Helper for calling {@link #setXYZproperty(String, String, String, MORSpatialDescriptor.MORSimpleDescriptor)},
     * {@link #setYproperty(String, MORSpatialDescriptor.MORSimpleDescriptor)}
     * and {@link #setXYZproperty(String, String, String, MORSpatialDescriptor.MORSimpleDescriptor)}
     * at once.
     * @param xPropertyName the name of the semantic property of the X component to set.
     * @param yPropertyName the name of the semantic property of the Y component to set.
     * @param zPropertyName the name of the semantic property of the Z component to set.
     * @param semantic the aMOR references of the ontology in which this 3D array should be mapped.
     */
    public void setXYZproperty(String xPropertyName, String yPropertyName, String zPropertyName, MORSpatialDescriptor.MORSimpleDescriptor semantic) {
        setXproperty( xPropertyName, semantic);
        setYproperty( yPropertyName, semantic);
        setZproperty( zPropertyName, semantic);
    }

    /**
     * @return true if the name of the X property has been set. In details:<br>
     *     <code>return xPropertyName != null;</code>
     * @see Resources#hasXproperty()
     */
    @Override
    public boolean hasXproperty(){
        return xPropertyName != null;
    }
    /**
     * @return true if the name of the Y property has been set. In details:<br>
     *     <code>return yPropertyName != null;</code>
     * @see Resources#hasYproperty()
     */
    @Override
    public boolean hasYproperty(){
        return yPropertyName != null;
    }
    /**
     * @return true if the name of the Z property has been set. In details:<br>
     *     <code>return zPropertyName != null;</code>
     * @see Resources#hasZproperty()
     */
    @Override
    public boolean hasZproperty(){
        return zPropertyName != null;
    }
    /**
     * Helper for calling {@link #hasXproperty()}, {@link #hasYproperty()}
     * and {@link #hasZproperty()} at once. It returns the logical <code>AND</code>
     * operation between those three boolean values.
     * @return <code>true</code> if this has X, Y and Z properties set.
     * <code>False</code> otherwise.
     */
    public boolean hasXYXpropertyName(){
        return hasXproperty() & hasYproperty() & hasZproperty();
    }

    /**
     * Use aMOR library to get the OWL literal representation of the X components of the array.
     * It {@link #testGetter(Double)} for {@link #getX()} parameter, if it is <code>false</code>
     * the {@link #NULL_LITERAL_VALUE} will be returned.
     * @param semantic the aMOR instance referring to the considering ontology.
     * @return the OWL literal of {@link #getX()} or {@link #NULL_LITERAL_VALUE},
     *         if the first is is not set (e.g.: <code>null</code>).
     */
    public OWLLiteral getXliteral( MORSpatialDescriptor.MORSimpleDescriptor semantic){
        if( testGetter( getX()))
            return semantic.getOntology().getOWLLiteral( getX());
        return getNullLiteral( semantic);
    }
    /**
     * Use aMOR library to get the OWL literal representation of the Y components of the array.
     * It {@link #testGetter(Double)} for {@link #getY()} parameter, if it is <code>false</code>
     * the {@link #NULL_LITERAL_VALUE} will be returned.
     * @param semantic the aMOR instance referring to the considering ontology.
     * @return the OWL literal of {@link #getY()} or {@link #NULL_LITERAL_VALUE},
     *         if the first is is not set (e.g.: <code>null</code>).
     */
    public OWLLiteral getYliteral( MORSpatialDescriptor.MORSimpleDescriptor semantic){
        if( testGetter( getY()))
            return semantic.getOntology().getOWLLiteral( getY());
        return getNullLiteral( semantic);
    }
    /**
     * Use aMOR library to get the OWL literal representation of the Z components of the array.
     * It {@link #testGetter(Double)} for {@link #getY()} parameter, if it is <code>false</code>
     * the {@link #NULL_LITERAL_VALUE} will be returned.
     * @param semantic the aMOR instance referring to the considering ontology.
     * @return the OWL literal of {@link #getZ()} or {@link #NULL_LITERAL_VALUE},
     *         if the first is is not set (e.g.: <code>null</code>).
     */
    public OWLLiteral getZliteral( MORSpatialDescriptor.MORSimpleDescriptor semantic){
        if( testGetter( getZ()))
            return semantic.getOntology().getOWLLiteral( getZ());
        return getNullLiteral( semantic);
    }

    /**
     * @param semantic containing {@link it.emarolab.amor.owlInterface.OWLReferences}
     *                 describing the literal.
     * @return a double literal equal to {@link #NULL_LITERAL_VALUE}
     */
    protected OWLLiteral getNullLiteral( MORSpatialDescriptor.MORSimpleDescriptor semantic) {
        return semantic.getOntology().getOWLLiteral( NULL_LITERAL_VALUE);
    }


    @Override
    public String toString(){
        return super.toString() + " semantic:[" + getXpropertyName() + "," + getYpropertyName() + "," + getZpropertyName() + "]";
    }

    /**
     * This method relies on {@link MORSpatialDescriptor.MORSimpleDescriptor#writeLiteral(String, OWLDataProperty, OWLLiteral)}
     * and {@link Semantics.WritingState#merge(Semantics.WritingState)}
     * for writing this 3D array on the OWL ontology, described by the input parameter,
     * and keep track of the computation state.
     * <p>
     * In particular, it sequentially add the flowing semantic entities
     * to the individual (<code>'I'</code>), described by {@link MORSpatialDescriptor.MORSimpleDescriptor}, and returns as
     * soon as a writing state results to be: <code>! {@link Semantics.WritingState#isOK()}</code>.
     * <ul>
     *    <li><code>I = hasXcomponent:x</code> ({@link #getXproperty()}, {@link #getXliteral(MORSpatialDescriptor.MORSimpleDescriptor)}),
     *    <li><code>I = hasYcomponent:y</code> ({@link #getYproperty()}, {@link #getYliteral(MORSpatialDescriptor.MORSimpleDescriptor)}),
     *    <li><code>I = hasZcomponent:z</code> ({@link #getZproperty()}, {@link #getZliteral(MORSpatialDescriptor.MORSimpleDescriptor)}),
     * </ul>
     * <p>
     * <b>REMARK</b>: so far, the {@link Semantics.MappingState#INCONSISTENT}
     * state is not managed (never returned) by this method.
     * @param semantic the object that can manipulate the ontology
     *                 and try to synchronise the java representation
     *                 with the semantic structure.
     * @return the state of the writing process undertaken.
     * @see Semantics.IndividualDescriptor.SemanticMap#writeSemantics(Semantics.IndividualDescriptor.DataDescriptor)
     */
    @Override
    public Semantics.WritingState writeSemantics(MORSpatialDescriptor.MORSimpleDescriptor semantic) {
        log( "writing semantic array: " + this + " ...");

        Semantics.WritingState r, rTmp;
        r = semantic.writeLiteral( getDebuggingDescription(), getXproperty(), getXliteral( semantic));
        if( ! r.isOK())
            return r;

        rTmp = semantic.writeLiteral( getDebuggingDescription(), getYproperty(), getYliteral( semantic));
        if( ! rTmp.isOK())
            return rTmp;
        r = r.merge( rTmp);

        rTmp = semantic.writeLiteral( getDebuggingDescription(), getZproperty(), getZliteral( semantic));
        if( ! rTmp.isOK())
            return rTmp;
        return r.merge( rTmp);
    }

    /**
     * This method relies on {@link MORSpatialDescriptor.MORSimpleDescriptor#readLiteral(String, OWLDataProperty, OWLLiteral)}
     * and {@link Semantics.ReadingState#merge(Semantics.ReadingState)}
     * for reading this 3D array from the OWL ontology, described by the input parameter,
     * and keep track of the computation state.
     * <p>
     * In particular, it sequentially reads the flowing semantic entities
     * from the individual (<code>'I'</code>), described by {@link MORSpatialDescriptor.MORSimpleDescriptor}, and returns as
     * soon as a reading state results to be: <code>! {@link Semantics.WritingState#isOK()}</code>.
     * <ul>
     *    <li><code>x = I(hasXcomponent)</code> ({@link #getXproperty()}, {@link #setX(double)}),
     *    <li><code>y = I(hasYcomponent)</code> ({@link #getYproperty()}, {@link #setY(double)}),
     *    <li><code>z = I(hasZcomponent)</code> ({@link #getZproperty()}, {@link #setZ(double)}),
     * </ul>
     * <p>
     * <b>REMARK</b>: so far, the {@link Semantics.MappingState#INCONSISTENT}
     * state is not managed (never returned) by this method.
     * @param semantic the object that can manipulate the ontology
     *                 and try to synchronise the java representation
     *                 with the semantic structure.
     * @return the state of the writing process undertaken.
     * @see Semantics.IndividualDescriptor.SemanticMap#writeSemantics(Semantics.IndividualDescriptor.DataDescriptor)
     */
    @Override
    public Semantics.ReadingState readSemantics(MORSpatialDescriptor.MORSimpleDescriptor semantic) {
        log( "reading semantic array: " + this + " ...");

        MORSpatialDescriptor.MORSimpleDescriptor.DataReadingOutcome r, rTmp;
        r = semantic.readLiteral( getDebuggingDescription(), getXproperty(), getXliteral( semantic));
        if( ! r.isOK())
            return r.getState();
        else if( r.isSuccess())
            setX( r.getDoubleReadBuffer());

        rTmp = semantic.readLiteral( getDebuggingDescription(), getYproperty(), getYliteral( semantic));
        if( ! rTmp.isOK())
            return rTmp.getState();
        else if( rTmp.isSuccess())
            setY( rTmp.getDoubleReadBuffer());
        r = r.merge( rTmp);

        rTmp = semantic.readLiteral( getDebuggingDescription(), getZproperty(), getZliteral( semantic));
        if( ! rTmp.isOK())
            return rTmp.getState();
        else if( rTmp.isSuccess())
            setZ( rTmp.getDoubleReadBuffer());
        return r.merge( rTmp).getState();
    }

}
