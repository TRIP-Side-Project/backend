package com.api.trip.domain.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {
}
