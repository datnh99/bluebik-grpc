package com.bluebik.service;

import com.bluebik.*;
import com.bluebik.exception.UserNotFoundException;
import com.bluebik.model.BluebikUser;
import com.bluebik.repository.BluebikUserRepository;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@GrpcService
@RequiredArgsConstructor
public class BluebikUserService extends UserServiceGrpc.UserServiceImplBase {

    private final BluebikUserRepository userRepository;

    @Override
    public void getUser(UserId request, StreamObserver<User> responseObserver) {
        var user = userRepository.findBluebikUserById(request.getUserId());

        if (user == null)
            throw new UserNotFoundException(String.format("Not found user with id: %d", request.getUserId()));

        responseObserver.onNext(User.newBuilder()
                .setUserId(user.getId())
                .setAccount(user.getAccount())
                .setName(user.getName())
                .setAge(user.getAge())
                .build());

        responseObserver.onCompleted();
    }

    @Override
    public void createUser(UserAddRequest request, StreamObserver<ResponseMessage> responseObserver) {
        BluebikUser bluebikUser = BluebikUser.builder()
                .account(request.getAccount())
                .name(request.getName())
                .age(request.getAge())
                .build();

        userRepository.save(bluebikUser);

        responseObserver.onNext(ResponseMessage.newBuilder()
                .setCode(200)
                .setMessage(String.format("An user has been created with id: %d", bluebikUser.getId()))
                .build());

        responseObserver.onCompleted();
    }

    @Override
    public void updateUser(User request, StreamObserver<User> responseObserver) {
        var user = userRepository.findBluebikUserById(request.getUserId());

        if (user == null)
            throw new UserNotFoundException(String.format("Not found user with id: %d", request.getUserId()));

        user.setAccount(request.getAccount());
        user.setName(request.getName());
        user.setAge(request.getAge());

        userRepository.save(user);

        responseObserver.onNext(request);

        responseObserver.onCompleted();
    }

    @Override
    public void deleteUser(UserId request, StreamObserver<ResponseMessage> responseObserver) {
        var user = userRepository.findBluebikUserById(request.getUserId());

        if (user == null)
            throw new UserNotFoundException(String.format("Not found user with id: %d", request.getUserId()));

        userRepository.delete(user);

        responseObserver.onNext(ResponseMessage.newBuilder()
                .setCode(200)
                .setMessage("User has been deleted!!!")
                .build());

        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<UserAddRequest> createMultipleUser(StreamObserver<ResponseMessage> responseObserver) {
        return new StreamObserver<>() {
            final List<ResponseMessage> responses = new ArrayList<>();

            @Override
            public void onNext(UserAddRequest request) {
                BluebikUser user = BluebikUser.builder()
                        .account(request.getAccount())
                        .name(request.getName())
                        .age(request.getAge())
                        .build();

                userRepository.save(user);

                responses.add(ResponseMessage.newBuilder()
                        .setCode(200)
                        .setMessage(String.format("User has been created with id: %d",
                                user.getId()))
                        .build());
            }

            @Override
            public void onError(Throwable throwable) {
                responseObserver.onError(throwable);
            }

            @Override
            public void onCompleted() {
                responses.parallelStream().forEach(responseObserver::onNext);
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public void searchUserByUsername(Username request, StreamObserver<User> responseObserver) {
        List<BluebikUser> users = userRepository.findAllByNameStartsWith(request.getName());
        users.forEach(user -> responseObserver.onNext(User.newBuilder()
                .setUserId(user.getId())
                .setAccount(user.getAccount())
                .setName(user.getName())
                .setAge(user.getAge())
                .build()));
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<User> updateMultipleUser(StreamObserver<User> responseObserver) {
        return new StreamObserver<>() {
            final List<User> responses = new ArrayList<>();

            @Override
            public void onNext(User request) {
                var user = userRepository.findBluebikUserById(request.getUserId());

                if (user == null)
                    throw new UserNotFoundException(String.format("Not found user with id: %d", request.getUserId()));

                user.setName(request.getName());
                user.setAge(request.getAge());

                userRepository.save(user);

                responses.add(request);
            }

            @Override
            public void onError(Throwable throwable) {
                responseObserver.onError(throwable);
            }

            @Override
            public void onCompleted() {
                responses.parallelStream().forEach(responseObserver::onNext);
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public StreamObserver<UserId> deleteMultipleUser(StreamObserver<ResponseMessage> responseObserver) {
        StringBuilder ids = new StringBuilder();

        return new StreamObserver<>() {
            @Override
            public void onNext(UserId userIdRequest) {
                var user = userRepository.findBluebikUserById(userIdRequest.getUserId());

                if (user == null)
                    throw new UserNotFoundException(String.format("Not found user with id: %d", userIdRequest.getUserId()));

                userRepository.delete(user);

                ids.append(user.getId() + ", ");
            }

            @Override
            public void onError(Throwable throwable) {
                responseObserver.onError(throwable);
            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(ResponseMessage.newBuilder()
                        .setCode(200)
                        .setMessage(String.format("Users have been deleted with ids: %s", ids.substring(0, ids.length() - 2)))
                        .build());

                responseObserver.onCompleted();
            }
        };
    }
}
