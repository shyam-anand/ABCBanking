package com.abcbank.banking.servicecounter.controllers;

import com.abcbank.banking.common.models.ApiResponse;
import com.abcbank.banking.servicecounter.models.Service;
import com.abcbank.banking.servicecounter.models.ServiceCounter;
import com.abcbank.banking.servicecounter.models.ServiceCounterDTO;
import com.abcbank.banking.servicecounter.models.ServiceDTO;
import com.abcbank.banking.servicecounter.services.ServiceService;
import com.abcbank.banking.token.controllers.TokensController;
import com.abcbank.banking.token.models.Token;
import com.abcbank.banking.token.models.TokenDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * @author Shyam Anand (shyamwdr@gmail.com)
 *         25/08/17
 */
@RestController
@RequestMapping("/services")
public class ServicesController {

    private final ServiceService services;

    @Autowired
    public ServicesController(ServiceService services) {
        this.services = services;
    }

    @ApiOperation("Get all the banking services")
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity list() {
        Collection<ServiceDTO> serviceList = services.listServices().stream()
                .map(ServiceDTO::new)
                .collect(Collectors.toList());
        ApiResponse response = new ApiResponse<>(serviceList);
        response.add(linkTo(ServicesController.class).slash("service-id").withRel("details"));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation("Get details of a service")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity get(@PathVariable(value = "id", required = false) Long serviceId) {
        if (serviceId == null)
            return list();

        Service service = services.findService(serviceId);
        ApiResponse response = new ApiResponse<>(new ServiceDTO(service));
        response.add(linkTo(methodOn(ServicesController.class).request(serviceId, "customerId")).withRel("service_request"));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(value = "Create a service request", notes = "Use this end point to create a token for a user")
    @RequestMapping(value = "/{serviceId}/request", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity request(@PathVariable("serviceId") Long serviceId,
                                  @RequestParam("customerId") String customerId) {

        Token token = services.process(serviceId, customerId);

        Map<String, TokenDTO> data = new HashMap<>();
        data.put("token", new TokenDTO(token));
        ApiResponse response = new ApiResponse<>(data);
        response.add(ControllerLinkBuilder.linkTo(methodOn(TokensController.class).getToken(token.getId())).withRel("token"));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation("List the counters under the service")
    @RequestMapping(value = "/{serviceId}/counters", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity counters(@PathVariable("serviceId") Long serviceId) {
        Collection<ServiceCounterDTO> counters = services.getCounters(serviceId).stream().map(ServiceCounterDTO::new).collect(Collectors.toList());
        return new ResponseEntity<>(new ApiResponse<>(counters), HttpStatus.OK);
    }
}
