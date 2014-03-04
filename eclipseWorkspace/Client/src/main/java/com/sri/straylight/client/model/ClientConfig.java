package com.sri.straylight.client.model;

import java.awt.Color;
import java.net.URL;
import java.util.Locale;

import com.sri.straylight.client.ConnectTo;

// TODO: Auto-generated Javadoc
/**
 * The Class ClientConfig.
 */
public class ClientConfig {

	/** The auto connect flag. */
	public boolean autoConnectFlag;

	/** The auto parse xml flag. */
	public boolean autoParseXMLFlag;

	/** The auto init flag. */
	public boolean autoInitFlag;

	/** The connect to. */
	public ConnectTo connectTo = ConnectTo.connectTo_file;

	/** The window icon url. */
	public URL windowIconUrl;

	public static final Locale DEFAULT_LOCALE = Locale.US;

	/** The Constant BACKGROUND_VERIFICATION_OK. */
	public static final Color BACKGROUND_VERIFICATION_OK = Color.WHITE;

	/** The Constant BACKGROUND_VERIFICATION_ERROR. */
	public static final Color BACKGROUND_VERIFICATION_ERROR = Color.ORANGE;

}
