package com.sismics.docs.core.dao;



import com.sismics.docs.core.model.jpa.UserRequest;
import com.sismics.docs.core.dao.dto.UserRequestDto;
import com.sismics.util.context.ThreadLocalContext;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

public class UserRequestDao {
    public void create(UserRequest userRequest) { //写入Request到DB
        if (userRequest.getId() == null) {
            userRequest.setId(UUID.randomUUID().toString());
        }
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        em.persist(userRequest);
    }

    public UserRequest getById(String requestId) {//单个查找

        if (requestId == null) {
            System.err.println("err");
            return null;
        }
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        return em.find(UserRequest.class, requestId);
    }

    //返回List for Admin
    public ArrayList<UserRequestDto> findAllPending() {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        TypedQuery<UserRequest> query = em.createQuery(
                "SELECT ur FROM UserRequest ur WHERE ur.status = 'PENDING'", UserRequest.class);
        List<UserRequest> userRequests = query.getResultList();

        ArrayList<UserRequestDto> userRequestDtos = new ArrayList<>();
        for (UserRequest userRequest : userRequests) {
            UserRequestDto dto = new UserRequestDto();
            dto.setId(userRequest.getId());
            dto.setUsername(userRequest.getUsername());
            dto.setStatus(userRequest.getStatus());
            userRequestDtos.add(dto);
        }

        return userRequestDtos;
    }
   
    //这真的需要吗？
    //需要返回是否申请通过 申请者获取通过信息
    public void updateStatus(String requestId, String status) {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        UserRequest userRequest = em.find(UserRequest.class, requestId);
        if (userRequest != null) {
            userRequest.setStatus(status);
        }
    }
    public void delete(String requestId) {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        UserRequest userRequest = em.find(UserRequest.class, requestId);
        if (userRequest != null) {
            em.remove(userRequest);
        }
    }
}