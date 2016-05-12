package jenkins.plugins.experimental.behaviour;

import hudson.Extension;
import org.kohsuke.stapler.DataBoundConstructor;

public class RenameSnapshot extends RTCExtension {
    @DataBoundConstructor
    public RenameSnapshot() {
    }

    @Extension
    public static class DescriptorImpl extends RTCExtensionDescriptor {
        @Override
        public String getDisplayName() {
            return "Rename Snapshot";
        }
    }
}
