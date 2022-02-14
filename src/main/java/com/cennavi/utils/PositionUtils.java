package com.cennavi.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 各地图API坐标系统比较与转换;
 * WGS84坐标系：即地球坐标系，国际上通用的坐标系。设备一般包含GPS芯片或者北斗芯片获取的经纬度为WGS84地理坐标系,
 * 谷歌地图采用的是WGS84地理坐标系（中国范围除外）;
 * GCJ02坐标系：即火星坐标系，是由中国国家测绘局制订的地理信息系统的坐标系统。由WGS84坐标系经加密后的坐标系。
 * 谷歌中国地图和搜搜中国地图采用的是GCJ02地理坐标系; BD09坐标系：即百度坐标系，GCJ02坐标系经加密后的坐标系;
 * 搜狗坐标系、图吧坐标系等，估计也是在GCJ02基础上加密而成的。
 */
public class PositionUtils {

	private static double[] Sp = { 1.289059486E7, 8362377.87, 5591021,
			3481989.83, 1678043.12, 0 };

	public static final String BAIDU_LBS_TYPE = "bd09ll";

	public static double pi = 3.1415926535897932384626;
	public static double a = 6378245.0;
	public static double ee = 0.00669342162296594323;

	/**
	 * 84 to 火星坐标系 (GCJ-02) World Geodetic System ==> Mars Geodetic System
	 *
	 * @param lat
	 * @param lon
	 * @return
	 */
	public static Double[] gps84_To_Gcj02(double lon, double lat) {
		if (outOfChina(lat, lon)) {
			return null;
		}
		double dLat = transformLat(lon - 105.0, lat - 35.0);
		double dLon = transformLon(lon - 105.0, lat - 35.0);
		double radLat = lat / 180.0 * pi;
		double magic = Math.sin(radLat);
		magic = 1 - ee * magic * magic;
		double sqrtMagic = Math.sqrt(magic);
		dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
		dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
		double mgLat = lat + dLat;
		double mgLon = lon + dLon;

		return new Double[] { mgLon, mgLat };
	}

	/**
	 * * 火星坐标系 (GCJ-02) to 84 * * @param lon * @param lat * @return
	 * */
	public static Double[] gcj_To_Gps84(double lon, double lat) {
		Double[] gps = transform(lat, lon);

		double lontitude = lon * 2 - gps[0];
		double latitude = lat * 2 - gps[1];
		return new Double[] { lontitude, latitude };
	}

