package it.emarolab.scene_identification_tracking.semanticSceneLibrary.semanticInterface.aMOR;

import it.emarolab.scene_identification_tracking.semanticSceneLibrary.semanticInterface.core.Semantic;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.semanticInterface.core.definition.Adef;
import org.semanticweb.owlapi.model.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by bubx on 25/03/17.
 */
public interface MORAtom<Y>
            extends Adef<Y>{

    abstract class MORABase<Y>
            extends SIBase
            implements  Adef<Y>, MORAtom<Y> {

        private Y atom = null;

        public MORABase(){
        }
        public MORABase(Y atom){
            set(atom);
        }
        public MORABase(MORABase<Y> copy) {
            set( copy.get());
        }

        public void set( Y atom){
            this.atom = atom;
        }

        @Override
        public Y get() {
            return atom;
        }

        @Override
        public boolean exists() {
            return atom != null;
        }

        @Override
        public void clear() {
            atom = null;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof MORAtom.MORABase)) return false;

            MORABase that = (MORABase) o;

            return atom != null ? atom.equals(that.atom) : that.atom == null;
        }
        @Override
        public int hashCode() {
            return atom != null ? atom.hashCode() : 0;
        }

        @Override
        public String toString() {
            return atom.toString();
        }
    }


    abstract class MORAtomBase<S,Y>
            extends MORABase<Y>
            implements Adef.Atom<S,Y>, MORAtom<Y>{

        public MORAtomBase() {
        }
        public MORAtomBase(Y atom) {
            super(atom);
        }
        public MORAtomBase(MORAtomBase<S,Y> copy) {
            super( copy);
        }
    }


    abstract class MORAtomsBase<S,YY extends Collection<? extends Semantic.Atom<S,Y>>,Y>
            extends SIBase
            implements Adef.Atoms<S,YY,Y>, MORAtom<YY> {

        private YY atoms;

        public MORAtomsBase(YY atoms){
            this.atoms = atoms;
        }

        public MORAtomsBase(MORAtomsBase<S,YY,Y> copy) {
            this.atoms = copy.get();
        }


        @Override
        public YY get() {
            return atoms;
        }

        @Override
        public boolean exists() {
            if ( atoms == null)
                return false;
            if ( atoms.isEmpty())
                return false;
            for( Semantic.Atomic<Y> a : atoms)
                if ( ! a.exists())
                    return false;
            return true;
        }

        @Override
        public void clear() {
            atoms.clear();
        }

    }





    class MORLink
            extends MORAtomBase<OWLObjectProperty,OWLNamedIndividual>
            implements Adef.Individual<OWLObjectProperty,OWLNamedIndividual>{
        public MORLink() {
        }
        public MORLink(OWLNamedIndividual atom) {
            super(atom);
        }
        public MORLink(MORAtomBase<OWLObjectProperty, OWLNamedIndividual> copy) {
            super(copy);
        }

        @Override
        public MORLink copy() {
            return new MORLink( this);
        }

        @Override
        public <I extends Semantic.Ground<?, ?>>
        void addAxiom(I instance, OWLObjectProperty semantic, OWLNamedIndividual atom) {
            MORGround.GroundIndividual i = MORGround.groundIndividual(instance);
            if ( i != null)
                i.getOntology().addObjectPropertyB2Individual( i.getInstance(), semantic, atom);
        }

        @Override
        public <I extends Semantic.Ground<?, ?>>
        void removeAxiom(I instance, OWLObjectProperty semantic, OWLNamedIndividual atom) {
            MORGround.GroundIndividual i = MORGround.groundIndividual( instance);
            if ( i != null)
                i.getOntology().removeObjectPropertyB2Individual( i.getInstance(), semantic, atom);

        }

        @Override
        public <I extends Semantic.Ground<?, ?>>
        OWLNamedIndividual queryAxiom(I instance, OWLObjectProperty semantic) {
            MORGround.GroundIndividual i = MORGround.groundIndividual( instance);
            if ( i != null)
                return i.getOntology().getOnlyObjectPropertyB2Individual( i.getInstance(), semantic);
            return null;
        }
    }

    class MORLinks
            extends MORAtomsBase<OWLObjectProperty,Set<MORLink>,OWLNamedIndividual>
            implements Adef.Individuals<OWLObjectProperty,Set<MORLink>,OWLNamedIndividual>{

        public MORLinks() {
            super( new HashSet<>());
        }
        public MORLinks(MORLink link) { // todo in others
            super( new HashSet<>());
            get().add( link);
        }
        public MORLinks(Collection< ?> links){ // todo in others
            super( new HashSet<>());
            for ( Object i : links)
                if ( i instanceof OWLNamedIndividual)
                    get().add( new MORLink( ( OWLNamedIndividual) i));
                else if( i instanceof MORLink)
                    get().add( ( MORLink) i);
                //else // todo log

        }
        public MORLinks(MORAtomsBase<OWLObjectProperty, Set<MORLink>, OWLNamedIndividual> copy) {
            super(copy);
        }

        @Override
        public MORLinks copy() {
            return new MORLinks( this);
        }

        @Override
        public <I extends Semantic.Ground<?, ?>>
        Set<OWLNamedIndividual> queryAxiom(I instance, OWLObjectProperty semantic) {
            MORGround.GroundIndividual i = MORGround.groundIndividual( instance);
            if ( i != null)
                return i.getOntology().getObjectPropertyB2Individual( i.getInstance(), semantic);
            return null;
        }

        @Override
        public MORLink getNewElement(OWLNamedIndividual value) {
            return new MORLink( value);
        }
    }

    class MORLiteral
            extends MORAtomBase<OWLDataProperty,OWLLiteral>
            implements Adef.Literal<OWLDataProperty,OWLLiteral>{

        public MORLiteral() {
        }
        public MORLiteral(OWLLiteral atom) {
            super(atom);
        }
        public MORLiteral(MORAtomBase<OWLDataProperty, OWLLiteral> copy) {
            super(copy);
        }

        @Override
        public MORLiteral copy() {
            return new MORLiteral( this);
        }

        @Override
        public <I extends Semantic.Ground<?, ?>>
        void addAxiom(I instance, OWLDataProperty semantic, OWLLiteral atom) {
            MORGround.GroundIndividual i = MORGround.groundIndividual(instance);
            if ( i != null)
                i.getOntology().addDataPropertyB2Individual( i.getInstance(), semantic, atom);
        }

        @Override
        public <I extends Semantic.Ground<?, ?>>
        void removeAxiom(I instance, OWLDataProperty semantic, OWLLiteral atom) {
            MORGround.GroundIndividual i = MORGround.groundIndividual( instance);
            if ( i != null)
                i.getOntology().removeDataPropertyB2Individual( i.getInstance(), semantic, atom);

        }

        @Override
        public <I extends Semantic.Ground<?, ?>>
        OWLLiteral queryAxiom(I instance, OWLDataProperty semantic) {
            MORGround.GroundIndividual i = MORGround.groundIndividual( instance);
            if ( i != null)
                return i.getOntology().getOnlyDataPropertyB2Individual( i.getInstance(), semantic);
            return null;
        }
    }

    class MORLiterals
            extends MORAtomsBase<OWLDataProperty,Set<MORLiteral>,OWLLiteral>
            implements Adef.Literals<OWLDataProperty,Set<MORLiteral>,OWLLiteral>{

        public MORLiterals() {
            super( new HashSet<>());
        }
        public MORLiterals(MORLiteral literal) { // todo in others
            super( new HashSet<>());
            get().add( literal);
        }
        public MORLiterals(Collection< ?> literal){ // todo in others
            super( new HashSet<>());
            for ( Object i : literal)
                if ( i instanceof OWLLiteral)
                    get().add( new MORLiteral( ( OWLLiteral) i));
                else if( i instanceof MORLiteral)
                    get().add( ( MORLiteral) i);
            //else // todo log
        }
        public MORLiterals(MORAtomsBase<OWLDataProperty, Set<MORLiteral>, OWLLiteral> copy) {
            super(copy);
        }



        @Override
        public MORLiterals copy() {
            return null;
        }

        @Override
        public <I extends Semantic.Ground<?, ?>>
        Set<OWLLiteral> queryAxiom(I instance, OWLDataProperty semantic) {
            MORGround.GroundIndividual i = MORGround.groundIndividual( instance);
            if ( i != null)
                return i.getOntology().getDataPropertyB2Individual( i.getInstance(), semantic);
            return null;
        }

        @Override
        public MORLiteral getNewElement(OWLLiteral value) {
            return new MORLiteral( value);
        }
    }

    class MORType
            extends MORAtomBase<Void,OWLClass>
            implements Adef.Class<OWLClass>{

        public MORType() {
        }
        public MORType(OWLClass atom) {
            super(atom);
        }
        public MORType(MORAtomBase<Void, OWLClass> copy) {
            super(copy);
        }

        @Override
        public MORType copy() {
            return new MORType( this);
        }

        @Override
        public <I extends Semantic.Ground<?, ?>>
        void addAxiom(I instance, Void semantic, OWLClass atom) {
            MORGround.GroundIndividual i = MORGround.groundIndividual(instance);
            if ( i != null)
                i.getOntology().addIndividualB2Class( i.getInstance(), atom);
        }

        @Override
        public <I extends Semantic.Ground<?, ?>>
        void removeAxiom(I instance, Void semantic, OWLClass atom) {
            MORGround.GroundIndividual i = MORGround.groundIndividual( instance);
            if ( i != null)
                i.getOntology().removeIndividualB2Class( i.getInstance(), atom);
        }

        @Override @Deprecated
        public <I extends Semantic.Ground<?, ?>>
        OWLClass queryAxiom(I instance, Void semantic) {
            MORGround.GroundIndividual i = MORGround.groundIndividual( instance);
            if ( i != null)
                return i.getOntology().getOnlyIndividualClasses( i.getInstance());
            return null;
        }
    }

    class MORTypes
            extends MORAtomsBase<Void,Set<MORType>,OWLClass>
            implements Adef.Classes<Set<MORType>,OWLClass>{

        public MORTypes() {
            super( new HashSet<>());
        }
        public MORTypes(MORType type) { // todo in others
            super( new HashSet<>());
            get().add( type);
        }
        public MORTypes(Collection< ?> literal){ // todo in others
            super( new HashSet<>());
            for ( Object i : literal)
                if ( i instanceof OWLClass)
                    get().add( new MORType( ( OWLClass) i));
                else if( i instanceof MORType)
                    get().add( ( MORType) i);
            //else // todo log
        }
        public MORTypes(MORAtomsBase<Void, Set<MORType>, OWLClass> copy) {
            super(copy);
        }

        @Override
        public MORTypes copy() {
            return new MORTypes( this);
        }

        @Override
        public <I extends Semantic.Ground<?, ?>>
        Set<OWLClass> queryAxiom(I instance, Void semantic) {
            MORGround.GroundIndividual i = MORGround.groundIndividual( instance);
            if ( i != null)
                return i.getOntology().getIndividualClasses( i.getInstance());
            return null;
        }

        @Override
        public MORType getNewElement(OWLClass value) {
            return new MORType( value);
        }
    }

    class MORChildren
            extends MORTypes
            implements Adef.Classes<Set<MORType>,OWLClass>{

        public MORChildren() {
        }
        public MORChildren(MORType type) {
            super(type);
        }
        public MORChildren(Collection<?> literal) {
            super(literal);
        }
        public MORChildren(MORAtomsBase<Void, Set<MORType>, OWLClass> copy) {
            super(copy);
        }

        @Override
        public MORChildren copy() {
            return new MORChildren(this);
        }

        @Override
        public <I extends Semantic.Ground<?, ?>>
        Set<OWLClass> queryAxiom(I instance, Void semantic) {
            MORGround.GroundClass i = MORGround.groundClass( instance);
            if ( i != null)
                return i.getOntology().getSubClassOf( i.getInstance());
            return null;
        }
    }

    class MORParents
            extends MORTypes
            implements Adef.Classes<Set<MORType>,OWLClass>{

        public MORParents() {
        }
        public MORParents(MORType type) {
            super(type);
        }
        public MORParents(Collection<?> literal) {
            super(literal);
        }
        public MORParents(MORAtomsBase<Void, Set<MORType>, OWLClass> copy) {
            super(copy);
        }

        @Override
        public MORParents copy() {
            return new MORParents( this);
        }

        @Override
        public <I extends Semantic.Ground<?, ?>>
        Set<OWLClass> queryAxiom(I instance, Void semantic) {
            MORGround.GroundClass i = MORGround.groundClass( instance);
            if ( i != null)
                return i.getOntology().getSubClassOf( i.getInstance());
            return null;
        }
    }


}


