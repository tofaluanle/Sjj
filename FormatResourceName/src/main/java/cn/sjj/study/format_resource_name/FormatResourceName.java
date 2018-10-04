package cn.sjj.study.format_resource_name;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.mozilla.intl.chardet.HtmlCharsetDetector;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.sjj.tool.FileUtil;


public class FormatResourceName {

    private static final boolean RELEASE = false;

    private static String  sDir     = "C:\\WorkSpace\\temp\\android-sojourn\\sojourn/";
    //        private static String  sDir     = "C:\\WorkSpace\\AndroidStudio\\Ziroom\\ziroom-client-android\\android-credit/";
    private static String  sPrefix  = "sjj_";
    //    private static String  sPrefix  = "credit_";
    private static boolean sExecute = true;
//    private static boolean sExecute = false;

    private static List<File>        sJavas        = new ArrayList<>();
    private static Map<File, String> sJavaContents = new HashMap<>();
    private static List<File>        sAnimators    = new ArrayList<>();
    private static List<File>        sDrawables    = new ArrayList<>();
    private static List<File>        sLayouts      = new ArrayList<>();
    private static List<File>        sValues       = new ArrayList<>();
    private static List<String>      sValueIds     = new ArrayList<>();
    private static List<String>      sViewIds      = new ArrayList<>();

    private static List<String> sSupportStyles     = new ArrayList<>();
    private static List<String> sSupportStyleAttrs = new ArrayList<>();

    private static int sLayoutNameMaxLength;

    private static String     sRuntimePath;
    private static FileWriter sFw;

    public static void main(String[] args) throws Exception {
        init(args);
        print("Version 1.5.1");

//        testResourcePattern();
//        testLayoutPattern();
//        testCharSet();
//        testIdWithQuote();
//        testJavaPattern();
        findSupportValues();
        findFiles();
        rename();


        closeFw();
    }

    private static void findSupportValues() throws Exception {
        findSupportV7Values();
        findDesignValues();
    }

    private static void findDesignValues() throws Exception {
        InputStream is;
        if (RELEASE) {
            is = FormatResourceName.class.getResourceAsStream("/design_values.xml");
        } else {
            is = new FileInputStream("C:\\Users\\sjj\\.gradle\\caches\\transforms-1\\files-1.1\\design-26.0.2.aar\\1cfdeda5893f28fa859689073bc01047\\res\\values/values.xml");
        }
        SAXReader reader = new SAXReader();
        Document document = reader.read(is);
        Element bookStore = document.getRootElement();
        scanElement(bookStore);

//        print(sSupportStyles);
//        print(sSupportStyleAttrs);
    }

    private static void findSupportV7Values() throws Exception {
        InputStream is;
        if (RELEASE) {
            is = FormatResourceName.class.getResourceAsStream("/support_v7_values.xml");
        } else {
            is = new FileInputStream("C:\\Users\\sjj\\.gradle\\caches\\transforms-1\\files-1.1\\appcompat-v7-26.0.2.aar\\87193e658e92abd1bac368e29baa204d\\res\\values/values.xml");
        }
        SAXReader reader = new SAXReader();
        Document document = reader.read(is);
        Element bookStore = document.getRootElement();
        scanElement(bookStore);

//        print(sSupportStyles);
//        print(sSupportStyleAttrs);
    }

    private static void scanElement(Element element) {
        Element parent = element.getParent();
        if (element.getName().equals("style")) {
//            System.out.println(element.getName());
            List<Attribute> attributes = element.attributes();
            for (Attribute attr : attributes) {
//                System.out.println("    " + attr.getName() + " = " + attr.getValue());
                if (attr.getName().equals("name")) {
//                    sSupportStyles.add(attr.getValue());
                }
            }
        } else if (parent != null) {
            if (parent.getName().equals("declare-styleable")) {
//                System.out.println(element.getName());
                List<Attribute> attributes = element.attributes();
                for (Attribute attr : attributes) {
//                    System.out.println("    " + attr.getName() + " = " + attr.getValue());
                    if (attr.getName().equals("name")) {
                        sSupportStyleAttrs.add(attr.getValue());
                    }
                }
            }
        }

        Iterator iterator = element.elementIterator();
        while (iterator.hasNext()) {
            scanElement((Element) iterator.next());
        }
    }

