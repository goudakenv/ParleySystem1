package com.example.app.service;

import com.example.app.domain.Admin;

public interface AdminService {

	Admin getAdminByLoginId(String logingId) throws Exception;

}
