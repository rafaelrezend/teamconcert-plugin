package jenkins.plugins.experimental.publisher;

import hudson.Extension;
import org.kohsuke.stapler.DataBoundConstructor;

public class RTCArtifactPublisher extends RTCPublisherExtension {
	

	@DataBoundConstructor
	public RTCArtifactPublisher() {
	}
	

	@Extension
	public static class DescriptorImpl extends RTCPublisherExtensionDescriptor {
		
		/**
		 * This human readable name is used in the configuration screen.
		 */
		@Override
		public String getDisplayName() {
			return "Artifact";
		}
	}

}
