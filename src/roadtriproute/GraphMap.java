/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roadtriproute;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MVCArray;
import com.lynden.gmapsfx.javascript.object.MapOptions;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;
import com.lynden.gmapsfx.shapes.Polyline;
import com.lynden.gmapsfx.shapes.PolylineOptions;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import static java.lang.Double.parseDouble;
import java.util.ArrayList;


public class GraphMap {
    private GoogleMapView mapView;
    private GoogleMap map;
    public static final int MAX_VERTEX = 200;
    private ArrayList<Vertex> intersectionList;
    private ArrayList<Vertex> solution;
    private Graph GeographicGraph;
    
    public GraphMap(GoogleMapView mapView, GoogleMap map, String IntersectionFile) {
        this.mapView = mapView;
        this.map = map;
        GeographicGraph = new Graph(MAX_VERTEX);
        intersectionList = new ArrayList<Vertex>(MAX_VERTEX);
        
        try (BufferedReader br = new BufferedReader(new FileReader("data/"+IntersectionFile))) {
            String CurrentLine;
            while (!(CurrentLine = br.readLine()).equals("***")) {
                String [] res = CurrentLine.split(" ");
                int idVertex = Integer.parseInt(res[0]);
                Double latitude = parseDouble(res[1]);
                Double longitude = parseDouble(res[2]);
                Vertex V = new Vertex(idVertex, latitude, longitude);
                intersectionList.add(V);
                GeographicGraph.addVertex(latitude, longitude);
            }
            while ((CurrentLine = br.readLine()) != null) {
                String [] res = CurrentLine.split(" ");
                Integer idV1 = Integer.parseInt(res[0]);
                Integer idV2 = Integer.parseInt(res[1]);
                Vertex V1 = GeographicGraph.getVertex(idV1);
                Vertex V2 = GeographicGraph.getVertex(idV2);
                GeographicGraph.AddEdge(V1, V2);
                System.out.println(res[0]);
            }
	} catch (IOException e) {
            e.printStackTrace();
	}
        
        intersectionList = GeographicGraph.getVertices();
    }
    
    //getter Graph
    public Graph getGeographicGraph() {
        return GeographicGraph;
    }
    
    //Show intersections markers
    public void DisplayIntersections(String fileChoise) {
        MapOptions mapOptions = new MapOptions();
        if (fileChoise.equals("ITB"))
            mapOptions.center(new LatLong(-6.891943, 107.610395))
                .overviewMapControl(false)
                .panControl(true)
                .rotateControl(false)
                .streetViewControl(false)
                .zoomControl(false)
                .zoom(17);
        else
            mapOptions.center(new LatLong(-6.921945, 107.607061))
                .overviewMapControl(false)
                .panControl(true)
                .rotateControl(false)
                .streetViewControl(false)
                .zoomControl(false)
                .zoom(17);
        map = mapView.createMap(mapOptions);
        //Add a marker to the map
        MarkerOptions markerOptions = new MarkerOptions();
        int i = 0;
        for (Vertex V: intersectionList) {
            markerOptions.position( new LatLong(V.getLatitude(), V.getLongitude()) )
                    .visible(Boolean.TRUE)
                    .title(Integer.toString(i));
            Marker marker = new Marker( markerOptions );
            map.addMarker(marker);
            i++;
        }
    }
    
    //Visualize Route
    public double DisplayRoute(int startPoint, int stopPoint) {
        double jarak = 0.0;
        if (startPoint != -1 && stopPoint != -1) {
            solution = GeographicGraph.AStar(startPoint, stopPoint);
            for (int i=0; i<solution.size()-1; i++) {
                LatLong poly1 = new LatLong(solution.get(i).getLatitude(), solution.get(i).getLongitude());
                LatLong poly2 = new LatLong(solution.get(i+1).getLatitude(), solution.get(i+1).getLongitude());
                jarak += solution.get(i).getDistance(solution.get(i+1));
                LatLong[] pAry = new LatLong[]{poly1, poly2};
                MVCArray pmvc = new MVCArray(pAry);
                PolylineOptions line = new PolylineOptions()
                                      .path(pmvc)    
                                      .strokeWeight(5)
                                      .strokeColor("red")
                                      .visible(true);
                Polyline poly = new Polyline(line);
                map.addMapShape(poly);
            }
        } else
            System.out.println("No start and stop Point.");
        return jarak;
    }
}
