package gov.nih.nci.evs.browser.properties;

import gov.nih.nci.evs.browser.utils.*;
import gov.nih.nci.evs.browser.webapp.*;

import java.util.*;

import org.apache.log4j.*;

public class AppProperties {
    private static final String PROPERTY_FILE = "NCITermFormPropertiesFile";
    private static final String BUILD_INFO = "NCITERMFORM_BUILD_INFO";
    private static final String DEBUG_ON = "DEBUG_ON";
    private static final String SEND_EMAIL = "SEND_EMAIL";
    private static final String MAIL_SMTP_SERVER = "MAIL_SMTP_SERVER";
    private static final String NCICB_CONTACT_URL = "NCICB_CONTACT_URL";
    private static final String VOCABULARY_PREFIX = "VOCABULARY_";
    private static final int VOCABULARY_MAX = 20;
    private static final String CADSR_EMAIL = "CADSR_EMAIL";
    private static final String CADSR_SOURCES = "CADSR_SOURCES";
    private static final String CADSR_TYPES = "CADSR_TYPES";
    private static final String CDISC_EMAIL = "CDISC_EMAIL";
    private static final String CDISC_REQUEST_TYPES = "CDISC_REQUEST_TYPES";
    private static final String CDISC_CODES = "CDISC_CODES";

    private static AppProperties _appProperties = null;
    private Logger _logger = Logger.getLogger(AppProperties.class);
    private HashMap<String, String> _configurableItemMap;
    private String _buildInfo = null;
    private ArrayList<VocabInfo> _vocabList = null;
    private ArrayList<VocabInfo> _vocabListCDISC = null;
    private String[] _cadsrEmail = null;
    private String[] _cadsrSourceList = null;
    private String[] _cadsrTypeList = null;
    private String[] _cdiscEmail = null;
    private String[] _cdiscRequestTypeList = null;
    private String[] _cdiscCodeList = null;

    private AppProperties() { // Singleton Pattern
        loadProperties();
    }

    public static AppProperties getInstance() {
        if (_appProperties == null)
            _appProperties = new AppProperties();
        return _appProperties;
    }

    private void loadProperties() {
        synchronized (AppProperties.class) {
            String propertyFile = System.getProperty(PROPERTY_FILE);
            _logger.info(StringUtils.SEPARATOR);
            _logger.info("AppProperties: " + propertyFile);

            PropertyFileParser parser = new PropertyFileParser(propertyFile);
            parser.run();
            _configurableItemMap = parser.getConfigurableItemMap();
        }
    }

    private String getProperty(String key) {
        String value = (String) _configurableItemMap.get(key);
        if (value == null)
            return null;
        if (value.compareToIgnoreCase("null") == 0)
            return null;
        return value;
    }

    public String getBuildInfo() {
        if (_buildInfo != null)
            return _buildInfo;
        try {
            _buildInfo = getProperty(AppProperties.BUILD_INFO);
            if (_buildInfo == null)
                _buildInfo = "null";
        } catch (Exception ex) {
            _buildInfo = ex.getMessage();
        }

        _logger.info("getBuildInfo returns " + _buildInfo);
        return _buildInfo;
    }

    public boolean isDebugOn() {
        return Boolean.parseBoolean(getProperty(DEBUG_ON));
    }

    public boolean isSendEmail() {
        //if (true) return true;
        return Boolean.parseBoolean(getProperty(SEND_EMAIL));
    }

    public String getContactUrl() {
        return getProperty(NCICB_CONTACT_URL);
    }

    public String[] getContactUsRecipients() {
        String value = getContactUrl();
        return StringUtils.toStrings(value, ";", false);
    }

    public String getMailSmtpServer() {
        return getProperty(MAIL_SMTP_SERVER);
    }
    
    private ArrayList<VocabInfo> parseVocabList() {
        ArrayList<VocabInfo> list = new ArrayList<VocabInfo>();
        for (int i=0; i<VOCABULARY_MAX; ++i) {
            String value = getProperty(VOCABULARY_PREFIX + i);
            VocabInfo vocab = VocabInfo.parse(value);
            if (vocab != null)
                list.add(vocab);
        }
        list.add(new VocabInfo("Other"));
        return list;
    }

