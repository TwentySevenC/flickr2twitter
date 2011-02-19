/**
 * 
 */
package com.googlecode.flickr2twitter.impl.ebay;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 * @author hochen
 * 
 */
public class FindItemsDAO {

	private String sellerId = "eforcity";
	
	public List<EbayItem> findItemsByKeywords() throws IOException, SAXException {

		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("SECURITY-APPNAME",
				"eBay929a8-96bf-4ad8-a71c-94de77a7c9e");
		parameters.put("OPERATION-NAME", "findItemsByKeywords");
		parameters.put("SERVICE-VERSION", "1.9.0");
		parameters.put("RESPONSE-DATA-FORMAT", "XML");
		parameters.put("REST-PAYLOAD", "");
		parameters.put("affiliate.networkId", "9");
		parameters.put("affiliate.trackingId", "1234567890");
		parameters.put("affiliate.customId", "k-man");
		parameters.put("sortOrder", "EndTime");
		parameters.put("paginationInput.entriesPerPage", "20");
		parameters.put("keywords", "watch");
		parameters.put("itemFilter[0].name", "Seller");
		parameters.put("itemFilter[0].value", sellerId);
		parameters.put("descriptionSearch", "true");

		URL url = URLHelper.buildUrl(false, "svcs.ebay.com", 80,
				"/services/search/FindingService/v1", parameters);

		System.out.println(url);

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");

		conn.connect();

		InputStream in = null;
		Document document = null;
		try {

			in = conn.getInputStream();

			DocumentBuilderFactory builderFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = builderFactory.newDocumentBuilder();

			document = builder.parse(in);

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {

				}
			}
		}

		if (document == null) {
			return null;
		}

		NodeList nodeList = document.getElementsByTagName("item");
		List<EbayItem> lstItem = new ArrayList<EbayItem>();
		if (nodeList != null) {
			
			for (int i = 0; i < nodeList.getLength(); i++) {

				Node node = nodeList.item(i);

				NodeList itemRootNode = node.getChildNodes();
				EbayItem item = new EbayItem(sellerId);
				lstItem.add(item);
				generateItem(itemRootNode, item);

			}
		}

		return lstItem;
	}
	
	private void generateItem(NodeList itemNode, EbayItem item) {
		
		if (itemNode == null || item == null) {
			return;
		}
		
		for (int k = 0; k < itemNode.getLength(); k++) {

			Node firstLevelNode = itemNode.item(k);
			if (itemNode != null) {

				String nodeName = firstLevelNode.getNodeName();
				String nodeValue = getNodeValue(firstLevelNode);
				
				if (nodeName != null) {
					if ("itemId".equals(nodeName)) {
						try {
							item.setItemId(Long.parseLong(nodeValue));
						} catch (NumberFormatException nfe) {

						}
					} else if ("title".equals(nodeName)) {
						item.setTitle(nodeValue);
					}
				}

			}

		}
	}
	
	

	private String getNodeValue(Node node) {

		if (node == null || node.getFirstChild() == null) {
			return null;
		}

		return node.getFirstChild().getNodeValue();

	}

	public static void main(String args[]) throws Exception {
		FindItemsDAO ItemDAO = new FindItemsDAO();
		System.out.println(ItemDAO.findItemsByKeywords());
	}

}
