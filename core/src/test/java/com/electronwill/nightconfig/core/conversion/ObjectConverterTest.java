package com.electronwill.nightconfig.core.conversion;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.SimpleConfig;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * @author TheElectronWill
 */
public class ObjectConverterTest {

	static final List<String> list1 = Arrays.asList("a", "b", "c");
	static final List<String> list2 = Collections.singletonList("element");
	static final Config config1 = new SimpleConfig(type -> true), config2 = new SimpleConfig();

	private final Config config = new SimpleConfig();

	{
		config.setValue("integer", 1234568790);
		config.setValue("decimal", Math.PI);
		config.setValue("string", "value");
		config.setValue("stringList", list1);
		config.setValue("config", config1);
		config.setValue("subObject.integer", -1);
		config.setValue("subObject.decimal", 0.5);
		config.setValue("subObject.string", "Hey!");
		config.setValue("subObject.stringList", list2);
		config.setValue("subObject.config", config2);
		config.setValue("subObject.subObject", null);
	}

	@Test
	public void configToObjectToConfig() throws Exception {
		ObjectConverter converter = new ObjectConverter();
		MyObject object = converter.toObject(config, MyObject::new);
		Config myConfig = new SimpleConfig();
		converter.toConfig(object, myConfig);

		System.out.println("Original config: " + config);
		System.out.println("New config: " + myConfig);

		assert myConfig.equals(config) : "Invalid conversion!";
	}

	@Test
	public void test() throws Exception {
		System.out.println("====== Test with non-final fields ======");
		{
			MyObject object = new MyObject();
			testConfigToObject(config, object);// does the mapping
			assert object.integer == config.<Integer>getValue("integer");
			assert object.decimal == config.<Double>getValue("decimal");
			assert object.string.equals(config.<String>getValue("string"));
			assert object.stringList == list1;
			assert object.config == config1;
			assert object.subObject != null;
			assert object.subObject.integer == config.<Integer>getValue("subObject.integer");
			assert object.subObject.decimal == config.<Double>getValue("subObject.decimal");
			assert object.subObject.string.equals(config.<String>getValue("subObject.string"));
			assert object.subObject.stringList == list2;
			assert object.subObject.config == config2;
			assert object.subObject.subObject == null;
		}

		System.out.println();
		System.out.println("====== Test with final fields ======");
		{
			MyObjectFinal object = new MyObjectFinal();
			testConfigToObject(config, object);//does the mapping
			assert object.integer == config.<Integer>getValue("integer");
			assert object.decimal == config.<Double>getValue("decimal");
			assert object.string.equals(config.<String>getValue("string"));
			assert object.stringList == list1;
			assert object.config == config1;
			assert object.subObject != null;
			assert object.subObject.integer == config.<Integer>getValue("subObject.integer");
			assert object.subObject.decimal == config.<Double>getValue("subObject.decimal");
			assert object.subObject.string.equals(config.<String>getValue("subObject.string"));
			assert object.subObject.stringList == list2;
			assert object.subObject.config == config2;
			assert object.subObject.subObject == null;
		}
	}

	private void testConfigToObject(Config config, Object object) throws Exception {
		System.out.println("Before: " + object);

		ObjectConverter converter = new ObjectConverter();
		converter.toObject(config, object);

		System.out.println("After: " + object);
	}

	private static class MyObject {
		int integer;
		double decimal;
		String string;
		List<String> stringList;
		Config config;
		MyObject subObject;

		@Override
		public String toString() {
			return "MyObject{"
				   + "integer="
				   + integer
				   + ", decimal="
				   + decimal
				   + ", string='"
				   + string
				   + '\''
				   + ", stringList="
				   + stringList
				   + ", config="
				   + config
				   + ", subObject="
				   + subObject
				   + '}';
		}
	}

	static class MyObjectFinal {
		final int integer;
		final double decimal;
		final String string;
		final List<String> stringList;
		final Config config;
		final MyObjectFinal subObject;

		public MyObjectFinal() {
			this(123, 1.23, "v", null, null, null);
		}

		/*
		Not necessary for the mapper to work, but necessary to prevent the compiler from inlining the use
		of the primitive fields. This allows us to print the changes correctly.
		 */
		public MyObjectFinal(int integer, double decimal, String string, List<String> stringList,
							 Config config, MyObjectFinal subObject) {
			this.integer = integer;
			this.decimal = decimal;
			this.string = string;
			this.stringList = stringList;
			this.config = config;
			this.subObject = subObject;
		}

		@Override
		public String toString() {
			return "MyObjectFinal{"
				   + "integer="
				   + integer
				   + ", decimal="
				   + decimal
				   + ", string='"
				   + string
				   + '\''
				   + ", stringList="
				   + stringList
				   + ", config="
				   + config
				   + ", subObject="
				   + subObject
				   + '}';
		}
	}
}