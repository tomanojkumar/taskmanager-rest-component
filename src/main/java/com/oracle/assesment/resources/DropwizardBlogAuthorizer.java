package com.oracle.assesment.resources;



import io.dropwizard.auth.Authorizer;

import java.util.Objects;

public class DropwizardBlogAuthorizer implements Authorizer<User> {
    @Override
    public boolean authorize(User principal, String role) {
        // Allow any logged in user.
        if (Objects.nonNull(principal)) {
            return true;
        }
        return false;
    }
}
