package uk.co.tso.tilda.core.util;

import org.eclipse.rdf4j.model.IRI;

import static org.eclipse.rdf4j.model.util.Values.iri;

public interface Constants {
    interface TILDA {
        String NS = "https://tilda.tso.co.uk/ns#";
        String ADD_TRIPLES = NS + "additionalTriples";
        String UNION_TEMPLATE = NS + "unionTemplate";
        String INCLUDE_DATASET_CLAUSE = NS + "includeDatasetClause";
        String INCLUDE_NAMESPACES = NS + "includeNamespaces";
    }


    interface LDA {
        String NS = "http://purl.org/linked-data/api/vocab#";
        String MAX_PAGE_SIZE = NS + "maxPageSize";
        String DEFAULT_PAGE_SIZE = NS + "defaultPageSize";
        String NAME = NS + "name";
        String MIME_TYPE = NS + "mimeType";
        String STYLESHEET = "http://purl.org/linked-data/api/vocab#stylesheet";
        String VIEWER_CLASS = "http://purl.org/linked-data/api/vocab#Viewer";
        String TEMPLATE = "http://purl.org/linked-data/api/vocab#template";
        String PROPERTIES = "http://purl.org/linked-data/api/vocab#properties";
        String PROPERTY = "http://purl.org/linked-data/api/vocab#property";
        String INCLUDE = "http://purl.org/linked-data/api/vocab#include";
        String SELECTOR_CLASS = "http://purl.org/linked-data/api/vocab#Selector";
        String PARENT = "http://purl.org/linked-data/api/vocab#parent";
        String SELECT = "http://purl.org/linked-data/api/vocab#select";
        String WHERE = "http://purl.org/linked-data/api/vocab#where";
        String ORDER_BY = "http://purl.org/linked-data/api/vocab#orderBy";
        String FILTER = "http://purl.org/linked-data/api/vocab#filter";
        String SORT = "http://purl.org/linked-data/api/vocab#sort";
        String ENDPOINT_CLASS = "http://purl.org/linked-data/api/vocab#Endpoint";
        String ITEM_ENDPOINT_CLASS = "http://purl.org/linked-data/api/vocab#ItemEndpoint";
        String LIST_ENDPOINT_CLASS = "http://purl.org/linked-data/api/vocab#ListEndpoint";
        String URI_TEMPLATE = "http://purl.org/linked-data/api/vocab#uriTemplate";
        String ITEM_TEMPLATE = "http://purl.org/linked-data/api/vocab#itemTemplate";
        String SELECTOR = "http://purl.org/linked-data/api/vocab#selector";
        String VIEWER = "http://purl.org/linked-data/api/vocab#viewer";
        String DEFAULT_VIEWER = "http://purl.org/linked-data/api/vocab#defaultViewer";
        String FORMATTER = "http://purl.org/linked-data/api/vocab#formatter";
        String DEFAULT_FORMATTER = "http://purl.org/linked-data/api/vocab#defaultFormatter";
        String EXAMPLE_REQUEST_PATH = "http://purl.org/linked-data/api/vocab#exampleRequestPath";
        String SPARQL_ENDPOINT = NS + "sparqlEndpoint";
        String CONTENT_NEGOTIATION = NS + "contentNegotiation";
        String BASE = NS + "base";
        String API_CLASS = NS + "API";
        String RDFXML_FORMATTER = NS + "RdfXmlFormatter";
        String RDFJSON_FORMATTER = NS + "RdfJsonFormatter";
        String JSON_FORMATTER = NS + "JsonFormatter";
        String JSONLD_FORMATTER = NS + "JsonLDFormatter";
        String TURTLE_FORMATTER = NS + "TurtleFormatter";
        String XML_FORMATTER = NS + "XmlFormatter";
        String XSLT_FORMATTER = NS + "XsltFormatter";
        String GRAPH_TEMPLATE = NS + "graphTemplate";
        String ENDPOINT = NS + "endpoint";
        String DESCRIBE_VIEWER = NS + "describeViewer";
        String LABELLED_DESCRIBE_VIEWER = NS + "labelledDescribeViewer";
        String BASIC_VIEWER = NS + "basicViewer";

        String API_LABEL = NS + "label";
        String LANG = NS + "lang";
        String LIST = NS + "List";
        String PAGE = NS + "Page";
        String ITEMS = NS + "items";
    }

    interface FOAF {
        String NS = "http://xmlns.com/foaf/0.1/#";
        String IS_PRIMARY_TOPIC_OF = NS + "isPrimaryTopicOf";
        String PRIMARY_TOPIC = NS + "primaryTopic";
    }

    interface RDF {
        String NS = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
        String TYPE = NS + "type";
        String PROPERTY = NS + "property";
        String LIST = NS + "List";
        String FIRST = NS + "first";
        String REST = NS + "rest";
        String NIL = NS + "nil";
    }

    interface RDFS {
        String NS = "http://www.w3.org/2000/01/rdf-schema#";
        String COMMENT = NS + "comment";
        String LABEL = NS + "label";
        String RANGE = NS + "range";
    }

    interface OPENSEARCH {
        String NS = "http://a9.com/-/spec/opensearch/1.1/";
        String ITEMS_PER_PAGE = NS + "itemsPerPage";
        String START_INDEX = NS + "startIndex";
    }

