package it.emarolab.scene_identification_tracking.semanticSceneLibrary.aMOR.semantic;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.Semantic;
import org.semanticweb.owlapi.model.*;

import java.util.Collection;
import java.util.Set;

/**
 * Created by bubx on 17/03/17.
 */
public interface MORSemantic extends Semantic{

    @SuppressWarnings("Duplicates")
    class MORType
            implements Semantic.Type<OWLReferences,OWLNamedIndividual,MORAxiom.MORTyped> {

        private MORAxiom.MORTyped types = new MORAxiom.MORTyped();

        public MORType(){}
        public MORType( Set<OWLClass> types){
            this.types.getParents().addAll( types);
        }

        @Override
        public void set( MORAxiom.MORTyped types) {
            this.types = types;
        }

        @Override
        public MORAxiom.MORTyped get() {
            return types;
        }


        @Override
        public MORAxiom.MORTyped query(OWLReferences ontology, OWLNamedIndividual instance) {
            return new MORAxiom.MORTyped( ontology.getIndividualClasses(instance));
        }

        @Override
        public <Y> void add(OWLReferences ontology, OWLNamedIndividual instance, Y type) {
            if ( type instanceof Set){
                for ( Object t : (Set) type){
                    if ( t instanceof OWLClass)
                        add( ontology, instance, (OWLClass) t);
                }
            } else if ( type instanceof OWLClass)
                add( ontology, instance, (OWLClass) type);
            //todo log error
        }
        private void add( OWLReferences ontology, OWLNamedIndividual instance, OWLClass type){
            ontology.addIndividualB2Class( instance, type);
        }

        @Override
        public <Y> void remove(OWLReferences ontology, OWLNamedIndividual instance, Y type) {
            if ( type instanceof Set){
                for ( Object t : (Set) type){
                    if ( t instanceof OWLClass)
                        remove( ontology, instance, (OWLClass) t);
                }
            } else if ( type instanceof OWLClass)
                remove( ontology, instance, (OWLClass) type);
            //todo log error
        }
        private void remove(OWLReferences ontology, OWLNamedIndividual instance, OWLClass type) {
            ontology.removeIndividualB2Class( instance, type);
        }

        @Override
        public String toString() {
            return "MORType{" +
                    "types=" + types +
                    '}';
        }
    }

    class MORHierarchy
            implements Semantic.Hierarchy<OWLReferences,OWLClass,MORAxiom.MORHierarchised>{

        private MORAxiom.MORHierarchised node = new MORAxiom.MORHierarchised();

        public MORHierarchy(){}
        public MORHierarchy( Set<OWLClass> parents, Set<OWLClass> children){
            this.node.getParents().addAll( parents);
            this.node.getChildren().addAll( children);
        }
        public MORHierarchy( OWLClass parent, OWLClass child){
            if (parent != null)
                this.node.getParents().add( parent);
            if (child != null)
                this.node.getChildren().add( child);
        }
        public MORHierarchy(MORAxiom.MORHierarchised axiom) {
            this.get().getParents().addAll( axiom.getParents());
            this.get().getChildren().addAll( axiom.getChildren());
        }

        @Override
        public void set(MORAxiom.MORHierarchised node) {
            this.node = node;
        }

        @Override
        public MORAxiom.MORHierarchised get() {
            return node;
        }

        @Override
        public MORAxiom.MORHierarchised query(OWLReferences ontology, OWLClass instance) {
            Set<OWLClass> children = ontology.getSubClassOf(instance);
            Set<OWLClass> parent = ontology.getSuperClassOf(instance);
            return new MORAxiom.MORHierarchised( parent, children);
        }

        @Override
        public <Y> void addParents(OWLReferences ontology, OWLClass instance, Y type) {
            if ( type instanceof Set){
                for ( Object t : (Set) type){
                    if ( t instanceof OWLClass)
                        add( ontology, (OWLClass) t, instance);
                }
            } else if ( type instanceof OWLClass)
                add( ontology, (OWLClass) type, instance);
            //todo log error
        }
        @Override
        public <Y> void addChildren(OWLReferences ontology, OWLClass instance, Y type) {
            if ( type instanceof Set){
                for ( Object t : (Set) type){
                    if ( t instanceof OWLClass)
                        add( ontology, instance, (OWLClass) t);
                }
            } else if ( type instanceof OWLClass)
                add( ontology, instance, (OWLClass) type);
            //todo log error
        }
        private void add( OWLReferences ontology, OWLClass parent, OWLClass child){
            ontology.addSubClassOf( parent, child);
        }

        @Override
        public <Y> void removeParents(OWLReferences ontology, OWLClass instance, Y type) {
            if ( type instanceof Set){
                for ( Object t : (Set) type){
                    if ( t instanceof OWLClass)
                        remove( ontology, (OWLClass) t, instance);
                }
            } else if ( type instanceof OWLClass)
                remove( ontology, (OWLClass) type, instance);
            //todo log error
        }
        @Override
        public <Y> void removeChildren(OWLReferences ontology, OWLClass instance, Y type) {
            if ( type instanceof Set){
                for ( Object t : (Set) type){
                    if ( t instanceof OWLClass)
                        remove( ontology, instance, (OWLClass) t);
                }
            } else if ( type instanceof OWLClass)
                remove( ontology, instance, (OWLClass) type);
            //todo log error
        }
        private void remove( OWLReferences ontology, OWLClass parent, OWLClass child){
            ontology.removeSubClassOf( parent, child);
        }
    }

