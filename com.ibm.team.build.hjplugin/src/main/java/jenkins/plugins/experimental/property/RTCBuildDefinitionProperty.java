package jenkins.plugins.experimental.property;

import java.io.IOException;

import org.kohsuke.stapler.AncestorInPath;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import com.cloudbees.plugins.credentials.CredentialsMatchers;
import com.cloudbees.plugins.credentials.CredentialsProvider;
import com.cloudbees.plugins.credentials.common.StandardListBoxModel;
import com.cloudbees.plugins.credentials.common.StandardUsernamePasswordCredentials;
import com.cloudbees.plugins.credentials.domains.URIRequirementBuilder;
import com.ibm.team.build.internal.hjplugin.RTCBuildToolInstallation;

import hudson.EnvVars;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.TaskListener;
import hudson.model.AbstractProject;
import hudson.model.Job;
import hudson.model.Run;
import hudson.security.ACL;
import hudson.tasks.BuildWrapperDescriptor;
import hudson.util.ListBoxModel;
import jenkins.tasks.SimpleBuildWrapper;

public class RTCBuildDefinitionProperty extends SimpleBuildWrapper {
	
	private boolean specified;
	
	@DataBoundConstructor
	public RTCBuildDefinitionProperty() {
		// TODO Auto-generated constructor stub
	}
	
	public String getCredentialsId() {
		return "rezende/******";
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
		
		/**
		 * Provides a listbox of the defined build tools to pick from. Also includes
		 * an entry to signify no toolkit is chosen.
		 * @return The valid build tool options
		 */
		public ListBoxModel doFillBuildToolItems() {
			ListBoxModel listBox = new ListBoxModel();
//			listBox.add(new ListBoxModel.Option(Messages.RTCScm_no_build_tool_name(), ""));
			RTCBuildToolInstallation[] allTools = RTCBuildToolInstallation.allInstallations();
			for (RTCBuildToolInstallation tool : allTools) {
				ListBoxModel.Option option = new ListBoxModel.Option(tool.getName());
				listBox.add(option);
			}
			return listBox;
		}
		
		public ListBoxModel doFillCredentialsIdItems(@AncestorInPath Job<?, ?> project, @QueryParameter String serverURI) {
			return new StandardListBoxModel()
			.withEmptySelection()
			.withMatching(CredentialsMatchers.instanceOf(StandardUsernamePasswordCredentials.class),
					CredentialsProvider.lookupCredentials(StandardUsernamePasswordCredentials.class, project, ACL.SYSTEM, URIRequirementBuilder.fromUri(serverURI).build()));

			// TODO look into us being able to support certificates
//			return new StandardListBoxModel()
//			.withEmptySelection()
//			.withMatching(CredentialsMatchers.anyOf(
//					CredentialsMatchers.instanceOf(StandardUsernamePasswordCredentials.class),
//					CredentialsMatchers.instanceOf(StandardCertificateCredentials.class)),
//					CredentialsProvider.lookupCredentials(StandardUsernamePasswordCredentials.class, project, ACL.SYSTEM, URIRequirementBuilder.fromUri(url).build()));
		}
		
		public String getGlobalServerURI() {
			return "https://192.168.99.100:9443/ccm";
		}
		
		public int getGlobalTimeout() {
    		return 480;
	    }

    }

	@Override
	public void setUp(Context arg0, Run<?, ?> arg1, FilePath arg2, Launcher arg3,
			TaskListener arg4, EnvVars arg5) throws IOException,
			InterruptedException {
		// TODO Auto-generated method stub
		
	}

}
