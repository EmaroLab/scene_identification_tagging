package it.emarolab.scene_identification_tagging;

import org.semanticweb.owlapi.model.OWLObjectProperty;

/**
 * The base interface for the SIT architecture.
 * <p>
 *     This interface is aimed to be a container for static methods
 *     and constants, as well as logging facility.
 *     It should be implemented by all classes of this architecture
 *
 * <div style="text-align:center;"><small>
 * <b>File</b>:        it.emarolab.scene_identification_tagging.SITBase <br>
 * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
 * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
 * <b>affiliation</b>: EMAROLab, DIBRIS, University of Genoa. <br>
 * <b>date</b>:        05/06/17 <br>
 * </small></div>
 */
public interface SITBase {

    /**
     * The threshold of the recognition confidence {@code [0,1]}
     * to consider a scene different enough (from the best recognition)
     * to be learned.
     */
    double CONFIDENCE_THRESHOLD = .8;

    /**
     * The path to the main t-box ontological representation
     * used by the SIT algorithm, with respect to the {@code src} folder.
     */
    String ONTO_FILE = "../resources/t_box/empty-scene.owl";

    /**
     * The {@code IRI} domain of the main t-box ontological representation
     * used by the SIT algorithm.
     */
    String ONTO_IRI = "http://www.semanticweb.org/emaroLab/luca-buoncompagni/sit";


    /**
     * The base interface for the SIT constants.
     * <p>
     *     This interface is aimed to be a container for all the
     *     {@code String} constants used in the ontology.
     *     It is extended in order to give a clear semantic to
     *     the constants by a name space.
     *     <br>
     *     Those names are tuned with respect to the t-box
     *     ontology representation used by the SIT algorithm
     *     (available at: {@link #ONTO_FILE})
     *
     * <div style="text-align:center;"><small>
     * <b>File</b>:        it.emarolab.scene_identification_tagging.SITBase <br>
     * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
     * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
     * <b>affiliation</b>: EMAROLab, DIBRIS, University of Genoa. <br>
     * <b>date</b>:        05/06/17 <br>
     * </small></div>
     */
    interface VOCABOLARY {

        /**
         * The prefix used for data properties as well
         * as for object properties that relate a generic
         * object to the abstract {@code Scene} representation.
         */
        String PREFIX_HAS = "has-";

        /**
         * The prefix used for all the data properties used
         * to describe geometric coefficients of objects.
         */
        String PREFIX_GEOMETRIC = PREFIX_HAS + "geometric_";

        /**
         * The suffix used for all the data and object properties that
         * are related to the {@code X} axis.
         */
        String SUFFIX_X = "X";

        /**
         * The suffix used for all the data and object properties that
         * are related to the {@code Y} axis.
         */
        String SUFFIX_Y = "Y";

        /**
         * The suffix used for all the data and object properties that
         * are related to the {@code Z} axis.
         */
        String SUFFIX_Z = "Z";

        /**
         * The prefix used in object properties, representing
         * a spatial relation between two objects.
         */
        String PREFIX_SPATIAL = "is";
        /**
         * The suffix used in not symmetric
         * object properties, representing
         * a spatial relation between two objects.
         */
        String SUFFIX_SPATIAL_ASYMMETRIC = "Of";
        /**
         * The suffix used in symmetric
         * object properties, representing
         * a spatial relation between two objects.
         */
        String SUFFIX_SPATIAL_SYMMETRIC = "With";

    }

    /**
     * The interface containing all the constants related to individuals.
     * <p>
     *     This interface is aimed to be a container for all the
     *     {@code String} constants used in the ontology
     *     for addressing individuals.
     *
     * <div style="text-align:center;"><small>
     * <b>File</b>:        it.emarolab.scene_identification_tagging.SITBase <br>
     * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
     * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
     * <b>affiliation</b>: EMAROLab, DIBRIS, University of Genoa. <br>
     * <b>date</b>:        05/06/17 <br>
     * </small></div>
     */
    interface INDIVIDUAL
            extends VOCABOLARY{

        /**
         * The prefix used for all the individuals with
         * a primitive shape (i.e.: belonging to
         * the {@link CLASS#PRIMITIVE}).
         */
        String PREFIX_PRIMITIVE = "P-";

        /**
         * The prefix used for all the individuals with
         * a spherical shape (i.e.: belonging to
         * the {@link CLASS#SPHERE}).
         */
        String PREFIX_SPHERE = "S-";

        /**
         * The prefix used for all the individuals with
         * shape that has an orientation (i.e.: belonging to
         * the {@link CLASS#ORIENTABLE}).
         */
        String PREFIX_ORIENTABLE = "O-";

        /**
         * The prefix used for all the individuals with
         * a conical shape (i.e.: belonging to
         * the {@link CLASS#CONE}).
         */
        String PREFIX_CONE = "C-";

        /**
         * The prefix used for all the individuals with
         * a cylindrical shape (i.e.: belonging to
         * the {@link CLASS#CYLINDER}).
         */
        String PREFIX_CYLINDER = "R-";

        /**
         * The prefix used for all the individuals with
         * a planar shape (i.e.: belonging to
         * the {@link CLASS#PLANE}).
         */
        String PREFIX_PLANE = "P-";

        /**
         * The prefix used for all the individuals
         * that define an abstract scene representation
         * (i.e.: belonging to the {@link CLASS#SPHERE}).
         */
        String SCENE = "Sn-";
    }

