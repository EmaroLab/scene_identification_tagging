package it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.aMORDescriptor;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.amor.owlInterface.OWLReferencesInterface;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.Base;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.Semantics;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.Semantics.Descriptor;
import org.semanticweb.owlapi.model.*;

import java.util.HashSet;
import java.util.Set;

/**
 * The Basic interface form aMOR and the {@link Semantics} {@link Descriptor}s.
 * <p>
 *     Basic interface for managing an {@link OWLReferences} and its facilities.
 *     It takes care of refer to an existing ontology in aMOR from its {@code owlReferenceName},
 *     that must be instantiated into the {@link it.emarolab.amor.owlInterface.OWLReferencesInterface.OWLReferencesContainer}.
 *     This implementation it does not constrains the {@link Descriptor}'s
 *     {@code instance} to any types.
 * <p>
 *     Anyway, this files contains also an {@link SWRLManager} and the {@link MORLiteralDescriptor.MOR3DataProperty} interface,
 *     as well as two basic implementations. The {@link MORIndividualDescriptor},
 *     which considers an {@link OWLNamedIndividual} as the {@code instance},
 *     and the {@link MORClassDescriptor}, which describes an {@link OWLClass}.
 *
 * <div style="text-align:center;"><small>
 * <b>File</b>:        {@link it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.aMORDescriptor.MORDescriptor} <br>
 * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
 * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
 * <b>affiliation</b>: DIBRIS, EMAROLab, University of Genoa. <br>
 * <b>date</b>:        04/02/2017 <br>
 * </small></div>
 *
 * @see it.emarolab.amor.owlInterface.OWLReferencesInterface.OWLReferencesContainer
 * @see Semantics
 * @see Descriptor
 * @see Base.TryMap
 * @see Base.Vocabolary
 */
public interface MORDescriptor<I extends OWLObject> extends Semantics.Descriptor<OWLReferences, I>  {

    // [[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[    aMOR INTERFACE    ]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]
    boolean setInstance( String instanceName);

    default String getInstanceName() {
        return getOWLName(getInstance());
    }
    default String getOntologyName() {
        if ( getOntology() == null)
            return null;
        return getOntology().getReferenceName();
    }

    default boolean setOntology(String ontoName) { // the class may change
        if (ontoName == null) {
            Logger.logERROR(ontoName + "\" aMOR ontology cannot be refereed by: 'null' !");
            return false;
        }
        if (ontoName.isEmpty()) {
            Logger.logERROR(ontoName + "\" aMOR ontology cannot be refereed by an empty name !");
            return false;
        }
        if (!OWLReferencesInterface.OWLReferencesContainer.isInstance(ontoName)) {
            Logger.logERROR(ontoName + "\" aMORObject reference not found!");
            return false;
        }

        // actual setting in error free manner
        setOntology((OWLReferences) OWLReferencesInterface.OWLReferencesContainer.getOWLReferences(ontoName));
        return true;
    }

    default boolean setDescriptor( String ontoName, String instanceName){
        if( setOntology( ontoName))
            if( setInstance( instanceName))
                return true;
        logError( "Wrong MORDescription settings: " + ontoName + ", " + instanceName + ".");
        return false;
    }
    default boolean setDescriptor( OWLReferences ontoRef, I instance){
        if( setOntology( ontoRef))
            if( setInstance( instance))
                return true;
        logError( "Wrong MORDescription settings: "
                + ontoRef.getReferenceName() + ", " + getOWLName(instance) + ".");
        return false;
    }


    default <O extends OWLObject> String getOWLName(O obj) {
        if (obj == null)
            return "";
        return getOntology().getOWLObjectName(obj);
    }
    default Set<String> getOWLName(Set<? extends OWLObject> objs) {
        if (objs == null)
            return new HashSet<>();
        return getOntology().getOWLObjectName(objs);
    }

    default void synchroniseReasoner() {
        this.getOntology().synchronizeReasoner();
    }






