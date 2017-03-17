package it.emarolab.scene_identification_tracking.semanticSceneLibrary.core;

import java.util.Collection;
import java.util.Set;

// describe the semantics of data that can be synchronised with an ontology
public interface Semantic<O,I,A extends Semantic.Atom<?>> {

    A query( O ontology, I instance); // retrieve from ontology

    void set( A atom); // set this semantic descriptor
    A get(); // get this semantic descriptor


    interface Axiom<O,I,A extends Semantic.Atom<?>> extends Semantic<O,I,A>{
        interface Type<O,I,A extends Atom.Family<?>> extends Axiom<O,I,A>{
        }

        //interface Hierarchy<O,I,A> extends Semantic<O,I,Atom.Node<A>>{
        //}

        interface Class<O,I,E,C> extends Semantic<O,I,Atom2.ConnectorSet<E,C>>{
        }

        interface Property<O,I,A extends Atom2.Connector<?,?>> extends Axiom<O,I,A>{
        }

        interface MultiProperty<O,I,P,V> extends Semantic<O,I,Atom2.ConnectorSet<P,V>>{
        }

        interface Property3D<O,I,P,V> extends Semantic<O,I,Atom2.Connector3D<P,V>>{
        }
    }

    interface Atom<Y> {

        interface Family<Y> extends Atom<Y> {
            Set<Y> getParents();

            void setParents(Set<Y> parents);

            default boolean hasParents() {
                if (getParents() == null)
                    return false;
                if (getParents().isEmpty())
                    return false;
                return true;
            }
        }
        interface FamilySet<Y> extends Collection<Family<Y>>, Atom<Y>{}

        interface Node<Y> extends Family<Y> {
            Set<Y> getChildren();

            void setChildren(Set<Y> children);

            default boolean hasChildren() {
                if (getParents() == null)
                    return false;
                if (getParents().isEmpty())
                    return false;
                return true;
            }
        }
        interface NodeSet<Y> extends Collection<Node<Y>>, Atom<Y> {}
    }
    interface Atom2<Z,Y> extends Atom<Y> {
        interface Container<E,C> extends Atom2<E,C>{
            E getExpression();
            void setExpression( E e);

            C getRange(); // codominio ( owl classes)
            void setRange( C r);

            int getCardinality();
            void setCardinality( int cardinality);
        }
        interface ContainerSet<E,C> extends Collection<Container<E,C>>, Atom2<E,C>{}

        interface Connector<P,V> extends Atom2<P,V>{
            P getProperty();
            void setProperty( P p);

            V getValue();
            void setValue( V v);
        }
        interface ConnectorSet<P,V> extends Collection<Connector<P,V>>, Atom2<P,V>{}

        interface Connector3D<P,V> extends Connector<P,V> {
            P getXproperty();
            void setXproperty( P p);
            V getXvalue();
            void setXvalue( V v);

            P getYproperty();
            void setYproperty( P p);
            V getYvalue();
            void setYvalue( V v);

            P getZproperty();
            void setZproperty( P p);
            V getZvalue();
            void setZvalue( V v);
        }
    }
}
