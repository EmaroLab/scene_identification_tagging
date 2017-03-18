package it.emarolab.scene_identification_tracking.semanticSceneLibrary.core;

import java.util.Collection;
import java.util.Set;

// todo ground with SITBase

// describe the semantics of data that can be synchronised with an ontology
public interface Semantic<O,I,A extends Semantic.Axiom> {

    void set( A atom); // set this semantic descriptor
    A get(); // get this semantic descriptor

    A query( O ontology, I instance); // retrieve from ontology
    void add( O ontology, I instance, A atom);
    void remove( O ontology, I instance, A atom);

    // todo: remove P from query, add & remove

    interface Type<O,I,A extends Axiom.Family<?>>
            extends Semantic<O,I,A>{
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

    interface Hierarchy<O,I,A extends Axiom.Node<?>>
            extends Semantic<O,I,A>{
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

    interface ClassRestriction<O,I,A extends Axiom.CardinalityConnectorSet<? extends Axiom.CardinalityConnector<?,?>>>
            extends Semantic<O,I,A>{
        @Override
        default void add(O ontology, I instance, A atom){
            for( Axiom.CardinalityConnector<?,?> a : atom)
                add(ontology,instance,a.getProperty(),a.getCardinality(),a.getValue());
        }
        <P,C> void add(O ontology, I instance, P property, int cardinality, C range);

        @Override
        default void remove(O ontology, I instance, A atom){
            for( Axiom.CardinalityConnector<?,?> a : atom)
                remove(ontology,instance,a.getProperty(),a.getCardinality(),a.getValue());
        }
        <P,C> void remove(O ontology, I instance, P property, int cardinality, C range);
    }

    interface Property<O,I,A extends Axiom.Connector<?,?>>
            extends Semantic<O,I,A>{
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

    interface MultiProperty<O,I,A extends Axiom.ConnectorSet<? extends Axiom.Connector<?,?>>>
            extends Semantic<O,I,A>{
        @Override
        default void add(O ontology, I instance, A atom){
            for( Axiom.Connector<?,?> a : atom.getSet())
                add(ontology,instance,a.getProperty(),a.getValue());
        }
        <P,V> void add( O ontology, I instance, P property, V value);

        @Override
        default void remove(O ontology, I instance, A atom){
            for( Axiom.Connector<?,?> a : atom.getSet())
                remove(ontology,instance,a.getProperty(),a.getValue());
        }
        <P,V> void remove( O ontology, I instance, P property, V value);
    }

    interface Property3D<O,I,A extends Axiom.Connector3D<?,?>>
            extends Semantic<O,I,A>{

        @Override
        default void add(O ontology, I instance, A atom){
            addX( ontology, instance, atom.getX());
            addY( ontology, instance, atom.getY());
            addZ( ontology, instance, atom.getZ());
        }

        default <P,V> void addX(O ontology, I instance, Axiom.Connector<P,V> connector){
            addX( ontology, instance, connector.getProperty(), connector.getValue());
        }
        <P,V> void addX(O ontology, I instance, P property, V value);

        default <P,V> void addY(O ontology, I instance, Axiom.Connector<P,V> connector){
            addY( ontology, instance, connector.getProperty(), connector.getValue());
        }
        <P,V> void addY(O ontology, I instance, P property, V value);

        default <P,V> void addZ(O ontology, I instance, Axiom.Connector<P,V> connector){
            addZ( ontology, instance, connector.getProperty(), connector.getValue());
        }
        <P,V> void addZ(O ontology, I instance, P property, V value);


        @Override
        default void remove(O ontology, I instance, A atom){
            removeX( ontology, instance, atom.getX());
            removeY( ontology, instance, atom.getY());
            removeZ( ontology, instance, atom.getZ());
        }

        default <P,V> void removeX( O ontology, I instance, Axiom.Connector<P,V> connector){
            removeX( ontology, instance, connector.getProperty(), connector.getValue());
        }
        <P,V> void removeX( O ontology, I instance, P property, V value);

        default <P,V> void removeY( O ontology, I instance, Axiom.Connector<P,V> connector){
            removeY( ontology, instance, connector.getProperty(), connector.getValue());
        }
        <P,V> void removeY( O ontology, I instance, P property, V value);

        default <P,V> void removeZ( O ontology, I instance, Axiom.Connector<P,V> connector){
            removeZ( ontology, instance, connector.getProperty(), connector.getValue());
        }
        <P,V> void removeZ( O ontology, I instance, P property, V value);
    }

    interface Axiom {

        interface Family<Y>
                extends Axiom {
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
                extends Collection<A>, Axiom {
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
                extends Collection<A>, Axiom {
            Collection<A> getSet();
        }

        interface Container<E,C>
                extends Axiom {
            E getExpression();
            void setExpression( E e);

            C getRange(); // codominio ( owl classes)
            void setRange( C r);

            Integer getCardinality();
            void setCardinality( Integer cardinality);

            default boolean hasElement(){
                if (getExpression() == null | getRange() == null | getCardinality() == null)
                    return false;
                return true;
            }
        }
        interface ContainerSet<A extends Container<?,?>>
                extends Collection<A>, Axiom {
            Collection<A> getSet();
        }

        interface Connector<P,V>
                extends Axiom {
            P getProperty();
            void setProperty( P p);

            V getValue();
            void setValue( V v);

            default void set( P p, V v){
                setProperty( p);
                setValue( v);
            }
            default <C extends Connector<P,V>> void set( C connector){
                set( connector.getProperty(), connector.getValue());
            }

            default boolean hasElement(){
                if( getProperty() == null | getValue() == null)
                    return false;
                return true;
            }
        }
        interface ConnectorSet<A extends Connector<?,?>>
                extends Collection<A>, Axiom {
            Collection<A> getSet();
        }

        interface CardinalityConnector<P,V>
                extends Connector<P,V>{
            Integer getCardinality();
            void setCardinality( Integer cardinality);

            @Override
            default boolean hasElement() {
                if ( ! Connector.super.hasElement())
                    return false;
                if ( getCardinality() == null)
                    return false;
                return true;
            }
        }
        interface CardinalityConnectorSet<A extends CardinalityConnector<?,?>>
                extends  Collection<A>, Axiom {
            Collection<A> getSet();
        }

        interface Connector3D<P,V>
                extends Axiom {

            Connector<P,V> getX();
            Connector<P,V> getY();
            Connector<P,V> getZ();

            default boolean hasXelement() {
                Connector<P, V> x = getX();
                if ( x.getProperty() == null | x.getValue() == null)
                    return false;
                return true;
            }
            default boolean hasYelement() {
                Connector<P, V> y = getY();
                if ( y.getProperty() == null | y.getValue() == null)
                    return false;
                return true;
            }
            default boolean hasZelement() {
                Connector<P, V> z = getZ();
                if ( z.getProperty() == null | z.getValue() == null)
                    return false;
                return true;
            }
            default boolean hasElement() {
                if ( hasXelement() & hasYelement() & hasZelement())
                    return true;
                return false;
            }
        }
    }
}
