package it.emarolab.sit.example.owloopDescriptor;

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
public class MemorySceneClassified extends MemorySceneDescriptor {

    private double threshold = 0;
    private double similarity = 0;

    public MemorySceneClassified(MemorySceneDescriptor descriptor, double similarity, double threshold) {
        super(descriptor.getInstance(), descriptor.getOntology());
        this.getRestrictionConcepts().addAll(descriptor.getRestrictionConcepts());
        this.getSubConcepts().addAll(descriptor.getSubConcepts());
        this.getSuperConcepts().addAll(descriptor.getSuperConcepts());
        this.getIndividualInstances().addAll(descriptor.getIndividualInstances());
        this.threshold = threshold;
        this.similarity = similarity;
    }

    public double getSimilarity() {
        return similarity;
    }

    public boolean highConfidence() {
        return getSimilarity() > threshold;
    }


    @Override
    public void printDescriptions() {
        super.printDescriptions();
        System.out.println( " ---> similarity: " + similarity+ " (>" + +threshold + ")");
    }

    @Override
    public String toString() {
        return getGroundInstanceName();
    }
}
