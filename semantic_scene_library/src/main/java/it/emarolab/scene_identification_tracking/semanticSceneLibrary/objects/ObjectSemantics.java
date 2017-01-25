package it.emarolab.scene_identification_tracking.semanticSceneLibrary.objects;

import it.emarolab.scene_identification_tracking.semanticSceneLibrary.Semantics;

public interface ObjectSemantics {

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
     * <b>File</b>:       it.emarolab.scene_identification_tracking.semanticSceneLibrary.Semantics <br>
     * <b>Licence</b>:    GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
     * <b>Author</b>:     Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
     * <b>affiliation</b>: DIBRIS, EMAROLab, University of Genoa. <br>
     * <b>date</b>:       06/01/2017 <br>
     * </small></div>
     *
     * @param <D> the class to refer to, for manipulating the semantic structure.
     *           It must extends: {@link IndividualDescriptor}.
     *
     * @see Semantics
     * @see IndividualDescriptor
     * @see IndividualDescriptor.Array3D
     * @see IndividualDescriptor.Orientable
     * @see IndividualDescriptor.Sphere
     */
    interface Primitive<D extends Semantics.IndividualDescriptor> extends Semantics { //extends Semantics {

        /** @return the unique identifier for this object */
        long getID();

        /**
         * @return the absolute time stamp in milli-seconds
         * (see: {@link System#currentTimeMillis()})
         * of the instant in which this object has been created.
         */
        long getTime();

        /** @return the spatial (and semantic) description of the centre of mass of this Object. */
        IndividualDescriptor.Array3D getCentroid();

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
        boolean average(Primitive object);
        // todo: implement an avaraging that consider too large changes with 'this' and the input primitive

        /**
         * It should call the proper copy constructor, not super class hierarchy.
         * @return a <code>new</code> copy of this object.
         */
        Primitive<D> copy();
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
     * <b>File</b>:       it.emarolab.scene_identification_tracking.semanticSceneLibrary.Semantics <br>
     * <b>Licence</b>:    GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
     * <b>Author</b>:     Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
     * <b>affiliation</b>: DIBRIS, EMAROLab, University of Genoa. <br>
     * <b>date</b>:       06/01/2017 <br>
     * </small></div>
     *
     * @param <D> the class to refer to, for manipulating the semantic structure.
     *           It must extends: {@link IndividualDescriptor}.
     *
     * @see Semantics
     * @see IndividualDescriptor
     * @see Primitive
     */
    interface Sphere<D extends Semantics.IndividualDescriptor> extends Primitive<D> {
        /** @return the radius of the sphere */
        Double getRadius();

        /**  @param radius the radius of the sphere to set */
        void setRadius(Double radius);
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
     * <b>File</b>:       it.emarolab.scene_identification_tracking.semanticSceneLibrary.Semantics <br>
     * <b>Licence</b>:    GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
     * <b>Author</b>:     Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
     * <b>affiliation</b>: DIBRIS, EMAROLab, University of Genoa. <br>
     * <b>date</b>:       06/01/2017 <br>
     * </small></div>
     *
     * @param <D> the class to refer to, for manipulating the semantic structure.
     *           It must extends: {@link IndividualDescriptor}.
     *
     * @see Semantics
     * @see IndividualDescriptor
     * @see Primitive
     * @see IndividualDescriptor.Plane
     * @see IndividualDescriptor.Cone
     * @see IndividualDescriptor.Cylinder
     */
    interface Orientable<D extends Semantics.IndividualDescriptor> extends Primitive<D> {
        /** @return the spatial (and semantic) description of the axis direction of this Object. */
        IndividualDescriptor.Array3D getAxis();
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
     * <b>File</b>:       it.emarolab.scene_identification_tracking.semanticSceneLibrary.Semantics <br>
     * <b>Licence</b>:    GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
     * <b>Author</b>:     Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
     * <b>affiliation</b>: DIBRIS, EMAROLab, University of Genoa. <br>
     * <b>date</b>:       06/01/2017 <br>
     * </small></div>
     *
     * @param <D> the class to refer to, for manipulating the semantic structure.
     *           It must extends: {@link IndividualDescriptor}.
     *
     * @see Semantics
     * @see IndividualDescriptor
     * @see Primitive
     * @see Orientable
     * @see Plane
     */
    interface Plane<D extends Semantics.IndividualDescriptor> extends Orientable<D> {
        /** @return the hessian (<code>'d'</code>) coefficient of this plane. */
        Double getHessian();

        /**@param hessian the hessian (<code>'d'</code>) coefficient of this plane to set. */
        void setHessian(Double hessian);
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
     * <b>File</b>:       it.emarolab.scene_identification_tracking.semanticSceneLibrary.Semantics <br>
     * <b>Licence</b>:    GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
     * <b>Author</b>:     Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
     * <b>affiliation</b>: DIBRIS, EMAROLab, University of Genoa. <br>
     * <b>date</b>:       06/01/2017 <br>
     * </small></div>
     *
     * @param <D> the class to refer to, for manipulating the semantic structure.
     *           It must extends: {@link IndividualDescriptor}.
     *
     * @see Semantics
     * @see IndividualDescriptor
     * @see Primitive
     * @see Orientable
     */
    interface Cone<D extends Semantics.IndividualDescriptor> extends Orientable<D> {
        /** @return the radius of the cone. */
        Double getRadius();

        /** @param radius the radius of the cone to set. */
        void setRadius(Double radius);

        /** @return the height of the cone. */
        Double getHeight();

        /** @param height the height of the cone to set. */
        void setHeight(Double height);

        /** @return the spatial (and semantic) description of the coordinates of the apex of this Cone. */
        IndividualDescriptor.Array3D getApex();
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
     * <b>File</b>:       it.emarolab.scene_identification_tracking.semanticSceneLibrary.Semantics <br>
     * <b>Licence</b>:    GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
     * <b>Author</b>:     Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
     * <b>affiliation</b>: DIBRIS, EMAROLab, University of Genoa. <br>
     * <b>date</b>:       06/01/2017 <br>
     * </small></div>
     *
     * @param <D> the class to refer to, for manipulating the semantic structure.
     *           It must extends: {@link IndividualDescriptor}.
     *
     * @see Semantics
     * @see IndividualDescriptor
     * @see Primitive
     * @see Orientable
     */
    interface Cylinder<D extends Semantics.IndividualDescriptor> extends Orientable<D> {
        /** @return the radius of the cylinder. */
        Double getRadius();

        /** @param radius the radius of the cylinder to set. */
        void setRadius(Double radius);

        /** @return the height of the cylinder. */
        Double getHeight();

        /** @param height the height of the cylinder to set. */
        void setHeight(Double height);

        /** @return the spatial (and semantic) description of the coordinates of the apex of this Cylinder. */
        IndividualDescriptor.Array3D getPointOnAxis();
    }
}
