package com.cennavi.modules.cross;

import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

/**
 * Created by sunpengyan on 2017/5/22.
 * Updated by sunpengyan on 2021/12/16.
 * 增加单侧扩充线，单侧扩线连成面，优化双侧扩线连成面
 */
public class JtsTools{
    private static GeometryFactory gf = new GeometryFactory();

    private static WKTReader wktReader = new WKTReader();

    public static com.vividsolutions.jts.geom.Point createPoint(double lon, double lat) {
        return gf.createPoint(new Coordinate(lon, lat));
    }
    /**
     * 判断点point(x,y)是否在Line加宽后的Polygon中
     * @param lon
     * @param lat
     * @param line  线的坐标集合字符串，
     * @return
     */
    public static boolean within(double lon, double lat, String line) {
        Polygon polygon = null;
        if(line.contains("LineString") || line.contains("linestring")) {
            polygon = CalculatePolygonByWktLine(line);
        }else {
            polygon = CalculatePolygonByLine(line);
        }
        com.vividsolutions.jts.geom.Point point = gf.createPoint(new Coordinate(lon, lat));
        // return point.within(polygon);
        return polygon.contains(point);
    }

    /**
     * 判断点point(x,y)是否在Polygon中
     * @param polygon  线的坐标集合字符串，
     * @return
     */
    public static boolean within(com.vividsolutions.jts.geom.Point point, Polygon polygon) {
        return polygon.contains(point);
    }

    /**
     * 判断点point(x,y)是否在Polygon中
     * @param lon
     * @param lat
     * @param coors  点的坐标集合字符串，
     * @return
     */
    public static boolean within(double lon, double lat, Coordinate[] coors) {
        Point point = gf.createPoint(new Coordinate(lon, lat));
        LinearRing linearRing = gf.createLinearRing(coors);
        Polygon polygon = gf.createPolygon(linearRing,null);
        return within(point,polygon);
    }

    /**
     *  根据线计算面
     * @param line String  "116.39158,39.91232;116.39163,39.91199"
     * @return Polygon
     */
    public static Polygon CalculatePolygonByLine(String line) {
        String[] ss = line.split(";");
        Coordinate[] points = new Coordinate[ss.length+2]; ///虚拟出一个头一个尾
        for (int i = 0; i < ss.length; i++) {
            String[] sps = ss[i].split(",");
            Coordinate pt1 = new Coordinate();
            pt1.x = Double.parseDouble(sps[0]);
            pt1.y = Double.parseDouble(sps[1]);
            points[i+1] = pt1;
        }

        Coordinate head = new Coordinate();
        head.x=points[1].x-(points[2].x-points[1].x);
        head.y=points[1].y-(points[2].y-points[1].y);
        points[0] = head;

        Coordinate tail = new Coordinate();
        int l = points.length;
        tail.x = points[l-2].x - (points[l-3].x - points[l-2].x);
        tail.y = points[l-2].y - (points[l-3].y - points[l-2].y);
        points[l-1] = tail;
        Coordinate[] coors1 = new Coordinate[points.length-2];
        Coordinate[] coors2 = new Coordinate[points.length-2];
        double linewidth = 0.01/300;////0.005*100000*2/300/100000;
        //double linewidth = 0.02/300;
        for(int i=1;i<=points.length-2;i++){
            Coordinate ppy =thickLineStripeCalculateJoints(points[i-1],points[i],points[i+1],linewidth);
            Coordinate ppy2 =thickLineStripeCalculateJoints(points[i-1],points[i],points[i+1],-linewidth);
            //RoadPoi ppy2 =thickLineStripeCalculateJoints(points[points.length-i],points[points.length-i-1],points[points.length-i-2],linewidth);
            coors1[i-1] = new Coordinate();
            coors1[i-1].x = ppy.x;
            coors1[i-1].y = ppy.y;

            coors2[coors1.length-i] = new Coordinate();
            coors2[coors1.length-i].x = ppy2.x;
            coors2[coors1.length-i].y = ppy2.y;
        }
        Coordinate[] c= new Coordinate[coors1.length+coors2.length+1];
        System.arraycopy(coors1, 0, c, 0, coors1.length);
        System.arraycopy(coors2, 0, c, coors1.length, coors2.length);

        c[c.length-1] = new Coordinate();
        c[c.length-1].x = coors1[0].x;
        c[c.length-1].y = coors1[0].y;

        LinearRing linearRing = gf.createLinearRing(c);
        Polygon polygon = gf.createPolygon(linearRing,null);
        return polygon;
    }


