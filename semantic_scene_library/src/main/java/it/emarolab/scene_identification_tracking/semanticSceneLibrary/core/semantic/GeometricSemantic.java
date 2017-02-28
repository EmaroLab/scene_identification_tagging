package it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.semantic;

import it.emarolab.scene_identification_tracking.semanticSceneLibrary.Base;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.Semantics;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.aMORDescriptor.MORSpatialDescriptor;

import java.util.Set;

/**
 * The namespace for the {@link Semantics} shape description.
 * <p>
 *     This class defines just the file that contains the class hierarchy
 *     of the shape of a real object to be semantically described.
 * <p>
 *     More in details, all the interfaces described in this file extends {@link Semantics}
 *     and each other in order to define the following hierarchy:<ul>
 *        <li> {@link Primitive}: has a center of mass.<ul>
 *            <li> {@link Sphere}: has a radius.
 *            <li> {@link Orientable}: has a principal axis direction.<ul>
 *                <li> {@link Plane}: has {@code a, b, c, d} coefficients of its geometrical representation.
 *                <li> {@link Cone}: has a radius, the coordinates of the apex and the height.
 *                <li> {@link Cylinder}: has a radius, the coordinates of a point in the axis and the height.
 *            </ul>
 *        </ul>
 *     Where, all the above interfaces extend also {@link ObjectDescriptor} to manage a
 *     unified semantic mapping between instances (that can belong to the above implementations in the ontology).
 * <p>
 *     This file contains also the basic interface for synchronise a semantic 3D array, see {@ Semantic3D}.
 *
 * <div style="text-align:center;"><small>
 * <b>File</b>:        {@link GeometricSemantic} <br>
 * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
 * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
 * <b>affiliation</b>: DIBRIS, EMAROLab, University of Genoa. <br>
 * <b>date</b>:        04/02/2017 <br>
 * </small></div>
 *
 * @see Semantics
 * @see Semantics.MappingState
 * @see Semantics.Descriptor
 * @see it.emarolab.scene_identification_tracking.semanticSceneLibrary.Base.Vocabolary
 */
public interface GeometricSemantic extends Base{

    // todo comment

    // [[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[   OBJECT DESCRIPTOR  ]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]
    /**
     * The Semantic Descriptor for mapping: types, links and literal of instances.
     * <p>
     *     This interface extends: {@link Semantics.TypedDescriptor}, {@link Semantics.LinkedDescriptor}
     *     and {@link Semantics.LiteralDescriptor} for managing the: class, object property and
     *     data property of an individual in the ontology, respectively.
     *
     * <div style="text-align:center;"><small>
     * <b>File</b>:        {@link GeometricSemantic} <br>
     * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
     * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
     * <b>affiliation</b>: DIBRIS, EMAROLab, University of Genoa. <br>
     * <b>date</b>:        04/02/2017 <br>
     * </small></div>
     *
     * @param <O> the reference to the ontology.
     * @param <I> the instance describing a semantic chunk of knowledge in the ontology.
     * @param <C> the types of the instance.
     * @param <DP> the type of property used for linking literals to {@code I}
     * @param <L> the type of literals managed by this descriptor.
     *
     * @see Semantics
     * @see Semantics.MappingState
     * @see Semantics.Descriptor
     */
    interface ObjectDescriptor<O, I, C, OP, DP, L,
                P extends SpatialSemantic.SpatialProperty<OP>,
                R extends SpatialSemantic.SpatialRelation<C,I,OP,P>,
                T extends SpatialSemantic.SpatialCollector<R>>
            extends
                Semantics.TypedDescriptor<O,I,C>,
                Semantics.LiteralDescriptor<O,I,C,DP,L>,
                SpatialSemantic.SpatialDescriptor<O,I,C,OP,P,R,T>
    {}