    /**
     * The interface containing all the constants related to classes.
     * <p>
     *     This interface is aimed to be a container for all the
     *     {@code String} constants used in the ontology
     *     for addressing classes.
     *
     * <div style="text-align:center;"><small>
     * <b>File</b>:        it.emarolab.scene_identification_tagging.SITBase <br>
     * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
     * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
     * <b>affiliation</b>: EMAROLab, DIBRIS, University of Genoa. <br>
     * <b>date</b>:        05/06/17 <br>
     * </small></div>
     */
    interface CLASS
            extends VOCABOLARY{

        /**
         * The name of te class containing all the 
         * abstract scene representations (i.e. {@link INDIVIDUAL#SCENE}).
         */
        String SCENE = "Scene";

        /**
         * The name of the class containing all the object
         * with coefficients related to a sphere
         * (i.e.: {@link it.emarolab.scene_identification_tagging.realObject.Sphere}).
         */
        String SPHERE = "Sphere";

        /**
         * The name of the class containing all the object
         * with coefficients related to a plane
         * (i.e.: {@link it.emarolab.scene_identification_tagging.realObject.Plane}).
         */
        String PLANE = "Plane";

        /**
         * The name of the class containing all the object
         * with coefficients related to an orientable sphere
         * (i.e.: {@link it.emarolab.scene_identification_tagging.realObject.Orientable})
         */
        String ORIENTABLE = "Orientable";

        /**
         * The name of the class containing all the object
         * with coefficients related to a cone
         * (i.e.: {@link it.emarolab.scene_identification_tagging.realObject.Cone})
         */
        String CONE = "Cone";

        /**
         * The name of the class containing all the object
         * with coefficients related to a cylinder
         * (i.e.: {@link it.emarolab.scene_identification_tagging.realObject.Cylinder})
         */
        String CYLINDER = "Cylinder";

        /**
         * The name of the class containing all the object
         * with coefficients related to a primitive shape
         * (i.e.: {@link it.emarolab.scene_identification_tagging.realObject.GeometricPrimitive})
         */
        String PRIMITIVE = "GeometricPrimitive";
    }

