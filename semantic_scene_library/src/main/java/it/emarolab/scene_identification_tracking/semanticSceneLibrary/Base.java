package it.emarolab.scene_identification_tracking.semanticSceneLibrary;

import it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.Semantics;
import org.semanticweb.owlapi.model.OWLObjectProperty;

/**
 * Contains common utilities among all the architecture. Mainly for debugging and logging.
 * <p>
 *     This interface should be implemented by all the classes in this software architecture
 *     and it is aimed into describe:<ul>
 *        <li> a common {@link Vocabolary} for the architecture,
 *        <li> common implementation for logging and debugging,
 *        <li> common policy for error catching (see: {@link TryMap}).
 *     </ul>
 *
 * <div style="text-align:center;"><small>
 * <b>File</b>:        it.emarolab.scene_identification_tracking.semanticSceneLibrary.Base <br>
 * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
 * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
 * <b>affiliation</b>: DIBRIS, EMAROLab, University of Genoa. <br>
 * <b>date</b>:        04/02/2017 <br>
 * </small></div>
 *
 * @see SITBase
 * @see TryMap
 * @see Vocabolary
 */
public interface Base {

    // [[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[  BASE INTERFACE ]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]
    //                                                todo: add print on file
    /**
     * shows a debugging information message.
     * @param msg the message to print
     */
    void log(Object msg); // showDebug
    /**
     * shows a warning information message.
     * @param msg the message to print in warning mode
     */
    void logWarning(Object msg);
    /**
     * shows an error information message.
     * @param msg the message to print in error mode
     */
    void logError(Object msg);
    /**
     * shows an exception information message.
     * @param e the exception to print in error mode
     */
    void logError(Exception e);

    /**
     * Return a string with the given message {@code message} and {@code lenght},
     * it is mainly used for logging purposes.
     * It is based on {@link Logger#padString(String, int, boolean)}.
     * @param message the string to pad
     * @param length the length of the returning string
     * @param right set to {@code true} for right flushing. {@code False} for left.
     * @return a string with the given message and length.
     */
    default String padString(String message, int length, boolean right){
        return Logger.padString( message, length, right);
    }
    default String padString(Object message, int length, boolean right){
        return Logger.padString( message.toString(), length, right);
    }

    /**
     * This method should be based on a copy constructor that takes the
     * derived class and all {@code super} copy constrcutors.<br>
     * Indeed it should just: {@code return new Base( this);}.
     * @return a new copy of this class.
     */
    Base copy();


    // [[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[     CONSTANTS   ]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]
    //                                                todo document
    /**
     * Contains all the constants of the architecture.
     * <p>
     *     The actual values are divided into sub classes describing:<ul>
     *        <li> constants for {@link LOGGING},
     *        <li> constants for the {@link STATEMAPPING} semantic operations,
     *        <li> constants for {@link ONTOLOGY} entities. Currently:
     *        <ul>
     *            <li> for classes: {@link CLASS},
     *            <li> for individuals: {@link INDIVIDUAL},
     *            <li> for object properties: {@link OBJECTPROPERTY}
     *            <li> for data properties: {@link DATAPROPERTY}.
     *        </ul>
     *     </ul>
     *
     * <div style="text-align:center;"><small>
     * <b>File</b>:        it.emarolab.scene_identification_tracking.semanticSceneLibrary.Base <br>
     * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
     * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
     * <b>affiliation</b>: DIBRIS, EMAROLab, University of Genoa. <br>
     * <b>date</b>:        04/02/2017 <br>
     * </small></div>
     *
     * @see LOGGING
     * @see ONTOLOGY
     * @see Base
     */
    static class Vocabolary implements LOGGING, ONTOLOGY, STATEMAPPING {
        private Vocabolary(){}// not instanciable
    }

    /**
     * Contains all the constants fields of the architecture regarding logging.
     * <p>
     *     It is a container for constants used for printing logs.
     *
     * <div style="text-align:center;"><small>
     * <b>File</b>:        it.emarolab.scene_identification_tracking.semanticSceneLibrary.Base <br>
     * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
     * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
     * <b>affiliation</b>: DIBRIS, EMAROLab, University of Genoa. <br>
     * <b>date</b>:        04/02/2017 <br>
     * </small></div>
     *
     * @see Vocabolary
     * @see Base
     */
    static interface LOGGING {
        public static final String NEW_LINE = "\n"; // todo also for file separator

