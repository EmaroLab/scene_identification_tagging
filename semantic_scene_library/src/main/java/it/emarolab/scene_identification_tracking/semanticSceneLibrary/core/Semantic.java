package it.emarolab.scene_identification_tracking.semanticSceneLibrary.core;

import java.util.Set;

// describe the semantics of data that can be synchronised with an ontology
public interface Semantic<O,I,X extends Semantic.Atom> {

    X query( O ontology, I instance); // retrieve from ontology

    void set( X x); // set this semantic descriptor
    X get(); // get this semantic descriptor


    interface Axiom<O,I,Y extends Semantic.Atom> extends Semantic<O,I,Y>{
        interface Type<O,I,Y extends Atom.Family<?>> extends Axiom<O,I,Y>{
        }

        interface Hierarchy<O,I,Y> extends Semantic<O,I,Atom.Node<Y>>{
        }

        interface Class<O,I,E,C> extends Semantic<O,I,Set<Atom.Container<E,C>>>{
        }

        interface Property<O,I,P,V> extends Semantic<O,I,Atom.Connector<P,V>>{
        }

        interface MultiProperty<O,I,P,V> extends Semantic<O,I,Set<Atom.Connector<P,V>>>{
        }

        interface Property3D<O,I,P,V> extends Semantic<O,I,Atom.Connector3D<P,V>>{
        }
    }
    
    interface Atom{

        interface Family<Y> extends Atom{
            Set<Y> getParents();
            void setParents( Set<Y> parents);
            default boolean hasParents(){
                if ( getParents() == null)
                    return false;
                if( getParents().isEmpty())
                    return false;
                return true;
            }
        }

        interface Node<Y> extends Family<Y> {
            Set<Y> getChildren();
            void setChildren( Set<Y> children);
            default boolean hasChildren(){
                if ( getParents() == null)
                    return false;
                if( getParents().isEmpty())
                    return false;
                return true;
            }
        }

        interface Container<E,C> extends Atom{
            E getExpression();
            void setExpression( E e);

            C getRange(); // codominio ( owl classes)
            void setRange( C r);

            int getCardinality();
            void setCardinality( int cardinality);
        }

        interface Connector<P,V> extends Atom{
            P getProperty();
            void setProperty( P p);

            V getValue();
            void setValue( V v);
        }

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
