package com.cennavi.modules.pbf.pbfUtils;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 *
 <dependency>
 <groupId>com.vector</groupId>
 <artifactId>vector</artifactId>
 <version>1.0</version>
 <scope>system</scope>
 <systemPath>${basedir}/src/main/java/com/cennavi/modules/pbf/lib/vector-1.0.jar</systemPath>
 </dependency>
 */
public class TileUtils {
	
	/**
	 * 确认计算tile的标识，目前只支持  MERCATORTILE  和  WMTSTILE ;
	 * 
	 * 特需指出在打包的时候，需要确认该变量，可能导致计算错误
	 */
	private static String TILESYSTERM = "MERCATORTILE";   // MERCATORTILE
	
	public static String getTileSysterm(){
		return TILESYSTERM;
	}
	
	public static String parseXyz2Bound(int x, int y, int z) {
		if (getTileSysterm().equals("MERCATORTILE")) {
			
			return parseXyz2Bound_Mercator(x, y, z);
			
		} else if (getTileSysterm().equals("WMTSTILE")) {
			
			return parseXyz2Bound_WMTS(x, y, z);
			
		} else {
			return null;
		}
	}
	
	
	public static void convert2Piexl(int x, int y, int z, Geometry geom) {

		if (getTileSysterm().equals("MERCATORTILE")) {
			
			convert2Piexl_Mercator(x, y, z, geom);
			
		} else if (getTileSysterm().equals("WMTSTILE")){
			
			convert2Piexl_WMTS(x, y, z, geom);
			
		}
	}
	
	public static String parseXyz2Bound2(int x, int y, int z) {
		if (getTileSysterm().equals("MERCATORTILE")) {
			
			return parseXyz2Bound2_Mercator(x, y, z);
			
		} else if (getTileSysterm().equals("WMTSTILE")) {
			
			return parseXyz2Bound2_WMTS(x, y, z);
			
		} else {
			return null;
		}
	}
	
	public static Geometry convert2PiexlRoad(int x,int y,int z,Geometry geom) throws ParseException {
		if (getTileSysterm().equals("MERCATORTILE")) {
			
			return convert2PiexlRoad_Mercator(x, y, z, geom);
			
		} else if (getTileSysterm().equals("WMTSTILE")) {
			
			return convert2PiexlRoad_WMTS(x, y, z, geom);
			
		} else {
			return null;
		}
	}
	
	public static String parseXyz2Bound_Mercator(int x, int y, int z){
		StringBuilder sb = new StringBuilder("POLYGON ((");
		
		double lngLeft = MercatorProjection.tileXToLongitude(x, (byte)z);
		
		double latUp = MercatorProjection.tileYToLatitude(y, (byte)z);
		
		double lngRight = MercatorProjection.tileXToLongitude(x + 1, (byte)z);
		
		double latDown = MercatorProjection.tileYToLatitude(y + 1, (byte)z);
		
		sb.append(lngLeft +" "+latUp+", ");
		
		sb.append(lngRight +" "+latUp+", ");
		
		sb.append(lngRight +" "+latDown+", ");
		
		sb.append(lngLeft +" "+latDown+", ");
		
		sb.append(lngLeft +" "+latUp+")) ");
		
		return sb.toString();
	}
	
	public static String parseXyz2Bound_WMTS(int x, int y, int z){
		StringBuilder sb = new StringBuilder("POLYGON ((");
		
		double lngLeft = WMTSProjection.tileX2Lon(x, z);
		
		double latUp = WMTSProjection.tileY2Lat(y, z);
		
		double lngRight = WMTSProjection.tileX2Lon(x + 1, z);
		
		double latDown = WMTSProjection.tileY2Lat(y + 1, z);
		
		sb.append(lngLeft +" "+latUp+", ");
		
		sb.append(lngRight +" "+latUp+", ");
		
		sb.append(lngRight +" "+latDown+", ");
		
		sb.append(lngLeft +" "+latDown+", ");
		
		sb.append(lngLeft +" "+latUp+")) ");
		
		return sb.toString();
	}
	
	public static String parseXyz2Bound2_Mercator(int x,int y,int z){
		
		StringBuilder sb = new StringBuilder("POLYGON ((");
		
		double lngLeft = MercatorProjection.tileXToLongitude(x, (byte)z)-0.00105;
		
		double latUp = MercatorProjection.tileYToLatitude(y, (byte)z)+0.00105;
		
		double lngRight = MercatorProjection.tileXToLongitude(x + 1, (byte)z)+0.00105;
		
		double latDown = MercatorProjection.tileYToLatitude(y + 1, (byte)z) -0.00105;
		
		sb.append(lngLeft +" "+latUp+", ");
		
		sb.append(lngRight +" "+latUp+", ");
		
		sb.append(lngRight +" "+latDown+", ");
		
		sb.append(lngLeft +" "+latDown+", ");
		
		sb.append(lngLeft +" "+latUp+")) ");
		
		return sb.toString();
		
	}
	
