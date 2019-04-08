package it.cosenzproject.mybatiscodegen.generator;

import it.cosenzproject.mybatiscodegen.model.mapper.Mapper;

public interface Generator {

	abstract void generate(Mapper mapper);
}
