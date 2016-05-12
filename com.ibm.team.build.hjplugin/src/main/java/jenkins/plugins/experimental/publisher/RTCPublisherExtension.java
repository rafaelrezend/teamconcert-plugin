package jenkins.plugins.experimental.publisher;

import hudson.model.AbstractDescribableImpl;

public abstract class RTCPublisherExtension extends AbstractDescribableImpl<RTCPublisherExtension> {

    @Override
    public RTCPublisherExtensionDescriptor getDescriptor() {
        return (RTCPublisherExtensionDescriptor) super.getDescriptor();
    }
}
