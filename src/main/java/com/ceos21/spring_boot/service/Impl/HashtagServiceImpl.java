package com.ceos21.spring_boot.service.Impl;

import com.ceos21.spring_boot.service.HashtagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HashtagServiceImpl implements HashtagService {
}
