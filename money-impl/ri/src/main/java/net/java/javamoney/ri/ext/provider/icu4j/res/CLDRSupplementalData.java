package net.java.javamoney.ri.ext.provider.icu4j.res;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public final class CLDRSupplementalData extends AbstractXmlResource {

	private static final CLDRSupplementalData INSTANCE = createInstance();

	/*
	 * <region iso3166="AD"> <currency iso4217="EUR" from="1999-01-01"/>
	 * <currency iso4217="ESP" from="1873-01-01" to="2002-02-28"/> <currency
	 * iso4217="FRF" from="1960-01-01" to="2002-02-17"/> <currency iso4217="ADP"
	 * from="1936-01-01" to="2001-12-31"/> </region>
	 */
	private Map<String, Currency4Region> currencyRegionData = new HashMap<String, Currency4Region>();

	private CLDRSupplementalData() throws MalformedURLException {
		super(
				"CLDR-SupplementalData",
				new URL(
						"http://unicode.org/repos/cldr/trunk/common/supplemental/supplementalData.xml"),
				CLDRSupplementalData.class
						.getResource("java-money/defaults/SupplementalData.xml"));
	}

	public static CLDRSupplementalData getInstance() {
		return INSTANCE;
	}

	private static CLDRSupplementalData createInstance() {
		try {
			return new CLDRSupplementalData();
		} catch (MalformedURLException e) {
			throw new IllegalStateException(
					"Error creating CLDR SupplementalData.", e);
		}
	}

	@Override
	protected void documentReloaded() {
		// load currencies
		Map<String, Currency4Region> currencyRegionData = new HashMap<String, Currency4Region>();

		NodeList nl = getDocument().getDocumentElement()
				.getElementsByTagName("currencyData");

		for (int i = 0; i < nl.getLength(); i++) {
			Node node = nl.item(i);
			// if ("fractions".equals(node.getNodeName())) {
			// NodeList infoNodes = node.getChildNodes();
			// for (int j = 0; j < infoNodes.getLength(); j++) {
			// CurrencyData cd = new CurrencyData(infoNodes.item(j));
			// currencyDataMap.put(cd.getCode(), cd);
			// }
			// }
			// else
			if ("region".equals(node.getNodeName())) {
				String regionCode = node.getAttributes()
						.getNamedItem("iso3166").getNodeValue();
				NodeList currencyNodes = node.getChildNodes();
				for (int j = 0; j < currencyNodes.getLength(); j++) {
					Currency4Region currencyEntry = new Currency4Region(
							currencyNodes.item(j));
					currencyRegionData.put(regionCode, currencyEntry);
				}
			}
		}
		this.currencyRegionData = currencyRegionData;
	}

	public Currency4Region getCurrencyData(String countryCode) {
		return this.currencyRegionData.get(countryCode);
	}

	public static final class Currency4Region implements Serializable {

		private String regionCode;
		private List<CurrencyRegionRecord> entries = new ArrayList<CurrencyRegionRecord>();

		public Currency4Region(Node item) {
			regionCode = item.getAttributes().getNamedItem("is03166")
					.getNodeValue();
			NodeList nl = item.getChildNodes();
			for (int i = 0; i < nl.getLength(); i++) {
				Node childNode = nl.item(i);
				entries.add(new CurrencyRegionRecord(regionCode, childNode));
			}
			// <region iso3166="AF">
			// <currency iso4217="AFN" from="2002-10-07"/>
			// <currency iso4217="AFA" from="1927-03-14" to="2002-12-31"/>
		}

		public String getRegionCode() {
			return regionCode;
		}

		public List<CurrencyRegionRecord> getEntries() {
			return Collections.unmodifiableList(this.entries);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "Currency4Region [regionCode=" + regionCode + ", entries="
					+ entries + "]";
		}

	}

	public static final class CurrencyRegionRecord implements Serializable {

		private String regionCode;
		private String currencyCode;
		private int[] fromYMD;
		private int[] toYMD;
		private boolean legalTender = true;

		public CurrencyRegionRecord(String regionCode, Node item) {
			this.regionCode = regionCode;
			NamedNodeMap attrs = item.getAttributes();
			this.currencyCode = attrs.getNamedItem("iso4217").getNodeValue();
			String from = attrs.getNamedItem("from").getNodeValue();
			String[] dtParts = from.split("-");
			this.fromYMD = new int[3];
			this.fromYMD[0] = Integer.parseInt(dtParts[0]);
			this.fromYMD[1] = Integer.parseInt(dtParts[1]);
			this.fromYMD[2] = Integer.parseInt(dtParts[2]);
			// <region iso3166="AF">
			// <currency iso4217="AFN" from="2002-10-07"/>
			// <currency iso4217="AFA" from="1927-03-14" to="2002-12-31"/>
			String to = null;
			if (attrs.getNamedItem("to") != null) {
				to = attrs.getNamedItem("to").getNodeValue();
				dtParts = to.split("-");
				this.toYMD = new int[3];
				this.toYMD[0] = Integer.parseInt(dtParts[0]);
				this.toYMD[1] = Integer.parseInt(dtParts[1]);
				this.toYMD[2] = Integer.parseInt(dtParts[2]);
			}
			if (attrs.getNamedItem("tender") != null) {
				this.legalTender = Boolean.parseBoolean(attrs.getNamedItem(
						"tender").getNodeValue());
			}
		}

		public String getRegionCode() {
			return regionCode;
		}

		/**
		 * @return the currencyCode
		 */
		public final String getCurrencyCode() {
			return currencyCode;
		}

		/**
		 * @return the fromYMD
		 */
		public final int[] getFromYMD() {
			return fromYMD;
		}

		/**
		 * @return the toYMD
		 */
		public final int[] getToYMD() {
			return toYMD;
		}

		public boolean isLegalTender() {
			return legalTender;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "CurrencyRegionRecord [regionCode=" + regionCode
					+ ", currencyCode=" + currencyCode + ", fromYMD="
					+ Arrays.toString(fromYMD) + ", toYMD="
					+ Arrays.toString(toYMD) + "]";
		}

	}

	public String getSource() {
		return "http://unicode.org/repos/cldr/trunk/common/supplemental/supplementalData.xml";
	}
}
