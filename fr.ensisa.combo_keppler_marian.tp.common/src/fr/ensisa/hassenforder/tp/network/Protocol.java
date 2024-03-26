package fr.ensisa.hassenforder.tp.network;

public class Protocol {

    public static final int TP_TCP_PORT				= 7777;

    private static final int REQUEST_START				= 1000;
    private static final int REPLY_START				= 2000;

	public static final int REQUEST_CONNECT 			= REQUEST_START+11;
	public static final int REQUEST_CREDENTIAL 			= REQUEST_START+12;
	public static final int REQUEST_CREATE_USER 		= REQUEST_START+13;
	public static final int REQUEST_UPDATE_USER 		= REQUEST_START+14;
	public static final int REQUEST_GET_ALL_TEXTS		= REQUEST_START+15;
	public static final int REQUEST_GET_TEXT			= REQUEST_START+16;
	public static final int REQUEST_PUT_TEXT_CONTENT	= REQUEST_START+17;
	public static final int REQUEST_DELETE_TEXT			= REQUEST_START+18;
	public static final int REQUEST_NEW_TEXT			= REQUEST_START+19;
	public static final int REQUEST_GET_ALL_PARTICIPANTS= REQUEST_START+20;
	public static final int REQUEST_GET_ALL_USERS		= REQUEST_START+21;
	public static final int REQUEST_GET_ALL_OPERATIONS 	= REQUEST_START+22;
	public static final int REQUEST_PUT_ALL_PARTICIPANTS= REQUEST_START+23;
	public static final int REQUEST_PUT_ALL_OPERATIONS 	= REQUEST_START+24;

	public static final int REPLY_NOWAY					= REPLY_START+1;
    public static final int REPLY_KO					= REPLY_START+2;
    public static final int REPLY_OK					= REPLY_START+3;

	public static final int REPLY_USER 					= REPLY_START+11;
	public static final int REPLY_CREDENTIAL			= REPLY_START+12;
	public static final int REPLY_ALL_TEXTS				= REPLY_START+15;
	public static final int REPLY_TEXT					= REPLY_START+16;
	public static final int REPLY_ALL_PARTICIPANTS 		= REPLY_START+20;
	public static final int REPLY_ALL_USERS 			= REPLY_START+21;
	public static final int REPLY_ALL_OPERATIONS		= REPLY_START+22;








}
