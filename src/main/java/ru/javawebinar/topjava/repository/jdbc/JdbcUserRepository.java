package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

import static ru.javawebinar.topjava.util.ValidationUtil.validateEntity;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private static final ResultSetExtractor<List<User>> USER_WITH_ROLES_EXTRACTOR = rs -> {
        Map<Integer, User> userMap = new LinkedHashMap<>();
        while (rs.next()) {
            Integer userId = rs.getInt("id");
            User user = userMap.computeIfAbsent(userId, id -> {
                User tmp = new User();
                tmp.setRoles(new HashSet<>());
                tmp.setId(userId);
                try {
                    tmp.setName(rs.getString("name"));
                    tmp.setEmail(rs.getString("email"));
                    tmp.setPassword(rs.getString("password"));
                    tmp.setEnabled(rs.getBoolean("enabled"));
                    tmp.setRegistered(rs.getDate("registered"));
                    tmp.setCaloriesPerDay(rs.getInt("calories_per_day"));
                } catch (Exception ignore) {
                }
                return tmp;
            });
            List<Role> roles = new ArrayList<>();
            String rolesString = rs.getString("role");
            if (!rs.wasNull() && rolesString != null) {
                Role tmp = Role.valueOf(rolesString);
                roles.add(tmp);
            }
            user.getRoles().addAll(roles);
        }
        return new ArrayList<>(userMap.values());
    };

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        validateEntity(user);
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
            if (!user.getRoles().isEmpty()) {
                jdbcTemplate.batchUpdate("INSERT INTO user_role (user_id, role) VALUES (?, ?)", new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        for (Role role : user.getRoles()) {
                            ps.setInt(1, user.getId());
                            ps.setString(2, role.name());
                        }
                    }

                    @Override
                    public int getBatchSize() {
                        return user.getRoles().size();
                    }
                });
            }
        } else if (namedParameterJdbcTemplate.update("""
                   UPDATE users SET name=:name, email=:email, password=:password, 
                   registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                """, parameterSource) == 0) {
            return null;
        } else if (!user.getRoles().isEmpty()
                && Arrays.stream(jdbcTemplate.batchUpdate("UPDATE user_role SET role=? WHERE user_id=?",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        for (Role role : user.getRoles()) {
                            ps.setInt(2, user.getId());
                            ps.setString(1, role.name());
                        }
                    }

                    @Override
                    public int getBatchSize() {
                        return user.getRoles().size();
                    }
                })).anyMatch(value -> value == 0)) {
            return null;
        } else if (jdbcTemplate.update("DELETE FROM user_role WHERE user_id=?", user.getId()) == 0) {
            return null;
        }
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query(
                "SELECT * FROM users u LEFT JOIN user_role ur ON u.id=ur.user_id WHERE id=?",
                USER_WITH_ROLES_EXTRACTOR, id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
        List<User> users = jdbcTemplate.query(
                "SELECT * FROM users u LEFT JOIN user_role ur ON u.id=ur.user_id WHERE email=?",
                USER_WITH_ROLES_EXTRACTOR, email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query(
                "SELECT * FROM users u LEFT JOIN user_role ur ON u.id=ur.user_id ORDER BY name, email",
                USER_WITH_ROLES_EXTRACTOR);
    }
}
