package com.xaadin;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.vaadin.ui.ComponentContainer;
import com.xaadin.elementfactory.*;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaders;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Parser {

	private Collection<ElementFactory> elementFactories = new ArrayList<>();
	private Namespace xaadinNamespace = null;
	private URL baseURL;

	public Parser() {
		// add default factories for vaadin namespaces
		elementFactories.add(new GridLayoutElementFactory());
		elementFactories.add(new ButtonElementFactory());
		elementFactories.add(new MenuItemElementFactory());
		elementFactories.add(new PanelElementFactory());
		elementFactories.add(new AbstractOrderedLayoutElementFactory());
		elementFactories.add(new SplitPanelElementFactory());
		elementFactories.add(new TabSheetElementFactory());
		for (String namespace : Constants.DEFAULT_PACKAGE_NAMESPACES) {
			elementFactories.add(new DefaultElementFactory(namespace));
		}
	}

	private URL getParentURL(URL url) {
		try {
			return url.getPath().endsWith("/") ? url.toURI().resolve("..").toURL() : url.toURI().resolve(".").toURL();
		} catch (URISyntaxException | MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	public VisualTreeNode parseVisualTree(ComponentContainer parent, URL url, Object eventHandlerTarget) {
		baseURL = getParentURL(url);
		try (Reader reader = new InputStreamReader(new FileInputStream(new File(url.toURI())), "UTF-8")) {
			return parseVisualTree(parent, reader, eventHandlerTarget);
		} catch (URISyntaxException | IOException | ParserException e) {
			throw new RuntimeException(e);
		}
	}

	private VisualTreeNode parseVisualTree(ComponentContainer parent, Reader inputStream, Object eventHandlerTarget) throws ParserException {

		SAXBuilder builder = new SAXBuilder(XMLReaders.NONVALIDATING);

		try {
			Document doc = builder.build(inputStream);
			//FIXME: I think this is not needed with XSD schema. Please use schema validation instead!
			if (!doc.hasRootElement() && (doc.getRootElement().getName().equalsIgnoreCase(Constants.XAADIN_ROOT_ELEMENT_NAME))) {
				throw new ParserException("could not find root rootElement with name xaadin");
			}

			Element rootElement = doc.getRootElement();
			xaadinNamespace = getDefaultXaadinNamespace(rootElement);
			List<Element> childrenList = rootElement.getChildren();
			if (childrenList.isEmpty()) {
				throw new ParserException("xaadin root element is empty");
			}
			return parseVisualTreeInt(childrenList.get(0), null, eventHandlerTarget, parent);
		} catch (Exception e) {
			throw new ParserException(e);
		}
	}

	private Namespace getDefaultXaadinNamespace(Element element) {
		List<Namespace> namespaceList = element.getNamespacesInScope();
		for (Namespace namespace : namespaceList) {
			if (namespace.getURI().equalsIgnoreCase(Constants.XAADIN_NAMESPACE_1_0_0)) {
				return namespace;
			}
		}
		throw new RuntimeException("No Namespace " + Constants.XAADIN_NAMESPACE_1_0_0 + " found!");
	}

	private ElementFactory getElementFactoryForElement(String fullClassName) {
		for (ElementFactory factory : elementFactories) {
			if (factory.isClassSupportedForElementFactory(fullClassName))
				return factory;
		}
		return null;
	}

	private VisualTreeNode parseVisualTreeInt(Element element, Object parentVisualObject, Object eventHandlerTarget, Object prevConstructedParent) throws ParserException {
		String elementName = element.getName();
		Namespace namespace = element.getNamespace();
		Preconditions.checkNotNull(namespace, "No namespace given");

		if (namespace.equals(xaadinNamespace)) {
			// parse include
			if (elementName.equals(Constants.INCLUDE_NODE_NAME)) {
				return parseInclude(element, eventHandlerTarget);
			}
		}

		try {
			// find component factory for element and create the visual element
			// itself using this factory
			List<String> possibleClassNames = possibleClassNames(element, elementName);

			String fullClassName = null;
			ElementFactory factory = null;
			for (String expectedClassName : possibleClassNames) {
				factory = getElementFactoryForElement(expectedClassName);
				if (factory != null) {
					fullClassName = expectedClassName;
					break;
				}
			}

			if (factory == null) {
				throw new ParserException("could not find a valid ElementFactory for node: " + namespace + ":" + elementName);
			}

			Object component;
			if (prevConstructedParent == null) {
				component = factory.createClass(parentVisualObject, fullClassName);
			} else {
				component = prevConstructedParent;
			}

			VisualTreeNode thisNode = new VisualTreeNodeImpl(component);
			processElementAttributes(element, eventHandlerTarget, factory, component, thisNode);

			List<Element> childrenList = element.getChildren();
			for (Element child : childrenList) {
				VisualTreeNode childNode = parseVisualTreeInt(child, thisNode.getComponent(), eventHandlerTarget, null);
				thisNode.addVisualTreeNode(childNode);
				factory.addComponentToParent(thisNode, childNode);
			}

			return thisNode;
		} catch (ElementFactoryException e) {
			throw new ParserException(e);
		}
	}

	private List<String> possibleClassNames(Element element, String elementName) {
		final List<String> possibleClassNames = new ArrayList<>();
		if (element.getNamespace().equals(xaadinNamespace)) {
			// add default vaadin namespace
			for (String ns : Constants.DEFAULT_PACKAGE_NAMESPACES) {
				final String className = Character.toUpperCase(elementName.charAt(0)) + elementName.substring(1);
				possibleClassNames.add(ns + '.' + className);
			}
		}
		return possibleClassNames;
	}

	private void processElementAttributes(Element element, Object eventHandlerTarget, ElementFactory factory, Object component, VisualTreeNode parentNode) throws ElementFactoryException {
		// set setters on component
		List<Attribute> attributeList = element.getAttributes();
		for (Attribute attribute : attributeList) {
			// default xaadin attributes have no namespace
			if (attribute.getName().equals(Constants.DEFAULT_STYLE_PROPERTY_NAME)) {
				factory.setItemStyles(component, attribute.getValue().split(" "));
            } else {
                try {
                    factory.setProperty(component, attribute.getName(), attribute.getValue());
				} catch (ElementFactoryException e) {
					// retry - set parameter as additional parameter with the factory (for GridLayout etc)
					parentNode.setAdditionalParameter(attribute.getName(), attribute.getValue());
				}
			}
		}

		if (eventHandlerTarget != null) {
			factory.processEvents(parentNode, eventHandlerTarget);
		}
	}

	private VisualTreeNode parseInclude(Element element, Object eventHandlerTarget) throws ParserException {
		String includeSrc = element.getAttributeValue("src", null, null);
		if (Strings.isNullOrEmpty(includeSrc))
			throw new ParserException("found include node without a src attribute");

		// build uri and parse subnode
		try {
			URL includeFileURI = baseURL.toURI().resolve(includeSrc).toURL();
			Parser parser = new Parser();
			return parser.parseVisualTree(null, includeFileURI, eventHandlerTarget);
		} catch (MalformedURLException | URISyntaxException e) {
			throw new ParserException("error creating include url");
		}
	}

	@SuppressWarnings("UnusedDeclaration")
	public static VisualTreeNode parseInParent(ComponentContainer parent, URL uri, Object eventHandlerTarget) {
		return new Parser().parseVisualTree(parent, uri, eventHandlerTarget);
	}

	@SuppressWarnings("UnusedDeclaration")
	public static VisualTreeNode parseInParent(ComponentContainer parent, InputStream inputStream, Object eventHandlerTarget) {
		try (Reader streamReader = new InputStreamReader(inputStream)) {
			return new Parser().parseVisualTree(parent, streamReader, eventHandlerTarget);
		} catch (ParserException | IOException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("UnusedDeclaration")
	public static VisualTreeNode parse(URL url, Object eventHandlerTarget) {
		return new Parser().parseVisualTree(null, url, eventHandlerTarget);
	}

	@SuppressWarnings("UnusedDeclaration")
	public static VisualTreeNode parse(InputStream inputStream, Object eventHandlerTarget) {
		try (Reader streamReader = new InputStreamReader(inputStream)) {
			return new Parser().parseVisualTree(null, streamReader, eventHandlerTarget);
		} catch (ParserException | IOException e) {
			throw new RuntimeException(e);
		}
	}
}
