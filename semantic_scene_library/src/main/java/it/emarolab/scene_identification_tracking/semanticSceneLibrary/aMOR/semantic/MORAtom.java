package it.emarolab.scene_identification_tracking.semanticSceneLibrary.aMOR.semantic;

import it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.Semantic;
import org.semanticweb.owlapi.model.OWLClass;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by bubx on 17/03/17.
 */
public interface MORAtom {


    class MORFamily implements Semantic.Atom.Family<OWLClass> {

        private Set<OWLClass> parents = new HashSet<>();

        public MORFamily(){}
        public MORFamily(Set<OWLClass> parent){
            this.parents = parent;
        }

        @Override
        public Set<OWLClass> getParents() {
            return parents;
        }

        @Override
        public void setParents(Set<OWLClass> parents) {
            this.parents = parents;
        }
    }

    class MORNode
            extends MORFamily
            implements Semantic.Atom.Node<OWLClass> {

        private Set<OWLClass> children = new HashSet<>();

        public MORNode(){
            super();
        }
        public MORNode(Set<OWLClass> parents, Set<OWLClass> children) {
            super( parents);
            this.children = children;
        }

        @Override
        public Set<OWLClass> getChildren() {
            return children;
        }

        @Override
        public void setChildren(Set<OWLClass> children) {
            this.children = children;
        }
    }

}
