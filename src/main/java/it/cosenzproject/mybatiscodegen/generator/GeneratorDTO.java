package it.cosenzproject.mybatiscodegen.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.lang.model.element.Modifier;

import org.apache.commons.lang3.StringUtils;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;

import it.cosenzproject.mybatiscodegen.model.Dto;
import it.cosenzproject.mybatiscodegen.model.Property;
import it.cosenzproject.mybatiscodegen.model.mapper.Mapper;
import it.cosenzproject.mybatiscodegen.model.mapper.Select;
import it.cosenzproject.mybatiscodegen.util.ApplicationConstants;
import it.cosenzproject.mybatiscodegen.util.FieldUtil;

/**
 * Generate DTO class
 * 
 * @author Cosenz
 *
 */
public class GeneratorDTO extends GeneratePojoClass {

	private static final Logger LOGGER = Logger.getLogger(GeneratorDTO.class.getName());

	@Override
	public void generate(Mapper mapper) {
		LOGGER.info(String.format(ApplicationConstants.LOG_START, "GeneratorDTO"));
		List<Dto> dtos = new ArrayList<>();
		List<Select> selects = mapper.getSelect().stream().filter(s -> s.getParameterType() != null)
		        .filter(distinctByKey(s -> FieldUtil.getClassName(s.getParameterType()))).collect(Collectors.toList());
		for (Select s : selects) {
			Dto dto = new Dto();
			dto.setName(FieldUtil.getClassName(s.getParameterType()));
			dto.setPackageName(FieldUtil.getPackageName(s.getParameterType()));
			dtos.add(dto);
		}

		for (Dto dto : dtos) {
			for (Select s : mapper.getSelect()) {
				if (dto.getName().equals(FieldUtil.getClassName(s.getParameterType()))) {
					dto.setProperty(dto.getProperty());
					dto.getProperty().addAll(getDTOProperties(s));
				}
			}
		}
		dtos.stream().forEach(dto -> createDTO(dto, mapper.getSelect()));
		LOGGER.info(String.format(ApplicationConstants.LOG_END, "GeneratorDTO"));
	}

	private TypeSpec createTypeSearchEnum(String packageName, List<Select> selects) {
		LOGGER.info(String.format(ApplicationConstants.LOG_START, "createTypeSearchEnum"));
		Builder searchTypeBuilder = TypeSpec.enumBuilder("TipoRicerca").addModifiers(Modifier.PUBLIC);
		for (Select s : selects) {
			searchTypeBuilder.addEnumConstant(StringUtils.substring(s.getId(), 0, 1).toUpperCase(Locale.ENGLISH)
			        .concat(StringUtils.substring(s.getId(), 1, s.getId().length())));
		}
		LOGGER.info(String.format(ApplicationConstants.LOG_END, "createTypeSearchEnum"));
		return searchTypeBuilder.build();
	}

	private void createDTO(Dto dto, List<Select> selects) {
		LOGGER.info(String.format(ApplicationConstants.LOG_START, "createDTO"));
		Iterable<FieldSpec> fields = createFileds(dto.getProperty());
		TypeSpec typeEntity = TypeSpec.classBuilder(dto.getName()).addModifiers(Modifier.PUBLIC).addFields(fields)
		        .addType(createTypeSearchEnum(dto.getPackageName(), selects)).addMethods(createGetMethods(fields))
		        .addMethods(createSetMethods(fields)).build();
		writeFile(dto, typeEntity);
		LOGGER.info(String.format(ApplicationConstants.LOG_END, "createDTO"));
	}

	private Iterable<FieldSpec> createFileds(List<Property> properties) {
		List<FieldSpec> fields = new ArrayList<>();
		for (Property property : properties) {
			Class<?> type = FieldUtil.findClass(property.getType());
			if (type == null && StringUtils.isNotBlank(property.getType())) {
				fields.add(
				        FieldSpec.builder(
				                ClassName.get(FieldUtil.getPackageName(property.getType()),
				                        FieldUtil.getClassName(property.getType())),
				                property.getName(), Modifier.PRIVATE).build());
			} else {
				fields.add(
				        FieldSpec.builder(FieldUtil.findClass(property.getType()), property.getName(), Modifier.PRIVATE)
				                .build());
			}

		}
		return fields;
	}

	private List<Property> getDTOProperties(Select s) {
		String body = StringUtils.trim(s.getBody());
		List<Property> properties = new ArrayList<>();
		if (body != null && StringUtils.isNotBlank(body)) {
			String[] textSplit = body.split("#");
			for (String line : textSplit) {
				if (line.contains(ApplicationConstants.JAVA_TYPE)) {
					String linetrim = line.replaceAll("\\s+", "");
					properties.add(new Property(findNameProperty(linetrim), findTypeProperty(linetrim)));
				}
			}
		}
		return properties;
	}

	private String findNameProperty(String line) {
		int dotIndex = StringUtils.indexOf(line, ".");
		int virIndex = StringUtils.indexOf(line, ",");
		if (dotIndex > -1 && virIndex > -1) {
			return StringUtils.substring(line, dotIndex + 1, virIndex);
		}
		return "";
	}

	public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
		Map<Object, Boolean> map = new ConcurrentHashMap<>();
		return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}
}
