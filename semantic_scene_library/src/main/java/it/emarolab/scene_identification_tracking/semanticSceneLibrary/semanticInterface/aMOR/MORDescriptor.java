package it.emarolab.scene_identification_tracking.semanticSceneLibrary.semanticInterface.aMOR;

import it.emarolab.scene_identification_tracking.semanticSceneLibrary.semanticInterface.aMOR.MORGround.GroundIndividual;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.semanticInterface.core.Ddef;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.semanticInterface.core.Semantic;

/**
 * Created by bubx on 27/03/17.
 */
public interface MORDescriptor<I extends MORGround<?>, X extends MORAxiom<I,?,?>>
        extends Ddef<I,X>{

    class Literal
        extends SIBase
        implements Ddef.Data<GroundIndividual,MORAxiom.MORLittered>, MORDescriptor<GroundIndividual,MORAxiom.MORLittered>{

        MORAxiom.MORLittered littered = new MORAxiom.MORLittered();

        // todo constructor

        @Override
        public MORAxiom.MORLittered getAxiom() {
            return littered;
        }

        @Override
        public Semantic.Intents read(GroundIndividual instance) {
            return null;
        }

        @Override
        public Semantic.Intents write(GroundIndividual instance) {
            return null;
        }
    }

}
