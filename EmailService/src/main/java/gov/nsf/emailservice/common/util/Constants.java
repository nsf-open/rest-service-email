package gov.nsf.emailservice.common.util;

public final class Constants {



    //Validation error strings
    public static final String ILLEGAL_CHARS_IN_CONTENT_BODY = "";
    public static final String ILLEGAL_CHARS_IN_SUBJECT = "Mail subject must not contain any illegal characters";
    public static final String MISSING_CONTENT_BODY = "Email content body must be specified";
    public static final String MISSING_TO_RECIPIENTS = "At least one TO recipient must be specified";
    public static final String MISSING_SENDER_ADDRESS = "Sender email address must be specified";
    public static final String MISSING_MAIL_SUBJECT = "Error sending letter - Missing required field(s): Mail subject must be specified";
    public static final String MISSING_ELTR_ID = "Letter ID must be specified";
    public static final String MISSING_ELTR_STATUS = "Letter Status must be specified";
    public static final String MISSING_ELTR_USER = "Letter User must be specified";
    public static final String NULL_CC_RECIPIENTS = "CC addresses list must not be null (but may be empty)";
    public static final String NULL_BCC_RECIPIENTS = "BCC addresses list must not be null (but may be empty)";
    public static final String ELTR_ID_NON_NUMERIC = "Letter ID has to be a valid integer";
    public static final String INVALID_FIELD = "Invalid/Missing field";
    public static final String MISSING_EMAIL_INFO = "Email information object must be populated";

    public static final String MISSING_NULL_FIELD = "The field cannot be missing or null: ";
    public static final String UNSUPPORTED_CHARACTERS_FIELD = "The field cannot contain unsupported characters: ";


    public static final String INVALID_ADDRESS_FORMAT = "The field cannot contain invalid email address format: ";


    public static final String NULL_TO_RECIPIENTS = "TO recipients must not be null";
    public static final String NULL_SENDER_ADDRESS = "Sender email address must not be null";
    public static final String NULL_MAIL_SUBJECT = "Error sending letter - Missing required field(s): Mail subject must not be null";


    public static final String ELTR_ID_FIELD = "eltrID";
    public static final String ELTR_CONTENT_FIELD = "eltrContent";
    public static final String TO_RECIPIENTS_FIELD = "emailInfo.toAddresses";
    public static final String SENDER_ADDRESS_FIELD = "emailInfo.fromAddress";
    public static final String MAIL_SUBJECT_FIELD = "emailInfo.mailSubject";
    public static final String EMAIL_INFO_FIELD = "emailInfo";
    public static final String ELTR_STATUS_FIELD = "eltrStatus";
    public static final String ELTR_USER_FIELD = "eltrStatusUser";
    public static final String CC_RECIPIENTS_FIELD = "emailInfo.ccAddresses";
    public static final String BCC_RECIPIENTS_FIELD = "emailInfo.bccAddresses";
    public static final String APPL_NAME_FIELD = "applName";

    // Exception messages
    public static final String SERVER_500_ERROR = "Server Error";
    public static final String SERVER_UNEXPECTED_ERROR = "Unexpected Error";
    public static final String SERVER_RESOURCE_NOT_FOUND = "Resource Not Found";
    public static final String DB_TRANSACTION_ERROR = "DB Transaction Error";
    public static final String INVALID_FORM_DATA = "Invalid/Missing Form Data";
    public static final String ACCESS_DENIED_EXCEPTION = "Access Denied";
    public static final String INVALID_REQUEST_PARAMETER = "Invalid/Missing request parameter";
    public static final String UNABLE_TO_READ_JSON = "Unable to read JSON";
    public static final String INCORRECT_FORMAT_PARAMETERS = "Incorrect format of parameters";

    public static final String INVALID_ELTR_STATUS = "Value for eltrStatus can only be \'Sent\' or \'Draft\'";
    public static final String ERROR_GETTING_LETTER = "Unable to retrieve letter - ";
    public static final String ERROR_SAVING_LETTER = "Unable to create letter - ";
    public static final String ERROR_UPDATING_LETTER = "Unable to update letter - ";

