package edu.cornell.mannlib.vitro.webapp.dynapi.data.conversion;

import edu.cornell.mannlib.vitro.webapp.dynapi.data.types.ImplementationConfig;
import edu.cornell.mannlib.vitro.webapp.dynapi.data.types.ParameterType;

public class ParameterConverter {

	public static String serialize(ParameterType type, Object input) throws ConversionException, InitializationException {
		ImplementationConfig config = type.getImplementationType().getSerializationConfig();
		if (!config.isMethodInitialized()) {
			config.setConversionMethod(new ConversionMethod(type, true));
		}
		return config.getConversionMethod().invoke(type, input).toString();
	}
	
	public static Object deserialize(ParameterType type, Object input) throws ConversionException, InitializationException {
		ImplementationConfig config = type.getImplementationType().getDeserializationConfig();
		if (!config.isMethodInitialized()) {
			config.setConversionMethod(new ConversionMethod(type, false));
		}
		return config.getConversionMethod().invoke(type, input);
	}
}
