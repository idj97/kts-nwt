package com.mbooking.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.mbooking.model.Section;
import com.mbooking.repository.SectionRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test_h2")
public class SectionServiceTest {

	@Autowired
	private SectionService sectionS;
	@MockBean
	private SectionRepository sectionRepoMock;

	@Test
	public void testFindById() {
		Section s = new Section();
		s.setId(1L);
		s.setName("ime");
		s.setSectionColumns(1);
		s.setSectionRows(2);
		Mockito.when(sectionRepoMock.findById(1L)).thenReturn(Optional.of(s));
		Optional<Section> rez = sectionS.findById(1L);
		Mockito.verify(sectionRepoMock).findById(1L);
		assertEquals(s.getName(), rez.get().getName());

	}
//exception
	@Test
	public void testFindById1() {

		Mockito.when(sectionRepoMock.findById(1L)).thenReturn(Optional.empty());
		sectionS.findById(1L);
	}

}
