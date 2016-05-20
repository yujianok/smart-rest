package com.tsfintech.rest.spring.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tsfintech.rest.core.dispatcher.RestRequestDispatcher;
import com.tsfintech.rest.core.exception.RestServiceException;
import com.tsfintech.rest.core.marshaller.EntityJsonMarshaller;
import com.tsfintech.rest.core.model.EntityQuery;
import com.tsfintech.rest.core.model.EntityUri;

/**
 * Created by jack on 14-9-3.
 */
@RestController
@RequestMapping("${rest.service.path}")
public class RestServiceController {

    private static final Logger logger = LoggerFactory.getLogger(RestServiceController.class);

    @Autowired
    private RestRequestDispatcher restRequestDispatcher;

    @Autowired
    private EntityJsonMarshaller entityJsonMarshaller;

    @Value("${rest.service.path}")
    private String relativePath;

    @ModelAttribute
    protected EntityUri buildEntityUri(HttpServletRequest request) {
        String requestUri = request.getPathInfo();
        if (requestUri == null) {
            requestUri = request.getRequestURI();
        }

        String path = requestUri.substring(request.getContextPath().length()).substring(relativePath.length());
        return EntityUri.parseUri(path);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/**")
    public ResponseEntity<String> doSave(HttpEntity<String> httpEntity, @ModelAttribute EntityUri entityUri) {
        String requestBody = httpEntity.getBody();

        Object entity = restRequestDispatcher.doCreate(entityUri);
        entityJsonMarshaller.updateBySource(requestBody, entity);
        Object result = restRequestDispatcher.doSave(entityUri, entity);

        return buildResponseEntity(result);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/**")
    public ResponseEntity<String> doUpdate(HttpEntity<String> httpEntity, @ModelAttribute EntityUri entityUri) {
        String requestBody = httpEntity.getBody();

        Object entity = restRequestDispatcher.doCreate(entityUri);
        entityJsonMarshaller.updateBySource(requestBody, entity);
        Object result = restRequestDispatcher.doUpdate(entityUri, entity);

        return buildResponseEntity(result);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/**")
    public ResponseEntity<String> doDelete(@ModelAttribute EntityUri entityUri) {

        Object result = restRequestDispatcher.doDelete(entityUri);

        return buildResponseEntity(result);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/**")
    public ResponseEntity<String> doGet(HttpServletRequest req, @ModelAttribute EntityUri entityUri) throws Exception {

        if (entityUri.getEntityId() != null) {
            Object result = restRequestDispatcher.doRetrieve(entityUri);
            return buildResponseEntity(result);
        } else {
            String queryString = req.getQueryString() == null ? null : URLDecoder.decode(req.getQueryString(), "UTF-8");
            EntityQuery entityQuery = EntityQuery.parseQuery(queryString);

            if (entityQuery.isCount()) {
                long count = restRequestDispatcher.doCount(entityUri, entityQuery);
                return buildResponseEntity(count);
            } else {
                Object result = restRequestDispatcher.doFind(entityUri, entityQuery);
                return buildResponseEntity(result);
            }
        }
    }


    @RequestMapping(method = RequestMethod.POST, value = "/*/*/*/*")
    public ResponseEntity<String> doOperation(HttpEntity<String> httpEntity, @ModelAttribute EntityUri entityUri) throws Exception {
        String body = httpEntity.getBody();
        List<String> args = new ArrayList<>();

        if (StringUtils.isNotBlank(body)) {
            args = entityJsonMarshaller.unmarshalCollection(body, String.class);
        }

        Object result = restRequestDispatcher.doOperation(entityUri, args.toArray(new String[args.size()]));
        return buildResponseEntity(result);
    }

    protected ResponseEntity<String> buildResponseEntity(Object result) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json; charset=utf-8");

        String stringResult = result == null ? null : entityJsonMarshaller.marshal(result);
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(stringResult, httpHeaders, HttpStatus.OK);

        return responseEntity;
    }

    @ExceptionHandler
    protected ResponseEntity<String> handleException(Exception e) throws IOException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "text/plain; charset=utf-8");
        if (e instanceof RestServiceException) {
            logger.warn("RestServiceException: " + e.getMessage());
            int stateCode = ((RestServiceException) e).getStateCode();
            return new ResponseEntity<String>(e.getMessage(), httpHeaders, HttpStatus.valueOf(stateCode));
        } else {
            logger.error(e.getMessage(), e);
            return new ResponseEntity<String>(e.getMessage(), httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
