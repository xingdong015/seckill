package com.system.design.seckil.graphql.runtimeWiring.component;

import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 全量构建graphql对象
 *
 * @author 程征波
 * @date 2021-11-15 15:31:02
 */
@Component
public class GraphqlBuilderConfig {

    private static final String[] SCHEMA_EXT = new String[]{"*.graphqls", "*.graphql", "*.gql", "*.gqls"};

    @Resource
    private ResourcePatternResolver resourcePatternResolver;

    @Resource
    @Lazy
    private List<CustomizerRuntimeWiring> customizerRuntimeWiring;

    @Bean
    public GraphQL getGraphql() throws IOException {
        RuntimeWiring.Builder builder = RuntimeWiring.newRuntimeWiring();
        customizerRuntimeWiring.forEach(v -> v.loader(builder));
        TypeDefinitionRegistry registry = getSchemaResources().stream()
                .map(this::parseSchemaResource).reduce(TypeDefinitionRegistry::merge)
                .orElseThrow(() -> new IllegalArgumentException("'schemaResources' should not be empty"));
        return GraphQL.newGraphQL(new SchemaGenerator().makeExecutableSchema(registry, builder.build())).build();
    }

    private List<org.springframework.core.io.Resource> getSchemaResources() throws IOException {
        List<org.springframework.core.io.Resource> resources = new ArrayList<>();
        for(String extension : SCHEMA_EXT) {
            resources.addAll(Arrays.asList(resourcePatternResolver.getResources("classpath:graphql/" + extension)));
        }
        return resources;
    }

    private TypeDefinitionRegistry parseSchemaResource(org.springframework.core.io.Resource schemaResource) {
        Assert.notNull(schemaResource, "'schemaResource' not provided");
        Assert.isTrue(schemaResource.exists(), "'schemaResource' does not exist");
        try {
            try(InputStream inputStream = schemaResource.getInputStream()) {
                return new SchemaParser().parse(inputStream);
            }
        } catch(IOException ex) {
            throw new IllegalArgumentException("Failed to load schema resource: " + schemaResource.toString());
        }
    }
}