    interface DCT {
        String NS = "http://purl.org/dc/terms/";
        String HAS_FORMAT = NS + "hasFormat";
        String IS_FORMAT_OF = NS + "isFormatOf";
        String FORMAT = NS + "format";
    }

    IRI ENDPOINT = iri(LDA.ENDPOINT);

    IRI GRAPH_TEMPLATE = iri(LDA.GRAPH_TEMPLATE);
    IRI API_CLASS = iri(LDA.API_CLASS);
    IRI SPARQL_ENDPOINT = iri(LDA.SPARQL_ENDPOINT);

    IRI BASE = iri(LDA.BASE);

    IRI MAX_PAGE_SIZE = iri(LDA.MAX_PAGE_SIZE);
    IRI DEFAULT_PAGE_SIZE = iri(LDA.DEFAULT_PAGE_SIZE);
    IRI CONTENT_NEGOTIATION = iri(LDA.CONTENT_NEGOTIATION);

    IRI XSLT_FORMATTER = iri(LDA.XSLT_FORMATTER);
    IRI NAME = iri(LDA.NAME);
    IRI MIME_TYPE = iri(LDA.MIME_TYPE);
    IRI STYLESHEET = iri(LDA.STYLESHEET);
    IRI VIEWER_CLASS = iri(LDA.VIEWER_CLASS);
    IRI TEMPLATE = iri(LDA.TEMPLATE);
    IRI PROPERTIES = iri(LDA.PROPERTIES);
    IRI PROPERTY = iri(LDA.PROPERTY);

    IRI INCLUDE = iri(LDA.INCLUDE);

    IRI SELECTOR_CLASS = iri(LDA.SELECTOR_CLASS);
    IRI PARENT = iri(LDA.PARENT);
    IRI SELECT = iri(LDA.SELECT);
    IRI WHERE = iri(LDA.WHERE);
    IRI ORDER_BY = iri(LDA.ORDER_BY);
    IRI FILTER = iri(LDA.FILTER);
    IRI SORT = iri(LDA.SORT);

    IRI ENDPOINT_CLASS = iri(LDA.ENDPOINT_CLASS);
    IRI ITEM_ENDPOINT_CLASS = iri(LDA.ITEM_ENDPOINT_CLASS);
    IRI LIST_ENDPOINT_CLASS = iri(LDA.LIST_ENDPOINT_CLASS);
    IRI URI_TEMPLATE = iri(LDA.URI_TEMPLATE);
    IRI ITEM_TEMPLATE = iri(LDA.ITEM_TEMPLATE);
    IRI SELECTOR = iri(LDA.SELECTOR);
    IRI VIEWER = iri(LDA.VIEWER);
    IRI DEFAULT_VIEWER = iri(LDA.DEFAULT_VIEWER);
    IRI FORMATTER = iri(LDA.FORMATTER);
    IRI DEFAULT_FORMATTER = iri(LDA.DEFAULT_FORMATTER);
    IRI EXAMPLE_REQUEST_PATH = iri(LDA.EXAMPLE_REQUEST_PATH);

    IRI API_LABEL = iri(LDA.API_LABEL);
    IRI API_LANG = iri(LDA.LANG);

    IRI RDFS_COMMENT = iri(RDFS.COMMENT);
    IRI RDFS_LABEL = iri(RDFS.LABEL);
    IRI RDFS_RANGE = iri(RDFS.RANGE);

    IRI RDF_TYPE = iri(RDF.TYPE);
    IRI RDF_PROPERTY = iri(RDF.PROPERTY);
    IRI RDF_LIST = iri(RDF.LIST);
    IRI RDF_FIRST = iri(RDF.FIRST);
    IRI RDF_REST = iri(RDF.REST);
    IRI RDF_NIL = iri(RDF.NIL);

    IRI FOAF_IS_PRIMARY_TOPIC_OF = iri(FOAF.IS_PRIMARY_TOPIC_OF);
    IRI FOAF_PRIMARY_TOPIC = iri(FOAF.PRIMARY_TOPIC);

    IRI API_PAGE = iri(LDA.PAGE);
    IRI API_LIST = iri(LDA.LIST);
    IRI API_ITEMS = iri(LDA.ITEMS);

    IRI OPENSEARCH_ITEMS_PER_PAGE = iri(OPENSEARCH.ITEMS_PER_PAGE);
    IRI OPENSEARCH_START_INDEX = iri(OPENSEARCH.START_INDEX);

    IRI DCT_HAS_FORMAT = iri(DCT.HAS_FORMAT);
    IRI DCT_IS_FORMAT_OF = iri(DCT.IS_FORMAT_OF);
    IRI DCT_FORMAT = iri(DCT.FORMAT);

    IRI TILDA_ADDITIONAL_TRIPLES = iri(TILDA.ADD_TRIPLES);
    IRI TILDA_UNION_TEMPLATE = iri(TILDA.UNION_TEMPLATE);
    IRI TILDA_INCLUDE_DATASET_CLAUSE = iri(TILDA.INCLUDE_DATASET_CLAUSE);
    IRI TILDA_INCLUDE_NAMESPACES = iri(TILDA.INCLUDE_NAMESPACES);
}
