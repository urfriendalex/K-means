package Vector;

import java.util.ArrayList;
import java.util.List;

public class Vector {

    private String _vectorClass;
    private List<Double> _values;
    private int _dimension;
    private Cluster cluster;

    public Vector(List<Double> values, String vectorClass) {
        this._vectorClass = vectorClass;
        this._values = values;
        this._dimension = values.size();
    }
    public Vector(List<Double> values) {
        this(values,"" );
    }

    public Vector(int dimension) {
        ArrayList<Double> vectorValues = new ArrayList<>();
        for (int i = 0; i < dimension; i++) {
            vectorValues.add(0.0);
        }
        this._vectorClass="";
        this._values=vectorValues;
        this._dimension=dimension;
    }

    public Vector sum(Vector anotherVector) {
        if (anotherVector.get_dimension() == get_dimension()) {
            Vector sumVector = new Vector(get_dimension());
            for (int i = 0; i < get_dimension(); i++) {
                sumVector.get_values().set(i, (anotherVector.get_values().get(i) + this.get_values().get(i)));
            }
            return sumVector;
        }
        else throw new RuntimeException("Operation cannot be done. Dimensions do not match");
    }

    public double calculateDistance(Vector anotherVetor){
        double sum = 0;
        for (int i = 0; i < _values.size(); i++)
            sum += Math.pow(_values.get(i) - anotherVetor.get_values().get(i), 2);
        return sum;
    }
    @Override
    public String toString() {
        return this.get_values() + ""; //+ "\tClass: " + get_vectorClass();
    }

    public String get_vectorClass() {
        return _vectorClass;
    }

    public List<Double> get_values() {
        return _values;
    }

    public int get_dimension() {
        return _dimension;
    }

    public Cluster getCluster() {
        return cluster;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    public double dotProduct(Vector anotherVector) {
        if (this.get_dimension() == anotherVector.get_dimension()) {
            double result = 0;
            for (int i = 0; i < this.get_dimension(); i++) {
                result += anotherVector.get_values().get(i) * this.get_values().get(i);
            }
            return result;
        }
        else throw new RuntimeException("Operation cannot be done. Dimensions need to match");
    }

    public Vector multiplyBy(double y) {
        Vector resultVector = new Vector(new ArrayList<>(this.get_values()));
        for (int i = 0; i < this.get_dimension(); i++) {
            resultVector.get_values().set(i, (resultVector.get_values().get(i) * y));
        }
        return resultVector;
    }
}