    class MORLink
            implements Connection<OWLReferences,OWLNamedIndividual,OWLObjectProperty,MORAxiom.MORLinked>{

        private OWLObjectProperty property;
        private MORAxiom.MORLinked link = new MORAxiom.MORLinked();

        public MORLink(){
        }
        public MORLink(OWLObjectProperty property){
            this.setSemantic( property);
        }
        public MORLink(OWLObjectProperty property, OWLNamedIndividual value){
            this.setSemantic( property);
            this.link.setAtom( value);
        }

        @Override
        public void set(MORAxiom.MORLinked link) {
            this.link = link;
        }

        @Override
        public MORAxiom.MORLinked get() {
            return link;
        }


        @Override
        public MORAxiom.MORLinked query(OWLReferences ontology, OWLNamedIndividual instance) {
            //ontology.setOWLEnquirerIncludesInferences( false);
            OWLNamedIndividual value = ontology.getOnlyObjectPropertyB2Individual( instance, getSemantic());
            //ontology.setOWLEnquirerIncludesInferences( true);
            return new MORAxiom.MORLinked( value);
        }

        @Override
        public OWLObjectProperty getSemantic() {
            return property;
        }

        @Override
        public void setSemantic(OWLObjectProperty property) {
            this.property = property;
        }

        @Override
        public <S,V> void add(OWLReferences ontology, OWLNamedIndividual instance, S property, V value) {
            if( property instanceof OWLObjectProperty)
                if( value instanceof OWLNamedIndividual)
                    ontology.addObjectPropertyB2Individual( instance, (OWLObjectProperty) property, (OWLNamedIndividual) value);
            // else // todo log
        }

        // todo remove parameter y to use global 'property3D' instead. (also for other semantics)
        @Override
        public <S,V> void remove(OWLReferences ontology, OWLNamedIndividual instance, S property, V value) {
            if( property instanceof OWLObjectProperty)
                if( value instanceof OWLNamedIndividual)
                    ontology.removeObjectPropertyB2Individual( instance, (OWLObjectProperty) property, (OWLNamedIndividual) value);
            // else // todo log
        }
    }

    class MORLiteral
            implements Connection<OWLReferences,OWLNamedIndividual,OWLDataProperty,MORAxiom.MORLiterised> {

        private OWLDataProperty property;
        private MORAxiom.MORLiterised literal = new MORAxiom.MORLiterised();

        public MORLiteral(){
        }
        public MORLiteral(OWLDataProperty property){
            this.setSemantic( property);
        }
        public MORLiteral(OWLDataProperty property, OWLLiteral value){
            this.setSemantic( property);
            this.literal.setAtom( value);
        }

        @Override
        public void set(MORAxiom.MORLiterised literal) {
            this.literal = literal;
        }

        @Override
        public MORAxiom.MORLiterised get() {
            return literal;
        }


        @Override
        public MORAxiom.MORLiterised query(OWLReferences ontology, OWLNamedIndividual instance) {
            //ontology.setOWLEnquirerIncludesInferences( false);
            OWLLiteral value = ontology.getOnlyDataPropertyB2Individual( instance, getSemantic());
            //ontology.setOWLEnquirerIncludesInferences( true);
            return new MORAxiom.MORLiterised( value);
        }

        @Override
        public OWLDataProperty getSemantic() {
            return property;
        }

        @Override
        public void setSemantic(OWLDataProperty property) {
            this.property = property;
        }

        @Override
        public <S,V> void add(OWLReferences ontology, OWLNamedIndividual instance, S property, V value) {
            if( property instanceof OWLDataProperty)
                if( value instanceof OWLLiteral)
                    ontology.addDataPropertyB2Individual( instance, (OWLDataProperty) property, (OWLLiteral) value);
            // else // todo log
        }

        // todo remove parameter y to use global 'property3D' instead. (also for other semantics)
        @Override
        public <S,V> void remove(OWLReferences ontology, OWLNamedIndividual instance, S property, V value) {
            if( property instanceof OWLDataProperty)
                if( value instanceof OWLLiteral)
                    ontology.removeDataPropertyB2Individual( instance, (OWLDataProperty) property, (OWLLiteral) value);
            // else // todo log
        }
    }