        public static final String MSG_EMPTY = "'??'";

        public static final String MSG_OWL_MANAGED = "OWL api already manage it !!!";

        public static final String INTENT_BELONG = "€";
        public static final String INTENT_BELONG_ADD = "€+";
        public static final String INTENT_BELONG_REMOVE = "€-";
        public static final String INTENT_LITERAL = "LITERAL";
        public static final String INTENT_LINK = "LINK";
        public static final String INTENT_LINK_MULTI = "M_LINK";
        public static final String INTENT_linkSPATIAL_MULTI = "LS";
        public static final String INTENT_LINK_MULTI_ADD = "+";
        public static final String INTENT_LINK_MULTI_REMOVE = "-";
        public static final String INTENT_TYPE = "TYPE";

        public static final int LENGTH = 15;
        public static final int LENGTH_MEDIUM = 10;
        public static final int LENGTH_SHORT = 8;
        public static final int LENGTH_LONG = 30;
        public static final int LENGTH_NUMBER = 4;

        /** The message suffix for error logging. */
        public static final String INDENTATION_ERROR   = "[ERROR]   " ;
        /** The message suffix for warning logging. */
        public static final String INDENTATION_WARNING = "[WARNING] " ;
        /** The message suffix for debugging logging. */
        public static final String INDENTATION_DEBUG   = "[DEBUG]   " ;

        public static final Boolean DEFAULT_DEBUG_FLAG = false;
        public static final Boolean DEFAULT_DOWN_FLAG = false;

        /** the default value for the description field (empty) */
        public static final String DEFAULT_3D_DESCRIPTION = "";

        public static final String STATE_INITIALISED  = "INITIALISED";
        public static final String STATE_ERROR        = "ERROR";
        public static final String STATE_INCONSISTENT = "INCONSISTENT";
        public static final String STATE_NOT_CHANGED  = "UNCHANGED";
        public static final String STATE_ADDED        = "ADDED";
        public static final String STATE_UPDATED      = "UPDATED";
        public static final String STATE_REMOVED      = "REMOVED";
        public static final String STATE_ABSENT       = "ABSENT";
        public static final String STATE_SUCCESS      = "SUCCESS";

        public static final String STATE_TYPE_WRITE = "WR";
        public static final String STATE_TYPE_READ = "RD";
        public static final String STATE_TYPE_MAPPING = "MP";
    }

    /**
     * Contains all the constants fields of the architecture regarding semantic manipulation states.
     * <p>
     *     It is a container for constants used for defining the value of {@link Semantics#readSemantic()}.
     * <p>
     *     <b>REMARK</b>: if this enumeration is changed you may to have to adjust
     *     {@link Semantics.MappingState}, {@link Semantics.ReadingState} and {@link Semantics.WritingState}.
     * <div style="text-align:center;"><small>
     * <b>File</b>:        {@link it.emarolab.scene_identification_tracking.semanticSceneLibrary.Base} <br>
     * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
     * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
     * <b>affiliation</b>: DIBRIS, EMAROLab, University of Genoa. <br>
     * <b>date</b>:        04/02/2017 <br>
     * </small></div>
     *
     * @see Vocabolary
     * @see Base
     */
    static interface STATEMAPPING {
        // if you change this enumerator remember to change on extending class (constructing validation problems)

        static public final int INITIALISED = -2;
        /** The identifier for the {@code ERROR} state. */
        static public final int ERROR = -1;
        /** The identifier for the {@code INCONSISTENT} state. */
        static public final int INCONSISTENT = 0;
        /** The identifier for the {@code NOT_CHANGED} (up to dated) state. */
        static public final int NOT_CHANGED = 1;

        /** The identifier for the {@code ADDED} state. */
        static public final int ADDED = 2; // something was added
        /** The identifier for the {@code UPDATED} state. */
        static public final int UPDATED = 3; // something was updated
        /** The identifier for the {@code REMOVED} state. */
        static public final int REMOVED = 4;

