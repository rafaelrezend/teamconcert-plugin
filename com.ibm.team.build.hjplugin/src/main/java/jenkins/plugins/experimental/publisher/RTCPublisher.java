package jenkins.plugins.experimental.publisher;

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
import hudson.util.DescribableList;

import java.io.IOException;
import java.util.List;

import jenkins.plugins.experimental.behaviour.RTCExtension;
import jenkins.plugins.experimental.behaviour.RTCExtensionDescriptor;
import jenkins.tasks.SimpleBuildStep;

import org.kohsuke.stapler.DataBoundConstructor;

/**
 * A buildstep wrapping any number of other buildsteps, controlling their
 * execution based on a defined condition.
 *
 */
public class RTCPublisher extends Recorder implements SimpleBuildStep {
	
	/**
     * All the configured extensions attached to this.
     */
    private DescribableList<RTCExtension,RTCExtensionDescriptor> extensions;

	@DataBoundConstructor
	public RTCPublisher(String label, String logFile) {
	}
	

	public DescribableList<RTCExtension, RTCExtensionDescriptor> getExtensions() {
        return extensions;
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
			return "Publish to RTC Build Result";
		}
		
		public List<RTCPublisherExtensionDescriptor> getExtensionDescriptors() {
            return RTCPublisherExtensionDescriptor.all();
        }
	}

	@Override
	public BuildStepMonitor getRequiredMonitorService() {
		// TODO Auto-generated method stub
		return BuildStepMonitor.NONE;
	}
}