    public static final String ERROR_GETTING_SAVED_LETTER = "Could not retrieve saved letter";
    public static final String ERROR_GETTING_UPDATED_LETTER = "Could not retrieve updated or recently saved letter: ";
    public static final String ERROR_LETTER_ALREADY_SENT_UPDATE = "Cannot update letter with SENT status: ";
    public static final String ERROR_LETTER_ALREADY_SENT_DELETE = "Cannot delete letter with SENT status: ";

    public static final String ERROR_UPDATING_ELTR_ELTR_TABLE = "Error updating eltr_eltr table: ";
    public static final String ERROR_INSERTING_MAIL_RECIPIENT = "Error inserting mail recipient into the database: ";
    public static final String ERROR_DELETING_MAIL_RECIPIENTS = "Error deleting mail recipients for eltrID: ";


    public static final String ERROR_LETTER_DOES_NOT_EXIST = "Letter ID does not exist: ";

    public static final String ERROR_EXECUTING_QUERY = "Error executing statement: ";

    public static final String LETTER_REQUEST_FIELD = "letterRequest";
    //MEF-805 - Send Email without storing
    public static final String EMAIL_REQUEST_FIELD = "emailRequest";
    public static final String MISSING_EMAIL_REQUEST = "Error sending letter - Missing required field(s): Email request object";
    public static final String MAIL_SUBJECT_EMAIL_FIELD = "letter.emailInfo.mailSubject";
    public static final String MISSING_EMAIL_CONTENT_BODY = "Error sending letter - Missing required field(s): Email content";
    public static final String MISSING_EMAIL_TO_RECIPIENTS = "At least one TO recipient must be specified";
    public static final String MISSING_EMAIL_SENDER_ADDRESS = "Error sending letter - Missing required field(s): Sender Email Address";
    public static final String MISSING_EMAIL_SUBJECT = "Error sending letter - Missing required field(s): Mail subject";
    public static final String SENDER_EMAIL_ADDRESS_FIELD = " letter.emailInfo.fromAddress";
    public static final String CC_EMAIL_RECIPIENTS_FIELD = "letter.emailInfo.ccAddresses";
    public static final String BCC_EMAIL_RECIPIENTS_FIELD = "letter.emailInfo.bccAddresses";
    public static final String INVALID_ADDRESS_ERROR = "Error sending letter - Invalid e-mail address format.";




    public static final String META_DEBUG_EMAIL_RECIPIENTS_FIELD = "sendMetaData.debugRecipients";
    public static final String META_DEFAULT_BCC_EMAIL_RECIPIENTS_FIELD = "sendMetaData.defaultBccRecipients";
    public static final String META_PROD_SUPPORT_EMAIL_RECIPIENTS_FIELD = "sendMetaData.prodSupportRecipients";


    public static final String NULL_CC_EMAIL_RECIPIENTS = "CC addresses list must not be null (but may be empty)";
    public static final String NULL_BCC_EMAIL_RECIPIENTS = "BCC addresses list must not be null (but may be empty)";
    public static final String MISSING_EMAIL_BODY = "Error sending letter - Missing required field(s): Mail Body";
    public static final String MISSING_LETTER = "Letter must not be null/empty";


    public static final String MISSING_NULL_EMPTY_FIELD = "The field cannot be null, missing, or empty: ";

    public static final String MAIL_BODY_EMAIL_FIELD = "letter.eltrContent";
    public static final String TO_EMAIL_RECIPIENTS_FIELD = "letter.emailInfo.toAddresses";
    public static final String EMPTY_TO_EMAIL_RECIPIENTS = "Error sending letter - Missing required field(s): To Email Address";

    public static final String EMPTY_STRING_EMAIL_RECIPIENTS = "Error sending letter - Empty String submitted in recipient field(s)";


    // DB query statements and parameter constants

    public static final String LETTER_FIELD = "letter";

