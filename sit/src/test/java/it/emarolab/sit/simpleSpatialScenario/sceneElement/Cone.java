package it.emarolab.sit.simpleSpatialScenario.sceneElement;

import it.emarolab.sit.simpleSpatialScenario.Point3D;

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
 * <b>date</b>:        20/07/19 <br>
 * </small></div>
 */
public class Cone extends Orientable {

    double radius;
    double height;
    Point3D apex;

    @Override
    protected String getNamePrefix() {
        return "C";
    }

    @Override
    public String getTypeName() {
        return "CONE";
    }

    public double getRadius() {
        return radius;
    }
    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getHeight() {
        return height;
    }
    public void setHeight(double height) {
        this.height = height;
    }

    public Point3D getApex() {
        return apex;
    }
    public void setApex(Point3D apex) {
        this.apex = apex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cone)) return false;
        if (!super.equals(o)) return false;
        Cone cone = (Cone) o;
        if (Double.compare(cone.getRadius(), getRadius()) != 0) return false;
        if (Double.compare(cone.getHeight(), getHeight()) != 0) return false;
        return getApex() != null ? getApex().equals(cone.getApex()) : cone.getApex() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        long temp;
        temp = Double.doubleToLongBits(getRadius());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getHeight());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (getApex() != null ? getApex().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return super.toString().replace("}","") + "radius=" + getRadius() + "height=" + getHeight() + "apex=" + getApex() + '}';
    }
}

