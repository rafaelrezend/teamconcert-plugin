package jenkins.plugins.experimental.behaviour;

import hudson.model.AbstractDescribableImpl;

public abstract class RTCExtension extends AbstractDescribableImpl<RTCExtension> {

    @Override
    public RTCExtensionDescriptor getDescriptor() {
        return (RTCExtensionDescriptor) super.getDescriptor();
    }
}
