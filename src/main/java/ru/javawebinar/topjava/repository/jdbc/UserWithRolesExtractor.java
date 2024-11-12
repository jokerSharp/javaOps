package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class UserWithRolesExtractor implements ResultSetExtractor<List<User>> {

    @Override
    public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Integer, User> userMap = new HashMap<>();
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
                } catch (Exception ignore) {}
                return tmp;
            });
            List<Role> roles = new ArrayList<>();
            if (!rs.wasNull()) {
                Role tmp;
                try {
                    tmp = Role.valueOf(rs.getString("role"));
                    roles.add(tmp);
                } catch (Exception ignore) {}
            }
            user.getRoles().addAll(roles);
        }
        return new ArrayList<>(userMap.values().stream()
                .sorted(Comparator.comparing(User::getName).thenComparing(User::getEmail))
                .toList());
    }
}
