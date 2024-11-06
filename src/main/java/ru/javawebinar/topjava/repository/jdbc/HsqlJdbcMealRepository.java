package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static ru.javawebinar.topjava.Profiles.HSQL_DB;

@Profile(HSQL_DB)
@Repository
public class HsqlJdbcMealRepository extends AbstractJdbcMealRepository {

    public HsqlJdbcMealRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbcTemplate, namedParameterJdbcTemplate);
    }

    @Override
    Object getTimestamp(LocalDateTime localDateTime) {
        return Timestamp.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
