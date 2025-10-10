package com.examly.springapp.userspecifications;


import com.examly.springapp.model.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecifications {
    public static Specification<User> hasName(String name) {
        return (root, query, builder) ->
            name == null ? null : builder.like(builder.lower(root.get("firstName")), "%" + name.toLowerCase() + "%");
    }
    public static Specification<User> hasCompany(String company) {
        return (root, query, builder) ->
            company == null ? null : builder.like(builder.lower(root.get("company")), "%" + company.toLowerCase() + "%");
    }
    public static Specification<User> hasSkills(String skill) {
        return (root, query, builder) ->
            skill == null ? null : builder.like(builder.lower(root.get("skills")), "%" + skill.toLowerCase() + "%");
    }
}
