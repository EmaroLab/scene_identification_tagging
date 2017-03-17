package it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.representation;

import it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.Semantic;

/**
 * Created by bubx on 16/03/17.
 */
public interface GeometricObject<O,I> extends Descriptor<O,I,Semantic<O,I,?>>{

    O getOntology();
    void setOntology( O o);

    I getInstance();
    void setInstance( I i);

    interface Primitive<O,I,DP,L> extends GeometricObject<O,I>{
        Semantic.Connector3DAtom<DP,L> getCenter();
    }

    interface Sphere<O,I,DP,L> extends Primitive<O,I,DP,L>{
        Semantic.ConnectorAtom<DP,L> getRadius();
    }

    interface Orientable<O,I,DP,L> extends Primitive<O,I,DP,L>{
        Semantic.Connector3DAtom<DP,L> getAxis();
    }

    interface Plane<O,I,DP,L> extends Primitive<O,I,DP,L>{
        Semantic.ConnectorAtom<DP,L> getHessian();
        // todo: add get border
    }

    interface Cone<O,I,DP,L> extends Primitive<O,I,DP,L>{
        Semantic.ConnectorAtom<DP,L> getRadius();
        Semantic.ConnectorAtom<DP,L> getHeight();
        Semantic.Connector3DAtom<DP,L> getPoint();
    }

    interface Cylinder<O,I,DP,L> extends Cone<O,I,DP,L>{
    }
}
