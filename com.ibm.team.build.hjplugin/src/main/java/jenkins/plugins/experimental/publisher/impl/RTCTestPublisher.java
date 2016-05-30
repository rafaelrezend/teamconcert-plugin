package jenkins.plugins.experimental.publisher.impl;

import jenkins.plugins.experimental.publisher.RTCPublisherExtension;
import jenkins.plugins.experimental.publisher.RTCPublisherExtensionDescriptor;
import hudson.Extension;

import org.kohsuke.stapler.DataBoundConstructor;

public class RTCTestPublisher extends RTCPublisherExtension {
	

	@DataBoundConstructor
	public RTCTestPublisher() {
	}
	

	@Extension
	public static class DescriptorImpl extends RTCPublisherExtensionDescriptor {
		
		/**
		 * This human readable name is used in the configuration screen.
		 */
		@Override
		public String getDisplayName() {
			return "JUnit test";
		}
	}

}