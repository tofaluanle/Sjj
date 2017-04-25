package cn.sjj.bean;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import cn.sjj.annotation.NotToString;

public class BaseBean {

	@Override
	public String toString() {
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
				if (Modifier.isFinal(field.getModifiers()) || field.isAnnotationPresent(NotToString.class)) {
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

}
