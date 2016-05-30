package jenkins.plugins.experimental.property;

import java.io.IOException;

import org.kohsuke.stapler.DataBoundConstructor;

import hudson.EnvVars;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.BuildListener;
import hudson.model.TaskListener;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Job;
import hudson.model.Run;
import hudson.tasks.BuildWrapper;
import hudson.tasks.BuildWrapperDescriptor;
import jenkins.model.OptionalJobProperty;
import jenkins.tasks.SimpleBuildWrapper;

public class RTCBuildDefinitionProperty extends SimpleBuildWrapper {
	
	private boolean specified;
	
	@DataBoundConstructor
	public RTCBuildDefinitionProperty() {
		// TODO Auto-generated constructor stub
	}

	@Extension
    public static class DescriptorImpl extends BuildWrapperDescriptor {

        @Override
        public String getDisplayName() {
            return "Connect to RTC Build Definition";
        }

		@Override
		public boolean isApplicable(AbstractProject<?, ?> arg0) {
			// TODO Auto-generated method stub
			return true;
		}

    }

	@Override
	public void setUp(Context arg0, Run<?, ?> arg1, FilePath arg2, Launcher arg3,
			TaskListener arg4, EnvVars arg5) throws IOException,
			InterruptedException {
		// TODO Auto-generated method stub
		
	}

}