    interface MORIndividualDescriptor extends MORDescriptor<OWLNamedIndividual> {
        @Override
        default boolean setInstance(String individualName) {
            if (individualName != null & getOntology() != null)
                this.setInstance(getOntology().getOWLIndividual(individualName));
            else {
                Logger.logWARNING("OWLIndividual not set for new name: " + individualName);
                return false;
            }
            return true;
        }
    }

    class MORIndividual extends Base.SITBase implements MORIndividualDescriptor {
        private OWLReferences ontoRef;
        private OWLNamedIndividual instance;

        public MORIndividual(MORIndividual copy) {
            this.ontoRef = copy.ontoRef;
            this.instance = copy.instance;
        }
        public MORIndividual() {
        }
        public MORIndividual(String ontoName) {
            this.setOntology( ontoName);
        }
        public MORIndividual(OWLReferences ontoRef) {
            this.setOntology( ontoRef);
        }
        public MORIndividual(OWLReferences ontoRef, OWLNamedIndividual instance) {
            this.setOntology( ontoRef);
            this.setInstance( instance);
        }
        public MORIndividual(String ontoRef, String instance) {
            this.setOntology( ontoRef);
            this.setInstance( instance);
        }

        /**
         * The object able to manipulate the ontology to set for this semantic descriptor.
         *
         * @param ontology the ontology to set
         * @return {@code false} if an error occurs, {@code true} otherwise
         */
        @Override
        public boolean setOntology(OWLReferences ontology) {
            if (ontology == null){
                logError( "Cannot set 'null' ontology in MORIndividualDescriptor.");
                return false;
            }
            this.ontoRef = ontology;
            return true;
        }

        /**
         * The instance describing a particular knowledge in the ontology.
         *
         * @param instance the semantic instance to set
         * @return {@code false} if an error occurs, {@code true} otherwise
         */
        @Override
        public boolean setInstance(OWLNamedIndividual instance) {
            if (instance == null){
                logError( "Cannot set 'null' individual in MORIndividualDescriptor.");
                return false;
            }
            this.instance = instance;
            return true;
        }

        /**
         * @return the object able to manipulating a semantic structure.
         */
        @Override
        public OWLReferences getOntology() {
            if (ontoRef == null)
                logWarning( "MORDescriptor is returning 'null' ontology.");
            return ontoRef;
        }

        /**
         * @return the instance that describes a particular knowledge in the ontology.
         */
        @Override
        public OWLNamedIndividual getInstance() {
            return instance;
        }

        /**
         * Writes the {@link #getInstance()} value into the {@code ontology}.
         *
         * @return the states of the writing operations.
         */
        @Override @Deprecated
        public Semantics.MappingTransitions writeInstance() {
            logWarning(LOGGING.MSG_OWL_MANAGED);
            //DebuggingIntent intent = new DebuggingIntent( getInstanceName(), "INSTANCE", LOGGING.MSG_OWL_MANAGED);
            //new Semantics.WritingState( intent);

            MORMappingIntent<OWLNamedIndividual, String, Void, Semantics.WritingState> intent = new MORMappingIntent<>
                    (MORIndividual.this, "EXISTS", new Semantics.WritingState());
            return new Semantics.MappingTransitions( intent);
        }

        /**
         * Reads the {@link #getInstance()} value into the {@code ontology}.
         *
         * @return the states of the writing operations
         */
        @Override @Deprecated
        public Semantics.MappingTransitions readInstance() {
            logWarning(LOGGING.MSG_OWL_MANAGED);
            //DebuggingIntent intent = new DebuggingIntent( getInstanceName(), "INSTANCE", LOGGING.MSG_OWL_MANAGED);
            //return new Semantics.ReadingState( intent);

            MORMappingIntent<OWLNamedIndividual, String, Void, Semantics.ReadingState> intent = new MORMappingIntent<>
                    (MORIndividual.this, "EXISTS",  new Semantics.ReadingState());
            return new Semantics.MappingTransitions( intent);
        }

