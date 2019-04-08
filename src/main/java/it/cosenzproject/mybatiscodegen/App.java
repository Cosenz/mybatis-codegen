package it.cosenzproject.mybatiscodegen;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;

import it.cosenzproject.mybatiscodegen.generator.GeneratorDAOMapper;
import it.cosenzproject.mybatiscodegen.generator.GeneratorDTO;
import it.cosenzproject.mybatiscodegen.generator.GeneratorEntity;
import it.cosenzproject.mybatiscodegen.model.mapper.Mapper;
import it.cosenzproject.mybatiscodegen.parser.XmlMapperParser;

/**
 * Start DAO class from mybatis xml mapper
 *
 */
public class App {

	private static final Logger LOGGER = Logger.getLogger(App.class.getName());

	public static void main(String[] args) {
		try {
			String mapperFile = System.getProperty("inputFile");
			if (StringUtils.isNotBlank(mapperFile)) {
				Mapper mapper = XmlMapperParser.readMapper(System.getProperty("inputFile"));
				// Write DAO interface
				GeneratorDAOMapper generatorDAO = new GeneratorDAOMapper();
				generatorDAO.generate(mapper);
				// Write DTO class
				GeneratorDTO generatorDTO = new GeneratorDTO();
				generatorDTO.generate(mapper);
				// Write Entity class
				GeneratorEntity generatorEntity = new GeneratorEntity();
				generatorEntity.generate(mapper);
			} else {
				LOGGER.log(Level.SEVERE, "inputFile is required");
			}
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "" + e);
		}
	}
}
