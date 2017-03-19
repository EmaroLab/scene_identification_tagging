package it.emarolab.scene_identification_tracking.semanticSceneLibrary.core;

import java.util.Set;

// todo ground with SITBase


public interface Semantic<O,I,A extends Semantic.Axiom> {

    void set( A atom); // set this semantic descriptor
    A get(); // get this semantic descriptor

    A query( O ontology, I instance); // retrieve from ontology
    void add( O ontology, I instance, A atom);
    void remove( O ontology, I instance, A atom);

    interface Type<O,I,A extends Axiom.Family<?>>
            extends Semantic<O,I,A>{
        @Override
        default void add(O ontology, I instance, A atom){
            if ( atom.hasParents())
                for ( Object y : atom.getParents())
                    add( ontology, instance, y);
        }
        <Y> void add(O ontology, I instance, Y type);

        @Override
        default void remove(O ontology, I instance, A atom){
            if ( atom.hasParents())
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
                    addParents( ontology, instance, y);
            if ( atom.hasChildren())
                for ( Object y : atom.getChildren())
                    addChildren( ontology, instance, y);
        }
        <Y> void addParents(O ontology, I instance, Y type);
        <Y> void addChildren(O ontology, I instance, Y type);

        @Override
        default void remove(O ontology, I instance, A atom){
            if ( atom.hasParents())
                for ( Object y : atom.getParents())
                    removeParents( ontology, instance, y);
            if ( atom.hasChildren())
                for ( Object y : atom.getChildren())
                    removeChildren( ontology, instance, y);
        }
        <Y> void removeParents(O ontology, I instance, Y type);
        <Y> void removeChildren(O ontology, I instance, Y type);
    }





    interface Axiom{

        boolean exists();

        interface Family<Y>
                extends Axiom {

            Set<Y> getParents();

            default boolean hasParents(){
                if (getParents() == null)
                    return false;
                if (getParents().isEmpty())
                    return false;
                return true;
            }

            @Override
            default boolean exists() {
                return hasParents();
            }
        }
        // todo add FamilySet

        interface Node<Y>
                extends Family<Y> {
            Set<Y> getChildren();

            default boolean hasChildren() {
                if (getParents() == null)
                    return false;
                if (getParents().isEmpty())
                    return false;
                return true;
            }

            @Override
            default boolean exists() {
                return Family.super.exists() & hasChildren();
            }
        }
    }
}

// describe the semantics of data that can be synchronised with an ontology
/*public interface Semantic<O,I,A extends Semantic.Axiom> {

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
            for ( Object y : atom.getParents())
                add( ontology, instance, y);
        }
        <Y> void add(O ontology, I instance, Y type);

        @Override
        default void remove(O ontology, I instance, A atom){
            for ( Object y : atom.getParents())
                remove( ontology, instance, y);
        }
        <Y> void remove(O ontology, I instance, Y type);
    }

    interface Hierarchy<O,I,A extends Axiom.Node<?>>
            extends Semantic<O,I,A>{
        @Override
        default void add(O ontology, I instance, A atom){
            for ( Object y : atom.getParents())
                addParents( ontology, instance, y);
            for ( Object y : atom.getChildren())
                addChildren( ontology, instance, y);
        }
        <Y> void addParents(O ontology, I instance, Y type);
        <Y> void addChildren(O ontology, I instance, Y type);

        @Override
        default void remove(O ontology, I instance, A atom){
            for ( Object y : atom.getParents())
                removeParents( ontology, instance, y);
            for ( Object y : atom.getChildren())
                removeChildren( ontology, instance, y);
        }
        <Y> void removeParents(O ontology, I instance, Y type);
        <Y> void removeChildren(O ontology, I instance, Y type);
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

        boolean exists();

        interface Family<Y>
                extends Axiom {
            Set<Y> getParents();

            void setParents(Set<Y> parents);

            default boolean hasParents(){
                if (getParents() == null)
                    return false;
                if (getParents().isEmpty())
                    return false;
                return true;
            }

            @Override
            default boolean exists() {
                return hasParents();
            }
        }
        interface FamilySet<A extends Family<?>>
                extends Collection<A>, Axiom {
            Collection<A> getSet();

            @Override
            default boolean exists(){
                if( getSet() == null)
                    return false;
                return ! isEmpty();
            }
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

            @Override
            default boolean exists() {
                return Family.super.exists() & hasChildren();
            }
        }
        interface NodeSet<A extends Node<?>>
                extends Collection<A>, Axiom {
            Collection<A> getSet();

            @Override
            default boolean exists(){
                if( getSet() == null)
                    return false;
                return ! isEmpty();
            }
        }

        interface Container<E,C>
                extends Axiom {
            E getExpression();
            void setExpression( E e);

            C getRange(); // codominio ( owl classes)
            void setRange( C r);

            Integer getCardinality();
            void setCardinality( Integer cardinality);

            @Override
            default boolean exists(){
                if (getExpression() == null | getRange() == null | getCardinality() == null)
                    return false;
                return true;
            }
        }
        interface ContainerSet<A extends Container<?,?>>
                extends Collection<A>, Axiom {
            Collection<A> getSet();

            @Override
            default boolean exists(){
                if( getSet() == null)
                    return false;
                return ! isEmpty();
            }
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

            @Override
            default boolean exists(){
                if( getProperty() == null | getValue() == null)
                    return false;
                return true;
            }
        }
        interface ConnectorSet<A extends Connector<?,?>>
                extends Collection<A>, Axiom {
            Collection<A> getSet();

            @Override
            default boolean exists(){
                if( getSet() == null)
                    return false;
                return ! isEmpty();
            }
        }

        interface CardinalityConnector<P,V>
                extends Connector<P,V>{
            Integer getCardinality();
            void setCardinality( Integer cardinality);

            @Override
            default boolean exists() {
                if ( ! Connector.super.exists())
                    return false;
                if ( getCardinality() == null)
                    return false;
                return true;
            }
        }
        interface CardinalityConnectorSet<A extends CardinalityConnector<?,?>>
                extends  Collection<A>, Axiom {
            Collection<A> getSet();

            @Override
            default boolean exists(){
                if( getSet() == null)
                    return false;
                return ! isEmpty();
            }
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
            @Override
            default boolean exists() {
                if ( hasXelement() & hasYelement() & hasZelement())
                    return true;
                return false;
            }
        }
    }
}*/

