package com.abcbank.banking.token.controllers;

import com.abcbank.banking.common.models.ApiResponse;
import com.abcbank.banking.token.models.Action;
import com.abcbank.banking.token.models.Status;
import com.abcbank.banking.token.models.Token;
import com.abcbank.banking.token.models.TokenDTO;
import com.abcbank.banking.token.services.TokenServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
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
@RequestMapping("/tokens")
public class TokensController {

    private final TokenServiceImpl tokenService;

    @Autowired
    public TokensController(TokenServiceImpl tokenService) {
        this.tokenService = tokenService;
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getTokens(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                    @RequestParam(value = "size", required = false, defaultValue = "20") Integer size) {
        return list(page, size, false);
    }

    @ApiOperation(value = "List all tokens", notes = "By default, only WAITING and PROCESSING tokens are lited")
    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity listAll(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                  @RequestParam(value = "size", required = false, defaultValue = "20") Integer size) {
        return list(page, size, true);
    }

    /**
     * List tokens
     * @param page Page number
     * @param size Number of items per page
     * @param includeCompleted TRUE to list COMPLETED and CANCELLED tokens
     * @return List of Tokens
     */
    private ResponseEntity list(Integer page, Integer size, boolean includeCompleted) {
        page = page == null ? 0 : page;
        size = size == null ? 20 : size;

        Page<Token> tokenPage = tokenService.list(new PageRequest(page, size), includeCompleted);
        List<TokenDTO> tokenList = tokenPage.getContent().stream().map(TokenDTO::new).collect(Collectors.toList());
        int pageNumber = tokenPage.getNumber();
        int totalPages = tokenPage.getTotalPages();

        ApiResponse response = new ApiResponse<>(tokenList);
        if (totalPages > 1) {
            if (pageNumber <= totalPages - 1) {
                response.add(linkTo(methodOn(TokensController.class).getTokens(totalPages - 1, size)).withRel("last"));
                if (pageNumber < totalPages - 1)
                    response.add(linkTo(methodOn(TokensController.class).getTokens(page + 1, size)).withRel("next"));
            } else if (pageNumber > 0) {
                response.add(linkTo(methodOn(TokensController.class).getTokens(0, size)).withRel("first"));
                if (pageNumber > 1)
                    response.add(linkTo(methodOn(TokensController.class).getTokens(pageNumber - 1, size)).withRel("prev"));
            }
            response.add(linkTo(methodOn(TokensController.class).getTokens(pageNumber, size)).withSelfRel());
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation("Get details of a token")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getToken(@PathVariable("id") long id) {
        Token token = tokenService.get(id, true);
        if (token != null) {
            return new ResponseEntity<>(new ApiResponse<>(new TokenDTO(token)), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(value = "Add an Action to the token", notes = "An action can have comments and a status")
    @RequestMapping(value = "/{id}/action", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addAction(@PathVariable("id") long id,
                                    @RequestBody Action action) {

        Token token = tokenService.addAction(id, action);

        ApiResponse response = new ApiResponse<>(new TokenDTO(token));
        response.add(linkTo(methodOn(TokensController.class).getToken(id)).withSelfRel());

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Set status for a token", notes = "This can be used for cancelling a token. Accepted values are WAITING, PROCESSING, CANCELLED and COMPLETED")
    @RequestMapping(value = "/{id}", method = {RequestMethod.POST, RequestMethod.PUT}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateStatus(@PathVariable("id") long id,
                                       @RequestParam("status") Status status) {

        Collection<Status> expected = Arrays.asList(Status.WAITING, Status.PROCESSING, Status.COMPLETED, Status.CANCELLED);
        if (!expected.contains(status))
            throw new IllegalArgumentException("Invalid status " + status.name());

        Token token = tokenService.updateStatus(id, status);

        ApiResponse response = new ApiResponse<>(new TokenDTO(token));
        response.add(linkTo(methodOn(TokensController.class).getToken(id)).withSelfRel());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
