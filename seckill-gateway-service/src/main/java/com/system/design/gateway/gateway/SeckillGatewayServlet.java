package com.system.design.gateway.gateway;

import com.alibaba.cloud.dubbo.http.MutableHttpServerRequest;
import com.alibaba.cloud.dubbo.metadata.DubboRestServiceMetadata;
import com.alibaba.cloud.dubbo.metadata.RequestMetadata;
import com.alibaba.cloud.dubbo.metadata.RestMethodMetadata;
import com.alibaba.cloud.dubbo.metadata.repository.DubboServiceMetadataRepository;
import com.alibaba.cloud.dubbo.service.DubboGenericServiceExecutionContext;
import com.alibaba.cloud.dubbo.service.DubboGenericServiceExecutionContextFactory;
import com.alibaba.cloud.dubbo.service.DubboGenericServiceFactory;
import com.system.design.gateway.adapter.HttpRequestAdapter;
import com.system.design.seckill.common.utils.StringUtils;
import org.apache.dubbo.rpc.service.GenericException;
import org.apache.dubbo.rpc.service.GenericService;
import org.springframework.util.StreamUtils;
import org.springframework.web.servlet.HttpServletBean;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static com.alibaba.cloud.commons.lang.StringUtils.substringAfter;

@WebServlet(urlPatterns = {"/seckill/*"})
public class SeckillGatewayServlet extends HttpServletBean {

    private final DubboServiceMetadataRepository repository;
    private final DubboGenericServiceFactory serviceFactory;
    private final DubboGenericServiceExecutionContextFactory contextFactory;
    private final Map<String, Object> dubboTranslatedAttributes = new HashMap<>();

    public SeckillGatewayServlet(DubboServiceMetadataRepository repository, DubboGenericServiceFactory serviceFactory,
                                 DubboGenericServiceExecutionContextFactory contextFactory){
        this.repository = repository;
        this.serviceFactory = serviceFactory;
        this.contextFactory = contextFactory;
        dubboTranslatedAttributes.put("protocol", "dubbo");
        dubboTranslatedAttributes.put("cluster", "failover");
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String serviceName = resolveServiceName(request);

        String restPath = substringAfter(request.getRequestURI(), serviceName);

        // 初始化 serviceName 的 REST 请求元数据
        repository.initializeMetadata(serviceName);

        // 将 HttpServletRequest 转化为 RequestMetadata
        RequestMetadata clientMetadata = buildRequestMetadata(request, restPath);

        DubboRestServiceMetadata dubboRestServiceMetadata = repository.get(serviceName,
                clientMetadata);

        if (dubboRestServiceMetadata == null) {
            // if DubboServiceMetadata is not found, executes next
            throw new ServletException("DubboServiceMetadata can't be found!");
        }

        RestMethodMetadata dubboRestMethodMetadata = dubboRestServiceMetadata
                .getRestMethodMetadata();

        GenericService genericService = serviceFactory.create(dubboRestServiceMetadata,
                dubboTranslatedAttributes);

        // TODO: Get the Request Body from HttpServletRequest
        byte[] body = getRequestBody(request);
        MutableHttpServerRequest httpServerRequest = new MutableHttpServerRequest(
                new HttpRequestAdapter(request), body);

        DubboGenericServiceExecutionContext context = contextFactory
                .create(dubboRestMethodMetadata, httpServerRequest);

        Object result = null;
        GenericException exception = null;
        try {
            result = genericService.$invoke(context.getMethodName(),
                    context.getParameterTypes(), context.getParameters());
        }
        catch (GenericException e) {
            exception = e;
        }
        response.getWriter().println(result);
    }


    /**
     * reslove service name
     * @param request
     * @return
     */
    private String resolveServiceName(HttpServletRequest request) {

        // /g/{app-name}/{rest-path}
        String requestURI = request.getRequestURI();
        // /g/
        String servletPath = request.getServletPath();

        String part = StringUtils.substringAfter(requestURI, servletPath);

        String serviceName = StringUtils.substringBetween(part, "/", "/");

        return serviceName;
    }


    private RequestMetadata buildRequestMetadata(HttpServletRequest request,
                                                 String restPath) {
        RequestMetadata requestMetadata = new RequestMetadata();
        requestMetadata.setPath(restPath);
        requestMetadata.setMethod(request.getMethod());
        requestMetadata.setParams(getParams(request));
        requestMetadata.setHeaders(getHeaders(request));
        return requestMetadata;
    }


    private Map<String, List<String>> getHeaders(HttpServletRequest request) {
        Map<String, List<String>> map = new LinkedHashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            Enumeration<String> headerValues = request.getHeaders(headerName);
            map.put(headerName, Collections.list(headerValues));
        }
        return map;
    }


    private Map<String, List<String>> getParams(HttpServletRequest request) {
        Map<String, List<String>> map = new LinkedHashMap<>();
        for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
            map.put(entry.getKey(), Arrays.asList(entry.getValue()));
        }
        return map;
    }

    private byte[] getRequestBody(HttpServletRequest request) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        return StreamUtils.copyToByteArray(inputStream);
    }

}