    private static void testIdWithQuote() {
        File file = new File("C:\\WorkSpace\\temp\\android-credit\\src\\main\\res\\layout\\activity_negative_record.xml");
        try {
            String content = "<ImageView\n" +
                    "        android:id=\"@+id/iv_close\"\n" +
                    "        style=\"@style/title_icon_close\"\n" +
                    "        android:layout_height=\"56dp\" />";
            content = FileUtil.readFile(file.getAbsolutePath());
            content = content.replaceAll("\"@\\+id/iv_close\"", "zzz");
//            System.out.println(content);
            FileUtil.writeFile(file.getAbsolutePath(), content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void testCharSet() {
        File dir = new File(sDir, "src/main/");
        checkCharset(dir);
    }

    private static void checkCharset(File dir) {
        for (File file : dir.listFiles()) {
//            System.out.println(file);
            if (file.isFile()) {
                try {
                    String charset = new HtmlCharsetDetector().main(new String[]{file.getAbsolutePath(), "2"});
//                    if (charset.startsWith("ASC")) {
                    System.out.println(charset + " " + file);
//                    }
//                    FileCharsetConvert.main(new String[]{file.getAbsolutePath(), charset, ""});
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                checkCharset(file);
            }
        }
    }

    private static void testJavaPattern() {
        String butterKnifeMulti = "private void hideRecommendEva() {\n" +
                "        rv_recommend_eva.setVisibility(View.GONE);\n" +
                "        recommend_eva_area.setVisibility(View.GONE);\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    @OnClick({R.id.sdv_top, R.id.tv_city_station, R.id.tv_search_content_station, R.id.fun_area,\n" +
                "            R.id.station_more, R.id.lav_station_home})\n" +
                "    public void onViewClicked(View view) {";
        String str = "(android.R.\n" +
                "layout\n" +
                ".tt);\n"
                + "(R\n" +
                ".layout.\n" +
                "zz, dd);" +
                "((TextView) mLlRealName.findViewById(R.id.tv_content)).setText(\"完成实名认证\")\n" +
                "        ;" +
                "public class CreditGridAdapter extends BaseAdapter {\n" +
                "    @BindViewa(R2" +
                ".id.butter)\n" +
                "  private   Context context;" +
                butterKnifeMulti;
//        Pattern p = Pattern.compile("[^\\.]R\\s*\\.\\s*.*\\s*\\.\\s*(.*?)[\\s,);]");
//        Pattern p = Pattern.compile("R\\s*\\.\\s*(.*?)\\s*\\.\\s*(.*?)[\\s,);]");
//        Pattern p = Pattern.compile("[R|R2]\\s*\\.([^\\.]*)\\.\\s*(.*?)[\\s,);]");
//        Pattern p = Pattern.compile("(R|R2)\\s*\\.([^\\.]*)\\.\\s*(.*?)[\\s,);]");
//        Pattern p = Pattern.compile("R2?\\s*\\.([^\\.]*)\\.\\s*(.*?)[\\s,);]");
        Pattern p = Pattern.compile("[^\\.]R2?\\s*\\.([^\\.]*)\\.\\s*(.*?)[\\s,);]");

        Matcher m = p.matcher(str);
        while (m.find()) {
            print("***" + m.group() + "***");
//            print("type = " + m.group(1));
//            print("id = " + m.group(2));
//            print("type = " + m.group(2));
//            print("id = " + m.group(3));
        }
    }

    private static void testLayoutPattern() {
        String str = "<RelativeLayout\n" +
                "                android:layout_width=\"@string/zzz\"\n" +
                "                android:layout_height=\"250dp\"\n" +
                "android:background=\"@drawable/credit_score_bg\">" +
                "<TextView\n" +
                "            android:id=\"@+id/xlistview_footer_hint_textview\"\n" +
                "            android:layout_width=\"wrap_content\"\n" +
                "            android:layout_height=\"wrap_content\"\n" +
                "            android:layout_centerInParent=\"true\"\n" +
                "            android:text=\"@string/xlistview_footer_hint_normal\"\n" +
                "            android:textColor=\"#BBBBBB\" />";
        try {
            str = FileUtil.readFile("C:\\WorkSpace\\temp\\android-credit\\src\\main\\res\\layout\\xlistview_footer.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String reg = "('@(.*)/(.*?)')|(\"@(.*)/(.*?)\")";
//        String reg = "('@.*/(.*?)')|(\"@.*/(.*?)\")";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(str);
        while (m.find()) {
            print(m.group());
        }
    }

    private static void testResourcePattern() {
        String str = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<resources>\n" +
                "    <color name= \"transparent\" ref='http'>#00</color>\n" +
                "    <color  name='colorGray_444444'>#444444</color>\n" +
                "    <color  name='color_444444'/>#444444</color>\n" +
                "</resources>";
//        Pattern p = Pattern.compile("name=(\"(.*?)\")");
//        Pattern p = Pattern.compile("\\s*name=\\s*(\"(.*?)\")");
//        Pattern p = Pattern.compile("\\s*name=\\s*((\"(.*?)\")|('(.*?)'))");
//        Pattern p = Pattern.compile("\\s+name=\\s*((\"(.*?)\")|('(.*?)'))(>|\\s+|/>)");
        Pattern p = Pattern.compile("\\s+name=\\s*(('(.*?)')|(\"(.*?)\"))(>|\\s+|/>)");
        Matcher m = p.matcher(str);
        while (m.find()) {
            print(m.group());

            String group = m.group();
            p = Pattern.compile("(\"(.*?)\")|('(.*?)')");
            Matcher m2 = p.matcher(group);
            while (m2.find()) {
                print(m2.group());
            }
        }
    }

    private static void rename() {
        renameValueContent();
        renameAnimatorContent();
        renameDrawableContent();
        renameLayoutContent();
        renameJavaContent2();
        renameManifestContent();
        renameAnimatorFile();
        renameDrawableFile();
        renameLayoutFile();
    }

    private static void renameDrawableContent() {
        print("\nREPLACE DRAWABLE FILE CONTENT ==============\n");
        for (File file : sDrawables) {
            if (!file.getName().endsWith(".xml")) {
                continue;
            }

            String content;
            try {
                content = FileUtil.readFile(file.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
                print(String.format("%s read content fail", file));
                return;
            }

            String[] filters = {"android:", "id", "+id"};
            String reg = "('@(.*)/(.*?)')|(\"@(.*)/(.*?)\")";
            Pattern p = Pattern.compile(reg);
            Matcher m = p.matcher(content);
            Map<String, String> contentMap = new HashMap<>();
            while (m.find()) {
//                print(m.group());
                String type;
                String mId;
                if (m.group(3) != null) {
                    type = m.group(2);
                    mId = m.group(3);
                } else {
                    type = m.group(5);
                    mId = m.group(6);
                }
                boolean ignore = false;
                for (String filter : filters) {
                    if (type.startsWith(filter)) {
                        ignore = true;
                        break;
                    }
                }
                if (ignore) {
                    continue;
                }

                for (File resource : sDrawables) {
                    String resourceName = resource.getName().substring(0, resource.getName().lastIndexOf("."));
                    if (mId.equals(resourceName) && !mId.startsWith(sPrefix)) {
                        String targetContent = m.group();
                        String replaceContent = targetContent.replace(mId, sPrefix + mId);
                        contentMap.put(targetContent, replaceContent);
                        break;
                    }
                }

                //TODO valueIds没有区分类型，可以会引起不同类型匹配到同一个字符串的问题
                for (String id : sValueIds) {
                    if (mId.equals(id) && !mId.startsWith(sPrefix)) {
                        String targetContent = m.group();
                        String replaceContent = targetContent.replace(mId, sPrefix + mId);
                        contentMap.put(targetContent, replaceContent);
                        break;
                    }
                }

            }

            print(String.format("[ %s ] -> %s", file.getName(), contentMap));
            if (!sExecute || contentMap.isEmpty()) {
                continue;
            }

            for (Map.Entry<String, String> entry : contentMap.entrySet()) {
                content = content.replaceAll(entry.getKey(), entry.getValue());
            }
            try {
                FileUtil.writeFile(file.getAbsolutePath(), content);
            } catch (Exception e) {
                e.printStackTrace();
                print(String.format("%s write content fail", file));
            }
        }
    }

    private static void renameAnimatorContent() {
    }

    private static void renameJavaContent2() {
        print("\nREPLACE JAVA FILE CONTENT ==============\n");
        for (File java : sJavaContents.keySet()) {
//            print(java);
            String content = sJavaContents.get(java);
//            Pattern p = Pattern.compile("[^\\.]R\\s*\\.\\s*.*\\s*\\.\\s*(.*?)[\\s,);]");
            Pattern p = Pattern.compile("[^\\.]R2?\\s*\\.([^\\.]*)\\.\\s*(.*?)[\\s,);]");
            Matcher m = p.matcher(content);
            Map<String, String> contentMap = new HashMap<>();
            while (m.find()) {
//                print(m.group() + " -> " + m.group(2) + ";");
                String targetContent = m.group();
                String type = m.group(1).trim();
                String id = m.group(2);

                if (type.equals("anim")) {
                    for (File resource : sAnimators) {
                        String resourceName = resource.getName().substring(0, resource.getName().lastIndexOf("."));
                        if (id.equals(resourceName) && !id.startsWith(sPrefix)) {
                            String replaceContent = targetContent.replace(id, sPrefix + id);
                            contentMap.put(targetContent, replaceContent);
                            break;
                        }
                    }
                } else if (type.equals("drawable") || type.equals("mipmap")) {
                    for (File resource : sDrawables) {
                        String resourceName = resource.getName().substring(0, resource.getName().lastIndexOf("."));
                        if (id.equals(resourceName) && !id.startsWith(sPrefix)) {
                            String replaceContent = targetContent.replace(id, sPrefix + id);
                            contentMap.put(targetContent, replaceContent);
                            break;
                        }
                    }
                } else if (type.equals("layout")) {
                    for (File resource : sLayouts) {
                        String resourceName = resource.getName().substring(0, resource.getName().lastIndexOf("."));
                        if (id.equals(resourceName) && !id.startsWith(sPrefix)) {
                            String replaceContent = targetContent.replace(id, sPrefix + id);
                            contentMap.put(targetContent, replaceContent);
                            break;
                        }
                    }
                } else {
                    if (sViewIds.contains(id)) {
                        String replaceContent = targetContent.replace(id, sPrefix + id);
                        contentMap.put(targetContent, replaceContent);
                    } else if (sValueIds.contains(id)) {
                        //TODO valueIds没有区分类型，可以会引起不同类型匹配到同一个字符串的问题
                        String replaceContent = targetContent.replace(id, sPrefix + id);
                        contentMap.put(targetContent, replaceContent);
                    }
                }
            }

            print(String.format("[ %s ] -> %s", java.getName(), contentMap));
            if (!sExecute || contentMap.isEmpty()) {
                continue;
            }

            for (Map.Entry<String, String> entry : contentMap.entrySet()) {
                String targetContent = entry.getKey().replace("(", "").replace(")", "").replace("{", "").replace("}", "").replace(",", "");
                String replaceContent = entry.getValue().replace("(", "").replace(")", "").replace("{", "").replace("}", "").replace(",", "");
                content = content.replaceAll(targetContent, replaceContent);
            }
            try {
                FileUtil.writeFile(java.getAbsolutePath(), content);
            } catch (Exception e) {
                e.printStackTrace();
                print(String.format("%s write content fail", java));
            }
        }
    }

    private static void renameJavaContent() {
        print("\nREPLACE JAVA FILE CONTENT ==============\n");
        for (File java : sJavaContents.keySet()) {
//                print(java);
            String content = sJavaContents.get(java);
            List<String> targetContents = new ArrayList<>();
            String[] suffixContents = {",", ";", ")", " "};
            for (File animator : sAnimators) {
                String resourceContent = animator.getName().substring(0, animator.getName().lastIndexOf("."));
                for (String suffix : suffixContents) {
                    String targetContent = "." + resourceContent + suffix;
                    if (content.contains(targetContent)) {
                        if (suffix.equals(")")) {
                            targetContent = "." + resourceContent + "\\)";
                        }
                        if (!targetContents.contains(targetContent)) {
                            targetContents.add(targetContent);
                        }
                    }
                }
            }

            for (File drawable : sDrawables) {
                String resourceContent = drawable.getName().substring(0, drawable.getName().lastIndexOf("."));
                for (String suffix : suffixContents) {
                    String targetContent = "." + resourceContent + suffix;
                    if (content.contains(targetContent)) {
                        if (suffix.equals(")")) {
                            targetContent = "." + resourceContent + "\\)";
                        }
                        if (!targetContents.contains(targetContent)) {
                            targetContents.add(targetContent);
                        }
                    }
                }
            }

            for (File layout : sLayouts) {
                String resourceContent = layout.getName().substring(0, layout.getName().lastIndexOf("."));
                for (String suffix : suffixContents) {
                    String targetContent = "." + resourceContent + suffix;
                    if (content.contains(targetContent)) {
                        if (suffix.equals(")")) {
                            targetContent = "." + resourceContent + "\\)";
                        }
                        targetContents.add(targetContent);
                    }
                }
            }

            //TODO android.R.color.xxx的形式没有过滤
            for (String id : sValueIds) {
                String resourceContent = id;
                for (String suffix : suffixContents) {
                    String targetContent = "." + resourceContent + suffix;
                    if (content.contains(targetContent)) {
                        if (suffix.equals(")")) {
                            targetContent = "." + resourceContent + "\\)";
                        }
                        targetContents.add(targetContent);
                    }
                }
            }

            print(String.format("[ %s ] -> %s", java.getName(), targetContents));
            if (!sExecute || targetContents.isEmpty()) {
                continue;
            }

            for (String targetContent : targetContents) {
                String replace = "." + sPrefix + targetContent.substring(1, targetContent.length());
                content = content.replaceAll(targetContent, replace);
            }
            try {
                FileUtil.writeFile(java.getAbsolutePath(), content);
            } catch (Exception e) {
                e.printStackTrace();
                print(String.format("%s write content fail", java));
            }
        }
    }

    private static void renameValueContent() {
        print("\nREPLACE VALUE FILE CONTENT ==============\n");

        for (File value : sValues) {
            Map<String, String> contentMap = new HashMap<>();
            String content;
            try {
                content = FileUtil.readFile(value.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
                print(String.format("%s read content fail", value));
                continue;
            }

            Pattern p = Pattern.compile("\\s+name=\\s*(('(.*?)')|(\"(.*?)\"))(>|\\s+|/>)");
            Matcher m = p.matcher(content);
            while (m.find()) {
//                print(m.group());
                String id;
                if (m.group(3) != null) {
                    id = m.group(3);
                } else {
                    id = m.group(5);
                }
                if (id.startsWith(sPrefix) || id.startsWith("android:") || sSupportStyleAttrs.contains(id)) {
                    continue;
                }

                String targetContent = m.group();
                String replaceContent = targetContent.replaceAll(id, sPrefix + id);
                contentMap.put(targetContent, replaceContent);
            }

            String reg = "\\s+parent=\\s*(('(.*?)')|(\"(.*?)\"))";
            Pattern pParent = Pattern.compile(reg);
            Matcher mParent = pParent.matcher(content);
            while (mParent.find()) {
//                print(mValue.group());
                String id;
                if (mParent.group(3) != null) {
                    id = mParent.group(3);
                } else {
                    id = mParent.group(5);
                }
                if (id.contains("/")) {
                    id = id.substring(id.indexOf("/") + 1, id.length());
                }
                if (id.startsWith(sPrefix) || id.startsWith("android:") || !sValueIds.contains(id)) {
                    continue;
                }

                String targetContent = mParent.group();
                String replaceContent = targetContent.replaceAll(id, sPrefix + id);
                contentMap.put(targetContent, replaceContent);
            }

            String regValue = ">\\s*@.*/(.*?)<";
//            String regValue = ">\\s*@.*/([^</]+?)<";
//            String regValue = ">([^</]+)</";
//            String regValue = "<.*>\\s*@.*/(.*?)(\\s*|<)";
            Pattern pValue = Pattern.compile(regValue);
            Matcher mValue = pValue.matcher(content);
            while (mValue.find()) {
//                print(mValue.group());
                String id = mValue.group(1).trim();
                if (id.startsWith(sPrefix) || id.startsWith("android:") || id.startsWith("@android:")) {
                    continue;
                }
                if (!sValueIds.contains(id)) {
                    boolean matched = false;
                    for (File file : sDrawables) {
                        String resourceName = file.getName().substring(0, file.getName().lastIndexOf("."));
                        if (id.equals(resourceName)) {
                            matched = true;
                            break;
                        }
                    }
                    if (!matched) {
                        for (File file : sAnimators) {
                            String resourceName = file.getName().substring(0, file.getName().lastIndexOf("."));
                            if (id.equals(resourceName)) {
                                matched = true;
                                break;
                            }
                        }
                    }
                    if (!matched) {
                        continue;
                    }
                }

                String targetContent = mValue.group();
                String replaceContent = targetContent.replaceAll(id, sPrefix + id);
                contentMap.put(targetContent, replaceContent);
            }

            print(String.format("[ %s ] -> %s", value.getName(), contentMap));
            if (!sExecute || contentMap.isEmpty()) {
                continue;
            }

            for (Map.Entry<String, String> entry : contentMap.entrySet()) {
                content = content.replaceAll(entry.getKey(), entry.getValue());
            }
            try {
                FileUtil.writeFile(value.getAbsolutePath(), content);
            } catch (Exception e) {
                e.printStackTrace();
                print(String.format("%s write content fail", value));
            }
        }
    }

    private static void renameLayoutFile() {
        print("\nRENAME LAYOUT FILE ==============\n");
        for (File file : sLayouts) {
            if (file.getName().startsWith(sPrefix)) {
                continue;
            }

            File newNameFile = new File(file.getParentFile(), sPrefix + file.getName());
            print(String.format("[ %s ] -> [ %s ]", file.getName(), newNameFile.getName()));
            if (!sExecute) {
                continue;
            }

            try {
                Runtime runtime = Runtime.getRuntime();
                String cmd = String.format("git mv -f %s %s", file.getAbsolutePath(), newNameFile.getAbsolutePath());
                System.out.println(cmd);
                Process exec = runtime.exec(cmd, null, file.getParentFile());
                int i = exec.waitFor();
                if (i != 0) {
                    print(String.format("[ %s ] rename fail", file.getName()));
                }
            } catch (Exception e) {
                e.printStackTrace();
                print(String.format("[ %s ] rename fail", file.getName()));
            }
//            boolean rename = file.renameTo(newNameFile);
//            if (!rename) {
//                print(String.format("[ %s ] rename fail", file.getName()));
//            }
        }
    }

    private static void renameLayoutContent() {
        print("\nREPLACE LAYOUT FILE CONTENT ==============\n");
        for (File file : sLayouts) {
            String content;
            try {
                content = FileUtil.readFile(file.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
                print(String.format("%s read content fail", file));
                return;
            }

            String[] filters = {"android:"};
            String reg = "('@(.*)/(.*?)')|(\"@(.*)/(.*?)\")";
            Pattern p = Pattern.compile(reg);
            Matcher m = p.matcher(content);
            Map<String, String> contentMap = new HashMap<>();
            while (m.find()) {
//                print(m.group());
                String targetContent = m.group();
                String type;
                String id;
                if (m.group(3) != null) {
                    type = m.group(2);
                    id = m.group(3);
                } else {
                    type = m.group(5);
                    id = m.group(6);
                }
                boolean ignore = false;
                for (String filter : filters) {
                    if (type.startsWith(filter)) {
                        ignore = true;
                        break;
                    }
                }
                if (ignore) {
                    continue;
                }

                if (type.equals("drawable") || type.equals("mipmap")) {
                    for (File resource : sDrawables) {
                        String resourceName = resource.getName().substring(0, resource.getName().lastIndexOf("."));
                        if (id.equals(resourceName) && !id.startsWith(sPrefix)) {
                            String replaceContent = targetContent.replace(id, sPrefix + id);
                            contentMap.put(targetContent, replaceContent);
                            break;
                        }
                    }
                } else if (type.equals("layout")) {
                    for (File resource : sLayouts) {
                        String resourceName = resource.getName().substring(0, resource.getName().lastIndexOf("."));
                        if (id.equals(resourceName) && !id.startsWith(sPrefix)) {
                            String replaceContent = targetContent.replace(id, sPrefix + id);
                            contentMap.put(targetContent, replaceContent);
                            break;
                        }
                    }
                } else if (type.equals("id") || type.equals("+id")) {
                    if (sViewIds.contains(id)) {
                        targetContent = targetContent.replaceAll("\\+", "\\\\+");
                        String replaceContent = targetContent.replace(id, sPrefix + id);
                        contentMap.put(targetContent, replaceContent);
                    }
                } else {
                    //TODO valueIds没有区分类型，可以会引起不同类型匹配到同一个字符串的问题
                    if (sValueIds.contains(id)) {
                        String replaceContent = targetContent.replace(id, sPrefix + id);
                        contentMap.put(targetContent, replaceContent);
                    }
                }
            }

            print(String.format("[ %s ] -> %s", file.getName(), contentMap));
            if (!sExecute || contentMap.isEmpty()) {
                continue;
            }

            for (Map.Entry<String, String> entry : contentMap.entrySet()) {
                String targetContent = entry.getKey();
                content = content.replaceAll(targetContent, entry.getValue());
            }
            try {
                FileUtil.writeFile(file.getAbsolutePath(), content);
            } catch (Exception e) {
                e.printStackTrace();
                print(String.format("%s write content fail", file));
            }
        }
    }

    private static void renameManifestContent() {
        print("\nREPLACE MANIFEST FILE CONTENT ==============\n");
        File manifest = new File(sDir, "src\\main/AndroidManifest.xml");
        String content;
        try {
            content = FileUtil.readFile(manifest.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            print(String.format("%s read content fail", manifest));
            return;
        }

        String[] filters = {"android:", "id", "+id"};
        String reg = "('@(.*)/(.*?)')|(\"@(.*)/(.*?)\")";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(content);
        Map<String, String> contentMap = new HashMap<>();
        while (m.find()) {
//            print(m.group());
            String type;
            String mId;
            if (m.group(3) != null) {
                type = m.group(2);
                mId = m.group(3);
            } else {
                type = m.group(5);
                mId = m.group(6);
            }
            boolean ignore = false;
            for (String filter : filters) {
                if (type.startsWith(filter)) {
                    ignore = true;
                    break;
                }
            }
            if (ignore) {
                continue;
            }

            //TODO valueIds没有区分类型，可以会引起不同类型匹配到同一个字符串的问题
            if (sValueIds.contains(mId)) {
                String targetContent = m.group();
                String replaceContent = targetContent.replace(mId, sPrefix + mId);
                contentMap.put(targetContent, replaceContent);
            }

            for (File resource : sDrawables) {
                String resourceName = resource.getName().substring(0, resource.getName().lastIndexOf("."));
                if (mId.equals(resourceName) && !mId.startsWith(sPrefix)) {
                    String targetContent = m.group();
                    String replaceContent = targetContent.replace(mId, sPrefix + mId);
                    contentMap.put(targetContent, replaceContent);
                    break;
                }
            }
        }

        print(String.format("[ %s ] -> %s", manifest.getName(), contentMap));
        if (!sExecute || contentMap.isEmpty()) {
            return;
        }

        for (Map.Entry<String, String> entry : contentMap.entrySet()) {
            content = content.replaceAll(entry.getKey(), entry.getValue());
        }
        try {
            FileUtil.writeFile(manifest.getAbsolutePath(), content);
        } catch (Exception e) {
            e.printStackTrace();
            print(String.format("%s write content fail", manifest));
        }
    }

    private static void renameAnimatorFile() {
        print("\nRENAME ANIMATOR FILE ==============\n");
        for (File file : sAnimators) {
            if (file.getName().startsWith(sPrefix)) {
                continue;
            }

            File newNameFile = new File(file.getParentFile(), sPrefix + file.getName());
            print(String.format("[ %s ] -> [ %s ]", file.getName(), newNameFile.getName()));
            if (!sExecute) {
                continue;
            }

            try {
                Runtime runtime = Runtime.getRuntime();
                String cmd = String.format("git mv -f %s %s", file.getAbsolutePath(), newNameFile.getAbsolutePath());
                System.out.println(cmd);
                Process exec = runtime.exec(cmd, null, file.getParentFile());
                int i = exec.waitFor();
                if (i != 0) {
                    print(String.format("[ %s ] rename fail", file.getName()));
                }
            } catch (Exception e) {
                e.printStackTrace();
                print(String.format("[ %s ] rename fail", file.getName()));
            }
//            boolean rename = file.renameTo(newNameFile);
//            if (!rename) {
//                print(String.format("[ %s ] rename fail", file.getName()));
//            }
        }
    }

    private static void renameDrawableFile() {
        print("\nRENAME DRAWABLE FILE ==============\n");
        for (File file : sDrawables) {
            if (file.getName().startsWith(sPrefix)) {
                continue;
            }

            File newNameFile = new File(file.getParentFile(), sPrefix + file.getName());
            print(String.format("[ %s ] -> [ %s ]", file.getName(), newNameFile.getName()));
            if (!sExecute) {
                continue;
            }

            try {
                Runtime runtime = Runtime.getRuntime();
                String cmd = String.format("git mv -f %s %s", file.getAbsolutePath(), newNameFile.getAbsolutePath());
                System.out.println(cmd);
                Process exec = runtime.exec(cmd, null, file.getParentFile());
                int i = exec.waitFor();
                if (i != 0) {
                    print(String.format("[ %s ] rename fail", file.getName()));
                }
            } catch (Exception e) {
                e.printStackTrace();
                print(String.format("[ %s ] rename fail", file.getName()));
            }
//            boolean rename = file.renameTo(newNameFile);
//            if (!rename) {
//                print(String.format("[ %s ] rename fail", file.getName()));
//            }
        }
    }

    private static void findFiles() {
        File javaDir = new File(sDir, "src\\main\\java");
        sJavas = FileUtil.findFileByEx(javaDir.getAbsolutePath(), ".java");

        for (File java : sJavas) {
            try {
                String content = FileUtil.readFile(java.getAbsolutePath());
                sJavaContents.put(java, content);
            } catch (Exception e) {
                e.printStackTrace();
                print(String.format("%s read content fail", java));
            }
        }

        File animatorDir = new File(sDir, "src\\main\\res/anim");
        File[] animatorFiles = animatorDir.listFiles();
        if (animatorFiles != null) {
            sAnimators.addAll(Arrays.asList(animatorFiles));
        }

        File layoutDir = new File(sDir, "src\\main\\res/layout");
        File[] layoutFiles = layoutDir.listFiles();
        if (layoutFiles != null) {
            sLayouts.addAll(Arrays.asList(layoutFiles));
        }

        findViewIds();
        findDrawableResource();
        findValueResource();
        findValueIds();
    }

    private static void findValueIds() {
        for (File value : sValues) {
            String content;
            try {
                content = FileUtil.readFile(value.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
                print(String.format("%s read content fail", value));
                continue;
            }

            Pattern p = Pattern.compile("\\s+name=\\s*(('(.*?)')|(\"(.*?)\"))(>|\\s+|/>)");
            Matcher m = p.matcher(content);
            while (m.find()) {
//                print(m.group());
                String id;
                if (m.group(3) != null) {
                    id = m.group(3);
                } else {
                    id = m.group(5);
                }
                if (id.startsWith(sPrefix) || id.startsWith("android:")) {
                    continue;
                }

                sValueIds.add(id);
            }
        }
//        print(sValueIds);
    }

    private static void findViewIds() {
        for (File file : sLayouts) {
            String content;
            try {
                content = FileUtil.readFile(file.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
                print(String.format("%s read content fail", file));
                return;
            }

            String reg = "('@\\+id/(.*?)')|(\"@\\+id/(.*?)\")";
            Pattern p = Pattern.compile(reg);
            Matcher m = p.matcher(content);
            while (m.find()) {
//                print(m.group());
                String id;
                if (m.group(2) != null) {
                    id = m.group(2);
                } else {
                    id = m.group(4);
                }
                if (id.startsWith(sPrefix)) {
                    continue;
                }

                sViewIds.add(id);
            }
        }
//        print(sViewIds);
    }

    private static void findDrawableResource() {
        if (true) {
            return;
        }

        File resDir = new File(sDir, "src\\main\\res");
        File[] drawableDirs = resDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                return s.startsWith("drawable") || s.startsWith("mipmap");
            }
        });
        for (File drawableDir : drawableDirs) {
            File[] files = drawableDir.listFiles();
            if (files != null) {
                sDrawables.addAll(Arrays.asList(files));
            }
        }
    }

    private static void findValueResource() {
        File resDir = new File(sDir, "src\\main\\res");
        File[] valuesDirs = resDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                return s.startsWith("values");
            }
        });
        if (valuesDirs == null) {
            return;
        }

        for (File valuesDir : valuesDirs) {
            File[] files = valuesDir.listFiles();
            if (files != null) {
                sValues.addAll(Arrays.asList(files));
            }
        }
    }

    private static void print(Object text) {
        System.out.println(text);
        try {
            sFw.write(text + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void init(String[] args) {
        if (args.length > 0) {
            sDir = args[0];
            sPrefix = args[1];
        }
        if (args.length >= 3 && args[2].equals("-e")) {
            sExecute = true;
        }

        try {
            sRuntimePath = new File(".").getCanonicalPath();
            sFw = new FileWriter(new File(sRuntimePath, "map.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void closeFw() {
        if (sFw != null) {
            try {
                sFw.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                sFw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
