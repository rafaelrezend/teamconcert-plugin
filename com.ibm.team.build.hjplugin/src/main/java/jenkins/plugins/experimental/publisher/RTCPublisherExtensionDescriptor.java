package jenkins.plugins.experimental.publisher;

import com.ibm.team.build.internal.hjplugin.RTCScm;

import hudson.DescriptorExtensionList;
import hudson.model.Descriptor;
import jenkins.model.Jenkins;

public abstract class RTCPublisherExtensionDescriptor extends Descriptor<RTCPublisherExtension> {
    public boolean isApplicable(Class<? extends RTCScm> type) {
        return true;
    }

    public static DescriptorExtensionList<RTCPublisherExtension,RTCPublisherExtensionDescriptor> all() {
        return Jenkins.getInstance().getDescriptorList(RTCPublisherExtension.class);
    }
}
