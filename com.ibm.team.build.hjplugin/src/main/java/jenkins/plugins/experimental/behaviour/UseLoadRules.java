package jenkins.plugins.experimental.behaviour;

import hudson.Extension;
import org.kohsuke.stapler.DataBoundConstructor;

public class UseLoadRules extends RTCExtension {
    @DataBoundConstructor
    public UseLoadRules() {
    }

    @Extension
    public static class DescriptorImpl extends RTCExtensionDescriptor {
        @Override
        public String getDisplayName() {
            return "Load using Load Rules";
        }
    }
}
