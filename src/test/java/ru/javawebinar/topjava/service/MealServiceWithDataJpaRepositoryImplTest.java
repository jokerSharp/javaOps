package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;

import static ru.javawebinar.topjava.Profiles.DATAJPA;
import static ru.javawebinar.topjava.Profiles.JDBC;

@ActiveProfiles(profiles = DATAJPA)
public class MealServiceWithDataJpaRepositoryImplTest extends BaseMealServiceTest {

}