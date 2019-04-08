package it.cosenzproject.mybatiscodegen.parser;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;

import it.cosenzproject.mybatiscodegen.model.mapper.Mapper;

public class XmlMapperParser {

	public static Mapper readMapper(String filePath)
	        throws JAXBException, FactoryConfigurationError, XMLStreamException {
		JAXBContext jaxbContext;
		jaxbContext = JAXBContext.newInstance(Mapper.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		XMLInputFactory xif = XMLInputFactory.newFactory();
		xif.setProperty(XMLInputFactory.SUPPORT_DTD, false);
		XMLStreamReader xsr = xif.createXMLStreamReader(new StreamSource(filePath));

		return (Mapper) jaxbUnmarshaller.unmarshal(xsr);
	}
}