	/**
	 * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换算法 将 GCJ-02 坐标转换成 BD-09 坐标
	 *
	 * @param gg_lat
	 * @param gg_lon
	 */
	public static Double[] gcj02_To_Bd09(double gg_lat, double gg_lon) {
		double x = gg_lon, y = gg_lat;
		double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * pi);
		double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * pi);
		double bd_lon = z * Math.cos(theta) + 0.0065;
		double bd_lat = z * Math.sin(theta) + 0.006;

		return new Double[] { bd_lon, bd_lat };
	}

	/**
	 * * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换算法 * * 将 BD-09 坐标转换成GCJ-02 坐标 * * @param
	 * bd_lat * @param bd_lon * @return
	 */
	public static Double[] bd09_To_Gcj02(double bd_lat, double bd_lon) {
		double x = bd_lon - 0.0067, y = bd_lat - 0.0063;// 调整后的值
		// double x = bd_lon - 0.0065, y = bd_lat - 0.006;//原值
		double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * pi);
		double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * pi);
		double gg_lon = z * Math.cos(theta);
		double gg_lat = z * Math.sin(theta);
		return new Double[] { gg_lon, gg_lat };
	}

	/**
	 * (BD-09)-->84
	 *
	 * @param bd_lat
	 * @param bd_lon
	 * @return
	 */
	public static Double[] bd09_To_Gps84(double bd_lat, double bd_lon) {

		Double[] gcj02 = PositionUtils.bd09_To_Gcj02(bd_lat, bd_lon);
		Double[] map84 = PositionUtils.gcj_To_Gps84(gcj02[1], gcj02[0]);
		return map84;

	}

	public static boolean outOfChina(double lat, double lon) {
		if (lon < 72.004 || lon > 137.8347)
			return true;
		if (lat < 0.8293 || lat > 55.8271)
			return true;
		return false;
	}

	public static Double[] transform(double lat, double lon) {
		if (outOfChina(lat, lon)) {
			return new Double[] { lat, lon };
		}
		double dLat = transformLat(lon - 105.0, lat - 35.0);
		double dLon = transformLon(lon - 105.0, lat - 35.0);
		double radLat = lat / 180.0 * pi;
		double magic = Math.sin(radLat);
		magic = 1 - ee * magic * magic;
		double sqrtMagic = Math.sqrt(magic);
		dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
		dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
		double mgLat = lat + dLat;
		double mgLon = lon + dLon;

		return new Double[] { mgLon, mgLat };
	}

	public static double transformLat(double x, double y) {
		double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y
				+ 0.2 * Math.sqrt(Math.abs(x));
		ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
		ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
		ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;
		return ret;
	}

	public static double transformLon(double x, double y) {
		double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1
				* Math.sqrt(Math.abs(x));
		ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
		ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
		ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0
				* pi)) * 2.0 / 3.0;
		return ret;
	}

	public static void main(String[] args) {

		// 北斗芯片获取的经纬度为WGS84地理坐标 31.426896,119.496145
		Double[] gps = new Double[] { 31.426896, 119.496145 };
		System.out.println("gps :" + gps[0] + "---" + gps[1]);
		Double[] gcj = gps84_To_Gcj02(gps[0], gps[1]);
		System.out.println("02 :" + gcj[0] + "--" + gcj[1]);
		Double[] star = gcj_To_Gps84(gcj[1], gcj[0]);
		System.out.println("84:" + star[0] + "--" + star[1]);
		Double[] bd = gcj02_To_Bd09(gcj[1], gcj[0]);
		System.out.println("bd  :" + bd[0] + "--" + bd[1]);
		Double[] gcj2 = bd09_To_Gcj02(bd[1], bd[0]);
		System.out.println("02 :" + gcj2[0] + "--" + gcj2[1]);
	}

	// / <summary>
	// / Bd09mc 转 Bd09（精度高）
	// / </summary>
	// / <param name="lng"></param>
	// / <param name="lat"></param>
	// / <returns></returns>
	public static double[] Mercator2BD09(double lng, double lat) {
		double[] lnglat = new double[2];
		ArrayList c = null;
		// List<Double> d0 = new ArrayList<Double>();
		ArrayList d0 = new ArrayList();
		double[] d0str = { 1.410526172116255E-8, 8.98305509648872E-6,
				-1.9939833816331, 200.9824383106796, -187.2403703815547,
				91.6087516669843, -23.38765649603339, 2.57121317296198,
				-0.03801003308653, 1.73379812E7 };
		for (int i = 0; i < d0str.length; i++) {
			d0.add(d0str[i]);
		}

		ArrayList d1 = new ArrayList();
		double[] d1str = { -7.435856389565537E-9, 8.983055097726239E-6,
				-0.78625201886289, 96.32687599759846, -1.85204757529826,
				-59.36935905485877, 47.40033549296737, -16.50741931063887,
				2.28786674699375, 1.026014486E7 };
		for (int i = 0; i < d1str.length; i++) {
			d1.add(d1str[i]);
		}

		ArrayList d2 = new ArrayList();
		double[] d2str = { -3.030883460898826E-8, 8.98305509983578E-6,
				0.30071316287616, 59.74293618442277, 7.357984074871,
				-25.38371002664745, 13.45380521110908, -3.29883767235584,
				0.32710905363475, 6856817.37 };
		for (int i = 0; i < d2str.length; i++) {
			d2.add(d2str[i]);
		}

		ArrayList d3 = new ArrayList();
		double[] d3str = { -1.981981304930552E-8, 8.983055099779535E-6,
				0.03278182852591, 40.31678527705744, 0.65659298677277,
				-4.44255534477492, 0.85341911805263, 0.12923347998204,
				-0.04625736007561, 4482777.06 };
		for (int i = 0; i < d3str.length; i++) {
			d3.add(d3str[i]);
		}

		ArrayList d4 = new ArrayList();
		double[] d4str = { 3.09191371068437E-9, 8.983055096812155E-6,
				6.995724062E-5, 23.10934304144901, -2.3663490511E-4,
				-0.6321817810242, -0.00663494467273, 0.03430082397953,
				-0.00466043876332, 2555164.4 };
		for (int i = 0; i < d4str.length; i++) {
			d4.add(d4str[i]);
		}

		ArrayList d5 = new ArrayList();
		double[] d5str = { 2.890871144776878E-9, 8.983055095805407E-6,
				-3.068298E-8, 7.47137025468032, -3.53937994E-6,
				-0.02145144861037, -1.234426596E-5, 1.0322952773E-4,
				-3.23890364E-6, 826088.5 };
		for (int i = 0; i < d5str.length; i++) {
			d5.add(d5str[i]);
		}

		lnglat[0] = Math.abs(lng);
		lnglat[1] = Math.abs(lat);

		for (int d = 0; d < 6; d++) {
			if (lnglat[1] >= Sp[d]) {
				if (d == 0) {
					c = d0;
				}

				if (d == 1) {
					c = d1;
				}

				if (d == 2) {
					c = d2;
				}

				if (d == 3) {
					c = d3;
				}

				if (d == 4) {
					c = d4;
				}

				if (d == 5) {
					c = d5;
				}

				break;
			}
		}
		lnglat = Yr(lnglat, c);
		return lnglat;
	}

	private static double[] Yr(double[] lnglat, List b) {
		if (b != null) {
			double c = Double.parseDouble(b.get(0).toString())
					+ Double.parseDouble(b.get(1).toString())
					* Math.abs(lnglat[0]);
			double d = Math.abs(lnglat[1])
					/ Double.parseDouble(b.get(9).toString());
			d = Double.parseDouble(b.get(2).toString())
					+ Double.parseDouble(b.get(3).toString()) * d
					+ Double.parseDouble(b.get(4).toString()) * d * d
					+ Double.parseDouble(b.get(5).toString()) * d * d * d
					+ Double.parseDouble(b.get(6).toString()) * d * d * d * d
					+ Double.parseDouble(b.get(7).toString()) * d * d * d * d
					* d + Double.parseDouble(b.get(8).toString()) * d * d * d
					* d * d * d;
			double bd;
			if (0 > lnglat[0]) {
				bd = -1 * c;
			} else {
				bd = c;
			}
			lnglat[0] = bd;

			double bd2;
			if (0 > lnglat[1]) {
				bd2 = -1 * d;
			} else {
				bd2 = d;
			}
			lnglat[1] = bd2;
			return lnglat;
		}
		return null;
	}
}
