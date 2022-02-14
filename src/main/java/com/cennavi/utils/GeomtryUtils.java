package com.cennavi.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2017-6-23.
 *
 <dependency>
 <groupId>com.vividsolutions</groupId>
 <artifactId>jts</artifactId>
 <version>1.13</version>
 </dependency>
 */
public class GeomtryUtils {

    private final static double EARTH_RADIUS = 6378.137;// 地球半径

    public static void main(String[] args) {
        String ss = "POLYGON ((108.92243546095574 34.27648724109814, 108.92230531949508 34.27648158111033, 108.91230531949508 34.23648158111033, 108.92430531949508 34.25648158111033, 108.92243546095574 34.27648724109814))";
//        String ss = "LINESTRING (108.92243546095574 34.27648724109814, 108.92230531949508 34.27648158111033)";
//        String ss = "{\"type\":\"LineString\",\"coordinates\":[[108.92243546095574,34.27648724109814],[108.92230531949508,34.27648158111033]]}";
//        String ss = "{\"type\":\"Polygon\",\"coordinates\":[[[108.92243546095574,34.27648724109814],[108.92230531949508,34.27648158111033],[108.91230531949508,34.23648158111033],[108.92430531949508,34.25648158111033],[108.92243546095574,34.27648724109814]]]}";
//        System.out.println(geoJson2Wkt(ss));
        System.out.println(wkt2GeoJson(ss));
        System.out.println(getCenterOfPolygon(ss));
       /* BuffersTestData bs = new BuffersTestData();
        String line1 = "LINESTRING (108.92230531949508 34.27653818095176,108.92242861140272 34.27653252096741)";
        Geometry g1 = bs.buildGeo(line1);
        //方式(一)
//        Geometry g = g1.buffer(0.00002);
//        System.out.println(g.toString());
        ////方式(二) BufferOP
        BufferOp bufOp = new BufferOp(g1);
        bufOp.setEndCapStyle(BufferOp.CAP_BUTT);
        Geometry bg = bufOp.getResultGeometry(0.00002);
        System.out.println(bg.toString());*/
    }

    /**
     * 将WKT转换为geojson
     * @param wkt
     * @return string(geojson格式)
     */
    public static String wkt2GeoJson(String wkt) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode geometry = JsonNodeFactory.instance.objectNode();

