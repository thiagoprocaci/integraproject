package com.integrareti.integraframework.service.google.sso;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import org.apache.commons.codec.binary.Base64;
import org.jdom.Document;

import com.integrareti.integraframework.exceptions.SamlException;
import com.integrareti.integraframework.util.SsoUtil;
import com.integrareti.integraframework.util.StringUtil;
import com.integrareti.integraframework.util.XmlDigitalSigner;

/**
 * Class that builds a google response
 * 
 * @author Thiago Baesso Procaci
 * 
 */
public class ProcessResponseGoogle {
	// private constants
	private static final ProcessResponseGoogle INSTANCE = new ProcessResponseGoogle();
	private static final String SAML_RESPONSE_TEMPLATE_FILE = "SamlResponseTemplate.xml";
	private static final String PRIVATE_KEY_FILE = "/com/integrareti/integraframework/service/google/sso/keys/DSAPrivateKey.key";
	private static final String PUBLIC_KEY_FILE = "/com/integrareti/integraframework/service/google/sso/keys/DSAPublicKey.key";

	/**
	 * Empty constructor
	 */
	private ProcessResponseGoogle() {
	}

	/**
	 * 
	 * @return Returns an instance of ProcessResponseGoogle
	 */
	public static ProcessResponseGoogle getInstance() {
		return INSTANCE;
	}