    // [[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[ OBJECT SHAPE HIERARCHY  ]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]
    /**
     * This interface represents the class that should describe a primitive object.
     * <p>
     * In particular, a Primitive object is semantically defined as:
     * <ul>
     *    <li><b>the instantiation time</b>,
     *    <li><b>the object unique identifier</b>,
     *    <li><b>the 3D coordinates of the center of mass</b>.
     * </ul>
     *
     * <div style="text-align:center;"><small>
     * <b>File</b>:        {@link GeometricSemantic} <br>
     * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
     * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
     * <b>affiliation</b>: DIBRIS, EMAROLab, University of Genoa. <br>
     * <b>date</b>:        04/02/2017 <br>
     * </small></div>
     *
     * @param <D> the class to refer to, for manipulating the semantic structure.
     *           It must extends: {@link ObjectDescriptor}.
     *
     * @see Semantics
     * @see GeometricSemantic
     * @see ObjectDescriptor
     */
    interface Primitive
            <D extends ObjectDescriptor, H extends Semantics.Property<?,?>, G extends Property3D<?,?>>
            extends Semantics { //extends Semantics {

        /** @return the unique identifier for this object */
        H getID();

        /**
         * @return the absolute time stamp in milli-seconds
         * (see: {@link System#currentTimeMillis()})
         * of the instant in which this object has been created.
         */
        H getTime();

        /** @return the spatial (and semantic) description of the centre of mass of this Object. */
        G getCentroid();
        void setCentroid( G centroid);

        /** @return the class to refer to, for manipulating the semantic structure. */
        D getSemantics();

        /**
         * Merge this primitive with other description of the same type
         * by averaging all the coefficients represented in this class.
         * @param object the coefficients object description to be
         *                  averaged with this structure.
         * @return <code>false</code> if an error occurs.
         * <code>True</code> otherwise.
         */
        boolean average( Primitive<D,H,G> object);


        /**
         * Reads the object characteristics from the semantic structure (map from semantic).
         *
         * @return the state of the reading process.
         */
        @Override // shared descriptor, write/read structure
        default MappingTransitions readSemantic() {
            MappingTransitions states = getSemantics().readType();
            states.addAll( getSemantics().readLiteral());

            getSemantics().querySpatialProperties(); // todo move away
            //log("££££££££££££££££££££33333 " + getSemantics().getLinksMap());

            states.addAll( getSemantics().readLink());
            return states;
        }
        /**
         * Writes the object characteristics in the semantic structure (map to semantic).
         *
         * @return the state of the writing process.
         */
        @Override
        default MappingTransitions writeSemantic() {
            MappingTransitions states = getSemantics().writeType();
            states.addAll( getSemantics().writeLiterals());
            states.addAll( getSemantics().writeLinks());
            return states;
        }
        /**
         * Delete all the references of the object characteristics from the semantic structure.
         */
        @Override
        default MappingTransitions deleteSemantic() {
            return getSemantics().deleteInstance(); // todo add debugging string
        }

        boolean isDesribed();
        boolean isPerceivable();
    }

    /**
     * This interface represents the class that should describe a spherical object.
     * <p>
     * In particular, a Sphere is an extension of a {@link Primitive}.
     * Thus, is semantically defined as:
     * <ul>
     *    <li> a {@link Primitive} describing:
     *    <ul>
     *        <li>the instantiation time,
     *        <li>the object unique identifier,
     *        <li>the 3D coordinate of the center of mass.
     *    </ul>
     *    <li> <b>The radius of the sphere</b>.
     * </ul>
     *
     * <div style="text-align:center;"><small>
     * <b>File</b>:        {@link GeometricSemantic} <br>
     * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
     * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
     * <b>affiliation</b>: DIBRIS, EMAROLab, University of Genoa. <br>
     * <b>date</b>:        04/02/2017 <br>
     * </small></div>
     *
     * @param <D> the class to refer to, for manipulating the semantic structure.
     *           It must extends: {@link ObjectDescriptor}.
     *
     * @see Semantics
     * @see GeometricSemantic
     * @see ObjectDescriptor
     * @see Semantics.Property
     */
    interface Sphere
            <D extends ObjectDescriptor, H extends Semantics.Property<?,?>, G extends Property3D<?,?>>
            extends Primitive<D,H,G> {
        /** @return the radius of the sphere */
        H getRadius();

        /**  @param radius the radius of the sphere to set */
        void setRadius( H radius);
    }