        /** The identifier for the ABSENT state. */
        static public final int ABSENT = 2; // not found in the ontology
        /** The identifier for the SUCCESS state. */
        static public final int SUCCESS = 3; // fond and read
    }

    /**
     * Contains all the constants fields of the architecture regarding ontology.
     * <p>
     *     It is a container for constants used for semantically representations,
     *     those are sub-organised in the classes:<ul>
     *         <li> for classes: {@link CLASS},
     *         <li> for individuals: {@link INDIVIDUAL},
     *         <li> for object properties: {@link OBJECTPROPERTY}
     *         <li> for data properties: {@link DATAPROPERTY}.
     *     </ul>
     *
     *
     * <div style="text-align:center;"><small>
     * <b>File</b>:        {@link it.emarolab.scene_identification_tracking.semanticSceneLibrary.Base} <br>
     * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
     * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
     * <b>affiliation</b>: DIBRIS, EMAROLab, University of Genoa. <br>
     * <b>date</b>:        04/02/2017 <br>
     * </small></div>
     *
     * @see Vocabolary
     * @see CLASS
     * @see INDIVIDUAL
     * @see DATAPROPERTY
     * @see OBJECTPROPERTY
     * @see Base
     */
    static interface ONTOLOGY extends CLASS, INDIVIDUAL, DATAPROPERTY, OBJECTPROPERTY {

        public static final String DEFAULT_3d_xSUFFIX = "X";
        public static final String DEFAULT_3d_ySUFFIX = "Y";
        public static final String DEFAULT_3d_zSUFFIX = "Z";
    }
    /**
     * Static container for for constants relate to classes in the {@link ONTOLOGY}.
     *
     * <div style="text-align:center;"><small>
     * <b>File</b>:        {@link it.emarolab.scene_identification_tracking.semanticSceneLibrary.Base} <br>
     * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
     * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
     * <b>affiliation</b>: DIBRIS, EMAROLab, University of Genoa. <br>
     * <b>date</b>:        04/02/2017 <br>
     * </small></div>
     *
     * @see Vocabolary
     * @see ONTOLOGY
     * @see Base
     */
    static interface CLASS {
        static public final String SPHERE       = "Sphere";
        static public final String CONE         = "Cone";
        static public final String CYLINDER     = "Cylinder";
        static public final String PLANE        = "Plane";
        static public final String ORIENTBLE    = "Orientable";
        static public final String PRIMITIVE    = "GeometricPrimitive";
        static public final String OBJECT       = "RealObject";
    }
    /**
     * Static container for for constants relate to individuals in the {@link ONTOLOGY}.
     *
     * <div style="text-align:center;"><small>
     * <b>File</b>:        {@link it.emarolab.scene_identification_tracking.semanticSceneLibrary.Base} <br>
     * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
     * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
     * <b>affiliation</b>: DIBRIS, EMAROLab, University of Genoa. <br>
     * <b>date</b>:        04/02/2017 <br>
     * </small></div>
     *
     * @see Vocabolary
     * @see ONTOLOGY
     * @see Base
     */
    static interface INDIVIDUAL {

    }
    /**
     * Static container for for constants relate to data properties in the {@link ONTOLOGY}.
     *
     * <div style="text-align:center;"><small>
     * <b>File</b>:        {@link it.emarolab.scene_identification_tracking.semanticSceneLibrary.Base} <br>
     * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
     * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
     * <b>affiliation</b>: DIBRIS, EMAROLab, University of Genoa. <br>
     * <b>date</b>:        04/02/2017 <br>
     * </small></div>
     *
     * @see Vocabolary
     * @see ONTOLOGY
     * @see Base
     */
    static interface DATAPROPERTY {

        public static final String DEFAULT_3dPROPERTY_xSUFFIX = ONTOLOGY.DEFAULT_3d_xSUFFIX;
        public static final String DEFAULT_3dPROPERTY_ySUFFIX = ONTOLOGY.DEFAULT_3d_ySUFFIX;
        public static final String DEFAULT_3dPROPERTY_zSUFFIX = ONTOLOGY.DEFAULT_3d_zSUFFIX;

