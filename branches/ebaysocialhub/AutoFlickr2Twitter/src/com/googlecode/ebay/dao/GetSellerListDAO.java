/**
 * 
 */
package com.googlecode.ebay.dao;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
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

import com.googlecode.ebay.domain.Item;
import com.googlecode.ebay.http.URLHelper;
import com.googlecode.flickr2twitter.com.aetrion.flickr.util.IOUtilities;

/**
 * @author hochen
 * 
 */
public class GetSellerListDAO {

	private static final String GET_SELLER_LIST_REQUEST = "<?xml version=\"1.0\" encoding=\"utf-8\"?> \n"
			+ "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n"
			+ "  <soapenv:Header>\n"
			+ "    <RequesterCredentials soapenv:mustUnderstand=\"0\" xmlns=\"urn:ebay:apis:eBLBaseComponents\">\n"
			+ "        <ebl:eBayAuthToken xmlns:ebl=\"urn:ebay:apis:eBLBaseComponents\">%s</ebl:eBayAuthToken>\n"
			+ "    </RequesterCredentials>\n"
			+ "  </soapenv:Header>\n"
			+ "  <soapenv:Body>\n"
			+ "    <GetSellerListRequest xmlns=\"urn:ebay:apis:eBLBaseComponents\">\n"
			+ "      <DetailLevel>ReturnAll</DetailLevel>\n"
			+ "      <Version>711</Version>\n"
			+ "      <UserID>%s</UserID>\n"
			+ "      <StartTimeFrom>%s</StartTimeFrom>\n"
			+ "      <StartTimeTo>%s</StartTimeTo>\n"
			+ "      <Pagination>\n"
			+ "        <EntriesPerPage>%s</EntriesPerPage>\n"
			+ "      </Pagination>\n"
			+ "    </GetSellerListRequest>\n"
			+ "  </soapenv:Body>\n" + "</soapenv:Envelope>\n";

	private static final String APP_ID = "ebaysjinternal";

	private static final String TOKEN = "AgAAAA**AQAAAA**aAAAAA**xre1Sw**nY+sHZ2PrBmdj6wVnY+sEZ2PrA2dj6wFlYCjDpaGowudj6x9nY+seQ**AgAAAA**AAMAAA**DIWV/4lHrc6zyd2bHG+Y7H7wFMfkNM72DZNhtS8iwb/DBPJph72ORDyQtnuBDNeHZPs89atJAIFQBRM/bgNTl9BOyjlUcCJFPuf2mPI4q8904jW07I8kLx1hp5aVAtJ8Z4CsAuELGfVg+f7JaFdw2+4vOHvRCDK6v/o2ucDauhz/wchtpfTnZw6djKIrmaUvJNTt6/YI0C8a5VV91DDNFSgEbzPMZ6hsDntknFDgGkCLyGf5InwJLtXvtZ7UmJ/KX86Lm8i8bTGXRNjLuwPBtjhNLzGfRyTIYK2MdIkJd8mLO9NClwuwSNPqPR4o4XoprREun4cm5YRdtXZ8ZCY4fkK2nC3FuHrgFUq5mcrJzqeKCwbqGiGXotJAfd4aU07rSCsNcuto2w0Bg067rFQNeHRubjWyky0rksVsbdV1sdkD5sOHkyp85oFoKX8F9fLNCjgKy2KoKtzc4f1RYcZBtjRd57+ViWW2Hn8mK6Trqa+wFcJ3uhomvFQkbUiIIieAX6gBkYTmovQTYgiJ4vlyM5uhLf8/DstejnCctM/azwZj8VaS9yV8SVR51bmp2p1KAXkNbGU/eVKqNdxUw06BTJfJstfEycBzhchGzC4D7OERKzpS2e7VlhhIezL+8HWXKvSfzK1MqG/KWenb0Yv8evQ0WnymnzXKkTT7emy3daD5FSanavnUmsjwHqrm3eT/5q7G1CG8LUi/Lv2Bomwj0mkAzMXQDu6w7mOl6/EFhZssD8l5WX1vj61+pJGxiYDp";

	private static final String SITE_ID = "0";

	private static final String CALL_NAME = "GetSellerList";

	private static final String CLIENT = "Nemo";

	public List<Item> getSellerList(String userId, Date StartTimeFrom,
			Date StartTimeTo, int entriesPerPage) throws IOException,
			SAXException {
		return getSellerList(APP_ID, TOKEN, userId, StartTimeFrom, StartTimeTo,
				entriesPerPage);
	}

