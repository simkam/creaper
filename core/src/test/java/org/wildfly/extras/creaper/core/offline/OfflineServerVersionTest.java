package org.wildfly.extras.creaper.core.offline;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.wildfly.extras.creaper.core.ServerVersion;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class OfflineServerVersionTest {
    private static final String STANDALONE_XML = ""
            + "<server xmlns=\"urn:jboss:domain:%ROOT_VERSION%\">\n"
            + "    <profile>\n"
            + "        <subsystem xmlns=\"urn:jboss:domain:logging:%LOGGING_VERSION%\">\n"
            + "        <subsystem xmlns=\"urn:jboss:domain:ee:%EE_VERSION%\"/>\n"
            + "    </profile>\n"
            + "</server>";

    private static final String HOST_XML = ""
            + "<host name=\"master\" xmlns=\"urn:jboss:domain:%ROOT_VERSION%\">\n"
            + "</host>\n";

    private static final String DOMAIN_XML = ""
            + "<domain xmlns=\"urn:jboss:domain:%ROOT_VERSION%\">\n"
            + "    <profiles>\n"
            + "        <profile name=\"default\">\n"
            + "            <subsystem xmlns=\"urn:jboss:domain:logging:%LOGGING_VERSION%\"/>\n"
            + "            <subsystem xmlns=\"urn:jboss:domain:ee:%EE_VERSION%\"/>\n"
            + "        </profile>\n"
            + "        <profile name=\"ha\">\n"
            + "            <subsystem xmlns=\"urn:jboss:domain:logging:%LOGGING_VERSION%\"/>\n"
            + "            <subsystem xmlns=\"urn:jboss:domain:ee:%EE_VERSION%\"/>\n"
            + "        </profile>\n"
            + "    </profiles>\n"
            + "</domain>";

    private static final String EAP6_ROOT = "1.7";
    private static final String EAP6_LOGGING = "1.5";
    private static final String EAP6_EE = "1.2";

    private static final String EAP70_ROOT = "4.0";
    private static final String EAP71_ROOT = "5.0";
    private static final String EAP7_LOGGING = "3.0";
    private static final String EAP7_EE = "4.0";

    private static final String WFLY12_ROOT = "6.0";
    private static final String WFLY13_ROOT = "7.0";
    private static final String WFLY14_ROOT = "8.0";

    @Rule
    public final TemporaryFolder tmp = new TemporaryFolder();

    @Test
    public void as70x() throws IOException {
        test(ServerVersion.VERSION_0_0_0, STANDALONE_XML, "1.0", EAP6_LOGGING, EAP6_EE);
    }

    @Test
    public void as710() throws IOException {
        test(ServerVersion.VERSION_1_0_0, STANDALONE_XML, "1.1", EAP6_LOGGING, EAP6_EE);
    }

    @Test
    public void as711() throws IOException {
        test(ServerVersion.VERSION_1_1_0, STANDALONE_XML, "1.2", EAP6_LOGGING, EAP6_EE);
    }

    @Test
    public void eap60x() throws IOException {
        test(ServerVersion.VERSION_1_2_0, STANDALONE_XML, "1.3", EAP6_LOGGING, EAP6_EE);
    }

    @Test
    public void eap61x() throws IOException {
        test(ServerVersion.VERSION_1_4_0, STANDALONE_XML, "1.4", EAP6_LOGGING, EAP6_EE);
    }

    @Test
    public void discoverStandaloneXml_eap6() throws IOException {
        test(ServerVersion.VERSION_1_7_0, STANDALONE_XML, EAP6_ROOT, EAP6_LOGGING, EAP6_EE);
    }

    @Test
    public void discoverStandaloneXml_eap70() throws IOException {
        test(ServerVersion.VERSION_4_0_0, STANDALONE_XML, EAP70_ROOT, EAP7_LOGGING, EAP7_EE);
    }

    @Test
    public void discoverStandaloneXml_eap71() throws IOException {
        test(ServerVersion.VERSION_5_0_0, STANDALONE_XML, EAP71_ROOT, EAP7_LOGGING, EAP7_EE);
    }

    @Test
    public void discoverStandaloneXml_wfly12() throws IOException {
        test(ServerVersion.VERSION_6_0_0, STANDALONE_XML, WFLY12_ROOT, EAP7_LOGGING, EAP7_EE);
    }

    @Test
    public void discoverStandaloneXml_wfly13() throws IOException {
        test(ServerVersion.VERSION_7_0_0, STANDALONE_XML, WFLY13_ROOT, EAP7_LOGGING, EAP7_EE);
    }

    @Test
    public void discoverStandaloneXml_wfly14() throws IOException {
        test(ServerVersion.VERSION_8_0_0, STANDALONE_XML, WFLY14_ROOT, EAP7_LOGGING, EAP7_EE);
    }

    @Test
    public void discoverHostXml_eap6() throws IOException {
        test(ServerVersion.VERSION_1_7_0, HOST_XML, EAP6_ROOT, EAP6_LOGGING, EAP6_EE);
    }

    @Test
    public void discoverHostXml_eap70() throws IOException {
        test(ServerVersion.VERSION_4_0_0, HOST_XML, EAP70_ROOT, EAP7_LOGGING, EAP7_EE);
    }

    @Test
    public void discoverHostXml_eap71() throws IOException {
        test(ServerVersion.VERSION_5_0_0, HOST_XML, EAP71_ROOT, EAP7_LOGGING, EAP7_EE);
    }

    @Test
    public void discoverHostXml_wfly12() throws IOException {
        test(ServerVersion.VERSION_6_0_0, HOST_XML, WFLY12_ROOT, EAP7_LOGGING, EAP7_EE);
    }

    @Test
    public void discoverHostXml_wfly13() throws IOException {
        test(ServerVersion.VERSION_7_0_0, HOST_XML, WFLY13_ROOT, EAP7_LOGGING, EAP7_EE);
    }

    @Test
    public void discoverHostXml_wfly14() throws IOException {
        test(ServerVersion.VERSION_8_0_0, HOST_XML, WFLY14_ROOT, EAP7_LOGGING, EAP7_EE);
    }

    @Test
    public void discoverDomainXml_eap6() throws IOException {
        test(ServerVersion.VERSION_1_7_0, DOMAIN_XML, EAP6_ROOT, EAP6_LOGGING, EAP6_EE);
    }

    @Test
    public void discoverDomainXml_eap70() throws IOException {
        test(ServerVersion.VERSION_4_0_0, DOMAIN_XML, EAP70_ROOT, EAP7_LOGGING, EAP7_EE);
    }

    @Test
    public void discoverDomainXml_eap71() throws IOException {
        test(ServerVersion.VERSION_5_0_0, DOMAIN_XML, EAP71_ROOT, EAP7_LOGGING, EAP7_EE);
    }

    @Test
    public void discoverDomainXml_wfly12() throws IOException {
        test(ServerVersion.VERSION_6_0_0, DOMAIN_XML, WFLY12_ROOT, EAP7_LOGGING, EAP7_EE);
    }

    @Test
    public void discoverDomainXml_wfly13() throws IOException {
        test(ServerVersion.VERSION_7_0_0, DOMAIN_XML, WFLY13_ROOT, EAP7_LOGGING, EAP7_EE);
    }

    @Test
    public void discoverDomainXml_wfly14() throws IOException {
        test(ServerVersion.VERSION_8_0_0, DOMAIN_XML, WFLY14_ROOT, EAP7_LOGGING, EAP7_EE);
    }

    private void test(ServerVersion expected, String xmlPattern,
                      String rootVersion, String loggingVersion, String eeVersion) throws IOException {
        String xml = xmlPattern
                .replace("%ROOT_VERSION%", rootVersion)
                .replace("%LOGGING_VERSION%", loggingVersion)
                .replace("%EE_VERSION%", eeVersion);

        File configurationFile = tmp.newFile("test.xml");
        Files.write(xml, configurationFile, Charsets.UTF_8);

        ServerVersion actual = OfflineServerVersion.discover(configurationFile);
        assertEquals(expected, actual);
    }
}