	/**
	 * 
	 * @throws SamlException
	 * @return Returns a new samlResponse
	 */
	public String getSAMLResponse(String SAMLReqst, String username) throws SamlException {
		String SAMLRequest = SAMLReqst;
		String samlResponse = null;
		boolean continueLogin = true;
		if (SAMLRequest == null || SAMLRequest.equals("null")) {
			continueLogin = false;
			throw new SamlException("ERROR: Unspecified SAML parameters.");
		} else {
			// Parse the SAML request and extract the ACS URL and provider
			// name
			// Extract the Assertion Consumer Service URL from AuthnRequest
			String requestXmlString = decodeAuthnRequestXML(SAMLRequest);
			String[] samlRequestAttributes = getRequestAttributes(requestXmlString);
			String issueInstant = samlRequestAttributes[0];
			@SuppressWarnings("unused")
			String providerName = samlRequestAttributes[1];
			String acsURL = samlRequestAttributes[2];
			String requestId = samlRequestAttributes[3];
			if (username == null) {
				throw new SamlException("Login Failed: Invalid user.");
			} else {
				String public_key_file_path = "";
				try {
					public_key_file_path = "/" + URLDecoder.decode(getClass().getResource(PUBLIC_KEY_FILE).toString(), "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				public_key_file_path = StringUtil.replaceSubstring(public_key_file_path, "file:/", "");
				DSAPublicKey publicKey = (DSAPublicKey) SsoUtil.getPublicKey(public_key_file_path, "DSA");
				String private_key_file_path = "";
				try {
					private_key_file_path = "/" + URLDecoder.decode(getClass().getResource(PRIVATE_KEY_FILE).toString(), "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				private_key_file_path = StringUtil.replaceSubstring(private_key_file_path, "file:/", "");
				DSAPrivateKey privateKey = (DSAPrivateKey) SsoUtil.getPrivateKey(private_key_file_path, "DSA");
				// Check for valid parameter values for SAML response
				// First, verify that the NotBefore and NotOnOrAfter values
				// are valid
				String notBefore = "2003-04-17T00:46:02Z";
				String notOnOrAfter = "2020-04-17T00:51:02Z";
				if (!validSamlDateFormat(issueInstant)) {
					continueLogin = false;
					throw new SamlException("ERROR: Invalid NotBefore date specified - " + notBefore);
				} else if (!validSamlDateFormat(notOnOrAfter)) {
					continueLogin = false;
					throw new SamlException("ERROR: Invalid NotOnOrAfter date specified - " + notOnOrAfter);
				}
				// Sign XML containing user name with specified keys
				if (continueLogin) {
					// Generate SAML response contaning specified user name
					String responseXmlString = createSamlResponse(username, notBefore, notOnOrAfter, requestId, acsURL);
					// Sign the SAML response XML
					String signedSamlResponse = signResponse(responseXmlString, publicKey, privateKey);
					samlResponse = signedSamlResponse;
				}
			}
		}
		return samlResponse;
	}

	/**
	 * Retrieves the AuthnRequest from the encoded and compressed String
	 * extracted from the URL. The AuthnRequest XML is retrieved in the
	 * following order:
	 * <p>
	 * 1. URL decode <br>
	 * 2. Base64 decode <br>
	 * 3. Inflate <br>
	 * Returns the String format of the AuthnRequest XML.
	 */
	private String decodeAuthnRequestXML(String encodedRequestXmlString) throws SamlException {
		try {
			// URL decode
			encodedRequestXmlString = URLDecoder.decode(encodedRequestXmlString, "UTF-8");
			// Base64 decode
			Base64 base64Decoder = new Base64();
			byte[] xmlBytes = encodedRequestXmlString.getBytes("UTF-8");
			byte[] base64DecodedByteArray = base64Decoder.decode(xmlBytes);
			// Uncompress the AuthnRequest data
			// First attempt to unzip the byte array according to DEFLATE (rfc
			// 1951)
			try {
				Inflater inflater = new Inflater(true);
				inflater.setInput(base64DecodedByteArray);
				// since we are decompressing, it's impossible to know how much
				// space we
				// might need; hopefully this number is suitably big
				byte[] xmlMessageBytes = new byte[5000];
				int resultLength = inflater.inflate(xmlMessageBytes);
				if (!inflater.finished()) {
					throw new RuntimeException("didn't allocate enough space to hold " + "decompressed data");
				}
				inflater.end();
				return new String(xmlMessageBytes, 0, resultLength, "UTF-8");
			} catch (DataFormatException e) {
				// if DEFLATE fails, then attempt to unzip the byte array
				// according
				// to
				// zlib (rfc 1950)
				ByteArrayInputStream bais = new ByteArrayInputStream(base64DecodedByteArray);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				InflaterInputStream iis = new InflaterInputStream(bais);
				byte[] buf = new byte[1024];
				int count = iis.read(buf);
				while (count != -1) {
					baos.write(buf, 0, count);
					count = iis.read(buf);
				}
				iis.close();
				return new String(baos.toByteArray());
			}
		} catch (UnsupportedEncodingException e) {
			throw new SamlException("Error decoding AuthnRequest: " + "Check decoding scheme - " + e.getMessage());
		} catch (IOException e) {
			throw new SamlException("Error decoding AuthnRequest: " + "Check decoding scheme - " + e.getMessage());
		}
	}

	/**
	 * Creates a DOM document from the specified AuthnRequest xmlString and
	 * extracts the value under the "AssertionConsumerServiceURL" attribute
	 */
	private String[] getRequestAttributes(String xmlString) throws SamlException {
		Document doc = SsoUtil.createJdomDoc(xmlString);
		if (doc != null) {
			String[] samlRequestAttributes = new String[4];
			samlRequestAttributes[0] = doc.getRootElement().getAttributeValue("IssueInstant");
			samlRequestAttributes[1] = doc.getRootElement().getAttributeValue("ProviderName");
			samlRequestAttributes[2] = doc.getRootElement().getAttributeValue("AssertionConsumerServiceURL");
			samlRequestAttributes[3] = doc.getRootElement().getAttributeValue("ID");
			return samlRequestAttributes;
		} else {
			throw new SamlException("Error parsing AuthnRequest XML: Null document");
		}
	}

	/**
	 * Generates a SAML response XML by replacing the specified username on the
	 * SAML response template file. Returns the String format of the XML file.
	 */
	private String createSamlResponse(String authenticatedUser, String notBefore, String notOnOrAfter, String requestId, String acsURL) throws SamlException {
		String file_path = "";
		try {
			file_path = "/" + URLDecoder.decode(getClass().getResource("/com/integrareti/integraframework/service/google/sso/template/").toString() + SAML_RESPONSE_TEMPLATE_FILE, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		file_path = file_path.replace("file:/", "");
		String samlResponse = SsoUtil.readFileContents(file_path);
		samlResponse = samlResponse.replace("<USERNAME_STRING>", authenticatedUser);
		samlResponse = samlResponse.replace("<RESPONSE_ID>", SsoUtil.createID());
		samlResponse = samlResponse.replace("<ISSUE_INSTANT>", SsoUtil.getDateAndTime());
		samlResponse = samlResponse.replace("<AUTHN_INSTANT>", SsoUtil.getDateAndTime());
		samlResponse = samlResponse.replace("<NOT_BEFORE>", notBefore);
		samlResponse = samlResponse.replace("<NOT_ON_OR_AFTER>", notOnOrAfter);
		samlResponse = samlResponse.replace("<ASSERTION_ID>", SsoUtil.createID());
		samlResponse = samlResponse.replace("<REQUEST_ID>", requestId);
		samlResponse = samlResponse.replace("<ACS_URL>", acsURL);
		return samlResponse;
	}

	/**
	 * Signs the SAML response XML with the specified private key, and embeds
	 * with public key. Uses helper class XmlDigitalSigner to digitally sign the
	 * XML.
	 */
	private String signResponse(String response, DSAPublicKey publicKey, DSAPrivateKey privateKey) throws SamlException {
		return (XmlDigitalSigner.signXML(response, publicKey, privateKey));
	}

	/**
	 * Checks if the specified samlDate is formatted as per the SAML 2.0
	 * specifications, namely YYYY-MM-DDTHH:MM:SSZ.
	 */
	private boolean validSamlDateFormat(String samlDate) {
		if (samlDate == null) {
			return false;
		}
		int indexT = samlDate.indexOf("T");
		int indexZ = samlDate.indexOf("Z");
		if (indexT != 10 || indexZ != 19) {
			return false;
		}
		String dateString = samlDate.substring(0, indexT);
		String timeString = samlDate.substring(indexT + 1, indexZ);
		SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		ParsePosition pos = new ParsePosition(0);
		Date parsedDate = dayFormat.parse(dateString, pos);
		pos = new ParsePosition(0);
		Date parsedTime = timeFormat.parse(timeString, pos);
		if (parsedDate == null || parsedTime == null) {
			return false;
		}
		return true;
	}
}
