package it.cosenzproject.mybatiscodegen.generator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.lang.model.element.Modifier;

import org.apache.ibatis.session.SqlSession;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;

import it.cosenzproject.mybatiscodegen.model.mapper.Insert;
import it.cosenzproject.mybatiscodegen.model.mapper.Mapper;
import it.cosenzproject.mybatiscodegen.model.mapper.Select;
import it.cosenzproject.mybatiscodegen.model.mapper.Update;
import it.cosenzproject.mybatiscodegen.util.ApplicationConstants;
import it.cosenzproject.mybatiscodegen.util.FieldUtil;

/**
 * Generate base dao implementation
 * 
 * @author Cosenz
 *
 */
public class GeneratorDAOProvider implements Generator {

	private static final Logger LOGGER = Logger.getLogger(GeneratorDAOProvider.class.getName());

	private static final String SQL_SESSION = "sqlSession";

	private static final String METHOD_BODY = "this.%s.getMapperType(%s.class);\nmapper.%s(entity)";

	private static final String METHOD_NAME = "execute%s";

	private static final String SUFFIX_CLASS = "Provider";

	@Override
	public void generate(Mapper mapper) {
		String daoMapper = FieldUtil.getClassName(mapper.getNamespace());
		String className = daoMapper.concat(SUFFIX_CLASS);
		try {
			TypeSpec daoProvider = TypeSpec.classBuilder(className).addModifiers(Modifier.PUBLIC)
			        .addField(SqlSession.class, SQL_SESSION, Modifier.PRIVATE)
			        .addMethods(createMethods(mapper, className, daoMapper)).build();

			JavaFile javaFile = JavaFile.builder(FieldUtil.getPackageName(mapper.getNamespace()), daoProvider).build();
			javaFile.writeTo(new File(ApplicationConstants.DAO));
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "" + e);
		}
	}

	private Iterable<MethodSpec> createMethods(Mapper mapper, String name, String daoMapper) {
		List<MethodSpec> methods = new ArrayList<>();
		ClassName mapperClass = ClassName.get(FieldUtil.getPackageName(mapper.getNamespace()),
		        FieldUtil.getClassName(mapper.getNamespace()));
		methods.add(createConstructor(name));
		for (Insert insert : mapper.getInsert()) {
			ClassName entityClass = ClassName.get(FieldUtil.getPackageName(insert.getParameterType()),
			        FieldUtil.getClassName(insert.getParameterType()));
			methods.add(MethodSpec.methodBuilder(String.format(METHOD_NAME, insert.getId()))
			        .addModifiers(Modifier.PUBLIC).addParameters(addParamters(entityClass, mapperClass))
			        .addStatement(String.format(METHOD_BODY, SQL_SESSION,
			                FieldUtil.getClassName(insert.getParameterType()), insert.getId()))
			        .returns(void.class).build());
		}
		for (Update update : mapper.getUpdate()) {
			ClassName entityClass = ClassName.get(FieldUtil.getPackageName(update.getParameterType()),
			        FieldUtil.getClassName(update.getParameterType()));
			methods.add(MethodSpec.methodBuilder(String.format(METHOD_NAME, update.getId()))
			        .addModifiers(Modifier.PUBLIC).addParameters(addParamters(entityClass, mapperClass))
			        .addStatement(String.format(METHOD_BODY, SQL_SESSION,
			                FieldUtil.getClassName(update.getParameterType()), update.getId()))
			        .returns(void.class).build());
		}
		for (Select select : mapper.getSelect()) {
			if (select.getParameterType() != null) {
				ClassName dtoClass = ClassName.get(FieldUtil.getPackageName(select.getParameterType()),
				        FieldUtil.getClassName(select.getParameterType()));
				ClassName entityClass = ClassName.get(FieldUtil.getPackageName(mapper.getNamespace()),
				        FieldUtil.getClassName(mapper.getNamespace()));
				methods.add(MethodSpec.methodBuilder(String.format(METHOD_NAME, select.getId()))
				        .addModifiers(Modifier.PUBLIC).addParameters(addParamters(dtoClass, mapperClass))
				        .addStatement(String.format(METHOD_BODY, SQL_SESSION,
				                FieldUtil.getClassName(select.getParameterType()), select.getId()))
				        .returns(entityClass).build());
			} else if (select.getResultType() != null) {
				methods.add(MethodSpec.methodBuilder(String.format(METHOD_NAME, select.getId()))
				        .addModifiers(Modifier.PUBLIC)
				        .addStatement(
				                String.format(METHOD_BODY, SQL_SESSION, Void.class.getSimpleName(), select.getId()))
				        .returns(FieldUtil.findClass(select.getResultType())).build());
			}
		}
		return methods;
	}

	private Iterable<ParameterSpec> addParamters(ClassName entityClass, ClassName mapperClass) {
		List<ParameterSpec> parameters = new ArrayList<>();
		parameters.add(ParameterSpec.builder(mapperClass, "mapper").build());
		parameters.add(ParameterSpec.builder(entityClass, "entity").build());
		return parameters;
	}

	private MethodSpec createConstructor(String name) {
		return MethodSpec.methodBuilder(name).addParameter(SqlSession.class, SQL_SESSION).addModifiers(Modifier.PUBLIC)
		        .addStatement(String.format("this.%s=%s", SQL_SESSION, SQL_SESSION)).build();
	}

}