        public static final String DEFAULT_obj_TIME = "has_time";
        public static final String DEFAULT_obj_ID = "has_id";

        public static final String DEFAULT_objPRIMITIVE_CENTER = "has-geometric_center";
        public static final String DEFAULT_objSPHERE_RADIUS = "has-sphere_radius";
        public static final String DEFAULT_objORIENTABLE_AXIS = "has-geometric_axis";
        public static final String DEFAULT_objPLANE_HESSIAN = "has-geometric_hessian";
        public static final String DEFAULT_objCONE_RADIUS = "has-cone_radius";
        public static final String DEFAULT_objCONE_HEIGHT = "has-cone_height";
        public static final String DEFAULT_objCONE_APEX = "has-cone_apex";
        public static final String DEFAULT_objCYLINDER_RADIUS = "has-cylinder_radius";
        public static final String DEFAULT_objCYLINDER_HEIGHT = "has-cylinder_height";
        public static final String DEFAULT_objCYLINDER_POINT = "has-cylinder_point";
    }
    /**
     * Static container for for constants relate to the data properties in the {@link ONTOLOGY}.
     *
     * <div style="text-align:center;"><small>
     * <b>File</b>:        {@link it.emarolab.scene_identification_tracking.semanticSceneLibrary.Base} <br>
     * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
     * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
     * <b>affiliation</b>: DIBRIS, EMAROLab, University of Genoa. <br>
     * <b>date</b>:        04/02/2017 <br>
     * </small></div>
     *
     * @see Vocabolary
     * @see ONTOLOGY
     * @see Base
     */
    static interface OBJECTPROPERTY {
        public static final String DEFAULT_3dPROPERTY_xSUFFIX = ONTOLOGY.DEFAULT_3d_xSUFFIX;
        public static final String DEFAULT_3dPROPERTY_ySUFFIX = ONTOLOGY.DEFAULT_3d_ySUFFIX;
        public static final String DEFAULT_3dPROPERTY_zSUFFIX = ONTOLOGY.DEFAULT_3d_zSUFFIX;

        // Asymmetric properties
        public static final String DEFAULT_RELATION_BEHIND = "isBehindOf";
        public static final String DEFAULT_RELATION_ABOVE = "isAboveOf";
        public static final String DEFAULT_RELATION_RIGHT = "isRightOf";
        public static final String DEFAULT_RELATION_BEHINDinverse = "isInFrontOf";
        public static final String DEFAULT_RELATION_ABOVEinverse = "isBelowOf";
        public static final String DEFAULT_RELATION_RIGHTinverse = "isLeftOf";

        // Symmetric properties
        public static final String DEFAULT_RELATION_LONGX = "isAlong" + DEFAULT_3dPROPERTY_xSUFFIX;
        public static final String DEFAULT_RELATION_LONGY = "isAlong"  + DEFAULT_3dPROPERTY_ySUFFIX;
        public static final String DEFAULT_RELATION_LONGZ = "isAlong"  + DEFAULT_3dPROPERTY_zSUFFIX;
        public static final String DEFAULT_RELATION_COAXIAL = "isCoaxialWith";
        public static final String DEFAULT_RELATION_PARALLEL = "isParallelTo";
        public static final String DEFAULT_RELATION_PERPENDICULAR = "isPerpendicularTo";




        public static final String DEFAULT_SPATIAL_COAXIAL = "has-scene_coaxial";
        public static final String DEFAULT_SPATIAL_PARALLEL = "has-scene_parallel";
        public static final String DEFAULT_SPATIAL_PERPENDICULAR = "has-scene_perpendicular";
        public static final String DEFAULT_SPATIAL_LONGX = "has-scene_alongX";
        public static final String DEFAULT_SPATIAL_LONGY = "has-scene_alongY";
        public static final String DEFAULT_SPATIAL_LONGZ = "has-scene_alongZ";
        public static final String DEFAULT_SPATIAL_RIGHT = "has-scene_right";
        public static final String DEFAULT_SPATIAL_ABOVE = "has-scene_above";
        public static final String DEFAULT_SPATIAL_BEHIND = "has-scene_behind";

    }



