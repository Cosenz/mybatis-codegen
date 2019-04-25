package it.cosenzproject.mybatiscodegen.generator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.lang.model.element.Modifier;

import org.apache.commons.lang3.StringUtils;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import it.cosenzproject.mybatiscodegen.model.Entity;
import it.cosenzproject.mybatiscodegen.model.Property;
import it.cosenzproject.mybatiscodegen.model.mapper.Id;
import it.cosenzproject.mybatiscodegen.model.mapper.Insert;
import it.cosenzproject.mybatiscodegen.model.mapper.Mapper;
import it.cosenzproject.mybatiscodegen.model.mapper.Result;
import it.cosenzproject.mybatiscodegen.model.mapper.ResultMap;
import it.cosenzproject.mybatiscodegen.model.mapper.Update;
import it.cosenzproject.mybatiscodegen.util.ApplicationConstants;
import it.cosenzproject.mybatiscodegen.util.FieldUtil;

/**
 * Generate entity class
 * 
 * @author Cosenz
 *
 */
public class GeneratorEntity extends GeneratePojoClass {

	private static final Logger LOGGER = Logger.getLogger(GeneratorEntity.class.getName());

	@Override
	public void generate(Mapper mapper) {
		for (Entity e : getEntity(mapper)) {
			createEntity(e);
		}
	}

	private void createEntity(Entity entity) {
		LOGGER.info(String.format(ApplicationConstants.LOG_START, "createEntity"));
		Iterable<FieldSpec> fields = createFileds(entity.getProperty());
		TypeSpec typeEntity = TypeSpec.classBuilder(entity.getName()).addModifiers(Modifier.PUBLIC).addFields(fields)
		        .addMethods(createGetMethods(fields)).addMethods(createSetMethods(fields)).build();

		JavaFile javaFile = JavaFile.builder(entity.getPackageName(), typeEntity).build();
		try {
			javaFile.writeTo(new File(ApplicationConstants.DAO));
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
		}
		LOGGER.info(String.format(ApplicationConstants.LOG_END, "createEntity"));
	}

	private Iterable<FieldSpec> createFileds(List<Property> properties) {
		List<FieldSpec> fields = new ArrayList<>();
		for (Property property : properties) {
			fields.add(FieldSpec.builder(ClassName.get(FieldUtil.getPackageName(property.getType()),
			        FieldUtil.getClassName(property.getType())), property.getName(), Modifier.PRIVATE).build());
		}
		return fields;
	}

	private List<Entity> getEntity(Mapper mapper) {
		List<Entity> entities = new ArrayList<>();
		for (ResultMap resultMap : mapper.getResultMap()) {
			Entity entity = new Entity();
			entity.setPackageName(FieldUtil.getPackageName(resultMap.getType()));
			entity.setName(FieldUtil.getClassName(resultMap.getType()));
			entity.setProperty(getEntityProperties(mapper, resultMap));
			entities.add(entity);
		}
		return entities;
	}

	private List<Property> getEntityProperties(Mapper mapper, ResultMap resultMap) {
		List<Property> properties = new ArrayList<>();
		for (Id id : resultMap.getIds()) {
			Property p = new Property();
			p.setName(id.getProperty());
			p.setType(getPropertyType(id.getProperty(), resultMap.getType(), mapper));
			properties.add(p);
		}
		for (Result r : resultMap.getResult()) {
			Property p = new Property();
			p.setName(r.getProperty());
			p.setType(getPropertyType(r.getProperty(), resultMap.getType(), mapper));
			properties.add(p);
		}
		return properties;
	}

	private String getPropertyType(String property, String resultType, Mapper mapper) {
		Insert insert = mapper.getInsert().stream().filter(i -> resultType.equals(i.getParameterType())).findFirst()
		        .orElse(null);
		Update update = mapper.getUpdate().stream().filter(i -> resultType.equals(i.getParameterType())).findFirst()
		        .orElse(null);
		String body = insert != null && StringUtils.isNotBlank(insert.getBody()) ? insert.getBody()
		        : update != null && StringUtils.isNotBlank(update.getBody()) ? update.getBody() : "";

		return searchProperty(property, body);
	}

}