    /**
     * This interface represents the class that should describe an orientable object.
     * <p>
     * In particular, an Orientable object is an extension of a {@link Primitive}.
     * Thus, is semantically defined as:
     * <ul>
     *    <li> a {@link Primitive} describing:
     *    <ul>
     *        <li>the instantiation time,
     *        <li>the object unique identifier,
     *        <li>the 3D coordinate of the center of mass.
     *    </ul>
     *    <li> <b>The axis direction</b>.
     * </ul>
     *
     * <div style="text-align:center;"><small>
     * <b>File</b>:        {@link GeometricSemantic} <br>
     * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
     * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
     * <b>affiliation</b>: DIBRIS, EMAROLab, University of Genoa. <br>
     * <b>date</b>:        04/02/2017 <br>
     * </small></div>
     *
     * @param <D> the class to refer to, for manipulating the semantic structure.
     *           It must extends: {@link ObjectDescriptor}.
     *
     * @see Semantics
     * @see GeometricSemantic
     * @see ObjectDescriptor
     * @see Semantics.Property
     */
    interface Orientable
            <D extends ObjectDescriptor, H extends Semantics.Property<?,?>, G extends Property3D<?,?>>
            extends Primitive<D,H,G> {
        /** @return the spatial (and semantic) description of the axis direction of this Object. */
        G getAxis();

        void setAxis( G axis);
    }

    /**
     * This interface represents the class that should describe a planar object.
     * <p>
     * In particular, a Plane is an extension of an {@link Orientable} object.
     * Thus, is semantically defined as:
     * <ul>
     *    <li> a {@link Primitive} describing:
     *    <ul>
     *        <li>the instantiation time,
     *        <li>the object unique identifier,
     *        <li>the 3D coordinate of the center of mass.
     *    </ul>
     *    <li> an {@link Orientable} describing:
     *    <ul>
     *        <li>The axis direction.
     *    </ul>
     *    <li> <b>The hessian coefficient</b>.
     * </ul>
     *
     * <p>
     * Where the hessian coefficient is <code>'d'</code> such that:
     * <code>ax + by + cd + d = 0</code>.<br>
     * While the <code>'a'</code>, <code>'b'</code>
     * and <code>'c'</code> coefficients are described by the components of
     * the direction of the plane normal (see {@link #getAxis()}).
     *
     * <div style="text-align:center;"><small>
     * <b>File</b>:        {@link GeometricSemantic} <br>
     * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
     * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
     * <b>affiliation</b>: DIBRIS, EMAROLab, University of Genoa. <br>
     * <b>date</b>:        04/02/2017 <br>
     * </small></div>
     *
     * @param <D> the class to refer to, for manipulating the semantic structure.
     *           It must extends: {@link ObjectDescriptor}.
     *
     * @see Semantics
     * @see GeometricSemantic
     * @see ObjectDescriptor
     * @see Semantics.Property
     */
    interface Plane
            <D extends ObjectDescriptor, H extends Semantics.Property<?,?>, G extends Property3D<?,?>>
            extends Orientable<D,H,G> {
        /** @return the hessian (<code>'d'</code>) coefficient of this plane. */
        H getHessian();

        /**@param hessian the hessian (<code>'d'</code>) coefficient of this plane to set. */
        void setHessian(H hessian);
    }

    /**
     * This interface represents the class that should describe a Cone like object.
     * <p>
     * In particular, a Cone is an extension of an {@link Orientable} object.
     * Thus, is semantically defined as:
     * <ul>
     *    <li> a {@link Primitive} describing:
     *    <ul>
     *        <li>the instantiation time,
     *        <li>the object unique identifier,
     *        <li>the 3D coordinates of the center of mass.
     *    </ul>
     *    <li> an {@link Orientable} describing:
     *    <ul>
     *        <li>The axis direction.
     *    </ul>
     *    <li> <b>The height of the cone</b>,
     *    <li> <b>The radius of the cone</b>,
     *    <li> <b>The 3D coordinates of the cone apex</b>.
     * </ul>
     *
     * <div style="text-align:center;"><small>
     * <b>File</b>:        {@link GeometricSemantic} <br>
     * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
     * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
     * <b>affiliation</b>: DIBRIS, EMAROLab, University of Genoa. <br>
     * <b>date</b>:        04/02/2017 <br>
     * </small></div>
     *
     * @param <D> the class to refer to, for manipulating the semantic structure.
     *           It must extends: {@link ObjectDescriptor}.
     *
     * @see Semantics
     * @see GeometricSemantic
     * @see ObjectDescriptor
     * @see Semantics.Property
     */
    interface Cone
            <D extends ObjectDescriptor, H extends Semantics.Property<?,?>, G extends Property3D<?,?>>
            extends Orientable<D,H,G> {
        /** @return the radius of the cone. */
        H getRadius();

        /** @param radius the radius of the cone to set. */
        void setRadius(H radius);

        /** @return the height of the cone. */
        H getHeight();

        /** @param height the height of the cone to set. */
        void setHeight(H height);

        /** @return the spatial (and semantic) description of the coordinates of the apex of this Cone. */
        G getApex();
        void setApex(G pointOnAxis);
    }

