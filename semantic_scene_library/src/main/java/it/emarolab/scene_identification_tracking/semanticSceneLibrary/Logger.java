package it.emarolab.scene_identification_tracking.semanticSceneLibrary;

import it.emarolab.scene_identification_tracking.semanticSceneLibrary.aMORDescriptor.MORSpatialDescriptor;


/**
 * Defines common function for debugging and logging.
 * <p>
 * This interface should be implemented by all the classes in this software architecture.
 *
 * <div style="text-align:center;"><small>
 * <b>File</b>:       it.emarolab.scene_identification_tracking.semanticSceneLibrary.Semantics <br>
 * <b>Licence</b>:    GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
 * <b>Author</b>:     Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
 * <b>affiliation</b>: DIBRIS, EMAROLab, University of Genoa. <br>
 * <b>date</b>:       06/01/2017 <br>
 * </small></div>
 *
 * @see SITBase
 */
public interface Logger{

    default String getStringfixedLength(String str, int lenght, boolean right){
        return getfixedStringLength( str, lenght, right);
    }
    static String getfixedStringLength(String str, int length, boolean right){ // todo move to logger
        int size = str.length();
        if( size ==  length)
            return str;
        if( size < length){
            if( right)
                return getOffset( length - size) + str;
            else return str + getOffset( length - size);
        } else {
            String outer = "..";
            size += outer.length();
            if (right)
                return outer + str.substring(size - length, str.length());
            else return str.substring(0, str.length() - (size - length)) + outer;
        }
    }
    static String getOffset(int size){
        String out = "";
        for( int i = 0; i < size; i++)
            out += " ";
        return out;
    }

    /** @param msg print <code>msg.{@link Object#toString()}</code> in debugging mode. */
    void log(Object msg); // showDebug
    /** @param msg print <code>msg.{@link Object#toString()}</code> in warning mode. */
    void logWarning(Object msg);
    /** @param msg print <code>msg.{@link Object#toString()}</code> in error mode. */
    void logError(Object msg);
    /** @param e to exception to deal with. */
    void logError(Exception e);
    // todo: add print on file


    /**
     * Defines common function for debugging and logging.
     * <p>
     * By default this implementation enable not verbose printing (only errors and warnings).
     * See {@link #showDebugs(boolean)}, {@link #showDebug(boolean)}, {@link #shutDownLoggers(boolean)}
     * and {@link #shutDownLogger(boolean)} for dynamic and static tune such a behaviour.
     * <p>
     * This interface should be implemented by all the classes in this software architecture.
     * Unfortunately, this implementation is a very basic interface to a logger.
     *
     * <div style="text-align:center;"><small>
     * <b>File</b>:       it.emarolab.scene_identification_tracking.semanticSceneLibrary.Semantics <br>
     * <b>Licence</b>:    GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
     * <b>Author</b>:     Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
     * <b>affiliation</b>: DIBRIS, EMAROLab, University of Genoa. <br>
     * <b>date</b>:       06/01/2017 <br>
     * </small></div>
     *
     * @see Logger
     * @see Semantics.MappingTry
     * @see Semantics.IndividualDescriptor.Array3D
     * @see MORSpatialDescriptor.MORSimpleDescriptor
     * @see it.emarolab.scene_identification_tracking.semanticSceneLibrary.objects.aMOR.MORPrimitive
     */
    class SITBase implements Logger{// todo move away this and Try from this file

        public static final int LOGGING_NAME_LENGTH = 15;
        public static final int LOGGING_MEDIUM_LENGTH = 10;
        public static final int LOGGING_SHORT_NAME_LENGTH = 6;
        public static final int LOGGING_LONG_NAME_LENGTH = 30;
        public static final int LOGGING_NUMBER_LENGTH = 2;
        public static final int LOGGING_LONG_NUMBER_LENGTH = 10;


        /** The message suffix for error logging. */
        public static final String INDENTATION_ERROR   = "[ERROR]   " ;
        /** The message suffix for warning logging. */
        public static final String INDENTATION_WARNING = "[WARNING] " ;
        /** The message suffix for debugging logging. */
        public static final String INDENTATION_DEBUG   = "[DEBUG]   " ;

