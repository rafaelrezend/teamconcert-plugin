package jenkins.plugins.experimental.behaviour;

import com.ibm.team.build.internal.hjplugin.RTCScm;

import hudson.DescriptorExtensionList;
import hudson.model.Descriptor;
import jenkins.model.Jenkins;

public abstract class RTCExtensionDescriptor extends Descriptor<RTCExtension> {
    public boolean isApplicable(Class<? extends RTCScm> type) {
        return true;
    }

    public static DescriptorExtensionList<RTCExtension,RTCExtensionDescriptor> all() {
        return Jenkins.getInstance().getDescriptorList(RTCExtension.class);
    }
}
