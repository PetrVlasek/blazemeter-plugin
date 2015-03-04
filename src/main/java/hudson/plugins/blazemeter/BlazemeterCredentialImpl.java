package hudson.plugins.blazemeter;

import com.cloudbees.plugins.credentials.CredentialsDescriptor;
import com.cloudbees.plugins.credentials.CredentialsScope;
import hudson.Extension;
import hudson.plugins.blazemeter.api.APIFactory;
import hudson.plugins.blazemeter.api.BlazemeterApi;
import hudson.plugins.blazemeter.utils.Constants;
import hudson.plugins.blazemeter.utils.BzmServiceManager;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;
import org.eclipse.jetty.util.log.AbstractLogger;
import org.eclipse.jetty.util.log.JavaUtilLog;
import org.json.JSONException;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import java.io.IOException;

/**
 * @author Vivek Pandey
 */
public class BlazemeterCredentialImpl extends AbstractBlazemeterCredential {

    /**
     * Ensure consistent serialization.
     */
    private static final long serialVersionUID = 1L;
    private static AbstractLogger jenCommonLog =new JavaUtilLog(Constants.BZM_JEN);

    //private final Secret apiKey;
    private final String apiKey;
    private final String description;

    @DataBoundConstructor
    public BlazemeterCredentialImpl(String apiKey, String description) {
        this.apiKey = apiKey;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }


    public String getApiKey() {
        return apiKey;
    }

    @Extension
    public static class DescriptorImpl extends CredentialsDescriptor {

        /**
         * {@inheritDoc}
         */
        @Override
        public String getDisplayName() {
            return Messages.BlazemeterCredential_DisplayName();
        }

        @Override
        public ListBoxModel doFillScopeItems() {
            ListBoxModel m = new ListBoxModel();
            m.add(CredentialsScope.GLOBAL.getDisplayName(), CredentialsScope.GLOBAL.toString());
            return m;
        }

        // Used by global.jelly to authenticate User key
        public FormValidation doTestConnection(@QueryParameter("apiKey") final String userKey) throws MessagingException, IOException, JSONException, ServletException {
            String apiVersion= BzmServiceManager.autoDetectApiVersion(userKey, jenCommonLog);
            BlazemeterApi bzm = APIFactory.getApiFactory().getAPI(userKey, APIFactory.ApiVersion.valueOf(apiVersion));
            int testCount = bzm.getTestCount();
            if (testCount < 0) {
                return FormValidation.warningWithMarkup("Error while checking test list on server. Check that BlazeMeterUrl is correct");
            } else if (testCount == 0) {
                return FormValidation.warningWithMarkup("User Key Invalid Or No Available Tests");
            } else {
                return FormValidation.ok("User Key Valid. " + testCount + " Available Tests");

            }
        }

    }
}