    // [[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[  STATIC LOGGER  ]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]
    /**
     * Contains common utilities for static logging.
     * <p>
     *     This is just a static implementation for textual printing,
     *     those procedures are used by the {@link SITBase} class.
     *     <br>
     *     It also propagate all the classes that contextualise constants by extending {@link Vocabolary}.
     *
     * <div style="text-align:center;"><small>
     * <b>File</b>:        {@link it.emarolab.scene_identification_tracking.semanticSceneLibrary.Base} <br>
     * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
     * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
     * <b>affiliation</b>: DIBRIS, EMAROLab, University of Genoa. <br>
     * <b>date</b>:        04/02/2017 <br>
     * </small></div>
     *
     * @see Vocabolary
     * @see Base
     * @see SITBase
     */
    class Logger {
        // verbose on/off
        private static boolean DEBUG = LOGGING.DEFAULT_DEBUG_FLAG;
        // logger off/on (shut down)
        private static boolean DOWN = LOGGING.DEFAULT_DOWN_FLAG;

        /**
         * Enable/Disable verbose printings.
         * Initially this value is set to {@link LOGGING#DEFAULT_DEBUG_FLAG}.
         * @param d set to <code>true</code> for being verbose.
         *          Otherwise, only errors and warnings will be printed.
         */
        public static void showDebugs( boolean d){
            Logger.DEBUG = d;
        }
        /**
         * Disable/Enable all the printings.
         * Initially this value is set to {@link LOGGING#DEFAULT_DOWN_FLAG}.
         * @param shutDown set to <code>true</code> to disable all printing.
         *                 Otherwise, prints based on {@link #showDebugs(boolean)}.
         */
        public static void shutDownLoggers(boolean shutDown){
            Logger.DOWN = shutDown;
        }

        /**
         * It return the {@code message} into a string of the given {@code length}.
         * If the {@code message.length()} is greater than the {@code length} the
         * string '..' is appended where the message is cut. Otherwise, empty space are added.
         * This manipulation is done on the {@code right} or on the left of the message based on the input parameter.
         * @param message the message to pad in a given length
         * @param length the length of the returning value
         * @param right set to {@code true} for right flushed message. {@code False} for left.
         * @return the message padded with the given length.
         */
        public static String padString(String message, int length, boolean right) {
            if (message == null)
                return null;
            int size = message.length();
            if (size == length)
                return message;
            if (size < length) {
                if (right)
                    return getOffset(length - size) + message;
                else return message + getOffset(length - size);
            } else {
                String outer = "..";
                size += outer.length();
                if (right)
                    return outer + message.substring(size - length, message.length());
                else return message.substring(0, message.length() - (size - length)) + outer;
            }
        }
        private static String getOffset(int size) { // return an "   " string of given size
            String out = "";
            for (int i = 0; i < size; i++)
                out += " ";
            return out;
        }

        /**
         * Static logger for errors, it is based on {@code printStackTrace()}.
         * See debugging flags: {@link #showDebugs(boolean)}, {@link #shutDownLoggers(boolean)}.
         * @param e the exception to be notified
         */
        public static void logERROR(Exception e){
            if( ! DOWN)
                e.printStackTrace();
        }
        /**
         * Static logger for errors.
         * See debugging flags: {@link #showDebugs(boolean)}, {@link #shutDownLoggers(boolean)}.
         * This methods call {@link #dumpStack()} if {@code ! {@link #DOWN}}.
         * It always logs something.
         * @param msg the error message to notify.
         */
        public static void logERROR( Object msg){
            log( true, msg);
            if( ! DOWN) {
                Thread.dumpStack();
            }
        }

        /**
         * It just calls {@code Thread.dumpStack();}.
         */
        public static void dumpStack(){
            System.err.println( "######################################");
            Thread.dumpStack();
            System.err.println( "######################################");
        }

        /**
         * Static logger for warning.
         * See debugging flags: {@link #showDebugs(boolean)}, {@link #shutDownLoggers(boolean)}
         * @param msg the warning message to notify.
         */
        public static void logWARNING(Object msg){
            log( null, msg);
        }
        /**
         * Static logger for debugging information.
         * See debugging flags: {@link #showDebugs(boolean)}, {@link #shutDownLoggers(boolean)}
         * @param msg the warning message to notify.
         */
        public static void LOG( Object msg){
            log( false, msg);
        }

