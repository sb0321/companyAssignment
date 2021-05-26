package com.assign.organization.service.duplication;

import com.assign.organization.domain.duplication.Duplication;
import com.assign.organization.domain.duplication.repository.DuplicationRepository;
import com.assign.organization.exception.NullDuplicationNameException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DuplicationService {

    private final DuplicationRepository duplicationRepository;

    @Transactional
    public void makeDuplicationAndIncreaseCount(String objectName) {

        if (objectName == null) {
            throw new NullDuplicationNameException();
        }

        Optional<Duplication> findDuplication = duplicationRepository.findById(objectName);

        if (findDuplication.isPresent()) {
            findDuplication.get().increaseCount();
            return;
        }

        duplicationRepository.save(new Duplication(objectName));
    }
}