	public List<Item> getSellerList(String appId, String token, String userId,
			Date fromTime, Date toTime, int entriesPerPage) throws IOException,
			SAXException {

		HttpURLConnection conn = getConnection(appId, token, userId, fromTime,
				toTime, entriesPerPage);

		conn.connect();

		Document document = getDocument(conn);

		return parseItems(document);
	}

	private HttpURLConnection getConnection(String appId, String token,
			String userId, Date from, Date to, int entriesPerPage)
			throws IOException {
		Map<String, String> parameters = generateParameter(appId);

		URL url = URLHelper.buildUrl(true, "api.ebay.com", -1, "/wsapi",
				parameters);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestProperty("SOAPAction", "");
		conn.setRequestProperty("Content-Type", "text/xml");
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String requestXml = String.format(GET_SELLER_LIST_REQUEST, token,
				userId, sdf.format(from), sdf.format(to),
				String.valueOf(entriesPerPage));

		System.out.println(url);
		System.out.println(requestXml);

		DataOutputStream out = new DataOutputStream(conn.getOutputStream());
		out.writeBytes(requestXml);
		out.flush();
		out.close();

		return conn;
	}

	private Map<String, String> generateParameter(String appId) {
		Map<String, String> parameters = new HashMap<String, String>();

		parameters.put("siteid", SITE_ID);
		parameters.put("callname", CALL_NAME);
		parameters.put("appid", appId);
		parameters.put("client", CLIENT);

		return parameters;
	}

	private static Document getDocument(HttpURLConnection conn)
			throws IOException, SAXException {
		Document document = null;
		InputStream in = null;
		try {

			System.out.println("response code: " + conn.getResponseCode());

			if (conn.getResponseCode() >= 400) {
				in = conn.getErrorStream();
				// FIXME exception required
				String err = IOUtilities.stringOf(in);
				System.out.println(err);
			} else {
				in = conn.getInputStream();
				DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

				document = builder.parse(in);
			}

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} finally {
			IOUtilities.close(in);
		}

		return document;
	}

	@SuppressWarnings("unchecked")
	private List<Item> parseItems(Document document) {
		if (document == null) {
			return Collections.EMPTY_LIST;
		}

		NodeList nodeList = document.getElementsByTagName("Item");
		if (nodeList == null) {
			return Collections.EMPTY_LIST;
		}

		List<Item> lstItem = new ArrayList<Item>();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);

			NodeList itemRootNode = node.getChildNodes();
			Item item = new Item();
			lstItem.add(item);
			generateItem(itemRootNode, item);
		}

		return lstItem;
	}

	private void generateItem(NodeList itemNode, Item item) {

		if (itemNode == null || item == null) {
			return;
		}

		for (int k = 0; k < itemNode.getLength(); k++) {

			Node firstLevelNode = itemNode.item(k);
			if (itemNode != null) {

				String nodeName = firstLevelNode.getNodeName();
				String nodeValue = getNodeValue(firstLevelNode);

				if (nodeName != null) {
					if ("ItemID".equals(nodeName)) {
						try {
							item.setItemId(Long.parseLong(nodeValue));
						} catch (NumberFormatException nfe) {

						}
					} else if ("Title".equals(nodeName)) {
						item.setTitle(nodeValue);
					} else if ("ListingDetails".equals(nodeName)) {
						generateItem(firstLevelNode.getChildNodes(), item);
					} else if ("ViewItemURL".equals(nodeName)) {
						item.setViewItemURL(nodeValue);
					} else if ("PictureDetails".equals(nodeName)) {
						generateItem(firstLevelNode.getChildNodes(), item);
					} else if ("GalleryURL".equals(nodeName)) {
						item.setGalleryURL(nodeValue);
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

		 String userId = "eforcity";
//		String userId = "gfw";

		Calendar tenMinBeforecurrentTime = Calendar.getInstance();
		tenMinBeforecurrentTime.add(Calendar.HOUR, -10);

		Calendar currentTime = Calendar.getInstance();

		List<Item> items = new GetSellerListDAO().getSellerList(userId,
				tenMinBeforecurrentTime.getTime(), currentTime.getTime(), 10);
		
		System.out.println("######################### items: " + items.size());
		for (Item each : items) {
			System.out.println(each);
		}

	}

}

// FIXME need a service to verify whether the userId existed
