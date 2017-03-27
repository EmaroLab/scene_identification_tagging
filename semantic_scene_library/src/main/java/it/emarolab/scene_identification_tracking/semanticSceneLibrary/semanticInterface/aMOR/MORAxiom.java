package it.emarolab.scene_identification_tracking.semanticSceneLibrary.semanticInterface.aMOR;

import it.emarolab.scene_identification_tracking.semanticSceneLibrary.semanticInterface.aMOR.MORGround.GroundBase;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.semanticInterface.aMOR.MORGround.GroundClass;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.semanticInterface.aMOR.MORGround.GroundIndividual;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.semanticInterface.core.definition.Xdef;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import java.util.Collection;

/**
 * Created by bubx on 25/03/17.
 */
public interface MORAxiom< I extends MORGround<?>, S, A extends MORAtom<?>>
        extends Xdef<I,S,A>{

    abstract class MORX< I extends GroundBase<?>, S, A extends MORAtom<?>>
            extends SIBase
            implements Xdef<I,S,A>, MORAxiom<I,S,A>{

        private S semantic = null;
        private A atom = null;

        public MORX(){
        }
        public MORX( A atom){
            setAtom( atom);
        }
        public MORX( S semantic){
            setSemantic( semantic);
        }
        public MORX( S semantic, A atom){
            setSemantic( semantic);
            setAtom( atom);
        }

        public MORX( I instance, String semanticName){
            setSemantic( instance, semanticName);
        }
        public MORX( String atomName, I instance){
            setAtom( instance, atomName);
        }
        public MORX( I instance, String semanticName, String atomName){
            setSemantic( instance, semanticName);
            setAtom( instance, atomName);
        }

        public MORX( MORX<I,S,A> copy){
            this.semantic = copy.semantic;
            this.atom = copy.atom;
        }

        @Override
        public S getSemantic() {
            return semantic;
        }

        public void setSemantic(S semantic) {
            this.semantic = semantic;
        }

        @Override
        public A getAtom() {
            return atom;
        }

        protected void setAtom(A atom) {
            this.atom = atom;
        }

        protected abstract void setSemantic(I instance, String semanticName);

        protected abstract void setAtom(I instance, String atomName);

        @Override
        public boolean exists() {
            if (semantic == null)
                return false;
            return atom.exists();
        }

        @Override
        public void clear() {
            semantic = null;
            atom = null; // todo override new MORAtom.MORLink();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof MORX)) return false;

            MORX<?, ?, ?> morx = (MORX<?, ?, ?>) o;

            if (getSemantic() != null ? !getSemantic().equals(morx.getSemantic()) : morx.getSemantic() != null)
                return false;
            return getAtom() != null ? getAtom().equals(morx.getAtom()) : morx.getAtom() == null;
        }
        @Override
        public int hashCode() {
            int result = getSemantic() != null ? getSemantic().hashCode() : 0;
            result = 31 * result + (getAtom() != null ? getAtom().hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return semantic + "." + atom ;
        }
    }

    abstract class MORAxiomBase< I extends GroundBase<?>, S, A extends MORAtom.MORAtomBase<S,Y>,Y>
            extends MORX<I,S,A> {

        public MORAxiomBase() {
        }
        public MORAxiomBase(A atom) {
            super(atom);
        }
        public MORAxiomBase(S semantic) {
            super(semantic);
        }
        public MORAxiomBase(S semantic, A atom) {
            super(semantic, atom);
        }
        public MORAxiomBase(I instance, String semanticName) {
            super(instance, semanticName);
        }
        public MORAxiomBase(String atomName, I instance) {
            super(atomName, instance);
        }
        public MORAxiomBase(I instance, String semanticName, String atomName) {
            super(instance, semanticName, atomName);
        }
        public MORAxiomBase(MORX<I, S, A> copy) {
            super(copy);
        }

        @Override
        public void addAxiom(I instance) {
            getAtom().addAxiom( instance, getSemantic());
        }

        @Override
        public void removeAxiom(I instance) {
            getAtom().removeAxiom( instance, getSemantic());
        }

        @Override
        public A queryAxiom(I instance) {
            Y y = getAtom().queryAxiom(instance, getSemantic());
            return getQueringAtom( y);
        }

        protected abstract A getQueringAtom(Y y);
    }

    abstract class MORAxiomsBase< I extends GroundBase<?>, S, A extends MORAtom.MORAtomsBase<S,?,?>>
            extends MORX<I,S,A>{

        public MORAxiomsBase() {
        }
        public MORAxiomsBase(A atom) {
            super(atom);
        }
        public MORAxiomsBase(S semantic) {
            super(semantic);
        }
        public MORAxiomsBase(S semantic, A atom) {
            super(semantic, atom);
        }
        public MORAxiomsBase(I instance, String semanticName) {
            super(instance, semanticName);
        }
        public MORAxiomsBase(String atomName, I instance) {
            super(atomName, instance);
        }
        public MORAxiomsBase(I instance, String semanticName, String atomName) {
            super(instance, semanticName, atomName);
        }

        public MORAxiomsBase(Collection<String> atomsName, I instance) {
            super();
            setAtom( instance, atomsName);
        }
        public MORAxiomsBase(I instance, String semanticsName, Collection<String> atomsName) {
            setSemantic( instance, semanticsName);
            setAtom( instance, atomsName);
        }

        public MORAxiomsBase(MORX<I, S, A> copy) {
            super(copy);
        }

        public void setAtom(I instance, Collection<String> atoms){
            for ( String a : atoms)
                setAtom( instance, a);
        }

        @Override
        public void addAxiom(I instance) {
            getAtom().addAxiom( instance, getSemantic());
        }

        @Override
        public void removeAxiom(I instance) {
            getAtom().removeAxiom( instance, getSemantic());
        }

        @Override
        public A queryAxiom(I instance) {
            Collection<?> y = getAtom().queryAxiom(instance, getSemantic());
            return getQueringAtom( y);
        }

        protected abstract A getQueringAtom(Collection<?> y);

        @Override
        public String toString() {
            String out = getSemantic() + ".{";
            int cnt = 0;
            for ( Object o : this.getAtom().get()) {
                out += o;
                if (++cnt < this.getAtom().get().size())
                    out += ", ";
            }
            return out + "}";
        }
    }


    class MORLinked
            extends MORAxiomBase<GroundIndividual,OWLObjectProperty,MORAtom.MORLink,OWLNamedIndividual>
            implements  Xdef.Link<GroundIndividual,OWLObjectProperty,MORAtom.MORLink>{

        public MORLinked() {
        }
        public MORLinked(MORAtom.MORLink atom) {
            super(atom);
        }
        public MORLinked(OWLObjectProperty semantic) {
            super(semantic);
        }
        public MORLinked(OWLObjectProperty semantic, MORAtom.MORLink atom) {
            super(semantic, atom);
        }
        public MORLinked(GroundIndividual instance, String semanticName) {
            super(instance, semanticName);
        }
        public MORLinked(String atomName, GroundIndividual instance) {
            super(atomName, instance);
        }
        public MORLinked(GroundIndividual instance, String semanticName, String atomName) {
            super(instance, semanticName, atomName);
        }
        public MORLinked(MORX<GroundIndividual, OWLObjectProperty, MORAtom.MORLink> copy) {
            super(copy);
        }

        @Override
        public MORLinked copy() {
            return new MORLinked( this);
        }

        @Override
        protected void setSemantic(GroundIndividual i, String semanticName) {
            setSemantic( i.getOntology().getOWLObjectProperty( semanticName));
        }

        @Override
        protected void setAtom(GroundIndividual i, String atomName) {
            getAtom().set( i.getOntology().getOWLIndividual( atomName));
        }

        @Override
        public void clear() {
            super.clear();
            setAtom( new MORAtom.MORLink());
        }

        @Override
        protected MORAtom.MORLink getQueringAtom(OWLNamedIndividual individual) {
            return new MORAtom.MORLink( individual);
        }
    }

    class MORMultiLinked
            extends MORAxiomsBase<GroundIndividual,OWLObjectProperty, MORAtom.MORLinks>
            implements  Xdef.LinkSet<GroundIndividual,OWLObjectProperty, MORAtom.MORLinks>{

        public MORMultiLinked() {
        }
        public MORMultiLinked(MORAtom.MORLinks atom) {
            super(atom);
        }
        public MORMultiLinked(OWLObjectProperty semantic) {
            super(semantic);
        }
        public MORMultiLinked(OWLObjectProperty semantic, MORAtom.MORLinks atom) {
            super(semantic, atom);
        }
        public MORMultiLinked(GroundIndividual instance, String semanticName) {
            super(instance, semanticName);
        }
        public MORMultiLinked(String atomName, GroundIndividual instance) {
            super(atomName, instance);
        }
        public MORMultiLinked(GroundIndividual instance, String semanticName, String atomName) {
            super(instance, semanticName, atomName);
        }
        public MORMultiLinked(Collection<String> atomsName, GroundIndividual instance) {
            super(atomsName, instance);
        }
        public MORMultiLinked(GroundIndividual instance, String semanticsName, Collection<String> atomsName) {
            super(instance, semanticsName, atomsName);
        }
        public MORMultiLinked(MORX<GroundIndividual, OWLObjectProperty, MORAtom.MORLinks> copy) {
            super(copy);
        }

        @Override
        public MORMultiLinked copy() {
            return new MORMultiLinked( this);
        }

        @Override
        protected void setSemantic(GroundIndividual i, String semanticName) {
            setSemantic( i.getOntology().getOWLObjectProperty( semanticName));
        }

        @Override
        protected void setAtom(GroundIndividual i, String atomName) {
            getAtom().get().add( new MORAtom.MORLink( i.getOntology().getOWLIndividual( atomName)));
        }

        @Override
        public void clear() {
            super.clear();
            setAtom( new MORAtom.MORLinks());
        }

        @Override
        protected MORAtom.MORLinks getQueringAtom(Collection<?> y) {
            return new MORAtom.MORLinks( y);
        }
    }


    class MORLittered
            extends MORAxiomBase<GroundIndividual,OWLDataProperty,MORAtom.MORLiteral,OWLLiteral>
            implements  Xdef.Data<GroundIndividual,OWLDataProperty,MORAtom.MORLiteral>{

        public MORLittered() {
        }
        public MORLittered(MORAtom.MORLiteral atom) {
            super(atom);
        }
        public MORLittered(OWLDataProperty semantic) {
            super(semantic);
        }
        public MORLittered(OWLDataProperty semantic, MORAtom.MORLiteral atom) {
            super(semantic, atom);
        }
        public MORLittered(GroundIndividual instance, String semanticName) {
            super(instance, semanticName);
        }
        public MORLittered(String atomName, GroundIndividual instance) {
            super(atomName, instance);
        }
        public MORLittered(GroundIndividual instance, String semanticName, String atomName) {
            super(instance, semanticName, atomName);
        }
        public MORLittered(MORX<GroundIndividual, OWLDataProperty, MORAtom.MORLiteral> copy) {
            super(copy);
        }

        @Override
        public MORLittered copy() {
            return new MORLittered( this);
        }

        @Override
        protected void setSemantic(GroundIndividual i, String semanticName) {
            setSemantic( i.getOntology().getOWLDataProperty( semanticName));
        }

        @Override
        protected void setAtom(GroundIndividual i, String atomName) {
            setAtom( i, (Object) atomName);
        }
        protected void setAtom(GroundIndividual i, Object atomObj) {
            getAtom().set( i.getOntology().getOWLLiteral( atomObj));
        }

        @Override
        public void clear() {
            super.clear();
            setAtom( new MORAtom.MORLiteral());
        }

        @Override
        protected MORAtom.MORLiteral getQueringAtom(OWLLiteral individual) {
            return new MORAtom.MORLiteral( individual);
        }
    }

    class MORMultiLettered
            extends MORAxiomsBase<GroundIndividual,OWLDataProperty, MORAtom.MORLiterals>
            implements  Xdef.DataSet<GroundIndividual,OWLDataProperty, MORAtom.MORLiterals>{

        public MORMultiLettered() {
        }
        public MORMultiLettered(MORAtom.MORLiterals atom) {
            super(atom);
        }
        public MORMultiLettered(OWLDataProperty semantic) {
            super(semantic);
        }
        public MORMultiLettered(OWLDataProperty semantic, MORAtom.MORLiterals atom) {
            super(semantic, atom);
        }
        public MORMultiLettered(GroundIndividual instance, String semanticName) {
            super(instance, semanticName);
        }
        public MORMultiLettered(String atomName, GroundIndividual instance) {
            super(atomName, instance);
        }
        public MORMultiLettered(GroundIndividual instance, String semanticName, String atomName) {
            super(instance, semanticName, atomName);
        }
        public MORMultiLettered(Collection<String> atomsName, GroundIndividual instance) {
            super(atomsName, instance);
        }
        public MORMultiLettered(GroundIndividual instance, String semanticsName, Collection<String> atomsName) {
            super(instance, semanticsName, atomsName);
        }
        public MORMultiLettered(MORX<GroundIndividual, OWLDataProperty, MORAtom.MORLiterals> copy) {
            super(copy);
        }

        @Override
        public MORMultiLettered copy() {
            return new MORMultiLettered( this);
        }

        @Override
        protected void setSemantic(GroundIndividual i, String semanticName) {
            setSemantic( i.getOntology().getOWLDataProperty( semanticName));
        }

        @Override
        protected void setAtom(GroundIndividual i, String atomName) {
            getAtom().get().add( new MORAtom.MORLiteral( i.getOntology().getOWLLiteral( atomName)));
        }

        @Override
        public void clear() {
            super.clear();
            setAtom( new MORAtom.MORLiterals());
        }

        @Override
        protected MORAtom.MORLiterals getQueringAtom(Collection<?> y) {
            return new MORAtom.MORLiterals( y);
        }
    }


    class MORMultiTyped
            extends MORAxiomsBase<GroundIndividual,Void,MORAtom.MORTypes>
            implements  Xdef.Types<GroundIndividual,MORAtom.MORTypes>{

        public MORMultiTyped() {
        }
        public MORMultiTyped(MORAtom.MORTypes atom) {
            super(atom);
        }
        public MORMultiTyped(String atomName, GroundIndividual instance) {
            super(atomName, instance);
        }
        public MORMultiTyped(Collection<String> atomsName, GroundIndividual instance) {
            super(atomsName, instance);
        }
        public MORMultiTyped(MORX<GroundIndividual, Void, MORAtom.MORTypes> copy) {
            super(copy);
        }

        @Override
        public MORMultiTyped copy() {
            return new MORMultiTyped( this);
        }

        @Override @Deprecated
        protected void setSemantic(GroundIndividual instance, String semanticName) {
        }

        @Override @Deprecated // todo deprecate also somwhere alse : Void
        public Void getSemantic() {
            return super.getSemantic();
        }

        @Override @Deprecated
        public void setSemantic(Void semantic) {
        }

        @Override
        protected void setAtom(GroundIndividual i, String atomName) {
            getAtom().get().add( new MORAtom.MORType( i.getOntology().getOWLClass( atomName)));
        }

        @Override
        public void clear() {
            super.clear();
            setAtom( new MORAtom.MORTypes());
        }
        @Override
        protected MORAtom.MORTypes getQueringAtom(Collection<?> y) {
            return new MORAtom.MORTypes( y);
        }
    }

    class MORSubType
            extends MORAxiomsBase<GroundClass,Void,MORAtom.MORChildren>
            implements Xdef.Children<GroundClass,MORAtom.MORChildren>{

        public MORSubType() {
        }
        public MORSubType(MORAtom.MORChildren atom) {
            super(atom);
        }
        public MORSubType(String atomName, GroundClass instance) {
            super(atomName, instance);
        }
        public MORSubType(Collection<String> atomsName, GroundClass instance) {
            super(atomsName, instance);
        }
        public MORSubType(MORX<GroundClass, Void, MORAtom.MORChildren> copy) {
            super(copy);
        }

        @Override
        public MORSubType copy() {
            return new MORSubType( this);
        }

        @Override @Deprecated
        protected void setSemantic(GroundClass instance, String semanticName) {
        }

        @Override @Deprecated // todo deprecate also somwhere alse : Void
        public Void getSemantic() {
            return super.getSemantic();
        }

        @Override @Deprecated
        public void setSemantic(Void semantic) {
        }

        @Override
        protected void setAtom(GroundClass i, String atomName) {
            getAtom().get().add( new MORAtom.MORType( i.getOntology().getOWLClass( atomName)));
        }

        @Override
        public void clear() {
            super.clear();
            setAtom( new MORAtom.MORChildren());
        }
        @Override
        protected MORAtom.MORChildren getQueringAtom(Collection<?> y) {
            return new MORAtom.MORChildren( y);
        }
    }

    class MORSuperType
            extends MORAxiomsBase<GroundClass,Void,MORAtom.MORParents>
            implements Xdef.Parents<GroundClass,MORAtom.MORParents>{

        public MORSuperType() {
        }
        public MORSuperType(MORAtom.MORParents atom) {
            super(atom);
        }
        public MORSuperType(String atomName, GroundClass instance) {
            super(atomName, instance);
        }
        public MORSuperType(Collection<String> atomsName, GroundClass instance) {
            super(atomsName, instance);
        }
        public MORSuperType(MORX<GroundClass, Void, MORAtom.MORParents> copy) {
            super(copy);
        }

        @Override
        public MORSuperType copy() {
            return new MORSuperType( this);
        }

        @Override @Deprecated
        protected void setSemantic(GroundClass instance, String semanticName) {
        }

        @Override @Deprecated // todo deprecate also somwhere alse : Void
        public Void getSemantic() {
            return super.getSemantic();
        }

        @Override @Deprecated
        public void setSemantic(Void semantic) {
        }

        @Override
        protected void setAtom(GroundClass i, String atomName) {
            getAtom().get().add( new MORAtom.MORType( i.getOntology().getOWLClass( atomName)));
        }

        @Override
        public void clear() {
            super.clear();
            setAtom( new MORAtom.MORParents());
        }
        @Override
        protected MORAtom.MORParents getQueringAtom(Collection<?> y) {
            return new MORAtom.MORParents( y);
        }
    }

}
