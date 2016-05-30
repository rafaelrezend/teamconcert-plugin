package jenkins.plugins.teamant.build.tasks;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.BuildListener;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import java.io.IOException;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * A buildstep wrapping any number of other buildsteps, controlling their
 * execution based on a defined condition.
 *
 */
public class RTCStartTeamBuild extends Builder {

	/**
	 * @param label
	 *            RTC Build Result Activity label.
	 */
	@DataBoundConstructor
	public RTCStartTeamBuild(String resultUUIDProperty,
			String buildDefinitionId, String engineId) {
	}

	@Override
	public boolean perform(final AbstractBuild<?, ?> build,
			final Launcher launcher, final BuildListener listener)
			throws InterruptedException, IOException {

		return true;
	}

	@Override
	public DescriptorImpl getDescriptor() {
		return (DescriptorImpl) super.getDescriptor();
	}

	@Extension
	public static final class DescriptorImpl extends
			BuildStepDescriptor<Builder> {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isApplicable(
				@SuppressWarnings("rawtypes") Class<? extends AbstractProject> aClass) {
			// indicates that this builder can be used with all kinds of project
			// types
			return false;
		}

		/**
		 * This human readable name is used in the configuration screen.
		 */
		@Override
		public String getDisplayName() {
			return "RTC Start Team Build";
		}

	}
}
