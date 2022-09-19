package com.optimagrowth.organization.events.source;

import com.optimagrowth.organization.utils.UserContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import com.optimagrowth.organization.events.model.OrganizationChangeModel;

@Component
public class SimpleSource {
    private Source source;

    private static final Logger logger = LoggerFactory.getLogger(SimpleSource.class);

    @Autowired
    public SimpleSource(Source source){
        this.source = source;
    }

    public void publishOrganizationChange(String action, String organizationId){
        logger.debug("Sending Kafka message {} for Organization Id: {}", action, organizationId);
        OrganizationChangeModel change =  new OrganizationChangeModel(
                OrganizationChangeModel.class.getTypeName(),
                action,
                organizationId,
                UserContextHolder.getContext().getCorrelationId());

        source.output().send(MessageBuilder.withPayload(change).build());
    }
}