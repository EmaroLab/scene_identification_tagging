package it.emarolab.sit.simpleSpatialScenario.sceneElement;

import it.emarolab.sit.core.ElementInterface;
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
public class GeometricPrimitive implements ElementInterface{

    protected static long cnt = 0;

    private Point3D center;
    private String cntName = "-" + ((cnt < Long.MAX_VALUE - 1) ? cnt++: resetNameCnt());

    public void setCenter( Point3D center){
        this.center = center;
    }

    public Point3D getCenter(){
        return this.center;
    }


    @Override
    public String getInstanceName() {
        return getNamePrefix() + cntName;
    }

    protected String getNamePrefix() {
        return "P";
    }

    @Override
    public String getTypeName() {
        return "GEOMETRIC-PRIMITIVE";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GeometricPrimitive)) return false;
        if (!super.equals(o)) return false;
        GeometricPrimitive that = (GeometricPrimitive) o;
        return getCenter() != null ? getCenter().equals(that.getCenter()) : that.getCenter() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getCenter() != null ? getCenter().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return getInstanceName() + "@" + getTypeName() + ":{center=" + center + '}';
    }

    public static long resetNameCnt(){
        cnt = 0;
        return cnt;
    }
}
