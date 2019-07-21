package it.emarolab.sit.example.example.simpleSpatialScenario.sceneElement;

import it.emarolab.sit.example.example.simpleSpatialScenario.Point3D;

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
public class Cylinder  extends Orientable {

    double radius;
    double height;
    Point3D pointOnAxis;

    @Override
    protected String getNamePrefix() {
        return "R";
    }

    @Override
    public String getTypeName() {
        return "CYLINDER";
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

    public Point3D getPointOnAxis() {
        return pointOnAxis;
    }
    public void setPointOnAxis(Point3D pointOnAxis) {
        this.pointOnAxis = pointOnAxis;
    }

    @Override
    public boolean equals(Object o) {
            if (this == o) return true;
        if (!(o instanceof Cylinder)) return false;
        if (!super.equals(o)) return false;
        Cylinder Cylinder = (Cylinder) o;
        if (Double.compare(Cylinder.getRadius(), getRadius()) != 0) return false;
        if (Double.compare(Cylinder.getHeight(), getHeight()) != 0) return false;
        return getPointOnAxis() != null ? getPointOnAxis().equals(Cylinder.getPointOnAxis()) : Cylinder.getPointOnAxis() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        long temp;
        temp = Double.doubleToLongBits(getRadius());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getHeight());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (getPointOnAxis() != null ? getPointOnAxis().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return super.toString().replace("}","") + "radius=" + getRadius() + "height=" + getHeight() + "pointOnAxis=" + getPointOnAxis() + '}';
    }
}

