package it.emarolab.sit.simpleSpatialScenario.sceneElement;

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
public class Plane extends Orientable {

    double hessian;

    @Override
    protected String getNamePrefix() {
        return "P";
    }

    @Override
    public String getTypeName() {
        return "PLANE";
    }

    public double getHessian() {
        return hessian;
    }

    public void setHessian(double hessian) {
        this.hessian = hessian;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Plane)) return false;
        if (!super.equals(o)) return false;
        Plane plane = (Plane) o;
        return Double.compare(plane.getHessian(), getHessian()) == 0;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        long temp;
        temp = Double.doubleToLongBits(getHessian());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return super.toString().replace("}","") + "hessian=" + getHessian() + '}';
    }
}