    /**
     * This interface represents the class that should describe a Cylinder like object.
     * <p>
     * In particular, a Cylinder is an extension of an {@link Orientable} object.
     * Thus, is semantically defined as:<ul>
     *    <li> a {@link Primitive} describing:
     *    <ul>
     *        <li>the instantiation time,
     *        <li>the object unique identifier,
     *        <li>the 3D coordinates of the center of mass.
     *    </ul>
     *    <li> an {@link Orientable} describing:
     *    <ul>
     *        <li>The axis direction.
     *    </ul>
     *    <li> <b>The height of the cone</b>,
     *    <li> <b>The radius of the cone</b>,
     *    <li> <b>The 3D coordinates of a point belonging to the object axis ({@link #getAxis()})</b>.
     * </ul>
     *
     * <div style="text-align:center;"><small>
     * <b>File</b>:        {@link GeometricSemantic} <br>
     * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
     * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
     * <b>affiliation</b>: DIBRIS, EMAROLab, University of Genoa. <br>
     * <b>date</b>:        04/02/2017 <br>
     * </small></div>
     *
     * @param <D> the class to refer to, for manipulating the semantic structure.
     *           It must extends: {@link ObjectDescriptor}.
     *
     * @see Semantics
     * @see GeometricSemantic
     * @see ObjectDescriptor
     * @see Semantics.Property
     */
    interface Cylinder
            <D extends ObjectDescriptor, H extends Semantics.Property<?,?>, G extends Property3D<?,?>>
            extends Orientable<D,H,G> {
        /** @return the radius of the cylinder. */
        H getRadius();

        /** @param radius the radius of the cylinder to set. */
        void setRadius(H radius);

        /** @return the height of the cylinder. */
        H getHeight();

        /** @param height the height of the cylinder to set. */
        void setHeight(H height);

        /** @return the spatial (and semantic) description of the coordinates of the apex of this Cylinder. */
        G getPointOnAxis();
        void setPointOnAxis( G pointOnAxis);
    }

    // [[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[        ARRAY 3D      ]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]

    interface Property3D<P,L> extends Base{

        Semantics.Property<P,L> getX();
        Semantics.Property<P,L> getY();
        Semantics.Property<P,L> getZ();

        default void setXYZ( Semantics.Descriptor<?,?> descriptor, L x, L y, L z){
            getX().setValue( descriptor, x);
            getY().setValue( descriptor, y);
            getZ().setValue( descriptor, z);
        }

        // todo use them on constructor
        default void setProperty( Semantics.Descriptor<?,?> descriptor, String prefix){
            getX().setProperty( descriptor, prefix + DATAPROPERTY.DEFAULT_3dPROPERTY_xSUFFIX);
            getY().setProperty( descriptor, prefix + DATAPROPERTY.DEFAULT_3dPROPERTY_ySUFFIX);
            getZ().setProperty( descriptor, prefix + DATAPROPERTY.DEFAULT_3dPROPERTY_zSUFFIX);
        }
        default void setProperty( Semantics.Descriptor<?,?> descriptor, String prefix, String xSuf, String ySuf, String zSuf){
            getX().setProperty( descriptor, prefix + xSuf);
            getY().setProperty( descriptor, prefix + ySuf);
            getZ().setProperty( descriptor, prefix + zSuf);
        }
        default void setProperty( Semantics.Descriptor<?,?> descriptor, String xProperty, String yProperty, String zProperty){
            getX().setProperty( descriptor, xProperty);
            getY().setProperty( descriptor, yProperty);
            getZ().setProperty( descriptor, zProperty);
        }
    }
}
