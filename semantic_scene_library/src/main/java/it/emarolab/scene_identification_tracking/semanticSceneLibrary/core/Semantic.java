package it.emarolab.scene_identification_tracking.semanticSceneLibrary.core;

import java.util.Collection;
import java.util.Set;

// describe the semantics of data that can be synchronised with an ontology
public interface Semantic<O,I,A extends Semantic.Atom> {

    void set( A atom); // set this semantic descriptor
    A get(); // get this semantic descriptor

    A query( O ontology, I instance); // retrieve from ontology
    void add( O ontology, I instance, A atom);
    void remove( O ontology, I instance, A atom);

    interface Axiom<O,I,A extends Semantic.Atom> extends Semantic<O,I,A>{

        interface Type<O,I,A extends Atom.Family<?>>
                extends Axiom<O,I,A>{
            @Override
            default void add(O ontology, I instance, A atom){
                if (atom.hasParents())
                    for ( Object y : atom.getParents())
                        add( ontology, instance, y);
            }
            <Y> void add(O ontology, I instance, Y type);

            @Override
            default void remove(O ontology, I instance, A atom){
                if (atom.hasParents())
                    for ( Object y : atom.getParents())
                        remove( ontology, instance, y);
            }
            <Y> void remove(O ontology, I instance, Y type);
        }

        interface Hierarchy<O,I,A extends Atom.Node<?>>
                extends Axiom<O,I,A>{
            @Override
            default void add(O ontology, I instance, A atom){
                if ( atom.hasParents())
                    for ( Object y : atom.getParents())
                        addParent( ontology, instance, y);
                if ( atom.hasChildren())
                    for ( Object y : atom.getChildren())
                        addChild( ontology, instance, y);
            }
            <Y> void addParent(O ontology, I instance, Y type);
            <Y> void addChild(O ontology, I instance, Y type);

            @Override
            default void remove(O ontology, I instance, A atom){
                if ( atom.hasParents())
                    for ( Object y : atom.getParents())
                        removeParent( ontology, instance, y);
                if ( atom.hasChildren())
                    for ( Object y : atom.getChildren())
                        removeChild( ontology, instance, y);
            }
            <Y> void removeParent(O ontology, I instance, Y type);
            <Y> void removeChild(O ontology, I instance, Y type);
        }

        interface ClassRestriction<O,I,A extends Atom.CardinalityConnectorSet<? extends Atom.Connector<?,?>>>
                extends Axiom<O,I,A>{
            @Override
            default void add(O ontology, I instance, A atom){
                for( Atom.Connector<?,?> a : atom)
                    add(ontology,instance,a.getProperty(),a.getValue());
            }
            <P,C> void add(O ontology, I instance, P property, C range);

            @Override
            default void remove(O ontology, I instance, A atom){
                for( Atom.Connector<?,?> a : atom)
                    remove(ontology,instance,a.getProperty(),a.getValue());
            }
            <P,C> void remove(O ontology, I instance, P property, C range);
        }

        interface Property<O,I,A extends Atom.Connector<?,?>>
                extends Axiom<O,I,A>{
            @Override
            default void add(O ontology, I instance, A atom){
                add( ontology, instance, atom.getProperty(), atom.getValue());
            }
            <P,V> void add( O ontology, I instance, P property, V value);

            @Override
            default void remove(O ontology, I instance, A atom){
                remove( ontology, instance, atom.getProperty(), atom.getValue());
            }
            <P,V> void remove( O ontology, I instance, P property, V value);
        }

        interface MultiProperty<O,I,A extends Atom.ConnectorSet<? extends Atom.Connector<?,?>>>
                extends Axiom<O,I,A>{
            @Override
            default void add(O ontology, I instance, A atom){
                for( Atom.Connector<?,?> a : atom.getSet())
                    add(ontology,instance,a.getProperty(),a.getValue());
            }
            <P,V> void add( O ontology, I instance, P property, V value);

            @Override
            default void remove(O ontology, I instance, A atom){
                for( Atom.Connector<?,?> a : atom.getSet())
                    remove(ontology,instance,a.getProperty(),a.getValue());
            }
            <P,V> void remove( O ontology, I instance, P property, V value);
        }

        interface Property3D<O,I,A extends Atom.Connector3D<?,?>>
                extends Axiom<O,I,A>{
            @Override
            default void add(O ontology, I instance, A atom){
                add( ontology, instance, atom.getXproperty(), atom.getXvalue());
                add( ontology, instance, atom.getYproperty(), atom.getYvalue());
                add( ontology, instance, atom.getZproperty(), atom.getZvalue());
            }
            <P,V> void add( O ontology, I instance, P property, V value);

            @Override
            default void remove(O ontology, I instance, A atom){
                remove( ontology, instance, atom.getXproperty(), atom.getXvalue());
                remove( ontology, instance, atom.getYproperty(), atom.getYvalue());
                remove( ontology, instance, atom.getZproperty(), atom.getZvalue());
            }
            <P,V> void remove( O ontology, I instance, P property, V value);
        }
    }

    interface Atom {

        interface Family<Y>
                extends Atom {
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
        interface FamilySet<A extends Family<?>>
                extends Collection<A>, Atom{
            Collection<A> getSet();
        }

        interface Node<Y>
                extends Family<Y> {
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
        interface NodeSet<A extends Node<?>>
                extends Collection<A>, Atom {
            Collection<A> getSet();
        }

        interface Container<E,C>
                extends Atom{
            E getExpression();
            void setExpression( E e);

            C getRange(); // codominio ( owl classes)
            void setRange( C r);

            int getCardinality();
            void setCardinality( int cardinality);
        }
        interface ContainerSet<A extends Container<?,?>>
                extends Collection<A>, Atom{
            Collection<A> getSet();
        }

        interface Connector<P,V>
                extends Atom{
            P getProperty();
            void setProperty( P p);

            V getValue();
            void setValue( V v);
        }
        interface ConnectorSet<A extends Connector<?,?>>
                extends Collection<A>, Atom{
            Collection<A> getSet();
        }

        interface CardinalityConnector<P,V>
                extends Connector<P,V>{
            int getCardinality();
            void setCardinality( int cardinality);
        }
        interface CardinalityConnectorSet<A extends CardinalityConnector<?,?>>
                extends  Collection<A>, Atom{
            Collection<A> getSet();
        }

        interface Connector3D<P,V>
                extends Atom{
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
