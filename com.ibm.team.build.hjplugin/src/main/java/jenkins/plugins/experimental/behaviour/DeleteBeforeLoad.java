package jenkins.plugins.experimental.behaviour;

import hudson.Extension;
import org.kohsuke.stapler.DataBoundConstructor;

public class DeleteBeforeLoad extends RTCExtension {
    @DataBoundConstructor
    public DeleteBeforeLoad() {
    }

    @Extension
    public static class DescriptorImpl extends RTCExtensionDescriptor {
        @Override
        public String getDisplayName() {
            return "Delete workspace before load";
        }
    }
}
