package it.emarolab.scene_identification_tracking.semanticSceneLibrary.aMOR.semantic;

import it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.Semantic;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLLiteral;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by bubx on 17/03/17.
 */
public interface MORAxiom extends Semantic.Axiom{
    // todo add equal and toString

    class MORTyped
            implements Semantic.Axiom.Family<OWLClass>, MORAxiom {

        private Set<OWLClass> types = new HashSet<>();

        public MORTyped(){}
        public MORTyped(OWLClass parent){
            this.types.add( parent);
        }
        public MORTyped(Set<OWLClass> parents){
            this.types = parents;
        }

        @Override
        public Set<OWLClass> getParents() {
            return types;
        }

        @Override
        public String toString() {
            return "MORTyped{" +
                    "types=" + types +
                    '}';
        }
    }

    class MORHierarchised
            extends MORTyped
            implements Semantic.Axiom.Node<OWLClass>, MORAxiom {

        private Set<OWLClass> children = new HashSet<>();

        public MORHierarchised(){
            super();
        }
        public MORHierarchised(Set<OWLClass> parents, Set<OWLClass> children) {
            super( parents);
            this.children = children;
        }
        public MORHierarchised( OWLClass parent, OWLClass child) {
            super();
            if ( parent != null)
                this.getParents().add( parent);
            if ( child != null)
                this.getChildren().add( child);
        }

        @Override
        public Set<OWLClass> getChildren() {
            return children;
        }

        @Override
        public String toString() {
            return super.toString() + "MORHierarchised{" +
                    "children=" + children +
                    '}';
        }
    }

    // todo make MORLinked
    class MORLiterised
        implements Semantic.Axiom.Atom<OWLLiteral>, MORAxiom{

        private OWLLiteral literal;

        public MORLiterised(){
        }
        public MORLiterised( OWLLiteral literal){
            this.literal = literal;
        }

        @Override
        public OWLLiteral getAtom() {
            return literal;
        }

        @Override
        public void setAtom(OWLLiteral literal) {
            this.literal = literal;
        }

        @Override
        public String toString() {
            return "MORLiterised{" +
                    "literal=" + literal +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof MORLiterised)) return false;

            MORLiterised that = (MORLiterised) o;

