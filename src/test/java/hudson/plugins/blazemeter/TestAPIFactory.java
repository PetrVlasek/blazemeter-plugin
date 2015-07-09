package hudson.plugins.blazemeter;

import hudson.plugins.blazemeter.api.APIFactory;
import hudson.plugins.blazemeter.api.BlazemeterApi;
import hudson.plugins.blazemeter.api.BlazemeterApiV2Impl;
import hudson.plugins.blazemeter.api.BlazemeterApiV3Impl;
import hudson.plugins.blazemeter.utils.Constants;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * Created by dzmitrykashlach on 12/01/15.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestAPIFactory {
    private BlazemeterApi blazemeterApi = null;
    private String userKey1 = "1234567890";
    private String userKey2 = "0987654321";


    @Test
    public void testMixVersion() {
        blazemeterApi = APIFactory.getAPI(userKey1, ApiVersion.v3, Constants.DEFAULT_BLAZEMETER_URL);
        Assert.assertTrue(blazemeterApi instanceof BlazemeterApiV3Impl);
        blazemeterApi = APIFactory.getAPI(userKey1, ApiVersion.v2, Constants.DEFAULT_BLAZEMETER_URL);
        Assert.assertTrue(blazemeterApi instanceof BlazemeterApiV2Impl);
        blazemeterApi = APIFactory.getAPI(userKey1, ApiVersion.v3, Constants.DEFAULT_BLAZEMETER_URL);
        Assert.assertTrue(blazemeterApi instanceof BlazemeterApiV3Impl);

    }
}