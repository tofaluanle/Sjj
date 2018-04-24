package cn.sjj.tool;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FormatResourceName {

    private static String  sDir     = "C:\\WorkSpace\\temp\\android-credit/";
    private static String  sPrefix  = "sjj_";
    //        private static boolean sExecute = true;
    private static boolean sExecute = false;

    private static List<File>        sJavas        = new ArrayList<>();
    private static Map<File, String> sJavaContents = new HashMap<>();
    private static List<File>        sAnimators    = new ArrayList<>();
    private static List<File>        sDrawables    = new ArrayList<>();
    private static List<File>        sLayouts      = new ArrayList<>();
    private static List<File>        sValues       = new ArrayList<>();
    private static List<String>      sValueIds     = new ArrayList<>();

    private static int sLayoutNameMaxLength;

    public static void main(String[] args) {
//        testResourcePattern();
//        testLayoutPattern();
        findFiles();
        rename();
    }

    private static void testLayoutPattern() {
        String str = "<RelativeLayout\n" +
                "                android:layout_width=\"@string/zzz\"\n" +
                "                android:layout_height=\"250dp\"\n" +
                "android:background=\"@drawable/credit_score_bg\">";
//        String noAndroidAndId = "";
        Pattern p = Pattern.compile("('@.*/(.*?)')|(\"@.*/(.*?)\")");
        Matcher m = p.matcher(str);
        while (m.find()) {
            System.out.println(m.group());
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
            System.out.println(m.group());

            String group = m.group();
            p = Pattern.compile("(\"(.*?)\")|('(.*?)')");
            Matcher m2 = p.matcher(group);
            while (m2.find()) {
                System.out.println(m2.group());
            }
        }
    }

    private static void rename() {
        renameValueContent();
//        renameAnimatorContent();
//        renameDrawableContent();
//        renameJavaContent();
//        renameLayoutContent();
//        renameManifestContent();
//        renameAnimatorFile();
//        renameDrawableFile();
//        renameLayoutFile();
    }

    private static void renameDrawableContent() {
        System.out.println("\nREPLACE DRAWABLE FILE CONTENT ==============\n");
        for (File file : sDrawables) {
            if (!file.getName().endsWith(".xml")) {
                continue;
            }

            String content;
            try {
                content = FileUtil.readFile(file.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(String.format("%s read content fail", file));
                return;
            }

            String[] filters = {"android:", "id", "+id"};
            String reg = "('@(.*)/(.*?)')|(\"@(.*)/(.*?)\")";
            Pattern p = Pattern.compile(reg);
            Matcher m = p.matcher(content);
            Map<String, String> contentMap = new HashMap<>();
            while (m.find()) {
//                System.out.println(m.group());
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

            if (!sExecute) {
                System.out.println(String.format("[ %s ] -> %s", file.getName(), contentMap));
                continue;
            }
            if (contentMap.isEmpty()) {
                continue;
            }

            for (Map.Entry<String, String> entry : contentMap.entrySet()) {
                content = content.replaceAll(entry.getKey(), entry.getValue());
            }
            try {
                FileUtil.writeFile(file.getAbsolutePath(), content);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(String.format("%s write content fail", file));
            }
        }
    }

    private static void renameAnimatorContent() {
    }

    private static void renameJavaContent() {
        System.out.println("\nREPLACE JAVA FILE CONTENT ==============\n");
        for (File java : sJavaContents.keySet()) {
//                System.out.println(java);
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

            if (!sExecute) {
                System.out.println(String.format("[ %s ] -> %s", java.getName(), targetContents));
                continue;
            }
            if (targetContents.isEmpty()) {
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
                System.out.println(String.format("%s write content fail", java));
            }
        }
    }

    private static void renameValueContent() {
        System.out.println("\nREPLACE VALUE FILE CONTENT ==============\n");

        for (File value : sValues) {
            Map<String, String> contentMap = new HashMap<>();
            String content;
            try {
                content = FileUtil.readFile(value.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(String.format("%s read content fail", value));
                continue;
            }

            Pattern p = Pattern.compile("\\s+name=\\s*(('(.*?)')|(\"(.*?)\"))(>|\\s+|/>)");
            Matcher m = p.matcher(content);
            while (m.find()) {
//                System.out.println(m.group());
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
                String targetContent = m.group();
                String replaceContent = targetContent.replaceAll(id, sPrefix + id);
                contentMap.put(targetContent, replaceContent);
            }

            String reg = "\\s+parent=\\s*(('(.*?)')|(\"(.*?)\"))";
            Pattern pParent = Pattern.compile(reg);
            Matcher mParent = pParent.matcher(content);
            while (mParent.find()) {
//                System.out.println(mValue.group());
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
                System.out.println(mValue.group());
                String id = mValue.group(1).trim();
                if (id.startsWith(sPrefix) || id.startsWith("android:") || id.startsWith("@android:") || !sValueIds.contains(id)) {
                    continue;
                }

                String targetContent = mValue.group();
                String replaceContent = targetContent.replaceAll(id, sPrefix + id);
                contentMap.put(targetContent, replaceContent);
            }

            if (!sExecute) {
                System.out.println(String.format("[ %s ] -> %s", value.getName(), contentMap));
                continue;
            }
            if (contentMap.isEmpty()) {
                continue;
            }

            for (Map.Entry<String, String> entry : contentMap.entrySet()) {
                content = content.replaceAll(entry.getKey(), entry.getValue());
            }
            try {
                FileUtil.writeFile(value.getAbsolutePath(), content);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(String.format("%s write content fail", value));
            }
        }
    }

    private static void renameLayoutFile() {
        System.out.println("\nRENAME LAYOUT FILE ==============\n");
        for (File layout : sLayouts) {
            File newNameFile = new File(layout.getParentFile(), sPrefix + layout.getName());
            if (!sExecute) {
                System.out.println(String.format("[ %s ] -> [ %s ]", layout.getName(), newNameFile.getName()));
                continue;
            }

            boolean rename = layout.renameTo(newNameFile);
            if (!rename) {
                System.out.println(String.format("[ %s ] rename fail", layout.getName()));
            }
        }
    }

    private static void renameLayoutContent() {
        System.out.println("\nREPLACE LAYOUT FILE CONTENT ==============\n");
        for (File file : sLayouts) {
            String content;
            try {
                content = FileUtil.readFile(file.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(String.format("%s read content fail", file));
                return;
            }

            String[] filters = {"android:", "id", "+id"};
            String reg = "('@(.*)/(.*?)')|(\"@(.*)/(.*?)\")";
            Pattern p = Pattern.compile(reg);
            Matcher m = p.matcher(content);
            Map<String, String> contentMap = new HashMap<>();
            while (m.find()) {
//                System.out.println(m.group());
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

            if (!sExecute) {
                System.out.println(String.format("[ %s ] -> %s", file.getName(), contentMap));
                continue;
            }
            if (contentMap.isEmpty()) {
                continue;
            }

            for (Map.Entry<String, String> entry : contentMap.entrySet()) {
                content = content.replaceAll(entry.getKey(), entry.getValue());
            }
            try {
                FileUtil.writeFile(file.getAbsolutePath(), content);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(String.format("%s write content fail", file));
            }
        }
    }

    private static void renameManifestContent() {
        System.out.println("\nREPLACE MANIFEST FILE CONTENT ==============\n");
        File manifest = new File(sDir, "src\\main/AndroidManifest.xml");
        String content;
        try {
            content = FileUtil.readFile(manifest.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(String.format("%s read content fail", manifest));
            return;
        }

        String[] filters = {"android:", "id", "+id"};
        String reg = "('@(.*)/(.*?)')|(\"@(.*)/(.*?)\")";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(content);
        Map<String, String> contentMap = new HashMap<>();
        while (m.find()) {
//            System.out.println(m.group());
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
            for (String id : sValueIds) {
                if (mId.equals(id) && !mId.startsWith(sPrefix)) {
                    String targetContent = m.group();
                    String replaceContent = targetContent.replace(mId, sPrefix + mId);
                    contentMap.put(targetContent, replaceContent);
                    break;
                }
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

        if (!sExecute) {
            System.out.println(String.format("[ %s ] -> %s", manifest.getName(), contentMap));
            return;
        }
        if (contentMap.isEmpty()) {
            return;
        }

        for (Map.Entry<String, String> entry : contentMap.entrySet()) {
            content = content.replaceAll(entry.getKey(), entry.getValue());
        }
        try {
            FileUtil.writeFile(manifest.getAbsolutePath(), content);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(String.format("%s write content fail", manifest));
        }
    }

    private static void renameAnimatorFile() {
        System.out.println("\nRENAME ANIMATOR FILE ==============\n");
        for (File file : sAnimators) {
            File newNameFile = new File(file.getParentFile(), sPrefix + file.getName());
            if (!sExecute) {
                System.out.println(String.format("[ %s ] -> [ %s ]", file.getName(), newNameFile.getName()));
                continue;
            }

            boolean rename = file.renameTo(newNameFile);
            if (!rename) {
                System.out.println(String.format("[ %s ] rename fail", file.getName()));
            }
        }
    }

    private static void renameDrawableFile() {
        System.out.println("\nRENAME DRAWABLE FILE ==============\n");
        for (File drawable : sDrawables) {
            File newNameFile = new File(drawable.getParentFile(), sPrefix + drawable.getName());
            if (!sExecute) {
                System.out.println(String.format("[ %s ] -> [ %s ]", drawable.getName(), newNameFile.getName()));
                continue;
            }

            boolean rename = drawable.renameTo(newNameFile);
            if (!rename) {
                System.out.println(String.format("[ %s ] rename fail", drawable.getName()));
            }
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
                System.out.println(String.format("%s read content fail", java));
            }
        }

        File animatorDir = new File(sDir, "src\\main\\res/anim");
        File[] animatorFiles = animatorDir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return !file.getName().startsWith(sPrefix);
            }
        });
        sAnimators.addAll(Arrays.asList(animatorFiles));

        File layoutDir = new File(sDir, "src\\main\\res/layout");
        File[] layoutFiles = layoutDir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return !file.getName().startsWith(sPrefix);
            }
        });
        sLayouts.addAll(Arrays.asList(layoutFiles));

        findDrawableResource();
        findValueResource();
    }

    private static void findDrawableResource() {
        File resDir = new File(sDir, "src\\main\\res");
        File[] drawableDirs = resDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                return s.startsWith("drawable") || s.startsWith("mipmap");
            }
        });
        for (File drawableDir : drawableDirs) {
            File[] files = drawableDir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    return !file.getName().startsWith(sPrefix);
                }
            });
            sDrawables.addAll(Arrays.asList(files));
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
        for (File valuesDir : valuesDirs) {
            File[] files = valuesDir.listFiles();
            sValues.addAll(Arrays.asList(files));
        }
    }

}