        /**
         * Removes the {@link #getInstance()} value into the {@code ontology}.
         *
         * @return the states of the writing operations.
         */
        @Override
        public Semantics.MappingTransitions deleteInstance() {

            return new MORTryWrite< OWLNamedIndividual, String, Void>( this, "DELETE"){
                @Override
                protected Semantics.MappingTransitions giveAtry() {
                    getOntology().removeIndividual( MORIndividual.this.getInstance());
                    getNewIntent( "!€").getState().asRemoved();
                    return getStateTransitions();
                }
            }.perform();

            /*return new TryWrite( new DebuggingIntent( getInstanceName(), "deleteIndividual")){
                protected Semantics.WritingState giveAtry() {
                    MORMappingIntent<OWLNamedIndividual, String, Void> intent = new MORMappingIntent<>
                            (MORIndividual.this, "DELETE",  new Semantics.WritingState().asRemoved());
                    Semantics.MTransiton out = new Semantics.MTransiton( intent);

                    MORIndividualIntent intent = new MORIndividualIntent( MORIndividual.this, null);
                    propagateAtomDescription( intent);
                    getOntology().removeIndividual( getInstance());
                    return out; //new Semantics.WritingState( intent).asRemoved();
                }
            }.perform();*/
        }

        @Override // equals if have equal ontoNmae and Instance
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof MORDescriptor.MORIndividual)) return false;

            MORIndividual that = (MORIndividual) o;

            if (getOntologyName() != null ? !getOntologyName().equals(that.getOntologyName()) : that.getOntologyName() != null) return false;
            return getInstance() != null ? getInstance().equals(that.getInstance()) : that.getInstance() == null;
        }
        @Override
        public int hashCode() {
            int result = ontoRef != null ? ontoRef.hashCode() : 0;
            result = 31 * result + (getInstance() != null ? getInstance().hashCode() : 0);
            return result;
        }

        @Override
        public MORIndividual copy() {
            return new MORIndividual( this);
        }

        @Override
        public String toString() {
            return padString( getInstanceName(), LOGGING.LENGTH_NUMBER, true)
                    + "@" + padString( getOntologyName(), LOGGING.LENGTH_SHORT, false);
        }

    }





    class MORMappingIntent< I extends OWLObject, A, L, M extends Semantics.MappingState> extends Semantics.MappingIntent<I,A,L,M> {
        //class MORMappingIntent< I extends OWLObject, A, L,
        //M extends Semantics.MappingState> extends Semantics.MappingIntent< I, A, L, M> {

        protected OWLReferences ontology;

        public MORMappingIntent(MORDescriptor<I> descriptor, A actions, M state) {
            super( descriptor.getInstance(), actions, state);
            ontology = descriptor.getOntology();
        }
        public MORMappingIntent(MORMappingIntent copy) {
            super(copy);
            this.ontology = copy.ontology;
        }

        public String getInstanceName(){
            return ontology.getOWLObjectName( getInstance());
        }

        public String getAtomName(){
            if ( getAtom() instanceof OWLObject)
                return ontology.getOWLObjectName( (OWLObject) getAtom());
            return getAtom().toString();
        }
        public String getJavaValueName() {
            return getSemanticName( getJavaValue());
        }
        public String getSemanticValueName() {
            return getSemanticName( getSemanticValue());
        }
        private String getSemanticName( L value){
            if (value == null)
                return "null";
            if ( value instanceof Set){
                Set<String> out = new HashSet<>();
                for ( Object o : (HashSet<?>) value) {
                    if ( o instanceof OWLObject)
                        out.add(ontology.getOWLObjectName((OWLObject) o));
                    else out.add( o.toString());
                }
                return out.toString();
            }
            if ( value instanceof OWLObject)
                return ontology.getOWLObjectName( (OWLObject) value);
            return value.toString();
        }

        public OWLReferences getOntology() {
            return ontology;
        }



        @Override
        public String toString() {
/*            String out =  padString( "{"+ getStateType() + "-"+getDescription() + "} ", LOGGING.LENGTH, true)
                    + padString( getInstanceName(), LOGGING.LENGTH_NUMBER, true) + ".";
            if( getAtom() != null)
                out += getAtomName() + "(j:" + getJavaValueName() + ", s:" + getSemanticValueName() + ")";
            else out+= getAtomName();
            return out + "[" + getNewIntent() + "~" + getIncomingState() + "=" + getMergedState() + "]";
*/


            return toStringDescription() + toStringStates() + toStringAxioms();

        }

        @Override
        public String toStringAxioms(){
            String out = padString( getInstanceName(), LOGGING.LENGTH_NUMBER, true);
            if (getAtom() != null)
                if ( ! getAtom().equals( LOGGING.MSG_EMPTY))
                    out += "." + getAtomName();
            if ( getJavaValue() == null)
                out += "(j,";
            else out += "(j:" + getJavaValueName() + ", ";
            if ( getSemanticValue() == null)
                out += "s)";
            else out += "s:" + getSemanticValueName() + ")";
            return out;
        }

        @Override
        public MORMappingIntent<I,A,L,M> copy() {
            return new MORMappingIntent<>( this);
        }
    }

    class MORIndividualIntent< A extends OWLProperty, L extends  OWLObject, M extends Semantics.MappingState>
            extends MORMappingIntent< OWLNamedIndividual,A,L,M>{

        public MORIndividualIntent( MORDescriptor<OWLNamedIndividual> descriptor, A actions, M state) {
            super(descriptor, actions, state);
        }
        public MORIndividualIntent(MORMappingIntent copy) {
            super(copy);
        }

        @Override
        public String getAtomName(){
            return ontology.getOWLObjectName( getAtom());
        }
        @Override
        public String getJavaValueName(){
            return ontology.getOWLObjectName( getJavaValue());
        }
        @Override
        public String getSemanticValueName(){
            return ontology.getOWLObjectName( getSemanticValue());
        }

    }

    class MORTypedIntent<M extends Semantics.MappingState>
            extends MORMappingIntent< OWLNamedIndividual, String, Set<OWLClass>,M>{

        public MORTypedIntent(MORDescriptor<OWLNamedIndividual> descriptor, M state) {
            super(descriptor, LOGGING.INTENT_BELONG, state);
        }
        public MORTypedIntent(MORMappingIntent copy) {
            super(copy);
        }

        public Set< String> getJavaValuesName(){
            return ontology.getOWLObjectName( getJavaValue());
        }
        @Override
        public String getJavaValueName(){
            return getJavaValuesName().toString();
        }

        public Set<String> getSemanticValuesName(){
            return ontology.getOWLObjectName( getSemanticValue());
        }
        @Override
        public String getSemanticValueName(){
            return getSemanticValuesName().toString();
        }
    }

    class MORClassIntent<M extends Semantics.MappingState>
            extends MORMappingIntent< OWLClass, String, Set<OWLClass>, M>{

        public MORClassIntent(MORDescriptor<OWLClass> descriptor, M state) {
            super(descriptor, LOGGING.INTENT_BELONG, state);
        }
        public MORClassIntent(MORMappingIntent copy) {
            super(copy);
        }
    }





    interface MORClassDescriptor extends MORDescriptor<OWLClass> {

        @Override
        default boolean setInstance(String individualName) {
            if (individualName != null & getOntology() != null)
                this.setInstance(getOntology().getOWLClass(individualName));
            else {
                Logger.logWARNING("OWLIndividual not set for new name: " + individualName);
                return false;
            }
            return true;
        }
    }

    class MORClass extends Base.SITBase implements MORClassDescriptor {

        private OWLReferences ontoRef;
        private OWLClass instance;

        public MORClass(MORClass copy) {
            this.ontoRef = copy.ontoRef;
            this.instance = copy.instance;
        }
        public MORClass() {
        }
        public MORClass(String ontoName) {
            this.setOntology( ontoName);
        }
        public MORClass(OWLReferences ontoRef) {
            this.setOntology( ontoRef);
        }
        public MORClass(OWLReferences ontoRef, OWLClass instance) {
            this.setOntology( ontoRef);
            this.setInstance( instance);
        }
        public MORClass(String ontoRef, String instance) {
            this.setOntology( ontoRef);
            this.setInstance( instance);
        }


        /**
         * The object able to manipulate the ontology to set for this semantic descriptor.
         *
         * @param ontology the ontology to set
         * @return {@code false} if an error occurs, {@code true} otherwise
         */
        @Override
        public boolean setOntology(OWLReferences ontology) {
            if (ontology == null){
                logError( "Cannot set 'null' ontology in MORClassDescriptor.");
                return false;
            }
            this.ontoRef = ontology;
            return true;
        }

        /**
         * The instance describing a particular knowledge in the ontology.
         *
         * @param instance the semantic instance to set
         * @return {@code false} if an error occurs, {@code true} otherwise
         */
        @Override
        public boolean setInstance(OWLClass instance) {
            if (instance == null){
                logError( "Cannot set 'null' individual in MORClassDescriptor.");
                return false;
            }
            this.instance = instance;
            return true;
        }

        /**
         * @return the object able to manipulating a semantic structure.
         */
        @Override
        public OWLReferences getOntology() {
            return ontoRef;
        }

        /**
         * @return the instance that describes a particular knowledge in the ontology.
         */
        @Override
        public OWLClass getInstance() {
            return instance;
        }

        /**
         * Writes the {@link #getInstance()} value into the {@code ontology}.
         * @return the states of the writing operations.
         */
        @Override @Deprecated
        public Semantics.MappingTransitions writeInstance() {
            logWarning(LOGGING.MSG_OWL_MANAGED);

            MORMappingIntent<OWLClass, String, Void, Semantics.WritingState> intent = new MORMappingIntent<>
                    (MORClass.this, "EXISTS", new Semantics.WritingState());
            return new Semantics.MappingTransitions( intent);


            //DebuggingIntent intent = new DebuggingIntent( getInstanceName(), "INSTANCE", LOGGING.MSG_OWL_MANAGED);
            //return new Semantics.WritingState( intent);
        }

        /**
         * Reads the {@link #getInstance()} value into the {@code ontology}.
         * @return the states of the writing operations
         */
        @Override @Deprecated
        public Semantics.MappingTransitions readInstance() {
            logWarning(LOGGING.MSG_OWL_MANAGED);

            MORMappingIntent<OWLClass, String, Void, Semantics.ReadingState> intent = new MORMappingIntent<>
                    (MORClass.this, "EXISTS", new Semantics.ReadingState());
            return new Semantics.MappingTransitions( intent);


            //DebuggingIntent intent = new DebuggingIntent( getInstanceName(), "INSTANCE", LOGGING.MSG_OWL_MANAGED);
            //return new Semantics.ReadingState( intent);
        }

        /**
         * Removes the {@link #getInstance()} value into the {@code ontology}.
         * @return the states of the writing operations.
         */
        @Override
        public Semantics.MappingTransitions deleteInstance() {
            return new MORTryWrite< OWLClass, String, Void>( this, "DELETE"){
                @Override
                protected Semantics.MappingTransitions giveAtry() {
                    getOntology().removeClass( MORClass.this.getInstance());
                    getNewIntent( "!€").getState().asRemoved();
                    return getStateTransitions();
                }
            }.perform();

/*            return new TryWrite( new DebuggingIntent( getInstanceName(), "deleteCLASS")){
                @Override
                protected Semantics.WritingState giveAtry() {
                    MORClassIntent<Semantics.WritingState> intent = new MORClassIntent<>( MORClass.this);
                    propagateAtomDescription( intent);
                    getOntology().removeClass( MORClass.this.getInstance());
                    return new Semantics.WritingState( intent).asRemoved();
                }
            }.perform();*/
        }

        @Override
        public MORClass copy() {
            return new MORClass( this);
        }

        @Override // equal if OntoName and instance are equals
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof MORDescriptor.MORClass)) return false;

            MORClass that = (MORClass) o;

            if (getOntologyName() != null ? !getOntologyName().equals(that.getOntologyName()) : that.getOntologyName() != null) return false;
            return getInstance() != null ? getInstance().equals(that.getInstance()) : that.getInstance() == null;
        }
        @Override
        public int hashCode() {
            int result = ontoRef != null ? ontoRef.hashCode() : 0;
            result = 31 * result + (getInstance() != null ? getInstance().hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return padString( getInstanceName(), LOGGING.LENGTH_NUMBER, true)
                    + "@" + padString( getOntologyName(), LOGGING.LENGTH_SHORT, false);
        }
    }


    class SWRLManager {
        public Set<SWRLAtom> antecedent, postecedent;
        private String ontologyIri;
        private OWLReferences ontology;

        public SWRLManager(OWLReferences ontology) {
            this.ontology = ontology;
            antecedent = new HashSet<SWRLAtom>();
            postecedent = new HashSet<SWRLAtom>();
            ontologyIri = ontology.getIriOntologyPath().toString();
        }

        // to get a variable
        public SWRLVariable getVariable(String var) {
            return ontology.getOWLFactory().getSWRLVariable( IRI.create(ontologyIri + "#" + var));
        }

        public SWRLIndividualArgument getConst(OWLNamedIndividual individual) {
            return (ontology.getOWLFactory().getSWRLIndividualArgument(individual));
        }

        public void addRoole() {
            SWRLRule rule = ontology.getOWLFactory().getSWRLRule(antecedent, postecedent); //Collections.singleton(postecedent));
            ontology.applyOWLManipulatorChangesAddAxiom(rule);
        }

        // to look for individual into class (i.e. cl( ?var))
        private SWRLClassAtom addClassAtom(OWLClass cl, SWRLVariable var, Set<SWRLAtom> collection) {
            SWRLClassAtom classAtom = ontology.getOWLFactory().getSWRLClassAtom(cl, var);
            collection.add(classAtom);
            return classAtom;
        }

        // to look for individual into class (i.e. cl( ?var))
        private SWRLClassAtom addClassAtom(OWLClass cl, SWRLIndividualArgument var, Set<SWRLAtom> collection) {
            SWRLClassAtom classAtom = ontology.getOWLFactory().getSWRLClassAtom(cl, var);
            collection.add(classAtom);
            return classAtom;
        }

        public SWRLClassAtom addClassCondition(OWLClass cl, SWRLVariable var) {
            return addClassAtom(cl, var, antecedent);
        }

        protected SWRLClassAtom addClassCondition(OWLClass cl, SWRLIndividualArgument var) {
            return addClassAtom(cl, var, antecedent);
        }

        protected SWRLClassAtom addClassInferation(OWLClass cl, SWRLVariable var) {
            return addClassAtom(cl, var, postecedent);
        }

        public SWRLClassAtom addClassInferation(OWLClass cl, SWRLIndividualArgument var) {
            return addClassAtom(cl, var, postecedent);
        }

        // to look for the value of an object property (i.e. objProp())
        public SWRLObjectPropertyAtom addObjectPropertyAtom(SWRLVariable preInd, OWLObjectProperty prop, SWRLVariable postInd, Set<SWRLAtom> collection) {
            SWRLObjectPropertyAtom dataRule = ontology.getOWLFactory().getSWRLObjectPropertyAtom(prop, preInd, postInd);
            collection.add(dataRule);
            return dataRule;
        }

        protected SWRLObjectPropertyAtom addObjectPropertyCondition(SWRLVariable preInd, OWLObjectProperty prop, SWRLVariable postInd) {
            return addObjectPropertyAtom(preInd, prop, postInd, antecedent);
        }

        protected SWRLObjectPropertyAtom addObjectPropertyInferation(SWRLVariable preInd, OWLObjectProperty prop, SWRLVariable postInd) {
            return addObjectPropertyAtom(preInd, prop, postInd, postecedent);
        }

        // to look for the value of an object property (i.e. objProp())
        private SWRLObjectPropertyAtom addObjectPropertyAtom(SWRLIndividualArgument preInd, OWLObjectProperty prop, SWRLVariable postInd, Set<SWRLAtom> collection) {
            SWRLObjectPropertyAtom dataRule = ontology.getOWLFactory().getSWRLObjectPropertyAtom(prop, preInd, postInd);
            collection.add(dataRule);
            return dataRule;
        }

        public SWRLObjectPropertyAtom addObjectPropertyCondition(SWRLIndividualArgument preInd, OWLObjectProperty prop, SWRLVariable postInd) {
            return addObjectPropertyAtom(preInd, prop, postInd, antecedent);
        }

        protected SWRLObjectPropertyAtom addObjectPropertyInferation(SWRLIndividualArgument preInd, OWLObjectProperty prop, SWRLVariable postInd) {
            return addObjectPropertyAtom(preInd, prop, postInd, postecedent);
        }

        // to look for the value of an object property (i.e. objProp())
        private SWRLObjectPropertyAtom addObjectPropertyAtom(SWRLVariable preInd, OWLObjectProperty prop, SWRLIndividualArgument postInd, Set<SWRLAtom> collection) {
            SWRLObjectPropertyAtom dataRule = ontology.getOWLFactory().getSWRLObjectPropertyAtom(prop, preInd, postInd);
            collection.add(dataRule);
            return dataRule;
        }

        protected SWRLObjectPropertyAtom addObjectPropertyCondition(SWRLVariable preInd, OWLObjectProperty prop, SWRLIndividualArgument postInd) {
            return addObjectPropertyAtom(preInd, prop, postInd, antecedent);
        }

        protected SWRLObjectPropertyAtom addObjectPropertyInferation(SWRLVariable preInd, OWLObjectProperty prop, SWRLIndividualArgument postInd) {
            return addObjectPropertyAtom(preInd, prop, postInd, postecedent);
        }


        public Set<SWRLAtom> getAntecedent() {
            return antecedent;
        }

        public Set<SWRLAtom> getPostecedent() {
            return postecedent;
        }
    }


    abstract class MORTryRead<I extends OWLObject, A, L> extends TryMap<I,A,L,Semantics.ReadingState>{
        private MORDescriptor<I> descriptor;

        public MORTryRead(MORDescriptor<I> descriptor) {
            this.descriptor = descriptor;
        }
        public MORTryRead(MORDescriptor<I> descriptor, String atom) {
            super( descriptor.getInstanceName(), atom);
            this.descriptor = descriptor;
        }

        @Override
        public Semantics.ReadingState getNewState() {
            return new Semantics.ReadingState();
        }

        @Override @Deprecated
        public Semantics.MappingIntent<I,A,L,Semantics.ReadingState> getNewIntent(I instance, A atom) {
            return super.getNewIntent(instance, atom);
        }
        public Semantics.MappingIntent<I,A,L,Semantics.ReadingState> getNewIntent(A atom) {
            Semantics.MappingIntent<I,A,L,Semantics.ReadingState> intent = new MORMappingIntent( descriptor, atom, getNewState());
            propagateAtomDescription( intent);
            transiton.add( intent);
            return intent;
        }
    }
    abstract class MORTryWrite<I extends OWLObject, A, L> extends TryMap<I,A,L,Semantics.WritingState>{
        private MORDescriptor<I> descriptor;

        public MORTryWrite(MORDescriptor<I> descriptor) {
            this.descriptor = descriptor;
        }
        public MORTryWrite(MORDescriptor<I> descriptor, String atom) {
            super( descriptor.getInstanceName(), atom);
            this.descriptor = descriptor;
        }


        @Override
        public Semantics.WritingState getNewState() {
            return new Semantics.WritingState();
        }

        public Semantics.MappingIntent<I,A,L,Semantics.WritingState> getNewIntent(A atom) {
            Semantics.MappingIntent<I,A,L,Semantics.WritingState> intent = new MORMappingIntent( descriptor, atom, getNewState());
            propagateAtomDescription( intent);
            transiton.add( intent);
            return intent;
        }
        @Override @Deprecated
        public Semantics.MappingIntent<I,A,L,Semantics.WritingState> getNewIntent(I instance, A atom) {
            return super.getNewIntent(instance, atom);
        }
    }


}

