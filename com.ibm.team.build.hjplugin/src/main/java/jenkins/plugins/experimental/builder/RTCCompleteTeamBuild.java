package jenkins.plugins.experimental.builder;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.BuildListener;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;
import java.io.IOException;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * A buildstep wrapping any number of other buildsteps, controlling their
 * execution based on a defined condition.
 * 
 * Equivalent to the completeTeamBuild Ant Task
 *
 */
public class RTCCompleteTeamBuild extends Recorder {

	/**
	 * @param label
	 *            RTC Build Result Activity label.
	 */
	@DataBoundConstructor
	public RTCCompleteTeamBuild(String buildResultUUID) {
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
			BuildStepDescriptor<Publisher> {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isApplicable(
				@SuppressWarnings("rawtypes") Class<? extends AbstractProject> aClass) {
			// indicates that this builder can be used with all kinds of project
			// types
			return true;
		}

		/**
		 * This human readable name is used in the configuration screen.
		 */
		@Override
		public String getDisplayName() {
			return "Complete RTC Build Result";
		}

	}

	/**
	 * {@inheritDoc}
	 */
	public BuildStepMonitor getRequiredMonitorService() {
		// TODO Auto-generated method stub
		return BuildStepMonitor.NONE;
	}
}
