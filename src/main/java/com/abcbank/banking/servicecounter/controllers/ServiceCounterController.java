package com.abcbank.banking.servicecounter.controllers;

import com.abcbank.banking.common.models.ApiResponse;
import com.abcbank.banking.customer.models.CustomerDTO;
import com.abcbank.banking.customer.models.CustomerType;
import com.abcbank.banking.servicecounter.models.ServiceCounter;
import com.abcbank.banking.servicecounter.models.ServiceCounterDTO;
import com.abcbank.banking.servicecounter.models.ServiceDTO;
import com.abcbank.banking.servicecounter.services.CounterService;
import com.abcbank.banking.token.models.Token;
import com.abcbank.banking.token.models.TokenDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * @author Shyam Anand (shyamwdr@gmail.com)
 *         25/08/17
 */
@RestController
@RequestMapping("/counters")
public class ServiceCounterController {

    private final CounterService counterService;

    @Autowired
    public ServiceCounterController(CounterService counterService) {
        this.counterService = counterService;
    }

    @ApiOperation("List all counters")
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity list(@RequestParam(value = "service", required = false) String serviceId) {
        System.out.println(serviceId);
        List<ServiceCounterDTO> serviceCounters = counterService.listCounters().stream()
                .filter(counter -> (serviceId == null || Long.valueOf(serviceId) == counter.getService().getId()))
                .map(ServiceCounterDTO::new)
                .collect(Collectors.toList());


        ApiResponse response = new ApiResponse<>(serviceCounters);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(value = "Create and add a counter to service", notes = "By default NORMAL priority will be assigned")
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity register(@RequestParam("service") long serviceId,
                                   @RequestParam(value = "priority", required = false, defaultValue = "false") String priority) {

        CustomerType customerType = priority.equalsIgnoreCase("true") || priority.equalsIgnoreCase("y") || priority.equalsIgnoreCase("yes") ? CustomerType.PRIORITY : CustomerType.NORMAL;
        ServiceCounter serviceCounter = counterService.addCounter(serviceId, customerType);

        if (serviceCounter == null) {
            throw new RuntimeException("service counter is null");
        }

        ApiResponse response = new ApiResponse<>(new ServiceCounterDTO(serviceCounter));
        response.add(linkTo(methodOn(ServiceCounterController.class).get(serviceCounter.getId())).withSelfRel());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation("Get details of a counter")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity get(@PathVariable("id") long counterId) {

        ServiceCounterDTO counterDTO = new ServiceCounterDTO(counterService.getCounter(counterId));
        ApiResponse response = new ApiResponse<>(counterDTO);
        response.add(linkTo(methodOn(ServiceCounterController.class).getTokens(counterId)).withRel("token_queue"));

        return new ResponseEntity<>(new ApiResponse<>(), HttpStatus.OK);
    }

    @ApiOperation("Get the token queue for a counter")
    @RequestMapping(value = "/{id}/queue", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getTokens(@PathVariable("id") long id) {
        Collection<TokenDTO> tokens = counterService.getTokenQueue(id).stream().map(token -> {
            TokenDTO tokenDTO = new TokenDTO();

            tokenDTO.setId(token.getId());
            tokenDTO.setCustomer(new CustomerDTO(token.getCustomer()));
            tokenDTO.setServices(token.getServices().stream().map(ServiceDTO::new).collect(Collectors.toList()));
            tokenDTO.setCreatedAt(token.getCreatedAt());
            tokenDTO.setStatus(token.getStatus());
            return tokenDTO;
        }).collect(Collectors.toList());
        return new ResponseEntity<>(new ApiResponse<>(tokens), HttpStatus.OK);
    }

    @ApiOperation("Get details of the current token")
    @RequestMapping(value = "{id}/token", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity currentToken(@PathVariable("id") Long counterId) {
        Token token = counterService.getCurrentToken(counterId);
        ApiResponse response;
        if (token != null) {
            response = new ApiResponse<>(new TokenDTO(token));
        } else
            response = new ApiResponse();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation("Move a token from WAITING to PROCESSING")
    @RequestMapping(value = "{id}/serve", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity serve(@PathVariable("id") Long counterId) {
        Token token = counterService.serve(counterId);
        ApiResponse response;
        if (token == null)
            response = new ApiResponse();
        else
            response = new ApiResponse<>(new TokenDTO(token));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation("Move to the next token")
    @RequestMapping(value = "/{id}/next", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity advance(@PathVariable("id") Long counterId) {
        Token token = counterService.advance(counterId);
        ApiResponse response;
        if (token != null)
            response = new ApiResponse<>(new TokenDTO(token));
        else
            response = new ApiResponse();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
