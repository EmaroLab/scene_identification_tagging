package it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.representation;

import it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.Semantic;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.synchronisation.Descriptor;

/**
 * Created by bubx on 16/03/17.
 */
public interface Scenario<O,I> extends Descriptor<O,I,Semantic<O,I,?>> {

    int getCardinality();

    interface Scene<O,I> extends Scenario<O,I>{

    }

    interface Template<O,I> extends Scenario<O,I>{

    }
}
