package gov.nsf.emailservice.common.util;

import gov.nsf.emailservice.api.model.LetterStatus;

/**
 * Constants used in test package
 */
public class TestConstants {

    public static final String TEST_ELTR_ID = "01";
    public static final String TEST_ELTR_CONTENT = "<HTML><body>Test Content</body></HTML>";
    public static final LetterStatus TEST_ELTR_STATUS = LetterStatus.Draft;
    public static final String TEST_ELTR_USER = "EmailSvc";
    public static final String TEST_FROM_ADDRESS = "test@nsf.gov";
    public static final String TEST_MAIL_PRIORITY = "H";
    public static final String TEST_MAIL_SUBJECT = "Test subject";

    public static final boolean TRUE = true;
    public static final boolean FALSE = true;


    public static final String LETTER_ENDPOINT = "/letter";

    public static final String TEST_TMPL_ID = "24";
}