    /**
     * The interface containing all the constants related to data properties.
     * <p>
     *     This interface is aimed to be a container for all the
     *     {@code String} constants used in the ontology
     *     for addressing data properties.
     *     <br>
     *     All related values should be expressed in meters.
     *
     * <div style="text-align:center;"><small>
     * <b>File</b>:        it.emarolab.scene_identification_tagging.SITBase <br>
     * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
     * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
     * <b>affiliation</b>: EMAROLab, DIBRIS, University of Genoa. <br>
     * <b>date</b>:        05/06/17 <br>
     * </small></div>
     */
    interface DATA_PROPERTY
            extends VOCABOLARY{

        /**
         * The name prefix of the data properties used to define the
         * center of mass of a {@link CLASS#PRIMITIVE}.
         */
        String CENTER = PREFIX_GEOMETRIC + "center";
        
        /**
         * The name of the data property defining the {@code X}
         * coordinate of the center of mass of each objects.
         */
        String CENTER_X = CENTER + SUFFIX_X;
        
        /**
         * The name of the data property defining the {@code Y}
         * coordinate of the center of mass of each objects.
         */
        String CENTER_Y = CENTER + SUFFIX_Y;
        
        /**
         * The name of the data property defining the {@code Z}
         * coordinate of the center of mass of each objects.
         */
        String CENTER_Z = CENTER + SUFFIX_Z;

        
        
        /**
         * The name prefix of the data properties used to define the
         * direction of the principal axis of an {@link CLASS#ORIENTABLE}.
         */
        String AXIS = PREFIX_GEOMETRIC + "axis";
        
        /**
         * The name of the data property defining the {@code X}
         * component of the principal direction of each orientable objects.
         */
        String AXIS_X = AXIS + SUFFIX_X;
        
        /**
         * The name of the data property defining the {@code Y}
         * component of the principal direction of each orientable objects.
         */
        String AXIS_Y = AXIS + SUFFIX_Y;
        
        /**
         * The name of the data property defining the {@code Z}
         * component of the principal direction of each orientable objects.
         */
        String AXIS_Z = AXIS + SUFFIX_Z;

        
        
        /**
         * The name of the data property used to define the
         * radius of a {@link CLASS#SPHERE}.
         */
        String RADIUS_SPHERE = PREFIX_HAS + "sphere_radius";

        
        
        /**
         * The name of the data property used to define the
         * hessian of a {@link CLASS#PLANE}.
         */
        String HESSIAN = PREFIX_GEOMETRIC + "hessian";

        
        
        /**
         * The name prefix of the data properties used to define the
         * coefficient specific to a {@link CLASS#CONE}.
         */
        String CONE = "cone_";

        /**
         * The name prefix of the data properties used to define the
         * 3D points of a {@link CLASS#CONE}.
         */
        String APEX = PREFIX_HAS + CONE + "apex";

        /**
         * The name of the data property used to define the {@code X}
         * coordinate of the apex of a {@link CLASS#CONE}.
         */
        String CONE_APEX_X = APEX + SUFFIX_X;

        /**
         * The name of the data property used to define the {@code Y}
         * coordinate of the apex of a {@link CLASS#CONE}.
         */
        String CONE_APEX_Y = APEX + SUFFIX_Y;

        /**
         * The name of the data property used to define the {@code Z}
         * coordinate of the apex of a {@link CLASS#CONE}.
         */
        String CONE_APEX_Z = APEX + SUFFIX_Z;

        /**
         * The name of the data property used to define 
         * the height of a {@link CLASS#CONE}.
         */
        String CONE_HEIGHT = PREFIX_HAS + CONE + "height";

        /**
         * The name of the data property used to define 
         * the radius of a {@link CLASS#CONE}.
         */
        String CONE_RADIUS = PREFIX_HAS + CONE + "radius";


        /**
         * The name prefix of the data properties used to define the
         * coefficient specific to a {@link CLASS#CYLINDER}.
         */
        String CYLINDER = "cylinder_";

        /**
         * The name prefix of the data properties used to define the
         * 3D points of a {@link CLASS#CYLINDER}.
         */
        String CYLINDER_POINT = PREFIX_HAS + CYLINDER + "point";

        /**
         * The name of the data property used to define the {@code X}
         * coordinate of a generic point in the axis of a {@link CLASS#CYLINDER}.
         */
        String CYLINDER_POINT_X = CYLINDER_POINT + SUFFIX_X;

        /**
         * The name of the data property used to define the {@code Y}
         * coordinate of a generic point in the axis of a {@link CLASS#CYLINDER}.
         */
        String CYLINDER_POINT_Y = CYLINDER_POINT + SUFFIX_Y;

        /**
         * The name of the data property used to define the {@code Z}
         * coordinate of a generic point in the axis of a {@link CLASS#CYLINDER}.
         */
        String CYLINDER_POINT_Z = CYLINDER_POINT + SUFFIX_Z;

        /**
         * The name of the data property used to define 
         * the height of a {@link CLASS#CYLINDER}.
         */
        String CYLINDER_HEIGHT = PREFIX_HAS + CYLINDER + "height";

        /**
         * The name of the data property used to define 
         * the radius of a {@link CLASS#CYLINDER}.
         */
        String CYLINDER_RADIUS = PREFIX_HAS + CYLINDER + "radius";

        /**
         * The name of the data property used to define the
         * time stamp of each individuals in the ontology.
         */
        String TIME = PREFIX_HAS + "time";

        /**
         * The name of the data property used to define the
         * unique identifier (with also {@code PREFIX}) of each individuals in the ontology.
         */
        String ID = PREFIX_HAS + "id";
    }