    public static final String TMPL_ID = "tmpl_id";
    public static final String ELTR_CONTENT = "ltr_cntn_txt";
    public static final String MAIL_SUBJECT = "ltr_subj";
    public static final String ELTR_STATUS = "ltr_stts_code";
    public static final String ELTR_ID = "ntfy_ltr_id";
    public static final String ELTR_USER = "ltr_stts_user_id";
    public static final String ELTR_DATE = "ltr_stts_date";
    public static final String PLAIN_TEXT = "plain_txt_fmt_flag";
    public static final String LAST_UPT_PGM = "last_updt_pgm";
    public static final String LAST_UPT_USER = "last_updt_user";
    public static final String LAST_UPT_TMSP = "last_updt_tmsp";
    public static final String RECIPIENT_TYPE_CODE = "ltr_recp_type_code";
    public static final String SEARCH_PARAMETER_KEY = "ntfy_ltr_atr_id";
    public static final String SEARCH_PARAMETER_VALUE = "atr_val";
    public static final String MAIL_ADDRESS = "emai_addr";
    public static final String CURRENT_DATE = "getdate()";
    public static final String EN_SVC = "EnSvc";
    public static final String MY_NSF = "MyNSF";
    public static final String FROM = "F";
    public static final String TO = "TO";
    public static final String CC = "CC";
    public static final String BCC = "BC";
    public static final String TEMPLATE_ID = "template_id";
    public static final String APPL_NAME = "appl_name";
    public static final String APPL_ID = "appl_id";
    public static final String APPL_ID_FIELD = "applID";


    public static final String GET_LETTER_BY_ID_QUERY = "select " +
            "ntfyLtr.ltr_cntn_txt, " +
            "ntfyLtr.ltr_stts_code, " +
            "ntfyLtr.ltr_stts_user_id, " +
            "ntfyLtr.ltr_stts_date, " +
            "ntfyLtr.tmpl_id, " +
            "ntfyLtr.plain_txt_fmt_flag," +
            "ntfyLtr.ntfy_ltr_id," +
            "ntfyLtr.appl_id," +
            "ntfyLtr.ltr_subj, " +
            "ltrRecp.ltr_recp_type_code," +
            "ltrRecp.emai_addr " +
            "from dbo.ntfy_ltr ntfyLtr, dbo.ntfy_ltr_recp ltrRecp " +
            "where ntfyLtr.ntfy_ltr_id=:" + ELTR_ID + " and " +
            "ntfyLtr.ntfy_ltr_id = ltrRecp.ntfy_ltr_id and " +
            "ltrRecp.ltr_recp_type_code in('BC','CC','F','TO')";

    public static final String GET_LETTER_BY_LAST_INSERTED_QUERY = "select " +
            "ntfyLtr.ltr_cntn_txt, " +
            "ntfyLtr.ltr_stts_code, " +
            "ntfyLtr.ltr_stts_user_id, " +
            "ntfyLtr.ltr_stts_date, " +
            "ntfyLtr.tmpl_id, " +
            "ntfyLtr.plain_txt_fmt_flag, " +
            "ntfyLtr.appl_id, " +
            "ntfyLtr.ntfy_ltr_id, " +
            "ntfyLtr.ltr_subj, " +
            "ltrRecp.ltr_recp_type_code, " +
            "ltrRecp.emai_addr " +
            "from dbo.ntfy_ltr ntfyLtr, dbo.ntfy_ltr_recp ltrRecp " +
            "where ntfyLtr.ntfy_ltr_id in(select max(ntfy_ltr_id) from dbo.ntfy_ltr) and " +
            "ntfyLtr.ntfy_ltr_id = ltrRecp.ntfy_ltr_id and " +
            "ltrRecp.ltr_recp_type_code in('BC','CC','F','TO')";


    public static final String UPDATE_LETTER_WITH_SAME_STATUS_QUERY = "update dbo.ntfy_ltr " +
            "set ltr_subj=:" + MAIL_SUBJECT + ", " +
            "ltr_cntn_txt=:" + ELTR_CONTENT + ", " +
            "last_updt_pgm= :" + LAST_UPT_PGM + ", " +
            "last_updt_user =:" + LAST_UPT_USER + ", " +
            "last_updt_tmsp = getdate() " +
            "where ntfy_ltr_id =:" + ELTR_ID;

    public static final String UPDATE_LETTER_WITH_NEW_STATUS_QUERY = "update dbo.ntfy_ltr " +
            "set ltr_subj=:" + MAIL_SUBJECT + ", " +
            "ltr_cntn_txt=:" + ELTR_CONTENT + ", " +
            "ltr_stts_code = :" + ELTR_STATUS + ", " +
            "ltr_stts_date = getdate(), " +
            "ltr_stts_user_id = :" + ELTR_USER + ", " +
            "last_updt_pgm= :" + LAST_UPT_PGM + ", " +
            "last_updt_user =:" + LAST_UPT_USER + ", " +
            "last_updt_tmsp = getdate() " +
            "where ntfy_ltr_id =:" + ELTR_ID;


