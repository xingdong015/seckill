package com.system.design.seckil.graphql.runtimeWiring.component;

import com.system.design.seckil.graphql.runtimeWiring.utils.ThreadUtils;
import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Component
@Configuration
public class GraphQLProviderConfig {

    private static final String[] SCHEMA_FILES_EXTENSIONS = new String[]{"*.graphqls", "*.graphql", "*.gql", "*.gqls"};


    @Bean
    public GraphQL graphQL(ResourcePatternResolver resourcePatternResolver) throws IOException {
        List<Resource>         schemaResources = resolveSchemaResources(resourcePatternResolver);
        TypeDefinitionRegistry registry        = new SchemaParser().parse(schemaResources.get(0).getFile());
        RuntimeWiring          runtimeWiring   = buildWiring();
        SchemaGenerator        schemaGenerator = new SchemaGenerator();
        GraphQLSchema          graphQLSchema   = schemaGenerator.makeExecutableSchema(registry, runtimeWiring);
        return GraphQL.newGraphQL(graphQLSchema).build();
    }

    private List<Resource> resolveSchemaResources(ResourcePatternResolver resolver) throws IOException {
        List<Resource> schemaResources = new ArrayList<>();
        for (String extension : SCHEMA_FILES_EXTENSIONS) {
            schemaResources.addAll(Arrays.asList(resolver.getResources("classpath:graphql/" + extension)));
        }
        return schemaResources;
    }


    private RuntimeWiring buildWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type("Query", item -> item.dataFetcher("getFieldName()", loaderFetcher()))
                .build();
    }

    private DataFetcher<CompletionStage<Object>> loaderFetcher() {
        return env -> CompletableFuture.supplyAsync(() -> "helloworld", ThreadUtils.getThread("hello"));
    }


}
