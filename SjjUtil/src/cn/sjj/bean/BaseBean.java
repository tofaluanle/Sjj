package cn.sjj.bean;

import android.os.Parcelable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import cn.sjj.BuildConfig;
import cn.sjj.annotation.NotToString;

/**
 * 基础bean，提供打印子类bean的属性的功能
 *
 * @author 宋疆疆
 * @since 2018/4/11.
 */
public class BaseBean {

    @Override
    public String toString() {
        if (!BuildConfig.DEBUG) {
            return super.toString();
        }

        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName() + " [ ");
        getValue(getClass(), sb);
        sb.delete(sb.length() - 2, sb.length());
        sb.append(" ]");
        return sb.toString();
    }

    private void getValue(Class clazz, StringBuilder sb) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            try {
                if (isNotToStringField(field)) {
                    continue;
                }

                field.setAccessible(true);
                Object object = field.get(this);
                String value = field.getName() + " = " + object + ", ";
                sb.append(value);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        String name = BaseBean.class.getName();
        if (clazz.getName().equals(name)) {
            return;
        }
        getValue(clazz.getSuperclass(), sb);
    }

    protected boolean isNotToStringField(Field field) {
        return Modifier.isFinal(field.getModifiers())
                || field.isAnnotationPresent(NotToString.class)
                || Parcelable.Creator.class.isAssignableFrom(field.getType());
    }

}
