package com.cennavi.modules.pbf.pbfUtils;

/**
 * 找不到vector的maven依赖，从中复制出来的源码
 * Created by sunpengyan on 2020/7/21.
 */
public class MercatorProjection {
    public static double longitudeToMetersX(double longitude) {
        return 6378137.0D * Math.toRadians(longitude);
    }

    public static double metersXToLongitude(double x) {
        return Math.toDegrees(x / 6378137.0D);
    }

    public static double metersYToLatitude(double y) {
        return Math.toDegrees(Math.atan(Math.sinh(y / 6378137.0D)));
    }

    public static double latitudeToMetersY(double latitude) {
        return 6378137.0D * Math.log(Math.tan(0.7853981633974483D + 0.5D * Math.toRadians(latitude)));
    }

    public static double calculateGroundResolution(double latitude, byte zoom) {
        return Math.cos(latitude * 3.141592653589793D / 180.0D) * 4.0075016686E7D / (double)(256L << zoom);
    }

    public static double latitudeToPixelY(double latitude, byte zoom) {
        double sinLatitude = Math.sin(latitude * 3.141592653589793D / 180.0D);
        return (0.5D - Math.log((1.0D + sinLatitude) / (1.0D - sinLatitude)) / 12.566370614359172D) * (double)(256L << zoom);
    }

    public static long latitudeToTileY(double latitude, byte zoom) {
        return pixelYToTileY(latitudeToPixelY(latitude, zoom), zoom);
    }

    public static double longitudeToPixelX(double longitude, byte zoom) {
        return (longitude + 180.0D) / 360.0D * (double)(256L << zoom);
    }

    public static long longitudeToTileX(double longitude, byte zoom) {
        return pixelXToTileX(longitudeToPixelX(longitude, zoom), zoom);
    }

    public static double pixelXToLongitude(double pixelX, byte zoom) {
        return 360.0D * (pixelX / (double)(256L << zoom) - 0.5D);
    }

    public static long pixelXToTileX(double pixelX, byte zoom) {
        return (long)Math.min(Math.max(pixelX / 256.0D, 0.0D), Math.pow(2.0D, (double)zoom) - 1.0D);
    }

    public static double tileXToPixelX(long tileX) {
        return (double)(tileX * 256L);
    }

    public static double tileYToPixelY(long tileY) {
        return (double)(tileY * 256L);
    }

    public static double pixelYToLatitude(double pixelY, byte zoom) {
        double y = 0.5D - pixelY / (double)(256L << zoom);
        return 90.0D - 360.0D * Math.atan(Math.exp(-y * 2.0D * 3.141592653589793D)) / 3.141592653589793D;
    }

    public static long pixelYToTileY(double pixelY, byte zoom) {
        return (long)Math.min(Math.max(pixelY / 256.0D, 0.0D), Math.pow(2.0D, (double)zoom) - 1.0D);
    }

    public static double tileXToLongitude(long tileX, byte zoom) {
        return pixelXToLongitude((double)(tileX * 256L), zoom);
    }

    public static double tileYToLatitude(long tileY, byte zoom) {
        return pixelYToLatitude((double)(tileY * 256L), zoom);
    }

    public static double deltaLat(double deltaPixel, double lat, byte zoom) {
        double pixelY = latitudeToPixelY(lat, zoom);
        double lat2 = pixelYToLatitude(pixelY + deltaPixel, zoom);
        return Math.abs(lat2 - lat);
    }

    public static void main(String[] args) {
        System.out.println(longitudeToPixelX(116.2167263D, (byte)14));
        System.out.println(pixelXToLongitude(3451174.9999638754D, (byte)14));
    }
}
