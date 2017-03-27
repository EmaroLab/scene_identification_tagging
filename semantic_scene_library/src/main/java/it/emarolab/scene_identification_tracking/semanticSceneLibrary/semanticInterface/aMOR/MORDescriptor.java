package it.emarolab.scene_identification_tracking.semanticSceneLibrary.semanticInterface.aMOR;

import it.emarolab.scene_identification_tracking.semanticSceneLibrary.semanticInterface.core.definition.Ddef;

/**
 * Created by bubx on 27/03/17.
 */
// those are axiom mapper that can eventually contains an axiom.
public interface MORDescriptor<I extends MORGround<?>, X extends MORAxiom<I,?,?>>
        extends Ddef<I,X>{

}