        // default values
        private static boolean DEBUG = false; // verbose on/off
        private static boolean DOWN = false; // logger off/on

        /**
         * Enable/disable verbose printings (has the same effect as {@link #showDebugs(boolean)}).
         * @param d set to <code>true</code> for being verbose.
         *          Otherwise, only errors and warnings will be printed.
         */
        public void showDebug(boolean d){
            DEBUG = d;
        }

        /**
         * Enable/disable verbose printings (has the same effect as {@link #showDebug(boolean)}).
         * @param d set to <code>true</code> for being verbose.
         *          Otherwise, only errors and warnings will be printed.
         */
        public static void showDebugs( boolean d){
            DEBUG = d;
        }

        /**
         * Dinable/Enable all the printings (has the same effect as {@link #shutDownLoggers(boolean)}).
         * @param shutDown set to <code>true</code> to disable all printing.
         *                 Otherwise, prints based on {@link #showDebug(boolean)}.
         */
        public void shutDownLogger(boolean shutDown){
            DOWN = shutDown;
        }

        /**
         * Dinable/Enable all the printings (has the same effect as {@link #shutDownLogger(boolean)}).
         * @param shutDown set to <code>true</code> to disable all printing.
         *                 Otherwise, prints based on {@link #showDebugs(boolean)}.
         */
        public static void shutDownLoggers(boolean shutDown){
            DOWN = shutDown;
        }

        /**
         * calls {@link #logERROR(Exception)}.
         * @param e to exception to deal with.
         */
        @Override
        public void logError( Exception e){
            logERROR( e);
        }
        /**
         * calls {@link #logERROR(Object)}.
         * @param msg print <code>msg.{@link Object#toString()}</code> in error mode.
         */
        @Override
        public void logError( Object msg){
            logERROR( msg);
        }
        /**
         * calls {@link #logWARNING(Object)}.
         * @param msg print <code>msg.{@link Object#toString()}</code> in warning mode.
         */
        @Override
        public void logWarning(Object msg){
            logWARNING( msg);
        }
        /**
         * calls {@link #LOG(Object)}.
         * @param msg print <code>msg.{@link Object#toString()}</code> in warning mode.
         */
        @Override
        public void log( Object msg){
            LOG( msg);
        }

        /**
         * Static helper for unifier logging in interface debugging.
         * See debugging flags: {@link #showDebugs(boolean)}, {@link #shutDownLoggers(boolean)}
         * @param e the exception to be notified
         */
        public static void logERROR(Exception e){
            if( ! DOWN)
                e.printStackTrace();
        }
        /**
         * Static helper for unifier logging in interface debugging.
         * See debugging flags: {@link #showDebugs(boolean)}, {@link #shutDownLoggers(boolean)}
         * @param msg the error message to notify.
         */
        public static void logERROR( Object msg){
            log( true, msg);
        }
        /**
         * Static helper for unifier logging in interface debugging.
         * See debugging flags: {@link #showDebugs(boolean)}, {@link #shutDownLoggers(boolean)}
         * @param msg the warning message to notify.
         */
        public static void logWARNING(Object msg){
            log( null, msg);
        }
        /**
         * Static helper for unifier logging in interface debugging.
         * See debugging flags: {@link #showDebugs(boolean)}, {@link #shutDownLoggers(boolean)}
         * @param msg the warning message to notify.
         */
        public static void LOG( Object msg){
            log( false, msg);
        }

        // set isError as: (false->DEBUG)(null->WARNING)(true->ERROR)
        private static void log( Boolean isError, Object msg){
            if( ! DOWN) {
                if (isError == null){
                    if (DEBUG)
                        System.out.println(INDENTATION_WARNING + msg.toString()); // warning
                } else if (isError)
                    System.err.println(INDENTATION_ERROR + msg.toString()); // error
                else if (DEBUG)
                    System.out.println(INDENTATION_DEBUG + msg.toString()); // info
            }
        }

        // todo move here copy()
    }

}