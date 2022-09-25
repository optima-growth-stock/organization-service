package com.optimagrowth.organization.service;

import java.util.Optional;
import java.util.UUID;

import com.optimagrowth.organization.events.source.SimpleSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.ScopedSpan;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.stereotype.Service;

import com.optimagrowth.organization.model.Organization;
import com.optimagrowth.organization.repository.OrganizationRepository;

@Service
public class OrganizationService {
	
    @Autowired
    private OrganizationRepository repository;

    @Autowired
    private SimpleSource simpleSource;

    @Autowired
    private Tracer tracer;

    public Organization findById(String organizationId) {
        ScopedSpan span = tracer.startScopedSpan("getOrgDBCall");
        span.event("Starting findById database query");

        Optional<Organization> opt = repository.findById(organizationId);
        simpleSource.publishOrganizationChange("GET", organizationId);

        span.tag("peer.service", "postgres");
        span.event("Finishing findById database query");
        span.end();

        return (opt.isPresent()) ? opt.get() : null;
    }

    public Organization create(Organization organization){
    	organization.setId( UUID.randomUUID().toString());
        organization = repository.save(organization);

        simpleSource.publishOrganizationChange("CREATED", organization.getId());

        return organization;

    }

    public void update(Organization organization){
        repository.save(organization);
        simpleSource.publishOrganizationChange("UPDATED", organization.getId());
    }

    public void delete(String organizationId){
        repository.deleteById(organizationId);
        simpleSource.publishOrganizationChange("DELETED", organizationId);
    }
}