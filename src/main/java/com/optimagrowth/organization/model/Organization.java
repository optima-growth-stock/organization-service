package com.optimagrowth.organization.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Setter
@Getter
@Entity
@Table(name = "organizations")
public class Organization {
    @Id
    @Column(name = "organization_id", nullable = false)
    String id;

    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    String contactName;

    @Column(nullable = false)
    String contactEmail;

    @Column(nullable = false)
    String contactPhone;

}
