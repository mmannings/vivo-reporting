package edu.cornell.mannlib.vitro.webapp.dynapi.data.types.implementation;

import org.apache.jena.rdf.model.ResourceFactory;

import edu.cornell.mannlib.vitro.webapp.dynapi.components.Parameter;
import edu.cornell.mannlib.vitro.webapp.dynapi.data.types.ImplementationConfig;
import edu.cornell.mannlib.vitro.webapp.dynapi.data.types.ImplementationType;
import edu.cornell.mannlib.vitro.webapp.dynapi.data.types.ParameterType;
import edu.cornell.mannlib.vitro.webapp.dynapi.data.types.RDFType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jena.rdf.model.Literal;

public class StringPlainLiteralParam extends Parameter {

	private static final String TYPE_NAME = "plain string literal";
	private static final Log log = LogFactory.getLog(StringPlainLiteralParam.class);

	public StringPlainLiteralParam(String var) {
		this.setName(var);
		try {
			ParameterType type = new ParameterType();
			type.setName(TYPE_NAME);
			ImplementationType implType = new ImplementationType();
			type.setImplementationType(implType);
			implType.setSerializationConfig(getSerializationConfig());
			implType.setDeserializationConfig(getDeserializationConfig());	
			implType.setClassName(Literal.class.getCanonicalName());
	        RDFType rdfType = new RDFType();
	        rdfType.setName("string");
	        type.setRdfType(rdfType);
			this.setType(type);
		} catch (Exception e) {
			log.error(e, e);
			throw new RuntimeException(e.getLocalizedMessage());
		}
	}
	
	private ImplementationConfig getSerializationConfig() throws ClassNotFoundException {
		ImplementationConfig serializationConfig = new ImplementationConfig();
		serializationConfig.setClassName(Literal.class.getCanonicalName());
		serializationConfig.setMethodName("getLexicalForm");
		serializationConfig.setMethodArguments("");
		serializationConfig.setStaticMethod(false);
		return serializationConfig;
	}
	
	private ImplementationConfig getDeserializationConfig() throws ClassNotFoundException {
		ImplementationConfig serializationConfig = new ImplementationConfig();
		serializationConfig.setClassName(ResourceFactory.class.getCanonicalName());
		serializationConfig.setMethodName("createPlainLiteral");
		serializationConfig.setMethodArguments("input");
		serializationConfig.setStaticMethod(true);
		return serializationConfig;
	}
}