    class MORLinked
            implements Connections<OWLReferences,OWLNamedIndividual,OWLObjectProperty,MORAxiom.MORMultiLinked>{

        private OWLObjectProperty property;
        private MORAxiom.MORMultiLinked links = new MORAxiom.MORMultiLinked();

        public MORLinked(){
        }
        public MORLinked(OWLObjectProperty property){
            this.setSemantic( property);
        }
        public MORLinked(OWLObjectProperty property, OWLNamedIndividual literal){
            this.setSemantic( property);
            this.links.add( new MORAxiom.MORLinked( literal));
        }
        public MORLinked(OWLObjectProperty property, Collection< OWLNamedIndividual> links){
            this.setSemantic( property);
            for (OWLNamedIndividual l : links)
                this.links.add( new MORAxiom.MORLinked( l));
        }
        public MORLinked(OWLObjectProperty property, MORAxiom.MORLinked literal){
            this.setSemantic( property);
            this.links.add( literal);
        }
        public MORLinked(OWLObjectProperty property, MORAxiom.MORMultiLinked links){
            this.setSemantic( property);
            this.links.addAll(links);
        }

        @Override
        public void set(MORAxiom.MORMultiLinked literals) {
            this.links = literals;
        }

        @Override
        public MORAxiom.MORMultiLinked get() {
            return links;
        }

        @Override
        public MORAxiom.MORMultiLinked query(OWLReferences ontology, OWLNamedIndividual instance) {
            //ontology.setOWLEnquirerIncludesInferences( false);
            Set< OWLNamedIndividual> value = ontology.getObjectPropertyB2Individual( instance, getSemantic());
            //ontology.setOWLEnquirerIncludesInferences( true);
            return new MORAxiom.MORMultiLinked( value);
        }

        @Override
        public OWLObjectProperty getSemantic() {
            return property;
        }

        @Override
        public void setSemantic(OWLObjectProperty property) {
            this.property = property;
        }

        @Override
        public <P,Y> void add(OWLReferences ontology, OWLNamedIndividual instance, P property, Y literal) {
            if( property instanceof OWLObjectProperty) {
                if (literal instanceof Set) {
                    for (Object l : (Set) literal) {
                        if (l instanceof OWLNamedIndividual)
                            add(ontology, instance, (OWLObjectProperty) property, (OWLNamedIndividual) l);
                    }
                } else if (literal instanceof OWLNamedIndividual)
                    add(ontology, instance, (OWLObjectProperty) property, (OWLNamedIndividual) literal);
                //todo log error
            }
        }
        private void add( OWLReferences ontology, OWLNamedIndividual instance, OWLObjectProperty property, OWLNamedIndividual value){
            ontology.addObjectPropertyB2Individual( instance, property, value);
        }

        @Override
        public <P,Y> void remove(OWLReferences ontology, OWLNamedIndividual instance, P property, Y literal) {
            if( property instanceof OWLObjectProperty) {
                if (literal instanceof Set) {
                    for (Object l : (Set) literal) {
                        if (l instanceof OWLNamedIndividual)
                            remove(ontology, instance, (OWLObjectProperty) property, (OWLNamedIndividual) l);
                    }
                } else if (literal instanceof OWLNamedIndividual)
                    remove(ontology, instance, (OWLObjectProperty) property, (OWLNamedIndividual) literal);
            }
            //todo log error
        }
        private void remove(OWLReferences ontology, OWLNamedIndividual instance, OWLObjectProperty property, OWLNamedIndividual value) {
            ontology.removeObjectPropertyB2Individual( instance, property, value);
        }

    }