    public ArrayList<VocabInfo> getVocabularies(Prop.Version version) {
        if (version == Prop.Version.CDISC)
            return getVocabulariesCDISC();
        return getVocabulariesDefault();
    }

    private ArrayList<VocabInfo> getVocabulariesDefault() {
        if (_vocabList == null) {
            _vocabList = parseVocabList();
            VocabInfo.debug(_vocabList);
        }
        return _vocabList;
    }

    private ArrayList<VocabInfo> getVocabulariesCDISC() {
        if (_vocabListCDISC == null) {
            _vocabListCDISC = parseVocabList();
            VocabInfo vocabInfo = new VocabInfo("CDISC Terminology");
            vocabInfo.setUrl("http://www.cancer.gov/cancertopics/terminologyresources/page6");
            vocabInfo.addEmails(getProperty(CDISC_EMAIL));
            _vocabListCDISC.add(0, vocabInfo);
            VocabInfo.debug(_vocabListCDISC);
        }
        return _vocabListCDISC;
    }

    public String[] getVocabularyNames(Prop.Version version) {
        ArrayList<VocabInfo> list = getVocabularies(version);
        Iterator<VocabInfo> iterator = list.iterator();
        ArrayList<String> names = new ArrayList<String>();
        while (iterator.hasNext()) {
            VocabInfo info = iterator.next();
            names.add(info.getName());
        }
        return names.toArray(new String[names.size()]);
    }

    public String getVocabularyName(Prop.Version version, String url) {
        ArrayList<VocabInfo> list = getVocabularies(version);
        Iterator<VocabInfo> iterator = list.iterator();
        while (iterator.hasNext()) {
            VocabInfo info = iterator.next();
            if (info.getUrl().equals(url))
                return info.getDisplayName();
        }
        return null;
    }

    public String[] getVocabularyEmails(Prop.Version version, 
        String vocabularyName) {
        ArrayList<VocabInfo> list = getVocabularies(version);
        Iterator<VocabInfo> iterator = list.iterator();
        while (iterator.hasNext()) {
            VocabInfo info = iterator.next();
            if (info.getName().equals(vocabularyName)) {
                ArrayList<String> emails = info.getEmails();
                return emails.toArray(new String[emails.size()]);
            }
        }
        return new String[0];
    }
    
    public String[] getList(String propertyName, String[] list, String debugText) {
        if (list != null)
            return list;
        String value = getProperty(propertyName);
        list = StringUtils.toStrings(value, ";", false);
        if (debugText != null)
            ListUtils.debug(_logger, debugText, list);
        return list;
    }

    public String getSources() {
        return getProperty(CADSR_SOURCES);
    }
    
    public String[] getCADSREmail() {
        if (_cadsrEmail != null)
            return _cadsrEmail;
        
        String value = getProperty(CADSR_EMAIL);
        String[] list = StringUtils.toStrings(value, ";", false);
        return _cadsrEmail = list;
    }

    public String[] getCADSRSourceList() {
        return _cadsrSourceList = getList(
            CADSR_SOURCES, _cadsrSourceList, "_cadsrSourceList");
    }
    
    public String getCADSRTypes() {
        return getProperty(CADSR_TYPES);
    }

    public String[] getCADSRTypeList() {
        return _cadsrTypeList = getList(
            CADSR_TYPES, _cadsrTypeList, "_cadsrTypeList");
    }
    
    public String[] getCDISCEmail() {
        if (_cdiscEmail != null)
            return _cdiscEmail;
        
        String value = getProperty(CDISC_EMAIL);
        String[] list = StringUtils.toStrings(value, ";", false);
        return _cdiscEmail = list;
    }

    public String getCDISCRequestTypes() {
        return getProperty(CDISC_REQUEST_TYPES);
    }

    public String[] getCDISCRequestTypeList() {
        return _cdiscRequestTypeList = getList(
            CDISC_REQUEST_TYPES, _cdiscRequestTypeList, "_cdiscRequestTypeList");
    }
    
    public String getCDISCCodes() {
        return getProperty(CDISC_CODES);
    }

    public String[] getCDISCCodeList() {
        return _cdiscCodeList = getList(
            CDISC_CODES, _cdiscCodeList, "_cdiscCodeList");
    }
}