    public static final String DELETE_MAIL_RECIPIENTS_QUERY = "delete from dbo.ntfy_ltr_recp " +
            "where ntfy_ltr_id = :" + ELTR_ID;

    public static final String INSERT_ELTR_QUERY = "INSERT INTO dbo.ntfy_ltr" +
            "( " +
            "ltr_cntn_txt, " +
            "ltr_subj, " +
            "ltr_stts_code, " +
            "ltr_stts_user_id, " +
            "ltr_stts_date, " +
            "tmpl_id, " +
            "appl_id, " +
            "last_updt_pgm, " +
            "last_updt_user, " +
            "last_updt_tmsp, " +
            "plain_txt_fmt_flag" +
            ") " + //Values
            "SELECT " +
            ":" + ELTR_CONTENT + ", " +
            ":" + MAIL_SUBJECT + ", " +
            ":" + ELTR_STATUS + ", " +
            ":" + ELTR_USER + ", " +
            "getdate(), " +
            ":" + TEMPLATE_ID + ", " +
            ":" + APPL_ID + ", " +
            ":" + LAST_UPT_PGM + ", " +
            ":" + LAST_UPT_USER + ", " +
            "getdate(), " +
            ":" + PLAIN_TEXT;





    public static final String INSERT_MAIL_RECIPIENT_QUERY_WITHOUT_ID = "INSERT INTO dbo.ntfy_ltr_recp" +
            "(" +
            "ntfy_ltr_id, " +
            "ltr_recp_type_code, " +
            "emai_addr, " +
            "last_updt_pgm, " +
            "last_updt_user, " +
            "last_updt_tmsp" +
            ") " + //Values
            "select max(ntfy_ltr_id), " +
            ":" + RECIPIENT_TYPE_CODE + ", " +
            ":" + MAIL_ADDRESS + ", " +
            ":" + LAST_UPT_PGM + ", " +
            ":" + LAST_UPT_USER + ", " +
            "getdate() " +
            "from dbo.ntfy_ltr";

    public static final String INSERT_MAIL_RECIPIENT_QUERY_WITH_ID = "INSERT INTO dbo.ntfy_ltr_recp" +
            "(" +
            "ntfy_ltr_id, " +
            "ltr_recp_type_code, " +
            "emai_addr, " +
            "last_updt_pgm, " +
            "last_updt_user, " +
            "last_updt_tmsp" +
            ") " +
            "VALUES(" +
            ":" + ELTR_ID + ", " +
            ":" + RECIPIENT_TYPE_CODE + ", " +
            ":" + MAIL_ADDRESS + ", " +
            ":" + LAST_UPT_PGM + ", " +
            ":" + LAST_UPT_USER + ", " +
            "getdate())";

    public static final String GET_SEARCH_PARAMETERS_QUERY = "select " +
            "a.ntfy_ltr_id, " +
            "a.atr_val, " +
            "b.atr_name, " +
            "a.last_updt_pgm, " +
            "a.last_updt_user, " +
            "a.last_updt_tmsp " +
            "from dbo.ntfy_ltr_addl_info a , dbo.ntfy_ltr_atr_lkup b " +
            "where a.ntfy_ltr_atr_id = b.ntfy_ltr_atr_id and " +
            "ntfy_ltr_id = :" + ELTR_ID;



    public static final String INSERT_SEARCH_PARAMETER_QUERY = "INSERT INTO dbo.ntfy_ltr_addl_info " +
            "(" +
            "ntfy_ltr_id, " +
            "ntfy_ltr_atr_id, " +
            "atr_val, " +
            "last_updt_pgm, " +
            "last_updt_user, " +
            "last_updt_tmsp" +
            ") SELECT " +
            ":" + ELTR_ID + ", " +
            "ntfy_ltr_atr_id , "  +
            ":" + SEARCH_PARAMETER_VALUE + ", " +
            ":" + LAST_UPT_PGM + ", " +
            ":" + LAST_UPT_USER + ", " +
            "getdate() FROM dbo.ntfy_ltr_atr_lkup where atr_name =:" + Constants.SEARCH_PARAMETER_KEY;

