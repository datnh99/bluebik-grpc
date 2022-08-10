package com.bluebik.service;

import com.bluebik.User;
import com.bluebik.UserAddRequest;
import com.bluebik.model.AddUserRequest;
import com.bluebik.model.BluebikUser;
import com.google.protobuf.Descriptors;

import java.util.List;
import java.util.Map;

public interface ApiService {
    Map<Descriptors.FieldDescriptor, Object> getUser(int id);

    Map<Descriptors.FieldDescriptor, Object> createUser(AddUserRequest request);

    Map<Descriptors.FieldDescriptor, Object> updateUser(BluebikUser user);

    Map<Descriptors.FieldDescriptor, Object> deleteUser(int id);

    List<Map<Descriptors.FieldDescriptor, Object>> createMultipleUser(List<AddUserRequest> requests) throws InterruptedException;

    List<Map<Descriptors.FieldDescriptor, Object>> searchUserByUsername(String username) throws InterruptedException;

    List<Map<Descriptors.FieldDescriptor, Object>> updateMultipleUser(List<BluebikUser> users) throws InterruptedException;

    Map<Descriptors.FieldDescriptor, Object> deleteMultipleUser(List<Integer> idList) throws InterruptedException;
}
