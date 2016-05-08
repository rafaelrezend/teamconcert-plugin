package jenkins.plugins.teamant.build.tasks;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.TaskListener;
import hudson.model.AbstractProject;
import hudson.model.Descriptor;
import hudson.model.Run;
import hudson.tasks.BuildStep;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import jenkins.tasks.SimpleBuildStep;

import org.kohsuke.stapler.DataBoundConstructor;

/**
 * A buildstep wrapping any number of other buildsteps, controlling their
 * execution based on a defined condition.
 *
 */
public class RTCBuildActivity extends Builder implements SimpleBuildStep {
	
	private String label;
	List<BuildStep> enclosedSteps;

	/**
	 * @param label
	 *            RTC Build Result Activity label.
	 * @param enclosedSteps
	 *            List of enclosed build steps.
	 */
	@DataBoundConstructor
	public RTCBuildActivity(String label, List<BuildStep> enclosedSteps) {
		this.label = label;
		this.enclosedSteps = enclosedSteps;
	}
	
	

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}



	/**
	 * @return the enclosedSteps
	 */
	public List<BuildStep> getEnclosedSteps() {
		return enclosedSteps;
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
			BuildStepDescriptor<Builder> {
		
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
			return "RTC Build Activity";
		}

		public List<? extends Descriptor<? extends BuildStep>> getBuilderDescriptors(
				AbstractProject<?, ?> project) {
			final List<BuildStepDescriptor<? extends Builder>> builders = new ArrayList<BuildStepDescriptor<? extends Builder>>();
			for (Descriptor<Builder> descriptor : Builder.all()) {
				// if (descriptor instanceof RTCBuildActivity.DescriptorImpl) {
				// continue;
				// }
				if (!(descriptor instanceof BuildStepDescriptor)) {
					continue;
				}
				BuildStepDescriptor<? extends Builder> buildStepDescriptor = (BuildStepDescriptor) descriptor;
				if (buildStepDescriptor.isApplicable(project.getClass())
						&& hasDbc(buildStepDescriptor.clazz)) {
					builders.add(buildStepDescriptor);
				}
			}
			return builders;
		}

		private boolean hasDbc(final Class<?> clazz) {
			for (Constructor<?> constructor : clazz.getConstructors()) {
				if (constructor.isAnnotationPresent(DataBoundConstructor.class))
					return true;
			}
			return false;
		}
	}
}