    public static final String SEARCH_PARAMETER_LOOKUP_QUERY = "select atr_name from dbo.ntfy_ltr_atr_lkup";


    public static final String DELETE_SEARCH_PARAMETERS_QUERY = "delete from dbo.ntfy_ltr_addl_info " +
            "where ntfy_ltr_id = :" + ELTR_ID;

    public static final String DELETE_ELTR_QUERY = "delete from dbo.ntfy_ltr " +
            "where ntfy_ltr_id = :" + ELTR_ID;;


    public static final String GET_LETTER_IDS_QUERY = "select a.ntfy_ltr_id from " +
            "dbo.ntfy_ltr_atr_lkup b, " +
            "dbo.ntfy_ltr_addl_info a " +
            "where " +
            "a.ntfy_ltr_atr_id = b.ntfy_ltr_atr_id and " +
            "b.atr_name=:" + Constants.ATR_NAME + " and " +
            "a.atr_val=:" + Constants.SEARCH_PARAMETER_VALUE;

    public static final String SEPARATOR = "-";
    public static final String YES = "Y";
    public static final String NO = "N";
    public static final String MAX_ID = "MAX_ID";

    public static final String LETTER_RESPONSE_WRAPPER = "letterResponseWrapper";

    public static final String BASE_RESPONSE_WRAPPER = "baseResponseWrapper";
    public static final String ERROR_JOINING_LETTER_WITH_TEMPLATE = "Could not join letter with template: ";
    public static final String SEARCH_PARAMETERS_FIELD = "searchParameters";
    public static final String MISSING_SEARCH_PARAMETERS = "Field cannot be null or empty: letter.searchParameters";
    public static final String INVALID_PARAMETER_KEY_OR_VALUE = "The field cannot contain a search parameter with a null or empty key or value";

    public static final String SEARCH_PARAMETER_LOOKUP_ID = "ntfy_ltr_addl_info_id";
    public static final String SEARCH_PARAMETER_LOOKUP_EFF_DATE = "eff_date";
    public static final String SEARCH_PARAMETER_LOOKUP_END_DATE = "end_date";


    public static final String ERROR_INSERTING_SEARCH_PARAMETER = "Error inserting search parameter: ";
    public static final String APPL_ID_DOES_NOT_EXIST = "Invalid value passed for " + Constants.APPL_ID_FIELD + ": ";
    public static final String ERROR_RETRIEVING_SEARCH_PARAMETERS = "Error retrieving search parameters for eltrID: ";
    public static final String SEARCH_PARAMETER_DOES_NOT_EXIST = "Invalid value passed for " + Constants.SEARCH_PARAMETER_KEY_FIELD + ": ";
    public static final String SEARCH_PARAMETER_KEY_FIELD = "searchParameter.key";

    public static final String ATR_NAME = "atr_name";
    public static final String INVALID_NUMBER_OF_SEARCH_PARAMETERS = "The field can only have one key-value pair";
    public static final String QUERY_PARAMETERS = "URL Query Parameters";

    public static final String ERROR_GETTING_LETTER_IDS = "Could not get letter IDs for search parameter name: ";
    public static final String ERROR_FINDING_LETTER = "Error finding letter: ";
    public static final String ERROR_DELETING_SEARCH_PARAMETERS = "Error deleting search parameters for eltrID: ";
    public static final String ERROR_DELETING_LETTER = "Unable to delete letter - ";
    public static final String ERROR_DELETING_ELTR = "Error deleting letter from DB with eltrID: ";

    public static final String TMPL_ID_FIELD = "templateID";
    public static final String ERROR_LOOKING_UP_TEMPLATE = "Network error occured when contacting the template service";

    public static final String APPL_ID_NON_NUMERIC = "Application ID has to be a valid interger";
    public static final String SEARCH_PARAMETER_RESPONSE_WRAPPER = "searchParameterResponseWrapper";
    public static final String ERROR_LOOKING_UP_PARAMETER_NAMES = "Error looking up search parameter names: ";
    public static final String ERROR_SENDING_STORED_LETTER = "Unable to send stored letter: ";
    public static final String ERROR_LETTER_ALREADY_SENT_SEND = "Cannot send a letter that has already been sent";
}