    /**
     * The interface containing all the constants related to object properties.
     * <p>
     *     This interface is aimed to be a container for all the
     *     {@code String} constants used in the ontology
     *     for addressing object properties.
     *     <br>
     *     Note that the implementation does not hard code the type
     *     of spatial relations. Those are obtained at run time
     *     by considering all the object properties that are used between
     *     two individual that belongs to {@link CLASS#PRIMITIVE} or its
     *     sub classes.
     *
     * <div style="text-align:center;"><small>
     * <b>File</b>:        it.emarolab.scene_identification_tagging.SITBase <br>
     * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
     * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
     * <b>affiliation</b>: EMAROLab, DIBRIS, University of Genoa. <br>
     * <b>date</b>:        05/06/17 <br>
     * </small></div>
     */
    interface OBJECT_PROPERTY
            extends VOCABOLARY{

        /**
         * The inverse property of {@link #SPATIAL_RIGHT}.
         */
        String SPATIAL_LEFT = PREFIX_SPATIAL + "Left" + SUFFIX_SPATIAL_ASYMMETRIC;
        /**
         * The inverse property of {@link #SPATIAL_ABOVE}.
         */
        String SPATIAL_BELOW = PREFIX_SPATIAL + "Below" + SUFFIX_SPATIAL_ASYMMETRIC;
        /**
         * TThe inverse property of {@link #SPATIAL_BEHIND}
         */
        String SPATIAL_FRONT = PREFIX_SPATIAL + "Front" + SUFFIX_SPATIAL_ASYMMETRIC;


        /**
         * The name of the object property representing a spatial relation
         * in which an object is on the right hand side of another.
         * Also, the ontology should consider it as {@code transitive} and
         * define its {@code inverse} as well.
         */
        String SPATIAL_RIGHT = PREFIX_SPATIAL + "Right" + SUFFIX_SPATIAL_ASYMMETRIC;
        /**
         * The name of the object property representing a spatial relation
         * in which an object is above of another.
         * Also, the ontology should consider it as {@code transitive} and
         * define its {@code inverse} as well.
         */
        String SPATIAL_ABOVE = PREFIX_SPATIAL + "Above" + SUFFIX_SPATIAL_ASYMMETRIC;
        /**
         * The name of the object property representing a spatial relation
         * in which an object is behind of another.
         * Also, the ontology should consider it as {@code transitive} and
         * define its {@code inverse} as well.
         */
        String SPATIAL_BEHIND = PREFIX_SPATIAL + "Behind" + SUFFIX_SPATIAL_ASYMMETRIC;
        /**
         * The name of the object property representing a spatial relation
         * in which an object is parallel with an another.
         * The ontology should consider it as {@code transitive} and {@code symmetric}.
         */
        String SPATIAL_PARALLEL = PREFIX_SPATIAL + "Parallel" + SUFFIX_SPATIAL_SYMMETRIC;
        /**
         * The name of the object property representing a spatial relation
         * in which an object is perpendicular with an another.
         * The ontology should consider it as {@code transitive} and {@code symmetric}.
         */
        String SPATIAL_PERPENDICULAR = PREFIX_SPATIAL + "Perpendicular" +SUFFIX_SPATIAL_SYMMETRIC;
        /**
         * The name of the object property representing a spatial relation
         * in which an object is coaxial with an another.
         * The ontology should consider it as {@code transitive} and {@code symmetric}.
         */
        String SPATIAL_COAXIAL = PREFIX_SPATIAL + "Coaxial" + SUFFIX_SPATIAL_SYMMETRIC;
        /**
         * The base name of an object property representing a spatial relation
         * in which an object is aligned with another along a specific axis.
         */
        String SPATIAL_ALONG = PREFIX_SPATIAL + "Along";
        /**
         * The name of the object property representing a spatial relation
         * in which an object is aligned with another along the {@code X} axis.
         * The ontology should consider it as {@code transitive} and {@code symmetric}.
         */
        String SPATIAL_ALONGX = SPATIAL_ALONG + SUFFIX_X + SUFFIX_SPATIAL_SYMMETRIC;
        /**
         * The name of the object property representing a spatial relation
         * in which an object is aligned with another along the {@code Y} axis.
         * The ontology should consider it as {@code transitive} and {@code symmetric}.
         */
        String SPATIAL_ALONGY = SPATIAL_ALONG + SUFFIX_Y + SUFFIX_SPATIAL_SYMMETRIC;
        /**
         * The name of the object property representing a spatial relation
         * in which an object is aligned with another along the {@code Z} axis.
         * The ontology should consider it as {@code transitive} and {@code symmetric}.
         */
        String SPATIAL_ALONGZ = SPATIAL_ALONG + SUFFIX_Z + SUFFIX_SPATIAL_SYMMETRIC;

        /**
         * The prefix used to define the spatial relations
         * between an abstract scene representation (i.e. {@link INDIVIDUAL#SCENE}).
         * The suffix is composed by the relative object property used between
         * individual identifying real objects, obtained a run time.
         */
        String SCENE_SPATIAL_PRFIX = "has-scene_";
    }
}