        // used by all the method that write on console
        // isError={(false->DEBUG),(null->WARNING),(true->ERROR)}
        private static void log( Boolean isError, Object msg){
            if( ! DOWN) {
                if (isError == null){
                    if (DEBUG)
                        System.out.println( LOGGING.INDENTATION_WARNING + msg.toString()); // warning
                } else if (isError)
                    System.err.println( LOGGING.INDENTATION_ERROR + msg.toString()); // error
                else if (DEBUG)
                    System.out.println( LOGGING.INDENTATION_DEBUG + msg.toString()); // info
            }
        }
    }



    // [[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[    BASE CLASS   ]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]
    /**
     * The base class for all the architecture.
     * <p>
     *     This defines the behaviour of all the classes of this architecture
     *     by implementing the {@link Base} interface.
     *     <br>
     *     Currently it implements a basic logger on console through
     *     the static class {@link Logger}, which also propagates the classes
     *     that contextualise the constants from the {@link Vocabolary} container.
     *     Also, it force all the class of the architecture to have a {@link #copy()}
     *     method that should be based on a copy constructor: {@code SITBase( SITBase)}.
     *
     * <div style="text-align:center;"><small>
     * <b>File</b>:        {@link it.emarolab.scene_identification_tracking.semanticSceneLibrary.Base} <br>
     * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
     * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
     * <b>affiliation</b>: DIBRIS, EMAROLab, University of Genoa. <br>
     * <b>date</b>:        04/02/2017 <br>
     * </small></div>
     *
     * @see Base
     * @see Logger
     * @see Vocabolary
     */
    abstract class SITBase extends Vocabolary implements Base {

        /**
         * Enable/disable verbose printings (has the same effect as {@link Logger#showDebugs(boolean)}).
         * @param d set to <code>true</code> for being verbose.
         *          Otherwise, only errors and warnings will be printed.
         */
        public void showDebug(boolean d){
            Logger.showDebugs( d);
        }
        /**
         * Dinable/Enable all the printings (has the same effect as {@link Logger#shutDownLoggers(boolean)}).
         * @param shutDown set to <code>true</code> to disable all printing.
         *                 Otherwise, prints based on {@link #showDebug(boolean)}.
         */
        public void shutDownLogger(boolean shutDown){
            Logger.showDebugs( shutDown);
        }

        /**
         * calls {@link Logger#logERROR(Exception)}.
         * @param e to exception to deal with.
         */
        @Override
        public void logError( Exception e){
            Logger.logERROR( e);
        }
        /**
         * calls {@link Logger#logERROR(Object)}.
         * @param msg print <code>msg.{@link Object#toString()}</code> in error mode.
         */
        @Override
        public void logError( Object msg){
            Logger.logERROR( msg);
        }
        /**
         * calls {@link Logger#logWARNING(Object)}.
         * @param msg print <code>msg.{@link Object#toString()}</code> in warning mode.
         */
        @Override
        public void logWarning(Object msg){
            Logger.logWARNING( msg);
        }
        /**
         * calls {@link Logger#LOG(Object)}.
         * @param msg print <code>msg.{@link Object#toString()}</code> in warning mode.
         */
        @Override
        public void log( Object msg){
            Logger.LOG( msg);
        }

    }



    // [[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[ ERROR MANAGERS  ]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]
/*    class DebuggingIntent<M extends Semantics.MappingState> extends Semantics.MIntent< String, String, String> {
        public DebuggingIntent() {
            //super( LOGGING.MSG_EMPTY, LOGGING.MSG_EMPTY);
            setDescription( "DEBUG");
        }
        public DebuggingIntent( String instance, String actions) {
            //super(instance, actions);
            setDescription( "DEBUG");
        }
        public DebuggingIntent( String instance, String actions, String description) {
            //super(instance, actions);
            if (description == null)
                setDescription( "DEBUG");
            else if ( description.isEmpty() | description.equals( LOGGING.MSG_EMPTY))
                setDescription( "DEBUG");
            setDescription( description);
        }
        public DebuggingIntent(Semantics.MappingIntent<String, String, String, M> copy) {
            super(copy);
        }


        @Override
        public M getMergedState() {
            return super.getMergedState();
        }
    }
*/


