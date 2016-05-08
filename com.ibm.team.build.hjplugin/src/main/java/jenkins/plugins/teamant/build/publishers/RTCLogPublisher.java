package jenkins.plugins.teamant.build.publishers;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.TaskListener;
import hudson.model.AbstractProject;
import hudson.model.Run;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;

import java.io.IOException;
import jenkins.tasks.SimpleBuildStep;

import org.kohsuke.stapler.DataBoundConstructor;

/**
 * A buildstep wrapping any number of other buildsteps, controlling their
 * execution based on a defined condition.
 *
 */
public class RTCLogPublisher extends Recorder implements SimpleBuildStep {
	
	private String label;
	private String logFile;

	/**
	 * @param label
	 *            RTC Build Result Activity label.
	 * @param enclosedSteps
	 *            List of enclosed build steps.
	 */
	@DataBoundConstructor
	public RTCLogPublisher(String label, String logFile) {
		this.label = label;
		this.logFile = logFile;
	}
	

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}


	/**
	 * @return the logFile
	 */
	public String getLogFile() {
		return logFile;
	}


	@Override
	public void perform(Run<?, ?> run, FilePath workspace, Launcher launcher,
			TaskListener listener) throws InterruptedException, IOException {
		
		// TODO TEST
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
			return "Publish log to RTC Build Result";
		}
	}

	@Override
	public BuildStepMonitor getRequiredMonitorService() {
		// TODO Auto-generated method stub
		return BuildStepMonitor.NONE;
	}
}
