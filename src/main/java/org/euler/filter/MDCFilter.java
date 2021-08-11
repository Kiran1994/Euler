package org.euler.filter;

import org.apache.commons.lang3.StringUtils;
import org.euler.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MDCFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(MDCFilter.class);
    private static final List<String> MANDATORY_REQUEST_HEADERS = Arrays.asList(
            Constants.RequestHeaders.CLIENT_ID,
            Constants.RequestHeaders.REQUEST_ID
    );

    @Override
    public void filter(ContainerRequestContext requestContext) {
        if (!this.validateMandatoryHeaders(requestContext)) {
            return;
        }

        String clientIdHeaderValue = requestContext.getHeaderString(Constants.RequestHeaders.CLIENT_ID);
        MDC.put(Constants.ThreadContext.CLIENT_ID, clientIdHeaderValue);

        String requestIdHeaderValaue = requestContext.getHeaderString(Constants.RequestHeaders.REQUEST_ID);
        MDC.put(Constants.ThreadContext.REQUEST_ID, requestIdHeaderValaue);
    }

    public boolean validateMandatoryHeaders(ContainerRequestContext requestContext) {
        Set<String> missingMandatoryHeaders = new HashSet<>();

        for (String mandatoryRequestHeader : MANDATORY_REQUEST_HEADERS) {
            String headerValue = requestContext.getHeaderString(mandatoryRequestHeader);
            if (StringUtils.isEmpty(headerValue)) {
                missingMandatoryHeaders.add(mandatoryRequestHeader);
            }
        }

        if (!missingMandatoryHeaders.isEmpty()) {
            String clientIdHeaderValue = requestContext.getHeaderString(Constants.RequestHeaders.CLIENT_ID);
            String missingMandatoryHeadersStr = String.join(",", missingMandatoryHeaders);
            LOGGER.error("aborting request due to missing headers. missing headers: {}, client: {}",
                    missingMandatoryHeadersStr, clientIdHeaderValue);
            String errorMessage = "the following request headers are mandatory: " + missingMandatoryHeadersStr;
            requestContext.abortWith(Response.status(Response.Status.BAD_REQUEST).entity(errorMessage).build());

            return false;
        }

        return true;
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) {
        LOGGER.debug("clearing mdc");
        MDC.clear();
    }
}