    /**
     * This is an helper to manage {@link Semantics.MappingState#ERROR} states for {@link Semantics} operations.
     * <p>
     *     It automatically catches for {@link Exception} and return {@link Semantics.MappingState#ERROR}
     *     in the {@link #perform()} method, to be implemented called. In turn, it executes (in a safety manner)
     *     the operation defined in the method {@link #giveAtry()} to be implemented.<br>
     *     Otherwise, the method {@link #onError()} will determine the value of the {@link #perform()} results.
     *
     * <div style="text-align:center;"><small>
     * <b>File</b>:        {@link it.emarolab.scene_identification_tracking.semanticSceneLibrary.Base} <br>
     * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
     * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
     * <b>affiliation</b>: DIBRIS, EMAROLab, University of Genoa. <br>
     * <b>date</b>:        04/02/2017 <br>
     * </small></div>
     *
     *
     * @see Semantics.MappingState
     */
    abstract class TryMap<I,A,L,M extends Semantics.MappingState> extends SITBase{
        protected Semantics.MappingTransitions transiton;

        public TryMap(){
            transiton = new Semantics.MappingTransitions();
        }

        /** Empty constructor, does not do nothing */
        public TryMap( String instance, String atom) {
            Semantics.MappingIntent<String, String, Void, M> intent = new Semantics.MappingIntent<>("\t\t" + instance, atom, getNewState());
            intent.setDescription( "DEBUG");
            transiton = new Semantics.MappingTransitions( intent);
        }

        /** Constructs and sets the message for an eventually {@link #onError()} state. */
        public TryMap(Semantics.MappingIntent<I,A,L,M> intent) {
            transiton = new Semantics.MappingTransitions( intent);
        }

        public Semantics.MappingTransitions getStateTransitions(){
            return transiton;
        }

        /**
         *  make your semantic operations safety from Java {@link Exception}
         *  @return the actual mapping state of the operation or {@link #onError()} if Exception occurs.
         */
        abstract protected Semantics.MappingTransitions giveAtry();

        protected Semantics.MappingIntent<I,A,L,M> getNewIntent(I instance, A atom) {
            Semantics.MappingIntent<I,A,L,M> intent = new Semantics.MappingIntent(instance, atom, getNewState());
            propagateAtomDescription( intent);
            transiton.add( intent);
            return intent;
        }

        /**
         * Called when a Java {@link Exception} occurs in the {@link #perform()} method.
         * @return the generated {@link Semantics.MappingState#ERROR} to be propagated.
         */
        protected Semantics.MappingTransitions onError(){
            transiton.get( transiton.size() - 1).getState().asError();
            return transiton;
        }

        // todo add OnInconsisent

        public Semantics.MappingIntent<?,?,?,M> propagateAtomDescription(Semantics.MappingIntent<?,?,?,M> intent){
            intent.setDescription( transiton.get( 0).getAtom().toString());
            return intent;
        }
        public Semantics.MappingIntent<?,?,?,M> propagateAtomDescription(Semantics.MappingIntent<?,?,?,M> intent, String description){
            intent.setDescription( transiton.get( 0).getAtom().toString() + " " + description);
            return intent;
        }

        /**
         * Perform the {@link #giveAtry()} method safely from Java {@link Exception}
         * also notified by {@link #logError(Exception)}.
         * @return the state of this mapping operation. It can be the
         * {@link #giveAtry()} returning value or {@link #onError()} value.
         */
        public Semantics.MappingTransitions perform() {
            try {
                return giveAtry();
            } catch (Exception e) {
                logError( e);
                return onError();
            }
        }

        /**
         * Since this class is supposed to be inhered and used on demand the copy
         * method is not propagated.
         * @return always null.
         */
        @Override
        public TryMap copy(){
            logError( this.getClass().getSimpleName() + ".copy() hierarchy noy implemented.");
            return null;
        }

        abstract public M getNewState();
    }
}