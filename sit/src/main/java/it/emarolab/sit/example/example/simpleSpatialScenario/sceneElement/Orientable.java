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
public class Orientable extends GeometricPrimitive{

    private Point3D axis;

    @Override
    protected String getNamePrefix() {
        return "O";
    }

    @Override
    public String getTypeName() {
        return "ORIENTABLE";
    }

    public void setAxis( Point3D axis){
        this.axis = axis;
    }

    public Point3D getAxis(){
        return this.axis;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Orientable)) return false;
        if (!super.equals(o)) return false;

        Orientable that = (Orientable) o;

        return getAxis() != null ? getAxis().equals(that.getAxis()) : that.getAxis() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getAxis() != null ? getAxis().hashCode() : 0);
        return result;
    }


    @Override
    public String toString() {
        return super.toString().replace("}","") + "axis=" + getAxis() + '}';
    }
}
