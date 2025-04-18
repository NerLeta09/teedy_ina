package com.sismics.docs.rest.resource;


import com.sismics.docs.core.dao.UserDao;
import com.sismics.docs.core.dao.UserRequestDao;

import com.sismics.docs.core.dao.dto.UserRequestDto;

import com.sismics.docs.core.model.jpa.User;
import com.sismics.docs.core.model.jpa.UserRequest;

import com.sismics.docs.core.constant.Constants;

import com.sismics.docs.rest.constant.BaseFunction;
import com.sismics.rest.exception.ClientException;
import com.sismics.rest.exception.ForbiddenClientException;
import com.sismics.rest.exception.ServerException;
import com.sismics.rest.util.ValidationUtil;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;
import jakarta.servlet.http.Cookie;
import jakarta.ws.rs.*;

import jakarta.ws.rs.core.Response;

import java.util.List;


/**
 * User Request REST resources.
 * This class handles user registration requests.
 *
 * @author YourName
 */
@Path("/user/request")
public class UserRequestResource extends BaseResource {
    @PUT
    public Response submitRequest(
            @FormParam("username") String username,
            @FormParam("email") String email,
            @FormParam("password") String password) {

        // Validate input data
        username = ValidationUtil.validateLength(username, "username", 3, 50);
        ValidationUtil.validateUsername(username, "username");
        email = ValidationUtil.validateLength(email, "email", 1, 100);
        ValidationUtil.validateEmail(email, "email");
        password = ValidationUtil.validateLength(password, "password", 8, 50);

        // // Check if user registration is allowed
        // if (!ConfigUtil.getConfigBooleanValue(ConfigType.ALLOW_USER_REGISTRATION)) {
        //     throw new ClientException("RegistrationDisabled", "User registration is disabled");
        // }

        // Create a new user request
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername(username);
        userRequest.setEmail(email);
        userRequest.setPassword(password);

        // Save the request to the database
        UserRequestDao userRequestDao = new UserRequestDao();
        try {
            userRequestDao.create(userRequest);
        } catch (Exception e) {
            throw new ServerException("UnknownError", "Unknown server error", e);
        }

        // Always return OK
        JsonObjectBuilder response = Json.createObjectBuilder()
                .add("status", "ok");
        return Response.ok().entity(response.build()).build();
    }

    @GET
    @Path("list")
    public Response listRequests() {
        if (!authenticate()) {
            throw new ForbiddenClientException();
        }
        checkBaseFunction(BaseFunction.ADMIN);

        // Get all pending user requests
        UserRequestDao userRequestDao = new UserRequestDao();
        List<UserRequestDto> userRequestDtoList = userRequestDao.findAllPending();

        // Build the response
        JsonArrayBuilder requests = Json.createArrayBuilder();
        for (UserRequestDto userRequestDto : userRequestDtoList) {
            requests.add(Json.createObjectBuilder()
                    .add("id", userRequestDto.getId())
                    .add("username", userRequestDto.getUsername())
                    .add("email", userRequestDto.getEmail())
                    .add("createDate", userRequestDto.getCreateDate().getTime()));
        }

        JsonObjectBuilder response = Json.createObjectBuilder()
                .add("requests", requests);
        return Response.ok().entity(response.build()).build();
    }

    @POST
    @Path("{id}")
    public Response approveRequest(
            @PathParam("id") String id,
            @FormParam("approve") boolean approve) {
        if (!authenticate()) {
            throw new ForbiddenClientException();
        }
        checkBaseFunction(BaseFunction.ADMIN);

        // Get the user request
        UserRequestDao userRequestDao = new UserRequestDao();
        UserRequest userRequest = userRequestDao.getById(id);
        if (userRequest == null) {
            throw new ClientException("RequestNotFound", "User request not found");
        }

        // Handle approval or rejection
        if (approve) {
            // Create a new user based on the request
            UserDao userDao = new UserDao();
            User newUser = new User();
            newUser.setUsername(userRequest.getUsername());
            newUser.setEmail(userRequest.getEmail());
            newUser.setPassword(userRequest.getPassword());
            newUser.setRoleId(Constants.DEFAULT_USER_ROLE);

            try {
                userDao.create(newUser, principal.getId());
            } catch (Exception e) {
                throw new ServerException("UnknownError", "Unknown server error", e);
            }
        }

        // Delete the request
        userRequestDao.delete(id);

        // Always return OK
        JsonObjectBuilder response = Json.createObjectBuilder()
                .add("status", "ok");
        return Response.ok().entity(response.build()).build();
    }
}