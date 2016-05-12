package jenkins.plugins.experimental.behaviour;

import hudson.Extension;
import org.kohsuke.stapler.DataBoundConstructor;

public class MyCustomLoadRuleGen extends RTCExtension {
    @DataBoundConstructor
    public MyCustomLoadRuleGen() {
    }

    @Extension
    public static class DescriptorImpl extends RTCExtensionDescriptor {
        @Override
        public String getDisplayName() {
            return "Run MBB Load Rule Generator";
        }
    }
}
