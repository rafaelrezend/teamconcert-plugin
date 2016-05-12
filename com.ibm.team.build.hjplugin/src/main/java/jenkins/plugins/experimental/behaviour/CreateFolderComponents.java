package jenkins.plugins.experimental.behaviour;

import hudson.Extension;
import org.kohsuke.stapler.DataBoundConstructor;

public class CreateFolderComponents extends RTCExtension {
    @DataBoundConstructor
    public CreateFolderComponents() {
    }

    @Extension
    public static class DescriptorImpl extends RTCExtensionDescriptor {
        @Override
        public String getDisplayName() {
            return "Create folder for Components";
        }
    }
}
