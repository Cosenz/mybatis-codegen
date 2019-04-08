package it.cosenzproject.mybatiscodegen.util;

import org.apache.commons.lang3.StringUtils;

public class FieldUtil {

	private FieldUtil() {
		throw new IllegalStateException("Utility Class");
	}

	public static Class<?> findClass(String type) {
		switch (type) {
			case "String":
				return String.class;
			case "Integer":
			case "int":
				return Integer.class;
			case "Boolean":
			case "boolean":
				return Boolean.class;
			case "Long":
			case "long":
				return Long.class;
			case "Float":
			case "float":
				return Float.class;
			case "Character":
			case "char":
				return Character.class;
			case "Byte":
			case "byte":
				return Byte.class;
			case "Short":
			case "short":
				return Short.class;
			case "Double":
			case "double":
				return Double.class;
			case "Object":
				return Object.class;
			default:
				return null;
		}
	}

	public static String getPackageName(String type) {
		return StringUtils.substring(type, 0, StringUtils.lastIndexOf(type, '.'));
	}

	public static String getClassName(String name) {
		String[] type = StringUtils.split(name, "\\.");
		if (type != null && type.length > 0)
			return type[type.length - 1];
		return name;
	}

}
