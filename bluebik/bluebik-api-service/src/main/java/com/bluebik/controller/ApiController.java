package com.bluebik.controller;

import com.bluebik.User;
import com.bluebik.UserAddRequest;
import com.bluebik.model.AddUserRequest;
import com.bluebik.model.BluebikUser;
import com.bluebik.service.ApiService;
import com.google.protobuf.Descriptors;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class ApiController {
    private final ApiService apiService;

    @GetMapping("/user/{id}")
    public Map<Descriptors.FieldDescriptor, Object> getUserById(@PathVariable int id) {
        return apiService.getUser(id);
    }

    @PostMapping("/user/add")
    public Map<Descriptors.FieldDescriptor, Object> createUser(@RequestBody AddUserRequest request) {
        return apiService.createUser(request);
    }

    @PostMapping("/user/update")
    public Map<Descriptors.FieldDescriptor, Object> updateUser(@RequestBody BluebikUser user) {
        return apiService.updateUser(user);
    }

    @GetMapping("user/delete/{id}")
    public Map<Descriptors.FieldDescriptor, Object> deleteUser(@PathVariable int id) {
        return apiService.deleteUser(id);
    }

    @PostMapping("/addList")
    public List<Map<Descriptors.FieldDescriptor, Object>> createMultipleUser(@RequestBody List<AddUserRequest> requests) throws InterruptedException {
        return apiService.createMultipleUser(requests);
    }

    @GetMapping("/search/{username}")
    public List<Map<Descriptors.FieldDescriptor, Object>> searchUserByUsername(@PathVariable String username) throws InterruptedException {
        return apiService.searchUserByUsername(username);
    }

    @PostMapping("/updateList")
    public List<Map<Descriptors.FieldDescriptor, Object>> updateMultipleUser(@RequestBody List<BluebikUser> users) throws InterruptedException {
        return apiService.updateMultipleUser(users);
    }

    @GetMapping("/deleteList")
    public Map<Descriptors.FieldDescriptor, Object> deleteMultipleUser(@RequestParam List<Integer> idList) throws InterruptedException {
        return apiService.deleteMultipleUser(idList);
    }
}
