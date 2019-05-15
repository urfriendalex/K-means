package Vector;



public class Cluster {
    private int _clusterNumber;
    private Vector _centroid;

    public Cluster(int clusterNumber){
        _clusterNumber = clusterNumber;
    }

    public int get_clusterNumber() {
        return _clusterNumber;
    }

    public Vector get_centroid() {
        return _centroid;
    }

    public void set_centroid(Vector _centroid) {
        this._centroid = _centroid;
    }

    @Override
    public String toString() {
        return "Cluster number: " + _clusterNumber;
    }
}
