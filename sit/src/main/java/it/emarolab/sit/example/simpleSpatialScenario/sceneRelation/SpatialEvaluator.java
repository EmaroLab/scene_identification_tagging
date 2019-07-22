package it.emarolab.sit.example.simpleSpatialScenario.sceneRelation;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.sit.example.simpleSpatialScenario.sceneElement.GeometricPrimitive;
import it.emarolab.sit.example.simpleSpatialScenario.sceneElement.Orientable;

import java.util.HashSet;
import java.util.Set;

/**
 * ...
 * <p>
 * ...
 * <p>
 * <div style="text-align:center;"><small>
 * <b>File</b>:        ${FILE} <br>
 * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
 * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
 * <b>affiliation</b>: DIBRIS, EMAROLab, University of Genoa. <br>
 * <b>date</b>:        21/07/19 <br>
 * </small></div>
 */
public class SpatialEvaluator {

    // TODO make them ros parameters
    private static final double SPATIAL_INTERVAL = .03; // in meters
    private static final double AXIS_INTERVAL = .05; // in ???? (based on axis scalar and dot product)

    private Set<GeometricPrimitive> elements = new HashSet<>();
    private Set<SpatialRelation> relations = new HashSet<>();
    private OWLReferences ontology = null;

    public SpatialEvaluator(){}// evaluate() maunally
    public SpatialEvaluator(Set<GeometricPrimitive> elements, OWLReferences ontology){
        this.elements = elements;
        this.ontology = ontology;
        this.evaluate();
    }

    public Set<GeometricPrimitive> getElements() {
        return elements;
    }

    public Set<SpatialRelation> getRelations() {
        return relations;
    }

    public void evaluate(){
        for (GeometricPrimitive g1 : elements) {
            for (GeometricPrimitive g2 : elements) {
                if (!g1.equals(g2)) {
                    if (g1.getCenter() != null & g2.getCenter() != null) {
                        // asymmetric
                        evaluateRightLeft(g1, g2);
                        evaluateBehindFront(g1, g2);
                        evaluateAboveBelow(g1, g2);
                        // symmetric
                        evaluateAlignedX(g1, g2);
                        evaluateAlignedY(g1, g2);
                        evaluateAlignedZ(g1, g2);
                    }
                    if (g1 instanceof Orientable & g2 instanceof Orientable) {
                        Orientable o1 = (Orientable) g1;
                        Orientable o2 = (Orientable) g2;
                        if ( o1.getAxis() != null & o2.getAxis() != null) {
                            evaluateParallel(o1, o2);
                            evaluatePerpendicular(o1, o2);
                        }
                    }
                    if (g1 instanceof Orientable) {
                        Orientable o1 = (Orientable) g1;
                        if ( o1.getAxis() != null & g2.getCenter() != null)
                            evaluateCoaxial( o1, g2);
                    }
                    if (g2 instanceof Orientable) {
                        Orientable o2 = (Orientable) g2;
                        if ( o2.getAxis() != null  & g1.getCenter() != null)
                            evaluateCoaxial( o2, g1);
                    }

                }
            }
        }
    }

