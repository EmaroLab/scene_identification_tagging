package it.emarolab.scene_identification_tracking.semanticSceneLibrary.semanticInterface.test;

import it.emarolab.scene_identification_tracking.semanticSceneLibrary.semanticInterface.Base;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.semanticInterface.core.Semantic;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.semanticInterface.core.definition.Adef;

/**
 * Created by bubx on 28/03/17.
 */
interface Test< I extends Semantic.Ground<?,?>, T extends Semantic>
            extends Base {

    Semantic.Transitions doTest();

    I getInstance();
    void setInstance( I instance);

    T getToTest();
    void setToTest( T test);


    abstract class AtomTest< I extends Semantic.Ground<?,?>, S, T extends Adef<Y>,Y>
            extends SIBase
            implements Test<I,T>{

        protected I instance;
        protected T toTest;
        protected S semantic;


        public AtomTest(I instance, S semantic, T toTest) {
            this.instance = instance;
            this.semantic = semantic;
            this.toTest = toTest;
        }
        public AtomTest(I instance, S semantic) {
            this.instance = instance;
            this.semantic = semantic;
            this.toTest = getNewToTest();
        }

        @Override
        public I getInstance() {
            return instance;
        }
        @Override
        public void setInstance(I instance) {
            this.instance = instance;
        }


        public S getSemantic() {
            return semantic;
        }
        public void setSemantic(S semantic) {
            this.semantic = semantic;
        }

        abstract public T getNewToTest();
        abstract public Y getAtomToTest();
        abstract public Y getAtom2ToTest();

        @Override
        public T getToTest() {
            return toTest;
        }
        @Override
        public void setToTest(T toTest) {
            this.toTest = toTest;
        }

        // todo load ontology, toString
    }
}
