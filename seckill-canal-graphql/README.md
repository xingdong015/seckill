## 关于graphql的引用和使用
### graphql解决的痛点问题：https://tech.meituan.com/2021/05/06/bff-graphql.html
## 引入和使用方法：
```text
graphql 解决的是数据的读取问题，本质上是根据用户所需提供资源，将数据的获取量交由调用这来定义。以此来减少网络带宽，增加效率。
graphql 主要由两部分组成，一部分数据加载 dataloader（类似service层），一部分是graphql定义文档（两个作用，1：解析资源映射数据，2：前端文档）
两部分整合到一块,组装成graphql整体，对外提供服务，将dataloader中的方法名提取出来当做B端查询索引名，(mvc中的controller层提供的接口)。
```
## 目前主流的springboot集成graphql的方法。
```text
https://blog.csdn.net/chen_duochuang/article/details/107211306
有两个不好地方，第一需要实体类(麻烦)，第二需要每个都要定义GraphQLQueryResolver的实现方法，自定义一些方法名及实现。
```
## 自定义抽象方法，增加开发效率
```textmate
自定义抽象类，GraphqlBuilderConfig，AbstractRuntimeWiring，
在graphql构建过程中，手动注入GraphqlBuilderConfig实现类和资源下的定义文档，初始化一个完整的graphql容器，
然后在初始化一个servlet容器，将graphql的数据暴露出去。（实现不需要实体类，且配置后基础的查询findOne，findList，findPage都会自动实现）
``` 
## 使用方法
#### 在wiring目录下创建一个类，实现AbstractRuntimeWiring
### 在service中添加一个实现类，实现IAbstractService的获取索引方法，这是es的索引，和scheam中的定义文件对应，（具体就是从这个索引中查出数据映射到schema中定义的字段中）
#### 在resources/graphql下创建一个定义文件schema文件
```java
@Component
public class ProductWiring extends AbstractRuntimeWiring<ProductImpl> {

    @Override
    public String getMethodName() {
        return "product";  // 这里就是schema文件中的定义的query名前缀，会自定生成三个方法，product和productList和productPage方法
    }

    @Override
    public void loader(@Autowired RuntimeWiring.Builder builder) {
        super.loader(builder);
    }
}
```
```java
@Service
public class ProductImpl extends IAbstractService {

    @Override
    public String getIndex() {
        return CommonIndex.PRODUCT_INDEX; //这里是es索引
    }
}

```
```text
extend type Query {
    product(query: String): Product    ## 页面请求索引
    productList(query: String): [Product] ## 页面请求索引
}
## 可返回具体字段
type Product {
    id: String
    ctime: String
    name: String
    price: Float
}
```
### 自动服务之前需要连接es，
```text
 @Bean
    public GraphQL graphql() throws IOException {
        GraphQLSchema schema = getGraphqlSchema();
        GraphQL build = GraphQL.newGraphQL(schema).build();
        ExecutionResult result = build.execute("{product{id}}"); #### 因为这里又启动自检先执行了一个查询，所以es中必须要有一个默认索引，如果不需要可以注掉这里
        log.info("graphql query ==>> {}", result);
        return build;
    }
```
## 启动后，可以打开ui页面 http://localhost:8089/graphql-ui
```text
# 编写查询脚本
{
  productList(query: "") {
    id
    ctime
    name
    price
  }
}
```

