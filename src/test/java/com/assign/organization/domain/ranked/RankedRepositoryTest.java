package com.assign.organization.domain.ranked;

import com.assign.organization.config.JpaDatabaseConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(JpaDatabaseConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RankedRepositoryTest {

    @Autowired
    private RankedRepository rankedRepository;

    @Test
    public void testRead() {

        // given
        Ranked ranked = Ranked
                .builder()
                .name("test")
                .build();

        rankedRepository.save(ranked);
        rankedRepository.flush();

        // when
        Optional<Ranked> findRanked = rankedRepository.findById(ranked.getId());

        // then
        assertFalse(findRanked.isEmpty());
        assertEquals(ranked, findRanked.get());

    }

    @Test
    public void testInsert() {

        // given
        Ranked ranked = Ranked
                .builder()
                .name("차장")
                .build();

        // when
        rankedRepository.save(ranked);

        // then
        Ranked findRanked = rankedRepository.getOne(ranked.getId());

        assertEquals(ranked, findRanked);

    }

    @Test
    public void testUpdateRankName() {

        String beforeRankedName = "팀장";

        // given
        Ranked ranked = Ranked
                .builder()
                .name(beforeRankedName)
                .build();

        // when
        rankedRepository.save(ranked);

        String afterRankedName = "센터장";

        ranked.updateRankedName(afterRankedName);
        rankedRepository.flush();

        // then
        Ranked findRanked = rankedRepository.getOne(ranked.getId());

        assertEquals(afterRankedName, findRanked.getName());
    }

    @Test
    public void testDeleteById() {

        // given
        Ranked ranked = Ranked
                .builder()
                .name("test")
                .build();

        rankedRepository.save(ranked);

        // when
        rankedRepository.deleteById(ranked.getId());

        // then
        Optional<Ranked> deletedRanekd = rankedRepository.findById(ranked.getId());

        assertTrue(deletedRanekd.isEmpty());

    }

}