	public static String parseXyz2Bound2_WMTS(int x,int y,int z){
		
		StringBuilder sb = new StringBuilder("POLYGON ((");
		
		double lngLeft = WMTSProjection.tileX2Lon(x, z)-0.00105;
		
		double latUp = WMTSProjection.tileY2Lat(y, z)+0.00105;
		
		double lngRight = WMTSProjection.tileX2Lon(x + 1, z)+0.00105;
		
		double latDown = WMTSProjection.tileY2Lat(y + 1, z) -0.00105;
		
		sb.append(lngLeft +" "+latUp+", ");
		
		sb.append(lngRight +" "+latUp+", ");
		
		sb.append(lngRight +" "+latDown+", ");
		
		sb.append(lngLeft +" "+latDown+", ");
		
		sb.append(lngLeft +" "+latUp+")) ");
		
		return sb.toString();
		
	}
	
	public static void convert2Piexl_Mercator(int x,int y,int z,Geometry geom){
		
		double px = MercatorProjection.tileXToPixelX(x);
		
		double py = MercatorProjection.tileYToPixelY(y);
		
		Coordinate[] cs = geom.getCoordinates();
		
		byte zoom = (byte)z;
		
		for(Coordinate c : cs){
			c.x = (int)(((MercatorProjection.longitudeToPixelX(c.x, zoom)) - px) * 16);
			
			c.y = (int)(((MercatorProjection.latitudeToPixelY(c.y, zoom)) - py) * 16);
			
		}
	}

	public static void convert2Piexl_WMTS(int x,int y,int z,Geometry geom){
		
		double px = WMTSProjection.tileX2PixelX(x);
		
		double py = WMTSProjection.tileY2PixelY(y);
		
		Coordinate[] cs = geom.getCoordinates();
		
		byte zoom = (byte)z;
		
		for(Coordinate c : cs){
			c.x = (int)(((WMTSProjection.lon2PixelX(c.x, zoom)) - px) * 16);
			
			c.y = (int)(((WMTSProjection.lat2PixelY(c.y, zoom)) - py) * 16);
		}
	}
	
	public static Geometry convert2PiexlRoad_Mercator(int x,int y,int z,Geometry geom) throws ParseException {
		
		double px = MercatorProjection.tileXToPixelX(x);
		
		double py = MercatorProjection.tileYToPixelY(y);
		
		Coordinate[] cs = geom.getCoordinates();
		
		byte zoom = (byte)z;
		
		for(Coordinate c : cs){
			c.x = (int)(((MercatorProjection.longitudeToPixelX(c.x, zoom)) - px) * 16);
			
			c.y = (int)(((MercatorProjection.latitudeToPixelY(c.y, zoom)) - py) * 16);
		}
		
		StringBuilder sb = new StringBuilder("LINESTRING(");
		
		Set<String> set = new HashSet<String>();
		
		for(int i=0;i<cs.length-1;i++){
			Coordinate c = cs[i];
			set.add((int)c.x +" " +(int)c.y+",");
		}
		
		Iterator<String> it = set.iterator();
		
		while(it.hasNext()){
			sb.append(it.next());
		}
		
		int len = cs.length;
		
		Coordinate last = cs[len-1];
		
		sb.append((int)last.x+" "+(int)last.y+")");
		
		Geometry newGeom = new WKTReader().read(sb.toString());

		return newGeom;
	}
	
	public static Geometry convert2PiexlRoad_WMTS(int x,int y,int z,Geometry geom) throws ParseException {
		
		double px = WMTSProjection.tileX2PixelX(x);
		
		double py = WMTSProjection.tileY2PixelY(y);
		
		Coordinate[] cs = geom.getCoordinates();
		
		byte zoom = (byte)z;
		
		for(Coordinate c : cs){
			c.x = (int)(((WMTSProjection.lon2PixelX(c.x, zoom)) - px) * 16);
			
			c.y = (int)(((WMTSProjection.lat2PixelY(c.y, zoom)) - py) * 16);
		}
		
		StringBuilder sb = new StringBuilder("LINESTRING(");
		
		Set<String> set = new HashSet<String>();
		
		for(int i=0;i<cs.length-1;i++){
			Coordinate c = cs[i];
			set.add((int)c.x +" " +(int)c.y+",");
		}
		
		Iterator<String> it = set.iterator();
		
		while(it.hasNext()){
			sb.append(it.next());
		}
		
		int len = cs.length;
		
		Coordinate last = cs[len-1];
		
		sb.append((int)last.x+" "+(int)last.y+")");
		
		Geometry newGeom = new WKTReader().read(sb.toString());

		return newGeom;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		
		double dx = MercatorProjection.longitudeToTileX(120.61002, (byte)15);
		double dy = MercatorProjection.latitudeToTileY(31.22837, (byte)15);
		
		WKTReader reader = new WKTReader();
		
		Geometry geom = reader.read("POLYGON ((101.25 48.92249926375824, 112.5 48.92249926375824, 112.5 40.97989806962013, 101.25 40.97989806962013, 101.25 48.92249926375824))");

//		System.out.println(MercatorProjection.longitudeToTileX((101.25 + 112.5)/2, (byte)5));
//		
//		System.out.println(MercatorProjection.latitudeToTileY((48.92249926375824 + 40.97989806962013)/2, (byte)5));
		
		convert2Piexl(25, 11, 5, geom);
		
	}

}