    class MORLiterals
            implements Connections<OWLReferences,OWLNamedIndividual,OWLDataProperty,MORAxiom.MORMultiLiterised>{

        private OWLDataProperty property;
        private MORAxiom.MORMultiLiterised literals = new MORAxiom.MORMultiLiterised();

        public MORLiterals(){
        }
        public MORLiterals(OWLDataProperty property){
            this.setSemantic( property);
        }
        public MORLiterals(OWLDataProperty property, OWLLiteral literal){
            this.setSemantic( property);
            this.literals.add( new MORAxiom.MORLiterised( literal));
        }
        public MORLiterals(OWLDataProperty property, Collection< OWLLiteral> literals){
            this.setSemantic( property);
            for (OWLLiteral l : literals)
                this.literals.add( new MORAxiom.MORLiterised( l));
        }
        public MORLiterals(OWLDataProperty property, MORAxiom.MORLiterised literal){
            this.setSemantic( property);
            this.literals.add( literal);
        }
        public MORLiterals(OWLDataProperty property, MORAxiom.MORMultiLiterised literals){
            this.setSemantic( property);
            this.literals.addAll( literals);
        }

        @Override
        public void set(MORAxiom.MORMultiLiterised literals) {
            this.literals = literals;
        }

        @Override
        public MORAxiom.MORMultiLiterised get() {
            return literals;
        }

        @Override
        public MORAxiom.MORMultiLiterised query(OWLReferences ontology, OWLNamedIndividual instance) {
            //ontology.setOWLEnquirerIncludesInferences( false);
            Set< OWLLiteral> value = ontology.getDataPropertyB2Individual( instance, getSemantic());
            //ontology.setOWLEnquirerIncludesInferences( true);
            return new MORAxiom.MORMultiLiterised( value);
        }

        @Override
        public OWLDataProperty getSemantic() {
            return property;
        }

        @Override
        public void setSemantic(OWLDataProperty property) {
            this.property = property;
        }

        @Override
        public <P,Y> void add(OWLReferences ontology, OWLNamedIndividual instance, P property, Y literal) {
            if( property instanceof OWLDataProperty) {
                if (literal instanceof Set) {
                    for (Object l : (Set) literal) {
                        if (l instanceof OWLLiteral)
                            add(ontology, instance, (OWLDataProperty) property, (OWLLiteral) l);
                    }
                } else if (literal instanceof OWLLiteral)
                    add(ontology, instance, (OWLDataProperty) property, (OWLLiteral) literal);
                //todo log error
            }
        }
        private void add( OWLReferences ontology, OWLNamedIndividual instance, OWLDataProperty property, OWLLiteral literal){
            ontology.addDataPropertyB2Individual( instance, property, literal);
        }

        @Override
        public <P,Y> void remove(OWLReferences ontology, OWLNamedIndividual instance, P property, Y literal) {
            if( property instanceof OWLDataProperty) {
                if (literal instanceof Set) {
                    for (Object l : (Set) literal) {
                        if (l instanceof OWLLiteral)
                            remove(ontology, instance, (OWLDataProperty) property, (OWLLiteral) l);
                    }
                } else if (literal instanceof OWLLiteral)
                    remove(ontology, instance, (OWLDataProperty) property, (OWLLiteral) literal);
            }
            //todo log error
        }
        private void remove(OWLReferences ontology, OWLNamedIndividual instance, OWLDataProperty property, OWLLiteral literal) {
            ontology.removeDataPropertyB2Individual( instance, property, literal);
        }

    }

    // todo make MORLink3D
    class MORLiteralSemantic3D extends Semantic3D<OWLReferences,OWLDataProperty,String>{

        public MORLiteralSemantic3D(){
        }
        public MORLiteralSemantic3D( OWLReferences onto, String prefix,
                                     String xSuffix, String ySuffix, String zSuffix) {
            super( onto, prefix + xSuffix, prefix + ySuffix, prefix + zSuffix);
        }
        public MORLiteralSemantic3D( OWLReferences onto,
                                     String xProp, String yProp, String zProp) {
            super( onto, xProp, yProp, zProp);
        }