            WKTReader wktReader = new WKTReader();
            if(StringUtils.isNotBlank(wkt)) {
                if(wkt.toUpperCase().contains("LINESTRING")){
                    LineString lineString = null;
                    List<List<Double>> coordinates = new ArrayList<>();
                    try {
                        lineString = (LineString) wktReader.read(wkt);
                        Coordinate[] lineStringCoordinates = lineString.getCoordinates();
                        for (Coordinate coor: lineStringCoordinates) {
                            List<Double> c = new ArrayList<>();
                            c.add(coor.x);
                            c.add(coor.y);
                            coordinates.add(c);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    geometry.put("type", "LineString");
                    geometry.set("coordinates", mapper.readTree(mapper.writeValueAsString(coordinates)));
                }else if(wkt.toUpperCase().contains("POINT")){
                    Point point = null;
                    try {
                        point = (Point) wktReader.read(wkt);
                        List<Double> coordinates = new ArrayList<>();
                        Coordinate[] pointCoordinates = point.getCoordinates();
                        for (Coordinate coor: pointCoordinates) {
                            coordinates.add(coor.x);
                            coordinates.add(coor.y);
                        }
                        geometry.put("type", "Point");
                        //geometry.put("coordinates", coordinates);
                        geometry.set("coordinates", mapper.readTree(mapper.writeValueAsString(coordinates)));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }else if(wkt.toUpperCase().contains("POLYGON")){
                    Polygon lineString = null;
                    List<List<Double[]>> coordinates = new ArrayList<>();
                    try {
                        lineString = (Polygon) wktReader.read(wkt);
                        Coordinate[] lineStringCoordinates = lineString.getCoordinates();
                        List<Double[]> c = new ArrayList<>();
                        for (Coordinate coor: lineStringCoordinates) {
                            c.add(new Double[]{coor.x,coor.y});
                        }
                        coordinates.add(c);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    geometry.put("type", "Polygon");
                    //geometry.put("coordinates", coordinates);
                    geometry.set("coordinates", mapper.readTree(mapper.writeValueAsString(coordinates)));
                }
            }
            return geometry.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 判断点是否在面内
     * @param  point 点
     * @param geometry  面
     * @return boolean
     */
    public static boolean pointInPolygon(String point, String geometry){
        WKTReader reader = new WKTReader();
        try {
            Geometry points = reader.read(point);
            Geometry poly = reader.read(geometry);
            //poly.contains(points); //返回true或false
            return poly.contains(points);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 判断点是否在面内
     * @param geometry  面
     * @return boolean
     */
    public static boolean pointInPolygon(double lon, double lat, Geometry geometry){
        WKTReader reader = new WKTReader();
        try {
            Geometry points = reader.read("point("+lon +" "+lat+")");
            return geometry.contains(points);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 查看是否包含
     * @param point 点
     * @param dis 度
     * @param geometry 形状几何
     * @return
     */
    public static boolean isWithinDistance(Point point,Double dis,Geometry geometry){
        return point.isWithinDistance(geometry, dis);
    }

    /**
     * 计算两点之间实际距离
     *
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return 米
     */
    public static double GetDistance(double lng1, double lat1, double lng2, double lat2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        if (a == 0 && b == 0)
            return 0;
        double s = 2 * Math.asin(Math.sqrt(
                Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        // s = Math.round(s * 1) / 1;
        s = s * 1000;
        return Math.floor(s);
    }

    public static double GetDistance(String wkt1, String wkt2) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode coord1 = mapper.readTree(wkt1).get("coordinates");
        double lng1 = coord1.get(0).asDouble();
        double lat1 = coord1.get(1).asDouble();
        JsonNode coord2 = mapper.readTree(wkt2).get("coordinates");
        double lng2 = coord2.get(0).asDouble();
        double lat2 = coord2.get(1).asDouble();

        /*List<Double> list = (List<Double>)JSONObject.parseObject(wkt1).get("coordinates");
        double lng1 = Double.parseDouble(list.get(0)+"");
        double lat1 = Double.parseDouble(list.get(1)+"");
        List<Double> list2 = (List<Double>)JSONObject.parseObject(wkt2).get("coordinates");
        double lng2 = Double.parseDouble(list2.get(0)+"");
        double lat2 = Double.parseDouble(list2.get(1)+"");*/

        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        if (a == 0 && b == 0)
            return 0;
        double s = 2 * Math.asin(Math.sqrt(
                Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        // s = Math.round(s * 1) / 1;
        s = s * 1000;
        BigDecimal decimal = new BigDecimal(s);
        //结果保留2位小数
        s = decimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return Math.floor(s);
    }


    /**
     * 获取不规则多边形重心点
     * @param wkt
     * @return
     */
    public static String getCenterOfPolygon(String wkt) {
        Polygon polygon = null;
        WKTReader reader = new WKTReader();
        double area = 0.0;//多边形面积
        double Gx = 0.0, Gy = 0.0;// 重心的x、y
        try {
            polygon = (Polygon) reader.read(wkt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Coordinate[] list = polygon.getCoordinates();
        for (int i = 1; i <= list.length; i++) {
            double iLng = list[i % list.length].x;
            double iLat = list[i % list.length].y;
            double nextLng = list[i - 1].x;
            double nextLat = list[i - 1].y;
            double temp = (iLat * nextLng - iLng * nextLat) / 2.0;
            area += temp;
            Gx += temp * (iLng + nextLng) / 3.0;
            Gy += temp * (iLat + nextLat) / 3.0;
        }
        Gx = Gx / area;
        Gy = Gy / area;
        return Gx+","+Gy;
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

}
