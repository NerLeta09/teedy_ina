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
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import java.util.List;

/**
 * User Request REST resources.
 * This class handles user registration requests.
 * 
 * @apiVersion 1.5.0
 * @apiGroup UserRequest
 */
@Path("/user/request")
public class UserRequestResource extends BaseResource {
    
    /**
     * Submit a new user registration request.
     *
     * @api {put} /user/request Submit a new user registration request
     * @apiName SubmitUserRequest
     * @apiGroup UserRequest
     * @apiParam {String} username The username of the user requesting registration.
     * @apiParam {String} password The password for the new user.
     * @apiSuccess {String} status The status of the request, will be "ok" on success.
     * @apiError (client) ValidationError Invalid input data.
     * @apiError (server) UnknownError Unknown server error during user request creation.
     * @apiPermission user
     * @apiVersion 1.5.0
     *
     * @param username The username of the user requesting registration.
     * @param password The password for the new user.
     * @return Response
     */
    @PUT
    public Response submitRequest(
            @FormParam("username") String username,
            @FormParam("password") String password) {

        // Validate input data
        username = ValidationUtil.validateLength(username, "username", 3, 50);
        ValidationUtil.validateUsername(username, "username");
        password = ValidationUtil.validateLength(password, "password", 8, 50);

        // Create a new user request
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername(username);
        userRequest.setPassword(password);

        // Store in DB
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

    /**
     * List all pending user registration requests.
     *
     * @api {get} /user/request/list List all pending user registration requests
     * @apiName ListUserRequests
     * @apiGroup UserRequest
     * @apiSuccess {Object[]} requests List of pending user requests.
     * @apiSuccess {String} requests.id The unique ID of the request.
     * @apiSuccess {String} requests.username The username of the user requesting registration.
     * @apiSuccess {Date} requests.createDate The creation date of the request.
     * @apiError (client) ForbiddenError Access denied.
     * @apiPermission admin
     * @apiVersion 1.5.0
     *
     * @return Response
     */
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
                    .add("createDate", userRequestDto.getCreateDate().getTime()));
        }

        JsonObjectBuilder response = Json.createObjectBuilder()
                .add("requests", requests);
        return Response.ok().entity(response.build()).build();
    }

    /**
     * Approve or reject a user registration request.
     *
     * @api {post} /user/request/:id Approve or reject a user registration request
     * @apiName ApproveUserRequest
     * @apiGroup UserRequest
     * @apiParam {String} id The unique ID of the user request.
     * @apiParam {Boolean} approve Whether to approve (`true`) or reject (`false`) the request.
     * @apiSuccess {String} status The status of the request, will be "ok" on success.
     * @apiError (client) ForbiddenError Access denied.
     * @apiError (client) RequestNotFound The user request could not be found.
     * @apiError (server) UnknownError Unknown server error during user creation.
     * @apiPermission admin
     * @apiVersion 1.5.0
     *
     * @param id The ID of the user request.
     * @param approve Whether to approve (`true`) or reject (`false`) the request.
     * @return Response
     */
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
