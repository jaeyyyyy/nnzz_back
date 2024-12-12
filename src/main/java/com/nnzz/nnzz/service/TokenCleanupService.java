package com.nnzz.nnzz.service;

import com.nnzz.nnzz.repository.BlacklistTokenMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TokenCleanupService {
    private final BlacklistTokenMapper blacklistTokenMapper;

    @Scheduled(fixedRate = 3600000) // 단위 : ms, 1시간마다 실행
    @Transactional
    @Profile("!test") // 'test' 프로파일이 아닐 때만 실행
    public void cleanupExpiredTokens() {
        blacklistTokenMapper.deleteExpiredTokens();
    }
}
