package jenkins.plugins.experimental.property;

import org.kohsuke.stapler.DataBoundConstructor;

import hudson.Extension;
import hudson.model.BuildListener;
import hudson.model.AbstractBuild;
import hudson.model.Job;
import jenkins.model.OptionalJobProperty;

public class RTCBuildDefinitionProperty extends OptionalJobProperty<Job<?,?>> {
	
	private boolean specified;
	
	@DataBoundConstructor
	public RTCBuildDefinitionProperty() {
		// TODO Auto-generated constructor stub
	}



	/* (non-Javadoc)
	 * @see hudson.model.JobProperty#prebuild(hudson.model.AbstractBuild, hudson.model.BuildListener)
	 */
	@Override
	public boolean prebuild(AbstractBuild<?, ?> build, BuildListener listener) {
		// TODO Auto-generated method stub
		
		listener.getLogger().println("##### rtc build def property, pre build");
		
		return super.prebuild(build, listener);
	}
	
	

	@Extension
    public static class DescriptorImpl extends OptionalJobPropertyDescriptor {

        @Override
        public String getDisplayName() {
            return "Connect to RTC Build Definition";
        }

    }

}
