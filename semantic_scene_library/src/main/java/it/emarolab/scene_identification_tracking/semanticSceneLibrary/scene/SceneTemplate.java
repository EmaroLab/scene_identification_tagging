package it.emarolab.scene_identification_tracking.semanticSceneLibrary.scene;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.Semantics;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.aMORDescriptor.MORClassDescriptor;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.aMORDescriptor.MORSpatialDescriptor;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.aMORDescriptor.SimpleSWRLInterface;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.objects.aMOR.MORPrimitive;
import org.semanticweb.owlapi.model.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


public class SceneTemplate extends MORClassDescriptor.MORSimpleClassSceneDescriptor
        implements Semantics {

    public static final String DEFAULT_BASE_NAME = "SL-";
    public static final boolean DEFAULT_BASE_NAME_WITH_TIME = true;
    public static final String DEFAULT_DATA_FORMATTING = "dd_MM_yy-HH:mm:ss.SSS"; // todo make it settable
    private static final double DEFAULT_CONFIDENCE_THRESHOLD = .8;
    public static final boolean DEFAULT_LEARN_SWRL = true;

    private String baseName; // all constrcutors must call setIntance(String, Boolean)
    private Boolean nameWithTime;
    private String dataFormatting = DEFAULT_DATA_FORMATTING;
    private double confidenceThreshold_MAIN = DEFAULT_CONFIDENCE_THRESHOLD;
    private boolean writeSWRl = DEFAULT_LEARN_SWRL;

    private SceneRepresentation.MORSpatialCollector rowScene = new SceneRepresentation.MORSpatialCollector( getOntology());
    private SceneTemplate learnedScened;

    //todo manage class name
    public SceneTemplate( SceneTemplate copy){
        super(copy);
        this.dataFormatting = copy.dataFormatting;
        this.confidenceThreshold_MAIN = copy.confidenceThreshold_MAIN;
        this.nameWithTime = copy.nameWithTime;
        setInstance( copy.baseName, nameWithTime);
        this.rowScene = new SceneRepresentation.MORSpatialCollector( copy.rowScene);
    }
    public SceneTemplate(String ontoName) {
        super(ontoName);
        setInstance( DEFAULT_BASE_NAME, DEFAULT_BASE_NAME_WITH_TIME);
    }
    public SceneTemplate(OWLReferences ontology) {
        super(ontology);
        setInstance( DEFAULT_BASE_NAME, DEFAULT_BASE_NAME_WITH_TIME);
    }
    public SceneTemplate(String ontoName, String instanceName) {
        super(ontoName);
        setInstance( instanceName, DEFAULT_BASE_NAME_WITH_TIME);
    }
    public SceneTemplate(OWLReferences ontology, String instanceName) {
        super(ontology, instanceName);
        setInstance( instanceName, DEFAULT_BASE_NAME_WITH_TIME);
    }
    public SceneTemplate(OWLReferences ontology, OWLClass instance) {
        super(ontology);
        setInstance( ontology.getOWLObjectName( instance), DEFAULT_BASE_NAME_WITH_TIME);
    }
    public SceneTemplate(String ontoName, String instanceName, boolean nameWithTime) {
        super(ontoName);
        setInstance( instanceName, nameWithTime);
    }
    public SceneTemplate(OWLReferences ontology, String instanceName, boolean nameWithTime) {
        super(ontology, instanceName);
        setInstance( instanceName, nameWithTime);
    }
    public SceneTemplate(OWLReferences ontology, OWLClass instance, boolean nameWithTime) {
        super(ontology);
        setInstance( ontology.getOWLObjectName( instance), nameWithTime);
    }

    // common initialiser
    public void setInstance(String base, boolean nameWithThime){
        this.nameWithTime = nameWithThime;
        this.baseName = base;
        setInstance( getNameWithTime(base, nameWithThime));
    }
    private String getNameWithTime( String base, boolean nameWithThime){
        if( nameWithThime)
            return base + new SimpleDateFormat(dataFormatting).format(new Date());
        return base;
    }

    public String getBaseName() {
        return baseName;
    }
    public void setBaseName(String baseName) {
        this.baseName = baseName;
    }

    public Boolean getNameWithTime() {
        return nameWithTime;
    }
    public void setNameWithTime(Boolean nameWithTime) {
        this.nameWithTime = nameWithTime;
    }

    public String getTimeFormatting() {
        return dataFormatting;
    }
    public void setDataFormatting(String dataFormatting) {
        this.dataFormatting = dataFormatting;
    }

    public double getConfidenceThreshold() {
        return confidenceThreshold_MAIN;
    }

    public boolean writesSWRl() {
        return writeSWRl;
    }
    public void shouldWriteSWRl(boolean writeSWRl) {
        this.writeSWRl = writeSWRl;
    }

    public LearningResults learn(SceneSemantics scene){
        LearningResults learned = new LearningResults( this, scene);
        return learned;
    }

    public Set<MORSpatialDescriptor.MORSpatialRelation> getRowScene() {
        return rowScene;
    }

    public class LearningResults{

        private int individualCardinality, conceptCardinality;
        private double confidence, confidenceThreshold;

        public LearningResults(SceneTemplate that, SceneSemantics scene) {
            this.conceptCardinality = that.getCardinality().computeTotal().getCardinality();
            this.individualCardinality = scene.getCardinality().computeTotal().getCardinality();
            this.confidence = computeConfidence();
            this.confidenceThreshold = that.getConfidenceThreshold();

            for(MORPrimitive p : scene.getObjects())
                rowScene.addAll( p.getSemantics().querySpatialProperties());

            learnedScened = new SceneTemplate(that);
            learnedScened.setCardinality( new SceneRepresentation.SceneCardinality( scene.getCardinality()));
        }
        private double computeConfidence(){
            if( conceptCardinality == 0)
                return 1;
            return individualCardinality / conceptCardinality; // € [0,1]
        }

        public double getConfidenceThreshold() {
            return confidenceThreshold;
        }
        public void setConfidenceThreshold(double confidenceThreshold) {
            this.confidenceThreshold = confidenceThreshold;
        }

        public int getIndividualCardinality() {
            return individualCardinality;
        }
        public int getConceptCardinality() {
            return conceptCardinality;
        }
        public double getConfidence() {
            return confidence;
        }
        public SceneTemplate getLearnedScene() {
            return learnedScened;
        }
        public boolean shouldBeLearned() {
            return confidence > confidenceThreshold;
        }

        public Set<MORSpatialDescriptor.MORSpatialRelation> getSpatialScene() {
            return rowScene;
        }

        @Override
        public String toString() {
            return "learningResults{" +
                    "individualCardinality=" + individualCardinality +
                    ", rowScene=" + shouldBeLearned() +
                    ", conceptCardinality=" + conceptCardinality +
                    ", confidence=" + confidence +
                    ", learnedScened=" + learnedScened +
                    '}';
        }
    }

    @Override
    public WritingState writeSemantics() {
        WritingState state = writeSubInstances();
        state = state.merge( writeSuperInstances());
        state = state.merge( writeSceneCardinality());
        if ( writesSWRl())
            state = state.merge( writeSWRL());
        return state;
    }

    // todo override
    private WritingState writeSWRL() {
        WritingState state = new WritingState();
        SimpleSWRLInterface.SWRLManager swrl = new SimpleSWRLInterface.SWRLManager( getOntology());
        Set< SymetricChecker> sym = new HashSet<>();
        for( MORSpatialDescriptor.MORSpatialRelation a : getRowScene()) {
            for (MORSpatialDescriptor.MORSpatialItem i : a.getItems()) {
                String var1 = a.getObjectName();
                String var2 = i.getObjectName();

                sym.add( new SymetricChecker( var2, var1, a.getInverseProperty())); // todo check if it is correct
                if( ! sym.contains( new SymetricChecker( var1, var2, a.getInverseProperty()))) {

                    // add C(?objectVar)
                    SWRLVariable objectVar = swrl.getVariable("x" + var1);
                    swrl.addClassCondition(a.getType(), objectVar);
                    // add Cj(?pj)
                    SWRLVariable itemVar = swrl.getVariable("x" + var2);
                    swrl.addClassCondition(i.getType(), itemVar);
                    // add psi+(?si, ?pi+)
                    OWLObjectProperty sceneProp = a.getProperty();
                    SWRLObjectPropertyAtom dataRule = getOntology().getOWLFactory().getSWRLObjectPropertyAtom(sceneProp, objectVar, itemVar);
                    swrl.getAntecedent().add(dataRule);
                }
            }
        }
        SWRLIndividualArgument s = swrl.getConst(getOntology().getOWLIndividual("S"));
        swrl.addClassInferation( getInstance(), s);
        swrl.addRoole();
        return state;
    }
    private class SymetricChecker{
        private String var1, var2;
        private OWLObjectProperty property;

        public SymetricChecker(String var1, String var2, OWLObjectProperty property) {
            this.var1 = var1;
            this.var2 = var2;
            this.property = property;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof SymetricChecker)) return false;

            SymetricChecker that = (SymetricChecker) o;

            if( this.var1.equals( that.var2) &
                    this.property.equals(that.property) &
                    this.var2.equals( that.var1))
                return true;

            if( this.var2.equals( that.var1) &
                    this.property.equals(that.property) &
                    this.var1.equals( that.var2))
                return true;

            if( this.var1.equals( that.var1) &
                    this.property.equals(that.property) &
                    this.var2.equals( that.var2))
                return true;
            return false;
        }

        @Override
        public int hashCode() {
            int result = var1 != null ? var1.hashCode() : 0;
            result = 31 * result + (var2 != null ? var2.hashCode() : 0);
            result = 31 * result + (property != null ? property.hashCode() : 0);
            return result;
        }

        public String getVar1() {
            return var1;
        }

        public String getVar2() {
            return var2;
        }

        public OWLObjectProperty getProperty() {
            return property;
        }
    }

    @Override
    public ReadingState readSemantics() {
        ReadingState state = readSubInstances();
        state = state.merge( readSuperInstances());
        state = state.merge( readSceneCardinality());
        // write SWRL
        return state;
    }

    @Override
    public String toString() {
        return "SceneTemplate{" +
                "baseName='" + baseName + '\'' +
                ", nameWithTime=" + nameWithTime +
                ", dataFormatting='" + dataFormatting + '\'' +
                ", confidenceThreshold_MAIN=" + confidenceThreshold_MAIN +
                '}';
    }
}
