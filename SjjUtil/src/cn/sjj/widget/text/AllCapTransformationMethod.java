package cn.sjj.widget.text;

import android.text.method.ReplacementTransformationMethod;

/**
 * 将输入的英文自动转化弄成大写显示的辅助类，edit_text.setTransformationMethod(new AllCapTransformationMethod());
 * 但是要注意这个只能改变显示，实际上getText方法获取到的还是小写字母
 * @auther 宋疆疆
 * @date 2015/10/8.
 */
public class AllCapTransformationMethod extends ReplacementTransformationMethod {

    @Override
    protected char[] getOriginal() {
        char[] aa = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        return aa;
    }

    @Override
    protected char[] getReplacement() {
        char[] cc = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        return cc;
    }

}