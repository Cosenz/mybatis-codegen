package it.cosenzproject.mybatiscodegen.generator;

import it.cosenzproject.mybatiscodegen.model.mapper.Mapper;

/**
 * The Generator interface
 * 
 * @author Cosenz
 *
 */
public interface Generator {

	abstract void generate(Mapper mapper);
}
