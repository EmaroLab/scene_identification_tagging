package it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.semantic;

import it.emarolab.scene_identification_tracking.semanticSceneLibrary.Base;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.Semantics;

import java.util.HashSet;
import java.util.Set;

public interface SpatialSemantic<C,I,PI extends SpatialSemantic.SpatialItem<C,I>> extends Base {

    Set< PI> queryItems(); // todo check usefulness

    public interface SpatialDescriptor< O, I, C, P, OP extends SpatialProperty<P>, R extends SpatialRelation<C,I,P,OP>, T extends SpatialCollector<R>>
           extends Semantics.LinkedDescriptor<O,I,C,OP,R> { // SpatialRelation<C,I, ?  extends SpatialSemantic.SpatialItem<C,I>>

        T queryBehind(); // < T extends SpatialCollector<R>>
        T queryFront();
        T queryAbove();
        T queryBelow();
        T queryRight();
        T queryLeft();
        T queryAlongX();
        T queryAlongY();
        T queryAlongZ();
        T queryAlong(); // todo implemented here
        T queryCoaxial();
        T queryParallel();
        T queryPerpendicular();

        T querySpatialProperties(); // todo implemented here
    }

    abstract class SpatialCollector< T extends SpatialRelation<?,?,?,?>> extends  HashSet< T>{}

    abstract class SpatialRelation<C,I,P, OP extends SpatialProperty<P>>
            extends Base.SITBase
            implements SpatialSemantic {
        private I subject; // todo rename to subjects
        private C type; // shape
        private OP relation;
        private Set<SpatialItem<C,I>> items;

        public SpatialRelation(I subject, C type) { // you shouldcall queryItem()
            this.subject = subject;
            this.type = type;
        }

        public SpatialRelation(SpatialRelation<C, I, P, OP> copy){
            this.subject = copy.subject;
            this.type = copy.type;
            this.relation = (OP) copy.relation.copy();
            this.items = new HashSet<>( copy.items);
        }


        public void setRelation(P relation) {
            this.relation.setProperty(relation);
        }
        // todo check for usage
        public void setInverseProperty(P inverseProperty) {
            this.relation.setInverseProperty( inverseProperty);
        }
        public void setSymmetric() {
            relation.setSymmetric();
        }

        public P getInverseRelations() {
            return relation.getInverseProperty();
        }
        public P getRelation() {
            return relation.getProperty();
        }
        public boolean isSymmetric() {
            return relation.isSymmetric();
        }

        public OP getSceneRelation(){
            return relation;
        }
        public void setSceneRelation( OP sceneRelation){
            this.relation = sceneRelation;
        }

        protected void setType(C type) {
            this.type = type;
        }
        public void setItems(Set<SpatialItem<C,I>> items) {
            this.items = items;
        }
        public abstract void addItems( Set< ? extends SpatialItem<C,I>> items);
        public void addItems( SpatialItem<C,I> item){
            HashSet<SpatialItem<C,I>> toAdd = new HashSet<SpatialItem<C,I>>();
            toAdd.add( item);
            addItems( item);
        }


        public void setSubject( I object) {
            this.subject = object;
            //this.items = queryItems(); // should manage also the type???
        }

        public I getSubject() {
            return subject;
        }
        public C getType() {
            return type;
        }
        public Set<SpatialItem<C,I>> getItems() {
            return items;
        }
        public Set<I> getItemsPrimitive(){
            Set<I> out = new HashSet<>();
            for( SpatialItem<C,I> i : items){
                out.add( i.getObject());
            }
            return out;
        }

        // consider only subject and relation
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof SpatialRelation)) return false;
            SpatialRelation<?, ?, ?, ?> that = (SpatialRelation<?, ?, ?, ?>) o;

            if( that.getItems().contains( this.getSubject()) &
                    that.getInverseRelations().equals( this.getInverseRelations()))
                return true;
            if( this.getItems().contains( that.getSubject()) &
                    this.getInverseRelations().equals( that.getInverseRelations()))
                return true;
            return false;
        }
        @Override
        public int hashCode() {
            int result = getSubject() != null ? getSubject().hashCode() : 0;
            result = 31 * result + (getRelation() != null ? getRelation().hashCode() : 0);
            return result;
        }
        // public Set< SpatialRelation<C,I,P>> asSet(){
        //    SpatialRelations<C,I,P> out = new SpatialRelations<C,I,P>();
        //    out.add( this);
        //    return out;
        //}
    }
    interface SpatialProperty<OP> extends Base {

        void setProperty( OP property);
        void setInverseProperty( OP inverseProperty);
        void setSymmetric();
        default void setSymmetric( OP property){
            setProperty( property);
            setSymmetric();
        }

        OP getProperty();
        OP getInverseProperty();
        boolean isSymmetric();
    }
    public interface SpatialItem<C, I> extends Base {
        I getObject();
        C getType();
    }

}