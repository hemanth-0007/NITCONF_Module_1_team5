package com.nitconfbackend.nitconf.service;

import com.nitconfbackend.nitconf.models.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.nitconfbackend.nitconf.repositories.TagsRepository;

@ExtendWith(MockitoExtension.class)
public class TagServiceTest {

	@Mock
	private TagsRepository tagsrepo;

	@InjectMocks
	private TagService tagService;

	@Test
	void testCreateNewTag() {
		Tag Tag = new Tag("BlockChain");
		// When stubbing the mock, if the method is called with the given parameters, it will return the given value.
		given(tagsrepo.save(Tag)).willReturn(Tag);
		var savedTag = tagService.CreateNewTag(Tag.title);
		// Then
		assertThat(savedTag).isNotNull();
		assertThat(savedTag.getTitle()).isEqualTo("BlockChain");
	}

	@Test
	void testFindSessions() {
		Tag Tag = new Tag("ML");
		// When
		given(tagsrepo.findByTitle("ML")).willReturn(java.util.Optional.of(Tag));
		var relatedSessions = tagService.findSessionByTitle("ML");
		// Then
		assertThat(relatedSessions).isNotNull();
		assertThat(relatedSessions.size()).isEqualTo(0);
	}

	@Test
	void testGetAllTags() {
		Tag Tag = new Tag("AI");
		Tag Tag2 = new Tag("Blockchain");
		// When
		given(tagsrepo.findAll())
				.willReturn(List.of(Tag, Tag2));
		var TagList = tagService.getAllTags();
		// Then
		// Make sure to import assertThat From org.assertj.core.api package
		assertThat(TagList).isNotNull();
		assertThat(TagList.size()).isEqualTo(2);
	}
}
