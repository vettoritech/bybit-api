package com.vettoritech.bybitapi.util;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import org.apache.commons.io.FileUtils;

public class TestUtils {

	public static String loadResourceTestFile(String path) throws Exception {
		return FileUtils.readFileToString(
				new File(Objects.requireNonNull(TestUtils.class.getClassLoader().getResource(path)).getFile()),
				StandardCharsets.UTF_8);
	}
}