    /**
     *  双侧扩充成面
     * @param wktline String  LineString(114.90344 40.76388, 114.88579 40.76365)
     * @return Polygon
     */
    public static Polygon CalculatePolygonByWktLine(String wktline) {
        LineString lineString = null;
        try {
            lineString = (LineString) wktReader.read(wktline);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Coordinate[] coordinates = lineString.getCoordinates();

        Coordinate[] points0 = new Coordinate[coordinates.length+2]; ///虚拟出一个头一个尾
        System.arraycopy(coordinates, 0, points0, 1, points0.length-2);

        double d = 0.0002;
        Coordinate head0 = new Coordinate();
        head0.x=points0[1].x+(points0[2].x>points0[1].x?-d:d);
        head0.y=points0[1].y+(points0[2].y>points0[1].y?-d:d);
        points0[0] = head0;
        Coordinate tail0 = new Coordinate();
        int l = points0.length;
        tail0.x = points0[l-2].x+(points0[l-3].x>points0[l-2].x?-d:d);
        tail0.y = points0[l-2].y+(points0[l-3].y>points0[l-2].y?-d:d);
        points0[l-1] = tail0;

        Coordinate[] points = new Coordinate[points0.length+2]; ///虚拟出一个头一个尾
        System.arraycopy(points0, 0, points, 1, points.length-2);

        Coordinate head = new Coordinate();
        head.x=points[1].x-(points[2].x-points[1].x);
        head.y=points[1].y-(points[2].y-points[1].y);
        points[0] = head;
        Coordinate tail = new Coordinate();
        l = points.length;
        tail.x = points[l-2].x - (points[l-3].x - points[l-2].x);
        tail.y = points[l-2].y - (points[l-3].y - points[l-2].y);
        points[l-1] = tail;

        Coordinate[] coors1 = new Coordinate[points.length-2];
        Coordinate[] coors2 = new Coordinate[points.length-2];
        //double linewidth = 0.01/300;////0.005*100000*2/300/100000;
        //double linewidth = 0.1/300;
        double linewidth = 0.006/300;
        for(int i=1;i<=points.length-2;i++){
            Coordinate ppy =thickLineStripeCalculateJoints(points[i-1],points[i],points[i+1],linewidth);
            Coordinate ppy2 =thickLineStripeCalculateJoints(points[i-1],points[i],points[i+1],-linewidth);
            //RoadPoi ppy2 =thickLineStripeCalculateJoints(points[points.length-i],points[points.length-i-1],points[points.length-i-2],linewidth);
            coors1[i-1] = new Coordinate();
            coors1[i-1].x = ppy.x;
            coors1[i-1].y = ppy.y;

            coors2[coors1.length-i] = new Coordinate();
            coors2[coors1.length-i].x = ppy2.x;
            coors2[coors1.length-i].y = ppy2.y;
        }

        /*Coordinate[] c= new Coordinate[coors1.length+coors2.length+1];
        System.arraycopy(coors1, 0, c, 0, coors1.length);
        System.arraycopy(coors2, 0, c, coors1.length, coors2.length);
        c[c.length-1] = new Coordinate();
        c[c.length-1].x = coors1[0].x;
        c[c.length-1].y = coors1[0].y;*/
        Coordinate[] c= new Coordinate[coors1.length + coors2.length-4 + 1];
        System.arraycopy(coors1, 1, c, 0, coors1.length-2);
        System.arraycopy(coors2, 1, c, coors1.length-2, coors1.length-2);
        c[c.length-1] = new Coordinate();
        c[c.length-1].x = coors1[1].x;
        c[c.length-1].y = coors1[1].y;

        LinearRing linearRing = gf.createLinearRing(c);
        Polygon polygon = gf.createPolygon(linearRing,null);
        return polygon;
    }

    /**
     *  从wkt中获取经纬度-单侧扩充成面
     * @param wktline String  LineString(114.90344 40.76388, 114.88579 40.76365)
     * @return Polygon
     */
    public static Polygon CalculatePolygonByWktLine1(String wktline) {
        LineString lineString = null;
        try {
            lineString = (LineString) wktReader.read(wktline);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Coordinate[] coordinates = lineString.getCoordinates();

        Coordinate[] points0 = new Coordinate[coordinates.length+2]; ///虚拟出一个头一个尾
        System.arraycopy(coordinates, 0, points0, 1, points0.length-2);

        double d = 0.0002;
        Coordinate head0 = new Coordinate();
        head0.x=points0[1].x+(points0[2].x>points0[1].x?-d:d);
        head0.y=points0[1].y+(points0[2].y>points0[1].y?-d:d);
        points0[0] = head0;
        Coordinate tail0 = new Coordinate();
        int l = points0.length;
        tail0.x = points0[l-2].x+(points0[l-3].x>points0[l-2].x?-d:d);
        tail0.y = points0[l-2].y+(points0[l-3].y>points0[l-2].y?-d:d);
        points0[l-1] = tail0;

        Coordinate[] points = new Coordinate[points0.length+2]; ///虚拟出一个头一个尾
        System.arraycopy(points0, 0, points, 1, points.length-2);

        Coordinate head = new Coordinate();
        head.x=points[1].x-(points[2].x-points[1].x);
        head.y=points[1].y-(points[2].y-points[1].y);
        points[0] = head;
        Coordinate tail = new Coordinate();
        l = points.length;
        tail.x = points[l-2].x - (points[l-3].x - points[l-2].x);
        tail.y = points[l-2].y - (points[l-3].y - points[l-2].y);
        points[l-1] = tail;

        Coordinate[] coors1 = new Coordinate[points.length-2];
        //double linewidth = 0.01/300;////0.005*100000*2/300/100000;
        double linewidth = 0.008/300;
        for(int i=1;i<=points.length-2;i++){
            Coordinate ppy =thickLineStripeCalculateJoints(points[i-1],points[i],points[i+1],linewidth);
            coors1[coors1.length-i] = new Coordinate();
            coors1[coors1.length-i].x = ppy.x;
            coors1[coors1.length-i].y = ppy.y;

            /*coors1[i-1] = new Coordinate();
            coors1[i-1].x = ppy.x;
            coors1[i-1].y = ppy.y;*/
        }

        Coordinate[] c = new Coordinate[coors1.length*2 -4 + 1];
        System.arraycopy(points, 2, c, 0, coors1.length-2);
        System.arraycopy(coors1, 1, c, coors1.length-2, coors1.length-2);

        c[c.length-1] = new Coordinate();
        c[c.length-1].x = points[2].x;
        c[c.length-1].y = points[2].y;

        LinearRing linearRing = gf.createLinearRing(c);
        Polygon polygon = gf.createPolygon(linearRing,null);
        return polygon;
    }

    /**
     *  将线向外侧 偏移 返回线
     * @param wktline String  LineString(114.90344 40.76388, 114.88579 40.76365)
     * @return LineString
     */
    public static LineString thickLineStripeCalculate(String wktline) {
        LineString lineString = null;
        try {
            lineString = (LineString) wktReader.read(wktline);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Coordinate[] coordinates = lineString.getCoordinates();

        Coordinate[] points0 = new Coordinate[coordinates.length+2]; ///虚拟出一个头一个尾
        System.arraycopy(coordinates, 0, points0, 1, points0.length-2);

        double d = 0.0002;
        Coordinate head0 = new Coordinate();
        head0.x=points0[1].x+(points0[2].x>points0[1].x?-d:d);
        head0.y=points0[1].y+(points0[2].y>points0[1].y?-d:d);
        points0[0] = head0;
        Coordinate tail0 = new Coordinate();
        int l = points0.length;
        tail0.x = points0[l-2].x+(points0[l-3].x>points0[l-2].x?-d:d);
        tail0.y = points0[l-2].y+(points0[l-3].y>points0[l-2].y?-d:d);
        points0[l-1] = tail0;

        Coordinate[] points = new Coordinate[points0.length+2]; ///虚拟出一个头一个尾
        System.arraycopy(points0, 0, points, 1, points.length-2);

        Coordinate head = new Coordinate();
        head.x=points[1].x-(points[2].x-points[1].x);
        head.y=points[1].y-(points[2].y-points[1].y);
        points[0] = head;
        Coordinate tail = new Coordinate();
        l = points.length;
        tail.x = points[l-2].x - (points[l-3].x - points[l-2].x);
        tail.y = points[l-2].y - (points[l-3].y - points[l-2].y);
        points[l-1] = tail;

        Coordinate[] coors1 = new Coordinate[points.length-2];
        //double linewidth = 0.01/300;////0.005*100000*2/300/100000;
        double linewidth = 0.006/300;
        for(int i=1;i<=points.length-2;i++){
            Coordinate ppy =thickLineStripeCalculateJoints(points[i-1],points[i],points[i+1],linewidth);
            coors1[i-1] = new Coordinate();
            coors1[i-1].x = ppy.x;
            coors1[i-1].y = ppy.y;
        }

        Coordinate[] c= new Coordinate[coors1.length-2];
        System.arraycopy(coors1, 1, c, 0, coors1.length-2);
        LineString line = gf.createLineString(c);
        //System.out.println(line.toString());
        return line;
    }

    /**将道路点向外侧偏移
     * @param p1  上一个点
     * @param p2  当前点
     * @param p3   下一个点
     * @param linewidth 移动宽度
     * @return RoadPoi 移动后的点
     * */
    public static Coordinate thickLineStripeCalculateJoints(Coordinate p1, Coordinate p2, Coordinate p3, double linewidth){
        Vector2 v21 = new Vector2();
        Vector2 v23 = new Vector2();
        Vector2 v24 = new Vector2();
        Coordinate p4 = new Coordinate();
        double cosA;
        double sinA;
        double l;
        try{
            v21.x = p1.x-p2.x;
            v21.y = p1.y-p2.y;
            v21.m = Math.sqrt(v21.x*v21.x+v21.y*v21.y);
            v21.x = v21.x/v21.m;
            v21.y = v21.y/v21.m;
            v21.m = 1;

            v23.x = p3.x-p2.x;
            v23.y = p3.y-p2.y;
            v23.m = Math.sqrt(v23.x*v23.x+v23.y*v23.y);
            v23.x = v23.x/v23.m;
            v23.y = v23.y/v23.m;
            v23.m = 1;

            v24.x = (v23.x + v21.x) / 2;
            v24.y = (v23.y + v21.y) / 2;
            v24.m = Math.sqrt(v24.x*v24.x+v24.y*v24.y);

            if ((v24.x*v24.x+v24.y*v24.y) < 0.0000001 ){
                v24.x = v21.y;
                v24.y = -v21.x;
            }else{
                v24.x = v24.x/v24.m;
                v24.y = v24.y/v24.m;
                v24.m = 1;
            }

            cosA = v24.x * v21.x + v24.y * v21.y;
            if (cosA > 1){
                v24.x = v21.y;
                v24.y = -v21.x;
                cosA = 0.0f;
                sinA = 1.0f;
            }else{
                sinA = Math.sqrt(1 - cosA * cosA);
            }

            if (v21.x * v24.y - v21.y * v24.x >= 0){
                l = linewidth * sinA;
            }else{
                l = -linewidth * sinA;
            }
            p4.x = (p2.x + l * v24.x * (36.0f / 28.0f));
            p4.y = (p2.y + l * v24.y);

        }catch(Exception e){
            p4 = p2;
            e.printStackTrace();
        }
        p4.x = p4.x;
        p4.y = p4.y;
        return p4;
    }

    public static void main(String[] args) {

        String s = "LINESTRING(120.0338 32.15884,120.03359 32.1592)";
        Polygon polygon = CalculatePolygonByWktLine1(s);
        System.out.println(polygon.toString());
        LineString lineString = thickLineStripeCalculate(s);
        System.out.println(lineString.toString());

        s = "LINESTRING (120.0177 32.16568,120.01789 32.16509)";
        System.out.println(CalculatePolygonByWktLine(s).toString());

        /*        Point point = gf.createPoint(new Coordinate(116.39162,39.91201));
        Point point2 = gf.createPoint(new Coordinate(116.39091,39.9126));
        String s = "116.39157,39.91266;116.39158,39.91239;116.39161,39.91217;116.39162,39.91196";
        String s1 = "116.39158,39.91232;116.39163,39.91199;116.39165,39.91163";
        //String s2 = "108.893774,34.19525;108.894839,34.196921;108.894559,34.197079;108.89441,34.19684;108.89424,34.196569;108.893301,34.194931;108.8927,34.193789;108.892711,34.19373;108.892711,34.19373;108.892619,34.193589;108.89174,34.19197;108.8913,34.191239;108.89125,34.19117;108.89117,34.191031;108.891131,34.190959;108.891039,34.19082;108.89061,34.190111;108.89061,34.190111;108.89046,34.19013;108.88974,34.190211;108.888991,34.19025;108.887411,34.19025;108.887331,34.190219;108.88727,34.19023;108.887181,34.19023;108.887131,34.19023;108.887131,34.19023;108.886901,34.19023;108.885959,34.190219;108.88574,34.190219;108.884659,34.190211;108.88451,34.190211;108.881239,34.190119;108.88015,34.19008;108.88005,34.19008;108.87872,34.190039;108.878581,34.19003;108.877741,34.190011;108.87648,34.18997;108.875549,34.189939;108.875449,34.189939;108.875,34.189939;108.874881,34.189939;108.87418,34.189889;108.872411,34.189781;108.87219,34.189759;108.87072,34.18964;108.870441,34.189609;108.869421,34.18952;108.869069,34.18947;108.864141,34.188819;108.86383,34.18878;108.862159,34.188559;108.8601,34.18826;108.857741,34.187971;108.857339,34.187921;108.856369,34.18781;108.855959,34.18776;108.855061,34.18763;108.85508,34.18735;108.85518,34.18617;108.855829,34.18627;108.855829,34.18627;108.855801,34.185549;108.855751,34.184559;108.855779,34.184371;108.855851,34.18428;108.855851,34.18428;108.855809,34.183381;108.855801,34.18321;108.85577,34.18253;108.85577,34.18253;108.854831,34.182489;108.853839,34.182511;108.8525,34.18252;108.852001,34.182511;108.851799,34.182511;108.851031,34.182489;108.847791,34.182539;108.84661,34.182561;108.84661,34.182561;108.84663,34.18373;108.846641,34.184379;108.846649,34.185421;108.846649,34.185829;108.846649,34.18638;108.84666,34.186649;108.84666,34.187101;108.84666,34.187101;108.846749,34.187331;108.84694,34.187589;108.847059,34.187661;108.847289,34.187719;108.847711,34.187741;108.84793,34.18768;108.84811,34.187569;108.848251,34.1874;108.848329,34.18719;108.84829,34.186921;108.84822,34.186799;108.848049,34.186641;108.847619,34.1865;108.84697,34.1865;108.8464,34.186549;108.84475,34.18666;108.843366,34.187051";
        String s2 = "108.939277,34.364878;108.939421,34.367309;108.939429,34.3676;108.93941,34.368761;108.939271,34.370521;108.93908,34.37049;108.939221,34.368609;108.93923,34.3676;108.939099,34.36748;108.939019,34.36643;108.93898,34.365901;108.938739,34.36541;108.938451,34.36533;108.937411,34.36533;108.93686,34.36533;108.93337,34.36526;108.93265,34.365219;108.932411,34.365211;108.9323,34.3652;108.931,34.364989;108.929809,34.364889;108.92798,34.36464;108.92725,34.364501;108.927079,34.364479;108.926799,34.36451;108.925499,34.36426;108.923309,34.36377;108.922711,34.363559;108.92179,34.363121;108.921361,34.363201;108.92099,34.36342;108.92082,34.363589;108.920161,34.363839;108.92,34.363891;108.91972,34.364;108.91985,34.36492;108.9199,34.36531;108.919911,34.36546;108.919839,34.36592;108.91974,34.366391;108.919659,34.367289;108.919701,34.368431;108.91975,34.370169;108.91985,34.370779;108.919961,34.371081;108.920219,34.371369;108.920399,34.371491;108.92092,34.371569;108.92151,34.371369;108.92181,34.370951;108.922029,34.370369;108.922201,34.36936;108.92224,34.36898;108.92225,34.36786;108.92212,34.366089;108.92224,34.36475;108.92207,34.36372;108.92194,34.36332;108.921691,34.36288;108.921361,34.36252;108.920829,34.362159;108.920011,34.36184;108.91903,34.361519;108.918531,34.36125;108.902339,34.35599;108.90048,34.35541;108.896699,34.35436;108.89275,34.353459;108.88878,34.35268;108.875,34.350521;108.874859,34.350499;108.870579,34.34985;108.86844,34.34951;108.867589,34.349349;108.86219,34.348229;108.86071,34.34786;108.85803,34.34712;108.85469,34.34605;108.85424,34.346009;108.85403,34.345951;108.849091,34.344579;108.84773,34.344299;108.84684,34.34421;108.84531,34.34408;108.844839,34.343961;108.84426,34.343711;108.84367,34.34332;108.843279,34.342971;108.842899,34.342461;108.84275,34.342151;108.842619,34.34178;108.842539,34.34125;108.842561,34.34079;108.842741,34.340141;108.842849,34.3399;108.843151,34.339451;108.84429,34.33798;108.844551,34.33725;108.844781,34.336591;108.844809,34.3365;108.844881,34.336311;108.844961,34.3361;108.845011,34.33597;108.845069,34.335809;108.84526,34.335291;108.845319,34.33515;108.84548,34.334709;108.84574,34.334019;108.84599,34.333333;108.84702,34.330269;108.847201,34.329659;108.84755,34.328179;108.847619,34.327869;108.84781,34.32707;108.84793,34.326369;108.84799,34.326031;108.84804,34.32564;108.848071,34.325369;108.84814,34.32464;108.84821,34.323969;108.84824,34.32224;108.84821,34.321109;108.848121,34.319939;108.847791,34.3176;108.8477,34.317079;108.847619,34.316691;108.8475,34.316131;108.847389,34.31561;108.846951,34.31339;108.846771,34.31217;108.84661,34.31082;108.846569,34.31018;108.846549,34.30974;108.84651,34.308911;108.846541,34.307259;108.846619,34.30612;108.84666,34.305549;108.846821,34.303171;108.846879,34.302339;108.84699,34.300959;108.84702,34.300549;108.847059,34.3;108.84717,34.298681;108.847289,34.29668;108.84727,34.29622;108.847209,34.295851;108.84707,34.295349;108.846821,34.29477;108.84645,34.294149;108.846109,34.29373;108.845191,34.29283;108.844299,34.29204;108.844099,34.29181;108.843689,34.291181;108.843481,34.29079;108.84327,34.290161;108.843129,34.289479;108.842849,34.28712;108.842691,34.2863;108.842511,34.285499;108.84222,34.28434;108.84212,34.28398;108.8411,34.28018;108.840809,34.279119;108.840269,34.27717;108.84015,34.27671;108.839701,34.274609;108.83954,34.27344;108.839321,34.2712;108.839271,34.2702;108.839271,34.270089;108.83923,34.268861;108.83923,34.26873;108.83926,34.268201;108.839299,34.267381;108.83987,34.262259;108.839961,34.261411;108.8398,34.260549;108.83997,34.259421;108.83995,34.259149;108.839859,34.258919;108.839789,34.258811;108.839631,34.258681;108.83934,34.258589;108.83844,34.25844;108.83826,34.25834;108.837389,34.258279;108.837389,34.258179;108.838459,34.25826;108.83939,34.258409;108.83997,34.258509;108.84199,34.258869;108.842611,34.25898;108.84349,34.2564;108.84375,34.255571;108.84411,34.25444;108.84436,34.253631;108.843882,34.253516";
        //System.out.println(JtsTools.within(116.39162,39.91201, s));
        //System.out.println(JtsTools.within(116.39162,39.91201, s1));
        System.out.println(JtsTools.within(108.851880,34.193790, s2));

        double minLon=108.89511;
        double minLat=34.28842;
        double maxLon=109.00635;
        double maxLat=34.2277;
        Coordinate[] coors = new Coordinate[5];
        coors[0] = new Coordinate();
        coors[0].x = minLon;
        coors[0].y = minLat;
        coors[1] = new Coordinate();
        coors[1].x = minLon;
        coors[1].y = maxLat;
        coors[2] = new Coordinate();
        coors[2].x = maxLon;
        coors[2].y = maxLat;
        coors[3] = new Coordinate();
        coors[3].x = maxLon;
        coors[3].y = minLat;
        coors[4] = new Coordinate();
        coors[4].x = minLon;
        coors[4].y = minLat;
        System.out.println(JtsTools.within(108.9418,34.24587,coors));*/
    }
}

/**
 * 向量类
 */
class Vector2{
    /**向量宽*/
    double x;
    /**向量高*/
    double y;
    /**向量模*/
    double m;
}