        public MORLiteralSemantic3D(OWLDataProperty propX, OWLDataProperty propY, OWLDataProperty propZ) {
            super( propX, propY, propZ);
        }

        @Override
        public OWLDataProperty getSemantic(OWLReferences o, String s) {
            return o.getOWLDataProperty( s);
        }
    }

    class MORLiteral3D
            implements Connection3D<OWLReferences,OWLNamedIndividual,MORLiteralSemantic3D,MORAxiom.MORLiterised3D>{

        private MORLiteralSemantic3D property3D;
        private MORAxiom.MORLiterised3D d3 = new MORAxiom.MORLiterised3D();

        public MORLiteral3D(){
        }
        public MORLiteral3D( OWLReferences onto, String prefix, String xSuffix, String ySuffix, String zSuffix){
            if (prefix != null)
                this.property3D = new MORLiteralSemantic3D( onto, prefix, xSuffix, ySuffix, zSuffix);
            else this.property3D = new MORLiteralSemantic3D( onto, xSuffix, ySuffix, zSuffix);
        }
        public MORLiteral3D( MORLiteralSemantic3D property3D){
            this.property3D = property3D;
        }
        public MORLiteral3D( MORLiteralSemantic3D property3D, MORAxiom.MORLiterised3D d3){
            this.property3D = property3D;
            this.d3 = d3;
        }
        public MORLiteral3D( MORLiteralSemantic3D property3D,
                             OWLLiteral x, OWLLiteral y, OWLLiteral z){
            this.property3D = property3D;
            this.d3.setX( x);
            this.d3.setY( y);
            this.d3.setZ( z);
        }
        public MORLiteral3D( OWLReferences onto, String prefix, String xSuffix, String ySuffix, String zSuffix,
                             OWLLiteral x, OWLLiteral y, OWLLiteral z){
            if (prefix != null)
                this.property3D = new MORLiteralSemantic3D( onto, prefix, xSuffix, ySuffix, zSuffix);
            else this.property3D = new MORLiteralSemantic3D( onto, xSuffix, ySuffix, zSuffix);
            this.d3.setX( x);
            this.d3.setY( y);
            this.d3.setZ( z);
        }

        @Override
        public void set(MORAxiom.MORLiterised3D atom3D) {
            this.d3 = atom3D;
        }

        @Override
        public MORAxiom.MORLiterised3D get() {
            return d3;
        }

        @Override
        public MORAxiom.MORLiterised3D query(OWLReferences ontology, OWLNamedIndividual instance) {
            OWLLiteral x = ontology.getOnlyDataPropertyB2Individual(instance, property3D.getX());
            OWLLiteral y = ontology.getOnlyDataPropertyB2Individual(instance, property3D.getY());
            OWLLiteral z = ontology.getOnlyDataPropertyB2Individual(instance, property3D.getZ());
            return new MORAxiom.MORLiterised3D( x, y, z);
        }

        @Override
        public MORLiteralSemantic3D getSemantic() {
            return property3D;
        }

        @Override
        public void setSemantic(MORLiteralSemantic3D property3D) {
            this.property3D = property3D;
        }

        @Override
        public <P,Y> void add(OWLReferences ontology, OWLNamedIndividual instance, P property, Y value) {
            if( property instanceof OWLDataProperty)
                if( value instanceof MORAxiom.MORLiterised) // todo check for others, was OWLLiteral
                    ontology.addDataPropertyB2Individual( instance, (OWLDataProperty) property, ((MORAxiom.MORLiterised) value).getAtom());
            // else // todo log
        }

        @Override
        public <P,Y> void remove(OWLReferences ontology, OWLNamedIndividual instance, P property, Y value) {
            if( property instanceof OWLDataProperty)
                if( value instanceof MORAxiom.MORLiterised) // todo check for others, was OWLLiteral
                    ontology.removeDataPropertyB2Individual( instance, (OWLDataProperty) property, ((MORAxiom.MORLiterised) value).getAtom());
            // else // todo log
        }
    }

/*
    class MORMinCardinalityRestriction
            implements Semantic.ClassRestriction<OWLReferences,OWLClass,MORAxiom.MORMultiMinCardinalised> {

        MORAxiom.MORMultiMinCardinalised cardinalities = new MORAxiom.MORMultiMinCardinalised();

        @Override
        public void set(MORAxiom.MORMultiMinCardinalised atom) {
            this.cardinalities = atom;
        }

        @Override
        public MORAxiom.MORMultiMinCardinalised get() {
            return cardinalities;
        }


        @Override
        public MORAxiom.MORMultiMinCardinalised query(OWLReferences ontology, OWLClass instance) {
            MORAxiom.MORMultiMinCardinalised out = new MORAxiom.MORMultiMinCardinalised();
            Set<OWLEnquirer.ClassRestriction> restrictions = ontology.getClassRestrictions(instance);
            for ( OWLEnquirer.ClassRestriction r : restrictions)
                if (r.isMinRestriction())
                    out.add( r.getObjectProperty(), r.getObjectRestriction(), r.getCardinality());
            return out;
        }

        @Override
        public <P, C> void add(OWLReferences ontology, OWLClass instance, P property3D, int cardinality, C range) {
            ontology.addMinObjectClassExpression(instance, (OWLObjectProperty) property3D, cardinality, (OWLClass) range);
        }

        @Override
        public <P, C> void remove(OWLReferences ontology, OWLClass instance, P property3D, int cardinality, C range) {
            ontology.removeMinObjectClassExpression(instance, (OWLObjectProperty) property3D, cardinality, (OWLClass) range);
        }
    }

    class MORLink
            implements Semantic.Property<OWLReferences,OWLNamedIndividual,MORAxiom.MORLinked>{

        private MORAxiom.MORLinked links = new MORAxiom.MORLinked();

        public MORLink(){
        }
        public MORLink( OWLObjectProperty property3D, OWLNamedIndividual value){
            this.links.setProperty( property3D);
            this.links.setValue( value);
        }

        @Override
        public void set(MORAxiom.MORLinked links) {
            this.links = links;
        }

        @Override
        public MORAxiom.MORLinked get() {
            return links;
        }

        @Override
        public MORAxiom.MORLinked query(OWLReferences ontology, OWLNamedIndividual instance) {
            OWLNamedIndividual value = ontology.getOnlyObjectPropertyB2Individual( instance, links.getProperty());
            return new MORAxiom.MORLinked( links.getProperty(), value);
        }

        @Override
        public <P, V> void add(OWLReferences ontology, OWLNamedIndividual instance, P property3D, V value) {
            ontology.addObjectPropertyB2Individual( instance, (OWLObjectProperty) property3D, (OWLNamedIndividual) value);
        }

        @Override
        public <P, V> void remove(OWLReferences ontology, OWLNamedIndividual instance, P property3D, V value) {
            ontology.removeObjectPropertyB2Individual( instance, (OWLObjectProperty) property3D, (OWLNamedIndividual) value);
        }
    }

    class MORLinks
            implements Semantic.MultiProperty<OWLReferences,OWLNamedIndividual,MORAxiom.MORMultiLinked>{

        MORAxiom.MORMultiLinked links = new MORAxiom.MORMultiLinked(v);

        public MORLinks(){
        }

        @Override
        public void set(MORAxiom.MORMultiLinked links) {
            this.links = links;
        }

        @Override
        public MORAxiom.MORMultiLinked get() {
            return links;
        }


        @Override
        public MORAxiom.MORMultiLinked query(OWLReferences ontology, OWLNamedIndividual instance) {
            //ontology.setOWLEnquirerIncludesInferences( false);
            Set<OWLEnquirer.ObjectPropertyRelations> values = ontology.getObjectPropertyB2Individual(instance);
            //ontology.setOWLEnquirerIncludesInferences( true);
            MORAxiom.MORMultiLinked links = new MORAxiom.MORMultiLinked(v);
            for ( OWLEnquirer.ObjectPropertyRelations r : values)
                links.add( r.getProperty(), r.getValues());
            return links;
        }

        @Override
        public <P,V> void add(OWLReferences ontology, OWLNamedIndividual instance, P property3D, V value) {
            ontology.addObjectPropertyB2Individual(instance,
                    (OWLObjectProperty) property3D,(OWLNamedIndividual) value);
        }
        @Override
        public <P,V> void remove(OWLReferences ontology, OWLNamedIndividual instance, P property3D, V value) {
            ontology.removeObjectPropertyB2Individual(instance,
                    (OWLObjectProperty) property3D,(OWLNamedIndividual) value);
        }
    }

    class MORLiteral
            implements Semantic.Property<OWLReferences,OWLNamedIndividual,MORAxiom.MORLiteralValue>{

        private MORAxiom.MORLiteralValue links = new MORAxiom.MORLiteralValue();

        public MORLiteral(){
        }
        public MORLiteral(OWLDataProperty property3D, OWLLiteral value){
            this.links.setProperty( property3D);
            this.links.setValue( value);
        }

        @Override
        public void set(MORAxiom.MORLiteralValue links) {
            this.links = links;
        }

        @Override
        public MORAxiom.MORLiteralValue get() {
            return links;
        }


        @Override
        public MORAxiom.MORLiteralValue query(OWLReferences ontology, OWLNamedIndividual instance) {
            //ontology.setOWLEnquirerIncludesInferences( false);
            OWLLiteral value = ontology.getOnlyDataPropertyB2Individual( instance, links.getProperty());
            //ontology.setOWLEnquirerIncludesInferences( true);
            return new MORAxiom.MORLiteralValue( links.getProperty(), value);
        }

        @Override
        public <P, V> void add(OWLReferences ontology, OWLNamedIndividual instance, P property3D, V value) {
            ontology.addDataPropertyB2Individual( instance, (OWLDataProperty) property3D, (OWLLiteral) value);
        }

        @Override
        public <P, V> void remove(OWLReferences ontology, OWLNamedIndividual instance, P property3D, V value) {
            ontology.removeDataPropertyB2Individual( instance, (OWLDataProperty) property3D, (OWLLiteral) value);
        }
    }

    class MORData3D
            implements Semantic.Property3D<OWLReferences,OWLNamedIndividual,MORAxiom.MORLiteralValue3D>{

        private MORAxiom.MORLiteralValue3D link3D;

        public MORData3D(){
            link3D = new MORAxiom.MORLiteralValue3D();
        }
        public MORData3D(OWLReferences onto, String prefix, String xSuff, String ySuff, String zSuff){
            link3D = new MORAxiom.MORLiteralValue3D( onto, prefix, xSuff, ySuff, zSuff);
        }

        @Override
        public void set(MORAxiom.MORLiteralValue3D atom) {
            link3D = atom;
        }

        @Override
        public MORAxiom.MORLiteralValue3D get() {
            return link3D;
        }


        @Override
        public MORAxiom.MORLiteralValue3D query(OWLReferences ontology, OWLNamedIndividual instance) {
            MORAxiom.MORLiteralValue3D queriedLink = new MORAxiom.MORLiteralValue3D();
            queriedLink.getX().setProperty( link3D.getX().getProperty());
            queriedLink.getX().setValue(
                    ontology.getOnlyDataPropertyB2Individual( instance, queriedLink.getX().getProperty()));
            queriedLink.getY().setProperty( link3D.getY().getProperty());
            queriedLink.getY().setValue(
                    ontology.getOnlyDataPropertyB2Individual( instance, queriedLink.getY().getProperty()));
            queriedLink.getZ().setProperty( link3D.getZ().getProperty());
            queriedLink.getZ().setValue(
                    ontology.getOnlyDataPropertyB2Individual( instance, queriedLink.getZ().getProperty()));
            return queriedLink;
        }

        private <P,V> void add(OWLReferences ontology, OWLNamedIndividual instance, P property3D, V value) {
            ontology.addDataPropertyB2Individual( instance, (OWLDataProperty) property3D, (OWLLiteral) value);
        }
        private <P,V> void remove(OWLReferences ontology, OWLNamedIndividual instance, P property3D, V value) {
            ontology.removeDataPropertyB2Individual( instance, (OWLDataProperty) property3D, (OWLLiteral) value);
        }

        @Override
        public <P, V> void addX(OWLReferences ontology, OWLNamedIndividual instance, P property3D, V value) {
            add(ontology,instance,property3D,value);
        }

        @Override
        public <P, V> void addY(OWLReferences ontology, OWLNamedIndividual instance, P property3D, V value) {

        }

        @Override
        public <P, V> void addZ(OWLReferences ontology, OWLNamedIndividual instance, P property3D, V value) {

        }

        @Override
        public <P, V> void removeX(OWLReferences ontology, OWLNamedIndividual instance, P property3D, V value) {

        }

        @Override
        public <P, V> void removeY(OWLReferences ontology, OWLNamedIndividual instance, P property3D, V value) {

        }

        @Override
        public <P, V> void removeZ(OWLReferences ontology, OWLNamedIndividual instance, P property3D, V value) {

        }
    }
    */
}
