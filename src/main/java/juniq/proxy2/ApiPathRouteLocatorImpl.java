package juniq.proxy2;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.BooleanSpec;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import reactor.core.publisher.Flux;

@AllArgsConstructor
public class ApiPathRouteLocatorImpl implements RouteLocator {
    private final RouteLocatorBuilder routeLocatorBuilder;

    @Override
    public Flux<Route> getRoutes() {
        RouteLocatorBuilder.Builder routesBuilder = routeLocatorBuilder.routes();

        List<ApiRoute> list = new Repository().list();
        list.forEach(a -> System.out.println(a.uri + " " + a.weight));
        return Flux.fromIterable(list)
            .map(apiRoute -> routesBuilder.route(predicateSpec -> setPredicateSpec(apiRoute, predicateSpec)))
            .collectList()
            .flatMapMany(builders -> routesBuilder.build()
                .getRoutes());
    }

    private Buildable<Route> setPredicateSpec(ApiRoute apiRoute, PredicateSpec predicateSpec) {
        BooleanSpec booleanSpec = predicateSpec.path("/**").and().weight(apiRoute.groupName, apiRoute.weight);
        return booleanSpec.uri(apiRoute.uri);
    }

    static class ApiRoute {
        private String uri;
        private int weight;
        private String groupName;

        ApiRoute(String uri, int weight, String groupName) {
            this.uri = uri;
            this.weight = weight;
            this.groupName = groupName;
        }
    }

    static class Repository {
        private static final List<ApiRoute> list = new ArrayList();

        static {
            list.add(new ApiRoute("http://localhost:8070", 8, "group1"));
            list.add(new ApiRoute("http://localhost:8090", 2, "group1"));
        }

        public Repository() {
        }

        public static List<ApiRoute> list() {
            return list;
        }

        public void add(ApiRoute apiRoute) {
            list.add(apiRoute);
        }

        public void add() {
            list.clear();
            list.add(new ApiRoute("http://localhost:8070", 1, "group2"));
            list.add(new ApiRoute("http://localhost:8090", 5, "group2"));
            list.add(new ApiRoute("http://localhost:8060", 4, "group2"));
        }
    }
}
