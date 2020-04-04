package it.cosenzproject.mybatiscodegen.generator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.lang.model.element.Modifier;

import org.apache.commons.lang3.StringUtils;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import it.cosenzproject.mybatiscodegen.model.DAOBase;
import it.cosenzproject.mybatiscodegen.util.ApplicationConstants;
import it.cosenzproject.mybatiscodegen.util.FileUtil;

/**
 * The GeneratePojoClass
 * 
 * @author Cosenz
 *
 */
public abstract class GeneratePojoClass implements Generator {

	private static final Logger LOGGER = Logger.getLogger(GeneratePojoClass.class.getName());

	/**
	 * Create get methods
	 * 
	 * @param fields
	 * @return
	 */
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

	/**
	 * Create set methods
	 * 
	 * @param fields
	 * @return
	 */
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

	/**
	 * Search object property in query or store procedure
	 * 
	 * @param property
	 * @param body
	 * @return
	 */
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

	/**
	 * Get type of property
	 * 
	 * @param line
	 * @return
	 */
	protected String findTypeProperty(String line) {
		int typeIndex = line.indexOf(ApplicationConstants.JAVA_TYPE);
		if (typeIndex > -1) {
			return StringUtils.substring(line, typeIndex + ApplicationConstants.JAVA_TYPE.length() + 1,
			        line.indexOf(',', typeIndex));
		}
		return "";
	}

	protected void writeFile(DAOBase dao, TypeSpec typeObject) {
		try {
			FileUtil.generateFile(dao, typeObject);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "" + e);
		}
	}
}
