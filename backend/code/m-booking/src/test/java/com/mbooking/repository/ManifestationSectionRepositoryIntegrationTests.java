package com.mbooking.repository;

import lombok.Data;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@Data
@ActiveProfiles("test_h2")
public class ManifestationSectionRepositoryIntegrationTests {
    

    @Test
    public void givenValidId_whenDeletingSection_removeSectionFromDB() {

        Long manifestSectionId = -1L;


    }
}
