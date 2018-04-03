package roadtriproute;

/**
 * kelas Vertex
 */
public class Vertex{
	private int index;
	private double latitude;
	private double longitude;
        
        //Konstruktor
	public Vertex(double latitude, double longitude, int index){
		this.index = index;
		this.latitude = latitude;
		this.longitude = longitude;
	}
        /**
         * Getter
         */
	public int getIndex(){
		return index;
	}
	public double getLatitude(){
		return this.latitude;
	}
	public double getLongitude(){
		return this.longitude;
	}
	public double getDistance(Vertex V2){
		return (Math.sqrt(Math.abs((V2.latitude - latitude)*(V2.latitude - latitude) + (V2.longitude - longitude)*(V2.longitude-longitude))));
	}
}
