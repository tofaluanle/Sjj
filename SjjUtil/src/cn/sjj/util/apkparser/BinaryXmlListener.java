package cn.sjj.util.apkparser;


public interface BinaryXmlListener {
    void onXmlEntry(String path, String name, Attribute... attrs);
}