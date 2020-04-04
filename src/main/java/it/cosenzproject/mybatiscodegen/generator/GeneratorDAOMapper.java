package it.cosenzproject.mybatiscodegen.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.lang.model.element.Modifier;

import org.apache.ibatis.annotations.Param;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;

import it.cosenzproject.mybatiscodegen.model.DAOMapper;
import it.cosenzproject.mybatiscodegen.model.Method;
import it.cosenzproject.mybatiscodegen.model.Property;
import it.cosenzproject.mybatiscodegen.model.ResultMethod;
import it.cosenzproject.mybatiscodegen.model.mapper.Insert;
import it.cosenzproject.mybatiscodegen.model.mapper.Mapper;
import it.cosenzproject.mybatiscodegen.model.mapper.ResultMap;
import it.cosenzproject.mybatiscodegen.model.mapper.Select;
import it.cosenzproject.mybatiscodegen.model.mapper.Update;
import it.cosenzproject.mybatiscodegen.util.ApplicationConstants;
import it.cosenzproject.mybatiscodegen.util.FieldUtil;

/**
 * Generate DAO interface
 * 
 * @author Cosenz
 *
 */
public class GeneratorDAOMapper extends GeneratePojoClass implements Generator {

	private static final Logger LOGGER = Logger.getLogger(GeneratorDAOMapper.class.getName());

	@Override
	public void generate(Mapper mapper) {
		LOGGER.info(String.format(ApplicationConstants.LOG_START, "GeneratorDAOMapper"));
		createDAOMapper(getDAOMapper(mapper));
		LOGGER.info(String.format(ApplicationConstants.LOG_END, "GeneratorDAOMapper"));
	}

	private void createDAOMapper(DAOMapper daoMapper) {

		Iterable<MethodSpec> methods = createMethods(daoMapper.getMethods());
		TypeSpec daoTypeSpec = TypeSpec.interfaceBuilder(daoMapper.getName()).addModifiers(Modifier.PUBLIC)
		        .addMethods(methods).build();

		writeFile(daoMapper, daoTypeSpec);
	}

	private Iterable<MethodSpec> createMethods(List<Method> methods) {
		List<MethodSpec> methodSpec = new ArrayList<>();
		for (Method m : methods) {
			if (m.getParameterType().getName() != null) {
				methodSpec.add(MethodSpec
				        .methodBuilder(m.getName()).addModifiers(Modifier.PUBLIC,
				                Modifier.ABSTRACT)
				        .addParameter(ParameterSpec
				                .builder(
				                        ClassName.get(FieldUtil.getPackageName(m.getParameterType().getType()),
				                                FieldUtil.getClassName(m.getParameterType().getType())),
				                        m.getParameterType().getName())
				                .addAnnotation(AnnotationSpec.builder(Param.class)
				                        .addMember("value", "$S", m.getParameterType().getName()).build())
				                .build())
				        .returns(m.getResult() != null ? ClassName.get(m.getResult().getType(), m.getResult().getName())
				                : ClassName.get(Void.class))
				        .build());
			} else {
				methodSpec.add(MethodSpec.methodBuilder(m.getName()).addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
				        .returns(m.getResult() != null ? ClassName.get(m.getResult().getType(), m.getResult().getName())
				                : ClassName.get(Void.class))
				        .build());
			}

		}
		return methodSpec;
	}

	private DAOMapper getDAOMapper(Mapper mapper) {
		LOGGER.info(String.format(ApplicationConstants.LOG_START, "getDAOMapper"));
		DAOMapper daoMapper = new DAOMapper();
		daoMapper.setPackageName(FieldUtil.getPackageName(mapper.getNamespace()));
		daoMapper.setName(FieldUtil.getClassName(mapper.getNamespace()));
		LOGGER.info("Add select operation");
		for (Select s : mapper.getSelect()) {
			daoMapper.getMethods().add(addMethod(mapper, s));
		}
		LOGGER.info("Add insert operation");
		for (Insert i : mapper.getInsert()) {
			daoMapper.getMethods().add(addMethod(i));
		}
		LOGGER.info("Add update operation");
		for (Update u : mapper.getUpdate()) {
			daoMapper.getMethods().add(addMethod(u));
		}
		LOGGER.info(String.format(ApplicationConstants.LOG_END, "getDAOMapper"));
		return daoMapper;
	}

	private Method addMethod(Mapper mapper, Select s) {
		Method method = new Method();
		method.setName(s.getId());
		method.setParameterType(getParameters(s));
		if (s.getResultType() != null) {
			method.setResult(new ResultMethod(s.getResultType(), "java.lang.".concat(s.getResultType())));
		} else {
			ResultMap resultMap = mapper.getResultMap().stream()
			        .filter(result -> result.getId().equals(s.getResultMap())).findFirst().orElse(null);
			method.setResult(new ResultMethod(FieldUtil.getClassName(resultMap.getType()),
			        FieldUtil.getPackageName(resultMap.getType())));
		}
		return method;
	}

	private Method addMethod(Insert i) {
		Method method = new Method();
		method.setName(i.getId());
		method.setParameterType(getParameters(i));
		return method;
	}

	private Method addMethod(Update u) {
		Method method = new Method();
		method.setName(u.getId());
		method.setParameterType(getParameters(u));
		return method;
	}

	private Property getParameters(Select s) {
		Property property = new Property();
		property.setName(parameterName(FieldUtil.getClassName(s.getParameterType())));
		property.setType(s.getParameterType());
		return property;
	}

	private Property getParameters(Insert i) {
		Property property = new Property();
		property.setName(parameterName(FieldUtil.getClassName(i.getParameterType())));
		property.setType(i.getParameterType());
		return property;
	}

	private Property getParameters(Update u) {
		Property property = new Property();
		property.setName(parameterName(FieldUtil.getClassName(u.getParameterType())));
		property.setType(u.getParameterType());
		return property;
	}

	private String parameterName(String entityName) {
		if (entityName != null) {
			return Character.toLowerCase(entityName.charAt(0)) + entityName.substring(1);
		}
		return entityName;
	}
}
