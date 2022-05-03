package com.oracle.assesment.main;

        import com.oracle.assesment.configuration.DropwizardBlogConfiguration;

        import com.oracle.assesment.resources.*;
        //import io.dropwizard.core.Application;
        import io.dropwizard.Application;
      //  import io.dropwizard.core.setup.Environment;


        import javax.servlet.DispatcherType;
        import javax.servlet.FilterRegistration;
        import javax.sql.DataSource;

        import io.dropwizard.setup.Environment;
        import org.eclipse.jetty.servlets.CrossOriginFilter;
        import org.skife.jdbi.v2.DBI;

        import java.util.EnumSet;

//        import com.toptal.blog.auth.DropwizardBlogAuthenticator;
//        import com.toptal.blog.auth.DropwizardBlogAuthorizer;
//        import com.toptal.blog.auth.User;
//        import com.toptal.blog.config.DropwizardBlogConfiguration;
//        import com.toptal.blog.health.DropwizardBlogApplicationHealthCheck;
//        import com.toptal.blog.resource.PartsResource;
//        import com.toptal.blog.service.PartsService;

public class DropwizardBlogApplication extends Application<DropwizardBlogConfiguration> {
    private static final String SQL = "sql";
    private static final String DROPWIZARD_BLOG_SERVICE = "Dropwizard blog service";
    private static final String BEARER = "Bearer";

    public static void main(String[] args) throws Exception {
        new DropwizardBlogApplication().run(args);
    }

    @Override
    public void run(DropwizardBlogConfiguration configuration, Environment environment) throws Exception{
        // Datasource configuration
        final DataSource dataSource =
                configuration.getDataSourceFactory().build(environment.metrics(), SQL);
        DBI dbi = new DBI(dataSource);

        // Register Health Check
        DropwizardBlogApplicationHealthCheck healthCheck =
                new DropwizardBlogApplicationHealthCheck(dbi.onDemand(TasksService.class));
        environment.healthChecks().register(DROPWIZARD_BLOG_SERVICE, healthCheck);
//
//        // Register OAuth authentication
//        environment.jersey()
//                .register(new AuthDynamicFeature(new OAuthCredentialAuthFilter.Builder<User>()
//                        .setAuthenticator(new DropwizardBlogAuthenticator())
//                        .setAuthorizer(new DropwizardBlogAuthorizer()).setPrefix(BEARER).buildAuthFilter()));
//        environment.jersey().register(RolesAllowedDynamicFeature.class);

        // Register resources
        environment.jersey().register(new TasksResource(dbi.onDemand(TasksService.class)));

        final FilterRegistration.Dynamic cors =
                environment.servlets().addFilter("CORS", CrossOriginFilter.class);

        // Configure CORS parameters
        cors.setInitParameter("allowedOrigins", "*");
        cors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
        cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");

        // Add URL mapping
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");

    }

//    @Override
//    public void run(DropwizardBlogConfiguration dropwizardBlogConfiguration, Environment environment) throws Exception {
//
//    }
}