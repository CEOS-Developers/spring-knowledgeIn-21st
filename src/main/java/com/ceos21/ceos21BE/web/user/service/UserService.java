package com.ceos21.ceos21BE.web.user.service;

import com.ceos21.ceos21BE.web.user.dto.UserInfoDto;

public interface UserService {
    public UserInfoDto getUserInto(String email);
    public void deleteUserAccount(String email);

}
