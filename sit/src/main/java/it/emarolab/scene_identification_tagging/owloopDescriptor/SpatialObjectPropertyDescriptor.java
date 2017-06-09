package it.emarolab.scene_identification_tagging.owloopDescriptor;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.owloop.aMORDescriptor.MORAxioms;
import it.emarolab.owloop.aMORDescriptor.MORObjectProperty;
import it.emarolab.owloop.aMORDescriptor.utility.MORObjectPropertyBase;
import it.emarolab.owloop.aMORDescriptor.utility.objectProperty.MORFullObjectProperty;
import it.emarolab.scene_identification_tagging.SITBase;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import java.awt.font.ImageGraphicAttribute;
import java.util.ArrayList;
import java.util.List;


/**
 * The  <a href="https://github.com/EmaroLab/owloop">OWLOOP</a> {@code Descriptor} for an object property representing a spatial relation.
 * <p>
 *     This is an OWLDataProperty {@code Descriptor} based on the
 *     <a href="https://github.com/EmaroLab/owloop">OWLOOP</a> API.
 *     Since the system needs anyway to hard code the name of the
 *     spatial relation in the {@link OBJECT_PROPERTY} interface,
 *     this {@link Descriptor} does not really synchronise any semantics
 *     since working with string is much faster.
 *
 * <div style="text-align:center;"><small>
 * <b>File</b>:        it.emarolab.scene_identification_tagging.owloopDescriptor.SpatialObjectPropertyDescriptor <br>
 * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
 * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
 * <b>affiliation</b>: EMAROLab, DIBRIS, University of Genoa. <br>
 * <b>date</b>:        05/06/17 <br>
 * </small></div>
 */
public class SpatialObjectPropertyDescriptor
        extends MORObjectPropertyBase
        implements MORObjectProperty.Inverse<SpatialObjectPropertyDescriptor>,
        SITBase{

    private MORAxioms.ObjectLinks inverseProperties = new MORAxioms.ObjectLinks();

    /**
     * Initialise this OWLOOP {@code Descriptor} by fully specifying the {@code Ground}.
     * @param instance the OWL object property managed by this {@code Descriptor}.
     * @param onto the {@link OWLReferences} to an ontology hosting the described object property.
     */
    public SpatialObjectPropertyDescriptor(OWLObjectProperty instance, OWLReferences onto) {
        super(instance, onto);
        this.getInverseObjectProperty().setSingleton( true);
    }

    /**
     * This method does not do nothing. Use {@link #getSpatialInverse()}
     * and {@link #isSpatialSymmetric()}. Those methods are much
     * faster and we have to add the name of the spatial relation anyway.
     * @return always an empty list.
     */
    @Override @Deprecated
    public List<MappingIntent> readSemantic() {
        return new ArrayList<>();
    }

    /**
     * This method does not do nothing. Use {@link #getSpatialInverse()}
     * and {@link #isSpatialSymmetric()}. Those methods are much
     * faster and we have to add the name of the spatial relation anyway.
     * @return always an empty list.
     */
    @Override @Deprecated
    public List<MappingIntent> writeSemantic() {
        return new ArrayList<>();
    }

    /**
     * This is a standard OWLOOP implementation. Due to the deprecation
     * of this {@link #readSemantic()} and {@link #writeSemantic()} the buildings
     * contains only the {@link Ground} of the property.
     * @return a new {@code Descriptor} for an inverse object property to {@code this}.
     */
    @Override
    public SpatialObjectPropertyDescriptor getNewInverseObjectProperty(OWLObjectProperty instance, OWLReferences ontology) {
        return new SpatialObjectPropertyDescriptor( instance, ontology);
        // not called, with static string is faster.
        // we need to add them anyway :(
    }

    /**
     * This is a standard OWLOOP implementation.
     * @return the name of all inverse properties of {@code this}. By construction of the ontology
     * there is a singleton.
     */
    @Override
    public MORAxioms.ObjectLinks getInverseObjectProperty() {
        return inverseProperties;
    }

    @Override
    public String toString() {
        return "MORFullObjectProperty{" +
                NL + "\t\t\t" + getGround() +
                "," + NL + "\t↔ " + inverseProperties +
                NL + "}";
    }

    /**
     * It returns if this property is symmetric or not by looking
     * in the constants coded into the {@link SITBase.OBJECT_PROPERTY}.
     * @return {@code true} if this relation is symmetric false otherwise.
     */
    public boolean isSpatialSymmetric() {
        if ( getInstanceName().equals( OBJECT_PROPERTY.SPATIAL_PARALLEL))
            return true;
        if ( getInstanceName().equals( OBJECT_PROPERTY.SPATIAL_PERPENDICULAR))
            return true;
        if ( getInstanceName().equals( OBJECT_PROPERTY.SPATIAL_COAXIAL))
            return true;
        return false;
    }

    /**
     * Returns the name of the inverse property by checking
     * in the constants coded into the {@link SITBase.OBJECT_PROPERTY}.
     * @return the name of the inverse property. {@code null} if it is not known.
     */
    public String getSpatialInverse(){
        if ( getInstanceName().equals( OBJECT_PROPERTY.SPATIAL_PARALLEL))
            return OBJECT_PROPERTY.SPATIAL_PARALLEL;
        if ( getInstanceName().equals( OBJECT_PROPERTY.SPATIAL_PERPENDICULAR))
            return OBJECT_PROPERTY.SPATIAL_PERPENDICULAR;
        if ( getInstanceName().equals( OBJECT_PROPERTY.SPATIAL_COAXIAL))
            return OBJECT_PROPERTY.SPATIAL_COAXIAL;
        if ( getInstanceName().equals( OBJECT_PROPERTY.SPATIAL_ALONGX))
            return OBJECT_PROPERTY.SPATIAL_ALONGX;
        if ( getInstanceName().equals( OBJECT_PROPERTY.SPATIAL_ALONGY))
            return OBJECT_PROPERTY.SPATIAL_ALONGY;
        if ( getInstanceName().equals( OBJECT_PROPERTY.SPATIAL_ALONGZ))
            return OBJECT_PROPERTY.SPATIAL_ALONGZ;

        if ( getInstanceName().equals( OBJECT_PROPERTY.SPATIAL_ABOVE))
            return OBJECT_PROPERTY.SPATIAL_FRONT;
        if ( getInstanceName().equals( OBJECT_PROPERTY.SPATIAL_BEHIND))
            return OBJECT_PROPERTY.SPATIAL_BELOW;
        if ( getInstanceName().equals( OBJECT_PROPERTY.SPATIAL_RIGHT))
            return OBJECT_PROPERTY.SPATIAL_LEFT;

        System.err.println( "Inverse property of " + getInstanceName() + " not known!!");
        return "";
    }

    /**
     * Returns the inverse property by checking
     * in the constants coded into the {@link SITBase.OBJECT_PROPERTY}.
     * @return the inverse property. {@code null} if it is not known.
     */
    public OWLObjectProperty getSpatialInverseProperty(){
        if (getSpatialInverse() == null)
            return null;
        return getOntology().getOWLObjectProperty( getSpatialInverse());
    }


    /**
     * Return the name of {@link #getInstance()}.
     * @return the name of this object property.
     */
    public String getInstanceName() {
        return getInstance().getIRI().getRemainder().get();
    }
}
