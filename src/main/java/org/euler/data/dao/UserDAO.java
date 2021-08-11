package org.euler.data.dao;

import org.euler.data.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UserDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDAO.class);

    private Map<Long, User> users;

    public UserDAO() {
        User kiran = new User();
        kiran.setId(1l);
        kiran.setName("Kiran");
        kiran.setEmail("perur.kiran@gmail.com");

        User appa = new User();
        appa.setId(2l);
        appa.setName("Sathyamurthy");
        appa.setEmail("perur.sathyamurthy@gmail.com");

        User kiran2 = new User();
        kiran2.setId(3l);
        kiran2.setName("Kiran");
        kiran2.setEmail("kirankumar.p@flipkart.com");

        this.users = Arrays.asList(kiran, appa, kiran2).stream().collect(Collectors.toMap(User::getId, Function.identity()));
    }

    public List<User> getUsersByIds(List<Long> ids) {
        LOGGER.info("fetching users by ids {}", ids);

        return ids.stream().map(this.users::get).collect(Collectors.toList());
    }

    public List<User> getUsersByName(List<String> names) {
        LOGGER.info("fetching users by names {}", names);

        return users.values().stream().filter(u -> names.contains(u.getName())).collect(Collectors.toList());
    }
}