    // asymmetric relations
    private void evaluateRightLeft(GeometricPrimitive domain, GeometricPrimitive range) {
        // since in the scenario there are symmetric relations we need to consider also the inverse
        evaluateSquaredRelation( "right", domain.getCenter().getY(), domain, range.getCenter().getY(), range);
        evaluateSquaredRelation( "left", range.getCenter().getY(), range, domain.getCenter().getY(), domain);
    }
    private void evaluateBehindFront(GeometricPrimitive domain, GeometricPrimitive range) {
        evaluateSquaredRelation( "behind", domain.getCenter().getX(), domain, range.getCenter().getX(), range);
        evaluateSquaredRelation( "front", range.getCenter().getX(), range, domain.getCenter().getX(), domain);
    }
    private void evaluateAboveBelow(GeometricPrimitive domain, GeometricPrimitive range) {
        evaluateSquaredRelation( "above", domain.getCenter().getZ(), domain, range.getCenter().getZ(), range);
        evaluateSquaredRelation( "below", range.getCenter().getZ(), range, domain.getCenter().getZ(), domain);
    }
    private void evaluateSquaredRelation( String relation, double d, GeometricPrimitive dElement, double r, GeometricPrimitive rElement){
        if ( d - r >= SPATIAL_INTERVAL)
            relations.add( new SpatialRelation( dElement, relation, rElement, ontology));
    }
    // symmetric relations
    private void evaluateAlignedX(GeometricPrimitive domain, GeometricPrimitive range) {
        if ( Math.abs( domain.getCenter().getY() - range.getCenter().getY()) <= SPATIAL_INTERVAL
                & Math.abs( domain.getCenter().getZ() - range.getCenter().getZ()) <= SPATIAL_INTERVAL) {
            relations.add( new SpatialRelation( domain, "alongX", range, ontology));
            relations.add( new SpatialRelation( range, "alongX", domain, ontology));
        }
    }
    private void evaluateAlignedY(GeometricPrimitive domain, GeometricPrimitive range) {
        if ( Math.abs( domain.getCenter().getX() - range.getCenter().getX()) <= SPATIAL_INTERVAL
                & Math.abs( domain.getCenter().getZ() - range.getCenter().getZ()) <= SPATIAL_INTERVAL) {
            relations.add( new SpatialRelation( domain, "alongY", range, ontology));
            relations.add( new SpatialRelation( range, "alongY", domain, ontology));
        }
    }
    private void evaluateAlignedZ(GeometricPrimitive domain, GeometricPrimitive range) {
        if ( Math.abs( domain.getCenter().getY() - range.getCenter().getY()) <= SPATIAL_INTERVAL
                & Math.abs( domain.getCenter().getX() - range.getCenter().getX()) <= SPATIAL_INTERVAL) {
            relations.add(new SpatialRelation(domain, "alongZ", range, ontology));
            relations.add(new SpatialRelation(range, "alongZ", domain, ontology));
        }
    }
    private void evaluateParallel(Orientable domain, Orientable range) {
        double dx = domain.getAxis().getX();
        double dy = domain.getAxis().getY();
        double dz = domain.getAxis().getZ();
        double rx = range.getAxis().getX();
        double ry = range.getAxis().getY();
        double rz = range.getAxis().getZ();
        if( Math.abs( (dy * rz) - (dz * ry)) <= AXIS_INTERVAL & Math.abs( (dz * rx) - (dx * rz)) <= AXIS_INTERVAL
                & Math.abs( (dx * ry) - (dy * rx)) <= AXIS_INTERVAL) {
            relations.add(new SpatialRelation(domain, "parallel", range, ontology));
            relations.add(new SpatialRelation(range, "parallel", domain, ontology));
        }
    }
    private void evaluatePerpendicular(Orientable domain, Orientable range) {
        double dx = domain.getAxis().getX();
        double dy = domain.getAxis().getY();
        double dz = domain.getAxis().getZ();
        double rx = range.getAxis().getX();
        double ry = range.getAxis().getY();
        double rz = range.getAxis().getZ();

        if( Math.abs( dx * rx + dy * ry + dz * rz) <= AXIS_INTERVAL) {
            relations.add(new SpatialRelation(domain, "perpendicular", range, ontology));
            relations.add(new SpatialRelation(range, "perpendicular", domain, ontology));
        }
    }
    private void evaluateCoaxial(Orientable domain, GeometricPrimitive range) {
        double dx = domain.getCenter().getX();
        double dy = domain.getCenter().getY();
        double dz = domain.getCenter().getZ();
        double dax = domain.getAxis().getX();
        double day = domain.getAxis().getY();
        double daz = domain.getAxis().getZ();
        double rx = range.getCenter().getX();
        double ry = range.getCenter().getY();
        double rz = range.getCenter().getZ();
        double ggx = dax * (rx - dx);
        double ggy = day * (ry - dy);
        double ggz = daz * (rz - dz);
        double tabx = dax * (ggx + ggy + ggz) / ((dax * dax) + (day * day) + (daz * daz)) - rx + dx;
        double taby = day * (ggx + ggy + ggz) / ((dax * dax) + (day * day) + (daz * daz)) - ry + dy;
        double tabz = daz * (ggx + ggy + ggz) / ((dax * dax) + (day * day) + (daz * daz)) - rz + dz;
        if( tabx * tabx + taby * taby + tabz * tabz <= AXIS_INTERVAL) {
            relations.add(new SpatialRelation(domain, "coaxial", range, ontology));
            relations.add(new SpatialRelation(range, "coaxial", domain, ontology));
        }
    }

}
