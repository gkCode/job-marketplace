package com.org.marketplace.controller;

import java.util.ArrayList;
import java.util.List;

import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.org.marketplace.payload.PagedResponse;
import com.org.marketplace.payload.ProjectResponse;
import com.org.marketplace.service.BidService;
import com.org.marketplace.service.ProjectService;

/**
 * @author gauravkahadane
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value = ProjectController.class, secure = false)
public class ProjectControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProjectService projectService;

	@MockBean
	private BidService bidService;

//	@Test
	public void getProjectById() throws Exception {

		ProjectResponse projectResponse = new ProjectResponse();
		projectResponse.setId(1L);
		projectResponse.setName("Project1");

		Mockito.when(projectService.getProjectById(1L)).thenReturn(projectResponse);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/mkt/projects/1")
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		String expected = "{\"id\":1,\"name\":\"Project1\",\"createdBy\":null,\"creationDateTime\":null,\"expirationDateTime\":null,\"isExpired\":null,\"budget\":null,\"bidExpiry\":null,\"bid\":0.0,\"expired\":null}";

		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}

//	@Test
	public void getProjects() throws Exception {

		PagedResponse<ProjectResponse> pagedResponse = new PagedResponse<ProjectResponse>();
		List<ProjectResponse> projectResponseList = new ArrayList<ProjectResponse>();
		
		for (long i = 1; i < 3; i++) {
			ProjectResponse projectResponse = new ProjectResponse();
			projectResponse.setId(i);
			projectResponse.setName("Project" + i);
			projectResponseList.add(projectResponse);
		}
		pagedResponse.setContent(projectResponseList);

		Mockito.when(projectService.getAllProjects(1, projectResponseList.size())).thenReturn(pagedResponse);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/mkt/projects")
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		String expected = "{\"id\":1,\"name\":\"Project1\",\"createdBy\":null,\"creationDateTime\":null,\"expirationDateTime\":null,\"isExpired\":null,\"budget\":null,\"bidExpiry\":null,\"bid\":0.0,\"expired\":null}";

		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}

}
