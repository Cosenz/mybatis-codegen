package it.cosenzproject.mybatiscodegen.util;

import java.io.File;
import java.io.IOException;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import it.cosenzproject.mybatiscodegen.model.DAOBase;

public class FileUtil {

	private FileUtil() {
		throw new IllegalStateException("Utility Class");
	}

	public static void generateFile(DAOBase dao, TypeSpec typeObject) throws IOException {
		JavaFile javaFile = JavaFile.builder(dao.getPackageName(), typeObject).build();
		javaFile.writeTo(new File(ApplicationConstants.DAO));
	}
}
