package com.xaadin;

import com.google.common.base.Strings;
import com.vaadin.ui.ComponentContainer;
import com.xaadin.elementfactory.*;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Parser {

	private Collection<ElementFactory> elementFactories = new ArrayList<>();
	private Namespace vaadinNamespace = null;
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

	private URL getParentURL(URL url) throws MalformedURLException, URISyntaxException {
		return url.getPath().endsWith("/") ? url.toURI().resolve("..").toURL() : url.toURI().resolve(".").toURL();
	}

	public VisualTreeNode parseVisualTree(ComponentContainer parent, URL url, Object eventHandlerTarget) throws ParserException {
		Reader reader;
		try {
			baseURL = getParentURL(url);
			reader = new InputStreamReader(new FileInputStream(new File(url.toURI())), "UTF-8");
			return parseVisualTree(parent, reader, eventHandlerTarget);
		} catch (Exception e) {
			throw new ParserException(e);
		}
	}

	private VisualTreeNode parseVisualTree(ComponentContainer parent, Reader inputStream, Object eventHandlerTarget) throws ParserException {

		SAXBuilder builder = new SAXBuilder();
		try {
			Document doc = builder.build(inputStream);
			if (!doc.hasRootElement() && (doc.getRootElement().getName().equalsIgnoreCase(Constants.XAADIN_ROOT_ELEMENT_NAME))) {
				throw new ParserException("could not find root element with name xaadin");
			}

			Element element = doc.getRootElement();
			getDefaultVaadinNamespace(element);
			getDefaultXaadinNamespace(element);
			List<Element> childrenList = element.getChildren();
			if (childrenList.size() != 1) {
				throw new ParserException("xaadin node should only contain one child");
			}
			return parseVisualTreeInt(childrenList.get(0), null, eventHandlerTarget, parent);
		} catch (Exception e) {
			throw new ParserException(e);
		}
	}

	private void getDefaultVaadinNamespace(Element element) {
		List<Namespace> namespaceList = element.getNamespacesInScope();
		for (Namespace namespace : namespaceList) {
			if (namespace.getURI().equalsIgnoreCase(Constants.DEFAULT_VAADIN_NAMESPACE)) {
				vaadinNamespace = namespace;
				break;
			}
		}
	}

	private void getDefaultXaadinNamespace(Element element) {
		List<Namespace> namespaceList = element.getNamespacesInScope();
		for (Namespace namespace : namespaceList) {
			if (namespace.getURI().equalsIgnoreCase(Constants.DEFAULT_XAADIN_NAMESPACE)) {
				xaadinNamespace = namespace;
				break;
			}
		}
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
		String namespace = element.getNamespacePrefix();

		if ((element.getNamespace() != null) && element.getNamespace().equals(xaadinNamespace)) {
			// parse include
			if (elementName.equals(Constants.INCLUDE_NODE_NAME)) {
				return parseInclude(element, eventHandlerTarget);
			}
		}

		try {
			// find component factory for element and create the visual element
			// itself using this factory
			ElementFactory factory = null;
			String fullClassName = null;

			if (element.getNamespace().equals(vaadinNamespace)) {
				// add default vaadin namespace
				for (String ns : Constants.DEFAULT_PACKAGE_NAMESPACES) {
					String className = ns + "." + elementName;
					factory = getElementFactoryForElement(className);
					if (factory != null) {
						fullClassName = className;
						break;
					}
				}
			}

			if (factory == null) {
				if (Strings.isNullOrEmpty(namespace)) {
					throw new ParserException("could not find a valid ElementFactory for node: " + elementName);
				} else {
					throw new ParserException("could not find a valid ElementFactory for node: " + namespace + ":" + elementName);
				}
			}

			Object component;
			if (prevConstructedParent == null) {
				component = factory.createClass(parentVisualObject, fullClassName);
			} else {
				component = prevConstructedParent;
			}

			VisualTreeNode parentNode = new VisualTreeNodeImpl(component);
			processElementAttributes(element, eventHandlerTarget, factory, component, parentNode);

			List<Element> childrenList = element.getChildren();
			for (Element child : childrenList) {
				VisualTreeNode childNode = parseVisualTreeInt(child, parentNode.getComponent(), eventHandlerTarget, null);
				parentNode.addVisualTreeNode(childNode);
				factory.addComponentToParent(parentNode, childNode);
			}

			return parentNode;
		} catch (ElementFactoryException e) {
			throw new ParserException(e);
		}

	}

	private void processElementAttributes(Element element, Object eventHandlerTarget, ElementFactory factory, Object component, VisualTreeNode parentNode) throws ElementFactoryException {
		// set setters on component
		List<Attribute> attributeList = element.getAttributes();
		for (Attribute attribute : attributeList) {
			// default xaadin attributes have no namespace
			if (attribute.getNamespace().equals(vaadinNamespace)) {
				factory.setProperty(component, attribute.getName(), attribute.getValue());
			} else if (attribute.getNamespace().equals(xaadinNamespace) && attribute.getName().equals(Constants.DEFAULT_STYLE_PROPERTY_NAME)) {
				factory.setItemStyles(component, attribute.getValue().split(" "));
			} else if (attribute.getNamespace().equals(xaadinNamespace)) {
				// everything from the xaadin namespace is added to the additional parameters list
				// i.e. GridLayout.row or GridLayout.col, which are used later for this element
				// in the GridLayoutElementFactory.addComponentToParent function
				parentNode.setAdditionalParameter(attribute.getName(), attribute.getValue());
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
		try {
			return new Parser().parseVisualTree(parent, uri, eventHandlerTarget);
		} catch (ParserException e) {
			throw new RuntimeException(e);
		}
	}

	public static VisualTreeNode parse(URL url, Object eventHandlerTarget) {
		try {
			return new Parser().parseVisualTree(null, url, eventHandlerTarget);
		} catch (ParserException e) {
			throw new RuntimeException(e);
		}
	}
}