            return literal != null ? literal.equals(that.literal) : that.literal == null;
        }

        @Override
        public int hashCode() {
            return literal != null ? literal.hashCode() : 0;
        }
    }

    // todo make MORMultiLinked
    class MORMultiLiterised
        implements Semantic.Axiom.AtomSet< MORLiterised>, MORAxiom{

        private Collection< MORLiterised> literals = new HashSet<>();

        public MORMultiLiterised(){
        }
        public MORMultiLiterised( MORMultiLiterised literals){
            this.literals = literals;
        }
        public MORMultiLiterised( MORLiterised literal){
            this.literals.add( literal);
        }
        public MORMultiLiterised( Set<OWLLiteral> literals){
            for( OWLLiteral l : literals)
                this.literals.add( new MORLiterised( l));
        }
        public MORMultiLiterised( OWLLiteral literal){
            this.literals.add( new MORLiterised( literal));
        }

        @Override
        public int size() {
            return literals.size();
        }
        @Override
        public boolean isEmpty() {
            return literals.isEmpty();
        }
        @Override
        public boolean contains(Object o) {
            return literals.contains( o);
        }
        @Override
        public Iterator<MORLiterised> iterator() {
            return literals.iterator();
        }
        @Override
        public Object[] toArray() {
            return literals.toArray();
        }
        @Override
        public <T> T[] toArray(T[] a) {
            return literals.toArray( a);
        }
        @Override
        public boolean add( MORLiterised atom) {
            return literals.add( atom);
        }
        public void add(OWLLiteral owlLiteral) { // todo others??
            this.add( new MORLiterised( owlLiteral));
        }
        @Override @Deprecated
        public boolean remove(Object o) {
            return literals.remove( o);
        }
        public boolean remove(OWLLiteral literal) {
            return literals.remove( new MORLiterised( literal));
        }
        public boolean remove( MORLiterised literal) {
            return literals.remove( literal);
        }
        @Override
        public boolean containsAll(Collection<?> c) {
            return literals.containsAll( c);
        }
        @Override
        public boolean addAll(Collection<? extends MORLiterised> c) {
            return literals.addAll( c);
        }
        @Override
        public boolean removeAll(Collection<?> c) {
            return literals.retainAll( c);
        }
        @Override
        public boolean retainAll(Collection<?> c) {
            return literals.retainAll( c);
        }
        @Override
        public void clear() {
            literals.clear();
        }

        @Override
        public Collection< OWLLiteral> getAtoms() {
            Collection< OWLLiteral> out = new HashSet<>();
            for( MORLiterised l : this)
                out.add( l.getAtom());
            return out;
        }

        @Override
        public String toString() {
            return "MORMultiLiterised{" +
                    "literals=" + literals +
                    '}';
        }
    }

    // todo make MORLinked3D
    class MORLiterised3D
        implements Semantic.Axiom.Atom3D< MORLiterised>, MORAxiom{

        private MORLiterised x, y, z;

        public MORLiterised3D(){
        }
        public MORLiterised3D( MORLiterised x, MORLiterised y, MORLiterised z){
            setXYZ( x, y, z);
        }
        public MORLiterised3D( OWLLiteral x, OWLLiteral y, OWLLiteral z){
            setXYZ( x, y, z);
        }

        @Override
        public void setX(MORLiterised xAtom) {
            this.x = xAtom;
        }
        @Override
        public void setY(MORLiterised yAtom) {
            this.y = yAtom;
        }
        @Override
        public void setZ(MORLiterised zAtom) {
            this.z = zAtom;
        }

        public void setXYZ(OWLLiteral x, OWLLiteral y, OWLLiteral z) {
            setX( x);
            setY( y);
            setZ( z);
        }

        @Override
        public MORLiterised getX() {
            return x;
        }

        public void setX(OWLLiteral x) {
            setX( new MORLiterised( x));
        }

        @Override
        public MORLiterised getY() {
            return y;
        }

        public void setY(OWLLiteral y) {
            setY( new MORLiterised( y));
        }

        @Override
        public MORLiterised getZ() {
            return z;
        }

        public void setZ(OWLLiteral Z) {
            setZ( new MORLiterised( Z));
        }

        @Override
        public String toString() {
            return "MORLiterised3D{" +
                    "x=" + x +
                    ", y=" + y +
                    ", z=" + z +
                    '}';
        }
    }

    /*
    class MORTyped
            implements Semantic.Axiom.Family<OWLClass>, MORAxiom {

        private Set<OWLClass> parents = new HashSet<>();

        public MORTyped(){}
        public MORTyped(OWLClass parent){
            this.parents.add( parent);
        }
        public MORTyped(Set<OWLClass> parents){
            this.parents = parents;
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

    class MORLinked
            implements Semantic.Axiom.Connector<OWLObjectProperty,OWLNamedIndividual>, MORAxiom{

        private OWLObjectProperty property;
        private OWLNamedIndividual value;

        public MORLinked(){}
        public MORLinked(OWLObjectProperty property){
            this.property = property;
        }
        public MORLinked(OWLObjectProperty property, OWLNamedIndividual value){
            this.property = property;
            this.value = value;
        }
        public MORLinked(MORLinked linked) {
            this.property = linked.getProperty();
            this.value = linked.value;
        }

        @Override
        public OWLObjectProperty getProperty() {
            return property;
        }

        @Override
        public void setProperty(OWLObjectProperty property) {
            this.property = property;
        }

        @Override
        public OWLNamedIndividual getValue() {
            return value;
        }

        @Override
        public void setValue(OWLNamedIndividual individual) {
            this.value = value;
        }
    }
    class MORMultiLinked
            implements Semantic.Axiom.ConnectorSet<MORLinked>, MORAxiom {

        private Set<MORLinked> links = new HashSet<>();

        public MORMultiLinked(){
        }
        public MORMultiLinked(MORLinked v){
            links.add( v);
        }

        public Set<MORLinked> getSet(){
            return links;
        }
        @Override
        public int size() {
            return links.size();
        }
        @Override
        public boolean isEmpty() {
            return links.isEmpty();
        }
        @Override
        public boolean contains(Object o) {
            return links.contains( o);
        }
        @Override
        public Iterator<MORLinked> iterator() {
            return links.iterator();
        }
        @Override
        public Object[] toArray() {
            return links.toArray();
        }
        @Override
        public <T> T[] toArray(T[] a) {
            return links.toArray( a);
        }
        @Override
        public boolean add(MORLinked morLinkedValue) {
            return links.add( morLinkedValue);
        }
        public boolean add( OWLObjectProperty link, OWLNamedIndividual linkValue) {
            return links.add( new MORLinked( link, linkValue));
        }
        public void add(OWLObjectProperty property, Set<OWLNamedIndividual> values) {
            for( OWLNamedIndividual i : values)
                add( property, values);
        }
        @Override
        public boolean remove(Object o) {
            return links.remove( o);
        }
        @Override
        public boolean containsAll(Collection<?> c) {
            return links.containsAll( c);
        }
        @Override
        public boolean addAll(Collection<? extends MORLinked> c) {
            return links.addAll( c);
        }
        @Override
        public boolean removeAll(Collection<?> c) {
            return links.removeAll( c);
        }
        @Override
        public boolean retainAll(Collection<?> c) {
            return links.retainAll( c);
        }
        @Override
        public void clear() {
            links.clear();
        }

        public MORLinked get(OWLObjectProperty property) {
            for( MORLinked l : this)
                if ( l.getProperty().equals( property))
                    return l;
            return null;
        }
    }

    class MORLiteralValue
            implements Semantic.Axiom.Connector<OWLDataProperty,OWLLiteral>, MORAxiom{

        private OWLDataProperty property;
        private OWLLiteral value;

        public MORLiteralValue(){}
        public MORLiteralValue(OWLDataProperty property){
            this.property = property;
        }
        public MORLiteralValue(OWLDataProperty property, OWLLiteral value){
            this.property = property;
            this.value = value;
        }

        @Override
        public OWLDataProperty getProperty() {
            return property;
        }

        @Override
        public void setProperty(OWLDataProperty property) {
            this.property = property;
        }

        @Override
        public OWLLiteral getValue() {
            return value;
        }

        @Override
        public void setValue(OWLLiteral individual) {
            this.value = value;
        }
    }

    class MORLiteralValue3D
            implements Semantic.Axiom.Connector3D<OWLDataProperty,OWLLiteral>, MORAxiom{

        private MORLiteralValue x = new MORLiteralValue();
        private MORLiteralValue y = new MORLiteralValue();
        private MORLiteralValue z = new MORLiteralValue();

        public MORLiteralValue3D(){}
        public MORLiteralValue3D(OWLReferences onto, String prefix, String xSuff, String ySuff, String zSuff){
            if (prefix == null | prefix.isEmpty()){
                x.setProperty( onto.getOWLDataProperty( xSuff));
                y.setProperty( onto.getOWLDataProperty( ySuff));
                z.setProperty( onto.getOWLDataProperty( zSuff));
            } else {
                x.setProperty( onto.getOWLDataProperty( prefix + xSuff));
                y.setProperty( onto.getOWLDataProperty( prefix + ySuff));
                z.setProperty( onto.getOWLDataProperty( prefix + zSuff));
            }
        }


        @Override
        public MORLiteralValue getX() {
            return x;
        }
        @Override
        public MORLiteralValue getY() {
            return y;
        }
        @Override
        public MORLiteralValue getZ() {
            return z;
        }
    }

    // todo MORDataClassCardinality & MORMultiDataClassCardinality
    class MORMinCardinalised
            implements Semantic.Axiom.CardinalityConnector<OWLObjectProperty, OWLClass>, MORAxiom{

        private OWLObjectProperty property;
        private OWLClass range;
        private Integer cardinality = 0;

        public MORMinCardinalised(){}
        public MORMinCardinalised(OWLObjectProperty property, OWLClass range, int cardinality){
            this.property = property;
            this.range = range;
            this.cardinality = cardinality;
        }

        @Override
        public OWLObjectProperty getProperty() {
            return property;
        }
        @Override
        public void setProperty(OWLObjectProperty property) {
            this.property = property;
        }

        @Override
        public OWLClass getValue() {
            return this.range;
        }
        @Override
        public void setValue(OWLClass range) {
            this.range = range;
        }

        @Override
        public Integer getCardinality() {
            return cardinality;
        }
        @Override
        public void setCardinality(Integer cardinality) {
            this.cardinality = cardinality;
        }
    }
    class MORMultiMinCardinalised
            implements Semantic.Axiom.CardinalityConnectorSet<MORMinCardinalised>, MORAxiom{

        private Set<MORMinCardinalised> links = new HashSet<>();

        public MORMultiMinCardinalised(){
        }

        @Override
        public Set<MORMinCardinalised> getSet(){
            return links;
        }

        @Override
        public int size() {
            return links.size();
        }
        @Override
        public boolean isEmpty() {
            return links.isEmpty();
        }
        @Override
        public boolean contains(Object o) {
            return links.contains(o);
        }
        @Override
        public Iterator<MORMinCardinalised> iterator() {
            return links.iterator();
        }
        @Override
        public Object[] toArray() {
            return links.toArray();
        }
        @Override
        public <T> T[] toArray(T[] a) {
            return links.toArray( a);
        }
        @Override
        public boolean add(MORMinCardinalised classCardinality) {
            return links.add( classCardinality);
        }
        public boolean add( OWLObjectProperty link, OWLClass range, int cardinality) {
            return links.add( new MORMinCardinalised(link, range, cardinality));
        }
        @Override
        public boolean remove(Object o) {
            return links.remove( o);
        }
        @Override
        public boolean containsAll(Collection<?> c) {
            return links.containsAll( c);
        }
        @Override
        public boolean addAll(Collection<? extends MORMinCardinalised> c) {
            return links.addAll( c);
        }
        @Override
        public boolean removeAll(Collection<?> c) {
            return links.removeAll( c);
        }
        @Override
        public boolean retainAll(Collection<?> c) {
            return links.retainAll( c);
        }
        @Override
        public void clear() {
            links.clear();
        }
    }
    */
}
