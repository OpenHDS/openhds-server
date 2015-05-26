package org.openhds.controller.service.impl;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.controller.service.EntityService;
import org.openhds.domain.model.*;
import org.openhds.domain.service.SitePropertiesService;

import java.sql.SQLException;
import java.util.Calendar;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class BaselineServiceTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private EntityService entityService = mock(EntityService.class);
    private SitePropertiesService siteProperties = mock(SitePropertiesService.class);

    private BaselineServiceImpl underTest = spy(new BaselineServiceImpl(null, entityService, siteProperties));
    private Individual individual;
    private SocialGroup socialGroup;
    private Residency residency;
    private Membership membership;
    private Location location;
    private FieldWorker fieldWorker;
    private Calendar startDate;
    private Relationship relationship;

    @Before
    public void setUp() {
        individual = new Individual();
        membership = new Membership();
        socialGroup = new SocialGroup();
        residency = new Residency();
        location = new Location();
        fieldWorker = new FieldWorker();
        startDate = Calendar.getInstance();
        relationship = new Relationship();

        when(siteProperties.getEnumerationCode()).thenReturn(null);
        when(siteProperties.getNotApplicableCode()).thenReturn(null);

        doReturn(residency).
                when(underTest).createResidency(
                any(Individual.class),
                any(Location.class),
                any(FieldWorker.class),
                any(Calendar.class));
    }

    @Test
    public void createResidencyAndMembershipForIndividual_createsIndividual_Membership_andResidency()
            throws SQLException, ConstraintViolations {

        underTest.createResidencyAndMembershipForIndividual(individual, membership, null, null, null);

        verify(entityService, times(1)).create(individual);
        verify(entityService, times(1)).create(membership);
        verify(entityService, times(1)).create(residency);
    }

    @Test
    public void createSocialGroupAndResidencyForIndividual_createsIndividual_SocialGroup_andResidency()
            throws SQLException, ConstraintViolations {

        underTest.createSocialGroupAndResidencyForIndividual(individual, socialGroup, null, null, null);

        verify(entityService, times(1)).create(individual);
        verify(entityService, times(1)).create(socialGroup);
        verify(entityService, times(1)).create(residency);
    }

    @Test
    public void createsIndividual()
            throws SQLException, ConstraintViolations {

        underTest.createIndividual(individual);

        verify(entityService, times(1)).create(individual);
    }


    @Test
    public void createsSocialGroup()
            throws SQLException, ConstraintViolations {

        underTest.createSocialGroup(socialGroup);

        verify(entityService, times(1)).create(socialGroup);
    }

    @Test
    public void createResidencyForIndividual_createsIndividual_andResidency() throws ConstraintViolations, SQLException {
        underTest.createResidencyForIndividual(individual, location, fieldWorker, startDate);

        verify(entityService, times(1)).create(individual);
        verify(entityService, times(1)).create(residency);
    }

    @Test
    public void createMembershipForIndividual_createsOnlyMembership() throws SQLException, ConstraintViolations {
        underTest.createMembershipForIndividual(individual, membership, socialGroup, fieldWorker, startDate);

        verify(entityService, times(1)).create(membership);
    }

    @Test
    public void createResidencyMembershipAndRelationshipForIndividual_createscreatesIndividual_Membership_Residency_andRelationship() throws ConstraintViolations, SQLException {
        underTest.createResidencyMembershipAndRelationshipForIndividual(individual, membership, relationship, location, fieldWorker, startDate);

        verify(entityService, times(1)).create(individual);
        verify(entityService, times(1)).create(membership);
        verify(entityService, times(1)).create(residency);
        verify(entityService, times(1)).create(relationship);
    }
}

