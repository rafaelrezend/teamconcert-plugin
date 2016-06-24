package jenkins.plugins.experimental.parameterizedtrigger;

import java.io.IOException;

import hudson.Extension;
import hudson.model.Action;
import hudson.model.TaskListener;
import hudson.model.AbstractBuild;
import hudson.model.Descriptor;
import hudson.plugins.parameterizedtrigger.AbstractBuildParameters;

public class RTCServerParameter extends AbstractBuildParameters {

	@Override
	public Action getAction(AbstractBuild<?, ?> arg0, TaskListener arg1)
			throws IOException, InterruptedException, DontTriggerException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Extension(optional = true)
    public static class DescriptorImpl extends Descriptor<AbstractBuildParameters> {
        @Override
        public String getDisplayName() {
            return "RTC Build Definition parameters";
        }
    }

}
