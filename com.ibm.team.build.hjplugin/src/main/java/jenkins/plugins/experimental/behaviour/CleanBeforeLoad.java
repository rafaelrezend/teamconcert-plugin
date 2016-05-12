package jenkins.plugins.experimental.behaviour;

import hudson.Extension;
import org.kohsuke.stapler.DataBoundConstructor;

public class CleanBeforeLoad extends RTCExtension {
    @DataBoundConstructor
    public CleanBeforeLoad() {
    }

    @Extension
    public static class DescriptorImpl extends RTCExtensionDescriptor {
        @Override
        public String getDisplayName() {
            return "Clean up workspace before load";
        }
    }
}
