package br.com.devdepijama.cryptoticker.log;

import org.apache.catalina.connector.RequestFacade;
import org.apache.catalina.connector.ResponseFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class HttpInFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpInFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        final RequestFacade requestFacade = (RequestFacade) request;
        final ResponseFacade responseFacade = (ResponseFacade) response;
        LOGGER.info("http_in [cid:{}][m:{}][path:{}][s:{}]", MDC.get(LogConstants.CID.getValue()), requestFacade.getMethod(), requestFacade.getRequestURI(), responseFacade.getStatus());

        chain.doFilter(request, response);
    }
}
