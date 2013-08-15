package util;

public class hbaseutils {

    public static String getTableNameInRegionName(String regionName) {
        return regionName.split(",")[0];
    }

    // skip the hash value of tablename,startkey,hash value
    public static String getRegionName(String regionName) {
        return regionName.split(",")[0] + "," + regionName.split(",")[1];
    }
}
