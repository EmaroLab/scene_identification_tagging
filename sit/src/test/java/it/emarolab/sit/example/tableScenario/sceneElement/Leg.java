package it.emarolab.sit.example.tableScenario.sceneElement;

import it.emarolab.sit.example.core.ElementInterface;
import it.emarolab.sit.example.simpleSpatialScenario.Point3D;

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
public class Leg extends SpatialObject {

    @Override
    protected String getNamePrefix() {
        return "L";
    }

    @Override
    public String getTypeName() {
        return "LEG";
    }
}
