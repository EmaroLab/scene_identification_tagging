package it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.aMORDescriptor;

import it.emarolab.amor.owlInterface.OWLEnquirer;
import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.Base;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.Semantics;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.aMORDescriptor.aMORObject.MORPrimitive;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.semantic.SceneSemantic;
import it.emarolab.scene_identification_tracking.semanticSceneLibrary.core.semantic.SpatialSemantic;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public interface MORSpatialDescriptor extends Base{

    // this consider unique property (replaces) todo: add for multiple
    interface MORMultiSpatialDescriptor
            extends MORDescriptor.MORIndividualDescriptor,
                MORTypedDescriptor,
            //Semantics.LinkedDescriptor<OWLReferences, OWLNamedIndividual, MORSpatialProperty, MORSpatialRelation>,
            SpatialSemantic.SpatialDescriptor<OWLReferences, OWLNamedIndividual, OWLClass, OWLObjectProperty,
                    MORSpatialProperty, MORSpatialRelation, MORSpatialCollector>
    {

        @Override
        default MORSpatialCollector queryBehind() {
            MORSpatialRelation relations = new MORSpatialRelation(getOntology(), getInstance(),
                    getBottomType(), OBJECTPROPERTY.DEFAULT_RELATION_BEHIND);
            relations.queryItems();
            return relations.asSet();
        }
        @Override
        default MORSpatialCollector queryFront() {
            MORSpatialRelation front = new MORSpatialRelation( getOntology(), getInstance(),
                    getBottomType(), OBJECTPROPERTY.DEFAULT_RELATION_BEHINDinverse);
            front.setInverseProperty(  OBJECTPROPERTY.DEFAULT_RELATION_BEHIND);
            front.queryItems();
            return front.asSet();
        }
        @Override
        default MORSpatialCollector queryAbove() {
            MORSpatialRelation relation = new MORSpatialRelation(getOntology(), getInstance(),
                    getBottomType(), OBJECTPROPERTY.DEFAULT_RELATION_ABOVE);
            relation.queryItems();
            return relation.asSet();
        }
        @Override
        default MORSpatialCollector queryBelow() {
            MORSpatialRelation below = new MORSpatialRelation( getOntology(), getInstance(),
                    getBottomType(), OBJECTPROPERTY.DEFAULT_RELATION_ABOVEinverse);
            below.setInverseProperty( OBJECTPROPERTY.DEFAULT_RELATION_ABOVE);
            below.queryItems();
            return below.asSet();
        }
        @Override
        default MORSpatialCollector queryRight() {
            MORSpatialRelation relation = new MORSpatialRelation(getOntology(), getInstance(),
                    getBottomType(), OBJECTPROPERTY.DEFAULT_RELATION_RIGHT);
            relation.queryItems();
            return relation.asSet();
        }
        @Override
        default MORSpatialCollector queryLeft() {
            MORSpatialRelation left = new MORSpatialRelation( getOntology(), getInstance(),
                    getBottomType(), OBJECTPROPERTY.DEFAULT_RELATION_RIGHTinverse);
            left.setInverseProperty( OBJECTPROPERTY.DEFAULT_RELATION_RIGHT);
            left.queryItems();
            return left.asSet();

        }
        @Override
        default MORSpatialCollector queryAlongX() {
            MORSpatialRelation relation = new MORSpatialRelation(getOntology(), getInstance(),
                    getBottomType(), OBJECTPROPERTY.DEFAULT_RELATION_LONGX);
            relation.queryItems();
            return relation.asSet();
        }
        @Override
        default MORSpatialCollector queryAlongY() {
            MORSpatialRelation relation = new MORSpatialRelation(getOntology(), getInstance(),
                    getBottomType(), OBJECTPROPERTY.DEFAULT_RELATION_LONGY);
            relation.queryItems();
            return relation.asSet();
        }
        @Override
        default MORSpatialCollector queryAlongZ() {
            MORSpatialRelation relation = new MORSpatialRelation(getOntology(), getInstance(),
                    getBottomType(), OBJECTPROPERTY.DEFAULT_RELATION_LONGZ);
            relation.queryItems();
            return relation.asSet();
        }
        default MORSpatialCollector queryAlong() {
            MORSpatialCollector out = new MORSpatialCollector( getOntology());
            out.addAll( queryAlongX());
            out.addAll( queryAlongY());
            out.addAll( queryAlongZ());
            return out;
        }
        @Override
        default MORSpatialCollector queryCoaxial() {
            MORSpatialRelation relation = new MORSpatialRelation(getOntology(), getInstance(),
                    getBottomType(), OBJECTPROPERTY.DEFAULT_RELATION_COAXIAL);
            relation.queryItems();
            return relation.asSet();
        }
        @Override
        default MORSpatialCollector queryParallel() {
            MORSpatialRelation relation = new MORSpatialRelation(getOntology(), getInstance(),
                    getBottomType(), OBJECTPROPERTY.DEFAULT_RELATION_PARALLEL);
            relation.queryItems();
            return relation.asSet();
        }
        @Override
        default MORSpatialCollector queryPerpendicular(){
            MORSpatialRelation relation = new MORSpatialRelation(getOntology(), getInstance(),
                    getBottomType(), OBJECTPROPERTY.DEFAULT_RELATION_PERPENDICULAR);
            relation.queryItems();
            return relation.asSet();
        }

        @Override
        default MORSpatialCollector querySpatialProperties() { // todo implement with a getter for performances
            MORSpatialCollector out = new MORSpatialCollector( getOntology());
            out.addAll( queryBehind());
            out.addAll( queryFront());
            out.addAll( queryAbove());
            out.addAll( queryBelow());
            out.addAll( queryRight());
            out.addAll( queryLeft());
            out.addAll( queryAlong());
            out.addAll( queryCoaxial());
            out.addAll( queryParallel());
            out.addAll( queryPerpendicular());

           // out.clean();
            for ( MORSpatialRelation i : out)
                getLinksMap().put(  i.getSceneRelation(), i);
            //log("££££££££££££££££££££5555 " + out + " " +  getLinksMap());

            return out;
        }



        @Override
        Map<MORSpatialProperty, MORSpatialRelation> getLinksMap();

        default void clearDescriptor(){
            for ( MORSpatialProperty p : getLinksMap().keySet())
                getLinksMap().put( p, null);
        }


        default void linkInstance(OWLObjectProperty property){
            getLinksMap().put( new MORSpatialProperty( getOntology(), property), null);
        }
        default void linkInstance(MORSpatialRelation relation){
            if( getLinksMap().containsKey( relation.getSceneRelation())){
                MORSpatialRelation old = getLinksMap().get( relation.getSceneRelation());
                for ( SpatialSemantic.SpatialItem<OWLClass,OWLNamedIndividual> r : relation.getItems()) {
                    old.getItems().add( r);
                }
            } else getLinksMap().put( relation.getSceneRelation(), relation);
        }

        default MORSpatialRelation removeLink(OWLObjectProperty property) {
            return getLinksMap().remove( property);
        }
        default MORSpatialRelation removeLink(MORSpatialRelation relation) {
            return getLinksMap().remove( relation);
        }
        default MORSpatialCollector removeAllLink(MORSpatialCollector relations) {
            MORSpatialCollector out = new MORSpatialCollector( getOntology());
            for ( MORSpatialRelation s : relations)
                out.add( getLinksMap().remove( s));
            return out;
        }

        default MORSpatialRelation getLink(MORSpatialProperty property){
            return getLinksMap().get( property);
        }
        default MORSpatialRelation getLink(OWLObjectProperty property){
            return getLinksMap().get( new MORSpatialProperty( getOntology(), property));
        }
        default MORSpatialRelation getLink(String property){
            return getLink( getOntology().getOWLObjectProperty( property));
        }

        @Override
        default Semantics.MappingTransitions readLink(){
            return new MORTryRead<OWLNamedIndividual, OWLObjectProperty, MORSpatialRelation>(MORMultiSpatialDescriptor.this,
                    LOGGING.INTENT_linkSPATIAL_MULTI) {
                @Override
                protected Semantics.MappingTransitions giveAtry() {
                    Set<OWLEnquirer.ObjectPropertyRelations> relations = getOntology().getObjectPropertyB2Individual( getInstance());

                    if ( relations.isEmpty()){
                        for ( MORSpatialRelation v : new HashMap<>( getLinksMap()).values()){

                            Semantics.MappingIntent<OWLNamedIndividual, OWLObjectProperty, MORSpatialRelation, Semantics.ReadingState>
                                    intent = getNewIntent( v.getRelation());
                            intent.setJavaValue( getLinksMap().get( v));
                            intent.setSemanticValue( null);
                            intent.getState().asAbsent();

                            removeLink( v);
                        }
                    } else {
                        for (OWLEnquirer.ObjectPropertyRelations rel : relations)
                            readSet(rel.getProperty(), rel.getValues());
                    }
                    return getStateTransitions();
                }
                private void readSet(OWLObjectProperty property, Set<OWLNamedIndividual> semanticValue){
                    MORSpatialRelation javaValue = getLink(property);

                    Set<MORSpatialRelation> semanticRelations = new HashSet<>();
                    if (semanticValue != null)
                        for ( OWLNamedIndividual i : semanticValue)
                            semanticRelations.add( new MORSpatialRelation( getOntology(), getInstance(), getBottomType(), property));


                    Semantics.MappingIntent<OWLNamedIndividual, OWLObjectProperty, MORSpatialRelation, Semantics.ReadingState>
                            intent = getNewIntent(property);
                    intent.setJavaValue( javaValue);
                    intent.setSemanticValue( null);//) semanticRelations);

                    if (javaValue != null) {
                        if (semanticValue != null) {
                            // both red, nothing changed
                            if (semanticValue.equals(javaValue)) {
                                intent.getState().asNotChanged();
                            } else {
                                Set<OWLNamedIndividual> javaValueCopy = new HashSet<>( javaValue.getItemsPrimitive());
                                for (OWLNamedIndividual cl : semanticValue) {

                                    Semantics.MappingIntent<OWLNamedIndividual, OWLObjectProperty, OWLNamedIndividual, Semantics.ReadingState> otherIntent
                                            = new MORMappingIntent<> (MORMultiSpatialDescriptor.this, property, getNewState());
                                    propagateAtomDescription( otherIntent, LOGGING.INTENT_LINK_MULTI_ADD);
                                    getStateTransitions().add( otherIntent);
                                    otherIntent.setSemanticValue( cl);
                                    otherIntent.setJavaValue( null);

                                    if ( javaValue.getItemsPrimitive().contains(cl)) {
                                        // both read, not changed
                                        otherIntent.getState().asNotChanged();
                                        // it does not do nothing on the set
                                        // take track of what has not beet added, to be removed later
                                        javaValueCopy.remove(cl);
                                    } else {
                                        // both read, added
                                        otherIntent.getState().asSuccess();
                                        MORSpatialRelation add = new MORSpatialRelation( getOntology(), getInstance(), getBottomType(), property);
                                        add.addItems( cl, MORPrimitive.inferShape( getOntology(), cl));
                                        linkInstance( add);
                                    }
                                }
                                for (OWLNamedIndividual rm : javaValueCopy) {
                                    Semantics.MappingIntent<OWLNamedIndividual, OWLObjectProperty, OWLNamedIndividual, Semantics.ReadingState> removeIntent
                                            = new MORMappingIntent<> (MORMultiSpatialDescriptor.this, property, getNewState());
                                    propagateAtomDescription( removeIntent, LOGGING.INTENT_LINK_MULTI_REMOVE);
                                    getStateTransitions().add( removeIntent);
                                    removeIntent.setSemanticValue( null);
                                    removeIntent.setJavaValue( rm);

                                    // remove remaining value
                                    removeIntent.getState().asAbsent();
                                    getLink( property).getItems().remove( rm);//removeLink( property, javaValueCopy);
                                }
                            }
                        } else {
                            // nothing read, nothing changed
                            if (javaValue.getItems().isEmpty()) {
                                intent.getState().asNotChanged();
                            } else {
                                // nothing read, remove all
                                clearDescriptor();
                                intent.getState().asSuccess();
                            }
                        }
                    } else { // java cannot be read
                        //logError("Cannot write null types for: " + getInstanceName());
                        //intent.getState().asError();
                        for ( MORSpatialRelation r : semanticRelations)
                            linkInstance( r);
                    }
                }
            }.perform();
        }


        @Override
        default Semantics.MappingTransitions writeLink(final MORSpatialProperty property) {

            return new MORTryWrite<OWLNamedIndividual, OWLObjectProperty, MORSpatialRelation>( MORMultiSpatialDescriptor.this,
                    LOGGING.INTENT_linkSPATIAL_MULTI){
                @Override
                protected Semantics.MappingTransitions giveAtry() {
                    return UnsafeWriter.write( MORMultiSpatialDescriptor.this, property, getNewIntent( property.getProperty()));
                    //return getStateTransitions();
                }
            }.perform();
        }
        @Override
        default Semantics.MappingTransitions writeLinks() {
            return new MORTryWrite<OWLNamedIndividual, OWLObjectProperty, MORSpatialRelation>( MORMultiSpatialDescriptor.this,
                    LOGGING.INTENT_linkSPATIAL_MULTI){
                @Override
                protected Semantics.MappingTransitions giveAtry() {
                    Semantics.MappingTransitions out = new Semantics.MappingTransitions();
                    for ( MORSpatialProperty o : getLinksMap().keySet())
                        out.addAll( UnsafeWriter.write( MORMultiSpatialDescriptor.this, o, getNewIntent( o.getProperty())));
                    return out;//return getStateTransitions();
                }
            }.perform();
        }
        class UnsafeWriter {
            public static Semantics.MappingTransitions write(final MORMultiSpatialDescriptor descriptor,
                                                             final MORSpatialProperty property,
                                                             final Semantics.MappingIntent<OWLNamedIndividual,
                                                                     OWLObjectProperty,
                                                                     MORSpatialRelation,
                                                                     Semantics.WritingState> intent) {

                MORSpatialRelation javaRelations = descriptor.getLink(property);
                intent.setJavaValue( javaRelations);
                Set<OWLNamedIndividual> javaValue = null;
                if ( javaRelations != null)
                    javaValue = javaRelations.getItemsPrimitive();

                Set<OWLNamedIndividual> semanticValue = descriptor.getOntology()
                        .getObjectPropertyB2Individual( descriptor.getInstance(), property.getProperty());
                semanticValue.addAll( descriptor.getOntology()
                        .getObjectPropertyB2Individual( descriptor.getInstance(), property.getInverseProperty()));

                MORSpatialRelation semanticRelations = new MORSpatialRelation(descriptor.getOntology(), descriptor.getInstance(), descriptor.getBottomType(), property);
                Set< MORSpatialItem> semanticItem = new HashSet<>();
                for ( OWLNamedIndividual s : semanticValue){
                    semanticItem.add( new MORSpatialItem(descriptor.getOntology(), s, MORPrimitive.inferShape( descriptor.getOntology(), s)));
                }
                semanticRelations.addItems( semanticItem) ;
                intent.setSemanticValue(  semanticRelations);

                Semantics.MappingTransitions out = new Semantics.MappingTransitions( intent);

                if( javaValue != null) {
                    if (semanticValue != null && !semanticValue.isEmpty()) {
                        // both red, nothing changed
                        if( semanticValue.equals( javaValue)) {
                            intent.getState().asNotChanged();
                        } else {
                            HashSet< OWLNamedIndividual> semanticValueCopy = new HashSet<>(semanticValue);
                            for ( OWLNamedIndividual cl : javaValue){
                                MORMappingIntent<OWLNamedIndividual, MORSpatialProperty, Object, Semantics.WritingState>
                                        otherIntent = new MORMappingIntent<>(descriptor, property, new Semantics.WritingState());
                                otherIntent.setDescription( intent.getAtom().toString());
                                out.add( otherIntent);
                                otherIntent.setSemanticValue( null);
                                otherIntent.setJavaValue( cl);

                                if (semanticValue.contains( cl)){
                                    // both read, not changed
                                    otherIntent.getState().asNotChanged();
                                    // it does not do nothing on the set
                                    // take track of what has not beet added, to be removed later
                                    semanticValueCopy.remove( cl);
                                } else {
                                    // both read, added
                                    otherIntent.getState().asAdded();
                                    descriptor.getOntology().addObjectPropertyB2Individual( descriptor.getInstance(), property.getProperty(), cl);
                                    // add inverse spatial properties
                                    descriptor.getOntology().addObjectPropertyB2Individual( cl, property.getInverseProperty(), descriptor.getInstance());
                                }
                            }
                            for (OWLNamedIndividual rm : semanticValueCopy){
                                MORMappingIntent<OWLNamedIndividual, MORSpatialProperty, Object, Semantics.WritingState>
                                        otherIntent = new MORMappingIntent<>(descriptor, property, new Semantics.WritingState());
                                otherIntent.setDescription( intent.getAtom().toString());
                                out.add( otherIntent);
                                otherIntent.setSemanticValue( null);
                                otherIntent.setJavaValue( rm);

                                // remove remaining value
                                descriptor.getOntology().removeObjectPropertyB2Individual( descriptor.getInstance(), property.getProperty(), rm);
                                // remove inverse spatial properties
                                descriptor.getOntology().removeObjectPropertyB2Individual( rm, property.getInverseProperty(), descriptor.getInstance());
                                otherIntent.getState().asRemoved();
                            }
                        }
                    } else {
                        // nothing read, nothing changed
                        if ( javaValue.isEmpty()){
                            intent.getState().asNotChanged();
                        } else {
                            // nothing read, write all
                            for (OWLNamedIndividual cl : javaValue){
                                MORMappingIntent<OWLNamedIndividual, MORSpatialProperty, Object, Semantics.WritingState>
                                        otherIntent = new MORMappingIntent<>(descriptor, property, new Semantics.WritingState());
                                otherIntent.setDescription( intent.getAtom().toString());
                                out.add( otherIntent);
                                otherIntent.setSemanticValue( null);
                                otherIntent.setJavaValue( cl);

                                otherIntent.getState().asAdded();
                                descriptor.getOntology().addObjectPropertyB2Individual( descriptor.getInstance(), property.getProperty(), cl);
                                // write inverse
                                descriptor.getOntology().addObjectPropertyB2Individual( cl, property.getInverseProperty(), descriptor.getInstance());
                            }
                        }
                    }
                } else { // java cannot be read
                    Logger.logERROR( "Cannot write null types for: " + descriptor.getInstanceName());
                    intent.getState().asError();
                }
                return out;
            }
        }

    }


    class MORSpatialRelation extends SpatialSemantic.SpatialRelation<
            OWLClass, OWLNamedIndividual, OWLObjectProperty, MORSpatialProperty> {

        private OWLReferences ontoRef;



        public MORSpatialRelation(OWLReferences ontoRef, OWLNamedIndividual object, OWLClass type) {
            super(object, type);
            initialise( ontoRef, object, type, null);
        }
        public MORSpatialRelation(OWLReferences ontoRef, OWLNamedIndividual object, OWLClass type, OWLObjectProperty property) {
            super(object, type);
            initialise( ontoRef, object, type, property);
        }
        public MORSpatialRelation(OWLReferences ontoRef, String object, String type) {
            super(null, null);
            initialise( ontoRef, ontoRef.getOWLIndividual( object), ontoRef.getOWLClass( type), null);
        }
        public MORSpatialRelation(OWLReferences ontoRef, OWLNamedIndividual object, String type) {
            super(object, null);
            initialise( ontoRef, object, ontoRef.getOWLClass( type), null);
        }
        public MORSpatialRelation(OWLReferences ontoRef, String object, String type, String property) {
            super(null, null);
            initialise( ontoRef, ontoRef.getOWLIndividual( object), ontoRef.getOWLClass( type), ontoRef.getOWLObjectProperty( property));
        }
        public MORSpatialRelation(OWLReferences ontoRef, OWLNamedIndividual object, String type, String property) {
            super( object, null);
            initialise( ontoRef, object, ontoRef.getOWLClass( type), ontoRef.getOWLObjectProperty( property));
        }
        public MORSpatialRelation(OWLReferences ontoRef, OWLNamedIndividual object, OWLClass type, String property) {
            super( object, null);
            initialise( ontoRef, object, type, ontoRef.getOWLObjectProperty( property));
        }
        public MORSpatialRelation(OWLReferences ontoRef, OWLNamedIndividual object, OWLClass type, MORSpatialProperty property) {
            super( object, null);
            initialise( ontoRef, object, type, property.getProperty());
            setInverseProperty( property.getInverseProperty());
        }

        public void initialise( OWLReferences ontoRef, OWLNamedIndividual object, OWLClass type, OWLObjectProperty property) {
            this.ontoRef = ontoRef;
            this.setSubject( object);
            this.setType( type);
            this.setSceneRelation( new MORSpatialProperty( ontoRef, property));
            this.setItems( new HashSet<>());
        }

        /*public MORSpatialRelation( OWLReferences ontoRef, OWLNamedIndividual object, String property) {
            super( null, null);
            initialise( ontoRef,object, ontoRef.getOWLObjectProperty( property));
        }
        public MORSpatialRelation( OWLReferences ontoRef, OWLNamedIndividual object){
            super( null, null);
            this.setSubject( object);
            this.setType( MORPrimitive.inferShape( ontoRef, getSubject()));
        }
        public MORSpatialRelation( OWLReferences ontoRef, OWLNamedIndividual object, OWLObjectProperty property) {
            super( null, null);

            initialise( ontoRef, object, property);

            OWLClass c = MORPrimitive.inferShape( ontoRef, getSubject());
            this.setType( c);
        }*/

        public MORSpatialRelation(MORSpatialRelation copy) {
            super(copy);
            this.ontoRef = copy.ontoRef;
        }



        public String getObjectName(){
            return this.ontoRef.getOWLObjectName( this.getSubject());
        }
        public String getObjectName(int length){
            return getObjectName( length, false);
        }
        public String getObjectName(int length, boolean right){
            return Logger.padString( getObjectName(), length, right);
        }

        public String getTypeName(){
            return this.ontoRef.getOWLObjectName( this.getType());
        }
        public String getTypeName(int length){
            return getTypeName( length, true);
        }
        public String getTypeName(int length, boolean right){
            return Logger.padString( getTypeName(), length, right);
        }

        public String getPropertyName(){
            return this.ontoRef.getOWLObjectName( this.getRelation());
        }
        public String getPropertyName(int length){
            return getPropertyName( length, true);
        }
        public String getPropertyName(int length, boolean right){
            return Logger.padString( getPropertyName(), length, right);
        }

        public void setInverseProperty(String inverseProperty) {
            super.setInverseProperty( ontoRef.getOWLObjectProperty(inverseProperty));
        }

        @Override
        public Set<MORSpatialItem> queryItems() { // todo it does not query invers property !!!!!!
            // todo make it void
            Set<MORSpatialItem> items = new HashSet<>();
            for ( OWLNamedIndividual i : ontoRef.getObjectPropertyB2Individual( this.getSubject(), this.getRelation()))
                addItems( i, MORPrimitive.inferShape( ontoRef, i));
                //items.add( new MORSpatialItem( ontoRef, i, MORPrimitive.inferShape( ontoRef, i)));
            return items;
        }


        public MORSpatialCollector asSet() {
            MORSpatialCollector out = new MORSpatialCollector( this.ontoRef);
            out.add( this);
            return out;
        }

        @Override
        public String toString() {
            return "(" + getTypeName( LOGGING.LENGTH_SHORT)
                    + "#" +  getObjectName( LOGGING.LENGTH_NUMBER) + ")  "
                    + getPropertyName( LOGGING.LENGTH_MEDIUM) + " : " + getItems();
        }

        @Override
        public MORSpatialRelation copy() {
            return new MORSpatialRelation( this);
        }

        @Override
        public void addItems(Set< ? extends SpatialItem<OWLClass,OWLNamedIndividual>>  items) {
            for ( SpatialItem<OWLClass,OWLNamedIndividual> i : items)
                getItems().add( i);
        }
        /*public void addItems( OWLNamedIndividual items) {
            getItems().add( new MORSpatialItem( ontoRef, items));
        }*/
        public void addItems( OWLNamedIndividual items, OWLClass type) {
            getItems().add( new MORSpatialItem( ontoRef, items, type));
        }

        public void addItems(String items, String type) {
            getItems().add( new MORSpatialItem( ontoRef, items, type));
        }
    }

    class MORSpatialItem extends SITBase
            implements SpatialSemantic.SpatialItem<OWLClass, OWLNamedIndividual> {
        protected OWLReferences ontoRef;
        private OWLNamedIndividual object;
        private OWLClass type; // shape

        public MORSpatialItem(OWLReferences ontoRef, OWLNamedIndividual object, OWLClass type) {
            this.ontoRef = ontoRef;
            this.object = object;
            this.type = type;
        }
        public MORSpatialItem(OWLReferences ontoRef, String object, String type) {
            this.ontoRef = ontoRef;
            setObject( ontoRef.getOWLIndividual( object));
            setType( ontoRef.getOWLClass( type));
        }

        public MORSpatialItem(MORSpatialItem copy) {
            this.ontoRef = copy.ontoRef;
            this.type = copy.type;
            this.object = copy.object;
        }
        public MORSpatialItem( OWLReferences ontoRef, SpatialSemantic.SpatialItem<OWLClass, OWLNamedIndividual> copy) {
            this.ontoRef = ontoRef;
            this.object = copy.getObject();
            this.type = copy.getType();
        }

        public String getObjectName(){
            return this.ontoRef.getOWLObjectName( this.getObject());
        }
        public String getObjectName( int length){
            return padString( getObjectName(), length, false);
        }


        public String getTypeName(){
            return this.ontoRef.getOWLObjectName( this.getType());
        }
        public String getTypeName( int length){
            return padString( getTypeName(), length, true);
        }


        protected void setObject(OWLNamedIndividual object) {
            this.object = object;
        }
        protected void setType(OWLClass type) {
            this.type = type;
        }

        @Override
        public OWLNamedIndividual getObject() {
            return object;
        }
        @Override
        public OWLClass getType() {
            return type;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof SpatialSemantic.SpatialItem)) return false;

            SpatialSemantic.SpatialItem<?,?> that = (SpatialSemantic.SpatialItem<?,?>) o;

            if (getObject() != null ? !getObject().equals(that.getObject()) : that.getObject() != null) return false;
            return getType() != null ? getType().equals(that.getType()) : that.getType() == null;
        }
        @Override
        public int hashCode() {
            int result = getObject() != null ? getObject().hashCode() : 0;
            result = 31 * result + (getType() != null ? getType().hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return getTypeName( LOGGING.LENGTH_SHORT) + "#" + getObjectName( LOGGING.LENGTH_NUMBER);
        }

        @Override
        public MORSpatialItem copy() {
            return new MORSpatialItem( this);
        }
    }

    class MORSpatialProperty extends SITBase
            implements SpatialSemantic.SpatialProperty< OWLObjectProperty> {

        // if symmetric inverseProperty <- null
        // if symmetric getInverseRelations returns getRelation
        private OWLObjectProperty property, inverseProperty = null;
        private OWLReferences ontoRef;

        public MORSpatialProperty( MORSpatialProperty copy) {
            this.property = copy.property;
            this.inverseProperty = copy.inverseProperty;
        }
        public MORSpatialProperty( OWLReferences ontoRef){
            this.ontoRef = ontoRef;
        }
        public MORSpatialProperty( OWLReferences ontoRef, OWLObjectProperty property){
            this.ontoRef = ontoRef;
            setProperty( property);
        }
        public MORSpatialProperty( OWLReferences ontoRef, String property){
            this.ontoRef = ontoRef;
            setProperty( ontoRef.getOWLObjectProperty( property));
        }
        public MORSpatialProperty( OWLReferences ontoRef, OWLObjectProperty property, OWLObjectProperty inverseProperty){
            this.ontoRef = ontoRef;
            setProperty( property);
            setInverseProperty( inverseProperty);
        }
        public MORSpatialProperty( OWLReferences ontoRef, String property, String inverseProperty){
            this.ontoRef = ontoRef;
            setProperty( ontoRef.getOWLObjectProperty( property));
            setInverseProperty( ontoRef.getOWLObjectProperty( inverseProperty));
        }
        /*public MORSpatialProperty( OWLReferences ontoRef) {
            this.ontoRef = ontoRef;
        }
        public MORSpatialProperty( OWLReferences ontoRef, OWLObjectProperty property) {
            this.ontoRef = ontoRef;
            this.property = property;
        }
        public MORSpatialProperty( OWLReferences ontoRef, OWLObjectProperty property, OWLObjectProperty inverseProperty) {
            this.ontoRef = ontoRef;
            this.property = property;
            this.inverseProperty = inverseProperty;
        }*/

        public String getPropertyName(){
            if ( getProperty() == null)
                return null;
            return ontoRef.getOWLObjectName( getProperty());
        }
        public String getInversePropertyName(){
            if ( getInverseProperty() == null)
                return null;
            return ontoRef.getOWLObjectName( getInverseProperty());
        }

        @Override
        public void setProperty(OWLObjectProperty property) {
            this.property = property;
        }
        @Override
        public void setInverseProperty(OWLObjectProperty inverseProperty) {
            this.inverseProperty = inverseProperty;
        }
        @Override
        public void setSymmetric(){
            this.inverseProperty = null;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof MORSpatialProperty)) return false;

            MORSpatialProperty that = (MORSpatialProperty) o;

            if (getProperty() != null ? !getProperty().equals(that.getProperty()) : that.getProperty() != null)
                return false;
            return getInverseProperty() != null ? getInverseProperty().equals(that.getInverseProperty()) : that.getInverseProperty() == null;
        }
        @Override
        public int hashCode() {
            int result = getProperty() != null ? getProperty().hashCode() : 0;
            result = 31 * result + (getInverseProperty() != null ? getInverseProperty().hashCode() : 0);
            return result;
        }

        @Override
        public MORSpatialProperty copy() {
            return new MORSpatialProperty( this);
        }

        @Override
        public OWLObjectProperty getProperty() {
            return property;
        }
        @Override
        public OWLObjectProperty getInverseProperty() {
            if ( isSymmetric())
                return property;
            return inverseProperty;
        }
        @Override
        public boolean isSymmetric(){
            return  inverseProperty == null;
        }

        @Override
        public String toString() {
            String info = "";
            if ( ! isSymmetric())
                info += "(" + padString( getInversePropertyName(), LOGGING.LENGTH_SHORT, false) + ")^-1";
            return padString( getPropertyName(), LOGGING.LENGTH_MEDIUM, true) + info;

        }
    }

    class MORSpatialCollector extends SpatialSemantic.SpatialCollector< MORSpatialRelation>{

        private OWLReferences ontoRef;

        public MORSpatialCollector(MORSpatialCollector copy){
            this.ontoRef = copy.ontoRef;
            for( MORSpatialRelation rel : copy)
                add( new MORSpatialRelation( rel));
        }

        public MORSpatialCollector(OWLReferences ontoRef){
            this.ontoRef = ontoRef;
        }

        public boolean hasUniqueApplyingObject(){
            if( getApplyngObject().size() == 1)
                return true;
            else return false;
        }
        public Set<MORSpatialItem> getApplyngObject(){
            Set< MORSpatialItem> out = new HashSet<>();
            for( MORSpatialRelation r : this)
                for ( SpatialSemantic.SpatialItem<OWLClass,OWLNamedIndividual> i : r.getItems())
                    out.add((MORSpatialItem) i);
            return out;
        }

        public HashSet<MORSpatialRelation> clean(){
            Set< MORSpatialRelation> toRemove = new HashSet<>();
            for( MORSpatialRelation r : this)
                if( r.getItems().isEmpty())
                    toRemove.add( r);
            this.removeAll( toRemove);
            return this;
        }

        public SceneSemantic.Scene getSceneAtoms(){ // use getInverseProperty to simplify!!!!!!!
            Set<MORSceneDescriptor.MORSceneAtom> atoms = new HashSet<>();
            SceneSemantic.SceneCardinality cardinality = new SceneSemantic.SceneCardinality( ontoRef);
            for( MORSpatialRelation r : this){
                // try to add the subject of the relation
                MORSceneDescriptor.MORSceneAtom subject = new MORSceneDescriptor.MORSceneAtom( ontoRef, r.getSubject(), r.getInverseRelations(), r.getType());
                if( atoms.add( subject)) {
                    addRelationCardinality(subject.getPropertyName(), subject.getTypeName(), cardinality);
                    System.out.println("-----------'''''''------------------");
                }
                // try to add the the objects of the relations
                for( Object i : r.getItems()) {
                    if( i instanceof MORSpatialItem) {
                        MORSpatialItem ii = (MORSpatialItem) i;
                        MORSceneDescriptor.MORSceneAtom object = new MORSceneDescriptor.MORSceneAtom(ontoRef, ii.getObject(), r.getInverseRelations(), r.getType());
                        if (atoms.add(object))
                            addRelationCardinality(subject.getPropertyName(), ii.getTypeName(), cardinality);
                    }
                    else Logger.logWARNING( this.getClass().getSimpleName() + " cannot manage spatial object " +
                            "of type " + i.getClass().getSimpleName());

                }
                System.out.println( "-----------,,,,,------------------");
            }
            return new MORSceneDescriptor.MORScene( atoms, cardinality);
        }
        private void addRelationCardinality(String property, String type, SceneSemantic.SceneCardinality cardinality){
            if( property == null)
                return;
            if( property.equals(OBJECTPROPERTY.DEFAULT_SPATIAL_COAXIAL))
                addShapeCardinality( type, cardinality.getCoaxialCardinality());
            if( property.equals( OBJECTPROPERTY.DEFAULT_SPATIAL_PARALLEL))
                addShapeCardinality( type, cardinality.getParallelCardinality());
            if( property.equals( OBJECTPROPERTY.DEFAULT_SPATIAL_PERPENDICULAR))
                addShapeCardinality( type, cardinality.getPerpendicularCardinality());
            if( property.equals( OBJECTPROPERTY.DEFAULT_SPATIAL_LONGX))
                addShapeCardinality( type, cardinality.getAlongXcardinality());
            if( property.equals( OBJECTPROPERTY.DEFAULT_SPATIAL_LONGY))
                addShapeCardinality( type, cardinality.getAlongYcardinality());
            if( property.equals( OBJECTPROPERTY.DEFAULT_SPATIAL_LONGZ))
                addShapeCardinality( type, cardinality.getAlongZcardinality());
            if( property.equals( OBJECTPROPERTY.DEFAULT_SPATIAL_RIGHT))
                addShapeCardinality( type, cardinality.getRightCardinality());
            if( property.equals( OBJECTPROPERTY.DEFAULT_SPATIAL_ABOVE))
                addShapeCardinality( type, cardinality.getAboveCardinality());
            if( property.equals( OBJECTPROPERTY.DEFAULT_SPATIAL_BEHIND))
                addShapeCardinality( type, cardinality.getBehindCardinality());
        }
        private void addShapeCardinality(String type, SceneSemantic.ShapeCardinality cardinality){
            if( type.equals(CLASS.PRIMITIVE))
                cardinality.getPrimitiveCardinality().increase();
            if( type.equals( CLASS.SPHERE))
                cardinality.getSphereCardinality().increase();
            if( type.equals( CLASS.ORIENTBLE))
                cardinality.getOrientableCardinality().increase();
            if( type.equals( CLASS.PLANE))
                cardinality.getPlaneCardinality().increase();
            if( type.equals( CLASS.CONE))
                cardinality.getConeCardinality().increase();
            if( type.equals( CLASS.CYLINDER))
                cardinality.getCylinderCardinality().increase();

            System.out.println("££££££££££££££ " + type + "  " + cardinality);
        }

        @Override
        public String toString() {
            Set< MORSpatialItem> items = getApplyngObject();
            if( items.isEmpty())
                return "[empty spatial rations]";
            if( items.size() == 1) {
                String out = "query spatial properties for individual: " + getApplyngObject() + "{\n";
                int cnt = 0;
                for (MORSpatialRelation r : this) {
                    out += "   -" + (cnt + 1) + ".\t\t" + r.getPropertyName(LOGGING.LENGTH_LONG) + "." + r.getItems();
                    if (++cnt < size())
                        out += ";\n";
                    else out += "\n}";
                }
                return out;
            }
            String out = "query spatial properties: {\n";
            int cnt = 0;
            for (MORSpatialRelation r : this) {
                out += "   -" + (cnt + 1) + ".\t\t" + r;
                if (++cnt < size())
                    out += ";\n";
                else out += "\n}";
            }
            return out;
        }
    }
}
