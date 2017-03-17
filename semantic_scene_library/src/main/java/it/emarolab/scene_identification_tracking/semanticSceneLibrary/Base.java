package it.emarolab.scene_identification_tracking.semanticSceneLibrary;

/**
 * Contains common utilities among all the architecture. Mainly for debugging and logging.
 * <p>
 *     This interface should be implemented by all the classes in this software architecture
 *     and it is aimed into describe:<ul>
 *        <li> a common {@link Vocabolary} for the architecture,
 *        <li> common implementation for logging and debugging,
 *        <li> common policy for error catching (see: { TryMap}).
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
        String NEW_LINE = "\n"; // todo also for file separator

        String MSG_EMPTY = "'??'";

        String MSG_OWL_MANAGED = "OWL api already manage it !!!";

        String INTENT_BELONG = "€";
        String INTENT_BELONG_ADD = "€+";
        String INTENT_BELONG_REMOVE = "€-";
        String INTENT_LITERAL = "LITERAL";
        String INTENT_LINK = "LINK";
        String INTENT_LINK_MULTI = "M_LINK";
        String INTENT_linkSPATIAL_MULTI = "LS";
        String INTENT_LINK_MULTI_ADD = "+";
        String INTENT_LINK_MULTI_REMOVE = "-";
        String INTENT_TYPE = "TYPE";

        int LENGTH = 15;
        int LENGTH_MEDIUM = 10;
        int LENGTH_SHORT = 8;
        int LENGTH_LONG = 30;
        int LENGTH_NUMBER = 4;

        /** The message suffix for error logging. */
        String INDENTATION_ERROR   = "[ERROR]   " ;
        /** The message suffix for warning logging. */
        String INDENTATION_WARNING = "[WARNING] " ;
        /** The message suffix for debugging logging. */
        String INDENTATION_DEBUG   = "[DEBUG]   " ;

        Boolean DEFAULT_DEBUG_FLAG = false;
        Boolean DEFAULT_DOWN_FLAG = false;

        /** the default value for the description field (empty) */
        String DEFAULT_3D_DESCRIPTION = "";

        String STATE_INITIALISED  = "INITIALISED";
        String STATE_ERROR        = "ERROR";
        String STATE_INCONSISTENT = "INCONSISTENT";
        String STATE_NOT_CHANGED  = "UNCHANGED";
        String STATE_ADDED        = "ADDED";
        String STATE_UPDATED      = "UPDATED";
        String STATE_REMOVED      = "REMOVED";
        String STATE_ABSENT       = "ABSENT";
        String STATE_SUCCESS      = "SUCCESS";

        String STATE_TYPE_WRITE = "WR";
        String STATE_TYPE_READ = "RD";
        String STATE_TYPE_MAPPING = "MP";
    }

    /**
     * Contains all the constants fields of the architecture regarding semantic manipulation states.
     * <p>
     *     It is a container for constants used for defining the value of { Semantics#readSemantic()}.
     * <p>
     *     <b>REMARK</b>: if this enumeration is changed you may to have to adjust
     *     { Mappin}, { Semantics.ReadingState} and { Semantics.WritingState}.
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

        int INITIALISED = -2;
        /** The identifier for the {@code ERROR} state. */
        int ERROR = -1;
        /** The identifier for the {@code INCONSISTENT} state. */
        int INCONSISTENT = 0;
        /** The identifier for the {@code NOT_CHANGED} (up to dated) state. */
        int NOT_CHANGED = 1;

        /** The identifier for the {@code ADDED} state. */
        int ADDED = 2; // something was added
        /** The identifier for the {@code UPDATED} state. */
        int UPDATED = 3; // something was updated
        /** The identifier for the {@code REMOVED} state. */
        int REMOVED = 4;

        /** The identifier for the ABSENT state. */
        int ABSENT = 2; // not found in the ontology
        /** The identifier for the SUCCESS state. */
        int SUCCESS = 3; // fond and read
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

        String DEFAULT_3d_xSUFFIX = "X";
        String DEFAULT_3d_ySUFFIX = "Y";
        String DEFAULT_3d_zSUFFIX = "Z";
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
        String SPHERE       = "Sphere";
        String CONE         = "Cone";
        String CYLINDER     = "Cylinder";
        String PLANE        = "Plane";
        String ORIENTBLE    = "Orientable";
        String PRIMITIVE    = "GeometricPrimitive";
        String OBJECT       = "RealObject";
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

        String DEFAULT_3dPROPERTY_xSUFFIX = ONTOLOGY.DEFAULT_3d_xSUFFIX;
        String DEFAULT_3dPROPERTY_ySUFFIX = ONTOLOGY.DEFAULT_3d_ySUFFIX;
        String DEFAULT_3dPROPERTY_zSUFFIX = ONTOLOGY.DEFAULT_3d_zSUFFIX;

        String DEFAULT_obj_TIME = "has_time";
        String DEFAULT_obj_ID = "has_id";

        String DEFAULT_objPRIMITIVE_CENTER = "has-geometric_center";
        String DEFAULT_objSPHERE_RADIUS = "has-sphere_radius";
        String DEFAULT_objORIENTABLE_AXIS = "has-geometric_axis";
        String DEFAULT_objPLANE_HESSIAN = "has-geometric_hessian";
        String DEFAULT_objCONE_RADIUS = "has-cone_radius";
        String DEFAULT_objCONE_HEIGHT = "has-cone_height";
        String DEFAULT_objCONE_APEX = "has-cone_apex";
        String DEFAULT_objCYLINDER_RADIUS = "has-cylinder_radius";
        String DEFAULT_objCYLINDER_HEIGHT = "has-cylinder_height";
        String DEFAULT_objCYLINDER_POINT = "has-cylinder_point";
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
        String DEFAULT_3dPROPERTY_xSUFFIX = ONTOLOGY.DEFAULT_3d_xSUFFIX;
        String DEFAULT_3dPROPERTY_ySUFFIX = ONTOLOGY.DEFAULT_3d_ySUFFIX;
        String DEFAULT_3dPROPERTY_zSUFFIX = ONTOLOGY.DEFAULT_3d_zSUFFIX;

        // Asymmetric properties
        String DEFAULT_RELATION_BEHIND = "isBehindOf";
        String DEFAULT_RELATION_ABOVE = "isAboveOf";
        String DEFAULT_RELATION_RIGHT = "isRightOf";
        String DEFAULT_RELATION_BEHINDinverse = "isInFrontOf";
        String DEFAULT_RELATION_ABOVEinverse = "isBelowOf";
        String DEFAULT_RELATION_RIGHTinverse = "isLeftOf";

        // Symmetric properties
        String DEFAULT_RELATION_LONGX = "isAlong" + DEFAULT_3dPROPERTY_xSUFFIX;
        String DEFAULT_RELATION_LONGY = "isAlong"  + DEFAULT_3dPROPERTY_ySUFFIX;
        String DEFAULT_RELATION_LONGZ = "isAlong"  + DEFAULT_3dPROPERTY_zSUFFIX;
        String DEFAULT_RELATION_COAXIAL = "isCoaxialWith";
        String DEFAULT_RELATION_PARALLEL = "isParallelTo";
        String DEFAULT_RELATION_PERPENDICULAR = "isPerpendicularTo";




        String DEFAULT_SPATIAL_COAXIAL = "has-scene_coaxial";
        String DEFAULT_SPATIAL_PARALLEL = "has-scene_parallel";
        String DEFAULT_SPATIAL_PERPENDICULAR = "has-scene_perpendicular";
        String DEFAULT_SPATIAL_LONGX = "has-scene_alongX";
        String DEFAULT_SPATIAL_LONGY = "has-scene_alongY";
        String DEFAULT_SPATIAL_LONGZ = "has-scene_alongZ";
        String DEFAULT_SPATIAL_RIGHT = "has-scene_right";
        String DEFAULT_SPATIAL_ABOVE = "has-scene_above";
        String DEFAULT_SPATIAL_BEHIND = "has-scene_behind";

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

    // todo add State, Intent, Transitions
}