package it.cosenzproject.mybatiscodegen.generator;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;

import org.apache.commons.lang3.StringUtils;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;

import it.cosenzproject.mybatiscodegen.util.ApplicationConstants;

public abstract class GeneratePojoClass implements Generator {

	protected Iterable<MethodSpec> createGetMethods(Iterable<FieldSpec> fields) {
		List<MethodSpec> methods = new ArrayList<>();
		for (FieldSpec field : fields) {
			methods.add(MethodSpec
			        .methodBuilder(ApplicationConstants.GET
			                .concat(Character.toUpperCase(field.name.charAt(0)) + field.name.substring(1)))
			        .addModifiers(Modifier.PUBLIC).addStatement(ApplicationConstants.RETURN.concat(field.name))
			        .returns(field.type).build());
		}
		return methods;
	}

	protected Iterable<MethodSpec> createSetMethods(Iterable<FieldSpec> fields) {
		List<MethodSpec> methods = new ArrayList<>();
		for (FieldSpec field : fields) {
			methods.add(MethodSpec
			        .methodBuilder(ApplicationConstants.SET
			                .concat(Character.toUpperCase(field.name.charAt(0)) + field.name.substring(1)))
			        .addModifiers(Modifier.PUBLIC).addParameter(field.type, field.name)
			        .addStatement(ApplicationConstants.THIS.concat(field.name).concat("=").concat(field.name))
			        .returns(void.class).build());
		}
		return methods;
	}

	protected String searchProperty(String property, String body) {
		String type = String.class.getName();
		String[] textSplit = StringUtils.split(StringUtils.trim(body), "#");
		for (String line : textSplit) {
			if (line.contains(ApplicationConstants.JAVA_TYPE) && line.contains(property)) {
				String linetrim = line.replaceAll("\\s+", "");
				type = findTypeProperty(linetrim);
			}
		}
		return type;
	}

	protected String findTypeProperty(String line) {
		int typeIndex = line.indexOf(ApplicationConstants.JAVA_TYPE);
		if (typeIndex > -1) {
			return StringUtils.substring(line, typeIndex + ApplicationConstants.JAVA_TYPE.length() + 1,
			        line.indexOf(',', typeIndex));
		}
		return "";
	}
}
