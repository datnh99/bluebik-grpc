package com.bluebik.service.impl;

import com.bluebik.*;
import com.bluebik.model.AddUserRequest;
import com.bluebik.model.BluebikUser;
import com.bluebik.service.ApiService;
import com.google.protobuf.Descriptors;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Service
public class ApiServiceImpl implements ApiService {

    @GrpcClient("grpc-bluebik-service")
    UserServiceGrpc.UserServiceBlockingStub userServiceBlockingStub;

    @GrpcClient("grpc-bluebik-service")
    UserServiceGrpc.UserServiceStub userServiceStub;

    @Override
    public Map<Descriptors.FieldDescriptor, Object> getUser(int id) {
        var userId = UserId.newBuilder().setUserId(id).build();
        var user = userServiceBlockingStub.getUser(userId);
        return user.getAllFields();
    }

    @Override
    public Map<Descriptors.FieldDescriptor, Object> createUser(AddUserRequest request) {
        var addRequest = UserAddRequest.newBuilder()
                .setAccount(request.getAccount())
                .setName(request.getName())
                .setAge(request.getAge())
                .build();
        var responseMessage = userServiceBlockingStub.createUser(addRequest);
        return responseMessage.getAllFields();
    }

    @Override
    public Map<Descriptors.FieldDescriptor, Object> updateUser(BluebikUser bluebikUser) {
        var user = User.newBuilder()
                .setUserId(bluebikUser.getId())
                .setAccount(bluebikUser.getAccount())
                .setName(bluebikUser.getName())
                .setAge(bluebikUser.getAge())
                .build();

        var result = userServiceBlockingStub.updateUser(user);

        return result.getAllFields();
    }

    @Override
    public Map<Descriptors.FieldDescriptor, Object> deleteUser(int id) {
        var userId = UserId.newBuilder()
                .setUserId(id)
                .build();

        var responseMessage = userServiceBlockingStub.deleteUser(userId);

        return responseMessage.getAllFields();
    }

    @Override
    public List<Map<Descriptors.FieldDescriptor, Object>> createMultipleUser(List<AddUserRequest> requests) throws InterruptedException {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final List<Map<Descriptors.FieldDescriptor, Object>> response = new ArrayList<>();

        StreamObserver<UserAddRequest> responseObserver = userServiceStub.createMultipleUser(new StreamObserver<>() {

            @Override
            public void onNext(ResponseMessage responseMessage) {
                response.add(responseMessage.getAllFields());
            }

            @Override
            public void onError(Throwable throwable) {
                countDownLatch.countDown();
            }

            @Override
            public void onCompleted() {
                countDownLatch.countDown();
            }

        });

        requests.forEach(userRequest -> responseObserver.onNext(
                UserAddRequest.newBuilder()
                        .setAccount(userRequest.getAccount())
                        .setName(userRequest.getName())
                        .setAge(userRequest.getAge())
                        .build()
        ));
        responseObserver.onCompleted();

        boolean await = countDownLatch.await(1, TimeUnit.MINUTES);
        return await ? response : Collections.emptyList();
    }

    @Override
    public List<Map<Descriptors.FieldDescriptor, Object>> searchUserByUsername(String keyword) throws InterruptedException {
        var username = Username.newBuilder()
                .setName(keyword).
                build();
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final List<Map<Descriptors.FieldDescriptor, Object>> response = new ArrayList<>();

        userServiceStub.searchUserByUsername(username, new StreamObserver<>() {

            @Override
            public void onNext(User user) {
                response.add(user.getAllFields());
            }

            @Override
            public void onError(Throwable throwable) {
                countDownLatch.countDown();
            }

            @Override
            public void onCompleted() {
                countDownLatch.countDown();
            }

        });
        boolean await = countDownLatch.await(1, TimeUnit.MINUTES);
        return await ? response : Collections.emptyList();
    }

    @Override
    public List<Map<Descriptors.FieldDescriptor, Object>> updateMultipleUser(List<BluebikUser> users) throws InterruptedException {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final List<Map<Descriptors.FieldDescriptor, Object>> response = new ArrayList<>();

        StreamObserver<User> responseObserver = userServiceStub.updateMultipleUser(new StreamObserver<>() {

            @Override
            public void onNext(User user) {
                response.add(user.getAllFields());
            }

            @Override
            public void onError(Throwable throwable) {
                countDownLatch.countDown();
            }

            @Override
            public void onCompleted() {
                countDownLatch.countDown();
            }

        });

        users.forEach(userRequest -> responseObserver.onNext(
                User.newBuilder()
                        .setUserId(userRequest.getId())
                        .setAccount(userRequest.getAccount())
                        .setName(userRequest.getName())
                        .setAge(userRequest.getAge())
                        .build()
        ));
        responseObserver.onCompleted();

        boolean await = countDownLatch.await(1, TimeUnit.MINUTES);
        return await ? response : Collections.emptyList();
    }

    @Override
    public Map<Descriptors.FieldDescriptor, Object> deleteMultipleUser(List<Integer> idList) throws InterruptedException {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final Map<Descriptors.FieldDescriptor, Object> response = new HashMap<>();

        StreamObserver<UserId> responseObserver = userServiceStub.deleteMultipleUser(new StreamObserver<>() {
            @Override
            public void onNext(ResponseMessage responseMessage) {
                response.putAll(responseMessage.getAllFields());
            }

            @Override
            public void onError(Throwable throwable) {
                countDownLatch.countDown();
            }

            @Override
            public void onCompleted() {
                countDownLatch.countDown();
            }
        });

        idList.forEach(idRequest -> responseObserver.onNext(
                UserId.newBuilder()
                        .setUserId(idRequest)
                        .build()
        ));
        responseObserver.onCompleted();

        boolean await = countDownLatch.await(1, TimeUnit.MINUTES);
        return await ? response : Collections.emptyMap();
    }
}
